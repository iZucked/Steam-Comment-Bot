/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
package com.mmxlabs.models.lng.cargo.ui.displaycomposites;

import java.util.Collection;
import java.util.Collections;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.emf.databinding.EMFDataBindingContext;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.edit.command.SetCommand;
import org.eclipse.jface.layout.GridLayoutFactory;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.ui.forms.widgets.FormToolkit;

import com.mmxlabs.license.features.KnownFeatures;
import com.mmxlabs.license.features.LicenseFeatures;
import com.mmxlabs.models.lng.cargo.CargoPackage;
import com.mmxlabs.models.lng.cargo.VesselCharter;
import com.mmxlabs.models.lng.commercial.CommercialFactory;
import com.mmxlabs.models.lng.commercial.CommercialPackage;
import com.mmxlabs.models.lng.commercial.GenericCharterContract;
import com.mmxlabs.models.lng.commercial.MonthlyBallastBonusContainer;
import com.mmxlabs.models.lng.commercial.SimpleBallastBonusContainer;
import com.mmxlabs.models.lng.port.ui.editors.PortMultiReferenceInlineEditor;
import com.mmxlabs.models.mmxcore.MMXRootObject;
import com.mmxlabs.models.ui.editorpart.DefaultStatusProvider;
import com.mmxlabs.models.ui.editors.IDisplayComposite;
import com.mmxlabs.models.ui.editors.dialogs.IDialogEditingContext;
import com.mmxlabs.models.ui.impl.DefaultTopLevelComposite;

/**
 * Detail composite for vessel state attributes; adds an additional bit to the bottom of the composite which contains a fuel curve table.
 * 
 * @author hinton, FM
 * 
 */
public class BallastBonusTermsDetailComposite extends DefaultTopLevelComposite implements IDisplayComposite {
	private static final String MONTHLY = "Monthly";
	private static final String NOTIONAL_JOURNEY = "Notional journey";
	private VesselCharter oldVesselCharter = null;
	private Composite owner = this;
	private Composite bottomComposite;

	private Combo ballastBonusCombobox;
	
	private GridData gridData;
	private IStatus status;
	private PortMultiReferenceInlineEditor portEditor;
	
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

	public BallastBonusTermsDetailComposite(final Composite parent, final int style, final IDialogEditingContext dialogContext, final FormToolkit toolkit, //
			Runnable resizeAction, final EObject object) {
		super(parent, style, dialogContext, toolkit);
		this.resizeAction = resizeAction;

		addDisposeListener(e -> removeAdapter());
		toolkit.adapt(this);
		setLayout(createCustomGridLayout(1, true));
		gridData = new GridData(SWT.FILL, SWT.BEGINNING, true, true);

		setLayoutData(gridData);
		setBackground(Display.getDefault().getSystemColor(SWT.COLOR_WHITE));
		
		if (!dialogContext.isMultiEdit() && LicenseFeatures.isPermitted(KnownFeatures.FEATURE_MONTHLY_BALLAST_BONUS)
				&& checkEObject(object)) {
			
			bottomComposite = toolkit.createComposite(this, SWT.NONE);
			bottomComposite.setLayout(createCustomGridLayout(4, false));
			GridData gridDataCheckbox = new GridData(SWT.FILL, SWT.BEGINNING, true, false);
			gridDataCheckbox.horizontalSpan = 2;
			bottomComposite.setLayoutData(gridDataCheckbox);
			
			final Label label = toolkit.createLabel(bottomComposite, "Ballast Bonus");
			label.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, false, false));

