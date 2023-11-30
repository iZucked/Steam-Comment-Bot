/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
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
import org.eclipse.emf.common.util.EList;
import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.Nullable;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.GroupMarker;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.FontData;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.ui.IMemento;
import org.eclipse.ui.IViewSite;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.XMLMemento;

import com.mmxlabs.common.Pair;
import com.mmxlabs.common.exposures.ExposureEnumerations.AggregationMode;
import com.mmxlabs.common.exposures.ExposureEnumerations.ValueMode;
import com.mmxlabs.license.features.KnownFeatures;
import com.mmxlabs.license.features.LicenseFeatures;
import com.mmxlabs.lingo.reports.IReportContents;
import com.mmxlabs.lingo.reports.IReportContentsGenerator;
import com.mmxlabs.lingo.reports.ReportContents;
import com.mmxlabs.lingo.reports.components.AbstractSimpleTabularReportTransformer;
import com.mmxlabs.lingo.reports.components.ColumnManager;
import com.mmxlabs.lingo.reports.components.DatesToolbarEditor;
import com.mmxlabs.lingo.reports.components.SimpleTabularReportContentProvider;
import com.mmxlabs.lingo.reports.services.ISelectedDataProvider;
import com.mmxlabs.lingo.reports.services.SelectedDataProviderImpl;
import com.mmxlabs.lingo.reports.services.SelectionServiceUtils;
import com.mmxlabs.lingo.reports.views.standard.SimpleTabularReportView;
import com.mmxlabs.lingo.reports.views.standard.exposures.IndexExposureData.IndexExposureType;
import com.mmxlabs.models.lng.cargo.Cargo;
import com.mmxlabs.models.lng.cargo.DealSet;
import com.mmxlabs.models.lng.cargo.PaperDeal;
import com.mmxlabs.models.lng.cargo.Slot;
import com.mmxlabs.models.lng.cargo.ui.editorpart.actions.DefaultMenuCreatorAction;
import com.mmxlabs.models.lng.pricing.CommodityCurve;
import com.mmxlabs.models.lng.pricing.PricingModel;
import com.mmxlabs.models.lng.pricing.util.ModelMarketCurveProvider;
import com.mmxlabs.models.lng.scenario.model.LNGScenarioModel;
import com.mmxlabs.models.lng.scenario.model.util.ScenarioModelUtil;
import com.mmxlabs.models.lng.schedule.CargoAllocation;
import com.mmxlabs.models.lng.schedule.ExposureDetail;
import com.mmxlabs.models.lng.schedule.OpenSlotAllocation;
import com.mmxlabs.models.lng.schedule.PaperDealAllocation;
import com.mmxlabs.models.lng.schedule.Schedule;
import com.mmxlabs.models.lng.schedule.SlotAllocation;
import com.mmxlabs.rcp.common.SelectionHelper;
import com.mmxlabs.rcp.common.actions.CopyGridToHtmlStringUtil;
import com.mmxlabs.rcp.icons.lingo.CommonImages;
import com.mmxlabs.rcp.icons.lingo.CommonImages.IconMode;
import com.mmxlabs.rcp.icons.lingo.CommonImages.IconPaths;
import com.mmxlabs.scenario.service.ScenarioResult;
import com.mmxlabs.scenario.service.model.manager.IScenarioDataProvider;
import com.mmxlabs.scenario.service.model.manager.ScenarioModelRecord;

public class ExposureReportView extends SimpleTabularReportView<IndexExposureData> {

	protected Color colourBlue;
	protected Color colourLightBlue;
	protected Color colourGreen;
	protected Color colourOrange;
	protected Color colourViolet;
	protected Font fontBold;

	protected Image cellImageDarkArrowDown;
	protected Image cellImageDarkArrowUp;

	protected List<String> entities;
	protected List<String> fiscalYears;
	protected String selectedEntity;
	protected int selectedFiscalYear = -1;
	protected AssetType selectedAssetType = AssetType.NET;

	protected boolean showGenerated = false;
	protected boolean showPeriod = false;
	
	private DatesToolbarEditor dates;
	
	private IMemento memento;

	// -------------------------------------Transformer class
	// starts-------------------------------------
	protected class ExposureData implements AbstractSimpleTabularReportTransformer<IndexExposureData> {
		@Override
		public List<ColumnManager<IndexExposureData>> getColumnManagers(@NonNull final ISelectedDataProvider selectedDataProvider) {

			final ArrayList<ColumnManager<IndexExposureData>> result = new ArrayList<>();
			if (selectedDataProvider.getAllScenarioResults().isEmpty()) {
				return result;
			}
			createGenericColumns(selectedDataProvider, result);
			final List<String> columnNames = getGenericColumnNames(selectedDataProvider);

			customiseColumnList(columnNames);

			generateColumnsFromList(result, columnNames);

			return result;
		}

