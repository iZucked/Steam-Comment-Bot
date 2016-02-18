/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
package com.mmxlabs.lingo.reports.views.standard;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
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
import com.mmxlabs.lingo.reports.views.formatters.BaseFormatter;
import com.mmxlabs.lingo.reports.views.formatters.Formatters;
import com.mmxlabs.models.lng.schedule.Event;
import com.mmxlabs.models.lng.schedule.PortVisit;
import com.mmxlabs.models.lng.schedule.Schedule;
import com.mmxlabs.models.lng.schedule.SchedulePackage;
import com.mmxlabs.models.lng.schedule.SlotVisit;
import com.mmxlabs.models.lng.schedule.VesselEventVisit;
import com.mmxlabs.models.lng.schedule.util.LatenessUtils;
import com.mmxlabs.models.ui.tabular.ICellRenderer;
import com.mmxlabs.rcp.common.actions.CopyGridToHtmlStringUtil;
import com.mmxlabs.scenario.service.model.ScenarioInstance;

/**
 * @author hinton
 * 
 */
public class LatenessReportView extends EMFReportView {
	public static final String ID = "com.mmxlabs.shiplingo.platform.reports.views.LatenessReportView";

	public LatenessReportView() {
		super("com.mmxlabs.lingo.doc.Reports_Lateness");
		addColumn("schedule", "Schedule", ColumnType.MULTIPLE, containingScheduleFormatter);

		final SchedulePackage sp = SchedulePackage.eINSTANCE;

		addColumn("id", "ID", ColumnType.NORMAL, Formatters.objectFormatter, sp.getEvent__Name());

		addColumn("type", "Type", ColumnType.NORMAL, Formatters.objectFormatter, sp.getEvent__Type());
		addColumn("lateness", "Lateness", ColumnType.NORMAL, new BaseFormatter() {
			@Override
			public String render(final Object object) {

				if (object instanceof PortVisit) {
					final PortVisit slotVisit = (PortVisit) object;
					return LatenessUtils.formatLatenessHours(LatenessUtils.getLatenessInHours(slotVisit));
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
		addColumn("flex", "Within Flex", ColumnType.NORMAL, new BaseFormatter() {
			@Override
			public String render(final Object object) {

				if (object instanceof PortVisit) {
					final PortVisit slotVisit = (PortVisit) object;
					if (LatenessUtils.isLateAfterFlex(slotVisit) != LatenessUtils.isLateExcludingFlex(slotVisit)) {
						return "Flex";
					}
				}

				return "";
			}

			@Override
			public Comparable<?> getComparable(final Object object) {

				if (object instanceof PortVisit) {
					final PortVisit slotVisit = (PortVisit) object;
					if (LatenessUtils.isLateAfterFlex(slotVisit) != LatenessUtils.isLateExcludingFlex(slotVisit)) {
						return "Flex";
					}
				}

				return "";
			}
		});

		addColumn("startby", "Start by", ColumnType.NORMAL, new BaseFormatter() {
			@Override
			public String render(final Object object) {
				return Formatters.asDateTimeFormatterNoTz.render(LatenessUtils.getWindowEndDate(object));
			}
		});
		addColumn("scheduledtime", "Scheduled time", ColumnType.NORMAL, Formatters.asDateTimeFormatterNoTz, sp.getEvent_Start());

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
				for (final Object e : result) {
					if (e instanceof SlotVisit) {
						final SlotVisit visit = (SlotVisit) e;
						setInputEquivalents(visit, Collections.singleton((Object) visit.getSlotAllocation().getCargoAllocation()));
					} else if (e instanceof VesselEventVisit) {
						final VesselEventVisit visit = (VesselEventVisit) e;
						setInputEquivalents(visit, Collections.singleton((Object) visit.getVesselEvent()));
					} else if (e instanceof PortVisit) {
						final PortVisit visit = (PortVisit) e;
					}
				}

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
			protected Collection<? extends Object> collectElements(final ScenarioInstance scenarioInstance, final Schedule schedule, final boolean pinned) {

				final Collection<? extends Object> collectedElements = super.collectElements(scenarioInstance, schedule, pinned);
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
			protected boolean filter(final Event e) {
				return LatenessUtils.isLateExcludingFlex(e);
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
