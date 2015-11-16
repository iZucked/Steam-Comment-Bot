/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2015
 * All rights reserved.
 */
package com.mmxlabs.lingo.its.tests;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.URL;
import java.nio.charset.StandardCharsets;

import org.eclipse.core.runtime.FileLocator;
import org.eclipse.jdt.annotation.Nullable;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.IViewPart;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.junit.Assert;
import org.osgi.framework.BundleContext;
import org.osgi.framework.FrameworkUtil;
import org.osgi.framework.ServiceReference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mmxlabs.lingo.reports.IReportContents;
import com.mmxlabs.lingo.reports.views.fleet.ConfigurableFleetReportView;
import com.mmxlabs.lingo.reports.views.portrotation.PortRotationReportView;
import com.mmxlabs.lingo.reports.views.schedule.ConfigurableScheduleReportView;
import com.mmxlabs.lingo.reports.views.standard.CapacityViolationReportView;
import com.mmxlabs.lingo.reports.views.standard.CooldownReportView;
import com.mmxlabs.lingo.reports.views.standard.LatenessReportView;
import com.mmxlabs.models.lng.scenario.model.LNGScenarioModel;
import com.mmxlabs.models.lng.transformer.ui.LNGScenarioRunner;
import com.mmxlabs.scenario.service.model.ScenarioInstance;
import com.mmxlabs.scenario.service.ui.IScenarioServiceSelectionProvider;

/**
 * Helper class to open up a view, set the scenario selection provider to the given instance and adapt the result to a {@link IReportContents} instance.
 * 
 * @author Simon Goodall
 * 
 */
public class ReportTesterHelper {

	public static final String SCHEDULE_SUMMARY_ID = ConfigurableScheduleReportView.ID;
	public static final String SCHEDULE_SUMMARY_SHORTNAME = "ScheduleSummary";

	public static final String PORT_ROTATIONS_ID = PortRotationReportView.ID;
	public static final String PORT_ROTATIONS_SHORTNAME = "PortRotations";

	public static final String VESSEL_REPORT_ID = ConfigurableFleetReportView.ID;
	public static final String VESSEL_REPORT_SHORTNAME = "VesselReport";

	public static final String VERTICAL_REPORT_ID = "com.mmxlabs.lingo.reports.verticalreport";
	public static final String VERTICAL_REPORT_SHORTNAME = "VerticalReport";

	public static final String LATENESS_REPORT_ID = LatenessReportView.ID;
	public static final String LATENESS_REPORT_SHORTNAME = "LatenessReport";

	public static final String CAPACITY_REPORT_ID = CapacityViolationReportView.ID;
	public static final String CAPACITY_REPORT_SHORTNAME = "CapacityReport";

	public static final String COOLDOWN_REPORT_ID = CooldownReportView.ID;
	public static final String COOLDOWN_REPORT_SHORTNAME = "CooldownReport";

	@Nullable
	public IReportContents getReportContents(final ScenarioInstance scenario, final String reportID) throws InterruptedException {

		// Get reference to the selection provider service
		final BundleContext bundleContext = FrameworkUtil.getBundle(ReportTesterHelper.class).getBundleContext();
		final ServiceReference<IScenarioServiceSelectionProvider> serviceReference = bundleContext.getServiceReference(IScenarioServiceSelectionProvider.class);
		Assert.assertNotNull(serviceReference);
		final IScenarioServiceSelectionProvider provider = bundleContext.getService(serviceReference);
		Assert.assertNotNull(provider);

		final IViewPart[] view = new IViewPart[1];
		final IReportContents[] contents = new IReportContents[1];
		try {

			// Step 1 open the view, release UI thread
			Display.getDefault().syncExec(new Runnable() {

				@Override
				public void run() {
					final IWorkbenchPage activePage = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage();
					Assert.assertNotNull(activePage);
					try {
						view[0] = activePage.showView(reportID);
						Assert.assertNotNull(view[0]);
						activePage.activate(view[0]);

					} catch (PartInitException e) {
						e.printStackTrace();
					}
				}
			});

			// Step two set the new selection, release UI thread
			Thread.sleep(1000);
			Thread.yield();
			Display.getDefault().syncExec(new Runnable() {

				@Override
				public void run() {
					provider.deselectAll(true);
					provider.select(scenario, true);
				}
			});
			Thread.yield();
			Thread.sleep(1000);

			// Step 3, obtain report contents
			Display.getDefault().syncExec(new Runnable() {

				@Override
				public void run() {
					contents[0] = (IReportContents) view[0].getAdapter(IReportContents.class);
				}
			});

		} finally {
			bundleContext.ungetService(serviceReference);
		}
		return contents[0];
	}
}