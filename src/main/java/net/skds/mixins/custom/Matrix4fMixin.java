package net.skds.mixins.custom;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import net.minecraft.util.math.vector.Matrix4f;
import net.skds.core.util.interfaces.IMixM4f;
import net.skds.core.util.mat.Vec3;

@Mixin(Matrix4f.class)
public class Matrix4fMixin implements IMixM4f {
	
	@Shadow
	float m03;
	@Shadow
	float m13;
	@Shadow
	float m23;

	@Override
	public Vec3 getT() {
		return new Vec3(m03, m13, m23);
	}
}
