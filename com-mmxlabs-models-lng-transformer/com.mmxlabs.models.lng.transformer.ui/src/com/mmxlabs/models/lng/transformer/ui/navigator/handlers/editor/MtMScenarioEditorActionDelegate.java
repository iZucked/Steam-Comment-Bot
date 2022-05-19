/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2022
 * All rights reserved.
 */
package com.mmxlabs.models.lng.transformer.ui.navigator.handlers.editor;

import java.lang.reflect.InvocationTargetException;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.SubMonitor;
import org.eclipse.emf.common.command.Command;
import org.eclipse.emf.common.command.CompoundCommand;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.edit.command.DeleteCommand;
import org.eclipse.emf.edit.domain.EditingDomain;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.dialogs.ProgressMonitorDialog;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.ui.IActionDelegate2;
import org.eclipse.ui.IEditorActionDelegate;
import org.eclipse.ui.IEditorPart;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.Sets;
import com.mmxlabs.license.features.KnownFeatures;
import com.mmxlabs.license.features.LicenseFeatures;
import com.mmxlabs.models.lng.analytics.AbstractSolutionSet;
import com.mmxlabs.models.lng.analytics.AnalyticsModel;
import com.mmxlabs.models.lng.analytics.BuyOption;
import com.mmxlabs.models.lng.analytics.BuyReference;
import com.mmxlabs.models.lng.analytics.ExistingCharterMarketOption;
import com.mmxlabs.models.lng.analytics.MTMModel;
import com.mmxlabs.models.lng.analytics.MTMResult;
import com.mmxlabs.models.lng.analytics.MTMRow;
import com.mmxlabs.models.lng.analytics.SellOption;
import com.mmxlabs.models.lng.analytics.SellReference;
import com.mmxlabs.models.lng.analytics.ShippingOption;
import com.mmxlabs.models.lng.analytics.ui.views.mtm.MTMUtils;
import com.mmxlabs.models.lng.cargo.CargoModel;
import com.mmxlabs.models.lng.cargo.DischargeSlot;
import com.mmxlabs.models.lng.cargo.LoadSlot;
import com.mmxlabs.models.lng.cargo.Slot;
import com.mmxlabs.models.lng.cargo.ui.editorpart.actions.CargoEditingCommands;
import com.mmxlabs.models.lng.cargo.ui.editorpart.actions.CargoEditorMenuHelper;
import com.mmxlabs.models.lng.fleet.Vessel;
import com.mmxlabs.models.lng.parameters.OptimisationPlan;
import com.mmxlabs.models.lng.scenario.model.LNGScenarioModel;
import com.mmxlabs.models.lng.scenario.model.util.ScenarioModelUtil;
import com.mmxlabs.models.lng.schedule.CargoAllocation;
import com.mmxlabs.models.lng.schedule.Cooldown;
import com.mmxlabs.models.lng.schedule.EventGrouping;
import com.mmxlabs.models.lng.schedule.Fuel;
import com.mmxlabs.models.lng.schedule.FuelQuantity;
import com.mmxlabs.models.lng.schedule.FuelUsage;
import com.mmxlabs.models.lng.schedule.Journey;
import com.mmxlabs.models.lng.schedule.Purge;
import com.mmxlabs.models.lng.schedule.Schedule;
import com.mmxlabs.models.lng.schedule.ScheduleModel;
import com.mmxlabs.models.lng.schedule.Sequence;
import com.mmxlabs.models.lng.schedule.SlotAllocation;
import com.mmxlabs.models.lng.schedule.SlotVisit;
import com.mmxlabs.models.lng.spotmarkets.CharterInMarket;
import com.mmxlabs.models.lng.spotmarkets.SpotMarket;
import com.mmxlabs.models.lng.transformer.inject.LNGTransformerHelper;
import com.mmxlabs.models.lng.transformer.ui.LNGOptimisationBuilder;
import com.mmxlabs.models.lng.transformer.ui.LNGOptimisationBuilder.LNGOptimisationRunnerBuilder;
import com.mmxlabs.models.lng.transformer.ui.OptimisationHelper;
import com.mmxlabs.models.lng.types.VolumeUnits;
import com.mmxlabs.rcp.common.RunnerHelper;
import com.mmxlabs.scenario.service.model.Container;
import com.mmxlabs.scenario.service.model.ScenarioInstance;
import com.mmxlabs.scenario.service.model.ScenarioService;
import com.mmxlabs.scenario.service.model.manager.IScenarioDataProvider;
import com.mmxlabs.scenario.service.model.manager.SSDataManager;
import com.mmxlabs.scenario.service.model.manager.ScenarioModelRecord;
import com.mmxlabs.scenario.service.ui.editing.IScenarioServiceEditorInput;

