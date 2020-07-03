/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2020
 * All rights reserved.
 */
package com.mmxlabs.models.lng.analytics.ui.views.evaluators;

import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

import org.eclipse.emf.common.command.CompoundCommand;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.edit.command.AddCommand;
import org.eclipse.jdt.annotation.Nullable;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;

import com.mmxlabs.common.Pair;
import com.mmxlabs.models.lng.analytics.AnalyticsFactory;
import com.mmxlabs.models.lng.analytics.AnalyticsPackage;
import com.mmxlabs.models.lng.analytics.BaseCase;
import com.mmxlabs.models.lng.analytics.BaseCaseRow;
import com.mmxlabs.models.lng.analytics.BuyMarket;
import com.mmxlabs.models.lng.analytics.BuyOption;
import com.mmxlabs.models.lng.analytics.BuyReference;
import com.mmxlabs.models.lng.analytics.ExistingCharterMarketOption;
import com.mmxlabs.models.lng.analytics.ExistingVesselCharterOption;
import com.mmxlabs.models.lng.analytics.OptionAnalysisModel;
import com.mmxlabs.models.lng.analytics.RoundTripShippingOption;
import com.mmxlabs.models.lng.analytics.SellMarket;
import com.mmxlabs.models.lng.analytics.SellOption;
import com.mmxlabs.models.lng.analytics.SellReference;
import com.mmxlabs.models.lng.analytics.ShippingOption;
import com.mmxlabs.models.lng.cargo.Cargo;
import com.mmxlabs.models.lng.cargo.DischargeSlot;
import com.mmxlabs.models.lng.cargo.LoadSlot;
import com.mmxlabs.models.lng.cargo.Slot;
import com.mmxlabs.models.lng.cargo.SpotDischargeSlot;
import com.mmxlabs.models.lng.cargo.SpotLoadSlot;
import com.mmxlabs.models.lng.cargo.VesselAvailability;
import com.mmxlabs.models.lng.cargo.VesselEvent;
import com.mmxlabs.models.lng.cargo.ui.editorpart.CargoModelRowTransformer;
import com.mmxlabs.models.lng.scenario.model.LNGScenarioModel;
import com.mmxlabs.models.lng.scenario.model.util.ScenarioModelUtil;
import com.mmxlabs.models.lng.schedule.CargoAllocation;
import com.mmxlabs.models.lng.schedule.Event;
import com.mmxlabs.models.lng.schedule.OpenSlotAllocation;
import com.mmxlabs.models.lng.schedule.PortVisit;
import com.mmxlabs.models.lng.schedule.Sequence;
import com.mmxlabs.models.lng.schedule.SlotAllocation;
import com.mmxlabs.models.lng.schedule.SlotVisit;
import com.mmxlabs.models.lng.spotmarkets.CharterInMarket;
import com.mmxlabs.models.lng.spotmarkets.SpotMarket;
import com.mmxlabs.models.lng.types.VesselAssignmentType;
import com.mmxlabs.rcp.common.RunnerHelper;
import com.mmxlabs.scenario.service.model.manager.IScenarioDataProvider;

public class SelectionToSandboxUtil {

	/**
	 * Returns true if the current selection can be used to create a Sandbox.
	 * 
	 * @param selection
	 * @return
	 */
	public static boolean canSelectionBeUsed(final ISelection selection) {

		boolean foundValidObject = false;
		final Set<LNGScenarioModel> scenarios = new HashSet<>();

		if (selection instanceof IStructuredSelection) {

			final IStructuredSelection ss = (IStructuredSelection) selection;
			final Iterator<?> itr = ss.iterator();

			LOOP_SELECTION: while (itr.hasNext()) {
				Object obj = itr.next();
				
				if (obj instanceof CargoModelRowTransformer.RowData) {
					CargoModelRowTransformer.RowData rowData = (CargoModelRowTransformer.RowData) obj;
					// Note - not strictly correct, row may contain independent an load and discharge
					if (rowData.getCargo() != null) {
						obj = rowData.getCargo();
					} else if (rowData.getLoadSlot() != null) {
						obj = rowData.getLoadSlot();
					} else {
						obj = rowData.getDischargeSlot();
					}
					
				}
				
				if (obj instanceof EObject) {
					final EObject eObject = (EObject) obj;
					scenarios.add(findScenarioModel(eObject));
				}
				

				// Check for schedule model objects before looking at underlying objects. E.g. sandbox from a result.
				if (obj instanceof Cargo || obj instanceof Slot) {
					foundValidObject = true;
					continue;
				}

				if (obj instanceof Event) {
					Event evt = (Event) obj;
					while (evt != null && !(evt instanceof PortVisit)) {
						evt = evt.getPreviousEvent();
					}
					if (evt != null) {
						obj = evt;
					}
				}

				if (obj instanceof OpenSlotAllocation) {
					foundValidObject = true;
					continue LOOP_SELECTION;
				} else if (obj instanceof PortVisit) {
					if (obj instanceof SlotVisit) {
						foundValidObject = true;
						continue LOOP_SELECTION;
					}
				}
			}
		}
		scenarios.remove(null);
		return foundValidObject && scenarios.size() == 1;
	}

