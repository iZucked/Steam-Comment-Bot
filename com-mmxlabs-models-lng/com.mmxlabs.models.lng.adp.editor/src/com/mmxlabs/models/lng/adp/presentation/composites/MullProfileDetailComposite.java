/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2021
 * All rights reserved.
 */
package com.mmxlabs.models.lng.adp.presentation.composites;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collectors;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.emf.common.command.CompoundCommand;
import org.eclipse.emf.common.notify.Adapter;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.databinding.EMFDataBindingContext;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.edit.command.AddCommand;
import org.eclipse.emf.edit.command.RemoveCommand;
import org.eclipse.emf.edit.domain.EditingDomain;
import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.ActionContributionItem;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.ColumnLabelProvider;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.window.Window;
import org.eclipse.nebula.widgets.grid.Grid;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.ui.ISharedImages;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.forms.widgets.FormToolkit;

import com.google.inject.Inject;
import com.mmxlabs.common.Pair;
import com.mmxlabs.models.lng.adp.ADPFactory;
import com.mmxlabs.models.lng.adp.ADPPackage;
import com.mmxlabs.models.lng.adp.DESSalesMarketAllocationRow;
import com.mmxlabs.models.lng.adp.MullEntityRow;
import com.mmxlabs.models.lng.adp.MullProfile;
import com.mmxlabs.models.lng.adp.MullSubprofile;
import com.mmxlabs.models.lng.adp.SalesContractAllocationRow;
import com.mmxlabs.models.lng.adp.utils.IMullRelativeEntitlementImportCommandProvider;
import com.mmxlabs.models.lng.cargo.Inventory;
import com.mmxlabs.models.lng.cargo.ui.editorpart.actions.DefaultMenuCreatorAction;
import com.mmxlabs.models.lng.commercial.BaseLegalEntity;
import com.mmxlabs.models.lng.commercial.SalesContract;
import com.mmxlabs.models.lng.spotmarkets.DESSalesMarket;
import com.mmxlabs.models.mmxcore.MMXCorePackage;
import com.mmxlabs.models.mmxcore.MMXRootObject;
import com.mmxlabs.models.mmxcore.impl.MMXContentAdapter;
import com.mmxlabs.models.ui.editorpart.DefaultStatusProvider;
import com.mmxlabs.models.ui.editorpart.IScenarioEditingLocation;
import com.mmxlabs.models.ui.editors.DetailToolbarManager;
import com.mmxlabs.models.ui.editors.ICommandHandler;
import com.mmxlabs.models.ui.editors.IDisplayComposite;
import com.mmxlabs.models.ui.editors.IInlineEditorWrapper;
import com.mmxlabs.models.ui.editors.dialogs.DetailCompositeDialogUtil;
import com.mmxlabs.models.ui.editors.dialogs.IDialogEditingContext;
import com.mmxlabs.models.ui.impl.DefaultDetailComposite;
import com.mmxlabs.models.ui.tabular.EObjectTableViewer;
import com.mmxlabs.models.ui.tabular.manipulators.BasicAttributeManipulator;
import com.mmxlabs.models.ui.tabular.manipulators.MultipleReferenceManipulator;
import com.mmxlabs.models.ui.tabular.manipulators.NumericAttributeManipulator;
import com.mmxlabs.models.ui.tabular.manipulators.ReadOnlyManipulatorWrapper;
import com.mmxlabs.models.ui.tabular.manipulators.SingleReferenceManipulator;
import com.mmxlabs.models.ui.valueproviders.IReferenceValueProvider;
import com.mmxlabs.rcp.common.actions.PackActionFactory;
import com.mmxlabs.rcp.common.dialogs.ListSelectionDialog;

public class MullProfileDetailComposite extends Composite implements IDisplayComposite {

	private IDisplayComposite delegate;
	private IDialogEditingContext dialogContext;
	private IStatus status;

	private MullProfile oldValue = null;
	private MullSubprofile oldSubprofile = null;

	@Inject
	private IMullRelativeEntitlementImportCommandProvider mullRelativeEntitlementImportCommandProvider;

