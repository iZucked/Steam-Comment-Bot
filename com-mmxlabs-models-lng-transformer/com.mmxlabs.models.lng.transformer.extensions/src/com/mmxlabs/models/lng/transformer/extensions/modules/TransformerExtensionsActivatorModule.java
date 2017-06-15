/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
package com.mmxlabs.models.lng.transformer.extensions.modules;

import org.ops4j.peaberry.activation.util.PeaberryActivationModule;

import com.mmxlabs.models.lng.transformer.extensions.actuals.ActualsTransformerFactory;
import com.mmxlabs.models.lng.transformer.extensions.contracts.ballastbonus.BallastBonusContractTransformerFactory;
import com.mmxlabs.models.lng.transformer.extensions.contracts.ballastbonus.BallastBonusExporterExtensionFactory;
import com.mmxlabs.models.lng.transformer.extensions.entities.EntityTransformerExtensionFactory;
import com.mmxlabs.models.lng.transformer.extensions.panamaslots.PanamaSlotsModule;
import com.mmxlabs.models.lng.transformer.extensions.restrictedelements.RestrictedElementsModule;
import com.mmxlabs.models.lng.transformer.extensions.shippingtype.ShippingTypeRequirementModule;
import com.mmxlabs.models.lng.transformer.extensions.simplecontracts.SimpleContractTransformerFactory;
import com.mmxlabs.models.lng.transformer.extensions.tradingexporter.BasicSlotPNLExporterExtensionFactory;
import com.mmxlabs.models.lng.transformer.extensions.tradingexporter.TradingExporterExtensionFactory;
import com.mmxlabs.models.lng.transformer.extensions.vesselavailabilities.VesselAvailabilityEntityTransformer;
import com.mmxlabs.models.lng.transformer.extensions.vesselavailabilities.VesselAvailabilityEntityTransformerExtensionFactory;

/**
 * Module to register Transformer extension factories as a service
 * 
 * @author Simon Goodall
 * 
 */
public class TransformerExtensionsActivatorModule extends PeaberryActivationModule {

	@Override
	protected void configure() {
		install(new RestrictedElementsModule());
		install(new PanamaSlotsModule());
		install(new ShippingTypeRequirementModule());

		bindService(SimpleContractTransformerFactory.class).export();
		bindService(BallastBonusContractTransformerFactory.class).export();

		bindService(EntityTransformerExtensionFactory.class).export();
		bindService(TradingExporterExtensionFactory.class).export();
		bindService(BasicSlotPNLExporterExtensionFactory.class).export();
		bindService(BallastBonusExporterExtensionFactory.class).export();
		
		bindService(ActualsTransformerFactory.class).export();
		
		bindService(VesselAvailabilityEntityTransformerExtensionFactory.class).export();
	}

}
