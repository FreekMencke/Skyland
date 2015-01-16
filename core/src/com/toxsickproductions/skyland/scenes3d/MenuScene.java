package com.toxsickproductions.skyland.scenes3d;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.graphics.g3d.Environment;
import com.badlogic.gdx.graphics.g3d.ModelBatch;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.bullet.Bullet;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.Align;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.toxsickproductions.data.Assets;
import com.toxsickproductions.g3d.bullet.BulletEntity;
import com.toxsickproductions.g3d.bullet.BulletWorld;
import com.toxsickproductions.skyland.Skyland;
import com.toxsickproductions.skyland.listeners.RayCastListener;
import com.toxsickproductions.skyland.scenes3d.generators.CloudGenerator;
import com.toxsickproductions.skyland.scenes3d.generators.WorldGenerator;
import com.toxsickproductions.skyland.scenes3d.util.ParticleUtils;
import com.toxsickproductions.skyland.scenes3d.util.WorldHover;

/**
 * Created by Freek on 6/01/2015.
 */
public class MenuScene implements Screen {

    private PerspectiveCamera camera;
    private ModelBatch batch;
    private ParticleUtils particleUtils;
    private RayCastListener rayCastListener;

    private Environment environment;
    private BulletWorld world;
    private CloudGenerator cloudGenerator;
    private BulletEntity island;

    @Override
    public void show() {
        Bullet.init();
        init3d();
        initScene2d();
        initRayCastListener();
    }

    private void initRayCastListener() {
        Gdx.input.setInputProcessor(new InputMultiplexer(Skyland.STAGE, rayCastListener = new RayCastListener(world, camera)));
        rayCastListener.initParticleUtils(particleUtils);
        rayCastListener.initCloudPoofer(cloudGenerator);
    }

    private void initScene2d() {
        Table table = new Table();
        table.setSize(Gdx.graphics.getWidth() / 3, 3 * Gdx.graphics.getHeight() / 4);
        table.setPosition(Gdx.graphics.getWidth() / 4, Gdx.graphics.getHeight() / 2, Align.center);
        Skyland.STAGE.addActor(table);

        TextButton start = new TextButton("Start", Assets.menuSkin),
                rate = new TextButton("Rate", Assets.menuSkin),
                exit = new TextButton("Exit", Assets.menuSkin);

        start.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Skyland.getInstance().setScreen(new GameScene());
            }
        });
        exit.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                System.exit(0);
            }
        });

        table.add(new Label("Skyland", Assets.menuSkin, "64")).expand().top().row();
        table.add(start).size(Gdx.graphics.getWidth() / 6, Gdx.graphics.getHeight() / 10).padBottom(Gdx.graphics.getHeight() / 30).row();
        table.add(rate).size(Gdx.graphics.getWidth() / 6, Gdx.graphics.getHeight() / 10).padBottom(Gdx.graphics.getHeight() / 30).row();
        table.add(exit).size(Gdx.graphics.getWidth() / 6, Gdx.graphics.getHeight() / 10).padBottom(Gdx.graphics.getHeight() / 30).row();
        table.add().expand();
    }

    private void init3d() {
        camera = WorldGenerator.generatePerspectiveCamera(1, 150, new Vector3(-8, 6, 15), new Vector3(0, -2, 0));
        batch = new ModelBatch();

        particleUtils = new ParticleUtils();
        particleUtils.initBillBoardParticles(camera);

        world = WorldGenerator.generateBaseWorld(false, false);
        cloudGenerator = new CloudGenerator(world, 40);
        environment = WorldGenerator.generateBaseEnvironment(new Vector3(8, 14, 6));
        island = WorldGenerator.createKinematicIsland(world, new Vector3(8, 1, 0), false);
        WorldHover.reinit();
    }

    @Override
    public void render(float delta) {
        update(delta);
        Gdx.gl.glViewport(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        Gdx.gl.glClearColor(.3f, .55f, 1, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);

        batch.begin(camera);
        world.renderFrustumCulling(batch, environment, 1);
        batch.render(particleUtils.updateAndDraw(), environment);
        batch.end();

        Skyland.getInstance().renderStage();
        Skyland.drawFPS();
    }

    private void update(float delta) {
        cloudGenerator.update(delta);
        island.transform.rotate(0, 1, 0, delta * 2);
        WorldHover.hover(delta, world);
        world.update();
    }

    @Override
    public void dispose() {
        cloudGenerator.dispose();
        particleUtils.dispose();
        batch.dispose();
        world.dispose();
    }

    @Override
    public void resize(int width, int height) {
        Skyland.STAGE.getViewport().update(width, height, true);
    }

    @Override
    public void hide() {
        dispose();
    }

    @Override
    public void pause() {
    }

    @Override
    public void resume() {
    }

}