	private final Composite inventoryTableComposite;
	private ICommandHandler commandHandler;

	private final EObjectTableViewer viewer;
	private final EObjectTableViewer entityTableViewer;
	private final EObjectTableViewer contractViewer;
	private final EObjectTableViewer marketViewer;

	private Group entityTableGroup;
	private Group contractTableGroup;
	private Group marketTableGroup;

	private Action inventoryTablePackAction;
	private Action entityTablePackAction;
	private Action contractTablePackAction;
	private Action marketTablePackAction;

	private Color systemWhite;

	protected DefaultStatusProvider statusProvider = new DefaultStatusProvider() {

		@Override
		public IStatus getStatus() {
			return status;
		}
	};

	private final Adapter adapter = new MMXContentAdapter() {

		@Override
		public void reallyNotifyChanged(Notification notification) {
			if (notification.getEventType() == Notification.REMOVING_ADAPTER) {
				return;
			}
			if (!isDisposed() && isVisible()) {
				boolean disposed = true;
				if (viewer == null || viewer.getGrid().isDisposed()) {
					disposed = false;
				} else {
					viewer.refresh();
				}
				if (disposed)
					dialogContext.getDialogController().validate();
			} else {
				MullProfileDetailComposite.this.removeAdapter();
			}
		}
	};

	MullProfileDetailComposite(final Composite parent, final IDialogEditingContext dialogContext, final int style, final FormToolkit toolkit) {
		super(parent, style);
		this.dialogContext = dialogContext;
		toolkit.adapt(this);
		setLayout(new GridLayout(1, false));
		delegate = new DefaultDetailComposite(this, style, toolkit);
		GridData gd = new GridData(GridData.FILL_HORIZONTAL);
		gd.widthHint = 500;
		delegate.getComposite().setLayoutData(gd);
		inventoryTableComposite = toolkit.createComposite(this, style);
		inventoryTableComposite.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		inventoryTableComposite.setLayout(new GridLayout());

		this.systemWhite = Display.getDefault().getSystemColor(SWT.COLOR_WHITE);

		this.viewer = getInventoryTableViewer(inventoryTableComposite);
		this.entityTableViewer = getEntityTableViewer(inventoryTableComposite);
		this.contractViewer = getContractTableViewer(inventoryTableComposite);
		this.marketViewer = getMarketTableViewer(inventoryTableComposite);
		final IWorkbenchWindow activeWorkbenchWindow = PlatformUI.getWorkbench().getActiveWorkbenchWindow();
		this.mullRelativeEntitlementImportCommandProvider = activeWorkbenchWindow.getService(IMullRelativeEntitlementImportCommandProvider.class);
	}

	@Override
	public Composite getComposite() {
		return this;
	}

	@Override
	public void display(IDialogEditingContext dialogContext, MMXRootObject root, EObject value, Collection<EObject> range, EMFDataBindingContext dbc) {
		delegate.display(dialogContext, root, value, range, dbc);
		removeAdapter();
		oldValue = (MullProfile) value;
		viewer.setInput(value);
		inventoryTablePackAction.run();
		entityTableGroup.setVisible(viewer.getStructuredSelection().size() == 1);
		contractTableGroup.setVisible(entityTableViewer.getStructuredSelection().size() == 1);
		marketTableGroup.setVisible(entityTableViewer.getStructuredSelection().size() == 1);
	}

	void removeAdapter() {
		if (oldValue != null) {
			oldValue.eAdapters().remove(adapter);
			oldValue = null;
		}
	}

	@Override
	public void setCommandHandler(ICommandHandler commandHandler) {
		delegate.setCommandHandler(commandHandler);
		this.commandHandler = commandHandler;

	}

	@Override
	public void setEditorWrapper(IInlineEditorWrapper wrapper) {
		delegate.setEditorWrapper(wrapper);
	}

	@Override
	public void displayValidationStatus(IStatus status) {
		this.status = status;
		delegate.displayValidationStatus(status);
		statusProvider.fireStatusChanged(status);

	}

	@Override
	public boolean checkVisibility(IDialogEditingContext context) {
		return delegate.checkVisibility(context);
	}

