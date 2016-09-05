/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
package com.mmxlabs.models.lng.adp.presentation.wizard;

import java.util.Collection;
import java.util.List;

import org.eclipse.core.databinding.ObservablesManager;
import org.eclipse.emf.common.command.BasicCommandStack;
import org.eclipse.emf.common.command.Command;
import org.eclipse.emf.common.notify.AdapterFactory;
import org.eclipse.emf.databinding.EMFDataBindingContext;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.edit.domain.AdapterFactoryEditingDomain;
import org.eclipse.emf.edit.domain.EditingDomain;
import org.eclipse.emf.edit.provider.ComposedAdapterFactory;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CTabFolder;
import org.eclipse.swt.custom.CTabItem;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.forms.widgets.FormToolkit;

import com.mmxlabs.models.lng.adp.ADPModel;
import com.mmxlabs.models.lng.adp.ContractProfile;
import com.mmxlabs.models.lng.scenario.model.LNGScenarioModel;
import com.mmxlabs.models.mmxcore.MMXRootObject;
import com.mmxlabs.models.ui.Activator;
import com.mmxlabs.models.ui.editorpart.IScenarioEditingLocation;
import com.mmxlabs.models.ui.editors.ICommandHandler;
import com.mmxlabs.models.ui.editors.IDisplayComposite;
import com.mmxlabs.models.ui.editors.IDisplayCompositeFactory;
import com.mmxlabs.models.ui.editors.dialogs.DefaultDialogEditingContext;
import com.mmxlabs.models.ui.editors.dialogs.IDialogController;
import com.mmxlabs.models.ui.validation.IExtraValidationContext;
import com.mmxlabs.models.ui.validation.IStatusProvider;
import com.mmxlabs.models.ui.valueproviders.IReferenceValueProviderProvider;
import com.mmxlabs.models.ui.valueproviders.ReferenceValueProviderCache;
import com.mmxlabs.scenario.service.model.ScenarioInstance;
import com.mmxlabs.scenario.service.model.ScenarioLock;

public class ADPContractsPage extends WizardPage {

	private final LNGScenarioModel scenarioModel;
	private final ADPModel adpModel;
	private final boolean isPurchase;
	IScenarioEditingLocation location;
	private ICommandHandler commandHandler;
	// private IDisplayCompositeFactory displayCompositeFactory;

	protected EMFDataBindingContext dbc;

	protected ObservablesManager observablesManager;
	private final IDialogController dialogController = new IDialogController() {

		@Override
		public void validate() {
			// TODO Auto-generated method stub

		}

		@Override
		public void relayout() {
			if (observablesManager != null) {
				observablesManager.dispose();
			}
			if (dbc != null) {
				dbc.dispose();
			}

			dbc = new EMFDataBindingContext();
			observablesManager = new ObservablesManager();

			// This call means we do not need to manually manage our databinding objects lifecycle manually.
			observablesManager.runAndCollect(new Runnable() {

				@Override
				public void run() {
					doCreateContent(parent, toolkit);
				}
			});
			getShell().pack();
		}

		@Override
		public void setEditorVisibility(EObject object, EStructuralFeature feature, boolean visible) {
			// TODO Auto-generated method stub

		}

		@Override
		public boolean getEditorVisibility(EObject object, EStructuralFeature feature) {
			// TODO Auto-generated method stub
			return true;
		}

		@Override
		public void updateEditorVisibility() {
			// TODO Auto-generated method stub

		}

		@Override
		public void rebuild(boolean pack) {
			// TODO Auto-generated method stub
			
		}
	};
	private DefaultDialogEditingContext dialogContext;
	// private IDisplayComposite displayComposite;
	private ReferenceValueProviderCache referenceValueProviderCache;
	private ComposedAdapterFactory adapterFactory;
	private AdapterFactoryEditingDomain adapterFactoryEditingDomain;
	private Composite parent;

