/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2022
 * All rights reserved.
 */
package com.mmxlabs.lingo.reports.customizable;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.IPath;
import org.eclipse.e4.ui.model.application.MApplication;
import org.eclipse.e4.ui.model.application.descriptor.basic.MPartDescriptor;
import org.eclipse.e4.ui.workbench.modeling.EModelService;
import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jface.action.IStatusLineManager;
import org.eclipse.jface.action.StatusLineContributionItem;
import org.eclipse.swt.widgets.Display;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mmxlabs.lingo.reports.services.CustomReportPermissions;
import com.mmxlabs.lingo.reports.services.ICustomReportDataRepository;
import com.mmxlabs.lingo.reports.views.schedule.ScheduleSummaryReport;
import com.mmxlabs.models.ui.tabular.columngeneration.ColumnBlock;
import com.mmxlabs.rcp.common.ServiceHelper;
import com.mmxlabs.rcp.common.application.E4ModelHelper;

public class CustomReportsRegistry {

	private static final String TEAM_REPORTS_CATEGORY_ID = "com.mmxlabs.lingo.reports.customizable.team";

	private static final String USER_REPORTS_CATEGORY_ID = "com.mmxlabs.lingo.reports.customizable.user";

	private static final Logger LOG = LoggerFactory.getLogger(CustomReportsRegistry.class);

	public static final String USER_REPORTS_DIR = "reports";

	public static final String TEAM_REPORTS_DIR = "team-reports";

	private static final String JSON_REPORT_PREFIX = "report";

	public static final String XML_REPORTS_PLUGIN_FILENAME = "reports.xml";

	private final CustomReportsDefaults defaults = new CustomReportsDefaults();

	private final StatusLineContributionItem statusLineItem = new StatusLineContributionItem("Custom Reports Status Bar");

	private static CustomReportsRegistry instance = new CustomReportsRegistry();

	public static CustomReportsRegistry getInstance() {
		return instance;
	}

	private CustomReportsRegistry() {
		// Singleton instance, so do not allow instantiation other than by once when
		// class loaded.
	}

	public void setStatusLineManager(final IStatusLineManager statusLineManager) {
		statusLineManager.add(this.statusLineItem);
		// Execute within the UI thread, but don't wait until complete (execute
		// asynchronously).
		ServiceHelper.withCheckedServiceConsumer(ICustomReportDataRepository.class, s -> s
				.registerLocalVersionListener(() -> Display.getDefault().asyncExec(() -> statusLineItem.setText("New team custom reports available - restart of LiNGO required to complete update."))));

	}

	public void regenerateReportsPluginXMLFile() {
		try {
			// Check if reports directory exists, if not create it.
			checkReportsDirectoryExistsOrCreate();
			final List<CustomReportDefinition> userReportDefinitions = readUserCustomReportDefinitions();
			final List<CustomReportDefinition> teamReportDefinitions = readTeamCustomReportDefinitions();
			writeReportsPluginXMLFile(userReportDefinitions, teamReportDefinitions);
		} catch (final Exception ex) {
			LOG.error("Error updating report definition plugin.xml", ex);
		}
	}

	private void writeReportsPluginXMLFile(final List<CustomReportDefinition> userReportDefinitions, final List<CustomReportDefinition> teamReportDefinitions) throws FileNotFoundException {
		final File reportsPluginXMLFile = new File(getReportsPluginXMLPath());

		final PrintStream out = new PrintStream(reportsPluginXMLFile);

		writePluginXMLHeader(out);

		writePluginXMLCategoryExtensionPoint(out, USER_REPORTS_CATEGORY_ID, "User Reports");
		writePluginXMLCategoryExtensionPoint(out, TEAM_REPORTS_CATEGORY_ID, "Team Reports");

		writeReportDefinitions(out, USER_REPORTS_CATEGORY_ID, userReportDefinitions);
		writeReportDefinitions(out, TEAM_REPORTS_CATEGORY_ID, teamReportDefinitions);

		writePluginXMLFooter(out);

		out.close();
	}

	private void writeReportDefinitions(final PrintStream out, final String categoryId, final List<CustomReportDefinition> reportDefinitions) {
		// Write out
		for (final CustomReportDefinition rd : reportDefinitions) {
			writePluginXMLExtensionInitialState(out, rd);
		}

		// Write out views section
		for (final CustomReportDefinition rd : reportDefinitions) {
			writePluginXMLExtensionView(out, categoryId, rd);
		}
	}

	public static String getReportsPluginXMLPath() {
		final IPath workspaceLocation = ResourcesPlugin.getWorkspace().getRoot().getLocation();
		return workspaceLocation.toOSString() + IPath.SEPARATOR + USER_REPORTS_DIR + IPath.SEPARATOR + XML_REPORTS_PLUGIN_FILENAME;
	}

	public List<CustomReportDefinition> readUserCustomReportDefinitions() {
		return readCustomReportDefinitions(USER_REPORTS_DIR);
	}

