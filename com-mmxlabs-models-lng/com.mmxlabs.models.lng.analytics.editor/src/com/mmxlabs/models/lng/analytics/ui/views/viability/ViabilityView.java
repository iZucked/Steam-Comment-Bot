/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
package com.mmxlabs.models.lng.analytics.ui.views.viability;

import java.time.LocalDate;
import java.util.EventObject;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ForkJoinPool;
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
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.actions.ActionFactory;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.eclipse.ui.views.properties.PropertySheet;

import com.mmxlabs.models.common.commandservice.CommandProviderAwareEditingDomain;
import com.mmxlabs.models.lng.analytics.AnalyticsFactory;
import com.mmxlabs.models.lng.analytics.AnalyticsModel;
import com.mmxlabs.models.lng.analytics.AnalyticsPackage;
import com.mmxlabs.models.lng.analytics.BuyOption;
import com.mmxlabs.models.lng.analytics.BuyReference;
import com.mmxlabs.models.lng.analytics.ExistingVesselAvailability;
import com.mmxlabs.models.lng.analytics.SellReference;
import com.mmxlabs.models.lng.analytics.ShippingOption;
import com.mmxlabs.models.lng.analytics.ViabilityModel;
import com.mmxlabs.models.lng.analytics.ViabilityResult;
import com.mmxlabs.models.lng.analytics.ViabilityRow;
import com.mmxlabs.models.lng.analytics.ui.views.evaluators.ViabilitySandboxEvaluator;
import com.mmxlabs.models.lng.cargo.CargoModel;
import com.mmxlabs.models.lng.cargo.DischargeSlot;
import com.mmxlabs.models.lng.cargo.LoadSlot;
import com.mmxlabs.models.lng.cargo.VesselAvailability;
import com.mmxlabs.models.lng.scenario.model.LNGScenarioModel;
import com.mmxlabs.models.lng.scenario.model.util.ScenarioModelUtil;
import com.mmxlabs.models.lng.spotmarkets.SpotMarket;
import com.mmxlabs.models.lng.spotmarkets.SpotMarketGroup;
import com.mmxlabs.models.lng.spotmarkets.SpotMarketsModel;
import com.mmxlabs.models.mmxcore.MMXRootObject;
import com.mmxlabs.models.ui.editorpart.ScenarioInstanceView;
import com.mmxlabs.models.ui.editors.ICommandHandler;
import com.mmxlabs.models.ui.valueproviders.IReferenceValueProviderProvider;
import com.mmxlabs.rcp.common.RunnerHelper;
import com.mmxlabs.rcp.common.SelectionHelper;
import com.mmxlabs.rcp.common.actions.CopyGridToClipboardAction;
import com.mmxlabs.rcp.common.actions.PackActionFactory;
import com.mmxlabs.rcp.common.actions.RunnableAction;
import com.mmxlabs.scenario.service.model.ScenarioInstance;
import com.mmxlabs.scenario.service.model.manager.ModelReference;

public class ViabilityView extends ScenarioInstanceView implements CommandStackListener {

	private ViabilityModel model;
	private ViabilityModel rootOptionsModel;
	private MMXRootObject rootObject;
	// listens which object is selected
	private org.eclipse.e4.ui.workbench.modeling.ISelectionListener selectionListener;
	// selection service from upper class
	private ESelectionService service;

	// Callbacks for objects that need the current input
	private final List<Consumer<ViabilityModel>> inputWants = new LinkedList<>();
	private final List<Runnable> disposables = new LinkedList<>();
	private Label errorLabel;
	
	private ScenarioInstance instance;

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
					ForkJoinPool.commonPool().submit(() -> {

						final LNGScenarioModel scenarioModel = (LNGScenarioModel) getRootObject();
						final ScenarioInstance si = getScenarioInstance();
						if (scenarioModel == null) {
							return;
						}
						final String name = "viability model" + (si != null ? si.getName() : "");
						final ViabilityModel model = ViabilityUtils.createModelFromScenario(scenarioModel, name);
						//populateModel(model);
						this.instance = si;

						ViabilitySandboxEvaluator.evaluate(getScenarioDataProvider(), si, model);

						RunnerHelper.asyncExec(() -> {
							final CompoundCommand cmd = new CompoundCommand("Create viability matrix");
							final AnalyticsModel analyticsModel = ScenarioModelUtil.getAnalyticsModel(getScenarioDataProvider());
							cmd.append(SetCommand.create(getEditingDomain(), analyticsModel, AnalyticsPackage.eINSTANCE.getAnalyticsModel_ViabilityModel(), model));
							getEditingDomain().getCommandStack().execute(cmd);

							doDisplayScenarioInstance(si, getRootObject(), model);
						});
					}).get();
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (ExecutionException e) {
					// TODO Auto-generated catch block
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
		listenToSelectionsFrom();

		listenToScenarioSelection();
	}

