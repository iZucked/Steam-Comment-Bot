/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2012
 * All rights reserved.
 */
package com.mmxlabs.shiplingo.platform.reports.views;

import java.util.Arrays;
import java.util.Collection;

import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.Viewer;

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
	protected IStructuredContentProvider getContentProvider() {
		final IStructuredContentProvider superProvider = super.getContentProvider();
		return new IStructuredContentProvider() {
			@Override
			public void inputChanged(final Viewer viewer, final Object oldInput, final Object newInput) {
				superProvider.inputChanged(viewer, oldInput, newInput);
			}

			@Override
			public void dispose() {
				superProvider.dispose();
			}

			@Override
			public Object[] getElements(final Object object) {
				clearInputEquivalents();
				final Object[] result = superProvider.getElements(object);

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

				return result;
			}
		};
	}

	@Override
	protected IScenarioInstanceElementCollector getElementCollector() {
		return new ScheduleElementCollector() {
			@Override
			protected Collection<? extends Object> collectElements(Schedule schedule) {
				return schedule.getCargoAllocations();
			}
		};
	}
}
