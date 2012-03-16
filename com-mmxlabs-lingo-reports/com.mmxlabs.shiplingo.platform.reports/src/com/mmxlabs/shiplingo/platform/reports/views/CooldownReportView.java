/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2012
 * All rights reserved.
 */
package com.mmxlabs.shiplingo.platform.reports.views;

import java.util.ArrayList;
import java.util.Collections;

import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.Viewer;

import com.mmxlabs.models.lng.schedule.Cooldown;
import com.mmxlabs.models.lng.schedule.Event;
import com.mmxlabs.models.lng.schedule.Idle;
import com.mmxlabs.models.lng.schedule.Schedule;
import com.mmxlabs.models.lng.schedule.SchedulePackage;
import com.mmxlabs.models.lng.schedule.Sequence;
import com.mmxlabs.models.lng.schedule.SlotVisit;
import com.mmxlabs.models.lng.schedule.VesselEventVisit;
import com.mmxlabs.models.mmxcore.MMXCorePackage;

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
	public static final String ID = "com.mmxlabs.shiplingo.platform.reports.views.CooldownReportView";

	public CooldownReportView() {
		super("com.mmxlabs.shiplingo.platform.reports.CooldownReportView");

		addColumn("Schedule", containingScheduleFormatter);
		addColumn("Vessel", objectFormatter, 
				MMXCorePackage.eINSTANCE.getMMXObject__EContainerOp(),
				SchedulePackage.eINSTANCE.getSequence__GetName());
		addColumn("Cause ID", new BaseFormatter() {
			@Override
			public String format(final Object object) {
				if (object instanceof Idle) {
					final Idle idle = (Idle) object;
					final Sequence sequence = (Sequence) idle.eContainer();
					int index = sequence.getEvents().indexOf(idle) - 1;

					while (index >= 0) {
						final Event before = sequence.getEvents().get(index);

						if ((before instanceof SlotVisit) || (before instanceof VesselEventVisit)) {
							return before.name();
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
					final Event after = sequence.getEvents().get(index);

					return after.name();
				}
				return "";
			}
		});

		addColumn("Date", datePartFormatter, SchedulePackage.eINSTANCE.getEvent__GetLocalStart());
		addColumn("Time", timePartFormatter, SchedulePackage.eINSTANCE.getEvent__GetLocalStart());
		addColumn("Port", objectFormatter, SchedulePackage.eINSTANCE.getEvent_Port(), MMXCorePackage.eINSTANCE.getNamedObject_Name());
		addColumn("Volume", new IntegerFormatter() {
			@Override
			public Integer getIntValue(final Object object) {
				if (object instanceof Cooldown) {
					return ((Cooldown) object).getVolume();
				}
				return null;
			}
		});

		addColumn("Cost", new IntegerFormatter() {
			@Override
			public Integer getIntValue(final Object object) {
				if (object instanceof Cooldown) {
					return ((Cooldown) object).getCost();
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
				final ArrayList<Event> events = new ArrayList<Event>();
				clearInputEquivalents();
				if (inputElement instanceof Iterable) {
					for (final Object schedule_ : ((Iterable<?>) inputElement)) {
						if (schedule_ instanceof Schedule) {
							final Schedule schedule = (Schedule) schedule_;
							for (final Sequence sequence : schedule.getSequences()) {
								Cooldown lastCooldown = null;
								for (final Event event : sequence.getEvents()) {
									if (event instanceof Cooldown) {
										events.add(event);
										lastCooldown = (Cooldown) event;
									} else if ((event instanceof SlotVisit) && (lastCooldown != null)) {
										setInputEquivalents(event, Collections.singleton((Object) ((SlotVisit) event).getSlotAllocation().getCargoAllocation()));
										lastCooldown = null;
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
