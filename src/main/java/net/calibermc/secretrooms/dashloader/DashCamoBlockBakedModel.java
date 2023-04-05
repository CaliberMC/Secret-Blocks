package net.calibermc.secretrooms.dashloader;

import net.calibermc.secretrooms.render.CamoBlockBakedModel;
import net.minecraft.client.texture.Sprite;
import net.oskarstrom.dashloader.DashRegistry;
import net.oskarstrom.dashloader.api.annotation.DashConstructor;
import net.oskarstrom.dashloader.api.annotation.DashObject;
import net.oskarstrom.dashloader.api.enums.ConstructorMode;
import net.oskarstrom.dashloader.model.DashModel;

@DashObject(CamoBlockBakedModel.class)
public class DashCamoBlockBakedModel implements DashModel {

    @DashConstructor(ConstructorMode.EMPTY)
    public DashCamoBlockBakedModel() {
    }

    public CamoBlockBakedModel toUndash(DashRegistry registry) {
        return new CamoBlockBakedModel() {
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
