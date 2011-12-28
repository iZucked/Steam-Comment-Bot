package com.mmxlabs.scenario.service.file;

import java.io.IOException;
import java.util.Dictionary;
import java.util.HashMap;
import java.util.Map;

import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.IPath;
import org.eclipse.emf.common.notify.Adapter;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.Notifier;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.osgi.service.component.ComponentContext;

import com.mmxlabs.scenario.service.IScenarioService;
import com.mmxlabs.scenario.service.model.ScenarioService;
import com.mmxlabs.scenario.service.model.ScenarioServiceFactory;

public class FileScenarioService implements IScenarioService {

	private static final String PROPERTY_MODEL = "com.mmxlabs.scenario.service.file.model";

	private ResourceSet resourceSet;
	private Resource resource;

	private ScenarioService serviceModel;

	private final Map<Object, Object> options;

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

		System.err.println(">>> Loading Resource : " + uri.toString());

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

	}

	public void save() {

		System.err.println(">>> Saving Resource");
		// TODO: Wrap in a workspace save job

		try {
			resource.save(options);
		} catch (final IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private ScenarioService initialise() {
		System.err.println(">>> Initialise Resource");

		final ScenarioService serviceService = ScenarioServiceFactory.eINSTANCE.createScenarioService();
		serviceService.setName(getName());
		serviceService.setDescription("File scenario service");

		return serviceService;
	}
}