	private EObjectTableViewer getInventoryTableViewer(final Composite parent) {
		final Group inventoryTableGroup = buildTableGroup(parent, "Inventory Table");

		final DetailToolbarManager buttonManager = new DetailToolbarManager(inventoryTableGroup, SWT.TOP);
		final EObjectTableViewer eViewer = new EObjectTableViewer(inventoryTableGroup, SWT.MULTI | SWT.V_SCROLL | SWT.H_SCROLL);

		buttonManager.getToolbarManager().getControl().setBackground(systemWhite);

		final Action addInventorySubprofileRows = new Action("Add") {
			@Override
			public void run() {
				final List<Inventory> selectedInventories = openSelectionDialogBox(parent, oldValue, MullProfile::getInventories, MullSubprofile::getInventory, ADPPackage.Literals.MULL_SUBPROFILE,
						ADPPackage.Literals.MULL_SUBPROFILE__INVENTORY, "Inventory");
				updateStateWithSelection(selectedInventories, oldValue, MullProfile::getInventories, MullSubprofile::getInventory, "Inventories", ADPPackage.Literals.MULL_PROFILE__INVENTORIES,
						ADPFactory.eINSTANCE::createMullSubprofile, MullSubprofile::setInventory);
			}
		};
		addInventorySubprofileRows.setImageDescriptor(PlatformUI.getWorkbench().getSharedImages().getImageDescriptor(ISharedImages.IMG_OBJ_ADD));
		buttonManager.getToolbarManager().add(addInventorySubprofileRows);

		final Action deleteInventorySubprofileRows = createDeleteTableRowsAction(eViewer, "Inventory Rows");
		deleteInventorySubprofileRows.setImageDescriptor(PlatformUI.getWorkbench().getSharedImages().getImageDescriptor(ISharedImages.IMG_ETOOL_DELETE));
		deleteInventorySubprofileRows.setEnabled(false);
		buttonManager.getToolbarManager().add(deleteInventorySubprofileRows);

		inventoryTablePackAction = PackActionFactory.createPackColumnsAction(eViewer);
		buttonManager.getToolbarManager().add(new ActionContributionItem(inventoryTablePackAction));

		buttonManager.getToolbarManager().update(true);

		final IScenarioEditingLocation sel = dialogContext.getScenarioEditingLocation();

		eViewer.addTypicalColumn("Inventory", new ReadOnlyManipulatorWrapper<>(
				new SingleReferenceManipulator(ADPPackage.eINSTANCE.getMullSubprofile_Inventory(), sel.getReferenceValueProviderCache(), sel.getDefaultCommandHandler())));

		eViewer.setStatusProvider(statusProvider);

		eViewer.init(sel.getAdapterFactory(), sel.getModelReference(), ADPPackage.Literals.MULL_PROFILE__INVENTORIES);
		final GridData gridData = new GridData(SWT.FILL, SWT.FILL, true, true);
		gridData.heightHint = 60;
		final Grid table = eViewer.getGrid();
		table.setLayoutData(gridData);
		table.setLinesVisible(true);
		table.setHeaderVisible(true);

		eViewer.addSelectionChangedListener(event -> {
			deleteInventorySubprofileRows.setEnabled(!event.getSelection().isEmpty());
			if (event.getStructuredSelection().size() == 1) {
				final Object selection = event.getStructuredSelection().getFirstElement();
				if (selection instanceof MullSubprofile) {
					final MullSubprofile selectedRow = (MullSubprofile) selection;
					final Inventory selectedInventory = selectedRow.getInventory();
					if (selectedInventory != null) {
						final String inventoryName = selectedInventory.getName();
						if (inventoryName != null) {
							entityTableGroup.setText(inventoryName + " Entity Table");
						} else {
							entityTableGroup.setText("Entity Table");
						}
					}
					oldSubprofile = selectedRow;
					entityTableViewer.setInput(selectedRow);
					entityTablePackAction.run();
					entityTableGroup.setVisible(true);
				} else {
					entityTableGroup.setVisible(false);
					contractTableGroup.setVisible(false);
					marketTableGroup.setVisible(false);
				}
			} else {
				entityTableGroup.setVisible(false);
				contractTableGroup.setVisible(false);
				marketTableGroup.setVisible(false);
			}
		});
		return eViewer;
	}

