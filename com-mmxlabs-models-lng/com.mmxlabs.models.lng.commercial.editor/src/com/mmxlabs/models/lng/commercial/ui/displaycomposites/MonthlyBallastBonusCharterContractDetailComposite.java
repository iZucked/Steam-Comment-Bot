/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2020
 * All rights reserved.
 */
package com.mmxlabs.models.lng.commercial.ui.displaycomposites;

import java.util.Collection;
import java.util.Collections;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.emf.databinding.EMFDataBindingContext;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.edit.command.SetCommand;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.ui.forms.widgets.FormToolkit;

import com.mmxlabs.models.lng.cargo.ui.displaycomposites.MonthlyBallastBonusContractTableCreator;
import com.mmxlabs.models.lng.commercial.BallastBonusCharterContract;
import com.mmxlabs.models.lng.commercial.CommercialFactory;
import com.mmxlabs.models.lng.commercial.CommercialPackage;
import com.mmxlabs.models.lng.commercial.MonthlyBallastBonusContract;
import com.mmxlabs.models.lng.commercial.RuleBasedBallastBonusContract;
import com.mmxlabs.models.lng.port.ui.editors.PortMultiReferenceInlineEditor;
import com.mmxlabs.models.mmxcore.MMXRootObject;
import com.mmxlabs.models.ui.editorpart.DefaultStatusProvider;
import com.mmxlabs.models.ui.editors.ICommandHandler;
import com.mmxlabs.models.ui.editors.IDisplayComposite;
import com.mmxlabs.models.ui.editors.dialogs.IDialogEditingContext;
import com.mmxlabs.models.ui.impl.DefaultDetailComposite;
import com.mmxlabs.models.ui.tabular.EObjectTableViewer;

public class MonthlyBallastBonusCharterContractDetailComposite extends DefaultDetailComposite implements IDisplayComposite {

	protected ICommandHandler commandHandler;
	protected IDialogEditingContext dialogContext;
	protected BallastBonusCharterContract oldValue = null;
	protected Composite owner = this;

	protected FormToolkit toolkit;
	protected GridData gridData;
	protected IStatus status;

	protected PortMultiReferenceInlineEditor portEditor;

	
	protected DefaultStatusProvider statusProvider = new DefaultStatusProvider() {
		@Override
		public IStatus getStatus() {
			return status;
		}
	};
	protected Runnable resizeAction;

	public MonthlyBallastBonusCharterContractDetailComposite(final Composite parent, final int style, final FormToolkit toolkit, Runnable resizeAction) {
		super(parent, style, toolkit);
		this.toolkit = toolkit;
		this.resizeAction = resizeAction;

		addDisposeListener(new DisposeListener() {
			@Override
			public void widgetDisposed(final DisposeEvent e) {
				removeAdapter();
			}
		});
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
	}

	@Override
	public Composite getComposite() {
		return this;
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
	public void displayValidationStatus(IStatus status) {
		super.displayValidationStatus(status);
		this.status = status;
		statusProvider.fireStatusChanged(status);
	}

	protected MonthlyBallastBonusContract getBallastBonus(BallastBonusCharterContract bbcc) {
		if (bbcc.getBallastBonusContract() == null) {
			MonthlyBallastBonusContract  ballastBonusContract = CommercialFactory.eINSTANCE.createMonthlyBallastBonusContract();
			commandHandler.handleCommand(SetCommand.create(commandHandler.getEditingDomain(), bbcc, CommercialPackage.Literals.BALLAST_BONUS_CHARTER_CONTRACT__BALLAST_BONUS_CONTRACT, ballastBonusContract), bbcc,
					CommercialPackage.Literals.BALLAST_BONUS_CHARTER_CONTRACT__BALLAST_BONUS_CONTRACT);
			return ballastBonusContract;
		} else {
			return (MonthlyBallastBonusContract ) bbcc.getBallastBonusContract();
		}
	}

	protected void createBallastBonusComposite(Composite parent, FormToolkit toolkit, RuleBasedBallastBonusContract ruleBasedBallastBonusContract) {
		final EObjectTableViewer ballastBonusTable = MonthlyBallastBonusContractTableCreator.createMonthlyBallastBonusTable(parent, toolkit, dialogContext, commandHandler, ruleBasedBallastBonusContract,
				statusProvider, resizeAction);
	}	
	
	@Override
	public void display(IDialogEditingContext dialogContext, MMXRootObject root, EObject value, Collection<EObject> range, EMFDataBindingContext dbc) {
		this.dialogContext = dialogContext;
		oldValue = (BallastBonusCharterContract) value;
		MonthlyBallastBonusContract bbc = getBallastBonus(oldValue);
		Composite hubsComp = toolkit.createComposite(owner);
		hubsComp.setLayout(new FillLayout());
		hubsComp.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));

		Label lbl = new Label(hubsComp, SWT.LEFT);
		lbl.setBackground(owner.getBackground());
		lbl.setText("Hubs: ");
		
		portEditor = new PortMultiReferenceInlineEditor(CommercialPackage.eINSTANCE.getMonthlyBallastBonusContract_Hubs());
		portEditor.setCommandHandler(commandHandler);
		portEditor.createControl(hubsComp, dbc, toolkit);
		portEditor.display(dialogContext, root, bbc, Collections.singleton(bbc));
		createBallastBonusComposite(owner, toolkit, bbc);
	}
}