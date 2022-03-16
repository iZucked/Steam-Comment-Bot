/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2022
 * All rights reserved.
 */
package com.mmxlabs.models.lng.analytics.ui.views.viability;

import java.lang.reflect.InvocationTargetException;
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
import com.mmxlabs.models.lng.analytics.BuyOption;
import com.mmxlabs.models.lng.analytics.BuyReference;
import com.mmxlabs.models.lng.analytics.ExistingVesselCharterOption;
import com.mmxlabs.models.lng.analytics.RoundTripShippingOption;
import com.mmxlabs.models.lng.analytics.ShippingOption;
import com.mmxlabs.models.lng.analytics.ViabilityModel;
import com.mmxlabs.models.lng.analytics.ViabilityResult;
import com.mmxlabs.models.lng.analytics.ViabilityRow;
import com.mmxlabs.models.lng.cargo.LoadSlot;
import com.mmxlabs.models.lng.cargo.VesselAvailability;
import com.mmxlabs.models.lng.fleet.Vessel;
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
import com.mmxlabs.scenario.service.model.ScenarioInstance;
import com.mmxlabs.scenario.service.model.manager.IScenarioDataProvider;
import com.mmxlabs.scenario.service.model.manager.ModelReference;

public class ViabilityView extends ScenarioInstanceView implements CommandStackListener {

	private ViabilityModel currentModel;
	// listens which object is selected
	private org.eclipse.e4.ui.workbench.modeling.ISelectionListener selectionListener;
	// selection service from upper class
	private ESelectionService service;

	// Callbacks for objects that need the current input
	private final List<Consumer<ViabilityModel>> inputWants = new LinkedList<>();
	private final List<Runnable> disposables = new LinkedList<>();
	private Label errorLabel;

	private Composite parent;

	private ICommandHandler localCommandHandler;

	private MainTableCompoment mainTableComponent;
	
	private static final Logger LOG = LoggerFactory.getLogger(ViabilityView.class);

