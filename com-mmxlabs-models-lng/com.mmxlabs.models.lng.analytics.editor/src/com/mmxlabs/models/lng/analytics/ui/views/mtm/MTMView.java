/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2019
 * All rights reserved.
 */
package com.mmxlabs.models.lng.analytics.ui.views.mtm;

import java.util.EventObject;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.Consumer;

import org.eclipse.e4.ui.workbench.modeling.ESelectionService;
import org.eclipse.emf.common.command.Command;
import org.eclipse.emf.common.command.CommandStackListener;
import org.eclipse.emf.common.command.CompoundCommand;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.util.EContentAdapter;
import org.eclipse.emf.edit.command.RemoveCommand;
import org.eclipse.emf.edit.command.SetCommand;
import org.eclipse.emf.edit.domain.EditingDomain;
import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.Nullable;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.layout.GridLayoutFactory;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.BusyIndicator;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Label;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.actions.ActionFactory;
import org.eclipse.ui.plugin.AbstractUIPlugin;

import com.mmxlabs.models.common.commandservice.CommandProviderAwareEditingDomain;
import com.mmxlabs.models.lng.analytics.AnalyticsModel;
import com.mmxlabs.models.lng.analytics.AnalyticsPackage;
import com.mmxlabs.models.lng.analytics.MTMModel;
import com.mmxlabs.models.lng.cargo.LoadSlot;
import com.mmxlabs.models.lng.scenario.model.LNGScenarioModel;
import com.mmxlabs.models.lng.scenario.model.util.ScenarioModelUtil;
import com.mmxlabs.models.mmxcore.MMXRootObject;
import com.mmxlabs.models.mmxcore.impl.MMXContentAdapter;
import com.mmxlabs.models.ui.editorpart.ScenarioInstanceView;
import com.mmxlabs.models.ui.editors.ICommandHandler;
import com.mmxlabs.models.ui.valueproviders.IReferenceValueProviderProvider;
import com.mmxlabs.rcp.common.RunnerHelper;
import com.mmxlabs.rcp.common.actions.CopyGridToClipboardAction;
import com.mmxlabs.rcp.common.actions.PackActionFactory;
import com.mmxlabs.rcp.common.actions.RunnableAction;
import com.mmxlabs.scenario.service.model.ScenarioInstance;
import com.mmxlabs.scenario.service.model.manager.IScenarioDataProvider;
import com.mmxlabs.scenario.service.model.manager.ModelReference;
/*
 * author Simon Goodall and FM
 * Based on ViabilityView
 */
public class MTMView extends ScenarioInstanceView implements CommandStackListener {

	private MTMModel currentModel;
	private MMXRootObject rootObject;
	// listens which object is selected
	private org.eclipse.e4.ui.workbench.modeling.ISelectionListener selectionListener;
	// selection service from upper class
	private ESelectionService service;

	// Callbacks for objects that need the current input
	private final List<Consumer<MTMModel>> inputWants = new LinkedList<>();
	private final List<Runnable> disposables = new LinkedList<>();
	private Label errorLabel;

	private Composite parent;

	private ICommandHandler commandHandler;

	private MainTableCompoment mainTableComponent;

