/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
package com.mmxlabs.models.lng.cargo.ui.displaycomposites;

import java.util.Collection;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.emf.databinding.EMFDataBindingContext;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.jface.layout.GridLayoutFactory;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.ui.forms.widgets.FormToolkit;

import com.mmxlabs.models.lng.cargo.CargoPackage;
import com.mmxlabs.models.lng.cargo.VesselCharter;
import com.mmxlabs.models.lng.commercial.GenericCharterContract;
import com.mmxlabs.models.mmxcore.MMXRootObject;
import com.mmxlabs.models.ui.editorpart.DefaultStatusProvider;
import com.mmxlabs.models.ui.editors.IDisplayComposite;
import com.mmxlabs.models.ui.editors.dialogs.IDialogEditingContext;
import com.mmxlabs.models.ui.impl.DefaultTopLevelComposite;

/**
 * 
 * @author FM
 * 
 */
public class RepositioningFeeTermsDetailComposite extends DefaultTopLevelComposite implements IDisplayComposite {
	private VesselCharter oldVesselCharter = null;
	private Composite owner = this;

	private GridData gridData;
	private IStatus status;
	public IDialogEditingContext getDialogContext() {
		return dialogContext;
	}
	
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
	}
	
	protected void createRepositioningFeeComposite(Composite parent, FormToolkit toolkit, GenericCharterContract originPortRepositioningContract) {
		
		Composite bottomComposite = toolkit.createComposite(parent, SWT.NONE);
		bottomComposite.setLayout(createCustomGridLayout(1, false));
		GridData gridDataCheckbox = new GridData(SWT.FILL, SWT.BEGINNING, true, false);
		gridDataCheckbox.horizontalSpan = 2;
		bottomComposite.setLayoutData(gridDataCheckbox);
		
		final Label label = toolkit.createLabel(bottomComposite, "Repositioning Fee");
		label.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, true, true));
		
		RepositioningFeeTermsTableCreator.createRepositioningFeeTable(bottomComposite, toolkit, dialogContext, commandHandler, originPortRepositioningContract,
				statusProvider, resizeAction);
	}

	@Override
	public void display(IDialogEditingContext dialogContext, MMXRootObject root, EObject value, Collection<EObject> range, EMFDataBindingContext dbc) {
		this.dialogContext = dialogContext;
		oldVesselCharter = (VesselCharter) value;
		final GenericCharterContract gcc = oldVesselCharter.getContainedCharterContract();
		
		if (oldVesselCharter != null) {
			createDefaultChildCompositeSection(dialogContext, root, oldVesselCharter, range, dbc, oldVesselCharter.eClass(), this);
		}
		
		doDisplay(gcc);
	}
	
	protected void doDisplay(final GenericCharterContract gcc) {
		if (!this.dialogContext.isMultiEdit() && gcc != null) {
			createRepositioningFeeComposite(owner, toolkit, gcc);
		}
		resizeAction.run();
	}

	protected void removeAdapter() {
		if (oldVesselCharter != null) {
			oldVesselCharter = null;
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
		return ref.isContainment() && !ref.isMany() 
				&& ref != CargoPackage.eINSTANCE.getVesselCharter_ContainedCharterContract()
				&& ref != CargoPackage.eINSTANCE.getVesselCharter_StartHeel()//
				&& ref != CargoPackage.eINSTANCE.getVesselCharter_EndHeel();
	}
	
	private GridLayout createCustomGridLayout(int numColumns, boolean makeColumnsEqualWidth) {
		return GridLayoutFactory.fillDefaults()
				.numColumns(numColumns)
				.equalWidth(makeColumnsEqualWidth)
				.margins(0, 0)
				.extendedMargins(3, 0, 0, 0)
				.create();
	}

}