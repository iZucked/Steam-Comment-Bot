/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
package com.mmxlabs.models.lng.schedule.util;

import java.util.List;
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
import com.mmxlabs.models.lng.commercial.CommercialPackage;
import com.mmxlabs.models.lng.schedule.BasicSlotPNLDetails;
import com.mmxlabs.models.lng.schedule.CapacityViolationsHolder;
import com.mmxlabs.models.lng.schedule.CargoAllocation;
import com.mmxlabs.models.lng.schedule.Cooldown;
import com.mmxlabs.models.lng.schedule.EndEvent;
import com.mmxlabs.models.lng.schedule.EntityProfitAndLoss;
import com.mmxlabs.models.lng.schedule.Event;
import com.mmxlabs.models.lng.schedule.EventGrouping;
import com.mmxlabs.models.lng.schedule.Fuel;
import com.mmxlabs.models.lng.schedule.FuelQuantity;
import com.mmxlabs.models.lng.schedule.FuelUsage;
import com.mmxlabs.models.lng.schedule.GeneralPNLDetails;
import com.mmxlabs.models.lng.schedule.GeneratedCharterOut;
import com.mmxlabs.models.lng.schedule.GroupProfitAndLoss;
import com.mmxlabs.models.lng.schedule.Idle;
import com.mmxlabs.models.lng.schedule.Journey;
import com.mmxlabs.models.lng.schedule.OpenSlotAllocation;
import com.mmxlabs.models.lng.schedule.PortVisit;
import com.mmxlabs.models.lng.schedule.ProfitAndLossContainer;
import com.mmxlabs.models.lng.schedule.Schedule;
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
				totalShippingPNL += getElementUpstreamPNL(openSlotAllocation);
			}
		}

		final long[] result = new long[PNL_COMPONENT_COUNT];
		result[SHIPPING_PNL_IDX] = totalShippingPNL;
		result[TRADING_PNL_IDX] = totalTradingPNL;
		result[UPSTREAM_PNL_IDX] = totalUpstreamPNL;

		return result;
	}

	public static long getScheduleProfitAndLoss(@NonNull final Schedule schedule) {

		long totalPNL = 0L;

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

	public static long getHedgeValue(@Nullable final ProfitAndLossContainer profitAndLossContainer) {
		long hedgeValue = 0;
		if (profitAndLossContainer != null) {
			for (final GeneralPNLDetails generalPNLDetails : profitAndLossContainer.getGeneralPNLDetails()) {
				if (generalPNLDetails instanceof SlotPNLDetails) {
					final SlotPNLDetails slotPNLDetails = (SlotPNLDetails) generalPNLDetails;
					for (final GeneralPNLDetails details : slotPNLDetails.getGeneralPNLDetails()) {
						if (details instanceof BasicSlotPNLDetails) {
							hedgeValue += ((BasicSlotPNLDetails) details).getHedgingValue();
						}
					}
				}
			}
		}
		return hedgeValue;
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

	public static Long calculateEventShippingCost(final @Nullable EventGrouping grouping, final boolean includeAllLNG, final boolean includeRevenue) {
		if (grouping != null) {

			// boolean collecting = false;
			long total = 0L;
			for (final Event event : grouping.getEvents()) {
				if (event instanceof SlotVisit) {
					final PortVisit slotVisit = (PortVisit) event;
					total += slotVisit.getCharterCost();
					total += slotVisit.getPortCost();
				} else if (event instanceof Journey) {
					final Journey journey = (Journey) event;
					total += journey.getCharterCost();
					total += journey.getToll();
				} else if (event instanceof Idle) {
					final Idle idle = (Idle) event;
					total += idle.getCharterCost();
				} else if (event instanceof Cooldown) {
					final Cooldown cooldown = (Cooldown) event;
					total += cooldown.getCharterCost();
					total += cooldown.getCost();
				} else if (event instanceof GeneratedCharterOut) {
					final GeneratedCharterOut generatedCharterOut = (GeneratedCharterOut) event;
					total += generatedCharterOut.getCharterCost();
					if (includeRevenue) {
						total -= generatedCharterOut.getRevenue();
					}
				} else if (event instanceof VesselEventVisit) {
					final VesselEventVisit vesselEventVisit = (VesselEventVisit) event;
					total += vesselEventVisit.getPortCost();
					total += vesselEventVisit.getCharterCost();
					final VesselEvent vesselEvent = vesselEventVisit.getVesselEvent();
					if (vesselEvent instanceof CharterOutEvent) {
						final CharterOutEvent charterOutEvent = (CharterOutEvent) vesselEvent;
						total += charterOutEvent.getRepositioningFee();
						if (includeRevenue) {
							total -= charterOutEvent.getDurationInDays() * charterOutEvent.getHireRate();
						}

					}
				} else if (event instanceof PortVisit) {
					final PortVisit portVisit = (PortVisit) event;
					total += portVisit.getCharterCost();
					total += portVisit.getPortCost();
				}
				if (event instanceof FuelUsage) {
					final FuelUsage fuelUsage = (FuelUsage) event;
					total += getFuelCost(fuelUsage, Fuel.BASE_FUEL, Fuel.PILOT_LIGHT);
					// Start event and charter out events pay for LNG use
					if (includeAllLNG //
							|| grouping instanceof StartEvent //
							|| grouping instanceof EndEvent //
							|| grouping instanceof VesselEventVisit //
							|| grouping instanceof GeneratedCharterOut //
					) {
						total += getFuelCost(fuelUsage, Fuel.NBO, Fuel.FBO);
					}
				}
			}

			return total;

		}
		return null;

	}

	protected static long getFuelCost(final FuelUsage fuelUser, final Fuel... fuels) {
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

	public static long getTotalShippingCost(@NonNull final EventGrouping eventGrouping) {
		return calculateEventShippingCost(eventGrouping, false, false);
	}

	public static long getTotalShippingCost(@NonNull final CargoAllocation cargoAllocation) {
		long shippingCost = 0;
		for (final SlotAllocation slotAllocation : cargoAllocation.getSlotAllocations()) {
			shippingCost += calculateLegCost(cargoAllocation, slotAllocation);
		}
		return shippingCost;
	}
}
