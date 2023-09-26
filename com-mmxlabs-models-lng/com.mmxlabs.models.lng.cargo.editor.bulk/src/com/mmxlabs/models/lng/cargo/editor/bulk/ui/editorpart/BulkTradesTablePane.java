/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
package com.mmxlabs.models.lng.cargo.editor.bulk.ui.editorpart;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.e4.core.services.events.IEventBroker;
import org.eclipse.emf.common.command.Command;
import org.eclipse.emf.common.command.CommandStack;
import org.eclipse.emf.common.command.CompoundCommand;
import org.eclipse.emf.common.notify.AdapterFactory;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EFactory;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.EcoreFactory;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.emf.edit.command.AddCommand;
import org.eclipse.emf.edit.command.SetCommand;
import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.Nullable;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.GroupMarker;
import org.eclipse.jface.action.IContributionItem;
import org.eclipse.jface.action.IMenuListener;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.action.ToolBarManager;
import org.eclipse.jface.bindings.keys.IKeyLookup;
import org.eclipse.jface.bindings.keys.KeyLookupFactory;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.util.IPropertyChangeListener;
import org.eclipse.jface.util.PropertyChangeEvent;
import org.eclipse.jface.viewers.ColumnViewerEditor;
import org.eclipse.jface.viewers.ColumnViewerEditorActivationEvent;
import org.eclipse.jface.viewers.ColumnViewerEditorActivationStrategy;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.TreeSelection;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.nebula.jface.gridviewer.GridTreeViewer;
import org.eclipse.nebula.jface.gridviewer.GridViewerColumn;
import org.eclipse.nebula.jface.gridviewer.GridViewerEditor;
import org.eclipse.nebula.widgets.grid.Grid;
import org.eclipse.nebula.widgets.grid.GridColumn;
import org.eclipse.nebula.widgets.grid.GridItem;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.MenuDetectEvent;
import org.eclipse.swt.events.MenuDetectListener;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.ui.IActionBars;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.actions.ActionFactory;
import org.osgi.service.event.EventHandler;

import com.google.inject.Guice;
import com.google.inject.Inject;
import com.google.inject.Injector;
import com.mmxlabs.common.CollectionsUtil;
import com.mmxlabs.license.features.KnownFeatures;
import com.mmxlabs.license.features.LicenseFeatures;
import com.mmxlabs.models.lng.cargo.Cargo;
import com.mmxlabs.models.lng.cargo.CargoFactory;
import com.mmxlabs.models.lng.cargo.CargoModel;
import com.mmxlabs.models.lng.cargo.CargoPackage;
import com.mmxlabs.models.lng.cargo.DischargeSlot;
import com.mmxlabs.models.lng.cargo.LoadSlot;
import com.mmxlabs.models.lng.cargo.Slot;
import com.mmxlabs.models.lng.cargo.editor.CargoEditorPlugin;
import com.mmxlabs.models.lng.cargo.editor.PreferenceConstants;
import com.mmxlabs.models.lng.cargo.editor.bulk.columns.extmodule.ITradeBasedBulkColumnExtension;
import com.mmxlabs.models.lng.cargo.editor.bulk.columns.extmodule.ITradeBasedBulkColumnFactoryExtension;
import com.mmxlabs.models.lng.cargo.editor.bulk.columns.extmodule.TradeBasedBulkModule;
import com.mmxlabs.models.lng.cargo.editor.bulk.rowmodel.extmodule.ITradeBasedBulkRowModelExtension;
import com.mmxlabs.models.lng.cargo.editor.bulk.rowmodel.extmodule.ITradeBasedBulkRowModelFactoryExtension;
import com.mmxlabs.models.lng.cargo.editor.bulk.rowtransformer.extmodule.ITradeBasedBulkRowTransformerExtension;
import com.mmxlabs.models.lng.cargo.editor.bulk.rowtransformer.extmodule.ITradeBasedBulkRowTransformerFactoryExtension;
import com.mmxlabs.models.lng.cargo.editor.bulk.views.ITradesBasedRowModelTransformerFactory;
import com.mmxlabs.models.lng.cargo.editor.bulk.views.ITradesColumnFactory;
import com.mmxlabs.models.lng.cargo.editor.bulk.views.ITradesRowTransformerFactory;
import com.mmxlabs.models.lng.cargo.editor.bulk.views.TradesBasedColumnFactory;
import com.mmxlabs.models.lng.cargo.editor.model.cargoeditormodel.CargoEditorModelPackage;
import com.mmxlabs.models.lng.cargo.editor.model.cargoeditormodel.TradesRow;
import com.mmxlabs.models.lng.cargo.editor.model.cargoeditormodel.TradesTableRoot;
import com.mmxlabs.models.lng.cargo.ui.editorpart.StructuredViewerFilterManager;
import com.mmxlabs.models.lng.cargo.ui.editorpart.DefaultExtraFiltersProvider;
import com.mmxlabs.models.lng.cargo.ui.editorpart.IExtraFiltersProvider;
import com.mmxlabs.models.lng.cargo.ui.editorpart.IFilterable;
import com.mmxlabs.models.lng.cargo.ui.editorpart.PromptToolbarEditor;
import com.mmxlabs.models.lng.cargo.ui.editorpart.TradesRowEMFPath;
import com.mmxlabs.models.lng.cargo.ui.editorpart.actions.CargoEditingCommands;
import com.mmxlabs.models.lng.cargo.ui.editorpart.actions.CargoEditorMenuHelper;
import com.mmxlabs.models.lng.cargo.ui.editorpart.actions.DefaultMenuCreatorAction;
import com.mmxlabs.models.lng.cargo.ui.editorpart.actions.DischargePortFilterAction;
import com.mmxlabs.models.lng.cargo.ui.editorpart.actions.EMFPathFilterAction;
import com.mmxlabs.models.lng.cargo.ui.editorpart.actions.EntityFilterAction;
import com.mmxlabs.models.lng.cargo.ui.editorpart.actions.LoadPortFilterAction;
import com.mmxlabs.models.lng.cargo.ui.editorpart.actions.OpenCargoFilterAction;
import com.mmxlabs.models.lng.cargo.ui.editorpart.actions.PeriodFilterAction;
import com.mmxlabs.models.lng.cargo.ui.editorpart.actions.ShippedCargoFilterAction;
import com.mmxlabs.models.lng.cargo.ui.editorpart.actions.TrimmedVesselFilterAction;
import com.mmxlabs.models.lng.cargo.ui.editorpart.trades.ITradesTableContextMenuExtension;
import com.mmxlabs.models.lng.cargo.ui.editorpart.trades.TradesTableContextMenuExtensionUtil;
import com.mmxlabs.models.lng.cargo.util.SlotContractParamsHelper;
import com.mmxlabs.models.lng.commercial.CommercialModel;
import com.mmxlabs.models.lng.commercial.CommercialPackage;
import com.mmxlabs.models.lng.fleet.FleetModel;
import com.mmxlabs.models.lng.scenario.model.LNGScenarioModel;
import com.mmxlabs.models.lng.scenario.model.util.ScenarioModelUtil;
import com.mmxlabs.models.lng.schedule.Schedule;
import com.mmxlabs.models.lng.schedule.ScheduleModel;
import com.mmxlabs.models.lng.schedule.SlotAllocation;
import com.mmxlabs.models.lng.ui.actions.AddModelAction.IAddContext;
import com.mmxlabs.models.lng.ui.actions.DuplicateAction;
import com.mmxlabs.models.lng.ui.tabular.ScenarioTableViewer;
import com.mmxlabs.models.lng.ui.tabular.ScenarioTableViewerPane;
import com.mmxlabs.models.mmxcore.MMXCorePackage;
import com.mmxlabs.models.mmxcore.MMXRootObject;
import com.mmxlabs.models.ui.Activator;
import com.mmxlabs.models.ui.editorpart.IScenarioEditingLocation;
import com.mmxlabs.models.ui.editorpart.JointModelEditorPartSelectionChangedListener;
import com.mmxlabs.models.ui.editors.ICommandHandler;
import com.mmxlabs.models.ui.editors.dialogs.DetailCompositeDialogUtil;
import com.mmxlabs.models.ui.tabular.EObjectTableViewer;
import com.mmxlabs.models.ui.tabular.EObjectTableViewerValidationSupport;
import com.mmxlabs.models.ui.tabular.GridViewerHelper;
import com.mmxlabs.models.ui.tabular.columngeneration.ColumnBlock;
import com.mmxlabs.models.ui.tabular.columngeneration.ColumnBlockManager;
import com.mmxlabs.models.ui.tabular.columngeneration.ColumnHandler;
import com.mmxlabs.models.ui.tabular.columngeneration.EMFReportColumnManager;
import com.mmxlabs.models.ui.tabular.columngeneration.EObjectTableViewerColumnFactory;
import com.mmxlabs.models.util.emfpath.EMFPath;
import com.mmxlabs.models.util.emfpath.IEMFPath;
import com.mmxlabs.rcp.common.SelectionHelper;
import com.mmxlabs.rcp.common.ServiceHelper;
import com.mmxlabs.rcp.common.actions.PackGridTreeColumnsAction;
import com.mmxlabs.rcp.common.handlers.TodayHandler;
import com.mmxlabs.rcp.icons.lingo.CommonImages;
import com.mmxlabs.rcp.icons.lingo.CommonImages.IconMode;
import com.mmxlabs.rcp.icons.lingo.CommonImages.IconPaths;
import com.mmxlabs.scenario.service.model.manager.ModelReference;

