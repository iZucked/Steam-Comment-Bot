/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2022
 * All rights reserved.
 */
package com.mmxlabs.lingo.reports.views.scenariodiff;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import org.eclipse.e4.ui.model.application.ui.basic.MPart;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.Nullable;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.GroupMarker;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.IToolBarManager;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.action.Separator;
import org.eclipse.jface.viewers.CellLabelProvider;
import org.eclipse.jface.viewers.ITableColorProvider;
import org.eclipse.jface.viewers.ITableFontProvider;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerCell;
import org.eclipse.jface.viewers.ViewerComparator;
import org.eclipse.nebula.jface.gridviewer.GridTreeViewer;
import org.eclipse.nebula.jface.gridviewer.GridViewerColumn;
import org.eclipse.nebula.widgets.grid.GridColumn;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.ui.IActionBars;
import org.eclipse.ui.IWorkbenchActionConstants;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.actions.ActionFactory;
import org.eclipse.ui.part.ViewPart;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mmxlabs.common.Pair;
import com.mmxlabs.lingo.reports.IReportContentsGenerator;
import com.mmxlabs.lingo.reports.ReportContentsGenerators;
import com.mmxlabs.lingo.reports.components.AbstractSimpleTabularReportTransformer;
import com.mmxlabs.lingo.reports.components.ColumnManager;
import com.mmxlabs.lingo.reports.components.SimpleTabularReportContentProvider;
import com.mmxlabs.lingo.reports.services.ISelectedDataProvider;
import com.mmxlabs.lingo.reports.services.ISelectedScenariosServiceListener;
import com.mmxlabs.lingo.reports.services.ReentrantSelectionManager;
import com.mmxlabs.lingo.reports.services.ScenarioComparisonService;
import com.mmxlabs.models.lng.cargo.CargoPackage;
import com.mmxlabs.models.lng.cargo.CharterOutEvent;
import com.mmxlabs.models.lng.cargo.DischargeSlot;
import com.mmxlabs.models.lng.cargo.DryDockEvent;
import com.mmxlabs.models.lng.cargo.LoadSlot;
import com.mmxlabs.models.lng.cargo.MaintenanceEvent;
import com.mmxlabs.models.lng.cargo.VesselEvent;
import com.mmxlabs.models.lng.commercial.CommercialPackage;
import com.mmxlabs.models.lng.commercial.PurchaseContract;
import com.mmxlabs.models.lng.commercial.SalesContract;
import com.mmxlabs.models.lng.fleet.FleetPackage;
import com.mmxlabs.models.lng.fleet.Vessel;
import com.mmxlabs.models.lng.port.Port;
import com.mmxlabs.models.lng.port.PortPackage;
import com.mmxlabs.models.lng.scenario.model.LNGScenarioModel;
import com.mmxlabs.models.lng.schedule.Schedule;
import com.mmxlabs.models.lng.spotmarkets.CharterInMarket;
import com.mmxlabs.models.lng.spotmarkets.CharterOutMarket;
import com.mmxlabs.models.lng.spotmarkets.DESPurchaseMarket;
import com.mmxlabs.models.lng.spotmarkets.DESSalesMarket;
import com.mmxlabs.models.lng.spotmarkets.FOBPurchasesMarket;
import com.mmxlabs.models.lng.spotmarkets.FOBSalesMarket;
import com.mmxlabs.models.lng.spotmarkets.SpotMarket;
import com.mmxlabs.models.lng.spotmarkets.SpotMarketsPackage;
import com.mmxlabs.models.mmxcore.MMXCorePackage;
import com.mmxlabs.models.mmxcore.NamedObject;
import com.mmxlabs.models.ui.tabular.renderers.ColumnHeaderRenderer;
import com.mmxlabs.models.ui.tabular.renderers.EmptyColumnHeaderRenderer;
import com.mmxlabs.models.ui.tabular.renderers.RowHeaderRenderer;
import com.mmxlabs.models.ui.tabular.renderers.TopLeftRenderer;
import com.mmxlabs.rcp.common.ViewerHelper;
import com.mmxlabs.rcp.common.actions.AbstractMenuAction;
import com.mmxlabs.rcp.common.actions.CopyGridToHtmlClipboardAction;
import com.mmxlabs.rcp.common.actions.PackGridTreeColumnsAction;
import com.mmxlabs.rcp.common.actions.RunnableAction;
import com.mmxlabs.rcp.common.json.EMFJacksonModule;
import com.mmxlabs.rcp.icons.lingo.CommonImages;
import com.mmxlabs.rcp.icons.lingo.CommonImages.IconMode;
import com.mmxlabs.rcp.icons.lingo.CommonImages.IconPaths;
import com.mmxlabs.scenario.service.ScenarioResult;

public abstract class AbstractScenarioDiff<T> extends ViewPart {

	private static final String KEY_PINNED = "pinned";
	private static final String KEY_NON_PINNED = "";

	private static final String KEY_CHARTER_IN_MARKETS = "CharterInMarkets";
	private static final String KEY_CHARTER_OUT_MARKETS = "CharterOutMarkets";
	private static final String KEY_DES_PURCHASE_MARKETS = "Cargo_Markets_Des_Purchase";
	private static final String KEY_DES_SALE_MARKETS = "Cargo_Markets_Des_Sales";
	private static final String KEY_FOB_PURCHASE_MARKETS = "Cargo_Markets_Fob_Purchase";
	private static final String KEY_FOB_SALE_MARKETS = "Cargo_Markets_Fob_Sales";
	private static final String KEY_PORTS = "Ports";
	private static final String KEY_PURCHASE_CONTRACTS = "PurchaseContracts";
	private static final String KEY_SALES_CONTRACTS = "SalesContracts";
	private static final String KEY_TRADES = "Trades";
	private static final String KEY_VESSELS = "Vessls";
	private static final String KEY_VESSEL_EVENTS = "VesselEvents";

	private static final String VALUE_NOT_APPLICABLE = "n/a";
	private static final String VALUE_PRESENT = "Present";

	private ScenarioComparisonService selectedScenariosService;

	private final ArrayList<ColumnManager<T>> sortColumns = new ArrayList<>();
	protected final ArrayList<ColumnManager<T>> columnManagers = new ArrayList<>();

