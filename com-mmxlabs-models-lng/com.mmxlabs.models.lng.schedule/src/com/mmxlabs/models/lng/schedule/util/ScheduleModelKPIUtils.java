/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2020
 * All rights reserved.
 */
package com.mmxlabs.models.lng.schedule.util;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.Nullable;

import com.google.common.collect.Sets;
import com.mmxlabs.models.lng.cargo.CharterOutEvent;
import com.mmxlabs.models.lng.cargo.LoadSlot;
import com.mmxlabs.models.lng.cargo.VesselEvent;
import com.mmxlabs.models.lng.commercial.BaseEntityBook;
import com.mmxlabs.models.lng.commercial.BaseLegalEntity;
import com.mmxlabs.models.lng.commercial.CommercialPackage;
import com.mmxlabs.models.lng.schedule.BasicSlotPNLDetails;
import com.mmxlabs.models.lng.schedule.CapacityViolationType;
import com.mmxlabs.models.lng.schedule.CapacityViolationsHolder;
import com.mmxlabs.models.lng.schedule.CargoAllocation;
import com.mmxlabs.models.lng.schedule.Cooldown;
import com.mmxlabs.models.lng.schedule.EndEvent;
import com.mmxlabs.models.lng.schedule.EntityProfitAndLoss;
import com.mmxlabs.models.lng.schedule.Event;
import com.mmxlabs.models.lng.schedule.EventGrouping;
import com.mmxlabs.models.lng.schedule.Fuel;
import com.mmxlabs.models.lng.schedule.FuelAmount;
import com.mmxlabs.models.lng.schedule.FuelQuantity;
import com.mmxlabs.models.lng.schedule.FuelUnit;
import com.mmxlabs.models.lng.schedule.FuelUsage;
import com.mmxlabs.models.lng.schedule.GeneralPNLDetails;
import com.mmxlabs.models.lng.schedule.GeneratedCharterOut;
import com.mmxlabs.models.lng.schedule.GroupProfitAndLoss;
import com.mmxlabs.models.lng.schedule.Idle;
import com.mmxlabs.models.lng.schedule.Journey;
import com.mmxlabs.models.lng.schedule.OpenSlotAllocation;
import com.mmxlabs.models.lng.schedule.OtherPNL;
import com.mmxlabs.models.lng.schedule.PortVisit;
import com.mmxlabs.models.lng.schedule.PortVisitLateness;
import com.mmxlabs.models.lng.schedule.PortVisitLatenessType;
import com.mmxlabs.models.lng.schedule.ProfitAndLossContainer;
import com.mmxlabs.models.lng.schedule.Purge;
import com.mmxlabs.models.lng.schedule.Schedule;
import com.mmxlabs.models.lng.schedule.ScheduleFactory;
import com.mmxlabs.models.lng.schedule.Sequence;
import com.mmxlabs.models.lng.schedule.SlotAllocation;
import com.mmxlabs.models.lng.schedule.SlotPNLDetails;
import com.mmxlabs.models.lng.schedule.SlotVisit;
import com.mmxlabs.models.lng.schedule.StartEvent;
import com.mmxlabs.models.lng.schedule.VesselEventVisit;

public class ScheduleModelKPIUtils {

	public static final int SHIPPING_PNL_IDX = 0;
	public static final int TRADING_PNL_IDX = 1;
	public static final int UPSTREAM_PNL_IDX = 2;
	private static final int PNL_COMPONENT_COUNT = 3;

	public static final int LATENESS_WITHOUT_FLEX_IDX = 0;
	public static final int LATENESS_WTH_FLEX_IDX = 1;
	private static final int LATENESS_COMPONENT_COUNT = 2;