public class BulkTradesTablePane extends ScenarioTableViewerPane implements IAdaptable {

	private static final String COLUMN_VISIBILITY_KEY = "BulkTradesTablePane.COLUMN_VISIBILITY_KEY";

	private static final String CUSTOMISABLE_ROW_NAME = "CustomisableRow";

	private Iterable<ITradesTableContextMenuExtension> contextMenuExtensions;

	@Inject(optional = true)
	private Iterable<ITradeBasedBulkColumnFactoryExtension> columnFactoryExtensions;

	@Inject(optional = true)
	private Iterable<ITradeBasedBulkColumnExtension> columnExtensions;

	@Inject(optional = true)
	private Iterable<ITradeBasedBulkRowModelFactoryExtension> rowModelFactoryExtensions;

	@Inject(optional = true)
	private Iterable<ITradeBasedBulkRowModelExtension> rowModelExtensions;

	@Inject(optional = true)
	private Iterable<ITradeBasedBulkRowTransformerFactoryExtension> rowTransformerFactoryExtensions;

	@Inject(optional = true)
	private Iterable<ITradeBasedBulkRowTransformerExtension> rowTransformerExtensions;

	private LNGScenarioModel lngScenarioModel;
	private TradesTableRoot tradesTableRoot;
	private final CargoPackage cargoPkg = CargoPackage.eINSTANCE;
	private final EMFReportColumnManager columnManager = new EMFReportColumnManager();

	// todayHandler
	private EventHandler todayHandler;

	private final Map<TradesRow, TradesRow> rowDataToRow = new HashMap<>();

	/*
	 * This is used to determine the order of column groups
	 */
	private final String[] orderedColumnGroupNames = new String[] { //
			TradesBasedColumnFactory.LOAD_START_GROUP, //
			TradesBasedColumnFactory.LOAD_PORT_GROUP, //
			TradesBasedColumnFactory.LOAD_DIVERSION_GROUP, //
			TradesBasedColumnFactory.LOAD_PRICING_GROUP, //
			TradesBasedColumnFactory.LOAD_PRICING_EXTRA_GROUP, //
			TradesBasedColumnFactory.LOAD_VOLUME_GROUP, //
			TradesBasedColumnFactory.LOAD_WINDOW_GROUP, //
			TradesBasedColumnFactory.LOAD_EXTRA_GROUP, //
			TradesBasedColumnFactory.LOAD_END_GROUP, //
			TradesBasedColumnFactory.DISCHARGE_START_GROUP, //
			TradesBasedColumnFactory.DISCHARGE_PORT_GROUP, //
			TradesBasedColumnFactory.DISCHARGE_DIVERSION_GROUP, //
			TradesBasedColumnFactory.DISCHARGE_PRICING_GROUP, //
			TradesBasedColumnFactory.DISCHARGE_PRICING_EXTRA_GROUP, //
			TradesBasedColumnFactory.DISCHARGE_VOLUME_GROUP, //
			TradesBasedColumnFactory.DISCHARGE_WINDOW_GROUP, //
			TradesBasedColumnFactory.DISCHARGE_EXTRA_GROUP, //
			TradesBasedColumnFactory.DISCHARGE_END_GROUP, //
			TradesBasedColumnFactory.CARGO_START_GROUP, //
			TradesBasedColumnFactory.CARGO_END_GROUP //
	};

	/*
	 * This is used to determine the default visible column groups
	 */
	private final String[] defaultFilters = { //
			TradesBasedColumnFactory.LOAD_START_GROUP, //
			TradesBasedColumnFactory.LOAD_PORT_GROUP, //
			TradesBasedColumnFactory.LOAD_PRICING_GROUP, //
			TradesBasedColumnFactory.LOAD_PRICING_EXTRA_GROUP, //
			TradesBasedColumnFactory.LOAD_VOLUME_GROUP, //
			TradesBasedColumnFactory.LOAD_WINDOW_GROUP, //
			TradesBasedColumnFactory.LOAD_EXTRA_GROUP, //
			TradesBasedColumnFactory.LOAD_END_GROUP, //
			TradesBasedColumnFactory.DISCHARGE_START_GROUP, //
			TradesBasedColumnFactory.DISCHARGE_PORT_GROUP, //
			TradesBasedColumnFactory.DISCHARGE_PRICING_GROUP, //
			TradesBasedColumnFactory.DISCHARGE_PRICING_EXTRA_GROUP, //
			TradesBasedColumnFactory.DISCHARGE_VOLUME_GROUP, //
			TradesBasedColumnFactory.DISCHARGE_WINDOW_GROUP, //
			TradesBasedColumnFactory.DISCHARGE_EXTRA_GROUP, //
			TradesBasedColumnFactory.DISCHARGE_END_GROUP, //
			TradesBasedColumnFactory.CARGO_END_GROUP //
	};
	// FM - properties +
	private IPreferenceStore preferenceStore;
	private IPropertyChangeListener propertyChangeListener = new IPropertyChangeListener() {

		@Override
		public void propertyChange(PropertyChangeEvent event) {
			setInitialState();
		}
	};

	@Override
	public void dispose() {
		if (preferenceStore != null) {
			preferenceStore.removePropertyChangeListener(propertyChangeListener);
		}
		if (this.todayHandler != null) {
			IEventBroker eventBroker = PlatformUI.getWorkbench().getService(IEventBroker.class);
			eventBroker.unsubscribe(this.todayHandler);
		}
		super.dispose();
	}

	// FM - properties -
	private final EPackage customRowPackage = createCustomisedRowEcore();
	private final ColumnFilters columnFilters;
	final Map<String, ITradesRowTransformerFactory> rowTransformerHandlers = new HashMap<>();
	
	private StructuredViewerFilterManager filterManager;
	private IExtraFiltersProvider extraFiltersProvider;
	
	private LocalDate scenarioStart;
	private LocalDate scenarioEnd;

	private final ColumnBlockManager columnBlockManager = new ColumnBlockManager();
	private final Map<ColumnHandler, GridColumn> handlerToColumnMap = new HashMap<>();

	private PromptToolbarEditor promptToolbarEditor;

	private final CargoEditingCommands cec;
	private final CargoEditorMenuHelper menuHelper;

	private boolean locked;

	public BulkTradesTablePane(final IWorkbenchPage page, final IWorkbenchPart part, final IScenarioEditingLocation location, final IActionBars actionBars) {
		super(page, part, location, actionBars);
		final Injector injector = Guice.createInjector(new TradeBasedBulkModule());
		injector.injectMembers(this);
		columnFilters = new ColumnFilters(defaultFilters);
		final LNGScenarioModel scenarioModel = (LNGScenarioModel) scenarioEditingLocation.getRootObject();

		this.cec = new CargoEditingCommands(scenarioEditingLocation.getEditingDomain(), scenarioModel, ScenarioModelUtil.getCargoModel(scenarioModel),
				ScenarioModelUtil.getCommercialModel(scenarioModel), Activator.getDefault().getModelFactoryRegistry());
		this.menuHelper = new CargoEditorMenuHelper(part.getSite().getShell(), scenarioEditingLocation, scenarioModel);
		preferenceStore = Activator.getDefault().getPreferenceStore();
		preferenceStore.addPropertyChangeListener(propertyChangeListener);
	}

	@Override
	public void init(final List<EReference> path, final AdapterFactory adapterFactory, final ModelReference modelReference) {
		super.init(path, adapterFactory, modelReference);

		// set up snap-to-today
		IEventBroker eventBroker = PlatformUI.getWorkbench().getService(IEventBroker.class);
		this.todayHandler = event -> snapTo((LocalDate) event.getProperty(IEventBroker.DATA));
		eventBroker.subscribe(TodayHandler.EVENT_SNAP_TO_DATE, this.todayHandler);
		
		this.scenarioStart = ScenarioModelUtil.getEarliestScenarioDate(scenarioEditingLocation.getScenarioDataProvider());
		this.scenarioEnd = ScenarioModelUtil.getLatestScenarioDate(scenarioEditingLocation.getScenarioDataProvider());

		extendRowModel();
		createColumns();
		buildRowTransformerHandlers(rowTransformerHandlers);

		buildToolbar();
		setInitialState();
	}