	private boolean inverseSort = false;

	protected GridTreeViewer viewer;

	private Action packColumnsAction;

	private Action copyTableAction;

	private AbstractMenuAction filterMenu;

	private final String helpContextId;

	protected boolean pinnedMode = false;

	protected Image pinImage;

	private boolean hideNotes = false;

	@NonNull
	protected final ISelectedScenariosServiceListener selectedScenariosServiceListener = new ISelectedScenariosServiceListener() {
		@Override
		public void selectedDataProviderChanged(@NonNull ISelectedDataProvider selectedDataProvider, boolean block) {
			ViewerHelper.runIfViewerValid(viewer, block, () -> refreshDiffViewer(selectedDataProvider));
		}

		@Override
		public void selectedObjectChanged(org.eclipse.e4.ui.model.application.ui.basic.MPart source, org.eclipse.jface.viewers.ISelection selection) {
			doSelectionChanged(source, selection);
		}

	};

	private void refreshDiffViewer(@NonNull final ISelectedDataProvider selectedDataProvider) {
		final AbstractSimpleTabularReportTransformer<T> transformer = (AbstractSimpleTabularReportTransformer<T>) createTransformer();

		columnManagers.clear();

		int numberOfSchedules = 0;
		Pair<Schedule, ScenarioResult> pinnedPair = null;
		List<Pair<Schedule, ScenarioResult>> otherPairs = new LinkedList<>();
		ScenarioResult pinned = selectedDataProvider.getPinnedScenarioResult();

		ScenarioResult other = null;
		if (!selectedDataProvider.getOtherScenarioResults().isEmpty()) {
			other = selectedDataProvider.getOtherScenarioResults().get(0);
		}

		pinnedPair = new Pair<>(null, pinned);
		otherPairs.add(new Pair<>(null, other));
		final List<T> rowElements = transformer.createData(pinnedPair, otherPairs);

		columnManagers.addAll(transformer.getColumnManagers(selectedDataProvider));
		clearColumns();
		addColumns();
		setShowColumns(pinned != null, numberOfSchedules);

		viewer.getLabelProvider().dispose();
		viewer.setLabelProvider(new ViewLabelProvider());

		ViewerHelper.setInput(viewer, true, rowElements);

		if (!rowElements.isEmpty() && packColumnsAction != null) {
			packColumnsAction.run();
		}	
	}

	public class ViewLabelProvider extends CellLabelProvider implements ITableLabelProvider, ITableFontProvider, ITableColorProvider {

		@SuppressWarnings("unchecked")
		@Override
		public String getColumnText(final Object obj, final int index) {
			if (index >= columnManagers.size()) {
				return "";
			}
			return columnManagers.get(index).getColumnText((T) obj);
		}

		@SuppressWarnings("unchecked")
		@Override
		public Image getColumnImage(final Object obj, final int index) {
			return columnManagers.get(index).getColumnImage((T) obj);
		}

		@Override
		public void update(final ViewerCell cell) {

		}

		@Override
		public Font getFont(final Object obj, final int index) {
			return columnManagers.get(index).getFont((T) obj);
		}

		@SuppressWarnings("unchecked")
		@Override
		public Color getBackground(final Object obj, final int index) {
			return columnManagers.get(index).getBackground((T) obj);
		}

		@SuppressWarnings("unchecked")
		@Override
		public Color getForeground(final Object obj, final int index) {
			return columnManagers.get(index).getForeground((T) obj);
		}
	}

	private void addSortSelectionListener(final GridColumn column, final ColumnManager<T> value) {
		column.addSelectionListener(new SelectionAdapter() {
			{
				final SelectionAdapter self = this;
				column.addDisposeListener(e -> column.removeSelectionListener(self));
			}

			@Override
			public void widgetSelected(final SelectionEvent e) {
				setSortColumn(column, value);
			}
		});
		if (!sortColumns.contains(value)) {
			sortColumns.add(value);
		}
	}

	protected void setSortColumn(final GridColumn column, final ColumnManager<T> value) {
		if (sortColumns.get(0) == value) {
			inverseSort = !inverseSort;
		} else {
			inverseSort = false;
			sortColumns.remove((Object) value);
			sortColumns.add(0, value);
		}

		ViewerHelper.refresh(viewer, true);
	}

	final List<GridViewerColumn> viewerColumns = new ArrayList<>();

	protected ReentrantSelectionManager selectionManager;

	/**
	 * This is a callback that will allow us to create the viewer and initialise it.
	 */
	@Override
	public void createPartControl(final Composite parent) {

		pinImage = CommonImages.getImage(IconPaths.Pin, IconMode.Enabled);

		selectedScenariosService = getSite().getService(ScenarioComparisonService.class);

		viewer = new GridTreeViewer(parent, SWT.MULTI | SWT.H_SCROLL | SWT.V_SCROLL | SWT.FULL_SELECTION);
		viewer.getGrid().setRowHeaderRenderer(new RowHeaderRenderer());
		viewer.getGrid().setTopLeftRenderer(new TopLeftRenderer());
		viewer.getGrid().setEmptyColumnHeaderRenderer(new EmptyColumnHeaderRenderer());

		viewer.setContentProvider(createContentProvider());
		viewer.setInput(getViewSite());

		viewer.setLabelProvider(new ViewLabelProvider());

		viewer.getGrid().setLinesVisible(true);
		viewer.getGrid().setHeaderVisible(true);

		viewer.setComparator(new ViewerComparator() {
			@SuppressWarnings("unchecked")
			@Override
			public int compare(final Viewer viewer, final Object e1, final Object e2) {
				final int d = inverseSort ? -1 : 1;
				for (final ColumnManager<T> cm : sortColumns) {
					final int sort = cm.compare((T) e1, (T) e2);
					if (sort != 0) {
						return d * sort;
					}
				}

				return 0;
			}
		});

		// Create the help context id for the viewer's control
		PlatformUI.getWorkbench().getHelpSystem().setHelp(viewer.getControl(), helpContextId);
		makeActions();
		hookContextMenu();
		contributeToActionBars();

		selectionManager = new ReentrantSelectionManager(viewer, selectedScenariosServiceListener, selectedScenariosService, false);
		try {
			selectedScenariosService.triggerListener(selectedScenariosServiceListener, false);
		} catch (Exception e) {
			// Ignore any initial issues.
		}
	}

