package com.mmxlabs.scheduler.optimiser.exposures.calculators;

import org.eclipse.jdt.annotation.NonNull;

import com.mmxlabs.common.Pair;
import com.mmxlabs.common.parser.astnodes.Operator;
import com.mmxlabs.common.parser.astnodes.OperatorASTNode;
import com.mmxlabs.scheduler.optimiser.Calculator;
import com.mmxlabs.scheduler.optimiser.exposures.Constant;
import com.mmxlabs.scheduler.optimiser.exposures.ExposureRecord;
import com.mmxlabs.scheduler.optimiser.exposures.ExposureRecords;
import com.mmxlabs.scheduler.optimiser.exposures.IExposureNode;
import com.mmxlabs.scheduler.optimiser.exposures.InputRecord;

public class OperatorExposuresCalculator {

	public static @NonNull Pair<Long, IExposureNode> getExposureNode(final @NonNull OperatorASTNode operatorNode, final InputRecord inputRecord) {

		final var operator = operatorNode.getOperator();
		final Pair<Long, IExposureNode> pc0 = ExposuresASTToCalculator.getExposureNode(operatorNode.getLHS(), inputRecord);
		final Pair<Long, IExposureNode> pc1 = ExposuresASTToCalculator.getExposureNode(operatorNode.getRHS(), inputRecord);

		if (operator == Operator.PLUS) {
			// addition: add coefficients of summands
			return add(pc0, pc1);
		} else if (operator == Operator.MINUS) {
			return subtract(pc0, pc1);
		} else if (operator == Operator.TIMES) {
			return times(pc0, pc1);
		} else if (operator == Operator.DIVIDE) {
			return divide(pc0, pc1);
		} else if (operator == Operator.PERCENT) {
			return percent(pc0, pc1);
		} else {
			throw new IllegalStateException("Invalid operator");
		}
	}

	public static Pair<Long, IExposureNode> add(final Pair<Long, IExposureNode> pc0, final Pair<Long, IExposureNode> pc1) {
		final IExposureNode c0 = pc0.getSecond();
		final IExposureNode c1 = pc1.getSecond();

		if (c0 instanceof final Constant c0Const && c1 instanceof final Constant c1Const) {
			return new Pair<>(pc0.getFirst() + pc1.getFirst(), new Constant(c0Const.getConstant() + c1Const.getConstant(), ""));
		} else if (c0 instanceof ExposureRecords && c1 instanceof Constant) {
			return new Pair<>(pc0.getFirst() + pc1.getFirst(), c0);
		} else if (c0 instanceof Constant && c1 instanceof ExposureRecords) {
			return new Pair<>(pc0.getFirst() + pc1.getFirst(), c1);
		} else if (c0 instanceof final ExposureRecords c0Records && c1 instanceof final ExposureRecords c1Records) {
			return new Pair<>(pc0.getFirst() + pc1.getFirst(),
					ExposuresCalculatorUtils.merge(c0Records, c1Records, (c_0, c_1) -> new ExposureRecord(c_0.curveName(), c_0.currencyUnit(), c_0.unitPrice(), //
							Math.addExact(c_0.nativeVolume(), c_1.nativeVolume()), //
							Math.addExact(c_0.nativeValue(), c_1.nativeValue()), //
							Math.addExact(c_0.mmbtuVolume(), c_1.mmbtuVolume()), //
							c_0.date(), c_0.volumeUnit())));
		}
		throw new IllegalStateException();
	}