	private void snapTo(final LocalDate property) {
		if (scenarioViewer == null) {
			return;
		}
		final Grid grid = scenarioViewer.getGrid();
		if (grid == null) {
			return;
		}
		final int count = grid.getItemCount();
		if (count <= 0) {
			return;
		}

		final GridItem[] items = grid.getItems();
		int pos = -1;
		for (GridItem item : items) {
			Object oData = item.getData();
			if (oData instanceof TradesRow tradesRow) {
				LoadSlot ls = tradesRow.getLoadSlot();
				if (ls != null) {
					if (ls.getWindowStart().isAfter(property)) {
						break;
					}
					pos++;
					continue;
				}
				DischargeSlot ds = tradesRow.getDischargeSlot();
				if (ds != null) {
					if (ds.getWindowStart().isAfter(property)) {
						break;
					}
					pos++;
				}
			}
		}
		if (pos != -1) {
			grid.deselectAll();
			grid.select(pos);
			grid.showSelection();
		}
	}

	public <T> T getAdapter(final Class<T> c) {
		if (GridTreeViewer.class.isAssignableFrom(c)) {
			return c.cast(getScenarioViewer());
		}
		if (TradesTableRoot.class.isAssignableFrom(c)) {
			return c.cast(getTradesTableRoot());
		}
		if (EObjectTableViewer.class.isAssignableFrom(c)) {
			return c.cast(getScenarioViewer());
		}
		return c.cast(null);
	}

	private void buildToolbar() {
		final Action addLoadSlotToDischarge = getAddLoadToDischargeAction();
		final Action addDischargeSlotToLoad = getAddDischargeToLoadAction();

		final ToolBarManager toolbar = getToolBarManager();
		toolbar.removeAll();
		
		filterManager = new StructuredViewerFilterManager(getScenarioViewer());

		setMinToolbarHeight(30);
		promptToolbarEditor = new PromptToolbarEditor("prompt", scenarioEditingLocation.getEditingDomain(), (LNGScenarioModel) scenarioEditingLocation.getRootObject());
		toolbar.add(promptToolbarEditor);

		toolbar.add(new GroupMarker(EDIT_GROUP));
		toolbar.add(new GroupMarker(ADD_REMOVE_GROUP));
		toolbar.add(new GroupMarker(VIEW_GROUP));
		toolbar.appendToGroup(VIEW_GROUP, new PackGridTreeColumnsAction(scenarioViewer));

		final Action addAction = new AddAction("Add", addLoadSlotToDischarge, addDischargeSlotToLoad);

		addAction.setImageDescriptor(CommonImages.getImageDescriptor(IconPaths.Plus, IconMode.Enabled));
		addAction.setDisabledImageDescriptor(CommonImages.getImageDescriptor(IconPaths.Plus, IconMode.Disabled));
		toolbar.appendToGroup(ADD_REMOVE_GROUP, addAction);

		ServiceHelper.withOptionalServiceConsumer(IExtraFiltersProvider.class, p -> {
			if (p == null) {
				setExtraFiltersProvider(new DefaultExtraFiltersProvider());
			} else {
				setExtraFiltersProvider(p);
			}
		});
		final Action filterAction = new BulkTradesTablePaneFilterMenuAction("Filter");
		filterAction.setImageDescriptor(CommonImages.getImageDescriptor(IconPaths.Filter, IconMode.Enabled));
		toolbar.appendToGroup(VIEW_GROUP, filterAction);
		
		deleteAction = createDeleteAction(null); // TODO: NEW check this
		if (deleteAction != null) {
			toolbar.appendToGroup(ADD_REMOVE_GROUP, deleteAction);
		}
		if (actionBars != null) {
			actionBars.setGlobalActionHandler(ActionFactory.DELETE.getId(), deleteAction);
		}

		final Action configureColumnsAction = new Action("Configure Contents") {
			@Override
			public void run() {

				final List<ColumnBlock> items = getBlockManager().getBlockOrderFromGrid();
				ColumnSelectionDialog myDialog = new ColumnSelectionDialog(part.getSite().getShell(), items);
				final List<ColumnBlock> results = myDialog.pick(part.getSite().getShell(), items, null);
				setState(results);
				scenarioViewer.refresh();
			}
		};
		getMenuManager().add(configureColumnsAction);
		getMenuManager().update(true);
		//
		toolbar.update(true);
	}

	private void setExtraFiltersProvider(IExtraFiltersProvider filtersProvider) {
		this.extraFiltersProvider = filtersProvider;
		
	}

	private ColumnBlockManager getBlockManager() {
		return columnBlockManager;
	}

	private void setInitialState() {
		// hide all
		for (final String blockId : getBlockManager().getBlockIDOrder()) {
			getBlockManager().getBlockByID(blockId).setUserVisible(false);
		}
		// init from file the settings
		int numberOfVisibleColumns = columnBlockManager.initFromPreferences(COLUMN_VISIBILITY_KEY, preferenceStore);
		if (numberOfVisibleColumns == 0) {
			for (final String blockId : getBlockManager().getBlockIDOrder()) {
				getBlockManager().getBlockByID(blockId).setUserVisible(true);
			}
		}
		scenarioViewer.refresh();
	}

	private void setState(List<ColumnBlock> list) {
		if (list != null) {
			if (!list.isEmpty()) {
				// hide all
				for (final String blockId : getBlockManager().getBlockIDOrder()) {
					getBlockManager().getBlockByID(blockId).setUserVisible(false);
				}
				// init from file the settings
				for (final ColumnBlock cb : list) {
					cb.setUserVisible(true);
				}
				scenarioViewer.refresh();
				columnBlockManager.saveToPreferences(COLUMN_VISIBILITY_KEY, preferenceStore);
			}
		}
	}

	private Action getAddDischargeToLoadAction() {
		return new Action("Create discharge for load") {

			@Override
			public boolean isEnabled() {
				final ISelection selection = viewer.getSelection();
				if (selection instanceof TreeSelection) {
					final Object[] elements = ((TreeSelection) selection).toArray();
					if (elements.length == 1) {
						final Object o = elements[0];
						if (o instanceof TradesRow tradesRow) {
							if (tradesRow.getCargo() == null && tradesRow.getDischargeSlot() == null && tradesRow.getLoadSlot() != null) {
								return true;
							}
						}
					}
				}
				return false;
			}

			public void run() {
				final ISelection selection = viewer.getSelection();
				if (selection instanceof TreeSelection) {
					final Object[] elements = ((TreeSelection) selection).toArray();
					if (elements.length == 1) {
						final Object o = elements[0];
						if (o instanceof TradesRow tradesRow && (tradesRow.getCargo() == null && tradesRow.getDischargeSlot() == null && tradesRow.getLoadSlot() != null)) {
							final CompoundCommand cmd = new CompoundCommand("Create discharge for load");
							final Cargo newCargo = CargoFactory.eINSTANCE.createCargo();
							final LoadSlot loadSlot = tradesRow.getLoadSlot();

							final DischargeSlot dischargeSlot = CargoFactory.eINSTANCE.createDischargeSlot();
							dischargeSlot.setWindowStart(loadSlot.getWindowStart());

							cmd.append(AddCommand.create(scenarioEditingLocation.getEditingDomain(), ScenarioModelUtil.getCargoModel(lngScenarioModel),
									CargoPackage.eINSTANCE.getCargoModel_DischargeSlots(), dischargeSlot));
							cmd.append(AddCommand.create(scenarioEditingLocation.getEditingDomain(), ScenarioModelUtil.getCargoModel(lngScenarioModel), CargoPackage.eINSTANCE.getCargoModel_Cargoes(),
									newCargo));
							cmd.append(SetCommand.create(scenarioEditingLocation.getEditingDomain(), loadSlot, CargoPackage.eINSTANCE.getSlot_Cargo(), newCargo));
							cmd.append(SetCommand.create(scenarioEditingLocation.getEditingDomain(), dischargeSlot, CargoPackage.eINSTANCE.getSlot_Cargo(), newCargo));
							scenarioEditingLocation.getEditingDomain().getCommandStack().execute(cmd);

						}
					}
				}
			}
		};
	}

	private Action getAddLoadToDischargeAction() {
		return new Action("Create load for discharge") {

			@Override
			public boolean isEnabled() {
				final ISelection selection = viewer.getSelection();
				if (selection instanceof TreeSelection ts) {
					final Object[] elements = ts.toArray();
					if (elements.length == 1) {
						final Object o = elements[0];
						if (o instanceof TradesRow tradesRow) {
							if (tradesRow.getCargo() == null && tradesRow.getLoadSlot() == null && tradesRow.getDischargeSlot() != null) {
								return true;
							}
						}
					}
				}
				return false;
			}

			@Override
			public void run() {
				final ISelection selection = viewer.getSelection();
				if (selection instanceof TreeSelection ts) {
					final Object[] elements = ts.toArray();
					if (elements.length == 1) {
						final Object o = elements[0];
						if (o instanceof TradesRow tradesRow) {
							if (tradesRow.getCargo() == null && tradesRow.getLoadSlot() == null && tradesRow.getDischargeSlot() != null) {
								final CompoundCommand cmd = new CompoundCommand("Create load for discharge");
								final Cargo newCargo = CargoFactory.eINSTANCE.createCargo();
								final LoadSlot loadSlot = CargoFactory.eINSTANCE.createLoadSlot();
								final DischargeSlot dischargeSlot = tradesRow.getDischargeSlot();
								loadSlot.setWindowStart(dischargeSlot.getWindowStart());
								cmd.append(AddCommand.create(scenarioEditingLocation.getEditingDomain(), ScenarioModelUtil.getCargoModel(lngScenarioModel),
										CargoPackage.eINSTANCE.getCargoModel_LoadSlots(), loadSlot));
								cmd.append(AddCommand.create(scenarioEditingLocation.getEditingDomain(), ScenarioModelUtil.getCargoModel(lngScenarioModel),
										CargoPackage.eINSTANCE.getCargoModel_Cargoes(), newCargo));
								cmd.append(SetCommand.create(scenarioEditingLocation.getEditingDomain(), loadSlot, CargoPackage.eINSTANCE.getSlot_Cargo(), newCargo));
								cmd.append(SetCommand.create(scenarioEditingLocation.getEditingDomain(), dischargeSlot, CargoPackage.eINSTANCE.getSlot_Cargo(), newCargo));
								scenarioEditingLocation.getEditingDomain().getCommandStack().execute(cmd);
							}
						}
					}
				}
			}
		};
	}