	private void hookContextMenu() {
		final MenuManager menuMgr = new MenuManager("#PopupMenu");
		menuMgr.setRemoveAllWhenShown(true);
		menuMgr.addMenuListener(manager -> fillContextMenu(manager));
		final Menu menu = menuMgr.createContextMenu(viewer.getControl());
		viewer.getControl().setMenu(menu);
		getSite().registerContextMenu(menuMgr, viewer);
	}

	private void contributeToActionBars() {
		final IActionBars bars = getViewSite().getActionBars();
		fillLocalPullDown(bars.getMenuManager());
		fillLocalToolBar(bars.getToolBarManager());
	}

	private void fillLocalPullDown(final IMenuManager manager) {
		// manager.add(new Separator());
	}

	private void fillContextMenu(final IMenuManager manager) {
		// Other plug-ins can contribute there actions here
		manager.add(new Separator(IWorkbenchActionConstants.MB_ADDITIONS));
	}

	private void fillLocalToolBar(final IToolBarManager manager) {
		manager.add(new GroupMarker("pack"));
		manager.add(new GroupMarker("additions"));
		manager.add(new GroupMarker("edit"));
		manager.add(new GroupMarker("copy"));
		manager.add(new GroupMarker("importers"));
		manager.add(new GroupMarker("exporters"));

		manager.appendToGroup("pack", packColumnsAction);
		manager.appendToGroup("copy", copyTableAction);
	}

	protected void makeActions() {

		{
			final Action collapseAll = new Action("Collapse All") {
				@Override
				public void run() {
					viewer.collapseAll();
				}
			};
			final Action expandAll = new Action("Expand All") {
				@Override
				public void run() {
					viewer.expandAll();
				}
			};

			CommonImages.setImageDescriptors(collapseAll, IconPaths.CollapseAll);
			CommonImages.setImageDescriptors(expandAll, IconPaths.ExpandAll);

			getViewSite().getActionBars().getToolBarManager().add(collapseAll);
			getViewSite().getActionBars().getToolBarManager().add(expandAll);
		}

		packColumnsAction = new PackGridTreeColumnsAction(viewer);
		copyTableAction = new CopyGridToHtmlClipboardAction(viewer.getGrid(), false, () -> setCopyPasteMode(true), () -> setCopyPasteMode(false));
		getViewSite().getActionBars().setGlobalActionHandler(ActionFactory.COPY.getId(), copyTableAction);
		{
			getViewSite().getActionBars().getToolBarManager().add(new GroupMarker("filters"));
			filterMenu = new AbstractMenuAction("Filter...") {

				@Override
				protected void populate(Menu menu) {
					final RunnableAction toggleHideNotes = new RunnableAction("Hide notes diff", AbstractScenarioDiff.this::doHideNotesToggle);
					toggleHideNotes.setToolTipText("Toggle diffing of notes");
					toggleHideNotes.setChecked(hideNotes);
					addActionToMenu(toggleHideNotes, menu);
				}

			};
			filterMenu.setToolTipText("Change filters");
			filterMenu.setImageDescriptor(CommonImages.getImageDescriptor(IconPaths.Filter, IconMode.Enabled));
			getViewSite().getActionBars().getToolBarManager().add(filterMenu);
		}
	}

	private void doHideNotesToggle() {
		hideNotes = !hideNotes;
		ViewerHelper.runIfViewerValid(viewer, true, () -> refreshDiffViewer(selectedScenariosService.getCurrentSelectedDataProvider()));
	}

	protected void setCopyPasteMode(boolean copyPasteMode) {
		this.pinnedMode = copyPasteMode;
		ViewerHelper.refresh(viewer, true);
	}

	/**
	 * Passing the focus request to the viewer's control.
	 */
	@Override
	public void setFocus() {
		ViewerHelper.setFocus(viewer);
	}

	private void setShowColumns(final boolean showDeltaColumn, final int numberOfSchedules) {

	}

	private void addColumns() {
		for (final ColumnManager<T> cv : columnManagers) {
			final String name = cv.getName();
			final GridViewerColumn gvc = new GridViewerColumn(viewer, SWT.NONE);
			if (name.equals(KEY_PINNED)) {
				gvc.getColumn().setImage(pinImage);
			}
			gvc.getColumn().setHeaderTooltip(cv.getTooltip());
			gvc.getColumn().setHeaderRenderer(new ColumnHeaderRenderer());
			viewerColumns.add(gvc);
			final GridColumn gc = gvc.getColumn();
			gc.setText("");
			gc.pack();
			addSortSelectionListener(gc, cv);
			if (cv.isTree()) {
				gvc.getColumn().setTree(true);
			}
		}
	}

	private void clearColumns() {
		for (final GridViewerColumn gvc : viewerColumns) {
			gvc.getColumn().dispose();
		}
		viewerColumns.clear();
	}

	protected void refresh() {
		selectedScenariosService.triggerListener(selectedScenariosServiceListener, false);
	}

	public void doSelectionChanged(MPart part, Object selectionObject) {
		// For subclasses to override if they need to respond to selection changes
	}

