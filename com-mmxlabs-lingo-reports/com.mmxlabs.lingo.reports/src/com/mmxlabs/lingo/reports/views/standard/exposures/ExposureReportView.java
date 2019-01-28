/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2018
 * All rights reserved.
 */
package com.mmxlabs.lingo.reports.views.standard.exposures;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import org.eclipse.e4.ui.model.application.ui.basic.MPart;
import org.eclipse.e4.ui.workbench.modeling.ESelectionService;
import org.eclipse.emf.common.util.EList;
import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.Nullable;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.views.properties.PropertySheet;

import com.mmxlabs.common.Equality;
import com.mmxlabs.common.Pair;
import com.mmxlabs.lingo.reports.IReportContents;
import com.mmxlabs.lingo.reports.components.AbstractSimpleTabularReportContentProvider;
import com.mmxlabs.lingo.reports.components.AbstractSimpleTabularReportTransformer;
import com.mmxlabs.lingo.reports.services.ISelectedDataProvider;
import com.mmxlabs.lingo.reports.views.standard.SimpleTabularReportView;
import com.mmxlabs.models.lng.cargo.Cargo;
import com.mmxlabs.models.lng.cargo.CargoModel;
import com.mmxlabs.models.lng.cargo.DischargeSlot;
import com.mmxlabs.models.lng.cargo.LoadSlot;
import com.mmxlabs.models.lng.cargo.PaperDeal;
import com.mmxlabs.models.lng.cargo.Slot;
import com.mmxlabs.models.lng.commercial.parseutils.Exposures;
import com.mmxlabs.models.lng.commercial.parseutils.Exposures.ValueMode;
import com.mmxlabs.models.lng.pricing.CommodityIndex;
import com.mmxlabs.models.lng.pricing.PricingModel;
import com.mmxlabs.models.lng.scenario.model.LNGScenarioModel;
import com.mmxlabs.models.lng.scenario.model.util.ScenarioModelUtil;
import com.mmxlabs.models.lng.schedule.CargoAllocation;
import com.mmxlabs.models.lng.schedule.ExposureDetail;
import com.mmxlabs.models.lng.schedule.Schedule;
import com.mmxlabs.models.lng.schedule.ScheduleModel;
import com.mmxlabs.models.lng.schedule.SlotAllocation;
import com.mmxlabs.rcp.common.SelectionHelper;
import com.mmxlabs.rcp.common.actions.CopyGridToHtmlStringUtil;
import com.mmxlabs.scenario.service.model.manager.IScenarioDataProvider;
import com.mmxlabs.scenario.service.model.manager.ScenarioModelRecord;
import com.mmxlabs.scenario.service.ui.ScenarioResult;

/**
 */

public class ExposureReportView extends SimpleTabularReportView<IndexExposureData> implements org.eclipse.e4.ui.workbench.modeling.ISelectionListener {

