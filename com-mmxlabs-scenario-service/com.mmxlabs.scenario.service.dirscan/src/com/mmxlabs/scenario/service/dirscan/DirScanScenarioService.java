/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2012
 * All rights reserved.
 */
package com.mmxlabs.scenario.service.dirscan;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Collections;
import java.util.Dictionary;
import java.util.HashMap;
import java.util.Map;

import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Platform;
import org.eclipse.emf.common.command.BasicCommandStack;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.edit.domain.AdapterFactoryEditingDomain;
import org.eclipse.emf.edit.domain.EditingDomain;
import org.eclipse.emf.edit.provider.ComposedAdapterFactory;
import org.eclipse.emf.edit.provider.ReflectiveItemProviderAdapterFactory;
import org.eclipse.emf.edit.provider.resource.ResourceItemProviderAdapterFactory;
import org.eclipse.emf.query.conditions.eobjects.structuralfeatures.EObjectAttributeValueCondition;
import org.eclipse.emf.query.statements.FROM;
import org.eclipse.emf.query.statements.IQueryResult;
import org.eclipse.emf.query.statements.SELECT;
import org.eclipse.emf.query.statements.WHERE;
import org.osgi.service.component.ComponentContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mmxlabs.scenario.service.IScenarioService;
import com.mmxlabs.scenario.service.IServiceModelTracker;
import com.mmxlabs.scenario.service.ScenarioServiceIOHelper;
import com.mmxlabs.scenario.service.model.Container;
import com.mmxlabs.scenario.service.model.Folder;
import com.mmxlabs.scenario.service.model.Metadata;
import com.mmxlabs.scenario.service.model.ScenarioInstance;
import com.mmxlabs.scenario.service.model.ScenarioService;
import com.mmxlabs.scenario.service.model.ScenarioServiceFactory;
import com.mmxlabs.scenario.service.model.ScenarioServicePackage;

public class DirScanScenarioService implements IScenarioService {

	private static final Logger log = LoggerFactory.getLogger(DirScanScenarioService.class);

	private ScenarioService scenarioService;

	private ScenarioServiceIOHelper ioHelper;

	private IPath dataPath;

	public DirScanScenarioService() {
	}

	@Override
	public String getName() {
		return "Dir Scan Scenario Service";
	}

	@Override
	public ScenarioService getServiceModel() {
		return scenarioService;
	}

	public void start(final ComponentContext context) {

		final Dictionary<?, ?> d = context.getProperties();

		final String scenarioServiceID = d.get("component.id").toString();

		dataPath = ResourcesPlugin.getWorkspace().getRoot().getLocation().append("/data/");

		scenarioService = initialise();

		ioHelper = new ScenarioServiceIOHelper(scenarioService, dataPath);

		scanForScenarios(scenarioServiceID);
	}

	public void stop(final ComponentContext context) {
		scenarioService = null;
	}

	private ScenarioService initialise() {

		final ScenarioService serviceService = ScenarioServiceFactory.eINSTANCE.createScenarioService();
		serviceService.setName(getName());
		serviceService.setDescription("DirScan scenario service");

		return serviceService;
	}

	@Override
	public InputStream createInputStream(final String uuid, final Map<?, ?> options) throws IOException {

		return ioHelper.createInputStream(uuid, options);
	}

	@Override
	public OutputStream createOutputStream(final String uuid, final Map<?, ?> options) throws IOException {

		return ioHelper.createOutputStream(uuid, options);
	}

	@Override
	public boolean exists(final String uuid, final Map<?, ?> options) {

		return ioHelper.exists(uuid, options);
	}

	@Override
	public void delete(final String uuid, final Map<?, ?> options) {

		ioHelper.delete(uuid, options);
	}

