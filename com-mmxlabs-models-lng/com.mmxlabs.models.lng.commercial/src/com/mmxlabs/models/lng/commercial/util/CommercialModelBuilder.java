/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
package com.mmxlabs.models.lng.commercial.util;

import java.time.LocalDate;

import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.jdt.annotation.NonNull;

import com.mmxlabs.models.lng.commercial.BaseEntityBook;
import com.mmxlabs.models.lng.commercial.BaseLegalEntity;
import com.mmxlabs.models.lng.commercial.CommercialFactory;
import com.mmxlabs.models.lng.commercial.CommercialModel;
import com.mmxlabs.models.lng.commercial.ExpressionPriceParameters;
import com.mmxlabs.models.lng.commercial.LegalEntity;
import com.mmxlabs.models.lng.commercial.PurchaseContract;
import com.mmxlabs.models.lng.commercial.SalesContract;
import com.mmxlabs.models.lng.commercial.TaxRate;

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

	public @NonNull SalesContract makeExpressionSalesContract(@NonNull String name, @NonNull BaseLegalEntity entity, @NonNull String expression) {
		final SalesContract contract = CommercialFactory.eINSTANCE.createSalesContract();
		final ExpressionPriceParameters params = CommercialFactory.eINSTANCE.createExpressionPriceParameters();
		contract.setName(name);

		contract.setEntity(entity);
		params.setPriceExpression(expression);

		contract.setPriceInfo(params);
		commercialModel.getSalesContracts().add(contract);

		return contract;
	}

	public @NonNull PurchaseContract makeExpressionPurchaseContract(@NonNull String name, @NonNull BaseLegalEntity entity, @NonNull String expression) {
		final PurchaseContract contract = CommercialFactory.eINSTANCE.createPurchaseContract();
		final ExpressionPriceParameters params = CommercialFactory.eINSTANCE.createExpressionPriceParameters();
		contract.setName(name);

		contract.setEntity(entity);
		params.setPriceExpression(expression);

		contract.setPriceInfo(params);
		commercialModel.getPurchaseContracts().add(contract);

		return contract;
	}

	public @NonNull TaxRate createTaxRate(@NonNull LocalDate date, float taxRate) {

		// Check sensible bounds.
		assert taxRate >= 0.0f;
		assert taxRate <= 1.0f;

		TaxRate rate = CommercialFactory.eINSTANCE.createTaxRate();

		rate.setDate(date);
		rate.setValue(taxRate);

		return rate;
	}

	/**
	 * Replaces existing tax rates with the new entry
	 * 
	 * @param book
	 * @param rate
	 */
	public void setTaxRate(BaseEntityBook book, @NonNull TaxRate rate) {
		if (book == null) {
			throw new NullPointerException();
		}
		book.getTaxRates().clear();
		book.getTaxRates().add(rate);
	}

	/**
	 * Replaces existing tax rates with a copy new entry for the standard entity books.
	 * 
	 * @param book
	 * @param rate
	 */
	public void setTaxRates(@NonNull BaseLegalEntity entity, @NonNull TaxRate rate) {
		setTaxRate(entity.getTradingBook(), EcoreUtil.copy(rate));
		setTaxRate(entity.getShippingBook(), EcoreUtil.copy(rate));
		setTaxRate(entity.getUpstreamBook(), EcoreUtil.copy(rate));
	}
}
