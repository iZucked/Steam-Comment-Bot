/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
package com.mmxlabs.models.lng.schedule.util;

import java.text.DecimalFormat;
import java.time.Year;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.eclipse.jdt.annotation.NonNull;

import com.mmxlabs.models.lng.cargo.CIIStartOptions;
import com.mmxlabs.models.lng.cargo.VesselCharter;
import com.mmxlabs.models.lng.fleet.BaseFuel;
import com.mmxlabs.models.lng.fleet.CIIGradeBoundary;
import com.mmxlabs.models.lng.fleet.CIIReferenceData;
import com.mmxlabs.models.lng.fleet.FuelEmissionReference;
import com.mmxlabs.models.lng.fleet.Vessel;
import com.mmxlabs.models.lng.schedule.Event;
import com.mmxlabs.models.lng.schedule.Fuel;
import com.mmxlabs.models.lng.schedule.FuelAmount;
import com.mmxlabs.models.lng.schedule.FuelQuantity;
import com.mmxlabs.models.lng.schedule.FuelUnit;
import com.mmxlabs.models.lng.schedule.FuelUsage;
import com.mmxlabs.models.lng.schedule.Schedule;
import com.mmxlabs.models.lng.schedule.ScheduleModel;
import com.mmxlabs.models.lng.schedule.Sequence;
import com.mmxlabs.models.lng.schedule.StartEvent;
import com.mmxlabs.models.lng.schedule.cii.CIIAccumulatableEventModel;
import com.mmxlabs.models.lng.schedule.cii.SimpleCIIAccumulatableModel;

public class EmissionsUtil {
	
	public static final double LNG_DENSITY_TON_PER_M3 = 0.450;
	private static final double LNG_EMISSION_RATE_TON_CO2_PER_TON_FUEL = 2.750;
	
	public static final int METHANE_CO2_EQUIVALENT = 25;
	public static final double MT_TO_GRAMS = 1_000_000.0;
	public static final double GRAMS_TO_TONS = 0.000_001;
	
	private EmissionsUtil() {
	}
	
	public static final double INFINITE_CII = -1;

	private static final double POWER_NON_LARGE_DWT = 2.673;
	private static final double MIN_CAPACITY = 65_000.0;
	private static final double LARGE_DWT_THESHOLD = 100_000.0;
	private static final double MULTIPLIER_COEFF_LARGE_DWT = 9.827;
	private static final double MULTIPLIER_COEFF_NON_LARGE_DWT = 14_479E+10;
	
	private static final Map<Year, Double> getRelativeReductionFactorTable(final @NonNull CIIReferenceData ciiReferenceData){
		final Map<Year, Double> result = new HashMap<>();
		ciiReferenceData.getReductionFactors().forEach(rf -> {
			result.putIfAbsent(Year.of(rf.getYear()), rf.getPercentage() / 100.0);
		});
		return result;
	}
	
	private static final int DWT_GREATER_OR_EQUAL_100K = 0;
	private static final int DWT_LESS_THAN_100K = 1;
	
	private static final double[][] getGradesExponentBoundaryTable(final @NonNull CIIReferenceData ciiReferenceData){
		final double[][] letterGradeBoundaries = new double[2][4];
		for (final CIIGradeBoundary gradeBoundary : ciiReferenceData.getCiiGradeBoundaries()) {
			if (gradeBoundary.getDwtUpperLimit() <= LARGE_DWT_THESHOLD) {
				letterGradeBoundaries[DWT_LESS_THAN_100K][Grade.A.index] = gradeBoundary.getGradeAValue();
				letterGradeBoundaries[DWT_LESS_THAN_100K][Grade.B.index] = gradeBoundary.getGradeBValue();
				letterGradeBoundaries[DWT_LESS_THAN_100K][Grade.C.index] = gradeBoundary.getGradeCValue();
				letterGradeBoundaries[DWT_LESS_THAN_100K][Grade.D.index] = gradeBoundary.getGradeDValue();
			} else {
				letterGradeBoundaries[DWT_GREATER_OR_EQUAL_100K][Grade.A.index] = gradeBoundary.getGradeAValue();
				letterGradeBoundaries[DWT_GREATER_OR_EQUAL_100K][Grade.B.index] = gradeBoundary.getGradeBValue();
				letterGradeBoundaries[DWT_GREATER_OR_EQUAL_100K][Grade.C.index] = gradeBoundary.getGradeCValue();
				letterGradeBoundaries[DWT_GREATER_OR_EQUAL_100K][Grade.D.index] = gradeBoundary.getGradeDValue();
			}
		}
		return letterGradeBoundaries;
	}
	
