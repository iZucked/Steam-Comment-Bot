/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2011
 * All rights reserved.
 */
package com.mmxlabs.lngscheduler.emf.extras.export;

import scenario.Detail;
import scenario.ScenarioFactory;

import com.mmxlabs.common.detailtree.IDetailTree;

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
		result.setValue("" + detail.getValue());
		for (final IDetailTree child : detail.getChildren()) {
			final Detail cd = exportDetail(child);
			if (cd != null)
				result.getChildren().add(cd);
		}
		return result;
	}
}
