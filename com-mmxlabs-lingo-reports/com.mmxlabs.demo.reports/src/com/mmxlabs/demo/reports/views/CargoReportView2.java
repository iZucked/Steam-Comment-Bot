/**
 * Copyright (C) Minimax Labs Ltd., 2010
 * All rights reserved.
 */
package com.mmxlabs.demo.reports.views;

import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.Viewer;

import scenario.cargo.CargoPackage;
import scenario.fleet.FleetPackage;
import scenario.port.PortPackage;
import scenario.schedule.CargoAllocation;
import scenario.schedule.Schedule;
import scenario.schedule.SchedulePackage;
import scenario.schedule.fleetallocation.AllocatedVessel;
import scenario.schedule.fleetallocation.FleetallocationPackage;


/**
 * 
 * 
 * @author hinton
 * 
 */
public class CargoReportView2 extends EMFReportView {
	/**
	 * The ID of the view as specified by the extension.
	 */
	public static final String ID = "com.mmxlabs.demo.reports.views.CargoReportView2";

	public CargoReportView2() {
		final CargoPackage c = CargoPackage.eINSTANCE;
		final SchedulePackage s = SchedulePackage.eINSTANCE;
		
		final PortPackage p = PortPackage.eINSTANCE;
		addColumn("ID", objectFormatter, s.getCargoAllocation_LoadSlot(),
				c.getSlot_Id()); // TODO cargo id not slot id.

		addColumn("Vessel", 
				objectFormatter,
				s.getCargoAllocation_Vessel(),
				FleetallocationPackage.eINSTANCE.getAllocatedVessel__GetName());

		addColumn("Load Port", objectFormatter,
				s.getCargoAllocation_LoadSlot(), c.getSlot_Port(),
				p.getPort_Name());

		addColumn("Discharge Port", objectFormatter,
				s.getCargoAllocation_DischargeSlot(), c.getSlot_Port(),
				p.getPort_Name());

		addColumn(
				"Load Date",
				calendarFormatter,
				s.getCargoAllocation__GetLocalLoadDate());
		addColumn(
				"Discharge Date",
				calendarFormatter,
				s.getCargoAllocation__GetLocalDischargeDate());

		addColumn("Load Volume", new IFormatter() {
			@Override
			public String format(final Object object) {
				final CargoAllocation a = (CargoAllocation) object;
				return a.getDischargeVolume() + a.getFuelVolume() + "";
			}
		});

		addColumn("Fuel Volume", integerFormatter, 
				s.getCargoAllocation_FuelVolume());
		
		addColumn("Discharge Volume", integerFormatter,
				s.getCargoAllocation_DischargeVolume());

		addColumn("Laden Cost", new IFormatter() {
			@Override
			public String format(Object object) {
				if (object instanceof CargoAllocation) {
					final CargoAllocation a = (CargoAllocation) object;
					long totalCost = 0;
					totalCost += (a.getLadenIdle() == null) ? 0 : a.getLadenIdle().getTotalCost();
					totalCost += (a.getLadenLeg() == null) ? 0 : a.getLadenLeg().getTotalCost();
					return String.format("%,d", totalCost);
				}
				return null;
			}
		});

		addColumn("Ballast Cost", new IFormatter() {
			@Override
			public String format(Object object) {
				if (object instanceof CargoAllocation) {
				//TODO this could be an operation on CargoAllocation.
					final CargoAllocation a = (CargoAllocation) object;
					long totalCost = 0;
					totalCost += (a.getBallastIdle() == null) ? 0 : a.getBallastIdle().getTotalCost();
					totalCost += (a.getBallastLeg() == null) ? 0 : a.getBallastLeg().getTotalCost();
					return String.format("%,d", totalCost);
				}
				return null;
			}
		});

		addColumn("Total Cost", new IFormatter() {
			@Override
			public String format(Object object) {
				final CargoAllocation a = (CargoAllocation) object;
				return String.format("%,d", a.getTotalCost());
			}
		});
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
				if (object instanceof Schedule) {
					final Schedule schedule = (Schedule) object;
					return schedule.getCargoAllocations().toArray();
				}
				return new Object[] {};
			}
		};
	}

}
