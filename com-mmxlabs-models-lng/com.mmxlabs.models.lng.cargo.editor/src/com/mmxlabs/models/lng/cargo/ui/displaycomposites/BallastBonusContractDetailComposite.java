/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2020
 * All rights reserved.
 */
package com.mmxlabs.models.lng.cargo.ui.displaycomposites;

import java.util.Collection;
import java.util.Collections;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.emf.databinding.EMFDataBindingContext;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.edit.command.SetCommand;
import org.eclipse.jdt.annotation.Nullable;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.ui.forms.widgets.FormToolkit;

import com.mmxlabs.license.features.LicenseFeatures;
import com.mmxlabs.models.lng.cargo.CargoPackage;
import com.mmxlabs.models.lng.cargo.VesselAvailability;
import com.mmxlabs.models.lng.commercial.CommercialFactory;
import com.mmxlabs.models.lng.commercial.CommercialPackage;
import com.mmxlabs.models.lng.commercial.MonthlyBallastBonusContract;
import com.mmxlabs.models.lng.commercial.RuleBasedBallastBonusContract;
import com.mmxlabs.models.lng.port.ui.editors.PortMultiReferenceInlineEditor;
import com.mmxlabs.models.mmxcore.MMXRootObject;
import com.mmxlabs.models.ui.editorpart.DefaultStatusProvider;
import com.mmxlabs.models.ui.editors.ICommandHandler;
import com.mmxlabs.models.ui.editors.IDisplayComposite;
import com.mmxlabs.models.ui.editors.IInlineEditor;
import com.mmxlabs.models.ui.editors.dialogs.IDialogEditingContext;
import com.mmxlabs.models.ui.impl.DefaultDetailComposite;
import com.mmxlabs.models.ui.tabular.EObjectTableViewer;

/**
 * Detail composite for vessel state attributes; adds an additional bit to the bottom of the composite which contains a fuel curve table.
 * 
 * @author hinton
 * 
 */
public class BallastBonusContractDetailComposite extends DefaultDetailComposite implements IDisplayComposite {
	private static final String MONTHLY = "Monthly";
	private static final String NOTIONAL_JOURNEY = "Notional journey";
	private ICommandHandler commandHandler;
	private IDialogEditingContext dialogContext;
	private VesselAvailability oldValue = null;
	private Composite owner = this;

	private Button ballastBonusCheckbox;
	private Combo ballastBonusCombobox;
	private FormToolkit toolkit;
	private GridData gridData;
	private IStatus status;
	
	private EObjectTableViewer ballastBonusTable;
	
	private DefaultStatusProvider statusProvider = new DefaultStatusProvider() {
		@Override
		public IStatus getStatus() {
			return status;
		}
	};
	private Runnable resizeAction;

