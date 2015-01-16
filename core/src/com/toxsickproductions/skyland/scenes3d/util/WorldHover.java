package com.toxsickproductions.skyland.scenes3d.util;

import com.badlogic.gdx.math.Vector3;
import com.toxsickproductions.g3d.bullet.BulletEntity;
import com.toxsickproductions.g3d.bullet.BulletWorld;

/**
 * Created by Freek on 8/01/2015.
 */
public class WorldHover {

    private static boolean up = true;
    private static float timer = 0;
    private static Vector3 position = new Vector3();

    public static void reinit() {
        up = true;
        timer = 0;
        position = new Vector3();
    }

    public static void hover(float delta, BulletWorld world) {
        timer += up ? delta / 2 : -delta / 2;
        if (timer > 1 || timer < -1)
            up = !up;

        for (BulletEntity e : world.entities) {
            e.transform.getTranslation(position);
            position.y += (up ? 1 : -1) * ((1 - (timer * timer)) / 250);
            e.transform.setTranslation(position);
            if (e.body != null && e.body.isKinematicObject()) {
                e.body.setWorldTransform(e.transform);
                e.body.activate();
            }
        }
    }

}