	public static Pair<Long, IExposureNode> subtract(final Pair<Long, IExposureNode> pc0, final Pair<Long, IExposureNode> pc1) {
		final IExposureNode c0 = pc0.getSecond();
		final IExposureNode c1 = pc1.getSecond();

		if (c0 instanceof final Constant c0Const && c1 instanceof final Constant c1Const) {
			return new Pair<>(pc0.getFirst() - pc1.getFirst(), new Constant(c0Const.getConstant() - c1Const.getConstant(), ""));
		} else if (c0 instanceof ExposureRecords && c1 instanceof Constant) {
			return new Pair<>(pc0.getFirst() - pc1.getFirst(), c0);
		} else if (c0 instanceof Constant && c1 instanceof ExposureRecords) {
			return new Pair<>(pc0.getFirst() - pc1.getFirst(), ExposuresCalculatorUtils.modify((ExposureRecords) c1,
					c -> new ExposureRecord(c.curveName(), c.currencyUnit(), c.unitPrice(), -c.nativeVolume(), -c.nativeValue(), -c.mmbtuVolume(), c.date(), c.volumeUnit())));
		} else if (c0 instanceof final ExposureRecords c0Records && c1 instanceof final ExposureRecords c1Records) {
			final ExposureRecords newC1 = ExposuresCalculatorUtils.modify(c1Records,
					c -> new ExposureRecord(c.curveName(), c.currencyUnit(), c.unitPrice(), -c.nativeVolume(), -c.nativeValue(), -c.mmbtuVolume(), c.date(), c.volumeUnit()));
			// + is fine here as we have just negated the volumes above
			return new Pair<>(pc0.getFirst() - pc1.getFirst(), ExposuresCalculatorUtils.merge(c0Records, newC1, (c_0, c_1) -> new ExposureRecord(c_0.curveName(), c_0.currencyUnit(), c_0.unitPrice(), //
					Math.addExact(c_0.nativeVolume(), c_1.nativeVolume()), //
					Math.addExact(c_0.nativeValue(), c_1.nativeValue()), //
					Math.addExact(c_0.mmbtuVolume(), c_1.mmbtuVolume()), //
					c_0.date(), c_0.volumeUnit())));
		}
		throw new IllegalStateException();

	}

	public static Pair<Long, IExposureNode> percent(final Pair<Long, IExposureNode> pc0, final Pair<Long, IExposureNode> pc1) {
		final IExposureNode c0 = pc0.getSecond();
		final IExposureNode c1 = pc1.getSecond();

		if (c0 instanceof final Constant c0Const && c1 instanceof final Constant c1Const) {
			return new Pair<>((ExposuresCalculatorUtils.multiplyConstantByConstant(pc0.getFirst(), pc1.getFirst()) / 100), //
					new Constant((ExposuresCalculatorUtils.multiplyConstantByConstant(c0Const.getConstant(), c1Const.getConstant()) / 100), ""));
		} else if (c0 instanceof ExposureRecords && c1 instanceof Constant) {
			assert false;
			throw new UnsupportedOperationException();
			// return modify((ExposureRecords) c0, c -> new ExposureRecord(c.index,
			// c.unitPrice(), -c.nativeVolume(), -c.nativeValue, -c.mmbtuVolume(), c.date));
		} else if (c0 instanceof final Constant c0Const && c1 instanceof final ExposureRecords c1Records) {
			final long constant = c0Const.getConstant() / 100;
			return new Pair<>((ExposuresCalculatorUtils.multiplyConstantByConstant(pc0.getFirst(), pc1.getFirst()) / 100),
					ExposuresCalculatorUtils.modify(c1Records,
							c -> new ExposureRecord(c.curveName(), c.currencyUnit(), c.unitPrice(), ExposuresCalculatorUtils.multiplyVolumeByConstant(c.nativeVolume(), constant),
									ExposuresCalculatorUtils.multiplyVolumeByConstant(c.nativeValue(), constant), ExposuresCalculatorUtils.multiplyVolumeByConstant(c.mmbtuVolume(), constant),
									c.date(), c.volumeUnit())));
		} else if (c0 instanceof final ExposureRecords c0Records && c1 instanceof final ExposureRecords c1Records) {
			// return merge((ExposureRecords) c0, (ExposureRecords) c1, (c_0, c_1) -> new
			// ExposureRecord(c_0.index, c_0.unitPrice(), c_0.nativeVolume -
			// c_1.nativeVolume(),
			// c_0.nativeValue - c_1.nativeValue, c_0.mmbtuVolume - c_1.mmbtuVolume(),
			// c_0.date));
			throw new UnsupportedOperationException();
		}

		throw new IllegalStateException();

	}

