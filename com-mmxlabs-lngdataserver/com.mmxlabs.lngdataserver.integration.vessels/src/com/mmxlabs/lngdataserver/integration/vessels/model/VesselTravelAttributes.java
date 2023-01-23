/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
package com.mmxlabs.lngdataserver.integration.vessels.model;

import java.util.List;
import java.util.Optional;
import java.util.OptionalDouble;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(Include.NON_EMPTY)
public class VesselTravelAttributes {

	private OptionalDouble nboRate = OptionalDouble.empty();
	private OptionalDouble idleNBORate = OptionalDouble.empty();
	private OptionalDouble idleBaseRate = OptionalDouble.empty();
	private OptionalDouble serviceSpeed = OptionalDouble.empty();
	private Optional<List<FuelConsumption>> fuelConsumption = Optional.empty();

	public OptionalDouble getNboRate() {
		return nboRate;
	}

	public void setNboRate(OptionalDouble nboRate) {
		this.nboRate = nboRate;
	}

	public OptionalDouble getIdleNBORate() {
		return idleNBORate;
	}

	public void setIdleNBORate(OptionalDouble idleNBORate) {
		this.idleNBORate = idleNBORate;
	}

	public OptionalDouble getIdleBaseRate() {
		return idleBaseRate;
	}

	public void setIdleBaseRate(OptionalDouble idleBaseRate) {
		this.idleBaseRate = idleBaseRate;
	}

	public OptionalDouble getServiceSpeed() {
		return serviceSpeed;
	}

	public void setServiceSpeed(OptionalDouble serviceSped) {
		this.serviceSpeed = serviceSped;
	}

	public Optional<List<FuelConsumption>> getFuelConsumption() {
		return fuelConsumption;
	}

	public void setFuelConsumption(Optional<List<FuelConsumption>> fuelConsumption) {
		this.fuelConsumption = fuelConsumption;
	}
}