	public static double getLNGEmissionRate(final @NonNull CIIReferenceData ciiReferenceData) {
		if (ciiReferenceData.getFuelEmissions() != null && !ciiReferenceData.getFuelEmissions().isEmpty()) {
			for (final var entry : ciiReferenceData.getFuelEmissions()) {
				final String name = entry.getName();
				if (name != null && !name.isBlank() && name.toLowerCase().contains("LNG") && entry.getCf() != 0.0) {
					return entry.getCf();
				}
			}
		}
		return LNG_EMISSION_RATE_TON_CO2_PER_TON_FUEL;
	}

	enum Grade {
		A(0), B(1), C(2), D(3), E(4);

		public final int index;

		Grade(int indexValue) {
			this.index = indexValue;
		}
	}

	public static double[][] getLetterGradeBoundaries(final @NonNull CIIReferenceData ciiReferenceData, final Vessel vessel, final Year referenceYear) {

		final double dwt = vessel.getVesselOrDelegateDeadWeight();

		//
		// Reference CII 2019
		final double capacity = Math.max(dwt, MIN_CAPACITY);
		final double a = dwt < LARGE_DWT_THESHOLD ? MULTIPLIER_COEFF_NON_LARGE_DWT : MULTIPLIER_COEFF_LARGE_DWT;
		final double c = dwt < LARGE_DWT_THESHOLD ? POWER_NON_LARGE_DWT : 0;
		//
		final double referenceValueCII = a * Math.pow(capacity, -c);
		final Map<Year, Double> reductionFactors = getRelativeReductionFactorTable(ciiReferenceData);
		//
		// Required CII current year
		final Year actualReferenceYear;
		if (referenceYear != null && reductionFactors.containsKey(referenceYear)) {
			actualReferenceYear = referenceYear;
		} else {
			actualReferenceYear = Year.now();
		}
		final double z = reductionFactors.get(actualReferenceYear);
		final double requiredCII = (1.0 - z) * referenceValueCII;

		//
		// Rating boundaries
		final double[][] letterGradeBoundaries = new double[2][4];
		final double[][] refBoundaries = getGradesExponentBoundaryTable(ciiReferenceData);
		//
		for (final Grade grade : Grade.values()) {
			if (grade == Grade.E) {
				break;
			}
			final double lowerExponentBoundary = refBoundaries[DWT_LESS_THAN_100K][grade.index];
			final double lowerGradeBoundary = lowerExponentBoundary * requiredCII;
			letterGradeBoundaries[DWT_LESS_THAN_100K][grade.index] = lowerGradeBoundary;

			final double upperExponentBoundary = refBoundaries[DWT_GREATER_OR_EQUAL_100K][grade.index];
			final double upperGradeBoundary = upperExponentBoundary * requiredCII;
			letterGradeBoundaries[DWT_GREATER_OR_EQUAL_100K][grade.index] = upperGradeBoundary;
		}
		return letterGradeBoundaries;
	}

	public static String getLetterGrade(final @NonNull CIIReferenceData ciiReferenceData, final Vessel vessel, final double ciiValue, final Year givenYear) {
		final int deadweightIndex = vessel.getVesselOrDelegateDeadWeight() >= LARGE_DWT_THESHOLD ? 0 : 1;
		final double[][] gradesTableForThatVessel = getLetterGradeBoundaries(ciiReferenceData, vessel, givenYear);
		double prevBoundary = -1;
		Grade result = null;
		for (final Grade grade : Grade.values()) {
			if (grade == Grade.E) {
				return Grade.E.toString();
			}
			final double boundary = gradesTableForThatVessel[deadweightIndex][grade.index];
			if (prevBoundary <= ciiValue && ciiValue < boundary) {
				result = grade;
				break;
			}
			prevBoundary = boundary;
		}
		if (result == null) {
			return "-";
		}
		return result.toString();
	}
	
	public static String formatCII(double ciiValue) {
		return new DecimalFormat("###0.###").format(ciiValue);
	}

	public static double findCII(final Vessel vessel, final double totalEmission, final double totalDistance) {
		final double denominatorCII = totalDistance * vessel.getVesselOrDelegateDeadWeight();
		if (denominatorCII == 0) {
			return INFINITE_CII;
		}
		return EmissionsUtil.MT_TO_GRAMS * totalEmission / denominatorCII;
	}
	
	public static double consumedCarbonEquivalentEmissionLNG(final @NonNull CIIReferenceData ciiReferenceData, final FuelQuantity fuelQuantity) {
		return consumedQuantityLNG(fuelQuantity) * getLNGEmissionRate(ciiReferenceData);//LNG_EMISSION_RATE_TON_CO2_PER_TON_FUEL;
	}
	
