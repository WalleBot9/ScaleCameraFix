package com.mrheller.scale_camera_fix.mixin;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RotationAxis;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyConstant;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(GameRenderer.class)
public class CameraMixin {
	@Shadow @Final
	private MinecraftClient client;

	@ModifyConstant(method = "getBasicProjectionMatrix", constant = @Constant(floatValue = 0.05F))
	public float getBasicProjectionMatrix(float original) {
		LivingEntity entity = (LivingEntity)((GameRenderer)(Object)this).getCamera().getFocusedEntity();
		float scale = (float) entity.getAttributeValue(EntityAttributes.SCALE);

		return (float)Math.clamp(original*scale, 0.005, 0.05f);
	}

	@Inject(method = "bobView", at = @At("HEAD"), cancellable = true)
	private void bobView(MatrixStack matrices, float tickDelta, CallbackInfo ci) {
		if (this.client.getCameraEntity() instanceof AbstractClientPlayerEntity abstractClientPlayerEntity) {
			float scale = (float) abstractClientPlayerEntity.getAttributeValue(EntityAttributes.SCALE) * 2f;

			float scaleMultiplier = Math.min(scale, 1);

			float var7 = abstractClientPlayerEntity.distanceMoved - abstractClientPlayerEntity.lastDistanceMoved;
			float g = -(abstractClientPlayerEntity.distanceMoved + var7 * tickDelta);
			float h = MathHelper.lerp(tickDelta, abstractClientPlayerEntity.lastStrideDistance, abstractClientPlayerEntity.strideDistance);
			matrices.translate(MathHelper.sin(g * (float) Math.PI) * scaleMultiplier * h * 0.5F,
								 -Math.abs(MathHelper.cos(g * (float) Math.PI) * scaleMultiplier * h), 0.0F);
			matrices.multiply(RotationAxis.POSITIVE_Z.rotationDegrees(MathHelper.sin(g * (float) Math.PI) * h * 3.0F));
			matrices.multiply(RotationAxis.POSITIVE_X.rotationDegrees(Math.abs(MathHelper.cos(g * (float) Math.PI - 0.2F) * h) * 5.0F));

			ci.cancel();
		}
	}
}