	public static Pair<Long, IExposureNode> divide(final Pair<Long, IExposureNode> pc0, final Pair<Long, IExposureNode> pc1) {
		final IExposureNode c0 = pc0.getSecond();
		final IExposureNode c1 = pc1.getSecond();

		if (c0 instanceof final Constant const_c0 && c1 instanceof final Constant const_c1) {
			final long value = (pc1.getFirst() == 0 ? 0 : ExposuresCalculatorUtils.divideConstantByConstant(pc0.getFirst(), pc1.getFirst()));
			final int newConstant = (int) (const_c1.getConstant() == 0 ? 0 : ExposuresCalculatorUtils.divideConstantByConstant(const_c0.getConstant(), const_c1.getConstant()));

			return new Pair<>(value, new Constant(newConstant, ""));
		} else if (c0 instanceof final ExposureRecords c0Records && c1 instanceof final Constant const_c1) {
			final long value = pc1.getFirst() == 0 ? 0 : ExposuresCalculatorUtils.divideVolumeByConstant(pc0.getFirst(), pc1.getFirst());
			final long constant = const_c1.getConstant();
			if (constant == 0.0) {
				return new Pair<>(value, ExposuresCalculatorUtils.modify(c0Records, c -> new ExposureRecord(c.curveName(), c.currencyUnit(), c.unitPrice(), 0, 0, 0, c.date(), c.volumeUnit())));
			} else {
				return new Pair<>(value, ExposuresCalculatorUtils.modify(c0Records, c -> {
					final long nativeVolume = ExposuresCalculatorUtils.divideVolumeByConstant(c.nativeVolume(), constant);
					final long nativeValue = ExposuresCalculatorUtils.divideVolumeByConstant(c.nativeValue(), constant);
					final long mmbtuVolume = ExposuresCalculatorUtils.divideVolumeByConstant(c.mmbtuVolume(), constant);
					return new ExposureRecord(c.curveName(), c.currencyUnit(), c.unitPrice(), nativeVolume, nativeValue, mmbtuVolume, c.date(), c.volumeUnit());
				}));
			}
		} else if (c0 instanceof final Constant const_c0 && c1 instanceof final ExposureRecords c1Records) {
			final long value = (pc1.getFirst() == 0 ? 0 : ExposuresCalculatorUtils.divideConstantByVolume(pc0.getFirst(), pc1.getFirst()));
			final long constant = const_c0.getConstant();
			if (constant == 0.0) {
				return new Pair<>(value, ExposuresCalculatorUtils.modify(c1Records, c -> new ExposureRecord(c.curveName(), c.currencyUnit(), c.unitPrice(), 0, 0, 0, c.date(), c.volumeUnit())));
			} else {
				return new Pair<>(value, ExposuresCalculatorUtils.modify(c1Records, c -> {
					final long nativeVolume = c.nativeVolume() == 0 ? 0 : ExposuresCalculatorUtils.divideConstantByVolume(constant, c.nativeVolume());
					final long nativeValue = c.nativeValue() == 0 ? 0 : ExposuresCalculatorUtils.divideConstantByVolume(constant, c.nativeValue());
					final long mmbtuVolume = c.mmbtuVolume() == 0 ? 0 : ExposuresCalculatorUtils.divideConstantByVolume(constant, c.mmbtuVolume());
					return new ExposureRecord(c.curveName(), c.currencyUnit(), c.unitPrice(), nativeVolume, nativeValue, mmbtuVolume, c.date(), c.volumeUnit());
				}));
			}
		} else if (c0 instanceof final ExposureRecords c0Records && c1 instanceof final ExposureRecords c1Records) {
			final long value = (pc1.getFirst() == 0 ? 0 : ExposuresCalculatorUtils.divideVolumeByVolume(pc0.getFirst(), pc1.getFirst()));
			return new Pair<>(value, ExposuresCalculatorUtils.merge(c0Records, c1Records, (c_0, c_1) -> {
				final long nativeVolume = c_1.nativeVolume() == 0 ? 0 : ExposuresCalculatorUtils.divideVolumeByVolume(c_0.nativeVolume(), c_1.nativeVolume()) * 10;
				final long nativeValue = c_1.nativeValue() == 0 ? 0 : ExposuresCalculatorUtils.divideVolumeByVolume(c_0.nativeValue(), c_1.nativeValue()) * 10;
				final long mmbtuVolume = c_1.mmbtuVolume() == 0 ? 0 : ExposuresCalculatorUtils.divideVolumeByVolume(c_0.mmbtuVolume(), c_1.mmbtuVolume()) * 10;
				return new ExposureRecord(c_0.curveName(), c_0.currencyUnit(), c_0.unitPrice(), nativeVolume, nativeValue, mmbtuVolume, c_0.date(), c_0.volumeUnit());
			}));
		}

		throw new IllegalStateException();

	}

