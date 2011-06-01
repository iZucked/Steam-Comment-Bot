/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2011
 * All rights reserved.
 */
package com.mmxlabs.demo.reports.views;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.widgets.Display;

import scenario.Scenario;
import scenario.ScenarioPackage;
import scenario.cargo.CargoPackage;
import scenario.contract.Entity;
import scenario.port.PortPackage;
import scenario.schedule.CargoAllocation;
import scenario.schedule.Schedule;
import scenario.schedule.SchedulePackage;
import scenario.schedule.fleetallocation.FleetallocationPackage;

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
	public static final String ID = "com.mmxlabs.demo.reports.views.CargoReportView";

	public CargoReportView() {
		final CargoPackage c = CargoPackage.eINSTANCE;
		final SchedulePackage s = SchedulePackage.eINSTANCE;

		final EAttribute name = ScenarioPackage.eINSTANCE.getNamedObject_Name();

		final PortPackage p = PortPackage.eINSTANCE;
		addColumn("Schedule", containingScheduleFormatter);
		addColumn("ID", objectFormatter, s.getCargoAllocation__GetName()); // TODO
																			// cargo
																			// id
																			// not
																			// slot
																			// id.

		addColumn("Type", objectFormatter, s.getCargoAllocation_CargoType());

		addColumn("Vessel", objectFormatter, s.getCargoAllocation_Vessel(),
				FleetallocationPackage.eINSTANCE.getAllocatedVessel__GetName());

		addColumn("Load Port", objectFormatter,
				s.getCargoAllocation_LoadSlot(), c.getSlot_Port(),
				name);

		addColumn("Discharge Port", objectFormatter,
				s.getCargoAllocation_DischargeSlot(), c.getSlot_Port(),
				name);

		addColumn("Load Date", calendarFormatter,
				s.getCargoAllocation__GetLocalLoadDate());
		addColumn("Discharge Date", calendarFormatter,
				s.getCargoAllocation__GetLocalDischargeDate());

		addColumn("Load Volume", integerFormatter,
				s.getCargoAllocation__GetLoadVolume());

		addColumn("Fuel Volume", integerFormatter,
				s.getCargoAllocation_FuelVolume());

		addColumn("Discharge Volume", integerFormatter,
				s.getCargoAllocation_DischargeVolume());

		addColumn("Laden Cost", new IntegerFormatter() {
			@Override
			public Integer getIntValue(Object object) {
				final CargoAllocation a = (CargoAllocation) object;
				if (a == null || a.getLadenIdle() == null
						|| a.getLadenLeg() == null)
					return null;
				return (int) (a.getLadenIdle().getTotalCost() + a.getLadenLeg()
						.getTotalCost());

			}
		});

		addColumn("Ballast Cost", new IntegerFormatter() {
			@Override
			public Integer getIntValue(Object object) {
				final CargoAllocation a = (CargoAllocation) object;
				if (a == null || a.getBallastIdle() == null
						|| a.getBallastLeg() == null)
					return null;
				return (int) (a.getBallastIdle().getTotalCost() + a
						.getBallastLeg().getTotalCost());
			}
		});

		addColumn("Total Cost", integerFormatter,
				s.getCargoAllocation__GetTotalCost());

		// addColumn("Discharge Entity", objectFormatter,
		// s.getCargoAllocation_DischargeRevenue(),
		// s.getBookedRevenue_Entity(), name);
		//
		// Object[][] fields = { { s.getCargoAllocation_LoadRevenue(), "Load" },
		// { s.getCargoAllocation_ShippingRevenue(), "Shipping" },
		// { s.getCargoAllocation_DischargeRevenue(), "Discharge" } };
		//
		// for (final Object[] f : fields) {
		// // addColumn(f[1] + " Revenue", integerFormatter,
		// // f[0],
		// // s.getBookedRevenue__GetUntaxedRevenues());
		// //
		// // addColumn(f[1] + " Costs", costFormatter,
		// // f[0],
		// // s.getBookedRevenue__GetUntaxedCosts());
		// //
		// // addColumn(f[1] + " untaxed value", integerFormatter,
		// // f[0],
		// // s.getBookedRevenue__GetUntaxedValue());
		// //
		//
		// addColumn(f[1] + " taxed value", integerFormatter, f[0],
		// s.getBookedRevenue__GetTaxedValue());
		// }
	}

	@Override
	protected IStructuredContentProvider getContentProvider() {
		return new IStructuredContentProvider() {
			@Override
			public void inputChanged(final Viewer viewer, final Object oldInput,
					final Object newInput) {
				Display.getCurrent().asyncExec(
						new Runnable() {
				@Override
				public void run() {
					if (viewer.getControl().isDisposed()) return;
					final Set<Scenario> scenarios = new HashSet<Scenario>();
					if (newInput instanceof Iterable) {
						for (final Object element : ((Iterable) newInput)) {
							if (element instanceof Schedule) {
								// find all referenced entities
								for (final String s : entityColumnNames) {
									removeColumn(s);
								}
								entityColumnNames.clear();
								
								EObject o = (EObject) element;
								while (o != null && !(o instanceof Scenario)) {
									o = o.eContainer();
								}
								
								if (o != null)
									scenarios.add((Scenario) o);
							}
						}
						
					}
					for (final Scenario scenario : scenarios) {
						addEntityColumns(scenario);
					}
					viewer.refresh();
//					viewer.getControl().getParent().layout();
				}});
					
				
			}

			@Override
			public void dispose() {

			}

			@Override
			public Object[] getElements(Object object) {
				final ArrayList<CargoAllocation> allocations = new ArrayList<CargoAllocation>();
				if (object instanceof Iterable) {
					for (final Object o : (Iterable) object) {
						if (o instanceof Schedule) {
							// collect allocations from object
							allocations.addAll(((Schedule) o)
									.getCargoAllocations());
						}
					}
				}
				return allocations.toArray();
			}
		};
	}

	private final List<String> entityColumnNames = new ArrayList<String>();

	protected void addEntityColumns(final Scenario o) {
		for (final Entity e : o.getContractModel().getEntities()) {
			addEntityColumn(e);
		}
		addEntityColumn(o.getContractModel().getShippingEntity());
	}

	private void addEntityColumn(final Entity entity) {
		if (entity == null || entity.getOwnership() == 0)
			return;
		final String title = "Profit to " + entity.getName();
		entityColumnNames.add(title);
		addColumn(title, new IntegerFormatter() {
			@Override
			public Integer getIntValue(final Object object) {
				if (object instanceof CargoAllocation) {
					// display P&L
					int value = 0;
					final CargoAllocation allocation = (CargoAllocation) object;
					if (allocation.getLoadRevenue() != null
							&& entity.equals(allocation.getLoadRevenue()
									.getEntity())) {
						value += allocation.getLoadRevenue().getTaxedValue();
					}
					if (allocation.getShippingRevenue() != null
							&& entity.equals(allocation.getShippingRevenue()
									.getEntity())) {
						value += allocation.getShippingRevenue()
								.getTaxedValue();
					}
					if (allocation.getDischargeRevenue() != null
							&& entity.equals(allocation.getDischargeRevenue()
									.getEntity())) {
						value += allocation.getDischargeRevenue()
								.getTaxedValue();
					}
					return value;
				}

				return null;
			}
		});
	}

}