	private Action getNewDischargeSlotAction() {
		return new Action("New Discharge") {
			public void run() {

				final CompoundCommand cmd = new CompoundCommand("New Discharge");

				final DischargeSlot dischargeSlot = CargoFactory.eINSTANCE.createDischargeSlot();
				cmd.append(AddCommand.create(scenarioEditingLocation.getEditingDomain(), ScenarioModelUtil.getCargoModel(lngScenarioModel), CargoPackage.eINSTANCE.getCargoModel_DischargeSlots(),
						dischargeSlot));
				scenarioEditingLocation.getEditingDomain().getCommandStack().execute(cmd);
			}
		};
	}

	private Action getNewLoadSlotAction() {
		return new Action("New Load") {
			public void run() {

				final CompoundCommand cmd = new CompoundCommand("New Load");

				final LoadSlot loadSlot = CargoFactory.eINSTANCE.createLoadSlot();
				cmd.append(
						AddCommand.create(scenarioEditingLocation.getEditingDomain(), ScenarioModelUtil.getCargoModel(lngScenarioModel), CargoPackage.eINSTANCE.getCargoModel_LoadSlots(), loadSlot));
				scenarioEditingLocation.getEditingDomain().getCommandStack().execute(cmd);
			}
		};
	}

	private Action getNewCargoAction() {
		return new Action("New Cargo") {
			public void run() {

				final CompoundCommand cmd = new CompoundCommand("New Cargo");

				final Cargo newCargo = CargoFactory.eINSTANCE.createCargo();
				final LoadSlot loadSlot = CargoFactory.eINSTANCE.createLoadSlot();
				final DischargeSlot dischargeSlot = CargoFactory.eINSTANCE.createDischargeSlot();
				cmd.append(AddCommand.create(scenarioEditingLocation.getEditingDomain(), ScenarioModelUtil.getCargoModel(lngScenarioModel), CargoPackage.eINSTANCE.getCargoModel_DischargeSlots(),
						dischargeSlot));
				cmd.append(
						AddCommand.create(scenarioEditingLocation.getEditingDomain(), ScenarioModelUtil.getCargoModel(lngScenarioModel), CargoPackage.eINSTANCE.getCargoModel_LoadSlots(), loadSlot));
				cmd.append(AddCommand.create(scenarioEditingLocation.getEditingDomain(), ScenarioModelUtil.getCargoModel(lngScenarioModel), CargoPackage.eINSTANCE.getCargoModel_Cargoes(), newCargo));
				cmd.append(SetCommand.create(scenarioEditingLocation.getEditingDomain(), loadSlot, CargoPackage.eINSTANCE.getSlot_Cargo(), newCargo));
				cmd.append(SetCommand.create(scenarioEditingLocation.getEditingDomain(), dischargeSlot, CargoPackage.eINSTANCE.getSlot_Cargo(), newCargo));
				scenarioEditingLocation.getEditingDomain().getCommandStack().execute(cmd);
			}
		};
	}

	public EPackage createCustomisedRowEcore() {
		final EPackage ePackage = EcoreFactory.eINSTANCE.createEPackage();
		final EClass customisableRow = EcoreFactory.eINSTANCE.createEClass();
		customisableRow.getESuperTypes().add(CargoEditorModelPackage.eINSTANCE.getTradesRow());
		customisableRow.setName(CUSTOMISABLE_ROW_NAME);
		ePackage.getEClassifiers().add(customisableRow);
		return ePackage;
	}

	public void setCargoes(final TradesTableRoot tradesTableRoot, final LNGScenarioModel lngScenarioModel) {
		final EList<Cargo> cargoes = ScenarioModelUtil.getCargoModel(lngScenarioModel).getCargoes();
		final ScheduleModel scheduleModel = ScenarioModelUtil.getScheduleModel(lngScenarioModel);
		final Schedule schedule = scheduleModel.getSchedule();
		List<SlotAllocation> slotAllocs = new ArrayList<>();
		if (schedule != null) {
			slotAllocs = schedule.getSlotAllocations();
		}
		resetTradesTableRoot(tradesTableRoot); // Clear rows and cache
		final HashSet<Slot<?>> slotsInCargo = new HashSet<>();
		final Map<Slot<?>, SlotAllocation> slots = new HashMap<>();
		for (final SlotAllocation sa : slotAllocs) {
			if (sa.getSlot() != null) {
				slots.put(sa.getSlot(), sa);
			}
		}
		for (final Cargo cargo : cargoes) {
			final EPackage dataModel = customRowPackage;
			final EFactory factory = dataModel.getEFactoryInstance();
			final EClass customRowClass = (EClass) dataModel.getEClassifier(CUSTOMISABLE_ROW_NAME);
			final TradesRow tradesRow = (TradesRow) factory.create(customRowClass);
			tradesRow.setCargo(cargo);
			final LoadSlot load = getLoadSlot(cargo);
			tradesRow.setLoadSlot(load);
			slotsInCargo.add(load);
			tradesRow.setLoadSlotContractParams(SlotContractParamsHelper.findOrCreateSlotContractParams(load));
			final DischargeSlot discharge = getDischargeSlot(cargo);
			tradesRow.setDischargeSlot(discharge);
			slotsInCargo.add(discharge);
			tradesRow.setDischargeSlotContractParams(SlotContractParamsHelper.findOrCreateSlotContractParams(discharge));
			tradesRow.setLoadAllocation(slots.get(load));
			tradesRow.setDischargeAllocation(slots.get(discharge));

			transformRowWithExtensions(cargo, tradesRow);
			addRowToTable(tradesTableRoot, tradesRow);
		}
		for (final LoadSlot loadSlot : ScenarioModelUtil.getCargoModel(lngScenarioModel).getLoadSlots()) {
			if (!slotsInCargo.contains(loadSlot)) {
				// open load
				final EPackage dataModel = customRowPackage;
				final EFactory factory = dataModel.getEFactoryInstance();
				final EClass customRowClass = (EClass) dataModel.getEClassifier(CUSTOMISABLE_ROW_NAME);
				final TradesRow tradesRow = (TradesRow) factory.create(customRowClass);
				tradesRow.setLoadSlot(loadSlot);
				tradesRow.setLoadSlotContractParams(SlotContractParamsHelper.findOrCreateSlotContractParams(loadSlot));

				transformRowWithExtensions(null, tradesRow);
				addRowToTable(tradesTableRoot, tradesRow);
			}
		}
		for (final DischargeSlot dischargeSlot : ScenarioModelUtil.getCargoModel(lngScenarioModel).getDischargeSlots()) {
			if (!slotsInCargo.contains(dischargeSlot)) {
				// open discharge
				final EPackage dataModel = customRowPackage;
				final EFactory factory = dataModel.getEFactoryInstance();
				final EClass customRowClass = (EClass) dataModel.getEClassifier(CUSTOMISABLE_ROW_NAME);
				final TradesRow tradesRow = (TradesRow) factory.create(customRowClass);
				tradesRow.setDischargeSlot(dischargeSlot);
				tradesRow.setDischargeSlotContractParams(SlotContractParamsHelper.findOrCreateSlotContractParams(dischargeSlot));
				transformRowWithExtensions(null, tradesRow);
				addRowToTable(tradesTableRoot, tradesRow);
			}
		}
	}

	private void resetTradesTableRoot(final TradesTableRoot tradesTableRoot) {
		tradesTableRoot.getTradesRows().clear();
	}

	private void addRowToTable(final TradesTableRoot tradesTableRoot, final TradesRow tradesRow) {
		tradesTableRoot.getTradesRows().add(tradesRow);
		for (final EReference ref : tradesRow.eClass().getEAllReferences()) {
			final Object o = tradesRow.eGet(ref);
			if (o instanceof TradesRow obj) {
				rowDataToRow.put(obj, tradesRow);
				tradesRow.getInputEquivalents().add(obj);
				addEObjectsToMap(obj, tradesRow);
			}
		}
	}

