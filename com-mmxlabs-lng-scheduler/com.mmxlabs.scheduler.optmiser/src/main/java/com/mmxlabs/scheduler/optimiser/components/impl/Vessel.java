package com.mmxlabs.scheduler.optimiser.components.impl;

import com.mmxlabs.scheduler.optimiser.components.IVessel;

/**
 * Default implementation of {@link IVessel}
 * 
 * @author Simon Goodall
 * 
 */
public final class Vessel implements IVessel {

	private String name;

	private long ballastNBORate;
	
	private long ladenNBORate;

	private long cargoCapacity;

	private int minSpeed;
	
	private int maxSpeed;

	@Override
	public String getName() {
		return name;
	}

	public void setName(final String name) {
		this.name = name;
	}
	
	@Override
	public long getBallastNBORate() {
		return ballastNBORate;
	}

	public void setBallastNBORate(long ballastNBORate) {
		this.ballastNBORate = ballastNBORate;
	}

	@Override
	public long getLadenNBORate() {
		return ladenNBORate;
	}

	public void setLadenNBORate(long ladenNBORate) {
		this.ladenNBORate = ladenNBORate;
	}

	@Override
	public long getCargoCapacity() {
		return cargoCapacity;
	}

	public void setCargoCapacity(long cargoCapacity) {
		this.cargoCapacity = cargoCapacity;
	}

	@Override
	public int getMinSpeed() {
		return minSpeed;
	}

	public void setMinSpeed(int minSpeed) {
		this.minSpeed = minSpeed;
	}

	@Override
	public int getMaxSpeed() {
		return maxSpeed;
	}

	public void setMaxSpeed(int maxSpeed) {
		this.maxSpeed = maxSpeed;
	}
}
