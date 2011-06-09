/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2011
 * All rights reserved.
 */
package scenario.presentation.cargoeditor.handlers.delete;

import java.util.Collection;
import java.util.Map;
import java.util.Set;

import org.eclipse.emf.common.command.CompoundCommand;
import org.eclipse.emf.common.command.IdentityCommand;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.emf.ecore.util.EcoreUtil.UsageCrossReferencer;
import org.eclipse.emf.edit.domain.EditingDomain;

/**
 * @author Tom Hinton
 * 
 */
public class TrackedDeleteCommand extends CompoundCommand {
	final Set<EObject> deletedObjects;
	protected Collection collection;
	protected EditingDomain domain;

	/**
	 * @param domain
	 * @param collection
	 * @param deletedObjects
	 */
	public TrackedDeleteCommand(EditingDomain domain, Collection<?> collection,
			Set<EObject> deletedObjects) {
		this.deletedObjects = deletedObjects;
		this.collection = collection;
		this.domain = domain;
		append(new IdentityCommand());
	}

	@Override
	public void execute() {
		deletedObjects.addAll((Collection<? extends EObject>) collection);
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
