package com.mmxlabs.model.service.impl;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.impl.ResourceImpl;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.eclipse.emf.ecore.xmi.XMLResource;
import org.eclipse.emf.ecore.xmi.impl.XMLParserPoolImpl;

import com.mmxlabs.model.service.IModelInstance;
import com.mmxlabs.model.service.IModelService;

public class ModelService implements IModelService {

	/**
	 * Shared resource set impl between all instances of {@link EcoreContentProvider}. FIXME: Should this really be static? - As the resource listener is not - will we reload the model multiple times?
	 */
	private final ResourceSetImpl resourceSet = new ResourceSetImpl();

	private final Map<URI, IModelInstance> instanceMap = new HashMap<URI, IModelInstance>();

	public ModelService() {

		// TODO: Make this more easily configurable.
		resourceSet.getLoadOptions().put(XMLResource.OPTION_DEFER_IDREF_RESOLUTION, true);
		resourceSet.getLoadOptions().put(XMLResource.OPTION_USE_PARSER_POOL, new XMLParserPoolImpl(true));

		resourceSet.getLoadOptions().put(XMLResource.OPTION_USE_XML_NAME_TO_FEATURE_MAP, new HashMap<Object, Object>());

		// Helper to speed up loading.
		resourceSet.getLoadOptions().put(XMLResource.OPTION_RESOURCE_HANDLER, new XMLResource.ResourceHandler() {
			@Override
			public void preLoad(final XMLResource resource, final InputStream inputStream, final Map<?, ?> options) {
				if (resource instanceof ResourceImpl) {
					((ResourceImpl) resource).setIntrinsicIDToEObjectMap(new HashMap<String, EObject>());
				}
			}

			@Override
			public void postLoad(final XMLResource resource, final InputStream inputStream, final Map<?, ?> options) {
				if (resource instanceof ResourceImpl) {
					((ResourceImpl) resource).setIntrinsicIDToEObjectMap(null);
				}
			}

			@Override
			public void preSave(final XMLResource resource, final OutputStream outputStream, final Map<?, ?> options) {
			}

			@Override
			public void postSave(final XMLResource resource, final OutputStream outputStream, final Map<?, ?> options) {
			}
		});
	}

	@Override
	public IModelInstance getModel(final URI uri) throws IOException {

		if (instanceMap.containsKey(uri)) {
			return instanceMap.get(uri);
		}

		final Resource resource = resourceSet.createResource(uri);

		resource.load(Collections.emptyMap());

		final IModelInstance instance = new ModelInstance(resource);

		instanceMap.put(uri, instance);

		return instance;
	}

	@Override
	public void saveAs(final IModelInstance instance, final URI uri) throws IOException {
		
		EObject model = instance.getModel();
		if (model.eResource() != null) {
			throw new IllegalStateException("Model already has a persisted resource");
		}
		final Resource resource = resourceSet.createResource(uri);
		
		resource.getContents().add(model);
		
		resource.save(Collections.emptyMap());
	}

	@Override
	public void copyTo(final IModelInstance from, final URI to) throws IOException {
		// TODO Auto-generated method stub

	}

	@Override
	public void copyTo(final URI from, final URI to) throws IOException {
		// TODO Auto-generated method stub

	}

	@Override
	public void delete(final IModelInstance instance) throws IOException {
		// TODO Auto-generated method stub

	}

}
