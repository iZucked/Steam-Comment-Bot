/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
package com.mmxlabs.lingo.reports.views.standard.inventory;

import java.util.List;
import org.eclipse.jdt.annotation.NonNullByDefault;
import org.eclipse.swt.graphics.Color;

import com.mmxlabs.models.lng.commercial.BaseLegalEntity;

@NonNullByDefault
public class OverliftChartState {

	private final List<MullInformation> mullData;
	private final List<BaseLegalEntity> entitiesOrdered;
	private final String[] xCategoryLabels;
	private final Color[] overliftChartColourSequence;

	public OverliftChartState(final List<MullInformation> mullData, final List<BaseLegalEntity> entitiesOrdered, final String[] xCategoryLabels, final Color[] overliftChartColourSequence) {
		this.mullData = mullData;
		this.entitiesOrdered = entitiesOrdered;
		this.xCategoryLabels = xCategoryLabels;
		this.overliftChartColourSequence = overliftChartColourSequence;
	}

	public List<MullInformation> getMullData() {
		return mullData;
	}

	public List<BaseLegalEntity> getEntitiesOrdered() {
		return entitiesOrdered;
	}
	
	public String[] getXCategoryLabels() {
		return xCategoryLabels;
	}

	public Color[] getOverliftChartColourSequence() {
		return overliftChartColourSequence;
	}
}