	@Override
	public void createPartControl(final Composite parent) {
		this.parent = parent;

		parent.setLayout(GridLayoutFactory.fillDefaults().numColumns(1).create());
		parent.setBackground(PlatformUI.getWorkbench().getDisplay().getSystemColor(SWT.COLOR_WHITE));

		mainTableComponent = new MainTableCompoment();
		mainTableComponent.createControls(this.parent, MTMView.this);
		inputWants.addAll(mainTableComponent.getInputWants());

		final RunnableAction go = new RunnableAction("Generate", Action.AS_PUSH_BUTTON, () -> {
			BusyIndicator.showWhile(Display.getDefault(), () -> {
				try {
					final LNGScenarioModel scenarioModel = (LNGScenarioModel) getRootObject();
					final ScenarioInstance scenarioInstance = getScenarioInstance();
					final IScenarioDataProvider sdp = getScenarioDataProvider();
					//FIXME : code doesn't work properly without the line below. Magic?
					final AnalyticsModel analyticsModelZero = ScenarioModelUtil.getAnalyticsModel(sdp);

					if (scenarioModel == null) {
						return;
					}
					final ExecutorService executor = Executors.newFixedThreadPool(1);
					try {
						executor.submit(() -> {
							final MTMModel model = MTMUtils.createModelFromScenario(scenarioModel, "mtm", true);
							MTMSandboxEvaluator.evaluate(sdp, scenarioInstance, model);

							RunnerHelper.asyncExec(() -> {
								final AnalyticsModel analyticsModel = ScenarioModelUtil.getAnalyticsModel(sdp);
								final EditingDomain editingDomain = sdp.getEditingDomain();

								final CompoundCommand cmd = new CompoundCommand("Create mtm matrix");
								cmd.append(SetCommand.create(editingDomain, analyticsModel, AnalyticsPackage.eINSTANCE.getAnalyticsModel_MtmModel(), model));
								editingDomain.getCommandStack().execute(cmd);

								doDisplayScenarioInstance(scenarioInstance, scenarioModel, model);
							});
						}).get();
					} finally {
						executor.shutdown();
					}
				} catch (final InterruptedException e) {
					e.printStackTrace();
				} catch (final ExecutionException e) {
					e.printStackTrace();
				}
			});
		});
		go.setImageDescriptor(AbstractUIPlugin.imageDescriptorFromPlugin("com.mmxlabs.models.lng.analytics.editor", "icons/sandbox_generate.gif"));
		getViewSite().getActionBars().getToolBarManager().add(go);

		final Action packColumnsAction = PackActionFactory.createPackColumnsAction(mainTableComponent.getViewer());
		getViewSite().getActionBars().getToolBarManager().add(packColumnsAction);

		final Action copyTableAction = new CopyGridToClipboardAction(mainTableComponent.getViewer().getGrid());
		getViewSite().getActionBars().setGlobalActionHandler(ActionFactory.COPY.getId(), copyTableAction);
		getViewSite().getActionBars().getToolBarManager().add(copyTableAction);

		getViewSite().getActionBars().getToolBarManager().update(true);

		service = getSite().getService(ESelectionService.class);

		listenToScenarioSelection();
		
		
	}

	@Override
	protected void doDisplayScenarioInstance(@Nullable final ScenarioInstance scenarioInstance, @Nullable final MMXRootObject rootObject) {
		doDisplayScenarioInstance(scenarioInstance, rootObject, null);
	}

	synchronized void doDisplayScenarioInstance(@Nullable final ScenarioInstance scenarioInstance, @Nullable final MMXRootObject rootObject, @Nullable MTMModel model) {

		if (errorLabel != null) {
			errorLabel.dispose();
			errorLabel = null;
		}

		boolean update = false;

		if (scenarioInstance == null) {
			errorLabel = new Label(this.parent, SWT.NONE);
			errorLabel.setBackground(PlatformUI.getWorkbench().getDisplay().getSystemColor(SWT.COLOR_WHITE));
			errorLabel.setText("No scenario selected");
			model = null;
			update = true;
			this.rootObject = null;
		} else {
			if (model == null && rootObject instanceof LNGScenarioModel) {
				final LNGScenarioModel lngScenarioModel = (LNGScenarioModel) rootObject;
				final @NonNull AnalyticsModel analyticsModel = ScenarioModelUtil.getAnalyticsModel(lngScenarioModel);
				model = analyticsModel.getMtmModel();
			}

			if (model != this.currentModel) {
				update = true;
			}
		}
		if (update) {
			setInput(model);
		}
	}

