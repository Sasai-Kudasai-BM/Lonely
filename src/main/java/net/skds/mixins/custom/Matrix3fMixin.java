package net.skds.mixins.custom;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import net.minecraft.util.math.vector.Matrix3f;
import net.skds.core.util.interfaces.IMixM3f;
import net.skds.core.util.mat.Matrix3;

@Mixin(Matrix3f.class)
public class Matrix3fMixin implements IMixM3f {
	
	@Shadow
	protected float m00;
	@Shadow
	protected float m01;
	@Shadow
	protected float m02;
	@Shadow
	protected float m10;
	@Shadow
	protected float m11;
	@Shadow
	protected float m12;
	@Shadow
	protected float m20;
	@Shadow
	protected float m21;
	@Shadow
	protected float m22;

	@Override
	public Matrix3 getM() {
		Matrix3 matrix3 = new Matrix3();

		matrix3.m00 = this.m00;
		matrix3.m01 = this.m01;
		matrix3.m02 = this.m02;
		matrix3.m10 = this.m10;
		matrix3.m11 = this.m11;
		matrix3.m12 = this.m12;
		matrix3.m20 = this.m20;
		matrix3.m21 = this.m21;
		matrix3.m22 = this.m22;

		return matrix3;
	}
}