	private EObjectTableViewer getEntityTableViewer(final Composite parent) {
		entityTableGroup = buildTableGroup(parent, "Entity Table");

		final DetailToolbarManager buttonManager = new DetailToolbarManager(entityTableGroup, SWT.TOP);
		final EObjectTableViewer eViewer = new EObjectTableViewer(entityTableGroup, SWT.MULTI | SWT.V_SCROLL | SWT.H_SCROLL);

		buttonManager.getToolbarManager().getControl().setBackground(systemWhite);

		final Action addAction = new AddAction("Add", parent);
		addAction.setImageDescriptor(PlatformUI.getWorkbench().getSharedImages().getImageDescriptor(ISharedImages.IMG_OBJ_ADD));
		buttonManager.getToolbarManager().add(addAction);

		final Action deleteEntityRows = createDeleteTableRowsAction(eViewer, "Entity Rows");
		deleteEntityRows.setImageDescriptor(PlatformUI.getWorkbench().getSharedImages().getImageDescriptor(ISharedImages.IMG_ETOOL_DELETE));
		deleteEntityRows.setEnabled(false);
		buttonManager.getToolbarManager().add(deleteEntityRows);

		entityTablePackAction = PackActionFactory.createPackColumnsAction(eViewer);
		buttonManager.getToolbarManager().add(new ActionContributionItem(entityTablePackAction));

		buttonManager.getToolbarManager().update(true);

		final IScenarioEditingLocation sel = dialogContext.getScenarioEditingLocation();

		eViewer.addTypicalColumn("Entity",
				new ReadOnlyManipulatorWrapper<>(new SingleReferenceManipulator(ADPPackage.eINSTANCE.getMullEntityRow_Entity(), sel.getReferenceValueProviderCache(), sel.getDefaultCommandHandler())));
		eViewer.addTypicalColumn("Initial Allocation", new BasicAttributeManipulator(ADPPackage.eINSTANCE.getMullEntityRow_InitialAllocation(), sel.getDefaultCommandHandler()));
		eViewer.addTypicalColumn("Reference Entitlement", new NumericAttributeManipulator(ADPPackage.eINSTANCE.getMullEntityRow_RelativeEntitlement(), sel.getDefaultCommandHandler()));

		eViewer.setStatusProvider(statusProvider);

		eViewer.init(sel.getAdapterFactory(), sel.getModelReference(), ADPPackage.Literals.MULL_SUBPROFILE__ENTITY_TABLE);
		final GridData gridData = new GridData(SWT.FILL, SWT.FILL, true, true);
		gridData.heightHint = 140;
		final Grid table = eViewer.getGrid();
		table.setLayoutData(gridData);
		table.setLinesVisible(true);
		table.setHeaderVisible(true);

		eViewer.addSelectionChangedListener(event -> {
			deleteEntityRows.setEnabled(!event.getSelection().isEmpty());
			if (event.getStructuredSelection().size() == 1) {
				final Object selection = event.getStructuredSelection().getFirstElement();
				if (selection instanceof MullEntityRow) {
					final MullEntityRow selectedRow = (MullEntityRow) selection;
					final BaseLegalEntity selectedEntity = selectedRow.getEntity();
					if (selectedEntity != null) {
						final String entityName = selectedEntity.getName();
						if (entityName != null) {
							contractTableGroup.setText(entityName + " sales contract allocations");
							marketTableGroup.setText(entityName + " market allocations");
						} else {
							contractTableGroup.setText("Sales contract allocations");
							marketTableGroup.setText("Market allocations");
						}
					} else {
						contractTableGroup.setText("Sales contract allocations");
						marketTableGroup.setText("Market allocations");
					}
					marketViewer.setInput(selectedRow);
					contractViewer.setInput(selectedRow);
					contractTablePackAction.run();
					marketTablePackAction.run();
					contractTableGroup.setVisible(true);
					marketTableGroup.setVisible(true);
				} else {
					marketTableGroup.setVisible(false);
					contractTableGroup.setVisible(false);
				}
			} else {
				marketTableGroup.setVisible(false);
				contractTableGroup.setVisible(false);
			}
		});
		return eViewer;
	}

