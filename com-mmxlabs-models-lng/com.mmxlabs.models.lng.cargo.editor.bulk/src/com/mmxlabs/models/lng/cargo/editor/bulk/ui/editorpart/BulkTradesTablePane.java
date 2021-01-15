/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2021
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
import java.util.LinkedHashSet;
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
import org.eclipse.jface.action.ActionContributionItem;
import org.eclipse.jface.action.GroupMarker;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.action.IContributionItem;
import org.eclipse.jface.action.IMenuCreator;
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
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.ui.IActionBars;
import org.eclipse.ui.ISharedImages;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.actions.ActionFactory;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.service.event.EventHandler;

import com.google.inject.Guice;
import com.google.inject.Inject;
import com.google.inject.Injector;
import com.mmxlabs.common.CollectionsUtil;
import com.mmxlabs.license.features.LicenseFeatures;
import com.mmxlabs.models.lng.cargo.Cargo;
import com.mmxlabs.models.lng.cargo.CargoFactory;
import com.mmxlabs.models.lng.cargo.CargoModel;
import com.mmxlabs.models.lng.cargo.CargoPackage;
import com.mmxlabs.models.lng.cargo.DischargeSlot;
import com.mmxlabs.models.lng.cargo.LoadSlot;
import com.mmxlabs.models.lng.cargo.Slot;
import com.mmxlabs.models.lng.cargo.editor.PreferenceConstants;
import com.mmxlabs.models.lng.cargo.editor.bulk.cargobulkeditor.CargoBulkEditorPackage;
import com.mmxlabs.models.lng.cargo.editor.bulk.cargobulkeditor.Row;
import com.mmxlabs.models.lng.cargo.editor.bulk.cargobulkeditor.Table;
import com.mmxlabs.models.lng.cargo.editor.bulk.columnfilter.extmodule.ITradeBasedBulkColumnFilterExtension;
import com.mmxlabs.models.lng.cargo.editor.bulk.columns.extmodule.ITradeBasedBulkColumnExtension;
import com.mmxlabs.models.lng.cargo.editor.bulk.columns.extmodule.ITradeBasedBulkColumnFactoryExtension;
import com.mmxlabs.models.lng.cargo.editor.bulk.columns.extmodule.TradeBasedBulkModule;
import com.mmxlabs.models.lng.cargo.editor.bulk.rowmodel.extmodule.ITradeBasedBulkRowModelExtension;
import com.mmxlabs.models.lng.cargo.editor.bulk.rowmodel.extmodule.ITradeBasedBulkRowModelFactoryExtension;
import com.mmxlabs.models.lng.cargo.editor.bulk.rowtransformer.extmodule.ITradeBasedBulkRowTransformerExtension;
import com.mmxlabs.models.lng.cargo.editor.bulk.rowtransformer.extmodule.ITradeBasedBulkRowTransformerFactoryExtension;
import com.mmxlabs.models.lng.cargo.editor.bulk.views.CargoTradesBasedFilterHandler;
import com.mmxlabs.models.lng.cargo.editor.bulk.views.DischargePortTradesBasedFilterHandler;
import com.mmxlabs.models.lng.cargo.editor.bulk.views.ITradesBasedFilterHandler;
import com.mmxlabs.models.lng.cargo.editor.bulk.views.ITradesBasedRowModelTransformerFactory;
import com.mmxlabs.models.lng.cargo.editor.bulk.views.ITradesColumnFactory;
import com.mmxlabs.models.lng.cargo.editor.bulk.views.ITradesRowTransformerFactory;
import com.mmxlabs.models.lng.cargo.editor.bulk.views.LoadPortTradesBasedFilterHandler;
import com.mmxlabs.models.lng.cargo.editor.bulk.views.PurchaseContractTradesBasedFilterHandler;
import com.mmxlabs.models.lng.cargo.editor.bulk.views.SalesContractTradesBasedFilterHandler;
import com.mmxlabs.models.lng.cargo.editor.bulk.views.ShippedCargoBasedFilterHandler;
import com.mmxlabs.models.lng.cargo.editor.bulk.views.TimePeriodTradesBasedFilterHandler;
import com.mmxlabs.models.lng.cargo.editor.bulk.views.TradesBasedColumnFactory;
import com.mmxlabs.models.lng.cargo.editor.bulk.views.VesselsTradesBasedFilterHandler;
import com.mmxlabs.models.lng.cargo.presentation.CargoEditorPlugin;
import com.mmxlabs.models.lng.cargo.ui.editorpart.PromptToolbarEditor;
import com.mmxlabs.models.lng.cargo.ui.editorpart.actions.CargoEditingCommands;
import com.mmxlabs.models.lng.cargo.ui.editorpart.actions.CargoEditorMenuHelper;
import com.mmxlabs.models.lng.cargo.ui.editorpart.actions.ComplexCargoAction;
import com.mmxlabs.models.lng.cargo.ui.editorpart.trades.ITradesTableContextMenuExtension;
import com.mmxlabs.models.lng.cargo.ui.editorpart.trades.TradesTableContextMenuExtensionUtil;
import com.mmxlabs.models.lng.cargo.util.SlotContractParamsHelper;
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
import com.mmxlabs.models.util.emfpath.IEMFPath;
import com.mmxlabs.rcp.common.SelectionHelper;
import com.mmxlabs.rcp.common.actions.LockableAction;
import com.mmxlabs.rcp.common.actions.PackGridTreeColumnsAction;
import com.mmxlabs.rcp.common.handlers.TodayHandler;
import com.mmxlabs.scenario.service.model.manager.IScenarioDataProvider;
import com.mmxlabs.scenario.service.model.manager.ModelReference;

