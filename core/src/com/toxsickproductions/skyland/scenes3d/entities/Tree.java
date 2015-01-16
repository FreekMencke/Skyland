package com.toxsickproductions.skyland.scenes3d.entities;

import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.physics.bullet.collision.btCollisionObject;
import com.toxsickproductions.g3d.bullet.BulletEntity;
import com.toxsickproductions.g3d.bullet.BulletWorld;
import com.toxsickproductions.skyland.scenes3d.util.BulletUserData;

/**
 * Created by Freek on 11/01/2015.
 */
public class Tree {

    public BulletEntity stump, tree;

    private float life = 60, age = 0;

    public Tree(BulletEntity stump, BulletEntity tree) {
        this.stump = stump;
        this.tree = tree;
    }

    public void update(BulletWorld world, float delta) {
        if (tree == null) {
            if ((age += delta) > life) {
                growTree(world);
                age = 0;
            }
        } else if (tree.body == null) {
            tree.dispose();
            tree = null;
        }
    }

    private void growTree(BulletWorld world) {
        Matrix4 transform = stump.transform.cpy();
        transform.translate(0, 1.15f, 0);
        BulletEntity staticTree = world.add("staticTree", transform);
        staticTree.body.setCollisionFlags(staticTree.body.getCollisionFlags()
                | btCollisionObject.CollisionFlags.CF_KINEMATIC_OBJECT);
        staticTree.body.setActivationState(com.badlogic.gdx.physics.bullet.collision.Collision.DISABLE_DEACTIVATION);
        staticTree.radius = 1.5f;
        staticTree.body.userData = new BulletUserData("staticTree", staticTree);
        this.tree = staticTree;
    }

    public void dispose() {
        stump.dispose();
        tree.dispose();
    }

}
