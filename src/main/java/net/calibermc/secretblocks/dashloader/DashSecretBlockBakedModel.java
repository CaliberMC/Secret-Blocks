package net.calibermc.secretblocks.dashloader;

import net.calibermc.secretblocks.render.SecretBlockBakedModel;
import net.minecraft.client.texture.Sprite;
import net.oskarstrom.dashloader.DashRegistry;
import net.oskarstrom.dashloader.api.annotation.DashConstructor;
import net.oskarstrom.dashloader.api.annotation.DashObject;
import net.oskarstrom.dashloader.api.enums.ConstructorMode;
import net.oskarstrom.dashloader.model.DashModel;

@DashObject(SecretBlockBakedModel.class)
public class DashSecretBlockBakedModel implements DashModel {

    @DashConstructor(ConstructorMode.EMPTY)
    public DashSecretBlockBakedModel() {
    }

    public SecretBlockBakedModel toUndash(DashRegistry registry) {
        return new SecretBlockBakedModel() {
            @Override
            public Sprite getParticleSprite() {
                return null;
            }
        };
    }

    public int getStage() {
        return 0;
    }
}
