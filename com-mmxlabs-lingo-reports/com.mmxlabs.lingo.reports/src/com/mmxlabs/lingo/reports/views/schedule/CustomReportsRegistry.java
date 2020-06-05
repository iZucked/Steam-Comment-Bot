/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2020
 * All rights reserved.
 */
package com.mmxlabs.lingo.reports.views.schedule;

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
import com.mmxlabs.models.ui.tabular.columngeneration.ColumnBlock;
import com.mmxlabs.rcp.common.ServiceHelper;
import com.mmxlabs.rcp.common.application.E4ModelHelper;

public class CustomReportsRegistry {

	private static final Logger logger = LoggerFactory.getLogger(ScheduleSummaryReport.class);
	
	public static final String USER_REPORTS_DIR = "reports";

	public static final String TEAM_REPORTS_DIR = "team-reports";

	private static final String JSON_REPORT_PREFIX = "report";

	public static final String XML_REPORTS_PLUGIN_FILENAME = "reports.xml";

	private CustomReportsDefaults defaults = new CustomReportsDefaults();

	private StatusLineContributionItem statusLineItem = new StatusLineContributionItem("Custom Reports Status Bar");

	private static CustomReportsRegistry instance = new CustomReportsRegistry();
	
	public static CustomReportsRegistry getInstance() {
		return instance;
	}
	
	private CustomReportsRegistry() {
		//Singleton instance, so do not allow instantiation other than by once when class loaded.
	}
		
	public void setStatusLineManager(IStatusLineManager statusLineManager) {	
		statusLineManager.add(this.statusLineItem);
		ServiceHelper.withCheckedServiceConsumer(ICustomReportDataRepository.class, s -> s.registerLocalVersionListener(new Runnable() {
			@Override
			public void run() {
				//Execute within the UI thread, but don't wait until complete (execute asynchronously).
				Display.getDefault().asyncExec(new Runnable() {
					public void run() {
						statusLineItem.setText("New team custom reports available - restart of LiNGO required to complete update.");
					}
				});
			}
		}));
	}

	public void regenerateReportsPluginXMLFile() {
		try {
			//Check if reports directory exists, if not create it.
			checkReportsDirectoryExistsOrCreate();
			List<ScheduleSummaryReportDefinition> userReportDefinitions = readUserCustomReportDefinitions();
			List<ScheduleSummaryReportDefinition> teamReportDefinitions = readTeamCustomReportDefinitions();
			List<ScheduleSummaryReportDefinition> reportDefinitions = new ArrayList<>(userReportDefinitions.size()+teamReportDefinitions.size());
			reportDefinitions.addAll(userReportDefinitions);
			reportDefinitions.addAll(teamReportDefinitions);
			writeReportsPluginXMLFile(reportDefinitions);
		}
		catch (Exception ex) {
			logger.error("Error updating report definition plugin.xml", ex);
		}
	}

	private void writeReportsPluginXMLFile(List<ScheduleSummaryReportDefinition> reportDefinitions) throws FileNotFoundException {
		File reportsPluginXMLFile = new File(getReportsPluginXMLPath());

		PrintStream out = new PrintStream(reportsPluginXMLFile);

		writePluginXMLHeader(out);
		
		//Write out 
		for (ScheduleSummaryReportDefinition rd : reportDefinitions) {
			writePluginXMLExtensionInitialState(out, rd);
		}

		//Write out views section
		for (ScheduleSummaryReportDefinition rd : reportDefinitions) {
			writePluginXMLExtensionView(out, rd);
		}
		
		writePluginXMLFooter(out);
		
		out.close();
	}

	public static String getReportsPluginXMLPath() {
		final IPath workspaceLocation = ResourcesPlugin.getWorkspace().getRoot().getLocation();		
		return workspaceLocation.toOSString()+IPath.SEPARATOR+USER_REPORTS_DIR+IPath.SEPARATOR+XML_REPORTS_PLUGIN_FILENAME;
	}

	public List<ScheduleSummaryReportDefinition> readUserCustomReportDefinitions() {
		return readCustomReportDefinitions(USER_REPORTS_DIR);
	}
	