	public static long @NonNull [] getScheduleProfitAndLossSplit(@NonNull final Schedule schedule) {

		long totalTradingPNL = 0L;
		long totalShippingPNL = 0L;
		long totalUpstreamPNL = 0L;

		for (final Sequence seq : schedule.getSequences()) {

			for (final Event evt : seq.getEvents()) {
				if (evt instanceof SlotVisit) {
					final SlotVisit visit = (SlotVisit) evt;

					if (visit.getSlotAllocation().getSlot() instanceof LoadSlot) {
						final CargoAllocation cargoAllocation = visit.getSlotAllocation().getCargoAllocation();
						if (cargoAllocation != null) {
							totalTradingPNL += getElementTradingPNL(cargoAllocation);
							totalShippingPNL += getElementShippingPNL(cargoAllocation);
							totalUpstreamPNL += getElementUpstreamPNL(cargoAllocation);
						}
					}

				} else if (evt instanceof ProfitAndLossContainer) {
					totalTradingPNL += getElementTradingPNL((ProfitAndLossContainer) evt);
					totalShippingPNL += getElementShippingPNL((ProfitAndLossContainer) evt);
					totalUpstreamPNL += getElementUpstreamPNL((ProfitAndLossContainer) evt);
				}
			}
		}
		// for (final MarketAllocation marketAllocation : schedule.getMarketAllocations()) {
		// totalMtMPNL += getElementTradingPNL(marketAllocation);
		// totalMtMPNL += getElementShippingPNL(marketAllocation);
		// }
		for (final OpenSlotAllocation openSlotAllocation : schedule.getOpenSlotAllocations()) {
			if (openSlotAllocation != null) {
				totalTradingPNL += getElementTradingPNL(openSlotAllocation);
				totalShippingPNL += getElementShippingPNL(openSlotAllocation);
				totalUpstreamPNL += getElementUpstreamPNL(openSlotAllocation);
			}
		}

		if (schedule.getOtherPNL() != null) {
			totalTradingPNL += getElementTradingPNL(schedule.getOtherPNL());
			totalShippingPNL += getElementShippingPNL(schedule.getOtherPNL());
			totalUpstreamPNL += getElementUpstreamPNL(schedule.getOtherPNL());
		}

		final long[] result = new long[PNL_COMPONENT_COUNT];
		result[SHIPPING_PNL_IDX] = totalShippingPNL;
		result[TRADING_PNL_IDX] = totalTradingPNL;
		result[UPSTREAM_PNL_IDX] = totalUpstreamPNL;

		return result;
	}

	public static long getScheduleProfitAndLoss(@NonNull final Schedule schedule) {

		long totalPNL = 0L;
		if (schedule == null) {
			return totalPNL;
		}
		for (final Sequence seq : schedule.getSequences()) {

			for (final Event evt : seq.getEvents()) {
				if (evt instanceof SlotVisit) {
					final SlotVisit visit = (SlotVisit) evt;

					if (visit.getSlotAllocation().getSlot() instanceof LoadSlot) {
						final CargoAllocation cargoAllocation = visit.getSlotAllocation().getCargoAllocation();
						totalPNL += getElementPNL(cargoAllocation);
					}

				} else if (evt instanceof ProfitAndLossContainer) {
					totalPNL += getElementPNL((ProfitAndLossContainer) evt);
				}
			}
		}
		for (final OpenSlotAllocation openSlotAllocation : schedule.getOpenSlotAllocations()) {
			totalPNL += getElementPNL(openSlotAllocation);
		}
		if (schedule.getOtherPNL() != null) {
			totalPNL += schedule.getOtherPNL().getGroupProfitAndLoss().getProfitAndLoss();
		}

		return totalPNL;
	}

	public static long getElementShippingPNL(final @Nullable ProfitAndLossContainer container) {
		return getElementPNL(container, CommercialPackage.Literals.BASE_LEGAL_ENTITY__SHIPPING_BOOK);
	}

	public static long getElementTradingPNL(final @Nullable ProfitAndLossContainer container) {
		return getElementPNL(container, CommercialPackage.Literals.BASE_LEGAL_ENTITY__TRADING_BOOK);
	}

	public static long getElementUpstreamPNL(final @Nullable ProfitAndLossContainer container) {
		return getElementPNL(container, CommercialPackage.Literals.BASE_LEGAL_ENTITY__UPSTREAM_BOOK);
	}

	public static long getElementPNL(final @Nullable ProfitAndLossContainer container, final @NonNull EStructuralFeature containmentFeature) {
		if (container != null) {
			final GroupProfitAndLoss groupProfitAndLoss = container.getGroupProfitAndLoss();
			if (groupProfitAndLoss != null) {
				long totalPNL = 0L;
				for (final EntityProfitAndLoss entityPNL : groupProfitAndLoss.getEntityProfitAndLosses()) {
					final BaseEntityBook entityBook = entityPNL.getEntityBook();
					if (entityBook == null) {
						// Fall back code path for old models.
						if (containmentFeature == CommercialPackage.Literals.BASE_LEGAL_ENTITY__TRADING_BOOK) {
							return groupProfitAndLoss.getProfitAndLoss();
						} else {
							return 0L;
						}
					} else {
						if (entityBook.eContainmentFeature() == containmentFeature) {
							totalPNL += entityPNL.getProfitAndLoss();
						}
					}
				}
				return totalPNL;
			}
		}
		return 0L;
	}

