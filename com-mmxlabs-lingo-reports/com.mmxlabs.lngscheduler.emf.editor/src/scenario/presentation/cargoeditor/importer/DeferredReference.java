/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2011
 * All rights reserved.
 */
package scenario.presentation.cargoeditor.importer;

import java.util.Map;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;

import com.mmxlabs.common.Pair;

/**
 * Handles deferred references
 * 
 * @author Tom Hinton
 * 
 */
public class DeferredReference implements Runnable {
	private final Pair<EClass, String> key;
	private final EObject target;
	private final EReference reference;

	private Map<Pair<EClass, String>, EObject> registry;

	public DeferredReference(final EObject target, final EReference reference,
			final String key) {
		super();
		this.target = target;
		this.reference = reference;
		this.key = new Pair<EClass, String>(reference.getEReferenceType(), key);
	}

	public Map<Pair<EClass, String>, EObject> getRegistry() {
		return registry;
	}

	public void setRegistry(Map<Pair<EClass, String>, EObject> registry) {
		this.registry = registry;
	}

	@Override
	public void run() {
		assert this.registry != null;

		final EObject value = registry.get(key);
		if (value != null) {
			if (reference.isMany()) {
				@SuppressWarnings("unchecked")
				final EList<EObject> stuff = (EList<EObject>) (target
						.eGet(reference));
				stuff.add(value);
			} else {
				target.eSet(reference, value);
			}
		} else {
			if (key.getSecond().isEmpty() == false)
				System.err.println("Warning: no value for "
						+ key.getFirst().getName() + " named "
						+ key.getSecond() + " (setting " + reference.getName()
						+ " on a " + target.eClass().getName() + ")");
		}
	}
}
