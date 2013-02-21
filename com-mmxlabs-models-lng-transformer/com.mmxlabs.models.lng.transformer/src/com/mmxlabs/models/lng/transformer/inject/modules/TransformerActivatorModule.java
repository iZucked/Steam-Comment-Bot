/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2013
 * All rights reserved.
 */
package com.mmxlabs.models.lng.transformer.inject.modules;

import org.ops4j.peaberry.activation.util.PeaberryActivationModule;

import com.mmxlabs.models.lng.transformer.extensions.entities.EntityTransformerExtensionFactory;
import com.mmxlabs.models.lng.transformer.extensions.restrictedelements.RestrictedElementsModule;
import com.mmxlabs.models.lng.transformer.extensions.restrictedelements.RestrictedElementsTransformerFactory;
import com.mmxlabs.models.lng.transformer.extensions.simplecontracts.SimpleContractTransformerFactory;
import com.mmxlabs.models.lng.transformer.extensions.tradingexporter.TradingExporterExtensionFactory;
import com.mmxlabs.scheduler.optimiser.fitness.components.pnl.ProfitAndLossAllocationComponentProvider;

/**
 * Module to register Transformer extension factories as a service
 * 
 * @author Simon Goodall
 * @since 2.0
 * 
 */
public class TransformerActivatorModule extends PeaberryActivationModule {

	@Override
	protected void configure() {
		install(new RestrictedElementsModule());

		bindService(SimpleContractTransformerFactory.class).export();
		bindService(RestrictedElementsTransformerFactory.class).export();
		
		bindService(ProfitAndLossAllocationComponentProvider.class).export();

		bindService(EntityTransformerExtensionFactory.class).export();
		bindService(TradingExporterExtensionFactory.class).export();
	}

}
