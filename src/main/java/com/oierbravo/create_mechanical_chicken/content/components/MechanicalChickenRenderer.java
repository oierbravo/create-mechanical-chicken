package com.oierbravo.create_mechanical_chicken.content.components;

import com.jozufozu.flywheel.backend.Backend;
import com.jozufozu.flywheel.core.PartialModel;
import com.jozufozu.flywheel.core.materials.model.ModelData;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import com.oierbravo.create_mechanical_chicken.foundation.blockEntity.behaviour.CycleBehavior;
import com.oierbravo.create_mechanical_chicken.registrate.ModPartials;
import com.simibubi.create.AllPartialModels;
import com.simibubi.create.content.kinetics.base.KineticBlockEntityRenderer;
import com.simibubi.create.foundation.render.CachedBufferer;
import com.simibubi.create.foundation.render.SuperByteBuffer;
import com.simibubi.create.foundation.utility.AngleHelper;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.core.Direction;
import net.minecraft.core.Vec3i;
import net.minecraft.util.Mth;
import net.minecraft.world.level.block.state.BlockState;
import org.joml.Quaternionf;
import org.joml.Vector3f;

import static net.minecraft.world.level.block.state.properties.BlockStateProperties.HORIZONTAL_FACING;

public class MechanicalChickenRenderer extends KineticBlockEntityRenderer<MechanicalChickenBlockEntity> {
    public MechanicalChickenRenderer(BlockEntityRendererProvider.Context context) {
        super(context);
    }

    @Override
    protected void renderSafe(MechanicalChickenBlockEntity be, float partialTicks, PoseStack ms, MultiBufferSource buffer, int light, int overlay) {
        if (Backend.canUseInstancing(be.getLevel()))
            return;

        VertexConsumer vb = buffer.getBuffer(RenderType.solid());

        BlockState blockState = be.getBlockState();

        CycleBehavior cycleBehavior = be.getCycleBehaviour();

        SuperByteBuffer headRender = CachedBufferer.partialFacing(ModPartials.MECHANICAL_CHICKEN_HEAD, blockState,
                blockState.getValue(HORIZONTAL_FACING).getOpposite());

        float yPos = 0.0f;
        if(cycleBehavior.getProgress(partialTicks) > 0)
            yPos = (float) Math.sin(cycleBehavior.getProgress(partialTicks)) /80;

        headRender.translate(0, .3+ yPos, 0)
                .light(light)
                .renderInto(ms, vb);

      /*double angle = 1 * cycleBehavior.getProgress(partialTicks);
      SuperByteBuffer cogRenderer = CachedBufferer.partial(ModPartials.MECHANICAL_CHICKEN_COG_HORIZONTAL, blockState);
                cogRenderer
                .centre()
                .rotateYRadians(-angle + Mth.HALF_PI)
               .unCentre()
               .light(light)
               .renderInto(ms, vb);*/

        SuperByteBuffer superBuffer = CachedBufferer.partial(ModPartials.MECHANICAL_CHICKEN_COG_HORIZONTAL,blockState);
        standardKineticRotationTransform(superBuffer, be, light).renderInto(ms, vb);
    }
}