	private<R extends NamedObject> ScenarioDiffData findDiffs(final R pinnedNamedObject, final R nonPinnedNamedObject) {
		Set<EStructuralFeature> blackList1 = new HashSet<>();
		blackList1.add(MMXCorePackage.eINSTANCE.getUUIDObject_Uuid());
		blackList1.add(CargoPackage.eINSTANCE.getAssignableElement_SequenceHint());
		blackList1.add(FleetPackage.eINSTANCE.getVessel_MmxReference());
		blackList1.add(PortPackage.eINSTANCE.getLocation_MmxId());
		blackList1.add(FleetPackage.eINSTANCE.getVessel_MmxId());
		blackList1.add(CargoPackage.eINSTANCE.getVesselCharter_ForceHireCostOnlyEndRule());
		if (hideNotes) {
			blackList1.add(CargoPackage.eINSTANCE.getSlot_Notes());
			blackList1.add(CommercialPackage.eINSTANCE.getContract_Notes());
			blackList1.add(FleetPackage.eINSTANCE.getVessel_Notes());
		}

		Map<String, String> parentMap = new HashMap<>();

		parentMap.put(KEY_PINNED, "");
		parentMap.put("", "");

		final ScenarioDiffData result = new ScenarioDiffData(parentMap, ((NamedObject) pinnedNamedObject).getName());
		final List<ScenarioDiffData> childrenList = new ArrayList<>();

		final Set<EStructuralFeature> featureSet = new HashSet<>();

		if (pinnedNamedObject instanceof LoadSlot) {
			featureSet.addAll(CargoPackage.eINSTANCE.getLoadSlot().getEAllStructuralFeatures());
		} else if (pinnedNamedObject instanceof DischargeSlot) {
			featureSet.addAll(CargoPackage.eINSTANCE.getDischargeSlot().getEAllStructuralFeatures());
		} else if (pinnedNamedObject instanceof Vessel) {
			featureSet.addAll(FleetPackage.eINSTANCE.getVessel().getEAllStructuralFeatures());
		} else if (pinnedNamedObject instanceof Port) {
			featureSet.addAll(PortPackage.eINSTANCE.getPort().getEAllStructuralFeatures());
			childrenList.addAll(ScenarioDiffUtil.extraDataGen(pinnedNamedObject, nonPinnedNamedObject));
		} else if (pinnedNamedObject instanceof CharterInMarket) {
			featureSet.addAll(SpotMarketsPackage.eINSTANCE.getCharterInMarket().getEAllStructuralFeatures());
			featureSet.addAll(SpotMarketsPackage.eINSTANCE.getSpotCharterMarket().getEAllStructuralFeatures());
		} else if (pinnedNamedObject instanceof CharterOutMarket) {
			featureSet.addAll(SpotMarketsPackage.eINSTANCE.getCharterOutMarket().getEAllStructuralFeatures());
			featureSet.addAll(SpotMarketsPackage.eINSTANCE.getSpotCharterMarket().getEAllStructuralFeatures());
		} else if (pinnedNamedObject instanceof CharterOutEvent) {
			featureSet.addAll(CargoPackage.eINSTANCE.getVesselEvent().getEAllStructuralFeatures());
			featureSet.addAll(CargoPackage.eINSTANCE.getCharterOutEvent().getEAllStructuralFeatures());
			childrenList.addAll(ScenarioDiffUtil.extraDataGen(pinnedNamedObject, nonPinnedNamedObject));
		} else if (pinnedNamedObject instanceof DryDockEvent) {
			featureSet.addAll(CargoPackage.eINSTANCE.getDryDockEvent().getEAllStructuralFeatures());
			featureSet.addAll(CargoPackage.eINSTANCE.getVesselEvent().getEAllStructuralFeatures());
			childrenList.addAll(ScenarioDiffUtil.extraDataGen(pinnedNamedObject, nonPinnedNamedObject));
		} else if (pinnedNamedObject instanceof MaintenanceEvent) {
			featureSet.addAll(CargoPackage.eINSTANCE.getMaintenanceEvent().getEAllStructuralFeatures());
			featureSet.addAll(CargoPackage.eINSTANCE.getVesselEvent().getEAllStructuralFeatures());
			childrenList.addAll(ScenarioDiffUtil.extraDataGen(pinnedNamedObject, nonPinnedNamedObject));
		} else if (pinnedNamedObject instanceof PurchaseContract) {
			featureSet.addAll(CommercialPackage.eINSTANCE.getPurchaseContract().getEAllStructuralFeatures());
			featureSet.addAll(CommercialPackage.eINSTANCE.getContract().getEAllStructuralFeatures());
		} else if (pinnedNamedObject instanceof SalesContract) {
			featureSet.addAll(CommercialPackage.eINSTANCE.getSalesContract().getEAllStructuralFeatures());
			featureSet.addAll(CommercialPackage.eINSTANCE.getContract().getEAllStructuralFeatures());
		} else if (pinnedNamedObject instanceof DESPurchaseMarket) {
			featureSet.addAll(SpotMarketsPackage.eINSTANCE.getSpotMarket().getEAllStructuralFeatures());
			featureSet.addAll(SpotMarketsPackage.eINSTANCE.getDESPurchaseMarket().getEAllStructuralFeatures());
		} else if (pinnedNamedObject instanceof DESSalesMarket) {
			featureSet.addAll(SpotMarketsPackage.eINSTANCE.getSpotMarket().getEAllStructuralFeatures());
			featureSet.addAll(SpotMarketsPackage.eINSTANCE.getDESSalesMarket().getEAllStructuralFeatures());
		} else if (pinnedNamedObject instanceof FOBPurchasesMarket) {
			featureSet.addAll(SpotMarketsPackage.eINSTANCE.getSpotMarket().getEAllStructuralFeatures());
			featureSet.addAll(SpotMarketsPackage.eINSTANCE.getFOBPurchasesMarket().getEAllStructuralFeatures());
		} else if (pinnedNamedObject instanceof FOBSalesMarket) {
			featureSet.addAll(SpotMarketsPackage.eINSTANCE.getSpotMarket().getEAllStructuralFeatures());
			featureSet.addAll(SpotMarketsPackage.eINSTANCE.getFOBSalesMarket().getEAllStructuralFeatures());
		}

		final List<EStructuralFeature> featureList = new ArrayList<>(featureSet);
		childrenList.addAll(ScenarioDiffUtil.diffDataGen(pinnedNamedObject, nonPinnedNamedObject, featureList, blackList1));

		List<EObject> l1 = new ArrayList<>();
		List<EObject> l2 = new ArrayList<>();
		if (pinnedNamedObject instanceof LoadSlot loadSlot) {
			l1 = loadSlot.getExtensions();
			l2 = ((LoadSlot) nonPinnedNamedObject).getExtensions();
		} else if (pinnedNamedObject instanceof DischargeSlot dischargeSlot) {
			l1 = dischargeSlot.getExtensions();
			l2 = ((DischargeSlot) nonPinnedNamedObject).getExtensions();
		}

		Map<EClass, EObject> map1 = new HashMap<>();
		Map<EClass, EObject> map2 = new HashMap<>();
		Set<EClass> classSet = new HashSet<>();
		for (EObject eo : l1) {
			map1.put(eo.eClass(), eo);
			classSet.add(eo.eClass());
		}
		for (EObject eo : l2) {
			map2.put(eo.eClass(), eo);
			classSet.add(eo.eClass());
		}

		List<EObject> tuple1 = new ArrayList<>();
		List<EObject> tuple2 = new ArrayList<>();
		List<EClass> tuple3 = new ArrayList<>();

		for (EClass c : classSet) {
			tuple3.add(c);
			if (!map1.containsKey(c)) {
				tuple1.add(null);
			} else {
				tuple1.add(map1.get(c));
			}
			if (!map2.containsKey(c)) {
				tuple2.add(null);
			} else {
				tuple2.add(map2.get(c));
			}
		}

		for (int i = 0; i < tuple3.size(); i++) {
			List<ScenarioDiffData> singleData = ScenarioDiffUtil.diffSingleDataGen(tuple1.get(i), tuple2.get(i), tuple3.get(i).getEAllStructuralFeatures(), blackList1);
			if (singleData != null && !singleData.isEmpty()) {
				childrenList.addAll(singleData);
			}
		}

		for (ScenarioDiffData sdd : childrenList) {
			result.children.add(sdd);
			sdd.parent = result;
		}
		if (result.children.isEmpty()) {
			return null;
		}
		return result;
	}

