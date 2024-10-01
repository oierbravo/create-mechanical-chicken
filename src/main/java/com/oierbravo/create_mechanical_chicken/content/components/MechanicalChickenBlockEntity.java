package com.oierbravo.create_mechanical_chicken.content.components;

import com.oierbravo.create_mechanical_chicken.foundation.blockEntity.behaviour.CycleBehavior;
import com.oierbravo.create_mechanical_chicken.foundation.utility.ModLang;
import com.simibubi.create.content.kinetics.base.KineticBlockEntity;
import com.simibubi.create.foundation.blockEntity.behaviour.BlockEntityBehaviour;
import com.simibubi.create.foundation.blockEntity.behaviour.fluid.SmartFluidTankBehaviour;
import com.simibubi.create.foundation.fluid.CombinedTankWrapper;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.Fluids;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidUtil;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fluids.capability.templates.FluidTank;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;
import net.minecraftforge.registries.ForgeRegistries;

import javax.annotation.Nonnull;
import java.util.List;
import java.util.Map;

import static net.minecraft.world.level.block.state.properties.BlockStateProperties.HORIZONTAL_FACING;

public class MechanicalChickenBlockEntity extends KineticBlockEntity implements CycleBehavior.CycleBehaviourSpecifics, IHavePercent {

    private CycleBehavior cycleBehaviour;




    public final ItemStackHandler outputInventory = new ItemStackHandler(1) {
        @Override
        protected void onContentsChanged(int slot) {
            setChanged();
            level.sendBlockUpdated(worldPosition, getBlockState(), getBlockState(), Block.UPDATE_ALL);
        }
    };
    public SmartFluidTankBehaviour inputTank;
    private boolean contentsChanged;
    protected LazyOptional<IFluidHandler> fluidCapability;
    private final LazyOptional<IItemHandler> itemCapability = LazyOptional.of(() -> outputInventory);


    public MechanicalChickenBlockEntity(BlockEntityType<?> typeIn, BlockPos pos, BlockState state) {
        super(typeIn, pos, state);
    }

    @Override
    public void addBehaviours(List<BlockEntityBehaviour> behaviours) {
        super.addBehaviours(behaviours);

        inputTank = new SmartFluidTankBehaviour(SmartFluidTankBehaviour.INPUT, this, 1, MechanicalChickenConfigs.FLUID_CAPACITY.get(), true)
                .whenFluidUpdates(() -> contentsChanged = true);
        behaviours.add(inputTank);
        FluidStack requiredFluidStack = getRequiredFluidStack();

        inputTank.getPrimaryHandler().setValidator(fluidStack ->fluidStack.isFluidEqual(requiredFluidStack));

        fluidCapability = LazyOptional.of(() -> {
            LazyOptional<? extends IFluidHandler> inputCap = inputTank.getCapability();
            return new CombinedTankWrapper(inputCap.orElse(null));
        });
        cycleBehaviour = new CycleBehavior(this, MechanicalChickenConfigs.PROCESSING_TIME.get(),true);
        behaviours.add(cycleBehaviour);
    }
    @Override
    public void invalidate() {
        super.invalidate();
        itemCapability.invalidate();
        fluidCapability.invalidate();
    }

    @Nonnull
    @Override
    public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, Direction side) {
        Direction localDir = this.getBlockState().getValue(MechanicalChickenBlock.HORIZONTAL_FACING);
        if (side == null) {
            if (cap == ForgeCapabilities.ITEM_HANDLER)
                return itemCapability.cast();
            if (cap == ForgeCapabilities.FLUID_HANDLER)
                return fluidCapability.cast();
        }
        if(side == Direction.DOWN ||side == Direction.UP) {
            return super.getCapability(cap, side);
        }
        if (cap == ForgeCapabilities.ITEM_HANDLER) {
            if(localDir == side.getOpposite())
                return itemCapability.cast();
        }
        if (cap == ForgeCapabilities.FLUID_HANDLER) {
            if(localDir == side)
                return fluidCapability.cast();
        }
        return super.getCapability(cap, side);
    }

    public CycleBehavior getCycleBehaviour() {
        return cycleBehaviour;
    }


    @Override
    public void write(CompoundTag compound, boolean clientPacket) {
        super.write(compound, clientPacket);
        compound.put("OutputInventory", outputInventory.serializeNBT());
    }

    @Override
    public void read(CompoundTag compound, boolean clientPacket) {
        super.read(compound, clientPacket);
        outputInventory.deserializeNBT(compound.getCompound("OutputInventory"));
    }

    @Override
    public void onCycleCompleted() {

    }

    @Override
    public float getKineticSpeed() {
        return getSpeed();
    }

    @Override
    public boolean tryProcess(boolean simulate) {

        if(inputTank.getPrimaryHandler().isEmpty())
            return false;

        assert getRequiredFluid() != null;
        FluidStack requiredFluidStack = new FluidStack(getRequiredFluid(), MechanicalChickenConfigs.REQUIRED_FLUID_AMOUNT.get());
        //((FluidStack fluid) -> fluid.getFluid().isSame(fluidOutput))

        if(!inputTank.getPrimaryHandler().isFluidValid(requiredFluidStack))
            return false;

        if(inputTank.getPrimaryHandler().getFluidAmount() < MechanicalChickenConfigs.REQUIRED_FLUID_AMOUNT.get())
            return false;
        if(ItemStack.EMPTY != outputInventory.insertItem(0, new ItemStack(Items.EGG,MechanicalChickenConfigs.OUTPUT_AMOUNT.get()),true))
            return false;

        FluidStack test = inputTank.getPrimaryHandler().drain(requiredFluidStack, IFluidHandler.FluidAction.SIMULATE);

        //if(FluidStack.EMPTY != inputTank.getPrimaryHandler().drain(requiredFluidStack, IFluidHandler.FluidAction.SIMULATE))
        //    return false;

        if(simulate)
            return true;

        inputTank.getPrimaryHandler().drain(requiredFluidStack, IFluidHandler.FluidAction.EXECUTE);
        outputInventory.insertItem(0, new ItemStack(Items.EGG,MechanicalChickenConfigs.OUTPUT_AMOUNT.get()),false);

        return true;
    }

    @Override
    public void playSound() {
        level.playSound((Entity) null, worldPosition, SoundEvents.CHICKEN_EGG, SoundSource.BLOCKS, 1.0F,1.0f);
    }
    @Override
    public boolean addToGoggleTooltip(List<Component> tooltip, boolean isPlayerSneaking) {
        boolean added = super.addToGoggleTooltip(tooltip, isPlayerSneaking);
        if( this.cycleBehaviour.isRunning()) {
            ModLang.translate("chicken.tooltip.progress", getProgressPercent()).style(ChatFormatting.YELLOW).forGoggles(tooltip);
            added = true;
        }

        return added;

    }

    @Override
    public int getProgressPercent() {
        return this.cycleBehaviour.getProgressPercent();
    }

    public Fluid getRequiredFluid(){
        return ForgeRegistries.FLUIDS.getValue(new ResourceLocation(MechanicalChickenConfigs.REQUIRED_FLUID.get()));
    }

    public FluidStack getRequiredFluidStack(){
        Fluid requiredFluid = getRequiredFluid();
        if(requiredFluid == null)
            return FluidStack.EMPTY;
        return new FluidStack(requiredFluid, MechanicalChickenConfigs.REQUIRED_FLUID_AMOUNT.get());
    }


}
