/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2012
 * All rights reserved.
 */
package com.mmxlabs.shiplingo.platform.reports.views;

import java.util.Arrays;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

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
				final Object[] result;
				if (currentlyPinned) {
					final List<CargoAllocation> objects = new LinkedList<CargoAllocation>();
					for (final Map.Entry<String, List<CargoAllocation>> e : namedObjects.entrySet()) {
						boolean isFirst = true;
						CargoAllocation ref = null;
						final LinkedHashSet<CargoAllocation> objectsToAdd = new LinkedHashSet<CargoAllocation>();
						for (final CargoAllocation ca : e.getValue()) {
							if (isFirst) {
								ref = ca;
								isFirst = false;
							} else if (ref != null) {
								boolean different = false;
								if ((ca.getSequence().getVessel() == null) != (ref.getSequence().getVessel() == null)) {
									different = true;
								} else if ((ca.getSequence().getVesselClass() == null) != (ref.getSequence().getVesselClass() == null)) {
									different = true;
								} else if (ca.getSequence().getVessel() != null && (!ca.getSequence().getVessel().getName().equals(ref.getSequence().getVessel().getName()))) {
									different = true;
								} else if (ca.getSequence().getVesselClass() != null && (!ca.getSequence().getVessel().getName().equals(ref.getSequence().getVesselClass().getName()))) {
									different = true;
								}
								if (different) {
									objectsToAdd.add(ref);
									objectsToAdd.add(ca);
								}
							}
						}
						objects.addAll(objectsToAdd);

					}
					result = objects.toArray();
				} else {
					result = superProvider.getElements(object);
				}

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

	private boolean currentlyPinned = false;

	private final Map<String, List<CargoAllocation>> namedObjects = new LinkedHashMap<String, List<CargoAllocation>>();

	@Override
	protected IScenarioInstanceElementCollector getElementCollector() {
		return new ScheduleElementCollector() {

			@Override
			public void beginCollecting() {
				super.beginCollecting();
				currentlyPinned = false;
				namedObjects.clear();
			}

			@Override
			protected Collection<? extends Object> collectElements(final Schedule schedule, final boolean isPinned) {
				currentlyPinned |= isPinned;

				final List<CargoAllocation> cargoAllocations = schedule.getCargoAllocations();

				for (final CargoAllocation ca : cargoAllocations) {
					final List<CargoAllocation> l;
					if (namedObjects.containsKey(ca.getName())) {
						l = namedObjects.get(ca.getName());
					} else {
						l = new LinkedList<CargoAllocation>();
						namedObjects.put(ca.getName(), l);
					}

					l.add(ca);
				}

				return cargoAllocations;
			}
		};
	}
}
