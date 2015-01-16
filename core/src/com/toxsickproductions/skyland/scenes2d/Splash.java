package com.toxsickproductions.skyland.scenes2d;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.utils.Align;
import com.toxsickproductions.data.Assets;
import com.toxsickproductions.interfaces.AssetHelper;
import com.toxsickproductions.skyland.Skyland;
import com.toxsickproductions.skyland.scenes3d.MenuScene;
import reference.Models;
import reference.Textures;


public class Splash implements AssetHelper, Screen {

    private Image splashImage;
    private boolean loaded = false, animationFinished = false;

    @Override
    public void show() {
        queueAssets();

        splashImage = new Image(new Texture(Gdx.files.internal(Textures.TEXTURE_TOXSICK_LOGO)));
        splashImage.setPosition(Gdx.graphics.getWidth() / 2, Gdx.graphics.getHeight() / 2, Align.center);
        Skyland.STAGE.addActor(splashImage);

        addSplashAnimation();
    }

    @Override
    public void render(float v) {
        Gdx.gl.glViewport(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);

        Skyland.getInstance().renderStage();

        if (Gdx.input.isKeyPressed(Input.Keys.BACK))
            System.exit(0);

        if (!loaded && Assets.update())
            loaded = true;

        if (loaded && animationFinished) {
            Assets.addSkinRegions("skins/skin.pack");
            addFonts();
            Assets.menuSkin.load(Gdx.files.internal("skins/skin.json"));
            Skyland.getInstance().setScreen(new MenuScene());
        }
    }

    @Override
    public void resize(int i, int i2) {
        Skyland.STAGE.getViewport().update(Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), true);
    }

    @Override
    public void dispose() {
        Skyland.STAGE.clear();
    }

    private void addSplashAnimation() {
        splashImage.addAction(Actions.sequence(Actions.alpha(0), Actions.delay(.25f), Actions.fadeIn(1), Actions.run(new Runnable() {
            @Override
            public void run() {
                animationFinished = true;
            }
        })));
    }

    @Override
    public void queueAssets() {
        Assets.queueFile("skins/skin.pack", TextureAtlas.class);

        for (String s : Models.MODELS)
            Assets.queueFile(s, Model.class);
        for (String s : Textures.TEXTURES)
            Assets.queueFile(s, Texture.class);

    }

    private void addFonts() {
        FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("fonts/forcedsquare.ttf"));
        FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        parameter.size = Gdx.graphics.getHeight() / 15;
        Assets.menuSkin.add("font32", generator.generateFont(parameter));
        parameter.size = Gdx.graphics.getHeight() / 10;
        Assets.menuSkin.add("font64", generator.generateFont(parameter));
        parameter.size = Gdx.graphics.getHeight() / 25;
        Assets.menuSkin.add("console", generator.generateFont(parameter));
        generator.dispose();
    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {
        dispose();
    }
}