	private class AddAction extends DefaultMenuCreatorAction {
		final Composite parent;

		public AddAction(final String label, Composite parent) {
			super(label);
			this.parent = parent;
		}

		protected void populate(final Menu menu) {
			final Action newEntityRow = new Action("Add entities") {
				@Override
				public void run() {
					Object selection = viewer.getStructuredSelection().getFirstElement();
					if (selection instanceof MullSubprofile) {
						final MullSubprofile selectedRow = (MullSubprofile) selection;
						final List<BaseLegalEntity> selectedEntities = openSelectionDialogBox(parent, selectedRow, MullSubprofile::getEntityTable, MullEntityRow::getEntity,
								ADPPackage.Literals.MULL_ENTITY_ROW, ADPPackage.Literals.MULL_ENTITY_ROW__ENTITY, "Entity");
						updateStateWithSelection(selectedEntities, selectedRow, MullSubprofile::getEntityTable, MullEntityRow::getEntity, "Entities", ADPPackage.Literals.MULL_SUBPROFILE__ENTITY_TABLE,
								ADPFactory.eINSTANCE::createMullEntityRow, MullEntityRow::setEntity);
					}
				}
			};
			addActionToMenu(newEntityRow, menu);
			final Action importFromCSVAction = new Action("Import from CSV") {
				@Override
				public void run() {
					mullRelativeEntitlementImportCommandProvider.run(oldSubprofile, dialogContext.getScenarioEditingLocation().getScenarioInstance());
					if (entityTableViewer != null) {
						entityTableViewer.setSelection(null);
						entityTableViewer.refresh();
					}
				}
			};
			addActionToMenu(importFromCSVAction, menu);
		}
	}