	public List<ScheduleSummaryReportDefinition> readTeamCustomReportDefinitions() {
		List<ScheduleSummaryReportDefinition> reports;
		try {
			reports = ServiceHelper.withCheckedService(ICustomReportDataRepository.class, s -> s.getTeamReports());
		}
		catch (Throwable ex) {
			logger.error("Something went wrong reading team reports", ex);
			reports = Collections.emptyList();
		}
		return reports;
	}

	/**
	 * Whether to display the option of viewing team reports or not.
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
	
	public List<ScheduleSummaryReportDefinition> readCustomReportDefinitions(String reportsDirShortName) {
		//Read in all report definitions.
		final IPath workspaceLocation = ResourcesPlugin.getWorkspace().getRoot().getLocation();		
		File reportsDir = new File(workspaceLocation.toOSString()+IPath.SEPARATOR+reportsDirShortName);
		List<ScheduleSummaryReportDefinition> reportDefinitions = new ArrayList<>();

		if (reportsDir.exists()) {
			File [] files = reportsDir.listFiles(new FilenameFilter() {
				@Override
				public boolean accept(File dir, String name) {
					return name.endsWith(".json") && !name.equalsIgnoreCase("records.json");
				}
			});

			ObjectMapper mapper = new ObjectMapper();

			for (File reportJsonFile : files) {
				//Read in json object.
				try {
					ScheduleSummaryReportDefinition reportDefinition = mapper.readValue(reportJsonFile, ScheduleSummaryReportDefinition.class);
					reportDefinitions.add(reportDefinition);
				} catch (Exception e) {
					logger.error("Problem reading file "+reportJsonFile.getName(), e);
				}
			}
		}
		
		return reportDefinitions;
	}
	
	private void writePluginXMLExtensionView(PrintStream out, ScheduleSummaryReportDefinition rd) {
		out.print("    <extension\r\n" + 
		"          point=\"org.eclipse.ui.views\">\r\n" + 
		"       <view\r\n" + 
		"             category=\"com.mmxlabs.shiplingo.platform.reports\"\r\n" + 
		"             class=\"org.ops4j.peaberry.eclipse.GuiceExtensionFactory:com.mmxlabs.lingo.reports.views.schedule.ScheduleSummaryReport\"\r\n" + 
		"             icon=\"icons/cview16/exec_statistic_view.gif\"\r\n" + 
		"             id=\"");
		out.print(rd.getUuid());
		out.print("\"\r\n" + 
		"             name=\"");
		out.print(rd.getName());
		out.print("\"\r\n" + 
		"             restorable=\"true\">\r\n" + 
		"       </view>\r\n");
		out.print("    </extension>\r\n");
	}

	private void writePluginXMLFooter(PrintStream out) {
		out.print(" </plugin>");
	}

	private void writePluginXMLHeader(PrintStream out) {
		out.print("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\r\n" + 
				"<!--\r\n" + 
				"\r\n" + 
				"    Copyright (C) Minimax Labs Ltd., 2010 - 2020\r\n" + 
				"    All rights reserved.\r\n" + 
				"\r\n" + 
				"-->\r\n" + 
				"<?eclipse version=\"3.4\"?>\r\n" + 
				"<plugin>\r\n");
	}
	
	private void writePluginXMLExtensionInitialState(PrintStream out, ScheduleSummaryReportDefinition ssrd) {
		out.print("    <extension\r\n" + 
				"          point=\"com.mmxlabs.lingo.reports.ScheduleBasedReportInitialState\">\r\n" + 
				"       <ScheduleBasedReportInitialState\r\n" + 
				"             customisable=\"false\"\r\n" + 
				"             id=\"");
		out.print(ssrd.getUuid());
		out.print("\">\r\n"); 
		
		for (String rowFilter : ssrd.getFilters()) {
			out.print("         <InitialRowType\r\n"); 
			out.print("               rowType=\"");
		    out.print(rowFilter);
			out.print("\"/>\r\n"); 
		}
		
		for (String column : ssrd.getColumns()) {
			out.print("         <InitialColumn\r\n");  
			out.print("               id=\"");
			out.print(column);
			out.print("\"/>\r\n");
		}
		
		for (String diffOption : ssrd.getDiffOptions()) {
			out.print("         <InitialDiffOption\r\n"); 
			out.print("               option=\"");
			out.print(diffOption);
			out.print("\"/>\r\n"); 
		}
		
		out.print("       </ScheduleBasedReportInitialState>\r\n");
		out.print("    </extension>\r\n");
	}
	
	public void writeToJSON(ScheduleSummaryReportDefinition rd) {
		//Check if reports directory exists, if not create it.
		checkReportsDirectoryExistsOrCreate();
		
		//Write report down to reportX.json
		writeCustomReportViewToJSON(rd);
				
		//Update reports.xml with all plug-in info in.
		regenerateReportsPluginXMLFile();
	}


	private void writeCustomReportViewToJSON(ScheduleSummaryReportDefinition jsonObject) {
		final IPath workspaceLocation = ResourcesPlugin.getWorkspace().getRoot().getLocation();		
		File reportsFile = new File(workspaceLocation.toOSString()+IPath.SEPARATOR+USER_REPORTS_DIR+IPath.SEPARATOR+JSON_REPORT_PREFIX+jsonObject.getUuid()+".json");

		ObjectMapper mapper = new ObjectMapper();
		try {  
			mapper.writeValue(reportsFile, jsonObject);
		} catch (IOException e) {  
			logger.error("Problem saving to json report file: ", e);
		}  
	}

	private void checkReportsDirectoryExistsOrCreate() {
		final IPath workspaceLocation = ResourcesPlugin.getWorkspace().getRoot().getLocation();
		
		File reportsDirectory = new File(workspaceLocation.toOSString()+IPath.SEPARATOR+USER_REPORTS_DIR);
		if (!(reportsDirectory.exists() && reportsDirectory.isDirectory())) {
			reportsDirectory.mkdir();
		}
	}

	public void removeDeletedViews(@NonNull MApplication application, @NonNull EModelService modelService) {
		List<ScheduleSummaryReportDefinition> reportDefinitions = readUserCustomReportDefinitions();
		Set<String> customReportIds = new HashSet<>();
		for (ScheduleSummaryReportDefinition rd : reportDefinitions) {
			customReportIds.add(rd.getUuid());
		}

		Set<String> reportIdsToRemove = new HashSet<>();
		final Iterator<MPartDescriptor> descriptorItr = application.getDescriptors().iterator();
		while (descriptorItr.hasNext()) {
			final MPartDescriptor descriptor = descriptorItr.next();
			String id = descriptor.getElementId();
			if (id.startsWith(ScheduleSummaryReport.UUID_PREFIX)) {
				reportIdsToRemove.add(id);
			}
		}

		for (String id : reportIdsToRemove) {
			E4ModelHelper.removeViewPart(id, application, modelService);
		}
	}
	
	public void publishReport(final ScheduleSummaryReportDefinition reportDefinition) throws Exception {
		if (!ServiceHelper.withCheckedService(ICustomReportDataRepository.class, s -> s.publishReport(reportDefinition))) {
			throw new IOException("Error publishing report to datahub");
		}
	}

	public List<ColumnBlock> getColumnDefinitions() {
		return defaults.getColumnDefinitions();
	}
	
	public boolean isBlockVisible(ColumnBlock column) {
		return defaults.isVisible(column);
	}
	
	public CustomReportsDefaults getDefaults() {
		return defaults;
	}

	public int getBlockIndex(ColumnBlock columnObj) {
		return defaults.getBlockManager().getBlockIndex((ColumnBlock) columnObj);
	}

	public void delete(ScheduleSummaryReportDefinition toDelete) {
		final IPath workspaceLocation = ResourcesPlugin.getWorkspace().getRoot().getLocation();		
		File reportsFile = new File(workspaceLocation.toOSString()+IPath.SEPARATOR+USER_REPORTS_DIR+IPath.SEPARATOR+JSON_REPORT_PREFIX+toDelete.getUuid()+".json");
		if (!reportsFile.delete()) {
			logger.error("Could not delete file: "+reportsFile.toString());
		}
		
		//Update reports.xml with all plug-in info in.
		regenerateReportsPluginXMLFile();
	}
	
	public void refreshTeamReports() throws IOException {
		ServiceHelper.withCheckedServiceConsumer(ICustomReportDataRepository.class, s -> s.refresh());
	}
	
	public void removeFromDatahub(ScheduleSummaryReportDefinition toDelete) throws Exception {
		ServiceHelper.withCheckedServiceConsumer(ICustomReportDataRepository.class, s -> s.removeReport(toDelete));
	}
}
