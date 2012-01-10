/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2012
 * All rights reserved.
 */
package scenario.presentation.cargoeditor.handlers.delete;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.eclipse.emf.common.command.Command;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.edit.command.DeleteCommand;
import org.eclipse.emf.edit.domain.EditingDomain;

import scenario.cargo.CargoPackage;
import scenario.fleet.FleetPackage;
import scenario.port.PortPackage;
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
		final Map<EClass, Set<EObject>> byClass = new HashMap<EClass, Set<EObject>>();
		
		for (final EObject object : objects) {
			if (object == null) continue;
			Set<EObject> b = byClass.get(object.eClass());
			if (b == null) {
				b = new HashSet<EObject>();
				byClass.put(object.eClass(), b);
			}
			b.add(object);
		}
		
		final Set<EObject> allObjects = new HashSet<EObject>();

		
		for (final Map.Entry<EClass, Set<EObject>> entry : byClass.entrySet()) {
			allObjects.addAll(createDeleter(domain, entry.getKey(), entry.getValue()).getObjectsToDelete());
		}
	
		return DeleteCommand.create(domain, allObjects);
	}
	
	public static Deleter createDeleter(final EditingDomain domain, final EObject object) {
		if (object == null)
			return new Deleter(domain, (Collection<? extends EObject>) Collections.emptySet());
		return createDeleter(domain, object.eClass(), Collections.singleton(object));
	}

	/**
	 * @param domain
	 * @param key
	 * @param value
	 * @param deletedObjects 
	 * @return
	 */
	public static Deleter createDeleter(EditingDomain domain, EClass key,
			Set<? extends EObject> value) {
		if (key == CargoPackage.eINSTANCE.getCargo()) {
			return new DeleteCargoCommand(domain, value);
		} else if (key == SchedulePackage.eINSTANCE.getCargoAllocation()) {
			return new DeleteCargoAllocationCommand(domain, value);
		} else if (key == FleetPackage.eINSTANCE.getVessel()) {
			return new DeleteVesselCommand(domain, value);
		} else if (key == FleetPackage.eINSTANCE.getVesselClass()) {
			return new DeleteVesselClassCommand(domain, value);
		} else if (FleetallocationPackage.eINSTANCE.getAllocatedVessel().isSuperTypeOf(key)) {
			return new DeleteAllocatedVesselCommand(domain, value);
		} else if (SchedulePackage.eINSTANCE.getSequence() == key) {
			return new DeleteSequenceCommand(domain, value);
		} else if (PortPackage.eINSTANCE.getCanal() == key) {
			return new DeleteCanalCommand(domain, value);
		}
		return new Deleter(domain, value);
	}
}