	@Override
	public EObject getScenario(final String uuid) {
		final ScenarioInstance findInstance = ioHelper.findInstance(uuid);

		if (findInstance.getInstance() == null) {
			try {
				final EObject scenario = ioHelper.loadScenario(uuid, Collections.EMPTY_MAP);

				Map<Class<?>, Object> adapters = findInstance.getAdapters();
				if (adapters == null) {
					adapters = new HashMap<Class<?>, Object>();
					findInstance.setAdapters(adapters);
				}
				final EditingDomain ed = initEditingDomain();
				adapters.put(EditingDomain.class, ed);
				ed.getResourceSet().getResources().add(scenario.eResource());

				findInstance.setInstance(scenario);

				final IServiceModelTracker tracker = (IServiceModelTracker) Platform.getAdapterManager().loadAdapter(scenario, IServiceModelTracker.class.getCanonicalName());
				if (tracker != null) {
					tracker.setScenarioInstance(findInstance);
				}
			} catch (final IOException e) {
				log.error(e.getMessage(), e);
			}
		}
		return findInstance.getInstance();
	}

	@Override
	public ScenarioInstance getScenarioInstance(final String uuid) {
		return ioHelper.findInstance(uuid);
	}

	@Override
	public <T> T getAdapter(final String uuid, final Class<T> adapter) {

		if (EditingDomain.class.isAssignableFrom(adapter)) {
			final ScenarioInstance instance = ioHelper.findInstance(uuid);

			final Map<Class<?>, Object> adapters = instance.getAdapters();
			if (adapters != null && adapters.containsKey(adapter)) {
				return adapter.cast(adapters.get(adapter));
			}
		}
		return null;
	}

	public EditingDomain initEditingDomain() {
		final BasicCommandStack commandStack = new BasicCommandStack();

		final ComposedAdapterFactory adapterFactory = new ComposedAdapterFactory(ComposedAdapterFactory.Descriptor.Registry.INSTANCE);

		adapterFactory.addAdapterFactory(new ResourceItemProviderAdapterFactory());
		adapterFactory.addAdapterFactory(new ReflectiveItemProviderAdapterFactory());

		// Create the editing domain with a special command stack.
		//
		return new AdapterFactoryEditingDomain(adapterFactory, commandStack, new HashMap<Resource, Boolean>());
	}

	public void scanForScenarios(final String scenarioServiceID) {

		final File dataDir = dataPath.toFile();
		scanForScenarios(scenarioServiceID, scenarioService, dataDir);
	}

	public void scanForScenarios(final String scenarioServiceID, final Container container, final File dataDir) {
		if (dataDir.isDirectory() || dataDir.exists()) {
			for (final File f : dataDir.listFiles()) {
				if (f.isFile()) {
					final String uuid = f.getName();

					// See if file exists in scenario
					final SELECT query = new SELECT(1, new FROM(scenarioService), new WHERE(new EObjectAttributeValueCondition(ScenarioServicePackage.eINSTANCE.getScenarioInstance_Uuid(),
							new org.eclipse.emf.query.conditions.strings.StringValue(uuid))));
					final IQueryResult queryResult = query.execute();

					if (queryResult.isEmpty()) {
						final ScenarioInstance scenarioInstance = ScenarioServiceFactory.eINSTANCE.createScenarioInstance();
						scenarioInstance.setUuid(uuid);
						scenarioInstance.setName(f.getName());

						scenarioInstance.setUri("service://" + scenarioServiceID + "/" + uuid);

						final Metadata metadata = ScenarioServiceFactory.eINSTANCE.createMetadata();
						// TODO: Set correct content type
						metadata.setContentType("text/xmi");
						scenarioInstance.setMetadata(metadata);

						container.getElements().add(scenarioInstance);
					}
				} else if (f.isDirectory()) {
					// Create container,
					final Folder folder = ScenarioServiceFactory.eINSTANCE.createFolder();
					container.getElements().add(folder);
					folder.setName(f.getName());
					// Recurse
					scanForScenarios(scenarioServiceID, folder, f);
				}
			}
		}
	}
}
