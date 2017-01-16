/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
package com.mmxlabs.models.lng.pricing.ui.editorpart;

import java.util.Collection;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.util.EContentAdapter;
import org.eclipse.emf.edit.command.AddCommand;
import org.eclipse.emf.edit.command.RemoveCommand;
import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.window.IShellProvider;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Shell;

import com.mmxlabs.models.lng.pricing.PricingFactory;
import com.mmxlabs.models.lng.pricing.PricingModel;
import com.mmxlabs.models.lng.pricing.PricingPackage;
import com.mmxlabs.models.lng.pricing.UnitConversion;
import com.mmxlabs.models.lng.scenario.model.LNGScenarioModel;
import com.mmxlabs.models.lng.scenario.model.util.ScenarioModelUtil;
import com.mmxlabs.models.ui.editorpart.DefaultStatusProvider;
import com.mmxlabs.models.ui.editorpart.IScenarioEditingLocation;
import com.mmxlabs.models.ui.editors.ICommandHandler;
import com.mmxlabs.models.ui.editors.dialogs.DialogValidationSupport;
import com.mmxlabs.models.ui.tabular.EObjectTableViewer;
import com.mmxlabs.models.ui.tabular.manipulators.BasicAttributeManipulator;
import com.mmxlabs.models.ui.tabular.manipulators.NumericAttributeManipulator;

/**
 * Detail composite for vessel state attributes; adds an additional bit to the bottom of the composite which contains a fuel curve table.
 * 
 * @author hinton
 * 
 */
public class UnitConversionEditor extends Dialog {

	public UnitConversionEditor(final Shell shell, final IScenarioEditingLocation scenarioEditingLocation) {
		super(shell);
		this.scenarioEditingLocation = scenarioEditingLocation;
		setShellStyle(SWT.CLOSE | SWT.BORDER | SWT.TITLE | SWT.RESIZE);
	}

	public UnitConversionEditor(final IShellProvider shellProvider, final IScenarioEditingLocation scenarioEditingLocation) {
		super(shellProvider);
		this.scenarioEditingLocation = scenarioEditingLocation;
		setShellStyle(SWT.CLOSE | SWT.BORDER | SWT.TITLE | SWT.RESIZE);
	}

	private final IScenarioEditingLocation scenarioEditingLocation;
	private DialogValidationSupport dialogValidationSupport;
	private EObjectTableViewer viewer;
	private EContentAdapter changeAdapter = new EContentAdapter() {
		public void notifyChanged(org.eclipse.emf.common.notify.Notification notification) {
			super.notifyChanged(notification);
			if (pricingModel != null) {
				validate(pricingModel.getConversionFactors());
			}
		}
	};
	private PricingModel pricingModel;

