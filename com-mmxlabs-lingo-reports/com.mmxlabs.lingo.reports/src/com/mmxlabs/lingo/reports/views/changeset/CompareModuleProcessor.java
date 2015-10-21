/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2015
 * All rights reserved.
 */
package com.mmxlabs.lingo.reports.views.changeset;

import org.apache.shiro.SecurityUtils;
import org.eclipse.e4.core.di.annotations.Execute;
import org.eclipse.e4.ui.model.application.MApplication;
import org.eclipse.e4.ui.workbench.modeling.EModelService;
import org.eclipse.jdt.annotation.NonNull;

import com.mmxlabs.rcp.common.application.E4ModelHelper;

public class CompareModuleProcessor {

	private static final String PERSPECTIVE_COMPARE_ID = "com.mmxlabs.lingo.reports.diff.DiffPerspective";
	private static final String VIEW_CHANGE_SETS_ID = "com.mmxlabs.lingo.reports.views.changeset.ChangeSetView";
	private static final String VIEW_CHANGES_ID = "com.mmxlabs.lingo.reports.diff.DiffGroupView";

	@Execute
	public void cleanUpActionPlanUI(@NonNull final MApplication application, @NonNull final EModelService modelService) {

		if (SecurityUtils.getSubject().isPermitted("features:difftools")) {
			// Feature is enabled, so do not clean up
			return;
		}

		E4ModelHelper.removePerspective(PERSPECTIVE_COMPARE_ID, application, modelService);
		E4ModelHelper.removeViewPart(VIEW_CHANGES_ID, application, modelService);
		E4ModelHelper.removeViewPart(VIEW_CHANGE_SETS_ID, application, modelService);
	}
}