	@SuppressWarnings("null")
	public static Long consumedQuantityLNG(final FuelQuantity fuelQuantity) {

		final Set<FuelUnit> uniqueUnits = new HashSet<>();
		for (final FuelAmount fuelAmount : fuelQuantity.getAmounts()) {
			uniqueUnits.add(fuelAmount.getUnit());
		}

		final boolean unitsAreTheSame = uniqueUnits.size() == 1;
		double quantity = 0.0;

		if (unitsAreTheSame) {
			final FuelUnit unitItSelf = uniqueUnits.stream().findAny().orElseThrow();
			quantity = fuelQuantity.getAmounts().stream().map(FuelAmount::getQuantity).reduce(Double::sum).orElse(0.0);

			// Convert whatever to MT
			quantity = switch (unitItSelf) {
			case MMBTU -> throw new IllegalStateException("Bad");
			case M3 -> quantity * LNG_DENSITY_TON_PER_M3;
			case MT -> quantity;
			};
		} else {
			for (final FuelAmount amount : fuelQuantity.getAmounts()) {
				if (amount.getUnit() == FuelUnit.M3) {
					quantity = amount.getQuantity();
				}
			}
			quantity *= LNG_DENSITY_TON_PER_M3;
		}
		return Math.round(quantity);
	}
	
	public static void processAccumulatableEventModelForCII(final @NonNull CIIReferenceData ciiReferenceData, final CIIAccumulatableEventModel model, final Vessel vessel, final Event event) {
		model.setCIIVessel(vessel);
		model.setCIIEvent(event);
		model.setCIIStartDate(event.getStart().toLocalDate());
		model.setCIIEndDate(event.getEnd().toLocalDate());
		if (event instanceof final FuelUsage fuelUsageEvent) {
			processFuelUsageEvent(model, ciiReferenceData, fuelUsageEvent);
		}
		if (event instanceof final StartEvent startEvent) {
			final VesselCharter vesselCharter = startEvent.getSequence().getVesselCharter();
			if (vesselCharter != null) {
				final CIIStartOptions ciiStartOptions = vesselCharter.getCiiStartOptions();
				if (ciiStartOptions != null) {
					model.addToTotalEmissionForCII(ciiStartOptions.getYearToDateEmissions());
				}
			}
		}
	}

	private static void processFuelUsageEvent(final CIIAccumulatableEventModel model, final @NonNull CIIReferenceData ciiReferenceData, FuelUsage fuelUsageEvent) {
		for (final FuelQuantity fuelQuantity : fuelUsageEvent.getFuels()) {
			final Fuel fuel = fuelQuantity.getFuel();
			final BaseFuel baseFuel = fuelQuantity.getBaseFuel();
			final FuelAmount fuelAmount = fuelQuantity.getAmounts().get(0);
			switch (fuel) {
			case BASE_FUEL, PILOT_LIGHT:
				if (fuelQuantity.getAmounts().get(0).getUnit() != FuelUnit.MT) {
					throw new IllegalStateException();
				}
				double er = 0.0;
				if (baseFuel != null) {
					final FuelEmissionReference fer = baseFuel.getEmissionReference();
					if (fer != null) {
						er = fer.getCf();
					}
				}
				model.addToTotalEmissionForCII(Math.round(fuelAmount.getQuantity() * er));
				break;
			case FBO, NBO:
				model.addToTotalEmissionForCII(EmissionsUtil.consumedCarbonEquivalentEmissionLNG(ciiReferenceData, fuelQuantity));
				break;
			default:
			}
		}
	}

	public static List<CIIAccumulatableEventModel> createCIIDataForVessel(ScheduleModel scheduleModel, final @NonNull CIIReferenceData ciiReferenceData, Vessel vessel) {
		final List<CIIAccumulatableEventModel> models = new LinkedList<>();

		final Schedule schedule = scheduleModel.getSchedule();

		if (schedule == null) {
			return models;
		}

		for (final Sequence seq : schedule.getSequences()) {
			final Vessel sequenceVessel = ScheduleModelUtils.getVessel(seq);
			if (sequenceVessel != null && sequenceVessel == vessel) {
				for (final Event event : seq.getEvents()) {
					final CIIAccumulatableEventModel model = new SimpleCIIAccumulatableModel();
					processAccumulatableEventModelForCII(ciiReferenceData, model, vessel, event);
					models.add(model);
				}
			}
		}

		return models;
	}
}
