package net.calibermc.secretrooms.render;

import net.fabricmc.fabric.api.client.model.ModelProviderContext;
import net.fabricmc.fabric.api.client.model.ModelProviderException;
import net.fabricmc.fabric.api.client.model.ModelResourceProvider;
import net.calibermc.secretrooms.SecretRooms;
import net.minecraft.client.render.model.UnbakedModel;
import net.minecraft.util.Identifier;

public class CamoBlockResourceProvider implements ModelResourceProvider {

	public static final CamoBlockUnbakedModel CAMO_MODEL = new CamoBlockUnbakedModel();
	public static final Identifier CAMO_BLOCK = SecretRooms.id("block/camo_block");

	@Override
	public UnbakedModel loadModelResource(Identifier identifier, ModelProviderContext modelProviderContext) throws ModelProviderException {
		if (identifier.equals(CAMO_BLOCK)) {
			return CAMO_MODEL;
		} else {
			return null;
		}
	}
}
