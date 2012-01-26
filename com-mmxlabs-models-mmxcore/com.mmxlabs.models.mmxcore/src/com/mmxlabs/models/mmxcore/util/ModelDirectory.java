/**
 * 
 */
package com.mmxlabs.models.mmxcore.util;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;

import com.mmxlabs.models.mmxcore.MMXRootObject;
import com.mmxlabs.models.mmxcore.MMXSubModel;
import com.mmxlabs.models.mmxcore.UUIDObject;

/**
 * A model directory contains several XMI files which are bound together into a single root model object.
 * 
 * This is more of a proof of concept than a production bit of code.
 * 
 * @author hinton
 *
 */
public class ModelDirectory {
	final MMXCoreResourceFactoryImpl resourceFactory = new MMXCoreResourceFactoryImpl();
	final ResourceSetImpl resourceSet = new ResourceSetImpl();
	private final MMXRootObject rootObject;
	private final File rootDirectory;
	
	/**
	 * Load a model from a given directory
	 * @param path
	 * @throws IOException
	 */
	public ModelDirectory(final String path) throws IOException {
		rootDirectory = new File(path);
		init();
		// attempt to load resources
		final List<Resource> resources = new LinkedList<Resource>();
		for (final File f : rootDirectory.listFiles(new FilenameFilter() {
			@Override
			public boolean accept(File dir, String name) {
				return name.endsWith(".xmi");
			}
		})) {
			final Resource resource = resourceSet.createResource(URI.createFileURI(f.getCanonicalPath()));
			resource.load(null);
			resources.add(resource);
		}
		
		rootObject = resourceFactory.composeRootObject(resources);
		Map<String, UUIDObject> map = rootObject.collectUUIDObjects();
		rootObject.resolveProxies(map);
		rootObject.restoreProxies();
	}
	
	/**
	 * Prepare a model directory with the given root object
	 * @param path
	 * @param root
	 * @throws IOException
	 */
	public ModelDirectory(final String path, final MMXRootObject root) throws IOException {
		rootDirectory = new File(path);
		if (rootDirectory.exists() == false) 
			rootDirectory.mkdirs();
		init();
		rootObject = root;
		
		final Resource rootResource = resourceSet.createResource(URI.createFileURI(new File(rootDirectory, rootObject.eClass().getName() + ".xmi").getCanonicalPath()));
		rootResource.getContents().add(rootObject);
		for (final MMXSubModel sub : rootObject.getSubModels()) {
			final Resource subResource = resourceSet.createResource(URI.createFileURI(new File(rootDirectory, 
					sub.getSubModelInstance().eClass().getName() + ".xmi").getAbsolutePath()));
			sub.setOriginalResource(subResource);
		}
	}
	
	private void init() {
		resourceSet.getResourceFactoryRegistry().getExtensionToFactoryMap().put("*", resourceFactory);
	}
	
	public void save() throws IOException {
		rootObject.restoreSubModels(); //put sub models back in their resources
		for (final Resource resource : resourceSet.getResources()) {
			resource.save(null);
		}
		// put sub models back how they were
		for (final Resource resource : resourceSet.getResources()) {
			rootObject.addSubModel((UUIDObject) resource.getContents().get(0));
		}
	}
	
	public MMXRootObject getRootObject() {
		return rootObject;
	}
}
