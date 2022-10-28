/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
package com.mmxlabs.lingo.app.e4;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Objects;
import java.util.function.Consumer;

import org.eclipse.e4.core.di.annotations.Execute;
import org.eclipse.e4.ui.model.application.MAddon;
import org.eclipse.e4.ui.model.application.MApplication;
import org.eclipse.e4.ui.model.application.MApplicationElement;
import org.eclipse.e4.ui.model.application.ui.MElementContainer;
import org.eclipse.e4.ui.model.application.ui.MUILabel;
import org.eclipse.e4.ui.model.application.ui.basic.MWindow;
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
		iconMapping.put("platform:/plugin/com.mmxlabs.lingo.reports/icons/full/obj16/small_transfers.png", "icons:/16/transfer");

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
		iconMapping.put("platform:/plugin/com.mmxlabs.lingo.ui/icons/risk.png", "icons:/24/risk");
		iconMapping.put("platform:/plugin/com.mmxlabs.rcp.icons.lingo/icons/legacy/16x16/exec_flow_view.gif", "icons:/16/report");
		iconMapping.put("icons:/icons/legacy/16x16/exec_flow_view.gif", "icons:/16/report");
		
		
		mapIcons(application, iconMapping);
		
		// Replace a specific view icon. 
		setElementIcon(application, "com.mmxlabs.shiplingo.platform.reports.views.CargoEconsReport", "icons:/16/econs");
		setElementIcon(application, "com.mmxlabs.shiplingo.platform.reports.views.LatenessReportView", "icons:/16/lateness");
		setElementIcon(application, "com.mmxlabs.lngdataserver.integration.ui.scenarios.cloud.view.TaskManagerView", "icons:/16/cloudplay");
		setElementIcon(application, "com.mmxlabs.shiplingo.platform.reports.views.PNLDetailsReport", "icons:/16/PNLDetails");
		setElementIcon(application, "com.mmxlabs.models.lng.nominations.editor.NominationSpecsView", "icons:/16/NominationSpecifications");
		setElementIcon(application, "com.mmxlabs.models.lng.transfers.editor.views.TransferRecordsView", "icons:/16/transfer");
		setElementIcon(application, "com.mmxlabs.lingo.reports.views.standard.InventoryReport", "icons:/16/inventory");
		
		setElementIcon(application, "com.mmxlabs.models.lng.cargo.editor.risk.DealSetTableEditorView", "icons:/16/report");
		setElementIcon(application, "com.mmxlabs.models.lng.cargo.editor.risk.CargoSelectionTableEditorView", "icons:/16/report");
		setElementIcon(application, "com.mmxlabs.lingo.reports.pricesensitivity.PriceSensitivityReport", "icons:/16/report");
		setElementIcon(application, "com.mmxlabs.models.lng.pricing.editor.CooldownCostsView", "icons:/16/report");
		setElementIcon(application, "com.mmxlabs.models.lng.pricing.editor.PortCostsView", "icons:/16/report");
		setElementIcon(application, "com.mmxlabs.models.lng.pricing.editor.CanalCostsView", "icons:/16/report");
		setElementIcon(application, "com.mmxlabs.models.lng.port.editor.views.PortView", "icons:/16/report");
		setElementIcon(application, "com.mmxlabs.models.lng.fleet.editor.views.VesselAndClassView", "icons:/16/report");
		setElementIcon(application, "com.mmxlabs.models.lng.cargo.editor.views.RouteOptionsEditorView", "icons:/16/report");
		setElementIcon(application, "com.mmxlabs.lngdataserver.browser.ui.DataBrowser", "icons:/16/report");
		setElementIcon(application, "com.mmxlabs.shiplingo.platform.reports.views.CooldownReportView", "icons:/16/report");
		setElementIcon(application, "com.mmxlabs.lingo.reports.views.standard.incomestatement.IncomeStatementByContract", "icons:/16/report");
		setElementIcon(application, "com.mmxlabs.lingo.reports.views.standard.incomestatement.IncomeStatementByRegion", "icons:/16/report");
		setElementIcon(application, "com.mmxlabs.models.lng.pricing.editor.SettledPricesView", "icons:/16/report");
		setElementIcon(application, "com.mmxlabs.models.lng.pricing.editor.HolidayCalendarsView", "icons:/16/report");
		setElementIcon(application, "com.mmxlabs.models.lng.pricing.editor.PricingCalendarsView", "icons:/16/report");
		setElementIcon(application, "com.mmxlabs.models.lng.actuals.editor.ActualsEditorView", "icons:/16/report");
		setElementIcon(application, "com.mmxlabs.shiplingo.platform.reports.views.EndHeelReportView", "icons:/16/report");
		setElementIcon(application, "com.mmxlabs.lingo.reports.views.standard.pnlcalcs.PNLCalcsReport", "icons:/16/report");
		setElementIcon(application, "com.mmxlabs.shiplingo.platform.reports.views.ExposureReportView", "icons:/16/report");
		setElementIcon(application, "com.mmxlabs.shiplingo.platform.reports.views.ExposureDetailReportView", "icons:/16/report");
		
		// Change to new part ID
		forEach(application, "com.mmxlabs.lngdataserver.integration.ui.scenarios.cloud.view.CloudManagerView",
				p -> p.setElementId("com.mmxlabs.lngdataserver.integration.ui.scenarios.cloud.view.TaskManagerView"));

		// Make sure the new name is picked up
		forEach(application, "com.mmxlabs.lngdataserver.integration.ui.scenarios.cloud.view.TaskManagerView", p -> p.setLabel("Run Manager"));

		// platform:/plugin/com.mmxlabs.lingo.reports/icons/cview16/changes_view.gif
		// platform:/plugin/com.mmxlabs.lingo.reports/icons/cview16/ihigh_obj.gif
		// platform:/plugin/com.mmxlabs.models.lng.analytics.editor/icons/sandbox.gif
		// platform:/plugin/com.mmxlabs.models.lng.nominations.editor/icons/cview16/ihigh_obj.gif
		// platform:/plugin/com.mmxlabs.models.lng.nominations.editor/icons/full/obj16/NominationsModelFile.gif
		// platform:/plugin/com.mmxlabs.models.ui.validation.views/icons/problems_view.gif
		// platform:/plugin/com.mmxlabs.scenario.service.ui/icons/filenav_nav.gif

		
		E4ModelHelper.removeViewPart("org.eclipse.ui.internal.introview", application, modelService);

		
		if (!LicenseFeatures.isPermitted("features:fitness-view")) {
			E4ModelHelper.removeViewPart("com.mmxlabs.shiplingo.platform.reports.views.FitnessReportView", application, modelService);
		}

		// Only remove if not permitted.
		if (!LicenseFeatures.isPermitted(KnownFeatures.FEATURE_EXPOSURES)) {
			E4ModelHelper.removeViewPart("com.mmxlabs.shiplingo.platform.reports.views.ExposureReportView", application, modelService);
		}

		// Remove Job Manager.
		E4ModelHelper.removeViewPart("com.mmxlabs.jobcontroller.views.JobManager", application, modelService);

		CustomReportsRegistry.getInstance().removeDeletedViews(application, modelService);

		// This is lost from IDE plugin
		Iterator<MAddon> itr = application.getAddons().iterator();
		while (itr.hasNext()) {
			var addon = itr.next();
			if (Objects.equals(addon.getContributionURI(), "bundleclass://org.eclipse.ui.ide/org.eclipse.ui.internal.ide.addons.SaveAllDirtyPartsAddon")) {
				itr.remove();
			}
		}

		// Remove views from the IDE plugin
		E4ModelHelper.removeViewPart("com.mmxlabs.lingo.reports.emissions.CIIReportView", application, modelService);
		E4ModelHelper.removeViewPart("com.mmxlabs.lingo.reports.emissions.TotalEmissionAccountingReportView", application, modelService);
		E4ModelHelper.removeViewPart("com.mmxlabs.lingo.reports.emissions.cii.CIIReportView", application, modelService);
		E4ModelHelper.removeViewPart("org.eclipse.ui.views.ProgressView", application, modelService);
		E4ModelHelper.removeViewPart("org.eclipse.ui.views.ResourceNavigator", application, modelService);
		E4ModelHelper.removeViewPart("org.eclipse.ui.views.BookmarkView", application, modelService);
		E4ModelHelper.removeViewPart("org.eclipse.ui.views.AllMarkersView", application, modelService);
		E4ModelHelper.removeViewPart("org.eclipse.ui.views.minimap.MinimapView", application, modelService);
		E4ModelHelper.removeViewPart("org.eclipse.ui.views.ProblemView", application, modelService);
		E4ModelHelper.removeViewPart("org.eclipse.ui.views.TaskList", application, modelService);
		
		E4ModelHelper.removeViewPart("com.mmxlabs.lingo.p.reports.pshippingschedule.ShippingScheduleReportView", application, modelService);
		
		E4ModelHelper.removeChildrenFromTrim("org.eclipse.ui.trim.status", application, modelService, //
				"com.mmxlabs.hub.toolcontrol.status",//
				"com.mmxlabs.lngdataserver.integration.ui.scenarios.basecasestatustrimcontribution",//
				"com.mmxlabs.lngdataserver.integration.ui.scenarios.referencedatastatustrimcontribution");

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

	private <T extends MApplicationElement & MUILabel> void forEach(MElementContainer parent, String elementID, Consumer<T> action) {
		for (var child : parent.getChildren()) {
			doForEach(child, elementID, action);
		}
		if (parent instanceof MWindow window) {
			for (var child : window.getSharedElements()) {
				doForEach(child, elementID, action);
			}
		}
	}

	private <T extends MApplicationElement & MUILabel> void doForEach(Object child, String elementID, Consumer<T> action) {
		if (child instanceof MElementContainer ec) {
			forEach(ec, elementID, action);
		}

		if (child instanceof MUILabel label) {
			if (label instanceof MApplicationElement element) {
				if (elementID.equals(element.getElementId())) {
					action.accept((T) element);
					// String existing = label.getIconURI();
					// if (existing != null) {
					// label.setIconURI(iconURI);
					// }
				}
			}
		}
	}

}