	@Override
	protected void doDisplayScenarioInstance(@Nullable final ScenarioInstance scenarioInstance, @Nullable final MMXRootObject rootObject) {
		doDisplayScenarioInstance(scenarioInstance, rootObject, null);
	}

	void doDisplayScenarioInstance(@Nullable final ScenarioInstance scenarioInstance, @Nullable final MMXRootObject rootObject, @Nullable final ViabilityModel model) {

		if (errorLabel != null) {
			errorLabel.dispose();
			errorLabel = null;
		}

		//extractForTest(model);
		
		ViabilityModel tmodel = null;
		boolean update = false;
		
		if (scenarioInstance == null) {

			errorLabel = new Label(this.parent, SWT.NONE);
			errorLabel.setBackground(PlatformUI.getWorkbench().getDisplay().getSystemColor(SWT.COLOR_WHITE));
			errorLabel.setText("No scenario selected");
			tmodel = null;
			update = true;
			this.instance = null;
		} else {
			final ViabilityModel tempModel = getModel();
			if (tempModel != null) {
				if (tempModel.equals(model)) {
					return;
				} else {
					if (this.instance.equals(scenarioInstance)) {
						return;
					}
				}
			}
			
			if (model != null) {
				tmodel = model;
				update = true;
			} else if (rootObject instanceof LNGScenarioModel) {
				final LNGScenarioModel lngScenarioModel = (LNGScenarioModel) rootObject;
				final @NonNull AnalyticsModel analyticsModel = ScenarioModelUtil.getAnalyticsModel(lngScenarioModel);
	
				if (analyticsModel.getViabilityModel() == null) {
					tmodel = null;
					this.instance = null;
					update = true;
				} else {
					if (!scenarioInstance.equals(this.instance)) {
						tmodel = analyticsModel.getViabilityModel();
						this.instance = scenarioInstance;
						update = true;
					}
				}
			}
		}
		if (update) {
			setModel(tmodel);
			setInput(tmodel);
		}
	}

