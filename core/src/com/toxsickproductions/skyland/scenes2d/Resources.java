package com.toxsickproductions.skyland.scenes2d;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.utils.Align;
import com.toxsickproductions.data.Assets;
import com.toxsickproductions.skyland.Skyland;
import reference.Textures;

/**
 * Created by Freek on 9/01/2015.
 */
public class Resources {

    public int wood, stone;
    public Label woodLabel, stoneLabel;

    public Resources() {
        wood = 0;
        stone = 0;
        initScene2d();
    }

    private void initScene2d() {
        woodLabel = new Label("" + wood, Assets.menuSkin, "black");
        stoneLabel = new Label("" + stone, Assets.menuSkin, "black");
    }

    public void lootLog() {
        wood++;
    }

    public void lootStone() {
        stone++;
    }

    private void update() {
        woodLabel.setText("" + wood);
        woodLabel.setPosition(Gdx.graphics.getWidth() / 2 - 50 - woodLabel.getText().length() * Gdx.graphics.getHeight() / 40, Gdx.graphics.getHeight() - 60, Align.right);
        stoneLabel.setText("" + stone);
        stoneLabel.setPosition(Gdx.graphics.getWidth() / 2 + 250 - stoneLabel.getText().length() * Gdx.graphics.getHeight() / 40, Gdx.graphics.getHeight() - 60, Align.right);
    }

    public void drawResources() {
        update();
        Skyland.BATCH.begin();
        Skyland.BATCH.draw(Assets.get(Textures.TEXTURE_UI_WOOD, Texture.class), Gdx.graphics.getWidth() / 2 - 250, Gdx.graphics.getHeight() - 110);
        Skyland.BATCH.draw(Assets.get(Textures.TEXTURE_UI_STONE, Texture.class), Gdx.graphics.getWidth() / 2 + 50, Gdx.graphics.getHeight() - 110);
        woodLabel.draw(Skyland.BATCH, 1);
        stoneLabel.draw(Skyland.BATCH, 1);
        Skyland.BATCH.end();
    }

}
