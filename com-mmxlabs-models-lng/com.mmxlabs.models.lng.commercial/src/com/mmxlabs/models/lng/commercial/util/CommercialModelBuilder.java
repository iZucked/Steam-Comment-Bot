/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
package com.mmxlabs.models.lng.commercial.util;

import org.eclipse.jdt.annotation.NonNull;

import com.mmxlabs.models.lng.commercial.CommercialFactory;
import com.mmxlabs.models.lng.commercial.CommercialModel;
import com.mmxlabs.models.lng.commercial.ExpressionPriceParameters;
import com.mmxlabs.models.lng.commercial.LegalEntity;
import com.mmxlabs.models.lng.commercial.PurchaseContract;
import com.mmxlabs.models.lng.commercial.SalesContract;

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

	public @NonNull SalesContract makeExpressionSalesContract(@NonNull String name, @NonNull LegalEntity entity, @NonNull String expression) {
		final SalesContract contract = CommercialFactory.eINSTANCE.createSalesContract();
		final ExpressionPriceParameters params = CommercialFactory.eINSTANCE.createExpressionPriceParameters();
		contract.setName(name);

		contract.setEntity(entity);
		params.setPriceExpression(expression);

		contract.setPriceInfo(params);
		commercialModel.getSalesContracts().add(contract);

		return contract;
	}

	public @NonNull PurchaseContract makeExpressionPurchaseContract(@NonNull String name, @NonNull LegalEntity entity, @NonNull String expression) {
		final PurchaseContract contract = CommercialFactory.eINSTANCE.createPurchaseContract();
		final ExpressionPriceParameters params = CommercialFactory.eINSTANCE.createExpressionPriceParameters();
		contract.setName(name);

		contract.setEntity(entity);
		params.setPriceExpression(expression);

		contract.setPriceInfo(params);
		commercialModel.getPurchaseContracts().add(contract);

		return contract;
	}
}
