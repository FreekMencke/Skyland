package com.toxsickproductions.skyland.listeners;

import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.math.collision.Ray;
import com.badlogic.gdx.physics.bullet.collision.ClosestRayResultCallback;
import com.badlogic.gdx.physics.bullet.collision.btCollisionObject;
import com.badlogic.gdx.utils.Array;
import com.toxsickproductions.g3d.bullet.BulletEntity;
import com.toxsickproductions.g3d.bullet.BulletWorld;
import com.toxsickproductions.skyland.scenes2d.Resources;
import com.toxsickproductions.skyland.scenes3d.generators.CloudGenerator;
import com.toxsickproductions.skyland.scenes3d.util.Builder;
import com.toxsickproductions.skyland.scenes3d.util.BulletUserData;
import com.toxsickproductions.skyland.scenes3d.util.CloudPoofer;
import com.toxsickproductions.skyland.scenes3d.util.ParticleUtils;
import reference.Models;

/**
 * Created by Freek on 9/01/2015.
 */
public class RayCastListener extends InputAdapter {

    private ParticleUtils particleUtils;
    private BulletWorld world;
    private Camera camera;
    private CloudPoofer cloudPoofer;

    private String[] bodies = {"cloud", "staticTree", "dynamicTree", "log", "cave", "stone"};
    private Array<String> bodiesArray = new Array<String>(bodies);

    private ClosestRayResultCallback rayTestCB;
    private Vector3 rayFrom = new Vector3(),
            rayTo = new Vector3();

    private Resources resources;

    public RayCastListener(BulletWorld world, Camera camera) {
        this.world = world;
        this.camera = camera;
    }

    public void initParticleUtils(ParticleUtils particleUtils) {
        this.particleUtils = particleUtils;
    }

    public void initCloudPoofer(CloudGenerator cloudGenerator) {
        cloudPoofer = new CloudPoofer(particleUtils, cloudGenerator);
    }

    public void initResources(Resources resources) {
        this.resources = resources;
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        if (rayTestCB == null) rayTestCB = new ClosestRayResultCallback(Vector3.Zero, Vector3.Z);
        Ray ray = camera.getPickRay(screenX, screenY);
        rayFrom.set(ray.origin);
        rayTo.set(ray.direction).scl(200).add(rayFrom);

        rayTestCB.setCollisionObject(null);
        rayTestCB.setClosestHitFraction(1f);
        rayTestCB.setRayFromWorld(rayFrom);
        rayTestCB.setRayToWorld(rayTo);

        world.collisionWorld.rayTest(rayFrom, rayTo, rayTestCB);
        if (rayTestCB.hasHit()) {
            Vector3 v3 = new Vector3();
            rayTestCB.getHitPointWorld(v3);
            System.out.println(v3);
            final btCollisionObject obj = rayTestCB.getCollisionObject();
            if (obj.userData instanceof BulletUserData) {
                BulletUserData data = ((BulletUserData) obj.userData);
                System.out.println(data.id);
                Matrix4 transform;
                switch (bodiesArray.indexOf(data.id, true)) {
                    case 0: //CloudPoofer
                        if (cloudPoofer != null)
                            cloudPoofer.shootCloud(data);
                        break;
                    case 1: //TreeChopper: converts static to dynamic tree; TODO temp code
                        Builder.setBuildModel(Models.MODEL_TREE_PROTOTYPE);
                        transform = ((BulletEntity) data.reference).transform;
                        Builder.destroy(data);
                        Builder.build(transform);
                        break;
                    case 2: //TreeSawmill: converts dynamic tree to log; TODO temp code
                        Builder.setBuildModel(Models.MODEL_LOG_PROTOTYPE);
                        transform = ((BulletEntity) data.reference).transform;
                        Builder.destroy(data);
                        Builder.build(transform);
                        break;
                    case 3:
                        resources.lootLog();
                        Builder.destroy(data);
                        break;
                    case 4:
                        Builder.setBuildModel(Models.MODEL_STONE_PROTOTYPE);
                        transform = new Matrix4().translate(new Vector3(-3.8f, ((BulletEntity) data.reference).transform.getTranslation(v3).y + 1f, -1.7f));
                        transform.rotate(0, 1, 0, 50);
                        transform.scale(1.3f, 1.3f, 1.3f);
                        particleUtils.caveDust(transform.cpy());
                        if (MathUtils.randomBoolean(.1f)) Builder.build(transform.getTranslation(new Vector3()));
                        break;
                    case 5:
                        resources.lootStone();
                        Builder.destroy(data);
                        break;
                    default:
                        return false;
                }
                return true;
            }

        }
        return false;
    }
}