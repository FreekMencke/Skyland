package com.toxsickproductions.skyland.scenes3d.generators;

import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.bullet.collision.Collision;
import com.badlogic.gdx.physics.bullet.collision.btCapsuleShapeZ;
import com.badlogic.gdx.physics.bullet.collision.btCollisionObject;
import com.badlogic.gdx.utils.Array;
import com.toxsickproductions.data.Assets;
import com.toxsickproductions.g3d.bullet.BulletConstructor;
import com.toxsickproductions.g3d.bullet.BulletEntity;
import com.toxsickproductions.g3d.bullet.BulletWorld;
import com.toxsickproductions.skyland.scenes3d.util.BulletUserData;
import reference.Models;

/**
 * Created by Freek on 6/01/2015.
 */
public class CloudGenerator {

    public Array<BulletEntity> clouds = new Array();
    private float timer = 0;
    private BulletWorld world;
    private BulletEntity cloud;
    private int density;

    public CloudGenerator(BulletWorld world, int density) {
        this.world = world;
        this.density = density;
        init();

        createCloud();
        for (int i = 0; i < 200; i++) {
            update(6);
        }
    }

    private void init() {
        Model cloud = Assets.get(Models.MODEL_CLOUD, Model.class);
        cloud.nodes.first().scale.set(2, 2, 2);
        cloud.nodes.first().translation.set(0, -2.5f, 0);
        world.addConstructor("cloud", new BulletConstructor(cloud, 0, new btCapsuleShapeZ(4, 7)));
    }

    public void update(float delta) {
        timer += delta;
        Vector3 position = new Vector3();
        for (BulletEntity e : clouds) {
            if (e.transform.getTranslation(position).x > 120) {
                e.transform.setTranslation(-120, position.y, position.z);
            } else e.transform.setTranslation(position.add(delta * 1.5f, 0, 0));
        }
        if (clouds.size < density && timer > 10) {
            timer = 0;
            createCloud();
        }
    }

    private void createCloud() {
        float y, z;
        do {
            y = MathUtils.random(-40, 30);
            z = MathUtils.random(-80, 80);
        } while (y < 15 && y > -20 && z < 15 && z > -15);
        clouds.add(cloud = world.add("cloud", -120, y, z));

        cloud.radius = 7;
        cloud.body.userData = new BulletUserData("cloud", cloud);
        cloud.transform.rotate(0, 1, 0, 90);
        cloud.body.setCollisionFlags(cloud.body.getCollisionFlags()
                | btCollisionObject.CollisionFlags.CF_KINEMATIC_OBJECT);
        cloud.body.setActivationState(Collision.DISABLE_DEACTIVATION);
    }

    public void dispose() {
        world.dispose();
        cloud.dispose();
        for (BulletEntity e : clouds)
            e.dispose();
        clouds.clear();
    }

}
