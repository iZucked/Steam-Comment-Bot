/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2022
 * All rights reserved.
 */
package com.mmxlabs.models.lng.adp.rateability.spacing;
import java.time.LocalDate;
import java.util.List;
import java.util.Objects;


public class Cargo implements Comparable<Cargo> {

	public Cargo(Contract contract, LocalDate minLoadDate, LocalDate maxLoadDate, LocalDate minDischargeDate, LocalDate maxDischargeDate) {
		this(contract);
		this.minLoadDate = minLoadDate;
		this.maxLoadDate = maxLoadDate;
		this.minDischargeDate = minDischargeDate;
		this.maxDischargeDate = maxDischargeDate;
	}

	public Cargo(Contract contract,  LocalDate minDischargeDate, LocalDate maxDischargeDate) {
		this(contract);
		this.minDischargeDate = minDischargeDate;
		this.maxDischargeDate = maxDischargeDate;
	}

	public Cargo(Contract contract) {
		this.contract = contract;
	}
	
	Contract contract;
	Vessel vessel;
	public long dayOfSchedule;
	LocalDate minDischargeDate;
	LocalDate maxDischargeDate;
	LocalDate minLoadDate;
	LocalDate maxLoadDate;
	
	@Override
	public boolean equals(Object o) {
		if (o instanceof Cargo) {
			Cargo c = (Cargo)o;
			if (this == c) {
				return true;
			}
			return (this.dayOfSchedule == c.dayOfSchedule && this.contract.fob == c.contract.fob && this.contract.contractSpacing == c.contract.contractSpacing &&
					this.contract.interval == c.contract.interval && Objects.equals(this.contract, c.contract));
		}
		else {
			return false;
		}
	}

	@Override
	public int compareTo(Cargo c) {
		return Long.compare(this.dayOfSchedule, c.dayOfSchedule);
	}
	
	public static long getAverageInterval(Cargo[] cargoes) {
		long avgInterval = 0;
		for (Cargo cargo : cargoes) {
			avgInterval += cargo.contract.interval;
		}
		avgInterval /= cargoes.length;
		return avgInterval;
	}
}