	private void addEObjectsToMap(final EObject parent, final TradesRow tradesRow) {
		for (final EReference ref : parent.eClass().getEAllReferences()) {
			if (ref.isContainment()) {
				if (ref.isMany()) {
					final Collection<Object> collection = (Collection<Object>) parent.eGet(ref);
					for (final Object o : collection) {
						if (o instanceof TradesRow otherTradesRow) {
							rowDataToRow.put(otherTradesRow, tradesRow);
							tradesRow.getInputEquivalents().add(otherTradesRow);
							addEObjectsToMap(otherTradesRow, tradesRow);
						}
					}
				} else {
					final Object o = parent.eGet(ref);
					if (o instanceof TradesRow otherTradesRow) {
						rowDataToRow.put(otherTradesRow, tradesRow);
						otherTradesRow.getInputEquivalents().add(tradesRow);
						addEObjectsToMap(otherTradesRow, tradesRow);
					}
				}
			}
		}
	}

	private DischargeSlot getDischargeSlot(final Cargo cargo) {
		return (DischargeSlot) (cargo.getSortedSlots().get(cargo.getSortedSlots().size() - 1));
	}

	private LoadSlot getLoadSlot(final Cargo cargo) {
		return (LoadSlot) (cargo.getSortedSlots().get(0));
	}

	public void setlngScenarioModel(final LNGScenarioModel lngScenarioModel) {
		this.lngScenarioModel = lngScenarioModel;
	}

	public TradesTableRoot getTradesTableRoot() {
		return tradesTableRoot;
	}

	public void setTradesTableRoot(final TradesTableRoot tradesTableRoot) {
		this.tradesTableRoot = tradesTableRoot;
	}

	public void createColumns() {
		columnBlockManager.setColumnFactory(new EObjectTableViewerColumnFactory(scenarioViewer) {
			@Override
			public GridViewerColumn createColumn(ColumnHandler handler) {
				GridViewerColumn gvc = super.createColumn(handler);
				gvc.getColumn().setResizeable(true);

				GridViewerHelper.configureLookAndFeel(gvc, GridViewerHelper.FLAGS_ROW_HOVER);
				return gvc;
			}
		});
		columnBlockManager.setGrid(scenarioViewer.getGrid());
		final EClass eclass = (EClass) customRowPackage.getEClassifier(CUSTOMISABLE_ROW_NAME);
		// Find any shared column factories and install.
		final Map<String, ITradesColumnFactory> handlerMap = new HashMap<>();
		if (columnFactoryExtensions != null) {
			for (final ITradeBasedBulkColumnFactoryExtension ext : columnFactoryExtensions) {
				final String handlerID = ext.getHandlerID();
				handlerMap.put(handlerID, ext.getFactory());
			}
		}

		// Now find the column definitions themselves.
		if (columnExtensions != null) {

			for (final ITradeBasedBulkColumnExtension ext : columnExtensions) {
				ITradesColumnFactory factory;
				if (ext.getHandlerID() != null) {
					factory = handlerMap.get(ext.getHandlerID());
				} else {
					factory = ext.getFactory();
				}
				if (factory != null) {
					factory.registerColumn(ext.getColumnID(), columnManager, getScenarioEditingLocation(), eclass, this);
				}
			}
		}

		// Create the actual columns instances.
		columnManager.addColumns("TRADES_TABLE", columnBlockManager);
		final List<Integer> columnOrderList = new LinkedList<>();
		for (final String groupName : orderedColumnGroupNames) {
			for (final ColumnHandler handler : columnBlockManager.getHandlerGroup(groupName)) {
				GridViewerColumn gvcolumn = handler.column;
				if (gvcolumn == null) {
					gvcolumn = handler.createColumn();
				}
				final GridColumn column = gvcolumn.getColumn();
				column.setMinimumWidth(70);
				handlerToColumnMap.put(handler, column);
				if (column.getData(EObjectTableViewer.COLUMN_COMPARABLE_PROVIDER) != null) {
					scenarioViewer.getSortingSupport().addSortableColumn(scenarioViewer, handler.column, column);
				}
				if (columnFilters.isColumnVisible(handler.block)) {
					column.setVisible(true);
					handler.block.setUserVisible(true);
				} else {
					column.setVisible(false);
					handler.block.setUserVisible(false);
				}
				handler.block.setViewState(false, false);
				columnOrderList.add(scenarioViewer.getGrid().indexOf(column));
			}
		}

		scenarioViewer.getGrid().setColumnOrder(CollectionsUtil.integersToIntArray(columnOrderList));

		scenarioViewer.getGrid().recalculateHeader();
		setColumnsImmovable();

		columnBlockManager.initFromPreferences(COLUMN_VISIBILITY_KEY, preferenceStore);
	}

	private void setColumnsImmovable() {
		if (scenarioViewer != null) {
			for (final GridColumn column : scenarioViewer.getGrid().getColumns()) {
				column.setMoveable(false);
			}
		}
	}

	public void setColumnsVisibility() {
		for (final String groupName : orderedColumnGroupNames) {
			for (final ColumnHandler handler : columnBlockManager.getHandlerGroup(groupName)) {
				final GridColumn column = handlerToColumnMap.get(handler);
				if (columnFilters.isColumnVisible(handler.block)) {
					column.setVisible(true);
				} else {
					column.setVisible(false);
				}
			}
		}
		scenarioViewer.getGrid().recalculateHeader();
	}

	
	private Set<String> getFiltersOpenContracts() {
		final IPreferenceStore cargoEditorPreferenceStore = CargoEditorPlugin.getPlugin().getPreferenceStore();
		final Set<String> filtersOpenContracts = new HashSet<>();
		final IPropertyChangeListener propertyChangeListener = event -> {
			final String property = event.getProperty();
			if (PreferenceConstants.P_CONTRACTS_TO_CONSIDER_OPEN.equals(property)) {
				final String value = cargoEditorPreferenceStore.getString(property);
				filtersOpenContracts.clear();
				if (value != null) {
					if (value.contains(",")) {
						final String[] split = value.split(",");
						for (final String str : split) {
							filtersOpenContracts.add(str.trim().toLowerCase());
						}
					} else {
						filtersOpenContracts.add(value.trim().toLowerCase());
					}
					filtersOpenContracts.remove("");
				}
				viewer.refresh();
			}
		};
		cargoEditorPreferenceStore.addPropertyChangeListener(propertyChangeListener);
		final String value = cargoEditorPreferenceStore.getString(PreferenceConstants.P_CONTRACTS_TO_CONSIDER_OPEN);
		if (value != null) {
			if (value.contains(",")) {
				final String[] split = value.split(",");
				for (final String str : split) {
					filtersOpenContracts.add(str.trim().toLowerCase());
				}
			} else {
				filtersOpenContracts.add(value.trim().toLowerCase());
			}
			filtersOpenContracts.remove("");
		}
		return filtersOpenContracts;
	}

	private void extendRowModel() {

		final EClass eclass = (EClass) customRowPackage.getEClassifier(CUSTOMISABLE_ROW_NAME);

		// Find any shared column factories and install.
		final Map<String, ITradesBasedRowModelTransformerFactory> handlerMap = new HashMap<>();
		if (rowModelFactoryExtensions != null) {
			for (final ITradeBasedBulkRowModelFactoryExtension ext : rowModelFactoryExtensions) {
				final String handlerID = ext.getHandlerID();
				handlerMap.put(handlerID, ext.getFactory());
			}
		}

		// Now find the column definitions themselves.
		if (rowModelExtensions != null) {

			for (final ITradeBasedBulkRowModelExtension ext : rowModelExtensions) {
				ITradesBasedRowModelTransformerFactory factory;
				if (ext.getHandlerID() != null) {
					final String handlerId = ext.getHandlerID();
					factory = handlerMap.get(handlerId);
				} else {
					factory = ext.getFactory();
				}
				if (factory != null) {
					factory.extendRowModel(ext.getRowExtensionID(), eclass);
				}
			}
		}
	}

	private void transformRowWithExtensions(final Cargo cargo, final TradesRow tradesRow) {
		final EClass eclass = (EClass) customRowPackage.getEClassifier(CUSTOMISABLE_ROW_NAME);
		if (rowTransformerExtensions != null) {

			for (final ITradeBasedBulkRowTransformerExtension ext : rowTransformerExtensions) {
				ITradesRowTransformerFactory factory;
				if (ext.getHandlerID() != null) {
					final String handlerId = ext.getHandlerID();
					factory = rowTransformerHandlers.get(handlerId);
				} else {
					factory = ext.getFactory();
				}
				if (factory != null) {
					factory.transformRowData(tradesTableRoot, lngScenarioModel, eclass, cargo, tradesRow);
				}
			}
		}

	}

	private void buildRowTransformerHandlers(final Map<String, ITradesRowTransformerFactory> rowTransformerHandlers) {
		if (rowTransformerFactoryExtensions != null) {
			for (final ITradeBasedBulkRowTransformerFactoryExtension ext : rowTransformerFactoryExtensions) {
				final String handlerID = ext.getHandlerID();
				rowTransformerHandlers.put(handlerID, ext.getFactory());
			}
		}
	}