	// -------------------------------------Transformer class starts-------------------------------------
	private final class ExposureData extends AbstractSimpleTabularReportTransformer<IndexExposureData> {
		@Override
		public List<ColumnManager<IndexExposureData>> getColumnManagers(@NonNull final ISelectedDataProvider selectedDataProvider) {
			final ArrayList<ColumnManager<IndexExposureData>> result = new ArrayList<ColumnManager<IndexExposureData>>();

			if (selectedDataProvider.getScenarioResults().size() > 1 && selectedDataProvider.getPinnedScenarioResult() == null) {
				result.add(new ColumnManager<IndexExposureData>("Scenario") {

					@Override
					public String getColumnText(final IndexExposureData data) {
						final ScenarioResult scenarioResult = data.scenarioResult;
						if (scenarioResult != null) {
							final ScenarioModelRecord modelRecord = scenarioResult.getModelRecord();
							if (modelRecord != null) {
								return modelRecord.getName();
							}
						}
						return null;
					}

					@Override
					public int compare(final IndexExposureData o1, final IndexExposureData o2) {
						final String s1 = getColumnText(o1);
						final String s2 = getColumnText(o2);
						if (s1 == null) {
							return -1;
						}
						if (s2 == null) {
							return 1;
						}
						return s1.compareTo(s2);
					}

					@Override
					public boolean isTree() {
						return false;
					}
				});
			}

			result.add(new ColumnManager<IndexExposureData>("Date") {
				@Override
				public String getColumnText(final IndexExposureData data) {
					if (data.isChild) {
						return data.childName;
					}
					return String.format("%04d-%02d", data.date.getYear(), data.date.getMonthValue());
				}

				@Override
				public int compare(final IndexExposureData o1, final IndexExposureData o2) {
					if (o1.isChild && o2.isChild) {
						return o1.childName.compareTo(o2.childName);
					} else if (!o1.isChild && !o2.isChild) {
						return o1.date.compareTo(o2.date);
					}
					return o1.hashCode() - o2.hashCode();
				}

				@Override
				public boolean isTree() {
					return true;
				}
			});

			for (final ScenarioResult sr : selectedDataProvider.getScenarioResults()) {
				final IScenarioDataProvider sdp = sr.getScenarioDataProvider();
				final PricingModel pm = ScenarioModelUtil.getPricingModel(sdp);
				final EList<CommodityIndex> indices = pm.getCommodityIndices();

				for (final CommodityIndex index : indices) {
					String indexName = index.getName();
					result.add(new ColumnManager<IndexExposureData>(indexName) {
						@Override
						public String getColumnText(final IndexExposureData data) {
							if (Objects.equals(data.scenarioResult, sr) && data.exposures.containsKey(indexName)) {
								final double result = data.exposures.get(indexName);
								return String.format("%,.01f", result);
							}
							return "";
						}

						@Override
						public int compare(final IndexExposureData o1, final IndexExposureData o2) {
							final double result1 = o1.exposures.containsKey(indexName) ? o1.exposures.get(indexName) : 0;
							final double result2 = o2.exposures.containsKey(indexName) ? o2.exposures.get(indexName) : 0;
							return Double.compare(result1, result2);
						}
					});
				}
				String indexName = "Physical";
				result.add(new ColumnManager<IndexExposureData>(indexName) {
					@Override
					public String getColumnText(final IndexExposureData data) {
						if (Objects.equals(data.scenarioResult, sr) && data.exposures.containsKey(indexName)) {
							final double result = data.exposures.get(indexName);
							return String.format("%,.01f", result);
						}
						return "";
					}

					@Override
					public int compare(final IndexExposureData o1, final IndexExposureData o2) {
						final double result1 = o1.exposures.containsKey(indexName) ? o1.exposures.get(indexName) : 0;
						final double result2 = o2.exposures.containsKey(indexName) ? o2.exposures.get(indexName) : 0;
						return Double.compare(result1, result2);
					}
				});

			}

			return result;
		}

		@Override
		public @NonNull List<@NonNull IndexExposureData> createData(@Nullable final Pair<@NonNull Schedule, @NonNull ScenarioResult> pinnedPair,
				@NonNull final List<@NonNull Pair<@NonNull Schedule, @NonNull ScenarioResult>> otherPairs) {
			dateRange.setBoth(null, null);

			final List<IndexExposureData> output = new LinkedList<>();

			if (pinnedPair != null && otherPairs.size() == 1) {
				// Pin/Diff mode
				final List<IndexExposureData> ref = createData(pinnedPair.getFirst(), pinnedPair.getSecond());

				final Pair<@NonNull Schedule, @NonNull ScenarioResult> p = otherPairs.get(0);
				final List<IndexExposureData> other = createData(p.getFirst(), p.getSecond());

				LOOP_REF_DATA: for (final IndexExposureData refData : ref) {
					final Iterator<IndexExposureData> otherIterator = other.iterator();
					while (otherIterator.hasNext()) {
						final IndexExposureData otherData = otherIterator.next();
						if (Equality.isEqual(refData.date, otherData.date)) { // indexName

							output.add(createDiffData(refData, otherData));
							otherIterator.remove();
							continue LOOP_REF_DATA;
						}
					}
					output.add(createDiffData(refData, null));

				}
				for (final IndexExposureData otherData : other) {
					output.add(createDiffData(null, otherData));

				}
			} else {
				if (pinnedPair != null) {
					output.addAll(createData(pinnedPair.getFirst(), pinnedPair.getSecond()));
				}
				for (final Pair<@NonNull Schedule, @NonNull ScenarioResult> p : otherPairs) {
					output.addAll(createData(p.getFirst(), p.getSecond()));
				}
			}
			for (final IndexExposureData d : output) {
				final YearMonth ym = d.date;
				if (dateRange.getFirst() == null || ym.isBefore(dateRange.getFirst())) {
					dateRange.setFirst(ym);
				}
				if (dateRange.getSecond() == null || ym.isAfter(dateRange.getSecond())) {
					dateRange.setSecond(ym);
				}
			}

			return output;
		}

