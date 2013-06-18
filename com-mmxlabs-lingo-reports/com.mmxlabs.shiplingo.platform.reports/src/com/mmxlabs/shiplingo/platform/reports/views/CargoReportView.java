/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2013
 * All rights reserved.
 */
package com.mmxlabs.shiplingo.platform.reports.views;

import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;

import com.mmxlabs.models.lng.cargo.CargoPackage;
import com.mmxlabs.models.lng.schedule.CargoAllocation;
import com.mmxlabs.models.lng.schedule.Event;
import com.mmxlabs.models.lng.schedule.Idle;
import com.mmxlabs.models.lng.schedule.Journey;
import com.mmxlabs.models.lng.schedule.SchedulePackage;
import com.mmxlabs.models.lng.schedule.Sequence;
import com.mmxlabs.models.lng.schedule.SlotAllocation;
import com.mmxlabs.models.lng.schedule.SlotVisit;
import com.mmxlabs.models.mmxcore.MMXCorePackage;

/**
 * 
 * 
 * @author hinton
 * 
 */
public class CargoReportView extends AbstractCargoReportView {
	/**
	 * The ID of the view as specified by the extension.
	 */
	public static final String ID = "com.mmxlabs.shiplingo.platform.reports.views.CargoReportView";

	public CargoReportView() {
		super("com.mmxlabs.shiplingo.platform.reports.CargoReportView");

		final SchedulePackage s = SchedulePackage.eINSTANCE;

		final EAttribute name = MMXCorePackage.eINSTANCE.getNamedObject_Name();

		addScheduleColumn("Schedule", containingScheduleFormatter);

		addColumn("ID", objectFormatter, cargoAllocationRef, s.getCargoAllocation__GetName());

		addColumn("Type", objectFormatter, cargoAllocationRef, s.getCargoAllocation_InputCargo(), CargoPackage.eINSTANCE.getCargo__GetCargoType());

		addColumn("Vessel", new BaseFormatter() {
			@Override
			public String format(final Object object) {

				if (object instanceof Sequence) {
					final Sequence sequence = (Sequence) object;
					if (sequence.getVesselAvailability() == null) {
						return "Chartered";
					} else {
						return sequence.getVesselAvailability().getVessel().getName();
					}
				}

				return super.format(object);
			}
		}, cargoAllocationRef, s.getCargoAllocation_Sequence());

		addColumn("Load Port", objectFormatter, loadAllocationRef, s.getSlotAllocation__GetPort(), name);

		addColumn("Discharge Port", objectFormatter, dischargeAllocationRef, s.getSlotAllocation__GetPort(), name);

		addColumn("Load Date", datePartFormatter, loadAllocationRef, s.getSlotAllocation__GetLocalStart());
		addColumn("Load Time", timePartFormatter, loadAllocationRef, s.getSlotAllocation__GetLocalStart());

		addColumn("Discharge Date", datePartFormatter, dischargeAllocationRef, s.getSlotAllocation__GetLocalEnd());
		addColumn("Discharge Time", timePartFormatter, dischargeAllocationRef, s.getSlotAllocation__GetLocalEnd());

		addColumn("Load Volume", integerFormatter, loadAllocationRef, s.getSlotAllocation_VolumeTransferred());

		addColumn("Discharge Volume", integerFormatter, dischargeAllocationRef, s.getSlotAllocation_VolumeTransferred());

		addColumn("Laden Cost", new IntegerFormatter() {
			@Override
			public Integer getIntValue(final Object object) {

				return calculateLegCost(object, loadAllocationRef);
			}

		});

		addColumn("Ballast Cost", new IntegerFormatter() {
			@Override
			public Integer getIntValue(final Object object) {

				return calculateLegCost(object, dischargeAllocationRef);
			}
		});

		addColumn("Total Cost", new IntegerFormatter() {
			@Override
			public Integer getIntValue(final Object object) {
				if (object instanceof CargoAllocation) {
					final CargoAllocation allocation = (CargoAllocation) object;

					int total = 0;
					for (final Event event : allocation.getEvents()) {

						if (event instanceof SlotVisit) {
							final SlotVisit slotVisit = (SlotVisit) event;
							total += slotVisit.getFuelCost();
							total += slotVisit.getHireCost();
							total += slotVisit.getPortCost();
						} else if (event instanceof Journey) {
							final Journey journey = (Journey) event;
							total += journey.getFuelCost();
							total += journey.getHireCost();
							total += journey.getToll();
						} else if (event instanceof Idle) {
							final Idle idle = (Idle) event;
							total += idle.getFuelCost();
							total += idle.getHireCost();
						}
					}
					return total;
				}
				return null;

			}
		}, cargoAllocationRef);
	}

	private Integer calculateLegCost(final Object object, EStructuralFeature allocationRef) {
		if (object instanceof EObject) {
			final EObject eObject = (EObject) object;
			final CargoAllocation cargoAllocation = (CargoAllocation) eObject.eGet(cargoAllocationRef);
			final SlotAllocation allocation = (SlotAllocation) eObject.eGet(allocationRef);
			if (allocation != null && cargoAllocation != null) {

				boolean collecting = false;
				int total = 0;
				for (final Event event : cargoAllocation.getEvents()) {
					if (event instanceof SlotVisit) {
						final SlotVisit slotVisit = (SlotVisit) event;
						if (allocation.getSlotVisit() == event) {
							collecting = true;
						} else {
							if (collecting) {
								// Finished!
								break;
							}
						}
						if (collecting) {
							total += slotVisit.getFuelCost();
							total += slotVisit.getHireCost();
							total += slotVisit.getPortCost();
						}

					} else if (event instanceof Journey) {
						final Journey journey = (Journey) event;
						if (collecting) {
							total += journey.getFuelCost();
							total += journey.getHireCost();
							total += journey.getToll();
						}
					} else if (event instanceof Idle) {
						final Idle idle = (Idle) event;
						if (collecting) {
							total += idle.getFuelCost();
							total += idle.getHireCost();
						}
					}
				}

				return total;
			}

		}
		return null;
	}
}
