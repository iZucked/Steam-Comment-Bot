/**
 * Copyright (C) Minimax Labs Ltd., 2010
 * All rights reserved.
 */
package com.mmxlabs.demo.reports.views;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.Viewer;

import scenario.Scenario;
import scenario.ScenarioPackage;
import scenario.cargo.CargoPackage;
import scenario.cargo.LoadSlot;
import scenario.contract.Entity;
import scenario.port.PortPackage;
import scenario.schedule.CargoAllocation;
import scenario.schedule.Schedule;
import scenario.schedule.SchedulePackage;
import scenario.schedule.Sequence;
import scenario.schedule.events.CharterOutVisit;
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

		addColumn("Schedule", containingScheduleFormatter);

		addColumn("Vessel", objectFormatter,
				ScenarioPackage.eINSTANCE.getScenarioObject__GetContainer(),
				sp.getSequence_Vessel(),
				FleetallocationPackage.eINSTANCE.getAllocatedVessel__GetName());
		addColumn("Type", new BaseFormatter() {
			@Override
			public String format(final Object object) {
				final ScheduledEvent se = (ScheduledEvent) object;
				return se.eClass().getName();
			}
		});

		addColumn("ID", objectFormatter, 
				ep.getSlotVisit_CargoAllocation(),
				sp.getCargoAllocation__GetName()
		);
		addColumn("Start Date", calendarFormatter,
				ep.getScheduledEvent__GetLocalStartTime());
		addColumn("End Date", calendarFormatter,
				ep.getScheduledEvent__GetLocalEndTime());
		addColumn("Duration (DD:HH)", new BaseFormatter() {
			@Override
			public String format(final Object object) {
				final ScheduledEvent se = (ScheduledEvent) object;
				final int duration = se.getEventDuration();
				return String.format("%02d:%02d", duration / 24, duration % 24);
			}

			@Override
			public Comparable getComparable(Object object) {
				return ((ScheduledEvent) object).getEventDuration();
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

		addColumn("Load Volume", new IntegerFormatter() {
			@Override
			public Integer getIntValue(final Object object) {
				if (object instanceof SlotVisit) {
					final SlotVisit sv = (SlotVisit) object;
					final CargoAllocation ca = sv.getCargoAllocation();
					if (ca == null) return null;
					if (ca.getLoadSlot().equals(sv.getSlot())) {
						return (int) ca.getLoadVolume();
					}
				}
				return null;
			}
		});

		addColumn("Discharge Volume", new IntegerFormatter() {
			@Override
			public Integer getIntValue(final Object object) {
				if (object instanceof SlotVisit) {
					final SlotVisit sv = (SlotVisit) object;
					final CargoAllocation ca = sv.getCargoAllocation();
					if (ca == null) return null;
					if (ca.getDischargeSlot().equals(sv.getSlot())) {
						return (int) ca.getDischargeVolume();
					}
				}
				return null;
			}
		});

		for (final FuelType ft : FuelType.values()) {
			addColumn(ft.getName(), new IntegerFormatter() {
				@Override
				public Integer getIntValue(final Object object) {

					if (object instanceof FuelMixture) {
						final FuelMixture mix = (FuelMixture) object;
						for (final FuelQuantity q : mix.getFuelUsage()) {
							if (q.getFuelType().equals(ft)) {
								return (int) q.getQuantity();
							}
						}

						return 0;
					} else {
						return null;
					}
				}
			});
			addColumn(ft.getName() + " Cost", new IntegerFormatter() {
				@Override
				public Integer getIntValue(final Object object) {

					if (object instanceof FuelMixture) {
						final FuelMixture mix = (FuelMixture) object;
						for (final FuelQuantity q : mix.getFuelUsage()) {
							if (q.getFuelType().equals(ft)) {
								return (int) q.getTotalPrice();
							}
						}
						return 0;
					} else {
						return null;
					}
				}
			});
		}

		addColumn("Fuel Cost", new IntegerFormatter() {
			@Override
			public Integer getIntValue(final Object object) {
				if (object instanceof FuelMixture) {
					final FuelMixture mix = (FuelMixture) object;
					return (int) mix.getTotalFuelCost();
				} else {
					return null;
				}
			}
		});
		addColumn("Charter Cost", new IntegerFormatter() {
			@Override
			public Integer getIntValue(final Object object) {
				return (int) ((ScheduledEvent) object).getHireCost();
			}
		});

		addColumn("Canal Cost", integerFormatter, ep.getJourney_RouteCost());
		addColumn("Total Cost", new IntegerFormatter() {
			@Override
			public Integer getIntValue(final Object object) {
				long total = 0;
				if (object instanceof FuelMixture)
					total += ((FuelMixture) object).getTotalFuelCost();
				if (object instanceof ScheduledEvent)
					total += ((ScheduledEvent) object).getHireCost();
				if (object instanceof Journey)
					total += ((Journey) object).getRouteCost();

				return (int) total;
			}
		});

	}

	private List<String> entityColumnNames = new ArrayList<String>();

	@Override
	protected IStructuredContentProvider getContentProvider() {
		return new IStructuredContentProvider() {

			@Override
			public void inputChanged(Viewer viewer, Object oldInput,
					Object newInput) {
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
			}

			@Override
			public void dispose() {

			}

			@Override
			public Object[] getElements(Object object) {
				final ArrayList<ScheduledEvent> allEvents = new ArrayList<ScheduledEvent>();
				if (object instanceof Iterable) {
					for (final Object o : ((Iterable) object)) {
						if (o instanceof Schedule) {
							for (final Sequence seq : ((Schedule) o)
									.getSequences()) {
								allEvents.addAll(seq.getEvents());
							}
						}
					}
				}
				return allEvents.toArray();
			}
		};
	}

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
				if (object instanceof SlotVisit) {
					final SlotVisit slotVisit = (SlotVisit) object;
					if (slotVisit.getSlot() instanceof LoadSlot) {
						// display P&L
						int value = 0;
						final CargoAllocation allocation = slotVisit
								.getCargoAllocation();
						if (entity.equals(allocation.getLoadRevenue()
								.getEntity())) {
							value += allocation.getLoadRevenue()
									.getTaxedValue();
						}
						if (entity.equals(allocation.getShippingRevenue()
								.getEntity())) {
							value += allocation.getShippingRevenue()
									.getTaxedValue();
						}
						if (entity.equals(allocation.getDischargeRevenue()
								.getEntity())) {
							value += allocation.getDischargeRevenue()
									.getTaxedValue();
						}
						return value;
					}
				} else if (object instanceof CharterOutVisit) {
					final CharterOutVisit cov = (CharterOutVisit) object;
					if (entity.equals(cov.getRevenue().getEntity())) {
						return cov.getRevenue().getTaxedValue();
					}
				}
				return null;
			}
		});
	}
}