	public static long getElementPNL(final @NonNull ProfitAndLossContainer container) {

		final GroupProfitAndLoss groupProfitAndLoss = container.getGroupProfitAndLoss();
		if (groupProfitAndLoss != null) {
			return groupProfitAndLoss.getProfitAndLoss();
		}
		return 0L;
	}

	public static int[] getScheduleLateness(@NonNull final Schedule schedule) {
		int totalLatenessHoursIncludingFlex = 0;
		int totalLatenessHoursExcludingFlex = 0;

		for (final Sequence seq : schedule.getSequences()) {
			for (final Event evt : seq.getEvents()) {
				if (evt instanceof PortVisit) {

					final long latenessInHours = LatenessUtils.getLatenessInHours((PortVisit) evt);
					if (LatenessUtils.isLateExcludingFlex(evt)) {
						// Ensure positive
						totalLatenessHoursExcludingFlex += Math.abs(latenessInHours);
					}
					if (LatenessUtils.isLateAfterFlex(evt)) {
						// Ensure positive
						totalLatenessHoursIncludingFlex += Math.abs(latenessInHours);
					}
				}
			}
		}
		if (schedule.getOtherPNL() != null) {

			final long latenessInHours = LatenessUtils.getLatenessInHours(schedule.getOtherPNL());
			if (LatenessUtils.isLateExcludingFlex(schedule.getOtherPNL())) {
				// Ensure positive
				totalLatenessHoursExcludingFlex += Math.abs(latenessInHours);
			}
		}
		final int[] result = new int[LATENESS_COMPONENT_COUNT];
		result[LATENESS_WTH_FLEX_IDX] = totalLatenessHoursIncludingFlex;
		result[LATENESS_WITHOUT_FLEX_IDX] = totalLatenessHoursExcludingFlex;

		return result;
	}

	public static int getScheduleViolationCount(@NonNull final Schedule schedule) {

		int totalCapacityViolationCount = 0;
		for (final Sequence seq : schedule.getSequences()) {
			for (final Event evt : seq.getEvents()) {
				if (evt instanceof CapacityViolationsHolder) {
					final CapacityViolationsHolder capacityViolationsHolder = (CapacityViolationsHolder) evt;
					totalCapacityViolationCount += capacityViolationsHolder.getViolations().size();
				}
			}

		}
		return totalCapacityViolationCount;
	}

	public static int getNominalChartersCount(@NonNull final Schedule schedule) {

		int totalNominalChartersCount = 0;
		for (final Sequence seq : schedule.getSequences()) {
			if (seq.isSetSpotIndex() && seq.getSpotIndex() == -1)
				totalNominalChartersCount++;
		}
		return totalNominalChartersCount;
	}

	public static long getGroupProfitAndLoss(@Nullable final ProfitAndLossContainer profitAndLossContainer) {
		if (profitAndLossContainer == null) {
			return 0L;
		}

		final GroupProfitAndLoss groupProfitAndLoss = profitAndLossContainer.getGroupProfitAndLoss();
		if (groupProfitAndLoss == null) {
			return 0L;
		}
		return groupProfitAndLoss.getProfitAndLoss();
	}

	public static long getGroupPreTaxProfitAndLoss(@Nullable final ProfitAndLossContainer profitAndLossContainer) {
		if (profitAndLossContainer == null) {
			return 0L;
		}

		final GroupProfitAndLoss groupProfitAndLoss = profitAndLossContainer.getGroupProfitAndLoss();
		if (groupProfitAndLoss == null) {
			return 0L;
		}
		return groupProfitAndLoss.getProfitAndLossPreTax();
	}

	public static long getAdditionalProfitAndLoss(@Nullable final ProfitAndLossContainer profitAndLossContainer) {
		long addnPNL = 0;
		if (profitAndLossContainer != null) {
			for (final GeneralPNLDetails generalPNLDetails : profitAndLossContainer.getGeneralPNLDetails()) {
				if (generalPNLDetails instanceof SlotPNLDetails) {
					final SlotPNLDetails slotPNLDetails = (SlotPNLDetails) generalPNLDetails;
					for (final GeneralPNLDetails details : slotPNLDetails.getGeneralPNLDetails()) {
						if (details instanceof BasicSlotPNLDetails) {
							addnPNL += ((BasicSlotPNLDetails) details).getAdditionalPNL();
						}
					}
				}
			}
		}
		return addnPNL;
	}

