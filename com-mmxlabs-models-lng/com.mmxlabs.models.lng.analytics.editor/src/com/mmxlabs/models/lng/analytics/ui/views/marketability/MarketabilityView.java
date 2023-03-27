/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
package com.mmxlabs.models.lng.analytics.ui.views.marketability;

import java.lang.reflect.InvocationTargetException;
import java.util.EventObject;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
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
import org.eclipse.emf.ecore.ETypedElement;
import org.eclipse.emf.ecore.util.EContentAdapter;
import org.eclipse.emf.edit.command.SetCommand;
import org.eclipse.emf.edit.domain.EditingDomain;
import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.Nullable;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.dialogs.ProgressMonitorDialog;
import org.eclipse.jface.layout.GridLayoutFactory;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.actions.ActionFactory;
import org.eclipse.ui.views.properties.PropertySheet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mmxlabs.models.common.commandservice.CommandProviderAwareEditingDomain;
import com.mmxlabs.models.lng.analytics.AnalyticsModel;
import com.mmxlabs.models.lng.analytics.AnalyticsPackage;
import com.mmxlabs.models.lng.analytics.MarketabilityModel;
import com.mmxlabs.models.lng.cargo.LoadSlot;
import com.mmxlabs.models.lng.scenario.model.LNGScenarioModel;
import com.mmxlabs.models.lng.scenario.model.util.ScenarioModelUtil;
import com.mmxlabs.models.mmxcore.MMXRootObject;
import com.mmxlabs.models.mmxcore.impl.SafeMMXContentAdapter;
import com.mmxlabs.models.ui.editorpart.ScenarioInstanceView;
import com.mmxlabs.models.ui.editors.ICommandHandler;
import com.mmxlabs.models.ui.valueproviders.IReferenceValueProviderProvider;
import com.mmxlabs.rcp.common.RunnerHelper;
import com.mmxlabs.rcp.common.SelectionHelper;
import com.mmxlabs.rcp.common.actions.CopyGridToClipboardAction;
import com.mmxlabs.rcp.common.actions.PackActionFactory;
import com.mmxlabs.rcp.common.actions.RunnableAction;
import com.mmxlabs.rcp.icons.lingo.CommonImages;
import com.mmxlabs.rcp.icons.lingo.CommonImages.IconPaths;
import com.mmxlabs.scenario.service.IScenarioServiceSelectionChangedListener;
import com.mmxlabs.scenario.service.IScenarioServiceSelectionProvider;
import com.mmxlabs.scenario.service.ScenarioResult;
import com.mmxlabs.scenario.service.model.ScenarioInstance;
import com.mmxlabs.scenario.service.model.manager.IScenarioDataProvider;
import com.mmxlabs.scenario.service.model.manager.ModelReference;

public class MarketabilityView extends ScenarioInstanceView implements CommandStackListener {

	private MarketabilityModel currentModel;
	// listens which object is selected
	private org.eclipse.e4.ui.workbench.modeling.ISelectionListener selectionListener;
	
	private IScenarioServiceSelectionProvider selectedScenariosService;
	
	private IScenarioServiceSelectionChangedListener scenarioChangedListener;

	// selection service from upper class
	private ESelectionService service;

	// Callbacks for objects that need the current input
	private final List<Consumer<MarketabilityModel>> inputWants = new LinkedList<>();
	private final List<Runnable> disposables = new LinkedList<>();
	private Label errorLabel;

	private Composite parent;

	private ICommandHandler localCommandHandler;

	private MainTableComponent mainTableComponent;

	private static final Logger LOG = LoggerFactory.getLogger(MarketabilityView.class);

