/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2012
 * All rights reserved.
 */
package com.mmxlabs.lngscheduler.emf.extras.contracts;

import java.util.Arrays;
import java.util.Collection;

import org.eclipse.emf.ecore.EClass;

import scenario.Scenario;
import scenario.cargo.Slot;
import scenario.contract.ContractPackage;
import scenario.contract.FixedPricePurchaseContract;
import scenario.contract.IndexPricePurchaseContract;
import scenario.contract.NetbackPurchaseContract;
import scenario.contract.ProfitSharingPurchaseContract;
import scenario.contract.PurchaseContract;
import scenario.contract.SalesContract;

import com.mmxlabs.common.curves.ICurve;
import com.mmxlabs.lngscheduler.emf.extras.ModelEntityMap;
import com.mmxlabs.optimiser.core.ISequences;
import com.mmxlabs.scheduler.optimiser.Calculator;
import com.mmxlabs.scheduler.optimiser.builder.ISchedulerBuilder;
import com.mmxlabs.scheduler.optimiser.components.IPortSlot;
import com.mmxlabs.scheduler.optimiser.contracts.ILoadPriceCalculator2;
import com.mmxlabs.scheduler.optimiser.contracts.IShippingPriceCalculator;

/**
 * 
 * 
 * @author hinton
 * 
 */
public class SimpleContractTransformer implements IContractTransformer {

	private ISchedulerBuilder builder;
	private SimpleContractBuilder contractBuilder;
	private ModelEntityMap map;
	private Scenario scenario;
	private final Collection<EClass> handledClasses
		= Arrays.asList(
				ContractPackage.eINSTANCE.getSalesContract(),
				ContractPackage.eINSTANCE.getFixedPricePurchaseContract(),
				ContractPackage.eINSTANCE.getNetbackPurchaseContract(),
				ContractPackage.eINSTANCE.getIndexPricePurchaseContract(),
				ContractPackage.eINSTANCE.getProfitSharingPurchaseContract());

	@Override
	public void startTransforming(final Scenario scenario, final ModelEntityMap map, final ISchedulerBuilder builder) {
		this.scenario = scenario;
		this.map = map;
		this.builder = builder;
		this.contractBuilder = new SimpleContractBuilder();
		builder.addBuilderExtension(contractBuilder);
	}

	@Override
	public void finishTransforming() {
		this.scenario = null;
		this.map = null;
		this.builder = null;
		this.contractBuilder = null;
	}

	@Override
	public IShippingPriceCalculator transformSalesContract(final SalesContract sc) {
		final ICurve index = map.getOptimiserObject(sc.getIndex(), ICurve.class);

		return new IShippingPriceCalculator() {
			@Override
			public void prepareEvaluation(final ISequences sequences) {

			}

			@Override
			public int calculateUnitPrice(final IPortSlot slot, final int time) {
				return (int) index.getValueAtPoint(time);
			}
		};
	}

	@Override
	public ILoadPriceCalculator2 transformPurchaseContract(final PurchaseContract pc) {
		if (pc instanceof FixedPricePurchaseContract) {
			return contractBuilder.createFixedPriceContract(Calculator.scaleToInt(((FixedPricePurchaseContract) pc).getUnitPrice()));
		} else if (pc instanceof IndexPricePurchaseContract) {
			final ICurve curve = map.getOptimiserObject(((IndexPricePurchaseContract) pc).getIndex(), ICurve.class);
			return contractBuilder.createMarketPriceContract(curve);
		} else if (pc instanceof ProfitSharingPurchaseContract) {
			final ICurve actualMarket = map.getOptimiserObject(((ProfitSharingPurchaseContract) pc).getIndex(), ICurve.class);
			final ICurve referenceMarket = map.getOptimiserObject(((ProfitSharingPurchaseContract) pc).getReferenceIndex(), ICurve.class);
			final int alpha = Calculator.scaleToInt(((ProfitSharingPurchaseContract) pc).getAlpha());
			final int beta = Calculator.scaleToInt(((ProfitSharingPurchaseContract) pc).getBeta());
			final int gamma = Calculator.scaleToInt(((ProfitSharingPurchaseContract) pc).getGamma());
			return contractBuilder.createProfitSharingContract(actualMarket, referenceMarket, alpha, beta, gamma);
		} else if (pc instanceof NetbackPurchaseContract) {
			return contractBuilder.createNetbackContract(Calculator.scaleToInt(((NetbackPurchaseContract) pc).getBuyersMargin()));
		}
		return null;
	}

	@Override
	public void slotTransformed(final Slot modelSlot, final IPortSlot optimiserSlot) {

	}

	/**
	 * @return
	 */
	public Collection<EClass> getContractEClasses() {
		return handledClasses;
	}
}
