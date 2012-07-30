/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2012
 * All rights reserved.
 */
package com.mmxlabs.models.mmxcore.jointmodel;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.emf.common.notify.Adapter;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.URIConverter;
import org.eclipse.emf.ecore.resource.impl.ExtensibleURIConverterImpl;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.eclipse.emf.ecore.xmi.XMLResource;
import org.eclipse.emf.ecore.xmi.impl.XMLParserPoolImpl;
import org.eclipse.emf.edapt.history.Release;
import org.eclipse.emf.edapt.migration.Metamodel;
import org.eclipse.emf.edapt.migration.MigrationException;
import org.eclipse.emf.edapt.migration.execution.Migrator;
import org.eclipse.emf.edapt.migration.execution.MigratorRegistry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mmxlabs.models.mmxcore.IMMXAdapter;
import com.mmxlabs.models.mmxcore.MMXObject;
import com.mmxlabs.models.mmxcore.MMXRootObject;
import com.mmxlabs.models.mmxcore.MMXSubModel;
import com.mmxlabs.models.mmxcore.UUIDObject;
import com.mmxlabs.models.mmxcore.util.MMXCoreBinaryResourceFactoryImpl;
import com.mmxlabs.models.mmxcore.util.MMXCoreHandlerUtil;
import com.mmxlabs.models.mmxcore.util.MMXCoreResourceFactoryImpl;

/**
 * A JointModel manages upgrading several connected submodels.
 * 
 * Each sub model should have a unique key string, and a URI defining how it's retrieved. Subclasses should add these using {@link #addSubModel(String, URI)}, possibly during construction.
 * 
 * Subclasses should also implement {@link #getReleases()}, which will return a list of {@link EmptyJointModelRelease} instances. Each release is responsible for handling any integration / metamodel
 * stuff. Edapt handles the other business.
 * 
 * @author hinton
 * 
 */
public abstract class JointModel {
	private static final Logger log = LoggerFactory.getLogger(JointModel.class);
	protected final Map<String, URI> subModels = new HashMap<String, URI>();
	private final MMXCoreResourceFactoryImpl resourceFactoryXMI = new MMXCoreResourceFactoryImpl();
	private final MMXCoreBinaryResourceFactoryImpl resourceFactoryBinary = new MMXCoreBinaryResourceFactoryImpl();
	final ResourceSet resourceSet = new ResourceSetImpl();

	private MMXRootObject rootObject;

	protected JointModel() {
		// Set XMI Extension
		resourceSet.getResourceFactoryRegistry().getExtensionToFactoryMap().put("xmi", resourceFactoryXMI);
		// Set Binary Extension
		resourceSet.getResourceFactoryRegistry().getExtensionToFactoryMap().put("xmb", resourceFactoryBinary);
		// Set XMI as default
		resourceSet.getResourceFactoryRegistry().getExtensionToFactoryMap().put("*", resourceFactoryXMI);

		resourceSet.getLoadOptions().put(XMLResource.OPTION_DEFER_IDREF_RESOLUTION, true);
		resourceSet.getLoadOptions().put(XMLResource.OPTION_USE_PARSER_POOL, new XMLParserPoolImpl(true));
		resourceSet.getLoadOptions().put(XMLResource.OPTION_USE_XML_NAME_TO_FEATURE_MAP, new HashMap<Object, Object>());
	}

	/**
	 * Equivalent to {@link #createResources(MMXRootObject, boolean)} with the existing root, assigning a new resource to every part.
	 * 
	 * If some parts are stored in remote URIs, this could allow a subclass to move the instances from remote URIs to local URIs.
	 * 
	 */
	public void consolidate() {
		createResources(rootObject, false);
	}