	@Override
	public void createPartControl(final Composite parent) {
		this.parent = parent;

		parent.setLayout(GridLayoutFactory.fillDefaults().numColumns(1).create());
		parent.setBackground(PlatformUI.getWorkbench().getDisplay().getSystemColor(SWT.COLOR_WIDGET_BACKGROUND));

		mainTableComponent = new MainTableComponent();

		
		mainTableComponent.createControls(this.parent, MarketabilityView.this);
		inputWants.addAll(mainTableComponent.getInputWants());
		selectedScenariosService = getSite().getService(IScenarioServiceSelectionProvider.class);
		scenarioChangedListener = (pinned, others) ->  {
			ScenarioResult scenarioResult = null;
			if(pinned != null) {
				scenarioResult = pinned;
			} else if(others.iterator().hasNext()) {
				scenarioResult = others.iterator().next();
			}
			if(scenarioResult != null) {
				final IScenarioDataProvider sdp = scenarioResult.getScenarioDataProvider();
				final AnalyticsModel analyticsModel =  ScenarioModelUtil.getAnalyticsModel(sdp);
				setInput(scenarioResult.getRootObject(), analyticsModel.getMarketabilityModel());
			} else {
				setInput(null, null);
			}
			
		};
		selectedScenariosService.addSelectionChangedListener(scenarioChangedListener);

		final RunnableAction go = new RunnableAction("Generate", IAction.AS_PUSH_BUTTON, () -> {

			if (modelRecord != null) {
				try (final IScenarioDataProvider sdp = modelRecord.aquireScenarioDataProvider("MtMScenarioEditorActionDelegate::Create")) {
					final ProgressMonitorDialog dialog = new ProgressMonitorDialog(Display.getDefault().getActiveShell());
					final Optional<Integer> vesselSpeed = mainTableComponent.getVesselSpeed();
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
											final MarketabilityModel model = MarketabilityUtils.createModelFromScenario(scenarioModel, "marketabilitymarket", vesselSpeed.orElse(null));
											boolean successful = MarketabilitySandboxEvaluator.evaluate(sdp, scenarioInstance, model, m, true);
											if (successful) {
												RunnerHelper.asyncExec(() -> {
													final AnalyticsModel analyticsModel = ScenarioModelUtil.getAnalyticsModel(sdp);
													final EditingDomain editingDomain = sdp.getEditingDomain();

													final CompoundCommand cmd = new CompoundCommand("Create marketability matrix");
													cmd.append(SetCommand.create(editingDomain, analyticsModel, AnalyticsPackage.eINSTANCE.getAnalyticsModel_MarketabilityModel(), model));
													editingDomain.getCommandStack().execute(cmd);
													doDisplayScenarioInstance(scenarioInstance, scenarioModel, model);
												});
											}
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

		CommonImages.setImageDescriptors(go, IconPaths.Play_16);
		getViewSite().getActionBars().getToolBarManager().add(go);

		final RunnableAction remove = new RunnableAction("Remove", IAction.AS_PUSH_BUTTON, () -> {
			if (modelRecord != null) {
				try (final IScenarioDataProvider sdp = modelRecord.aquireScenarioDataProvider("MtMScenarioEditorActionDelegate::Delete")) {

					sdp.getModelReference().executeWithTryLock(true, 2_000, () -> {
						final LNGScenarioModel scenarioModel = (LNGScenarioModel) getRootObject();
						final EditingDomain editingDomain = sdp.getEditingDomain();
						final AnalyticsModel analyticsModel = ScenarioModelUtil.getAnalyticsModel(sdp);
						final MarketabilityModel model = analyticsModel.getMarketabilityModel();

						if (model != null) {
							final CompoundCommand cmd = new CompoundCommand("Remove marketability matrix");
							cmd.append(SetCommand.create(editingDomain, analyticsModel, AnalyticsPackage.eINSTANCE.getAnalyticsModel_MarketabilityModel(), SetCommand.UNSET_VALUE));
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

		final Action copyTableAction = new CopyGridToClipboardAction(mainTableComponent.getViewer().getGrid());
		getViewSite().getActionBars().setGlobalActionHandler(ActionFactory.COPY.getId(), copyTableAction);
		getViewSite().getActionBars().getToolBarManager().add(copyTableAction);

		getViewSite().getActionBars().getToolBarManager().update(true);

		service = getSite().getService(ESelectionService.class);

		listenToSelectionsFrom();

		listenToScenarioSelection();

	}

	@Override
	protected synchronized void doDisplayScenarioInstance(@Nullable final ScenarioInstance scenarioInstance, @Nullable final MMXRootObject rootObject, @Nullable Object target) {

		MarketabilityModel model = (MarketabilityModel) target;

		if (errorLabel != null) {
			errorLabel.dispose();
			errorLabel = null;
		}

		if (scenarioInstance == null) {
			errorLabel = new Label(this.parent, SWT.NONE);
			errorLabel.setBackground(PlatformUI.getWorkbench().getDisplay().getSystemColor(SWT.COLOR_WHITE));
			errorLabel.setText("No scenario selected");
			model = null;
			this.rootObject = null;
		} else {
			if (model == null && rootObject instanceof @NonNull LNGScenarioModel lngScenarioModel) {
				final @NonNull AnalyticsModel analyticsModel = ScenarioModelUtil.getAnalyticsModel(lngScenarioModel);
				model = analyticsModel.getMarketabilityModel();
			}
			this.rootObject = rootObject;
		}
		setInput(rootObject, model);
	}

	/**
	 * If the current model is deleted, then clear the input
	 */
	private final EContentAdapter deletedOptionModelAdapter = new SafeMMXContentAdapter() {

		@Override
		protected synchronized void missedNotifications(java.util.List<Notification> missed) {
			boolean doDisplay = false;
			for (Notification notification : missed) {

				if (notification.isTouch()) {
					continue;
				}

				if (notification.getFeature() == AnalyticsPackage.eINSTANCE.getAnalyticsModel_MarketabilityModel()
						|| notification.getFeature() == AnalyticsPackage.eINSTANCE.getMarketabilityModel_Rows()) {
					doDisplay = true;
					break;
				}
			}
			if (doDisplay) {
				doDisplayScenarioInstance(getScenarioInstance(), getRootObject(), currentModel);
			}
		}

		@Override
		public void reallyNotifyChanged(Notification notification) {
			if (notification.isTouch()) {
				return;
			}
			if (notification.getFeature() == AnalyticsPackage.eINSTANCE.getAnalyticsModel_MarketabilityModel()
					|| notification.getFeature() == AnalyticsPackage.eINSTANCE.getMarketabilityModel_Rows()) {
				doDisplayScenarioInstance(getScenarioInstance(), getRootObject(), currentModel);
			}
		}
	};

	public void setInput(final @Nullable MMXRootObject rootObject, final @Nullable MarketabilityModel model) {
		if (this.rootObject != null) {
			this.rootObject.eAdapters().remove(deletedOptionModelAdapter);
			this.rootObject = null;
		}
		this.rootObject = rootObject;
		if (this.rootObject != null) {
			this.rootObject.eAdapters().add(deletedOptionModelAdapter);
		}
		this.currentModel = model;

		inputWants.forEach(want -> want.accept(model));
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
		if(selectedScenariosService != null) {
			selectedScenariosService.removeSelectionChangedListener(scenarioChangedListener);
			selectedScenariosService = null;
		}
		
		mainTableComponent.dispose();
		super.dispose();
	}

	public MarketabilityModel getModel() {
		return currentModel;
	}

	@Override
	public synchronized ICommandHandler getDefaultCommandHandler() {

		if (localCommandHandler == null) {
			final ICommandHandler superHandler = super.getDefaultCommandHandler();

			final EditingDomain domain = super.getEditingDomain();

			localCommandHandler = new ICommandHandler() {

				@Override
				public void handleCommand(final Command command, final EObject target, final ETypedElement typedElement) {
					CommandProviderAwareEditingDomain.withAdaptersDisabled(domain, currentModel, () -> superHandler.handleCommand(command, target, typedElement));
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

		return localCommandHandler;

	}

	@Override
	public void commandStackChanged(final EventObject event) {
		displayScenarioInstance(getScenarioInstance(), getRootObject(), currentModel);
	}

	@Override
	public void setFocus() {
		mainTableComponent.setFocus();
	}

	/*
	 * Looks for selection
	 */
	public void listenToSelectionsFrom() {

		selectionListener = (part, selectedObjects) -> {
			final IWorkbenchPart e3part = SelectionHelper.getE3Part(part);
			{
				// TODO: Ignore navigator
				if (e3part == MarketabilityView.this) {
					return;
				}
				if (e3part instanceof PropertySheet) {
					return;
				}
			}

			final ISelection selection = SelectionHelper.adaptSelection(selectedObjects);

			// Find valid, selected objects
			final LoadSlot validSlot = MarketabilityView.this.processSelection(e3part, selection);
			if (validSlot != null) {
				mainTableComponent.selectRowWithLoad(validSlot);
			}
		};
		service.addPostSelectionListener(selectionListener);
	}

	public ESelectionService getSelectionService() {
		return service;
	}

	/*
	 * At the moment only allows to process ONE load slot
	 */
	private LoadSlot processSelection(final IWorkbenchPart part, final ISelection selection) {

		if (selection instanceof IStructuredSelection) {

			final Iterator<?> itr = ((IStructuredSelection) selection).iterator();
			while (itr.hasNext()) {
				final Object obj = itr.next();
				/*
				 * Just the first Load Slot selection now
				 */
				if (obj instanceof LoadSlot) {
					return (LoadSlot) obj;
				}
			}
		}

		return null;
	}

}