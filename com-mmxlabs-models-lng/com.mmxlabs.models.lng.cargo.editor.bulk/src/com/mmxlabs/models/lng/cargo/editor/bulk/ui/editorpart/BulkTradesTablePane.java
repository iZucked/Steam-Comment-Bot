/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2015
 * All rights reserved.
 */
package com.mmxlabs.models.lng.cargo.editor.bulk.ui.editorpart;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.emf.common.command.CompoundCommand;
import org.eclipse.emf.common.notify.AdapterFactory;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EFactory;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.EcoreFactory;
import org.eclipse.emf.edit.command.AddCommand;
import org.eclipse.emf.edit.command.SetCommand;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.ActionContributionItem;
import org.eclipse.jface.action.GroupMarker;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.action.IMenuCreator;
import org.eclipse.jface.action.ToolBarManager;
import org.eclipse.jface.bindings.keys.IKeyLookup;
import org.eclipse.jface.bindings.keys.KeyLookupFactory;
import org.eclipse.jface.viewers.ColumnViewerEditor;
import org.eclipse.jface.viewers.ColumnViewerEditorActivationEvent;
import org.eclipse.jface.viewers.ColumnViewerEditorActivationStrategy;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.TreeSelection;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.nebula.jface.gridviewer.GridTreeViewer;
import org.eclipse.nebula.jface.gridviewer.GridViewerColumn;
import org.eclipse.nebula.jface.gridviewer.GridViewerEditor;
import org.eclipse.nebula.widgets.grid.GridColumn;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.ui.IActionBars;
import org.eclipse.ui.ISharedImages;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.actions.ActionFactory;
import org.eclipse.ui.plugin.AbstractUIPlugin;

import com.google.inject.Guice;
import com.google.inject.Inject;
import com.google.inject.Injector;
import com.mmxlabs.models.lng.cargo.Cargo;
import com.mmxlabs.models.lng.cargo.CargoFactory;
import com.mmxlabs.models.lng.cargo.CargoPackage;
import com.mmxlabs.models.lng.cargo.DischargeSlot;
import com.mmxlabs.models.lng.cargo.LoadSlot;
import com.mmxlabs.models.lng.cargo.Slot;
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
import com.mmxlabs.models.lng.cargo.editor.bulk.views.ITradesBasedFilterHandler;
import com.mmxlabs.models.lng.cargo.editor.bulk.views.ITradesBasedRowModelTransformerFactory;
import com.mmxlabs.models.lng.cargo.editor.bulk.views.ITradesColumnFactory;
import com.mmxlabs.models.lng.cargo.editor.bulk.views.ITradesRowTransformerFactory;
import com.mmxlabs.models.lng.cargo.editor.bulk.views.TradesBasedColumnFactory;
import com.mmxlabs.models.lng.cargo.ui.editorpart.PromptToolbarEditor;
import com.mmxlabs.models.lng.scenario.model.LNGScenarioModel;
import com.mmxlabs.models.lng.scenario.model.util.ScenarioModelUtil;
import com.mmxlabs.models.lng.ui.tabular.ScenarioTableViewer;
import com.mmxlabs.models.lng.ui.tabular.ScenarioTableViewerPane;
import com.mmxlabs.models.ui.editorpart.IScenarioEditingLocation;
import com.mmxlabs.models.ui.tabular.EObjectTableViewer;
import com.mmxlabs.models.ui.tabular.EObjectTableViewerValidationSupport;
import com.mmxlabs.models.ui.tabular.columngeneration.ColumnBlockManager;
import com.mmxlabs.models.ui.tabular.columngeneration.ColumnHandler;
import com.mmxlabs.models.ui.tabular.columngeneration.EMFReportColumnManager;
import com.mmxlabs.models.ui.tabular.columngeneration.EObjectTableViewerColumnFactory;
import com.mmxlabs.rcp.common.actions.LockableAction;
import com.mmxlabs.rcp.common.actions.PackGridTreeColumnsAction;
import com.mmxlabs.scenario.service.model.manager.ModelReference;

public class BulkTradesTablePane extends ScenarioTableViewerPane implements IAdaptable {

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
	private CargoPackage cargoPkg = CargoPackage.eINSTANCE;
	private EMFReportColumnManager columnManager = new EMFReportColumnManager();

	private Map<EObject, Row> rowDataToRow = new HashMap<>();

