/**
 * Copyright (C) Minimax Labs Ltd., 2010
 * All rights reserved.
 */
package com.mmxlabs.demo.reports.views;

import java.util.ArrayList;

import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.Viewer;

import scenario.ScenarioPackage;
import scenario.cargo.CargoPackage;
import scenario.port.PortPackage;
import scenario.schedule.CargoAllocation;
import scenario.schedule.Schedule;
import scenario.schedule.SchedulePackage;
import scenario.schedule.Sequence;
import scenario.schedule.events.EventsPackage;
import scenario.schedule.events.FuelMixture;
import scenario.schedule.events.FuelQuantity;
import scenario.schedule.events.FuelType;
import scenario.schedule.events.Journey;
import scenario.schedule.events.ScheduledEvent;
import scenario.schedule.events.SlotVisit;
import scenario.schedule.fleetallocation.FleetallocationPackage;

/**
 * @author hinton
 * 
 */
public class PortRotationReportView extends EMFReportView {
	/**
	 * The ID of the view as specified by the extension.
	 */
	public static final String ID = "com.mmxlabs.demo.reports.views.PortRotationReportView";

	public PortRotationReportView() {
		final SchedulePackage sp = SchedulePackage.eINSTANCE;
		final EventsPackage ep = EventsPackage.eINSTANCE;
		final CargoPackage cp = CargoPackage.eINSTANCE;
		final PortPackage pp = PortPackage.eINSTANCE;
		addColumn("Vessel", objectFormatter,
				ScenarioPackage.eINSTANCE.getScenarioObject__GetContainer(),
				sp.getSequence_Vessel(),
				FleetallocationPackage.eINSTANCE.getAllocatedVessel__GetName());
		addColumn("Type", new IFormatter() {
			@Override
			public String format(final Object object) {
				final ScheduledEvent se = (ScheduledEvent) object;
				return se.eClass().getName();
			}
		});
		addColumn("ID", objectFormatter, ep.getSlotVisit_Slot(),
				cp.getSlot_Id());
		addColumn("Start Date", calendarFormatter,
				ep.getScheduledEvent__GetLocalStartTime());
		addColumn("End Date", calendarFormatter,
				ep.getScheduledEvent__GetLocalEndTime());
		addColumn("Duration (DD:HH)", new IFormatter() {
			@Override
			public String format(final Object object) {
				final ScheduledEvent se = (ScheduledEvent) object;
				final int duration = se.getEventDuration();
				return String.format("%02d:%02d", duration / 24, duration % 24);
			}
		});
		addColumn("Speed", objectFormatter, ep.getJourney_Speed());
		addColumn("From Port", objectFormatter, ep.getJourney_FromPort(),
				pp.getPort_Name());
		addColumn("To Port", objectFormatter, ep.getJourney_ToPort(),
				pp.getPort_Name());
		addColumn("At Port", objectFormatter, ep.getPortVisit_Port(),
				pp.getPort_Name());
		addColumn("Route", objectFormatter, ep.getJourney_Route());

		addColumn("Load Volume", new IFormatter() {
			@Override
			public String format(final Object object) {
				if (object instanceof SlotVisit) {
					final SlotVisit sv = (SlotVisit) object;
					final CargoAllocation ca = sv.getCargoAllocation();

					if (ca.getLoadSlot().equals(sv.getSlot())) {
						//TODO add operation for this
						return String.format("%,d",
								ca.getFuelVolume() + ca.getDischargeVolume());
					}
				}
				return "";
			}
		});

		addColumn("Discharge Volume", new IFormatter() {
			@Override
			public String format(final Object object) {
				if (object instanceof SlotVisit) {
					final SlotVisit sv = (SlotVisit) object;
					final CargoAllocation ca = sv.getCargoAllocation();

					if (ca.getDischargeSlot().equals(sv.getSlot())) {
						return String.format("%,d", ca.getDischargeVolume());
					}
				}
				return "";
			}
		});

		for (final FuelType ft : FuelType.values()) {
			addColumn(ft.getName(), new IFormatter() {
				@Override
				public String format(final Object object) {

					if (object instanceof FuelMixture) {
						final FuelMixture mix = (FuelMixture) object;
						for (final FuelQuantity q : mix.getFuelUsage()) {
							if (q.getFuelType().equals(ft)) {
								return String.format("%,d", q.getQuantity());
							}
						}

						return "0";
					} else {
						return "";
					}
				}
			});
			addColumn(ft.getName() + " Cost", new IFormatter() {
				@Override
				public String format(final Object object) {

					if (object instanceof FuelMixture) {
						final FuelMixture mix = (FuelMixture) object;
						for (final FuelQuantity q : mix.getFuelUsage()) {
							if (q.getFuelType().equals(ft)) {
								return String.format("%,d", q.getTotalPrice());
							}
						}
						return "0";
					} else {
						return "";
					}
				}
			});
		}

		addColumn("Fuel Cost", new IFormatter() {
			@Override
			public String format(final Object object) {
				if (object instanceof FuelMixture) {
					final FuelMixture mix = (FuelMixture) object;
					return String.format("%,d", mix.getTotalFuelCost());
				} else {
					return "";
				}
			}
		});
		addColumn("Charter Cost", new IFormatter() {
			@Override
			public String format(final Object object) {
				return String.format("%,d",
						((ScheduledEvent) object).getHireCost());
			}
		});

		addColumn("Canal Cost", integerFormatter, ep.getJourney_RouteCost());
		addColumn("Total Cost", new IFormatter() {
			@Override
			public String format(final Object object) {
				long total = 0;
				if (object instanceof FuelMixture)
					total += ((FuelMixture) object).getTotalFuelCost();
				if (object instanceof ScheduledEvent)
					total += ((ScheduledEvent) object).getHireCost();
				if (object instanceof Journey)
					total += ((Journey) object).getRouteCost();

				return String.format("%,d", total);
			}
		});
	}

	@Override
	protected IStructuredContentProvider getContentProvider() {
		return new IStructuredContentProvider() {

			@Override
			public void inputChanged(Viewer viewer, Object oldInput,
					Object newInput) {
				// TODO Auto-generated method stub

			}

			@Override
			public void dispose() {
				// TODO Auto-generated method stub

			}

			@Override
			public Object[] getElements(Object object) {

				if (object instanceof Schedule) {
					final ArrayList<ScheduledEvent> allEvents = new ArrayList<ScheduledEvent>();
					final Schedule schedule = (Schedule) object;
					for (final Sequence seq : schedule.getSequences()) {
						allEvents.addAll(seq.getEvents());
					}
					return allEvents.toArray();
				}
				return new Object[] {};
			}
		};
	}

}
