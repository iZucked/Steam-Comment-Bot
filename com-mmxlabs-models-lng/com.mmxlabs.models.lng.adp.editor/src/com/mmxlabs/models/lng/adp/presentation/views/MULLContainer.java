package com.mmxlabs.models.lng.adp.presentation.views;

import java.time.LocalDate;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.mmxlabs.common.Pair;
import com.mmxlabs.models.lng.cargo.Inventory;
import com.mmxlabs.models.lng.cargo.LoadSlot;
import com.mmxlabs.models.lng.commercial.BaseLegalEntity;
import com.mmxlabs.models.lng.commercial.SalesContract;
import com.mmxlabs.models.lng.fleet.Vessel;
import com.mmxlabs.models.lng.spotmarkets.DESSalesMarket;

public class MULLContainer {
	private Inventory inventory;
	private Map<BaseLegalEntity, Long> runningAllocation;
	private Map<BaseLegalEntity, Integer> currentMonthAbsoluteEntitlement;
	private final int fullCargoLotValue;
	private List<Pair<BaseLegalEntity, Double>> globalRelativeEntitlement;
	private Map<BaseLegalEntity, MUDContainer> mudContainers;
	
	public MULLContainer(final Map<BaseLegalEntity, Long> initialAllocations, final int fullCargoLotValue, final List<Pair<BaseLegalEntity, Double>> globalRelativeEntitlement, final Map<BaseLegalEntity, MUDContainer> mudContainers) {
		this.runningAllocation = new HashMap<>();
		initialAllocations.entrySet().stream().forEach(e -> this.runningAllocation.put(e.getKey(), e.getValue()));
		this.fullCargoLotValue = fullCargoLotValue;
		this.globalRelativeEntitlement = globalRelativeEntitlement;
		this.mudContainers = mudContainers;
		this.currentMonthAbsoluteEntitlement = new HashMap<>();
		initialAllocations.entrySet().stream().forEach(e -> this.currentMonthAbsoluteEntitlement.put(e.getKey(), e.getValue().intValue()));
	}
	
	public BaseLegalEntity calculateMull(final Map<Vessel, LocalDate> vesselToMostRecentUseDate) {
		Comparator<BaseLegalEntity> comparator = new Comparator<BaseLegalEntity>() {

			@Override
			public int compare(BaseLegalEntity arg0, BaseLegalEntity arg1) {
				final Long allocation0 = runningAllocation.get(arg0);
				final Long allocation1 = runningAllocation.get(arg1);
				final MUDContainer mud0 = mudContainers.get(arg0);
				final MUDContainer mud1 = mudContainers.get(arg1);
				final int expectedAllocationDrop0 = mud0.calculateExpectedAllocationDrop(vesselToMostRecentUseDate);
				final int expectedAllocationDrop1 = mud1.calculateExpectedAllocationDrop(vesselToMostRecentUseDate);
				final int beforeDrop0 = currentMonthAbsoluteEntitlement.get(arg0);
				final int beforeDrop1 = currentMonthAbsoluteEntitlement.get(arg1);
				final int afterDrop0 = beforeDrop0 - expectedAllocationDrop0;
				final int afterDrop1 = beforeDrop1 - expectedAllocationDrop1;
				
				final boolean belowLower0 = afterDrop0 < -fullCargoLotValue;
				final boolean belowLower1 = afterDrop1 < -fullCargoLotValue;
				final boolean aboveUpper0 = afterDrop0 > fullCargoLotValue;
				final boolean aboveUpper1 = afterDrop1 > fullCargoLotValue;
				
				if (belowLower0) {
					if (belowLower1) {
						if (afterDrop0 < afterDrop1) {
							return -1;
						} else {
							return 1;
						}
					} else {
						return -1;
					}
				} else {
					if (belowLower1) {
						return 1;
					} else {
						if (aboveUpper0) {
							if (aboveUpper1) {
								if (allocation0 > allocation1) {
									return 1;
								} else {
									return -1;
								}
							} else {
								return 1;
							}
						} else {
							if (aboveUpper1) {
								return -1;
							} else {
								if (beforeDrop0 > fullCargoLotValue) {
									if (beforeDrop1 > beforeDrop0) {
										return -1;
									} else {
										return 1;
									}
								} else {
									if (beforeDrop1 > fullCargoLotValue) {
										return -1;
									} else {
										if (allocation0 > allocation1) {
											return 1;
										} else {
											return -1;
										}
									}
								}
							}
						}
					}
				}
					
//				if (allocation0 > allocation1) {
//					if (afterDrop0 > fullCargoLotValue) {
//						return 1;
//					} else if (afterDrop0 < -fullCargoLotValue) {
//						return -1;
//					} else {
//						return 1;
//					}
//				} else if (allocation0 < allocation1) {
//					if (afterDrop1 < -fullCargoLotValue) {
//						if (afterDrop0 < afterDrop1) {
//							return -1;
//						} else {
//							return 1;
//						}
//					} else {
//						return -1;
//					}
//				} else {
//					if (afterDrop0 == afterDrop1) {
//						return 0;
//					} else if (afterDrop0 < afterDrop1) {
//						return -1;
//					} else {
//						return 1;
//					}
//				}
			}
		};
		BaseLegalEntity mull =  globalRelativeEntitlement.stream().map(Pair::getFirst).max(comparator).get();
		if (mull.getName().equalsIgnoreCase("ogg")) {
			int j = 0;
		}
		return mull;
//		return this.runningAllocation.entrySet().stream() //
//				.max((e1, e2) -> e1.getValue().compareTo(e2.getValue())).get().getKey();
	}
	
