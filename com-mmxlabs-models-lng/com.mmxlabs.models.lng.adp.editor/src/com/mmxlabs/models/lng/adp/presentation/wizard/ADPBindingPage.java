/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
package com.mmxlabs.models.lng.adp.presentation.wizard;

import java.util.Collections;

import org.eclipse.core.databinding.ObservablesManager;
import org.eclipse.emf.common.command.BasicCommandStack;
import org.eclipse.emf.common.command.Command;
import org.eclipse.emf.common.command.CompoundCommand;
import org.eclipse.emf.common.notify.AdapterFactory;
import org.eclipse.emf.databinding.EMFDataBindingContext;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.edit.command.AddCommand;
import org.eclipse.emf.edit.command.SetCommand;
import org.eclipse.emf.edit.domain.AdapterFactoryEditingDomain;
import org.eclipse.emf.edit.domain.EditingDomain;
import org.eclipse.emf.edit.provider.ComposedAdapterFactory;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IMenuListener;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.ListViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.MenuDetectEvent;
import org.eclipse.swt.events.MenuDetectListener;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.forms.widgets.FormToolkit;

import com.mmxlabs.models.lng.actuals.ActualsPackage;
import com.mmxlabs.models.lng.adp.ADPFactory;
import com.mmxlabs.models.lng.adp.ADPModel;
import com.mmxlabs.models.lng.adp.ADPPackage;
import com.mmxlabs.models.lng.adp.BindingRule;
import com.mmxlabs.models.lng.cargo.CargoPackage;
import com.mmxlabs.models.lng.cargo.CargoType;
import com.mmxlabs.models.lng.scenario.model.LNGScenarioModel;
import com.mmxlabs.models.mmxcore.MMXRootObject;
import com.mmxlabs.models.ui.editorpart.IScenarioEditingLocation;
import com.mmxlabs.models.ui.editors.ICommandHandler;
import com.mmxlabs.models.ui.editors.dialogs.DefaultDialogEditingContext;
import com.mmxlabs.models.ui.editors.dialogs.DetailCompositeDialog;
import com.mmxlabs.models.ui.editors.dialogs.IDialogController;
import com.mmxlabs.models.ui.validation.DefaultExtraValidationContext;
import com.mmxlabs.models.ui.validation.IExtraValidationContext;
import com.mmxlabs.models.ui.validation.IStatusProvider;
import com.mmxlabs.models.ui.valueproviders.IReferenceValueProviderProvider;
import com.mmxlabs.models.ui.valueproviders.ReferenceValueProviderCache;
import com.mmxlabs.scenario.service.model.ScenarioInstance;
import com.mmxlabs.scenario.service.model.ScenarioLock;

public class ADPBindingPage extends WizardPage {

	private final LNGScenarioModel scenarioModel;
	private final ADPModel adpModel;
	IScenarioEditingLocation location;
	private ICommandHandler commandHandler;

	protected EMFDataBindingContext dbc;

	protected ObservablesManager observablesManager;
	private final IDialogController dialogController = new IDialogController() {

		@Override
		public void validate() {

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
		public void setEditorVisibility(final EObject object, final EStructuralFeature feature, final boolean visible) {
			// TODO Auto-generated method stub

		}

		@Override
		public boolean getEditorVisibility(final EObject object, final EStructuralFeature feature) {
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

	protected ADPBindingPage(final ADPModel adpModel, final LNGScenarioModel scenarioModel) {
		super("ADP Model Bindings", "Create profile bindings", null);
		this.adpModel = adpModel;
		this.scenarioModel = scenarioModel;
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
			public void handleCommand(final Command command, final EObject target, final EStructuralFeature feature) {
				// TODO Auto-generated method stub
				command.execute();
			}

		};

		this.location = new IScenarioEditingLocation() {

			@Override
			public void setDisableUpdates(final boolean disable) {
				// TODO Auto-generated method stub

			}

			@Override
			public void setDisableCommandProviders(final boolean disable) {
				// TODO Auto-generated method stub

			}

			@Override
			public void setCurrentViewer(final Viewer viewer) {
				// TODO Auto-generated method stub

			}

			@Override
			public void pushExtraValidationContext(final IExtraValidationContext context) {
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
				return ADPBindingPage.this.getShell();
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
				return new DefaultExtraValidationContext(getRootObject(), false);
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

		dialogContext = new DefaultDialogEditingContext(dialogController, location, false, true);

		final ListViewer viewer = new ListViewer(parent);
		viewer.setContentProvider(new BindingRuleContentProvider());
		viewer.setLabelProvider(new BindingRuleLabelProvider());

		final Button addButton = new Button(parent, SWT.PUSH);

		final MenuManager mgr = new MenuManager();
		viewer.getControl().addMenuDetectListener(new MenuDetectListener() {

			private Menu menu;

			@Override
			public void menuDetected(final MenuDetectEvent e) {

				if (menu == null) {
					menu = mgr.createContextMenu(viewer.getControl());
				}
				mgr.removeAll();
				final IStructuredSelection selection = (IStructuredSelection) viewer.getSelection();

				final IMenuListener listener = new IMenuListener() {

					@Override
					public void menuAboutToShow(final IMenuManager manager) {

						{
							final Action action = new Action("New") {
								@Override
								public void run() {

									BindingRule bindingRule = ADPFactory.eINSTANCE.createBindingRule();
									CompoundCommand cmd = new CompoundCommand();
									cmd.append(AddCommand.create(location.getEditingDomain(), adpModel, ADPPackage.Literals.ADP_MODEL__BINDING_RULES, bindingRule));
									// Disallow re-wiring

									if (cmd.canExecute()) {
										location.getEditingDomain().getCommandStack().execute(cmd);
									}

									final DetailCompositeDialog dcd = new DetailCompositeDialog(viewer.getControl().getShell(), location.getDefaultCommandHandler(), ~SWT.MAX) {
										@Override
										protected void configureShell(final Shell newShell) {
											newShell.setMinimumSize(SWT.DEFAULT, 630);
											super.configureShell(newShell);
										}
									};
									dcd.open(location, location.getRootObject(), Collections.singletonList(bindingRule), location.isLocked());

									viewer.refresh();
								}
							};
							manager.add(action);
						}

						if (!selection.isEmpty()) {
							final Object l = selection.iterator().next();
							if (l instanceof BindingRule) {

								// final TableItem[] items = viewer.getSelection();

								final BindingRule bindingRule = (BindingRule) l;

								final Action action = new Action("Edit") {
									@Override
									public void run() {
										final DetailCompositeDialog dcd = new DetailCompositeDialog(viewer.getControl().getShell(), location.getDefaultCommandHandler(), ~SWT.MAX) {
											@Override
											protected void configureShell(final Shell newShell) {
												newShell.setMinimumSize(SWT.DEFAULT, 630);
												super.configureShell(newShell);
											}
										};
										dcd.open(location, location.getRootObject(), Collections.singletonList(bindingRule), location.isLocked());

										viewer.refresh();
									}
								};
								manager.add(action);
							}
						}
					}
				};
				listener.menuAboutToShow(mgr);
				menu.setVisible(true);
			}
		});

		viewer.setInput(adpModel);

		setControl(viewer.getControl());
	}
}
