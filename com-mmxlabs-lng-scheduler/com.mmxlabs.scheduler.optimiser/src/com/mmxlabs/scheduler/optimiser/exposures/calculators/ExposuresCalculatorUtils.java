/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2022
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.exposures.calculators;

import java.math.BigInteger;
import java.util.Iterator;
import java.util.Objects;
import java.util.function.BinaryOperator;
import java.util.function.UnaryOperator;

import com.mmxlabs.scheduler.optimiser.Calculator;
import com.mmxlabs.scheduler.optimiser.exposures.ExposureRecord;
import com.mmxlabs.scheduler.optimiser.exposures.ExposureRecords;

public class ExposuresCalculatorUtils {

	public static final String MMBTU = "mmbtu";

	
	public static ExposureRecords modify(final ExposureRecords c0, final UnaryOperator<ExposureRecord> mapper) {
		final ExposureRecords n = new ExposureRecords();
		for (final ExposureRecord c : c0.records) {
			n.records.add(mapper.apply(c));
		}
		return n;
	}

	public static ExposureRecords merge(final ExposureRecords c0, final ExposureRecords c1, final BinaryOperator<ExposureRecord> mapper) {
		final ExposureRecords n = new ExposureRecords();
		final Iterator<ExposureRecord> c0Itr = c0.records.iterator();
		LOOP_C0: while (c0Itr.hasNext()) {
			final ExposureRecord c_c0 = c0Itr.next();
			final Iterator<ExposureRecord> c1Itr = c1.records.iterator();
			while (c1Itr.hasNext()) {
				final ExposureRecord c_c1 = c1Itr.next();
				if (c_c0.curveName().equalsIgnoreCase(c_c1.curveName())) {
					if (c_c0.date().equals(c_c1.date())) {
						if (Objects.equals(c_c0.volumeUnit(), c_c1.volumeUnit())) {
							n.records.add(mapper.apply(c_c0, c_c1));
							c1Itr.remove();
							c0Itr.remove();
							continue LOOP_C0;
						}
					}
				}
			}
			n.records.add(c_c0);
			c0Itr.remove();
		}
		// Add remaining.
		n.records.addAll(c1.records);

		return n;
	}

	public static long multiplyVolumeByConstant(final long volume, final long constant) {
		try {
			return Calculator.costFromVolume(volume, constant);
		} catch (final ArithmeticException e) {
			return BigInteger.valueOf(volume) //
					.multiply(BigInteger.valueOf(constant)) //
					.divide(BigInteger.valueOf(Calculator.HighScaleFactor)) //
					.longValueExact();
		}
	}

	public static long divideVolumeByConstant(final long volume, final long constant) {
		try {
			return Math.multiplyExact(volume, Calculator.HighScaleFactor) / constant;
		} catch (final ArithmeticException e) {
			return BigInteger.valueOf(volume) //
					.multiply(BigInteger.valueOf(Calculator.HighScaleFactor)) //
					.divide(BigInteger.valueOf(constant)) //
					.longValueExact();
		}
	}

	public static long divideConstantByVolume(final long constant, final long volume) {
		try {
			return Math.multiplyExact(constant, Calculator.ScaleFactor) / volume;
		} catch (final ArithmeticException e) {
			return BigInteger.valueOf(constant) //
					.multiply(BigInteger.valueOf(Calculator.ScaleFactor)) //
					.divide(BigInteger.valueOf(volume)) //
					.longValueExact();
		}
	}

	public static long divideVolumeByVolume(final long volumeA, final long volumeB) {
		try {
			return Math.multiplyExact(volumeA, Calculator.ScaleFactor) / volumeB;
		} catch (final ArithmeticException e) {
			return BigInteger.valueOf(volumeA) //
					.multiply(BigInteger.valueOf(Calculator.ScaleFactor)) //
					.divide(BigInteger.valueOf(volumeB)) //
					.longValueExact();
		}
	}

	public static long divideConstantByConstant(final long constantA, final long constantB) {
		try {
			return Math.multiplyExact(constantA, Calculator.HighScaleFactor) / constantB;
		} catch (final ArithmeticException e) {
			return BigInteger.valueOf(constantA) //
					.multiply(BigInteger.valueOf(Calculator.HighScaleFactor)) //
					.divide(BigInteger.valueOf(constantB)) //
					.longValueExact();
		}
	}

	public static long multiplyConstantByConstant(final long constantA, final long constantB) {
		try {
			return Math.multiplyExact(constantA, constantB) / Calculator.HighScaleFactor;
		} catch (final ArithmeticException e) {
			return BigInteger.valueOf(constantA) //
					.multiply(BigInteger.valueOf(constantB)) //
					.divide(BigInteger.valueOf(Calculator.HighScaleFactor)) //
					.longValueExact();
		}
	}

}
