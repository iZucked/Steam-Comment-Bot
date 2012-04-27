/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2012
 * All rights reserved.
 */
package com.mmxlabs.scenario.service.file;

import java.io.IOException;
import java.util.Collection;
import java.util.Dictionary;
import java.util.HashMap;
import java.util.Map;

import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.IPath;
import org.eclipse.emf.common.command.BasicCommandStack;
import org.eclipse.emf.common.notify.Adapter;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.Notifier;
import org.eclipse.emf.common.util.TreeIterator;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.eclipse.emf.edit.domain.AdapterFactoryEditingDomain;
import org.eclipse.emf.edit.domain.EditingDomain;
import org.eclipse.emf.edit.provider.ComposedAdapterFactory;
import org.eclipse.emf.edit.provider.ReflectiveItemProviderAdapterFactory;
import org.eclipse.emf.edit.provider.resource.ResourceItemProviderAdapterFactory;
import org.osgi.service.component.ComponentContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mmxlabs.scenario.service.IScenarioService;
import com.mmxlabs.scenario.service.model.Container;
import com.mmxlabs.scenario.service.model.ScenarioInstance;
import com.mmxlabs.scenario.service.model.ScenarioService;
import com.mmxlabs.scenario.service.model.ScenarioServiceFactory;

public class FileScenarioService implements IScenarioService {

	private static final Logger log = LoggerFactory.getLogger(FileScenarioService.class);

	private static final String PROPERTY_MODEL = "com.mmxlabs.scenario.service.file.model";

	private ResourceSet resourceSet;
	private Resource resource;

	private ScenarioService serviceModel;

	private final Map<Object, Object> options;
	private final Map<String, ScenarioInstance> instanceMap = new HashMap<String, ScenarioInstance>();

	public FileScenarioService() {
		options = new HashMap<Object, Object>();
	}

	@Override
	public String getName() {
		return "File Scenario Service";
	}

	@Override
	public ScenarioService getServiceModel() {
		return serviceModel;
	}

	public void start(final ComponentContext context) {

		final Dictionary<?, ?> d = context.getProperties();

		final Object value = d.get(PROPERTY_MODEL);

		if (value == null) {
			throw new RuntimeException("FileScenarioService: No model URI property set");
		}
		final String modelURIString = value.toString();

		final IPath workspaceLocation = ResourcesPlugin.getWorkspace().getRoot().getLocation();

		final URI uri = URI.createFileURI(workspaceLocation + "/" + modelURIString);
		load(uri);
	}

	public void stop(final ComponentContext context) {
		save();
	}

	public void load(final URI uri) {

		resourceSet = new ResourceSetImpl();

		resource = resourceSet.createResource(uri);
		try {
			resource.load(options);
			serviceModel = (ScenarioService) resource.getContents().get(0);
		} catch (final IOException e) {
			// Initialise a new model
			serviceModel = initialise();
			resource.getContents().add(serviceModel);
			save();
		}

		// Save on any change
		resource.eAdapters().add(new Adapter() {

			@Override
			public void notifyChanged(final Notification notification) {

				// Auto save on change
				// TODO: Filter Changes
				save();
			}

			@Override
			public Notifier getTarget() {
				// TODO Auto-generated method stub
				return null;
			}

			@Override
			public void setTarget(final Notifier newTarget) {
				// TODO Auto-generated method stub

			}

			@Override
			public boolean isAdapterForType(final Object type) {
				// TODO Auto-generated method stub
				return false;
			}
		});

		final TreeIterator<EObject> eAllContents = serviceModel.eAllContents();
		while (eAllContents.hasNext()) {
			final EObject obj = eAllContents.next();
			if (obj instanceof ScenarioInstance) {
				final ScenarioInstance instance = (ScenarioInstance) obj;
				instanceMap.put(instance.getUuid(), instance);

			}
		}

	}

	public void save() {
		// TODO: Wrap in a workspace save job

		try {
			resource.save(options);
		} catch (final IOException e) {
			log.error(e.getMessage(), e);
		}
	}

	private ScenarioService initialise() {

		final ScenarioService serviceService = ScenarioServiceFactory.eINSTANCE.createScenarioService();
		serviceService.setName(getName());
		serviceService.setDescription("File scenario service");

		return serviceService;
	}

	// public EObject getScenario(final String uuid) {
	// final ScenarioInstance instance = ioHelper.findInstance(uuid);
	//
	// if (instance.getInstance() == null) {
	// try {
	// final EObject scenario = ioHelper.loadScenario(uuid, Collections.EMPTY_MAP);
	//
	// Map<Class<?>, Object> adapters = instance.getAdapters();
	// if (adapters == null) {
	// adapters = new HashMap<Class<?>, Object>();
	// instance.setAdapters(adapters);
	// }
	// final EditingDomain ed = initEditingDomain();
	// adapters.put(EditingDomain.class, ed);
	// ed.getResourceSet().getResources().add(scenario.eResource());
	//
	// instance.setInstance(scenario);
	//
	// final IServiceModelTracker tracker = (IServiceModelTracker) Platform.getAdapterManager().loadAdapter(scenario, IServiceModelTracker.class.getCanonicalName());
	// if (tracker != null) {
	// tracker.setScenarioInstance(instance);
	// }
	// } catch (final IOException e) {
	// log.error(e.getMessage(), e);
	// }
	// }
	// return instance.getInstance();
	// }

	public EditingDomain initEditingDomain() {
		final BasicCommandStack commandStack = new BasicCommandStack();

		final ComposedAdapterFactory adapterFactory = new ComposedAdapterFactory(ComposedAdapterFactory.Descriptor.Registry.INSTANCE);

		adapterFactory.addAdapterFactory(new ResourceItemProviderAdapterFactory());
		adapterFactory.addAdapterFactory(new ReflectiveItemProviderAdapterFactory());

		// Create the editing domain with a special command stack.
		//
		return new AdapterFactoryEditingDomain(adapterFactory, commandStack, new HashMap<Resource, Boolean>());
	}

	@Override
	public boolean exists(final String uuid) {
		return instanceMap.containsKey(uuid);
	}

	@Override
	public void delete(final ScenarioInstance instance) {
		// TODO Auto-generated method stub
	}

	@Override
	public ScenarioInstance getScenarioInstance(final String uuid) {
		return instanceMap.get(uuid);

	}

	@Override
	public ScenarioInstance insert(Container container, Collection<ScenarioInstance> dependencies, Collection<EObject> models) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ScenarioInstance duplicate(ScenarioInstance original, Container destination) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public EObject load(ScenarioInstance instance) throws IOException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void save(ScenarioInstance instance) throws IOException {
		// TODO Auto-generated method stub
		
	}
}
