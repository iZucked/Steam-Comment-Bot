package com.mmxlabs.models.lng.adp.presentation.composites;


import java.util.Collection;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.emf.common.notify.Adapter;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.databinding.EMFDataBindingContext;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.edit.command.AddCommand;
import org.eclipse.emf.edit.command.RemoveCommand;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.nebula.widgets.grid.Grid;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.forms.widgets.FormToolkit;

import com.mmxlabs.models.lng.adp.ADPFactory;
import com.mmxlabs.models.lng.adp.ADPPackage;
import com.mmxlabs.models.lng.adp.InventoryADPEntityRow;
import com.mmxlabs.models.lng.adp.InventoryProfile;
import com.mmxlabs.models.mmxcore.MMXRootObject;
import com.mmxlabs.models.mmxcore.impl.MMXContentAdapter;
import com.mmxlabs.models.ui.editorpart.DefaultStatusProvider;
import com.mmxlabs.models.ui.editorpart.IScenarioEditingLocation;
import com.mmxlabs.models.ui.editors.ICommandHandler;
import com.mmxlabs.models.ui.editors.IDisplayComposite;
import com.mmxlabs.models.ui.editors.IInlineEditorWrapper;
import com.mmxlabs.models.ui.editors.dialogs.IDialogEditingContext;
import com.mmxlabs.models.ui.impl.DefaultDetailComposite;
import com.mmxlabs.models.ui.tabular.EObjectTableViewer;
import com.mmxlabs.models.ui.tabular.manipulators.BasicAttributeManipulator;
import com.mmxlabs.models.ui.tabular.manipulators.NumericAttributeManipulator;
import com.mmxlabs.models.ui.tabular.manipulators.SingleReferenceManipulator;

public class InventoryProfileDetailComposite extends Composite implements IDisplayComposite {

	private IDisplayComposite delegate;
	private final Composite superParent;
	private InventoryProfile oldValue = null;
	private IDialogEditingContext dialogContext;
	private IStatus status;
	private final EObjectTableViewer viewer;
	private final Composite entityTableComposite;
	private ICommandHandler commandHandler;
	
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
	}
	
	@Override
	public Composite getComposite() {
		return this;
	}

	@Override
	public void display(IDialogEditingContext dialogContext, MMXRootObject root, EObject value, Collection<EObject> range, EMFDataBindingContext dbc) {
//		int i = 0;
		delegate.display(dialogContext, root, value, range, dbc);
		removeAdapter();
		oldValue = (InventoryProfile) value;
		value.eAdapters().add(adapter);
		viewer.setInput(value);
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
	
	private EObjectTableViewer getEntityTableViewer(Composite parent, FormToolkit toolkit) {
		toolkit.createLabel(parent, "Entity Table");
		final IScenarioEditingLocation sel = dialogContext.getScenarioEditingLocation();
		final EObjectTableViewer eViewer = new EObjectTableViewer(parent, SWT.FULL_SELECTION | SWT.V_SCROLL | SWT.H_SCROLL);
		eViewer.addTypicalColumn("Entity", new SingleReferenceManipulator(ADPPackage.eINSTANCE.getInventoryADPEntityRow_Entity(), sel.getReferenceValueProviderCache(), sel.getEditingDomain()) {
			@Override
			public void runSetCommand(Object object, Object value) {
				super.runSetCommand(object, value);
				dialogContext.getDialogController().validate();
				eViewer.refresh();
			}
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
		
		final Composite buttons = toolkit.createComposite(parent);
		buttons.setLayoutData(new GridData(SWT.RIGHT, SWT.TOP, true, false));
		final GridLayout buttonLayout = new GridLayout(2, true);
		buttons.setLayout(buttonLayout);
		buttonLayout.marginHeight = 0;
		buttonLayout.marginWidth = 0;
		final Button remove = toolkit.createButton(buttons, "Remove", SWT.NONE);
		remove.setLayoutData(new GridData(SWT.RIGHT, SWT.TOP, false, false));
		remove.setEnabled(false);
		
		eViewer.addSelectionChangedListener(event -> remove.setEnabled(!event.getSelection().isEmpty()));
		remove.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(final SelectionEvent e) {
				final ISelection sel = eViewer.getSelection();
				if (sel.isEmpty())
					return;
				if (sel instanceof IStructuredSelection) {
					final InventoryADPEntityRow line = (InventoryADPEntityRow) ((IStructuredSelection) sel).getFirstElement();
					commandHandler.handleCommand(RemoveCommand.create(commandHandler.getEditingDomain(), line.eContainer(), line.eContainingFeature(), line), oldValue, ADPPackage.Literals.INVENTORY_PROFILE__ENTITY_TABLE);
					dialogContext.getDialogController().validate();
					eViewer.refresh();
				}
			}
		});
		
		final Button add = toolkit.createButton(buttons, "Add new", SWT.NONE);
		add.setLayoutData(new GridData(SWT.RIGHT, SWT.TOP, false, false));
		add.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(final SelectionEvent e) {
				final InventoryADPEntityRow newLine = ADPFactory.eINSTANCE.createInventoryADPEntityRow();
				commandHandler.handleCommand(AddCommand.create(commandHandler.getEditingDomain(), oldValue, ADPPackage.Literals.INVENTORY_PROFILE__ENTITY_TABLE, newLine), oldValue, ADPPackage.Literals.INVENTORY_PROFILE__ENTITY_TABLE);
				eViewer.setSelection(new StructuredSelection(newLine));
			}
		});
		return eViewer;
	}
}
