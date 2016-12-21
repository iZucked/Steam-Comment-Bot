/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
package com.mmxlabs.lingo.reports.views.standard;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.ETypedElement;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.Viewer;

import com.mmxlabs.lingo.reports.IReportContents;
import com.mmxlabs.lingo.reports.IScenarioInstanceElementCollector;
import com.mmxlabs.lingo.reports.ScheduledEventCollector;
import com.mmxlabs.lingo.reports.components.ColumnBlock;
import com.mmxlabs.lingo.reports.components.ColumnHandler;
import com.mmxlabs.lingo.reports.components.ColumnType;
import com.mmxlabs.lingo.reports.components.EMFReportView;
import com.mmxlabs.lingo.reports.views.formatters.Formatters;
import com.mmxlabs.lingo.reports.views.formatters.IntegerFormatter;
import com.mmxlabs.models.lng.scenario.model.LNGScenarioModel;
import com.mmxlabs.models.lng.schedule.Cooldown;
import com.mmxlabs.models.lng.schedule.Event;
import com.mmxlabs.models.lng.schedule.Schedule;
import com.mmxlabs.models.lng.schedule.SchedulePackage;
import com.mmxlabs.models.lng.schedule.Sequence;
import com.mmxlabs.models.lng.schedule.SlotVisit;
import com.mmxlabs.models.lng.schedule.StartEvent;
import com.mmxlabs.models.lng.schedule.VesselEventVisit;
import com.mmxlabs.models.mmxcore.MMXCorePackage;
import com.mmxlabs.models.ui.tabular.BaseFormatter;
import com.mmxlabs.models.ui.tabular.ICellRenderer;
import com.mmxlabs.rcp.common.actions.CopyGridToHtmlStringUtil;
import com.mmxlabs.scenario.service.model.ScenarioInstance;
import com.mmxlabs.scenario.service.ui.ScenarioResult;

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
		super("com.mmxlabs.lingo.doc.Reports_Cooldown");

		addColumn("schedule", "Schedule", ColumnType.MULTIPLE, containingScheduleFormatter);
		addColumn("vessel", "Vessel", ColumnType.NORMAL, Formatters.objectFormatter, MMXCorePackage.eINSTANCE.getMMXObject__EContainerOp(), SchedulePackage.eINSTANCE.getSequence__GetName());
		addColumn("causeid", "Cause ID", ColumnType.NORMAL, new BaseFormatter() {
			@Override
			public String render(final Object object) {
				if (object instanceof Cooldown) {
					final Cooldown idle = (Cooldown) object;
					final Sequence sequence = (Sequence) idle.eContainer();
					int index = sequence.getEvents().indexOf(idle) - 1;

					while (index >= 0) {
						final Event before = sequence.getEvents().get(index);

						if ((before instanceof SlotVisit) || (before instanceof VesselEventVisit) || (before instanceof StartEvent)) {
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
			public String render(final Object object) {
				if (object instanceof Cooldown) {
					final Cooldown idle = (Cooldown) object;
					final Sequence sequence = (Sequence) idle.eContainer();
					final int index = sequence.getEvents().indexOf(idle) + 1;
					final Event after = sequence.getEvents().get(index);

					return after.name();
				}
				return "";
			}
		});

		addColumn("date", "Date", ColumnType.NORMAL, Formatters.asLocalDateFormatter, SchedulePackage.eINSTANCE.getEvent_Start());
		addColumn("time", "Time", ColumnType.NORMAL, Formatters.asLocalTimeFormatter, SchedulePackage.eINSTANCE.getEvent_Start());
		addColumn("port", "Port", ColumnType.NORMAL, Formatters.objectFormatter, SchedulePackage.eINSTANCE.getEvent_Port(), MMXCorePackage.eINSTANCE.getNamedObject_Name());
		addColumn("cost", "Cost", ColumnType.NORMAL, new IntegerFormatter() {
			@Override
			public Integer getIntValue(final Object object) {
				if (object instanceof Cooldown) {
					return ((Cooldown) object).getCost();
				}
				return null;
			}
		});

		getBlockManager().makeAllBlocksVisible();

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
			public void beginCollecting(final boolean pinDiffMode) {
				clearPinModeData();
				super.beginCollecting(pinDiffMode);
			}

			@Override
			protected Collection<? extends Object> collectElements(final ScenarioResult scenarioResult, final LNGScenarioModel scenarioModel, final Schedule schedule, final boolean pinned) {

				final Collection<? extends Object> collectedElements = super.collectElements(scenarioResult, scenarioModel, schedule, pinned);
				final List<EObject> elements = new ArrayList<>(collectedElements.size());
				for (final Object o : collectedElements) {
					if (o instanceof EObject) {
						elements.add((EObject) o);
					}
				}
				collectPinModeElements(elements, pinned);
				return collectedElements;
			}

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

	public ColumnHandler addColumn(final String blockID, final String title, final ColumnType columnType, final ICellRenderer formatter, final ETypedElement... path) {
		final ColumnBlock block = getBlockManager().createBlock(blockID, title, columnType);
		return getBlockManager().createColumn(block, title, formatter, path);
	}

	@Override
	public <T> T getAdapter(final Class<T> adapter) {

		if (IReportContents.class.isAssignableFrom(adapter)) {

			final CopyGridToHtmlStringUtil util = new CopyGridToHtmlStringUtil(viewer.getGrid(), false, true);
			final String contents = util.convert();
			return (T) new IReportContents() {

				@Override
				public String getStringContents() {
					return contents;
				}
			};

		}
		return super.getAdapter(adapter);
	}
}
