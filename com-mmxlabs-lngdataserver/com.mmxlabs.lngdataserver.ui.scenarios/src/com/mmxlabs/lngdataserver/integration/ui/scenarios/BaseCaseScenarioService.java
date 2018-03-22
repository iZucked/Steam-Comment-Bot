/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2018
 * All rights reserved.
 */
package com.mmxlabs.lngdataserver.integration.ui.scenarios;

import java.io.File;
import java.io.IOException;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.List;

import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.IPath;
import org.eclipse.emf.common.util.URI;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mmxlabs.lngdataserver.integration.ui.scenarios.api.BaseCaseServiceClient;
import com.mmxlabs.rcp.common.RunnerHelper;
import com.mmxlabs.scenario.service.manifest.Manifest;
import com.mmxlabs.scenario.service.model.Container;
import com.mmxlabs.scenario.service.model.Metadata;
import com.mmxlabs.scenario.service.model.ScenarioInstance;
import com.mmxlabs.scenario.service.model.ScenarioService;
import com.mmxlabs.scenario.service.model.ScenarioServiceFactory;
import com.mmxlabs.scenario.service.model.manager.IScenarioDataProvider;
import com.mmxlabs.scenario.service.model.manager.SSDataManager;
import com.mmxlabs.scenario.service.model.manager.ScenarioModelRecord;
import com.mmxlabs.scenario.service.model.manager.ScenarioStorageUtil;
import com.mmxlabs.scenario.service.util.AbstractScenarioService;

public class BaseCaseScenarioService extends AbstractScenarioService {

	private static final Logger log = LoggerFactory.getLogger(BaseCaseScenarioService.class);

	public BaseCaseScenarioService() {
		super("Base case");
	}

	/**
	 * Root of the directory tree
	 */
	private File dataPath;

	/**
	 * Name of this service
	 */
	private String serviceName;

	private File baseCaseFolder;

	private BaseCaseServiceClient client;

	@Override
	public ScenarioInstance copyInto(Container parent, ScenarioModelRecord tmpRecord, String name) throws Exception {

		return null;

	}

	@Override
	public ScenarioInstance copyInto(Container parent, IScenarioDataProvider scenarioDataProvider, String name) throws Exception {

		return null;

	}

	@Override
	public void delete(final Container container) {

	}
	@Override
	public void fireEvent(ScenarioServiceEvent event, ScenarioInstance scenarioInstance) {
		super.fireEvent(event, scenarioInstance);
	}
	
	@Override
	protected ScenarioService initServiceModel() {
		final ScenarioService serviceModel = ScenarioServiceFactory.eINSTANCE.createScenarioService();
		serviceModel.setName(serviceName);
		serviceModel.setDescription("Shared base cases");
		serviceModel.setLocal(false);
		serviceModel.setServiceID(getSerivceID());
		// serviceModel.eAdapters().add(serviceModelAdapter);

		return serviceModel;
	}

	@Override
	public URI resolveURI(final String uri) {
		return URI.createURI(uri);
	}

	public void start() throws IOException {

		final IPath workspaceLocation = ResourcesPlugin.getWorkspace().getRoot().getLocation();
		File workspaceLocationFile = workspaceLocation.toFile();

		baseCaseFolder = new File(workspaceLocationFile.getAbsolutePath() + File.separator + "scenarios" + File.separator + "basecases");
		if (!baseCaseFolder.exists()) {
			baseCaseFolder.mkdirs();
		}

		// storeURI = URI.createFileURI(baseCaseFolder.getCanonicalPath());

		client = new BaseCaseServiceClient();

		// Initial model load
		new Thread(() -> {

			getServiceModel();
			setReady();
			client.start(baseCaseFolder, (f, creationDate) -> {
				ScenarioInstance instance = constructInstance(f);
				ZonedDateTime date = creationDate.atZone(ZoneId.of("UTC"));
				instance.setName(String.format("Basecase %04d-%02d-%02d %02d:%02d", date.getYear(), date.getMonthValue(), date.getDayOfMonth(), date.getHour(), date.getMinute()));
				if (instance != null) {
					RunnerHelper.asyncExec(() -> {
						serviceModel.getElements().add(instance);
					});
				}
			});
		}).start();

		// final String path = d.get("path").toString();
		// serviceName = d.get("serviceName").toString();

		// Convert to absolute path

	}

	public void stop() {
		if (client != null) {
			client.stop();
		}
	}

	@Override
	public void moveInto(final List<Container> elements, final Container destination) {
		return;
	}

	@Override
	public void makeFolder(final Container parent, final String name) {
		// Not supported...
		return;
	}

	@Override
	public String getSerivceID() {
		return "base-case-" + serviceName;
	}

	private ScenarioInstance constructInstance(File f) {
		URI archiveURI = URI.createFileURI(f.getAbsolutePath());
		final Manifest manifest = ScenarioStorageUtil.loadManifest(f, getScenarioCipherProvider());
		if (manifest != null) {
			final ScenarioInstance scenarioInstance = ScenarioServiceFactory.eINSTANCE.createScenarioInstance();
			scenarioInstance.setReadonly(true);
			scenarioInstance.setUuid(manifest.getUUID());

			final URI fileURI = URI.createFileURI(f.getAbsolutePath());
			scenarioInstance.setRootObjectURI(archiveURI.toString());

			final String scenarioname = f.getName().replaceFirst("\\.lingo$", "");
			scenarioInstance.setName(scenarioname);
			scenarioInstance.setVersionContext(manifest.getVersionContext());
			scenarioInstance.setScenarioVersion(manifest.getScenarioVersion());

			scenarioInstance.setClientVersionContext(manifest.getClientVersionContext());
			scenarioInstance.setClientScenarioVersion(manifest.getClientScenarioVersion());

			final Metadata meta = ScenarioServiceFactory.eINSTANCE.createMetadata();
			scenarioInstance.setMetadata(meta);
			meta.setContentType(manifest.getScenarioType());

			ScenarioModelRecord modelRecord = ScenarioStorageUtil.loadInstanceFromURI(archiveURI, true, false, false, getScenarioCipherProvider());
			if (modelRecord != null) {
				modelRecord.setName(scenarioInstance.getName());
				modelRecord.setScenarioInstance(scenarioInstance);
				SSDataManager.Instance.register(scenarioInstance, modelRecord);
				scenarioInstance.setRootObjectURI(archiveURI.toString());
			}

			return scenarioInstance;
		}
		return null;
	}
}
