/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2021
 * All rights reserved.
 */
package com.mmxlabs.lngdataserver.integration.ui.scenarios;

import java.io.File;
import java.util.List;

import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.emf.common.util.URI;
import org.eclipse.jdt.annotation.Nullable;

import com.mmxlabs.lngdataserver.integration.ui.scenarios.api.BaseCaseServiceClient;
import com.mmxlabs.lngdataserver.integration.ui.scenarios.internal.BaseCaseScenarioUpdater;
import com.mmxlabs.lngdataserver.integration.ui.scenarios.internal.BaseCaseVersionsProviderService;
import com.mmxlabs.rcp.common.locking.WellKnownTriggers;
import com.mmxlabs.scenario.service.model.Container;
import com.mmxlabs.scenario.service.model.ScenarioInstance;
import com.mmxlabs.scenario.service.model.ScenarioService;
import com.mmxlabs.scenario.service.model.ScenarioServiceFactory;
import com.mmxlabs.scenario.service.model.manager.IScenarioDataProvider;
import com.mmxlabs.scenario.service.model.manager.ScenarioModelRecord;
import com.mmxlabs.scenario.service.util.AbstractScenarioService;

public class BaseCaseScenarioService extends AbstractScenarioService {

	public BaseCaseScenarioService(final BaseCaseVersionsProviderService versionsProviderService) {
		super("Base case");
		this.versionsProviderService = versionsProviderService;
	}

	/**
	 * Name of this service
	 */
	private String serviceName;

	private BaseCaseScenarioUpdater updater;

	private final BaseCaseVersionsProviderService versionsProviderService;

	@Override
	public ScenarioInstance copyInto(final Container parent, final ScenarioModelRecord tmpRecord, final String name, @Nullable final IProgressMonitor progressMonitor) throws Exception {
		// Silently ignore
		return null;

	}

	@Override
	public ScenarioInstance copyInto(final Container parent, final IScenarioDataProvider scenarioDataProvider, final String name, @Nullable final IProgressMonitor progressMonitor) throws Exception {
		// Silently ignore
		return null;

	}

	@Override
	public void delete(final Container container) {
		// Silently ignore
	}

	@Override
	public void fireEvent(final ScenarioServiceEvent event, final ScenarioInstance scenarioInstance) {
		super.fireEvent(event, scenarioInstance);
	}

	@Override
	protected ScenarioService initServiceModel() {
		final ScenarioService serviceModel = ScenarioServiceFactory.eINSTANCE.createScenarioService();
		serviceModel.setName(serviceName);
		serviceModel.setDescription("Shared base cases");
		serviceModel.setLocal(false);
		serviceModel.setServiceID(getSerivceID());

		return serviceModel;
	}

	@Override
	public URI resolveURI(final String uri) {
		return URI.createURI(uri);
	}

	public void start() {

		final IPath workspaceLocation = ResourcesPlugin.getWorkspace().getRoot().getLocation();
		final File workspaceLocationFile = workspaceLocation.toFile();

		File baseCaseFolder = new File(workspaceLocationFile.getAbsolutePath() + File.separator + "scenarios" + File.separator + "basecases");
		if (!baseCaseFolder.exists()) {
			baseCaseFolder.mkdirs();
		}

		WellKnownTriggers.WORKSPACE_DATA_ENCRYPTION_CHECK.delayUntilTriggered(() -> {

			// Initial model load
			new Thread(() -> {

				getServiceModel();
				setReady();
				this.updater = new BaseCaseScenarioUpdater(serviceModel, baseCaseFolder, BaseCaseServiceClient.INSTANCE, versionsProviderService);
				updater.start();
			}).start();
		});
	}

	public void stop() {
		if (updater != null) {
			updater.stop();
		}
	}

	@Override
	public void moveInto(final List<Container> elements, final Container destination) {
		// Silently ignore
		return;
	}

	@Override
	public void makeFolder(final Container parent, final String name) {
		// Silently ignore
		return;
	}

	@Override
	public String getSerivceID() {
		return "base-case-" + serviceName;
	}
}