	public static long getCancellationFees(@Nullable final ProfitAndLossContainer profitAndLossContainer) {
		long cancellationFees = 0;
		if (profitAndLossContainer != null) {
			for (final GeneralPNLDetails generalPNLDetails : profitAndLossContainer.getGeneralPNLDetails()) {
				if (generalPNLDetails instanceof SlotPNLDetails) {
					final SlotPNLDetails slotPNLDetails = (SlotPNLDetails) generalPNLDetails;
					for (final GeneralPNLDetails details : slotPNLDetails.getGeneralPNLDetails()) {
						if (details instanceof BasicSlotPNLDetails) {
							cancellationFees += ((BasicSlotPNLDetails) details).getCancellationFees();
						}
					}
				}
			}
		}
		return cancellationFees;
	}

	/**
	 * Returns the value of the misc costs - typically a negative number!
	 * 
	 * @param profitAndLossContainer
	 * @return
	 */
	public static long getMiscCostsValue(@Nullable final ProfitAndLossContainer profitAndLossContainer) {
		long miscCosts = 0;
		if (profitAndLossContainer != null) {
			for (final GeneralPNLDetails generalPNLDetails : profitAndLossContainer.getGeneralPNLDetails()) {
				if (generalPNLDetails instanceof SlotPNLDetails) {
					final SlotPNLDetails slotPNLDetails = (SlotPNLDetails) generalPNLDetails;
					for (final GeneralPNLDetails details : slotPNLDetails.getGeneralPNLDetails()) {
						if (details instanceof BasicSlotPNLDetails) {
							miscCosts += ((BasicSlotPNLDetails) details).getMiscCostsValue();
						}
					}
				}
			}
		}
		return miscCosts;
	}

	public static long getAdditionalShippingProfitAndLoss(@Nullable final ProfitAndLossContainer profitAndLossContainer) {
		long addnPNL = 0;
		if (profitAndLossContainer != null) {
			for (final GeneralPNLDetails generalPNLDetails : profitAndLossContainer.getGeneralPNLDetails()) {
				if (generalPNLDetails instanceof SlotPNLDetails) {
					final SlotPNLDetails slotPNLDetails = (SlotPNLDetails) generalPNLDetails;
					for (final GeneralPNLDetails details : slotPNLDetails.getGeneralPNLDetails()) {
						if (details instanceof BasicSlotPNLDetails) {
							addnPNL += ((BasicSlotPNLDetails) details).getExtraShippingPNL();
						}
					}
				}
			}
		}
		return addnPNL;
	}

	public static long getEntityProfitAndLoss(@Nullable final ProfitAndLossContainer profitAndLossContainer, final BaseLegalEntity targetEntity) {
		long pnl = 0L;
		if (profitAndLossContainer != null) {
			final GroupProfitAndLoss groupPnL = profitAndLossContainer.getGroupProfitAndLoss();
			for (final EntityProfitAndLoss entityPnL : groupPnL.getEntityProfitAndLosses()) {
				final BaseLegalEntity entity = entityPnL.getEntity();
				if (entity == targetEntity) {
					pnl += entityPnL.getProfitAndLoss();
				}
			}
		}
		return pnl;
	}

	public static long getAdditionalUpsideProfitAndLoss(@Nullable final ProfitAndLossContainer profitAndLossContainer) {
		long addnPNL = 0;
		if (profitAndLossContainer != null) {
			for (final GeneralPNLDetails generalPNLDetails : profitAndLossContainer.getGeneralPNLDetails()) {
				if (generalPNLDetails instanceof SlotPNLDetails) {
					final SlotPNLDetails slotPNLDetails = (SlotPNLDetails) generalPNLDetails;
					for (final GeneralPNLDetails details : slotPNLDetails.getGeneralPNLDetails()) {
						if (details instanceof BasicSlotPNLDetails) {
							addnPNL += ((BasicSlotPNLDetails) details).getExtraUpsidePNL();
						}
					}
				}
			}
		}
		return addnPNL;
	}

	public static long getCapacityViolationCount(@Nullable final EventGrouping eventGrouping) {
		long violations = 0;
		if (eventGrouping != null) {
			for (final Event evt : eventGrouping.getEvents()) {
				if (evt instanceof SlotVisit) {
					if (evt instanceof CapacityViolationsHolder) {
						final CapacityViolationsHolder capacityViolationsHolder = (CapacityViolationsHolder) evt;
						violations += capacityViolationsHolder.getViolations().size();
					}
				}
			}
		}
		return violations;
	}

	public static Set<CapacityViolationType> getCapacityViolations(@Nullable final EventGrouping eventGrouping) {
		final Set<CapacityViolationType> violations = new HashSet<>();
		if (eventGrouping != null) {
			for (final Event evt : eventGrouping.getEvents()) {
				if (evt instanceof SlotVisit) {
					if (evt instanceof CapacityViolationsHolder) {
						final CapacityViolationsHolder capacityViolationsHolder = (CapacityViolationsHolder) evt;
						violations.addAll(capacityViolationsHolder.getViolations().keySet());
					}
				}
			}
		}
		return violations;
	}