	public static void selectionToSandbox(final ISelection selection, final boolean portfolioMode, final IScenarioDataProvider sdp) {

		if (selection instanceof IStructuredSelection) {
			final IStructuredSelection ss = (IStructuredSelection) selection;

			final BaseCase baseCase = AnalyticsFactory.eINSTANCE.createBaseCase();
			baseCase.setKeepExistingScenario(portfolioMode);

			final Set<Object> seenObjects = new HashSet<>();
			final Set<Object> objectsForPass2 = new HashSet<>();

			final Map<LoadSlot, BuyOption> buyMap = new HashMap<>();
			final Map<DischargeSlot, SellOption> sellMap = new HashMap<>();

			final Map<VesselAvailability, ShippingOption> vaMap = new HashMap<>();
			final Map<Pair<CharterInMarket, Integer>, ShippingOption> cimMap = new HashMap<>();
			{
				final Iterator<?> itr = ss.iterator();
				LOOP_SELECTION: while (itr.hasNext()) {

					Object obj = itr.next();
					
					if (obj instanceof CargoModelRowTransformer.RowData) {
						CargoModelRowTransformer.RowData rowData = (CargoModelRowTransformer.RowData) obj;
						// Note - not strictly correct, row may contain independent an load and discharge
						if (rowData.getCargo() != null) {
							obj = rowData.getCargo();
						} else if (rowData.getLoadSlot() != null) {
							obj = rowData.getLoadSlot();
						} else {
							obj = rowData.getDischargeSlot();
						}
						
					}
					
					// Check for schedule model objects before looking at underlying objects. E.g. sandbox from a result.
					if (obj instanceof Cargo || obj instanceof Slot) {
						objectsForPass2.add(obj);
						continue;
					}

					if (obj instanceof Event) {
						Event evt = (Event) obj;
						while (evt != null && !(evt instanceof PortVisit)) {
							evt = evt.getPreviousEvent();
						}
						if (evt != null) {
							obj = evt;
						}
					}

					if (obj instanceof OpenSlotAllocation) {
						final OpenSlotAllocation sa = (OpenSlotAllocation) obj;
						Slot<?> slot = sa.getSlot();
						if (!seenObjects.add(slot)) {
							continue LOOP_SELECTION;
						}

						// Create a new row
						final BaseCaseRow row = AnalyticsFactory.eINSTANCE.createBaseCaseRow();
						baseCase.getBaseCase().add(row);
						if (slot instanceof LoadSlot) {
							final LoadSlot loadSlot = (LoadSlot) slot;
							final BuyOption option = loadSlotToOption(loadSlot, buyMap);
							row.setBuyOption(option);
						} else {
							final DischargeSlot dischargeSlot = (DischargeSlot) slot;
							final SellOption option = dischargeSlotToOption(dischargeSlot, sellMap);
							row.setSellOption(option);
						}
					} else if (obj instanceof PortVisit) {
						if (obj instanceof SlotVisit) {
							final SlotVisit slotVisit = (SlotVisit) obj;
							final CargoAllocation cargoAllocation = slotVisit.getSlotAllocation().getCargoAllocation();
							// Have we seen any of the slots before?
							for (final SlotAllocation sa : cargoAllocation.getSlotAllocations()) {
								if (!seenObjects.add(sa.getSlot())) {
									continue LOOP_SELECTION;
								}
							}
							// Create a new row
							final BaseCaseRow row = AnalyticsFactory.eINSTANCE.createBaseCaseRow();
							baseCase.getBaseCase().add(row);
							for (final SlotAllocation sa : cargoAllocation.getSlotAllocations()) {
								final Slot<?> slot = sa.getSlot();
								if (slot instanceof LoadSlot) {
									final LoadSlot loadSlot = (LoadSlot) slot;
									final BuyOption option = loadSlotToOption(loadSlot, buyMap);
									row.setBuyOption(option);
								} else {
									final DischargeSlot dischargeSlot = (DischargeSlot) slot;
									final SellOption option = dischargeSlotToOption(dischargeSlot, sellMap);
									row.setSellOption(option);
								}
							}

							// Get vessel allocation
							final Sequence sequence = cargoAllocation.getSequence();
							if (sequence != null) {
								final VesselAvailability vesselAvailability = sequence.getVesselAvailability();
								if (vesselAvailability != null) {
									setVesselAvailability(portfolioMode, vaMap, row, vesselAvailability);
								} else {
									final CharterInMarket charterInMarket = sequence.getCharterInMarket();
									if (charterInMarket != null) {
										setCharterInMarket(portfolioMode, cimMap, row, charterInMarket, sequence.getSpotIndex());
									}
								}
							}
						}
					}
				}
			}
			objectsForPass2.removeAll(seenObjects);
			if (baseCase.getBaseCase().isEmpty() && !objectsForPass2.isEmpty()) {

				seenObjects.clear();
				final Iterator<?> itr = objectsForPass2.iterator();
				LOOP_SELECTION: while (itr.hasNext()) {
					Object obj = itr.next();

					Cargo cargo = null;
					Slot<?> slot = null;
					VesselEvent event = null;
					if (obj instanceof Cargo) {
						cargo = (Cargo) obj;
					} else if (obj instanceof Slot) {
						slot = (Slot<?>) obj;
						cargo = slot.getCargo();
					} else if (obj instanceof VesselEvent) {
						event = (VesselEvent) obj;
					}

					if (cargo != null) {
						// Have we seen any of the slots before?
						for (final Slot<?> s : cargo.getSlots()) {
							if (!seenObjects.add(s)) {
								continue LOOP_SELECTION;
							}
						}
						// Create a new row
						final BaseCaseRow row = AnalyticsFactory.eINSTANCE.createBaseCaseRow();
						baseCase.getBaseCase().add(row);
						for (final Slot<?> s : cargo.getSlots()) {
							if (s instanceof LoadSlot) {
								final LoadSlot loadSlot = (LoadSlot) s;
								final BuyOption option = loadSlotToOption(loadSlot, buyMap);
								row.setBuyOption(option);
							} else {
								final DischargeSlot dischargeSlot = (DischargeSlot) s;
								final SellOption option = dischargeSlotToOption(dischargeSlot, sellMap);
								row.setSellOption(option);
							}
						}
						// Get vessel allocation
						final VesselAssignmentType sequence = cargo.getVesselAssignmentType();
						if (sequence instanceof VesselAvailability) {
							final VesselAvailability vesselAvailability = (VesselAvailability) sequence;
							if (vesselAvailability != null) {
								setVesselAvailability(portfolioMode, vaMap, row, vesselAvailability);
							} else if (sequence instanceof CharterInMarket) {
								final CharterInMarket charterInMarket = (CharterInMarket) sequence;
								setCharterInMarket(portfolioMode, cimMap, row, charterInMarket, cargo.getSpotIndex());
							}
						}

					} else if (slot != null) {

						if (!seenObjects.add(slot)) {
							continue LOOP_SELECTION;
						}

						// Create a new row
						final BaseCaseRow row = AnalyticsFactory.eINSTANCE.createBaseCaseRow();
						baseCase.getBaseCase().add(row);
						if (slot instanceof LoadSlot) {
							final LoadSlot loadSlot = (LoadSlot) slot;
							final BuyOption option = loadSlotToOption(loadSlot, buyMap);
							row.setBuyOption(option);
						} else {
							final DischargeSlot dischargeSlot = (DischargeSlot) slot;
							final SellOption option = dischargeSlotToOption(dischargeSlot, sellMap);
							row.setSellOption(option);
						}
					} else if (event != null) {
						// Nothing to do...
					}
				}
			}

			final OptionAnalysisModel sandbox = AnalyticsFactory.eINSTANCE.createOptionAnalysisModel();
			sandbox.setName("New sandbox");
			sandbox.setBaseCase(baseCase);
			sandbox.setPartialCase(AnalyticsFactory.eINSTANCE.createPartialCase());

			// Push through a set to ensure uniqueness
			sandbox.getBuys().addAll(new LinkedHashSet<>(buyMap.values()));
			sandbox.getSells().addAll(new LinkedHashSet<>(sellMap.values()));
			sandbox.getShippingTemplates().addAll(new LinkedHashSet<>(vaMap.values()));
			sandbox.getShippingTemplates().addAll(new LinkedHashSet<>(cimMap.values()));

			RunnerHelper.asyncExec(() -> {
				final CompoundCommand cmd = new CompoundCommand("Convert to new sandbox");
				cmd.append(AddCommand.create(sdp.getEditingDomain(), ScenarioModelUtil.getAnalyticsModel(sdp), AnalyticsPackage.Literals.ANALYTICS_MODEL__OPTION_MODELS,
						Collections.singletonList(sandbox)));
				sdp.getEditingDomain().getCommandStack().execute(cmd);
			});

		}

	}

