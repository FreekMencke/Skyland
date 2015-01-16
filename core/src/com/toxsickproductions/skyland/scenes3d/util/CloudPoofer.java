package com.toxsickproductions.skyland.scenes3d.util;

import com.badlogic.gdx.math.Vector3;
import com.toxsickproductions.g3d.bullet.BulletEntity;
import com.toxsickproductions.skyland.scenes3d.generators.CloudGenerator;

/**
 * Created by Freek on 8/01/2015.
 */

public class CloudPoofer {

    CloudGenerator cloudGenerator;
    ParticleUtils particleUtils;

    public CloudPoofer(ParticleUtils particleUtils, CloudGenerator cloudGenerator) {
        this.particleUtils = particleUtils;
        this.cloudGenerator = cloudGenerator;
    }

    public void shootCloud(BulletUserData data) {
        particleUtils.cloudPoof(((BulletEntity) data.reference).body.getWorldTransform().getTranslation(new Vector3()));
        cloudGenerator.clouds.removeValue((BulletEntity) data.reference, false);
        Builder.destroy(data);
    }
}