/**
 * @author FM
 * @version 2 06/09/2021
 * 
 */
public class MtMScenarioEditorActionDelegate implements IEditorActionDelegate, IActionDelegate2 {

	private static class MtMBuyCriterion {

		public double salePrice = Double.MIN_VALUE;
		public int saleVolume = Integer.MIN_VALUE;
		public double shippingMargin = Double.MIN_VALUE;
		public MTMResult bestResult = null;

		public void setNewValues(double salePrice, int saleVolume, double shippingMargin, final MTMResult result) {
			this.salePrice = salePrice;
			this.saleVolume = saleVolume;
			this.shippingMargin = shippingMargin;
			this.bestResult = result;
		}
	}

	private static class MtMSellCriterion {

		public double buyPrice = Double.MAX_VALUE;
		public int purchaseVolume = Integer.MAX_VALUE;
		public double shippingMargin = Double.MAX_VALUE;

		public MTMResult bestResult = null;

		public void setNewValues(double buyPrice, int purchaseVolume, double shippingMargin, final MTMResult result) {
			this.buyPrice = buyPrice;
			this.purchaseVolume = purchaseVolume;
			this.shippingMargin = shippingMargin;
			this.bestResult = result;
		}
	}

	private static final Logger LOG = LoggerFactory.getLogger(MtMScenarioEditorActionDelegate.class);

	// make decision based on PNL
	private static final boolean OLD_MTM = LicenseFeatures.isPermitted(KnownFeatures.FEATURE_MTM_OLD);

	private IEditorPart editor;

	@Override
	public void run(final IAction action) {

		if (editor != null && editor.getEditorInput() instanceof IScenarioServiceEditorInput) {
			final IScenarioServiceEditorInput scenarioServiceEditorInput = (IScenarioServiceEditorInput) editor.getEditorInput();
			final ScenarioInstance instance = scenarioServiceEditorInput.getScenarioInstance();

			if (instance != null) {
				final ScenarioModelRecord modelRecord = SSDataManager.Instance.getModelRecord(instance);
				if (modelRecord != null) {
					try (final IScenarioDataProvider sdp = modelRecord.aquireScenarioDataProvider("MtMScenarioEditorActionDelegate")) {
						final ProgressMonitorDialog dialog = new ProgressMonitorDialog(Display.getDefault().getActiveShell());

						sdp.getModelReference().executeWithTryLock(true, () -> {
							try {
								dialog.run(true, false, m -> executeOperation(sdp, instance, m));
							} catch (final InvocationTargetException | InterruptedException e) {
								LOG.error(e.getMessage(), e);
							}

						});
					} catch (final Exception e) {
						throw new RuntimeException("Unable to mark scenario to market", e);
					}
				}
			}
		}
	}

