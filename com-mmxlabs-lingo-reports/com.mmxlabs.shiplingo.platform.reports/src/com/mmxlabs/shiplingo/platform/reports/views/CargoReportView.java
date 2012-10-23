/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2012
 * All rights reserved.
 */
package com.mmxlabs.shiplingo.platform.reports.views;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EObject;

import com.mmxlabs.models.lng.schedule.CargoAllocation;
import com.mmxlabs.models.lng.schedule.Idle;
import com.mmxlabs.models.lng.schedule.Journey;
import com.mmxlabs.models.lng.schedule.Schedule;
import com.mmxlabs.models.lng.schedule.SchedulePackage;
import com.mmxlabs.models.lng.schedule.Sequence;
import com.mmxlabs.models.lng.schedule.SlotAllocation;
import com.mmxlabs.models.mmxcore.MMXCorePackage;
import com.mmxlabs.shiplingo.platform.reports.IScenarioInstanceElementCollector;
import com.mmxlabs.shiplingo.platform.reports.ScheduleElementCollector;

/**
 * 
 * 
 * @author hinton
 * 
 */
public class CargoReportView extends EMFReportView {
	/**
	 * The ID of the view as specified by the extension.
	 */
	public static final String ID = "com.mmxlabs.shiplingo.platform.reports.views.CargoReportView";

	public CargoReportView() {
		super("com.mmxlabs.shiplingo.platform.reports.CargoReportView");

		final SchedulePackage s = SchedulePackage.eINSTANCE;

		final EAttribute name = MMXCorePackage.eINSTANCE.getNamedObject_Name();

		addScheduleColumn("Schedule", containingScheduleFormatter);

		addColumn("ID", objectFormatter, s.getCargoAllocation__GetName());

		addColumn("Type", objectFormatter, s.getCargoAllocation__GetType());

		addColumn("Vessel", new BaseFormatter() {
			@Override
			public String format(final Object object) {

				if (object instanceof Sequence) {
					final Sequence sequence = (Sequence) object;
					if (sequence.getVessel() == null) {
						return "Chartered";
					} else {
						return sequence.getVessel().getName();
					}
				}

				return super.format(object);
			}
		}, s.getCargoAllocation_Sequence());

		addColumn("Load Port", objectFormatter, s.getCargoAllocation_LoadAllocation(), s.getSlotAllocation__GetPort(), name);

		addColumn("Discharge Port", objectFormatter, s.getCargoAllocation_DischargeAllocation(), s.getSlotAllocation__GetPort(), name);

		addColumn("Load Date", datePartFormatter, s.getCargoAllocation_LoadAllocation(), s.getSlotAllocation__GetLocalStart());
		addColumn("Load Time", timePartFormatter, s.getCargoAllocation_LoadAllocation(), s.getSlotAllocation__GetLocalStart());

		addColumn("Discharge Date", datePartFormatter, s.getCargoAllocation_DischargeAllocation(), s.getSlotAllocation__GetLocalEnd());
		addColumn("Discharge Time", timePartFormatter, s.getCargoAllocation_DischargeAllocation(), s.getSlotAllocation__GetLocalEnd());

		addColumn("Load Volume", integerFormatter, s.getCargoAllocation_LoadVolume());

		// addColumn("Fuel Volume", integerFormatter, s.getCargoAllocation_FuelVolume());

		addColumn("Discharge Volume", integerFormatter, s.getCargoAllocation_DischargeVolume());

		addColumn("Laden Cost", new IntegerFormatter() {
			@Override
			public Integer getIntValue(final Object object) {

				if (object instanceof CargoAllocation) {
					final CargoAllocation allocation = (CargoAllocation) object;

					int total = 0;
					final SlotAllocation slotAllocation = allocation.getLoadAllocation();
					total += slotAllocation.getSlotVisit().getFuelCost();
					total += slotAllocation.getSlotVisit().getHireCost();
					total += slotAllocation.getSlotVisit().getPortCost();

					final Journey journey = allocation.getLadenLeg();
					if (journey != null) {
						total += journey.getFuelCost();
						total += journey.getHireCost();
						total += journey.getToll();
					}

					final Idle idle = allocation.getLadenIdle();
					if (idle != null) {
						total += idle.getFuelCost();
						total += idle.getHireCost();
					}
					return total;
				}
				return null;
			}

		});

		addColumn("Ballast Cost", new IntegerFormatter() {
			@Override
			public Integer getIntValue(final Object object) {

				if (object instanceof CargoAllocation) {
					final CargoAllocation allocation = (CargoAllocation) object;

					int total = 0;

					final SlotAllocation slotAllocation = allocation.getDischargeAllocation();
					total += slotAllocation.getSlotVisit().getFuelCost();
					total += slotAllocation.getSlotVisit().getHireCost();
					total += slotAllocation.getSlotVisit().getPortCost();

					final Journey journey = allocation.getBallastLeg();
					if (journey != null) {
						total += journey.getFuelCost();
						total += journey.getHireCost();
						total += journey.getToll();
					}

					final Idle idle = allocation.getBallastIdle();
					if (idle != null) {
						total += idle.getFuelCost();
						total += idle.getHireCost();
					}

					return total;
				}
				return null;
			}
		});

		addColumn("Total Cost", new IntegerFormatter() {
			@Override
			public Integer getIntValue(final Object object) {
				if (object instanceof CargoAllocation) {
					final CargoAllocation allocation = (CargoAllocation) object;

					int total = 0;
					{
						final SlotAllocation slotAllocation = allocation.getLoadAllocation();
						total += slotAllocation.getSlotVisit().getFuelCost();
						total += slotAllocation.getSlotVisit().getHireCost();
						total += slotAllocation.getSlotVisit().getPortCost();

						final Journey journey = allocation.getLadenLeg();
						if (journey != null) {
							total += journey.getFuelCost();
							total += journey.getHireCost();
							total += journey.getToll();
						}

						final Idle idle = allocation.getLadenIdle();
						if (idle != null) {
							total += idle.getFuelCost();
							total += idle.getHireCost();
						}
					}
					{
						final SlotAllocation slotAllocation = allocation.getDischargeAllocation();
						total += slotAllocation.getSlotVisit().getFuelCost();
						total += slotAllocation.getSlotVisit().getHireCost();
						total += slotAllocation.getSlotVisit().getPortCost();

						final Journey journey = allocation.getBallastLeg();
						if (journey != null) {
							total += journey.getFuelCost();
							total += journey.getHireCost();
							total += journey.getToll();
						}

						final Idle idle = allocation.getBallastIdle();
						if (idle != null) {
							total += idle.getFuelCost();
							total += idle.getHireCost();
						}
					}
					return total;
				}
				return null;

			}
		});
	}