	public static Integer calculateLegCost(final Object object, final EStructuralFeature cargoAllocationRef, final EStructuralFeature allocationRef) {
		if (object instanceof EObject) {
			final EObject eObject = (EObject) object;
			final CargoAllocation cargoAllocation = (CargoAllocation) eObject.eGet(cargoAllocationRef);
			final SlotAllocation allocation = (SlotAllocation) eObject.eGet(allocationRef);
			return calculateLegCost(cargoAllocation, allocation);

		}
		return null;
	}

	public static Integer calculateLegCost(final CargoAllocation cargoAllocation, final SlotAllocation allocation) {
		if (allocation != null && cargoAllocation != null) {

			boolean collecting = false;
			int total = 0;
			for (final Event event : cargoAllocation.getEvents()) {
				if (event instanceof SlotVisit) {
					final SlotVisit slotVisit = (SlotVisit) event;
					if (allocation.getSlotVisit() == event) {
						collecting = true;
					} else {
						if (collecting) {
							// Finished!
							break;
						}
					}
					if (collecting) {
						total += slotVisit.getFuelCost();
						total += slotVisit.getCharterCost();
						total += slotVisit.getPortCost();
					}

				} else if (event instanceof Journey) {
					final Journey journey = (Journey) event;
					if (collecting) {
						total += journey.getFuelCost();
						total += journey.getCharterCost();
						total += journey.getToll();
					}
				} else if (event instanceof Idle) {
					final Idle idle = (Idle) event;
					if (collecting) {
						total += idle.getFuelCost();
						total += idle.getCharterCost();
					}
				}
			}

			return total;
		}
		return null;
	}

	public static Integer calculateLegFuel(final Object object, final EStructuralFeature cargoAllocationRef, final EStructuralFeature allocationRef, final ShippingCostType shippingCostType,
			@NonNull final TotalType totalType) {
		if (object instanceof EObject) {
			final EObject eObject = (EObject) object;
			final CargoAllocation cargoAllocation = (CargoAllocation) eObject.eGet(cargoAllocationRef);
			final SlotAllocation allocation = (SlotAllocation) eObject.eGet(allocationRef);
			return calculateLegFuel(cargoAllocation, allocation, shippingCostType, totalType);

		}
		return null;
	}

	private static Integer calculateLegFuel(final CargoAllocation cargoAllocation, final SlotAllocation allocation, final ShippingCostType shippingCostType, @NonNull final TotalType totalType) {
		if (allocation != null && cargoAllocation != null) {

			boolean collecting = false;
			int total = 0;
			for (final Event event : cargoAllocation.getEvents()) {
				if (event instanceof SlotVisit) {
					final SlotVisit slotVisit = (SlotVisit) event;
					if (allocation.getSlotVisit() == event) {
						collecting = true;
					} else {
						if (collecting) {
							// Finished!
							break;
						}
					}
					if (collecting) {
						if (shippingCostType == ShippingCostType.LNG_COSTS) {
							total += getFuelDetails(slotVisit, totalType, Fuel.NBO, Fuel.FBO);
						}
						if (shippingCostType == ShippingCostType.BUNKER_COSTS) {
							total += getFuelDetails(slotVisit, totalType, Fuel.BASE_FUEL, Fuel.PILOT_LIGHT);
						}

					}

				} else if (event instanceof Journey) {
					final Journey journey = (Journey) event;
					if (collecting) {
						if (shippingCostType == ShippingCostType.LNG_COSTS) {
							total += getFuelDetails(journey, totalType, Fuel.NBO, Fuel.FBO);
						}
						if (shippingCostType == ShippingCostType.BUNKER_COSTS) {
							total += getFuelDetails(journey, totalType, Fuel.BASE_FUEL, Fuel.PILOT_LIGHT);
						}
					}
				} else if (event instanceof Idle) {
					final Idle idle = (Idle) event;
					if (collecting) {
						if (shippingCostType == ShippingCostType.LNG_COSTS) {
							total += getFuelDetails(idle, totalType, Fuel.NBO, Fuel.FBO);
						}
						if (shippingCostType == ShippingCostType.BUNKER_COSTS) {
							total += getFuelDetails(idle, totalType, Fuel.BASE_FUEL, Fuel.PILOT_LIGHT);
						}
					}
				}
			}
			return total;
		}
		return null;
	}

