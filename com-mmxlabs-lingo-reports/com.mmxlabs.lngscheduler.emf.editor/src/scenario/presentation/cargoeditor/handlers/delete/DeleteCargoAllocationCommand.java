/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2011
 * All rights reserved.
 */
package scenario.presentation.cargoeditor.handlers.delete;

import java.util.Collection;
import java.util.Collections;
import java.util.Set;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.edit.command.DeleteCommand;
import org.eclipse.emf.edit.domain.EditingDomain;

import scenario.schedule.CargoAllocation;

/**
 * Command for deleting a cargo allocation; also deletes the associated booked
 * revenue.
 * 
 * @author Tom Hinton
 * 
 */
public class DeleteCargoAllocationCommand extends TrackedDeleteCommand {

	/**
	 * @param domain
	 * @param collection
	 * @param deletedObjects 
	 */
	public DeleteCargoAllocationCommand(final EditingDomain domain,
			final Collection<?> collection, Set<EObject> deletedObjects) {
		super(domain, collection, deletedObjects);
	}

	@Override
	public void execute() {
		for (final Object object : collection) {
			if (object instanceof CargoAllocation) {
				final CargoAllocation a = (CargoAllocation) object;
				if (a.getLoadRevenue() != null)
					appendAndExecute(DeleteHelper.createDeleteCommand(domain,
							Collections.singleton(a.getLoadRevenue())));
				if (a.getShippingRevenue() != null)
					appendAndExecute(DeleteHelper.createDeleteCommand(domain,
							Collections.singleton(a.getShippingRevenue())));
				if (a.getDischargeRevenue() != null)
					appendAndExecute(DeleteHelper.createDeleteCommand(domain,
							Collections.singleton(a.getDischargeRevenue())));
			}
		}

		super.execute();
	}
}