	/*
	 * This is used to determine the order of column groups
	 */
	private final String[] orderedColumnGroupNames = new String[] { TradesBasedColumnFactory.LOAD_START_GROUP, //
			TradesBasedColumnFactory.LOAD_MAIN_GROUP, //
			TradesBasedColumnFactory.LOAD_EXTRA_GROUP, //
			TradesBasedColumnFactory.LOAD_END_GROUP, //
			TradesBasedColumnFactory.DISCHARGE_START_GROUP, //
			TradesBasedColumnFactory.DISCHARGE_MAIN_GROUP, //
			TradesBasedColumnFactory.DISCHARGE_EXTRA_GROUP, //
			TradesBasedColumnFactory.DISCHARGE_END_GROUP, //
			TradesBasedColumnFactory.CARGO_START_GROUP, //
			TradesBasedColumnFactory.CARGO_END_GROUP //
	};

	/*
	 * This is used to determine the default visible column groups
	 */
	private String[] defaultFilters = { //
			TradesBasedColumnFactory.LOAD_START_GROUP, //
			TradesBasedColumnFactory.LOAD_MAIN_GROUP, //
			TradesBasedColumnFactory.LOAD_END_GROUP, //
			TradesBasedColumnFactory.DISCHARGE_START_GROUP, //
			TradesBasedColumnFactory.DISCHARGE_MAIN_GROUP, //
			TradesBasedColumnFactory.DISCHARGE_END_GROUP, //
			TradesBasedColumnFactory.CARGO_END_GROUP //
	};

	private EPackage customRowPackage = createCustomisedRowEcore();
	private ColumnFilters columnFilters;
	private Set<ITradesBasedFilterHandler> allColumnFilterHandlers = new HashSet<>();
	private Set<ITradesBasedFilterHandler> activeColumnFilterHandlers = new HashSet<>();
	final Map<String, ITradesRowTransformerFactory> rowTransformerHandlers = new HashMap<>();

	private ColumnBlockManager columnBlockManager = new ColumnBlockManager();
	private Map<ColumnHandler, GridColumn> handlerToColumnMap = new HashMap<>();

	protected final SortData sortData = new SortData();

	private PromptToolbarEditor promptToolbarEditor;

	public BulkTradesTablePane(final IWorkbenchPage page, final IWorkbenchPart part, final IScenarioEditingLocation location, final IActionBars actionBars) {
		super(page, part, location, actionBars);
		Injector injector = Guice.createInjector(new TradeBasedBulkModule());
		injector.injectMembers(this);
		columnFilters = new ColumnFilters(defaultFilters);
		registerColumnFilterHandlers();
	}

	@Override
	public void init(final List<EReference> path, final AdapterFactory adapterFactory, final ModelReference modelReference) {
		super.init(path, adapterFactory, modelReference);

		extendRowModel();
		createColumns();
		buildRowTransformerHandlers(rowTransformerHandlers);

		buildToolbar();

	}

	public Object getAdapter(Class c) {
		if (GridTreeViewer.class.isAssignableFrom(c)) {
			return getScenarioViewer();
		}
		if (Table.class.isAssignableFrom(c)) {
			return getTable();
		}
		if (SortData.class.isAssignableFrom(c)) {
			return sortData;
		}
		if (EObjectTableViewer.class.isAssignableFrom(c)) {
			return getScenarioViewer();
		}
		return null;
	}