	public BallastBonusContractDetailComposite(final Composite parent, final int style, final FormToolkit toolkit, Runnable resizeAction,
			VesselAvailability va) {
		super(parent, style, toolkit);
		this.toolkit = toolkit;
		this.resizeAction = resizeAction;

		addDisposeListener(e -> removeAdapter());
		toolkit.adapt(this);
		setLayout(new GridLayout(1, true));
		gridData = new GridData(SWT.FILL, SWT.BEGINNING, true, true);

		setLayoutData(gridData);
		setBackground(Display.getDefault().getSystemColor(SWT.COLOR_WHITE));

		Composite ballastCheckbox = toolkit.createComposite(this, SWT.NONE);
		GridLayout gridLayoutCheckbox = new GridLayout(3, false);
		ballastCheckbox.setLayout(gridLayoutCheckbox);
		GridData gridDataCheckbox = new GridData(SWT.FILL, SWT.BEGINNING, true, false);
		gridDataCheckbox.horizontalSpan = 2;
		ballastCheckbox.setLayoutData(gridDataCheckbox);
		toolkit.createLabel(ballastCheckbox, "Set ballast bonus");
		
		ballastBonusCheckbox = toolkit.createButton(ballastCheckbox, null, SWT.CHECK | SWT.LEFT);
		ballastBonusCheckbox.setSelection(false);
		ballastBonusCheckbox.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				boolean selection = ballastBonusCheckbox.getSelection();
				if (selection == true) {
					changeBallastBonusType();
				} else {
					oldValue.setBallastBonusContract(null);
					dialogContext.getDialogController().rebuild(true);
					resizeAction.run();
				}
			}
		});
		
		ballastBonusCombobox = new Combo(ballastCheckbox, SWT.NONE);
		ballastBonusCombobox.setItems(MONTHLY, NOTIONAL_JOURNEY);
		ballastBonusCombobox.setText("Change Type...");
		ballastBonusCombobox.addSelectionListener(new SelectionListener() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				changeBallastBonusType();
			}

			@Override
			public void widgetDefaultSelected(SelectionEvent e) {
				//Do nothing.
			}
		});
		
		if (!LicenseFeatures.isPermitted("features:monthly-ballast-bonus")) {
			ballastBonusCombobox.setVisible(false);
			ballastBonusCombobox.setEnabled(false);
		}
	}

	private void changeBallastBonusType() {
		oldValue.setBallastBonusContract(null);
		switch (this.ballastBonusCombobox.getText()) {
		case MONTHLY:
			MonthlyBallastBonusContract monthlyBallastBonusContract = addMonthlyBallastBonus(oldValue);
			createMonthlyBallastBonusComposite(owner, toolkit, monthlyBallastBonusContract);
			dialogContext.getDialogController().rebuild(true);
			resizeAction.run();
			break;
		case NOTIONAL_JOURNEY:
		default:
			RuleBasedBallastBonusContract ruleBasedBallastBonusContract = addBallastBonus(oldValue);
			createBallastBonusComposite(owner, toolkit, ruleBasedBallastBonusContract);
			dialogContext.getDialogController().rebuild(true);
			resizeAction.run();
			break;
		}
	}
	
	protected MonthlyBallastBonusContract addMonthlyBallastBonus(VesselAvailability va) {
		MonthlyBallastBonusContract ballastBonusContract = CommercialFactory.eINSTANCE.createMonthlyBallastBonusContract();
		commandHandler.handleCommand(SetCommand.create(commandHandler.getEditingDomain(), va, CargoPackage.Literals.VESSEL_AVAILABILITY__BALLAST_BONUS_CONTRACT, ballastBonusContract), va,
				CargoPackage.Literals.VESSEL_AVAILABILITY__BALLAST_BONUS_CONTRACT);
		return ballastBonusContract;
	}
	
	protected RuleBasedBallastBonusContract addBallastBonus(VesselAvailability va) {
		RuleBasedBallastBonusContract ballastBonusContract = CommercialFactory.eINSTANCE.createRuleBasedBallastBonusContract();
		commandHandler.handleCommand(SetCommand.create(commandHandler.getEditingDomain(), va, CargoPackage.Literals.VESSEL_AVAILABILITY__BALLAST_BONUS_CONTRACT, ballastBonusContract), va,
				CargoPackage.Literals.VESSEL_AVAILABILITY__BALLAST_BONUS_CONTRACT);
		return ballastBonusContract;
	}

	private void createMonthlyBallastBonusComposite(Composite parent, FormToolkit toolkit, RuleBasedBallastBonusContract ruleBasedBallastBonusContract) {
		ballastBonusTable = MonthlyBallastBonusContractTableCreator.createMonthlyBallastBonusTable(parent, toolkit, dialogContext, commandHandler, ruleBasedBallastBonusContract,
				statusProvider, resizeAction);
	}
	
	private void createBallastBonusComposite(Composite parent, FormToolkit toolkit, RuleBasedBallastBonusContract ruleBasedBallastBonusContract) {
		ballastBonusTable = BallastBonusContractTableCreator.createBallastBonusTable(parent, toolkit, dialogContext, commandHandler, ruleBasedBallastBonusContract,
				statusProvider, resizeAction);
	}
	
	@Override
	public Composite getComposite() {
		return this;
	}

	@Override
	public void display(IDialogEditingContext dialogContext, MMXRootObject root, EObject value, Collection<EObject> range, EMFDataBindingContext dbc) {
		this.dialogContext = dialogContext;
		oldValue = (VesselAvailability) value;

		if (ballastBonusCheckbox != null && oldValue.getBallastBonusContract() != null) {
			ballastBonusCheckbox.setSelection(true);
		
			if (oldValue.getBallastBonusContract() instanceof MonthlyBallastBonusContract) {
				
				MonthlyBallastBonusContract bbc = (MonthlyBallastBonusContract)oldValue.getBallastBonusContract();
				Composite hubsComp = toolkit.createComposite(owner);
				hubsComp.setLayout(new FillLayout());
				hubsComp.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));

				Label lbl = new Label(hubsComp, SWT.LEFT);
				lbl.setBackground(owner.getBackground());
				lbl.setText("Hubs: ");
				
				PortMultiReferenceInlineEditor portEditor = new PortMultiReferenceInlineEditor(CommercialPackage.eINSTANCE.getMonthlyBallastBonusContract_Hubs());
				portEditor.setCommandHandler(commandHandler);
				portEditor.createControl(hubsComp, dbc, toolkit);
				portEditor.display(dialogContext, root, bbc, Collections.singleton(bbc));
				
				createMonthlyBallastBonusComposite(owner, toolkit, (RuleBasedBallastBonusContract) oldValue.getBallastBonusContract());
			}
			else {
				createBallastBonusComposite(owner, toolkit, (RuleBasedBallastBonusContract) oldValue.getBallastBonusContract());
			}
		}
	}

	@Override
	public void setCommandHandler(final ICommandHandler commandHandler) {
		this.commandHandler = commandHandler;
	}

	void removeAdapter() {
		if (oldValue != null) {
			oldValue = null;
		}
	}

	@Override
	public @Nullable IInlineEditor addInlineEditor(IInlineEditor editor) {
		return null;
	}

	@Override
	public void displayValidationStatus(IStatus status) {
		super.displayValidationStatus(status);
		this.status = status;
		statusProvider.fireStatusChanged(status);
	}

}