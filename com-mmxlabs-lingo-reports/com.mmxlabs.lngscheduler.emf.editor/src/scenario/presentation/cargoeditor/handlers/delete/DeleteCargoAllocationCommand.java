/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2012
 * All rights reserved.
 */
package scenario.presentation.cargoeditor.handlers.delete;

import java.util.Collection;
import java.util.Set;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.edit.domain.EditingDomain;

import scenario.schedule.CargoAllocation;

/**
 * Command for deleting a cargo allocation; also deletes the associated booked
 * revenue.
 * 
 * @author Tom Hinton
 * 
 */
public class DeleteCargoAllocationCommand extends Deleter {

	/**
	 * @param domain
	 * @param collection
	 * @param deletedObjects
	 */
	public DeleteCargoAllocationCommand(final EditingDomain domain,
			final Collection<? extends EObject> collection) {
		super(domain, collection);
	}

	@Override
	public Set<EObject> getObjectsToDelete() {
		final Set<EObject> t = super.getObjectsToDelete();
		for (final Object object : collection) {
			if (object instanceof CargoAllocation) {
				final CargoAllocation a = (CargoAllocation) object;
				if (a.getLoadRevenue() != null)
					t.addAll(DeleteHelper.createDeleter(domain,
							a.getLoadRevenue()).getObjectsToDelete());
				if (a.getShippingRevenue() != null)

					t.addAll(DeleteHelper.createDeleter(domain,
							a.getShippingRevenue()).getObjectsToDelete());
				if (a.getDischargeRevenue() != null)
					t.addAll(DeleteHelper.createDeleter(domain,
							a.getDischargeRevenue()).getObjectsToDelete());
			}
		}
		return t;
	}
}
