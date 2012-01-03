/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2012
 * All rights reserved.
 */
package scenario.presentation.cargoeditor.handlers.delete;

import java.util.Collection;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.emf.ecore.util.EcoreUtil.UsageCrossReferencer;
import org.eclipse.emf.edit.domain.EditingDomain;

/**
 * @author Tom Hinton
 * 
 */
public class Deleter {
	protected Collection<? extends EObject> collection;
	protected EditingDomain domain;

	/**
	 * @param domain
	 * @param collection
	 * @param deletedObjects
	 */
	public Deleter(final EditingDomain domain, final Collection<? extends EObject> targets) {
		this.domain = domain;
		this.collection = targets;
	}

	public Set<EObject> getObjectsToDelete() {
		return new HashSet<EObject>(collection);
	}

	/**
	 * Returns the references to the objects in the given collection that are to
	 * be cleared. The default implementation uses {@link UsageCrossReferencer}
	 * to find all incoming cross references.
	 * 
	 * @see UsageCrossReferencer
	 * @since 2.6
	 */
	protected Map<EObject, Collection<EStructuralFeature.Setting>> findReferences(
			Collection<EObject> eObjects) {
		return EcoreUtil.UsageCrossReferencer.findAll(eObjects,
				domain.getResourceSet());
	}
}
