package net.calibermc.secretblocks.mixin;

import java.util.Map;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import net.minecraft.util.math.Direction;

@Mixin(Direction.class)
public interface DirectionAccessor {

	@Accessor("NAME_MAP")
	public static Map<String, Direction> NAME_MAP() {
		throw new AssertionError();
	}

}