	public static Pair<Long, IExposureNode> times(final Pair<Long, IExposureNode> pc0, final Pair<Long, IExposureNode> pc1) {
		final IExposureNode c0 = pc0.getSecond();
		final IExposureNode c1 = pc1.getSecond();

		if (c0 instanceof final Constant c0Const && c1 instanceof final Constant c1Const) {
			return new Pair<>(ExposuresCalculatorUtils.multiplyConstantByConstant(pc0.getFirst(), pc1.getFirst()),
					new Constant((int) ExposuresCalculatorUtils.multiplyConstantByConstant(c0Const.getConstant(), c1Const.getConstant()), ""));
		} else if (c0 instanceof final ExposureRecords c0Records && c1 instanceof final Constant const_c1) {
			final long constant = const_c1.getConstant();
			return new Pair<>(ExposuresCalculatorUtils.multiplyConstantByConstant(pc0.getFirst(), pc1.getFirst()),
					ExposuresCalculatorUtils.modify(c0Records,
							c -> new ExposureRecord(c.curveName(), c.currencyUnit(), c.unitPrice(), ExposuresCalculatorUtils.multiplyVolumeByConstant(c.nativeVolume(), constant),
									ExposuresCalculatorUtils.multiplyVolumeByConstant(c.nativeValue(), constant), ExposuresCalculatorUtils.multiplyVolumeByConstant(c.mmbtuVolume(), constant),
									c.date(), c.volumeUnit())));
		} else if (c0 instanceof final Constant const_c0 && c1 instanceof final ExposureRecords c1Records) {
			final long constant = const_c0.getConstant();
			return new Pair<>(ExposuresCalculatorUtils.multiplyConstantByConstant(pc0.getFirst(), pc1.getFirst()),
					ExposuresCalculatorUtils.modify(c1Records,
							c -> new ExposureRecord(c.curveName(), c.currencyUnit(), c.unitPrice(), ExposuresCalculatorUtils.multiplyVolumeByConstant(c.nativeVolume(), constant),
									ExposuresCalculatorUtils.multiplyVolumeByConstant(c.nativeValue(), constant), ExposuresCalculatorUtils.multiplyVolumeByConstant(c.mmbtuVolume(), constant),
									c.date(), c.volumeUnit())));
		} else if (c0 instanceof final ExposureRecords c0Records && c1 instanceof final ExposureRecords c1Records) {
			return new Pair<>(ExposuresCalculatorUtils.multiplyConstantByConstant(pc0.getFirst(), pc1.getFirst()),
					ExposuresCalculatorUtils.merge(c0Records, c1Records, (c_0, c_1) -> new ExposureRecord(c_0.curveName(), c_0.currencyUnit(), c_0.unitPrice(), //
							Calculator.multiply(c_0.nativeVolume() * 10L, c_1.nativeVolume()), //
							Calculator.multiply(c_0.nativeValue() * 10L, c_1.nativeValue()), //
							Calculator.multiply(c_0.mmbtuVolume() * 10L, c_1.mmbtuVolume()), //
							c_0.date(), c_0.volumeUnit())));
		}

		throw new IllegalStateException();

	}

}
