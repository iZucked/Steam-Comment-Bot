/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2011
 * All rights reserved.
 */
package com.mmxlabs.lngscheduler.emf.extras.validation;

import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.SortedSet;
import java.util.TreeSet;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.validation.AbstractModelConstraint;
import org.eclipse.emf.validation.IValidationContext;
import org.eclipse.emf.validation.model.IConstraintStatus;

import scenario.cargo.Cargo;
import scenario.cargo.CargoModel;
import scenario.cargo.CargoPackage;
import scenario.cargo.Slot;
import scenario.port.Port;

import com.mmxlabs.lngscheduler.emf.extras.validation.status.DetailConstraintStatusDecorator;

/**
 * Checks whether multiple cargos will be at the same port at the same time.
 * 
 * This may be too slow to use in batch mode when editing other fields.
 * 
 * @author Tom Hinton
 * 
 */
public class PortContentionConstraint extends AbstractModelConstraint {
	/**
	 * I think this is a problem because of intransitivity.
	 * 
	 * @param o1
	 * @param o2
	 * @return
	 */
	private int overlaps(final Slot o1, final Slot o2) {
		if (o1.getWindowStart() == o2.getWindowStart())
			return 0;
		if (o1.getWindowStart() == null)
			return -1;
		if (o2.getWindowStart() == null)
			return 1;

		// if o1 or o2 overlap anywhere, they are equal
		// otherwise, normal rules apply.
		if (o1.getWindowStart().before(o2.getWindowStart())) {
			if (o1.getWindowEnd().after(o2.getWindowStart())) {
				return 0;
			} else {
				return -1;
			}
		} else {
			if (o1.getWindowStart().before(o2.getWindowEnd())) {
				return 0;
			} else {
				return 1;
			}
		}
	}

	@Override
	public IStatus validate(final IValidationContext ctx) {
		final EObject target = ctx.getTarget();
		if (target instanceof Slot) {

			final Slot slot = (Slot) target;
			final Port port = slot.getPort();

			Map<Port, SortedSet<Slot>> slotsByPort = (Map<Port, SortedSet<Slot>>) ctx
					.getCurrentConstraintData();
			if (slotsByPort == null) {
				slotsByPort = new HashMap<Port, SortedSet<Slot>>();
				ctx.putCurrentConstraintData(slotsByPort);
			}

			SortedSet<Slot> slotsAtPort = slotsByPort.get(port);
			if (slotsAtPort == null) {
				slotsAtPort = new TreeSet<Slot>(new Comparator<Slot>() {
					@Override
					public int compare(Slot o1, Slot o2) {
						return o1.getWindowStart().compareTo(
								o2.getWindowStart());
					}
				});
				// locate other slots
				EObject container = slot.eContainer();
				while (container != null && !(container instanceof CargoModel)) {
					container = container.eContainer();
				}
				if (container instanceof CargoModel) {
					for (final Cargo c : ((CargoModel) container).getCargoes()) {
						if (c.getLoadSlot() != null
								&& c.getLoadSlot().getPort() == port) {
							slotsAtPort.add(c.getLoadSlot());
						}
						if (c.getDischargeSlot() != null
								&& c.getDischargeSlot().getPort() == port) {
							slotsAtPort.add(c.getDischargeSlot());
						}
					}
				} else {
					return ctx.createSuccessStatus();
				}

				slotsByPort.put(port, slotsAtPort);
			}

			// now check for overlap
			final StringBuffer message = new StringBuffer();

			// iterate over slots and check for overlap
			// we could make this faster by having a second set reverse-ordered on end
			// date and using that to find a suitable starting point in slotsAtPort?
			for (final Slot s : slotsAtPort) {
				final int order = overlaps(s, slot);
				if (order == 0) {
					if (s != slot) {
						message.append((message.length() > 0 ? ", " : "")
								+ s.getId());
					}
					if (s.getWindowStart().after(slot.getWindowEnd()))
						break;
				} // else {
					// break;
					// }
			}

			if (message.length() > 0) {
				return new DetailConstraintStatusDecorator(
						(IConstraintStatus) ctx.createFailureStatus(
								slot.getId(), port.getName(),
								message.toString()), slot,
						CargoPackage.eINSTANCE.getSlot_Port());
			}
		}

		return ctx.createSuccessStatus();
	}

}