	private static void executeOperation(final IScenarioDataProvider sdp, final ScenarioInstance instance, final IProgressMonitor parentProgressMonitor) {

		parentProgressMonitor.beginTask("Perform MtM action to scenario", 1000);
		final SubMonitor progressMonitor = SubMonitor.convert(parentProgressMonitor, 1000);

		progressMonitor.subTask("Prepare scenario");

		final LNGScenarioModel scenarioModel = ScenarioModelUtil.findScenarioModel(sdp);
		if (scenarioModel == null) {
			progressMonitor.done();
			return;
		}
		final AnalyticsModel analyticsModel = ScenarioModelUtil.getAnalyticsModel(scenarioModel);
		final CargoModel cargoModel = ScenarioModelUtil.getCargoModel(sdp);
		final EditingDomain editingDomain = sdp.getEditingDomain();

		progressMonitor.worked(200);
		progressMonitor.subTask("Checking the schedule model");
		final Set<String> usedIDStrings = getUsedIDs(cargoModel);
		final Map<Slot<?>, SlotAllocation> s2sa = populateSlotAllocationsFromScheduleModel(scenarioModel.getScheduleModel());

		progressMonitor.worked(100);
		if (!promptClearModels()) {
			progressMonitor.done();
			return;
		} else {
			final Command dc = createClearModelsCommand(editingDomain, scenarioModel, analyticsModel);
			if (dc != null) {
				assert dc.canExecute();
				RunnerHelper.syncExecDisplayOptional(() -> {
					editingDomain.getCommandStack().execute(dc);
				});
			}
		}

		progressMonitor.worked(100);

		final List<Command> setCommands = new LinkedList();
		final List<EObject> deleteObjects = new LinkedList();

		progressMonitor.worked(200);
		progressMonitor.subTask("Creating MtM model");
		final MTMModel[] model = new MTMModel[1];
		RunnerHelper.syncExecDisplayOptional(() -> {
			model[0] = MTMUtils.createModelFromScenario(scenarioModel, "MtMScenarioEditorActionDelegate", false, true, null, null);
		});
		if (model[0] == null) {
			progressMonitor.done();
			throw new RuntimeException("Unable to create an MTM model");
		}

		progressMonitor.worked(200);
		progressMonitor.subTask("Evaluating the MtM model");

		final ExecutorService executor = Executors.newFixedThreadPool(1);
		try {
			executor.submit(() -> {
				MTMUtils.evaluateMTMModel(model[0], instance, sdp, progressMonitor);
			}).get();
		} catch (Exception e) {
			progressMonitor.done();
			throw new RuntimeException("Unable to evaluate the MTM model", e);
		} finally {
			executor.shutdown();
		}

		progressMonitor.worked(200);
		progressMonitor.subTask("Looking for the best solutions in the MtM model");

		for (final MTMRow row : model[0].getRows()) {
			if (row.getBuyOption() != null) {
				final BuyOption bo = row.getBuyOption();
				final LoadSlot loadSlot;
				if (bo instanceof BuyReference) {
					final BuyReference br = (BuyReference) bo;
					loadSlot = br.getSlot();
				} else {
					continue;
				}

				MtMBuyCriterion criterion = new MtMBuyCriterion();

				double loadCV = loadSlot.getSlotOrDelegateCV();

				int purchaseVolume = getPurchaseVolume(criterion, s2sa, loadSlot, loadCV);

				// make sure that there's a vessel availability
				for (final MTMResult result : row.getRhsResults()) {
					if (result.getEarliestETA() == null)
						continue;

					final int vesselCapacity = getVesselCapacity(result, loadCV);

					if (criterion.salePrice == Double.MIN_VALUE || criterion.shippingMargin == Double.MIN_VALUE || criterion.saleVolume == Integer.MIN_VALUE) {
						criterion.setNewValues(result.getEarliestPrice(), result.getEarliestVolume(), result.getShippingCost(), result);
					} else {
						if (OLD_MTM) {
							if ((criterion.salePrice - criterion.shippingMargin) < (result.getEarliestPrice() - result.getShippingCost())) {
								criterion.setNewValues(result.getEarliestPrice(), result.getEarliestVolume(), result.getShippingCost(), result);
							}
						} else {
							chooseNewBestSale(result, criterion, purchaseVolume, vesselCapacity);
						}
					}
				}

				if (criterion.bestResult != null) {
					final SpotMarket market = criterion.bestResult.getTarget();
					if (market != null) {

						// find the existing shipping option
						final ShippingOption so = criterion.bestResult.getShipping();
						int spotIndex = -2;
						CharterInMarket cim = null;
						if (so != null) {
							final ExistingCharterMarketOption ecmo = (ExistingCharterMarketOption) so;
							cim = ecmo.getCharterInMarket();
							if (cim != null) {
								spotIndex = ecmo.getSpotIndex();
							}
						}

						// create dischargeSlot
						final DischargeSlot dischargeSlot = CargoEditingCommands.createNewSpotDischarge(editingDomain, setCommands, cargoModel, market);

						// making up the dischargeSlot
						{
							Vessel assignedVessel = null;
							if (cim != null) {
								assignedVessel = cim.getVessel();
							}
							CargoEditorMenuHelper.makeUpDischargeSlot(usedIDStrings, loadSlot, dischargeSlot, market, sdp, assignedVessel);
						}
						//
						CargoEditingCommands.runWiringUpdate(editingDomain, cargoModel, setCommands, deleteObjects, loadSlot, dischargeSlot, cim, spotIndex);
					}
				}

			} else if (row.getSellOption() != null) {
				final SellOption so = row.getSellOption();
				final DischargeSlot dischargeSlot;
				if (so instanceof SellReference) {
					final SellReference sr = (SellReference) so;
					dischargeSlot = sr.getSlot();
				} else {
					continue;
				}

				final MtMSellCriterion criterion = new MtMSellCriterion();

				int saleVolume = getSaleVolume(criterion, s2sa, dischargeSlot);

				// make sure that there's a vessel availability
				for (final MTMResult result : row.getLhsResults()) {
					if (result.getEarliestETA() == null)
						continue;

					if (criterion.buyPrice == Double.MAX_VALUE || criterion.shippingMargin == Double.MAX_VALUE || criterion.purchaseVolume == Integer.MAX_VALUE) {
						criterion.setNewValues(result.getEarliestPrice(), result.getEarliestVolume(), result.getShippingCost(), result);
					} else {
						if (OLD_MTM) {
							if ((criterion.buyPrice + criterion.shippingMargin) > (result.getEarliestPrice() + result.getShippingCost())) {
								criterion.setNewValues(result.getEarliestPrice(), result.getEarliestVolume(), result.getShippingCost(), result);
							}
						} else {
							chooseNewBestBuy(result, criterion, saleVolume);
						}
					}

				}

				if (criterion.bestResult != null) {
					final SpotMarket market = criterion.bestResult.getTarget();
					if (market != null) {

						// find the existing shipping option
						final ShippingOption sop = criterion.bestResult.getShipping();
						int spotIndex = -2;
						CharterInMarket cim = null;
						if (sop != null) {
							final ExistingCharterMarketOption ecmo = (ExistingCharterMarketOption) sop;
							cim = ecmo.getCharterInMarket();
							if (cim != null) {
								spotIndex = ecmo.getSpotIndex();
							}
						}

						final LoadSlot loadSlot = CargoEditingCommands.createNewSpotLoad(editingDomain, setCommands, cargoModel, market);
						// making up the loadSlot
						{
							Vessel assignedVessel = null;
							if (cim != null) {
								assignedVessel = cim.getVessel();
							}
							CargoEditorMenuHelper.makeUpLoadSlot(usedIDStrings, loadSlot, dischargeSlot, market, sdp, assignedVessel);
						}
						//
						CargoEditingCommands.runWiringUpdate(editingDomain, cargoModel, setCommands, deleteObjects, loadSlot, dischargeSlot, cim, spotIndex);
					}
				}
			}
		}

		progressMonitor.worked(100);
		progressMonitor.subTask("Applying changes from the MtM model");

		processNewWirings(editingDomain, setCommands, deleteObjects);
		progressMonitor.worked(100);
		progressMonitor.subTask("Evaluating the scenario");

		final OptimisationPlan optimisationPlan = OptimisationHelper.getOptimiserSettings(scenarioModel, true, false, false, null);
		assert optimisationPlan != null;

		// Hack: Add on shipping only hint to avoid generating spot markets during eval.
		final LNGOptimisationRunnerBuilder runnerBuilder = LNGOptimisationBuilder.begin(sdp, instance) //
				.withThreadCount(1) //
				.withOptimisationPlan(optimisationPlan) //
				.withHints(LNGTransformerHelper.HINT_SHIPPING_ONLY) //
				.buildDefaultRunner();

		try {
			sdp.setLastEvaluationFailed(true);
			runnerBuilder.evaluateInitialState();
			sdp.setLastEvaluationFailed(false);
		} catch (final Exception e) {
			progressMonitor.done();
			System.out.print(e.getMessage() + "\n");
		} finally {
			progressMonitor.done();
		}
	}

