/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2022
 * All rights reserved.
 */
package com.mmxlabs.lingo.reports.views.scenarioDiff;

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
import org.eclipse.emf.ecore.ENamedElement;
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
import com.mmxlabs.rcp.common.actions.CopyGridToHtmlClipboardAction;
import com.mmxlabs.rcp.common.actions.PackGridTreeColumnsAction;
import com.mmxlabs.rcp.common.json.EMFJacksonModule;
import com.mmxlabs.rcp.icons.lingo.CommonImages;
import com.mmxlabs.rcp.icons.lingo.CommonImages.IconMode;
import com.mmxlabs.rcp.icons.lingo.CommonImages.IconPaths;
import com.mmxlabs.scenario.service.ScenarioResult;

public abstract class AbstractScenarioDiff<T> extends ViewPart {

	private ScenarioComparisonService selectedScenariosService;

	private final ArrayList<ColumnManager<T>> sortColumns = new ArrayList<>();
	protected final ArrayList<ColumnManager<T>> columnManagers = new ArrayList<>();

	private boolean inverseSort = false;

	protected GridTreeViewer viewer;

	private Action packColumnsAction;

	private Action copyTableAction;

	private final String helpContextId;

	protected boolean pinnedMode = false;

	protected Image pinImage;

	@NonNull
	protected final ISelectedScenariosServiceListener selectedScenariosServiceListener = new ISelectedScenariosServiceListener() {
		@Override
		public void selectedDataProviderChanged(@NonNull ISelectedDataProvider selectedDataProvider, boolean block) {

			final Runnable r = () -> {
				final AbstractSimpleTabularReportTransformer<T> transformer = (AbstractSimpleTabularReportTransformer<T>) createTransformer();

				columnManagers.clear();

				int numberOfSchedules = 0;
				Pair<Schedule, ScenarioResult> pinnedPair = null;
				List<Pair<Schedule, ScenarioResult>> otherPairs = new LinkedList<>();
				ScenarioResult pinned = selectedDataProvider.getPinnedScenarioResult();

				ScenarioResult other = null;
				if (selectedDataProvider.getOtherScenarioResults().size() > 0) {
					other = selectedDataProvider.getOtherScenarioResults().get(0);
				}

				pinnedPair = new Pair<>(null, pinned);
				otherPairs.add(new Pair<>(null, other));
				final Object[] expanded = viewer.getExpandedElements();
				final List<T> rowElements = transformer.createData(pinnedPair, otherPairs);

				columnManagers.addAll(transformer.getColumnManagers(selectedDataProvider));
				clearColumns();
				addColumns();
				setShowColumns(pinned != null, numberOfSchedules);

				viewer.getLabelProvider().dispose();
				viewer.setLabelProvider(new ViewLabelProvider());

				ViewerHelper.setInput(viewer, true, rowElements);

				if (!rowElements.isEmpty()) {
					if (packColumnsAction != null) {
						packColumnsAction.run();
					}
				}
			};
			ViewerHelper.runIfViewerValid(viewer, block, r);
		}

		@Override
		public void selectedObjectChanged(org.eclipse.e4.ui.model.application.ui.basic.MPart source, org.eclipse.jface.viewers.ISelection selection) {
			doSelectionChanged(source, selection);
		}

	};


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
			if (name.equals("pinned")) {
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

	private ScenarioDiffData findDiffs(EObject o, EObject ot) {
		Set<EStructuralFeature> blackList1 = new HashSet<>();
		blackList1.add(MMXCorePackage.eINSTANCE.getUUIDObject_Uuid());

		Map<String, String> parentMap = new HashMap<String, String>();

		parentMap.put("pinned", "");
		parentMap.put("", "");

		ScenarioDiffData result = new ScenarioDiffData(parentMap, ((NamedObject) o).getName());
		List<ScenarioDiffData> childrenList = new ArrayList<>();

		Set<EStructuralFeature> fs = new HashSet<>();
		
		if (o instanceof LoadSlot) {
			fs.addAll(CargoPackage.eINSTANCE.getLoadSlot().getEAllStructuralFeatures());
		}

		if (o instanceof DischargeSlot) {
			fs.addAll(CargoPackage.eINSTANCE.getDischargeSlot().getEAllStructuralFeatures());
		}
		if (o instanceof Vessel) {
			fs.addAll(FleetPackage.eINSTANCE.getVessel().getEAllStructuralFeatures());
		}
		if (o instanceof Port) {
			fs.addAll(PortPackage.eINSTANCE.getPort().getEAllStructuralFeatures());
			childrenList.addAll(ScenarioDiffUtil.extraDataGen(o, ot));
		}
		if (o instanceof CharterInMarket) {
			fs.addAll(SpotMarketsPackage.eINSTANCE.getCharterInMarket().getEAllStructuralFeatures());
			fs.addAll(SpotMarketsPackage.eINSTANCE.getSpotCharterMarket().getEAllStructuralFeatures());
		}
		if (o instanceof CharterOutMarket) {
			fs.addAll(SpotMarketsPackage.eINSTANCE.getCharterOutMarket().getEAllStructuralFeatures());
			fs.addAll(SpotMarketsPackage.eINSTANCE.getSpotCharterMarket().getEAllStructuralFeatures());
		}
		if (o instanceof CharterOutEvent) {
			fs.addAll(CargoPackage.eINSTANCE.getVesselEvent().getEAllStructuralFeatures());
			fs.addAll(CargoPackage.eINSTANCE.getCharterOutEvent().getEAllStructuralFeatures());
			childrenList.addAll(ScenarioDiffUtil.extraDataGen(o, ot));
		}
		if (o instanceof DryDockEvent) {
			fs.addAll(CargoPackage.eINSTANCE.getDryDockEvent().getEAllStructuralFeatures());
			fs.addAll(CargoPackage.eINSTANCE.getVesselEvent().getEAllStructuralFeatures());
			childrenList.addAll(ScenarioDiffUtil.extraDataGen(o, ot));
		}
		if (o instanceof MaintenanceEvent) {
			fs.addAll(CargoPackage.eINSTANCE.getMaintenanceEvent().getEAllStructuralFeatures());
			fs.addAll(CargoPackage.eINSTANCE.getVesselEvent().getEAllStructuralFeatures());
			childrenList.addAll(ScenarioDiffUtil.extraDataGen(o, ot));
		}

		if (o instanceof PurchaseContract) {
			fs.addAll(CommercialPackage.eINSTANCE.getPurchaseContract().getEAllStructuralFeatures());
			fs.addAll(CommercialPackage.eINSTANCE.getContract().getEAllStructuralFeatures());
		}
		if (o instanceof SalesContract) {
			fs.addAll(CommercialPackage.eINSTANCE.getSalesContract().getEAllStructuralFeatures());
			fs.addAll(CommercialPackage.eINSTANCE.getContract().getEAllStructuralFeatures());
		}

		if (o instanceof DESPurchaseMarket) {
			fs.addAll(SpotMarketsPackage.eINSTANCE.getSpotMarket().getEAllStructuralFeatures());
			fs.addAll(SpotMarketsPackage.eINSTANCE.getDESPurchaseMarket().getEAllStructuralFeatures());
		}
		if (o instanceof DESSalesMarket) {
			fs.addAll(SpotMarketsPackage.eINSTANCE.getSpotMarket().getEAllStructuralFeatures());
			fs.addAll(SpotMarketsPackage.eINSTANCE.getDESSalesMarket().getEAllStructuralFeatures());
		}
		if (o instanceof FOBPurchasesMarket) {
			fs.addAll(SpotMarketsPackage.eINSTANCE.getSpotMarket().getEAllStructuralFeatures());
			fs.addAll(SpotMarketsPackage.eINSTANCE.getFOBPurchasesMarket().getEAllStructuralFeatures());
		}
		if (o instanceof FOBSalesMarket) {
			fs.addAll(SpotMarketsPackage.eINSTANCE.getSpotMarket().getEAllStructuralFeatures());
			fs.addAll(SpotMarketsPackage.eINSTANCE.getFOBSalesMarket().getEAllStructuralFeatures());
		}

		List<EStructuralFeature> fs_list = new ArrayList<>();
		for (EStructuralFeature f : fs) {
			fs_list.add(f);
		}
		childrenList.addAll(ScenarioDiffUtil.diffDataGen(o, ot, fs_list, blackList1));

		List<EObject> L1 = new ArrayList<>();
		List<EObject> L2 = new ArrayList<>();
		if (o instanceof LoadSlot) {
			L1 = ((LoadSlot) o).getExtensions();
			L2 = ((LoadSlot) ot).getExtensions();
		}
		if (o instanceof DischargeSlot) {
			L1 = ((DischargeSlot) o).getExtensions();
			L2 = ((DischargeSlot) ot).getExtensions();
		}

		Map<EClass, EObject> map1 = new HashMap<>();
		Map<EClass, EObject> map2 = new HashMap<>();
		Set<EClass> classSet = new HashSet<>();
		for (EObject eo : L1) {
			map1.put(eo.eClass(), eo);
			classSet.add(eo.eClass());
		}
		for (EObject eo : L2) {
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
			if (singleData != null && singleData.size() > 0) {
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

	protected Map<String, List<ScenarioDiffData>> getDiff(List<ScenarioDiffData> output, LNGScenarioModel sm, LNGScenarioModel tm) {

		Map<String, List<ScenarioDiffData>> res = new HashMap<>();
		res.put("Trades", new ArrayList<>());
		res.put("Vessels", new ArrayList<>());
		res.put("Ports", new ArrayList<>());
		res.put("CharterInMarkets", new ArrayList<>());
		res.put("CharterOutMarkets", new ArrayList<>());

		res.put("VesselEvents", new ArrayList<>());

		res.put("PurchaseContracts", new ArrayList<>());
		res.put("SalesContracts", new ArrayList<>());

		res.put("Cargo_Markets_Des_Purchase", new ArrayList<>());
		res.put("Cargo_Markets_Des_Sales", new ArrayList<>());
		res.put("Cargo_Markets_Fob_Purchase", new ArrayList<>());
		res.put("Cargo_Markets_Fob_Sales", new ArrayList<>());

		if (sm != null && tm != null) {
			List<LoadSlot> emfObjectsS = sm.getCargoModel().getLoadSlots();
			List<LoadSlot> emfObjectsT = tm.getCargoModel().getLoadSlots();

			List<DischargeSlot> emfObjectsS_discharge = sm.getCargoModel().getDischargeSlots();
			List<DischargeSlot> emfObjectsT_discharge = tm.getCargoModel().getDischargeSlots();

			List<Vessel> s_vessel = sm.getReferenceModel().getFleetModel().getVessels();
			List<Vessel> t_vessel = tm.getReferenceModel().getFleetModel().getVessels();

			List<Port> s_port = sm.getReferenceModel().getPortModel().getPorts();
			List<Port> t_port = tm.getReferenceModel().getPortModel().getPorts();

			List<CharterInMarket> s_charter_in = sm.getReferenceModel().getSpotMarketsModel().getCharterInMarkets();
			List<CharterInMarket> t_charter_in = tm.getReferenceModel().getSpotMarketsModel().getCharterInMarkets();
			List<CharterOutMarket> s_charter_out = sm.getReferenceModel().getSpotMarketsModel().getCharterOutMarkets();
			List<CharterOutMarket> t_charter_out = tm.getReferenceModel().getSpotMarketsModel().getCharterOutMarkets();

			List<VesselEvent> s_vessel_events = sm.getCargoModel().getVesselEvents();
			List<VesselEvent> t_vessel_events = tm.getCargoModel().getVesselEvents();

			List<PurchaseContract> s_purchase_contract = sm.getReferenceModel().getCommercialModel().getPurchaseContracts();
			List<PurchaseContract> t_purchase_contract = tm.getReferenceModel().getCommercialModel().getPurchaseContracts();
			List<SalesContract> s_sales_contract = sm.getReferenceModel().getCommercialModel().getSalesContracts();
			List<SalesContract> t_sales_contract = tm.getReferenceModel().getCommercialModel().getSalesContracts();

			List<SpotMarket> s_des_purchase = sm.getReferenceModel().getSpotMarketsModel().getDesPurchaseSpotMarket().getMarkets();
			List<SpotMarket> s_des_sales = sm.getReferenceModel().getSpotMarketsModel().getDesSalesSpotMarket().getMarkets();
			List<SpotMarket> s_fob_purchase = sm.getReferenceModel().getSpotMarketsModel().getFobPurchasesSpotMarket().getMarkets();
			List<SpotMarket> s_fob_sales = sm.getReferenceModel().getSpotMarketsModel().getFobSalesSpotMarket().getMarkets();
			List<SpotMarket> t_des_purchase = tm.getReferenceModel().getSpotMarketsModel().getDesPurchaseSpotMarket().getMarkets();
			List<SpotMarket> t_des_sales = tm.getReferenceModel().getSpotMarketsModel().getDesSalesSpotMarket().getMarkets();
			List<SpotMarket> t_fob_purchase = tm.getReferenceModel().getSpotMarketsModel().getFobPurchasesSpotMarket().getMarkets();
			List<SpotMarket> t_fob_sales = tm.getReferenceModel().getSpotMarketsModel().getFobSalesSpotMarket().getMarkets();


			
			for (LoadSlot o : emfObjectsS) {
				String name = o.getName();
				Optional<LoadSlot> oT = emfObjectsT.stream().filter(ot -> ot.getName().equals(name)).findFirst();
				if (!oT.isPresent()) {
					Map<String, String> parentMap = new HashMap<String, String>();
					parentMap.put("pinned", "Present");
					parentMap.put("", "NA");
					ScenarioDiffData instance = new ScenarioDiffData(parentMap, o.getName());
					output.add(instance);
					res.get("Trades").add(instance);
				}
			}
			for (LoadSlot o : emfObjectsT) {
				String name = o.getName();
				Optional<LoadSlot> oT = emfObjectsS.stream().filter(ot -> ot.getName().equals(name)).findFirst();
				if (!oT.isPresent()) {
					Map<String, String> parentMap = new HashMap<String, String>();
					parentMap.put("pinned", "NA");
					parentMap.put("", "Present");
					ScenarioDiffData instance = new ScenarioDiffData(parentMap, o.getName());
					output.add(instance);
					res.get("Trades").add(instance);
				}
			}
			for (DischargeSlot o : emfObjectsS_discharge) {
				String name = o.getName();
				Optional<DischargeSlot> oT = emfObjectsT_discharge.stream().filter(ot -> ot.getName().equals(name)).findFirst();
				if (!oT.isPresent()) {
					Map<String, String> parentMap = new HashMap<String, String>();
					parentMap.put("pinned", "Present");
					parentMap.put("", "NA");
					ScenarioDiffData instance = new ScenarioDiffData(parentMap, o.getName());
					output.add(instance);
					res.get("Trades").add(instance);
				}
			}
			for (DischargeSlot o : emfObjectsT_discharge) {
				String name = o.getName();
				Optional<DischargeSlot> oT = emfObjectsS_discharge.stream().filter(ot -> ot.getName().equals(name)).findFirst();
				if (!oT.isPresent()) {
					Map<String, String> parentMap = new HashMap<String, String>();
					parentMap.put("pinned", "NA");
					parentMap.put("", "Present");
					ScenarioDiffData instance = new ScenarioDiffData(parentMap, o.getName());
					output.add(instance);
					res.get("Trades").add(instance);
				}
			}

			for (Vessel o : s_vessel) {
				String name = o.getName();
				Optional<Vessel> oT = t_vessel.stream().filter(ot -> ot.getName().equals(name)).findFirst();
				if (!oT.isPresent()) {
					Map<String, String> parentMap = new HashMap<String, String>();
					parentMap.put("pinned", "Present");
					parentMap.put("", "NA");
					ScenarioDiffData instance = new ScenarioDiffData(parentMap, o.getName());
					output.add(instance);
					res.get("Vessels").add(instance);
				}
			}
			for (Vessel o : t_vessel) {
				String name = o.getName();
				Optional<Vessel> oT = s_vessel.stream().filter(ot -> ot.getName().equals(name)).findFirst();
				if (!oT.isPresent()) {
					Map<String, String> parentMap = new HashMap<String, String>();
					parentMap.put("pinned", "NA");
					parentMap.put("", "Present");
					ScenarioDiffData instance = new ScenarioDiffData(parentMap, o.getName());
					output.add(instance);
					res.get("Vessels").add(instance);
				}
			}
			for (VesselEvent o : s_vessel_events) {
				String name = o.getName();
				Optional<VesselEvent> oT = t_vessel_events.stream().filter(ot -> ot.getName().equals(name)).findFirst();
				if (!oT.isPresent()) {
					Map<String, String> parentMap = new HashMap<String, String>();
					parentMap.put("pinned", "Present");
					parentMap.put("", "NA");
					ScenarioDiffData instance = new ScenarioDiffData(parentMap, o.getName());
					output.add(instance);
					res.get("VesselEvents").add(instance);
				}
			}
			for (VesselEvent o : t_vessel_events) {
				String name = o.getName();
				Optional<VesselEvent> oT = s_vessel_events.stream().filter(ot -> ot.getName().equals(name)).findFirst();
				if (!oT.isPresent()) {
					Map<String, String> parentMap = new HashMap<String, String>();
					parentMap.put("pinned", "NA");
					parentMap.put("", "Present");
					ScenarioDiffData instance = new ScenarioDiffData(parentMap, o.getName());
					output.add(instance);
					res.get("VesselEvents").add(instance);
				}
			}
			for (CharterInMarket o : s_charter_in) {
				String name = o.getName();
				Optional<CharterInMarket> oT = t_charter_in.stream().filter(ot -> ot.getName().equals(name)).findFirst();
				if (!oT.isPresent()) {
					Map<String, String> parentMap = new HashMap<String, String>();
					parentMap.put("pinned", "Present");
					parentMap.put("", "NA");
					ScenarioDiffData instance = new ScenarioDiffData(parentMap, o.getName());
					output.add(instance);
					res.get("CharterInMarkets").add(instance);
				}
			}
			for (CharterInMarket o : t_charter_in) {
				String name = o.getName();
				Optional<CharterInMarket> oT = s_charter_in.stream().filter(ot -> ot.getName().equals(name)).findFirst();
				if (!oT.isPresent()) {
					Map<String, String> parentMap = new HashMap<String, String>();
					parentMap.put("pinned", "NA");
					parentMap.put("", "Present");
					ScenarioDiffData instance = new ScenarioDiffData(parentMap, o.getName());
					output.add(instance);
					res.get("CharterInMarkets").add(instance);
				}
			}
			for (CharterOutMarket o : s_charter_out) {
				String name = o.getName();
				Optional<CharterOutMarket> oT = t_charter_out.stream().filter(ot -> ot.getName().equals(name)).findFirst();
				if (!oT.isPresent()) {
					Map<String, String> parentMap = new HashMap<String, String>();
					parentMap.put("pinned", "Present");
					parentMap.put("", "NA");
					ScenarioDiffData instance = new ScenarioDiffData(parentMap, o.getName());
					output.add(instance);
					res.get("CharterOutMarkets").add(instance);
				}
			}
			for (CharterOutMarket o : t_charter_out) {
				String name = o.getName();
				Optional<CharterOutMarket> oT = s_charter_out.stream().filter(ot -> ot.getName().equals(name)).findFirst();
				if (!oT.isPresent()) {
					Map<String, String> parentMap = new HashMap<String, String>();
					parentMap.put("pinned", "NA");
					parentMap.put("", "Present");
					ScenarioDiffData instance = new ScenarioDiffData(parentMap, o.getName());
					output.add(instance);
					res.get("CharterOutMarkets").add(instance);
				}
			}
			for (PurchaseContract o : s_purchase_contract) {
				String name = o.getName();
				Optional<PurchaseContract> oT = t_purchase_contract.stream().filter(ot -> ot.getName().equals(name)).findFirst();
				if (!oT.isPresent()) {
					Map<String, String> parentMap = new HashMap<String, String>();
					parentMap.put("pinned", "Present");
					parentMap.put("", "NA");
					ScenarioDiffData instance = new ScenarioDiffData(parentMap, o.getName());
					output.add(instance);
					res.get("PurchaseContracts").add(instance);
				}
			}
			for (PurchaseContract o : t_purchase_contract) {
				String name = o.getName();
				Optional<PurchaseContract> oT = s_purchase_contract.stream().filter(ot -> ot.getName().equals(name)).findFirst();
				if (!oT.isPresent()) {
					Map<String, String> parentMap = new HashMap<String, String>();
					parentMap.put("pinned", "NA");
					parentMap.put("", "Present");
					ScenarioDiffData instance = new ScenarioDiffData(parentMap, o.getName());
					output.add(instance);
					res.get("PurchaseContracts").add(instance);
				}
			}
			for (SalesContract o : s_sales_contract) {
				String name = o.getName();
				Optional<SalesContract> oT = t_sales_contract.stream().filter(ot -> ot.getName().equals(name)).findFirst();
				if (!oT.isPresent()) {
					Map<String, String> parentMap = new HashMap<String, String>();
					parentMap.put("pinned", "Present");
					parentMap.put("", "NA");
					ScenarioDiffData instance = new ScenarioDiffData(parentMap, o.getName());
					output.add(instance);
					res.get("SalesContracts").add(instance);
				}
			}
			for (SalesContract o : t_sales_contract) {
				String name = o.getName();
				Optional<SalesContract> oT = s_sales_contract.stream().filter(ot -> ot.getName().equals(name)).findFirst();
				if (!oT.isPresent()) {
					Map<String, String> parentMap = new HashMap<String, String>();
					parentMap.put("pinned", "NA");
					parentMap.put("", "Present");
					ScenarioDiffData instance = new ScenarioDiffData(parentMap, o.getName());
					output.add(instance);
					res.get("SalesContracts").add(instance);
				}
			}

			for (Port o : s_port) {
				String uuid = o.getUuid();
				Optional<Port> oT = t_port.stream().filter(ot -> ot.getUuid().equals(uuid)).findFirst();
				if (!oT.isPresent()) {
					Map<String, String> parentMap = new HashMap<String, String>();
					parentMap.put("pinned", "Present");
					parentMap.put("", "NA");
					ScenarioDiffData instance = new ScenarioDiffData(parentMap, o.getName());
					output.add(instance);
					res.get("Ports").add(instance);
				}
			}
			for (Port o : t_port) {
				String uuid = o.getUuid();
				Optional<Port> oT = s_port.stream().filter(ot -> ot.getUuid().equals(uuid)).findFirst();
				if (!oT.isPresent()) {
					Map<String, String> parentMap = new HashMap<String, String>();
					parentMap.put("pinned", "Present");
					parentMap.put("", "NA");
					ScenarioDiffData instance = new ScenarioDiffData(parentMap, o.getName());
					output.add(instance);
					res.get("Ports").add(instance);
				}
			}

			for (SpotMarket o : s_des_purchase) {
				String name = o.getName();
				Optional<SpotMarket> oT = t_des_purchase.stream().filter(ot -> ot.getName().equals(name)).findFirst();
				if (!oT.isPresent()) {
					Map<String, String> parentMap = new HashMap<String, String>();
					parentMap.put("pinned", "Present");
					parentMap.put("", "NA");
					ScenarioDiffData instance = new ScenarioDiffData(parentMap, o.getName());
					output.add(instance);
					res.get("Cargo_Markets_Des_Purchase").add(instance);
				}
			}
			for (SpotMarket o : t_des_purchase) {
				String name = o.getName();
				Optional<SpotMarket> oT = s_des_purchase.stream().filter(ot -> ot.getName().equals(name)).findFirst();
				if (!oT.isPresent()) {
					Map<String, String> parentMap = new HashMap<String, String>();
					parentMap.put("pinned", "NA");
					parentMap.put("", "Present");
					ScenarioDiffData instance = new ScenarioDiffData(parentMap, o.getName());
					output.add(instance);
					res.get("Cargo_Markets_Des_Purchase").add(instance);
				}
			}
			for (SpotMarket o : s_des_sales) {
				String name = o.getName();
				Optional<SpotMarket> oT = t_des_sales.stream().filter(ot -> ot.getName().equals(name)).findFirst();
				if (!oT.isPresent()) {
					Map<String, String> parentMap = new HashMap<String, String>();
					parentMap.put("pinned", "Present");
					parentMap.put("", "NA");
					ScenarioDiffData instance = new ScenarioDiffData(parentMap, o.getName());
					output.add(instance);
					res.get("Cargo_Markets_Des_Sales").add(instance);
				}
			}
			for (SpotMarket o : t_des_sales) {
				String name = o.getName();
				Optional<SpotMarket> oT = s_des_sales.stream().filter(ot -> ot.getName().equals(name)).findFirst();
				if (!oT.isPresent()) {
					Map<String, String> parentMap = new HashMap<String, String>();
					parentMap.put("pinned", "NA");
					parentMap.put("", "Present");
					ScenarioDiffData instance = new ScenarioDiffData(parentMap, o.getName());
					output.add(instance);
					res.get("Cargo_Markets_Des_Sales").add(instance);
				}
			}
			for (SpotMarket o : s_fob_purchase) {
				String name = o.getName();
				Optional<SpotMarket> oT = t_fob_purchase.stream().filter(ot -> ot.getName().equals(name)).findFirst();
				if (!oT.isPresent()) {
					Map<String, String> parentMap = new HashMap<String, String>();
					parentMap.put("pinned", "Present");
					parentMap.put("", "NA");
					ScenarioDiffData instance = new ScenarioDiffData(parentMap, o.getName());
					output.add(instance);
					res.get("Cargo_Markets_Fob_Purchase").add(instance);
				}
			}
			for (SpotMarket o : t_fob_purchase) {
				String name = o.getName();
				Optional<SpotMarket> oT = s_fob_purchase.stream().filter(ot -> ot.getName().equals(name)).findFirst();
				if (!oT.isPresent()) {
					Map<String, String> parentMap = new HashMap<String, String>();
					parentMap.put("pinned", "NA");
					parentMap.put("", "Present");
					ScenarioDiffData instance = new ScenarioDiffData(parentMap, o.getName());
					output.add(instance);
					res.get("Cargo_Markets_Fob_Purchase").add(instance);
				}
			}
			for (SpotMarket o : s_fob_sales) {
				String name = o.getName();
				Optional<SpotMarket> oT = t_fob_sales.stream().filter(ot -> ot.getName().equals(name)).findFirst();
				if (!oT.isPresent()) {
					Map<String, String> parentMap = new HashMap<String, String>();
					parentMap.put("pinned", "Present");
					parentMap.put("", "NA");
					ScenarioDiffData instance = new ScenarioDiffData(parentMap, o.getName());
					output.add(instance);
					res.get("Cargo_Markets_Fob_Sales").add(instance);
				}
			}
			for (SpotMarket o : t_fob_sales) {
				String name = o.getName();
				Optional<SpotMarket> oT = s_fob_sales.stream().filter(ot -> ot.getName().equals(name)).findFirst();
				if (!oT.isPresent()) {
					Map<String, String> parentMap = new HashMap<String, String>();
					parentMap.put("pinned", "NA");
					parentMap.put("", "Present");
					ScenarioDiffData instance = new ScenarioDiffData(parentMap, o.getName());
					output.add(instance);
					res.get("Cargo_Markets_Fob_Sales").add(instance);
				}
			}

			for (LoadSlot o : emfObjectsS) {

				String name = o.getName();
				Optional<LoadSlot> oT = emfObjectsT.stream().filter(ot -> ot.getName().equals(name)).findFirst();

				if (oT.isPresent()) {
					LoadSlot ot = oT.get();
					if (!isJSONEqual(o, oT.get())) {
						ScenarioDiffData instance = findDiffs(o, oT.get());
						if (instance != null) {
							output.add(instance);
							res.get("Trades").add(instance);
						}
					} else {

					}
				}
			}

			for (DischargeSlot o : emfObjectsS_discharge) {
				String name = o.getName();
				Optional<DischargeSlot> oT = emfObjectsT_discharge.stream().filter(ot -> ot.getName().equals(name)).findFirst();

				if (oT.isPresent()) {
					DischargeSlot ot = oT.get();
					if (!isJSONEqual(o, oT.get())) {
						ScenarioDiffData instance = findDiffs(o, oT.get());
						if (instance != null) {
							output.add(instance);
							res.get("Trades").add(instance);
						}
					} else {

					}
				}
			}

			for (Vessel o : s_vessel) {

				String name = o.getName();
				Optional<Vessel> oT = t_vessel.stream().filter(ot -> ot.getName().equals(name)).findFirst();

				if (oT.isPresent()) {
					Vessel ot = oT.get();
					if (!isJSONEqual(o, oT.get())) {
						ScenarioDiffData instance = findDiffs(o, oT.get());
						if (instance == null) {

						}
						if (instance != null) {
							output.add(instance);
							res.get("Vessels").add(instance);
						}
					} else {

					}
				}
			}

			for (VesselEvent o : s_vessel_events) {

				String name = o.getName();
				Optional<VesselEvent> oT = t_vessel_events.stream().filter(ot -> ot.getName().equals(name)).findFirst();

				if (oT.isPresent()) {
					VesselEvent ot = oT.get();
					if (!isJSONEqual(o, oT.get())) {
						ScenarioDiffData instance = findDiffs(o, oT.get());
						if (instance == null) {

						}
						if (instance != null) {
							output.add(instance);
							res.get("VesselEvents").add(instance);
						}
					} else {

					}
				}
			}
			for (CharterInMarket o : s_charter_in) {

				String name = o.getName();
				Optional<CharterInMarket> oT = t_charter_in.stream().filter(ot -> ot.getName().equals(name)).findFirst();

				if (oT.isPresent()) {
					CharterInMarket ot = oT.get();
					if (!isJSONEqual(o, oT.get())) {
						ScenarioDiffData instance = findDiffs(o, oT.get());
						if (instance == null) {

						}
						if (instance != null) {
							output.add(instance);
							res.get("CharterInMarkets").add(instance);
						}
					} else {

					}
				}
			}
			for (CharterOutMarket o : s_charter_out) {

				String name = o.getName();
				Optional<CharterOutMarket> oT = t_charter_out.stream().filter(ot -> ot.getName().equals(name)).findFirst();

				if (oT.isPresent()) {
					CharterOutMarket ot = oT.get();
					if (!isJSONEqual(o, oT.get())) {
						ScenarioDiffData instance = findDiffs(o, oT.get());
						if (instance == null) {

						}
						if (instance != null) {
							output.add(instance);
							res.get("CharterOutMarkets").add(instance);
						}
					} else {

					}
				}
			}

			for (Port o : s_port) {

				String uuid = o.getUuid();
				Optional<Port> oT = t_port.stream().filter(ot -> ot.getUuid().equals(uuid)).findFirst();

				if (oT.isPresent()) {

					Port ot = oT.get();
					if (!isJSONEqual(o, oT.get())) {
						ScenarioDiffData instance = findDiffs(o, oT.get());
						if (instance != null) {
							output.add(instance);
							res.get("Ports").add(instance);
						}
					} else {

					}
				}
			}
			for (PurchaseContract o : s_purchase_contract) {

				String name = o.getName();
				Optional<PurchaseContract> oT = t_purchase_contract.stream().filter(ot -> ot.getName().equals(name)).findFirst();

				if (oT.isPresent()) {
					PurchaseContract ot = oT.get();
					if (!isJSONEqual(o, oT.get())) {
						ScenarioDiffData instance = findDiffs(o, oT.get());

						if (instance != null) {
							output.add(instance);
							res.get("PurchaseContracts").add(instance);
						}
					} else {

					}
				}
			}
			for (SalesContract o : s_sales_contract) {

				String name = o.getName();
				Optional<SalesContract> oT = t_sales_contract.stream().filter(ot -> ot.getName().equals(name)).findFirst();

				if (oT.isPresent()) {
					SalesContract ot = oT.get();
					if (!isJSONEqual(o, oT.get())) {
						ScenarioDiffData instance = findDiffs(o, oT.get());

						if (instance != null) {
							output.add(instance);
							res.get("SalesContracts").add(instance);
						}
					} else {

					}
				}
			}

			for (SpotMarket o : s_des_purchase) {

				String name = o.getName();
				Optional<SpotMarket> oT = t_des_purchase.stream().filter(ot -> ot.getName().equals(name)).findFirst();

				if (oT.isPresent()) {
					SpotMarket ot = oT.get();

					if (!isJSONEqual(o, oT.get())) {
						ScenarioDiffData instance = findDiffs(o, oT.get());

						if (instance != null) {
							output.add(instance);
							res.get("Cargo_Markets_Des_Purchase").add(instance);
						}
					} else {

					}
				}
			}

			for (SpotMarket o : s_des_sales) {

				String name = o.getName();
				Optional<SpotMarket> oT = t_des_sales.stream().filter(ot -> ot.getName().equals(name)).findFirst();

				if (oT.isPresent()) {
					SpotMarket ot = oT.get();
					if (!isJSONEqual(o, oT.get())) {
						ScenarioDiffData instance = findDiffs(o, oT.get());

						if (instance != null) {
							output.add(instance);
							res.get("Cargo_Markets_Des_Sales").add(instance);
						}
					} else {

					}
				}
			}
			for (SpotMarket o : s_fob_purchase) {

				String name = o.getName();
				Optional<SpotMarket> oT = t_fob_purchase.stream().filter(ot -> ot.getName().equals(name)).findFirst();

				if (oT.isPresent()) {
					SpotMarket ot = oT.get();

					if (!isJSONEqual(o, ot)) {
						ScenarioDiffData instance = findDiffs(o, oT.get());

						if (instance != null) {
							output.add(instance);
							res.get("Cargo_Markets_Fob_Purchase").add(instance);
						}
					} else {

					}
				}
			}
			for (SpotMarket o : s_fob_sales) {

				String name = o.getName();
				Optional<SpotMarket> oT = t_fob_sales.stream().filter(ot -> ot.getName().equals(name)).findFirst();

				if (oT.isPresent()) {
					SpotMarket ot = oT.get();
					if (!isJSONEqual(o, oT.get())) {
						ScenarioDiffData instance = findDiffs(o, oT.get());

						if (instance != null) {
							output.add(instance);
							res.get("Cargo_Markets_Fob_Sales").add(instance);
						}
					} else {

					}
				}
			}

		}
		return res;
	}

	private final ObjectMapper mapper = new ObjectMapper();

	private String filterIDs(String o1Str) {
		StringBuilder filtered = new StringBuilder();
		String[] lines = o1Str.split(",");
		for (String line : lines) {
			if (line.contains("\"uuid\"") || line.contains("@lookupID") || line.contains("globalId")) {

			} else {
				filtered.append(line);
			}
		}
		return filtered.toString();
	}

	private boolean isJSONEqual(EObject o1, EObject o2) {
		if (o1 == o2) {
			return true;
		}
		if (o1 == null && o2 != null || (o1 != null && o2 == null)) {
			return false;
		}
		mapper.registerModule(new EMFJacksonModule());
		try {
			String o1Str = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(o1);
			o1Str = filterIDs(o1Str);
			String o2Str = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(o2);
			o2Str = filterIDs(o2Str);
			boolean equal = o1Str.equals(o2Str);

			if (!equal) {
				// System.out.println("o1Str = " + o1.toString() + "o2Str = " + o2.toString());
				String[] o1Lines = o1Str.split("\n");
				String[] o2Lines = o2Str.split("\n");
				if (o1Lines.length != o2Lines.length) {
					// System.out.println("o1 lines = " + o1Lines.length + " o2 lines = " +
					// o2Lines.length);
				}

				for (int i = 0; i < Math.max(o1Lines.length, o2Lines.length); i++) {
					if (i < o1Lines.length) {
						if (!o2Str.contains(o1Lines[i])) {
							// System.out.println("Diff " + i + "(1):\t" + o1Lines[i]);
						}
					}
					if (i < o2Lines.length) {
						if (!o1Str.contains(o2Lines[i])) {
							// System.out.println("Diff " + i + "(2):\t" + o2Lines[i]);
						}
					}
				}
			}
			return equal;
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
		return false;
	}

	public final Set<String> overallIncomeMonths = new HashSet<>();

	protected AbstractScenarioDiff(final String helpContextId) {
		this.helpContextId = helpContextId;
		overallIncomeMonths.clear();
		overallIncomeMonths.add(new String("pinned"));
		overallIncomeMonths.add(new String(""));
	}

	protected SimpleTabularReportContentProvider createContentProvider() {

		return new SimpleTabularReportContentProvider() {

			@Override
			public Object getParent(final Object element) {
				if (element instanceof ScenarioDiffData) {
					return ((ScenarioDiffData) element).parent;
				}
				return super.getParent(element);
			}

			@Override
			public Object[] getChildren(final Object parentElement) {
				if (parentElement instanceof ScenarioDiffData) {
					return ((ScenarioDiffData) parentElement).children.toArray();
				}
				return super.getChildren(parentElement);
			}

			@Override
			public boolean hasChildren(final Object element) {
				if (element instanceof ScenarioDiffData) {
					return !((ScenarioDiffData) element).children.isEmpty();
				}
				return super.hasChildren(element);
			}

		};
	}

	protected AbstractSimpleTabularReportTransformer<ScenarioDiffData> createTransformer() {
		return new AbstractSimpleTabularReportTransformer<ScenarioDiffData>() {

			/**
			 * Returns the list of year / month labels for the entire known exposure data
			 * range. This may conceivably include months in which no transactions occur.
			 * 
			 * @return
			 */
			// The list of two scenario names: 1 and 2.
			private List<String> dateRange() {
				final List<String> result = new ArrayList<>();
				for (final String key : overallIncomeMonths) {
					result.add(key);
				}
				return result;
			}

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
							// final ScenarioResult scenarioResult = data.scenarioResult;

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
						// "-"
						final String result = data.valuesByMonth.containsKey(date) ? data.valuesByMonth.get(date) : "";
						return result;
					}

					@Override
					public int compare(final ScenarioDiffData o1, final ScenarioDiffData o2) {
						// "-"
						final String result1 = o1.valuesByMonth.containsKey(date) ? o1.valuesByMonth.get(date) : "";
						final String result2 = o2.valuesByMonth.containsKey(date) ? o2.valuesByMonth.get(date) : "";
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
				final List<ScenarioDiffData> output = new LinkedList<>();
				final List<ScenarioDiffData> parent_res = new ArrayList<>();
				{
					if (pinnedPair != null && otherPairs != null && otherPairs.size() > 0 && pinnedPair.getSecond() != null && otherPairs.get(0).getSecond() != null) {

						ScenarioResult sr = pinnedPair.getSecond();
						ScenarioResult tr = otherPairs.get(0).getSecond();

						sr.getRootObject();

						LNGScenarioModel sm = sr.getScenarioDataProvider().getTypedScenario(LNGScenarioModel.class);
						LNGScenarioModel tm = tr.getScenarioDataProvider().getTypedScenario(LNGScenarioModel.class);

						Map<String, List<ScenarioDiffData>> res1 = getDiff(output, sm, tm);

						if (res1.get("Trades").size() > 0) {
							Map<String, String> parentMap = new HashMap<String, String>();
							parentMap.put("pinned", "");
							parentMap.put("", "");
							ScenarioDiffData result = new ScenarioDiffData(parentMap, "Trades");
							for (ScenarioDiffData sdd : res1.get("Trades")) {
								result.children.add(sdd);
								sdd.parent = result;
							}
							parent_res.add(result);
						}
						if (res1.get("Vessels").size() > 0) {
							Map<String, String> parentMap = new HashMap<String, String>();
							parentMap.put("pinned", "");
							parentMap.put("", "");
							ScenarioDiffData result = new ScenarioDiffData(parentMap, "Vessels");
							for (ScenarioDiffData sdd : res1.get("Vessels")) {
								result.children.add(sdd);
								sdd.parent = result;
							}
							parent_res.add(result);
						}
						if (res1.get("VesselEvents").size() > 0) {
							Map<String, String> parentMap = new HashMap<String, String>();
							parentMap.put("pinned", "");
							parentMap.put("", "");
							ScenarioDiffData result = new ScenarioDiffData(parentMap, "VesselEvents");
							for (ScenarioDiffData sdd : res1.get("VesselEvents")) {
								result.children.add(sdd);
								sdd.parent = result;
							}
							parent_res.add(result);
						}
						if (res1.get("CharterInMarkets").size() > 0) {
							Map<String, String> parentMap = new HashMap<String, String>();
							parentMap.put("pinned", "");
							parentMap.put("", "");
							ScenarioDiffData result = new ScenarioDiffData(parentMap, "CharterInMarkets");
							for (ScenarioDiffData sdd : res1.get("CharterInMarkets")) {
								result.children.add(sdd);
								sdd.parent = result;
							}
							parent_res.add(result);
						}
						if (res1.get("CharterOutMarkets").size() > 0) {
							Map<String, String> parentMap = new HashMap<String, String>();
							parentMap.put("pinned", "");
							parentMap.put("", "");
							ScenarioDiffData result = new ScenarioDiffData(parentMap, "CharterOutMarkets");
							for (ScenarioDiffData sdd : res1.get("CharterOutMarkets")) {
								result.children.add(sdd);
								sdd.parent = result;
							}
							parent_res.add(result);
						}
						if (res1.get("Ports").size() > 0) {
							Map<String, String> parentMap = new HashMap<String, String>();
							parentMap.put("pinned", "");
							parentMap.put("", "");
							ScenarioDiffData result = new ScenarioDiffData(parentMap, "Ports");
							for (ScenarioDiffData sdd : res1.get("Ports")) {
								result.children.add(sdd);
								sdd.parent = result;
							}
							parent_res.add(result);
						}
						if (res1.get("PurchaseContracts").size() > 0 || res1.get("SalesContracts").size() > 0) {

							Map<String, String> parentMap = new HashMap<String, String>();
							parentMap.put("pinned", "");
							parentMap.put("", "");
							ScenarioDiffData result = new ScenarioDiffData(parentMap, "Commercial");
							if (res1.get("PurchaseContracts").size() > 0) {
								Map<String, String> sub_parentMap = new HashMap<String, String>();
								sub_parentMap.put("pinned", "");
								sub_parentMap.put("", "");
								ScenarioDiffData sub_result = new ScenarioDiffData(parentMap, "Purchase Contracts");
								for (ScenarioDiffData sdd : res1.get("PurchaseContracts")) {
									sub_result.children.add(sdd);
									sdd.parent = sub_result;
								}
								result.children.add(sub_result);
								sub_result.parent = result;
							}
							if (res1.get("SalesContracts").size() > 0) {
								Map<String, String> sub_parentMap = new HashMap<String, String>();
								sub_parentMap.put("pinned", "");
								sub_parentMap.put("", "");
								ScenarioDiffData sub_result = new ScenarioDiffData(parentMap, "Sales Contracts");
								for (ScenarioDiffData sdd : res1.get("SalesContracts")) {
									sub_result.children.add(sdd);
									sdd.parent = sub_result;
								}
								result.children.add(sub_result);
								sub_result.parent = result;
							}
							parent_res.add(result);
						}

						if (res1.get("Cargo_Markets_Fob_Purchase").size() > 0 || res1.get("Cargo_Markets_Fob_Sales").size() > 0 || res1.get("Cargo_Markets_Des_Purchase").size() > 0
								|| res1.get("Cargo_Markets_Des_Sales").size() > 0) {
							Map<String, String> parentMap = new HashMap<String, String>();
							parentMap.put("pinned", "");
							parentMap.put("", "");
							ScenarioDiffData result = new ScenarioDiffData(parentMap, "Cargo Markets");
							if (res1.get("Cargo_Markets_Fob_Purchase").size() > 0) {
								Map<String, String> sub_parentMap = new HashMap<String, String>();
								sub_parentMap.put("pinned", "");
								sub_parentMap.put("", "");
								ScenarioDiffData sub_result = new ScenarioDiffData(parentMap, "Fob Purchase");
								for (ScenarioDiffData sdd : res1.get("Cargo_Markets_Fob_Purchase")) {
									sub_result.children.add(sdd);
									sdd.parent = sub_result;
								}
								result.children.add(sub_result);
								sub_result.parent = result;
							}
							if (res1.get("Cargo_Markets_Fob_Sales").size() > 0) {
								Map<String, String> sub_parentMap = new HashMap<String, String>();
								sub_parentMap.put("pinned", "");
								sub_parentMap.put("", "");
								ScenarioDiffData sub_result = new ScenarioDiffData(parentMap, "Fob Sales");
								for (ScenarioDiffData sdd : res1.get("Cargo_Markets_Fob_Sales")) {
									sub_result.children.add(sdd);
									sdd.parent = sub_result;
								}
								result.children.add(sub_result);
								sub_result.parent = result;
							}
							if (res1.get("Cargo_Markets_Des_Purchase").size() > 0) {
								Map<String, String> sub_parentMap = new HashMap<String, String>();
								sub_parentMap.put("pinned", "");
								sub_parentMap.put("", "");
								ScenarioDiffData sub_result = new ScenarioDiffData(parentMap, "Des Purchase");
								for (ScenarioDiffData sdd : res1.get("Cargo_Markets_Des_Purchase")) {
									sub_result.children.add(sdd);
									sdd.parent = sub_result;
								}
								result.children.add(sub_result);
								sub_result.parent = result;
							}
							if (res1.get("Cargo_Markets_Des_Sales").size() > 0) {
								Map<String, String> sub_parentMap = new HashMap<String, String>();
								sub_parentMap.put("pinned", "");
								sub_parentMap.put("", "");
								ScenarioDiffData sub_result = new ScenarioDiffData(parentMap, "Des Sales");
								for (ScenarioDiffData sdd : res1.get("Cargo_Markets_Des_Sales")) {
									sub_result.children.add(sdd);
									sdd.parent = sub_result;
								}
								result.children.add(sub_result);
								sub_result.parent = result;
							}

							parent_res.add(result);
						}

					}

				}

				return parent_res;
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