package com.mmxlabs.models.lng.adp.presentation.composites;


import java.util.Collection;

import javax.swing.plaf.basic.BasicLabelUI;

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
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.forms.widgets.FormToolkit;

import com.mmxlabs.models.lng.adp.ADPFactory;
import com.mmxlabs.models.lng.adp.ADPPackage;
import com.mmxlabs.models.lng.adp.ContractAllocationRow;
import com.mmxlabs.models.lng.adp.InventoryADPEntityRow;
import com.mmxlabs.models.lng.adp.InventoryProfile;
import com.mmxlabs.models.lng.adp.MarketAllocationRow;
import com.mmxlabs.models.lng.commercial.BaseLegalEntity;
import com.mmxlabs.models.lng.fleet.FleetModel;
import com.mmxlabs.models.lng.scenario.model.util.ScenarioModelUtil;
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
import com.mmxlabs.models.ui.impl.DefaultDetailComposite;
import com.mmxlabs.models.ui.tabular.EObjectTableViewer;
import com.mmxlabs.models.ui.tabular.manipulators.BasicAttributeManipulator;
import com.mmxlabs.models.ui.tabular.manipulators.MultipleReferenceManipulator;
import com.mmxlabs.models.ui.tabular.manipulators.NumericAttributeManipulator;
import com.mmxlabs.models.ui.tabular.manipulators.SingleReferenceManipulator;
import com.mmxlabs.models.ui.tabular.manipulators.ValueListAttributeManipulator;
import com.mmxlabs.scenario.service.model.manager.IScenarioDataProvider;

public class InventoryProfileDetailComposite extends Composite implements IDisplayComposite {

	private IDisplayComposite delegate;
	private final Composite superParent;
	private InventoryProfile oldValue = null;
	private IDialogEditingContext dialogContext;
	private IStatus status;
	private final EObjectTableViewer viewer;
	private final Composite entityTableComposite;
	private ICommandHandler commandHandler;
	private final EObjectTableViewer subViewer;
	private final EObjectTableViewer contractViewer;
	
	private Group subTableGroup;
	private Group contractTableGroup;
	
