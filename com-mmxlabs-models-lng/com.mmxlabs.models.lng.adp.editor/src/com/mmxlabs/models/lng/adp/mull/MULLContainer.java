/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2022
 * All rights reserved.
 */
package com.mmxlabs.models.lng.adp.mull;

import java.time.LocalDateTime;
import java.time.YearMonth;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import com.mmxlabs.models.lng.adp.MullEntityRow;
import com.mmxlabs.models.lng.adp.MullSubprofile;
import com.mmxlabs.models.lng.cargo.Cargo;
import com.mmxlabs.models.lng.cargo.Inventory;
import com.mmxlabs.models.lng.fleet.Vessel;

public class MULLContainer {
	private Inventory inventory;
	private final int fullCargoLotValue;

	private final List<MUDContainer> mudContainerList;

	public MULLContainer(final MullSubprofile subprofile, final int fullCargoLotValue) {
		this.inventory = subprofile.getInventory();
		this.fullCargoLotValue = fullCargoLotValue;
		final double totalWeight = subprofile.getEntityTable().stream().mapToDouble(MullEntityRow::getRelativeEntitlement).sum();
		mudContainerList = subprofile.getEntityTable().stream().filter(row -> row.getRelativeEntitlement() > 0.0).map(row -> new MUDContainer(row, totalWeight)).collect(Collectors.toList());
	}

	public MUDContainer calculateMULL(final Map<Vessel, LocalDateTime> vesselToMostRecentUseDateTime, final Map<Vessel, LocalDateTime> vesselToNextForwardUseTime, final int defaultAllocationDrop,
			final Set<Vessel> firstPartyVessels, final LocalDateTime currentDateTime) {
		return this.mudContainerList.stream().max((Comparator<MUDContainer>) (mc0, mc1) -> {
			final Long allocation0 = mc0.getRunningAllocation();
			final Long allocation1 = mc1.getRunningAllocation();
			final int expectedAllocationDrop0 = mc0.calculateExpectedAllocationDrop(vesselToMostRecentUseDateTime, vesselToNextForwardUseTime, defaultAllocationDrop,
					inventory.getPort().getLoadDuration(), firstPartyVessels, currentDateTime);
			final int expectedAllocationDrop1 = mc1.calculateExpectedAllocationDrop(vesselToMostRecentUseDateTime, vesselToNextForwardUseTime, defaultAllocationDrop,
					inventory.getPort().getLoadDuration(), firstPartyVessels, currentDateTime);
			final int beforeDrop0 = mc0.getCurrentMonthAbsoluteEntitlement();
			final int beforeDrop1 = mc1.getCurrentMonthAbsoluteEntitlement();
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
		}).get();
	}

	public MUDContainer phase1CalculateMULL(final Map<Vessel, LocalDateTime> vesselToMostRecentUseDateTime, final Map<Vessel, LocalDateTime> vesselToNextForwardUseTime,
			final int defaultAllocationDrop, final Set<Vessel> firstPartyVessels, final LocalDateTime currentDateTime) {
		return this.mudContainerList.stream().max((Comparator<MUDContainer>) (mc0, mc1) -> {
			final Long allocation0 = mc0.getRunningAllocation();
			final Long allocation1 = mc1.getRunningAllocation();
			final int expectedAllocationDrop0 = mc0.phase1CalculateExpectedAllocationDrop(vesselToMostRecentUseDateTime, vesselToNextForwardUseTime, defaultAllocationDrop,
					inventory.getPort().getLoadDuration(), firstPartyVessels, currentDateTime);
			final int expectedAllocationDrop1 = mc1.phase1CalculateExpectedAllocationDrop(vesselToMostRecentUseDateTime, vesselToNextForwardUseTime, defaultAllocationDrop,
					inventory.getPort().getLoadDuration(), firstPartyVessels, currentDateTime);
			final int beforeDrop0 = mc0.getCurrentMonthAbsoluteEntitlement();
			final int beforeDrop1 = mc1.getCurrentMonthAbsoluteEntitlement();
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
		}).get();
	}

	public MUDContainer phase2CalculateMULL(final Map<Vessel, LocalDateTime> vesselToMostRecentUseDateTime, final Map<Vessel, LocalDateTime> vesselToNextForwardUseTime,
			final int defaultAllocationDrop, final Set<Vessel> firstPartyVessels, final LocalDateTime currentDateTime) {
		return this.mudContainerList.stream().max((Comparator<MUDContainer>) (mc0, mc1) -> {
			final Long allocation0 = mc0.getRunningAllocation();
			final Long allocation1 = mc1.getRunningAllocation();
			final int expectedAllocationDrop0 = mc0.phase2CalculateExpectedAllocationDrop(vesselToMostRecentUseDateTime, vesselToNextForwardUseTime, defaultAllocationDrop,
					inventory.getPort().getLoadDuration(), firstPartyVessels, currentDateTime);
			final int expectedAllocationDrop1 = mc1.phase2CalculateExpectedAllocationDrop(vesselToMostRecentUseDateTime, vesselToNextForwardUseTime, defaultAllocationDrop,
					inventory.getPort().getLoadDuration(), firstPartyVessels, currentDateTime);
			final int beforeDrop0 = mc0.getCurrentMonthAbsoluteEntitlement();
			final int beforeDrop1 = mc1.getCurrentMonthAbsoluteEntitlement();
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
		}).get();
	}

