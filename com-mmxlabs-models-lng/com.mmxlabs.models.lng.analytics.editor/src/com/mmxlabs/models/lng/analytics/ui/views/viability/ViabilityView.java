/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2019
 * All rights reserved.
 */
package com.mmxlabs.models.lng.analytics.ui.views.viability;

import java.util.EventObject;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.Consumer;

import org.eclipse.e4.ui.model.application.ui.basic.MPart;
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
import org.eclipse.jface.layout.GridLayoutFactory;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.BusyIndicator;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.ui.ISharedImages;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.actions.ActionFactory;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.eclipse.ui.views.properties.PropertySheet;

import com.mmxlabs.models.common.commandservice.CommandProviderAwareEditingDomain;
import com.mmxlabs.models.lng.analytics.AnalyticsModel;
import com.mmxlabs.models.lng.analytics.AnalyticsPackage;
import com.mmxlabs.models.lng.analytics.BuyOption;
import com.mmxlabs.models.lng.analytics.BuyReference;
import com.mmxlabs.models.lng.analytics.ExistingVesselAvailability;
import com.mmxlabs.models.lng.analytics.MTMModel;
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
import com.mmxlabs.models.mmxcore.impl.MMXContentAdapter;
import com.mmxlabs.models.ui.editorpart.ScenarioInstanceView;
import com.mmxlabs.models.ui.editors.ICommandHandler;
import com.mmxlabs.models.ui.valueproviders.IReferenceValueProviderProvider;
import com.mmxlabs.rcp.common.RunnerHelper;
import com.mmxlabs.rcp.common.SelectionHelper;
import com.mmxlabs.rcp.common.actions.CopyGridToClipboardAction;
import com.mmxlabs.rcp.common.actions.PackActionFactory;
import com.mmxlabs.rcp.common.actions.RunnableAction;
import com.mmxlabs.scenario.service.model.ScenarioInstance;
import com.mmxlabs.scenario.service.model.manager.IScenarioDataProvider;
import com.mmxlabs.scenario.service.model.manager.ModelReference;

public class ViabilityView extends ScenarioInstanceView implements CommandStackListener {

	private ViabilityModel currentModel;
	private MMXRootObject rootObject;
	// listens which object is selected
	private org.eclipse.e4.ui.workbench.modeling.ISelectionListener selectionListener;
	// selection service from upper class
	private ESelectionService service;

	// Callbacks for objects that need the current input
	private final List<Consumer<ViabilityModel>> inputWants = new LinkedList<>();
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
		mainTableComponent.createControls(this.parent, ViabilityView.this);
		inputWants.addAll(mainTableComponent.getInputWants());

		final RunnableAction go = new RunnableAction("Generate", Action.AS_PUSH_BUTTON, () -> {
			BusyIndicator.showWhile(Display.getDefault(), () -> {
				try {
					final LNGScenarioModel scenarioModel = (LNGScenarioModel) getRootObject();
					final ScenarioInstance scenarioInstance = getScenarioInstance();
					final IScenarioDataProvider sdp = getScenarioDataProvider();
					if (scenarioModel == null) {
						return;
					}
					final ExecutorService executor = Executors.newFixedThreadPool(1);
					try {
						executor.submit(() -> {
							final ViabilityModel model = ViabilityUtils.createModelFromScenario(scenarioModel, "viabilitymarket");
							ViabilitySandboxEvaluator.evaluate(sdp, scenarioInstance, model);

							RunnerHelper.asyncExec(() -> {
								final AnalyticsModel analyticsModel = ScenarioModelUtil.getAnalyticsModel(sdp);
								final EditingDomain editingDomain = sdp.getEditingDomain();
								// clearing the viability model before the evaluation of the new one
								// SG - No need for this?
								// editingDomain.getCommandStack()
								// .execute(SetCommand.create(editingDomain, analyticsModel, AnalyticsPackage.eINSTANCE.getAnalyticsModel_ViabilityModel(), SetCommand.UNSET_VALUE));

								final CompoundCommand cmd = new CompoundCommand("Create viability matrix");
								cmd.append(SetCommand.create(editingDomain, analyticsModel, AnalyticsPackage.eINSTANCE.getAnalyticsModel_ViabilityModel(), model));
								editingDomain.getCommandStack().execute(cmd);

								doDisplayScenarioInstance(scenarioInstance, scenarioModel, model);
							});
						}).get();
					} finally {
						executor.shutdown();
					}
				} catch (final InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (final ExecutionException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			});
		});
		go.setImageDescriptor(AbstractUIPlugin.imageDescriptorFromPlugin("com.mmxlabs.models.lng.analytics.editor", "icons/sandbox_generate.gif"));
		getViewSite().getActionBars().getToolBarManager().add(go);
		
		final Action remove = new Action("Remove", Action.AS_PUSH_BUTTON) {
			@Override
			public void run() {
				final LNGScenarioModel scenarioModel = (LNGScenarioModel) getRootObject();
				final ScenarioInstance scenarioInstance = getScenarioInstance();
				final IScenarioDataProvider sdp = getScenarioDataProvider();
				final EditingDomain editingDomain = sdp.getEditingDomain();
				final AnalyticsModel analyticsModel = ScenarioModelUtil.getAnalyticsModel(sdp);
				final ViabilityModel model = analyticsModel.getViabilityModel();
				
				if (model != null) {
					final CompoundCommand cmd = new CompoundCommand("Remove viability matrix");
					cmd.append(SetCommand.create(editingDomain, analyticsModel, AnalyticsPackage.eINSTANCE.getAnalyticsModel_ViabilityModel(), SetCommand.UNSET_VALUE));
					editingDomain.getCommandStack().execute(cmd);

					doDisplayScenarioInstance(scenarioInstance, scenarioModel, null);
				}
			}
		};
		remove.setImageDescriptor(PlatformUI.getWorkbench().getSharedImages().getImageDescriptor(ISharedImages.IMG_TOOL_DELETE));
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
	protected void doDisplayScenarioInstance(@Nullable final ScenarioInstance scenarioInstance, @Nullable final MMXRootObject rootObject) {
		doDisplayScenarioInstance(scenarioInstance, rootObject, null);
	}

	synchronized void doDisplayScenarioInstance(@Nullable final ScenarioInstance scenarioInstance, @Nullable final MMXRootObject rootObject, @Nullable ViabilityModel model) {

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
					//extractPlainForTest(model);
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
					if (so instanceof ExistingVesselAvailability) {
						final VesselAvailability va = ((ExistingVesselAvailability) so).getVesselAvailability();
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
					if (so instanceof ExistingVesselAvailability) {
						final VesselAvailability va = ((ExistingVesselAvailability) so).getVesselAvailability();
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
	private final EContentAdapter deletedOptionModelAdapter = new MMXContentAdapter() {

		@Override
		protected void missedNotifications(java.util.List<Notification> missed) {
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
				displayScenarioInstance(getScenarioInstance());
			}
		};

		public void reallyNotifyChanged(Notification notification) {
			if (notification.isTouch()) {
				return;
			}
			if (notification.getFeature() == AnalyticsPackage.eINSTANCE.getAnalyticsModel_ViabilityModel()) {
				displayScenarioInstance(getScenarioInstance());
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

	/*
	 * Looks for selection
	 */
	public void listenToSelectionsFrom() {

		selectionListener = new org.eclipse.e4.ui.workbench.modeling.ISelectionListener() {

			@Override
			public void selectionChanged(final MPart part, final Object selectedObjects) {
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

			}
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
					final LoadSlot load = (LoadSlot) obj;
					return load;
				}
			}
		}

		return null;
	}

}