/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2013
 * All rights reserved.
 */
package com.mmxlabs.models.lng.input.editorpart;

import java.util.Date;
import java.util.HashMap;

import javax.management.timer.Timer;

import org.eclipse.emf.common.util.EList;

import com.mmxlabs.common.Pair;
import com.mmxlabs.models.lng.cargo.Cargo;
import com.mmxlabs.models.lng.cargo.Slot;
import com.mmxlabs.models.lng.fleet.CharterOutEvent;
import com.mmxlabs.models.lng.fleet.FleetModel;
import com.mmxlabs.models.lng.fleet.Vessel;
import com.mmxlabs.models.lng.fleet.VesselClass;
import com.mmxlabs.models.lng.fleet.VesselEvent;
import com.mmxlabs.models.lng.input.ElementAssignment;
import com.mmxlabs.models.lng.input.InputModel;
import com.mmxlabs.models.lng.input.editor.IAssignmentInformationProvider;
import com.mmxlabs.models.lng.input.editor.utils.AssignmentEditorHelper;
import com.mmxlabs.models.lng.input.editor.utils.CollectedAssignment;
import com.mmxlabs.models.lng.port.Port;
import com.mmxlabs.models.lng.port.PortModel;
import com.mmxlabs.models.lng.port.Route;
import com.mmxlabs.models.lng.port.RouteLine;
import com.mmxlabs.models.mmxcore.MMXRootObject;
import com.mmxlabs.models.mmxcore.NamedObject;
import com.mmxlabs.models.mmxcore.UUIDObject;

public final class AssignmentInformationProviderImplementation implements IAssignmentInformationProvider<CollectedAssignment, UUIDObject> {
	/**
	 * 
	 */
	private final MMXRootObject rootObject;
	private final InputModel modelObject;
	private final HashMap<Pair<Port, Port>, Integer> minTravelTimes = new HashMap<Pair<Port, Port>, Integer>();

	/**
	 * @param inputJointModelEditorContribution
	 */
	public AssignmentInformationProviderImplementation(final MMXRootObject rootObject) {
		this.rootObject = rootObject;
		this.modelObject = rootObject.getSubModel(InputModel.class);
		updateMinTravelTimes();
	}

	@Override
	public Date getStartDate(final UUIDObject task) {
		return AssignmentEditorHelper.getStartDate(task);
	}

	@Override
	public Date getEndDate(final UUIDObject task) {
		return AssignmentEditorHelper.getEndDate(task);
	}

	@Override
	public String getLabel(final UUIDObject element) {
		if (element instanceof NamedObject) {
			return ((NamedObject) element).getName();
		} else {
			return "";
		}
	}

	@Override
	public String getResourceLabel(final CollectedAssignment resource) {
		return resource.getVesselOrClass().getName();
	}

	private Port getEndPort(final UUIDObject task) {
		if (task instanceof Cargo) {
			final Cargo cargo = (Cargo) task;
			final EList<Slot> slots = cargo.getSlots();
			if (slots.isEmpty()) {
				return null;
			}
			final Slot lastSlot = slots.get(slots.size() - 1);
			return lastSlot.getPort();
		} else if (task instanceof CharterOutEvent) {
			return ((CharterOutEvent) task).getEndPort();
		} else if (task instanceof VesselEvent) {
			return ((VesselEvent) task).getPort();
		} else {
			return null;
		}
	}

	private Port getStartPort(final UUIDObject task) {
		if (task instanceof Cargo) {
			final Cargo cargo = (Cargo) task;
			final EList<Slot> slots = cargo.getSlots();
			if (slots.isEmpty()) {
				return null;
			}
			final Slot firstSlot = slots.get(0);
			return firstSlot.getPort();
		} else if (task instanceof VesselEvent) {
			return ((VesselEvent) task).getPort();
		} else {
			return null;
		}
	}

	@Override
	public String getTooltip(final UUIDObject task) {
		String secondLine = "";
		if (task instanceof Cargo) {
			final Cargo cargo = (Cargo) task;
			boolean first = true;
			for (final Slot slot : cargo.getSlots()) {
				if (first) {
					secondLine += "\n";
				} else {
					secondLine += " to ";
				}
				secondLine += slot.getPort().getName();
				first = false;
			}
		} else if (task instanceof VesselEvent) {
			secondLine = "\n" + ((VesselEvent) task).getPort().getName();
			if (task instanceof CharterOutEvent) {
				if (((CharterOutEvent) task).isSetRelocateTo())
					secondLine += " to " + ((CharterOutEvent) task).getRelocateTo().getName();
			}
		}
		return getLabel(task) + secondLine;// + "\n" + AssignmentEditorHelper.getElementAssignment(modelObject, task).getSequence();
	}

	@Override
	public boolean isLocked(final UUIDObject task) {
		final ElementAssignment elementAssignment = AssignmentEditorHelper.getElementAssignment(modelObject, task);
		return elementAssignment == null ? false : elementAssignment.isLocked();
	}

	@Override
	public Date getResourceStartDate(final CollectedAssignment resource) {
		if (resource.isSpotVessel() == false) {

			final Vessel v2 = (Vessel) resource.getVesselOrClass();
			if (v2.getAvailability().isSetStartAfter()) {
				return v2.getAvailability().getStartAfter();
			}

		}
		return null;
	}

	@Override
	public Date getResourceEndDate(final CollectedAssignment resource) {
		if (resource.isSpotVessel() == false) {
			final Vessel v2 = (Vessel) resource.getVesselOrClass();
			if (v2.getAvailability().isSetStartAfter()) {
				return v2.getAvailability().getEndBy();
			}
		}

		return null;
	}

	@Override
	public boolean isSensibleSequence(final UUIDObject task1, final UUIDObject task2) {
		final Date end1 = getEndDate(task1);
		final Date start2 = getStartDate(task2);

		if (end1.before(start2)) {
			final long time = (start2.getTime() - end1.getTime()) / Timer.ONE_HOUR;
			final Port p1 = getEndPort(task1);
			final Port p2 = getStartPort(task2);
			// guess travel time
			final Integer travelTime = minTravelTimes.get(new Pair<Port, Port>(p1, p2));
			if (travelTime != null) {
				return time >= travelTime;
			}
		}
		return false;
	}

	protected void updateMinTravelTimes() {
		// TODO run this only when ports change or speeds change.
		final PortModel pm = rootObject.getSubModel(PortModel.class);
		final FleetModel fm = rootObject.getSubModel(FleetModel.class);
		minTravelTimes.clear();
		if (pm != null && fm != null) {
			double maxSpeed = 0;
			for (final VesselClass vc : fm.getVesselClasses()) {
				maxSpeed = Math.max(maxSpeed, vc.getMaxSpeed());
			}
			for (final Route route : pm.getRoutes()) {
				for (final RouteLine line : route.getLines()) {
					final Pair<Port, Port> p = new Pair<Port, Port>(line.getFrom(), line.getTo());
					final Integer i = minTravelTimes.get(p);
					final int t = (int) (line.getDistance() / maxSpeed);
					if (i == null || t < i) {
						minTravelTimes.put(p, t);
					}
				}
			}
		}
	}
}