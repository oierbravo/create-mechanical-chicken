package com.oierbravo.create_mechanical_chicken.foundation.blockEntity.behaviour;

import com.oierbravo.create_mechanical_chicken.content.components.MechanicalChickenBlockEntity;
import com.simibubi.create.AllSoundEvents;
import com.simibubi.create.foundation.blockEntity.SmartBlockEntity;
import com.simibubi.create.foundation.blockEntity.behaviour.BehaviourType;
import com.simibubi.create.foundation.blockEntity.behaviour.BlockEntityBehaviour;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.Mth;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.data.SoundDefinition;

public class CycleBehavior extends BlockEntityBehaviour {

	private final int CYCLE;
	private boolean ACTUATE_HALFCYCLE;
	public static final BehaviourType<CycleBehavior> TYPE = new BehaviourType<>();
	public CycleBehaviourSpecifics specifics;
	private int prevRunningTicks;
	private int runningTicks;
	private boolean running;
	private boolean finished;
	private int cycleDivider;


	public interface CycleBehaviourSpecifics {

		public void onCycleCompleted();
		public float getKineticSpeed();
		public boolean tryProcess(boolean simulate);
		public void playSound();
	}

	public <T extends SmartBlockEntity & CycleBehaviourSpecifics> CycleBehavior(T te, int pCycle, boolean pActuateHalfCycle) {
		super(te);
		this.specifics = te;
		CYCLE = pCycle;
		ACTUATE_HALFCYCLE = pActuateHalfCycle;
		cycleDivider = (ACTUATE_HALFCYCLE) ? 2 : 1;
	}

	@Override
	public void read(CompoundTag compound, boolean clientPacket) {
		running = compound.getBoolean("Running");
		finished = compound.getBoolean("Finished");
		prevRunningTicks = runningTicks = compound.getInt("Ticks");
		super.read(compound, clientPacket);
	}

	@Override
	public void write(CompoundTag compound, boolean clientPacket) {
		compound.putBoolean("Running", running);
		compound.putBoolean("Finished", finished);
		compound.putInt("Ticks", runningTicks);
		super.write(compound, clientPacket);
	}

	public void start() {
		running = true;
		prevRunningTicks = 0;
		runningTicks = 0;
		blockEntity.sendData();
	}

	@Override
	public BehaviourType<?> getType() {
		return TYPE;
	}

	@Override
	public void tick() {
		super.tick();

		Level level = getWorld();
		if (!running || level == null) {
			if (level != null && !level.isClientSide) {

				if (specifics.getKineticSpeed() == 0)
					return;

				if (specifics.tryProcess( true))
					start();
			}
			return;
		}


		if (level.isClientSide && runningTicks == -CYCLE / cycleDivider) {
			prevRunningTicks = CYCLE / cycleDivider;
			return;
		}

		if (runningTicks == CYCLE / 2 && specifics.getKineticSpeed() != 0) {
			apply();
			specifics.playSound();
			if (!level.isClientSide)
				blockEntity.sendData();
		}

		if (!level.isClientSide && runningTicks > CYCLE / cycleDivider) {
			finished = true;
			running = false;
			blockEntity.sendData();
			specifics.onCycleCompleted();
			return;
		}

		prevRunningTicks = runningTicks;
		runningTicks += getRunningTickSpeed();
		if (prevRunningTicks < CYCLE / 2 && runningTicks >= CYCLE / cycleDivider) {
			runningTicks = CYCLE / 2;
			// Pause the ticks until a packet is received
			if (level.isClientSide && !blockEntity.isVirtual())
				runningTicks = -(CYCLE / 2);
		}
	}

	public float getProgress(float partialTicks){
		if (!running)
			return 0;
		int runningTicks = Math.abs(this.runningTicks);
		float ticks = Mth.lerp(partialTicks, prevRunningTicks, runningTicks);
		return ticks/ CYCLE * 100;
	}


	protected void apply() {
		Level level = getWorld();

		if (level.isClientSide)
			return;

		if (specifics.tryProcess(false))
			blockEntity.sendData();
	}

	public int getRunningTickSpeed() {
		float speed = specifics.getKineticSpeed();
		if (speed == 0)
			return 0;
		return (int) Mth.lerp(Mth.clamp(Math.abs(speed) / 512f, 0, 1), 1, 60);
	}
	public boolean isRunning(){
		return running;
	}
	public int getProgressPercent() {
		return Mth.clamp(runningTicks * 100 / (CYCLE/cycleDivider), 0,100);
	}
}
