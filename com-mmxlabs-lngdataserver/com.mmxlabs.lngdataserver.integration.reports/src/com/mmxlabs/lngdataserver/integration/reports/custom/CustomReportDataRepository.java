/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2020
 * All rights reserved.
 */
package com.mmxlabs.lngdataserver.integration.reports.custom;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.Collection;
import java.util.concurrent.ConcurrentLinkedQueue;

import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.Nullable;
import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;
import org.osgi.framework.FrameworkUtil;
import org.osgi.framework.ServiceReference;
import org.osgi.util.tracker.ServiceTracker;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.mmxlabs.hub.IUpstreamDetailChangedListener;
import com.mmxlabs.hub.UpstreamUrlProvider;
import com.mmxlabs.hub.common.http.WrappedProgressMonitor;
import com.mmxlabs.lingo.reports.customizable.CustomReportDefinition;
import com.mmxlabs.lingo.reports.services.CustomReportPermissions;

public class CustomReportDataRepository {

	public static final int CURRENT_MODEL_VERSION = 1;

	private static final Logger LOGGER = LoggerFactory.getLogger(CustomReportDataRepository.class);

	public static final CustomReportDataRepository INSTANCE = new CustomReportDataRepository();

	private File dataFolder;

	private CustomReportDataServiceClient client;

	private CustomReportDataUpdater updater;
	private ConcurrentLinkedQueue<CustomReportDataRecord> dataList;
	private ConcurrentLinkedQueue<IUpdateListener> listeners = new ConcurrentLinkedQueue<>();


	private final IUpstreamDetailChangedListener upstreamDetailsChangedListener;
	
	private CustomReportDataRepository() {
		dataList = new ConcurrentLinkedQueue<>();
		upstreamDetailsChangedListener = dataList::clear;
		
		if (CustomReportPermissions.hasCustomReportPermission()) {
			start();
		}
	}
	
	

	public Collection<CustomReportDataRecord> getRecords() {
		return dataList;
	}

	private void update(final CustomReportDataRecord record) {
		if (!dataList.contains(record)) {
			dataList.add(record);
			fireListeners();
		}
	}

	private void fireListeners() {
		try {
			listeners.forEach(IUpdateListener::updated);
		} catch (Exception e) {
			LOGGER.error(e.getMessage(), e);
		}

	}

	public @Nullable File getDataFile(final String uuid) {
		return new File(String.format("%s/%s.data", dataFolder.getAbsoluteFile(), uuid));
	}

	public void uploadData(final String uuid, final String contentType, final File dataFile, @Nullable final IProgressMonitor progressMonitor) throws Exception {
		try {

			try {
				updater.pause();
				client.upload(uuid, contentType, dataFile, WrappedProgressMonitor.wrapMonitor(progressMonitor));
			} finally {
				updater.resume();
			}
			try {
				updater.refresh();
			} catch (final Exception e) {
				e.printStackTrace();
			}
		} finally {
			if (progressMonitor != null) {
				progressMonitor.done();
			}
		}
	}

	public void delete(final String uuid) throws IOException {

		client.deleteData(uuid);
		try {
			updater.refresh();
		} catch (final IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public void start() {

		final Bundle bundle = FrameworkUtil.getBundle(CustomReportDataRecord.class);
		final BundleContext bundleContext = bundle.getBundleContext();

		new ServiceTracker<IWorkspace, IWorkspace>(bundleContext, IWorkspace.class, null) {
			private boolean started = false;

			@Override
			public IWorkspace addingService(final ServiceReference<IWorkspace> reference) {
				final IWorkspace workspace = super.addingService(reference);

				if (!started) {
					started = true;
					final IPath workspaceLocation = workspace.getRoot().getLocation();
					final File workspaceLocationFile = workspaceLocation.toFile();

					dataFolder = new File(workspaceLocationFile.getAbsolutePath() + File.separator + "team-reports");
					if (!dataFolder.exists()) {
						dataFolder.mkdirs();
					}

					client = new CustomReportDataServiceClient();
					CustomReportDataRepository.this.updater = new CustomReportDataUpdater(dataFolder, client, CustomReportDataRepository.this::update);
					updater.start();
				}
				this.close();
				return workspace;
			}
		}.open();
	}

	public void stop() {

		UpstreamUrlProvider.INSTANCE.deregisterDetailsChangedLister(upstreamDetailsChangedListener);
		if (updater != null) {
			updater.stop();
		}
		dataList.clear();
	}

	public interface IUpdateListener {
		void updated();
	}

	public void addListener(IUpdateListener l) {
		if (!listeners.contains(l)) {
			listeners.add(l);
		}
	}

	public void removeListener(IUpdateListener l) {
		listeners.remove(l);
	}
	
	public static @NonNull ObjectMapper createObjectMapper() {
		final JsonFactory jsonFactory = new JsonFactory();
		jsonFactory.configure(JsonGenerator.Feature.AUTO_CLOSE_TARGET, false);

		final ObjectMapper objectMapper = new ObjectMapper(jsonFactory);
		objectMapper.registerModule(new Jdk8Module());
		objectMapper.registerModule(new JavaTimeModule());
		return objectMapper;
	}

	public static CustomReportDataRecord read(final InputStream is) throws IOException {
		final ObjectMapper mapper = createObjectMapper();
		final CustomReportModelVersionHeader header = mapper.readValue(is, CustomReportModelVersionHeader.class);
		if (header.getModelVersion() == CURRENT_MODEL_VERSION) {
			return mapper.readValue(is, CustomReportDataRecord.class);
		} else {
			switch (header.getModelVersion()) {
			case 0: {
				try (BufferedReader reader = new BufferedReader(new InputStreamReader(is))) {
				} catch (Exception e) {
					if (e instanceof IOException) {
						throw e;
					}
				}
			}
			default:
				throw new RuntimeException("Unknown model data version");
			}
		}
	}
	
	public static void write(final CustomReportDefinition reportDefinition, final OutputStream os) throws IOException {
		
		final ObjectMapper mapper = new ObjectMapper();
		try {
			mapper.writerWithDefaultPrettyPrinter().writeValue(os, reportDefinition);
		} catch (final Exception e) {
			e.printStackTrace();
		}
		
	}
	
	public void refresh()  throws IOException {
		if (updater != null) {
			updater.pause();
			try {
				updater.refresh();
			} catch(Exception e) {
				updater.resume();
				throw e;
			}
			updater.resume();
		}
	}
	
	public void pause() {
		if (updater != null) {
			updater.pause();
		}
	}
	
	public void resume() {
		if (updater != null) {
			updater.resume();
		}
	}
}