	@Override
	protected Control createDialogArea(Composite parent) {
		dialogValidationSupport = new DialogValidationSupport(scenarioEditingLocation.getExtraValidationContext());

		parent = (Composite) super.createDialogArea(parent);
		viewer = new EObjectTableViewer(parent, SWT.BORDER | SWT.V_SCROLL);
		viewer.getGrid().setLayoutData(GridDataFactory.fillDefaults().grab(true, true).create());
		viewer.getGrid().setHeaderVisible(true);

		viewer.addTypicalColumn("Number of ...", new BasicAttributeManipulator(PricingPackage.Literals.UNIT_CONVERSION__FROM, scenarioEditingLocation.getEditingDomain()));
		viewer.addTypicalColumn("in ...", new BasicAttributeManipulator(PricingPackage.Literals.UNIT_CONVERSION__TO, scenarioEditingLocation.getEditingDomain()));
		viewer.addTypicalColumn("is ...", new NumericAttributeManipulator(PricingPackage.Literals.UNIT_CONVERSION__FACTOR, scenarioEditingLocation.getEditingDomain()));

		viewer.init(scenarioEditingLocation.getAdapterFactory(), scenarioEditingLocation.getEditingDomain().getCommandStack(), PricingPackage.Literals.PRICING_MODEL__CONVERSION_FACTORS);

		@NonNull
		final PricingModel pricingModel = ScenarioModelUtil.getPricingModel((LNGScenarioModel) scenarioEditingLocation.getRootObject());
		viewer.setInput(pricingModel);

		final Composite buttons = new Composite(parent, SWT.NONE);

		buttons.setLayoutData(new GridData(SWT.LEFT, SWT.BOTTOM, true, false));
		final GridLayout buttonLayout = new GridLayout(3, true);
		buttons.setLayout(buttonLayout);
		buttonLayout.marginHeight = 0;
		buttonLayout.marginWidth = 0;

		final Button add = new Button(buttons, SWT.NONE);
		add.setText("Add");
		add.setLayoutData(new GridData(SWT.RIGHT, SWT.TOP, false, false));

		add.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(final SelectionEvent e) {

				final ICommandHandler commandHandler = scenarioEditingLocation.getDefaultCommandHandler();

				final UnitConversion line = PricingFactory.eINSTANCE.createUnitConversion();
				commandHandler.handleCommand(AddCommand.create(commandHandler.getEditingDomain(), pricingModel, PricingPackage.Literals.PRICING_MODEL__CONVERSION_FACTORS, line), pricingModel,
						PricingPackage.Literals.PRICING_MODEL__CONVERSION_FACTORS);
				viewer.refresh();
				validate(pricingModel.getConversionFactors());
				viewer.setSelection(new StructuredSelection(line));

			}
		});

		final Button remove = new Button(buttons, SWT.NONE);
		remove.setText("Remove");

		remove.setLayoutData(new GridData(SWT.RIGHT, SWT.TOP, false, false));
		remove.setEnabled(false);
		viewer.addSelectionChangedListener(new ISelectionChangedListener() {

			@Override
			public void selectionChanged(final SelectionChangedEvent event) {
				remove.setEnabled(event.getSelection().isEmpty() == false);
			}
		});

		remove.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(final SelectionEvent e) {
				final ISelection sel = viewer.getSelection();
				if (sel.isEmpty()) {
					return;
				}
				if (sel instanceof IStructuredSelection) {
					final UnitConversion line = (UnitConversion) ((IStructuredSelection) sel).getFirstElement();
					final ICommandHandler commandHandler = scenarioEditingLocation.getDefaultCommandHandler();
					commandHandler.handleCommand(RemoveCommand.create(commandHandler.getEditingDomain(), line.eContainer(), line.eContainingFeature(), line), line,
							PricingPackage.Literals.PRICING_MODEL__CONVERSION_FACTORS);
					validate(pricingModel.getConversionFactors());

					viewer.refresh();
				}
			}
		});

		viewer.setStatusProvider(validationStatusProvider);
		viewer.setDisplayValidationErrors(true);
		validate(pricingModel.getConversionFactors());
		this.pricingModel = pricingModel;
		this.pricingModel.eAdapters().add(changeAdapter);

		viewer.getControl().addDisposeListener(new DisposeListener() {

			@Override
			public void widgetDisposed(DisposeEvent e) {
				if (pricingModel != null) {
					pricingModel.eAdapters().remove(changeAdapter);
				}
			}
		});
		return viewer.getControl();

	}

	@Override
	protected boolean isResizable() {
		return true;
	}

	private DefaultStatusProvider validationStatusProvider = new DefaultStatusProvider() {
		private IStatus status = null;

		@Override
		public IStatus getStatus() {
			return status;
		}

		@Override
		public void fireStatusChanged(IStatus status) {
			this.status = status;
			super.fireStatusChanged(status);
		}
	};

	private void validate(final Collection<? extends EObject> validationTargets) {
		dialogValidationSupport.setValidationTargets(validationTargets);
		final IStatus status = dialogValidationSupport.validate();
		validationStatusProvider.fireStatusChanged(status);
	}
}
