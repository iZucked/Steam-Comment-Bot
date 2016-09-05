/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2015
 * All rights reserved.
 */
package com.mmxlabs.models.lng.adp.internal;

import org.ops4j.peaberry.activation.util.PeaberryActivationModule;

import com.mmxlabs.models.lng.adp.ext.impl.CargoByQuarterFactory;
import com.mmxlabs.models.lng.adp.ext.impl.CargoByQuarterGenerator;
import com.mmxlabs.models.lng.adp.ext.impl.CargoIntervalFactory;
import com.mmxlabs.models.lng.adp.ext.impl.CargoIntervalGenerator;
import com.mmxlabs.models.lng.adp.ext.impl.CargoNumberFactory;
import com.mmxlabs.models.lng.adp.ext.impl.CargoNumberGenerator;
import com.mmxlabs.models.lng.adp.ext.impl.CargoSizeFactory;
import com.mmxlabs.models.lng.adp.ext.impl.CargoSizeGenerator;
import com.mmxlabs.models.lng.adp.ext.impl.DeliverToProfileFlowFactory;
import com.mmxlabs.models.lng.adp.ext.impl.DeliverToSpotFlowFactory;
import com.mmxlabs.models.lng.adp.ext.impl.SupplyFromProfileFlowFactory;
import com.mmxlabs.models.lng.adp.ext.impl.SupplyFromSpotFlowFactory;

public class ADPActivatorModule extends PeaberryActivationModule {

	@Override
	protected void configure() {

		bindService(CargoNumberGenerator.class).export();
		bindService(CargoSizeGenerator.class).export();
		bindService(CargoByQuarterGenerator.class).export();
		bindService(CargoIntervalGenerator.class).export();
		
		bindService(CargoNumberFactory.class).export();
		bindService(CargoSizeFactory.class).export();
		bindService(CargoByQuarterFactory.class).export();
		bindService(CargoIntervalFactory.class).export();
		
		bindService(SupplyFromProfileFlowFactory.class).export();
		bindService(SupplyFromSpotFlowFactory.class).export();
		bindService(DeliverToProfileFlowFactory.class).export();
		bindService(DeliverToSpotFlowFactory.class).export();

	}
}
