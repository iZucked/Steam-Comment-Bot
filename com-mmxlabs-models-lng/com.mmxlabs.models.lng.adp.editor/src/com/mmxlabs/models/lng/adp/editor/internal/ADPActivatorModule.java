/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
package com.mmxlabs.models.lng.adp.editor.internal;

import org.ops4j.peaberry.activation.util.PeaberryActivationModule;

import com.mmxlabs.models.lng.adp.ext.impl.CargoIntervalFactory;
import com.mmxlabs.models.lng.adp.ext.impl.CargoIntervalGenerator;
import com.mmxlabs.models.lng.adp.ext.impl.CargoNumberFactory;
import com.mmxlabs.models.lng.adp.ext.impl.CargoNumberGenerator;
import com.mmxlabs.models.lng.adp.ext.impl.CargoSizeFactory;
import com.mmxlabs.models.lng.adp.ext.impl.CargoSizeGenerator;
import com.mmxlabs.models.lng.adp.ext.impl.PeriodDistributionProfileConstraintFactory;
import com.mmxlabs.models.lng.adp.ext.impl.PreDefinedSlotFactory;
import com.mmxlabs.models.lng.adp.ext.impl.PreDefinedSlotGenerator;
import com.mmxlabs.models.lng.adp.ext.impl.TargetCargoOnVesselProfileConstraintFactory;
import com.mmxlabs.models.lng.adp.ext.impl.VesselRestrictionFactory;

public class ADPActivatorModule extends PeaberryActivationModule {

	@Override
	protected void configure() {

		// Distribution models and generators
		bindService(CargoNumberFactory.class).export();
		bindService(CargoNumberGenerator.class).export();
		bindService(CargoSizeFactory.class).export();
		bindService(CargoSizeGenerator.class).export();
		// bindService(CargoByQuarterFactory.class).export();
		// bindService(CargoByQuarterGenerator.class).export();
		bindService(CargoIntervalFactory.class).export();
		bindService(CargoIntervalGenerator.class).export();
		bindService(PreDefinedSlotFactory.class).export();
		bindService(PreDefinedSlotGenerator.class).export();

		// Profile Constraints
//		bindService(MinCargoProfileConstraintFactory.class).export();
//		bindService(MaxCargoProfileConstraintFactory.class).export();
		bindService(PeriodDistributionProfileConstraintFactory.class).export();

		// Sub Profile Constraints
		bindService(VesselRestrictionFactory.class).export();
		// bindService(SupplyFromProfileFlowFactory.class).export();
		// bindService(SupplyFromSpotFlowFactory.class).export();
		// bindService(DeliverToProfileFlowFactory.class).export();
		// bindService(DeliverToSpotFlowFactory.class).export();

		// Fleet Profile Constraints
		bindService(TargetCargoOnVesselProfileConstraintFactory.class).export();
	}
}