	public List<CustomReportDefinition> readTeamCustomReportDefinitions() {
		final List<CustomReportDefinition> reports = new ArrayList<>();
		List<CustomReportDefinition> fromHub = new ArrayList<>();
		try {
			fromHub = ServiceHelper.withCheckedService(ICustomReportDataRepository.class, ICustomReportDataRepository::getTeamReports);
		} catch (final Throwable ex) {
			LOG.error("Something went wrong reading team reports", ex);
			fromHub = Collections.emptyList();
		}
		reports.addAll(fromHub);
		return reports;
	}

	/**
	 * Whether to display the option of viewing team reports or not.
	 * 
	 * @return
	 */
	public boolean hasTeamReportsCapability() {
		return CustomReportPermissions.hasCustomReportPermission();
	}

	public boolean hasTeamReportsPublishPermission() {
		return CustomReportPermissions.hasCustomReportPublishPermission();
	}

	public boolean hasTeamReportsReadPermission() {
		return CustomReportPermissions.hasCustomReportReadPermission();
	}

	public boolean hasTeamReportsDeletePermission() {
		return CustomReportPermissions.hasCustomReportDeletePermission();
	}

	public List<CustomReportDefinition> readCustomReportDefinitions(final String reportsDirShortName) {
		// Read in all report definitions.
		final IPath workspaceLocation = ResourcesPlugin.getWorkspace().getRoot().getLocation();
		final File reportsDir = new File(workspaceLocation.toOSString() + IPath.SEPARATOR + reportsDirShortName);
		final List<CustomReportDefinition> reportDefinitions = new ArrayList<>();

		if (reportsDir.exists()) {
			final File[] files = reportsDir.listFiles(new FilenameFilter() {
				@Override
				public boolean accept(final File dir, final String name) {
					return name.endsWith(".json") && !name.equalsIgnoreCase("records.json");
				}
			});

			final ObjectMapper mapper = new ObjectMapper();

			for (final File reportJsonFile : files) {
				// Read in json object.
				try {
					final CustomReportDefinition reportDefinition = mapper.readValue(reportJsonFile, CustomReportDefinition.class);
					reportDefinitions.add(reportDefinition);
				} catch (final Exception e) {
					LOG.error("Problem reading file " + reportJsonFile.getName(), e);
				}
			}
		}

		return reportDefinitions;
	}

	private void writePluginXMLExtensionView(final PrintStream out, final String categoryId, final CustomReportDefinition rd) {
		out.print("    <extension\r\n" + "          point=\"org.eclipse.ui.views\">\r\n" + "       <view\r\n" + "             category=\"" + categoryId + "\"\r\n"
				+ "             class=\"org.ops4j.peaberry.eclipse.GuiceExtensionFactory:com.mmxlabs.lingo.reports.views.schedule.ScheduleSummaryReport\"\r\n"
				+ "             icon=\"platform:/plugin/com.mmxlabs.rcp.icons.lingo/icons/legacy/16x16/exec_statistic_view.gif\"\r\n" + "             id=\"");
		out.print(rd.getUuid());
		out.print("\"\r\n" + "             name=\"");
		out.print(rd.getName());
		out.print("\"\r\n" + "             restorable=\"true\">\r\n" + "       </view>\r\n");
		out.print("    </extension>\r\n");
	}

	private void writePluginXMLFooter(final PrintStream out) {
		out.print(" </plugin>");
	}

	private void writePluginXMLHeader(final PrintStream out) {
		out.print("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\r\n" + "<!--\r\n" + "\r\n" + "    Copyright (C) Minimax Labs Ltd., 2010 - 2020\r\n" + "    All rights reserved.\r\n" + "\r\n" + "-->\r\n"
				+ "<?eclipse version=\"3.4\"?>\r\n" + "<plugin>\r\n");
	}

	private void writePluginXMLCategoryExtensionPoint(final PrintStream out, final String categoryId, final String categoryName) {
		out.print("   <extension\r\n" + "         point=\"org.eclipse.ui.views\">\r\n" + "      <category\r\n" + "            id=\"" + categoryId + "\"\r\n" + "            name=\"" + categoryName
				+ "\">\r\n" + "      </category>\r\n" + "   </extension>\r\n");
	}

	private void writePluginXMLExtensionInitialState(final PrintStream out, final CustomReportDefinition ssrd) {
		out.print("    <extension\r\n" + "          point=\"com.mmxlabs.lingo.reports.ScheduleBasedReportInitialState\">\r\n" + "       <ScheduleBasedReportInitialState\r\n"
				+ "             customisable=\"false\"\r\n" + "             id=\"");
		out.print(ssrd.getUuid());
		out.print("\">\r\n");

		for (final String rowFilter : ssrd.getFilters()) {
			out.print("         <InitialRowType\r\n");
			out.print("               rowType=\"");
			out.print(rowFilter);
			out.print("\"/>\r\n");
		}

		for (final String column : ssrd.getColumns()) {
			out.print("         <InitialColumn\r\n");
			out.print("               id=\"");
			out.print(column);
			out.print("\"/>\r\n");
		}

		for (final String diffOption : ssrd.getDiffOptions()) {
			out.print("         <InitialDiffOption\r\n");
			out.print("               option=\"");
			out.print(diffOption);
			out.print("\"/>\r\n");
		}

		out.print("       </ScheduleBasedReportInitialState>\r\n");
		out.print("    </extension>\r\n");
	}

