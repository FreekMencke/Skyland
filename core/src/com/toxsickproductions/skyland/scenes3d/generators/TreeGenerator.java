package com.toxsickproductions.skyland.scenes3d.generators;

import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Quaternion;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.bullet.collision.*;
import com.badlogic.gdx.physics.bullet.dynamics.btRigidBody;
import com.toxsickproductions.data.Assets;
import com.toxsickproductions.g3d.bullet.BulletConstructor;
import com.toxsickproductions.g3d.bullet.BulletEntity;
import com.toxsickproductions.g3d.bullet.BulletWorld;
import com.toxsickproductions.skyland.scenes3d.entities.Tree;
import com.toxsickproductions.skyland.scenes3d.util.BulletUserData;
import reference.Models;

/**
 * Created by Freek on 8/01/2015.
 */
public class TreeGenerator {

    public static void initWorld(BulletWorld world) {
        //TreeShape
        Model model = Assets.get(Models.MODEL_TREE_PROTOTYPE, Model.class);
        model.nodes.first().translation.set(0, -1.15f, 0);
        btCompoundShape treeShape = new btCompoundShape();
        treeShape.addChildShape(new Matrix4(new Vector3(0, 0, 0), new Quaternion(), new Vector3(1, 1, 1)), new btBoxShape(new Vector3(.2f, .9f, .2f)));
        treeShape.addChildShape(new Matrix4(new Vector3(0, 1, 0), new Quaternion(), new Vector3(1, 1, 1)), new btSphereShape(1));
        //LogShape
        model = Assets.get(Models.MODEL_LOG_PROTOTYPE, Model.class);
        model.nodes.first().translation.set(0, -1.15f, 0);

        world.addConstructor("log", new BulletConstructor(Assets.get(Models.MODEL_LOG_PROTOTYPE, Model.class), 75, new btBoxShape(new Vector3(.2f, .9f, .2f))));
        world.addConstructor("stump", new BulletConstructor(Assets.get(Models.MODEL_STUMP_PROTOTYPE, Model.class), 0, new btCylinderShape(new Vector3(.2f, .22f, .2f))));
        world.addConstructor("staticTree", new BulletConstructor(Assets.get(Models.MODEL_TREE_PROTOTYPE, Model.class), 0, treeShape));
        world.addConstructor("dynamicTree", new BulletConstructor(Assets.get(Models.MODEL_TREE_PROTOTYPE, Model.class), 100, treeShape));
    }

    public static Tree spawnStaticTree(BulletWorld world, Vector3 position) {
        BulletEntity stump = world.add("stump", position.x, position.y, position.z);
        stump.body.setCollisionFlags(stump.body.getCollisionFlags()
                | btCollisionObject.CollisionFlags.CF_KINEMATIC_OBJECT);
        stump.body.setActivationState(Collision.DISABLE_DEACTIVATION);
        BulletEntity staticTree = world.add("staticTree", position.x, position.y + 1.15f, position.z);
        staticTree.body.setCollisionFlags(staticTree.body.getCollisionFlags()
                | btCollisionObject.CollisionFlags.CF_KINEMATIC_OBJECT);
        staticTree.body.setActivationState(Collision.DISABLE_DEACTIVATION);

        //radius
        stump.radius = .5f;
        staticTree.radius = 1.5f;

        //userdata
        stump.body.userData = new BulletUserData("stump", stump);
        staticTree.body.userData = new BulletUserData("staticTree", staticTree);

        //random rotation
        int random = MathUtils.random(0, 360);
        stump.transform.rotate(0, 1, 0, random);
        staticTree.transform.rotate(0, 1, 0, random);

        return new Tree(stump, staticTree);
    }


    public static void spawnDynamicTree(BulletWorld world, Matrix4 worldTransform) {
        BulletEntity dynamicTree = world.add("dynamicTree", 0, 0, 0);
        dynamicTree.radius = 1.5f;
        dynamicTree.body.userData = new BulletUserData("dynamicTree", dynamicTree);
        dynamicTree.body.setWorldTransform(worldTransform);
        ((btRigidBody) dynamicTree.body).applyImpulse(new Vector3(MathUtils.random(50, 60) * (MathUtils.random(0, 1) > 0 ? 1 : -1), 0, MathUtils.random(50, 60) * (MathUtils.random(0, 1) > 0 ? 1 : -1)), new Vector3(0, 1, 0));
    }

    public static BulletEntity spawnLog(BulletWorld world, Matrix4 worldTransform) {
        BulletEntity log = world.add("log", 0, 0, 0);
        log.radius = 1;
        log.body.userData = new BulletUserData("log", log);
        log.body.setWorldTransform(worldTransform);
        ((btRigidBody) log.body).applyImpulse(new Vector3(MathUtils.random(50, 60) * (MathUtils.random(0, 1) > 0 ? 1 : -1), 0, MathUtils.random(50, 60) * (MathUtils.random(0, 1) > 0 ? 1 : -1)), new Vector3(0, 1, 0));
        return log;
    }

}
