/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
package com.mmxlabs.models.lng.transformer.ui;

import java.time.YearMonth;

import org.eclipse.jdt.annotation.NonNull;

import com.mmxlabs.common.time.Months;
import com.mmxlabs.models.lng.parameters.ActionPlanSettings;
import com.mmxlabs.models.lng.parameters.ParametersFactory;
import com.mmxlabs.models.lng.parameters.SimilarityMode;

public class ActionPlanUIParameters {
	
	public static ActionPlanSettings getActionPlanSettings(@NonNull SimilarityMode similarityMode, @NonNull YearMonth start, @NonNull YearMonth end) {
		int monthsInPeriod = Months.between(start, end);
		final ActionPlanSettings actionPlanSettings;

		switch(similarityMode) {
			case HIGH:
				actionPlanSettings = ParametersFactory.eINSTANCE.createActionPlanSettings();
				actionPlanSettings.setTotalEvaluations(roundToInt(3_000_000/3.0 * monthsInPeriod));
				actionPlanSettings.setInRunEvaluations(roundToInt(1_000_000/3.0 * monthsInPeriod));
				actionPlanSettings.setSearchDepth(5_000);
				break;
			case MEDIUM:
				actionPlanSettings = ParametersFactory.eINSTANCE.createActionPlanSettings();
				if (monthsInPeriod < 4) {
					actionPlanSettings.setTotalEvaluations(roundToInt(5_000_000/3.0 * monthsInPeriod));
					actionPlanSettings.setInRunEvaluations(roundToInt(Math.min(2_000_000, 1_500_000/3.0 * monthsInPeriod)));
					actionPlanSettings.setSearchDepth(5_000);
				} else {
					actionPlanSettings.setTotalEvaluations(roundToInt(30_000_000/6.0 * monthsInPeriod));
					actionPlanSettings.setInRunEvaluations(2_000_000);
					actionPlanSettings.setSearchDepth(5_000);
				}
				break;
			case LOW:
				actionPlanSettings = ParametersFactory.eINSTANCE.createActionPlanSettings();
				actionPlanSettings.setTotalEvaluations(roundToInt(30_000_000/3.0 * monthsInPeriod));
				actionPlanSettings.setInRunEvaluations(roundToInt(Math.min(2_000_000, 2_000_000/3.0 * monthsInPeriod)));
				actionPlanSettings.setSearchDepth(5_000);
				break;
			default:
				actionPlanSettings = ParametersFactory.eINSTANCE.createActionPlanSettings();
				actionPlanSettings.setTotalEvaluations(5_000_000);
				actionPlanSettings.setInRunEvaluations(1_500_000);
				actionPlanSettings.setSearchDepth(5_000);
				break;
		}
		
		return actionPlanSettings;
	}

	public static final ActionPlanSettings getDefaultSettings() {
		ActionPlanSettings actionPlanSettings = ParametersFactory.eINSTANCE.createActionPlanSettings();
		actionPlanSettings.setTotalEvaluations(5_000_000);
		actionPlanSettings.setInRunEvaluations(1_500_000);
		actionPlanSettings.setSearchDepth(5_000);
		return actionPlanSettings;
	}
	
	private static int roundToInt(double d) {
		return (int) Math.ceil(d);
	}

}
