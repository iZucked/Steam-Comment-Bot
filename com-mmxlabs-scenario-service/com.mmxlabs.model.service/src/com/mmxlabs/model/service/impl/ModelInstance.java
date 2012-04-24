package com.mmxlabs.model.service.impl;

import java.io.IOException;
import java.util.Collections;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;

import com.mmxlabs.model.service.IModelInstance;

public class ModelInstance implements IModelInstance {

	private final Resource resource;

	public ModelInstance(final Resource resource) {
		this.resource = resource;
	}

	@Override
	public EObject getModel() {
		return resource.getContents().get(0);
	}

	@Override
	public void save() throws IOException {

		resource.save(Collections.emptyMap());
	}

	@Override
	public void delete() throws IOException {
		throw new UnsupportedOperationException("Not yet implemented");
	}

	@Override
	public void rollback() {
		throw new UnsupportedOperationException("Not yet implemented");
	}

}