	private static int getSaleVolume(final MtMSellCriterion criterion, final Map<Slot<?>, SlotAllocation> s2sa, final DischargeSlot dischargeSlot) {
		int saleVolume = dischargeSlot.getSlotOrDelegateMinQuantity();
		if (dischargeSlot.getSlotOrDelegateVolumeLimitsUnit() == VolumeUnits.M3) {
			saleVolume *= dischargeSlot.getSlotOrDelegateMinCv();
		}
		if (!s2sa.isEmpty()) {
			final SlotAllocation sa = s2sa.get(dischargeSlot);
			if (sa != null) {
				criterion.buyPrice = sa.getPrice();
				criterion.purchaseVolume = sa.getEnergyTransferred();
				if (sa.getCargoAllocation() != null) {
					Integer shippingCost = getShippingCost(sa.getCargoAllocation());
					if (shippingCost != null) {
						criterion.shippingMargin = (double) shippingCost / criterion.purchaseVolume;
					}
				}
				final LoadSlot ls = (LoadSlot) sa.getSlot();
				if (!s2sa.isEmpty()) {
					final SlotAllocation dssa = s2sa.get(ls);
					if (dssa != null) {
						saleVolume = dssa.getEnergyTransferred();
					}
				}
			}
		}
		return saleVolume;
	}