	public static Double calculateLegSpeed(final Object object, final EStructuralFeature cargoAllocationRef, final EStructuralFeature allocationRef) {
		if (object instanceof EObject) {
			final EObject eObject = (EObject) object;
			final CargoAllocation cargoAllocation = (CargoAllocation) eObject.eGet(cargoAllocationRef);
			final SlotAllocation allocation = (SlotAllocation) eObject.eGet(allocationRef);
			return calculateLegSpeed(cargoAllocation, allocation);

		}
		return null;
	}

	private static Double calculateLegSpeed(final CargoAllocation cargoAllocation, final SlotAllocation allocation) {
		if (allocation != null && cargoAllocation != null) {

			boolean collecting = false;
			final int total = 0;
			for (final Event event : cargoAllocation.getEvents()) {
				if (event instanceof SlotVisit) {
					final SlotVisit slotVisit = (SlotVisit) event;
					if (allocation.getSlotVisit() == event) {
						collecting = true;
					} else {
						if (collecting) {
							// Finished!
							break;
						}
					}
				} else if (event instanceof Journey) {
					final Journey journey = (Journey) event;
					if (collecting) {
						return journey.getSpeed();
					}
				}
			}
		}
		return null;
	}

	public enum ShippingCostType {
		ALL, LNG_COSTS, PORT_COSTS, BUNKER_COSTS, CANAL_COSTS, COOLDOWN_COSTS, PURGE_COSTS, HIRE_COSTS, OTHER_COSTS, HEEL_COST, HEEL_REVENUE, //
		HOURS // Not included in all
	}

	public enum TotalType {
		QUANTITY_M3, QUANTITY_MMBTU, QUANTITY_MT, COST
	}

	public static Integer getEventBallastBonus(final @Nullable EventGrouping grouping) {
		int total = 0;
		if (grouping != null) {
			for (final Event event : grouping.getEvents()) {
				if (event instanceof StartEvent) {
					total += ((StartEvent) event).getRepositioningFee();
				}
				if (event instanceof EndEvent) {
					total += ((EndEvent) event).getBallastBonusFee();
				}
			}
		}
		return total;
	}

	public static Long calculateEventShippingCost(final @Nullable EventGrouping grouping, final boolean includeAllLNG, final boolean includeRevenue, final ShippingCostType costType) {
		long hours = 0;
		if (grouping != null) {
			boolean priceBOG = false;
			if (grouping instanceof CargoAllocation) {
				priceBOG = includeAllLNG;
			}
			// boolean collecting = false;
			long total = 0L;
			for (final Event event : grouping.getEvents()) {
				hours += event.getDuration();
				if (costType == ShippingCostType.ALL || costType == ShippingCostType.HIRE_COSTS) {
					total += event.getCharterCost();
				}
				if (event instanceof StartEvent) {
					if (costType == ShippingCostType.ALL || costType == ShippingCostType.OTHER_COSTS) {
						total += ((StartEvent) event).getRepositioningFee();
					}
				}
				if (event instanceof EndEvent) {
					if (costType == ShippingCostType.ALL || costType == ShippingCostType.OTHER_COSTS) {
						total += ((EndEvent) event).getBallastBonusFee();
					}
				}

				if (event instanceof Journey) {
					final Journey journey = (Journey) event;
					if (costType == ShippingCostType.ALL || costType == ShippingCostType.CANAL_COSTS) {
						total += journey.getToll();
					}
				} else if (event instanceof Cooldown) {
					final Cooldown cooldown = (Cooldown) event;

					if (costType == ShippingCostType.ALL || costType == ShippingCostType.COOLDOWN_COSTS) {
						total += cooldown.getCost();
					}
				} else if (event instanceof Purge) {
					final Purge purge = (Purge) event;

					if (costType == ShippingCostType.ALL || costType == ShippingCostType.PURGE_COSTS) {
						total += purge.getCost();
					}
				} else if (event instanceof GeneratedCharterOut) {
					final GeneratedCharterOut generatedCharterOut = (GeneratedCharterOut) event;
					if (includeRevenue) {
						if (costType == ShippingCostType.ALL || costType == ShippingCostType.OTHER_COSTS) {
							total -= generatedCharterOut.getRevenue();
						}
					}
				} else if (event instanceof VesselEventVisit) {
					final VesselEventVisit vesselEventVisit = (VesselEventVisit) event;

					final VesselEvent vesselEvent = vesselEventVisit.getVesselEvent();
					if (vesselEvent instanceof CharterOutEvent) {
						final CharterOutEvent charterOutEvent = (CharterOutEvent) vesselEvent;
						if (costType == ShippingCostType.ALL || costType == ShippingCostType.OTHER_COSTS) {
							total += charterOutEvent.getRepositioningFee();
							total += charterOutEvent.getBallastBonus();
							if (includeRevenue) {
								total -= charterOutEvent.getDurationInDays() * charterOutEvent.getHireRate();
							}
						}
					}
				}

				if (event instanceof PortVisit) {
					final PortVisit portVisit = (PortVisit) event;
					if (costType == ShippingCostType.ALL || costType == ShippingCostType.PORT_COSTS) {
						total += portVisit.getPortCost();
					}
					if (costType == ShippingCostType.ALL || costType == ShippingCostType.HEEL_COST) {
						total += portVisit.getHeelCost();
					}
					if (costType == ShippingCostType.ALL || costType == ShippingCostType.HEEL_REVENUE) {
						total -= portVisit.getHeelRevenue();
					}
				}

				if (event instanceof FuelUsage) {
					final FuelUsage fuelUsage = (FuelUsage) event;
					if (costType == ShippingCostType.ALL || costType == ShippingCostType.BUNKER_COSTS) {
						total += getFuelCost(fuelUsage, Fuel.BASE_FUEL, Fuel.PILOT_LIGHT);
					}
					if (costType == ShippingCostType.ALL || costType == ShippingCostType.LNG_COSTS) {
						// // Start event and charter out events pay for LNG use
						if (priceBOG //
						// || grouping instanceof StartEvent //
						// || grouping instanceof EndEvent //
						// || grouping instanceof VesselEventVisit //
						// || grouping instanceof GeneratedCharterOut //
						) {
							total += getFuelCost(fuelUsage, Fuel.NBO, Fuel.FBO);
						}
					}
				}
			}
			if (costType == ShippingCostType.HOURS) {
				return hours;
			}
			return total;

		}
		return null;

	}

