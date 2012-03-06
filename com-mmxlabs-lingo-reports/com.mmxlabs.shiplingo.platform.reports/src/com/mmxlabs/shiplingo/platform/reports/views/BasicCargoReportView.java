/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2012
 * All rights reserved.
 */
package com.mmxlabs.shiplingo.platform.reports.views;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.widgets.Display;

import com.mmxlabs.models.lng.cargo.CargoPackage;
import com.mmxlabs.models.lng.schedule.CargoAllocation;
import com.mmxlabs.models.lng.schedule.Schedule;
import com.mmxlabs.models.lng.schedule.SchedulePackage;
import com.mmxlabs.models.mmxcore.MMXCorePackage;
import com.mmxlabs.models.mmxcore.MMXRootObject;

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

		addColumn("Schedule", containingScheduleFormatter);

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
		return new IStructuredContentProvider() {
			@Override
			public void inputChanged(final Viewer viewer, final Object oldInput, final Object newInput) {
				Display.getCurrent().asyncExec(new Runnable() {
					@Override
					public void run() {
						if (viewer.getControl().isDisposed()) {
							return;
						}
//						final Set<MMXRootObject> scenarios = new HashSet<MMXRootObject>();
//						if (newInput instanceof Iterable) {
//							for (final Object element : ((Iterable<?>) newInput)) {
//								if (element instanceof Schedule) {
//									// find all referenced entities
////									for (final String s : entityColumnNames) {
////										removeColumn(s);
////									}
////									entityColumnNames.clear();
//////
////									EObject o = (EObject) element;
////									while ((o != null) && !(o instanceof Scenario)) {
////										o = o.eContainer();
////									}
////
////									if (o != null) {
////										scenarios.add((Scenario) o);
////									}
//								}
//							}
//
//						}
//						for (final Scenario scenario : scenarios) {
//							addEntityColumns(scenario);
//						}
						viewer.refresh();
					}
				});
			}

			@Override
			public void dispose() {

			}

			@Override
			public Object[] getElements(final Object object) {
				final ArrayList<CargoAllocation> allocations = new ArrayList<CargoAllocation>();
				clearInputEquivalents();
				if (object instanceof Iterable) {
					for (final Object o : (Iterable<?>) object) {
						if (o instanceof Schedule) {
							// collect allocations from object
							allocations.addAll(((Schedule) o).getCargoAllocations());
						}
					}
				}

				for (final CargoAllocation allocation : allocations) {
					// map to events
					setInputEquivalents(
							allocation,
							Arrays.asList(new Object[] { 
									allocation.getLoadAllocation().getSlotVisit(), 
									allocation.getLoadAllocation().getSlot(), 
									allocation.getDischargeAllocation().getSlotVisit(), 
									allocation.getDischargeAllocation().getSlot(),
									allocation.getBallastIdle(),
									allocation.getBallastLeg(), 
									allocation.getLadenIdle(), 
									allocation.getLadenLeg() }));
				}

				return allocations.toArray();
			}
		};
	}

	private final List<String> entityColumnNames = new ArrayList<String>();
//
//	protected void addEntityColumns(final Scenario o) {
//		for (final LegalEntity e : o.getContractModel().getEntities()) {
//			addEntityColumn(e);
//		}
//		addEntityColumn(o.getContractModel().getShippingEntity());
//	}
//
//	private void addEntityColumn(final LegalEntity entity) {
////		if (!(entity instanceof GroupEntity)) {
////			return;
////		}
//		final String title = "Profit to " + entity.getName();
//		entityColumnNames.add(title);
//		addColumn(title, new IntegerFormatter() {
//			@Override
//			public Integer getIntValue(final Object object) {
//				if (object instanceof CargoAllocation) {
//					// display P&L
//					int value = 0;
//					final CargoAllocation allocation = (CargoAllocation) object;
//					if ((allocation.getLoadRevenue() != null) && entity.equals(allocation.getLoadRevenue().getEntity())) {
//						value += allocation.getLoadRevenue().getValue();
//					}
//					if ((allocation.getShippingRevenue() != null) && entity.equals(allocation.getShippingRevenue().getEntity())) {
//						value += allocation.getShippingRevenue().getValue();
//					}
//					if ((allocation.getDischargeRevenue() != null) && entity.equals(allocation.getDischargeRevenue().getEntity())) {
//						value += allocation.getDischargeRevenue().getValue();
//					}
//					return value;
//				}
//
//				return null;
//			}
//		});
//	}

}
