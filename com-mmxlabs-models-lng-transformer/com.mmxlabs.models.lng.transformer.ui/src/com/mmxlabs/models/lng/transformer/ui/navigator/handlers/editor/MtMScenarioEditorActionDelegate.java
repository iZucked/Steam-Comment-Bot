/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2019
 * All rights reserved.
 */
package com.mmxlabs.models.lng.transformer.ui.navigator.handlers.editor;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.eclipse.emf.common.command.Command;
import org.eclipse.emf.common.command.CompoundCommand;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.edit.command.DeleteCommand;
import org.eclipse.emf.edit.domain.EditingDomain;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.swt.widgets.Event;
import org.eclipse.ui.IActionDelegate2;
import org.eclipse.ui.IEditorActionDelegate;
import org.eclipse.ui.IEditorPart;

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
import com.mmxlabs.models.lng.scenario.model.LNGScenarioModel;
import com.mmxlabs.models.lng.scenario.model.util.ScenarioModelUtil;
import com.mmxlabs.models.lng.spotmarkets.CharterInMarket;
import com.mmxlabs.models.lng.spotmarkets.SpotMarket;
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

	private IEditorPart editor;

	@Override
	public void run(final IAction action) {

		if (editor.getEditorInput() instanceof IScenarioServiceEditorInput) {
			final IScenarioServiceEditorInput scenarioServiceEditorInput = (IScenarioServiceEditorInput) editor.getEditorInput();
			final ScenarioInstance instance = scenarioServiceEditorInput.getScenarioInstance();
			
			if (instance != null) {
				final ScenarioModelRecord modelRecord =	SSDataManager.Instance.getModelRecord(instance);
				if (modelRecord != null) {
					try (final IScenarioDataProvider sdp = modelRecord.aquireScenarioDataProvider("MtMScenarioEditorActionDelegate")) {
						
						final List<Command> setCommands = new LinkedList();
						final List<EObject> deleteObjects = new LinkedList();
						
						final LNGScenarioModel scenarioModel = ScenarioModelUtil.findScenarioModel(sdp);
						final CargoModel cargoModel = ScenarioModelUtil.getCargoModel(sdp);
						final MTMModel[] model = new MTMModel[1];
						
						final ExecutorService executor = Executors.newFixedThreadPool(1);
						try {
							executor.submit(() -> {
								model[0] = MTMUtils.evaluateMTMModel(scenarioModel, instance, sdp, false, "MtMScenarioEditorActionDelegate", true);
							}).get();
						} catch (Exception e) {
							throw new RuntimeException("Unable to evaluate MTM model", e);
						} finally {
							executor.shutdown();
						}
						if (model[0] == null) {
							return;
						}
						
						final EditingDomain editingDomain = sdp.getEditingDomain();
						
						final Set<String> usedDischargeIDStrings = new HashSet<>();
						for (final DischargeSlot slot : cargoModel.getDischargeSlots()) {
							usedDischargeIDStrings.add(slot.getName());
						}
						final Set<String> usedLoadIDStrings = new HashSet<>();
						for (final LoadSlot slot : cargoModel.getLoadSlots()) {
							usedLoadIDStrings.add(slot.getName());
						}

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
								MTMResult bestResult = null;
								
								// make sure that there's a vessel availability
								for (final MTMResult result : row.getRhsResults()) {
									if (result.getEarliestETA() == null) continue;
									if (price < result.getEarliestPrice()){
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
								
								double price = Double.MIN_VALUE;
								MTMResult bestResult = null;
								
								// make sure that there's a vessel availability
								for (final MTMResult result : row.getLhsResults()) {
									if (result.getEarliestETA() == null) continue;
									if (price < result.getEarliestPrice()){
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
										
										final LoadSlot loadSlot = CargoEditingCommands.createNewSpotLoad(editingDomain, setCommands, cargoModel, !dischargeSlot.isFOBSale(), market);										
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

							editingDomain.getCommandStack().execute(currentWiringCommand);
						}
						//
					} catch (final Exception e) {
						throw new RuntimeException("Unable to mark scenario to market", e);
					}
				}
			}
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
}
