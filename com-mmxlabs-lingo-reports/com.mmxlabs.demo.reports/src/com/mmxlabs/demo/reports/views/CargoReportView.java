/**
 * Copyright (C) Minimax Labs Ltd., 2010
 * All rights reserved.
 */
package com.mmxlabs.demo.reports.views;

import java.util.ArrayList;

import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.Viewer;

import scenario.ScenarioPackage;
import scenario.cargo.CargoPackage;
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
		addColumn("ID", objectFormatter, s.getCargoAllocation_LoadSlot(),
				c.getSlot_Id()); // TODO cargo id not slot id.

		addColumn("Vessel", objectFormatter, s.getCargoAllocation_Vessel(),
				FleetallocationPackage.eINSTANCE.getAllocatedVessel__GetName());

		addColumn("Load Port", objectFormatter,
				s.getCargoAllocation_LoadSlot(), c.getSlot_Port(),
				p.getPort_Name());

		addColumn("Discharge Port", objectFormatter,
				s.getCargoAllocation_DischargeSlot(), c.getSlot_Port(),
				p.getPort_Name());

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
				return (int) (a.getLadenIdle().getTotalCost()
						+ a.getLadenLeg().getTotalCost());
			}
		});

		addColumn("Ballast Cost", new IntegerFormatter() {
			@Override
			public Integer getIntValue(Object object) {
				final CargoAllocation a = (CargoAllocation) object;
				return (int) (a.getBallastIdle().getTotalCost()
						+ a.getBallastLeg().getTotalCost());
			}
		});

		addColumn("Total Cost", integerFormatter,
				s.getCargoAllocation__GetTotalCost());

		addColumn("Load Entity", objectFormatter,
				s.getCargoAllocation_LoadRevenue(),
				s.getBookedRevenue_Entity(), name);

		addColumn("Discharge Entity", objectFormatter,
				s.getCargoAllocation_DischargeRevenue(),
				s.getBookedRevenue_Entity(), name);

		Object[][] fields = { { s.getCargoAllocation_LoadRevenue(), "Load" },
				{ s.getCargoAllocation_ShippingRevenue(), "Shipping" },
				{ s.getCargoAllocation_DischargeRevenue(), "Discharge" } };

		for (final Object[] f : fields) {
			// addColumn(f[1] + " Revenue", integerFormatter,
			// f[0],
			// s.getBookedRevenue__GetUntaxedRevenues());
			//
			// addColumn(f[1] + " Costs", costFormatter,
			// f[0],
			// s.getBookedRevenue__GetUntaxedCosts());
			//
			// addColumn(f[1] + " untaxed value", integerFormatter,
			// f[0],
			// s.getBookedRevenue__GetUntaxedValue());
			//

			addColumn(f[1] + " taxed value", integerFormatter, f[0],
					s.getBookedRevenue__GetTaxedValue());
		}
	}

	@Override
	protected IStructuredContentProvider getContentProvider() {
		return new IStructuredContentProvider() {
			@Override
			public void inputChanged(Viewer viewer, Object oldInput,
					Object newInput) {

			}

			@Override
			public void dispose() {

			}

			@Override
			public Object[] getElements(Object object) {
				final ArrayList<CargoAllocation> allocations = 
					new ArrayList<CargoAllocation>();
				if (object instanceof Iterable) {
					for (final Object o : (Iterable) object) {
						if (o instanceof Schedule) {
							// collect allocations from object
							allocations.addAll(((Schedule) o).getCargoAllocations());
						}
					}
				}
				return allocations.toArray();
			}
		};
	}

}