	@Override
	protected boolean handleSelections() {
		return true;
	}

	@Override
	protected Class<?> getSelectionAdaptionClass() {
		return CargoAllocation.class;
	}

	@Override
	protected void processInputs(final Object[] result) {
		for (final Object a : result) {
			// map to events
			if (a instanceof CargoAllocation) {
				final CargoAllocation allocation = (CargoAllocation) a;

				setInputEquivalents(
						allocation,
						Arrays.asList(new Object[] { allocation.getLoadAllocation().getSlotVisit(), allocation.getLoadAllocation().getSlot(),
								allocation.getDischargeAllocation().getSlotVisit(), allocation.getDischargeAllocation().getSlot(), allocation.getBallastIdle(), allocation.getBallastLeg(),
								allocation.getLadenIdle(), allocation.getLadenLeg(), allocation.getInputCargo() }));
			}
		}
	}

	@Override
	protected boolean isElementDifferent(EObject pinnedObject, EObject otherObject) {
		CargoAllocation ref = null;
		if (pinnedObject instanceof CargoAllocation) {
			ref = (CargoAllocation) pinnedObject;
		}

		CargoAllocation ca = null;
		if (otherObject instanceof CargoAllocation) {
			ca = (CargoAllocation) otherObject;
		}

		if (ca == null || ref == null) {
			return true;
		}

		boolean different = false;

		// Check vessel
		if ((ca.getSequence().getVessel() == null) != (ref.getSequence().getVessel() == null)) {
			different = true;
		} else if ((ca.getSequence().getVesselClass() == null) != (ref.getSequence().getVesselClass() == null)) {
			different = true;
		} else if (ca.getSequence().getVessel() != null && (!ca.getSequence().getVessel().getName().equals(ref.getSequence().getVessel().getName()))) {
			different = true;
		} else if (ca.getSequence().getVesselClass() != null && (!ca.getSequence().getVesselClass().getName().equals(ref.getSequence().getVesselClass().getName()))) {
			different = true;
		}

		if (!different) {
			if (!ca.getLoadAllocation().getPort().getName().equals(ref.getLoadAllocation().getPort().getName())) {
				different = true;
			}
		}
		if (!different) {
			if (!ca.getLoadAllocation().getContract().getName().equals(ref.getLoadAllocation().getContract().getName())) {
				different = true;
			}
		}
		if (!different) {
			if (!ca.getDischargeAllocation().getPort().getName().equals(ref.getDischargeAllocation().getPort().getName())) {
				different = true;
			}
		}
		if (!different) {
			if (!ca.getDischargeAllocation().getContract().getName().equals(ref.getDischargeAllocation().getContract().getName())) {
				different = true;
			}
		}

		return different;
	}

	protected IScenarioInstanceElementCollector getElementCollector() {
		return new ScheduleElementCollector() {

			@Override
			public void beginCollecting() {
				super.beginCollecting();
				CargoReportView.this.clearPinModeData();
			}

			@Override
			protected Collection<? extends Object> collectElements(final Schedule schedule, final boolean isPinned) {

				final List<CargoAllocation> cargoAllocations = schedule.getCargoAllocations();

				CargoReportView.this.collectPinModeElements(cargoAllocations, isPinned);

				return cargoAllocations;
			}
		};
	}

	/**
	 * Returns a key of some kind for the element
	 * 
	 * @param element
	 * @return
	 * @since 1.1
	 */
	protected String getElementKey(final EObject element) {
		if (element instanceof CargoAllocation) {
			return ((CargoAllocation) element).getName();
		}
		return super.getElementKey(element);
	}
}