	public MUDContainer harmonisationPhaseCalculateMULL(final Map<Vessel, LocalDateTime> vesselToMostRecentUseDateTime, final Map<Vessel, LocalDateTime> vesselToNextForwardUseTime,
			final int defaultAllocationDrop, final Set<Vessel> firstPartyVessels, final LocalDateTime currentDateTime) {
		return this.mudContainerList.stream().max((Comparator<MUDContainer>) (mc0, mc1) -> {
			final Long allocation0 = mc0.getRunningAllocation();
			final Long allocation1 = mc1.getRunningAllocation();
			final int expectedAllocationDrop0 = mc0.phase2CalculateExpectedAllocationDrop(vesselToMostRecentUseDateTime, vesselToNextForwardUseTime, defaultAllocationDrop,
					inventory.getPort().getLoadDuration(), firstPartyVessels, currentDateTime);
			final int expectedAllocationDrop1 = mc1.phase2CalculateExpectedAllocationDrop(vesselToMostRecentUseDateTime, vesselToNextForwardUseTime, defaultAllocationDrop,
					inventory.getPort().getLoadDuration(), firstPartyVessels, currentDateTime);
			final int beforeDrop0 = mc0.getCurrentMonthAbsoluteEntitlement();
			final int beforeDrop1 = mc1.getCurrentMonthAbsoluteEntitlement();
			final int afterDrop0 = beforeDrop0 - expectedAllocationDrop0;
			final int afterDrop1 = beforeDrop1 - expectedAllocationDrop1;

			final boolean belowLower0 = afterDrop0 < -fullCargoLotValue;
			final boolean belowLower1 = afterDrop1 < -fullCargoLotValue;
			final boolean aboveUpper0 = afterDrop0 > fullCargoLotValue;
			final boolean aboveUpper1 = afterDrop1 > fullCargoLotValue;

			final boolean satisfiedMonthlyAllocation0 = mc0.satisfiedMonthlyAllocation();
			final boolean satisfiedMonthlyAllocation1 = mc1.satisfiedMonthlyAllocation();

			if (!satisfiedMonthlyAllocation0 && satisfiedMonthlyAllocation1) {
				return 1;
			} else if (satisfiedMonthlyAllocation0 && !satisfiedMonthlyAllocation1) {
				return -1;
			}

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
		}).get();
	}

	public void updateRunningAllocation(final int volumeIn) {
		this.mudContainerList.stream().forEach(mc -> mc.updateRunningAllocation((long) volumeIn));
	}

	public void updateCurrentMonthAbsoluteEntitlement(final int totalMonthIn) {
		this.mudContainerList.stream().forEach(mc -> mc.updateCurrentMonthAbsoluteEntitlement(totalMonthIn));
	}

	public void updateCurrentMonthAllocations(final YearMonth nextMonth) {
		this.mudContainerList.forEach(mc -> mc.updateCurrentMonthAllocations(nextMonth));
	}

	public List<MUDContainer> getMUDContainers() {
		return this.mudContainerList;
	}

	public void undo(final CargoBlueprint cargoBlueprint) {
		for (final MUDContainer mudContainer : mudContainerList) {
			mudContainer.undo(cargoBlueprint);
		}
	}

	public void harmonisationPhaseUndo(final CargoBlueprint cargoBlueprint) {
		for (final MUDContainer mudContainer : mudContainerList) {
			mudContainer.harmonisationPhaseUndo(cargoBlueprint);
		}
	}

	public void phase1Undo(final CargoBlueprint cargoBlueprint) {
		for (final MUDContainer mudContainer : mudContainerList) {
			mudContainer.phase1Undo(cargoBlueprint);
		}
	}

	public void dropFixedLoad(final Cargo cargo) {
		for (final MUDContainer mudContainer : this.mudContainerList) {
			mudContainer.dropFixedLoad(cargo);
		}
	}

	public void phase1DropFixedLoad(final Cargo cargo) {
		for (final MUDContainer mudContainer : this.mudContainerList) {
			mudContainer.phase1DropFixedLoad(cargo);
		}
	}

	public Inventory getInventory() {
		return this.inventory;
	}
}
