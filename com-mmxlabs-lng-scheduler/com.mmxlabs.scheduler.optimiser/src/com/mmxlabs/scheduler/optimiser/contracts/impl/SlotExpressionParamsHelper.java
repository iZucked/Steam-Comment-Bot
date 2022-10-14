/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2022
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.contracts.impl;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.eclipse.jdt.annotation.Nullable;

import com.mmxlabs.common.curves.IParameterisedCurve;
import com.mmxlabs.scheduler.optimiser.OptimiserUnitConvertor;
import com.mmxlabs.scheduler.optimiser.components.IPortSlot;
import com.mmxlabs.scheduler.optimiser.fitness.components.allocation.IAllocationAnnotation;
import com.mmxlabs.scheduler.optimiser.voyage.IPortTimesRecord;
import com.mmxlabs.scheduler.optimiser.voyage.impl.VoyagePlan;

public class SlotExpressionParamsHelper {

	// @Inject
	// private ShippingCostHelper shippingCostHelper;

	public int getValueAtPoint(IParameterisedCurve curve, int pricingDate, final IPortSlot portSlot, final @Nullable VoyagePlan plan, final @Nullable IPortTimesRecord ptr) {
		if (curve.hasParameters()) {
			return curve.getValueAtPoint(pricingDate, buildParameters(portSlot, plan, ptr));
		} else {
			return curve.getValueAtPoint(pricingDate, Collections.emptyMap());
		}
	}

	public Map<String, String> buildParameters(final IPortSlot portSlot, final @Nullable VoyagePlan plan, final @Nullable IPortTimesRecord ptr) {
		final Map<String, String> params = new HashMap<>();
		if (ptr instanceof final IAllocationAnnotation aa) {
//			params.put(VolumeTierSeries.PARAM_VOLUME_M3, Double.toString(OptimiserUnitConvertor.convertToExternalFloatVolume(aa.getCommercialSlotVolumeInM3(portSlot))));
//			params.put(VolumeTierSeries.PARAM_VOLUME_MMBTU, Double.toString(OptimiserUnitConvertor.convertToExternalFloatVolume(aa.getCommercialSlotVolumeInMMBTu(portSlot))));
			params.put("CV", Double.toString(OptimiserUnitConvertor.convertToExternalConversionFactor(aa.getSlotCargoCV(portSlot))));
		}
//
//		if (plan != null) {
//			long totalRouteCost = plan.getTotalRouteCost();
//			params.put("CANAL_COSTS", Double.toString(OptimiserUnitConvertor.convertToExternalFixedCost(totalRouteCost)));
//
//		}
//
//		if (portSlot instanceof ILoadOption && ptr instanceof ICargoValueAnnotation cva) {
//			params.put("ASP", Double.toString(OptimiserUnitConvertor.convertToExternalPrice(cva.getSlotPricePerMMBTu(cva.getSlots().get(1)))));
//
//		}
//		if (plan != null) {
//			ShippingCostHelper sh = new ShippingCostHelper();
//			final long shippingCosts = sh.getRouteExtraCosts(plan) + sh.getFuelCosts(plan);
//			final long portCosts = sh.getPortCosts(plan);
//			final long hireCosts = plan.getCharterCost();
//
//			long total = shippingCosts + portCosts + hireCosts;
//			params.put("SHIP_COSTS", Double.toString(OptimiserUnitConvertor.convertToExternalFixedCost(total)));
//
//		}
		return params;
	}
}
