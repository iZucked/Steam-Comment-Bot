/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2011
 * All rights reserved.
 */
package scenario.schedule.commands;

import java.util.Collection;
import java.util.Collections;

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
public class CargoAllocationDeleteCommand extends DeleteCommand {

	/**
	 * @param domain
	 * @param collection
	 */
	public CargoAllocationDeleteCommand(final EditingDomain domain,
			final Collection<?> collection) {
		super(domain, collection);
	}

	@Override
	public void execute() {
		for (final Object object : collection) {
			if (object instanceof CargoAllocation) {
				final CargoAllocation a = (CargoAllocation) object;
				if (a.getLoadRevenue() != null)
					appendAndExecute(DeleteCommand.create(domain,
							Collections.singleton(a.getLoadRevenue())));
				if (a.getShippingRevenue() != null)
					appendAndExecute(DeleteCommand.create(domain,
							Collections.singleton(a.getShippingRevenue())));
				if (a.getDischargeRevenue() != null)
					appendAndExecute(DeleteCommand.create(domain,
							Collections.singleton(a.getDischargeRevenue())));
			}
		}

		super.execute();
	}
}
