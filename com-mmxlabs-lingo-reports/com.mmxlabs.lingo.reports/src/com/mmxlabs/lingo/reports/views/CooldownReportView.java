/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2014
 * All rights reserved.
 */
package com.mmxlabs.lingo.reports.views;

import org.eclipse.emf.ecore.ETypedElement;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.Viewer;

import com.mmxlabs.lingo.reports.IScenarioInstanceElementCollector;
import com.mmxlabs.lingo.reports.ScheduledEventCollector;
import com.mmxlabs.lingo.reports.components.ColumnBlock;
import com.mmxlabs.lingo.reports.components.ColumnHandler;
import com.mmxlabs.lingo.reports.components.ColumnType;
import com.mmxlabs.lingo.reports.components.EMFReportView;
import com.mmxlabs.lingo.reports.views.formatters.BaseFormatter;
import com.mmxlabs.lingo.reports.views.formatters.IFormatter;
import com.mmxlabs.lingo.reports.views.formatters.IntegerFormatter;
import com.mmxlabs.models.lng.schedule.Cooldown;
import com.mmxlabs.models.lng.schedule.Event;
import com.mmxlabs.models.lng.schedule.Idle;
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

		addColumn("schedule", "Schedule", ColumnType.MULTIPLE, containingScheduleFormatter);
		addColumn("vessel", "Vessel", ColumnType.NORMAL, objectFormatter, MMXCorePackage.eINSTANCE.getMMXObject__EContainerOp(), SchedulePackage.eINSTANCE.getSequence__GetName());
		addColumn("causeid", "Cause ID", ColumnType.NORMAL, new BaseFormatter() {
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

		addColumn("id", "ID", ColumnType.NORMAL, new BaseFormatter() {
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

		addColumn("date", "Date", ColumnType.NORMAL, datePartFormatter, SchedulePackage.eINSTANCE.getEvent__GetLocalStart());
		addColumn("time", "Time", ColumnType.NORMAL, timePartFormatter, SchedulePackage.eINSTANCE.getEvent__GetLocalStart());
		addColumn("port", "Port", ColumnType.NORMAL, objectFormatter, SchedulePackage.eINSTANCE.getEvent_Port(), MMXCorePackage.eINSTANCE.getNamedObject_Name());
		addColumn("volume", "Volume", ColumnType.NORMAL, new IntegerFormatter() {
			@Override
			public Integer getIntValue(final Object object) {
				if (object instanceof Cooldown) {
					return ((Cooldown) object).getVolume();
				}
				return null;
			}
		});

		addColumn("cost", "Cost", ColumnType.NORMAL, new IntegerFormatter() {
			@Override
			public Integer getIntValue(final Object object) {
				if (object instanceof Cooldown) {
					return ((Cooldown) object).getCost();
				}
				return null;
			}
		});
		
		makeAllBlocksVisible();

	}

	@Override
	protected ITreeContentProvider getContentProvider() {
		final ITreeContentProvider superProvider = super.getContentProvider();
		return new ITreeContentProvider() {

			@Override
			public void inputChanged(final Viewer viewer, final Object oldInput, final Object newInput) {
				superProvider.inputChanged(viewer, oldInput, newInput);
			}

			@Override
			public void dispose() {
				superProvider.dispose();
			}

			@Override
			public boolean hasChildren(final Object element) {
				return superProvider.hasChildren(element);
			}

			@Override
			public Object getParent(final Object element) {
				return superProvider.getParent(element);
			}

			@Override
			public Object[] getElements(final Object inputElement) {
				clearInputEquivalents();
				return superProvider.getElements(inputElement);
			}

			@Override
			public Object[] getChildren(final Object parentElement) {
				return superProvider.getChildren(parentElement);
			}
		};
	}

	@Override
	protected IScenarioInstanceElementCollector getElementCollector() {
		return new ScheduledEventCollector() {
			@Override
			protected boolean filter(final Event event) {
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

	public ColumnHandler addColumn(final String blockID, final String title, final ColumnType columnType, final IFormatter formatter, final ETypedElement... path) {
		final ColumnBlock block = createBlock(blockID, title, columnType);
		return createColumn(block, title, formatter, path);
	}
}