	/**
	 * If the current model is deleted, then clear the input
	 */
	private final EContentAdapter deletedOptionModelAdapter = new MMXContentAdapter() {

		@Override
		protected void missedNotifications(java.util.List<Notification> missed) {
			boolean doDisplay = false;
			for (Notification notification : missed) {

				if (notification.isTouch()) {
					continue;
				}
				if (notification.getFeature() == AnalyticsPackage.eINSTANCE.getAnalyticsModel_MtmModel()) {
					doDisplay = true;
					break;
				}
			}
			if (doDisplay) {
				displayScenarioInstance(getScenarioInstance());
			}
		};

		public void reallyNotifyChanged(Notification notification) {
			if (notification.isTouch()) {
				return;
			}
			if (notification.getFeature() == AnalyticsPackage.eINSTANCE.getAnalyticsModel_MtmModel()) {
				displayScenarioInstance(getScenarioInstance());
			}
		}
	};

	public void setInput(final @Nullable MTMModel model) {
		if (rootObject != null) {
			rootObject.eAdapters().remove(deletedOptionModelAdapter);
			rootObject = null;
		}

		this.currentModel = model;

		inputWants.forEach(want -> want.accept(model));

		rootObject = getRootObject();
		if (rootObject != null) {
			rootObject.eAdapters().add(deletedOptionModelAdapter);
		}
	}

	@Override
	public void dispose() {

		disposables.forEach(Runnable::run);

		if (currentModel != null) {
			currentModel = null;
		}
		if (rootObject != null) {
			rootObject.eAdapters().remove(deletedOptionModelAdapter);
			rootObject = null;
		}

		if (selectionListener != null) {
			service.removePostSelectionListener(selectionListener);
			selectionListener = null;
		}
		if(mainTableComponent != null) {
			mainTableComponent.dispose();
		}

		super.dispose();
	}

	public MTMModel getModel() {
		return currentModel;
	}

	@Override
	public synchronized ICommandHandler getDefaultCommandHandler() {

		if (commandHandler == null) {
			final ICommandHandler superHandler = super.getDefaultCommandHandler();

			final EditingDomain domain = super.getEditingDomain();

			commandHandler = new ICommandHandler() {

				@Override
				public void handleCommand(final Command command, final EObject target, final EStructuralFeature feature) {

					if (domain instanceof CommandProviderAwareEditingDomain) {
						final CommandProviderAwareEditingDomain commandProviderAwareEditingDomain = (CommandProviderAwareEditingDomain) domain;
						commandProviderAwareEditingDomain.disableAdapters(currentModel);
					}
					superHandler.handleCommand(command, target, feature);
					if (domain instanceof CommandProviderAwareEditingDomain) {
						final CommandProviderAwareEditingDomain commandProviderAwareEditingDomain = (CommandProviderAwareEditingDomain) domain;
						commandProviderAwareEditingDomain.enableAdapters(currentModel, false);
					}

				}

				@Override
				public IReferenceValueProviderProvider getReferenceValueProviderProvider() {
					return superHandler.getReferenceValueProviderProvider();
				}

				@Override
				public EditingDomain getEditingDomain() {
					return superHandler.getEditingDomain();
				}

				@Override
				public ModelReference getModelReference() {
					return superHandler.getModelReference();
				}
			};
		}

		return commandHandler;

	}

	@Override
	public void commandStackChanged(final EventObject event) {
		displayScenarioInstance(getScenarioInstance());
	}

	@Override
	public void setFocus() {
		mainTableComponent.setFocus();
	}
	
	public class ExportMTMToExcellAction extends Action{

		public ExportMTMToExcellAction(final String label) {
			super(label);
		}
		@Override
		public void run() {
			//MTMExporter.export(getTable(), getFile(), dates.getStartMonth(), dates.getEndMonth());
		}
		
		private String getFile() {

	        FileDialog dialog = new FileDialog(getViewSite().getShell(), SWT.SAVE);
	        String [] filterNames = new String [] {"Excel files", "All Files (*)"};
	    	String [] filterExtensions = new String [] {"*.xlsx;*.xls;", "*"};
	        dialog.setFilterNames(filterNames);
	        dialog.setFilterExtensions(filterExtensions);
	        String file = dialog.open();
	        if (file != null) {
	            file = file.trim();
	            if (file.length() > 0) {
					return file;
				}
	        }
	        return "";
	    }
	}

}