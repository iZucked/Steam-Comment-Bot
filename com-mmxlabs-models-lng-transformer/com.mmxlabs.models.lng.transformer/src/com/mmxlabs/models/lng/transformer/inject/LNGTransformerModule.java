/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2011
 * All rights reserved.
 */
package com.mmxlabs.models.lng.transformer.inject;

import com.google.inject.AbstractModule;
import com.mmxlabs.common.CollectionsUtil;
import com.mmxlabs.models.lng.commercial.CommercialPackage;
import com.mmxlabs.models.lng.transformer.LNGScenarioTransformer;
import com.mmxlabs.models.lng.transformer.ModelEntityMap;
import com.mmxlabs.models.lng.transformer.ResourcelessModelEntityMap;
import com.mmxlabs.models.lng.transformer.contracts.IContractTransformer;
import com.mmxlabs.models.lng.transformer.contracts.SimpleContractTransformer;
import com.mmxlabs.models.mmxcore.MMXRootObject;

/**
 * Main entry point to create {@link LNGScenarioTransformer}. This uses injection to populate the data structures.
 */
public class LNGTransformerModule extends AbstractModule {

	private final MMXRootObject scenario;

	public LNGTransformerModule(final MMXRootObject scenario) {
		this.scenario = scenario;
	}

	@Override
	protected void configure() {
		install(new ScheduleBuilderModule());

		final LNGScenarioTransformer lngScenarioTransformer = new LNGScenarioTransformer(scenario);
		if (!lngScenarioTransformer.addPlatformTransformerExtensions()) {
			IContractTransformer sct = new SimpleContractTransformer();
			lngScenarioTransformer.addTransformerExtension(sct);
			lngScenarioTransformer.addContractTransformer(sct, CollectionsUtil.makeArrayList(CommercialPackage.eINSTANCE.getFixedPriceContract(),
					CommercialPackage.eINSTANCE.getIndexPriceContract()));
		}

		bind(LNGScenarioTransformer.class).toInstance(lngScenarioTransformer);

		bind(ModelEntityMap.class).to(ResourcelessModelEntityMap.class);
	}

}
