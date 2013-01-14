/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2013
 * All rights reserved.
 */
package com.mmxlabs.shiplingo.platform.reports.views;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EObject;

import com.mmxlabs.models.lng.cargo.CargoPackage;
import com.mmxlabs.models.lng.schedule.CargoAllocation;
import com.mmxlabs.models.lng.schedule.Schedule;
import com.mmxlabs.models.lng.schedule.SchedulePackage;
import com.mmxlabs.models.mmxcore.MMXCorePackage;
import com.mmxlabs.shiplingo.platform.reports.IScenarioInstanceElementCollector;
import com.mmxlabs.shiplingo.platform.reports.ScheduleElementCollector;

/**
 * 
 * 
 * @author hinton
 * 
 */
public class BasicCargoReportView extends EMFReportView {
	/**
	 * The ID of the view as specified by the extension.
	 */
	public static final String ID = "com.mmxlabs.shiplingo.platform.reports.views.BasicCargoReportView";

	public BasicCargoReportView() {
		super("com.mmxlabs.shiplingo.platform.reports.BasicCargoReportView");

		final CargoPackage c = CargoPackage.eINSTANCE;
		final SchedulePackage s = SchedulePackage.eINSTANCE;

		final EAttribute name = MMXCorePackage.eINSTANCE.getNamedObject_Name();

		addScheduleColumn("Schedule", containingScheduleFormatter);

		// TODO cargo id not slot id.
		addColumn("ID", objectFormatter, s.getCargoAllocation__GetName());

		addColumn("Type", objectFormatter, s.getCargoAllocation__GetType());

		addColumn("Load Port", objectFormatter, s.getCargoAllocation_LoadAllocation(), s.getSlotAllocation__GetPort(), name);
		addColumn("Load Date", datePartFormatter, s.getCargoAllocation_LoadAllocation(), s.getSlotAllocation__GetLocalStart());
		addColumn("Purchase Contract", objectFormatter, s.getCargoAllocation_LoadAllocation(), s.getSlotAllocation__GetContract(), name);

		addColumn("Discharge Port", objectFormatter, s.getCargoAllocation_DischargeAllocation(), s.getSlotAllocation__GetPort(), name);
		addColumn("Discharge Date", datePartFormatter, s.getCargoAllocation_DischargeAllocation(), s.getSlotAllocation__GetLocalStart());
		addColumn("Sales Contract", objectFormatter, s.getCargoAllocation_DischargeAllocation(), s.getSlotAllocation__GetContract(), name);

		addColumn("Vessel", objectFormatter, s.getCargoAllocation_Sequence(), SchedulePackage.eINSTANCE.getSequence__GetName());

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
						Arrays.asList(new Object[] { allocation.getLoadAllocation().getSlotVisit(), allocation.getLoadAllocation().getSlot(), allocation.getDischargeAllocation().getSlotVisit(),
								allocation.getDischargeAllocation().getSlot(), allocation.getBallastIdle(), allocation.getBallastLeg(), allocation.getLadenIdle(), allocation.getLadenLeg(),
								allocation.getInputCargo() }));
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
				BasicCargoReportView.this.clearPinModeData();
			}

			@Override
			protected Collection<? extends Object> collectElements(final Schedule schedule, final boolean isPinned) {

				final List<CargoAllocation> cargoAllocations = schedule.getCargoAllocations();

				BasicCargoReportView.this.collectPinModeElements(cargoAllocations, isPinned);

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
