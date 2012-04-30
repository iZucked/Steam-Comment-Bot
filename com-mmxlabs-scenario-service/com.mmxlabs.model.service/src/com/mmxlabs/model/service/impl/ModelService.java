package com.mmxlabs.model.service.impl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.emf.ecore.xmi.XMLResource;
import org.eclipse.emf.ecore.xmi.impl.XMLParserPoolImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mmxlabs.model.service.IModelInstance;
import com.mmxlabs.model.service.IModelService;
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
	 * The cache of loaded instances
	 * TODO add weak references.
	 */
	final Map<URI, IModelInstance> cache = new HashMap<URI, IModelInstance>();

	private final MMXCoreResourceFactoryImpl resourceFactory = new MMXCoreResourceFactoryImpl();
	final ResourceSet resourceSet = new ResourceSetImpl();
	
	public ModelService() {
		resourceSet.getResourceFactoryRegistry().getExtensionToFactoryMap()
		.put("*", resourceFactory);

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
		//TODO handle this.
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
			if (cache.containsKey(uri)) return cache.get(uri);
			final Resource resource = resourceSet.createResource(uri);
			resource.getContents().add(instance);
			final IModelInstance result = new ModelInstance(resource);
			return result;
		}
	}

	private void collect(final EObject object, final HashMap<String, UUIDObject> table) {
		if (object == null) {
			log.warn("Given a null object to collect UUIDObjects from");
			return;
		}
		if (object instanceof MMXObject) ((MMXObject) object).collectUUIDObjects(table);
		else {
			for (final EObject o : object.eContents()) collect(o, table);
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
			for (final EObject child :part.eContents())
				resolve(child, table);
		}
	}
}
