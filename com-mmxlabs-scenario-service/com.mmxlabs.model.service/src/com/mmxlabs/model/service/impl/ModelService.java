/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2012
 * All rights reserved.
 */
package com.mmxlabs.model.service.impl;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mmxlabs.model.service.IModelInstance;
import com.mmxlabs.model.service.IModelService;
import com.mmxlabs.models.mmxcore.IMMXAdapter;
import com.mmxlabs.models.mmxcore.MMXObject;
import com.mmxlabs.models.mmxcore.UUIDObject;
import com.mmxlabs.models.mmxcore.util.MMXCoreResourceFactoryImpl;

/**
 * Default implementation of {@link IModelService}.
 * 
 */
public class ModelService implements IModelService {
	private static final Logger log = LoggerFactory.getLogger(ModelService.class);
	/**
	 * The cache of loaded instances TODO add weak references.
	 */
	final Map<URI, IModelInstance> cache = new HashMap<URI, IModelInstance>();

	private final MMXCoreResourceFactoryImpl resourceFactory = new MMXCoreResourceFactoryImpl();
	final ResourceSet resourceSet = new ResourceSetImpl();

	public ModelService() {
		resourceSet.getResourceFactoryRegistry().getExtensionToFactoryMap().put("*", resourceFactory);

		resourceSet.getLoadOptions().put(XMLResource.OPTION_DEFER_IDREF_RESOLUTION, true);
		resourceSet.getLoadOptions().put(XMLResource.OPTION_USE_PARSER_POOL, new XMLParserPoolImpl(true));
		resourceSet.getLoadOptions().put(XMLResource.OPTION_USE_XML_NAME_TO_FEATURE_MAP, new HashMap<Object, Object>());
	}

	@Override
	public IModelInstance getModel(final URI uri) throws IOException {
		synchronized (cache) {
			if (cache.containsKey(uri))
				return cache.get(uri);

			final ModelInstance instance = new ModelInstance(resourceSet.createResource(uri));
			cache.put(uri, instance);
			return instance;
		}
	}

	@Override
	public IModelInstance copyTo(IModelInstance from, URI to) throws IOException {
		return store(EcoreUtil.copy(from.getModel()), to);
	}

	@Override
	public void copyTo(URI from, URI to) throws IOException {
		copyTo(getModel(from), to);
	}

	@Override
	public void delete(IModelInstance instance) throws IOException {
		// TODO handle this.
	}

	@Override
	public Collection<IModelInstance> getModels(Collection<URI> uris) throws IOException {
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
			final IModelInstance result = new ModelInstance(resource);
			
			cache.put(uri,  result);
			
			return result;
		}
	}

	private void collect(final EObject object, final HashMap<String, UUIDObject> table) {
		if (object == null) {
			log.warn("Given a null object to collect UUIDObjects from");
			return;
		}
		if (object instanceof MMXObject)
			((MMXObject) object).collectUUIDObjects(table);
		else {
			for (final EObject o : object.eContents())
				collect(o, table);
		}
	}

	@Override
	public void resolve(List<EObject> parts) {
		final HashMap<String, UUIDObject> table = new HashMap<String, UUIDObject>();
		for (final EObject part : parts) {
			collect(part, table);
		}
		// now restore proxies
		for (final EObject part : parts) {
			resolve(part, table);
		}
	}

	private void resolve(EObject part, HashMap<String, UUIDObject> table) {
		if (part == null) {
			log.warn("Asked to resolve references in a null object");
			return;
		}
		if (part instanceof MMXObject) {
			((MMXObject) part).resolveProxies(table);
			((MMXObject) part).restoreProxies();
		} else {
			for (final EObject child : part.eContents())
				resolve(child, table);
		}
	}

	@Override
	public void saveTogether(final Collection<IModelInstance> instances) throws IOException {
		// 1. backup all old model files
		
		synchronized (this) {
			final URIConverter converter = resourceSet.getURIConverter();
			final HashMap<IModelInstance, byte[]> backups = new HashMap<IModelInstance, byte[]>();
			// first copy every instance's current save state into a byte array
			for (final IModelInstance instance : instances) {
				try {
					final InputStream input = converter
							.createInputStream(instance.getURI());
					final ByteArrayOutputStream creator = new ByteArrayOutputStream(
							2048); // 2K? is this a reasonable size?
					int value;
					while ((value = input.read()) != -1)
						creator.write(value);
					creator.flush();
					backups.put(instance, creator.toByteArray());
					creator.close();
					input.close();
				} catch (final FileNotFoundException fnfe) {
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
			} catch (IOException error) {
				// if an instance didn't save, copy all the backups back
				for (final IModelInstance instance : touchedInstances) {
					final byte[] backup = backups.get(instance);
					final OutputStream output = converter.createOutputStream(instance.getURI());
					output.write(backup);
					output.flush();
					output.close();
				}
				throw error;
			} finally {
				for (final IModelInstance instance : instances) {
					switchAdapters(instance.getModel(), true);
				}
			}
		}
	}
	
	private void switchAdapters(EObject model, boolean on) {
		if (model == null) return;
		for (final Adapter a : model.eAdapters().toArray(new Adapter[0])) {
			if (a instanceof IMMXAdapter) {
				if (on) ((IMMXAdapter) a).enable(true);
				else ((IMMXAdapter) a).disable();
			}
		}
		for (final EObject child : model.eContents()) {
			switchAdapters(child, on);
		}
	}
}
