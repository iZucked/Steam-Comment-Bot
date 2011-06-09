/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2011
 * All rights reserved.
 */
package scenario.presentation.cargoeditor.handlers.delete;

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.edit.command.DeleteCommand;
import org.eclipse.emf.edit.domain.AdapterFactoryEditingDomain;
import org.eclipse.emf.edit.domain.EditingDomain;

import scenario.schedule.Sequence;

/**
 * @author Tom Hinton
 * 
 */
public class DeleteAllocatedVesselCommand extends TrackedDeleteCommand {
	/**
	 * @param domain
	 * @param collection
	 * @param deletedObjects 
	 */
	public DeleteAllocatedVesselCommand(EditingDomain domain, Collection<?> collection, Set<EObject> deletedObjects) {
		super(domain, collection, deletedObjects);
	}

	@Override
	public void execute() {
		// find objects related to cargo; the superclass already does this step,
		// but we end up doing it again.
		final Collection<EObject> eObjects = new LinkedHashSet<EObject>();
		for (final Object wrappedObject : collection) {
			final Object object = AdapterFactoryEditingDomain
					.unwrap(wrappedObject);
			if (object instanceof EObject) {
				eObjects.add((EObject) object);
				for (Iterator<EObject> j = ((EObject) object).eAllContents(); j
						.hasNext();) {
					eObjects.add(j.next());
				}
			} else if (object instanceof Resource) {
				for (Iterator<EObject> j = ((Resource) object).getAllContents(); j
						.hasNext();) {
					eObjects.add(j.next());
				}
			}
		}

		final Map<EObject, Collection<EStructuralFeature.Setting>> usages = super
				.findReferences(eObjects);

		final HashSet<EObject> seen = new HashSet<EObject>();
		for (final Map.Entry<EObject, Collection<EStructuralFeature.Setting>> entry : usages
				.entrySet()) {
			final EObject referer = entry.getKey();
			if (seen.contains(referer)) continue;
			seen.add(referer);
			if (referer instanceof Sequence) {
				appendAndExecute(DeleteHelper.createDeleteCommand(domain,
						Collections.singleton(referer)));
			}
		}

		super.execute();
	}
}