	protected Map<String, List<ScenarioDiffData>> getDiff(final LNGScenarioModel pinnedScenarioModel, final LNGScenarioModel nonPinnedScenarioModel) {

		Map<String, List<ScenarioDiffData>> res = new HashMap<>();
		res.put(KEY_TRADES, new ArrayList<>());
		res.put(KEY_VESSELS, new ArrayList<>());
		res.put(KEY_PORTS, new ArrayList<>());
		res.put(KEY_CHARTER_IN_MARKETS, new ArrayList<>());
		res.put(KEY_CHARTER_OUT_MARKETS, new ArrayList<>());

		res.put(KEY_VESSEL_EVENTS, new ArrayList<>());

		res.put(KEY_PURCHASE_CONTRACTS, new ArrayList<>());
		res.put(KEY_SALES_CONTRACTS, new ArrayList<>());

		res.put(KEY_DES_PURCHASE_MARKETS, new ArrayList<>());
		res.put(KEY_DES_SALE_MARKETS, new ArrayList<>());
		res.put(KEY_FOB_PURCHASE_MARKETS, new ArrayList<>());
		res.put(KEY_FOB_SALE_MARKETS, new ArrayList<>());

		if (pinnedScenarioModel != null && nonPinnedScenarioModel != null) {
			List<LoadSlot> pinnedLoadSlots = pinnedScenarioModel.getCargoModel().getLoadSlots();
			List<LoadSlot> nonPinnedLoadSlots = nonPinnedScenarioModel.getCargoModel().getLoadSlots();

			List<DischargeSlot> pinnedDischargeSlots = pinnedScenarioModel.getCargoModel().getDischargeSlots();
			List<DischargeSlot> nonPinnedDischargeSlots = nonPinnedScenarioModel.getCargoModel().getDischargeSlots();

			List<Vessel> pinnedVessels = pinnedScenarioModel.getReferenceModel().getFleetModel().getVessels();
			List<Vessel> nonPinnedVessels = nonPinnedScenarioModel.getReferenceModel().getFleetModel().getVessels();

			List<Port> pinnedPorts = pinnedScenarioModel.getReferenceModel().getPortModel().getPorts();
			List<Port> nonPinnedPorts = nonPinnedScenarioModel.getReferenceModel().getPortModel().getPorts();

			List<CharterInMarket> pinnedCharterInMarkets = pinnedScenarioModel.getReferenceModel().getSpotMarketsModel().getCharterInMarkets();
			List<CharterInMarket> nonPinnedCharterInMarkets = nonPinnedScenarioModel.getReferenceModel().getSpotMarketsModel().getCharterInMarkets();
			List<CharterOutMarket> pinnedCharterOutMarkets = pinnedScenarioModel.getReferenceModel().getSpotMarketsModel().getCharterOutMarkets();
			List<CharterOutMarket> nonPinnedCharterOutMarkets = nonPinnedScenarioModel.getReferenceModel().getSpotMarketsModel().getCharterOutMarkets();

			List<VesselEvent> pinnedVesselEvents = pinnedScenarioModel.getCargoModel().getVesselEvents();
			List<VesselEvent> nonPinnedVesselEvents = nonPinnedScenarioModel.getCargoModel().getVesselEvents();

			List<PurchaseContract> pinnedPurchaseContracts = pinnedScenarioModel.getReferenceModel().getCommercialModel().getPurchaseContracts();
			List<PurchaseContract> nonPinnedPurchaseContracts = nonPinnedScenarioModel.getReferenceModel().getCommercialModel().getPurchaseContracts();
			List<SalesContract> pinnedSalesContracts = pinnedScenarioModel.getReferenceModel().getCommercialModel().getSalesContracts();
			List<SalesContract> nonPinnedSalesContracts = nonPinnedScenarioModel.getReferenceModel().getCommercialModel().getSalesContracts();

			List<SpotMarket> pinnedDesPurchaseMarkets = pinnedScenarioModel.getReferenceModel().getSpotMarketsModel().getDesPurchaseSpotMarket().getMarkets();
			List<SpotMarket> pinnedDesSaleMarkets = pinnedScenarioModel.getReferenceModel().getSpotMarketsModel().getDesSalesSpotMarket().getMarkets();
			List<SpotMarket> pinnedFobPurchaseMarkets = pinnedScenarioModel.getReferenceModel().getSpotMarketsModel().getFobPurchasesSpotMarket().getMarkets();
			List<SpotMarket> pinnedFobSaleMarkets = pinnedScenarioModel.getReferenceModel().getSpotMarketsModel().getFobSalesSpotMarket().getMarkets();
			List<SpotMarket> nonPinnedDesPurchaseMarkets = nonPinnedScenarioModel.getReferenceModel().getSpotMarketsModel().getDesPurchaseSpotMarket().getMarkets();
			List<SpotMarket> nonPinnedDesSaleMarkets = nonPinnedScenarioModel.getReferenceModel().getSpotMarketsModel().getDesSalesSpotMarket().getMarkets();
			List<SpotMarket> nonPinnedFobPurchaseMarkets = nonPinnedScenarioModel.getReferenceModel().getSpotMarketsModel().getFobPurchasesSpotMarket().getMarkets();
			List<SpotMarket> nonPinnedFobSaleMarkets = nonPinnedScenarioModel.getReferenceModel().getSpotMarketsModel().getFobSalesSpotMarket().getMarkets();

			populateDiffData(res, KEY_TRADES, pinnedLoadSlots, nonPinnedLoadSlots);
			populateDiffData(res, KEY_TRADES, pinnedDischargeSlots, nonPinnedDischargeSlots);
			populateDiffData(res, KEY_VESSELS, pinnedVessels, nonPinnedVessels);
			populateDiffData(res, KEY_VESSEL_EVENTS, pinnedVesselEvents, nonPinnedVesselEvents);
			populateDiffData(res, KEY_CHARTER_IN_MARKETS, pinnedCharterInMarkets, nonPinnedCharterInMarkets);
			populateDiffData(res, KEY_CHARTER_OUT_MARKETS, pinnedCharterOutMarkets, nonPinnedCharterOutMarkets);
			populateDiffData(res, KEY_PURCHASE_CONTRACTS, pinnedPurchaseContracts, nonPinnedPurchaseContracts);
			populateDiffData(res, KEY_SALES_CONTRACTS, pinnedSalesContracts, nonPinnedSalesContracts);
			populateDiffData(res, KEY_PORTS, pinnedPorts, nonPinnedPorts);
			populateDiffData(res, KEY_DES_PURCHASE_MARKETS, pinnedDesPurchaseMarkets, nonPinnedDesPurchaseMarkets);
			populateDiffData(res, KEY_DES_SALE_MARKETS, pinnedDesSaleMarkets, nonPinnedDesSaleMarkets);
			populateDiffData(res, KEY_FOB_PURCHASE_MARKETS, pinnedFobPurchaseMarkets, nonPinnedFobPurchaseMarkets);
			populateDiffData(res, KEY_FOB_SALE_MARKETS, pinnedFobSaleMarkets, nonPinnedFobSaleMarkets);

		}
		return res;
	}

