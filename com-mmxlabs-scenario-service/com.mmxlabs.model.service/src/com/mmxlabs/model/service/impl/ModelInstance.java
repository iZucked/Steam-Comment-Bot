package com.mmxlabs.model.service.impl;

import java.io.IOException;
import java.util.Collections;

import org.eclipse.emf.common.notify.Adapter;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;

import com.mmxlabs.model.service.IModelInstance;
import com.mmxlabs.models.mmxcore.IMMXAdapter;

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
		final EObject model = getModel();
		try {
			switchAdapters(model, false);
			resource.save(Collections.emptyMap());
		}finally {
			switchAdapters(model, true);
		}
	}
	
	/**
	 * @param model
	 */
	private void switchAdapters(EObject model, boolean on) {
		if (model == null) return;
		for (final Adapter a : model.eAdapters()) {
			if (a instanceof IMMXAdapter) {
				if (on) ((IMMXAdapter) a).enable();
				else ((IMMXAdapter) a).disable();
			}
		}
		for (final EObject child : model.eContents()) {
			switchAdapters(child, on);
		}
	}

	@Override
	public void delete() throws IOException {
		throw new UnsupportedOperationException("Not yet implemented");
	}

	@Override
	public void rollback() {
		throw new UnsupportedOperationException("Not yet implemented");
	}

	@Override
	public URI getURI() {
		return resource.getURI();
	}
}
