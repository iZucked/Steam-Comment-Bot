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

import scenario.schedule.CargoAllocation;
import scenario.schedule.events.SlotVisit;

/**
 * @author Tom Hinton
 * 
 */
public class DeleteCargoCommand extends TrackedDeleteCommand {
	/**
	 * @param domain
	 * @param collection
	 * @param deletedObjects 
	 */
	public DeleteCargoCommand(EditingDomain domain, Collection<?> collection, Set<EObject> deletedObjects) {
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

		// what objects refer to the cargo, or its contents?
		// sequence elements
		// cargo allocations
		// profit and loss bookings
		final HashSet<EObject> seen = new HashSet<EObject>();
		for (final Map.Entry<EObject, Collection<EStructuralFeature.Setting>> entry : usages
				.entrySet()) {
			for (final EStructuralFeature.Setting setting : entry.getValue()) {
				final EObject referer = setting.getEObject();
				if (seen.contains(referer))
					continue;
				seen.add(referer);
				if (referer instanceof SlotVisit) {
					appendAndExecute(DeleteHelper.createDeleteCommand(domain,
							Collections.singleton(referer)));
				} else if (referer instanceof CargoAllocation) {

					final CargoAllocation ca = (CargoAllocation) referer;
					appendAndExecute(DeleteHelper.createDeleteCommand(domain,
							Collections.singleton(ca.getBallastIdle())));
					appendAndExecute(DeleteHelper.createDeleteCommand(domain,
							Collections.singleton(ca.getLadenIdle())));
					appendAndExecute(DeleteHelper.createDeleteCommand(domain,
							Collections.singleton(ca.getBallastLeg())));
					appendAndExecute(DeleteHelper.createDeleteCommand(domain,
							Collections.singleton(ca.getLadenLeg())));
					/*
					 * This should create a CargoAllocationDeleteCommand, which
					 * will delete the associated revenue entries as well.
					 */
					appendAndExecute(DeleteHelper.createDeleteCommand(domain,
							Collections.singleton(ca)));
				}
			}
		}

		super.execute();
	}
}
