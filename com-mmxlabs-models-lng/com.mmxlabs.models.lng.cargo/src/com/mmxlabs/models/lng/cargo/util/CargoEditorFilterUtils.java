/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2021
 * All rights reserved.
 */
package com.mmxlabs.models.lng.cargo.util;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.Set;

import com.mmxlabs.models.lng.cargo.Cargo;
import com.mmxlabs.models.lng.cargo.CargoType;
import com.mmxlabs.models.lng.cargo.DischargeSlot;
import com.mmxlabs.models.lng.cargo.LoadSlot;
import com.mmxlabs.models.lng.cargo.SpotSlot;
import com.mmxlabs.models.lng.commercial.Contract;

public class CargoEditorFilterUtils {
	
	public enum ShippedCargoFilterOption {
		NONE, SHIPPED, NON_SHIPPED, DES, FOB, NOMINAL
	}
	
	public enum TimeFilterOption {
		NONE, YEARMONTH, PROMPT, CURRENT
	}
	
	public enum CargoFilterOption {
		NONE, CARGO, LONG, SHORT, OPEN
	}
	
	public static boolean shippedCargoFilter(final ShippedCargoFilterOption option, final Cargo cargo) {
		if (option == ShippedCargoFilterOption.NONE) {
			return true;
		}
		if (cargo == null) {
			return false;
		}
		switch (option) {
		case SHIPPED:
			return cargo.getCargoType() == CargoType.FLEET;
		case NON_SHIPPED:
			return cargo.getCargoType() != CargoType.FLEET;
		case DES:
			return cargo.getCargoType() == CargoType.DES;
		case FOB:
			return cargo.getCargoType() == CargoType.FOB;
		case NOMINAL:
			if (cargo.getSpotIndex() == -1)
				return true;
		}
		return false;
	}
	
	public static boolean timePeriodFilter(final TimeFilterOption option, final LoadSlot load, final DischargeSlot discharge, final YearMonth ymChoice, final int promptMonth) {
		if (option == TimeFilterOption.NONE) {
			return true;
		}
		switch (option) {
		case YEARMONTH:
			return checkYearMonth(load, discharge, ymChoice);
		case PROMPT:
			return checkPrompt(load, discharge, promptMonth);
		case CURRENT:
			return checkCurrent(load, discharge);
		}
		return false;
	}
	
	public static boolean cargoTradesFilter(final CargoFilterOption option, final Cargo cargo, final LoadSlot load, final DischargeSlot discharge, final Set<String> filtersOpenContracts) {
		switch (option) {
		case NONE:
			return true;
		case CARGO:
			return cargo != null;
		case LONG:
			return isLong(cargo, load, discharge, filtersOpenContracts);
		case SHORT:
			return isShort(cargo, load, discharge, filtersOpenContracts);
		case OPEN:
			return isShort(cargo, load, discharge, filtersOpenContracts) || isLong(cargo, load, discharge, filtersOpenContracts);
		}
		return false;
	}
	
	private static boolean checkYearMonth(final LoadSlot load, final DischargeSlot discharge, final YearMonth choice) {
		LocalDate start = null;
		LocalDate end = null;
		if (load != null) {
			start = load.getSchedulingTimeWindow().getStart().toLocalDate();
			end = load.getSchedulingTimeWindow().getEnd().toLocalDate();
			if ((start != null) && (end != null)) {
				final YearMonth yms = YearMonth.from(start);
				final YearMonth yme = YearMonth.from(end);
				if ((yms.equals(choice)) || (yme.equals(choice))) {
					return true;
				}
			}
		}
		if (discharge != null) {
			start = discharge.getSchedulingTimeWindow().getStart().toLocalDate();
			end = discharge.getSchedulingTimeWindow().getEnd().toLocalDate();
			if ((start != null) && (end != null)) {
				final YearMonth yms = YearMonth.from(start);
				final YearMonth yme = YearMonth.from(end);
				if ((yms.equals(choice)) || (yme.equals(choice))) {
					return true;
				}
			}
		}
		return false;
	}
	
	private static boolean checkPrompt(final LoadSlot load, final DischargeSlot discharge, final int month) {
		LocalDate start = null;
		LocalDate end = null;
		final LocalDate today = LocalDate.now();
		final LocalDate prompt = today.plusMonths(month);
		if (load != null) {
			start = load.getSchedulingTimeWindow().getStart().toLocalDate();
			end = load.getSchedulingTimeWindow().getEnd().toLocalDate();
			if (start != null && end != null) {
				if (start.isAfter(today) && start.isBefore(prompt)) {
					return true;
				}
				if (end.isAfter(today) && end.isBefore(prompt)) {
					return true;
				}
			}
		}
		if (discharge != null) {
			start = discharge.getSchedulingTimeWindow().getStart().toLocalDate();
			end = discharge.getSchedulingTimeWindow().getEnd().toLocalDate();
			if (start != null && end != null) {
				if (start.isAfter(today) && start.isBefore(prompt)) {
					return true;
				}
				if (end.isAfter(today) && end.isBefore(prompt)) {
					return true;
				}
			}
		}
		return false;
	}
	
	private static boolean checkCurrent(final LoadSlot load, final DischargeSlot discharge) {
		LocalDate start = null;
		LocalDate end = null;
		final LocalDate today = LocalDate.now();
		if (load != null && load.getSchedulingTimeWindow().getStart() != null &&//
				load.getSchedulingTimeWindow().getEnd() != null) {
			start = load.getSchedulingTimeWindow().getStart().toLocalDate();
			end = load.getSchedulingTimeWindow().getEnd().toLocalDate();
			if (start != null && end != null) {
				if (start.isAfter(today)) {
					return true;
				}
				if (end.isAfter(today)) {
					return true;
				}
			}
		}
		if (discharge != null && discharge.getSchedulingTimeWindow().getStart() != null &&//
				discharge.getSchedulingTimeWindow().getEnd() != null) {
			start = discharge.getSchedulingTimeWindow().getStart().toLocalDate();
			end = discharge.getSchedulingTimeWindow().getEnd().toLocalDate();
			if (start != null && end != null) {
				if (start.isAfter(today)) {
					return true;
				}
				if (end.isAfter(today)) {
					return true;
				}
			}
		}
		return false;
	}
	
	private static boolean isLong(final Cargo cargo, final LoadSlot load, final DischargeSlot discharge, final Set<String> filtersOpenContracts) {
		if (cargo == null && load != null) {
			return true;
		} else if (cargo != null && load != null && discharge instanceof SpotSlot) {
			return true;
		} else if (cargo != null && load != null && discharge != null) {
			final Contract contract = discharge.getContract();
			if (contract != null && contract.getName() != null) {
				return filtersOpenContracts.contains(contract.getName().toLowerCase());
			}
		}
		return false;
	}
	
	private static boolean isShort(final Cargo cargo, final LoadSlot load, final DischargeSlot discharge, final Set<String> filtersOpenContracts) {
		if (cargo == null && discharge != null) {
			return true;
		} else if (cargo != null && discharge != null && load instanceof SpotSlot) {
			return true;
		} else if (cargo != null && discharge != null && load != null) {
			final Contract contract = load.getContract();
			if (contract != null && contract.getName() != null) {
				return filtersOpenContracts.contains(contract.getName().toLowerCase());
			}
		}
		return false;
	}
}