	private <R extends NamedObject> void populateDiffData(@NonNull final Map<String, List<ScenarioDiffData>> diffData, @NonNull final String diffDataKey, @NonNull final List<R> pinnedNamedObjects,
			@NonNull final List<R> nonPinnedNamedObjects) {
		for (final R pinnedNamedObject : pinnedNamedObjects) {
			final String pinnedName = pinnedNamedObject.getName();
			final Optional<R> optNonPinnedNamedObject = nonPinnedNamedObjects.stream() //
					.filter(nonPinnedNamedObject -> nonPinnedNamedObject.getName().equals(pinnedName)) //
					.findFirst();
			if (optNonPinnedNamedObject.isEmpty()) {
				final Map<String, String> parentMap = new HashMap<>();
				parentMap.put(KEY_PINNED, VALUE_PRESENT);
				parentMap.put(KEY_NON_PINNED, VALUE_NOT_APPLICABLE);
				final ScenarioDiffData localDiffData = new ScenarioDiffData(parentMap, pinnedName);
				diffData.get(diffDataKey).add(localDiffData);
			}
		}
		for (final R nonPinnedNamedObject : nonPinnedNamedObjects) {
			final String nonPinnedName = nonPinnedNamedObject.getName();
			final Optional<R> optPinnedNamedObject = pinnedNamedObjects.stream() //
					.filter(pinnedNamedObject -> pinnedNamedObject.getName().equals(nonPinnedName)) //
					.findFirst();
			if (optPinnedNamedObject.isEmpty()) {
				final Map<String, String> parentMap = new HashMap<>();
				parentMap.put(KEY_PINNED, VALUE_NOT_APPLICABLE);
				parentMap.put(KEY_NON_PINNED, VALUE_PRESENT);
				final ScenarioDiffData localDiffData = new ScenarioDiffData(parentMap, nonPinnedName);
				diffData.get(diffDataKey).add(localDiffData);
			}
		}
		for (final R pinnedNamedObject : pinnedNamedObjects) {
			final String pinnedName = pinnedNamedObject.getName();
			final Optional<R> optNonPinnedNamedObject = nonPinnedNamedObjects.stream() //
					.filter(nonPinnedNamedObject -> nonPinnedNamedObject.getName().equals(pinnedName)) //
					.findFirst();
			if (optNonPinnedNamedObject.isPresent() && !isJSONEqual(pinnedNamedObject, optNonPinnedNamedObject.get())) {
				final ScenarioDiffData localDiffData = findDiffs(pinnedNamedObject, optNonPinnedNamedObject.get());
				if (localDiffData != null) {
					diffData.get(diffDataKey).add(localDiffData);
				}
			}
		}
	}

	private final ObjectMapper mapper = new ObjectMapper();

	private String filterIDs(String o1Str) {
		StringBuilder filtered = new StringBuilder();
		String[] lines = o1Str.split(",");
		for (String line : lines) {
			if (line.contains("\"uuid\"") || line.contains("@lookupID") || line.contains("globalId")) {
				continue;
			}
			filtered.append(line);
		}
		return filtered.toString();
	}