	private void buildToolbar() {
		final Action newCargo = getNewCargoAction();
		final Action newLoadSlot = getNewLoadSlotAction();
		final Action newDischargeSlot = getNewDischargeSlotAction();
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

		final Action addAction = new AddAction("Add", newCargo, newLoadSlot, newDischargeSlot, addLoadSlotToDischarge, addDischargeSlotToLoad);
		addAction.setImageDescriptor(PlatformUI.getWorkbench().getSharedImages().getImageDescriptor(ISharedImages.IMG_OBJ_ADD));
		toolbar.appendToGroup(ADD_REMOVE_GROUP, addAction);

		// getToolBarManager().add(newCargo);
		// getToolBarManager().add(newLoadSlot);
		// getToolBarManager().add(newDischargeSlot);
		// getToolBarManager().add(addLoadSlotToDischarge);
		// getToolBarManager().add(addDischargeSlotToLoad);
		List<Action> filterActions = new LinkedList<>();
		for (ITradesBasedFilterHandler filterHandler : getFiltersList(allColumnFilterHandlers)) {
			if (filterHandler.activateAction(columnFilters, activeColumnFilterHandlers, this) != null) {
				filterActions.add(filterHandler.activateAction(columnFilters, activeColumnFilterHandlers, this));
			}
		}

		final Action filterAction = new FilterMenuAction("Filter", filterActions.toArray(new Action[filterActions.size()]));
		filterAction.setImageDescriptor(AbstractUIPlugin.imageDescriptorFromPlugin("com.mmxlabs.models.ui.tabular", "/icons/filter.gif"));
		toolbar.appendToGroup(VIEW_GROUP, filterAction);

		deleteAction = createDeleteAction(null); // TODO: NEW check this
		if (deleteAction != null) {
			toolbar.appendToGroup(ADD_REMOVE_GROUP, deleteAction);
		}
		if (actionBars != null) {
			actionBars.setGlobalActionHandler(ActionFactory.DELETE.getId(), deleteAction);
		}

		// final Action lockAction = new Action("Lock") {
		// };
		// lockAction.setImageDescriptor(AbstractUIPlugin.imageDescriptorFromPlugin("com.mmxlabs.models.ui.tabular", "/icons/lock.gif"));
		// toolbar.appendToGroup(VIEW_GROUP, lockAction);
		toolbar.update(true);
	}

