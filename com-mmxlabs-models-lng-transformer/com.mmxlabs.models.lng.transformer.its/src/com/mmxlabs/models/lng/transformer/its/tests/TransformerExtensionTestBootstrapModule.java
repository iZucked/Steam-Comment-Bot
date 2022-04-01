/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2022
 * All rights reserved.
 */
package com.mmxlabs.models.lng.transformer.its.tests;

import java.util.List;

import org.eclipse.core.runtime.Platform;
import org.ops4j.peaberry.util.TypeLiterals;

import com.google.common.collect.Lists;
import com.google.inject.AbstractModule;
import com.mmxlabs.models.lng.transformer.extensions.panamaslots.PanamaSlotsModule;
import com.mmxlabs.models.lng.transformer.extensions.portshipsizeconstraint.PortShipSizeModule;
import com.mmxlabs.models.lng.transformer.extensions.restrictedelements.RestrictedElementsModule;
import com.mmxlabs.models.lng.transformer.extensions.shippingtype.ShippingTypeRequirementModule;
import com.mmxlabs.scheduler.optimiser.peaberry.IOptimiserInjectorService;

/**
 * Module to "bootstrap" the LNG Transformer framework when running outside of OSGi (i.e. in a JUnit test environment)
 * 
 * @author Simon Goodall
 *
 */
public class TransformerExtensionTestBootstrapModule extends AbstractModule {

	@Override
	protected void configure() {

		if (!Platform.isRunning()) {
			final List<IOptimiserInjectorService> injectorServices = Lists.<IOptimiserInjectorService> newArrayList(//
					new ShippingTypeRequirementModule.DesPermissionInjectorService(), //
					new RestrictedElementsModule.RestrictedElementsInjectorService(), //
					new PortShipSizeModule.PortShipSizeInjectorService(),
					new PanamaSlotsModule.PanamaSlotsInjectorService(), //
					createTradingInjectorService());

			bind(TypeLiterals.iterable(IOptimiserInjectorService.class)).toInstance(injectorServices);
		}

	}

	private IOptimiserInjectorService createTradingInjectorService() {
		return new TransformerITSOptimiserInjectorService();
	}
}
