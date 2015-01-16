package com.toxsickproductions.skyland.scenes3d.generators;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.graphics.g3d.Environment;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.graphics.g3d.environment.DirectionalLight;
import com.badlogic.gdx.graphics.g3d.environment.PointLight;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.bullet.Bullet;
import com.badlogic.gdx.physics.bullet.collision.Collision;
import com.badlogic.gdx.physics.bullet.collision.btCollisionObject;
import com.badlogic.gdx.physics.bullet.collision.btCollisionShape;
import com.badlogic.gdx.physics.bullet.linearmath.btIDebugDraw;
import com.badlogic.gdx.utils.Array;
import com.toxsickproductions.data.Assets;
import com.toxsickproductions.g3d.ModelGenerator;
import com.toxsickproductions.g3d.bullet.BulletConstructor;
import com.toxsickproductions.g3d.bullet.BulletEntity;
import com.toxsickproductions.g3d.bullet.BulletWorld;
import com.toxsickproductions.interfaces.Task;
import com.toxsickproductions.skyland.Skyland;
import com.toxsickproductions.skyland.scenes3d.entities.Tree;
import com.toxsickproductions.skyland.scenes3d.util.Builder;
import com.toxsickproductions.skyland.scenes3d.util.BulletUserData;
import reference.Models;

/**
 * Created by Freek on 7/01/2015.
 */
public class WorldGenerator {

    private static void plantTrees(BulletWorld world, Vector3 island) {
        Builder.setBuildModel(Models.MODEL_TREE_PROTOTYPE);
        Array<Tree> trees = new Array<Tree>();
        trees.add((Tree) Builder.build(new Vector3(-3.9f, -0.25f, -7.1f).add(island)));
        trees.add((Tree) Builder.build(new Vector3(-1.1f, -0.35f, -4.4f).add(island)));
        trees.add((Tree) Builder.build(new Vector3(-1.85f, -0.45f, -8.16f).add(island)));
        trees.add((Tree) Builder.build(new Vector3(-1.94f, -0.4f, -6.12f).add(island)));
        trees.add((Tree) Builder.build(new Vector3(0.45f, -0.48f, -7.3f).add(island)));
        trees.add((Tree) Builder.build(new Vector3(1, -0.39f, -5.19f).add(island)));
        trees.add((Tree) Builder.build(new Vector3(2.53f, -0.41f, -6.08f).add(island)));
        trees.add((Tree) Builder.build(new Vector3(2.79f, -0.3f, -3.93f).add(island)));
        trees.add((Tree) Builder.build(new Vector3(4.67f, -0.29f, -4.3f).add(island)));
        trees.add((Tree) Builder.build(new Vector3(4.5f, -.25f, -2.5f).add(island)));
        trees.add((Tree) Builder.build(new Vector3(6.1f, -0.2f, -2.63f).add(island)));
        trees.add((Tree) Builder.build(new Vector3(7.8f, -.1f, -1.9f).add(island)));
        trees.add((Tree) Builder.build(new Vector3(6, -.15f, -1f).add(island)));
        trees.add((Tree) Builder.build(new Vector3(6, -0.07f, 1.3f).add(island)));
        trees.add((Tree) Builder.build(new Vector3(8, -0.06f, 0).add(island)));
        trees.add((Tree) Builder.build(new Vector3(7.5f, 0.08f, 2.4f).add(island)));
        trees.add((Tree) Builder.build(new Vector3(9.2f, 0.10f, 1.3f).add(island)));
        // add regrow task
        Skyland.TASKMANAGER.every100MiliSecond.add(new TreeCultivator(world, trees));
    }

    private static void placeCave(Vector3 island) {
        Matrix4 transform = new Matrix4();
        transform.setToTranslation(new Vector3(-5.4f, -.45f, -2.9f).add(island));
        transform.rotate(new Vector3(0, 1, 0), -35);
        Builder.setBuildModel(Models.MODEL_CAVE_PROTOTYPE);
        Builder.build(transform);
    }

    public static BulletEntity createKinematicIsland(BulletWorld world, Vector3 position, boolean landscape) {
        BulletEntity island = world.add("island", position.x, position.y, position.z);
        island.radius = 5; // not really the radius, but this should be always in the frustum
        island.body.userData = new BulletUserData("island", island);
        island.body.setCollisionFlags(island.body.getCollisionFlags()
                | btCollisionObject.CollisionFlags.CF_KINEMATIC_OBJECT);
        island.body.setActivationState(Collision.DISABLE_DEACTIVATION);
        if (landscape) {
            Vector3 islePosition = island.transform.getTranslation(new Vector3());
            placeCave(islePosition);
            plantTrees(world, islePosition);
        }
        return island;
    }

    public static BulletWorld generateBaseWorld(boolean grid, boolean debug) {
        BulletWorld world = new BulletWorld(new Vector3(0, -9.81f, 0));
        Builder.init(world); //Sets the stuff so you can use Builder class

        if (debug)
            world.setDebugMode(btIDebugDraw.DebugDrawModes.DBG_DrawWireframe | btIDebugDraw.DebugDrawModes.DBG_DrawFeaturesText | btIDebugDraw.DebugDrawModes.DBG_DrawText | btIDebugDraw.DebugDrawModes.DBG_DrawContactPoints);
        if (grid)
            world.add(new BulletEntity(ModelGenerator.generateAxis(-10, 10, 1), null));

        btCollisionShape collisionShape = Bullet.obtainStaticNodeShape(Assets.get(Models.MODEL_ISLAND_PROTOTYPE, Model.class).nodes.first(), true);
        world.addConstructor("island", new BulletConstructor(Assets.get(Models.MODEL_ISLAND_PROTOTYPE, Model.class), 0, collisionShape));
        return world;
    }

    public static Environment generateBaseEnvironment(Vector3 sun) {
        Environment environment = new Environment();
        environment.set(new ColorAttribute(ColorAttribute.AmbientLight, 0.4f, 0.4f, 0.4f, 1.f));
        environment.set(new ColorAttribute(ColorAttribute.Fog, .3f, .55f, 1, 1));
        environment.add(new DirectionalLight().set(.3f, .3f, .3f, -.2f, 0.6f, .8f));
        environment.add(new DirectionalLight().set(1f, 1f, 1f, .2f, -0.6f, -.8f));
        environment.add(new PointLight().set(1, 1, 1, sun, 200));
        return environment;
    }

    public static PerspectiveCamera generatePerspectiveCamera(float near, float far, Vector3 position, Vector3 lookat) {
        PerspectiveCamera camera = new PerspectiveCamera(67, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        camera.position.set(position);
        camera.lookAt(lookat);
        camera.near = near;
        camera.far = far;
        camera.update();
        return camera;
    }

    private static class TreeCultivator implements Task {

        private BulletWorld world;
        private Array<Tree> trees;

        public TreeCultivator(BulletWorld world, Array<Tree> trees) {
            this.world = world;
            this.trees = trees;
        }

        @Override
        public boolean isAlive() {
            return true;
        }

        @Override
        public void update() {
            for (Tree t : trees)
                t.update(world, .1f);
        }

        @Override
        public void init() {

        }
    }
}