	private List<ITradesBasedFilterHandler> getFiltersList(Set<ITradesBasedFilterHandler> allColumnFilterHandlers) {
		if (allColumnFilterHandlers.isEmpty()) {
			return Collections.<ITradesBasedFilterHandler> emptyList();
		}
		List<ITradesBasedFilterHandler> filters = new LinkedList<>(allColumnFilterHandlers);
		Collections.sort(filters, new Comparator<ITradesBasedFilterHandler>() {

			@Override
			public int compare(ITradesBasedFilterHandler o1, ITradesBasedFilterHandler o2) {
				if (o1.isDefaultFilter() && !o2.isDefaultFilter()) {
					return -1;
				} else if (!o1.isDefaultFilter() && o2.isDefaultFilter()) {
					return 1;
				} else {
					return o1.getClass().getName().compareTo(o2.getClass().getName());
				}
			}
		});
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

	private class AddAction extends DefaultMenuCreatorAction {
		private Action[] actions;

		public AddAction(final String label, final Action... actions) {
			super(label);
			this.actions = actions;
		}

		protected void populate(final Menu menu) {
			for (Action action : actions) {
				addActionToMenu(action, menu);
			}
		}
	}

	private class FilterMenuAction extends DefaultMenuCreatorAction {
		private Action[] actions;

		public FilterMenuAction(final String label, final Action... actions) {
			super(label);
			this.actions = actions;
		}

		protected void populate(final Menu menu) {
			for (Action action : actions) {
				addActionToMenu(action, menu);
			}
		}
	}

	private Action getAddDischargeToLoadAction() {
		return new Action("Add Discharge To Load") {
			public void run() {
				ISelection selection = viewer.getSelection();
				if (selection instanceof TreeSelection) {
					Object[] elements = ((TreeSelection) selection).toArray();
					if (elements.length == 1) {
						Object o = elements[0];
						if (o instanceof Row) {
							Row row = (Row) o;
							if (row.getCargo() == null && row.getDischargeSlot() == null && row.getLoadSlot() != null) {
								final CompoundCommand cmd = new CompoundCommand("Add Discharge to Load");
								final Cargo newCargo = CargoFactory.eINSTANCE.createCargo();
								final LoadSlot loadSlot = row.getLoadSlot();
								final DischargeSlot dischargeSlot = CargoFactory.eINSTANCE.createDischargeSlot();
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
		return new Action("Add Load To Discharge") {
			public void run() {
				ISelection selection = viewer.getSelection();
				if (selection instanceof TreeSelection) {
					Object[] elements = ((TreeSelection) selection).toArray();
					if (elements.length == 1) {
						Object o = elements[0];
						if (o instanceof Row) {
							Row row = (Row) o;
							if (row.getCargo() == null && row.getLoadSlot() == null && row.getDischargeSlot() != null) {
								final CompoundCommand cmd = new CompoundCommand("Add Load to Discharge");
								final Cargo newCargo = CargoFactory.eINSTANCE.createCargo();
								final LoadSlot loadSlot = CargoFactory.eINSTANCE.createLoadSlot();
								final DischargeSlot dischargeSlot = row.getDischargeSlot();
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

	public void setCargoes(Table table, LNGScenarioModel lngScenarioModel) {
		EList<Cargo> cargoes = ScenarioModelUtil.getCargoModel(lngScenarioModel).getCargoes();
		resetTable(table); // Clear rows and cache
		HashSet<Slot> slotsInCargo = new HashSet<>();
		for (Cargo cargo : cargoes) {
			EPackage dataModel = customRowPackage;
			EFactory factory = dataModel.getEFactoryInstance();
			EClass customRowClass = (EClass) dataModel.getEClassifier("CustomisableRow");
			Row row = (Row) factory.create(customRowClass);
			row.setCargo(cargo);
			LoadSlot load = getLoadSlot(cargo);
			row.setLoadSlot(load);
			slotsInCargo.add(load);
			DischargeSlot discharge = getDischargeSlot(cargo);
			row.setDischargeSlot(discharge);
			slotsInCargo.add(discharge);
			transformRowWithExtensions(cargo, row);
			addRowToTable(table, row);
		}
		for (LoadSlot loadSlot : ScenarioModelUtil.getCargoModel(lngScenarioModel).getLoadSlots()) {
			if (!slotsInCargo.contains(loadSlot)) {
				// open load
				EPackage dataModel = customRowPackage;
				EFactory factory = dataModel.getEFactoryInstance();
				EClass customRowClass = (EClass) dataModel.getEClassifier("CustomisableRow");
				Row row = (Row) factory.create(customRowClass);
				row.setLoadSlot(loadSlot);
				transformRowWithExtensions(null, row);
				addRowToTable(table, row);
			}
		}
		for (DischargeSlot dischargeSlot : ScenarioModelUtil.getCargoModel(lngScenarioModel).getDischargeSlots()) {
			if (!slotsInCargo.contains(dischargeSlot)) {
				// open discharge
				EPackage dataModel = customRowPackage;
				EFactory factory = dataModel.getEFactoryInstance();
				EClass customRowClass = (EClass) dataModel.getEClassifier("CustomisableRow");
				Row row = (Row) factory.create(customRowClass);
				row.setDischargeSlot(dischargeSlot);
				transformRowWithExtensions(null, row);
				addRowToTable(table, row);
			}
		}
	}

	private void resetTable(Table table) {
		table.getRows().clear();
		rowDataToRow.clear();
	}

	private void addRowToTable(Table table, Row row) {
		for (ITradesBasedFilterHandler filter : activeColumnFilterHandlers) {
			if (!filter.isRowVisible(row)) {
				return;
			}
		}
		table.getRows().add(row);
		for (EReference ref : row.eClass().getEAllReferences()) {
			Object o = row.eGet(ref);
			if (o instanceof EObject) {
				EObject obj = (EObject) o;
				rowDataToRow.put(obj, row);
				row.getInputEquivalents().add(obj);
				addEObjectsToMap(obj, row);
			}
		}
	}

	private void addEObjectsToMap(EObject parent, Row row) {
		for (EReference ref : parent.eClass().getEAllReferences()) {
			if (ref.isContainment()) {
				if (ref.isMany()) {
					Collection<Object> collection = (Collection<Object>) parent.eGet(ref);
					for (Object o : collection) {
						if (o instanceof EObject) {
							EObject obj = (EObject) o;
							rowDataToRow.put(obj, row);
							row.getInputEquivalents().add(obj);
							addEObjectsToMap(obj, row);
						}
					}
				} else {
					Object o = parent.eGet(ref);
					if (o instanceof EObject) {
						EObject obj = (EObject) o;
						rowDataToRow.put(obj, row);
						row.getInputEquivalents().add(obj);
						addEObjectsToMap(obj, row);
					}
				}
			}
		}
	}

	private DischargeSlot getDischargeSlot(Cargo cargo) {
		return (DischargeSlot) (cargo.getSortedSlots().get(cargo.getSortedSlots().size() - 1));
	}

	private LoadSlot getLoadSlot(Cargo cargo) {
		return (LoadSlot) (cargo.getSortedSlots().get(0));
	}

	public void setlngScenarioModel(LNGScenarioModel lngScenarioModel) {
		this.lngScenarioModel = lngScenarioModel;
	}

	public Table getTable() {
		return table;
	}

	public void setTable(Table table) {
		this.table = table;
	}

	public void createColumns() {
		columnBlockManager.setColumnFactory(new EObjectTableViewerColumnFactory(scenarioViewer));
		columnBlockManager.setGrid(scenarioViewer.getGrid());
		EClass eclass = (EClass) customRowPackage.getEClassifier("CustomisableRow");
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
		for (String groupName : orderedColumnGroupNames) {
			for (final ColumnHandler handler : columnBlockManager.getHandlerGroup(groupName)) {
				final GridViewerColumn gvcolumn = handler.createColumn();
				// GridViewerHelper.configureLookAndFeel(gvcolumn);
				final GridColumn column = gvcolumn.getColumn();
				// DefaultColumnHeaderRenderer colRenderer = new DefaultColumnHeaderRenderer();
				// colRenderer.setWordWrap(true);
				// column.setHeaderRenderer(colRenderer);
				column.setMinimumWidth(70);
				column.setResizeable(false);
				handlerToColumnMap.put(handler, column);
				if (column.getData(EObjectTableViewer.COLUMN_COMPARABLE_PROVIDER) != null) {
					scenarioViewer.getSortingSupport().addSortableColumn(scenarioViewer, handler.column, column);
				}
				if (columnFilters.isColumnVisible(handler.block)) {
					column.setVisible(true);
				}
			}
		}
		scenarioViewer.getGrid().recalculateHeader();
	}

	public void setColumnsVisibility() {
		for (String groupName : orderedColumnGroupNames) {
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
		if (columnFilterExtensions != null) {
			for (final ITradeBasedBulkColumnFilterExtension ext : columnFilterExtensions) {
				ITradesBasedFilterHandler handler = ext.getFactory();
				allColumnFilterHandlers.add(handler);
				if (handler.isDefaultFilter()) {
					// default filters are always active
					activeColumnFilterHandlers.add(handler);
				}
			}
		}
	}

	private void extendRowModel() {

		EClass eclass = (EClass) customRowPackage.getEClassifier("CustomisableRow");

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
					String handlerId = ext.getHandlerID();
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

	private void transformRowWithExtensions(Cargo cargo, Row row) {
		EClass eclass = (EClass) customRowPackage.getEClassifier("CustomisableRow");
		if (rowTransformerExtensions != null) {

			for (final ITradeBasedBulkRowTransformerExtension ext : rowTransformerExtensions) {
				ITradesRowTransformerFactory factory;
				if (ext.getHandlerID() != null) {
					String handlerId = ext.getHandlerID();
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

	private void buildRowTransformerHandlers(Map<String, ITradesRowTransformerFactory> rowTransformerHandlers) {
		if (rowTransformerFactoryExtensions != null) {
			for (final ITradeBasedBulkRowTransformerFactoryExtension ext : rowTransformerFactoryExtensions) {
				final String handlerID = ext.getHandlerID();
				rowTransformerHandlers.put(handlerID, ext.getFactory());
			}
		}
	}

	@Override
	protected ScenarioTableViewer constructViewer(final Composite parent) {
		ScenarioTableViewer viewer = new ScenarioTableViewer(parent, SWT.BORDER | SWT.H_SCROLL | SWT.V_SCROLL, scenarioEditingLocation) {

			@Override
			public void refresh() {
				// TODO Auto-generated method stub
				super.refresh();
				for (GridColumn c : this.getGrid().getColumns()) {
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
			protected Object[] getSortedChildren(final Object parent) {
				// This is the filtered and sorted children.
				// This may be smaller than the original set.
				sortData.sortedChildren = super.getSortedChildren(parent);

				sortData.sortedIndices = new int[getTable() == null ? 0 : getTable().getRows().size()];
				sortData.reverseSortedIndices = new int[getTable() == null ? 0 : getTable().getRows().size()];

				Arrays.fill(sortData.sortedIndices, -1);
				Arrays.fill(sortData.reverseSortedIndices, -1);

				for (int i = 0; i < sortData.sortedChildren.length; ++i) {
					final int rawIndex = getTable().getRows().indexOf(sortData.sortedChildren[i]);
					sortData.sortedIndices[rawIndex] = i;
					sortData.reverseSortedIndices[i] = rawIndex;
				}
				return sortData.sortedChildren;
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

						Row rd = rowDataToRow.get(object);

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

					final List<Object> selectedObjects = new LinkedList<Object>();

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

		viewer.getSortingSupport().setSortOnlyOnSelect(true);

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
					boolean activate = event.eventType == ColumnViewerEditorActivationEvent.MOUSE_CLICK_SELECTION //
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

		if (promptToolbarEditor != null) {
			promptToolbarEditor.setLocked(locked);
		}

		super.setLocked(locked);
	}

}