	public static long getFuelCost(final FuelUsage fuelUser, final Fuel... fuels) {
		final Set<Fuel> fuelsOfInterest = Sets.newHashSet(fuels);
		long sum = 0L;
		if (fuelUser != null) {
			final List<FuelQuantity> fuelQuantities = fuelUser.getFuels();
			for (final FuelQuantity fq : fuelQuantities) {
				if (fuelsOfInterest.contains(fq.getFuel())) {
					sum += fq.getCost();
				}
			}
		}
		return sum;
	}

	protected static long getFuelDetails(final FuelUsage fuelUser, final TotalType totalType, final Fuel... fuels) {
		final Set<Fuel> fuelsOfInterest = Sets.newHashSet(fuels);
		long sum = 0L;
		if (fuelUser != null) {
			final List<FuelQuantity> fuelQuantities = fuelUser.getFuels();
			for (final FuelQuantity fq : fuelQuantities) {
				if (fuelsOfInterest.contains(fq.getFuel())) {
					if (totalType == TotalType.COST) {
						sum += fq.getCost();
					} else if (totalType == TotalType.QUANTITY_M3) {
						final Optional<FuelAmount> quantity = fq.getAmounts().stream().filter(a -> a.getUnit() == FuelUnit.M3).findFirst();
						if (quantity.isPresent()) {
							sum += quantity.get().getQuantity();
						}
					} else if (totalType == TotalType.QUANTITY_MMBTU) {
						final Optional<FuelAmount> quantity = fq.getAmounts().stream().filter(a -> a.getUnit() == FuelUnit.MMBTU).findFirst();
						if (quantity.isPresent()) {
							sum += quantity.get().getQuantity();
						}
					} else if (totalType == TotalType.QUANTITY_MT) {
						final Optional<FuelAmount> quantity = fq.getAmounts().stream().filter(a -> a.getUnit() == FuelUnit.MMBTU).findFirst();
						if (quantity.isPresent()) {
							sum += quantity.get().getQuantity();
						}
					}
				}
			}
		}
		return sum;
	}

	public static long getTotalShippingCost(@NonNull final EventGrouping eventGrouping) {
		return calculateEventShippingCost(eventGrouping, false, false, ShippingCostType.ALL);
	}

	public static long getTotalShippingCost(@NonNull final CargoAllocation cargoAllocation) {
		long shippingCost = 0;
		for (final SlotAllocation slotAllocation : cargoAllocation.getSlotAllocations()) {
			shippingCost += calculateLegCost(cargoAllocation, slotAllocation);
		}
		return shippingCost;
	}

	public enum Mode {
		INCREMENT, DECREMENT
	}