	private static int getPurchaseVolume(MtMBuyCriterion criterion, final Map<Slot<?>, SlotAllocation> s2sa, final LoadSlot loadSlot, double loadCV) {
		int purchaseVolume = loadSlot.getSlotOrDelegateMaxQuantity();
		if (loadSlot.getSlotOrDelegateVolumeLimitsUnit() == VolumeUnits.M3) {
			purchaseVolume *= (int) (loadCV);
		}

		// getting the price of the original cargo
		if (!s2sa.isEmpty()) {
			final SlotAllocation sa = s2sa.get(loadSlot);

			if (sa instanceof SlotAllocation) {
				criterion.salePrice = sa.getPrice();
				criterion.saleVolume = sa.getEnergyTransferred();
				if (sa.getCargoAllocation() != null) {
					Integer shippingCost = getShippingCost(sa.getCargoAllocation());
					if (shippingCost != null) {
						criterion.shippingMargin = (double) shippingCost / criterion.saleVolume;
					}
				}
				final DischargeSlot ds = (DischargeSlot) sa.getSlot();
				if (!s2sa.isEmpty()) {
					final SlotAllocation lssa = s2sa.get(ds);
					if (lssa != null) {
						purchaseVolume = lssa.getEnergyTransferred();
					}
				}
			}
		}
		return purchaseVolume;
	}

	private static Set<String> getUsedIDs(final CargoModel cargoModel) {
		final Set<String> usedIDStrings = new HashSet<>();
		for (final DischargeSlot slot : cargoModel.getDischargeSlots()) {
			usedIDStrings.add(slot.getName());
		}
		for (final LoadSlot slot : cargoModel.getLoadSlots()) {
			usedIDStrings.add(slot.getName());
		}
		return usedIDStrings;
	}