	@Override
	protected ScenarioTableViewer constructViewer(final Composite parent) {
		final ScenarioTableViewer viewer = new BulkTradesTableViewer(parent, SWT.BORDER | SWT.H_SCROLL | SWT.V_SCROLL, scenarioEditingLocation);

		// Middle-click to replicate value
		viewer.getGrid().addMouseListener(new MouseListener() {

			@Override
			public void mouseUp(final MouseEvent e) {

			}

			@Override
			public void mouseDown(final MouseEvent e) {

				if (e.button == 2) {

					final Point focusCell = viewer.getGrid().getFocusCell();
					if (focusCell != null) {
						final Point[] selectedCells = viewer.getGrid().getCellSelection();
						if (selectedCells.length != 0) {
							int minX = 0;
							int minY = 0;
							int maxX = 0;
							int maxY = 0;
							for (int i = 0; i < selectedCells.length; ++i) {
								final Point p = selectedCells[i];
								if (i == 0) {
									minX = maxX = p.x;
									minY = maxY = p.y;
								} else {
									if (p.x < minX) {
										minX = p.x;
									}
									if (p.x > maxX) {
										maxX = p.x;
									}
									if (p.y < minY) {
										minY = p.y;
									}
									if (p.y > maxY) {
										maxY = p.y;
									}
								}
							}
							// Single column!
							if ((maxX - minX) == 0 && (maxY - minY) != 0) {
								final GridColumn column = viewer.getGrid().getColumn(minX);
								final ColumnHandler h = (ColumnHandler) column.getData(ColumnHandler.COLUMN_HANDLER);
								if (h != null) {
									final GridItem focusItm = viewer.getGrid().getFocusItem();
									final IEMFPath path = (IEMFPath) column.getData(EObjectTableViewer.COLUMN_PATH);

									final Object referenceObject = path.get((EObject) focusItm.getData());
									if (h.getManipulator().canEdit(referenceObject)) {
										final Object referenceValue = h.getManipulator().getValue(referenceObject);

										for (final Point p : selectedCells) {
											final GridItem itm = viewer.getGrid().getItem(p.y);
											if (itm == focusItm) {
												continue;
											}
											final Object targetObject = path.get((EObject) itm.getData());
											if (h.getManipulator().canEdit(targetObject)) {
												h.getManipulator().setValue(targetObject, referenceValue);
											}
										}
									}
								}
							}
						}
					}

				}
			}

			@Override
			public void mouseDoubleClick(final MouseEvent e) {

			}
		});

		// viewer.getSortingSupport().setSortOnlyOnSelect(true);

		final MenuManager mgr = new MenuManager();

		contextMenuExtensions = TradesTableContextMenuExtensionUtil.getContextMenuExtensions();

		viewer.getGrid().addMenuDetectListener(new MenuDetectListener() {

			private Menu menu;

			private void populateSingleSelectionMenu(final GridItem item, final GridColumn column) {
				if (item == null) {
					return;
				}

				final Object data = item.getData();
				if (data instanceof TradesRow tradesRow) {
					if (menu == null) {
						menu = mgr.createContextMenu(scenarioViewer.getGrid());
					}
					mgr.removeAll();
					{
						final IContributionItem[] items = mgr.getItems();
						mgr.removeAll();
						for (final IContributionItem mItem : items) {
							mItem.dispose();
						}
					}
					String blockType = null;
					final ColumnHandler handler = (ColumnHandler) column.getData(ColumnHandler.COLUMN_HANDLER);
					if (handler != null) {
						blockType = handler.block.getblockType();
					}

					if (ITradesColumnFactory.isLoadGroup(blockType)) {
						if (tradesRow.getLoadSlot() != null) {
							final IMenuListener listener = menuHelper.createLoadSlotMenuListener(Collections.singletonList(tradesRow.getLoadSlot()), 0);
							listener.menuAboutToShow(mgr);
							if (contextMenuExtensions != null) {
								final Slot slot = tradesRow.getLoadSlot();
								for (final ITradesTableContextMenuExtension ext : contextMenuExtensions) {
									ext.contributeToMenu(scenarioEditingLocation, slot, mgr);
								}
							}
						} else {
							final IMenuListener listener = menuHelper.createDischargeSlotMenuListener(Collections.singletonList(tradesRow.getDischargeSlot()), 0);
							listener.menuAboutToShow(mgr);
							if (contextMenuExtensions != null) {
								final Slot slot = tradesRow.getDischargeSlot();
								for (final ITradesTableContextMenuExtension ext : contextMenuExtensions) {
									ext.contributeToMenu(scenarioEditingLocation, slot, mgr);
								}
							}
						}
					}
					if (ITradesColumnFactory.isDischargeGroup(blockType)) {
						if (tradesRow.getDischargeSlot() != null) {
							final IMenuListener listener = menuHelper.createDischargeSlotMenuListener(Collections.singletonList(tradesRow.getDischargeSlot()), 0);
							listener.menuAboutToShow(mgr);
							if (contextMenuExtensions != null) {
								final Slot slot = tradesRow.getDischargeSlot();
								for (final ITradesTableContextMenuExtension ext : contextMenuExtensions) {
									ext.contributeToMenu(scenarioEditingLocation, slot, mgr);
								}
							}
						} else if (tradesRow.getLoadSlot() != null) {
							final IMenuListener listener = menuHelper.createLoadSlotMenuListener(Collections.singletonList(tradesRow.getLoadSlot()), 0);
							listener.menuAboutToShow(mgr);
							if (contextMenuExtensions != null) {
								final Slot slot = tradesRow.getLoadSlot();
								for (final ITradesTableContextMenuExtension ext : contextMenuExtensions) {
									ext.contributeToMenu(scenarioEditingLocation, slot, mgr);
								}
							}
						}
					}

					menu.setVisible(true);
				}
			}

			@Override
			public void menuDetected(final MenuDetectEvent e) {

				if (locked) {
					return;
				}

				final Grid grid = getScenarioViewer().getGrid();

				final Point mousePoint = grid.toControl(new Point(e.x, e.y));
				final GridColumn column = grid.getColumn(mousePoint);

				final IStructuredSelection selection = (IStructuredSelection) getScenarioViewer().getSelection();

				if (selection.size() <= 1) {
					populateSingleSelectionMenu(grid.getItem(mousePoint), column);
				} else {
					final Set<Cargo> cargoes = new HashSet<>();
					final Set<LoadSlot> loads = new HashSet<>();
					final Set<DischargeSlot> discharges = new HashSet<>();
					String blockType = null;
					final ColumnHandler handler = (ColumnHandler) column.getData(ColumnHandler.COLUMN_HANDLER);
					if (handler != null) {
						blockType = handler.block.getblockType();
					}
					boolean loadSide = ITradesColumnFactory.isLoadGroup(blockType);
					boolean dischargeSide = ITradesColumnFactory.isDischargeGroup(blockType);
					boolean vesselCol = ITradesColumnFactory.isVesselColumn(handler.block.blockID);
					for (final Object item : selection.toList()) {
						TradesRow tradesRow = (TradesRow) item;
						final Cargo cargo = tradesRow.getCargo();
						if (cargo != null) {
							cargoes.add(cargo);
						}
						if (loadSide && tradesRow.getLoadSlot() != null) {
							loads.add(tradesRow.getLoadSlot());
						}
						if (dischargeSide && tradesRow.getDischargeSlot() != null) {
							discharges.add(tradesRow.getDischargeSlot());
						}
					}

					populateMultipleSelectionMenu(cargoes, loads, discharges, selection, loadSide, dischargeSide, vesselCol);
				}
			}

			private void populateMultipleSelectionMenu(final Set<Cargo> cargoes, Set<LoadSlot> loads, Set<DischargeSlot> discharges, final IStructuredSelection selection, boolean loadSide,
					boolean dischargeSide, boolean vesselCol) {
				if (menu == null) {
					menu = mgr.createContextMenu(scenarioViewer.getGrid());
				}
				mgr.removeAll();
				{
					final IContributionItem[] items = mgr.getItems();
					mgr.removeAll();
					for (final IContributionItem item : items) {
						item.dispose();
					}
				}
				final IMenuListener listener = menuHelper.createMultipleSelectionMenuListener(cargoes, loads, discharges, loadSide, dischargeSide, vesselCol);
				listener.menuAboutToShow(mgr);

				if (contextMenuExtensions != null) {
					for (final ITradesTableContextMenuExtension ext : contextMenuExtensions) {
						ext.contributeToMenu(scenarioEditingLocation, selection, mgr);
					}
				}

				menu.setVisible(true);
			}
		});

		// GridViewerHelper.configureLookAndFeel(viewer);

		return viewer;
	}

