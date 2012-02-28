/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2012
 * All rights reserved.
 */
package com.mmxlabs.models.lng.transformer.export;

import scenario.Detail;
import scenario.ScenarioFactory;

import com.mmxlabs.common.detailtree.IDetailTree;
import com.mmxlabs.scheduler.optimiser.Calculator;

/**
 * Utility for exporting detail trees.
 * 
 * TODO handle datatypes in a meaningful way.
 * 
 * @author hinton
 * 
 */
public class DetailTreeExporter {
	public static Detail exportDetail(final IDetailTree detail) {
		if (detail == null)
			return null;
		final Detail result = ScenarioFactory.eINSTANCE.createDetail();
		result.setName(detail.getKey());
		if (detail.getValue() instanceof Long) {
			result.setValue(String.format("%,d", ((Long) detail.getValue()) / Calculator.ScaleFactor));
		} else if (detail.getValue() instanceof Integer) {
			result.setValue(String.format("%,f", ((Integer) detail.getValue()) / (float)Calculator.ScaleFactor));
		} else {
			result.setValue("" + detail.getValue());
		}
		for (final IDetailTree child : detail.getChildren()) {
			final Detail cd = exportDetail(child);
			if (cd != null)
				result.getChildren().add(cd);
		}
		return result;
	}
}
