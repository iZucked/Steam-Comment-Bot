/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2013
 * All rights reserved.
 */
package com.mmxlabs.shiplingo.platform.reports.views;

import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.Viewer;

import com.mmxlabs.models.lng.schedule.Cooldown;
import com.mmxlabs.models.lng.schedule.Event;
import com.mmxlabs.models.lng.schedule.Idle;
import com.mmxlabs.models.lng.schedule.SchedulePackage;
import com.mmxlabs.models.lng.schedule.Sequence;
import com.mmxlabs.models.lng.schedule.SlotVisit;
import com.mmxlabs.models.lng.schedule.VesselEventVisit;
import com.mmxlabs.models.mmxcore.MMXCorePackage;
import com.mmxlabs.shiplingo.platform.reports.IScenarioInstanceElementCollector;
import com.mmxlabs.shiplingo.platform.reports.ScheduledEventCollector;

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

		addScheduleColumn("Schedule", containingScheduleFormatter);
		addColumn("Vessel", objectFormatter, MMXCorePackage.eINSTANCE.getMMXObject__EContainerOp(), SchedulePackage.eINSTANCE.getSequence__GetName());
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
	protected ITreeContentProvider getContentProvider() {
		final ITreeContentProvider superProvider = super.getContentProvider();
		return new ITreeContentProvider() {

			@Override
			public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
				superProvider.inputChanged(viewer, oldInput, newInput);
			}

			@Override
			public void dispose() {
				superProvider.dispose();
			}

			@Override
			public boolean hasChildren(Object element) {
				return superProvider.hasChildren(element);
			}

			@Override
			public Object getParent(Object element) {
				return superProvider.getParent(element);
			}

			@Override
			public Object[] getElements(Object inputElement) {
				clearInputEquivalents();
				return superProvider.getElements(inputElement);
			}

			@Override
			public Object[] getChildren(Object parentElement) {
				return superProvider.getChildren(parentElement);
			}
		};
	}

	@Override
	protected IScenarioInstanceElementCollector getElementCollector() {
		return new ScheduledEventCollector() {
			@Override
			protected boolean filter(Event event) {
				return event instanceof Cooldown;
			}

			@Override
			protected boolean filter() {
				return true;
			}
		};
	}

	@Override
	protected boolean handleSelections() {
		return true;
	}
}