	private EObjectTableViewer getContractTableViewer(final Composite parent) {
		contractTableGroup = buildTableGroup(parent, "Contract allocations");
		contractTableGroup.setVisible(false);

		final DetailToolbarManager buttonManager = new DetailToolbarManager(contractTableGroup, SWT.TOP);
		final EObjectTableViewer eViewer = new EObjectTableViewer(contractTableGroup, SWT.MULTI | SWT.V_SCROLL | SWT.H_SCROLL);
		buttonManager.getToolbarManager().getControl().setBackground(systemWhite);

		final Action addContractRows = new Action("Add") {
			@Override
			public void run() {
				Object selection = entityTableViewer.getStructuredSelection().getFirstElement();
				if (selection instanceof MullEntityRow) {
					final MullEntityRow selectedRow = (MullEntityRow) selection;
					final List<SalesContract> selectedSalesContracts = openSelectionDialogBox(parent, selectedRow, MullEntityRow::getSalesContractAllocationRows,
							SalesContractAllocationRow::getContract, ADPPackage.Literals.SALES_CONTRACT_ALLOCATION_ROW, ADPPackage.Literals.SALES_CONTRACT_ALLOCATION_ROW__CONTRACT, "Sales Contract");
					updateStateWithSelection(selectedSalesContracts, selectedRow, MullEntityRow::getSalesContractAllocationRows, SalesContractAllocationRow::getContract, "Sales Contracts",
							ADPPackage.Literals.MULL_ENTITY_ROW__SALES_CONTRACT_ALLOCATION_ROWS, ADPFactory.eINSTANCE::createSalesContractAllocationRow, SalesContractAllocationRow::setContract);
				}
			}
		};
		addContractRows.setImageDescriptor(PlatformUI.getWorkbench().getSharedImages().getImageDescriptor(ISharedImages.IMG_OBJ_ADD));
		buttonManager.getToolbarManager().add(addContractRows);

		final Action deleteContractRows = createDeleteTableRowsAction(eViewer, "Sales Contract Rows");
		deleteContractRows.setImageDescriptor(PlatformUI.getWorkbench().getSharedImages().getImageDescriptor(ISharedImages.IMG_ETOOL_DELETE));
		deleteContractRows.setEnabled(false);
		buttonManager.getToolbarManager().add(deleteContractRows);

		contractTablePackAction = PackActionFactory.createPackColumnsAction(eViewer);
		buttonManager.getToolbarManager().add(new ActionContributionItem(contractTablePackAction));

		buttonManager.getToolbarManager().update(true);

		final IScenarioEditingLocation sel = dialogContext.getScenarioEditingLocation();

		eViewer.addTypicalColumn("Contract", new ReadOnlyManipulatorWrapper<>(
				new SingleReferenceManipulator(ADPPackage.eINSTANCE.getSalesContractAllocationRow_Contract(), sel.getReferenceValueProviderCache(), sel.getDefaultCommandHandler())));
		eViewer.addTypicalColumn("AACQ", new NumericAttributeManipulator(ADPPackage.eINSTANCE.getMullAllocationRow_Weight(), sel.getDefaultCommandHandler()));
		eViewer.addTypicalColumn("Vessels", new MultipleReferenceManipulator(ADPPackage.eINSTANCE.getMullAllocationRow_Vessels(), sel.getReferenceValueProviderCache(), sel.getDefaultCommandHandler(),
				MMXCorePackage.eINSTANCE.getNamedObject_Name()));

		eViewer.setStatusProvider(statusProvider);

		eViewer.init(sel.getAdapterFactory(), sel.getModelReference(), ADPPackage.Literals.MULL_ENTITY_ROW__SALES_CONTRACT_ALLOCATION_ROWS);
		final GridData gridData = new GridData(SWT.FILL, SWT.FILL, true, true);
		gridData.heightHint = 120;
		final Grid table = eViewer.getGrid();
		table.setLayoutData(gridData);
		table.setLinesVisible(true);
		table.setHeaderVisible(true);

		eViewer.addSelectionChangedListener(event -> {
			final ISelection iSelection = event.getSelection();
			deleteContractRows.setEnabled(!iSelection.isEmpty());
			if (!iSelection.isEmpty() && !marketViewer.getSelection().isEmpty()) {
				marketViewer.getGrid().deselectAll();
			}
		});
		eViewer.addOpenListener(event -> {
			final ISelection selection = eViewer.getSelection();
			if (selection instanceof IStructuredSelection) {
				final IStructuredSelection structuredSelection = (IStructuredSelection) selection;
				if (!structuredSelection.isEmpty()) {
					DetailCompositeDialogUtil.editSelection(sel, structuredSelection);
				}
			}
		});
		return eViewer;
	}