	public void writeToJSON(final CustomReportDefinition rd) {
		// Check if reports directory exists, if not create it.
		checkReportsDirectoryExistsOrCreate();

		// Write report down to reportX.json
		writeCustomReportViewToJSON(rd);

		// Update reports.xml with all plug-in info in.
		regenerateReportsPluginXMLFile();
	}

	private void writeCustomReportViewToJSON(final CustomReportDefinition jsonObject) {
		final IPath workspaceLocation = ResourcesPlugin.getWorkspace().getRoot().getLocation();
		final File reportsFile = new File(workspaceLocation.toOSString() + IPath.SEPARATOR + USER_REPORTS_DIR + IPath.SEPARATOR + JSON_REPORT_PREFIX + jsonObject.getUuid() + ".json");

		final ObjectMapper mapper = new ObjectMapper();
		try {
			mapper.writeValue(reportsFile, jsonObject);
		} catch (final IOException e) {
			LOG.error("Problem saving to json report file: ", e);
		}
	}

	private void checkReportsDirectoryExistsOrCreate() {
		final IPath workspaceLocation = ResourcesPlugin.getWorkspace().getRoot().getLocation();

		final File reportsDirectory = new File(workspaceLocation.toOSString() + IPath.SEPARATOR + USER_REPORTS_DIR);
		if (!(reportsDirectory.exists() && reportsDirectory.isDirectory())) {
			reportsDirectory.mkdir();
		}
	}

	public void removeDeletedViews(@NonNull final MApplication application, @NonNull final EModelService modelService) {
		final List<CustomReportDefinition> reportDefinitions = readUserCustomReportDefinitions();
		final Set<String> customReportIds = new HashSet<>();
		for (final CustomReportDefinition rd : reportDefinitions) {
			customReportIds.add(rd.getUuid());
		}

		final Set<String> reportIdsToRemove = new HashSet<>();
		final Iterator<MPartDescriptor> descriptorItr = application.getDescriptors().iterator();
		while (descriptorItr.hasNext()) {
			final MPartDescriptor descriptor = descriptorItr.next();
			final String id = descriptor.getElementId();
			if (id.startsWith(ScheduleSummaryReport.UUID_PREFIX)) {
				reportIdsToRemove.add(id);
			}
		}

		for (final String id : reportIdsToRemove) {
			E4ModelHelper.removeViewPart(id, application, modelService);
		}
	}

	public void publishReport(final CustomReportDefinition reportDefinition) throws Exception {
		if (!ServiceHelper.withCheckedService(ICustomReportDataRepository.class, s -> s.publishReport(reportDefinition))) {
			throw new IOException("Error publishing report to datahub");
		}
	}

	public List<ColumnBlock> getColumnDefinitions() {
		return defaults.getColumnDefinitions();
	}

	public boolean isBlockDefaultVisible(final ColumnBlock column) {
		return defaults.isVisible(column);
	}

	public CustomReportsDefaults getDefaults() {
		return defaults;
	}

	public int getBlockDefaultIndex(final ColumnBlock columnObj) {
		return defaults.getBlockManager().getBlockIndex(columnObj);
	}

	public void deleteUserReport(final CustomReportDefinition toDelete) {
		final IPath workspaceLocation = ResourcesPlugin.getWorkspace().getRoot().getLocation();
		final File reportsFile = new File(workspaceLocation.toOSString() + IPath.SEPARATOR + USER_REPORTS_DIR + IPath.SEPARATOR + JSON_REPORT_PREFIX + toDelete.getUuid() + ".json");
		if (reportsFile.exists() && !reportsFile.delete()) {
			LOG.error("Could not delete file: " + reportsFile.toString());
		}

		// Update reports.xml with all plug-in info in.
		regenerateReportsPluginXMLFile();
	}

	public void deleteTeamReport(final CustomReportDefinition toDelete) {
		// FIXME: @Farukh to move below code block to correct place in
		// ICustomReportDataRepository implementation file
		final IPath workspaceLocation = ResourcesPlugin.getWorkspace().getRoot().getLocation();
		final File reportsFile = new File(workspaceLocation.toOSString() + IPath.SEPARATOR + TEAM_REPORTS_DIR + IPath.SEPARATOR + toDelete.getUuid() + ".json");
		if (reportsFile.exists() && !reportsFile.delete()) {
			LOG.error("Could not delete file: " + reportsFile.toString());
		}
		// FIXME: end

		// Update reports.xml with all plug-in info in.
		regenerateReportsPluginXMLFile();
	}

	public void refreshTeamReports() throws IOException {
		ServiceHelper.withCheckedServiceConsumer(ICustomReportDataRepository.class, ICustomReportDataRepository::refresh);
	}

	public void removeFromDatahub(final CustomReportDefinition toDelete) throws Exception {
		ServiceHelper.withCheckedServiceConsumer(ICustomReportDataRepository.class, s -> s.removeReport(toDelete));
	}
}
