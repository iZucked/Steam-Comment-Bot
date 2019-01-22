/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2018
 * All rights reserved.
 */
package com.mmxlabs.lngdataserver.integration.ui.scenarios;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.emf.common.util.URI;
import org.eclipse.jdt.annotation.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mmxlabs.lngdataserver.integration.ui.scenarios.api.BaseCaseServiceClient;
import com.mmxlabs.lngdataserver.integration.ui.scenarios.internal.BaseCaseScenarioUpdater;
import com.mmxlabs.lngdataserver.integration.ui.scenarios.internal.BaseCaseVersionsProviderService;
import com.mmxlabs.scenario.service.model.Container;
import com.mmxlabs.scenario.service.model.ScenarioInstance;
import com.mmxlabs.scenario.service.model.ScenarioService;
import com.mmxlabs.scenario.service.model.ScenarioServiceFactory;
import com.mmxlabs.scenario.service.model.manager.IScenarioDataProvider;
import com.mmxlabs.scenario.service.model.manager.ScenarioModelRecord;
import com.mmxlabs.scenario.service.util.AbstractScenarioService;

public class BaseCaseScenarioService extends AbstractScenarioService {

	private static final Logger log = LoggerFactory.getLogger(BaseCaseScenarioService.class);

	public BaseCaseScenarioService( BaseCaseVersionsProviderService versionsProviderService) {
		super("Base case");
		this.versionsProviderService = versionsProviderService;
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

	private BaseCaseScenarioUpdater updater;

	private BaseCaseVersionsProviderService versionsProviderService;

	@Override
	public ScenarioInstance copyInto(Container parent, ScenarioModelRecord tmpRecord, String name, @Nullable IProgressMonitor progressMonitor) throws Exception {

		return null;

	}

	@Override
	public ScenarioInstance copyInto(Container parent, IScenarioDataProvider scenarioDataProvider, String name, @Nullable IProgressMonitor progressMonitor) throws Exception {

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

		client = new BaseCaseServiceClient();

		// Initial model load
		new Thread(() -> {

			getServiceModel();
			setReady();
			this.updater = new BaseCaseScenarioUpdater(serviceModel, baseCaseFolder, client, versionsProviderService);
			updater.start();
		}).start();
	}

	public void stop() {
		if (updater != null) {
			updater.stop();
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
}
