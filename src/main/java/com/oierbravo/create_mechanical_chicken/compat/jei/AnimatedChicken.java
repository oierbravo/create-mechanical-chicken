package com.oierbravo.create_mechanical_chicken.compat.jei;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import com.oierbravo.create_mechanical_chicken.ModRegistration;
import com.oierbravo.create_mechanical_chicken.registrate.ModPartials;
import com.simibubi.create.compat.jei.category.animations.AnimatedKinetics;
import com.simibubi.create.foundation.utility.AnimationTickHolder;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.util.Mth;

public class AnimatedChicken extends AnimatedKinetics {
    public int offset = 1;
    @Override
    public void draw(GuiGraphics guiGraphics, int xOffset, int yOffset) {
        PoseStack matrixStack = guiGraphics.pose();

        matrixStack.pushPose();
        matrixStack.translate(xOffset, yOffset, 200);
        matrixStack.mulPose(Axis.XP.rotationDegrees(-15.5f));
        matrixStack.mulPose(Axis.YP.rotationDegrees(180.0f));
        int scale = 24;

        blockElement(ModRegistration.MECHANICAL_CHICKEN_BLOCK.getDefaultState())
                .scale(scale)
                .rotate(0,-75,0)
                .render(guiGraphics);

        blockElement(ModPartials.MECHANICAL_CHICKEN_HEAD)
                .atLocal(0, -.5f + getAnimatedHeadOffset() /5, 0)
                .scale(scale)
                .rotate(0,-75,0)
                .render(guiGraphics);

        matrixStack.popPose();
    }
    private float getAnimatedHeadOffset() {
        float cycle = (AnimationTickHolder.getRenderTime() - offset * 8) % 30;
        float progress = cycle / 10;
        float sin = (float) Math.sin(progress);
        float clamp = Mth.clamp(sin,-1,1);
        //return (float) Math.sin(progress) /100;
        return clamp;
        /*if (cycle < 10) {
            float progress = cycle / 10;
            return (float) Math.sin(progress) /100;
            //return -(progress * progress * progress);
        }
        if (cycle < 15)
            //return -1;
            return 0;
        if (cycle < 20)
            return -1 + (1 - ((20 - cycle) / 5));
        return 0;*/
    }

}