	public void updateRunningAllocation(final int volumeIn) {
		for (Pair<BaseLegalEntity, Double> p : this.globalRelativeEntitlement) {
			final Long additionalAllocation = ((Double) (volumeIn*p.getSecond())).longValue();
			this.runningAllocation.compute(p.getFirst(), (k, v) -> v + additionalAllocation);
			final BaseLegalEntity currentEntity = p.getFirst();
			mudContainers.get(currentEntity).updateRunningAllocation(additionalAllocation);
		}
	}
	
	public void updateEntityRunningAllocation(final LoadSlot loadSlot) {
		final BaseLegalEntity entity = loadSlot.getEntity();
		final Long entityAllocation = this.runningAllocation.get(entity);
		if (entityAllocation != null) {
			this.runningAllocation.put(entity, entityAllocation - loadSlot.getMaxQuantity());
		}
	}
	
	public void reverseEntityLoadSlot(final LoadSlot loadSlot) {
		final BaseLegalEntity entity = loadSlot.getEntity();
		this.runningAllocation.compute(entity, (k, v) -> v + loadSlot.getMaxQuantity());
	}
	
	public void dropEntityAllocation(final BaseLegalEntity entity, final int allocationDrop) {
		this.runningAllocation.compute(entity, (k, v) -> v- ((long) allocationDrop));
		this.currentMonthAbsoluteEntitlement.compute(entity, (k, v) -> v - allocationDrop);
	}
	
	public boolean runningAllocationEqual(final Map<BaseLegalEntity, Long> runningAllocation) {
		for (final Entry<BaseLegalEntity, Long> entry : this.runningAllocation.entrySet()) {
			final Long currentAllocation = runningAllocation.get(entry.getKey());
			if (!currentAllocation.equals(entry.getValue())) {
				return false;
			}
		}
		return true;
	}
	
	public Entry<SalesContract, Long> getMUDSalesContract(final BaseLegalEntity entity) {
		return this.mudContainers.get(entity).calculateMUDSalesContract();
	}
	
	public Entry<DESSalesMarket, Long> getMUDMarket(final BaseLegalEntity entity) {
		return this.mudContainers.get(entity).calculateMUDMarket();
	}
	
	public void dropAllocation(final BaseLegalEntity entity, final SalesContract salesContract, final Long allocationDrop) {
		this.mudContainers.get(entity).dropAllocation(salesContract, allocationDrop);
	}
	
	public void dropAllocation(final BaseLegalEntity entity, final DESSalesMarket market, final Long allocationDrop) {
		this.mudContainers.get(entity).dropAllocation(market, allocationDrop);
	}
	
	public void updateCurrentMonthAbsoluteEntitlement(final int totalMonthIn) {
		for (final Pair<BaseLegalEntity, Double> p : this.globalRelativeEntitlement) {
			// this.currentMonthAbsoluteEntitlement.put(p.getFirst(), ((Double) (totalMonthIn*p.getSecond())).intValue());
			this.currentMonthAbsoluteEntitlement.compute(p.getFirst(), (k, v) -> v + ((Double) (totalMonthIn*p.getSecond())).intValue());
		}
	}
}
