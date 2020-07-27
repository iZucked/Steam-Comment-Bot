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
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.IPath;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mmxlabs.common.util.CheckedBiConsumer;
import com.mmxlabs.lingo.reports.customizable.CustomReportDefinition;
import com.mmxlabs.lingo.reports.services.CustomReportPermissions;
import com.mmxlabs.lingo.reports.services.ICustomReportDataRepository;

public class TopCustomReportDataRepository implements ICustomReportDataRepository {
	
	public static final TopCustomReportDataRepository INSTANCE = new TopCustomReportDataRepository();
	private static final Logger LOGGER = LoggerFactory.getLogger(TopCustomReportDataRepository.class);
	
	private boolean isFirst = true;
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

	protected boolean doPublishOutputStream(final CustomReportDefinition reportDefinition, String uuid, CheckedBiConsumer<CustomReportDefinition, OutputStream, IOException> writeAction) throws Exception {

		Path p = Files.createTempFile("hub-", ".data");
		try {
			// Write data to temp file
			try (FileOutputStream os = new FileOutputStream(p.toFile())) {
				writeAction.accept(reportDefinition, os);
			}
			// Upload it
			return CustomReportDataRepository.INSTANCE.uploadData(uuid, "application/octet-stream", p.toFile(), null);
		} catch (Exception e) {
			LOGGER.error(e.getMessage());
			return false;
		} finally {
			Files.delete(p);
		}
	}

	@Override
	public boolean publishReport(final CustomReportDefinition reportDefinition) throws Exception {
		if (CustomReportPermissions.hasCustomReportPublishPermission()) {
			return doPublishOutputStream(reportDefinition, reportDefinition.getUuid(), CustomReportDataRepository::write);
		}
		return false;
	}

	@Override
	public List<CustomReportDefinition> getTeamReports() throws IOException{
		
		final List<CustomReportDefinition> results = new ArrayList<>();
		final IPath wsPath = ResourcesPlugin.getWorkspace().getRoot().getLocation();		
		final File directory = new File(wsPath.toOSString() + IPath.SEPARATOR + "team-reports");
		if (!directory.exists()) {
			return Collections.emptyList();
		} else if (!directory.canRead()) {
			throw new IOException(String.format(//
					"LiNGO is not permitted to read from team reports folder - %s! Check user permissions!",//
					directory.getAbsolutePath()));
		}

		final Collection<CustomReportDataRecord> records = getRecordsWithCacheOptionality(directory);

		CustomReportDataRepository.INSTANCE.pause();

		final ObjectMapper mapper = new ObjectMapper();
		for(final CustomReportDataRecord record : records) {
			try {
				CustomReportDefinition customReport = readReportJSONFile(directory, mapper, record);
				if (customReport != null) {
					results.add(customReport);
				}
			} catch (IOException e) {
				CustomReportDataRepository.INSTANCE.resume();
				throw e;
			}
		}

		CustomReportDataRepository.INSTANCE.resume();

		return results;
	}

	private Collection<CustomReportDataRecord> getRecordsWithCacheOptionality(final File directory) throws IOException {
		final Collection<CustomReportDataRecord> records = new ArrayList<>();
		final File recordsFile = new File(directory.getAbsolutePath() + "/records.json");
		final File recordsFileBKP = new File(directory.getAbsolutePath() + "/records.json.bkp");
		if (CustomReportPermissions.hasCustomReportReadPermission()) {
			final Collection<CustomReportDataRecord> temp = CustomReportDataRepository.INSTANCE.getRecords();
			if (temp != null && !temp.isEmpty()) {
				records.addAll(temp);
				isFirst = false;
			}
		}
		
		if (((isFirst || !CustomReportPermissions.isOnline()) && records.isEmpty()) && (!recordsFile.exists() && recordsFileBKP.exists())) {
			Files.copy(recordsFileBKP.toPath(), recordsFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
		}
		
		if (((isFirst || !CustomReportPermissions.isOnline()) && records.isEmpty()) && recordsFile.exists() && recordsFile.canRead()) {
			String json = Files.readString(recordsFile.toPath());
			final List<CustomReportDataRecord> temp = CustomReportDataServiceClient.parseRecordsJSONData(json);
			if (temp != null) {
				records.addAll(temp);
			}
			isFirst = false;
		}
		return records;
	}

	private synchronized CustomReportDefinition readReportJSONFile(final File directory, //
			final ObjectMapper mapper, final CustomReportDataRecord record) throws IOException {

		CustomReportDefinition definition = null;
		final File repofile = new File(directory, String.format("%s.json", record.getUuid()));
		if (repofile.exists() && repofile.canRead()) {
			definition = //
					mapper.readValue(repofile, CustomReportDefinition.class);
		}
		return definition;
	}

	@Override
	public void refresh() throws IOException {
		if (CustomReportPermissions.hasCustomReportReadPermission()) {
			CustomReportDataRepository.INSTANCE.refresh();
		}
	}

	@Override
	public void removeReport(CustomReportDefinition reportDefinition) throws IOException{
		if (CustomReportPermissions.hasCustomReportDeletePermission()) {
			CustomReportDataRepository.INSTANCE.delete(reportDefinition.getUuid());
		}
	}
}
