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
import java.util.List;
import java.util.concurrent.ConcurrentLinkedQueue;

import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
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
import com.mmxlabs.rcp.common.locking.WellKnownTriggers;
import com.mmxlabs.scenario.service.model.Container;
import com.mmxlabs.scenario.service.model.ScenarioInstance;
import com.mmxlabs.scenario.service.model.ScenarioService;
import com.mmxlabs.scenario.service.model.ScenarioServiceFactory;
import com.mmxlabs.scenario.service.model.manager.IScenarioDataProvider;
import com.mmxlabs.scenario.service.model.manager.ScenarioModelRecord;
import com.mmxlabs.scenario.service.util.AbstractScenarioService;

public class CloudOptimisationDataService extends AbstractScenarioService {

	public static final int CURRENT_MODEL_VERSION = 1;

	private static final Logger LOGGER = LoggerFactory.getLogger(CloudOptimisationDataService.class);
	public static final CloudOptimisationDataService INSTANCE = new CloudOptimisationDataService();

	private File dataFolder;
	private String serviceName;

	private CloudOptimisationDataServiceClient client;
	private CloudOptimisationDataUpdater updater;
	
	private ConcurrentLinkedQueue<CloudOptimisationDataResultRecord> dataList;
	private ConcurrentLinkedQueue<IUpdateListener> listeners = new ConcurrentLinkedQueue<>();
	// Set used to emit load failure messages once
	
	private final IUpstreamDetailChangedListener upstreamDetailsChangedListener;
	
	public CloudOptimisationDataService() {
		super("Online Tasks");
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
	
	public synchronized boolean updateRecords(final CloudOptimisationDataResultRecord record) {
		updater.pause();
		if (!dataList.contains(record)) {
			final File recordsFile = new File(dataFolder.getAbsolutePath() + IPath.SEPARATOR + "records.json");
			String json = "";
			if (recordsFile.exists()) {
				try {
					json = Files.readString(recordsFile.toPath());
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			final List<CloudOptimisationDataResultRecord> records = CloudOptimisationDataServiceClient.parseRecordsJSONData(json);
			records.add(record);
			final String output = CloudOptimisationDataServiceClient.getJSON(records);
			if (output != null) {
				try {
					Files.writeString(recordsFile.toPath(), output, Charsets.UTF_8);
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
					
					getServiceModel();
					setReady();
					WellKnownTriggers.WORKSPACE_DATA_ENCRYPTION_CHECK.delayUntilTriggered(() -> {

						// Initial model load
						new Thread(() -> {

							getServiceModel();
							setReady();
							updater = new CloudOptimisationDataUpdater(dataFolder, client, serviceModel, CloudOptimisationDataService.this::update);
							updater.start();
						}).start();
					});
//					updater = new CloudOptimisationDataUpdater(dataFolder, client, serviceModel, CloudOptimisationDataService.this::update);
//					updater.start();
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

	@Override
	public void delete(@NonNull Container container) {
		// do nothing
	}

	@Override
	public void moveInto(@NonNull List<Container> elements, @NonNull Container destination) {
		// do nothing
	}

	@Override
	public void makeFolder(@NonNull Container parent, @NonNull String name) {
		// do nothing
	}

	@Override
	public String getSerivceID() {
		return "cloud-workspace-" + serviceName;
	}

	@Override
	public ScenarioInstance copyInto(@NonNull Container parent, @NonNull ScenarioModelRecord tmpRecord, @NonNull String name, @NonNull IProgressMonitor progressMonitor) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ScenarioInstance copyInto(@NonNull Container parent, @NonNull IScenarioDataProvider scenarioDataProvider, @NonNull String name, @NonNull IProgressMonitor progressMonitor) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void fireEvent(final ScenarioServiceEvent event, final ScenarioInstance scenarioInstance) {
		super.fireEvent(event, scenarioInstance);
	}

	@Override
	protected ScenarioService initServiceModel() {
		final ScenarioService serviceModel = ScenarioServiceFactory.eINSTANCE.createScenarioService();
		serviceModel.setName(serviceName);
		serviceModel.setDescription("Online tasks results");
		serviceModel.setLocal(false);
		serviceModel.setOffline(false);
		serviceModel.setServiceID(getSerivceID());
		return serviceModel;
	}

	@Override
	public URI resolveURI(final String uri) {
		return URI.createURI(uri);
	}
}
