/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
package com.mmxlabs.lingo.reports.emissions.cii.managers;

import org.eclipse.swt.graphics.Image;

import com.mmxlabs.lingo.reports.emissions.cii.CIIGradesData;
import com.mmxlabs.lingo.reports.services.ISelectedDataProvider;
import com.mmxlabs.scenario.service.ScenarioResult;

public class CIIScenarioColumnManager extends AbstractCIIColumnManager {
	
	private final ISelectedDataProvider selectedDataProvider;
	private final Image pinImage;

	public CIIScenarioColumnManager(final ISelectedDataProvider selectedDataProvider, final Image pinImage) {
		super("Scenario");
		this.pinImage = pinImage;
		this.selectedDataProvider = selectedDataProvider;
	}
	
	@Override
	public Image getColumnImage(final CIIGradesData data) {
		final ScenarioResult scenarioResult = data.getScenarioResult();
		if (selectedDataProvider.getPinnedScenarioResult() == scenarioResult) {
			return pinImage;
		}
		return null;
	}

	@Override
	public String getColumnText(final CIIGradesData data) {
		final ScenarioResult scenarioResult = data.getScenarioResult();
		if (scenarioResult == null) {
			return null;
		}
		return scenarioResult.getModelRecord().getName();
	}
}