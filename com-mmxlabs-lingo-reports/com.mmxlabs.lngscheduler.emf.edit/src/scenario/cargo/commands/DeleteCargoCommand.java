/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2011
 * All rights reserved.
 */
package scenario.cargo.commands;

import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.Map;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.edit.command.DeleteCommand;
import org.eclipse.emf.edit.domain.AdapterFactoryEditingDomain;
import org.eclipse.emf.edit.domain.EditingDomain;

import scenario.schedule.BookedRevenue;
import scenario.schedule.CargoAllocation;
import scenario.schedule.events.SlotVisit;

/**
 * @author Tom Hinton
 * 
 */
public class DeleteCargoCommand extends DeleteCommand {
	/**
	 * @param domain
	 * @param collection
	 */
	public DeleteCargoCommand(EditingDomain domain, Collection<?> collection) {
		super(domain, collection);
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

		for (final Map.Entry<EObject, Collection<EStructuralFeature.Setting>> entry : usages
				.entrySet()) {
			final EObject referer = entry.getKey();
			if (referer instanceof SlotVisit) {
				appendAndExecute(DeleteCommand.create(domain,
						Collections.singleton(referer)));
			} else if (referer instanceof CargoAllocation) {
				final CargoAllocation ca = (CargoAllocation) referer;
				appendAndExecute(DeleteCommand.create(domain,
						Collections.singleton(ca.getBallastIdle())));
				appendAndExecute(DeleteCommand.create(domain,
						Collections.singleton(ca.getLadenIdle())));
				appendAndExecute(DeleteCommand.create(domain,
						Collections.singleton(ca.getBallastLeg())));
				appendAndExecute(DeleteCommand.create(domain,
						Collections.singleton(ca.getLadenLeg())));
				/*
				 * This should create a CargoAllocationDeleteCommand, which will
				 * delete the associated revenue entries as well.
				 */
				appendAndExecute(DeleteCommand.create(domain,
						Collections.singleton(ca)));
			}
		}

		super.execute();
	}
}
