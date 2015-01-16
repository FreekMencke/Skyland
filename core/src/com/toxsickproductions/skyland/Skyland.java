package com.toxsickproductions.skyland;


import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.toxsickproductions.data.Assets;
import com.toxsickproductions.interfaces.ToxSickGame;
import com.toxsickproductions.skyland.scenes2d.Splash;

public class Skyland extends Game implements ToxSickGame {

    private static Skyland instance;

    public static Skyland getInstance() {
        return instance;
    }

    public static void drawFPS() {
        Skyland.STAGE.getBatch().begin();
        Assets.menuSkin.get("console", BitmapFont.class).draw(Skyland.STAGE.getBatch(), "FPS: " + Gdx.graphics.getFramesPerSecond(), 5, Gdx.graphics.getHeight() - 5);
        Skyland.STAGE.getBatch().end();
    }

    @Override
    public void create() {
        instance = this;
        setScreen(new Splash());
    }

    @Override
    public void render() {
        super.render();
        TASKMANAGER.update(Gdx.graphics.getDeltaTime());
    }

    public void renderStage() {
        STAGE.act();
        STAGE.draw();
    }

}