/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2021
 * All rights reserved.
 */
package com.mmxlabs.models.lng.transformer.ui.navigator.handlers.editor;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.HashSet;
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
import com.mmxlabs.models.lng.cargo.ui.editorpart.actions.CargoEditingCommands;
import com.mmxlabs.models.lng.cargo.ui.editorpart.actions.CargoEditorMenuHelper;
import com.mmxlabs.models.lng.fleet.Vessel;
import com.mmxlabs.models.lng.parameters.OptimisationPlan;
import com.mmxlabs.models.lng.scenario.model.LNGScenarioModel;
import com.mmxlabs.models.lng.scenario.model.util.ScenarioModelUtil;
import com.mmxlabs.models.lng.schedule.CargoAllocation;
import com.mmxlabs.models.lng.schedule.Schedule;
import com.mmxlabs.models.lng.schedule.ScheduleModel;
import com.mmxlabs.models.lng.schedule.SlotAllocation;
import com.mmxlabs.models.lng.spotmarkets.CharterInMarket;
import com.mmxlabs.models.lng.spotmarkets.SpotMarket;
import com.mmxlabs.models.lng.transformer.inject.LNGTransformerHelper;
import com.mmxlabs.models.lng.transformer.ui.LNGOptimisationBuilder;
import com.mmxlabs.models.lng.transformer.ui.LNGOptimisationBuilder.LNGOptimisationRunnerBuilder;
import com.mmxlabs.models.lng.transformer.ui.OptimisationHelper;
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
 * 
 */
public class MtMScenarioEditorActionDelegate implements IEditorActionDelegate, IActionDelegate2 {

	private static final Logger LOG = LoggerFactory.getLogger(MtMScenarioEditorActionDelegate.class);

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
		if (scenarioModel == null)
			return;
		final AnalyticsModel analyticsModel = ScenarioModelUtil.getAnalyticsModel(scenarioModel);
		final CargoModel cargoModel = ScenarioModelUtil.getCargoModel(sdp);
		final EditingDomain editingDomain = sdp.getEditingDomain();
		progressMonitor.worked(100);

		if (!promptClearModels()) {
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

		progressMonitor.worked(100);
		progressMonitor.subTask("Creating MtM model");
		final MTMModel[] model = new MTMModel[1];
		RunnerHelper.syncExecDisplayOptional(() -> {
			model[0] = MTMUtils.createModelFromScenario(scenarioModel, "MtMScenarioEditorActionDelegate", false, true, null, null);
		});
		if (model[0] == null) {
			throw new RuntimeException("Unable to create an MTM model");
		}

		progressMonitor.worked(100);
		progressMonitor.subTask("Evaluating the MtM model");

		final ExecutorService executor = Executors.newFixedThreadPool(1);
		try {
			executor.submit(() -> {
				MTMUtils.evaluateMTMModel(model[0], instance, sdp);
			}).get();
		} catch (Exception e) {
			throw new RuntimeException("Unable to evaluate the MTM model", e);
		} finally {
			progressMonitor.done();
			executor.shutdown();
		}

		progressMonitor.worked(200);
		progressMonitor.subTask("Checking changes in the MtM model");
		final Set<String> usedDischargeIDStrings = new HashSet<>();
		for (final DischargeSlot slot : cargoModel.getDischargeSlots()) {
			usedDischargeIDStrings.add(slot.getName());
		}
		final Set<String> usedLoadIDStrings = new HashSet<>();
		for (final LoadSlot slot : cargoModel.getLoadSlots()) {
			usedLoadIDStrings.add(slot.getName());
		}
		final Map<LoadSlot, SlotAllocation> discharges = new HashMap();
		final Map<DischargeSlot, SlotAllocation> loads = new HashMap();
		final ScheduleModel sm = scenarioModel.getScheduleModel();
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

				double price = Double.MIN_VALUE;

				// getting the price of the original cargo
				if (!discharges.isEmpty()) {
					final SlotAllocation sa = discharges.get(loadSlot);
					if (sa instanceof SlotAllocation) {
						price = sa.getPrice();
					}
				}

				MTMResult bestResult = null;

				// make sure that there's a vessel availability
				for (final MTMResult result : row.getRhsResults()) {
					if (result.getEarliestETA() == null)
						continue;
					if (price < (result.getEarliestPrice() - result.getShippingCost())) {
						price = result.getEarliestPrice();
						bestResult = result;
					}
				}

				if (bestResult != null) {
					final SpotMarket market = bestResult.getTarget();
					if (market != null) {

						// find the existing shipping option
						final ShippingOption so = bestResult.getShipping();
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
							CargoEditorMenuHelper.makeUpDischargeSlot(usedDischargeIDStrings, loadSlot, dischargeSlot, market, sdp, assignedVessel);
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

				double price = Double.MAX_VALUE;

				if (!loads.isEmpty()) {
					final SlotAllocation sa = loads.get(dischargeSlot);
					if (sa != null) {
						price = sa.getPrice();
					}
				}

				MTMResult bestResult = null;

				// make sure that there's a vessel availability
				for (final MTMResult result : row.getLhsResults()) {
					if (result.getEarliestETA() == null)
						continue;
					if (price > (result.getEarliestPrice() + result.getShippingCost())) {
						price = result.getEarliestPrice();
						bestResult = result;
					}
				}

				if (bestResult != null) {
					final SpotMarket market = bestResult.getTarget();
					if (market != null) {

						// find the existing shipping option
						final ShippingOption sop = bestResult.getShipping();
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
							CargoEditorMenuHelper.makeUpLoadSlot(usedDischargeIDStrings, loadSlot, dischargeSlot, market, sdp, assignedVessel);
						}
						//
						CargoEditingCommands.runWiringUpdate(editingDomain, cargoModel, setCommands, deleteObjects, loadSlot, dischargeSlot, cim, spotIndex);
					}
				}
			}
		}

		progressMonitor.worked(100);
		progressMonitor.subTask("Applying changes from the MtM model");

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
		progressMonitor.worked(100);
		progressMonitor.subTask("Evaluating the scenario");

		final OptimisationPlan optimisationPlan = OptimisationHelper.getOptimiserSettings(scenarioModel, true, null, false, false, null);
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
			System.out.print(e.getMessage() + "\n");
		} finally {
			runnerBuilder.dispose();
		}

		progressMonitor.done();
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
}