	protected ADPContractsPage(final ADPModel adpModel, final LNGScenarioModel scenarioModel, final boolean isPurchase) {
		super("ADP Model Contracts", isPurchase ? "Define purchase contracts profiles" : "Define sales contracts profiles", null);
		this.adpModel = adpModel;
		this.scenarioModel = scenarioModel;
		this.isPurchase = isPurchase;
		adapterFactory = ADPModelUtil.createAdapterFactory();
		adapterFactoryEditingDomain = new AdapterFactoryEditingDomain(adapterFactory, new BasicCommandStack());
		referenceValueProviderCache = new ReferenceValueProviderCache(scenarioModel);
		this.commandHandler = new ICommandHandler() {

			@Override
			public IReferenceValueProviderProvider getReferenceValueProviderProvider() {

				// TODO Auto-generated method stub
				return referenceValueProviderCache;

			}

			@Override
			public EditingDomain getEditingDomain() {
				return adapterFactoryEditingDomain;

			}

			@Override
			public void handleCommand(Command command, EObject target, EStructuralFeature feature) {
				// TODO Auto-generated method stub
				command.execute();
			}

		};

		this.location = new IScenarioEditingLocation() {

			@Override
			public void setDisableUpdates(boolean disable) {
				// TODO Auto-generated method stub

			}

			@Override
			public void setDisableCommandProviders(boolean disable) {
				// TODO Auto-generated method stub

			}

			@Override
			public void setCurrentViewer(Viewer viewer) {
				// TODO Auto-generated method stub

			}

			@Override
			public void pushExtraValidationContext(IExtraValidationContext context) {
				// TODO Auto-generated method stub

			}

			@Override
			public void popExtraValidationContext() {
				// TODO Auto-generated method stub

			}

			@Override
			public boolean isLocked() {
				// TODO Auto-generated method stub
				return false;
			}

			@Override
			public IStatusProvider getStatusProvider() {
				// TODO Auto-generated method stub
				return null;
			}

			@Override
			public Shell getShell() {
				// TODO Auto-generated method stub
				return ADPContractsPage.this.getShell();
			}

			@Override
			public ScenarioInstance getScenarioInstance() {
				// TODO Auto-generated method stub
				throw new UnsupportedOperationException();
			}

			@Override
			public MMXRootObject getRootObject() {
				// TODO Auto-generated method stub
				return scenarioModel;
			}

			@Override
			public IReferenceValueProviderProvider getReferenceValueProviderCache() {
				// TODO Auto-generated method stub
				return referenceValueProviderCache;
			}

			@Override
			public IExtraValidationContext getExtraValidationContext() {
				// TODO Auto-generated method stub
				return null;
			}

			@Override
			public ScenarioLock getEditorLock() {
				// TODO Auto-generated method stub
				return null;
			}

			@Override
			public EditingDomain getEditingDomain() {
				// TODO Auto-generated method stub
				return adapterFactoryEditingDomain;
			}

			@Override
			public ICommandHandler getDefaultCommandHandler() {
				// TODO Auto-generated method stub
				return commandHandler;
			}

			@Override
			public AdapterFactory getAdapterFactory() {
				// TODO Auto-generated method stub
				return adapterFactory;
			}
		};
	}

	@Override
	public boolean isPageComplete() {

		return super.isPageComplete();
	}

	private FormToolkit toolkit;
	private CTabFolder folder;

	@Override
	public void createControl(final Composite parent) {
		this.parent = parent;
		this.dbc = new EMFDataBindingContext();
		this.observablesManager = new ObservablesManager();

		// This call means we do not need to manually manage our databinding objects lifecycle manually.
		observablesManager.runAndCollect(new Runnable() {

			@Override
			public void run() {

				toolkit = new FormToolkit(PlatformUI.getWorkbench().getDisplay());

				doCreateContent(parent, toolkit);
			}

		});

	}

	private void doCreateContent(final Composite parent, final FormToolkit toolkit) {
		if (folder != null) {
			folder.dispose();
			folder = null;
		}
		dialogContext = new DefaultDialogEditingContext(dialogController, location, false, true);

		try {
			folder = new CTabFolder(parent, SWT.BOTTOM | SWT.FLAT);

			List<? extends ContractProfile<?>> profiles = isPurchase ? adpModel.getPurchaseContractProfiles() : adpModel.getSalesContractProfiles();
			for (final ContractProfile<?> profile : profiles) {

				final CTabItem item = new CTabItem(folder, SWT.NONE);
				IDisplayCompositeFactory displayCompositeFactory = Activator.getDefault().getDisplayCompositeFactoryRegistry().getDisplayCompositeFactory(profile.eClass());

				IDisplayComposite displayComposite = displayCompositeFactory.createToplevelComposite(item.getParent(), profile.eClass(), dialogContext, toolkit);

				final Collection<EObject> range = displayCompositeFactory.getExternalEditingRange(scenarioModel, profile);
				range.add(profile);

				// dialogValidationSupport.setValidationTargets(profile);

				displayComposite.setCommandHandler(commandHandler);

				Composite composite = displayComposite.getComposite();

				composite.setLayoutData(new GridData(GridData.FILL_BOTH));

				displayComposite.display(dialogContext, scenarioModel, profile, range, dbc);
				//
				// getShell().layout(true, true);
				// getShell().pack();
				// handle enablement
				// validate();
				//
				// if (lockedForEditing) {
				// final String text2 = getShell().getText();
				// getShell().setText(text2 + " (Editor Locked - reopen to edit)");
				// disableControls(displayComposite.getComposite());
				// }

				// Trigger update of inline editor visibility and UI state update
				dialogController.updateEditorVisibility();

				item.setText(profile.getContract().getName());
				item.setControl(composite);
				item.getParent().setLayout(new FillLayout());
			}
			folder.setSelection(0);
			setControl(folder);
		} catch (final Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
