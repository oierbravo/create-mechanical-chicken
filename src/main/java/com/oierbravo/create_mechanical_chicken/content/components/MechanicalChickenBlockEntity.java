package com.oierbravo.create_mechanical_chicken.content.components;

import com.oierbravo.create_mechanical_chicken.CreateMechanicalChicken;
import com.oierbravo.create_mechanical_chicken.ModLang;
import com.oierbravo.create_mechanical_chicken.ModRegistration;
import com.oierbravo.create_mechanical_chicken.infrastructure.config.MConfigs;
import com.oierbravo.mechanicals.compat.jade.IHavePercent;
import com.oierbravo.mechanicals.foundation.blockEntity.behaviour.DynamicCycleBehavior;
import com.simibubi.create.content.kinetics.base.KineticBlockEntity;
import com.simibubi.create.foundation.blockEntity.behaviour.BlockEntityBehaviour;
import com.simibubi.create.foundation.blockEntity.behaviour.fluid.SmartFluidTankBehaviour;
import com.simibubi.create.foundation.fluid.FluidIngredient;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.BuiltInRegistries;
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
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent;
import net.neoforged.neoforge.common.util.Lazy;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.fluids.capability.IFluidHandler;
import net.neoforged.neoforge.items.IItemHandler;
import net.neoforged.neoforge.items.ItemStackHandler;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;

import java.util.List;

public class MechanicalChickenBlockEntity extends KineticBlockEntity implements DynamicCycleBehavior.DynamicCycleBehaviorSpecifics, IHavePercent {

    private DynamicCycleBehavior cycleBehaviour;
    public SmartFluidTankBehaviour inputTank;
    private boolean contentsChanged;
    private FluidIngredient requiredFluidIngredient;

    public final ItemStackHandler outputInventory = new ItemStackHandler(1) {
        @Override
        protected void onContentsChanged(int slot) {
            setChanged();
            level.sendBlockUpdated(worldPosition, getBlockState(), getBlockState(), Block.UPDATE_ALL);
        }
    };
    private final Lazy<IItemHandler> itemCapability = Lazy.of(() -> outputInventory);

    public MechanicalChickenBlockEntity(BlockEntityType<?> typeIn, BlockPos pos, BlockState state) {
        super(typeIn, pos, state);
        verifyConfig(CreateMechanicalChicken.LOGGER);
        inputTank.getPrimaryHandler().setValidator(requiredFluidIngredient);
    }

    @Override
    public void addBehaviours(List<BlockEntityBehaviour> behaviours) {
        inputTank = SmartFluidTankBehaviour.single(this, MConfigs.server().mechanicalChicken.fluidCapacity.get());
        behaviours.add(inputTank);

        cycleBehaviour = new DynamicCycleBehavior(this);
        behaviours.add(cycleBehaviour);
    }

    @Override
    public void remove() {
        super.remove();
    }

   @Override
    public void invalidate() {
        super.invalidate();
        invalidateCapabilities();
    }

    public static void registerCapabilities(RegisterCapabilitiesEvent event) {
        event.registerBlockEntity(
                Capabilities.ItemHandler.BLOCK,
                ModRegistration.MECHANICAL_CHICKEN_BLOCK_ENTITY.get(),
                (be, context) -> {
                    Direction localDir = be.getBlockState().getValue(MechanicalChickenBlock.HORIZONTAL_FACING);
                    if(context != null && localDir == context.getOpposite())
                        return be.getItemHandler();
                    if(context == null)
                        return be.getItemHandler();
                    return null;
                }
        );

        event.registerBlockEntity(
                Capabilities.FluidHandler.BLOCK,
                ModRegistration.MECHANICAL_CHICKEN_BLOCK_ENTITY.get(),
                (be, context) -> {
                    Direction localDir = be.getBlockState().getValue(MechanicalChickenBlock.HORIZONTAL_FACING);
                    if(context != null && localDir == context)
                        return be.inputTank.getCapability();
                    if(context == null)
                        return be.inputTank.getCapability();
                    return null;
                }
        );
    }

    private @Nullable IItemHandler getItemHandler() {
        return outputInventory;
    }

    public DynamicCycleBehavior getCycleBehaviour() {
        return cycleBehaviour;
    }

