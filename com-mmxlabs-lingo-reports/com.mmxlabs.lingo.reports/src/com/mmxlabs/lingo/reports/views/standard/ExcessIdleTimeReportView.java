/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2022
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

import com.mmxlabs.lingo.reports.IReportContentsGenerator;
import com.mmxlabs.lingo.reports.IScenarioInstanceElementCollector;
import com.mmxlabs.lingo.reports.ReportContentsGenerators;
import com.mmxlabs.lingo.reports.ScheduledEventCollector;
import com.mmxlabs.lingo.reports.components.EMFReportView;
import com.mmxlabs.lingo.reports.views.formatters.Formatters;
import com.mmxlabs.models.lng.scenario.model.LNGScenarioModel;
import com.mmxlabs.models.lng.schedule.Event;
import com.mmxlabs.models.lng.schedule.Idle;
import com.mmxlabs.models.lng.schedule.PortVisit;
import com.mmxlabs.models.lng.schedule.Schedule;
import com.mmxlabs.models.lng.schedule.SchedulePackage;
import com.mmxlabs.models.lng.schedule.util.LatenessUtils;
import com.mmxlabs.models.ui.tabular.BaseFormatter;
import com.mmxlabs.models.ui.tabular.ICellRenderer;
import com.mmxlabs.models.ui.tabular.columngeneration.ColumnBlock;
import com.mmxlabs.models.ui.tabular.columngeneration.ColumnHandler;
import com.mmxlabs.models.ui.tabular.columngeneration.ColumnType;
import com.mmxlabs.scenario.service.ScenarioResult;

/**
 * @author hinton
 * 
 */
public class ExcessIdleTimeReportView extends EMFReportView {
	public static final String ID = "com.mmxlabs.shiplingo.platform.reports.views.ExcessIdleTimeReportView";

	public ExcessIdleTimeReportView() {
		super("com.mmxlabs.lingo.doc.Reports_Idle");
		addColumn("schedule", "Schedule", ColumnType.MULTIPLE, containingScheduleFormatter);

		final SchedulePackage sp = SchedulePackage.eINSTANCE;

		addColumn("id", "ID", ColumnType.NORMAL, Formatters.objectFormatter, sp.getEvent__Name());
		addColumn("idle", "Excess Idle Time", ColumnType.NORMAL, new BaseFormatter() {
			@Override
			public String render(final Object object) {

				if (object instanceof Idle) {
					final Idle journey = (Idle) object;
					return LatenessUtils.formatLatenessHours(journey.getDuration());
				}

				return "";
			}

			@Override
			public Comparable<?> getComparable(final Object object) {

				if (object instanceof PortVisit) {
					final long latenessInHours = LatenessUtils.getLatenessInHours((PortVisit) object);
					return Long.valueOf(latenessInHours);
				}

				return super.getComparable(object);
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
				final Object[] result = superProvider.getElements(inputElement);
				// for (final Object e : result) {
				// if (e instanceof SlotVisit) {
				// final SlotVisit visit = (SlotVisit) e;
				// setInputEquivalents(visit, Collections.singleton((Object) visit.getSlotAllocation().getCargoAllocation()));
				// } else if (e instanceof VesselEventVisit) {
				// final VesselEventVisit visit = (VesselEventVisit) e;
				// setInputEquivalents(visit, Collections.singleton((Object) visit.getVesselEvent()));
				// } else if (e instanceof PortVisit) {
				// final PortVisit visit = (PortVisit) e;
				// }
				// }

				return result;
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
			protected Collection<EObject> collectElements(final ScenarioResult scenarioResult, LNGScenarioModel scenarioModel, final Schedule schedule, final boolean pinned) {

				final Collection<EObject> collectedElements = super.collectElements(scenarioResult, scenarioModel, schedule, pinned);
				final List<EObject> elements = new ArrayList<>(collectedElements);
				collectPinModeElements(elements, pinned);
				return collectedElements;
			}

			@Override
			protected boolean filter(final Event e) {
				if (e instanceof Idle) {
					if (e.getDuration() > 13 * 24) {
						return true;
					}
				}
				return false;
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

		if (IReportContentsGenerator.class.isAssignableFrom(adapter)) {
			return adapter.cast(ReportContentsGenerators.createJSONFor(selectedScenariosServiceListener, viewer.getGrid()));
		}
		return super.getAdapter(adapter);
	}
}