		public List<IndexExposureData> createData(final @NonNull Schedule schedule, final @NonNull ScenarioResult scenarioResult) {
			final List<IndexExposureData> output = new LinkedList<>();

			final YearMonth ymStart = getEarliestExposureDate(scenarioResult.getScenarioDataProvider());
			final YearMonth ymEnd = getLatestExposureDate(scenarioResult.getScenarioDataProvider());

			final LNGScenarioModel rootObject = scenarioResult.getTypedRoot(LNGScenarioModel.class);
			if (rootObject == null) {
				return output;
			}

			List<Object> selected = (!selectionMode || selection == null) ? Collections.emptyList() : SelectionHelper.convertToList(selection, Object.class);
			selected = selected.stream().filter(s -> s instanceof Slot || s instanceof SlotAllocation || s instanceof Cargo || s instanceof CargoAllocation || s instanceof PaperDeal)
					.collect(Collectors.toList());

			for (YearMonth cym = ymStart; cym.isBefore(ymEnd); cym = cym.plusMonths(1)) {
				IndexExposureData exposuresByMonth = ExposuresTransformer.getExposuresByMonth(scenarioResult, schedule, cym, mode, selected);
				if (exposuresByMonth.exposures.size() != 0.0) {
					output.add(exposuresByMonth);
				}
			}
			return output;
		}
	}
	// -------------------------------------Transformer class ends----------------------------------------

	@Override
	public void createPartControl(final Composite parent) {
		super.createPartControl(parent);
		// Get e4 selection service!
		final ESelectionService service = (ESelectionService) getSite().getService(ESelectionService.class);
		service.addPostSelectionListener(this);
	}

	@Override
	public void dispose() {
		final ESelectionService service = (ESelectionService) getSite().getService(ESelectionService.class);
		service.removePostSelectionListener(this);

		super.dispose();
	}

	protected final Pair<YearMonth, YearMonth> dateRange = new Pair<>();

	protected Exposures.ValueMode mode = ValueMode.VOLUME_MMBTU;
	protected boolean selectionMode = false;

	protected ISelection selection;

	public ExposureReportView() {
		super("com.mmxlabs.lingo.doc.Reports_IndexExposures");
	}

	@Override
	protected AbstractSimpleTabularReportContentProvider<IndexExposureData> createContentProvider() {
		return new AbstractSimpleTabularReportContentProvider<IndexExposureData>() {

			@Override
			public void inputChanged(final Viewer viewer, final Object oldInput, final Object newInput) {

			}

			@Override
			public Object[] getElements(final Object inputElement) {

				if (inputElement instanceof Collection<?>) {
					Collection<?> collection = (Collection<?>) inputElement;
					return collection.toArray();
				}
				return new Object[0];
			}

			@Override
			public void dispose() {

			}

			@Override
			public Object[] getChildren(Object parentElement) {
				if (parentElement instanceof Collection<?>) {
					Collection<?> collection = (Collection<?>) parentElement;
					return collection.toArray();
				}
				if (parentElement instanceof IndexExposureData) {
					IndexExposureData indexExposureData = (IndexExposureData) parentElement;
					if (indexExposureData.children != null) {
						return indexExposureData.children.toArray();
					}
				}
				return null;
			}

			@Override
			public Object getParent(Object element) {
				return null;
			}

			@Override
			public boolean hasChildren(Object element) {
				return element instanceof Collection<?> || (element instanceof IndexExposureData && ((IndexExposureData) element).children != null);
			}
		};
	}

	@Override
	protected AbstractSimpleTabularReportTransformer<IndexExposureData> createTransformer() {
		return new ExposureData();
	}

	protected @NonNull IndexExposureData createDiffData(final IndexExposureData pinData, final IndexExposureData otherData) {

		final IndexExposureData modelData = pinData != null ? pinData : otherData;
		final Map<String, Double> exposuresByMonth = new HashMap<>();

		Collection<YearMonth> keys = new LinkedHashSet<>();
		Map<YearMonth, IndexExposureData> pinChildren = new HashMap<>();
		Map<YearMonth, IndexExposureData> otherChildren = new HashMap<>();
		if (pinData != null) {
			for (final Map.Entry<String, Double> e : pinData.exposures.entrySet()) {
				final double val = exposuresByMonth.getOrDefault(e.getKey(), 0.0);
				exposuresByMonth.put(e.getKey(), val + e.getValue());
			}
			if (pinData.children != null) {
				for (IndexExposureData d : pinData.children) {
					keys.add(d.date);
					pinChildren.put(d.date, d);
				}
			}
		}
		if (otherData != null) {
			for (final Map.Entry<String, Double> e : otherData.exposures.entrySet()) {
				final double val = exposuresByMonth.getOrDefault(e.getKey(), 0.0);
				exposuresByMonth.put(e.getKey(), val - e.getValue());
			}
			if (otherData.children != null) {
				for (IndexExposureData d : otherData.children) {
					keys.add(d.date);
					otherChildren.put(d.date, d);
				}
			}
		}

		List<IndexExposureData> newChildren = null;
		if (!keys.isEmpty()) {
			newChildren = new ArrayList<>(keys.size());
			for (YearMonth key : keys) {
				IndexExposureData childDiffData = createDiffData(pinChildren.get(key), otherChildren.get(key));
				for (Double value : childDiffData.exposures.values()) {
					if (value.doubleValue() != 0.0) {
						newChildren.add(childDiffData);
						break;
					}
				}

			}
		}

		return new IndexExposureData(null, null, modelData.date, exposuresByMonth, newChildren);
	}

