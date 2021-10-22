/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2021
 * All rights reserved.
 */
package com.mmxlabs.lngdataserver.integration.ui.scenarios.cloud;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Collection;
import java.util.concurrent.ConcurrentLinkedQueue;

import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.runtime.IPath;
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
import com.mmxlabs.hub.common.http.IProgressListener;

public class CloudOptimisationDataRepository {

	public static final int CURRENT_MODEL_VERSION = 1;

	private static final Logger LOGGER = LoggerFactory.getLogger(CloudOptimisationDataRepository.class);

	public static final CloudOptimisationDataRepository INSTANCE = new CloudOptimisationDataRepository();

	private File dataFolder;

	private CloudOptimisationDataServiceClient client;

	private CloudOptimisationDataUpdater updater;
	private ConcurrentLinkedQueue<CloudOptimisationResultDataRecord> dataList;
	private ConcurrentLinkedQueue<IUpdateListener> listeners = new ConcurrentLinkedQueue<>();

	private final IUpstreamDetailChangedListener upstreamDetailsChangedListener;
	
	private CloudOptimisationDataRepository() {
		dataList = new ConcurrentLinkedQueue<>();
		upstreamDetailsChangedListener = dataList::clear;
		start();
	}

	public Collection<CloudOptimisationResultDataRecord> getRecords() {
		return dataList;
	}

	private void update(final CloudOptimisationResultDataRecord record) {
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
	
	public String uploadData(final File dataFile, //
			final String checksum, //
			final String scenarioName, //
			final IProgressListener progressListener) throws Exception {
		String response = null;
		try {

			try {
				updater.pause();
				response = //
						client.upload(dataFile, checksum, scenarioName, progressListener);
			} catch (final Exception e){
				LOGGER.error(e.getMessage());
			} finally {
				updater.resume();
			}
			
			try {
				updater.pause();
				updater.refresh();
			} finally {
				updater.resume();
			}
		} catch (Exception e) {
			throw e;
		}
		return response;
	}

	public void start() {

		final Bundle bundle = FrameworkUtil.getBundle(CloudOptimisationResultDataRecord.class);
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

					client = new CloudOptimisationDataServiceClient();
					updater = new CloudOptimisationDataUpdater(dataFolder, client, CloudOptimisationDataRepository.this::update);
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

	public static CloudOptimisationResultDataRecord read(final InputStream is) throws IOException {
		final ObjectMapper mapper = createObjectMapper();
		return mapper.readValue(is, CloudOptimisationResultDataRecord.class);
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
