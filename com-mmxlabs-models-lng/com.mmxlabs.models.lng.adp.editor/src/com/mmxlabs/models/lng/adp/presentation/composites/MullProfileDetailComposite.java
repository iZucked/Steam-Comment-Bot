/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2021
 * All rights reserved.
 */
package com.mmxlabs.models.lng.adp.presentation.composites;

import java.util.Collection;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.emf.common.command.CompoundCommand;
import org.eclipse.emf.common.notify.Adapter;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.databinding.EMFDataBindingContext;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.edit.command.AddCommand;
import org.eclipse.emf.edit.command.RemoveCommand;
import org.eclipse.emf.edit.domain.EditingDomain;
import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.resource.ResourceLocator;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.nebula.widgets.grid.Grid;
import org.eclipse.nebula.widgets.grid.GridColumn;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Group;
import org.eclipse.ui.ISharedImages;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.forms.widgets.FormToolkit;

import com.google.inject.Inject;
import com.mmxlabs.common.Equality;
import com.mmxlabs.models.lng.adp.ADPFactory;
import com.mmxlabs.models.lng.adp.ADPPackage;
import com.mmxlabs.models.lng.adp.DESSalesMarketAllocationRow;
import com.mmxlabs.models.lng.adp.MullEntityRow;
import com.mmxlabs.models.lng.adp.MullProfile;
import com.mmxlabs.models.lng.adp.MullSubprofile;
import com.mmxlabs.models.lng.adp.SalesContractAllocationRow;
import com.mmxlabs.models.lng.adp.utils.IMullRelativeEntitlementImportCommandProvider;
import com.mmxlabs.models.lng.cargo.Inventory;
import com.mmxlabs.models.lng.commercial.BaseLegalEntity;
import com.mmxlabs.models.mmxcore.MMXCorePackage;
import com.mmxlabs.models.mmxcore.MMXRootObject;
import com.mmxlabs.models.mmxcore.impl.MMXContentAdapter;
import com.mmxlabs.models.ui.editorpart.DefaultStatusProvider;
import com.mmxlabs.models.ui.editorpart.IScenarioEditingLocation;
import com.mmxlabs.models.ui.editors.DetailToolbarManager;
import com.mmxlabs.models.ui.editors.ICommandHandler;
import com.mmxlabs.models.ui.editors.IDisplayComposite;
import com.mmxlabs.models.ui.editors.IInlineEditorWrapper;
import com.mmxlabs.models.ui.editors.dialogs.IDialogEditingContext;
import com.mmxlabs.models.ui.editors.util.CommandUtil;
import com.mmxlabs.models.ui.impl.DefaultDetailComposite;
import com.mmxlabs.models.ui.tabular.EObjectTableViewer;
import com.mmxlabs.models.ui.tabular.manipulators.BasicAttributeManipulator;
import com.mmxlabs.models.ui.tabular.manipulators.MultipleReferenceManipulator;
import com.mmxlabs.models.ui.tabular.manipulators.NumericAttributeManipulator;
import com.mmxlabs.models.ui.tabular.manipulators.SingleReferenceManipulator;
import com.mmxlabs.rcp.common.actions.RunnableAction;

public class MullProfileDetailComposite extends Composite implements IDisplayComposite {

	private IDisplayComposite delegate;
	private final Composite superParent;
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
	
	private Action addInventorySubprofileRow;
	private Action deleteInventorySubprofileRow;
	private Action addEntityRow;
	private Action deleteEntityRow;
	private Action addContractRow;
	private Action deleteContractRow;
	private Action addMarketRow;
	private Action deleteMarketRow;
	
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
		
		this.superParent = parent;
		this.dialogContext = dialogContext;
		toolkit.adapt(this);
		setLayout(new GridLayout(1, false));
		delegate = new DefaultDetailComposite(this, style, toolkit);
		GridData gd = new GridData(GridData.FILL_HORIZONTAL);
		gd.widthHint = 800;
		delegate.getComposite().setLayoutData(gd);
		inventoryTableComposite = toolkit.createComposite(this, style);
		inventoryTableComposite.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		inventoryTableComposite.setLayout(new GridLayout());
		
