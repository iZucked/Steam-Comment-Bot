/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2013
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.voyage;

/**
 * Enum describing the possible breakdowns of fuel components. Each component represents a specific calculation required in a voyage calculation.
 * 
 * @author Simon Goodall TODO: Unit for NBO etc could also be MMBTu - especially for discharge pricing!
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
	IdleBase(FuelUnit.MT),

	/**
	 * Some vessels require a pilot light when running on base fuel only. This tracks use during travel times.
	 */
	PilotLight(FuelUnit.MT),

	/**
	 * Some vessels require a pilot light when running on base fuel only. This tracks use during idle times.
	 */
	IdlePilotLight(FuelUnit.MT),

	/**
	 * Gas was purchased from the port for cooldown.
	 */
	Cooldown(FuelUnit.M3);

	private final FuelUnit fuelUnit;

	private FuelComponent(final FuelUnit fuelUnit) {
		this.fuelUnit = fuelUnit;
	}

	/**
	 * Returns the default unit of measure for this {@link FuelComponent}.
	 * 
	 * @return
	 */
	public final FuelUnit getDefaultFuelUnit() {
		return fuelUnit;
	}

	private static final FuelComponent[] travelFuelComponents = new FuelComponent[] { Base, NBO, Base_Supplemental, FBO, FuelComponent.PilotLight };

	public static FuelComponent[] getTravelFuelComponents() {
		return travelFuelComponents;
	}

	private static final FuelComponent[] idleFuelComponents = new FuelComponent[] { FuelComponent.IdleBase, FuelComponent.IdleNBO, FuelComponent.Cooldown, FuelComponent.IdlePilotLight };

	public static FuelComponent[] getIdleFuelComponents() {
		return idleFuelComponents;
	}

	private static final FuelComponent[] baseFuelComponents = new FuelComponent[] { Base, Base_Supplemental, IdleBase, PilotLight, IdlePilotLight };

	public static FuelComponent[] getBaseFuelComponents() {
		return baseFuelComponents;
	}

	private static final FuelComponent[] lngFuelComponents = new FuelComponent[] { NBO, FBO, IdleNBO };

	public static FuelComponent[] getLNGFuelComponents() {
		return lngFuelComponents;
	}

	private static final FuelComponent[] baseNoPilotFuelComponents = new FuelComponent[] { Base, Base_Supplemental, IdleBase };

	/**
	 * @since 2.0
	 */
	public static FuelComponent[] getBaseFuelComponentsNoPilot() {
		return baseNoPilotFuelComponents;
	}

	private static final FuelComponent[] pilotLightFuelComponents = new FuelComponent[] { PilotLight, IdlePilotLight };

	/**
	 * @since 2.0
	 */
	public static FuelComponent[] getPilotLightFuelComponents() {
		return pilotLightFuelComponents;
	}

}