	private static void setCharterInMarket(final boolean portfolioMode, final Map<Pair<CharterInMarket, Integer>, ShippingOption> cimMap, final BaseCaseRow row, final CharterInMarket charterInMarket,
			int spotIndex) {
		final Pair<CharterInMarket, Integer> key = Pair.of(charterInMarket, spotIndex);
		if (cimMap.containsKey(key)) {
			row.setShipping(cimMap.get(key));
		} else {
			if (portfolioMode) {
				final ExistingCharterMarketOption eva = AnalyticsFactory.eINSTANCE.createExistingCharterMarketOption();
				eva.setCharterInMarket(charterInMarket);
				eva.setSpotIndex(spotIndex);
				cimMap.put(key, eva);
				row.setShipping(eva);
			} else {
				final RoundTripShippingOption eva = AnalyticsFactory.eINSTANCE.createRoundTripShippingOption();
				eva.setVessel(charterInMarket.getVessel());
				eva.setHireCost(charterInMarket.getCharterInRate());
				cimMap.put(key, eva);
				row.setShipping(eva);
			}
		}
	}

	private static void setVesselAvailability(final boolean portfolioMode, final Map<VesselAvailability, ShippingOption> vaMap, final BaseCaseRow row, final VesselAvailability vesselAvailability) {
		if (vaMap.containsKey(vesselAvailability)) {
			row.setShipping(vaMap.get(vesselAvailability));
		} else {
			if (portfolioMode) {
				final ExistingVesselCharterOption eva = AnalyticsFactory.eINSTANCE.createExistingVesselCharterOption();
				eva.setVesselCharter(vesselAvailability);
				vaMap.put(vesselAvailability, eva);
				row.setShipping(eva);
			} else {
				final RoundTripShippingOption eva = AnalyticsFactory.eINSTANCE.createRoundTripShippingOption();
				eva.setVessel(vesselAvailability.getVessel());
				eva.setHireCost(vesselAvailability.getTimeCharterRate());
				vaMap.put(vesselAvailability, eva);
				row.setShipping(eva);
			}
		}
	}