	@Override
	public ScenarioTableViewer createViewer(final Composite parent) {
		if (scenarioViewer == null) {
			scenarioViewer = constructViewer(parent);
			// scenarioViewer.setAutoPreferredHeight(true);
			scenarioViewer.getGrid().setCellSelectionEnabled(true);
			enableOpenListener();

			filterField.setFilterSupport(scenarioViewer.getFilterSupport());

			final ColumnViewerEditorActivationStrategy actSupport = new ColumnViewerEditorActivationStrategy(scenarioViewer) {
				@Override
				protected boolean isEditorActivationEvent(final ColumnViewerEditorActivationEvent event) {
					final boolean activate = event.eventType == ColumnViewerEditorActivationEvent.MOUSE_DOUBLE_CLICK_SELECTION //
							|| event.eventType == ColumnViewerEditorActivationEvent.PROGRAMMATIC //
							|| event.eventType == ColumnViewerEditorActivationEvent.TRAVERSAL;
					if (activate) {
						return true;
					}
					if (event.eventType == ColumnViewerEditorActivationEvent.KEY_PRESSED) {
						if (event.keyCode == KeyLookupFactory.getDefault().formalKeyLookup(IKeyLookup.ENTER_NAME)) {
							return true;
						}
					}
					return false;
				}
			};

			GridViewerEditor.create(scenarioViewer, actSupport, ColumnViewerEditor.TABBING_MOVE_TO_ROW_NEIGHBOR | ColumnViewerEditor.KEYBOARD_ACTIVATION);

			return scenarioViewer;
		} else

		{
			throw new RuntimeException("Did not expect two calls to createViewer()");
		}

	}

	@Override
	protected void enableOpenListener() {
	}

	@Override
	public ScenarioTableViewer getScenarioViewer() {
		return super.getScenarioViewer();
	}

	public void setLocked(final boolean locked) {

		this.locked = locked;
		if (promptToolbarEditor != null) {
			promptToolbarEditor.setLocked(locked);
		}

		super.setLocked(locked);
	}

	public void setInput(final TradesTableRoot tradesTableRoot2) {
		viewer.setInput(tradesTableRoot2);
		// Trigger sorting on the load date column to make this the initial sort column.
		{
			for (final GridColumn gc : scenarioViewer.getGrid().getColumns()) {
				final ColumnHandler handler = (ColumnHandler) gc.getData(ColumnHandler.COLUMN_HANDLER);
				if (handler != null) {
					if ("com.mmxlabs.models.lng.cargo.editor.bulk.columns.TradesBasedColumnFactory.l-date".equals(handler.block.blockID)) {
						if ("Date".equals(handler.title)) {
							final Listener[] listeners = gc.getListeners(SWT.Selection);
							for (final Listener l : listeners) {
								final org.eclipse.swt.widgets.Event e = new org.eclipse.swt.widgets.Event();
								e.type = SWT.Selection;
								e.widget = gc;
								l.handleEvent(e);
							}
						}
					}
				}
			}
		}
	}

	protected IAddContext getAddContext(final EReference containment) {
		return new IAddContext() {
			@Override
			public MMXRootObject getRootObject() {
				return scenarioEditingLocation.getRootObject();
			}

			@Override
			public EReference getContainment() {
				return containment;
			}

			@Override
			public EObject getContainer() {
				return scenarioViewer.getCurrentContainer();
			}

			@Override
			public ICommandHandler getCommandHandler() {
				return scenarioEditingLocation.getDefaultCommandHandler();
			}

			@Override
			public IScenarioEditingLocation getEditorPart() {
				return scenarioEditingLocation;
			}

			@Override
			public @Nullable Collection<@NonNull EObject> getCurrentSelection() {
				return SelectionHelper.convertToList(viewer.getSelection(), EObject.class);
			}
		};
	}


	public class BulkTradesTableViewer extends ScenarioTableViewer implements IFilterable {
		private BulkTradesTableViewer(Composite parent, int style, IScenarioEditingLocation part) {
			super(parent, style, part);
		}

		@Override
		public void addSelectionChangedListener(ISelectionChangedListener listener) {
			if (listener instanceof JointModelEditorPartSelectionChangedListener) {
				return;
			}
			super.addSelectionChangedListener(listener);
		}

		@Override
		public void refresh() {
			// TODO Auto-generated method stub
			super.refresh();
			for (final GridColumn c : this.getGrid().getColumns()) {
				c.setMinimumWidth(70);
			}
			this.getGrid().recalculateHeader();
		}

		@Override
		public void init(final AdapterFactory adapterFactory, final ModelReference modelReference, final EReference... path) {
			super.init(adapterFactory, modelReference, path);

			init(new ITreeContentProvider() {
				@Override
				public void dispose() {

				}

				@Override
				public Object[] getElements(final Object inputElement) {

					setCargoes(tradesTableRoot, lngScenarioModel);
					return tradesTableRoot.getTradesRows().toArray();
				}

				@Override
				public void inputChanged(final Viewer viewer, final Object oldInput, final Object newInput) {

				}

				@Override
				public Object[] getChildren(final Object parentElement) {
					return null;
				}

				@Override
				public Object getParent(final Object element) {
					return null;
				}

				@Override
				public boolean hasChildren(final Object element) {
					return false;
				}

			}, modelReference);
		}

		@Override
		protected EObjectTableViewerValidationSupport createValidationSupport() {
			return new EObjectTableViewerValidationSupport(this) {

				@Override
				public EObject getElementForValidationTarget(final EObject source) {
					return getElementForNotificationTarget(source);
				}

				@Override
				protected void processStatus(final IStatus status, final boolean update) {
					super.processStatus(status, update);
					if (!isBusy()) {
						refresh();
					}
				}

				protected void updateObject(final EObject object, final IStatus status, final boolean update) {
					if (object == null) {
						return;
					}
					TradesRow tradesRow = null;
					if (object instanceof TradesRow otherTradesRow) {
						tradesRow = rowDataToRow.get(otherTradesRow);
					}

					if (tradesRow != null) {
						getValidationSupport().setStatus(tradesRow, status);
					}
				}
			};
		}

		/**
		 * Overridden method to convert internal RowData objects into a collection of
		 * EMF Objects
		 */
		protected void updateSelection(final ISelection selection) {

			if (selection instanceof IStructuredSelection) {
				final IStructuredSelection originalSelection = (IStructuredSelection) selection;

				final List<Object> selectedObjects = new LinkedList<>();

				final Iterator<?> itr = originalSelection.iterator();
				while (itr.hasNext()) {
					final Object obj = itr.next();
					if (obj instanceof TradesRow tradesRow) {
						selectedObjects.addAll(tradesRow.getInputEquivalents());
					}
				}

				super.updateSelection(new StructuredSelection(selectedObjects));
			} else {
				super.updateSelection(selection);
			}
		}

		@Override
		public EObject getElementForNotificationTarget(final EObject source) {

			if (rowDataToRow.keySet().contains(source)) {
				return source;
			}

			return super.getElementForNotificationTarget(source);
		}

		@Override
		public StructuredViewerFilterManager getFilterManager() {
			return filterManager;
		}
	}

	private class AddAction extends DefaultMenuCreatorAction {

		private final Action[] actions;

		public AddAction(final String label, final Action... actions) {
			super(label);
			this.actions = actions;
		}

