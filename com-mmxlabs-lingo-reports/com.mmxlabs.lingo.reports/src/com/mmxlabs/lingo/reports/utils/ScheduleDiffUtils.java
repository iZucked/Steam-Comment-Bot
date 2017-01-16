/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
package com.mmxlabs.lingo.reports.utils;

import java.util.List;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.Nullable;

import com.mmxlabs.common.Equality;
import com.mmxlabs.models.lng.cargo.CargoType;
import com.mmxlabs.models.lng.cargo.Slot;
import com.mmxlabs.models.lng.cargo.SpotSlot;
import com.mmxlabs.models.lng.commercial.Contract;
import com.mmxlabs.models.lng.port.Port;
import com.mmxlabs.models.lng.schedule.CargoAllocation;
import com.mmxlabs.models.lng.schedule.Event;
import com.mmxlabs.models.lng.schedule.GeneratedCharterOut;
import com.mmxlabs.models.lng.schedule.OpenSlotAllocation;
import com.mmxlabs.models.lng.schedule.PortVisit;
import com.mmxlabs.models.lng.schedule.SlotAllocation;
import com.mmxlabs.models.lng.schedule.SlotVisit;
import com.mmxlabs.models.lng.schedule.util.ScheduleModelUtils;
import com.mmxlabs.models.lng.spotmarkets.SpotMarket;

/**
 */
public class ScheduleDiffUtils {

	private boolean checkAssignmentDifferences = true;
	private boolean checkSpotMarketDifferences = true;
	private boolean checkNextPortDifferences = true;