		protected void generateColumnsFromList(final ArrayList<ColumnManager<IndexExposureData>> result, final List<String> columnNames) {
			for (final String columnName : columnNames) {
				result.add(new ColumnManager<IndexExposureData>(columnName) {
					@Override
					public String getColumnText(final IndexExposureData data) {
						if (data == null)
							return "";
						if (data.exposures.isEmpty())
							return "";
						if (data.exposures.containsKey(columnName.toLowerCase())) {
							final double result;
							if (pinnedMode) {
								if (useImage(data)) {
									result = Math.abs(data.exposures.get(columnName.toLowerCase()));
								} else {
									result = data.exposures.get(columnName.toLowerCase());
								}
							} else {
								result = data.exposures.get(columnName.toLowerCase());
							}
							if (mode == ValueMode.VOLUME_TBTU) {
								return String.format("%,.03f", result);
							} else {
								return String.format("%,.0f", result);
							}
						}
						return "";
					}

					@Override
					public @Nullable Image getColumnImage(final IndexExposureData ied) {
						if (!useImage(ied))
							return null;
						if (!pinnedMode)
							return null;
						if (ied.exposures.containsKey(columnName)) {
							final double result = ied.exposures.get(columnName);
							if (result > 0.0) {
								return cellImageDarkArrowUp;
							} else if (result < 0.0) {
								return cellImageDarkArrowDown;
							}
						}
						return null;
					}

					@Override
					public @Nullable Color getBackground(final IndexExposureData element) {
						return colorByType(element, columnName);
					}

					@Override
					public @Nullable Font getFont(final IndexExposureData element) {
						return fontByType(element);
					}

					@Override
					public int compare(final IndexExposureData o1, final IndexExposureData o2) {
						final double result1 = o1.exposures.containsKey(columnName) ? o1.exposures.get(columnName) : 0;
						final double result2 = o2.exposures.containsKey(columnName) ? o2.exposures.get(columnName) : 0;
						return Double.compare(result1, result2);
					}
				});
			}
		}

		protected List<String> getGenericColumnNames(final ISelectedDataProvider selectedDataProvider) {
			final List<String> columnNames = new ArrayList<String>();
			{
				final ScenarioResult sr = selectedDataProvider.getAllScenarioResults().get(0);
				final IScenarioDataProvider sdp = sr.getScenarioDataProvider();
				final PricingModel pm = ScenarioModelUtil.getPricingModel(sdp);

				final EList<CommodityCurve> indices = pm.getCommodityCurves();
				for (final CommodityCurve index : indices) {
					if (index.getMarketIndex() == null) {
						continue;
					}
					String indexName = index.getMarketIndex().getName();
					if (!columnNames.contains(indexName)) {
						columnNames.add(indexName);
					}
				}
			}
			return columnNames;
		}

		protected boolean useImage(IndexExposureData ied) {
			return false;
		}

		protected @Nullable Color colorByType(final IndexExposureData ied, final String customColumnName) {
			if (IndexExposureType.ANNUAL.equals(ied.type)) {
				return colourBlue;
			} else if (IndexExposureType.QUARTERLY.equals(ied.type)) {
				return colourLightBlue;
			}
			return null;
		}

		protected Font fontByType(final IndexExposureData ied) {
			if (!IndexExposureType.MONTHLY.equals(ied.type)) {
				return fontBold;
			}
			return null;
		}

