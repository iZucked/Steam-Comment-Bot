/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2020
 * All rights reserved.
 */
package com.mmxlabs.models.lng.nominations.ui.views;

import org.eclipse.e4.core.di.annotations.Execute;
import org.eclipse.e4.ui.model.application.MApplication;
import org.eclipse.e4.ui.workbench.modeling.EModelService;
import org.eclipse.jdt.annotation.NonNull;

import com.mmxlabs.license.features.LicenseFeatures;
import com.mmxlabs.rcp.common.application.E4ModelHelper;

public class RemoveOldNominationsAlertViewModuleProcessor {

	@NonNull
	private static final String OLD_NONOMINATION_REPORT_VIEW_ID = "com.mmxlabs.lingo.reports.views.nomination.NominationAlertReport";

	@Execute
	public void cleanUpActionPlanUI(@NonNull final MApplication application, @NonNull final EModelService modelService) {
		E4ModelHelper.removeViewPart(OLD_NONOMINATION_REPORT_VIEW_ID, application, modelService);
	}
}
