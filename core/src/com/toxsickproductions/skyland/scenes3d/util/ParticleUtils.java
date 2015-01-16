package com.toxsickproductions.skyland.scenes3d.util;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.resolvers.InternalFileHandleResolver;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.g3d.particles.ParticleEffect;
import com.badlogic.gdx.graphics.g3d.particles.ParticleEffectLoader;
import com.badlogic.gdx.graphics.g3d.particles.ParticleSystem;
import com.badlogic.gdx.graphics.g3d.particles.batches.BillboardParticleBatch;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector3;
import reference.Particles;

/**
 * Created by Freek on 8/01/2015.
 */
public class ParticleUtils {
    public AssetManager particleManager;
    private ParticleSystem particleSystem;

    public ParticleUtils() {
        particleSystem = ParticleSystem.get();
    }

    public void update() {
        particleSystem.begin();
        particleSystem.updateAndDraw();
        particleSystem.end();
    }

    public void initBillBoardParticles(Camera camera) {
        BillboardParticleBatch billboardParticleBatch = new BillboardParticleBatch();
        billboardParticleBatch.setCamera(camera);
        particleSystem.add(billboardParticleBatch);
        initManager();
    }

    public void cloudPoof(Vector3 translation) {
        ParticleEffect effect = particleManager.get(Particles.PARTICLE_CLOUD_PUFF, ParticleEffect.class).copy();
        effect.init();
        effect.start();
        effect.translate(translation.add(0, -2.5f, 0));
        effect.scale(1.5f, 1.5f, 1.5f);
        effect.rotate(new Vector3(0, 1, 0), 90);

        particleSystem.add(effect);
    }

    public void caveDust(Matrix4 transform) {
        ParticleEffect effect = particleManager.get(Particles.PARTICLE_CAVE_DUST, ParticleEffect.class).copy();
        effect.init();
        effect.start();
        effect.setTransform(transform);
        particleSystem.add(effect);
    }

    private void initManager() {
        particleManager = new AssetManager();
        ParticleEffectLoader.ParticleEffectLoadParameter loadParam = new ParticleEffectLoader.ParticleEffectLoadParameter(particleSystem.getBatches());
        ParticleEffectLoader loader = new ParticleEffectLoader(new InternalFileHandleResolver());
        particleManager.setLoader(ParticleEffect.class, loader);
        particleManager.load(Particles.PARTICLE_CLOUD_PUFF, ParticleEffect.class, loadParam);
        particleManager.load(Particles.PARTICLE_CAVE_DUST, ParticleEffect.class, loadParam);
        particleManager.finishLoading();
    }

    public ParticleSystem updateAndDraw() {
        update();
        return particleSystem;
    }

    public void dispose() {
        particleSystem.removeAll();
        particleSystem.getBatches().clear();
        particleManager.dispose();
    }

}
