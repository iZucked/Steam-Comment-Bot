/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2012
 * All rights reserved.
 */
package com.mmxlabs.model.service.impl;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

import org.eclipse.emf.common.notify.Adapter;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mmxlabs.model.service.IModelInstance;
import com.mmxlabs.models.mmxcore.IMMXAdapter;
import com.mmxlabs.models.mmxcore.impl.MMXContentAdapter;

public class ModelInstance implements IModelInstance {
	private static final Logger log = LoggerFactory.getLogger(ModelInstance.class);
	private final Resource resource;
	private boolean dirty = false;
	private EObject modelObject = null;

	private final MMXContentAdapter dirtyAdapter = new MMXContentAdapter() {
		@Override
		public void reallyNotifyChanged(final Notification notification) {
			if (!notification.isTouch()) {
				dirty = true;
				modelObject.eAdapters().remove(this);
				log.debug("Marking " + resource.getURI() + " as dirty");
			}
		}

		@Override
		protected void missedNotifications(final List<Notification> missed) {
			for (final Notification notification : missed.toArray(new Notification[missed.size()])) {
				reallyNotifyChanged(notification);
				if (dirty) {
					break;
				}
			}
		}
	};

	public ModelInstance(final Resource resource, boolean dirty) {
		this.resource = resource;
		this.dirty = dirty;
		if (!resource.getContents().isEmpty()) {
			modelObject = resource.getContents().get(0);
			if (!dirty) {
				modelObject.eAdapters().add(dirtyAdapter);
			}
		}
	}

	public ModelInstance(final Resource resource) {
		this(resource, false);
	}

	@Override
	public EObject getModel() throws IOException {
		if (!resource.isLoaded()) {
			resource.load(Collections.emptyMap());
			if (resource.getContents().isEmpty()) {
				throw new IOException("Failed to get contents for " + resource.getURI() + ", as it is empty");
			} else {
				modelObject = resource.getContents().get(0);
				modelObject.eAdapters().add(dirtyAdapter);
			}
		}
		return modelObject;
	}

	@Override
	public void save() throws IOException {
		if (!isDirty()) {
			log.debug("Not saving " + resource.getURI() + ", as it's not modified");
			return;
		}
		final EObject model = getModel();
		try {
			switchAdapters(model, false);
			doActualSave();

		} finally {
			switchAdapters(model, true);
		}
	}

	public void saveWithMany() throws IOException {
		if (!isDirty()) {
			log.debug("Not saving " + resource.getURI() + ", as it's not modified");
			return;
		}
		doActualSave();
	}

	private void doActualSave() throws IOException {
		log.debug("Saving " + resource.getURI());
		resource.save(Collections.emptyMap());
		dirty = false;
		modelObject.eAdapters().add(dirtyAdapter);
	}

	/**
	 * TODO this could be in the MMXCoreResourceHandler
	 * 
	 * @param model
	 */
	private void switchAdapters(final EObject model, final boolean on) {
		if (model == null)
			return;
		for (final Adapter a : model.eAdapters()) {
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

	@Override
	public void delete() throws IOException {
		resource.delete(null);
	}

	@Override
	public void rollback() {
		throw new UnsupportedOperationException("Not yet implemented");
	}

	@Override
	public URI getURI() {
		return resource.getURI();
	}

	@Override
	public boolean isDirty() {
		return dirty;
	}
}
