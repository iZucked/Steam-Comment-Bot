/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
package com.mmxlabs.models.lng.commercial.util;

import java.time.LocalDate;
import java.util.Collection;

import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.jdt.annotation.NonNull;

import com.mmxlabs.models.lng.commercial.BaseEntityBook;
import com.mmxlabs.models.lng.commercial.BaseLegalEntity;
import com.mmxlabs.models.lng.commercial.CommercialFactory;
import com.mmxlabs.models.lng.commercial.CommercialModel;
import com.mmxlabs.models.lng.commercial.ExpressionPriceParameters;
import com.mmxlabs.models.lng.commercial.LegalEntity;
import com.mmxlabs.models.lng.commercial.LumpSumBallastBonusContractLine;
import com.mmxlabs.models.lng.commercial.NotionalJourneyBallastBonusContractLine;
import com.mmxlabs.models.lng.commercial.PurchaseContract;
import com.mmxlabs.models.lng.commercial.RuleBasedBallastBonusContract;
import com.mmxlabs.models.lng.commercial.SalesContract;
import com.mmxlabs.models.lng.commercial.TaxRate;
import com.mmxlabs.models.lng.port.Port;
import com.mmxlabs.models.lng.types.APortSet;

public class CommercialModelBuilder {

	private final @NonNull CommercialModel commercialModel;

	public CommercialModelBuilder(@NonNull final CommercialModel commercialModel) {
		this.commercialModel = commercialModel;
	}

	public @NonNull LegalEntity makeLegalEntityAndBooks(@NonNull final String name) {
		final LegalEntity entity = CommercialFactory.eINSTANCE.createLegalEntity();

		entity.setShippingBook(CommercialFactory.eINSTANCE.createSimpleEntityBook());

		entity.setTradingBook(CommercialFactory.eINSTANCE.createSimpleEntityBook());

		entity.setUpstreamBook(CommercialFactory.eINSTANCE.createSimpleEntityBook());

		commercialModel.getEntities().add(entity);

		return entity;
	}

	public @NonNull SalesContract makeExpressionSalesContract(@NonNull final String name, @NonNull final BaseLegalEntity entity, @NonNull final String expression) {
		final SalesContract contract = CommercialFactory.eINSTANCE.createSalesContract();
		final ExpressionPriceParameters params = CommercialFactory.eINSTANCE.createExpressionPriceParameters();
		contract.setName(name);

		contract.setEntity(entity);
		params.setPriceExpression(expression);

		contract.setPriceInfo(params);
		commercialModel.getSalesContracts().add(contract);

		return contract;
	}

	public @NonNull PurchaseContract makeExpressionPurchaseContract(@NonNull final String name, @NonNull final BaseLegalEntity entity, @NonNull final String expression) {
		final PurchaseContract contract = CommercialFactory.eINSTANCE.createPurchaseContract();
		final ExpressionPriceParameters params = CommercialFactory.eINSTANCE.createExpressionPriceParameters();
		contract.setName(name);

		contract.setEntity(entity);
		params.setPriceExpression(expression);

		contract.setPriceInfo(params);
		commercialModel.getPurchaseContracts().add(contract);

		return contract;
	}

	public @NonNull TaxRate createTaxRate(@NonNull final LocalDate date, final float taxRate) {

		// Check sensible bounds.
		assert taxRate >= 0.0f;
		assert taxRate <= 1.0f;

		final TaxRate rate = CommercialFactory.eINSTANCE.createTaxRate();

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
	public void setTaxRate(final BaseEntityBook book, @NonNull final TaxRate rate) {
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
	public void setTaxRates(@NonNull final BaseLegalEntity entity, @NonNull final TaxRate rate) {
		setTaxRate(entity.getTradingBook(), EcoreUtil.copy(rate));
		setTaxRate(entity.getShippingBook(), EcoreUtil.copy(rate));
		setTaxRate(entity.getUpstreamBook(), EcoreUtil.copy(rate));
	}

	public @NonNull RuleBasedBallastBonusContract createSimpleLumpSumBallastBonusContract(@NonNull final Port redeliveryPort, @NonNull final String priceExpression) {

		final LumpSumBallastBonusContractLine lumpSumBallastBonusContractLine = CommercialFactory.eINSTANCE.createLumpSumBallastBonusContractLine();
		lumpSumBallastBonusContractLine.getRedeliveryPorts().add(redeliveryPort);
		lumpSumBallastBonusContractLine.setPriceExpression(priceExpression);

		final RuleBasedBallastBonusContract ballastBonusContract = CommercialFactory.eINSTANCE.createRuleBasedBallastBonusContract();
		ballastBonusContract.getRules().add(lumpSumBallastBonusContractLine);

		return ballastBonusContract;
	}

	public @NonNull RuleBasedBallastBonusContract createSimpleNotionalJourneyBallastBonusContract(final @NonNull Collection<@NonNull APortSet<Port>> redeliveryPorts, final double speed,
			final @NonNull String hireExpression, final @NonNull String fuelExpression, final boolean includeCanal, final @NonNull Collection<@NonNull APortSet<Port>> returnPorts) {

		final NotionalJourneyBallastBonusContractLine notionalJourneyBallastBonusContractLine = CommercialFactory.eINSTANCE.createNotionalJourneyBallastBonusContractLine();
		notionalJourneyBallastBonusContractLine.getRedeliveryPorts().addAll(redeliveryPorts);
		notionalJourneyBallastBonusContractLine.getReturnPorts().addAll(returnPorts);
		notionalJourneyBallastBonusContractLine.setFuelPriceExpression(fuelExpression);
		notionalJourneyBallastBonusContractLine.setHirePriceExpression(hireExpression);
		notionalJourneyBallastBonusContractLine.setIncludeCanal(includeCanal);
		notionalJourneyBallastBonusContractLine.setSpeed(speed);

		final RuleBasedBallastBonusContract ballastBonusContract = CommercialFactory.eINSTANCE.createRuleBasedBallastBonusContract();
		ballastBonusContract.getRules().add(notionalJourneyBallastBonusContractLine);

		return ballastBonusContract;
	}
}