		/**
		 * Subclasses should fill their menu with actions here.
		 * 
		 * @param menu the menu which is about to be displayed
		 */
		protected void populate(final Menu menu) {
			{
				final DuplicateAction result = new DuplicateAction(getScenarioEditingLocation());
				// Translate into real objects, not just row object!
				final List<Object> selectedObjects = new LinkedList<>();
				if (scenarioViewer.getSelection() instanceof IStructuredSelection) {
					final IStructuredSelection structuredSelection = (IStructuredSelection) scenarioViewer.getSelection();

					final Iterator<?> itr = structuredSelection.iterator();
					while (itr.hasNext()) {
						final Object o = itr.next();
						if (o instanceof TradesRow tradesRow) {
							// TODO: Check logic, a row may contain two distinct items
							if (tradesRow.getCargo() != null) {
								selectedObjects.add(tradesRow.getCargo());
								continue;
							}
							if (tradesRow.getLoadSlot() != null) {
								selectedObjects.add(tradesRow.getLoadSlot());
							}
							if (tradesRow.getDischargeSlot() != null) {
								selectedObjects.add(tradesRow.getDischargeSlot());
							}
						}
					}
				}

				result.selectionChanged(new SelectionChangedEvent(scenarioViewer, new StructuredSelection(selectedObjects)));
				addActionToMenu(result, menu);
			}

			final CargoModel cargoModel = ScenarioModelUtil.getCargoModel(scenarioEditingLocation.getScenarioDataProvider());
			final CommandStack commandStack = scenarioEditingLocation.getEditingDomain().getCommandStack();

			TradesRow discoveredRowData = null;
			final ISelection selection = getScenarioViewer().getSelection();
			if (selection instanceof IStructuredSelection structuredSelection) {
				final Object firstElement = structuredSelection.getFirstElement();
				if (firstElement instanceof TradesRow tradesRow) {
					discoveredRowData = tradesRow;
				}
			}
			final TradesRow referenceRowData = discoveredRowData;
			{
				final Action newLoad = new Action("Cargo") {
					public void run() {

						final List<Command> setCommands = new LinkedList<>();

						final Cargo newCargo = cec.createNewCargo(setCommands, cargoModel);

						final LoadSlot newLoad = cec.createNewLoad(setCommands, cargoModel, false);
						initialiseSlot(newLoad, true, referenceRowData);

						final DischargeSlot newDischarge = cec.createNewDischarge(setCommands, cargoModel, false);
						initialiseSlot(newDischarge, false, referenceRowData);

						newLoad.setCargo(newCargo);
						newDischarge.setCargo(newCargo);

						newCargo.setAllowRewiring(true);
						final CompoundCommand cmd = new CompoundCommand("Cargo");
						setCommands.forEach(cmd::append);

						// scenarioViewer .getSortingSupport().setSortOnlyOnSelect(false);
						scenarioEditingLocation.getEditingDomain().getCommandStack().execute(cmd);
						// scenarioViewer .getSortingSupport().setSortOnlyOnSelect(true);
					}
				};
				addActionToMenu(newLoad, menu);
			}
			if (LicenseFeatures.isPermitted(KnownFeatures.FEATURE_COMPLEX_CARGO)) {

				final Action newLoad = new Action("LDD cargo") {
					@Override
					public void run() {

						final List<Command> setCommands = new LinkedList<>();

						final Cargo newCargo = cec.createNewCargo(setCommands, cargoModel);

						final LoadSlot newLoad = cec.createNewLoad(setCommands, cargoModel, false);
						initialiseSlot(newLoad, true, referenceRowData);

						final DischargeSlot newDischarge = cec.createNewDischarge(setCommands, cargoModel, false);
						initialiseSlot(newDischarge, false, referenceRowData);

						final DischargeSlot newDischarge2 = cec.createNewDischarge(setCommands, cargoModel, false);
						initialiseSlot(newDischarge2, false, referenceRowData);
						if (newDischarge2.getWindowStart() != null) {
							newDischarge2.setWindowStart(newDischarge2.getWindowStart().plusDays(7));
						}

						newLoad.setCargo(newCargo);
						newDischarge.setCargo(newCargo);
						newDischarge2.setCargo(newCargo);

						newCargo.setAllowRewiring(true);
						final CompoundCommand cmd = new CompoundCommand("Cargo");
						setCommands.forEach(cmd::append);

						scenarioEditingLocation.getEditingDomain().getCommandStack().execute(cmd);
					}
				};
				addActionToMenu(newLoad, menu);
			}
			{
				final Action newLoad = new Action("FOB Purchase") {
					public void run() {

						final List<Command> setCommands = new LinkedList<>();

						final LoadSlot newLoad = cec.createNewLoad(setCommands, cargoModel, false);
						initialiseSlot(newLoad, true, referenceRowData);

						final CompoundCommand cmd = new CompoundCommand("FOB Purchase");
						setCommands.forEach(c -> cmd.append(c));

						commandStack.execute(cmd);
						DetailCompositeDialogUtil.editSingleObjectWithUndoOnCancel(getScenarioEditingLocation(), newLoad, commandStack.getMostRecentCommand());
					}
				};
				addActionToMenu(newLoad, menu);
			}
			{
				final Action newDESPurchase = new Action("DES Purchase") {
					public void run() {

						final List<Command> setCommands = new LinkedList<>();

						final LoadSlot newLoad = cec.createNewLoad(setCommands, cargoModel, true);
						initialiseSlot(newLoad, true, referenceRowData);

						final CompoundCommand cmd = new CompoundCommand("DES Purchase");
						setCommands.forEach(c -> cmd.append(c));
						commandStack.execute(cmd);
						DetailCompositeDialogUtil.editSingleObjectWithUndoOnCancel(getScenarioEditingLocation(), newLoad, commandStack.getMostRecentCommand());
					}
				};
				addActionToMenu(newDESPurchase, menu);
			}
			{
				final Action newDischarge = new Action("DES Sale") {
					public void run() {

						final List<Command> setCommands = new LinkedList<>();

						final DischargeSlot newDischarge = cec.createNewDischarge(setCommands, cargoModel, false);
						initialiseSlot(newDischarge, false, referenceRowData);

						final CompoundCommand cmd = new CompoundCommand("DES Sale");
						setCommands.forEach(c -> cmd.append(c));

						commandStack.execute(cmd);
						DetailCompositeDialogUtil.editSingleObjectWithUndoOnCancel(getScenarioEditingLocation(), newDischarge, commandStack.getMostRecentCommand());
					}
				};

				addActionToMenu(newDischarge, menu);
			}
			{
				final Action newFOBSale = new Action("FOB Sale") {

					@Override
					public void run() {

						final List<Command> setCommands = new LinkedList<>();

						final DischargeSlot newDischarge = cec.createNewDischarge(setCommands, cargoModel, true);
						initialiseSlot(newDischarge, false, referenceRowData);

						final CompoundCommand cmd = new CompoundCommand("FOB Sale");
						setCommands.forEach(cmd::append);

						commandStack.execute(cmd);
						DetailCompositeDialogUtil.editSingleObjectWithUndoOnCancel(getScenarioEditingLocation(), newDischarge, commandStack.getMostRecentCommand());
					}
				};
				addActionToMenu(newFOBSale, menu);
			}

			if (actions != null) {
				for (final Action action : actions) {
					addActionToMenu(action, menu);
				}
			}
		}

		private final void initialiseSlot(final Slot newSlot, final boolean isLoad, final TradesRow referenceRowData) {
			newSlot.eSet(MMXCorePackage.eINSTANCE.getUUIDObject_Uuid(), EcoreUtil.generateUUID());
			newSlot.setOptional(false);
			newSlot.setName("");
			// Set window so that via default sorting inserts new slot at current table
			// position
			if (referenceRowData != null) {
				final Slot primarySortSlot = isLoad ? referenceRowData.getLoadSlot() : referenceRowData.getDischargeSlot();
				final Slot secondarySortSlot = isLoad ? referenceRowData.getDischargeSlot() : referenceRowData.getLoadSlot();
				if (primarySortSlot != null) {
					newSlot.setWindowStart(primarySortSlot.getWindowStart());
					newSlot.setPort(primarySortSlot.getPort());
				} else if (secondarySortSlot != null) {
					newSlot.setWindowStart(secondarySortSlot.getWindowStart());
				}
			}
		}

	}

	class BulkTradesTablePaneFilterMenuAction extends DefaultMenuCreatorAction {
		

		/**
		 * A holder for a menu list of filter actions on different fields for the trades
		 * wiring table.
		 * 
		 * @param label The label to show in the UI for this menu.
		 */
		public BulkTradesTablePaneFilterMenuAction(final String label) {
			super(label);
		}

		/**
		 * Add the filterable fields to the menu for this item.
		 */
		@Override
		protected void populate(final Menu menu) {
			final CommercialModel commercialModel = lngScenarioModel.getReferenceModel().getCommercialModel();
			final FleetModel fleetModel = lngScenarioModel.getReferenceModel().getFleetModel();
			final CargoModel cargoModel = ScenarioModelUtil.getCargoModel(scenarioEditingLocation.getScenarioDataProvider());

			final EMFPath purchaseContractPath = new TradesRowEMFPath(false, TradesRowEMFPath.Type.LOAD, CargoPackage.Literals.SLOT__CONTRACT);
			final EMFPath salesContractPath = new TradesRowEMFPath(false, TradesRowEMFPath.Type.DISCHARGE, CargoPackage.Literals.SLOT__CONTRACT);

			final Action clearAction = new Action("Clear All Filters") {
				@Override
				public void run() {
					filterManager.removeFilters();
				}
			};

			addActionToMenu(clearAction, menu);

			addActionToMenu(new EMFPathFilterAction(filterManager, "Purchase Contracts", commercialModel, CommercialPackage.Literals.COMMERCIAL_MODEL__PURCHASE_CONTRACTS, purchaseContractPath), menu);
			addActionToMenu(new EMFPathFilterAction(filterManager, "Sale Contracts", commercialModel, CommercialPackage.Literals.COMMERCIAL_MODEL__SALES_CONTRACTS, salesContractPath), menu);
			addActionToMenu(new TrimmedVesselFilterAction(filterManager, ScenarioModelUtil.getCargoModel(scenarioEditingLocation.getScenarioDataProvider())), menu);
			addActionToMenu(new EntityFilterAction(filterManager, commercialModel), menu);

			final OpenCargoFilterAction ocfa = new OpenCargoFilterAction(filterManager, getFiltersOpenContracts());

			addActionToMenu(ocfa, menu);

			final DefaultMenuCreatorAction dmcaShippedCargos = new ShippedCargoFilterAction(filterManager);
			addActionToMenu(dmcaShippedCargos, menu);			
			
			addActionToMenu(new LoadPortFilterAction(filterManager, cargoModel), menu);
			addActionToMenu(new DischargePortFilterAction(filterManager, cargoModel), menu);

			final DefaultMenuCreatorAction dmcaTimePeriod = new PeriodFilterAction(filterManager, scenarioStart, scenarioEnd);

			addActionToMenu(dmcaTimePeriod, menu);
			
			if (extraFiltersProvider != null) {
				for (final DefaultMenuCreatorAction edmca : extraFiltersProvider.getExtraMenuActions(filterManager.getViewer())) {
					addActionToMenu(edmca, menu);
				}
			}

		}
	}
}
