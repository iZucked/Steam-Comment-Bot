/**
 * Copyright (C) Minimax Labs Ltd., 2010
 * All rights reserved.
 */

package com.mmxlabs.scheduler.optimiser.voyage;

/**
 * Enum describing the possible breakdowns of fuel components. Each component
 * represents a specific calculation required in a voyage calculation.
 * 
 * @author Simon Goodall
 * TODO: Unit for NBO etc could also be MMBTu - especially for discharge pricing!
 */
public enum FuelComponent {

	/**
	 * Voyage used base fuel as main fuel source.
	 */
	Base(FuelUnit.MT),

	/**
	 * Voyage used NBO as main fuel source
	 */
	NBO(FuelUnit.M3),

	/**
	 * NBO Voyage is supplemented by FBO.
	 */
	FBO(FuelUnit.M3),

	/**
	 * NBO Voyage is supplemented by base fuel.
	 */
	Base_Supplemental(FuelUnit.MT),

	/**
	 * Idle time NBO use
	 */
	IdleNBO(FuelUnit.M3),

	/**
	 * Idle time base fuel use
	 */
	IdleBase(FuelUnit.MT);

	private final FuelUnit fuelUnit;

	private FuelComponent(final FuelUnit fuelUnit) {
		this.fuelUnit = fuelUnit;
	}

	/**
	 * Returns the default unit of measure for this {@link FuelComponent}.
	 * @return
	 */
	public final FuelUnit getDefaultFuelUnit() {
		return fuelUnit;
	}
	
	private static final FuelComponent[] travelFuelComponents = new FuelComponent[] {
			Base, NBO,
			Base_Supplemental, FBO };
	
	public static FuelComponent[] getTravelFuelComponents() {
		return travelFuelComponents;
	}
	
	private static final FuelComponent[] idleFuelComponents = new FuelComponent[] {
			FuelComponent.IdleBase, FuelComponent.IdleNBO };
	
	public static FuelComponent[] getIdleFuelComponents() {
		return idleFuelComponents;
	}
}
