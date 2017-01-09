/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.voyage;

import org.eclipse.jdt.annotation.NonNull;

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

	private final @NonNull FuelUnit fuelUnit;
	private final @NonNull FuelUnit pricingFuelUnit;

	private FuelComponent(final @NonNull FuelUnit fuelUnit, final @NonNull FuelUnit pricingFuelUnit) {
		this.fuelUnit = fuelUnit;
		this.pricingFuelUnit = pricingFuelUnit;
	}

	/**
	 * Returns the default unit of measure for this {@link FuelComponent}.
	 * 
	 * @return
	 */
	public final @NonNull FuelUnit getDefaultFuelUnit() {
		return fuelUnit;
	}

	private static final @NonNull FuelComponent @NonNull [] travelFuelComponents = new @NonNull FuelComponent @NonNull [] { Base, NBO, Base_Supplemental, FBO, FuelComponent.PilotLight };

	public static @NonNull FuelComponent @NonNull [] getTravelFuelComponents() {
		return travelFuelComponents;
	}

	private static final @NonNull FuelComponent @NonNull [] idleFuelComponents = new @NonNull FuelComponent @NonNull [] { FuelComponent.IdleBase, FuelComponent.IdleNBO, FuelComponent.IdlePilotLight };

	public static @NonNull FuelComponent @NonNull [] getIdleFuelComponents() {
		return idleFuelComponents;
	}

	private static final @NonNull FuelComponent @NonNull [] baseFuelComponents = new @NonNull FuelComponent @NonNull [] { Base, Base_Supplemental, IdleBase, PilotLight, IdlePilotLight };

	public static @NonNull FuelComponent @NonNull [] getBaseFuelComponents() {
		return baseFuelComponents;
	}

	private static final @NonNull FuelComponent @NonNull [] lngFuelComponents = new @NonNull FuelComponent @NonNull [] { NBO, FBO, IdleNBO };

	public static @NonNull FuelComponent @NonNull [] getLNGFuelComponents() {
		return lngFuelComponents;
	}

	private static final @NonNull FuelComponent @NonNull [] baseNoPilotFuelComponents = new @NonNull FuelComponent @NonNull [] { Base, Base_Supplemental, IdleBase };

	/**
	 */
	public static @NonNull FuelComponent @NonNull [] getBaseFuelComponentsNoPilot() {
		return baseNoPilotFuelComponents;
	}

	private static final @NonNull FuelComponent @NonNull [] pilotLightFuelComponents = new @NonNull FuelComponent @NonNull [] { PilotLight, IdlePilotLight };

	/**
	 */
	public static @NonNull FuelComponent @NonNull [] getPilotLightFuelComponents() {
		return pilotLightFuelComponents;
	}

	/**
	 * Returns true of the given {@link FuelComponent} is a type of LNG
	 * 
	 */
	public static boolean isLNGFuelComponent(final @NonNull FuelComponent fc) {
		if (fc == FuelComponent.NBO || fc == FuelComponent.FBO || fc == FuelComponent.IdleNBO) {
			return true;
		}
		return false;
	}

	/**
	 */
	public @NonNull FuelUnit getPricingFuelUnit() {
		return pricingFuelUnit;
	}

}
