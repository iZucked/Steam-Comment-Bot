/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2020
 * All rights reserved.
 */
package com.mmxlabs.lngdataserver.integration.reports.custom;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.IPath;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mmxlabs.common.util.CheckedBiConsumer;
import com.mmxlabs.lingo.reports.services.CustomReportPermissions;
import com.mmxlabs.lingo.reports.services.ICustomReportDataRepository;
import com.mmxlabs.lingo.reports.views.schedule.ScheduleSummaryReportDefinition;

public class TopCustomReportDataRepository implements ICustomReportDataRepository {
	
	public static final TopCustomReportDataRepository INSTANCE = new TopCustomReportDataRepository();

	protected final List<Runnable> newLocalVersionCallbacks = new LinkedList<>();

	public TopCustomReportDataRepository() {
		startListenForNewLocalVersions();
	}

	private CustomReportDataRepository.IUpdateListener updateListener = () -> newLocalVersionCallbacks.forEach(Runnable::run);

	@Override
	public void startListenForNewLocalVersions() {
		if (CustomReportPermissions.hasCustomReportPublishPermission()
				|| CustomReportPermissions.hasCustomReportReadPermission()) {
			CustomReportDataRepository.INSTANCE.addListener(updateListener);
		}
	}

	@Override
	public void stopListeningForNewLocalVersions() {
		if (CustomReportPermissions.hasCustomReportPublishPermission()
				|| CustomReportPermissions.hasCustomReportReadPermission()) {
			CustomReportDataRepository.INSTANCE.removeListener(updateListener);
		}
	}

	@Override
	public void registerLocalVersionListener(final Runnable versionConsumer) {
		newLocalVersionCallbacks.add(versionConsumer);
	}

	protected boolean doPublishOutputStream(final ScheduleSummaryReportDefinition reportDefinition, String uuid, CheckedBiConsumer<ScheduleSummaryReportDefinition, OutputStream, IOException> writeAction) throws Exception {

		Path p = Files.createTempFile("hub-", ".data");
		try {
			// Write data to temp file
			try (FileOutputStream os = new FileOutputStream(p.toFile())) {
				writeAction.accept(reportDefinition, os);
			}
			// Upload it
			CustomReportDataRepository.INSTANCE.uploadData(uuid, "application/octet-stream", p.toFile(), null);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		} finally {
			Files.delete(p);
		}
	}

	@Override
	public boolean publishReport(final ScheduleSummaryReportDefinition reportDefinition) throws Exception {
		if (CustomReportPermissions.hasCustomReportPublishPermission()) {
			return doPublishOutputStream(reportDefinition, reportDefinition.getUuid(), CustomReportDataRepository::write);
		}
		return false;
	}

	@Override
	public List<ScheduleSummaryReportDefinition> getTeamReports() throws IOException{
		
		if (CustomReportPermissions.hasCustomReportReadPermission()) {
			CustomReportDataRepository.INSTANCE.pause();

			final List<ScheduleSummaryReportDefinition> results = new ArrayList<ScheduleSummaryReportDefinition>();

			final IPath wsPath = ResourcesPlugin.getWorkspace().getRoot().getLocation();		
			final File directory = new File(wsPath.toOSString() + IPath.SEPARATOR + "team-reports");
			if (!directory.exists()) {
				throw new IOException("Team report directory does not exist!");
			}
			if (!directory.canRead()) {
				throw new IOException("LiNGO is not permitted to read from team reports folder! Check user permissions!");
			}

			final Collection<CustomReportDataRecord> crdr = CustomReportDataRepository.INSTANCE.getRecords();
			final ObjectMapper mapper = new ObjectMapper();
			for(final CustomReportDataRecord record : crdr) {
				final File repofile = new File(directory, String.format("%s.json", record.getUuid()));
				if (repofile.exists()) {
					try {
						final ScheduleSummaryReportDefinition definition 
						= mapper.readValue(repofile, ScheduleSummaryReportDefinition.class);
						results.add(definition);
					} catch (Exception e) {
						CustomReportDataRepository.INSTANCE.resume();
						if (e instanceof IOException) {
							throw e;
						}
					}
				}
			}

			CustomReportDataRepository.INSTANCE.resume();

			return results;
		}
		return Collections.emptyList();
	}

	@Override
	public void refresh() throws IOException {
		if (CustomReportPermissions.hasCustomReportReadPermission()) {
			CustomReportDataRepository.INSTANCE.refresh();
		}
	}

	@Override
	public void removeReport(ScheduleSummaryReportDefinition reportDefinition) throws IOException{
		if (CustomReportPermissions.hasCustomReportDeletePermission()) {
			CustomReportDataRepository.INSTANCE.delete(reportDefinition.getUuid());
		}
	}
}
