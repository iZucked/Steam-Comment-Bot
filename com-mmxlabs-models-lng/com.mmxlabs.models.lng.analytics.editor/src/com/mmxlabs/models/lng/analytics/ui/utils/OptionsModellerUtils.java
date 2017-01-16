/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
package com.mmxlabs.models.lng.analytics.ui.utils;

import org.eclipse.jdt.annotation.Nullable;

import com.mmxlabs.models.lng.analytics.OptionAnalysisModel;

public class OptionsModellerUtils {

	public static OptionAnalysisModel getRootOptionsModel(@Nullable final OptionAnalysisModel optionModel) {
		OptionAnalysisModel optionAnalysisModel = optionModel;
		while (optionAnalysisModel != null && optionAnalysisModel.eContainer() != null && optionAnalysisModel.eContainer() instanceof OptionAnalysisModel) {
			optionAnalysisModel = (OptionAnalysisModel) optionAnalysisModel.eContainer();
		}
		if (optionAnalysisModel != null && optionAnalysisModel.getName() == null) {
			optionAnalysisModel.setName("root");
		}
		return optionAnalysisModel;
	}

}
