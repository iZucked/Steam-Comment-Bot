package com.mmxlabs.models.lng.adp.rateability.spacing;

import java.time.LocalDate;
import java.util.List;

import org.eclipse.jdt.annotation.NonNull;

import com.mmxlabs.common.Pair;

public class Vessel {
	String name; // Vessel name (info only)
	String spotIndex; // Spot index string for CSV
	Cargo cargoes[];
	@Deprecated
	List<Pair<LocalDate,LocalDate>> allowedDateRanges;
	
	public Vessel(String name, String spotIndex, Cargo[] cargoes) {
		this.name = name;
		this.spotIndex = spotIndex;
		this.cargoes = cargoes;
		for (Cargo c : cargoes) {
			c.vessel = this;
		}
	}

	public Vessel(String name, String spotIndex, Cargo[] cargoes, List<Pair<LocalDate,LocalDate>> allowedDateRanges) {
		this(name, spotIndex, cargoes);
		this.allowedDateRanges = allowedDateRanges;
	}

	private static Cargo[] buildCargoesFromContractNumber(final Contract contract, final int number) {
		final Cargo[] cargoes = new Cargo[number];
		for (int i = 0; i < cargoes.length; ++i) {
			cargoes[i] = new Cargo(contract);
		}
		return cargoes;
	}
	
	public static @NonNull Vessel createVessel(final String name, final String spotindex, final Contract contract, final int number) {
		final Cargo[] cargoes = buildCargoesFromContractNumber(contract, number);
		return new Vessel(name, spotindex, cargoes);
	}
	
	public static @NonNull Vessel createVessel(String name, String spotindex, Contract contract, int number, List<Pair<LocalDate, LocalDate>> availableDates) {
		final Cargo[] cargoes = buildCargoesFromContractNumber(contract, number);
		return new Vessel(name, spotindex, cargoes, availableDates);
	}
}