	@SuppressWarnings("unused")
	private void extractForTest(final ViabilityModel model) {
		//for tests
		if(model != null) {
			for(ViabilityRow row : model.getRows()) {
				for (BuyOption bo : model.getBuys()) {
					final BuyReference br = (BuyReference) bo;
					String line1 = "//" + br.getSlot().getName();
					
					String vesselName = "//";
					
					final ShippingOption so = row.getShipping();
					if (so instanceof ExistingVesselAvailability) {
						final VesselAvailability va = ((ExistingVesselAvailability) so).getVesselAvailability();
						vesselName += va.getVessel().getName();
					}
					
					for (ViabilityResult vr : row.getRhsResults()) {
						if (vr.getEarliestETA() == null) {
							continue;
						}
						System.out.println(line1);
						System.out.println(vesselName);
						String line2 = "vls.add(createResult(findMarketByName(spotModel, \"" + vr.getTarget().getName() 
								+ "\"), //\n"
								+ vr.getEarliestVolume() + "," + vr.getLatestVolume() + ",\n"
								+ vr.getEarliestPrice() + ", " + vr.getLatestPrice() + ",\n" 
								+ "LocalDate.of(" + vr.getEarliestETA().getYear() + "," 
								+ vr.getEarliestETA().getMonthValue() + ","
								+ vr.getEarliestETA().getDayOfMonth() + "),"
								+ "LocalDate.of(" + vr.getLatestETA().getYear() + "," 
								+ vr.getLatestETA().getMonthValue() + ","
								+ vr.getLatestETA().getDayOfMonth() + ")));\n";
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
	private final EContentAdapter deletedOptionModelAdapter = new EContentAdapter() {
		@Override
		public void notifyChanged(final Notification notification) {
			super.notifyChanged(notification);
			if (notification.isTouch()) {
				return;
			}
			if (notification.getFeature() == AnalyticsPackage.eINSTANCE.getAnalyticsModel_ViabilityModel()) {
				if (model != null && notification.getOldValue() == model) {
					displayScenarioInstance(getScenarioInstance());
				} else if (rootOptionsModel != null && notification.getOldValue() == rootOptionsModel) {
					displayScenarioInstance(getScenarioInstance());
				}
			}
		}
	};

	public void setInput(final @Nullable ViabilityModel model) {
		if (rootOptionsModel != null) {
			rootOptionsModel.eAdapters().remove(deletedOptionModelAdapter);
			rootOptionsModel = null;
		}
		if (rootObject != null) {
			rootObject.eAdapters().remove(deletedOptionModelAdapter);
			rootObject = null;
		}

		this.setModel(model);

		inputWants.forEach(want -> want.accept(model));

		rootOptionsModel = getRootOptionsModel(model);

		rootObject = getRootObject();
		if (rootObject != null) {
			rootObject.eAdapters().add(deletedOptionModelAdapter);
		}
	}

	private ViabilityModel getRootOptionsModel(@Nullable final ViabilityModel optionModel) {
		return optionModel;
	}

	@Override
	public void dispose() {

		disposables.forEach(r -> r.run());

		if (model != null) {
			model = null;
		}
		if (rootOptionsModel != null) {
			rootOptionsModel = null;
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
		return model;
	}

	public void setModel(final ViabilityModel model) {
		this.model = model;
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
						commandProviderAwareEditingDomain.disableAdapters(model);
					}
					superHandler.handleCommand(command, target, feature);
					if (domain instanceof CommandProviderAwareEditingDomain) {
						final CommandProviderAwareEditingDomain commandProviderAwareEditingDomain = (CommandProviderAwareEditingDomain) domain;
						commandProviderAwareEditingDomain.enableAdapters(model, false);
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
	
//	public static ViabilityModel createModelFromScenario(final @NonNull LNGScenarioModel sm, final @NonNull String name) {
//		final ViabilityModel model = AnalyticsFactory.eINSTANCE.createViabilityModel();
//		model.setName(name);
//		final CargoModel cargoModel = ScenarioModelUtil.getCargoModel(sm);
//		final SpotMarketsModel spotModel = ScenarioModelUtil.getSpotMarketsModel(sm);
//
//		for (final LoadSlot slot : cargoModel.getLoadSlots()) {
//			if (slot.getCargo() == null) {
//				final BuyReference buy = AnalyticsFactory.eINSTANCE.createBuyReference();
//				buy.setSlot(slot);
//				model.getBuys().add(buy);
//			}
//		}
//		for (final DischargeSlot slot : cargoModel.getDischargeSlots()) {
//			if (slot.getCargo() == null) {
//				final SellReference sale = AnalyticsFactory.eINSTANCE.createSellReference();
//				sale.setSlot(slot);
//				model.getSells().add(sale);
//			}
//		}
//		for (final VesselAvailability vessel : cargoModel.getVesselAvailabilities()) {
//			if (vessel != null) {
//				final ExistingVesselAvailability v = AnalyticsFactory.eINSTANCE.createExistingVesselAvailability();
//				v.setVesselAvailability(vessel);
//				model.getShippingTemplates().add(v);
//			}
//		}
//		// final SpotMarketGroup smgDP = spotModel.getDesPurchaseSpotMarket();
//		// if (smgDP != null) {
//		// for (final SpotMarket spotMarket : smgDP.getMarkets()) {
//		// if (spotMarket != null) {
//		// model.getMarkets().add(spotMarket);
//		// }
//		// }
//		// }
//		final SpotMarketGroup smgDS = spotModel.getDesSalesSpotMarket();
//		if (smgDS != null) {
//			for (final SpotMarket spotMarket : smgDS.getMarkets()) {
//				if (spotMarket != null) {
//					if (spotMarket.isEnabled()) {
//						model.getMarkets().add(spotMarket);
//					}
//				}
//			}
//		}
//		// final SpotMarketGroup smgFP = spotModel.getFobPurchasesSpotMarket();
//		// if (smgFP != null) {
//		// for (final SpotMarket spotMarket : smgFP.getMarkets()) {
//		// if (spotMarket != null) {
//		// model.getMarkets().add(spotMarket);
//		// }
//		// }
//		// }
//		final SpotMarketGroup smgFS = spotModel.getFobSalesSpotMarket();
//		if (smgFS != null) {
//			for (final SpotMarket spotMarket : smgFS.getMarkets()) {
//				if (spotMarket != null) {
//					if (spotMarket.isEnabled()) {
//						model.getMarkets().add(spotMarket);
//					}
//				}
//			}
//		}
//
//		populateModel(model);
//		
//		return model;
//	}
//	
//	public static void populateModel(final @NonNull ViabilityModel model) {
//		for (final BuyOption bo : model.getBuys()) {
//			for (final ShippingOption so : model.getShippingTemplates()) {
//				final ViabilityRow row = AnalyticsFactory.eINSTANCE.createViabilityRow();
//				row.setBuyOption(bo);
//				row.setShipping(so);
//				model.getRows().add(row);
//			}
//		}
//	}

	@Override
	public void commandStackChanged(final EventObject event) {
		// TODO Auto-generated method stub

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
				Object obj = itr.next();
				/*
				 * Just the first Load Slot selection now
				 */
				if (obj instanceof LoadSlot) {
					LoadSlot load = (LoadSlot) obj;
					if (load.getCargo() == null) {
						return load;
					}
				}
			}
		}
		
		return null;
	}

}