	private static void updateOtherPNLInternal(final GroupProfitAndLoss target, final ProfitAndLossContainer source, final Mode mode) {
		// Pre-tax value
		{
			final GroupProfitAndLoss sourceG = source.getGroupProfitAndLoss();
			if (sourceG != null) {
				long value = target.getProfitAndLossPreTax();
				value += (mode == Mode.INCREMENT) ? sourceG.getProfitAndLossPreTax() : -sourceG.getProfitAndLossPreTax();
				target.setProfitAndLossPreTax(value);
			}
		}
		// Post-tax value

		{
			final GroupProfitAndLoss sourceG = source.getGroupProfitAndLoss();
			long value = target.getProfitAndLoss();
			value += (mode == Mode.INCREMENT) ? sourceG.getProfitAndLoss() : -sourceG.getProfitAndLoss();
			target.setProfitAndLoss(value);
		}
		{
			final GroupProfitAndLoss sourceG = source.getGroupProfitAndLoss();
			for (final EntityProfitAndLoss sourcePL : sourceG.getEntityProfitAndLosses()) {
				EntityProfitAndLoss targetPL = null;
				// Find existing match...
				for (final EntityProfitAndLoss e : target.getEntityProfitAndLosses()) {
					if (e.getEntity() == sourcePL.getEntity()) {
						if (e.getEntityBook() == sourcePL.getEntityBook()) {
							targetPL = e;
							break;
						}
					}
				}
				// ...or create new one
				if (targetPL == null) {
					targetPL = ScheduleFactory.eINSTANCE.createEntityProfitAndLoss();
					targetPL.setEntity(sourcePL.getEntity());
					targetPL.setEntityBook(sourcePL.getEntityBook());
					target.getEntityProfitAndLosses().add(targetPL);
				}

				// Update pre-tax counter
				{
					long value = targetPL.getProfitAndLossPreTax();
					value += (mode == Mode.INCREMENT) ? sourcePL.getProfitAndLossPreTax() : -sourcePL.getProfitAndLossPreTax();
					targetPL.setProfitAndLossPreTax(value);
				}
				// Update post-tax value
				{
					long value = targetPL.getProfitAndLoss();
					value += (mode == Mode.INCREMENT) ? sourcePL.getProfitAndLoss() : -sourcePL.getProfitAndLoss();
					targetPL.setProfitAndLoss(value);
				}
			}
		}
	}

	public static void updateOtherPNL(final OtherPNL otherPNL, final Schedule schedule, final Mode mode) {

		GroupProfitAndLoss groupProfitAndLoss = otherPNL.getGroupProfitAndLoss();
		if (groupProfitAndLoss == null) {
			groupProfitAndLoss = ScheduleFactory.eINSTANCE.createGroupProfitAndLoss();
			otherPNL.setGroupProfitAndLoss(groupProfitAndLoss);
		}

		for (final Sequence seq : schedule.getSequences()) {

			for (final Event evt : seq.getEvents()) {
				if (evt instanceof SlotVisit) {
					final SlotVisit visit = (SlotVisit) evt;

					if (visit.getSlotAllocation().getSlot() instanceof LoadSlot) {
						final CargoAllocation cargoAllocation = visit.getSlotAllocation().getCargoAllocation();
						if (cargoAllocation != null) {
							updateOtherPNLInternal(groupProfitAndLoss, cargoAllocation, mode);
						}
					}
				} else if (evt instanceof ProfitAndLossContainer) {
					updateOtherPNLInternal(groupProfitAndLoss, (ProfitAndLossContainer) evt, mode);
				}

				if (evt instanceof PortVisit) {
					final PortVisit visit = (PortVisit) evt;
					if (visit.getLateness() != null) {
						final PortVisitLateness visitLateness = visit.getLateness();
						if (otherPNL.getLateness() == null) {
							otherPNL.setLateness(ScheduleFactory.eINSTANCE.createPortVisitLateness());
							otherPNL.getLateness().setType(PortVisitLatenessType.BEYOND);
						}
						final PortVisitLateness otherLateness = otherPNL.getLateness();
						// Set lateness type to highest priority.
						otherLateness.setType(PortVisitLatenessType.values()[Math.min(visitLateness.getType().ordinal(), otherLateness.getType().ordinal())]);
						final int latenessInHours = visitLateness.getLatenessInHours();
						if (mode == Mode.INCREMENT) {
							final int total = otherLateness.getLatenessInHours() + latenessInHours;
							otherLateness.setLatenessInHours(total);
						} else {
							final int total = otherLateness.getLatenessInHours() - latenessInHours;
							otherLateness.setLatenessInHours(total);
						}

					}
				}
			}
		}
		for (final OpenSlotAllocation openSlotAllocation : schedule.getOpenSlotAllocations()) {
			if (openSlotAllocation != null) {
				updateOtherPNLInternal(groupProfitAndLoss, openSlotAllocation, mode);
			}
		}
	}
}
