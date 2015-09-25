package com.mmxlabs.lingo.reports.views.changeset;

import org.apache.shiro.SecurityUtils;
import org.eclipse.e4.core.di.annotations.Execute;
import org.eclipse.e4.ui.model.application.MApplication;
import org.eclipse.e4.ui.workbench.modeling.EModelService;
import org.eclipse.jdt.annotation.NonNull;

import com.mmxlabs.rcp.common.application.E4ModelHelper;

public class ActionPlanModuleProcessor {

	private static final String VIEW_ACTION_SET_ID = "com.mmxlabs.lingo.reports.views.changeset.ActionSetView";

	@Execute
	public void cleanUpActionPlanUI(@NonNull final MApplication application, @NonNull final EModelService modelService) {

		if (SecurityUtils.getSubject().isPermitted("features:optimisation-actionset")) {
			// Feature is enabled, so do not clean up
			return;
		}

		E4ModelHelper.removeViewPart(VIEW_ACTION_SET_ID, application, modelService);
	}
}
