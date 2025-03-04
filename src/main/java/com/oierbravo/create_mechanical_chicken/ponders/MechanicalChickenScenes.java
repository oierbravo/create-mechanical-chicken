package com.oierbravo.create_mechanical_chicken.ponders;

import com.oierbravo.create_mechanical_chicken.content.components.MechanicalChickenBlock;
import com.oierbravo.create_mechanical_chicken.content.components.MechanicalChickenBlockEntity;
import com.simibubi.create.foundation.ponder.*;
import net.createmod.ponder.api.PonderPalette;
import net.createmod.ponder.api.scene.SceneBuilder;
import net.createmod.ponder.api.scene.SceneBuildingUtil;
import net.createmod.ponder.api.scene.Selection;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

public class MechanicalChickenScenes {
    public static void mechanicalChicken(SceneBuilder builder, SceneBuildingUtil util) {
        CreateSceneBuilder scene = new CreateSceneBuilder(builder);

        scene.title("mechanical_chicken", "Mechanical chicken");
        scene.configureBasePlate(0, 0, 7);
        //scene.showBasePlate();

        BlockPos chickenPos = util.grid().at(3, 2, 3);
        Selection chickenSelection = util.select().position(3, 2, 3);
        Selection chickenShaftSelection = util.select().fromTo(3, 0, 3,3, 1, 3);

        scene.world().showSection(util.select().layer(0).substract(chickenShaftSelection), Direction.DOWN);
        scene.idle(10);

        scene.world().modifyBlock(chickenPos, s -> s.setValue(MechanicalChickenBlock.HORIZONTAL_FACING, Direction.WEST), false);

        scene.world().showSection(chickenSelection,Direction.UP);
        scene.world().showSection(chickenShaftSelection,Direction.UP);

        scene.addKeyframe();
        scene.idle(10);



        scene.overlay().showOutlineWithText(chickenSelection, 60)
                .text("The Mechanical Chicken uses rotational force and an specific fluid to generate eggs")
                .pointAt(util.vector().blockSurface(chickenPos, Direction.WEST)
                        //.add(-.5, .4, 0)
                        )
                .placeNearTarget();
        scene.idle(70);

        scene.world().setKineticSpeed(chickenSelection, 32);
        scene.world().setKineticSpeed(chickenShaftSelection, 32);
        scene.effects().indicateSuccess(chickenPos);
        scene.effects().indicateSuccess(chickenPos.below());
        scene.effects().indicateSuccess(chickenPos.below(2));


        scene.overlay().showText(60)
                .attachKeyFrame()
                .colored(PonderPalette.GREEN)
                .text("Its powered from the bottom")
                .pointAt(util.vector().centerOf(chickenPos.below()))
                .placeNearTarget();
        scene.idle(70);

        //Vec3 millstoneTop = util.vector().topOf(millstone);
        //scene.addKeyframe();

        Selection fluidSelection = util.select().fromTo(4, 1, 0, 6,4,6);
        scene.world().setKineticSpeed(fluidSelection,-32);
        scene.world().showSection(fluidSelection,Direction.WEST);

        scene.world().modifyBlock(chickenPos, s -> s.setValue(MechanicalChickenBlock.HORIZONTAL_FACING, Direction.NORTH), false);
        scene.idle(5);
        scene.world().modifyBlock(chickenPos, s -> s.setValue(MechanicalChickenBlock.HORIZONTAL_FACING, Direction.EAST), false);



        scene.overlay().showText(60)
                .attachKeyFrame()
                .colored(PonderPalette.GREEN)
                .text("Fluid input can ONLY go from the FRONT side")
                .pointAt(util.vector().centerOf(chickenPos.east()))
                .placeNearTarget();
        scene.idle(70);

        BlockPos chickenPumpPos = util.grid().at(5, 2, 3);

        scene.world().propagatePipeChange(chickenPumpPos);

        Class<MechanicalChickenBlockEntity> type = MechanicalChickenBlockEntity.class;
        scene.world().modifyBlockEntity(chickenPos, type, pte -> pte.getCycleBehaviour()
                .start());

        scene.idle(70);

        BlockPos depotPos = util.grid().at(2, 1, 3);
        Selection depotSelection = util.select().position(2,1,3);
        Selection funnelSelection = util.select().position(2, 2, 3);
        scene.world().showSection(depotSelection,Direction.EAST);
        scene.world().showSection(funnelSelection,Direction.DOWN);

        ItemStack eggStack = new ItemStack(Items.EGG);

        scene.world().modifyBlockEntity(chickenPos, MechanicalChickenBlockEntity.class,
                ms -> ms.outputInventory.setStackInSlot(0, eggStack));

        scene.overlay().showText(45)
                .attachKeyFrame()
                .colored(PonderPalette.GREEN)
                .text("Egg output is ONLY located on the BACK side")
                .pointAt(util.vector().centerOf(chickenPos.west()))
                .placeNearTarget();
        scene.idle(55);


        scene.overlay().showText(45)
                .attachKeyFrame()
                .colored(PonderPalette.GREEN)
                .text("Can only be extracted by automation")
                .pointAt(util.vector().centerOf(chickenPos.west()))
                .placeNearTarget();
        scene.idle(55);


        scene.world().createItemOnBeltLike(depotPos, Direction.UP, eggStack);
        scene.idle(20);

        scene.markAsFinished();

    }
}
