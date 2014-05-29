/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2014
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
	Base(FuelUnit.MT, FuelUnit.MT),

	/**
	 * Voyage used NBO as main fuel source
	 */
	NBO(FuelUnit.M3, FuelUnit.MMBTu),

	/**
	 * NBO Voyage is supplemented by FBO.
	 */
	FBO(FuelUnit.M3, FuelUnit.MMBTu),

	/**
	 * NBO Voyage is supplemented by base fuel.
	 */
	Base_Supplemental(FuelUnit.MT, FuelUnit.MT),

	/**
	 * Idle time NBO use
	 */
	IdleNBO(FuelUnit.M3, FuelUnit.MMBTu),

	/**
	 * Idle time base fuel use
	 */
	IdleBase(FuelUnit.MT, FuelUnit.MT),

	/**
	 * Some vessels require a pilot light when running on base fuel only. This tracks use during travel times.
	 */
	PilotLight(FuelUnit.MT, FuelUnit.MT),

	/**
	 * Some vessels require a pilot light when running on base fuel only. This tracks use during idle times.
	 */
	IdlePilotLight(FuelUnit.MT, FuelUnit.MT),

	/**
	 * Gas was purchased from the port for cooldown.
	 */
	Cooldown(FuelUnit.M3, FuelUnit.MMBTu);

	private final FuelUnit fuelUnit;
	private final FuelUnit pricingFuelUnit;

	private FuelComponent(final FuelUnit fuelUnit, final FuelUnit pricingFuelUnit) {
		this.fuelUnit = fuelUnit;
		this.pricingFuelUnit = pricingFuelUnit;
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
	 */
	public static FuelComponent[] getBaseFuelComponentsNoPilot() {
		return baseNoPilotFuelComponents;
	}

	private static final FuelComponent[] pilotLightFuelComponents = new FuelComponent[] { PilotLight, IdlePilotLight };

	/**
	 */
	public static FuelComponent[] getPilotLightFuelComponents() {
		return pilotLightFuelComponents;
	}

	/**
	 * Returns true of the given {@link FuelComponent} is a type of LNG
	 * 
	 */
	public static boolean isLNGFuelComponent(final FuelComponent fc) {
		if (fc == FuelComponent.NBO || fc == FuelComponent.FBO || fc == FuelComponent.IdleNBO) {
			return true;
		}
		return false;
	}

	/**
	 */
	public FuelUnit getPricingFuelUnit() {
		return pricingFuelUnit;
	}

}