	/**
	 * Initialize a joint model from a handmade MMXRootObject. Uses {@link #createURI(MMXObject)} to find URIs for each part. If skipExistingResources is true, only finds URIs for parts which don't
	 * have an existing resource.
	 * 
	 * @param root
	 * @param skipExistingResources
	 */
	public void createResources(final MMXRootObject root, final boolean skipExistingResources) {
		if (!skipExistingResources || root.eResource() == null) {
			final URI rootURI = createURI(root);

			final Resource rootResource = resourceSet.createResource(rootURI);
			rootResource.getContents().add(root);
		}
		final List<UUIDObject> subObjects = new LinkedList<UUIDObject>();
		final Iterator<MMXSubModel> iterator = root.getSubModels().iterator();

		while (iterator.hasNext()) {
			final MMXSubModel subModel = iterator.next();
			if (!skipExistingResources || subModel.getOriginalResource() == null) {
				iterator.remove();
				final URI uri = createURI(subModel.getSubModelInstance());
				subObjects.add(subModel.getSubModelInstance());
				final Resource resource = MMXCoreHandlerUtil.createResource(resourceSet.getResourceFactoryRegistry(), uri, null);
				resource.getContents().add(subModel.getSubModelInstance());
			}
		}

		for (final UUIDObject object : subObjects) {
			root.addSubModel(object);
		}
		rootObject = root;
	}

	/**
	 * Subclasses should use this method to return a URI for storing the given model part.
	 * 
	 * This is invoked by {@link #createResources(MMXRootObject)} when it is asked to set up storage for an entirely new model.
	 * 
	 * @param object
	 * @return
	 */
	protected abstract URI createURI(final UUIDObject object);

	/**
	 * Load the root object and return it, upgrading if necessary.
	 * 
	 * @return
	 * @throws MigrationException
	 * @throws IOException
	 */
	public MMXRootObject load() throws MigrationException, IOException {
		upgrade();

		final List<Resource> resources = new ArrayList<Resource>(subModels.size());

		for (final URI uri : subModels.values()) {
			final Resource resource = resourceSet.createResource(uri);
			resource.load(null);
			resources.add(resource);
		}

		rootObject = MMXCoreHandlerUtil.composeRootObject(resources);
		final Map<String, UUIDObject> map = rootObject.collectUUIDObjects();
		rootObject.resolveProxies(map);
		rootObject.restoreProxies();

		return rootObject;
	}

	/**
	 * Save all the models in this joint model.
	 * 
	 * @throws IOException
	 */
	public void save() throws IOException {
		disableAdapters(rootObject);
		createResources(rootObject, true);
		rootObject.restoreSubModels(); // put sub models back in their
										// resources
		// save each resource
		for (final Resource resource : resourceSet.getResources()) {
			resource.save(null);
		}
		// put sub models back how they were, making the change transparent
		for (final Resource resource : resourceSet.getResources()) {
			if (resource.getContents().size() != 1) {
				log.warn("Resource " + resource.getURI() + " does not contain exactly one element");
			} else {
				final EObject top = resource.getContents().get(0);
				if (top != rootObject && top instanceof UUIDObject)
					rootObject.addSubModel((UUIDObject) top);
			}
		}
		enableAdapters(rootObject);
	}

	private void disableAdapters(final EObject top) {
		for (final Adapter a : top.eAdapters()) {
			if (a instanceof IMMXAdapter) {
				((IMMXAdapter) a).disable();
			}
		}
		for (final EObject o : top.eContents())
			disableAdapters(o);
	}

	private void enableAdapters(final EObject top) {
		for (final Adapter a : top.eAdapters()) {
			if (a instanceof IMMXAdapter) {
				((IMMXAdapter) a).enable();
			}
		}
		for (final EObject o : top.eContents())
			enableAdapters(o);
	}

	private void copy(final InputStream in, final OutputStream out) throws IOException {
		int c;
		while ((c = in.read()) != -1) {
			out.write(c);
		}
	}

	protected boolean needsUpgrade() {
		return true;
	}

