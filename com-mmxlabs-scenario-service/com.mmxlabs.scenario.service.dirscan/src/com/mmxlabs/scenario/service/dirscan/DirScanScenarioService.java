/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2012
 * All rights reserved.
 */
package com.mmxlabs.scenario.service.dirscan;

import java.io.File;
import java.io.IOException;
import java.util.Dictionary;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.eclipse.emf.ecore.util.EContentAdapter;
import org.eclipse.emf.ecore.xmi.impl.XMIResourceFactoryImpl;
import org.osgi.service.component.ComponentContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mmxlabs.scenario.service.manifest.Manifest;
import com.mmxlabs.scenario.service.model.Container;
import com.mmxlabs.scenario.service.model.Folder;
import com.mmxlabs.scenario.service.model.Metadata;
import com.mmxlabs.scenario.service.model.ScenarioInstance;
import com.mmxlabs.scenario.service.model.ScenarioService;
import com.mmxlabs.scenario.service.model.ScenarioServiceFactory;
import com.mmxlabs.scenario.service.util.AbstractScenarioService;

public class DirScanScenarioService extends AbstractScenarioService {

	private static final Logger log = LoggerFactory.getLogger(DirScanScenarioService.class);

	private ScenarioService scenarioService;

	private File dataPath;

	private String serviceName;

	private EContentAdapter serviceModelAdapter = new EContentAdapter() {
		@Override
		public void notifyChanged(org.eclipse.emf.common.notify.Notification notification) {

			super.notifyChanged(notification);

			// TODO: Process changes and replicate on FileSystem

		}

	};

	public DirScanScenarioService(String name) {
		super(name);
	}

	public void start(final ComponentContext context) {

		final Dictionary<?, ?> d = context.getProperties();

		final String scenarioServiceID = d.get("component.id").toString();
		final String path = d.get("path").toString();
		serviceName = d.get("serviceName").toString();

		dataPath = new File(path);

		scenarioService = initialise();

		// initFileWatcher(dataPath);
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

	public void scanForScenarios(final String scenarioServiceID) {

		scanForScenarios(scenarioServiceID, scenarioService, dataPath);
	}

	public void scanForScenarios(final String scenarioServiceID, final Container container, final File dataDir) {
		if (dataDir.isDirectory() || dataDir.exists()) {
			for (final File f : dataDir.listFiles()) {
				if (f.isFile()) {
					Manifest manifest = loadManifest(f);
					if (manifest != null) {
						final ScenarioInstance scenarioInstance = ScenarioServiceFactory.eINSTANCE.createScenarioInstance();
						scenarioInstance.setUuid(manifest.getUUID());
						URI fileURI = URI.createFileURI(f.getAbsolutePath());
						scenarioInstance.setRootObjectURI("archive://" + fileURI.toString() + "/!rootObject.xmi");
						scenarioInstance.setName(f.getName());
						scenarioInstance.setVersionContext(manifest.getVersionContext());
						scenarioInstance.setScenarioVersion(manifest.getScenarioVersion());
						final Metadata meta = ScenarioServiceFactory.eINSTANCE.createMetadata();
						scenarioInstance.setMetadata(meta);
						meta.setContentType(manifest.getScenarioType());
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

	@Override
	public ScenarioInstance insert(Container container, EObject rootObject) throws IOException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void delete(Container container) {

	}

	@Override
	protected ScenarioService initServiceModel() {

		ScenarioService serviceModel = ScenarioServiceFactory.eINSTANCE.createScenarioService();
		serviceModel.setName(serviceName);

		return serviceModel;
	}

	@Override
	public URI resolveURI(String uri) {
		return URI.createURI(uri);
	}

	private Manifest loadManifest(File scenario) {
		final URI fileURI = URI.createFileURI(scenario.toString());

		final URI manifestURI = URI.createURI("archive:" + fileURI.toString() + "!/MANIFEST.xmi");
		final ResourceSetImpl resourceSet = new ResourceSetImpl();

		resourceSet.getResourceFactoryRegistry().getExtensionToFactoryMap().put("*", new XMIResourceFactoryImpl());

		final Resource resource = resourceSet.createResource(manifestURI);
		try {
			resource.load(null);
			if (resource.getContents().size() == 1) {
				final EObject top = resource.getContents().get(0);
				if (top instanceof Manifest) {
					return (Manifest) top;
				}
			}
		} catch (Exception e) {
			// Unable to parse file for some reason
			log.error("Unable to find manifest for " + scenario, e);
		}
		return null;
	}
}