    @Override
    public void write(CompoundTag compound, HolderLookup.Provider registries, boolean clientPacket) {
        compound.put("OutputInventory", outputInventory.serializeNBT(registries));
        super.write(compound, registries, clientPacket);
    }

    @Override
    protected void read(CompoundTag compound, HolderLookup.Provider registries, boolean clientPacket) {
        outputInventory.deserializeNBT(registries, compound.getCompound("OutputInventory"));
        super.read(compound, registries, clientPacket);
    }


    @Override
    public float getKineticSpeed() {
        return getSpeed();
    }

    @Override
    public boolean tryProcess(boolean simulate) {

        if(inputTank.getPrimaryHandler().isEmpty())
            return false;

        if(inputTank.getPrimaryHandler().getFluidAmount() < MConfigs.server().mechanicalChicken.requiredFluidAmount.get())
            return false;
        if(ItemStack.EMPTY != outputInventory.insertItem(0, new ItemStack(Items.EGG,MConfigs.server().mechanicalChicken.outputAmount.get()),true))
            return false;

        if(simulate)
            return true;

        inputTank.getPrimaryHandler().drain(MConfigs.server().mechanicalChicken.requiredFluidAmount.get(), IFluidHandler.FluidAction.EXECUTE);
        outputInventory.insertItem(0, new ItemStack(Items.EGG,MConfigs.server().mechanicalChicken.outputAmount.get()),false);

        return true;
    }

    @Override
    public int getProcessingTime() {
        return MConfigs.server().mechanicalChicken.processingTime.get();
    }


    @Override
    public void playCompletionSound() {
        //level.playSound((Entity) null, worldPosition, SoundEvents.CHICKEN_EGG, SoundSource.BLOCKS, MechanicalChickenConfigs.SOUND_VOLUME.get().floatValue(),1.0f);
        level.playSound((Entity) null, worldPosition, SoundEvents.CHICKEN_EGG, SoundSource.BLOCKS, MConfigs.server().mechanicalChicken.soundVolume.get().floatValue(),1.0f);
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
        int amount = MConfigs.server().mechanicalChicken.requiredFluidAmount.get();
        String name = MConfigs.server().mechanicalChicken.requiredFluid.get();
        if(name.startsWith("#"))
            return FluidIngredient.fromTag(FluidTags.create(ResourceLocation.parse(name.replace("#",""))), amount);
       return FluidIngredient.fromFluid(getRequiredFluid(),amount);
    }

    public Fluid getRequiredFluid(){
        return BuiltInRegistries.FLUID.get(ResourceLocation.tryParse(MConfigs.server().mechanicalChicken.requiredFluid.get()));
    }

    public FluidStack getRequiredFluidStack(){
        Fluid requiredFluid = getRequiredFluid();
        if(requiredFluid == null)
            return FluidStack.EMPTY;
        return new FluidStack(requiredFluid, MConfigs.server().mechanicalChicken.requiredFluidAmount.get());
    }

    public void verifyConfig(final Logger logger) {
        if (requiredFluidIngredient == null) {
            // verify and set the configured fluid
            final String fluidResourceRaw = MConfigs.server().mechanicalChicken.requiredFluid.get();
            int configuredAmount = MConfigs.server().mechanicalChicken.requiredFluidAmount.get();
            if(fluidResourceRaw.startsWith("#")){
                ResourceLocation fluidTag = ResourceLocation.tryParse(fluidResourceRaw.replace("#",""));
                assert fluidTag != null;
                requiredFluidIngredient = FluidIngredient.fromTag(FluidTags.create(fluidTag),configuredAmount);
                return;
            }
            final ResourceLocation desiredFluid = ResourceLocation.parse(fluidResourceRaw);

            if (BuiltInRegistries.FLUID.containsKey(desiredFluid)) {
                requiredFluidIngredient = FluidIngredient.fromFluid(BuiltInRegistries.FLUID.get(desiredFluid), configuredAmount);
            } else {
                logger.error("Unknown fluid '{}' in config, using default '{}' instead", fluidResourceRaw, "minecraft:water");
                requiredFluidIngredient = FluidIngredient.fromFluid(Fluids.WATER, configuredAmount);
            }
        }
    }

}
