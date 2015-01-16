package com.toxsickproductions.skyland.scenes3d;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.graphics.g3d.Environment;
import com.badlogic.gdx.graphics.g3d.ModelBatch;
import com.badlogic.gdx.math.Vector3;
import com.toxsickproductions.g3d.bullet.BulletWorld;
import com.toxsickproductions.skyland.Skyland;
import com.toxsickproductions.skyland.listeners.CameraController;
import com.toxsickproductions.skyland.listeners.RayCastListener;
import com.toxsickproductions.skyland.scenes2d.Resources;
import com.toxsickproductions.skyland.scenes3d.generators.CloudGenerator;
import com.toxsickproductions.skyland.scenes3d.generators.WorldGenerator;
import com.toxsickproductions.skyland.scenes3d.util.ParticleUtils;
import com.toxsickproductions.skyland.scenes3d.util.WorldHover;

/**
 * Created by Freek on 7/01/2015.
 */
public class GameScene extends InputAdapter implements Screen {

    private BulletWorld world;
    private Environment environment;
    private CloudGenerator cloudGenerator;

    private ParticleUtils particleUtils;
    private RayCastListener rayCastListener;
    private PerspectiveCamera camera;
    private ModelBatch batch;

    private Resources resources;

    @Override
    public void show() {
        initWorld();
        Gdx.input.setInputProcessor(new InputMultiplexer(rayCastListener = new RayCastListener(world, camera), new CameraController(camera, -20, 7, -1.5f, 2)));
        initRayCasting();
    }

    private void initWorld() {
        camera = WorldGenerator.generatePerspectiveCamera(1, 150, new Vector3(-8, 10, 15), new Vector3(0, 2, 0));
        batch = new ModelBatch();

        particleUtils = new ParticleUtils();
        particleUtils.initBillBoardParticles(camera);

        //adding generators
        world = WorldGenerator.generateBaseWorld(false, false);
        cloudGenerator = new CloudGenerator(world, 30);
        environment = WorldGenerator.generateBaseEnvironment(new Vector3(-6, 14, 6));
        WorldGenerator.createKinematicIsland(world, new Vector3(0, 0, 0), true);

        WorldHover.reinit();
    }

    private void initRayCasting() {
        rayCastListener.initResources(resources = new Resources());
        rayCastListener.initParticleUtils(particleUtils);
        rayCastListener.initCloudPoofer(cloudGenerator);
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

        Skyland.drawFPS();
        resources.drawResources();
    }

    private void update(float delta) {
        WorldHover.hover(delta, world);
        cloudGenerator.update(delta);
        world.update();
    }

    @Override
    public void dispose() {
        particleUtils.dispose();
        cloudGenerator.dispose();
        batch.dispose();
        world.dispose();
    }

    @Override
    public void resize(int width, int height) {
        Skyland.STAGE.getViewport().update(Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), true);
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
