/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
package com.mmxlabs.models.lng.commercial.util;

import org.eclipse.jdt.annotation.NonNull;

import com.mmxlabs.models.lng.commercial.CommercialFactory;
import com.mmxlabs.models.lng.commercial.CommercialModel;
import com.mmxlabs.models.lng.commercial.LegalEntity;

public class CommercialModelBuilder {

	private final @NonNull CommercialModel commercialModel;

	public CommercialModelBuilder(@NonNull final CommercialModel commercialModel) {
		this.commercialModel = commercialModel;
	}

	public @NonNull LegalEntity makeLegalEntityAndBooks(@NonNull String name) {
		final LegalEntity entity = CommercialFactory.eINSTANCE.createLegalEntity();

		entity.setShippingBook(CommercialFactory.eINSTANCE.createSimpleEntityBook());

		entity.setTradingBook(CommercialFactory.eINSTANCE.createSimpleEntityBook());

		entity.setUpstreamBook(CommercialFactory.eINSTANCE.createSimpleEntityBook());

		commercialModel.getEntities().add(entity);

		return entity;
	}
}
