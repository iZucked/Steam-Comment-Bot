/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2022
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.providers.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.jdt.annotation.Nullable;

import com.mmxlabs.common.curves.ICurve;
import com.mmxlabs.common.curves.StepwiseIntegerCurve;
import com.mmxlabs.common.parser.IExpression;
import com.mmxlabs.common.parser.series.ISeries;
import com.mmxlabs.common.parser.series.SeriesParser;
import com.mmxlabs.scheduler.optimiser.OptimiserUnitConvertor;
import com.mmxlabs.scheduler.optimiser.components.IPortSlot;
import com.mmxlabs.scheduler.optimiser.providers.ITransferModelDataProviderEditor;
import com.mmxlabs.scheduler.optimiser.transfers.BasicTransferRecord;
import com.mmxlabs.scheduler.optimiser.transfers.TransfersLookupData;

public class DefaultTransferModelDataProviderEditor implements ITransferModelDataProviderEditor {
	
	private TransfersLookupData lookupData;
	
	private SeriesParser commodityParser;
	
	private SeriesParser pricingBasisParser;
	
	private Map<IPortSlot, List<BasicTransferRecord>> slotsToTransferRecords = new HashMap<>();

	@Override
	public boolean isSlotTransferred(final IPortSlot slot) {
		return (slotsToTransferRecords.containsKey(slot));
	}

	@Override
	public List<BasicTransferRecord> getTransferRecordsForSlot(final IPortSlot slot) {
		return slotsToTransferRecords.getOrDefault(slot, Collections.emptyList());
	}

	@Override
	public TransfersLookupData getTransferLookupData() {
		return this.lookupData;
	}

	/**
	 * Do NOT call before look up data is set!
	 */
	@Override
	public void reconsileIPortSlotWithLookupData(final IPortSlot slot) {
		if (this.lookupData != null) {
			final List<BasicTransferRecord> temp = new ArrayList<>();
			for(final BasicTransferRecord record : lookupData.records) {
				if (record.getSlot().equals(slot)) {
					temp.add(record);
				}
			}
			if (!temp.isEmpty() && !slotsToTransferRecords.containsKey(slot)) {
				slotsToTransferRecords.put(slot, temp);
			}
		} else {
			throw new IllegalStateException("Must initialise the Transfer Look Up Data before reconsinling it with the slots.");
		}
	}

	@Override
	public void setLookupData(TransfersLookupData lookupdata) {
		this.lookupData = lookupdata;
	}

	@Override
	public void setSeriesParsers(SeriesParser commodityParser, SeriesParser pricingBasisParser) {
		this.commodityParser = commodityParser;
		this.pricingBasisParser = pricingBasisParser;
	}

	@Override
	public int getTransferPrice(String priceExpression, int pricingDate, int internalSalesPrice, boolean isBasis) {
		ICurve curve = null;
		if (isBasis) {
			curve = generateExpressionCurve(priceExpression, pricingBasisParser);
		} else {
			double externalSalesPrice = OptimiserUnitConvertor.convertToExternalPrice(internalSalesPrice);
			String modifiedPriceExpression = priceExpression.toLowerCase().replace("salesprice", String.format("(%f)", externalSalesPrice));
			curve = generateExpressionCurve(modifiedPriceExpression, commodityParser);
		}
		
		if (curve != null) {
			return curve.getValueAtPoint(pricingDate);
		} else {
			throw new IllegalStateException(String.format("Cannot get curve for the expressions %s", priceExpression));
		}
	}
	
	/**
	 * Copy from the DateAndCurveHelper! Keep in sync
	 * @param priceExpression
	 * @param indices
	 * @return
	 */
	public @Nullable StepwiseIntegerCurve generateExpressionCurve(final @Nullable String priceExpression, final SeriesParser indices) {

		if (priceExpression == null || priceExpression.isEmpty()) {
			return null;
		}

		final IExpression<ISeries> expression = indices.parse(priceExpression);
		final ISeries parsed = expression.evaluate();

		final StepwiseIntegerCurve curve = new StepwiseIntegerCurve();
		if (parsed.getChangePoints().length == 0) {
			curve.setDefaultValue(OptimiserUnitConvertor.convertToInternalPrice(parsed.evaluate(0).doubleValue()));
		} else {
			curve.setDefaultValue(0);
			for (final int i : parsed.getChangePoints()) {
				curve.setValueAfter(i, OptimiserUnitConvertor.convertToInternalPrice(parsed.evaluate(i).doubleValue()));
			}
		}
		return curve;
	}
}