			ballastBonusCombobox = new Combo(bottomComposite, SWT.NONE);
			ballastBonusCombobox.setItems(MONTHLY, NOTIONAL_JOURNEY);
			ballastBonusCombobox.setText(determineSelection(object));
			ballastBonusCombobox.addSelectionListener(new SelectionListener() {
				@Override
				public void widgetSelected(SelectionEvent e) {
					if (!ballastBonusCombobox.getText().equalsIgnoreCase(determineSelection(object))) {
						changeBallastBonusType();
					}
				}

				@Override
				public void widgetDefaultSelected(SelectionEvent e) {
					//Do nothing.
				}
			});
		}
	}
	
	private String determineSelection(final EObject object) {
		if (object instanceof VesselCharter va) {
			if (va.getContainedCharterContract() != null) {
				if (va.getContainedCharterContract().getBallastBonusTerms() instanceof MonthlyBallastBonusContainer) {
					return MONTHLY;
				}
			}
		} else if (object instanceof GenericCharterContract gcc) {
			if (gcc.getBallastBonusTerms() instanceof MonthlyBallastBonusContainer ) {
				return MONTHLY;
			}
		}
		return NOTIONAL_JOURNEY;
	}
	
	private boolean checkEObject(final EObject object) {
		if (object instanceof VesselCharter va) {
			if (va.getContainedCharterContract() != null) {
				return true;
			}
		} else if (object instanceof GenericCharterContract) {
			return true;
		}
		return false;
	}

	protected boolean changeBallastBonusType() {
		return changeBallastBonusType(getCharterContract(oldVesselCharter));
	}
	
	protected boolean changeBallastBonusType(GenericCharterContract charterContract) {
		if (charterContract == null) {
			return false;
		}
		if (this.ballastBonusCombobox != null) {
			switch (this.ballastBonusCombobox.getText()) {
			case MONTHLY:
				createMonthlyBallastBonusComposite(owner, toolkit, charterContract);
				break;
			case NOTIONAL_JOURNEY:
			default:
				createBallastBonusComposite(owner, toolkit, charterContract);
				break;
			}			
		} else {
			createBallastBonusComposite(owner, toolkit, charterContract);
		}
		dialogContext.getDialogController().rebuild(true);
		resizeAction.run();
		return true;
	}
	
	private GenericCharterContract getCharterContract(VesselCharter va) {
		GenericCharterContract charterContract = null;
		if (va.getContainedCharterContract() == null) {
			charterContract = CommercialFactory.eINSTANCE.createGenericCharterContract();
			charterContract.setName(validateOrOfferName("ballast_bonus_terms", va));
			commandHandler.handleCommand(SetCommand.create(commandHandler.getEditingDomain(), va, //
					CargoPackage.Literals.VESSEL_CHARTER__CONTAINED_CHARTER_CONTRACT, charterContract), va,//
					CargoPackage.Literals.VESSEL_CHARTER__CONTAINED_CHARTER_CONTRACT);
			commandHandler.handleCommand(SetCommand.create(commandHandler.getEditingDomain(), va, //
					CargoPackage.Literals.VESSEL_CHARTER__CHARTER_CONTRACT_OVERRIDE, Boolean.TRUE), va,//
					CargoPackage.Literals.VESSEL_CHARTER__CHARTER_CONTRACT_OVERRIDE);
		} else {
			charterContract = va.getContainedCharterContract();
		}
		return charterContract;
	}

	private String validateOrOfferName(final String type, final VesselCharter va) {
		if (va.getVessel() == null) {
			return String.format("%s_%s", type, va.getUuid());
		}
		return String.format("%s_%s_%d", type, va.getVessel().getName(), va.getCharterNumber());
	}

	protected void createMonthlyBallastBonusComposite(Composite parent, FormToolkit toolkit, GenericCharterContract charterContract) {
		if (charterContract != null) {
			if (!(charterContract.getBallastBonusTerms() instanceof MonthlyBallastBonusContainer)) {
				final MonthlyBallastBonusContainer emptyContainer = CommercialFactory.eINSTANCE.createMonthlyBallastBonusContainer();
				commandHandler.handleCommand(SetCommand.create(commandHandler.getEditingDomain(), charterContract, //
						CommercialPackage.Literals.GENERIC_CHARTER_CONTRACT__BALLAST_BONUS_TERMS, emptyContainer), charterContract,//
						CommercialPackage.Literals.GENERIC_CHARTER_CONTRACT__BALLAST_BONUS_TERMS);
			}
		}
		MonthlyBallastBonusTermsTableCreator.createMonthlyBallastBonusTable(parent, toolkit, dialogContext, commandHandler, charterContract,
				statusProvider, resizeAction);
	}
	
	protected void createBallastBonusComposite(Composite parent, FormToolkit toolkit, GenericCharterContract charterContract) {
		if (charterContract != null) {
			if (!(charterContract.getBallastBonusTerms() instanceof SimpleBallastBonusContainer)) {
				final SimpleBallastBonusContainer emptyContainer = CommercialFactory.eINSTANCE.createSimpleBallastBonusContainer();
				commandHandler.handleCommand(SetCommand.create(commandHandler.getEditingDomain(), charterContract, //
						CommercialPackage.Literals.GENERIC_CHARTER_CONTRACT__BALLAST_BONUS_TERMS, emptyContainer), charterContract,//
						CommercialPackage.Literals.GENERIC_CHARTER_CONTRACT__BALLAST_BONUS_TERMS);
			}
		}
		BallastBonusTermsTableCreator.createBallastBonusTable(parent, toolkit, dialogContext, commandHandler, charterContract,
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
		doDisplay(dialogContext, root, dbc, gcc);
	}

	protected void doDisplay(IDialogEditingContext dialogContext, MMXRootObject root, EMFDataBindingContext dbc, final GenericCharterContract gcc) {
		if (!dialogContext.isMultiEdit() && gcc != null) {
			if (gcc.getBallastBonusTerms() instanceof MonthlyBallastBonusContainer mbbc) {
				
				Composite hubsComp = toolkit.createComposite(bottomComposite);
				hubsComp.setLayout(new FillLayout());
				hubsComp.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
				
				Label lbl = new Label(hubsComp, SWT.RIGHT);
				lbl.setBackground(owner.getBackground());
				lbl.setText("\u2000\u2000\u2000\u2000Hubs: ");

				portEditor = new PortMultiReferenceInlineEditor(CommercialPackage.eINSTANCE.getMonthlyBallastBonusContainer_Hubs());
				portEditor.setCommandHandler(commandHandler);
				portEditor.createControl(hubsComp, dbc, toolkit);
				portEditor.display(dialogContext, root, mbbc, Collections.singleton(mbbc));

				createMonthlyBallastBonusComposite(owner, toolkit, gcc);
			} else {
				createBallastBonusComposite(owner, toolkit, gcc);
			}
		}
	}

	void removeAdapter() {
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
				&& ref != CargoPackage.eINSTANCE.getVesselCharter_ContainedCharterContract()//
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