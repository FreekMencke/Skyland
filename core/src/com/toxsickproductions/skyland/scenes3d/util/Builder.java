package com.toxsickproductions.skyland.scenes3d.util;

import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.model.Node;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.bullet.Bullet;
import com.badlogic.gdx.physics.bullet.collision.Collision;
import com.badlogic.gdx.physics.bullet.collision.btCollisionObject;
import com.badlogic.gdx.physics.bullet.collision.btCollisionShape;
import com.badlogic.gdx.physics.bullet.collision.btSphereShape;
import com.badlogic.gdx.physics.bullet.dynamics.btDynamicsWorld;
import com.badlogic.gdx.physics.bullet.dynamics.btRigidBody;
import com.toxsickproductions.data.Assets;
import com.toxsickproductions.g3d.bullet.BulletConstructor;
import com.toxsickproductions.g3d.bullet.BulletEntity;
import com.toxsickproductions.g3d.bullet.BulletWorld;
import com.toxsickproductions.skyland.scenes3d.generators.TreeGenerator;
import reference.Models;

/**
 * Created by Freek on 9/01/2015.
 */
public class Builder {

    private static String CURR_MODEL;
    private static BulletWorld WORLD;

    public static void init(BulletWorld world) {
        Builder.WORLD = world;
        TreeGenerator.initWorld(world);
        initStone(world);
    }

    public static void setBuildModel(String model) {
        Builder.CURR_MODEL = model;
    }

    public static Object build(Vector3 translation) {
        if (CURR_MODEL.equals(Models.MODEL_TREE_PROTOTYPE)) {
            return TreeGenerator.spawnStaticTree(Builder.WORLD, translation);
        }
        if (CURR_MODEL.equals(Models.MODEL_STONE_PROTOTYPE)) {
            return spawnRock(translation);
        }
        return null;
    }

    public static void build(Matrix4 transform) {
        if (CURR_MODEL.equals(Models.MODEL_TREE_PROTOTYPE)) {
            TreeGenerator.spawnDynamicTree(Builder.WORLD, transform);
        } else if (CURR_MODEL.equals(Models.MODEL_LOG_PROTOTYPE)) {
            TreeGenerator.spawnLog(Builder.WORLD, transform);
        } else if (CURR_MODEL.equals(Models.MODEL_CAVE_PROTOTYPE)) {
            buildCave(transform);
        }
    }

    public static void destroy(BulletUserData data) {
        Builder.WORLD.entities.removeValue((BulletEntity) data.reference, false);
        if (((BulletEntity) data.reference).body instanceof btRigidBody)
            ((btDynamicsWorld) Builder.WORLD.collisionWorld).removeRigidBody((btRigidBody) ((BulletEntity) data.reference).body);
        else Builder.WORLD.collisionWorld.removeCollisionObject(((BulletEntity) data.reference).body);
        ((BulletEntity) data.reference).dispose();
    }

    private static void buildCave(Matrix4 transform) {
        Model caveModel = Assets.get(CURR_MODEL, Model.class);
        if (WORLD.getConstructor("cave") == null) {
            for (Node n : caveModel.nodes) n.scale.set(.6f, .6f, .6f);
            btCollisionShape collisionShape = Bullet.obtainStaticNodeShape(caveModel.nodes);
            collisionShape.setLocalScaling(new Vector3(.6f, .6f, .6f));
            WORLD.addConstructor("cave", new BulletConstructor(caveModel, 0, collisionShape));
        }
        BulletEntity cave = WORLD.add("cave", transform);
        cave.body.setCollisionFlags(cave.body.getCollisionFlags()
                | btCollisionObject.CollisionFlags.CF_KINEMATIC_OBJECT);
        cave.body.setActivationState(Collision.DISABLE_DEACTIVATION);
        cave.body.userData = new BulletUserData("cave", cave);
    }

    private static BulletEntity spawnRock(Vector3 translation) {
        BulletEntity stone = WORLD.add("stone", translation.x + .5f, translation.y, translation.z + .5f);
        stone.radius = 1.5f;
        stone.body.userData = new BulletUserData("stone", stone);
        ((btRigidBody) stone.body).applyCentralImpulse(new Vector3(MathUtils.random(100, 200), 0, MathUtils.random(100, 200)));

        return null;
    }

    private static void initStone(BulletWorld world) {
        Model stoneModel = Assets.get(Models.MODEL_STONE_PROTOTYPE, Model.class);
        stoneModel.nodes.first().scale.set(.6f, .6f, .6f);
        world.addConstructor("stone", new BulletConstructor(stoneModel, 100, new btSphereShape(.6f)));
    }
}
