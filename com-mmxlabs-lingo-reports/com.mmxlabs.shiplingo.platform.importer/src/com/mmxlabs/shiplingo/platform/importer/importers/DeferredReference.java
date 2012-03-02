/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2012
 * All rights reserved.
 */
package com.mmxlabs.shiplingo.platform.importer.importers;

import java.util.Map;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mmxlabs.common.Pair;

/**
 * Handles deferred references
 * 
 * @author Tom Hinton
 * 
 */
public class DeferredReference implements Runnable {

	private static final Logger log = LoggerFactory.getLogger(DeferredReference.class);

	protected final Pair<EClass, String> key;
	protected EObject target;
	private final EReference reference;

	protected Map<Pair<EClass, String>, EObject> registry;

	public DeferredReference(final EObject target, final EReference reference, final String key) {
		super();
		this.target = target;
		this.reference = reference;
		this.key = new Pair<EClass, String>(reference.getEReferenceType(), key);
	}

	public DeferredReference(final EObject target, final EReference reference, final EClass referenceType, final String key) {
		super();
		this.target = target;
		this.reference = reference;
		this.key = new Pair<EClass, String>(referenceType, key);
	}

	public DeferredReference() {
		this.key = null;
		this.reference = null;
		this.target = null;
	}

	public Map<Pair<EClass, String>, EObject> getRegistry() {
		return registry;
	}

	public void setRegistry(final Map<Pair<EClass, String>, EObject> registry) {
		this.registry = registry;
	}

	@Override
	public void run() {
		assert this.registry != null;

		EObject value = registry.get(key);

		if (value == null) {
			if (key.getSecond().isEmpty() == false) {
				for (final Map.Entry<Pair<EClass, String>, EObject> entry : registry.entrySet()) {
					if (entry.getKey().getSecond().equals(key.getSecond())) {
						if (key.getFirst().isSuperTypeOf(entry.getKey().getFirst())) {
							value = entry.getValue();
							break;
						}
					}
				}
			}
		}

		if (value != null) {
			if (reference.isMany()) {
				@SuppressWarnings("unchecked")
				final EList<EObject> stuff = (EList<EObject>) (target.eGet(reference));
				stuff.add(value);
			} else {
				target.eSet(reference, value);
			}
		} else {
			if (key.getSecond().isEmpty() == false) {
				if (log.isWarnEnabled()) {
					log.warn("Warning: no value for " + key.getFirst().getName() + " named " + key.getSecond() + " (setting " + reference.getName() + " on a " + target.eClass().getName() + ")",
							new RuntimeException());
				}
			}
		}
	}
}