	private static void processNewWirings(final EditingDomain editingDomain, final List<Command> setCommands, final List<EObject> deleteObjects) {
		if (!setCommands.isEmpty()) {
			final CompoundCommand currentWiringCommand = new CompoundCommand("MtMScenarioEditorActionDelegate.currentWiringCommand");
			// Process set before delete
			for (final Command c : setCommands) {
				currentWiringCommand.append(c);
			}
			if (!deleteObjects.isEmpty()) {
				currentWiringCommand.append(DeleteCommand.create(editingDomain, deleteObjects));
			}

			assert currentWiringCommand.canExecute();

			RunnerHelper.syncExecDisplayOptional(() -> {
				editingDomain.getCommandStack().execute(currentWiringCommand);
			});

		}
	}

	/**
	 * Populates the maps of discharge allocations for corresponding load slots And
	 * map of load allocations for the corresponding discharge slots from the
	 * pre-existing cargoes
	 * 
	 * @param sm
	 * @param discharges
	 * @param loads
	 */
	private static void populateSlotAllocationsFromScheduleModel(final ScheduleModel sm, final Map<Slot<?>, SlotAllocation> discharges, final Map<DischargeSlot, SlotAllocation> loads) {
		if (sm != null) {
			final Schedule schedule = sm.getSchedule();
			if (schedule != null) {
				for (final CargoAllocation ca : schedule.getCargoAllocations()) {
					LoadSlot ls = null;
					SlotAllocation dischargeSlotAllocation = null;
					DischargeSlot ds = null;
					SlotAllocation loadSlotAllocation = null;
					for (final SlotAllocation sa : ca.getSlotAllocations()) {
						if (sa.getSlot() instanceof LoadSlot) {
							ls = (LoadSlot) sa.getSlot();
							loadSlotAllocation = sa;
						}
						if (sa.getSlot() instanceof DischargeSlot) {
							dischargeSlotAllocation = sa;
							ds = (DischargeSlot) sa.getSlot();
						}
					}
					if (ls != null && dischargeSlotAllocation != null) {
						discharges.putIfAbsent(ls, dischargeSlotAllocation);
					}
					if (ds != null && loadSlotAllocation != null) {
						loads.putIfAbsent(ds, loadSlotAllocation);
					}
				}
			}
		}
	}

	/**
	 * Populates the maps of discharge allocations for corresponding load slots And
	 * map of load allocations for the corresponding discharge slots from the
	 * pre-existing cargoes
	 * 
	 * @param sm
	 * @param discharges
	 * @param loads
	 */
	private static Map<Slot<?>, SlotAllocation> populateSlotAllocationsFromScheduleModel(final ScheduleModel sm) {
		final Map<Slot<?>, SlotAllocation> slotToSlotAllocations = new HashMap<>();
		if (sm != null) {
			final Schedule schedule = sm.getSchedule();
			if (schedule != null) {
				for (final CargoAllocation ca : schedule.getCargoAllocations()) {
					LoadSlot ls = null;
					SlotAllocation dischargeSlotAllocation = null;
					DischargeSlot ds = null;
					SlotAllocation loadSlotAllocation = null;
					for (final SlotAllocation sa : ca.getSlotAllocations()) {
						if (sa.getSlot() instanceof LoadSlot) {
							ls = (LoadSlot) sa.getSlot();
							loadSlotAllocation = sa;
						}
						if (sa.getSlot() instanceof DischargeSlot) {
							dischargeSlotAllocation = sa;
							ds = (DischargeSlot) sa.getSlot();
						}
					}
					if (ls != null && dischargeSlotAllocation != null) {
						slotToSlotAllocations.putIfAbsent(ls, dischargeSlotAllocation);
					}
					if (ds != null && loadSlotAllocation != null) {
						slotToSlotAllocations.putIfAbsent(ds, loadSlotAllocation);
					}
				}
			}
		}
		return slotToSlotAllocations;
	}

