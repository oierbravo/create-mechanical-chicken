package com.oierbravo.create_mechanical_chicken.content.components;

import com.oierbravo.create_mechanical_chicken.CreateMechanicalChicken;
import com.oierbravo.create_mechanical_chicken.foundation.blockEntity.behaviour.CycleBehavior;
import com.oierbravo.create_mechanical_chicken.foundation.utility.ModLang;
import com.simibubi.create.content.kinetics.base.KineticBlockEntity;
import com.simibubi.create.foundation.blockEntity.behaviour.BlockEntityBehaviour;
import com.simibubi.create.foundation.blockEntity.behaviour.fluid.SmartFluidTankBehaviour;
import com.simibubi.create.foundation.fluid.CombinedTankWrapper;
import com.simibubi.create.foundation.fluid.FluidIngredient;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.FluidTags;
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
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;
import net.minecraftforge.registries.ForgeRegistries;
import org.slf4j.Logger;

import javax.annotation.Nonnull;
import java.util.List;

public class MechanicalChickenBlockEntity extends KineticBlockEntity implements CycleBehavior.CycleBehaviourSpecifics, IHavePercent {

    private CycleBehavior cycleBehaviour;

    private FluidIngredient requiredFluidIngredient;



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
        verifyConfig(CreateMechanicalChicken.LOGGER);
        inputTank.getPrimaryHandler().setValidator(requiredFluidIngredient);
    }

    @Override
    public void addBehaviours(List<BlockEntityBehaviour> behaviours) {
        super.addBehaviours(behaviours);

        inputTank = new SmartFluidTankBehaviour(SmartFluidTankBehaviour.INPUT, this, 1, MechanicalChickenConfigs.FLUID_CAPACITY.get(), true)
                .whenFluidUpdates(() -> contentsChanged = true);
        behaviours.add(inputTank);

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

        if(inputTank.getPrimaryHandler().getFluidAmount() < MechanicalChickenConfigs.REQUIRED_FLUID_AMOUNT.get())
            return false;
        if(ItemStack.EMPTY != outputInventory.insertItem(0, new ItemStack(Items.EGG,MechanicalChickenConfigs.OUTPUT_AMOUNT.get()),true))
            return false;


        if(simulate)
            return true;

        inputTank.getPrimaryHandler().drain(MechanicalChickenConfigs.REQUIRED_FLUID_AMOUNT.get(), IFluidHandler.FluidAction.EXECUTE);
        outputInventory.insertItem(0, new ItemStack(Items.EGG,MechanicalChickenConfigs.OUTPUT_AMOUNT.get()),false);

        return true;
    }

    @Override
    public void playSound() {
        //MechanicalChickenConfigs.REQUIRED_FLUID.get()
        level.playSound((Entity) null, worldPosition, SoundEvents.CHICKEN_EGG, SoundSource.BLOCKS, 0.6F,1.0f);
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
    public FluidIngredient getFluidIngredient(){
        int amount = MechanicalChickenConfigs.REQUIRED_FLUID_AMOUNT.get();
        String name = MechanicalChickenConfigs.REQUIRED_FLUID.get();
        if(name.startsWith("#"))
            return FluidIngredient.fromTag(FluidTags.create(ResourceLocation.tryParse(name.replace("#",""))), amount);
       return FluidIngredient.fromFluid(getRequiredFluid(),amount);
    }
    public Fluid getRequiredFluid(){
        return ForgeRegistries.FLUIDS.getValue(ResourceLocation.tryParse(MechanicalChickenConfigs.REQUIRED_FLUID.get()));
    }

    public FluidStack getRequiredFluidStack(){
        Fluid requiredFluid = getRequiredFluid();
        if(requiredFluid == null)
            return FluidStack.EMPTY;
        return new FluidStack(requiredFluid, MechanicalChickenConfigs.REQUIRED_FLUID_AMOUNT.get());
    }
    public void verifyConfig(final Logger logger) {
        if (requiredFluidIngredient == null) {
            // verify and set the configured fluid
            final String fluidResourceRaw = MechanicalChickenConfigs.REQUIRED_FLUID.get();
            int configuredAmount = MechanicalChickenConfigs.REQUIRED_FLUID_AMOUNT.get();
            if(fluidResourceRaw.startsWith("#")){
                ResourceLocation fluidTag = ResourceLocation.tryParse(fluidResourceRaw.replace("#",""));
                assert fluidTag != null;
                requiredFluidIngredient = FluidIngredient.fromTag(FluidTags.create(fluidTag),configuredAmount);
                return;
            }
            final ResourceLocation desiredFluid = new ResourceLocation(fluidResourceRaw);

            if (ForgeRegistries.FLUIDS.containsKey(desiredFluid)) {
                requiredFluidIngredient = FluidIngredient.fromFluid(ForgeRegistries.FLUIDS.getValue(desiredFluid), configuredAmount);
            } else {
                logger.error("Unknown fluid '{}' in config, using default '{}' instead", fluidResourceRaw, "minecraft:water");
                requiredFluidIngredient = FluidIngredient.fromFluid(Fluids.WATER, configuredAmount);
            }
        }
    }

}