public class BulkTradesTablePane extends ScenarioTableViewerPane implements IAdaptable {

	private static final String COLUMN_VISIBILITY_KEY = "BulkTradesTablePane.COLUMN_VISIBILITY_KEY";

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

	@Inject(optional = true)
	private Iterable<ITradeBasedBulkColumnFilterExtension> columnFilterExtensions;

	private LNGScenarioModel lngScenarioModel;
	private Table table;
	private final CargoPackage cargoPkg = CargoPackage.eINSTANCE;
	private final EMFReportColumnManager columnManager = new EMFReportColumnManager();

	// todayHandler
	private EventHandler todayHandler;

	private final Map<EObject, Row> rowDataToRow = new HashMap<>();

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
	private final Set<ITradesBasedFilterHandler> allColumnFilterHandlers = new LinkedHashSet<>();
	private final Set<ITradesBasedFilterHandler> activeColumnFilterHandlers = new LinkedHashSet<>();
	final Map<String, ITradesRowTransformerFactory> rowTransformerHandlers = new HashMap<>();

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
		registerColumnFilterHandlers();
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
			if (oData instanceof Row) {
				Row rd = (Row) oData;
				LoadSlot ls = rd.getLoadSlot();
				if (ls != null) {
					if (ls.getWindowStart().isAfter(property)) {
						break;
					}
					pos++;
					continue;
				}
				DischargeSlot ds = rd.getDischargeSlot();
				if (ds != null) {
					if (ds.getWindowStart().isAfter(property)) {
						break;
					}
					pos++;
					continue;
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
		if (Table.class.isAssignableFrom(c)) {
			return c.cast(getTable());
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

		setMinToolbarHeight(30);
		promptToolbarEditor = new PromptToolbarEditor("prompt", scenarioEditingLocation.getEditingDomain(), (LNGScenarioModel) scenarioEditingLocation.getRootObject());
		toolbar.add(promptToolbarEditor);

		toolbar.add(new GroupMarker(EDIT_GROUP));
		toolbar.add(new GroupMarker(ADD_REMOVE_GROUP));
		toolbar.add(new GroupMarker(VIEW_GROUP));
		toolbar.appendToGroup(VIEW_GROUP, new PackGridTreeColumnsAction(scenarioViewer));

		final Action addAction = new AddAction("Add", addLoadSlotToDischarge, addDischargeSlotToLoad);

		addAction.setImageDescriptor(PlatformUI.getWorkbench().getSharedImages().getImageDescriptor(ISharedImages.IMG_OBJ_ADD));
		toolbar.appendToGroup(ADD_REMOVE_GROUP, addAction);

		final List<Action> filterActions = new LinkedList<>();
		filterActions.add(new Action("Clear Filter") {
			@Override
			public void run() {
				activeColumnFilterHandlers.clear();
				scenarioViewer.refresh(false);
			}
		});

		for (final ITradesBasedFilterHandler filterHandler : getFiltersList(allColumnFilterHandlers)) {
			if (filterHandler.activateAction(columnFilters, activeColumnFilterHandlers, this) != null) {
				filterActions.add(filterHandler.activateAction(columnFilters, activeColumnFilterHandlers, this));
			}
		}

		if (!filterActions.isEmpty()) {
			final Action filterAction = new FilterMenuAction("Filter", filterActions.toArray(new Action[filterActions.size()]));
			filterAction.setImageDescriptor(AbstractUIPlugin.imageDescriptorFromPlugin("com.mmxlabs.models.ui.tabular", "/icons/filter.gif"));
			toolbar.appendToGroup(VIEW_GROUP, filterAction);
		}
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

	private List<ITradesBasedFilterHandler> getFiltersList(final Set<ITradesBasedFilterHandler> allColumnFilterHandlers) {
		if (allColumnFilterHandlers.isEmpty()) {
			return Collections.<ITradesBasedFilterHandler> emptyList();
		}
		final List<ITradesBasedFilterHandler> filters = new LinkedList<>(allColumnFilterHandlers);
		return filters;
	}

	private abstract class DefaultMenuCreatorAction extends LockableAction implements IMenuCreator {
		private Menu lastMenu;

		public DefaultMenuCreatorAction(final String label) {
			super(label, IAction.AS_DROP_DOWN_MENU);
		}

		@Override
		public void dispose() {
			if ((lastMenu != null) && (lastMenu.isDisposed() == false)) {
				lastMenu.dispose();
			}
			lastMenu = null;
		}

		@Override
		public IMenuCreator getMenuCreator() {
			return this;
		}

		@Override
		public Menu getMenu(final Control parent) {
			if (lastMenu != null) {
				lastMenu.dispose();
			}
			lastMenu = new Menu(parent);

			populate(lastMenu);

			return lastMenu;
		}

		protected abstract void populate(Menu menu);

		@Override
		public Menu getMenu(final Menu parent) {
			if (lastMenu != null) {
				lastMenu.dispose();
			}
			lastMenu = new Menu(parent);

			populate(lastMenu);

			return lastMenu;
		}

		protected void addActionToMenu(final Action a, final Menu m) {
			final ActionContributionItem aci = new ActionContributionItem(a);
			aci.fill(m, -1);
		}

	}

	private class FilterMenuAction extends DefaultMenuCreatorAction {
		private final Action[] actions;

		public FilterMenuAction(final String label, final Action... actions) {
			super(label);
			this.actions = actions;
		}

		protected void populate(final Menu menu) {
			for (final Action action : actions) {
				addActionToMenu(action, menu);
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
						if (o instanceof Row) {
							final Row row = (Row) o;
							if (row.getCargo() == null && row.getDischargeSlot() == null && row.getLoadSlot() != null) {
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
						if (o instanceof Row) {
							final Row row = (Row) o;
							if (row.getCargo() == null && row.getDischargeSlot() == null && row.getLoadSlot() != null) {
								final CompoundCommand cmd = new CompoundCommand("Create discharge for load");
								final Cargo newCargo = CargoFactory.eINSTANCE.createCargo();
								final LoadSlot loadSlot = row.getLoadSlot();

								final DischargeSlot dischargeSlot = CargoFactory.eINSTANCE.createDischargeSlot();
								dischargeSlot.setWindowStart(loadSlot.getWindowStart());

								cmd.append(AddCommand.create(scenarioEditingLocation.getEditingDomain(), ScenarioModelUtil.getCargoModel(lngScenarioModel),
										CargoPackage.eINSTANCE.getCargoModel_DischargeSlots(), dischargeSlot));
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

	private Action getAddLoadToDischargeAction() {
		return new Action("Create load for discharge") {

			@Override
			public boolean isEnabled() {
				final ISelection selection = viewer.getSelection();
				if (selection instanceof TreeSelection) {
					final Object[] elements = ((TreeSelection) selection).toArray();
					if (elements.length == 1) {
						final Object o = elements[0];
						if (o instanceof Row) {
							final Row row = (Row) o;
							if (row.getCargo() == null && row.getLoadSlot() == null && row.getDischargeSlot() != null) {
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
						if (o instanceof Row) {
							final Row row = (Row) o;
							if (row.getCargo() == null && row.getLoadSlot() == null && row.getDischargeSlot() != null) {
								final CompoundCommand cmd = new CompoundCommand("Create load for discharge");
								final Cargo newCargo = CargoFactory.eINSTANCE.createCargo();
								final LoadSlot loadSlot = CargoFactory.eINSTANCE.createLoadSlot();
								final DischargeSlot dischargeSlot = row.getDischargeSlot();
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
		customisableRow.getESuperTypes().add(CargoBulkEditorPackage.eINSTANCE.getRow());
		customisableRow.setName("CustomisableRow");
		ePackage.getEClassifiers().add(customisableRow);
		return ePackage;
	}

	public void setCargoes(final Table table, final LNGScenarioModel lngScenarioModel) {
		final EList<Cargo> cargoes = ScenarioModelUtil.getCargoModel(lngScenarioModel).getCargoes();
		final ScheduleModel scheduleModel = ScenarioModelUtil.getScheduleModel(lngScenarioModel);
		final Schedule schedule = scheduleModel.getSchedule();
		List<SlotAllocation> slotAllocs = new ArrayList<SlotAllocation>();
		if (schedule != null) {
			slotAllocs = schedule.getSlotAllocations();
		}
		resetTable(table); // Clear rows and cache
		final HashSet<Slot> slotsInCargo = new HashSet<>();
		final Map<Slot, SlotAllocation> slots = new HashMap<Slot, SlotAllocation>();
		for (final SlotAllocation sa : slotAllocs) {
			if (sa.getSlot() != null) {
				slots.put(sa.getSlot(), sa);
			}
		}
		for (final Cargo cargo : cargoes) {
			final EPackage dataModel = customRowPackage;
			final EFactory factory = dataModel.getEFactoryInstance();
			final EClass customRowClass = (EClass) dataModel.getEClassifier("CustomisableRow");
			final Row row = (Row) factory.create(customRowClass);
			row.setCargo(cargo);
			final LoadSlot load = getLoadSlot(cargo);
			row.setLoadSlot(load);
			slotsInCargo.add(load);
			row.setLoadSlotContractParams(SlotContractParamsHelper.findOrCreateSlotContractParams(load));
			final DischargeSlot discharge = getDischargeSlot(cargo);
			row.setDischargeSlot(discharge);
			slotsInCargo.add(discharge);
			row.setDischargeSlotContractParams(SlotContractParamsHelper.findOrCreateSlotContractParams(discharge));
			row.setLoadAllocation(slots.get(load));
			row.setDischargeAllocation(slots.get(discharge));

			transformRowWithExtensions(cargo, row);
			addRowToTable(table, row);
		}
		for (final LoadSlot loadSlot : ScenarioModelUtil.getCargoModel(lngScenarioModel).getLoadSlots()) {
			if (!slotsInCargo.contains(loadSlot)) {
				// open load
				final EPackage dataModel = customRowPackage;
				final EFactory factory = dataModel.getEFactoryInstance();
				final EClass customRowClass = (EClass) dataModel.getEClassifier("CustomisableRow");
				final Row row = (Row) factory.create(customRowClass);
				row.setLoadSlot(loadSlot);
				row.setLoadSlotContractParams(SlotContractParamsHelper.findOrCreateSlotContractParams(loadSlot));

				transformRowWithExtensions(null, row);
				addRowToTable(table, row);
			}
		}
		for (final DischargeSlot dischargeSlot : ScenarioModelUtil.getCargoModel(lngScenarioModel).getDischargeSlots()) {
			if (!slotsInCargo.contains(dischargeSlot)) {
				// open discharge
				final EPackage dataModel = customRowPackage;
				final EFactory factory = dataModel.getEFactoryInstance();
				final EClass customRowClass = (EClass) dataModel.getEClassifier("CustomisableRow");
				final Row row = (Row) factory.create(customRowClass);
				row.setDischargeSlot(dischargeSlot);
				row.setDischargeSlotContractParams(SlotContractParamsHelper.findOrCreateSlotContractParams(dischargeSlot));
				transformRowWithExtensions(null, row);
				addRowToTable(table, row);
			}
		}
	}

	private void resetTable(final Table table) {
		table.getRows().clear();
		rowDataToRow.clear();
	}

	private void addRowToTable(final Table table, final Row row) {
		for (final ITradesBasedFilterHandler filter : activeColumnFilterHandlers) {
			if (!filter.isRowVisible(row)) {
				return;
			}
		}
		table.getRows().add(row);
		for (final EReference ref : row.eClass().getEAllReferences()) {
			final Object o = row.eGet(ref);
			if (o instanceof EObject) {
				final EObject obj = (EObject) o;
				rowDataToRow.put(obj, row);
				row.getInputEquivalents().add(obj);
				addEObjectsToMap(obj, row);
			}
		}
	}

	private void addEObjectsToMap(final EObject parent, final Row row) {
		for (final EReference ref : parent.eClass().getEAllReferences()) {
			if (ref.isContainment()) {
				if (ref.isMany()) {
					final Collection<Object> collection = (Collection<Object>) parent.eGet(ref);
					for (final Object o : collection) {
						if (o instanceof EObject) {
							final EObject obj = (EObject) o;
							rowDataToRow.put(obj, row);
							row.getInputEquivalents().add(obj);
							addEObjectsToMap(obj, row);
						}
					}
				} else {
					final Object o = parent.eGet(ref);
					if (o instanceof EObject) {
						final EObject obj = (EObject) o;
						rowDataToRow.put(obj, row);
						row.getInputEquivalents().add(obj);
						addEObjectsToMap(obj, row);
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

	public Table getTable() {
		return table;
	}

	public void setTable(final Table table) {
		this.table = table;
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
		final EClass eclass = (EClass) customRowPackage.getEClassifier("CustomisableRow");
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
				System.out.println(ext.getColumnID());
				if (ext.getHandlerID() != null) {
					factory = handlerMap.get(ext.getHandlerID());
				} else {
					factory = ext.getFactory();
				}
				if (factory != null) {
					factory.registerColumn(ext.getColumnID(), columnManager, getReferenceValueProviderCache(), getEditingDomain(), getJointModelEditorPart(), eclass, this);
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

	protected void registerColumnFilterHandlers() {
		{
			final PurchaseContractTradesBasedFilterHandler handler = new PurchaseContractTradesBasedFilterHandler();
			registerHandler(handler, true);
		}
		{
			final SalesContractTradesBasedFilterHandler handler = new SalesContractTradesBasedFilterHandler();
			registerHandler(handler, true);
		}
		{
			final VesselsTradesBasedFilterHandler handler = new VesselsTradesBasedFilterHandler();
			registerHandler(handler, true);
		}
		{
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
			final CargoTradesBasedFilterHandler handler = new CargoTradesBasedFilterHandler(filtersOpenContracts);
			registerHandler(handler, true);
		}
		{
			final ShippedCargoBasedFilterHandler handler = new ShippedCargoBasedFilterHandler();
			registerHandler(handler, true);
		}
		{
			final DischargePortTradesBasedFilterHandler handler = new DischargePortTradesBasedFilterHandler();
			registerHandler(handler, true);
		}
		{
			final LoadPortTradesBasedFilterHandler handler = new LoadPortTradesBasedFilterHandler();
			registerHandler(handler, true);
		}
		{
			final LocalDate scenarioStart = getEarliestScenarioDate(scenarioEditingLocation.getScenarioDataProvider());
			final LocalDate scenarioEnd = getLatestScenarioDate(scenarioEditingLocation.getScenarioDataProvider());
			final TimePeriodTradesBasedFilterHandler handler = new TimePeriodTradesBasedFilterHandler(scenarioStart, scenarioEnd);
			registerHandler(handler, true);
		}

		if (columnFilterExtensions != null) {
			for (final ITradeBasedBulkColumnFilterExtension ext : columnFilterExtensions) {
				final ITradesBasedFilterHandler handler = ext.getFactory();
				registerHandler(handler, handler.isDefaultFilter());
			}
		}
	}

	private void registerHandler(ITradesBasedFilterHandler handler, boolean activate) {
		allColumnFilterHandlers.add(handler);
		if (activate) {
			// default filters are always active
			activeColumnFilterHandlers.add(handler);
			handler.activate(columnFilters, activeColumnFilterHandlers);
		}
	}

	private void extendRowModel() {

		final EClass eclass = (EClass) customRowPackage.getEClassifier("CustomisableRow");

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

	private void transformRowWithExtensions(final Cargo cargo, final Row row) {
		final EClass eclass = (EClass) customRowPackage.getEClassifier("CustomisableRow");
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
					factory.transformRowData(table, lngScenarioModel, eclass, cargo, row);
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
		final ScenarioTableViewer viewer = new ScenarioTableViewer(parent, SWT.BORDER | SWT.H_SCROLL | SWT.V_SCROLL, scenarioEditingLocation) {

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

						setCargoes(table, lngScenarioModel);
						return table.getRows().toArray();
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

						final Row rd = rowDataToRow.get(object);

						if (rd != null) {
							getValidationSupport().setStatus(rd, status);
						}
					}
				};
			}

			/**
			 * Overridden method to convert internal RowData objects into a collection of EMF Objects
			 */
			protected void updateSelection(final ISelection selection) {

				if (selection instanceof IStructuredSelection) {
					final IStructuredSelection originalSelection = (IStructuredSelection) selection;

					final List<Object> selectedObjects = new LinkedList<>();

					final Iterator<?> itr = originalSelection.iterator();
					while (itr.hasNext()) {
						final Object obj = itr.next();
						if (obj instanceof Row) {
							final Row rd = (Row) obj;
							selectedObjects.addAll(rd.getInputEquivalents());
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

		};

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
				if (data instanceof Row) {

					final Row row = (Row) data;
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
						if (row.getLoadSlot() != null) {
							final IMenuListener listener = menuHelper.createLoadSlotMenuListener(Collections.singletonList(row.getLoadSlot()), 0);
							listener.menuAboutToShow(mgr);
							if (contextMenuExtensions != null) {
								final Slot slot = row.getLoadSlot();
								for (final ITradesTableContextMenuExtension ext : contextMenuExtensions) {
									ext.contributeToMenu(scenarioEditingLocation, slot, mgr);
								}
							}
						} else {
							final IMenuListener listener = menuHelper.createDischargeSlotMenuListener(Collections.singletonList(row.getDischargeSlot()), 0);
							listener.menuAboutToShow(mgr);
							if (contextMenuExtensions != null) {
								final Slot slot = row.getDischargeSlot();
								for (final ITradesTableContextMenuExtension ext : contextMenuExtensions) {
									ext.contributeToMenu(scenarioEditingLocation, slot, mgr);
								}
							}
						}
					}
					if (ITradesColumnFactory.isDischargeGroup(blockType)) {
						if (row.getDischargeSlot() != null) {
							final IMenuListener listener = menuHelper.createDischargeSlotMenuListener(Collections.singletonList(row.getDischargeSlot()), 0);
							listener.menuAboutToShow(mgr);
							if (contextMenuExtensions != null) {
								final Slot slot = row.getDischargeSlot();
								for (final ITradesTableContextMenuExtension ext : contextMenuExtensions) {
									ext.contributeToMenu(scenarioEditingLocation, slot, mgr);
								}
							}
						} else if (row.getLoadSlot() != null) {
							final IMenuListener listener = menuHelper.createLoadSlotMenuListener(Collections.singletonList(row.getLoadSlot()), 0);
							listener.menuAboutToShow(mgr);
							if (contextMenuExtensions != null) {
								final Slot slot = row.getLoadSlot();
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
						Row row = (Row) item;
						final Cargo cargo = row.getCargo();
						if (cargo != null) {
							cargoes.add(cargo);
						}
						if (loadSide && row.getLoadSlot() != null) {
							loads.add(row.getLoadSlot());
						}
						if (dischargeSide && row.getDischargeSlot() != null) {
							discharges.add(row.getDischargeSlot());
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

	public void setInput(final Table table2) {
		viewer.setInput(table2);
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

	private LocalDate getEarliestScenarioDate(final IScenarioDataProvider sdp) {
		LocalDate result = LocalDate.now();

		// final IScenarioDataProvider sdp = scenarioEditingLocation.getScenarioDataProvider();
		final CargoModel cargoModel = ScenarioModelUtil.getCargoModel(sdp);

		LocalDate erl = result;

		for (final LoadSlot ls : cargoModel.getLoadSlots()) {
			if (erl.isAfter(ls.getWindowStart())) {
				erl = ls.getWindowStart();
			}
		}
		for (final DischargeSlot ds : cargoModel.getDischargeSlots()) {
			if (erl.isAfter(ds.getWindowStart())) {
				erl = ds.getWindowStart();
			}
		}
		if (erl.isBefore(result)) {
			result = erl;
		}

		return result;
	}

	private LocalDate getLatestScenarioDate(final IScenarioDataProvider sdp) {
		LocalDate result = LocalDate.now();

		// final IScenarioDataProvider sdp = scenarioEditingLocation.getScenarioDataProvider();
		final CargoModel cargoModel = ScenarioModelUtil.getCargoModel(sdp);

		LocalDate erl = result;

		for (final LoadSlot ls : cargoModel.getLoadSlots()) {
			if (erl.isBefore(ls.getSchedulingTimeWindow().getEnd().toLocalDate())) {
				erl = ls.getSchedulingTimeWindow().getEnd().toLocalDate();
			}
		}
		for (final DischargeSlot ds : cargoModel.getDischargeSlots()) {
			if (erl.isBefore(ds.getSchedulingTimeWindow().getEnd().toLocalDate())) {
				erl = ds.getSchedulingTimeWindow().getEnd().toLocalDate();
			}
		}
		if (erl.isAfter(result)) {
			result = erl;
		}

		return result;
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
		 * @param menu
		 *            the menu which is about to be displayed
		 */
		protected void populate(final Menu menu) {
			{
				final DuplicateAction result = new DuplicateAction(getJointModelEditorPart());
				// Translate into real objects, not just row object!
				final List<Object> selectedObjects = new LinkedList<>();
				if (scenarioViewer.getSelection() instanceof IStructuredSelection) {
					final IStructuredSelection structuredSelection = (IStructuredSelection) scenarioViewer.getSelection();

					final Iterator<?> itr = structuredSelection.iterator();
					while (itr.hasNext()) {
						final Object o = itr.next();
						if (o instanceof Row) {
							final Row rowData = (Row) o;
							// TODO: Check logic, a row may contain two distinct items
							if (rowData.getCargo() != null) {
								selectedObjects.add(rowData.getCargo());
								continue;
							}
							if (rowData.getLoadSlot() != null) {
								selectedObjects.add(rowData.getLoadSlot());
							}
							if (rowData.getDischargeSlot() != null) {
								selectedObjects.add(rowData.getDischargeSlot());
							}
						}
					}
				}

				result.selectionChanged(new SelectionChangedEvent(scenarioViewer, new StructuredSelection(selectedObjects)));
				addActionToMenu(result, menu);
			}

			final CargoModel cargoModel = ScenarioModelUtil.getCargoModel(scenarioEditingLocation.getScenarioDataProvider());
			final CommandStack commandStack = scenarioEditingLocation.getEditingDomain().getCommandStack();

			Row discoveredRowData = null;
			final ISelection selection = getScenarioViewer().getSelection();
			if (selection instanceof IStructuredSelection) {
				final Object firstElement = ((IStructuredSelection) selection).getFirstElement();
				if (firstElement instanceof Row) {
					discoveredRowData = (Row) firstElement;
				}
			}
			final Row referenceRowData = discoveredRowData;
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
			{
				final Action newLoad = new Action("FOB Purchase") {
					public void run() {

						final List<Command> setCommands = new LinkedList<>();

						final LoadSlot newLoad = cec.createNewLoad(setCommands, cargoModel, false);
						initialiseSlot(newLoad, true, referenceRowData);

						final CompoundCommand cmd = new CompoundCommand("FOB Purchase");
						setCommands.forEach(c -> cmd.append(c));

						commandStack.execute(cmd);
						DetailCompositeDialogUtil.editSingleObjectWithUndoOnCancel(getJointModelEditorPart(), newLoad, commandStack.getMostRecentCommand());
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
						DetailCompositeDialogUtil.editSingleObjectWithUndoOnCancel(getJointModelEditorPart(), newLoad, commandStack.getMostRecentCommand());
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
						DetailCompositeDialogUtil.editSingleObjectWithUndoOnCancel(getJointModelEditorPart(), newDischarge, commandStack.getMostRecentCommand());
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
						DetailCompositeDialogUtil.editSingleObjectWithUndoOnCancel(getJointModelEditorPart(), newDischarge, commandStack.getMostRecentCommand());
					}
				};
				addActionToMenu(newFOBSale, menu);
			}

			if (LicenseFeatures.isPermitted("features:complex-cargo")) {
				final ComplexCargoAction newComplexCargo = new ComplexCargoAction("Complex Cargo", scenarioEditingLocation, viewer.getControl().getShell());
				addActionToMenu(newComplexCargo, menu);
			}
			if (actions != null) {
				for (final Action action : actions) {
					addActionToMenu(action, menu);
				}
			}
		}

		private final void initialiseSlot(final Slot newSlot, final boolean isLoad, final Row referenceRowData) {
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
}
