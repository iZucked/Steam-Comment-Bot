/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2011
 * All rights reserved.
 */
package scenario.presentation.cargoeditor.handlers.delete;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Map;
import java.util.Set;

import org.eclipse.emf.common.command.Command;
import org.eclipse.emf.common.command.CompoundCommand;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.edit.command.DeleteCommand;
import org.eclipse.emf.edit.domain.EditingDomain;

import scenario.cargo.CargoPackage;
import scenario.fleet.FleetPackage;
import scenario.schedule.SchedulePackage;
import scenario.schedule.fleetallocation.FleetallocationPackage;



/**
 * Temporary hack to hook up delete commands
 * 
 * @author Tom Hinton
 *
 */
public class DeleteHelper {
	public static Command createDeleteCommand(final EditingDomain domain, final Collection<? extends EObject> objects) {
		final Map<EClass, Collection<EObject>> byClass = new HashMap<EClass, Collection<EObject>>();
		
		final Set<EObject> deletedObjects = new HashSet<EObject>();
		
		for (final EObject object : objects) {
			Collection<EObject> b = byClass.get(object.eClass());
			if (b == null) {
				b = new LinkedList<EObject>();
				byClass.put(object.eClass(), b);
			}
			b.add(object);
		}
		
		final CompoundCommand cc = new CompoundCommand();
		
		for (final Map.Entry<EClass, Collection<EObject>> entry : byClass.entrySet()) {
			cc.append(createDeleteCommand(domain, entry.getKey(), entry.getValue(), deletedObjects));
		}
		
		deletedObjects.addAll(objects);
		
		cc.append(DeleteCommand.create(domain, deletedObjects));
		
		return cc;
	}

	/**
	 * @param domain
	 * @param key
	 * @param value
	 * @param deletedObjects 
	 * @return
	 */
	private static Command createDeleteCommand(EditingDomain domain, EClass key,
			Collection<? extends EObject> value, Set<EObject> deletedObjects) {
		if (key == CargoPackage.eINSTANCE.getCargo()) {
			return new DeleteCargoCommand(domain, value, deletedObjects);
		} else if (key == SchedulePackage.eINSTANCE.getCargoAllocation()) {
			return new DeleteCargoAllocationCommand(domain, value, deletedObjects);
		} else if (key == FleetPackage.eINSTANCE.getVessel()) {
			return new DeleteVesselCommand(domain, value, deletedObjects);
		} else if (key == FleetPackage.eINSTANCE.getVesselClass()) {
			return new DeleteVesselClassCommand(domain, value, deletedObjects);
		} else if (FleetallocationPackage.eINSTANCE.getAllocatedVessel().isSuperTypeOf(key)) {
			return new DeleteAllocatedVesselCommand(domain, value, deletedObjects);
		} else if (SchedulePackage.eINSTANCE.getSequence() == key) {
			return new DeleteSequenceCommand(domain, value, deletedObjects);
		}
		return new TrackedDeleteCommand(domain, value, deletedObjects);
	}
}
