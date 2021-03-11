/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2021
 * All rights reserved.
 */
package com.mmxlabs.models.lng.cargo.ui.displaycomposites;

import java.util.Collection;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.emf.databinding.EMFDataBindingContext;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.edit.command.SetCommand;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.layout.GridLayoutFactory;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.forms.widgets.FormToolkit;

import com.mmxlabs.models.lng.cargo.CargoPackage;
import com.mmxlabs.models.lng.cargo.VesselAvailability;
import com.mmxlabs.models.lng.commercial.CommercialFactory;
import com.mmxlabs.models.lng.commercial.GenericCharterContract;
import com.mmxlabs.models.mmxcore.MMXRootObject;
import com.mmxlabs.models.ui.editorpart.DefaultStatusProvider;
import com.mmxlabs.models.ui.editors.ICommandHandler;
import com.mmxlabs.models.ui.editors.IDisplayComposite;
import com.mmxlabs.models.ui.editors.dialogs.IDialogEditingContext;
import com.mmxlabs.models.ui.impl.DefaultTopLevelComposite;
import com.mmxlabs.models.ui.tabular.EObjectTableViewer;

/**
 * 
 * @author FM based on Hinton
 * 
 */
public class RepositioningFeeTermsDetailComposite extends DefaultTopLevelComposite implements IDisplayComposite {
	private ICommandHandler commandHandler;
	private VesselAvailability oldVesselAvailability = null;
	private Composite owner = this;

	protected Button repositioningFeeCheckbox;
	protected Composite startHeelComposite;
	private GridData gridData;
	private IStatus status;
	
	private DefaultStatusProvider statusProvider = new DefaultStatusProvider() {
		@Override
		public IStatus getStatus() {
			return status;
		}
	};
	private Runnable resizeAction;

