/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2022
 * All rights reserved.
 */
package com.mmxlabs.models.lng.analytics.ui.views.mtm;

import java.lang.reflect.InvocationTargetException;
import java.util.EventObject;
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
import org.eclipse.emf.edit.command.SetCommand;
import org.eclipse.emf.edit.domain.EditingDomain;
import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.Nullable;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.dialogs.ProgressMonitorDialog;
import org.eclipse.jface.layout.GridLayoutFactory;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.actions.ActionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mmxlabs.models.common.commandservice.CommandProviderAwareEditingDomain;
import com.mmxlabs.models.lng.analytics.AnalyticsModel;
import com.mmxlabs.models.lng.analytics.AnalyticsPackage;
import com.mmxlabs.models.lng.analytics.MTMModel;
import com.mmxlabs.models.lng.scenario.model.LNGScenarioModel;
import com.mmxlabs.models.lng.scenario.model.util.ScenarioModelUtil;
import com.mmxlabs.models.mmxcore.MMXRootObject;
import com.mmxlabs.models.mmxcore.impl.SafeMMXContentAdapter;
import com.mmxlabs.models.ui.editorpart.ScenarioInstanceView;
import com.mmxlabs.models.ui.editors.ICommandHandler;
import com.mmxlabs.models.ui.valueproviders.IReferenceValueProviderProvider;
import com.mmxlabs.rcp.common.CommonImages;
import com.mmxlabs.rcp.common.CommonImages.IconPaths;
import com.mmxlabs.rcp.common.RunnerHelper;
import com.mmxlabs.rcp.common.actions.CopyGridToHtmlClipboardAction;
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
	private DatesToolbarEditor dates;

	// Callbacks for objects that need the current input
	private final List<Consumer<MTMModel>> inputWants = new LinkedList<>();
	private final List<Runnable> disposables = new LinkedList<>();
	private Label errorLabel;

	private Composite parent;

	private ICommandHandler commandHandler;

	private MainTableCompoment mainTableComponent;
	
	private static final Logger LOG = LoggerFactory.getLogger(MTMView.class);

	@Override
	public void createPartControl(final Composite parent) {
		this.parent = parent;

		parent.setLayout(GridLayoutFactory.fillDefaults().numColumns(1).create());
		parent.setBackground(PlatformUI.getWorkbench().getDisplay().getSystemColor(SWT.COLOR_WHITE));

		mainTableComponent = new MainTableCompoment();
		mainTableComponent.createControls(this.parent, MTMView.this);
		inputWants.addAll(mainTableComponent.getInputWants());

		dates = new DatesToolbarEditor("netbacks_dates_toolbar", e -> refresh());
		getViewSite().getActionBars().getToolBarManager().add(dates);
		
		final RunnableAction go = new RunnableAction("Generating the MTM report", Action.AS_PUSH_BUTTON, () -> {
			
			if (modelRecord != null) {
				try (final IScenarioDataProvider sdp = modelRecord.aquireScenarioDataProvider("MtMScenarioEditorActionDelegate::Create")) {
					final ProgressMonitorDialog dialog = new ProgressMonitorDialog(Display.getDefault().getActiveShell());

					sdp.getModelReference().executeWithTryLock(true, 2_000, () -> {
						try {
							dialog.run(true, false, m -> {
								try {
									LNGScenarioModel foo = (LNGScenarioModel) getRootObject();
									if (foo == null) {
										foo = ScenarioModelUtil.findScenarioModel(sdp);
									}
									final LNGScenarioModel scenarioModel = foo;
									final ScenarioInstance scenarioInstance = getScenarioInstance();
									if (scenarioModel == null) {
										return;
									}
									final ExecutorService executor = Executors.newFixedThreadPool(1);
									try {
										executor.submit(() -> {
											final MTMModel model = MTMUtils.evaluateMTMModel(scenarioModel, scenarioInstance, sdp, true, "mtm", false,//
													dates.getStartDate(), dates.getEndDate(), m);

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
								} catch (final InterruptedException | ExecutionException e) {
									e.printStackTrace();
								}
							});
						} catch (final InvocationTargetException | InterruptedException e) {
							LOG.error(e.getMessage(), e);
						}

					});
				} catch (final Exception e) {
					throw new RuntimeException("Unable to mark scenario to market", e);
				}
			}
		});
		CommonImages.setImageDescriptors(go, IconPaths.Play);
		getViewSite().getActionBars().getToolBarManager().add(go);

		final RunnableAction remove = new RunnableAction("Remove", Action.AS_PUSH_BUTTON, () -> {
			if (modelRecord != null) {
				try (final IScenarioDataProvider sdp = modelRecord.aquireScenarioDataProvider("MtMScenarioEditorActionDelegate::Delete")) {

					sdp.getModelReference().executeWithTryLock(true, 2_000, () -> {
						final LNGScenarioModel scenarioModel = (LNGScenarioModel) getRootObject();
						final EditingDomain editingDomain = sdp.getEditingDomain();
						final AnalyticsModel analyticsModel = ScenarioModelUtil.getAnalyticsModel(sdp);
						final MTMModel model = analyticsModel.getMtmModel();

						if (model != null) {
							final CompoundCommand cmd = new CompoundCommand("Remove mtm matrix");
							cmd.append(SetCommand.create(editingDomain, analyticsModel, AnalyticsPackage.eINSTANCE.getAnalyticsModel_MtmModel(), SetCommand.UNSET_VALUE));
							editingDomain.getCommandStack().execute(cmd);

							doDisplayScenarioInstance(scenarioInstance, scenarioModel, null);
						}
					});
				}
			}
		});
		CommonImages.setImageDescriptors(remove, IconPaths.Delete);
		getViewSite().getActionBars().getToolBarManager().add(remove);

		final Action packColumnsAction = PackActionFactory.createPackColumnsAction(mainTableComponent.getViewer());
		getViewSite().getActionBars().getToolBarManager().add(packColumnsAction);

		final CopyGridToHtmlClipboardAction copyTableExtendedAction = new CopyGridToHtmlClipboardAction(mainTableComponent.getViewer().getGrid(), true);

		copyTableExtendedAction.setShowForegroundColours(true);
		
		getViewSite().getActionBars().setGlobalActionHandler(ActionFactory.COPY.getId(), copyTableExtendedAction);
		getViewSite().getActionBars().getToolBarManager().add(copyTableExtendedAction);

		getViewSite().getActionBars().getToolBarManager().update(true);

		service = getSite().getService(ESelectionService.class);

		listenToScenarioSelection();

	}
	
	private void refresh() {
	}

	@Override
	protected synchronized void doDisplayScenarioInstance(@Nullable final ScenarioInstance scenarioInstance, @Nullable final MMXRootObject rootObject, @Nullable Object target) {
		MTMModel model = (MTMModel) target;

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
	private final EContentAdapter deletedOptionModelAdapter = new SafeMMXContentAdapter() {

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
				displayScenarioInstance(getScenarioInstance(), getRootObject(), currentModel);
			}
		};

		public void reallyNotifyChanged(Notification notification) {
			if (notification.isTouch()) {
				return;
			}
			if (notification.getFeature() == AnalyticsPackage.eINSTANCE.getAnalyticsModel_MtmModel()) {
				displayScenarioInstance(getScenarioInstance(), getRootObject(), currentModel);
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
		if (mainTableComponent != null) {
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
					CommandProviderAwareEditingDomain.withAdaptersDisabled(domain, currentModel, () -> superHandler.handleCommand(command, target, feature));
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
		displayScenarioInstance(getScenarioInstance(), getRootObject(), currentModel);
	}

	@Override
	public void setFocus() {
		mainTableComponent.setFocus();
	}

}