/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2012
 * All rights reserved.
 */
package com.mmxlabs.scenario.service.manifest;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.eclipse.emf.ecore.xmi.impl.XMIResourceFactoryImpl;

import com.mmxlabs.scenario.service.IScenarioService;
import com.mmxlabs.scenario.service.model.Metadata;
import com.mmxlabs.scenario.service.model.ScenarioInstance;
import com.mmxlabs.scenario.service.model.ScenarioServiceFactory;

/**
 * Utility class for getting a scenario out into a zipfile or vice-versa
 * @author hinton
 *
 */
public class ScenarioStorageUtil {
	public static String storeToTemporaryFile(final ScenarioInstance instance) throws IOException {
		//TODO this inserts some gibberish into the name for uniqueness; could put it in a folder instead.
		final File tempFile = File.createTempFile(instance.getName(), ".sc2");

		tempFile.deleteOnExit();
		storeToFile(instance, tempFile);
		
		return tempFile.getAbsolutePath();
	}
	
	public static void storeToFile(final ScenarioInstance instance, final File file) throws IOException {
		final ResourceSetImpl resourceSet = new ResourceSetImpl();
		resourceSet.getResourceFactoryRegistry().getExtensionToFactoryMap().put("*", new XMIResourceFactoryImpl());
		final Manifest manifest = ManifestFactory.eINSTANCE.createManifest();
		manifest.setScenarioType(instance.getMetadata().getContentType());
		manifest.setUUID(instance.getUuid());
		final URI manifestURI = URI.createURI("archive:" + URI.createFileURI(file.getAbsolutePath()) + "!/MANIFEST.xmi");
		final Resource manifestResource = resourceSet.createResource(manifestURI);
		
		manifestResource.getContents().add(manifest);
		
		final IScenarioService scenarioService = instance.getScenarioService();
		final List<String> partURIs = new ArrayList<String>();
		
		addURIs(partURIs, instance, scenarioService);

		int index = 0;
		for (final String partURI : partURIs) {
			final URI u = URI.createURI(partURI);
			final Resource r = resourceSet.createResource(u);
			r.load(null);
			if (!r.getContents().isEmpty()) {
				final EObject top = r.getContents().get(0);
				final URI relativeURI = URI.createURI("/" + top.eClass().getName() + index++ + ".xmi");
				manifest.getModelURIs().add(relativeURI.toString());
				final URI resolved = relativeURI.resolve(manifestURI);
				final Resource r2 = resourceSet.createResource(resolved);
				r2.getContents().add(top);
				r2.save(null);
			}
		}
		
		manifestResource.save(null);
	}

	private static void addURIs(List<String> partURIs, ScenarioInstance instance, IScenarioService scenarioService) {
		if (instance == null) return;
		partURIs.addAll(instance.getSubModelURIs());
		for (final String depUUID : instance.getDependencyUUIDs()) {
			addURIs(partURIs, scenarioService.getScenarioInstance(depUUID), scenarioService);
		}
	}

	/**
	 * @param filePath
	 * @return
	 */
	public static ScenarioInstance loadInstanceFromFile(final String filePath) {
		final ResourceSetImpl resourceSet = new ResourceSetImpl();
		resourceSet.getResourceFactoryRegistry().getExtensionToFactoryMap().put("*", new XMIResourceFactoryImpl());
		
		final URI manifestURI = URI.createURI("archive:" + URI.createFileURI(filePath)+"!/MANIFEST.xmi");
		final Resource resource = resourceSet.createResource(manifestURI);
		try {
			resource.load(null);
			if (resource.getContents().size() == 1) {
				final EObject top = resource.getContents().get(0);
				if (top instanceof Manifest) {
					final Manifest manifest = (Manifest) top;
					final ScenarioInstance result = ScenarioServiceFactory.eINSTANCE.createScenarioInstance();
					final Metadata meta = ScenarioServiceFactory.eINSTANCE.createMetadata();

					result.setMetadata(meta);
					
					result.setName(new File(filePath).getName());
					result.setUuid(manifest.getUUID());
					
					meta.setContentType(manifest.getScenarioType());
					
					for (final String smu : manifest.getModelURIs()) {
						URI rel = URI.createURI(smu);
						if (rel.isRelative()) {
							rel = rel.resolve(manifestURI);
						}
						result.getSubModelURIs().add(rel.toString());
					}
					
					result.getDependencyUUIDs().addAll(manifest.getDependencyUUIDs());
					
					return result;
				}
			}
		} catch (IOException e) {
		}
		return null;
	}
}
