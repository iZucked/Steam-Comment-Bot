/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
package com.mmxlabs.lingo.reports.views.standard;

import java.time.YearMonth;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.eclipse.e4.ui.model.application.ui.basic.MPart;
import org.eclipse.e4.ui.workbench.modeling.ESelectionService;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.edit.domain.EditingDomain;
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
import com.mmxlabs.lingo.reports.services.SelectedScenariosService;
import com.mmxlabs.models.lng.cargo.Cargo;
import com.mmxlabs.models.lng.cargo.Slot;
import com.mmxlabs.models.lng.commercial.parseutils.Exposures;
import com.mmxlabs.models.lng.commercial.parseutils.Exposures.ValueMode;
import com.mmxlabs.models.lng.pricing.CommodityIndex;
import com.mmxlabs.models.lng.pricing.PricingModel;
import com.mmxlabs.models.lng.scenario.model.LNGScenarioModel;
import com.mmxlabs.models.lng.scenario.model.util.ScenarioModelUtil;
import com.mmxlabs.models.lng.schedule.CargoAllocation;
import com.mmxlabs.models.lng.schedule.Schedule;
import com.mmxlabs.models.lng.schedule.SlotAllocation;
import com.mmxlabs.rcp.common.SelectionHelper;
import com.mmxlabs.rcp.common.actions.CopyGridToHtmlStringUtil;
import com.mmxlabs.scenario.service.model.ModelReference;
import com.mmxlabs.scenario.service.model.ScenarioInstance;
import com.mmxlabs.scenario.service.ui.ScenarioResult;

/**
 */

public class ExposureReportView extends SimpleTabularReportView<IndexExposureData> implements org.eclipse.e4.ui.workbench.modeling.ISelectionListener {

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

	private final Pair<YearMonth, YearMonth> dateRange = new Pair<>();

	private Exposures.ValueMode mode = ValueMode.VOLUME_MMBTU;
	private boolean selectionMode = false;

	private ISelection selection;

	public ExposureReportView() {
		super("com.mmxlabs.lingo.doc.Reports_IndexExposures");
	}

	@Override
	protected AbstractSimpleTabularReportContentProvider<IndexExposureData> createContentProvider() {
		return new AbstractSimpleTabularReportContentProvider<IndexExposureData>() {

			@Override
			public void inputChanged(final Viewer viewer, final Object oldInput, final Object newInput) {

			}
		};
	}

