package com.toxsickproductions.skyland.listeners;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.g3d.utils.CameraInputController;

/**
 * Created by Freek on 7/01/2015.
 */
public class CameraController extends CameraInputController {

    float curZoom = 0, minZoom, maxZoom, curRotationY = 0, minRotationY, maxRotationY;

    public CameraController(Camera camera, float minZoom, float maxZoom, float minRotationY, float maxRotationY) {
        super(camera);
        this.minZoom = minZoom;
        this.maxZoom = maxZoom;
        this.minRotationY = minRotationY / 10;
        this.maxRotationY = maxRotationY / 10;
        this.pinchZoomFactor *= 2;
    }

    @Override
    public boolean zoom(float amount) {
        if (amount < 0 && curZoom > minZoom) {
            curZoom += amount;
            return super.zoom(amount);
        } else if (amount > 0 && curZoom < maxZoom) {
            curZoom += amount;
            return super.zoom(amount);
        }
        return false;
    }

    @Override
    protected boolean process(float deltaX, float deltaY, int button) {
        if (button == rotateButton && deltaY > 0 && curRotationY < maxRotationY) {
            curRotationY += deltaY;
        } else if (button == rotateButton && deltaY < 0 && curRotationY > minRotationY) {
            curRotationY += deltaY;
        } else deltaY = 0;

        return super.process(deltaX, deltaY, button);
    }
}