	public RepositioningFeeTermsDetailComposite(final Composite parent, final int style, final IDialogEditingContext dialogContext, final FormToolkit toolkit, Runnable resizeAction) {
		super(parent, style, dialogContext, toolkit);
		this.resizeAction = resizeAction;

		addDisposeListener(e -> removeAdapter());
		toolkit.adapt(this);
		setLayout(new GridLayout(1, true));
		gridData = new GridData(SWT.FILL, SWT.BEGINNING, true, true);

		setLayoutData(gridData);
		setBackground(Display.getDefault().getSystemColor(SWT.COLOR_WHITE));
		
		startHeelComposite = toolkit.createComposite(this, SWT.NONE);
		final GridLayout layout = GridLayoutFactory.swtDefaults() //
				.numColumns(1) //
				.equalWidth(true) //
				.margins(0, 0) //
				.extendedMargins(0, 0, 0, 0) //
				.create();
		startHeelComposite.setLayout(layout);
		startHeelComposite.setLayoutData(new GridData(GridData.FILL_BOTH));
		startHeelComposite.setBackground(Display.getDefault().getSystemColor(SWT.COLOR_WHITE));

		Composite repoCheckbox = toolkit.createComposite(this, SWT.NONE);
		GridLayout gridLayoutCheckbox = new GridLayout(3, false);
		repoCheckbox.setLayout(gridLayoutCheckbox);
		GridData gridDataCheckbox = new GridData(SWT.FILL, SWT.BEGINNING, true, false);
		gridDataCheckbox.horizontalSpan = 2;
		repoCheckbox.setLayoutData(gridDataCheckbox);
		toolkit.createLabel(repoCheckbox, "Set repositioning fee");
		
		repositioningFeeCheckbox = toolkit.createButton(repoCheckbox, null, SWT.CHECK | SWT.LEFT);
		repositioningFeeCheckbox.setSelection(false);
		repositioningFeeCheckbox.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				if (repositioningFeeCheckbox.getSelection()) {
					if (!createComposite()) {
						repositioningFeeCheckbox.setSelection(false);
					}
				} else {
					dialogContext.getDialogController().rebuild(true);
					resizeAction.run();
				}
			}
		});
	}
	
	protected boolean createComposite() {
		return createComposite(getCharterContract(oldVesselAvailability));
	}
	
	protected boolean createComposite(final GenericCharterContract charterContract) {
		if (charterContract == null) {
			return false;
		}
		createRepositioningFeeComposite(owner, toolkit, charterContract);
		dialogContext.getDialogController().rebuild(true);
		resizeAction.run();
		return true;
	}
	
	private GenericCharterContract getCharterContract(VesselAvailability va) {
		GenericCharterContract charterContract = null;
		if (va.getContainedCharterContract() == null) {
			charterContract = CommercialFactory.eINSTANCE.createGenericCharterContract();
			String name = validateOrOfferName("repositioning_fee_terms", va);
			if (name == null) {
				return null;
			}
			charterContract.setName(name);
			commandHandler.handleCommand(SetCommand.create(commandHandler.getEditingDomain(), va, CargoPackage.Literals.VESSEL_AVAILABILITY__CONTAINED_CHARTER_CONTRACT, charterContract), va,
					CargoPackage.Literals.VESSEL_AVAILABILITY__CONTAINED_CHARTER_CONTRACT);
			commandHandler.handleCommand(SetCommand.create(commandHandler.getEditingDomain(), va, CargoPackage.Literals.VESSEL_AVAILABILITY__CHARTER_CONTRACT_OVERRIDE, Boolean.TRUE), va,
					CargoPackage.Literals.VESSEL_AVAILABILITY__CHARTER_CONTRACT_OVERRIDE);
		} else {
			charterContract = va.getContainedCharterContract();
		}
		return charterContract;
	}
	
	private String validateOrOfferName(final String type, final VesselAvailability va) {
		if (va.getVessel() == null) {
			MessageDialog.openInformation(getShell(), "ERROR", "Vessel must be set before creating the ballast bonus contract!");
			return null;
		}
		return String.format("%s_%s_%d", type, va.getVessel().getName(), va.getCharterNumber());
	}
	
	protected void createRepositioningFeeComposite(Composite parent, FormToolkit toolkit, GenericCharterContract originPortRepositioningContract) {
		/* EObjectTableViewer repositioningFeeTable =*/ RepositioningFeeTermsTableCreator.createRepositioningFeeTable(parent, toolkit, dialogContext, commandHandler, originPortRepositioningContract,
				statusProvider, resizeAction);
	}
	
	@Override
	public Composite getComposite() {
		return this;
	}

	@Override
	public void display(IDialogEditingContext dialogContext, MMXRootObject root, EObject value, Collection<EObject> range, EMFDataBindingContext dbc) {
		this.dialogContext = dialogContext;
		oldVesselAvailability = (VesselAvailability) value;
		final GenericCharterContract gcc = oldVesselAvailability.getContainedCharterContract();
		
		if (oldVesselAvailability != null) {
			createDefaultChildCompositeSection(dialogContext, root, oldVesselAvailability, range, dbc, oldVesselAvailability.eClass(), startHeelComposite);
		}
		
		doDisplay(gcc);
	}

	protected void doDisplay(final GenericCharterContract gcc) {
		if (repositioningFeeCheckbox != null && gcc != null) {
			repositioningFeeCheckbox.setSelection(true);
			createRepositioningFeeComposite(owner, toolkit, gcc);
		}
	}

	@Override
	public void setCommandHandler(final ICommandHandler commandHandler) {
		this.commandHandler = commandHandler;
	}

	protected void removeAdapter() {
		if (oldVesselAvailability != null) {
			oldVesselAvailability = null;
		}
	}

	@Override
	public void displayValidationStatus(IStatus status) {
		super.displayValidationStatus(status);
		this.status = status;
		statusProvider.fireStatusChanged(status);
	}
	
	@Override
	protected boolean shouldDisplay(final EReference ref) {
		return ref.isContainment() && !ref.isMany() && ref != CargoPackage.eINSTANCE.getVesselAvailability_ContainedCharterContract()
				&& ref != CargoPackage.eINSTANCE.getVesselAvailability_EndHeel();
	}

}