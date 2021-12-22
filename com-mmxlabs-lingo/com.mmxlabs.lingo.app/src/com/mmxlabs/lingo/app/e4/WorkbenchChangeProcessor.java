/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2021
 * All rights reserved.
 */
package com.mmxlabs.lingo.app.e4;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.e4.core.di.annotations.Execute;
import org.eclipse.e4.ui.model.application.MApplication;
import org.eclipse.e4.ui.model.application.ui.MElementContainer;
import org.eclipse.e4.ui.model.application.ui.MUILabel;
import org.eclipse.e4.ui.model.application.ui.advanced.MPerspective;
import org.eclipse.e4.ui.model.application.ui.advanced.MPerspectiveStack;
import org.eclipse.e4.ui.model.application.ui.basic.MPartSashContainer;
import org.eclipse.e4.ui.model.application.ui.basic.MPartSashContainerElement;
import org.eclipse.e4.ui.model.application.ui.basic.MWindow;
import org.eclipse.e4.ui.model.application.ui.basic.MWindowElement;
import org.eclipse.e4.ui.workbench.modeling.EModelService;
import org.eclipse.jdt.annotation.NonNull;

import com.mmxlabs.license.features.KnownFeatures;
import com.mmxlabs.license.features.LicenseFeatures;
import com.mmxlabs.lingo.reports.customizable.CustomReportsRegistry;
import com.mmxlabs.rcp.common.application.E4ModelHelper;

public class WorkbenchChangeProcessor {

	@Execute
	void process(@NonNull final MApplication application, @NonNull final EModelService modelService) {

		// Single replacement mappings, old to new
		// Note: Some images copied into many places are handled explicitly in the
		// mapIcons functions
		Map<String, String> iconMapping = new HashMap<>();
		iconMapping.put("platform:/plugin/com.mmxlabs.lingo.reports.scheduleview/icons/schedule_view.gif", "platform:/plugin/com.mmxlabs.lingo.reports.scheduleview/icons/schedule_view.png");
		iconMapping.put("platform:/plugin/com.mmxlabs.lingo.reports/icons/cview16/VerticalReport.gif", "platform:/plugin/com.mmxlabs.lingo.reports/icons/cview16/vertical_schedule.png");
		iconMapping.put("platform:/plugin/com.mmxlabs.lingo.reports/icons/cview16/changes_view.gif", "platform:/plugin/com.mmxlabs.lingo.reports/icons/compare.png");

		
		mapIcons(application, iconMapping);
//		
//		platform:/plugin/com.mmxlabs.lingo.reports/icons/cview16/changes_view.gif
//		platform:/plugin/com.mmxlabs.lingo.reports/icons/cview16/ihigh_obj.gif
//		platform:/plugin/com.mmxlabs.models.lng.analytics.editor/icons/sandbox.gif
//		platform:/plugin/com.mmxlabs.models.lng.nominations.editor/icons/cview16/ihigh_obj.gif
//		platform:/plugin/com.mmxlabs.models.lng.nominations.editor/icons/full/obj16/NominationsModelFile.gif
//		platform:/plugin/com.mmxlabs.models.ui.validation.views/icons/problems_view.gif
//		platform:/plugin/com.mmxlabs.scenario.service.ui/icons/filenav_nav.gif

		// Remove ADP view part
		E4ModelHelper.removeViewPart("com.mmxlabs.models.lng.adp.presentation.views.ADPEditorView", application, modelService);

		// Removed from *.port.editor/plugin.xml
		// <view
		// category="com.mmxlabs.models.lng.views.physical"
		// class="com.mmxlabs.models.lng.port.editor.views.PortGroupView"
		// id="com.mmxlabs.models.lng.port.editor.views.PortGroupView"
		// name="Port Groups">
		// </view>
		E4ModelHelper.removeViewPart("com.mmxlabs.models.lng.port.editor.views.PortGroupView", application, modelService);

		if (!LicenseFeatures.isPermitted("features:fitness-view")) {
			E4ModelHelper.removeViewPart("com.mmxlabs.shiplingo.platform.reports.views.FitnessReportView", application, modelService);
		}

		// Added for 3.8.x to 3.9.0 changes
		// Rename Diff Tools Perspective to Compare
		for (final MWindow window : application.getChildren()) {
			for (final MWindowElement mWindowElement : window.getChildren()) {
				if (mWindowElement instanceof MPartSashContainer) {
					final MPartSashContainer mPartSashContainer = (MPartSashContainer) mWindowElement;
					for (final MPartSashContainerElement mPartSashContainerElement : mPartSashContainer.getChildren()) {
						if (mPartSashContainerElement instanceof MPerspectiveStack) {
							final MPerspectiveStack mPerspectiveStack = (MPerspectiveStack) mPartSashContainerElement;
							for (final MPerspective perspective : mPerspectiveStack.getChildren()) {
								if (perspective.getElementId().equals("com.mmxlabs.lingo.reports.diff.DiffPerspective") && perspective.getLabel().equals("Diff Tools")) {
									perspective.setLabel("Compare");
								}
							}
						}
					}
				}
			}
		}

		E4ModelHelper.removeViewPart("com.mmxlabs.lingo.reports.diff.DiffGroupView", application, modelService);

		// Only remove if not permitted.
		if (!LicenseFeatures.isPermitted(KnownFeatures.FEATURE_EXPOSURES)) {
			E4ModelHelper.removeViewPart("com.mmxlabs.shiplingo.platform.reports.views.ExposureReportView", application, modelService);
		}
		// View no longer present
		E4ModelHelper.removeViewPart("com.mmxlabs.lngdataserver.integration.ui.WebNavigatorView", application, modelService);

		// Remove Job Manager.
		E4ModelHelper.removeViewPart("com.mmxlabs.jobcontroller.views.JobManager", application, modelService);

		CustomReportsRegistry.getInstance().removeDeletedViews(application, modelService);
	}

	private void applyMapping(Object child, Map<String, String> iconMapping) {
		if (child instanceof MElementContainer ec) {
			mapIcons(ec, iconMapping);
		}

		if (child instanceof MUILabel label) {
			String existing = label.getIconURI();
			if (existing != null) {
				// Special common mappings
				if (existing.contains("cview16")) {
					if (existing.endsWith("exec_statistic_view.gif")) {
						label.setIconURI("platform:/plugin/com.mmxlabs.rcp.common/icons/legacy/16x16/exec_statistic_view.gif");
					}
					if (existing.endsWith("exec_flow_view.gif")) {
						label.setIconURI("platform:/plugin/com.mmxlabs.rcp.common/icons/legacy/16x16/exec_flow_view.gif");
					}
				}
				String replacement = iconMapping.get(existing);
				if (replacement != null) {
					label.setIconURI(replacement);
				}
			}
		}
	}

	private void mapIcons(MElementContainer parent, Map<String, String> iconMapping) {
		for (var child : parent.getChildren()) {
			applyMapping(child, iconMapping);
		}
		if (parent instanceof MWindow window) {
			for (var child : window.getSharedElements()) {
				applyMapping(child, iconMapping);
			}
		}
	}
}