	@Override
	public void createPartControl(final Composite parent) {
		this.parent = parent;

		parent.setLayout(GridLayoutFactory.fillDefaults().numColumns(1).create());
		parent.setBackground(PlatformUI.getWorkbench().getDisplay().getSystemColor(SWT.COLOR_WHITE));

		mainTableComponent = new MainTableCompoment();
		mainTableComponent.createControls(this.parent, ViabilityView.this);
		inputWants.addAll(mainTableComponent.getInputWants());
		
		final RunnableAction go = new RunnableAction("Generate", IAction.AS_PUSH_BUTTON, () -> {
		
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
										final ViabilityModel model = ViabilityUtils.createModelFromScenario(scenarioModel, "viabilitymarket");
										ViabilitySandboxEvaluator.evaluate(sdp, scenarioInstance, model, m);

										RunnerHelper.asyncExec(() -> {
											final AnalyticsModel analyticsModel = ScenarioModelUtil.getAnalyticsModel(sdp);
											final EditingDomain editingDomain = sdp.getEditingDomain();

											final CompoundCommand cmd = new CompoundCommand("Create viability matrix");
											cmd.append(SetCommand.create(editingDomain, analyticsModel, AnalyticsPackage.eINSTANCE.getAnalyticsModel_ViabilityModel(), model));
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
		}});
		
		CommonImages.setImageDescriptors(go, IconPaths.Play);
		getViewSite().getActionBars().getToolBarManager().add(go);

		final RunnableAction remove = new RunnableAction("Remove", IAction.AS_PUSH_BUTTON, () -> {
			if (modelRecord != null) {
				try (final IScenarioDataProvider sdp = modelRecord.aquireScenarioDataProvider("MtMScenarioEditorActionDelegate::Delete")) {

					sdp.getModelReference().executeWithTryLock(true, 2_000, () -> {
						final LNGScenarioModel scenarioModel = (LNGScenarioModel) getRootObject();
						final EditingDomain editingDomain = sdp.getEditingDomain();
						final AnalyticsModel analyticsModel = ScenarioModelUtil.getAnalyticsModel(sdp);
						final ViabilityModel model = analyticsModel.getViabilityModel();

						if (model != null) {
							final CompoundCommand cmd = new CompoundCommand("Remove viability matrix");
							cmd.append(SetCommand.create(editingDomain, analyticsModel, AnalyticsPackage.eINSTANCE.getAnalyticsModel_ViabilityModel(), SetCommand.UNSET_VALUE));
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

		ViabilityModel model = (ViabilityModel) target;

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
				model = analyticsModel.getViabilityModel();
			}

			if (model != this.currentModel) {
				update = true;
				if (model != null) {
					// extractPlainForTest(model);
				}
			}
		}
		if (update) {
			setInput(model);
		}
	}

	@SuppressWarnings("unused")
	private void extractForTest(final ViabilityModel model) {
		// for tests
		if (model != null) {
			for (final ViabilityRow row : model.getRows()) {
				for (final BuyOption bo : model.getBuys()) {
					final BuyReference br = (BuyReference) bo;
					final String line1 = "//" + br.getSlot().getName();

					String vesselName = "//";

					final ShippingOption so = row.getShipping();
					if (so instanceof ExistingVesselCharterOption) {
						final VesselAvailability va = ((ExistingVesselCharterOption) so).getVesselCharter();
						vesselName += va.getVessel().getName();
					}

					for (final ViabilityResult vr : row.getRhsResults()) {
						if (vr.getEarliestETA() == null) {
							continue;
						}
						System.out.println(line1);
						System.out.println(vesselName);
						final String line2 = "vls.add(createResult(findMarketByName(spotModel, \"" + vr.getTarget().getName() + "\"), //\n" + vr.getEarliestVolume() + "," + vr.getLatestVolume()
								+ ",\n" + vr.getEarliestPrice() + ", " + vr.getLatestPrice() + ",\n" + "LocalDate.of(" + vr.getEarliestETA().getYear() + "," + vr.getEarliestETA().getMonthValue() + ","
								+ vr.getEarliestETA().getDayOfMonth() + ")," + "LocalDate.of(" + vr.getLatestETA().getYear() + "," + vr.getLatestETA().getMonthValue() + ","
								+ vr.getLatestETA().getDayOfMonth() + ")));\n";
						System.out.print(line2);
						System.out.println(line1);
					}
				}
			}
		}
	}

	@SuppressWarnings("unused")
	private void extractPlainForTest(final ViabilityModel model) {
		// for tests
		if (model != null) {
			for (final ViabilityRow row : model.getRows()) {
				for (final BuyOption bo : model.getBuys()) {
					final BuyReference br = (BuyReference) bo;
					final String line1 = "//" + br.getSlot().getName();

					String vesselName = "//";

					final ShippingOption so = row.getShipping();
					if (so instanceof ExistingVesselCharterOption) {
						final VesselAvailability va = ((ExistingVesselCharterOption) so).getVesselCharter();
						vesselName += va.getVessel().getName();
					}
					if (so instanceof RoundTripShippingOption) {
						final Vessel v = ((RoundTripShippingOption) so).getVessel();
						vesselName += v.getName();
					}

					for (final ViabilityResult vr : row.getRhsResults()) {
						if (vr.getEarliestETA() == null) {
							continue;
						}
						System.out.println(line1);
						System.out.println(vesselName);
						final String line2 = vr.getTarget().getName() + "\n" + vr.getEarliestVolume() + "," + vr.getLatestVolume() + ",\n" + vr.getEarliestPrice() + ", " + vr.getLatestPrice() + ",\n"
								+ vr.getEarliestETA().toString() + ",\n" + vr.getLatestETA().toString() + "\n";
						System.out.print(line2);
						System.out.println(line1);
					}
				}
			}
		}
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
				if (notification.getFeature() == AnalyticsPackage.eINSTANCE.getAnalyticsModel_ViabilityModel()) {
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
			if (notification.getFeature() == AnalyticsPackage.eINSTANCE.getAnalyticsModel_ViabilityModel()) {
				displayScenarioInstance(getScenarioInstance(), getRootObject(), currentModel);
			}
		}
	};

	public void setInput(final @Nullable ViabilityModel model) {
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

		super.dispose();
	}

	public ViabilityModel getModel() {
		return currentModel;
	}

	@Override
	public synchronized ICommandHandler getDefaultCommandHandler() {

		if (localCommandHandler == null) {
			final ICommandHandler superHandler = super.getDefaultCommandHandler();

			final EditingDomain domain = super.getEditingDomain();

			localCommandHandler = new ICommandHandler() {

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
				if (e3part == ViabilityView.this) {
					return;
				}
				if (e3part instanceof PropertySheet) {
					return;
				}
			}

			final ISelection selection = SelectionHelper.adaptSelection(selectedObjects);

			// Find valid, selected objects
			final LoadSlot validSlot = ViabilityView.this.processSelection(e3part, selection);
			mainTableComponent.setSelectedLong(validSlot);
		};
		service.addPostSelectionListener(selectionListener);
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