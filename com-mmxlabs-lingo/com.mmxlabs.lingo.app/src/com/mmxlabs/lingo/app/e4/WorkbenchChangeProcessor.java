/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2022
 * All rights reserved.
 */
package com.mmxlabs.lingo.app.e4;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.e4.core.di.annotations.Execute;
import org.eclipse.e4.ui.model.application.MApplication;
import org.eclipse.e4.ui.model.application.MApplicationElement;
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
		iconMapping.put("platform:/plugin/com.mmxlabs.lingo.reports.scheduleview/icons/schedule_view.gif", "icons:/icons/16x16/schedule_view.png");
		iconMapping.put("platform:/plugin/com.mmxlabs.lingo.reports.scheduleview/icons/schedule_view.png", "icons:/icons/16x16/schedule_view.png");
		iconMapping.put("platform:/plugin/com.mmxlabs.lingo.reports/icons/cview16/VerticalReport.gif", "icons:/icons/16x16/vertical_schedule.png");
		iconMapping.put("platform:/plugin/com.mmxlabs.lingo.reports/icons/cview16/vertical_schedule.png", "icons:/icons/16x16/vertical_schedule.png");
		iconMapping.put("platform:/plugin/com.mmxlabs.lingo.reports/icons/cview16/changes_view.gif", "icons:/icons/16x16/compare.png");
		iconMapping.put("platform:/plugin/com.mmxlabs.lingo.reports/icons/compare.png", "icons:/icons/16x16/compare.png");
		iconMapping.put("platform:/plugin/com.mmxlabs.models.lng.analytics.editor/icons/sandbox.gif", "icons:/16/sandbox");
		iconMapping.put("platform:/plugin/com.mmxlabs.models.lng.analytics.editor/icons/sandbox-08.png", "icons:/16/sandbox");
		iconMapping.put("platform:/plugin/com.mmxlabs.scenario.service.ui/icons/filenav_nav.gif", "icons:/16/scenario");

		iconMapping.put("platform:/plugin/com.mmxlabs.rcp.common/icons/16x16/cloud.png", "icons:/16/cloud");
		iconMapping.put("platform:/plugin/com.mmxlabs.rcp.common/icons/legacy/16x16/exec_flow_view.gif", "icons:/icons/legacy/16x16/exec_flow_view.gif");
		iconMapping.put("platform:/plugin/com.mmxlabs.rcp.common/icons/legacy/16x16/exec_statistic_view.gif", "icons:/icons/legacy/16x16/exec_statistic_view.gif");
		iconMapping.put("platform:/plugin/com.mmxlabs.rcp.common/icons/16x16/schedule_view.png", "icons:/icons/16x16/schedule_view.png");
		iconMapping.put("platform:/plugin/com.mmxlabs.rcp.common/icons/16x16/vertical_schedule.png", "icons:/icons/16x16/vertical_schedule.png");
		iconMapping.put("platform:/plugin/com.mmxlabs.rcp.common/icons/16x16/compare.png", "icons:/icons/16x16/compare.png");
		iconMapping.put("platform:/plugin/com.mmxlabs.lingo.reports/icons/compare.png", "icons:/icons/16x16/compare.png");
		iconMapping.put("platform:/plugin/com.mmxlabs.rcp.common/icons/16x16/sandbox.png", "icons:/16/sandbox");
		iconMapping.put("platform:/plugin/com.mmxlabs.rcp.common/icons/16x16/scenario.png", "icons:/16/scenario");
		iconMapping.put("platform:/plugin/com.mmxlabs.models.lng.analytics.editor/icons/sandbox.png", "icons:/16/sandbox");
		iconMapping.put("platform:/plugin/com.mmxlabs.models.lng.nominations.editor/icons/cview16/ihigh_obj.gif", "icons:/16/nominations");
		
		
		
		mapIcons(application, iconMapping);
		
		// Replace a specific view icon. 
		// setElementIcon(application, "com.mmxlabs.shiplingo.platform.reports.views.CargoEconsReport", "icons:/icons/16/econs");
		
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
						label.setIconURI("icons:/icons/legacy/16x16/exec_statistic_view.gif");
					}
					if (existing.endsWith("exec_flow_view.gif")) {
						label.setIconURI("icons:/icons/legacy/16x16/exec_flow_view.gif");
					}
				}
				if (existing.startsWith("platform:/plugin/com.mmxlabs.rcp.icons.lingo")) {
					label.setIconURI("icons:" + existing.substring("platform:/plugin/com.mmxlabs.rcp.icons.lingo".length()));
				}
				if (existing.startsWith("platform:/plugin/com.mmxlabs.rcp.common")) {
					label.setIconURI("icons:" + existing.substring("platform:/plugin/com.mmxlabs.rcp.common".length()));
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

	private void setElementIcon(MElementContainer parent, String elementID, String iconURI) {
		for (var child : parent.getChildren()) {
			setElementIconRecursive(child, elementID, iconURI);
		}
		if (parent instanceof MWindow window) {
			for (var child : window.getSharedElements()) {
				setElementIconRecursive(child, elementID, iconURI);
			}
		}
	}

	private void setElementIconRecursive(Object child, String elementID, String iconURI) {
		if (child instanceof MElementContainer ec) {
			setElementIcon(ec, elementID, iconURI);
		}

		if (child instanceof MUILabel label) {
			if (label instanceof MApplicationElement element) {
				if (elementID.equals(element.getElementId())) {
					String existing = label.getIconURI();
					if (existing != null) {
						label.setIconURI(iconURI);
					}
				}
			}
		}
	}

}