	private EObjectTableViewer getMarketTableViewer(final Composite parent) {
		marketTableGroup = buildTableGroup(parent, "Market allocations");
		marketTableGroup.setVisible(false);

		final DetailToolbarManager buttonManager = new DetailToolbarManager(marketTableGroup, SWT.TOP);
		final EObjectTableViewer eViewer = new EObjectTableViewer(marketTableGroup, SWT.MULTI | SWT.V_SCROLL | SWT.H_SCROLL);

		buttonManager.getToolbarManager().getControl().setBackground(systemWhite);

		final Action addMarketRows = new Action("Add") {
			@Override
			public void run() {
				Object selection = entityTableViewer.getStructuredSelection().getFirstElement();
				if (selection instanceof MullEntityRow) {
					final MullEntityRow selectedRow = (MullEntityRow) selection;
					final List<DESSalesMarket> selectedDesSalesMarkets = openSelectionDialogBox(parent, selectedRow, MullEntityRow::getDesSalesMarketAllocationRows,
							DESSalesMarketAllocationRow::getDesSalesMarket, ADPPackage.Literals.DES_SALES_MARKET_ALLOCATION_ROW, ADPPackage.Literals.DES_SALES_MARKET_ALLOCATION_ROW__DES_SALES_MARKET,
							"DES Sales Market");
					updateStateWithSelection(selectedDesSalesMarkets, selectedRow, MullEntityRow::getDesSalesMarketAllocationRows, DESSalesMarketAllocationRow::getDesSalesMarket, "DES Sales Markets",
							ADPPackage.Literals.MULL_ENTITY_ROW__DES_SALES_MARKET_ALLOCATION_ROWS, ADPFactory.eINSTANCE::createDESSalesMarketAllocationRow,
							DESSalesMarketAllocationRow::setDesSalesMarket);
				}
			}
		};
		addMarketRows.setImageDescriptor(PlatformUI.getWorkbench().getSharedImages().getImageDescriptor(ISharedImages.IMG_OBJ_ADD));
		buttonManager.getToolbarManager().add(addMarketRows);

		final Action deleteMarketRows = createDeleteTableRowsAction(eViewer, "DES Sales Market Rows");
		deleteMarketRows.setImageDescriptor(PlatformUI.getWorkbench().getSharedImages().getImageDescriptor(ISharedImages.IMG_ETOOL_DELETE));
		deleteMarketRows.setEnabled(false);
		buttonManager.getToolbarManager().add(deleteMarketRows);

		marketTablePackAction = PackActionFactory.createPackColumnsAction(eViewer);
		buttonManager.getToolbarManager().add(new ActionContributionItem(marketTablePackAction));

		buttonManager.getToolbarManager().update(true);

		final IScenarioEditingLocation sel = dialogContext.getScenarioEditingLocation();

		eViewer.addTypicalColumn("Market", new ReadOnlyManipulatorWrapper<>(
				new SingleReferenceManipulator(ADPPackage.eINSTANCE.getDESSalesMarketAllocationRow_DesSalesMarket(), sel.getReferenceValueProviderCache(), sel.getDefaultCommandHandler())));
		eViewer.addTypicalColumn("AACQ", new NumericAttributeManipulator(ADPPackage.eINSTANCE.getMullAllocationRow_Weight(), sel.getDefaultCommandHandler()));
		eViewer.addTypicalColumn("Vessels", new MultipleReferenceManipulator(ADPPackage.eINSTANCE.getMullAllocationRow_Vessels(), sel.getReferenceValueProviderCache(), sel.getDefaultCommandHandler(),
				MMXCorePackage.eINSTANCE.getNamedObject_Name()));

		eViewer.setStatusProvider(statusProvider);

		eViewer.init(sel.getAdapterFactory(), sel.getModelReference(), ADPPackage.Literals.MULL_ENTITY_ROW__DES_SALES_MARKET_ALLOCATION_ROWS);
		final GridData gridData = new GridData(SWT.FILL, SWT.FILL, true, true);
		gridData.heightHint = 120;
		final Grid table = eViewer.getGrid();
		table.setLayoutData(gridData);
		table.setLinesVisible(true);
		table.setHeaderVisible(true);

		eViewer.addSelectionChangedListener(event -> {
			final ISelection iSelection = event.getSelection();
			deleteMarketRows.setEnabled(!iSelection.isEmpty());
			if (!iSelection.isEmpty() && !contractViewer.getSelection().isEmpty()) {
				contractViewer.getGrid().deselectAll();
			}
		});
		eViewer.addOpenListener(event -> {
			final ISelection selection = eViewer.getSelection();
			if (selection instanceof IStructuredSelection) {
				final IStructuredSelection structuredSelection = (IStructuredSelection) selection;
				if (!structuredSelection.isEmpty()) {
					DetailCompositeDialogUtil.editSelection(sel, structuredSelection);
				}
			}
		});
		return eViewer;
	}