		this.viewer = getInventoryTableViewer(inventoryTableComposite, toolkit);
		this.entityTableViewer = getEntityTableViewer(inventoryTableComposite, toolkit);
		this.contractViewer = getContractTableViewer(inventoryTableComposite, toolkit);
		this.marketViewer = getMarketTableViewer(inventoryTableComposite, toolkit);
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
		packViewer(viewer);
		entityTableGroup.setVisible(!viewer.getSelection().isEmpty());
		contractTableGroup.setVisible(!entityTableViewer.getSelection().isEmpty());
		marketTableGroup.setVisible(!entityTableViewer.getSelection().isEmpty());
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
	
	private EObjectTableViewer getInventoryTableViewer(Composite parent, FormToolkit toolkit) {
		Group inventoryTableGroup = new Group(parent, SWT.NONE);
		inventoryTableGroup.setLayout(new GridLayout(1, false));
		
		final Color systemWhite = Display.getDefault().getSystemColor(SWT.COLOR_WHITE);
		inventoryTableGroup.setBackground(systemWhite);
		inventoryTableGroup.setLayoutData(new GridData(GridData.VERTICAL_ALIGN_BEGINNING | GridData.FILL_BOTH));
		inventoryTableGroup.setText("Inventory Table");
		
		final DetailToolbarManager buttonManager = new DetailToolbarManager(inventoryTableGroup, SWT.TOP);
		buttonManager.getToolbarManager().getControl().setBackground(systemWhite);
		
		
		
		addInventorySubprofileRow = new Action("Add") {
			@Override
			public void run() {
				final MullSubprofile newLine = ADPFactory.eINSTANCE.createMullSubprofile();
				commandHandler.handleCommand(AddCommand.create(commandHandler.getEditingDomain(), oldValue, ADPPackage.Literals.MULL_PROFILE__INVENTORIES, newLine), oldValue, ADPPackage.Literals.MULL_PROFILE__INVENTORIES);
				if (viewer != null) {
					viewer.setSelection(new StructuredSelection(newLine));
					viewer.refresh();
				}
			}
		};
		addInventorySubprofileRow.setImageDescriptor(PlatformUI.getWorkbench().getSharedImages().getImageDescriptor(ISharedImages.IMG_OBJ_ADD));
		buttonManager.getToolbarManager().add(addInventorySubprofileRow);
		
		deleteInventorySubprofileRow = new Action("Delete") {
			@Override
			public void run() {
				if (viewer != null) {
					final ISelection selection = viewer.getSelection();
					if (selection.isEmpty()) 
						return;
					if (selection instanceof IStructuredSelection) {
						final IStructuredSelection iStructuredSelection = (IStructuredSelection) selection;
						final CompoundCommand cc = new CompoundCommand();
						final EditingDomain ed = dialogContext.getScenarioEditingLocation().getEditingDomain();
						for (final Object o : iStructuredSelection) {
							final MullSubprofile line  = (MullSubprofile) o;
							cc.append(RemoveCommand.create(ed, line.eContainer(), line.eContainingFeature(), line));
						}
						ed.getCommandStack().execute(cc);
						viewer.refresh();
					}
				}
			}
		};
		deleteInventorySubprofileRow.setImageDescriptor(PlatformUI.getWorkbench().getSharedImages().getImageDescriptor(ISharedImages.IMG_ETOOL_DELETE));
		deleteInventorySubprofileRow.setEnabled(false);
		buttonManager.getToolbarManager().add(deleteInventorySubprofileRow);
		
		Action packAction = new Action("Pack") {
			@Override
			public void run() {
				if (viewer != null && !viewer.getControl().isDisposed()) {
					packViewer(viewer);
				}
			}
		};
		ResourceLocator.imageDescriptorFromBundle("com.mmxlabs.rcp.common", "/icons/pack.gif").ifPresent(packAction::setImageDescriptor);
		buttonManager.getToolbarManager().add(packAction);
		buttonManager.getToolbarManager().update(true);
		
		final IScenarioEditingLocation sel = dialogContext.getScenarioEditingLocation();
		final EObjectTableViewer eViewer = new EObjectTableViewer(inventoryTableGroup, SWT.FULL_SELECTION | SWT.V_SCROLL | SWT.H_SCROLL);
		eViewer.addTypicalColumn("Inventory", new SingleReferenceManipulator(ADPPackage.eINSTANCE.getMullSubprofile_Inventory(), sel.getReferenceValueProviderCache(), sel.getEditingDomain()) {
			@Override
			public void runSetCommand(Object object, Object value) {
				super.runSetCommand(object, value);
				dialogContext.getDialogController().validate();
				eViewer.refresh();
			}
		});
		
		eViewer.setStatusProvider(statusProvider);
		
		eViewer.init(sel.getAdapterFactory(), sel.getModelReference(), ADPPackage.Literals.MULL_PROFILE__INVENTORIES);
		final GridData gridData = new GridData(SWT.FILL, SWT.FILL, true, true);
		gridData.heightHint = 60;
		final Grid table = eViewer.getGrid();
		table.setLayoutData(gridData);
		table.setLinesVisible(true);
		table.setHeaderVisible(true);
		
		eViewer.addSelectionChangedListener(event -> {
			deleteInventorySubprofileRow.setEnabled(!event.getSelection().isEmpty());
			if (!event.getSelection().isEmpty()) {
				Object selection = event.getStructuredSelection().getFirstElement();
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
					packViewer(entityTableViewer);
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
	
	private EObjectTableViewer getEntityTableViewer(Composite parent, FormToolkit toolkit) {
		entityTableGroup = new Group(parent, SWT.NONE);
		entityTableGroup.setLayout(new GridLayout(1, false));
		
		final Color systemWhite = Display.getDefault().getSystemColor(SWT.COLOR_WHITE);
		entityTableGroup.setLayout(new GridLayout(1, false));
		entityTableGroup.setBackground(systemWhite);
		
		entityTableGroup.setLayoutData(new GridData(GridData.VERTICAL_ALIGN_BEGINNING | GridData.FILL_BOTH));
		entityTableGroup.setText("Entity Table");
		
		final DetailToolbarManager buttonManager = new DetailToolbarManager(entityTableGroup, SWT.TOP);
		buttonManager.getToolbarManager().getControl().setBackground(systemWhite);
		
		addEntityRow = new Action("Add") {
			@Override
			public void run() {
				final MullEntityRow newLine = ADPFactory.eINSTANCE.createMullEntityRow();
				commandHandler.handleCommand(AddCommand.create(commandHandler.getEditingDomain(), oldSubprofile, ADPPackage.Literals.MULL_SUBPROFILE__ENTITY_TABLE, newLine), oldSubprofile, ADPPackage.Literals.MULL_SUBPROFILE__ENTITY_TABLE);
				if (entityTableViewer != null) {
					entityTableViewer.setSelection(new StructuredSelection(newLine));
					entityTableViewer.refresh();
				}
			}
		};
		addEntityRow.setImageDescriptor(PlatformUI.getWorkbench().getSharedImages().getImageDescriptor(ISharedImages.IMG_OBJ_ADD));
		buttonManager.getToolbarManager().add(addEntityRow);
		
		final Action importMullEntitiesAction = new Action("Import from CSV") {
			@Override
			public void run() {
				mullRelativeEntitlementImportCommandProvider.run(oldSubprofile, dialogContext.getScenarioEditingLocation().getScenarioInstance());
				if (entityTableViewer != null) {
					entityTableViewer.setSelection(null);
					entityTableViewer.refresh();
				}
			}
		};
		importMullEntitiesAction.setImageDescriptor(PlatformUI.getWorkbench().getSharedImages().getImageDescriptor(ISharedImages.IMG_OBJ_ADD));
		buttonManager.getToolbarManager().add(importMullEntitiesAction);
		
		deleteEntityRow = new Action("Delete") {
			@Override
			public void run() {
				if (entityTableViewer != null) {
					final ISelection selection = entityTableViewer.getSelection();
					if (selection.isEmpty())
						return;
					if (selection instanceof IStructuredSelection) {
						final IStructuredSelection iStructuredSelection = (IStructuredSelection) selection;
						final CompoundCommand cc = new CompoundCommand();
						final EditingDomain ed = dialogContext.getScenarioEditingLocation().getEditingDomain();
						for (final Object o : iStructuredSelection) {
							final MullEntityRow line = (MullEntityRow) o;
							cc.append(RemoveCommand.create(ed, line.eContainer(), line.eContainingFeature(), line));
						}
						ed.getCommandStack().execute(cc);
						entityTableViewer.refresh();
					}
				}
			}
		};
		deleteEntityRow.setImageDescriptor(PlatformUI.getWorkbench().getSharedImages().getImageDescriptor(ISharedImages.IMG_ETOOL_DELETE));
		deleteEntityRow.setEnabled(false);
		buttonManager.getToolbarManager().add(deleteEntityRow);
		
		Action packAction = new Action("Pack") {
			@Override
			public void run() {
				if (entityTableViewer != null && !entityTableViewer.getControl().isDisposed()) {
					packViewer(entityTableViewer);
				}
			}
		};
		ResourceLocator.imageDescriptorFromBundle("com.mmxlabs.rcp.common", "/icons/pack.gif").ifPresent(packAction::setImageDescriptor);
		buttonManager.getToolbarManager().add(packAction);
		buttonManager.getToolbarManager().update(true);
		
		final IScenarioEditingLocation sel = dialogContext.getScenarioEditingLocation();
		final EObjectTableViewer eViewer = new EObjectTableViewer(entityTableGroup, SWT.FULL_SELECTION | SWT.V_SCROLL | SWT.H_SCROLL);
		eViewer.addTypicalColumn("Entity", new SingleReferenceManipulator(ADPPackage.eINSTANCE.getMullEntityRow_Entity(), sel.getReferenceValueProviderCache(), sel.getEditingDomain()) {
			@Override
			public void runSetCommand(Object object, Object value) {
				super.runSetCommand(object, value);
				dialogContext.getDialogController().validate();
				if (value != null && value instanceof BaseLegalEntity) {
					final String entityName = ((BaseLegalEntity) value).getName();
					if (entityName != null) {
						contractTableGroup.setText(entityName + " sales contract allocations");
						marketTableGroup.setText(entityName + " market allocations");
					} else {
						contractTableGroup.setText("Sales contract allocations");
						marketTableGroup.setText("Market allocations");
					}
				}
				eViewer.refresh();
			}
		});
		eViewer.addTypicalColumn("Initial Allocation", new BasicAttributeManipulator(ADPPackage.eINSTANCE.getMullEntityRow_InitialAllocation(), sel.getEditingDomain()) {
			@Override
			public void runSetCommand(Object object, Object value) {
				super.runSetCommand(object, value);
				dialogContext.getDialogController().validate();
				eViewer.refresh();
			}
		});
		eViewer.addTypicalColumn("Reference Entitlement", new NumericAttributeManipulator(ADPPackage.eINSTANCE.getMullEntityRow_RelativeEntitlement(), sel.getEditingDomain()) {
			@Override
			public void runSetCommand(Object object, Object value) {
				super.runSetCommand(object, value);
				dialogContext.getDialogController().validate();
				eViewer.refresh();
			}
		});
		
		eViewer.setStatusProvider(statusProvider);
		
		eViewer.init(sel.getAdapterFactory(), sel.getModelReference(), ADPPackage.Literals.MULL_SUBPROFILE__ENTITY_TABLE);
		final GridData gridData = new GridData(SWT.FILL, SWT.FILL, true, true);
		gridData.heightHint = 140;
		final Grid table = eViewer.getGrid();
		table.setLayoutData(gridData);
		table.setLinesVisible(true);
		table.setHeaderVisible(true);
		
		eViewer.addSelectionChangedListener(event -> {
			deleteEntityRow.setEnabled(!event.getSelection().isEmpty());
			if (!event.getSelection().isEmpty()) {
				Object selection = event.getStructuredSelection().getFirstElement();
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
					packViewer(contractViewer);
					packViewer(marketViewer);
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
	
	private EObjectTableViewer getContractTableViewer(Composite parent, FormToolkit toolkit) {
		contractTableGroup = new Group(parent, SWT.NONE);
		contractTableGroup.setLayout(new GridLayout(1, false));
		final Color systemWhite = Display.getDefault().getSystemColor(SWT.COLOR_WHITE);
		contractTableGroup.setBackground(systemWhite);
		contractTableGroup.setLayoutData(new GridData(GridData.VERTICAL_ALIGN_BEGINNING | GridData.FILL_BOTH));
		contractTableGroup.setText("Contract allocations");
		contractTableGroup.setVisible(false);
		
		final DetailToolbarManager buttonManager = new DetailToolbarManager(contractTableGroup, SWT.TOP);
		buttonManager.getToolbarManager().getControl().setBackground(systemWhite);
		
		addContractRow = new Action("Add") {
			@Override
			public void run() {
				Object selection = entityTableViewer.getStructuredSelection().getFirstElement();
				if (selection instanceof MullEntityRow) {
					final SalesContractAllocationRow newLine = ADPFactory.eINSTANCE.createSalesContractAllocationRow();
					MullEntityRow selectedRow = (MullEntityRow) selection;
					commandHandler.handleCommand(AddCommand.create(commandHandler.getEditingDomain(), selectedRow, ADPPackage.Literals.MULL_ENTITY_ROW__SALES_CONTRACT_ALLOCATION_ROWS, newLine), selectedRow, ADPPackage.Literals.MULL_ENTITY_ROW__SALES_CONTRACT_ALLOCATION_ROWS);
					if (contractViewer != null) {
						contractViewer.setSelection(new StructuredSelection(newLine));
						contractViewer.refresh();
					}
				}
			}
		};
		addContractRow.setImageDescriptor(PlatformUI.getWorkbench().getSharedImages().getImageDescriptor(ISharedImages.IMG_OBJ_ADD));
		buttonManager.getToolbarManager().add(addContractRow);
		
		deleteContractRow = new Action("Delete") {
			@Override
			public void run() {
				if (contractViewer != null) {
					final ISelection selection = contractViewer.getSelection();
					if (selection.isEmpty()) {
						return;
					}
					if (selection instanceof IStructuredSelection) {
						final IStructuredSelection iStructuredSelection = (IStructuredSelection) selection;
						final CompoundCommand cc = new CompoundCommand();
						final EditingDomain ed = dialogContext.getScenarioEditingLocation().getEditingDomain();
						for (final Object o : iStructuredSelection) {
							final SalesContractAllocationRow line = (SalesContractAllocationRow) o;
							cc.append(RemoveCommand.create(ed, line.eContainer(), line.eContainingFeature(), line));
						}
						ed.getCommandStack().execute(cc);
						contractViewer.refresh();
					}
				}
			}
		};
		deleteContractRow.setImageDescriptor(PlatformUI.getWorkbench().getSharedImages().getImageDescriptor(ISharedImages.IMG_ETOOL_DELETE));
		deleteContractRow.setEnabled(false);
		buttonManager.getToolbarManager().add(deleteContractRow);
		
		Action packAction = new Action("Pack") {
			@Override
			public void run() {
				if (contractViewer != null && !contractViewer.getControl().isDisposed()) {
					packViewer(contractViewer);
				}
			}
		};
		ResourceLocator.imageDescriptorFromBundle("com.mmxlabs.rcp.common", "/icons/pack.gif").ifPresent(packAction::setImageDescriptor);
		buttonManager.getToolbarManager().add(packAction);
		buttonManager.getToolbarManager().update(true);
		
		final IScenarioEditingLocation sel = dialogContext.getScenarioEditingLocation();
		final EObjectTableViewer eViewer = new EObjectTableViewer(contractTableGroup, SWT.FULL_SELECTION | SWT.V_SCROLL | SWT.H_SCROLL);
		eViewer.addTypicalColumn("Contract", new SingleReferenceManipulator(ADPPackage.eINSTANCE.getSalesContractAllocationRow_Contract(), sel.getReferenceValueProviderCache(), sel.getEditingDomain()) {
			@Override
			public void runSetCommand(Object object, Object value) {
				super.runSetCommand(object, value);
				dialogContext.getDialogController().validate();
				eViewer.refresh();
			}
		});
		eViewer.addTypicalColumn("ACQ", new NumericAttributeManipulator(ADPPackage.eINSTANCE.getMullAllocationRow_Weight(), sel.getEditingDomain()) {
			@Override
			public void runSetCommand(Object object, Object value) {
				super.runSetCommand(object, value);
				dialogContext.getDialogController().validate();
				eViewer.refresh();
			}
		});
		eViewer.addTypicalColumn("Vessels", new MultipleReferenceManipulator(ADPPackage.eINSTANCE.getMullAllocationRow_Vessels(), sel.getReferenceValueProviderCache(), sel.getEditingDomain(), MMXCorePackage.eINSTANCE.getNamedObject_Name()) {
//			@Override
//			public void runSetCommand(Object object, Object value) {
//				super.runSetCommand(object, value);
//				dialogContext.getDialogController().validate();
//				eViewer.refresh();
//			}
		});
		
		eViewer.setStatusProvider(statusProvider);
	
		eViewer.init(sel.getAdapterFactory(), sel.getModelReference(), ADPPackage.Literals.MULL_ENTITY_ROW__SALES_CONTRACT_ALLOCATION_ROWS);
		final GridData gridData = new GridData(SWT.FILL, SWT.FILL, true, true);
		gridData.heightHint = 120;
		final Grid table = eViewer.getGrid();
		table.setLayoutData(gridData);
		table.setLinesVisible(true);
		table.setHeaderVisible(true);
		
		eViewer.addSelectionChangedListener(event -> {
			deleteContractRow.setEnabled(!event.getSelection().isEmpty());
		});
		return eViewer;
	}
	
	private EObjectTableViewer getMarketTableViewer(Composite parent, FormToolkit toolkit) {
		marketTableGroup = new Group(parent, SWT.NONE);
		marketTableGroup.setLayout(new GridLayout(1, false));
		final Color systemWhite = Display.getDefault().getSystemColor(SWT.COLOR_WHITE);
		marketTableGroup.setBackground(systemWhite);
		marketTableGroup.setLayoutData(new GridData(GridData.VERTICAL_ALIGN_BEGINNING | GridData.FILL_BOTH));
		marketTableGroup.setText("Market allocations");
		marketTableGroup.setVisible(false);
		
		final DetailToolbarManager buttonManager = new DetailToolbarManager(marketTableGroup, SWT.TOP);
		buttonManager.getToolbarManager().getControl().setBackground(systemWhite);
		
		addMarketRow = new Action("Add") {
			@Override
			public void run() {
				Object selection = entityTableViewer.getStructuredSelection().getFirstElement();
				if (selection instanceof MullEntityRow) {
					final DESSalesMarketAllocationRow newLine = ADPFactory.eINSTANCE.createDESSalesMarketAllocationRow();
					MullEntityRow selectedRow = (MullEntityRow) selection;
					commandHandler.handleCommand(AddCommand.create(commandHandler.getEditingDomain(), selectedRow, ADPPackage.Literals.MULL_ENTITY_ROW__DES_SALES_MARKET_ALLOCATION_ROWS, newLine), selectedRow, ADPPackage.Literals.MULL_ENTITY_ROW__DES_SALES_MARKET_ALLOCATION_ROWS);
					if (marketViewer != null) {
						marketViewer.setSelection(new StructuredSelection(newLine));
						marketViewer.refresh();
					}
				}
			}
		};
		addMarketRow.setImageDescriptor(PlatformUI.getWorkbench().getSharedImages().getImageDescriptor(ISharedImages.IMG_OBJ_ADD));
		buttonManager.getToolbarManager().add(addMarketRow);
		
		deleteMarketRow = new Action("Delete") {
			@Override
			public void run() {
				if (marketViewer != null) {
					final ISelection selection = marketViewer.getSelection();
					if (selection.isEmpty()) {
						return;
					}
					if (selection instanceof IStructuredSelection) {
						final IStructuredSelection iStructuredSelection = (IStructuredSelection) selection;
						final CompoundCommand cc = new CompoundCommand();
						final EditingDomain ed = dialogContext.getScenarioEditingLocation().getEditingDomain();
						for (final Object o : iStructuredSelection) {
							final DESSalesMarketAllocationRow line = (DESSalesMarketAllocationRow) o;
							cc.append(RemoveCommand.create(ed, line.eContainer(), line.eContainingFeature(), line));
						}
						ed.getCommandStack().execute(cc);
						marketViewer.refresh();
					}
				}
			}
		};
		deleteMarketRow.setImageDescriptor(PlatformUI.getWorkbench().getSharedImages().getImageDescriptor(ISharedImages.IMG_ETOOL_DELETE));
		deleteMarketRow.setEnabled(false);
		buttonManager.getToolbarManager().add(deleteMarketRow);
		
		Action packAction = new Action("Pack") {
			@Override
			public void run() {
				if (marketViewer != null && !marketViewer.getControl().isDisposed()) {
					packViewer(marketViewer);
				}
			}
		};
		ResourceLocator.imageDescriptorFromBundle("com.mmxlabs.rcp.common", "/icons/pack.gif").ifPresent(packAction::setImageDescriptor);
		buttonManager.getToolbarManager().add(packAction);
		buttonManager.getToolbarManager().update(true);
		
		final IScenarioEditingLocation sel = dialogContext.getScenarioEditingLocation();
		final EObjectTableViewer eViewer = new EObjectTableViewer(marketTableGroup, SWT.FULL_SELECTION | SWT.V_SCROLL | SWT.H_SCROLL);
//		eViewer.addTypicalColumn("Market", new BasicAttributeManipulator(ADPPackage.eINSTANCE.getmarketAllocationRow_Market(), sel.getEditingDomain()) {
//			@Override
//			public void runSetCommand(Object object, Object value) {
//				super.runSetCommand(object, value);
//				dialogContext.getDialogController().validate();
//				eViewer.refresh();
//			}
//		});
		eViewer.addTypicalColumn("Market", new SingleReferenceManipulator(ADPPackage.eINSTANCE.getDESSalesMarketAllocationRow_DesSalesMarket(), sel.getReferenceValueProviderCache(), sel.getEditingDomain()) {
			@Override
			public void runSetCommand(Object object, Object value) {
				super.runSetCommand(object, value);
				dialogContext.getDialogController().validate();
				eViewer.refresh();
			}
		});
		eViewer.addTypicalColumn("ACQ", new NumericAttributeManipulator(ADPPackage.eINSTANCE.getMullAllocationRow_Weight(), sel.getEditingDomain()) 
		{
			@Override
			public void runSetCommand(Object object, Object value) {
				super.runSetCommand(object, value);
//				dialogContext.getDialogController().validate();
//				eViewer.refresh();
			}
		}
		);

		eViewer.addTypicalColumn("Vessels", new MultipleReferenceManipulator(ADPPackage.eINSTANCE.getMullAllocationRow_Vessels(), sel.getReferenceValueProviderCache(), sel.getEditingDomain(), MMXCorePackage.eINSTANCE.getNamedObject_Name()) {
			@Override
			public void doSetValue(final Object object, final Object value) {
				super.doSetValue(object, value);
			}
			
//			@Override
//			public void runSetCommand(Object object, Object value) {
//				super.runSetCommand(object, value);
//				dialogContext.getDialogController().validate();
//				eViewer.refresh();
//			}
		});
		
		eViewer.setStatusProvider(statusProvider);
		
		eViewer.init(sel.getAdapterFactory(), sel.getModelReference(), ADPPackage.Literals.MULL_ENTITY_ROW__DES_SALES_MARKET_ALLOCATION_ROWS);
		final GridData gridData = new GridData(SWT.FILL, SWT.FILL, true, true);
		gridData.heightHint = 120;
		final Grid table = eViewer.getGrid();
		table.setLayoutData(gridData);
		table.setLinesVisible(true);
		table.setHeaderVisible(true);
		
		eViewer.addSelectionChangedListener(event -> {
			int i = 0;
			deleteMarketRow.setEnabled(!event.getSelection().isEmpty());
		});
		return eViewer;
	}
	
	private static void packViewer(@NonNull EObjectTableViewer viewer) {
		final GridColumn[] columns = viewer.getGrid().getColumns();
		for (final GridColumn c : columns) {
			if (c.getResizeable()) {
				c.pack();
			}
		}
	}
	

}
