/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2012
 * All rights reserved.
 */
package com.mmxlabs.shiplingo.platform.reports.views;

import java.util.ArrayList;
import java.util.Collections;

import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.Viewer;

import scenario.ScenarioPackage;
import scenario.schedule.Schedule;
import scenario.schedule.SchedulePackage;
import scenario.schedule.Sequence;
import scenario.schedule.events.EventsPackage;
import scenario.schedule.events.FuelMixture;
import scenario.schedule.events.FuelQuantity;
import scenario.schedule.events.FuelType;
import scenario.schedule.events.Idle;
import scenario.schedule.events.ScheduledEvent;
import scenario.schedule.events.SlotVisit;
import scenario.schedule.events.VesselEventVisit;
import scenario.schedule.fleetallocation.FleetallocationPackage;

/**
 * A report which displays the cooldowns in the selected schedules.
 * 
 * @author hinton
 * 
 */
public class CooldownReportView extends EMFReportView {
	/**
	 * The ID of the view as specified by the extension.
	 */
	public static final String ID = "com.mmxlabs.demo.reports.views.CooldownReportView";

	public CooldownReportView() {
		super("com.mmxlabs.demo.reports.CooldownReportView");

		addColumn("Schedule", containingScheduleFormatter);
		addColumn("Vessel", objectFormatter, ScenarioPackage.eINSTANCE.getScenarioObject__GetContainer(), SchedulePackage.eINSTANCE.getSequence_Vessel(),
				FleetallocationPackage.eINSTANCE.getAllocatedVessel__GetName());
		addColumn("Cause ID", new BaseFormatter() {
			@Override
			public String format(final Object object) {
				if (object instanceof Idle) {
					final Idle idle = (Idle) object;
					final Sequence sequence = (Sequence) idle.eContainer();
					int index = sequence.getEvents().indexOf(idle) - 1;

					while (index >= 0) {
						final ScheduledEvent before = sequence.getEvents().get(index);

						if ((before instanceof SlotVisit) || (before instanceof VesselEventVisit)) {
							return before.getName();
						}

						index--;
					}
				}
				return "";
			}
		});

		addColumn("ID", new BaseFormatter() {
			@Override
			public String format(final Object object) {
				if (object instanceof Idle) {
					final Idle idle = (Idle) object;
					final Sequence sequence = (Sequence) idle.eContainer();
					final int index = sequence.getEvents().indexOf(idle) + 1;
					final ScheduledEvent after = sequence.getEvents().get(index);

					return after.getName();
				}
				return "";
			}
		});

		addColumn("Date", datePartFormatter, EventsPackage.eINSTANCE.getScheduledEvent__GetLocalStartTime());
		addColumn("Time", timePartFormatter, EventsPackage.eINSTANCE.getScheduledEvent__GetLocalStartTime());
		addColumn("Port", objectFormatter, EventsPackage.eINSTANCE.getPortVisit_Port(), ScenarioPackage.eINSTANCE.getNamedObject_Name());
		addColumn("Volume", new IntegerFormatter() {
			@Override
			public Integer getIntValue(final Object object) {
				if (object instanceof FuelMixture) {
					for (final FuelQuantity quantity : ((FuelMixture) object).getFuelUsage()) {
						if (quantity.getFuelType() == FuelType.COOLDOWN) {
							return (int) quantity.getQuantity();
						}
					}
				}
				return null;
			}
		});

		addColumn("Cost", new IntegerFormatter() {
			@Override
			public Integer getIntValue(final Object object) {
				if (object instanceof FuelMixture) {
					for (final FuelQuantity quantity : ((FuelMixture) object).getFuelUsage()) {
						if (quantity.getFuelType() == FuelType.COOLDOWN) {
							return (int) quantity.getTotalPrice();
						}
					}
				}
				return null;
			}
		});
	}

	@Override
	protected IStructuredContentProvider getContentProvider() {
		return new IStructuredContentProvider() {
			@Override
			public void inputChanged(final Viewer viewer, final Object oldInput, final Object newInput) {

			}

			@Override
			public void dispose() {

			}

			@Override
			public Object[] getElements(final Object inputElement) {
				final ArrayList<ScheduledEvent> events = new ArrayList<ScheduledEvent>();
				clearInputEquivalents();
				if (inputElement instanceof Iterable) {
					for (final Object schedule_ : ((Iterable<?>) inputElement)) {
						if (schedule_ instanceof Schedule) {
							final Schedule schedule = (Schedule) schedule_;
							for (final Sequence sequence : schedule.getSequences()) {
								Idle lastIdle = null;
								for (final ScheduledEvent event : sequence.getEvents()) {
									if (event instanceof Idle) {
										final Idle idle = (Idle) event;
										for (final FuelQuantity quantity : idle.getFuelUsage()) {
											if ((quantity.getFuelType() == FuelType.COOLDOWN) && (quantity.getQuantity() > 0)) {
												events.add(idle);
												lastIdle = idle;
											}
										}
									} else if ((event instanceof SlotVisit) && (lastIdle != null)) {
										setInputEquivalents(event, Collections.singleton((Object) ((SlotVisit) event).getCargoAllocation()));
										lastIdle = null;
									}
								}
							}
						}
					}
				}

				return events.toArray();
			}
		};
	}

	@Override
	protected boolean handleSelections() {
		return true;
	}
}
