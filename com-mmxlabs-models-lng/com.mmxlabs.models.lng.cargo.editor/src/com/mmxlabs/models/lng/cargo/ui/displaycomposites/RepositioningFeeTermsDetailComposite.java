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
import org.eclipse.jface.layout.GridLayoutFactory;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.forms.widgets.FormToolkit;

import com.mmxlabs.models.lng.cargo.CargoPackage;
import com.mmxlabs.models.lng.cargo.VesselAvailability;
import com.mmxlabs.models.lng.commercial.GenericCharterContract;
import com.mmxlabs.models.mmxcore.MMXRootObject;
import com.mmxlabs.models.ui.editorpart.DefaultStatusProvider;
import com.mmxlabs.models.ui.editors.ICommandHandler;
import com.mmxlabs.models.ui.editors.IDisplayComposite;
import com.mmxlabs.models.ui.editors.dialogs.IDialogEditingContext;
import com.mmxlabs.models.ui.impl.DefaultTopLevelComposite;

/**
 * 
 * @author FM
 * 
 */
public class RepositioningFeeTermsDetailComposite extends DefaultTopLevelComposite implements IDisplayComposite {
	private VesselAvailability oldVesselAvailability = null;
	private Composite owner = this;

	protected Composite startHeelComposite;
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
		
		GridLayout layout = GridLayoutFactory.swtDefaults() //
				.numColumns(1) //
				.equalWidth(true) //
				.margins(0, 0) //
				.extendedMargins(0, 0, 0, 0) //
				.create();
		
		startHeelComposite = toolkit.createComposite(this, SWT.NONE);
		startHeelComposite.setLayout(layout);
		startHeelComposite.setLayoutData(new GridData(GridData.FILL_BOTH));
		startHeelComposite.setBackground(Display.getDefault().getSystemColor(SWT.COLOR_WHITE));
	}
	
	protected void createRepositioningFeeComposite(Composite parent, FormToolkit toolkit, GenericCharterContract originPortRepositioningContract) {
		RepositioningFeeTermsTableCreator.createRepositioningFeeTable(parent, toolkit, dialogContext, commandHandler, originPortRepositioningContract,
				statusProvider, resizeAction);
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

		if (gcc != null) {
			createRepositioningFeeComposite(owner, toolkit, gcc);
		}
		
		resizeAction.run();
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