	/**
	 * Upgrade to the latest release, if necessary.
	 * 
	 * Interjects some extra URI mangling stages because EDapt only works with files
	 * 
	 * @throws MigrationException
	 * @throws IOException
	 */
	public void upgrade() throws MigrationException, IOException {
		if (!needsUpgrade())
			return;

		final Map<String, Integer> initialVersions = new HashMap<String, Integer>();
		final Map<URI, Migrator> migrators = new HashMap<URI, Migrator>();

		final Map<URI, Metamodel> currentMetamodels = new HashMap<URI, Metamodel>();
		final Map<URI, Release> currentReleases = new HashMap<URI, Release>();

		final URIConverter converter = new ExtensibleURIConverterImpl();
		final Map<String, URI> temporarySubModels = new HashMap<String, URI>();

		for (final String key : subModels.keySet()) {
			final URI uri = subModels.get(key);
			final File tempFile = File.createTempFile(UUID.randomUUID().toString(), "tmp");
			final InputStream modelInputStream = converter.createInputStream(uri);
			final FileOutputStream fos = new FileOutputStream(tempFile);
			copy(modelInputStream, fos);
			fos.close();
			modelInputStream.close();
			temporarySubModels.put(key, URI.createFileURI(tempFile.getCanonicalPath()));
			tempFile.deleteOnExit();
		}

		/**
		 * This is true if we have found the start release and begun upgrading
		 */
		boolean upgrading = false;
		{
			// alias temporary submodels to submodels, for migration purposes
			final Map<String, URI> subModels = temporarySubModels;

			// first we detect the current release, and get the data for it
			for (final String key : subModels.keySet()) {
				final URI uri = subModels.get(key);
				final Migrator m = MigratorRegistry.getInstance().getMigrator(uri);
				if (m == null)
					continue;
				migrators.put(uri, m);
				final Set<Release> releases = m.getRelease(uri);
				if (releases.size() != 1) {
					throw new RuntimeException(uri + " has " + releases.size() + " matching releases");
				}
				final Release release = releases.iterator().next();
				initialVersions.put(key, release.getNumber());
				currentReleases.put(uri, release);
				currentMetamodels.put(uri, m.getMetamodel(release));
			}

			final List<IJointModelRelease> jointReleases = getReleases();
			for (final IJointModelRelease jointRelease : jointReleases) {
				if (upgrading) {
					// at this point, the releases should be happening in order
					// and the state of the submodels should be correct for the
					// joint release before this one.
					jointRelease.prepare(subModels, currentMetamodels);

					for (final Map.Entry<String, URI> entry : subModels.entrySet()) {
						final URI uri = entry.getValue();
						final String key = entry.getKey();
						final Migrator migrator = migrators.get(uri);
						if (migrator == null)
							continue;
						final Release currentRelease = currentReleases.get(uri);
						Release targetRelease = currentRelease;
						while (targetRelease != null && targetRelease.getNumber() != jointRelease.getReleaseVersion(key)) {
							targetRelease = targetRelease.getNextRelease();
						}
						if (targetRelease == null) {
							throw new RuntimeException("Cannot find release version " + jointRelease.getReleaseVersion(key) + " for model with key " + key);
						}
						migrator.migrateAndSave(Collections.singletonList(uri), currentRelease, targetRelease, new NullProgressMonitor());
						currentReleases.put(uri, targetRelease);
						currentMetamodels.put(uri, migrator.getMetamodel(targetRelease));
					}

					jointRelease.integrate(subModels, currentMetamodels);
				} else {
					// check whether the initial version matches this joint
					// release;
					// if it does
					// set upgrading to true, and the next time around this loop
					// we
					// will do an upgrade.
					upgrading = true;
					for (final String key : subModels.keySet()) {
						if (initialVersions.get(key) != null) {
							upgrading = upgrading && jointRelease.getReleaseVersion(key) == initialVersions.get(key).intValue();
							if (!upgrading)
								break;
						}
					}
				}
			}
		}

		if (upgrading) {
			// we just did an upgrade, so we have to undo the file-switcheroo
			// that happened above
			for (final String key : subModels.keySet()) {
				final URI uri = subModels.get(key);
				final URI tempURI = temporarySubModels.get(key);
				final InputStream tempIS = converter.createInputStream(tempURI);
				final OutputStream realOS = converter.createOutputStream(uri);
				copy(tempIS, realOS);
				tempIS.close();
				realOS.close();
			}
		}

		// clear the resource set, to prevent empty duplicate resources flailing around
		resourceSet.getResources().clear();
	}

	/**
	 * Subclasses should use this method to set up the submodel key value map.
	 * 
	 * @param key
	 * @param uri
	 */
	protected void addSubModel(final String key, final URI uri) {
		subModels.put(key, uri);
	}

	/**
	 * Subclasses should implement this method to return the list of releases for the joint model they define
	 * 
	 * @return
	 */
	protected abstract List<IJointModelRelease> getReleases();

	protected void setRootObject(final MMXRootObject newRootObject) {
		this.rootObject = newRootObject;
	}

	public MMXRootObject getRootObject() {
		return rootObject;
	}

	protected ResourceSet getResourceSet() {
		return resourceSet;
	}
}