	/**
	 * Compute the buy mtm price of the current MTMResult and compare it with the
	 * existing criterion
	 * 
	 * @param result
	 * @param criterion
	 * @param saleVolume
	 */
	private static void chooseNewBestBuy(final MTMResult result, final MtMSellCriterion criterion, int saleVolume) {
		double purchaseCost = criterion.buyPrice * criterion.purchaseVolume;
		double shippingCost = criterion.shippingMargin * saleVolume;
		double buyPlusShip = purchaseCost + shippingCost;
		double mtm = buyPlusShip / (double) saleVolume;

		double cPurchaseCost = result.getEarliestPrice() * result.getEarliestVolume();
		double cShippingCost = result.getShippingCost() * result.getEarliestVolume();
		double cBuyPlusShip = cPurchaseCost + cShippingCost;
		double cMtM = cBuyPlusShip / (double) saleVolume;

		if (mtm < cMtM) {
			criterion.setNewValues(result.getEarliestPrice(), result.getEarliestVolume(), result.getShippingCost(), result);
		}
	}

	/**
	 * Compute the sell mtm price of the current MTMResult and compare it with the
	 * existing criterion
	 * 
	 * @param result
	 * @param criterion
	 * @param purchaseVolume
	 * @param vesselCapacity
	 */
	private static void chooseNewBestSale(final MTMResult result, MtMBuyCriterion criterion, int purchaseVolume, final int vesselCapacity) {
		double revenue = criterion.salePrice * criterion.saleVolume;
		double shippingCost = criterion.shippingMargin * criterion.saleVolume;
		double saleMinusShip = revenue - shippingCost;
		double mtm = saleMinusShip / (double) Math.min(vesselCapacity, purchaseVolume);

		double cRevenue = result.getEarliestPrice() * result.getEarliestVolume();
		double cShippingCost = result.getShippingCost() * result.getEarliestVolume();
		double cSaleMinusShip = cRevenue - cShippingCost;
		double cMtM = cSaleMinusShip / (double) Math.min(vesselCapacity, purchaseVolume);

		if (mtm < cMtM) {
			criterion.setNewValues(result.getEarliestPrice(), result.getEarliestVolume(), result.getShippingCost(), result);
		}
	}

	@Override
	public void setActiveEditor(final IAction action, final IEditorPart targetEditor) {
		this.editor = targetEditor;
		if (editor != null) {
			if (editor.getEditorInput() instanceof IScenarioServiceEditorInput) {
				final IScenarioServiceEditorInput scenarioServiceEditorInput = (IScenarioServiceEditorInput) editor.getEditorInput();
				final ScenarioInstance instance = scenarioServiceEditorInput.getScenarioInstance();

				{
					Container c = instance;
					while (c != null && !(c instanceof ScenarioService)) {
						c = c.getParent();
					}
					if (c instanceof ScenarioService) {
						action.setEnabled(true);
						return;
					} else {
						action.setEnabled(false);
						return;
					}
				}
			}
		}

		if (action != null) {
			action.setEnabled(true);
		}
	}

	@Override
	public void selectionChanged(IAction action, ISelection selection) {

	}

	@Override
	public void init(IAction action) {

	}

	@Override
	public void dispose() {

	}

	@Override
	public void runWithEvent(IAction action, Event event) {
		run(action);
	}

	// TODO: keep updates in-line with ScheduleModelInvalidateCommandProvider
	private static Command createClearModelsCommand(final EditingDomain domain, final LNGScenarioModel scenarioModel, final AnalyticsModel analyticsModel) {
		final List<EObject> delete = new LinkedList<>();

		final ScheduleModel scheduleModel = ScenarioModelUtil.getScheduleModel(scenarioModel);
		if (scheduleModel.getSchedule() != null) {
			delete.add(scheduleModel.getSchedule());
		}
		if (analyticsModel.getViabilityModel() != null) {
			delete.add(analyticsModel.getViabilityModel());
		}
		if (analyticsModel.getMtmModel() != null) {
			delete.add(analyticsModel.getMtmModel());
		}
		if (!analyticsModel.getOptimisations().isEmpty()) {
			delete.addAll(analyticsModel.getOptimisations());
		}
		if (!analyticsModel.getBreakevenModels().isEmpty()) {
			delete.addAll(analyticsModel.getBreakevenModels());
		}
		// Clear sandbox results, but not the sandbox itself
		if (!analyticsModel.getOptionModels().isEmpty()) {
			analyticsModel.getOptionModels().forEach(m -> {
				AbstractSolutionSet r = m.getResults();
				if (r != null) {
					delete.add(r);
				}
			});
		}

		if (delete.isEmpty()) {
			return null;
		}
		return DeleteCommand.create(domain, delete);
	}