	@Override
	protected void makeActions() {
		super.makeActions();

		final Action modeToggle = new Action("Units: currency", Action.AS_PUSH_BUTTON) {
			@Override
			public void run() {

				final int modeIdx = (mode.ordinal() + 1) % Exposures.ValueMode.values().length;
				mode = Exposures.ValueMode.values()[modeIdx];
				setUnitsActionText(this);
				getViewSite().getActionBars().updateActionBars();
				// columnManagers.get(modeIdx)
				ExposureReportView.this.refresh();

			}
		};
		setUnitsActionText(modeToggle);

		getViewSite().getActionBars().getToolBarManager().add(modeToggle);
		final Action selectionToggle = new Action("View: " + (selectionMode ? "Selection" : "All"), Action.AS_CHECK_BOX) {
			@Override
			public void run() {

				selectionMode = !selectionMode;
				setText("View: " + (selectionMode ? "Selection" : "All"));
				getViewSite().getActionBars().updateActionBars();
				ExposureReportView.this.refresh();

			}
		};

		getViewSite().getActionBars().getToolBarManager().add(selectionToggle);

		getViewSite().getActionBars().getToolBarManager().update(true);
	}

	@Override
	public void selectionChanged(final MPart part, final Object selectionObject) {

		final IWorkbenchPart e3Part = SelectionHelper.getE3Part(part);
		if (e3Part != null) {
			if (e3Part == this) {
				return;
			}
			if (e3Part instanceof PropertySheet) {
				return;
			}
		}
		selection = SelectionHelper.adaptSelection(selectionObject);
		// viewer.setSelection(selection, true);
		ExposureReportView.this.refresh();
	}

	private void setUnitsActionText(final Action a) {
		String modeStr = null;
		switch (mode) {
		case NATIVE_VALUE:
			modeStr = "currency";
			break;
		case VOLUME_MMBTU:
			modeStr = "mmBtu";
			break;
		case VOLUME_NATIVE:
			modeStr = "native";
			break;
		default:
			assert false;
			break;

		}
		assert modeStr != null;
		a.setText("Units: " + modeStr);
	}

	@Override
	public <T> T getAdapter(final Class<T> adapter) {

		if (IReportContents.class.isAssignableFrom(adapter)) {

			final CopyGridToHtmlStringUtil util = new CopyGridToHtmlStringUtil(viewer.getGrid(), false, true);
			util.setRowHeadersIncluded(true);
			util.setShowBackgroundColours(true);
			final String contents = util.convert();
			return adapter.cast((IReportContents) () -> contents);
		}
		return super.getAdapter(adapter);
	}
	
	protected YearMonth getEarliestExposureDate(final IScenarioDataProvider sdp) {
		YearMonth result = YearMonth.now();
		
		final ScheduleModel sm = ScenarioModelUtil.getScheduleModel(sdp);
		final Schedule schedule = sm.getSchedule();
		if (schedule != null) {
			for(final CargoAllocation ca : schedule.getCargoAllocations()) {
				for(final SlotAllocation sa : ca.getSlotAllocations()) {
					for (final ExposureDetail detail : sa.getExposures()) {
						if (detail.getDate().isBefore(result)) {
							result = detail.getDate();
						}
					}
				}
			}
		}
		return result;		
	}
	
	protected YearMonth getLatestExposureDate(final IScenarioDataProvider sdp) {
		YearMonth result = YearMonth.now();
		
		final ScheduleModel sm = ScenarioModelUtil.getScheduleModel(sdp);
		final Schedule schedule = sm.getSchedule();
		if (schedule != null) {
			for(final CargoAllocation ca : schedule.getCargoAllocations()) {
				for(final SlotAllocation sa : ca.getSlotAllocations()) {
					for (final ExposureDetail detail : sa.getExposures()) {
						if (detail.getDate().isAfter(result)) {
							result = detail.getDate();
						}
					}
				}
			}
		}
		return result;		
	}
}