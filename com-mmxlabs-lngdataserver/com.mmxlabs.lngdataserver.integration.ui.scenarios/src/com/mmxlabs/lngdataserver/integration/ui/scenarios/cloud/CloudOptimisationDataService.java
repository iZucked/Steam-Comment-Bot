/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2021
 * All rights reserved.
 */
package com.mmxlabs.lngdataserver.integration.ui.scenarios.cloud;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ConcurrentLinkedQueue;

import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.runtime.IPath;
import org.eclipse.emf.common.util.URI;
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
import com.google.common.base.Charsets;
import com.mmxlabs.hub.IUpstreamDetailChangedListener;
import com.mmxlabs.hub.UpstreamUrlProvider;
import com.mmxlabs.hub.common.http.IProgressListener;
import com.mmxlabs.rcp.common.ServiceHelper;
import com.mmxlabs.scenario.service.manifest.Manifest;
import com.mmxlabs.scenario.service.model.Metadata;
import com.mmxlabs.scenario.service.model.ScenarioInstance;
import com.mmxlabs.scenario.service.model.ScenarioServiceFactory;
import com.mmxlabs.scenario.service.model.manager.SSDataManager;
import com.mmxlabs.scenario.service.model.manager.ScenarioModelRecord;
import com.mmxlabs.scenario.service.model.manager.ScenarioStorageUtil;
import com.mmxlabs.scenario.service.model.util.encryption.IScenarioCipherProvider;
import com.mmxlabs.scenario.service.model.util.encryption.ScenarioEncryptionException;

public class CloudOptimisationDataService {

	public static final int CURRENT_MODEL_VERSION = 1;

	private static final Logger LOGGER = LoggerFactory.getLogger(CloudOptimisationDataService.class);

	public static final CloudOptimisationDataService INSTANCE = new CloudOptimisationDataService();

	private File dataFolder;

	private CloudOptimisationDataServiceClient client;

	private CloudOptimisationDataUpdater updater;
	private ConcurrentLinkedQueue<CloudOptimisationDataResultRecord> dataList;
	private ConcurrentLinkedQueue<IUpdateListener> listeners = new ConcurrentLinkedQueue<>();
	// Set used to emit load failure messages once
		private Set<String> warnedLoadFailures = new HashSet<>();

	private final IUpstreamDetailChangedListener upstreamDetailsChangedListener;
	
	protected ScenarioInstance loadScenarioFrom(final File f, final CloudOptimisationDataResultRecord record, final String scenarioname) {
		final URI archiveURI = URI.createFileURI(f.getAbsolutePath());
		final Manifest manifest = ScenarioStorageUtil.loadManifest(f);
		if (manifest != null) {
			final ScenarioInstance scenarioInstance = ScenarioServiceFactory.eINSTANCE.createScenarioInstance();
			scenarioInstance.setReadonly(true);
			scenarioInstance.setUuid(manifest.getUUID());
			scenarioInstance.setExternalID(record.getUuid());

			scenarioInstance.setRootObjectURI(archiveURI.toString());

			scenarioInstance.setName(scenarioname);
			scenarioInstance.setVersionContext(manifest.getVersionContext());
			scenarioInstance.setScenarioVersion(manifest.getScenarioVersion());

			scenarioInstance.setClientVersionContext(manifest.getClientVersionContext());
			scenarioInstance.setClientScenarioVersion(manifest.getClientScenarioVersion());

			final Metadata meta = ScenarioServiceFactory.eINSTANCE.createMetadata();
			meta.setCreator(record.getCreator());
			meta.setCreated(Date.from(record.getCreationDate()));

			scenarioInstance.setMetadata(meta);
			meta.setContentType(manifest.getScenarioType());
			// Probably better pass in from service
			ServiceHelper.withOptionalServiceConsumer(IScenarioCipherProvider.class, scenarioCipherProvider -> {
				try {
					final ScenarioModelRecord modelRecord = ScenarioStorageUtil.loadInstanceFromURIChecked(archiveURI, true, false, false, scenarioCipherProvider);
					if (modelRecord != null) {
						modelRecord.setName(scenarioInstance.getName());
						modelRecord.setScenarioInstance(scenarioInstance);
						SSDataManager.Instance.register(scenarioInstance, modelRecord);
						scenarioInstance.setRootObjectURI(archiveURI.toString());
					}
				} catch (ScenarioEncryptionException e) {
					LOGGER.error(e.getMessage(), e);
				} catch (Exception e) {
					LOGGER.error(e.getMessage(), e);
				}
			});
			return scenarioInstance;
		}

		if (warnedLoadFailures.add(f.getName())) {
			LOGGER.error("Error reading team scenario file {}. Check encryption certificate.", f.getName());
		}
		return null;
	}
	
	private CloudOptimisationDataService() {
		dataList = new ConcurrentLinkedQueue<>();
		upstreamDetailsChangedListener = dataList::clear;
		start();
	}

	public Collection<CloudOptimisationDataResultRecord> getRecords() {
		return dataList;
	}

	private void update(final CloudOptimisationDataResultRecord record) {
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
	
	public boolean updateRecords(final CloudOptimisationDataResultRecord record) {
		updater.pause();
		if (!dataList.contains(record)) {
			final File recordsFile = new File(dataFolder.getAbsolutePath() + IPath.SEPARATOR + "records.json");
			final String json = CloudOptimisationDataServiceClient.getJSON(Collections.singletonList(record));
			if (json != null) {
				try {
					Files.writeString(recordsFile.toPath(), json, Charsets.UTF_8);
				} catch (IOException e) {
					e.printStackTrace();
					return false;
				}
			}
			dataList.add(record);
		}
		updater.resume();
		return true;
	}

	public void start() {

		final Bundle bundle = FrameworkUtil.getBundle(CloudOptimisationDataResultRecord.class);
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

					dataFolder = new File(workspaceLocationFile.getAbsolutePath() + File.separator + "cloud-opti");
					if (!dataFolder.exists()) {
						dataFolder.mkdirs();
					}

					client = new CloudOptimisationDataServiceClient();
					updater = new CloudOptimisationDataUpdater(dataFolder, client, CloudOptimisationDataService.this::update);
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

	public static CloudOptimisationDataResultRecord read(final InputStream is) throws IOException {
		final ObjectMapper mapper = createObjectMapper();
		return mapper.readValue(is, CloudOptimisationDataResultRecord.class);
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