	public boolean isElementDifferent(final @Nullable EObject pinnedObject, final @Nullable EObject otherObject) {

		// oops - dynamic eobjects coming here...

		if (pinnedObject == null || otherObject == null) {
			return true;
		}

		if (pinnedObject instanceof GeneratedCharterOut || otherObject instanceof GeneratedCharterOut) {
			final GeneratedCharterOut pinnedCharterOut = (GeneratedCharterOut) pinnedObject;
			final GeneratedCharterOut otherCharterOut = (GeneratedCharterOut) otherObject;

			if (pinnedCharterOut.getDuration() != otherCharterOut.getDuration()) {
				return true;
			}
			if (pinnedCharterOut.getRevenue() != otherCharterOut.getRevenue()) {
				return true;
			}
			// Specific to each scenario/schedule so always mark as different.
			return false;
		}

		if (pinnedObject instanceof CargoAllocation && otherObject instanceof CargoAllocation) {
			CargoAllocation ref = null;
			CargoAllocation ca = null;
			ref = (CargoAllocation) pinnedObject;

			ca = (CargoAllocation) otherObject;

			if (checkAssignmentDifferences) {
				// Check vessel
				if ((ca.getSequence().getVesselAvailability() == null) != (ref.getSequence().getVesselAvailability() == null)) {
					return true;
				} else if ((ca.getSequence().getCharterInMarket() == null) != (ref.getSequence().getCharterInMarket() == null)) {
					return true;
				} else if (ca.getSequence().getVesselAvailability() != null
						&& (!ca.getSequence().getVesselAvailability().getVessel().getName().equals(ref.getSequence().getVesselAvailability().getVessel().getName()))) {
					return true;
				} else if (ca.getSequence().getCharterInMarket() != null && (!ca.getSequence().getCharterInMarket().getName().equals(ref.getSequence().getCharterInMarket().getName()))) {
					return true;
				}
			}

			final List<SlotAllocation> caSlotAllocations = ca.getSlotAllocations();
			final List<SlotAllocation> refSlotAllocations = ref.getSlotAllocations();

			if (caSlotAllocations.size() != refSlotAllocations.size()) {
				return true;
			}

			for (int i = 0; i < caSlotAllocations.size(); ++i) {
				final SlotAllocation caAllocation = caSlotAllocations.get(i);
				final SlotAllocation refAllocation = refSlotAllocations.get(i);

				{
					final Slot caSlot = caAllocation.getSlot();
					final Slot refSlot = refAllocation.getSlot();

					if (!(caSlot instanceof SpotSlot && refSlot instanceof SpotSlot)) {

						final String caName = caSlot == null ? null : caSlot.getName();
						final String refName = refSlot == null ? null : refSlot.getName();

						if (!Equality.isEqual(caName, refName)) {
							return true;
						}
					} else {
						if (checkSpotMarketDifferences) {
							final SpotSlot caSpot = (SpotSlot) caSlot;
							final SpotSlot refSpot = (SpotSlot) refSlot;

							final SpotMarket caMarket = caSpot == null ? null : caSpot.getMarket();
							final SpotMarket refMarket = refSpot == null ? null : refSpot.getMarket();

							final String caName = caMarket == null ? null : caMarket.getName();
							final String refName = refMarket == null ? null : refMarket.getName();

							if (!Equality.isEqual(caName, refName)) {
								return true;
							}
						}
					}
				}

				{
					final Port caPort = caAllocation.getPort();
					final Port refPort = refAllocation.getPort();
					final String caName = caPort == null ? null : caPort.getName();
					final String refName = refPort == null ? null : refPort.getName();

					if (!Equality.isEqual(caName, refName)) {
						return true;
					}
				}

				{
					final Contract caContract = caAllocation.getContract();
					final Contract refContract = refAllocation.getContract();
					final String caName = caContract == null ? null : caContract.getName();
					final String refName = refContract == null ? null : refContract.getName();

					if (!Equality.isEqual(caName, refName)) {
						return true;
					}
				}
			}

			if (checkNextPortDifferences && ref.getCargoType() == CargoType.FLEET) {
				final Event caLastEvent = ca.getEvents().get(ca.getEvents().size() - 1);
				final Event refLastEvent = ref.getEvents().get(ref.getEvents().size() - 1);

				final Event caNextEvent = caLastEvent.getNextEvent();
				final Event refNextEvent = refLastEvent.getNextEvent();

				if (caNextEvent instanceof PortVisit && refNextEvent instanceof PortVisit) {
					final Port caPort = caNextEvent.getPort();
					final Port refPort = refNextEvent.getPort();
					final String caName = caPort == null ? null : caPort.getName();
					final String refName = refPort == null ? null : refPort.getName();

					if (!Equality.isEqual(caName, refName)) {
						return true;
					}
				}
			}

			return false;
		} else if (pinnedObject instanceof SlotAllocation && otherObject instanceof SlotAllocation) {
			return isElementDifferent(((SlotAllocation) pinnedObject).getSlotVisit(), ((SlotAllocation) otherObject).getSlotVisit());
		} else if (pinnedObject instanceof SlotVisit && otherObject instanceof SlotVisit) {
			SlotVisit ref = null;
			SlotVisit ca = null;
			ref = (SlotVisit) pinnedObject;

			ca = (SlotVisit) otherObject;
			if (checkAssignmentDifferences) {

				if ((ca.getSequence().getVesselAvailability() == null) != (ref.getSequence().getVesselAvailability() == null)) {
					return true;
				} else if ((ca.getSequence().getCharterInMarket() == null) != (ref.getSequence().getCharterInMarket() == null)) {
					return true;
				} else if (ca.getSequence().getVesselAvailability() != null
						&& (!ca.getSequence().getVesselAvailability().getVessel().getName().equals(ref.getSequence().getVesselAvailability().getVessel().getName()))) {
					return true;
				} else if (ca.getSequence().getCharterInMarket() != null && (!ca.getSequence().getCharterInMarket().getName().equals(ref.getSequence().getCharterInMarket().getName()))) {
					return true;
				}
			}

			boolean isSpot = false;
			{
				final Slot caSlot = ca.getSlotAllocation().getSlot();
				final Slot refSlot = ref.getSlotAllocation().getSlot();
				if (!(caSlot instanceof SpotSlot && refSlot instanceof SpotSlot)) {

					final String caName = caSlot == null ? null : caSlot.getName();
					final String refName = refSlot == null ? null : refSlot.getName();

					if (!Equality.isEqual(caName, refName)) {
						return true;
					}
				} else {
					isSpot = true;
				}
			}

			{
				final Port caPort = ca.getSlotAllocation().getPort();
				final Port refPort = ref.getSlotAllocation().getPort();
				final String caName = caPort == null ? null : caPort.getName();
				final String refName = refPort == null ? null : refPort.getName();

				if (!Equality.isEqual(caName, refName)) {
					return true;
				}
			}

			{
				final Contract caContract = ca.getSlotAllocation().getContract();
				final Contract refContract = ref.getSlotAllocation().getContract();
				final String caName = caContract == null ? null : caContract.getName();
				final String refName = refContract == null ? null : refContract.getName();

				if (!Equality.isEqual(caName, refName)) {
					return true;
				}
			}
			return isSpot ? false : isElementDifferent(ref.getSlotAllocation().getCargoAllocation(), ca.getSlotAllocation().getCargoAllocation());
		} else if (pinnedObject instanceof OpenSlotAllocation && otherObject instanceof OpenSlotAllocation) {
			OpenSlotAllocation ref = null;
			OpenSlotAllocation ca = null;
			ref = (OpenSlotAllocation) pinnedObject;

			ca = (OpenSlotAllocation) otherObject;

			{
				final Slot caSlot = ca.getSlot();
				final Slot refSlot = ref.getSlot();
				if (!(caSlot instanceof SpotSlot && refSlot instanceof SpotSlot)) {

					final String caName = caSlot == null ? null : caSlot.getName();
					final String refName = refSlot == null ? null : refSlot.getName();

					if (!Equality.isEqual(caName, refName)) {
						return true;
					}
				}
			}

			{
				final Port caPort = ca.getSlot().getPort();
				final Port refPort = ref.getSlot().getPort();
				final String caName = caPort == null ? null : caPort.getName();
				final String refName = refPort == null ? null : refPort.getName();

				if (!Equality.isEqual(caName, refName)) {
					return true;
				}
			}

			{
				final Contract caContract = ca.getSlot().getContract();
				final Contract refContract = ref.getSlot().getContract();
				final String caName = caContract == null ? null : caContract.getName();
				final String refName = refContract == null ? null : refContract.getName();

				if (!Equality.isEqual(caName, refName)) {
					return true;
				}
			}
			return false;
		} else if (pinnedObject instanceof Event && otherObject instanceof Event) {
			final Event vev = (Event) otherObject;
			final Event ref = (Event) pinnedObject;

			final int refTime = ScheduleModelUtils.getEventDuration(ref);
			final int vevTime = ScheduleModelUtils.getEventDuration(vev);

			// 3 Days
			if (Math.abs(refTime - vevTime) > 3 * 24) {
				return true;
			}

			if (checkNextPortDifferences) {
				final PortVisit vevNextVisit = findNextPortVisit(vev);
				final PortVisit refNextVisit = findNextPortVisit(ref);

				if (vevNextVisit != null && refNextVisit != null) {
					final Port caPort = vevNextVisit.getPort();
					final Port refPort = refNextVisit.getPort();
					final String caName = caPort == null ? null : caPort.getName();
					final String refName = refPort == null ? null : refPort.getName();

					if (!Equality.isEqual(caName, refName)) {
						return true;
					}
				}
			}

			return false;
		}

		return true;
	}

	private @Nullable PortVisit findNextPortVisit(final @NonNull Event evt) {
		Event e = evt.getNextEvent();
		while (!(e instanceof PortVisit)) {
			if (e != null) {
				e = e.getNextEvent();
			} else {
				break;
			}
		}
		return (PortVisit) e;
	}

	public boolean isCheckAssignmentDifferences() {
		return checkAssignmentDifferences;
	}

	public void setCheckAssignmentDifferences(final boolean checkAssignmentDifferences) {
		this.checkAssignmentDifferences = checkAssignmentDifferences;
	}

	public boolean isCheckSpotMarketDifferences() {
		return checkSpotMarketDifferences;
	}

	public void setCheckSpotMarketDifferences(final boolean checkSpotMarketDifferences) {
		this.checkSpotMarketDifferences = checkSpotMarketDifferences;
	}

	public boolean isCheckNextPortDifferences() {
		return checkNextPortDifferences;
	}

	public void setCheckNextPortDifferences(final boolean checkNextPortDifferences) {
		this.checkNextPortDifferences = checkNextPortDifferences;
	}

}