	private static BuyOption loadSlotToOption(final LoadSlot slot, final Map<LoadSlot, BuyOption> map) {
		if (map.containsKey(slot)) {
			return map.get(slot);
		}

		if (slot instanceof SpotLoadSlot) {
			final SpotLoadSlot spotLoadSlot = (SpotLoadSlot) slot;
			final SpotMarket market = spotLoadSlot.getMarket();
			// TODO: AnalyticsScenarioEvaluator Code currently assumes we use a market option once rather than multiple times. I.e. it will filter out combinations where the market option is used more
			// than once.

			// if (marketMap.containsKey(market)) {
			// return marketMap.get(market);
			// }
			final BuyMarket marketOption = AnalyticsFactory.eINSTANCE.createBuyMarket();
			marketOption.setMarket(market);
			map.put(slot, marketOption);

			return marketOption;
		} else {
			final BuyReference ref = AnalyticsFactory.eINSTANCE.createBuyReference();
			ref.setSlot(slot);
			map.put(slot, ref);
			return ref;
		}
	}

	private static SellOption dischargeSlotToOption(final DischargeSlot slot, final Map<DischargeSlot, SellOption> map) {
		if (map.containsKey(slot)) {
			return map.get(slot);
		}

		if (slot instanceof SpotDischargeSlot) {
			final SpotDischargeSlot spotLoadSlot = (SpotDischargeSlot) slot;
			final SpotMarket market = spotLoadSlot.getMarket();
			// TODO: AnalyticsScenarioEvaluator Code currently assumes we use a market option once rather than multiple times. I.e. it will filter out combinations where the market option is used more
			// than once.

			// if (marketMap.containsKey(market)) {
			// return marketMap.get(market);
			// }
			final SellMarket marketOption = AnalyticsFactory.eINSTANCE.createSellMarket();
			marketOption.setMarket(market);
			map.put(slot, marketOption);

			return marketOption;
		} else {
			final SellReference ref = AnalyticsFactory.eINSTANCE.createSellReference();
			ref.setSlot(slot);
			map.put(slot, ref);
			return ref;
		}
	}

	private static @Nullable LNGScenarioModel findScenarioModel(EObject obj) {
		while (obj != null && !(obj instanceof LNGScenarioModel)) {
			obj = obj.eContainer();
		}
		return (LNGScenarioModel) obj;
	}

}