	@Override
	protected AbstractSimpleTabularReportTransformer<IndexExposureData> createTransformer() {
		return new AbstractSimpleTabularReportTransformer<IndexExposureData>() {

			@Override
			public List<ColumnManager<IndexExposureData>> getColumnManagers(@NonNull final ISelectedDataProvider selectedDataProvider) {
				final ArrayList<ColumnManager<IndexExposureData>> result = new ArrayList<ColumnManager<IndexExposureData>>();

				if (selectedDataProvider.getScenarioResults().size() > 1) {
					result.add(new ColumnManager<IndexExposureData>("Scenario") {

						@Override
						public String getColumnText(final IndexExposureData data) {
							final ScenarioResult scenarioResult = data.scenarioResult;
							final ScenarioInstance scenarioInstance = scenarioResult.getScenarioInstance();
							if (scenarioInstance != null) {
								return scenarioInstance.getName();
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

				result.add(new ColumnManager<IndexExposureData>("Index") {
					@Override
					public String getColumnText(final IndexExposureData data) {
						return data.indexName;
					}

					@Override
					public int compare(final IndexExposureData o1, final IndexExposureData o2) {
						return o1.indexName.compareTo(o2.indexName);
					}
				});

				result.add(new ColumnManager<IndexExposureData>("Units") {
					@Override
					public String getColumnText(final IndexExposureData data) {

						if (mode == ValueMode.VOLUME_MMBTU) {
							return "mmBtu";
						} else if (mode == ValueMode.NATIVE_VALUE) {
							return data.currencyUnit;

						} else if (mode == ValueMode.VOLUME_NATIVE) {
							return data.volumeUnit;
						}
						return "";
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
				});

				if (dateRange != null && dateRange.getFirst() != null) {
					YearMonth date = dateRange.getFirst();
					while (!date.isAfter(dateRange.getSecond())) {
						final YearMonth fDate = date;
						result.add(new ColumnManager<IndexExposureData>(String.format("%04d-%02d", date.getYear(), date.getMonthValue())) {
							@Override
							public String getColumnText(final IndexExposureData data) {
								if (data.exposures.containsKey(fDate)) {
									final double result = data.exposures.get(fDate);
									return String.format("%,.01f", result);

								}
								return "";
							}

							@Override
							public int compare(final IndexExposureData o1, final IndexExposureData o2) {
								final double result1 = o1.exposures.containsKey(fDate) ? o1.exposures.get(fDate) : 0;
								final double result2 = o2.exposures.containsKey(fDate) ? o2.exposures.get(fDate) : 0;
								return Double.compare(result1, result2);
							}
						});

						date = date.plusMonths(1);
					}
				}
				return result;
			}

			@Override
			public @NonNull List<@NonNull IndexExposureData> createData(@Nullable final Pair<@NonNull Schedule, @NonNull ScenarioResult> pinnedPair,
					@NonNull final List<@NonNull Pair<@NonNull Schedule, @NonNull ScenarioResult>> otherPairs) {
				dateRange.setBoth(null, null);

				final List<@NonNull IndexExposureData> output = new LinkedList<>();

				if (pinnedPair != null && otherPairs.size() == 1) {
					// Pin/Diff mode
					final List<IndexExposureData> ref = createData(pinnedPair.getFirst(), pinnedPair.getSecond());

					final Pair<@NonNull Schedule, @NonNull ScenarioResult> p = otherPairs.get(0);
					final List<IndexExposureData> other = createData(p.getFirst(), p.getSecond());

					LOOP_REF_DATA: for (final IndexExposureData refData : ref) {
						final Iterator<IndexExposureData> otherIterator = other.iterator();
						while (otherIterator.hasNext()) {
							final IndexExposureData otherData = otherIterator.next();
							if (Equality.isEqual(refData.indexName, otherData.indexName)) {

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
					for (final YearMonth ym : d.exposures.keySet()) {
						if (dateRange.getFirst() == null || ym.isBefore(dateRange.getFirst())) {
							dateRange.setFirst(ym);
						}
						if (dateRange.getSecond() == null || ym.isAfter(dateRange.getSecond())) {
							dateRange.setSecond(ym);
						}
					}
				}

				return output;
			}

			public List<@NonNull IndexExposureData> createData(final @NonNull Schedule schedule, final @NonNull ScenarioResult scenarioResult) {
				final List<@NonNull IndexExposureData> output = new LinkedList<>();

				LNGScenarioModel rootObject = scenarioResult.getTypedRoot(LNGScenarioModel.class);
				if (rootObject == null) {
					return output;
				}
				final PricingModel pm = ScenarioModelUtil.getPricingModel(rootObject);
				final EList<CommodityIndex> indices = pm.getCommodityIndices();

				List<Object> selected = (!selectionMode || selection == null) ? Collections.emptyList() : SelectionHelper.convertToList(selection, Object.class);
				selected = selected.stream().filter(s -> s instanceof Slot || s instanceof SlotAllocation || s instanceof Cargo || s instanceof CargoAllocation).collect(Collectors.toList());

				for (final CommodityIndex index : indices) {
					assert index != null;
					final Map<YearMonth, Double> exposures = Exposures.getExposuresByMonth(schedule, index, ScenarioModelUtil.getPricingModel(rootObject), mode, selected);
					if (exposures.size() != 0.0) {
						final String currencyUnit = index.getCurrencyUnit();
						final String volumeUnit = index.getVolumeUnit();
						output.add(new IndexExposureData(scenarioResult, schedule, index.getName(), index, exposures, currencyUnit, volumeUnit));
					}
				}
				return output;
			}

		};
	}

	protected @NonNull IndexExposureData createDiffData(final IndexExposureData pinData, final IndexExposureData otherData) {

		final IndexExposureData modelData = pinData != null ? pinData : otherData;
		final Map<YearMonth, Double> exposuresByMonth = new HashMap<>();
		final IndexExposureData newData = new IndexExposureData(null, null, modelData.indexName, null, exposuresByMonth, modelData.currencyUnit, modelData.volumeUnit);

		if (pinData != null) {
			for (final Map.Entry<YearMonth, Double> e : pinData.exposures.entrySet()) {
				final double val = exposuresByMonth.getOrDefault(e.getKey(), 0.0);
				exposuresByMonth.put(e.getKey(), val + e.getValue());
			}
		}
		if (otherData != null) {
			for (final Map.Entry<YearMonth, Double> e : otherData.exposures.entrySet()) {
				final double val = exposuresByMonth.getOrDefault(e.getKey(), 0.0);
				exposuresByMonth.put(e.getKey(), val - e.getValue());
			}
		}

		return newData;
	}

	protected void makeActions() {
		super.makeActions();

		final Action calculateExposures = new Action("Calculate") {
			public void run() {

				final SelectedScenariosService service = getViewSite().getService(SelectedScenariosService.class);
				if (service == null) {
					return;
				}
				@Nullable
				final ISelectedDataProvider currentSelectedDataProvider = service.getCurrentSelectedDataProvider();

				if (currentSelectedDataProvider == null) {
					return;
				}

				@NonNull
				final Collection<@NonNull ScenarioResult> scenarioResults = currentSelectedDataProvider.getScenarioResults();
				for (final ScenarioResult scenarioResult : scenarioResults) {
					final ScenarioInstance instance = scenarioResult.getScenarioInstance();

					try (ModelReference ref = instance.getReference("ExposuresReportView")) {

						final LNGScenarioModel scenarioModel = (LNGScenarioModel) ref.getInstance();
						final EditingDomain domain = (EditingDomain) instance.getAdapters().get(EditingDomain.class);
						final Schedule schedule = ScenarioModelUtil.getScheduleModel(scenarioModel).getSchedule();
						Exposures.calculateExposures(scenarioModel, schedule, domain);
					}
				}
				// refresh();
			}
		};

		getViewSite().getActionBars().getToolBarManager().add(calculateExposures);

		final Action modeToggle = new Action("Units: currency", Action.AS_PUSH_BUTTON) {
			public void run() {

				final int modeIdx = (mode.ordinal() + 1) % Exposures.ValueMode.values().length;
				mode = Exposures.ValueMode.values()[modeIdx];
				setUnitsActionText(this);
				getViewSite().getActionBars().updateActionBars();
				ExposureReportView.this.refresh();

			}
		};
		setUnitsActionText(modeToggle);

		getViewSite().getActionBars().getToolBarManager().add(modeToggle);
		final Action selectionToggle = new Action("View: " + (selectionMode ? "Selection" : "All"), Action.AS_CHECK_BOX) {
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