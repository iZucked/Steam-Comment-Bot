/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2015
 * All rights reserved.
 */
package com.mmxlabs.models.lng.transformer.ui;

import java.time.YearMonth;

import org.eclipse.jdt.annotation.NonNull;

import com.mmxlabs.common.time.Months;
import com.mmxlabs.models.lng.parameters.OptimiserSettings;
import com.mmxlabs.models.lng.parameters.ParametersFactory;
import com.mmxlabs.models.lng.parameters.SimilarityInterval;
import com.mmxlabs.models.lng.parameters.SimilarityMode;
import com.mmxlabs.models.lng.parameters.SimilaritySettings;
import com.mmxlabs.models.lng.parameters.UserSettings;

public class SimilarityUIParameters {
	private static final double loW = 8.0/3.0;
	private static final double medW = 16.0/3.0;
	private static final double highW = 32.0/3.0;
	private static final double topW = 64.0/3.0;
	
	public static SimilaritySettings getSimilaritySettings(@NonNull SimilarityMode similarityMode, @NonNull YearMonth start, @NonNull YearMonth end) {
		int monthsInPeriod = Months.between(start, end);
		final SimilaritySettings similaritySettings;

		switch(similarityMode) {
			case HIGH:
				similaritySettings = ParametersFactory.eINSTANCE.createSimilaritySettings();
				similaritySettings.setLowInterval(createSimilarityInterval(roundToInt(monthsInPeriod * loW), 250_000));
				similaritySettings.setMedInterval(createSimilarityInterval(roundToInt(monthsInPeriod * medW), 500_000));
				similaritySettings.setHighInterval(createSimilarityInterval(roundToInt(monthsInPeriod * highW), 500_000));
				similaritySettings.setOutOfBoundsWeight(5_000_000);
				break;
			case MEDIUM:
				similaritySettings = ParametersFactory.eINSTANCE.createSimilaritySettings();
				similaritySettings.setLowInterval(createSimilarityInterval(roundToInt(monthsInPeriod * loW), 0));
				similaritySettings.setMedInterval(createSimilarityInterval(roundToInt(monthsInPeriod * medW), 250_000));
				similaritySettings.setHighInterval(createSimilarityInterval(roundToInt(monthsInPeriod * highW), 500_000));
				similaritySettings.setOutOfBoundsWeight(5_000_000);
				break;
			case LOW:
				similaritySettings = ParametersFactory.eINSTANCE.createSimilaritySettings();
				similaritySettings.setLowInterval(createSimilarityInterval(roundToInt(monthsInPeriod * medW), 0));
				similaritySettings.setMedInterval(createSimilarityInterval(roundToInt(monthsInPeriod * highW), 250_000));
				similaritySettings.setHighInterval(createSimilarityInterval(roundToInt(monthsInPeriod * topW), 500_000));
				similaritySettings.setOutOfBoundsWeight(5_000_000);
				break;
			default:
				similaritySettings = createOffSimilaritySettings();
				break;
		}
		
		return similaritySettings;
	}

	public static SimilaritySettings createOffSimilaritySettings() {
		final SimilaritySettings similaritySettings = ParametersFactory.eINSTANCE.createSimilaritySettings();
		similaritySettings.setLowInterval(createSimilarityInterval(8, 0));
		similaritySettings.setMedInterval(createSimilarityInterval(16, 0));
		similaritySettings.setHighInterval(createSimilarityInterval(32, 0));
		similaritySettings.setOutOfBoundsWeight(0);
		return similaritySettings;
	}
	
	private static int roundToInt(double d) {
		return (int) Math.ceil(d);
	}

	public static SimilarityInterval createSimilarityInterval(final int upperChangeCount, final int weight) {
		final SimilarityInterval interval = ParametersFactory.eINSTANCE.createSimilarityInterval();

		interval.setThreshold(upperChangeCount);
		interval.setWeight(weight);

		return interval;
	}

}
