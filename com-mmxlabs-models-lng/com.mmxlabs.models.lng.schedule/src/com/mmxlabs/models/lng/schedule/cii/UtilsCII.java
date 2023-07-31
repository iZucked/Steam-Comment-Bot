package com.mmxlabs.models.lng.schedule.cii;

import java.text.DecimalFormat;
import java.time.Year;
import java.util.Map;

import com.mmxlabs.models.lng.fleet.Vessel;
import com.mmxlabs.models.lng.schedule.emissions.EmissionModelUtils;

public class UtilsCII {

	public static final double INFINITE_CII = -1;

	private static final double POWER_NON_LARGE_DWT = 2.673;
	private static final double MIN_CAPACITY = 65_000.0;
	private static final double LARGE_DWT_THESHOLD = 100_000.0;
	private static final double MULTIPLIER_COEFF_LARGE_DWT = 9.827;
	private static final double MULTIPLIER_COEFF_NON_LARGE_DWT = 14_479E+10;

	private static final Map<Year, Double> RELATIVE_REDUCTION_FACTOR_TABLE = Map.of(
			Year.of(2023), 0.05, 
			Year.of(2024), 0.07, 
			Year.of(2025), 0.09  // hard code new values here
	);

	private static final int DWT_GREATER_OR_EQUAL_100K = 0;
	private static final int DWT_LESS_THAN_100K = 1;
	private static final double[][] GRADES_EXPONENT_BOUNDARY_TABLES = { 
			{ 0.89, 0.98, 1.06, 1.13 },
			{ 0.78, 0.92, 1.10, 1.17 } 
	};

	enum Grade {
		A(0), B(1), C(2), D(3), E(4);

		public final int index;

		Grade(int indexValue) {
			this.index = indexValue;
		}
	}

	private UtilsCII() {
	}

	public static double[][] getLetterGradeBoundaries(final Vessel vessel, final Year referenceYear) {

		final double dwt = vessel.getVesselOrDelegateDeadWeight();

		//
		// Reference CII 2019
		final double capacity = Math.max(dwt, MIN_CAPACITY);
		final double a = dwt < LARGE_DWT_THESHOLD ? MULTIPLIER_COEFF_NON_LARGE_DWT : MULTIPLIER_COEFF_LARGE_DWT;
		final double c = dwt < LARGE_DWT_THESHOLD ? POWER_NON_LARGE_DWT : 0;
		//
		final double referenceValueCII = a * Math.pow(capacity, -c);

		//
		// Required CII current year
		final Year actualReferenceYear;
		if (referenceYear != null && RELATIVE_REDUCTION_FACTOR_TABLE.containsKey(referenceYear)) {
			actualReferenceYear = referenceYear;
		} else {
			actualReferenceYear = Year.now();
		}
		final double z = RELATIVE_REDUCTION_FACTOR_TABLE.get(actualReferenceYear);
		final double requiredCII = (1 - z / 100) * referenceValueCII;

		//
		// Rating boundaries
		final double[][] letterGradeBoundaries = new double[2][4];
		//
		for (final Grade grade : Grade.values()) {
			if (grade == Grade.E) {
				break;
			}
			final double lowerExponentBoundary = GRADES_EXPONENT_BOUNDARY_TABLES[DWT_LESS_THAN_100K][grade.index];
			final double lowerGradeBoundary = Math.exp(lowerExponentBoundary) * requiredCII;
			letterGradeBoundaries[DWT_LESS_THAN_100K][grade.index] = lowerGradeBoundary;

			final double upperExponentBoundary = GRADES_EXPONENT_BOUNDARY_TABLES[DWT_GREATER_OR_EQUAL_100K][grade.index];
			final double upperGradeBoundary = Math.exp(upperExponentBoundary) * requiredCII;
			letterGradeBoundaries[DWT_GREATER_OR_EQUAL_100K][grade.index] = upperGradeBoundary;
		}
		return letterGradeBoundaries;
	}

	public static String getLetterGrade(final Vessel vessel, final double ciiValue, final Year givenYear) {
		final int deadweightIndex = vessel.getVesselOrDelegateDeadWeight() >= LARGE_DWT_THESHOLD ? 1 : 0;
		final double[][] gradesTableForThatVessel = getLetterGradeBoundaries(vessel, givenYear);
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
		return EmissionModelUtils.MT_TO_GRAMS * totalEmission / denominatorCII;
	}
}