	private static boolean promptClearModels() {
		boolean[] result = new boolean[1];
		RunnerHelper.syncExec((display) -> {
			result[0] = MessageDialog.openConfirm(display.getActiveShell(), "Scenario edit",
					"This change will remove all results. Press OK to continue, otherwise press cancel and fork the scenario.");
		});
		return result[0];
	}

	/**
	 * Get the total shipping cost of the cargoAllocation. Shipping cost is for the
	 * round trip journey
	 * 
	 * @param cargoAllocation
	 * @return
	 */
	private static Integer getShippingCost(final CargoAllocation cargoAllocation) {

		if (cargoAllocation == null) {
			return 0;
		}

		Sequence sequence = cargoAllocation.getSequence();
		List<com.mmxlabs.models.lng.schedule.Event> events = ((EventGrouping) cargoAllocation).getEvents();

		if (sequence == null || events == null) {
			return 0;
		}

		// Bit of a double count here, but need to decide what to add to the model
		int shippingCost = 0;
		int charterCost = 0;
		for (final com.mmxlabs.models.lng.schedule.Event event : events) {

			charterCost += event.getCharterCost();

			if (event instanceof SlotVisit) {
				final SlotVisit slotVisit = (SlotVisit) event;
				// Port Costs
				shippingCost += slotVisit.getPortCost();
			}

			if (event instanceof Journey) {
				final Journey journey = (Journey) event;
				// Canal Costs
				shippingCost += journey.getToll();
			}

			if (event instanceof FuelUsage) {
				final FuelUsage fuelUsage = (FuelUsage) event;
				// Base fuel costs
				shippingCost += getFuelCost(fuelUsage, Fuel.BASE_FUEL, Fuel.PILOT_LIGHT);
			}

			if (event instanceof Purge) {
				final Purge purge = (Purge) event;
				shippingCost += purge.getCost();
			}
			if (event instanceof Cooldown) {
				final Cooldown cooldown = (Cooldown) event;
				shippingCost += cooldown.getCost();
			}
		}

		// Add on chartering costs
		if (sequence == null || sequence.isSpotVessel() || sequence.isTimeCharterVessel()) {
			shippingCost += charterCost;
		}
		return shippingCost;
	}

	/**
	 * Get the fuel cost
	 * 
	 * @param fuelUser
	 * @param fuels
	 * @return
	 */
	private static int getFuelCost(final FuelUsage fuelUser, final Fuel... fuels) {
		final Set<Fuel> fuelsOfInterest = Sets.newHashSet(fuels);
		int sum = 0;
		if (fuelUser != null) {
			final EList<FuelQuantity> fuelQuantities = fuelUser.getFuels();
			for (final FuelQuantity fq : fuelQuantities) {
				if (fuelsOfInterest.contains(fq.getFuel())) {
					sum += fq.getCost();
				}
			}
		}
		return sum;
	}

	/**
	 * Looks up the usable vessel capacity if CharterInMarket is present on the
	 * MTMResult Returns Integer.MAX_VALUE if no result present
	 * 
	 * @param result
	 * @return
	 */
	private static int getVesselCapacity(final MTMResult result, final double cv) {
		if (result.getShipping() instanceof ExistingCharterMarketOption) {
			final ExistingCharterMarketOption ecmo = (ExistingCharterMarketOption) result.getShipping();
			final CharterInMarket cim = ecmo.getCharterInMarket();
			if (cim != null) {
				final Vessel vessel = cim.getVessel();
				if (vessel != null) {
					return (int) (vessel.getVesselOrDelegateCapacity() * vessel.getVesselOrDelegateFillCapacity() * cv);
				}
			}
		}
		return Integer.MAX_VALUE;
	}
}