	private Action addEntityRow;
	private Action deleteEntityRow;
	private Action addSubRow;
	private Action deleteSubRow;
	private Action addContractRow;
	private Action deleteContractRow;
	
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
				InventoryProfileDetailComposite.this.removeAdapter();
			}
		}
	};

	InventoryProfileDetailComposite(final Composite parent, final IDialogEditingContext dialogContext, final int style, final FormToolkit toolkit) {
		super(parent, style);
		addDisposeListener(event -> removeAdapter());
		this.superParent = parent;
		this.dialogContext = dialogContext;
		toolkit.adapt(this);
		setLayout(new GridLayout(1, false));
		delegate = new DefaultDetailComposite(this, style, toolkit);
		GridData gd = new GridData(GridData.FILL_HORIZONTAL);
		gd.widthHint = 400;
		delegate.getComposite().setLayoutData(gd);
		entityTableComposite = toolkit.createComposite(this, style);
		entityTableComposite.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		entityTableComposite.setLayout(new GridLayout());
		
		this.viewer = getEntityTableViewer(entityTableComposite, toolkit);
		this.contractViewer = getContractTableViewer(entityTableComposite, toolkit);
		this.subViewer = getSubViewer(entityTableComposite, toolkit);
	}
	
	@Override
	public Composite getComposite() {
		return this;
	}

	@Override
	public void display(IDialogEditingContext dialogContext, MMXRootObject root, EObject value, Collection<EObject> range, EMFDataBindingContext dbc) {
		delegate.display(dialogContext, root, value, range, dbc);
		removeAdapter();
		oldValue = (InventoryProfile) value;
		value.eAdapters().add(adapter);
		viewer.setInput(value);
		subTableGroup.setVisible(!viewer.getSelection().isEmpty());
		contractTableGroup.setVisible(!viewer.getSelection().isEmpty());
		packViewer(viewer);
	}
	
	@Override
	public void dispose() {
		removeAdapter();
		super.dispose();
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
	
	void removeAdapter() {
		if (oldValue != null) {
			oldValue.eAdapters().remove(adapter);
			oldValue = null;
		}
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
				Object selection = viewer.getStructuredSelection().getFirstElement();
				if (selection instanceof InventoryADPEntityRow) {
					final ContractAllocationRow newLine = ADPFactory.eINSTANCE.createContractAllocationRow();
					InventoryADPEntityRow selectedRow = (InventoryADPEntityRow) selection;
					commandHandler.handleCommand(AddCommand.create(commandHandler.getEditingDomain(), selectedRow, ADPPackage.Literals.INVENTORY_ADP_ENTITY_ROW__CONTRACT_ALLOCATION_ROWS, newLine), selectedRow, ADPPackage.Literals.INVENTORY_ADP_ENTITY_ROW__CONTRACT_ALLOCATION_ROWS);
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
							final ContractAllocationRow line = (ContractAllocationRow) o;
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
		eViewer.addTypicalColumn("Contract", new SingleReferenceManipulator(ADPPackage.eINSTANCE.getContractAllocationRow_Contract(), sel.getReferenceValueProviderCache(), sel.getEditingDomain()) {
			@Override
			public void runSetCommand(Object object, Object value) {
				super.runSetCommand(object, value);
				dialogContext.getDialogController().validate();
				eViewer.refresh();
			}
		});
		eViewer.addTypicalColumn("Weight", new NumericAttributeManipulator(ADPPackage.eINSTANCE.getContractAllocationRow_Weight(), sel.getEditingDomain()) {
			@Override
			public void runSetCommand(Object object, Object value) {
				super.runSetCommand(object, value);
				dialogContext.getDialogController().validate();
				eViewer.refresh();
			}
		});
		eViewer.addTypicalColumn("Vessels", new MultipleReferenceManipulator(ADPPackage.eINSTANCE.getContractAllocationRow_Vessels(), sel.getReferenceValueProviderCache(), sel.getEditingDomain(), MMXCorePackage.eINSTANCE.getNamedObject_Name()) {
			@Override
			public void runSetCommand(Object object, Object value) {
				super.runSetCommand(object, value);
				dialogContext.getDialogController().validate();
				eViewer.refresh();
			}
		});
		
		eViewer.setStatusProvider(statusProvider);
	
		eViewer.init(sel.getAdapterFactory(), sel.getModelReference(), ADPPackage.Literals.INVENTORY_ADP_ENTITY_ROW__CONTRACT_ALLOCATION_ROWS);
		final GridData gridData = new GridData(SWT.FILL, SWT.FILL, true, true);
		gridData.heightHint = 120;
		final Grid table = eViewer.getGrid();
		table.setLayoutData(gridData);
		table.setLinesVisible(true);
		table.setHeaderVisible(true);
		
		eViewer.addSelectionChangedListener(event -> {
			int i = 0;
			deleteContractRow.setEnabled(!event.getSelection().isEmpty());
		});
		return eViewer;
	}
	
	private EObjectTableViewer getSubViewer(Composite parent, FormToolkit toolkit) {
		subTableGroup = new Group(parent, SWT.NONE);
		subTableGroup.setLayout(new GridLayout(1, false));
		final Color systemWhite = Display.getDefault().getSystemColor(SWT.COLOR_WHITE);
		subTableGroup.setBackground(systemWhite);
		subTableGroup.setLayoutData(new GridData(GridData.VERTICAL_ALIGN_BEGINNING | GridData.FILL_BOTH));
		subTableGroup.setText("Market allocations");
		subTableGroup.setVisible(false);
		
		final DetailToolbarManager buttonManager = new DetailToolbarManager(subTableGroup, SWT.TOP);
		buttonManager.getToolbarManager().getControl().setBackground(systemWhite);
		
		addSubRow = new Action("Add") {
			@Override
			public void run() {
				Object selection = viewer.getStructuredSelection().getFirstElement();
				if (selection instanceof InventoryADPEntityRow) {
					final MarketAllocationRow newLine = ADPFactory.eINSTANCE.createMarketAllocationRow();
					InventoryADPEntityRow selectedRow = (InventoryADPEntityRow) selection;
					commandHandler.handleCommand(AddCommand.create(commandHandler.getEditingDomain(), selectedRow, ADPPackage.Literals.INVENTORY_ADP_ENTITY_ROW__MARKET_ALLOCATION_ROWS, newLine), selectedRow, ADPPackage.Literals.INVENTORY_ADP_ENTITY_ROW__MARKET_ALLOCATION_ROWS);
					if (subViewer != null) {
						subViewer.setSelection(new StructuredSelection(newLine));
						subViewer.refresh();
					}
				}
			}
		};
		addSubRow.setImageDescriptor(PlatformUI.getWorkbench().getSharedImages().getImageDescriptor(ISharedImages.IMG_OBJ_ADD));
		buttonManager.getToolbarManager().add(addSubRow);
		
		deleteSubRow = new Action("Delete") {
			@Override
			public void run() {
				if (subViewer != null) {
					final ISelection selection = subViewer.getSelection();
					if (selection.isEmpty()) {
						return;
					}
					if (selection instanceof IStructuredSelection) {
						final IStructuredSelection iStructuredSelection = (IStructuredSelection) selection;
						final CompoundCommand cc = new CompoundCommand();
						final EditingDomain ed = dialogContext.getScenarioEditingLocation().getEditingDomain();
						for (final Object o : iStructuredSelection) {
							final MarketAllocationRow line = (MarketAllocationRow) o;
							cc.append(RemoveCommand.create(ed, line.eContainer(), line.eContainingFeature(), line));
						}
						ed.getCommandStack().execute(cc);
						subViewer.refresh();
					}
				}
			}
		};
		deleteSubRow.setImageDescriptor(PlatformUI.getWorkbench().getSharedImages().getImageDescriptor(ISharedImages.IMG_ETOOL_DELETE));
		deleteSubRow.setEnabled(false);
		buttonManager.getToolbarManager().add(deleteSubRow);
		
		Action packAction = new Action("Pack") {
			@Override
			public void run() {
				if (subViewer != null && !subViewer.getControl().isDisposed()) {
					packViewer(subViewer);
				}
			}
		};
		ResourceLocator.imageDescriptorFromBundle("com.mmxlabs.rcp.common", "/icons/pack.gif").ifPresent(packAction::setImageDescriptor);
		buttonManager.getToolbarManager().add(packAction);
		buttonManager.getToolbarManager().update(true);
		
		final IScenarioEditingLocation sel = dialogContext.getScenarioEditingLocation();
		final EObjectTableViewer eViewer = new EObjectTableViewer(subTableGroup, SWT.FULL_SELECTION | SWT.V_SCROLL | SWT.H_SCROLL);
//		eViewer.addTypicalColumn("Market", new BasicAttributeManipulator(ADPPackage.eINSTANCE.getmarketAllocationRow_Market(), sel.getEditingDomain()) {
//			@Override
//			public void runSetCommand(Object object, Object value) {
//				super.runSetCommand(object, value);
//				dialogContext.getDialogController().validate();
//				eViewer.refresh();
//			}
//		});
		eViewer.addTypicalColumn("Market", new SingleReferenceManipulator(ADPPackage.eINSTANCE.getMarketAllocationRow_Market(), sel.getReferenceValueProviderCache(), sel.getEditingDomain()) {
			@Override
			public void runSetCommand(Object object, Object value) {
				super.runSetCommand(object, value);
				dialogContext.getDialogController().validate();
				eViewer.refresh();
			}
		});
		eViewer.addTypicalColumn("Weight", new NumericAttributeManipulator(ADPPackage.eINSTANCE.getMarketAllocationRow_Weight(), sel.getEditingDomain()) {
			@Override
			public void runSetCommand(Object object, Object value) {
				super.runSetCommand(object, value);
				dialogContext.getDialogController().validate();
				eViewer.refresh();
			}
		});
		eViewer.addTypicalColumn("Vessels", new MultipleReferenceManipulator(ADPPackage.eINSTANCE.getMarketAllocationRow_Vessels(), sel.getReferenceValueProviderCache(), sel.getEditingDomain(), MMXCorePackage.eINSTANCE.getNamedObject_Name()) {
			@Override
			public void runSetCommand(Object object, Object value) {
				super.runSetCommand(object, value);
				dialogContext.getDialogController().validate();
				eViewer.refresh();
			}
		});
		
		eViewer.setStatusProvider(statusProvider);
		
		eViewer.init(sel.getAdapterFactory(), sel.getModelReference(), ADPPackage.Literals.INVENTORY_ADP_ENTITY_ROW__MARKET_ALLOCATION_ROWS);
		final GridData gridData = new GridData(SWT.FILL, SWT.FILL, true, true);
		gridData.heightHint = 120;
		final Grid table = eViewer.getGrid();
		table.setLayoutData(gridData);
		table.setLinesVisible(true);
		table.setHeaderVisible(true);
		
		eViewer.addSelectionChangedListener(event -> {
			int i = 0;
			deleteSubRow.setEnabled(!event.getSelection().isEmpty());
		});
		return eViewer;
	}

	private EObjectTableViewer getEntityTableViewer(Composite parent, FormToolkit toolkit) {
		// toolkit.createLabel(parent, "Entity Table");
		Group entityTableGroup = new Group(parent, SWT.NONE);
		entityTableGroup.setLayout(new GridLayout(1, false));
		
		final Color systemWhite = Display.getDefault().getSystemColor(SWT.COLOR_WHITE);
		entityTableGroup.setBackground(systemWhite);
		// GridData gd = new GridData(GridData.VERTICAL_ALIGN_BEGINNING | GridData.FILL_BOTH);
		entityTableGroup.setLayoutData(new GridData(GridData.VERTICAL_ALIGN_BEGINNING | GridData.FILL_BOTH));
		entityTableGroup.setText("Entity Table");
		
		final DetailToolbarManager buttonManager = new DetailToolbarManager(entityTableGroup, SWT.TOP);
		buttonManager.getToolbarManager().getControl().setBackground(systemWhite);
		
		addEntityRow = new Action("Add") {
			@Override
			public void run() {
				final InventoryADPEntityRow newLine = ADPFactory.eINSTANCE.createInventoryADPEntityRow();
				commandHandler.handleCommand(AddCommand.create(commandHandler.getEditingDomain(), oldValue, ADPPackage.Literals.INVENTORY_PROFILE__ENTITY_TABLE, newLine), oldValue, ADPPackage.Literals.INVENTORY_PROFILE__ENTITY_TABLE);
				if (viewer != null) {
					viewer.setSelection(new StructuredSelection(newLine));
					viewer.refresh();
				}
			}
		};
		addEntityRow.setImageDescriptor(PlatformUI.getWorkbench().getSharedImages().getImageDescriptor(ISharedImages.IMG_OBJ_ADD));
		buttonManager.getToolbarManager().add(addEntityRow);
		
		deleteEntityRow = new Action("Delete") {
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
							final InventoryADPEntityRow line = (InventoryADPEntityRow) o;
							cc.append(RemoveCommand.create(ed, line.eContainer(), line.eContainingFeature(), line));
						}
//						final Iterator<?> itr = iStructuredSelection.iterator();
//						final List<Object> objectsToDelete = Lists.newArrayList(itr);
//						final EditingDomain ed = dialogContext.getScenarioEditingLocation().getEditingDomain();
//						cc.append(DeleteCommand.create(ed, objectsToDelete));
						ed.getCommandStack().execute(cc);
						viewer.refresh();
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
				if (viewer != null && !viewer.getControl().isDisposed()) {
					packViewer(viewer);
				}
			}
		};
		ResourceLocator.imageDescriptorFromBundle("com.mmxlabs.rcp.common", "/icons/pack.gif").ifPresent(packAction::setImageDescriptor);
		buttonManager.getToolbarManager().add(packAction);
		buttonManager.getToolbarManager().update(true);
		
		final IScenarioEditingLocation sel = dialogContext.getScenarioEditingLocation();
		final EObjectTableViewer eViewer = new EObjectTableViewer(entityTableGroup, SWT.FULL_SELECTION | SWT.V_SCROLL | SWT.H_SCROLL);
		eViewer.addTypicalColumn("Entity", new SingleReferenceManipulator(ADPPackage.eINSTANCE.getInventoryADPEntityRow_Entity(), sel.getReferenceValueProviderCache(), sel.getEditingDomain()) {
			@Override
			public void runSetCommand(Object object, Object value) {
				super.runSetCommand(object, value);
				dialogContext.getDialogController().validate();
				eViewer.refresh();
				if (value != null && value instanceof BaseLegalEntity) {
					subTableGroup.setText(((BaseLegalEntity) value).getName() + " market allocations");
				}
			}
			
//			@Override
//			public void doSetValue(final Object object, final Object value) {
//				if (value.equals(-1)) {
//					return;
//				}
//				final EObject newValue = valueList.get((Integer) value);
//				runSetCommand(object, newValue);
//			}
		});
		eViewer.addTypicalColumn("Initial Allocation", new BasicAttributeManipulator(ADPPackage.eINSTANCE.getInventoryADPEntityRow_InitialAllocation(), sel.getEditingDomain()) {
			@Override
			public void runSetCommand(Object object, Object value) {
				super.runSetCommand(object, value);
				InventoryProfile ov = oldValue;
				dialogContext.getDialogController().validate();
				eViewer.refresh();
			}
		});
		eViewer.addTypicalColumn("Reference Entitlement", new NumericAttributeManipulator(ADPPackage.eINSTANCE.getInventoryADPEntityRow_RelativeEntitlement(), sel.getEditingDomain()) {
			@Override
			public void runSetCommand(Object object, Object value) {
				super.runSetCommand(object, value);
				dialogContext.getDialogController().validate();
				eViewer.refresh();
			}
		});

		eViewer.setStatusProvider(statusProvider);
		
		eViewer.init(sel.getAdapterFactory(), sel.getModelReference(), ADPPackage.Literals.INVENTORY_PROFILE__ENTITY_TABLE);
		final GridData gridData = new GridData(SWT.FILL, SWT.FILL, true, true);
		gridData.heightHint = 240;
		final Grid table = eViewer.getGrid();
		table.setLayoutData(gridData);
		table.setLinesVisible(true);
		table.setHeaderVisible(true);

		eViewer.addSelectionChangedListener(event -> {
			deleteEntityRow.setEnabled(!event.getSelection().isEmpty());
			if (!event.getSelection().isEmpty()) {
				Object selection = event.getStructuredSelection().getFirstElement();
				if (selection instanceof InventoryADPEntityRow) {
					InventoryADPEntityRow selectedRow = (InventoryADPEntityRow) selection;
					BaseLegalEntity entity = selectedRow.getEntity();
					if (entity == null) {
						subTableGroup.setText("<?> market allocations");
						contractTableGroup.setText("<?> contract allocations");
					} else {
						subTableGroup.setText(entity.getName()+" market allocations");
						contractTableGroup.setText(entity.getName()+" contract allocations");
					}
					subViewer.setInput(selectedRow);
					contractViewer.setInput(selectedRow);
					packViewer(subViewer);
					packViewer(contractViewer);
					subTableGroup.setVisible(true);
					contractTableGroup.setVisible(true);
				} else {
					subTableGroup.setVisible(false);
					contractTableGroup.setVisible(false);
				}
			} else {
				subTableGroup.setVisible(false);
				contractTableGroup.setVisible(false);
			}
		});
		
//		final Composite buttons = toolkit.createComposite(parent);
//		buttons.setLayoutData(new GridData(SWT.RIGHT, SWT.TOP, true, false));
//		final GridLayout buttonLayout = new GridLayout(2, true);
//		buttons.setLayout(buttonLayout);
//		buttonLayout.marginHeight = 0;
//		buttonLayout.marginWidth = 0;
//		final Button remove = toolkit.createButton(buttons, "Remove", SWT.NONE);
//		remove.setLayoutData(new GridData(SWT.RIGHT, SWT.TOP, false, false));
//		remove.setEnabled(false);
//		
//		eViewer.addSelectionChangedListener(event -> remove.setEnabled(!event.getSelection().isEmpty()));
//		eViewer.addSelectionChangedListener(event -> deleteEntityRow.setEnabled(!event.getSelection().isEmpty()));
//		remove.addSelectionListener(new SelectionAdapter() {
//			@Override
//			public void widgetSelected(final SelectionEvent e) {
//				final ISelection sel = eViewer.getSelection();
//				if (sel.isEmpty())
//					return;
//				if (sel instanceof IStructuredSelection) {
//					final InventoryADPEntityRow line = (InventoryADPEntityRow) ((IStructuredSelection) sel).getFirstElement();
//					commandHandler.handleCommand(RemoveCommand.create(commandHandler.getEditingDomain(), line.eContainer(), line.eContainingFeature(), line), oldValue, ADPPackage.Literals.INVENTORY_PROFILE__ENTITY_TABLE);
//					dialogContext.getDialogController().validate();
//					eViewer.refresh();
//				}
//			}
//		});
//		
//		final Button add = toolkit.createButton(buttons, "Add new", SWT.NONE);
//		add.setLayoutData(new GridData(SWT.RIGHT, SWT.TOP, false, false));
//		add.addSelectionListener(new SelectionAdapter() {
//			@Override
//			public void widgetSelected(final SelectionEvent e) {
//				final InventoryADPEntityRow newLine = ADPFactory.eINSTANCE.createInventoryADPEntityRow();
//				commandHandler.handleCommand(AddCommand.create(commandHandler.getEditingDomain(), oldValue, ADPPackage.Literals.INVENTORY_PROFILE__ENTITY_TABLE, newLine), oldValue, ADPPackage.Literals.INVENTORY_PROFILE__ENTITY_TABLE);
//				eViewer.setSelection(new StructuredSelection(newLine));
//			}
//		});
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