		protected void createGenericColumns(final ISelectedDataProvider selectedDataProvider, final ArrayList<ColumnManager<IndexExposureData>> result) {

			if (selectedDataProvider.getAllScenarioResults().size() > 1 && selectedDataProvider.getPinnedScenarioResult() == null) {
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

			pinnedMode = selectedDataProvider.getPinnedScenarioResult() != null;

			result.add(new ColumnManager<IndexExposureData>("Date") {
				@Override
				public String getColumnText(final IndexExposureData data) {
					if (data == null)
						return "";
					if (data.isChild) {
						return data.childName;
					}
					if (aggregationMode.equals(AggregationMode.BY_DEALSET)) {
						if (data.dealSet != null) {
							return data.dealSet;
						}
					}
					if (data.type != null) {
						if (data.type == IndexExposureType.ANNUAL) {
							return String.format("%04d Total", data.year);
						} else if (data.type == IndexExposureType.QUARTERLY) {
							return String.format("Q%1d Total", data.quarter);
						}
					}
					return String.format("%04d-%02d", data.date.getYear(), data.date.getMonthValue());
				}

				@Override
				public Color getForeground(final IndexExposureData element) {
					return colorByType(element, "");
				}

				@Override
				public Font getFont(final IndexExposureData element) {
					return fontByType(element);
				}

				@Override
				public int compare(final IndexExposureData o1, final IndexExposureData o2) {
					if (o1.dealSet != null && o2.dealSet != null) {
						return o1.dealSet.compareTo(o2.dealSet);
					}
					if (o1.isChild && o2.isChild) {
						return o1.childName.compareTo(o2.childName);
					} else if (!o1.isChild && !o2.isChild) {
						int ret = o1.date.compareTo(o2.date);
						if (ret == 0) {
							ret += o1.type.getValue() - o2.type.getValue();
						}
						return ret;
					}
					return o1.hashCode() - o2.hashCode();
				}

				@Override
				public boolean isTree() {
					return true;
				}
			});
		}

		@Override
		public List<IndexExposureData> createData(@Nullable final Pair<@NonNull Schedule, @NonNull ScenarioResult> pinnedPair,
				@NonNull final List<@NonNull Pair<@NonNull Schedule, @NonNull ScenarioResult>> otherPairs) {
			final List<IndexExposureData> output = new LinkedList<>();

			if (pinnedPair != null && otherPairs.size() == 1) {
				// Pin/Diff mode
				final List<IndexExposureData> ref = createData(pinnedPair.getFirst(), pinnedPair.getSecond());

				final Pair<@NonNull Schedule, com.mmxlabs.scenario.service.ScenarioResult> p = otherPairs.get(0);
				final List<IndexExposureData> other = createData(p.getFirst(), p.getSecond());

				LOOP_REF_DATA: for (final IndexExposureData refData : ref) {
					final Iterator<IndexExposureData> otherIterator = other.iterator();
					while (otherIterator.hasNext()) {
						final IndexExposureData otherData = otherIterator.next();
						if (Objects.equals(refData.date, otherData.date) && Objects.equals(refData.type, otherData.type)) { // indexName

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
				for (final Pair<@NonNull Schedule, com.mmxlabs.scenario.service.ScenarioResult> p : otherPairs) {
					output.addAll(createData(p.getFirst(), p.getSecond()));
				}
			}

			return output;
		}

		public List<IndexExposureData> createData(final @NonNull Schedule schedule, final @NonNull ScenarioResult scenarioResult) {

			entities = getEntities(schedule);
			fiscalYears = getFiscalYears(schedule);
			final YearMonth ymStart = getEarliestExposureDate(schedule);
			final YearMonth ymEnd = getLatestExposureDate(schedule);
			final LNGScenarioModel rootObject = scenarioResult.getTypedRoot(LNGScenarioModel.class);
			if (ymStart == null || ymEnd == null || rootObject == null) {
				return Collections.emptyList();
			}

			List<Object> selected = (!selectionMode || selection == null) ? //
					Collections.emptyList() : SelectionHelper.convertToList(selection, Object.class);
			selected = selected.stream().filter(s -> s instanceof Slot || s instanceof SlotAllocation || s instanceof Cargo || s instanceof CargoAllocation || s instanceof PaperDeal || s instanceof DealSet)
					.collect(Collectors.toList());
			selected = ExposuresTransformer.expandFilter(selected);
			return getExposuresByMonth(schedule, scenarioResult, ymStart, ymEnd, selected);
		}

		protected List<IndexExposureData> getExposuresByMonth(final Schedule schedule, final ScenarioResult scenarioResult, final YearMonth ymStart, final YearMonth ymEnd, List<Object> selected) {
			final List<IndexExposureData> output = new LinkedList<>();
			final IScenarioDataProvider sdp = scenarioResult.getScenarioDataProvider();
			final Map<String, String> curve2Index = ModelMarketCurveProvider.getCurveToIndexMap(ScenarioModelUtil.getPricingModel(sdp));

			for (YearMonth cym = ymStart; cym.isBefore(ymEnd.plusMonths(1)); cym = cym.plusMonths(1)) {
				IndexExposureData exposuresByMonth = ExposuresTransformer.getExposuresByMonth(scenarioResult, sdp, schedule, cym, mode, selected, selectedEntity, selectedFiscalYear, selectedAssetType,
						showGenerated, curve2Index);
				if (inspectChildrenAndExposures(exposuresByMonth)) {
					output.add(exposuresByMonth);
				}
			}
			return ExposuresTransformer.aggregateBy(aggregationMode, output, scenarioResult);
		}

		protected void customiseColumnList(final List<String> columnNames) {
			columnNames.add("Physical");
		}
	}
	// -------------------------------------Transformer class
	// ends----------------------------------------

	protected List<String> getFiscalYears(final Schedule schedule) {
		final List<String> result = new LinkedList<>();
		for (final PaperDealAllocation paperDealAllocation : schedule.getPaperDealAllocations()) {
			final PaperDeal pd = paperDealAllocation.getPaperDeal();
			if (pd == null) {
				continue;
			}
			final String year = String.format("%d", pd.getYear());
			if (!result.contains(year)) {
				result.add(year);
			}
		}
		for (final CargoAllocation cargoAllocation : schedule.getCargoAllocations()) {
			for (final SlotAllocation slotAllocation : cargoAllocation.getSlotAllocations()) {
				final Slot s = slotAllocation.getSlot();
				if (s == null)
					continue;
				final String year = String.format("%d", s.getWindowStart().getYear());
				if (!result.contains(year)) {
					result.add(year);
				}
			}
		}
		result.sort((a, b) -> a.compareTo(b));
		return result;
	}

	protected List<String> getEntities(final Schedule schedule) {
		final List<String> result = new LinkedList<String>();
		for (final PaperDealAllocation paperDealAllocation : schedule.getPaperDealAllocations()) {
			final PaperDeal pd = paperDealAllocation.getPaperDeal();
			if (pd == null || pd.getEntity() == null)
				continue;
			final String entity = pd.getEntity().getName();
			if (!result.contains(entity)) {
				result.add(entity);
			}
		}
		for (final CargoAllocation cargoAllocation : schedule.getCargoAllocations()) {
			for (final SlotAllocation slotAllocation : cargoAllocation.getSlotAllocations()) {
				final Slot s = slotAllocation.getSlot();
				if (s == null)
					continue;
				if (s.getSlotOrDelegateEntity() == null)
					continue;
				final String entity = s.getSlotOrDelegateEntity().getName();
				if (!result.contains(entity)) {
					result.add(entity);
				}
			}
		}
		return result;
	}

	@Override
	public void createPartControl(final Composite parent) {
		super.createPartControl(parent);

		cellImageDarkArrowDown = CommonImages.getImage(IconPaths.DarkArrowDown, IconMode.Enabled);
		cellImageDarkArrowUp = CommonImages.getImage(IconPaths.DarkArrowUp, IconMode.Enabled);

		colourBlue = new Color(Display.getDefault(), 135, 206, 235);
		colourLightBlue = new Color(Display.getDefault(), 135, 206, 250);
		colourGreen = new Color(Display.getDefault(), 132, 148, 67);
		colourOrange = new Color(Display.getDefault(), 255, 161, 79);
		colourViolet = new Color(Display.getDefault(), 244, 238, 224);
		final Font systemFont = Display.getDefault().getSystemFont();
		final FontData fontData = systemFont.getFontData()[0];
		fontBold = new Font(Display.getDefault(), new FontData(fontData.getName(), fontData.getHeight(), SWT.BOLD));
		
		if (memento != null) {
			final IMemento configMemento = memento.getChild(getConfigStateName());
	
			if (configMemento != null) {
				initConfigMemento(configMemento);
			}
		}
	}
	
	@Override
	public void init(final IViewSite site, IMemento memento) throws PartInitException {
		if (memento == null) {
			memento = XMLMemento.createWriteRoot("workbench");
		}
		this.memento = memento;

		super.init(site, memento);
	}
	
	@Override
	public void saveState(final IMemento memento) {
		super.saveState(memento);
		final IMemento configMemento;
		if (memento.getChild(getConfigStateName()) != null) {
			configMemento = memento.getChild(getConfigStateName());
		} else {
			configMemento = memento.createChild(getConfigStateName());
		}
		saveConfigState(configMemento);
	}
	
	protected String getConfigStateName() {
		return this.getClass().getName();
	}

	private static final String VALUE_MODE = "EXPOSURES_REPORT_GROUPING_MODE";
	private static final String AGGREGATION_MODE = "EXPOSURES_REPORT_AGGREGATION_MODE";
	private static final String PERIOD_MODE = "EXPOSURES_REPORT_PERIOD_MODE";
	private static final String SHOW_GENERATED_MODE = "EXPOSURES_REPORT_SHOW_GENERATED_MODE";
	private static final String ASSET_TYPE_MODE = "EXPOSURES_REPORT_ASSET_TYPE_MODE";
	private static final String SELECTION_MODE = "EXPOSURES_REPORT_SELECTION_MODE";
	
	protected void saveConfigState(final IMemento configMemento) {
		final IMemento am = configMemento.createChild(AGGREGATION_MODE);
		am.putString(AGGREGATION_MODE, aggregationMode.name());
		final IMemento vm = configMemento.createChild(VALUE_MODE);
		vm.putString(VALUE_MODE, mode.name());
		final IMemento pm = configMemento.createChild(PERIOD_MODE);
		pm.putBoolean(PERIOD_MODE, showPeriod);
		final IMemento sgm = configMemento.createChild(SHOW_GENERATED_MODE);
		sgm.putBoolean(SHOW_GENERATED_MODE, showGenerated);
		final IMemento atm = configMemento.createChild(ASSET_TYPE_MODE);
		atm.putString(ASSET_TYPE_MODE, selectedAssetType.value);
		final IMemento sm = configMemento.createChild(SELECTION_MODE);
		sm.putBoolean(SELECTION_MODE, selectionMode);
	}
	
	protected void initConfigMemento(final IMemento configMemento) {
		final IMemento am = configMemento.getChild(AGGREGATION_MODE);
		if (am != null && am.getBoolean(AGGREGATION_MODE) != null) {
			aggregationMode = getAggregationModeFromName(am.getString(AGGREGATION_MODE));
		}
		final IMemento vm = configMemento.getChild(VALUE_MODE);
		if (vm != null && vm.getString(VALUE_MODE) != null){
			mode = getValueModeFromName(vm.getString(VALUE_MODE)); 
		}
		final IMemento sp = configMemento.getChild(PERIOD_MODE);
		if (sp != null && sp.getBoolean(PERIOD_MODE) != null) {
			showPeriod = sp.getBoolean(PERIOD_MODE);
		}
		final IMemento sgm = configMemento.getChild(SHOW_GENERATED_MODE);
		if (sgm != null && sgm.getBoolean(SHOW_GENERATED_MODE) != null) {
			showGenerated = sgm.getBoolean(SHOW_GENERATED_MODE);
		}
		final IMemento atm = configMemento.getChild(ASSET_TYPE_MODE);
		if (atm != null && atm.getString(ASSET_TYPE_MODE) != null) {
			selectedAssetType = getAssetTypeFromName(atm.getString(ASSET_TYPE_MODE));
		}
		final IMemento sm = configMemento.getChild(SELECTION_MODE);
		if (sm != null && sm.getBoolean(SELECTION_MODE) != null) {
			selectionMode = sm.getBoolean(SELECTION_MODE);
		}
	}
	
	private AggregationMode getAggregationModeFromName(final String name) {
		switch (name) {
		case "BY_MONTH_NO_TOTAL":
			return AggregationMode.BY_MONTH_NO_TOTAL;
		case "BY_MONTH":
			return AggregationMode.BY_MONTH;
		case "BY_DEALSET":
			return AggregationMode.BY_DEALSET;
		case "BY_CALENDAR_YEAR":
			return AggregationMode.BY_CALENDAR_YEAR;
		default:
			return AggregationMode.BY_MONTH;
		}
	}
	
	private ValueMode getValueModeFromName(final String name) {
		switch (name) {
		case "VOLUME_MMBTU":
			return ValueMode.VOLUME_MMBTU;
		case "VOLUME_TBTU":
			return ValueMode.VOLUME_TBTU;
		case "VOLUME_NATIVE":
			return ValueMode.VOLUME_NATIVE;
		case "NATIVE_VALUE":
			return ValueMode.NATIVE_VALUE;
		default:
			return ValueMode.VOLUME_MMBTU;
		}
	}
	
	private AssetType getAssetTypeFromName(final String name) {
		switch(name) {
		case "All":
			return AssetType.NET;
		case "Index":
			return AssetType.FINANCIAL;
		case "Paper":
			return AssetType.PAPER;
		case "Physical Index":
			return AssetType.INDEX;
		case "Physical":
			return AssetType.PHYSICAL;
		default:
			return AssetType.NET;
		}
	}

	@Override
	public void dispose() {

		if (colourBlue != null) {
			colourBlue.dispose();
		}
		if (colourLightBlue != null) {
			colourLightBlue.dispose();
		}
		if (colourGreen != null) {
			colourGreen.dispose();
		}
		if (colourOrange != null) {
			colourOrange.dispose();
		}
		if (colourViolet != null) {
			colourViolet.dispose();
		}
		if (fontBold != null) {
			fontBold.dispose();
		}
		super.dispose();
	}

	protected ValueMode mode = ValueMode.VOLUME_MMBTU;
	protected AggregationMode aggregationMode = AggregationMode.BY_MONTH;
	protected boolean selectionMode = false;

	protected ISelection selection;

	public ExposureReportView() {
		super("com.mmxlabs.lingo.doc.Reports_IndexExposures");
		showGenerated = false;
	}

	@Override
	protected SimpleTabularReportContentProvider createContentProvider() {
		return new SimpleTabularReportContentProvider() {

			@Override
			public Object[] getElements(final Object inputElement) {

				if (inputElement instanceof final Collection<?> collection) {
					return collection.toArray();
				}
				return new Object[0];
			}

			@Override
			public Object[] getChildren(final Object parentElement) {
				if (parentElement instanceof final Collection<?> collection) {
					return collection.toArray();
				}
				if (parentElement instanceof final IndexExposureData indexExposureData) {
					if (indexExposureData.children != null) {
						return indexExposureData.children.toArray();
					}
				}
				return null;
			}

			@Override
			public Object getParent(final Object element) {
				return null;
			}

			@Override
			public boolean hasChildren(final Object element) {
				return element instanceof Collection<?>
						|| (element instanceof final IndexExposureData indexExposureData //
								&& indexExposureData.children != null //
								&& indexExposureData.type == IndexExposureType.MONTHLY);
			}
		};
	}

	@Override
	protected AbstractSimpleTabularReportTransformer<IndexExposureData> createTransformer() {
		return new ExposureData();
	}

	protected @NonNull IndexExposureData createDiffData(final IndexExposureData pinData, final IndexExposureData otherData) {

		final IndexExposureData modelData = pinData != null ? pinData : otherData;
		assert modelData != null;

		final Map<String, Double> exposuresByMonth = new HashMap<>();

		Collection<String> k = new LinkedHashSet<>();
		Map<String, IndexExposureData> pc = new HashMap<>();
		Map<String, IndexExposureData> oc = new HashMap<>();
		if (pinData != null) {
			for (final Map.Entry<String, Double> e : pinData.exposures.entrySet()) {
				exposuresByMonth.merge(e.getKey(), -e.getValue(), Double::sum);
			}
			if (pinData.children != null) {
				for (IndexExposureData d : pinData.children) {
					k.add(d.childName);
					pc.put(d.childName, d);
				}
			}
		}
		if (otherData != null) {
			for (final Map.Entry<String, Double> e : otherData.exposures.entrySet()) {
				exposuresByMonth.merge(e.getKey(), e.getValue(), Double::sum);
			}
			if (otherData.children != null) {
				for (IndexExposureData d : otherData.children) {
					k.add(d.childName);
					oc.put(d.childName, d);
				}
			}
		}

		List<IndexExposureData> newChildren = null;
		if (!k.isEmpty()) {
			newChildren = new ArrayList<>(k.size());
			for (String key : k) {
				IndexExposureData childDiffData = createDiffData(pc.get(key), oc.get(key));
				newChildren.add(childDiffData);
			}
		}
		IndexExposureData ied1;
		if (modelData.isChild) {
			ied1 = new IndexExposureData(null, null, modelData.date, modelData.childName, exposuresByMonth, modelData.entity);
		} else {
			ied1 = new IndexExposureData(null, null, modelData.date, exposuresByMonth, newChildren);
		}
		ied1.setType(modelData.type);
		return ied1;
	}

	private Action period = null;
	
	@Override
	protected void makeActions() {
		
		getViewSite().getActionBars().getToolBarManager().add(new GroupMarker("dates"));
		
		dates = new DatesToolbarEditor("exposures_dates_toolbar", e -> {
			getViewSite().getActionBars().updateActionBars();
			ExposureReportView.this.refresh();
		});
		
		period = new Action("Period : All", Action.AS_PUSH_BUTTON) {
			@Override
			public void run() {
				showPeriod = !showPeriod;
				setText(showPeriod ? "Period : Set" : "Period : All");
				if (showPeriod) {
					getViewSite().getActionBars().getToolBarManager().prependToGroup("dates", dates);
				} else {
					getViewSite().getActionBars().getToolBarManager().remove(dates);
				}
				getViewSite().getActionBars().updateActionBars();
				saveState(memento);
				ExposureReportView.this.refresh();
			}
		};
		
		period.setImageDescriptor(CommonImages.getImageDescriptor(IconPaths.Date, IconMode.Enabled));
		getViewSite().getActionBars().getToolBarManager().prependToGroup("dates", period);
		
		getViewSite().getActionBars().getToolBarManager().add(new GroupMarker("filters"));
		
		final ValueModeActionMenu valueModeAction = new ValueModeActionMenu("Units: currency");
		valueModeAction.setImageDescriptor(CommonImages.getImageDescriptor(IconPaths.Units, IconMode.Enabled));
		setUnitsActionText(valueModeAction);
		getViewSite().getActionBars().getToolBarManager().appendToGroup("filters", valueModeAction);

		final Action selectionToggle = new Action("View: " + (selectionMode ? "Selection" : "All"), Action.AS_PUSH_BUTTON) {
			@Override
			public void run() {

				selectionMode = !selectionMode;
				setText("View: " + (selectionMode ? "Selection" : "All"));
				getViewSite().getActionBars().updateActionBars();
				saveState(memento);
				ExposureReportView.this.refresh();

			}
		};
		selectionToggle.setImageDescriptor(CommonImages.getImageDescriptor(IconPaths.Selected, IconMode.Enabled));
		getViewSite().getActionBars().getToolBarManager().appendToGroup("filters", selectionToggle);

		final AggregationModeActionMenu aggregationModeAction = new AggregationModeActionMenu("Group: Month");
		aggregationModeAction.setImageDescriptor(CommonImages.getImageDescriptor(IconPaths.Group, IconMode.Enabled));
		setAggregationModeActionText(aggregationModeAction);
		getViewSite().getActionBars().getToolBarManager().appendToGroup("filters", aggregationModeAction);
		
		final FilterMenuAction filterAction = new FilterMenuAction("Filters");
		filterAction.setImageDescriptor(CommonImages.getImageDescriptor(IconPaths.Filter, IconMode.Enabled));
		getViewSite().getActionBars().getToolBarManager().appendToGroup("filters", filterAction);
		
		super.makeActions();
	}

	private class FilterMenuAction extends DefaultMenuCreatorAction {

		public FilterMenuAction(final String label) {
			super(label);
		}

		@Override
		protected void populate(Menu menu) {
			final Action clearAction = new Action("Clear Filter") {
				@Override
				public void run() {
					selectedEntity = null;
					selectedFiscalYear = -1;
					selectedAssetType = AssetType.NET;
					showGenerated = false;
					saveState(memento);
					ExposureReportView.this.refresh();
				}
			};

			addActionToMenu(clearAction, menu);
			
			if (LicenseFeatures.isPermitted(KnownFeatures.FEATURE_PAPER_DEALS) && LicenseFeatures.isPermitted(KnownFeatures.FEATURE_GENERATED_PAPER_DEALS)) {
				final Action showGeneratedPaperDeals = new Action("Generated") {
					@Override
					public void run() {
						showGenerated = !showGenerated;
						saveState(memento);
						this.setChecked(showGenerated);
						ExposureReportView.this.refresh();
					}
				};
				showGeneratedPaperDeals.setChecked(showGenerated);
				addActionToMenu(showGeneratedPaperDeals, menu);
				
			}

			final DefaultMenuCreatorAction dmcaEntity = new DefaultMenuCreatorAction("Entity") {

				@Override
				protected void populate(Menu subMenu) {
					if (entities == null || entities.isEmpty())
						return;
					for (final String e : entities) {
						final Action entityAction = new Action(e) {
							@Override
							public void run() {
								selectedEntity = e;
								ExposureReportView.this.refresh();
							}
						};
						if (Objects.equals(selectedEntity, e)) {
							entityAction.setChecked(true);
						}
						addActionToMenu(entityAction, subMenu);
					}
				}

			};
			addActionToMenu(dmcaEntity, menu);

			final DefaultMenuCreatorAction dmcaFiscalYear = new DefaultMenuCreatorAction("Fiscal Year") {

				@Override
				protected void populate(Menu subMenu) {
					if (fiscalYears == null || fiscalYears.isEmpty())
						return;
					for (final String e : fiscalYears) {
						final Action fiscalYearAction = new Action(e) {
							@Override
							public void run() {
								selectedFiscalYear = Integer.parseInt(e);
								ExposureReportView.this.refresh();
							}
						};
						if (selectedFiscalYear == Integer.parseInt(e)) {
							fiscalYearAction.setChecked(true);
						}
						addActionToMenu(fiscalYearAction, subMenu);
					}
				}

			};
			addActionToMenu(dmcaFiscalYear, menu);

			final DefaultMenuCreatorAction dmcaAssetTypes = new DefaultMenuCreatorAction("Phys/Fin") {

				@Override
				protected void populate(Menu subMenu) {
					for (final AssetType at : AssetType.values()) {
						final Action assetTypeAction = new Action(at.toString()) {
							@Override
							public void run() {
								selectedAssetType = at;
								ExposureReportView.this.refresh();
							}
						};
						if (selectedAssetType == at) {
							assetTypeAction.setChecked(true);
						}
						addActionToMenu(assetTypeAction, subMenu);
					}
				}
			};
			addActionToMenu(dmcaAssetTypes, menu);
		}
	}
	
	private class AggregationModeActionMenu extends DefaultMenuCreatorAction {

		public AggregationModeActionMenu(String label) {
			super(label);
		}

		@Override
		protected void populate(Menu menu) {
			for (final AggregationMode am : AggregationMode.values()) {
				final Action amAction = new Action(getAggregationMode(am)) {
					@Override
					public void run() {
						aggregationMode = am;
						setAggregationModeActionText(AggregationModeActionMenu.this);
						getViewSite().getActionBars().updateActionBars();
						saveState(memento);
						ExposureReportView.this.refresh();
					}
				};
				amAction.setChecked(aggregationMode == am);
				addActionToMenu(amAction, menu);
			}
		}
	}
	
	private class ValueModeActionMenu extends DefaultMenuCreatorAction{
		
		public ValueModeActionMenu(String label) {
			super(label);
		}
		
		@Override
		protected void populate(Menu menu) {
			for (final ValueMode vm : ValueMode.values()) {
				final Action vmAction = new Action(getUnitsActionText(vm)) {
					@Override
					public void run() {
						mode = vm;
						setUnitsActionText(ValueModeActionMenu.this);
						getViewSite().getActionBars().updateActionBars();
						saveState(memento);
						ExposureReportView.this.refresh();
					}
				};
				vmAction.setChecked(mode == vm);
				addActionToMenu(vmAction, menu);
			}
		}
	}

	@Override
	public void doSelectionChanged(final MPart part, final Object selectionObject) {
		if (selectionMode && SelectionServiceUtils.isSelectionValid(part, selectionObject)) {
			selection = SelectionHelper.adaptSelection(selectionObject);
			ExposureReportView.this.refresh();
		}
	}

	private void setUnitsActionText(final Action a) {
		a.setText(String.format("Units: %s", getUnitsActionText(mode)));
	}
	
	private String getUnitsActionText(final ValueMode valueMode) {
		return switch (valueMode) {
		case NATIVE_VALUE -> "currency";
		case VOLUME_MMBTU -> "mmBtu";
		case VOLUME_TBTU -> "TBtu";
		case VOLUME_NATIVE -> "native";
		default -> throw new IllegalArgumentException();
		};
	}

	private void setAggregationModeActionText(final Action a) {
		a.setText(String.format("Group: %s", getAggregationMode(aggregationMode)));
	}
	
	private String getAggregationMode(final AggregationMode am) {
		return switch (am) {
		case BY_MONTH -> "Month";
		case BY_CALENDAR_YEAR -> "Calendar Year";
		case BY_DEALSET -> "Deal Set";
		case BY_MONTH_NO_TOTAL -> "Month (No total)";
		default -> throw new IllegalArgumentException();
		};
	}

	@Override
	public <T> T getAdapter(final Class<T> adapter) {

		if (IReportContentsGenerator.class.isAssignableFrom(adapter)) {

			return adapter.cast(new IReportContentsGenerator() {
				public IReportContents getReportContents(final ScenarioResult pin, final ScenarioResult other, final @Nullable List<Object> selectedObjects) {
					if (selectedObjects == null) {
						selection = new StructuredSelection();
					} else {
						selection = new StructuredSelection(selectedObjects);
					}
					final SelectedDataProviderImpl provider = new SelectedDataProviderImpl();
					if (pin != null) {
						provider.addScenario(pin);
						provider.setPinnedScenarioInstance(pin);
					}
					if (other != null) {
						provider.addScenario(other);
					}
					// Request a blocking update ...
					selectedScenariosServiceListener.selectedDataProviderChanged(provider, true);
					// ... so the data is ready to be read here.
					final CopyGridToHtmlStringUtil util = new CopyGridToHtmlStringUtil(viewer.getGrid(), false, true);
					util.setRowHeadersIncluded(true);
					util.setShowBackgroundColours(true);
					final String contents = util.convert();

					return ReportContents.makeHTML(contents);

				}
			});
		}
		return super.getAdapter(adapter);
	}

	protected YearMonth getEarliestExposureDate(final Schedule schedule) {
		YearMonth result = null;
		
		if (dates!= null && showPeriod) {
			final LocalDate date = dates.getStartDate();
			if (date != null) {
				return YearMonth.from(date);
			}
		}

		if (schedule != null) {
			for (final CargoAllocation ca : schedule.getCargoAllocations()) {
				for (final SlotAllocation sa : ca.getSlotAllocations()) {
					for (final ExposureDetail detail : sa.getExposures()) {
						if (detail.getHedgingPeriodStart() != null) {
							final YearMonth hedgeMonth = YearMonth.from(detail.getHedgingPeriodStart());
							if (result == null || hedgeMonth.isBefore(result)) {
								result = hedgeMonth;
							}
						}
					}
				}
			}
			if (LicenseFeatures.isPermitted(KnownFeatures.FEATURE_PAPER_DEALS)) {
				for (final PaperDealAllocation pd : schedule.getPaperDealAllocations()) {
					for (final var entry: pd.getEntries()) {
						for (final ExposureDetail detail : entry.getExposures()) {
							if (detail.getHedgingPeriodStart() != null) {
								final YearMonth hedgeMonth = YearMonth.from(detail.getHedgingPeriodStart());
								if (result == null || hedgeMonth.isBefore(result)) {
									result = hedgeMonth;
								}
							}
						}
					}
				}
			}
			if (!LicenseFeatures.isPermitted(KnownFeatures.FEATURE_OPEN_SLOT_EXPOSURE)) {
				return result;
			}
			for (final OpenSlotAllocation sa : schedule.getOpenSlotAllocations()) {
				for (final ExposureDetail detail : sa.getExposures()) {
					final YearMonth hedgeMonth = YearMonth.from(detail.getHedgingPeriodStart());
					if (result == null || hedgeMonth.isBefore(result)) {
						result = hedgeMonth;
					}
				}
			}
		}
		return result;
	}

	protected YearMonth getLatestExposureDate(final Schedule schedule) {
		YearMonth result = null;
		
		if (dates!= null && showPeriod) {
			final LocalDate date = dates.getEndDate();
			if (date != null) {
				return YearMonth.from(date);
			}
		}

		if (schedule != null) {
			for (final CargoAllocation ca : schedule.getCargoAllocations()) {
				for (final SlotAllocation sa : ca.getSlotAllocations()) {
					for (final ExposureDetail detail : sa.getExposures()) {
						if (detail.getHedgingPeriodEnd() != null) {
							final YearMonth hedgeMonth = YearMonth.from(detail.getHedgingPeriodEnd());
							if (result == null || hedgeMonth.isAfter(result)) {
								result = hedgeMonth;
							}
						}
					}
				}
			}
			if (LicenseFeatures.isPermitted(KnownFeatures.FEATURE_PAPER_DEALS)) {
				for (final PaperDealAllocation pd : schedule.getPaperDealAllocations()) {
					for (final var entry: pd.getEntries()) {
						for (final ExposureDetail detail : entry.getExposures()) {
							if (detail.getHedgingPeriodEnd() != null) {
								final YearMonth hedgeMonth = YearMonth.from(detail.getHedgingPeriodEnd());
								if (result == null || hedgeMonth.isAfter(result)) {
									result = hedgeMonth;
								}
							}
						}
					}
				}
			}
			if (!LicenseFeatures.isPermitted(KnownFeatures.FEATURE_OPEN_SLOT_EXPOSURE))
				return result;
			for (final OpenSlotAllocation sa : schedule.getOpenSlotAllocations()) {
				for (final ExposureDetail detail : sa.getExposures()) {
					final YearMonth hedgeMonth = YearMonth.from(detail.getHedgingPeriodEnd());
					if (result == null || hedgeMonth.isAfter(result)) {
						result = hedgeMonth;
					}
				}
			}
		}
		return result;
	}

	public enum AssetType {
		NET("All"), FINANCIAL("Index"), PAPER("Paper"), INDEX("Physical Index"), PHYSICAL("Physical");

		private String value;

		private AssetType(String value) {
			this.value = value;
		}

		@Override
		public String toString() {
			return value;
		}
	}

	public static boolean inspectChildrenAndExposures(IndexExposureData fd) {
		if (fd.children != null && !fd.children.isEmpty()) {
			return true;
		}
		if (fd.exposures != null && !fd.exposures.isEmpty()) {
			return true;
		}
		return false;
	}

	@Override
	protected void applyExpansionOnNewElements(final Object[] expanded, final List<?> rowElements) {
		final List<Object> newToExpand = new ArrayList<>();
		for (var e : expanded) {
			if (e instanceof final IndexExposureData oldE) {
				for (var elem : rowElements) {
					if (elem instanceof final IndexExposureData newE) {

						if (newE.date.equals(oldE.date) && Objects.equals(newE.scenarioResult, oldE.scenarioResult)) {
							newToExpand.add(newE);
						}
					}
				}
			}
		}
		if (!newToExpand.isEmpty()) {
			viewer.setExpandedElements(newToExpand.toArray());
		}
	}
	
	@Override
	protected synchronized void refresh() {
		if (viewer != null) {
			viewer.refresh();
		}
		super.refresh();
	}
}