	private boolean isJSONEqual(EObject o1, EObject o2) {
		if (o1 == o2) {
			return true;
		}
		if ((o1 == null && o2 != null) //
				|| (o1 != null && o2 == null)) {
			return false;
		}
		mapper.registerModule(new EMFJacksonModule());
		try {
			String o1Str = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(o1);
			o1Str = filterIDs(o1Str);
			String o2Str = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(o2);
			o2Str = filterIDs(o2Str);
			return o1Str.equals(o2Str);
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
		return false;
	}

	public final Set<String> overallIncomeMonths = new HashSet<>();

	protected AbstractScenarioDiff(final String helpContextId) {
		this.helpContextId = helpContextId;
		overallIncomeMonths.clear();
		overallIncomeMonths.add(new String(KEY_PINNED));
		overallIncomeMonths.add(new String(""));
	}

	protected SimpleTabularReportContentProvider createContentProvider() {

		return new SimpleTabularReportContentProvider() {

			@Override
			public Object getParent(final Object element) {
				if (element instanceof final ScenarioDiffData scenarioDiffData) {
					return scenarioDiffData.parent;
				}
				return super.getParent(element);
			}

			@Override
			public Object[] getChildren(final Object parentElement) {
				if (parentElement instanceof final ScenarioDiffData scenarioDiffData) {
					return scenarioDiffData.children.toArray();
				}
				return super.getChildren(parentElement);
			}

			@Override
			public boolean hasChildren(final Object element) {
				if (element instanceof final ScenarioDiffData scenarioDiffData) {
					return !scenarioDiffData.children.isEmpty();
				}
				return super.hasChildren(element);
			}

		};
	}

	protected AbstractSimpleTabularReportTransformer<ScenarioDiffData> createTransformer() {
		return new AbstractSimpleTabularReportTransformer<ScenarioDiffData>() {

			@Override
			public List<ColumnManager<ScenarioDiffData>> getColumnManagers(@NonNull final ISelectedDataProvider selectedDataProvider) {
				final ArrayList<ColumnManager<ScenarioDiffData>> result = new ArrayList<>();

				if (selectedDataProvider.getAllScenarioResults().size() > 1) {
					// "+"
					result.add(new ColumnManager<ScenarioDiffData>("") {
						@Override
						public Color getForeground(final ScenarioDiffData element) {
							return null;
						}

						@Override
						public String getColumnText(final ScenarioDiffData data) {
							return data.id;
						}

						@Override
						public Image getColumnImage(ScenarioDiffData data) {
							return null;
						}

						@Override
						public int compare(final ScenarioDiffData o1, final ScenarioDiffData o2) {
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
							return true;
						}
					});
				}

				for (String sn : overallIncomeMonths) {
					result.add(createMonthColumn(sn));
				}

				return result;
			}

			private ColumnManager<ScenarioDiffData> createMonthColumn(final String date) {
				return new ColumnManager<ScenarioDiffData>(date) {
					@Override
					public String getColumnText(final ScenarioDiffData data) {
						return data.valuesByMonth.containsKey(date) ? data.valuesByMonth.get(date) : "";
					}

					@Override
					public int compare(final ScenarioDiffData o1, final ScenarioDiffData o2) {
						return 0;
					}

					@Override
					public Color getForeground(final ScenarioDiffData element) {
						return null;
					}
				};
			}

			@Override
			public List<ScenarioDiffData> createData(@Nullable final Pair<@NonNull Schedule, @NonNull ScenarioResult> pinnedPair,
					@NonNull final List<@NonNull Pair<@NonNull Schedule, @NonNull ScenarioResult>> otherPairs) {
				resetData();
				final List<ScenarioDiffData> parentRes = new ArrayList<>();
				{
					if (pinnedPair != null && otherPairs != null && !otherPairs.isEmpty() && pinnedPair.getSecond() != null && otherPairs.get(0).getSecond() != null) {

						final ScenarioResult pinnedScenarioResult = pinnedPair.getSecond();
						final ScenarioResult nonPinnedScenarioResult = otherPairs.get(0).getSecond();

						final LNGScenarioModel pinnedScenarioModel = pinnedScenarioResult.getScenarioDataProvider().getTypedScenario(LNGScenarioModel.class);
						final LNGScenarioModel nonPinnedScenarioModel = nonPinnedScenarioResult.getScenarioDataProvider().getTypedScenario(LNGScenarioModel.class);

						final Map<String, List<ScenarioDiffData>> res1 = getDiff(pinnedScenarioModel, nonPinnedScenarioModel);

						if (!res1.get(KEY_TRADES).isEmpty()) {
							Map<String, String> parentMap = new HashMap<>();
							parentMap.put(KEY_PINNED, "");
							parentMap.put(KEY_NON_PINNED, "");
							ScenarioDiffData result = new ScenarioDiffData(parentMap, "Trades");
							for (ScenarioDiffData sdd : res1.get(KEY_TRADES)) {
								result.children.add(sdd);
								sdd.parent = result;
							}
							parentRes.add(result);
						}
						if (!res1.get(KEY_VESSELS).isEmpty()) {
							Map<String, String> parentMap = new HashMap<>();
							parentMap.put(KEY_PINNED, "");
							parentMap.put(KEY_NON_PINNED, "");
							ScenarioDiffData result = new ScenarioDiffData(parentMap, "Vessels");
							for (ScenarioDiffData sdd : res1.get(KEY_VESSELS)) {
								result.children.add(sdd);
								sdd.parent = result;
							}
							parentRes.add(result);
						}
						if (!res1.get(KEY_VESSEL_EVENTS).isEmpty()) {
							Map<String, String> parentMap = new HashMap<>();
							parentMap.put(KEY_PINNED, "");
							parentMap.put(KEY_NON_PINNED, "");
							ScenarioDiffData result = new ScenarioDiffData(parentMap, "Vessel Events");
							for (ScenarioDiffData sdd : res1.get(KEY_VESSEL_EVENTS)) {
								result.children.add(sdd);
								sdd.parent = result;
							}
							parentRes.add(result);
						}
						if (!res1.get(KEY_CHARTER_IN_MARKETS).isEmpty()) {
							Map<String, String> parentMap = new HashMap<>();
							parentMap.put(KEY_PINNED, "");
							parentMap.put(KEY_NON_PINNED, "");
							ScenarioDiffData result = new ScenarioDiffData(parentMap, "Charter In Markets");
							for (ScenarioDiffData sdd : res1.get(KEY_CHARTER_IN_MARKETS)) {
								result.children.add(sdd);
								sdd.parent = result;
							}
							parentRes.add(result);
						}
						if (!res1.get(KEY_CHARTER_OUT_MARKETS).isEmpty()) {
							Map<String, String> parentMap = new HashMap<>();
							parentMap.put(KEY_PINNED, "");
							parentMap.put(KEY_NON_PINNED, "");
							ScenarioDiffData result = new ScenarioDiffData(parentMap, "Charter Out Markets");
							for (ScenarioDiffData sdd : res1.get(KEY_CHARTER_OUT_MARKETS)) {
								result.children.add(sdd);
								sdd.parent = result;
							}
							parentRes.add(result);
						}
						if (!res1.get(KEY_PORTS).isEmpty()) {
							Map<String, String> parentMap = new HashMap<>();
							parentMap.put(KEY_PINNED, "");
							parentMap.put(KEY_NON_PINNED, "");
							ScenarioDiffData result = new ScenarioDiffData(parentMap, "Ports");
							for (ScenarioDiffData sdd : res1.get(KEY_PORTS)) {
								result.children.add(sdd);
								sdd.parent = result;
							}
							parentRes.add(result);
						}
						if (!res1.get(KEY_PURCHASE_CONTRACTS).isEmpty() || !res1.get(KEY_SALES_CONTRACTS).isEmpty()) {

							Map<String, String> parentMap = new HashMap<>();
							parentMap.put(KEY_PINNED, "");
							parentMap.put(KEY_NON_PINNED, "");
							ScenarioDiffData result = new ScenarioDiffData(parentMap, "Commercial");
							if (!res1.get(KEY_PURCHASE_CONTRACTS).isEmpty()) {
								Map<String, String> sub_parentMap = new HashMap<>();
								sub_parentMap.put(KEY_PINNED, "");
								sub_parentMap.put(KEY_NON_PINNED, "");
								ScenarioDiffData subResult = new ScenarioDiffData(parentMap, "Purchase Contracts");
								for (ScenarioDiffData sdd : res1.get(KEY_PURCHASE_CONTRACTS)) {
									subResult.children.add(sdd);
									sdd.parent = subResult;
								}
								result.children.add(subResult);
								subResult.parent = result;
							}
							if (!res1.get(KEY_SALES_CONTRACTS).isEmpty()) {
								Map<String, String> subParentMap = new HashMap<>();
								subParentMap.put(KEY_PINNED, "");
								subParentMap.put(KEY_NON_PINNED, "");
								ScenarioDiffData subResult = new ScenarioDiffData(parentMap, "Sales Contracts");
								for (ScenarioDiffData sdd : res1.get(KEY_SALES_CONTRACTS)) {
									subResult.children.add(sdd);
									sdd.parent = subResult;
								}
								result.children.add(subResult);
								subResult.parent = result;
							}
							parentRes.add(result);
						}

						if (!res1.get(KEY_FOB_PURCHASE_MARKETS).isEmpty() //
								|| !res1.get(KEY_FOB_SALE_MARKETS).isEmpty() //
								|| !res1.get(KEY_DES_PURCHASE_MARKETS).isEmpty() //
								|| !res1.get(KEY_DES_SALE_MARKETS).isEmpty()) {
							Map<String, String> parentMap = new HashMap<>();
							parentMap.put(KEY_PINNED, "");
							parentMap.put(KEY_NON_PINNED, "");
							ScenarioDiffData result = new ScenarioDiffData(parentMap, "Cargo Markets");
							if (!res1.get(KEY_FOB_PURCHASE_MARKETS).isEmpty()) {
								Map<String, String> subParentMap = new HashMap<>();
								subParentMap.put(KEY_PINNED, "");
								subParentMap.put(KEY_NON_PINNED, "");
								ScenarioDiffData subResult = new ScenarioDiffData(parentMap, "Fob Purchase");
								for (ScenarioDiffData sdd : res1.get(KEY_FOB_PURCHASE_MARKETS)) {
									subResult.children.add(sdd);
									sdd.parent = subResult;
								}
								result.children.add(subResult);
								subResult.parent = result;
							}
							if (!res1.get(KEY_FOB_SALE_MARKETS).isEmpty()) {
								Map<String, String> subParentMap = new HashMap<>();
								subParentMap.put(KEY_PINNED, "");
								subParentMap.put(KEY_NON_PINNED, "");
								ScenarioDiffData subResult = new ScenarioDiffData(parentMap, "Fob Sales");
								for (ScenarioDiffData sdd : res1.get(KEY_FOB_SALE_MARKETS)) {
									subResult.children.add(sdd);
									sdd.parent = subResult;
								}
								result.children.add(subResult);
								subResult.parent = result;
							}
							if (!res1.get(KEY_DES_PURCHASE_MARKETS).isEmpty()) {
								Map<String, String> subParentMap = new HashMap<>();
								subParentMap.put(KEY_PINNED, "");
								subParentMap.put(KEY_NON_PINNED, "");
								ScenarioDiffData subResult = new ScenarioDiffData(parentMap, "Des Purchase");
								for (ScenarioDiffData sdd : res1.get(KEY_DES_PURCHASE_MARKETS)) {
									subResult.children.add(sdd);
									sdd.parent = subResult;
								}
								result.children.add(subResult);
								subResult.parent = result;
							}
							if (!res1.get(KEY_DES_SALE_MARKETS).isEmpty()) {
								Map<String, String> subParentMap = new HashMap<>();
								subParentMap.put(KEY_PINNED, "");
								subParentMap.put(KEY_NON_PINNED, "");
								ScenarioDiffData subResult = new ScenarioDiffData(parentMap, "Des Sales");
								for (ScenarioDiffData sdd : res1.get(KEY_DES_SALE_MARKETS)) {
									subResult.children.add(sdd);
									sdd.parent = subResult;
								}
								result.children.add(subResult);
								subResult.parent = result;
							}

							parentRes.add(result);
						}

					}

				}

				return parentRes;
			}

		};
	}

	protected abstract Collection<T> getSubTypes();

	protected abstract void resetData();

	protected abstract T getSubType(DischargeSlot dischargeSlot);

	protected abstract int doCompare(final ScenarioDiffData o1, final ScenarioDiffData o2);

	protected abstract String doGetColumnText(final ScenarioDiffData data);

	@Override
	public <U> U getAdapter(final Class<U> adapter) {
		if (IReportContentsGenerator.class.isAssignableFrom(adapter)) {
			return adapter.cast(ReportContentsGenerators.createJSONFor(selectedScenariosServiceListener, viewer.getGrid()));
		}
		return super.getAdapter(adapter);
	}

}