	private <R extends EObject, S extends EObject, T extends EObject> List<T> openSelectionDialogBox(final Control cellEditorWindow, final S target, final Function<S, List<R>> rowsExtractor,
			final Function<R, T> valueExtractor, final EClass owner, final EReference field, @NonNull final String selectionLabel) {
		final IReferenceValueProvider valueProvider = commandHandler.getReferenceValueProviderProvider().getReferenceValueProvider(owner, field);
		final List<Pair<String, EObject>> options = valueProvider.getAllowedValues(target, field);
		final ListSelectionDialog dlg = new ListSelectionDialog(cellEditorWindow.getShell(), options.toArray(), new ArrayContentProvider(), new LabelProvider() {
			@Override
			public String getText(final Object element) {
				return ((Pair<String, ?>) element).getFirst();
			}
		});
		dlg.setTitle(String.format("%s Selection", selectionLabel));
		final ArrayList<Pair<String, EObject>> selectedOptions = new ArrayList<>();
		final Set<T> sel = rowsExtractor.apply(target).stream().map(valueExtractor).collect(Collectors.toSet());
		if (sel != null) {
			for (final Pair<String, EObject> p : options) {
				if (sel.contains(p.getSecond())) {
					selectedOptions.add(p);
				}
			}
		}
		dlg.setInitialSelections(selectedOptions.toArray());

		dlg.addColumn(selectionLabel, new ColumnLabelProvider() {
			@Override
			public String getText(final Object element) {
				return ((Pair<String, ?>) element).getFirst();
			}
		});

		if (dlg.open() == Window.OK) {
			final Object[] result = dlg.getResult();
			final ArrayList<T> resultList = new ArrayList<>();
			for (final Object o : result) {
				resultList.add(((Pair<String, T>) o).getSecond());
			}
			return resultList;
		}
		return null;
	}

	private <R extends EObject, S extends EObject, T extends EObject> void updateStateWithSelection(final List<T> selectedValues, final S selectedRow, final Function<S, List<R>> rowsExtractor,
			final Function<R, T> valueExtractor, final String elementString, final EReference feature, final Supplier<R> rowCreator, final BiConsumer<R, T> valueSetter) {
		if (selectedValues != null) {
			final Set<T> afterSelectionSet = new HashSet<>(selectedValues);
			final List<R> rowsToRemove = new LinkedList<>();
			rowsExtractor.apply(selectedRow).stream().forEach(row -> {
				final T oldRowValue = valueExtractor.apply(row);
				if (afterSelectionSet.contains(oldRowValue)) {
					afterSelectionSet.remove(oldRowValue);
				} else {
					rowsToRemove.add(row);
				}
			});
			final CompoundCommand cmd = new CompoundCommand(String.format("Add %s", elementString));
			if (!rowsToRemove.isEmpty()) {
				cmd.append(RemoveCommand.create(commandHandler.getEditingDomain(), selectedRow, feature, rowsToRemove));
			}
			if (!afterSelectionSet.isEmpty()) {
				final List<R> newRows = afterSelectionSet.stream() //
						.map(s -> {
							final R newRow = rowCreator.get();
							valueSetter.accept(newRow, s);
							return newRow;
						}) //
						.collect(Collectors.toList());
				cmd.append(AddCommand.create(commandHandler.getEditingDomain(), selectedRow, feature, newRows));
			}
			if (!cmd.isEmpty()) {
				commandHandler.handleCommand(cmd, selectedRow, feature);
			}
		}
	}

	private Action createDeleteTableRowsAction(final EObjectTableViewer tableViewer, final String deleteCommandText) {
		return new Action("Delete") {
			@Override
			public void run() {
				if (tableViewer != null) {
					final ISelection selection = tableViewer.getSelection();
					if (selection.isEmpty()) {
						return;
					}
					if (selection instanceof IStructuredSelection) {
						final IStructuredSelection iStructuredSelection = (IStructuredSelection) selection;
						final CompoundCommand cc = new CompoundCommand(String.format("Delete %s", deleteCommandText));
						final EditingDomain ed = dialogContext.getScenarioEditingLocation().getEditingDomain();
						final EObject firstLine = (EObject) iStructuredSelection.getFirstElement();
						cc.append(RemoveCommand.create(ed, firstLine.eContainer(), firstLine.eContainingFeature(), iStructuredSelection.toList()));
						ed.getCommandStack().execute(cc);
					}
				}
			}
		};
	}

	private @NonNull Group buildTableGroup(final Composite parent, final String groupName) {
		final Group group = new Group(parent, SWT.NONE);
		group.setLayout(new GridLayout(1, false));
		group.setBackground(systemWhite);
		group.setLayoutData(new GridData(GridData.VERTICAL_ALIGN_BEGINNING | GridData.FILL_BOTH));
		group.setText(groupName);
		return group;
	}
}
