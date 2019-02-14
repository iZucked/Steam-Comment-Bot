/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2019
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.voyage;

import org.eclipse.jdt.annotation.NonNullByDefault;

import com.mmxlabs.scheduler.optimiser.components.IBaseFuel;

@NonNullByDefault
public final class LNGFuelKeys {
	private LNGFuelKeys() {

	}

	public static final FuelKey Cooldown_In_m3 = new FuelKey(FuelComponent.Cooldown, FuelUnit.M3, IBaseFuel.LNG);

	public static final FuelKey NBO_In_m3 = new FuelKey(FuelComponent.NBO, FuelUnit.M3, IBaseFuel.LNG);
	public static final FuelKey NBO_In_mmBtu = new FuelKey(FuelComponent.NBO, FuelUnit.MMBTu, IBaseFuel.LNG);
	public static final FuelKey NBO_In_MT = new FuelKey(FuelComponent.NBO, FuelUnit.MT, IBaseFuel.LNG);

	public static final FuelKey FBO_In_m3 = new FuelKey(FuelComponent.FBO, FuelUnit.M3, IBaseFuel.LNG);
	public static final FuelKey FBO_In_mmBtu = new FuelKey(FuelComponent.FBO, FuelUnit.MMBTu, IBaseFuel.LNG);
	public static final FuelKey FBO_In_MT = new FuelKey(FuelComponent.FBO, FuelUnit.MT, IBaseFuel.LNG);

	public static final FuelKey IdleNBO_In_m3 = new FuelKey(FuelComponent.IdleNBO, FuelUnit.M3, IBaseFuel.LNG);
	public static final FuelKey IdleNBO_In_mmBtu = new FuelKey(FuelComponent.IdleNBO, FuelUnit.MMBTu, IBaseFuel.LNG);
	public static final FuelKey IdleNBO_In_MT = new FuelKey(FuelComponent.IdleNBO, FuelUnit.MT, IBaseFuel.LNG);

	public static final FuelKey[] LNG_In_m3 = { NBO_In_m3, FBO_In_m3, IdleNBO_In_m3 };
	public static final FuelKey[] LNG_In_mmBtu = { NBO_In_mmBtu, FBO_In_mmBtu, IdleNBO_In_mmBtu };

	public static final FuelKey[][] LNG_In_m3_mmBtu_Pair = { //
			{ NBO_In_m3, NBO_In_mmBtu }, //
			{ FBO_In_m3, FBO_In_mmBtu }, //
			{ IdleNBO_In_m3, IdleNBO_In_mmBtu }//
	};
	public static final FuelKey[][] Travel_LNG_In_m3_mmBtu_Pair = { //
			{ NBO_In_m3, NBO_In_mmBtu }, //
			{ FBO_In_m3, FBO_In_mmBtu } //
	};
	public static final FuelKey[] Travel_LNG = { //
			NBO_In_m3, NBO_In_mmBtu, NBO_In_MT, //
			FBO_In_m3, FBO_In_mmBtu, FBO_In_MT //
	};
	public static final FuelKey[] Idle_LNG = { //
			IdleNBO_In_m3, IdleNBO_In_mmBtu, IdleNBO_In_MT, //
	};
	public static final FuelKey[] Travel_LNG_In_m3 = { NBO_In_m3, FBO_In_m3 };

	public static final FuelKey[][] Idle_LNG_In_m3_mmBtu_Pair = { //
			{ IdleNBO_In_m3, IdleNBO_In_mmBtu } //
	};

}
