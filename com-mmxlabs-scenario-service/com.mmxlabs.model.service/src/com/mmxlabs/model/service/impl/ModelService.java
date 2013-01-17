/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2013
 * All rights reserved.
 */
package com.mmxlabs.model.service.impl;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.emf.common.notify.Adapter;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.URIConverter;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.emf.ecore.xmi.XMLResource;
import org.eclipse.emf.ecore.xmi.impl.XMLParserPoolImpl;

import com.mmxlabs.model.service.IModelInstance;
import com.mmxlabs.model.service.IModelService;
import com.mmxlabs.models.mmxcore.IMMXAdapter;
import com.mmxlabs.models.mmxcore.util.MMXCoreBinaryResourceFactoryImpl;
import com.mmxlabs.models.mmxcore.util.MMXCoreHandlerUtil;
import com.mmxlabs.models.mmxcore.util.MMXCoreResourceFactoryImpl;

/**
 * Default implementation of {@link IModelService}.
 * 
 */
public class ModelService implements IModelService {
	/**
	 * The cache of loaded instances TODO add weak references.
	 */
	final Map<URI, IModelInstance> cache = new HashMap<URI, IModelInstance>();

	private final MMXCoreResourceFactoryImpl resourceFactory = new MMXCoreResourceFactoryImpl();
	private final MMXCoreBinaryResourceFactoryImpl binaryResourceFactory = new MMXCoreBinaryResourceFactoryImpl();
	final ResourceSet resourceSet = new ResourceSetImpl();

	public ModelService() {
		resourceSet.getResourceFactoryRegistry().getExtensionToFactoryMap().put("xmb", binaryResourceFactory);
		resourceSet.getResourceFactoryRegistry().getExtensionToFactoryMap().put("xmi", resourceFactory);
		// Default to XMI factory
		resourceSet.getResourceFactoryRegistry().getExtensionToFactoryMap().put("*", resourceFactory);

		resourceSet.getLoadOptions().put(XMLResource.OPTION_DEFER_IDREF_RESOLUTION, true);
		resourceSet.getLoadOptions().put(XMLResource.OPTION_USE_PARSER_POOL, new XMLParserPoolImpl(true));
		resourceSet.getLoadOptions().put(XMLResource.OPTION_USE_XML_NAME_TO_FEATURE_MAP, new HashMap<Object, Object>());
	}

	@Override
	public IModelInstance getModel(final URI uri) throws IOException {
		synchronized (cache) {
			if (cache.containsKey(uri)) {
				return cache.get(uri);
			}
			final ModelInstance instance = new ModelInstance(resourceSet.createResource(uri));
			cache.put(uri, instance);
			return instance;
		}
	}

	@Override
	public IModelInstance copyTo(final IModelInstance from, final URI to) throws IOException {
		return store(EcoreUtil.copy(from.getModel()), to);
	}

	@Override
	public void copyTo(final URI from, final URI to) throws IOException {
		copyTo(getModel(from), to);
	}

	@Override
	public void delete(final IModelInstance instance) throws IOException {
		instance.delete();
	}

	@Override
	public Collection<IModelInstance> getModels(final Collection<URI> uris) throws IOException {
		final List<IModelInstance> result = new ArrayList<IModelInstance>(uris.size());
		for (final URI uri : uris) {
			result.add(getModel(uri));
		}
		return result;
	}

	@Override
	public IModelInstance store(final EObject instance, final URI uri) throws IOException {
		synchronized (cache) {
			// TODO warn here, or return what's already there, or what?
			if (cache.containsKey(uri)) {
				return cache.get(uri);
			}
			final Resource resource = resourceSet.createResource(uri);
			resource.getContents().add(instance);
			final IModelInstance result = new ModelInstance(resource, true);

			cache.put(uri, result);

			return result;
		}
	}

	@Override
	public void resolve(final List<EObject> parts) {
		MMXCoreHandlerUtil.restoreProxiesForEObjects(parts);
	}

	@Override
	public void saveTogether(final Collection<IModelInstance> instances) throws IOException {
		// 1. backup all old model files

		synchronized (this) {
			final URIConverter converter = resourceSet.getURIConverter();
			final HashMap<IModelInstance, File> backups = new HashMap<IModelInstance, File>();
			// first copy every instance's current save state into a byte array
			for (final IModelInstance instance : instances) {
				final File f = File.createTempFile("mmx", "backup");
				f.deleteOnExit();

				InputStream input = null;
				FileOutputStream creator = null;
				try {
					input = converter.createInputStream(instance.getURI());
					creator = new FileOutputStream(f);
					int value;
					final byte[] buf = new byte[4096];
					while ((value = input.read(buf)) != -1) {
						creator.write(buf, 0, value);
					}
					backups.put(instance, f);
				} catch (final FileNotFoundException e) {
					// Ignore this as the original resource may not exist yet. This can happen we we first create a resource and persist it.
				} finally {
					if (creator != null) {
						try {
							creator.close();
						} catch (final IOException e) {

						}
					}
					if (input != null) {
						try {
							input.close();
						} catch (final IOException e) {

						}
					}
				}
			}
			// now try saving each instance
			final List<IModelInstance> touchedInstances = new ArrayList<IModelInstance>();
			for (final IModelInstance instance : instances) {
				switchAdapters(instance.getModel(), false);
			}
			try {
				for (final IModelInstance instance : instances) {
					touchedInstances.add(instance);
					instance.saveWithMany();
				}
			} catch (final IOException error) {
				// if an instance didn't save, copy all the backups back
				for (final IModelInstance instance : touchedInstances) {

					final File backup = backups.get(instance);
					InputStream input = null;
					OutputStream output = null;
					try {
						output = converter.createOutputStream(instance.getURI());
						input = new FileInputStream(backup);
						int value;
						final byte[] buf = new byte[4096];
						while ((value = input.read(buf)) != -1) {
							output.write(buf, 0, value);
						}
					} finally {
						if (output != null) {
							try {
								output.close();
							} catch (final IOException e) {

							}
						}
						if (input != null) {
							try {
								input.close();
							} catch (final IOException e) {

							}
						}
					}

				}

				throw error;
			} finally {
				for (final IModelInstance instance : instances) {
					switchAdapters(instance.getModel(), true);
				}
			}
			// Clean up temp files
			for (final File f : backups.values()) {
				f.delete();
			}
		}
	}

	private void switchAdapters(final EObject model, final boolean on) {
		if (model == null)
			return;
		for (final Adapter a : model.eAdapters().toArray(new Adapter[0])) {
			if (a instanceof IMMXAdapter) {
				if (on)
					((IMMXAdapter) a).enable(true);
				else
					((IMMXAdapter) a).disable();
			}
		}
		for (final EObject child : model.eContents()) {
			switchAdapters(child, on);
		}
	}
}
