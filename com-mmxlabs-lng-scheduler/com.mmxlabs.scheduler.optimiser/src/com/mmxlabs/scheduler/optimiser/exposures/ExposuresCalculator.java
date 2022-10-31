/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2022
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.exposures;

import java.math.BigInteger;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Function;

import javax.inject.Named;

import org.eclipse.jdt.annotation.NonNull;

import com.google.inject.Inject;
import com.mmxlabs.common.Pair;
import com.mmxlabs.common.calendars.BasicHolidayCalendar;
import com.mmxlabs.common.calendars.BasicPricingCalendar;
import com.mmxlabs.common.calendars.BasicPricingCalendarEntry;
import com.mmxlabs.common.curves.BasicCommodityCurveData;
import com.mmxlabs.common.curves.BasicUnitConversionData;
import com.mmxlabs.common.exposures.BasicExposureRecord;
import com.mmxlabs.common.exposures.ExposuresLookupData;
import com.mmxlabs.common.parser.astnodes.ASTNode;
import com.mmxlabs.common.parser.astnodes.ConstantASTNode;
import com.mmxlabs.common.parser.astnodes.DatedAvgFunctionASTNode;
import com.mmxlabs.common.parser.astnodes.FunctionASTNode;
import com.mmxlabs.common.parser.astnodes.FunctionType;
import com.mmxlabs.common.parser.astnodes.MonthFunctionASTNode;
import com.mmxlabs.common.parser.astnodes.NamedSeriesASTNode;
import com.mmxlabs.common.parser.astnodes.Operator;
import com.mmxlabs.common.parser.astnodes.OperatorASTNode;
import com.mmxlabs.common.parser.astnodes.SCurveFunctionASTNode;
import com.mmxlabs.common.parser.astnodes.ShiftFunctionASTNode;
import com.mmxlabs.common.parser.astnodes.SplitMonthFunctionASTNode;
import com.mmxlabs.common.parser.astnodes.TierFunctionASTNode;
import com.mmxlabs.common.parser.astnodes.VolumeTierASTNode;
import com.mmxlabs.common.parser.series.ISeries;
import com.mmxlabs.common.parser.series.SeriesParser;
import com.mmxlabs.common.time.Hours;
import com.mmxlabs.license.features.KnownFeatures;
import com.mmxlabs.license.features.LicenseFeatures;
import com.mmxlabs.scheduler.optimiser.Calculator;
import com.mmxlabs.scheduler.optimiser.OptimiserUnitConvertor;
import com.mmxlabs.scheduler.optimiser.SchedulerConstants;
import com.mmxlabs.scheduler.optimiser.components.IDischargeOption;
import com.mmxlabs.scheduler.optimiser.components.ILoadOption;
import com.mmxlabs.scheduler.optimiser.components.IPortSlot;
import com.mmxlabs.scheduler.optimiser.contracts.impl.PricingEventHelper;
import com.mmxlabs.scheduler.optimiser.fitness.components.allocation.impl.ICargoValueAnnotation;
import com.mmxlabs.scheduler.optimiser.providers.IExposureDataProvider;
import com.mmxlabs.scheduler.optimiser.providers.IExternalDateProvider;
import com.mmxlabs.scheduler.optimiser.providers.PortType;

/**
 * Utility class to calculate schedule exposure to market indices.
 * 
 * @author FM
 */
public class ExposuresCalculator {

	private static final String MMBTU = "mmbtu";

	@Inject
	@Named(SchedulerConstants.Parser_Commodity)
	private SeriesParser commodityIndices;

	@Inject
	@Named(SchedulerConstants.Parser_Currency)
	private SeriesParser currencyIndices;

	@Inject
	@Named(SchedulerConstants.INDIVIDUAL_EXPOSURES)
	private boolean individualExposures;

	@Inject
	private IExternalDateProvider externalDateProvider;

	@Inject
	private PricingEventHelper pricingEventHelper;

	@Inject
	private IExposureDataProvider exposureDataProvider;

	@Inject
	@Named(SchedulerConstants.COMPUTE_EXPOSURES)
	private boolean exposuresEnabled;

	public List<BasicExposureRecord> calculateExposures(final ICargoValueAnnotation cargoValueAnnotation, final IPortSlot portSlot) {
		if (exposuresEnabled) {

			final ExposuresLookupData lookupData = exposureDataProvider.getExposuresLookupData();

			if (individualExposures && !lookupData.slotsToInclude.contains(portSlot.getId())) {
				return Collections.emptyList();
			}

			boolean isLong = false;
			if (portSlot.getPortType() == PortType.Load) {
				isLong = true;
			} else if (portSlot.getPortType() == PortType.Discharge) {
				isLong = false;
			} else {
				return Collections.emptyList();
			}

			final long volumeMMBTU = cargoValueAnnotation.getPhysicalSlotVolumeInMMBTu(portSlot);
			final int pricePerMMBTU = cargoValueAnnotation.getSlotPricePerMMBTu(portSlot);
			final String priceExpression = exposureDataProvider.getPriceExpression(portSlot);
			int pricingTime = 0;
			if (isLong) {

				// Pricing event assumes first discharge in the case of an LDD cargo
				IDischargeOption firstDischarge = null;
				for (final IPortSlot ps : cargoValueAnnotation.getSlots()) {
					if (ps instanceof final IDischargeOption dOpt) {
						firstDischarge = dOpt;
						break;
					}
				}
				assert firstDischarge != null;
				pricingTime = pricingEventHelper.getLoadPricingDate((ILoadOption) portSlot, firstDischarge, cargoValueAnnotation);
			} else {
				pricingTime = pricingEventHelper.getDischargePricingDate((IDischargeOption) portSlot, cargoValueAnnotation);
			}
			final LocalDate pricingDate = externalDateProvider.getDateFromHours(pricingTime, portSlot.getPort()).toLocalDate();

			if (pricingDate.isAfter(lookupData.cutoffDate)) {
				final int cargoCV = cargoValueAnnotation.getSlotCargoCV(portSlot);
				return calculateExposures(portSlot.getId(), volumeMMBTU, pricePerMMBTU, priceExpression, pricingDate, isLong, cargoCV, lookupData);
			}

		}

		return Collections.emptyList();
	}

	private List<BasicExposureRecord> calculateExposures(final String name, final long volumeMMBTU, final int pricePerMMBTU, final String priceExpression, final LocalDate pricingDate,
			final boolean isLong, final int cargoCV, final ExposuresLookupData lookupData) {

		final List<BasicExposureRecord> result = new ArrayList<>();
		result.add(calculatePhysicalExposure(volumeMMBTU, pricePerMMBTU, pricingDate, isLong));
		result.addAll(calculateFinancialExposures(name, priceExpression, volumeMMBTU, pricingDate, isLong, cargoCV, lookupData));
		return result;
	}

	/**
	 * For unit test use only
	 * 
	 * @param volumeMMBTU
	 * @param priceExpression
	 * @param pricingDate
	 * @param isLong
	 * @param lookupData
	 * @return
	 */
	public List<BasicExposureRecord> calculateExposuresForITS(final long volumeMMBTU, final String priceExpression, final LocalDate pricingDate, final boolean isLong, final int cargoCV,
			final ExposuresLookupData lookupData) {

		final List<BasicExposureRecord> result = new ArrayList<>();
		result.addAll(calculateFinancialExposures("", priceExpression, volumeMMBTU, pricingDate, isLong, cargoCV, lookupData));
		return result;
	}

	private BasicExposureRecord calculatePhysicalExposure(final long volumeMMBTU, final int pricePerMMBTU, final LocalDate arrivalTime, final boolean isLong) {

		final BasicExposureRecord physical = new BasicExposureRecord();
		final long volumeValue = Calculator.costFromVolume(volumeMMBTU, pricePerMMBTU);

		physical.setVolumeMMBTU((isLong ? 1 : -1) * volumeMMBTU);
		physical.setVolumeNative((isLong ? 1 : -1) * volumeMMBTU);
		physical.setUnitPrice(pricePerMMBTU);
		physical.setVolumeValueNative((isLong ? -1 : 1) * volumeValue);
		physical.setVolumeUnit("mmBtu");
		physical.setIndexName("Physical");
		physical.setTime(arrivalTime);
		physical.setCurrencyUnit("$");

		return physical;
	}

	private List<BasicExposureRecord> calculateFinancialExposures(final String name, final String priceExpression, final long volumeMMBTU, final LocalDate pricingDate, //
			final boolean isLong, final int cargoCV, final ExposuresLookupData lookupData) {
		final List<BasicExposureRecord> result = new ArrayList<>();
		if (lookupData != null) {
			final ASTNode node = getExposureCoefficient(priceExpression, lookupData);
			if (node != null) {
				final Collection<BasicExposureRecord> records = createOptimiserExposureRecord(name, node, pricingDate, volumeMMBTU, isLong, cargoCV, lookupData);
				if (!records.isEmpty()) {
					result.addAll(records);
				}
			}
		}
		return result;
	}

	/**
	 * Determines the amount of exposure to a particular index which is created by a
	 * specific contract.
	 * 
	 * @param priceExpression
	 * @param lookupData
	 * @return
	 */
	private ASTNode getExposureCoefficient(final String priceExpression, final @NonNull ExposuresLookupData lookupData) {
		if (priceExpression != null && !priceExpression.isEmpty()) {
			if (!priceExpression.equals("?")) {

				if (lookupData.expressionToNode.containsKey(priceExpression)) {
					return lookupData.expressionToNode.get(priceExpression);
				}

				// Parse the expression
				final ASTNode expressionAST = commodityIndices.parse(priceExpression);
				lookupData.expressionToNode.put(priceExpression, expressionAST);
				return expressionAST;
			}
		}
		return null;
	}

	private Collection<BasicExposureRecord> createOptimiserExposureRecord(final String name, final @NonNull ASTNode node, final LocalDate pricingDate, final long volumeInMMBtu, final boolean isLong,
			final int cargoCV, final ExposuresLookupData lookupData) {
		final List<BasicExposureRecord> results = new LinkedList<>();

		final InputRecord inputRecord = new InputRecord(pricingDate, volumeInMMBtu, cargoCV, lookupData);
		final IExposureNode enode = getExposureNode(node, inputRecord).getSecond();
		if (enode instanceof final ExposureRecords exposureRecords) {
			for (final ExposureRecord record : exposureRecords.records) {

				final BasicExposureRecord exposure = new BasicExposureRecord();

				exposure.setPortSlotName(name);
				exposure.setIndexName(record.curveName);
				exposure.setCurrencyUnit(record.currencyUnit);
				exposure.setVolumeUnit(record.volumeUnit);
				exposure.setTime(record.date);
				exposure.setUnitPrice(record.unitPrice);
				exposure.setVolumeMMBTU(isLong ? -record.mmbtuVolume / 10 : record.mmbtuVolume / 10);
				exposure.setVolumeNative(isLong ? -record.nativeVolume / 10 : record.nativeVolume / 10);
				exposure.setVolumeValueNative(isLong ? -record.nativeValue / 10 : record.nativeValue / 10);

				final BasicHolidayCalendar holidays = lookupData.holidayCalendars.get(record.curveName);
				final BasicPricingCalendar pricingCalendar = lookupData.pricingCalendars.get(record.curveName);
				applyProRataCorrection(pricingCalendar, holidays, exposure);
				results.add(exposure);
			}
		}
		return results;
	}

	static interface IExposureNode {

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
			return new Pair<>(pc0.getFirst() + pc1.getFirst(), merge(c0Records, c1Records, (c_0, c_1) -> new ExposureRecord(c_0.curveName, c_0.currencyUnit, c_0.unitPrice, //
					Math.addExact(c_0.nativeVolume, c_1.nativeVolume), //
					Math.addExact(c_0.nativeValue, c_1.nativeValue), //
					Math.addExact(c_0.mmbtuVolume, c_1.mmbtuVolume), //
					c_0.date, c_0.volumeUnit)));
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
			return new Pair<>(pc0.getFirst() - pc1.getFirst(),
					modify((ExposureRecords) c1, c -> new ExposureRecord(c.curveName, c.currencyUnit, c.unitPrice, -c.nativeVolume, -c.nativeValue, -c.mmbtuVolume, c.date, c.volumeUnit)));
		} else if (c0 instanceof final ExposureRecords c0Records && c1 instanceof final ExposureRecords c1Records) {
			final ExposureRecords newC1 = modify(c1Records, c -> new ExposureRecord(c.curveName, c.currencyUnit, c.unitPrice, -c.nativeVolume, -c.nativeValue, -c.mmbtuVolume, c.date, c.volumeUnit));
			// + is fine here as we have just negated the volumes above
			return new Pair<>(pc0.getFirst() - pc1.getFirst(), merge(c0Records, newC1, (c_0, c_1) -> new ExposureRecord(c_0.curveName, c_0.currencyUnit, c_0.unitPrice, //
					Math.addExact(c_0.nativeVolume, c_1.nativeVolume), //
					Math.addExact(c_0.nativeValue, c_1.nativeValue), //
					Math.addExact(c_0.mmbtuVolume, c_1.mmbtuVolume), //
					c_0.date, c_0.volumeUnit)));
		}
		throw new IllegalStateException();

	}

	public static Pair<Long, IExposureNode> percent(final Pair<Long, IExposureNode> pc0, final Pair<Long, IExposureNode> pc1) {
		final IExposureNode c0 = pc0.getSecond();
		final IExposureNode c1 = pc1.getSecond();

		if (c0 instanceof final Constant c0Const && c1 instanceof final Constant c1Const) {
			return new Pair<>((multiplyConstantByConstant(pc0.getFirst(), pc1.getFirst()) / 100), //
					new Constant((multiplyConstantByConstant(c0Const.getConstant(), c1Const.getConstant()) / 100), ""));
		} else if (c0 instanceof ExposureRecords && c1 instanceof Constant) {
			assert false;
			throw new UnsupportedOperationException();
			// return modify((ExposureRecords) c0, c -> new ExposureRecord(c.index,
			// c.unitPrice, -c.nativeVolume, -c.nativeValue, -c.mmbtuVolume, c.date));
		} else if (c0 instanceof final Constant c0Const && c1 instanceof final ExposureRecords c1Records) {
			final long constant = c0Const.getConstant() / 100;
			return new Pair<>((multiplyConstantByConstant(pc0.getFirst(), pc1.getFirst()) / 100), modify(c1Records, c -> new ExposureRecord(c.curveName, c.currencyUnit, c.unitPrice,
					multiplyVolumeByConstant(c.nativeVolume, constant), multiplyVolumeByConstant(c.nativeValue, constant), multiplyVolumeByConstant(c.mmbtuVolume, constant), c.date, c.volumeUnit)));
		} else if (c0 instanceof final ExposureRecords c0Records && c1 instanceof final ExposureRecords c1Records) {
			// return merge((ExposureRecords) c0, (ExposureRecords) c1, (c_0, c_1) -> new
			// ExposureRecord(c_0.index, c_0.unitPrice, c_0.nativeVolume - c_1.nativeVolume,
			// c_0.nativeValue - c_1.nativeValue, c_0.mmbtuVolume - c_1.mmbtuVolume,
			// c_0.date));
			throw new UnsupportedOperationException();
		}

		throw new IllegalStateException();

	}

	public static Pair<Long, IExposureNode> divide(final Pair<Long, IExposureNode> pc0, final Pair<Long, IExposureNode> pc1) {
		final IExposureNode c0 = pc0.getSecond();
		final IExposureNode c1 = pc1.getSecond();

		if (c0 instanceof final Constant const_c0 && c1 instanceof final Constant const_c1) {
			final long value = (pc1.getFirst() == 0 ? 0 : divideConstantByConstant(pc0.getFirst(), pc1.getFirst()));
			final int newConstant = (int) (const_c1.getConstant() == 0 ? 0 : divideConstantByConstant(const_c0.getConstant(), const_c1.getConstant()));

			return new Pair<>(value, new Constant(newConstant, ""));
		} else if (c0 instanceof final ExposureRecords c0Records && c1 instanceof final Constant const_c1) {
			final long value = pc1.getFirst() == 0 ? 0 : divideVolumeByConstant(pc0.getFirst(), pc1.getFirst());
			final long constant = const_c1.getConstant();
			if (constant == 0.0) {
				return new Pair<>(value, modify(c0Records, c -> new ExposureRecord(c.curveName, c.currencyUnit, c.unitPrice, 0, 0, 0, c.date, c.volumeUnit)));
			} else {
				return new Pair<>(value, modify(c0Records, c -> {
					final long nativeVolume = divideVolumeByConstant(c.nativeVolume, constant);
					final long nativeValue = divideVolumeByConstant(c.nativeValue, constant);
					final long mmbtuVolume = divideVolumeByConstant(c.mmbtuVolume, constant);
					return new ExposureRecord(c.curveName, c.currencyUnit, c.unitPrice, nativeVolume, nativeValue, mmbtuVolume, c.date, c.volumeUnit);
				}));
			}
		} else if (c0 instanceof final Constant const_c0 && c1 instanceof final ExposureRecords c1Records) {
			final long value = (pc1.getFirst() == 0 ? 0 : divideConstantByVolume(pc0.getFirst(), pc1.getFirst()));
			final long constant = const_c0.getConstant();
			if (constant == 0.0) {
				return new Pair<>(value, modify(c1Records, c -> new ExposureRecord(c.curveName, c.currencyUnit, c.unitPrice, 0, 0, 0, c.date, c.volumeUnit)));
			} else {
				return new Pair<>(value, modify(c1Records, c -> {
					final long nativeVolume = c.nativeVolume == 0 ? 0 : divideConstantByVolume(constant, c.nativeVolume);
					final long nativeValue = c.nativeValue == 0 ? 0 : divideConstantByVolume(constant, c.nativeValue);
					final long mmbtuVolume = c.mmbtuVolume == 0 ? 0 : divideConstantByVolume(constant, c.mmbtuVolume);
					return new ExposureRecord(c.curveName, c.currencyUnit, c.unitPrice, nativeVolume, nativeValue, mmbtuVolume, c.date, c.volumeUnit);
				}));
			}
		} else if (c0 instanceof final ExposureRecords c0Records && c1 instanceof final ExposureRecords c1Records) {
			final long value = (pc1.getFirst() == 0 ? 0 : divideVolumeByVolume(pc0.getFirst(), pc1.getFirst()));
			return new Pair<>(value, merge(c0Records, c1Records, (c_0, c_1) -> {
				final long nativeVolume = c_1.nativeVolume == 0 ? 0 : divideVolumeByVolume(c_0.nativeVolume, c_1.nativeVolume) * 10;
				final long nativeValue = c_1.nativeValue == 0 ? 0 : divideVolumeByVolume(c_0.nativeValue, c_1.nativeValue) * 10;
				final long mmbtuVolume = c_1.mmbtuVolume == 0 ? 0 : divideVolumeByVolume(c_0.mmbtuVolume, c_1.mmbtuVolume) * 10;
				return new ExposureRecord(c_0.curveName, c_0.currencyUnit, c_0.unitPrice, nativeVolume, nativeValue, mmbtuVolume, c_0.date, c_0.volumeUnit);
			}));
		}

		throw new IllegalStateException();

	}

	public static Pair<Long, IExposureNode> times(final Pair<Long, IExposureNode> pc0, final Pair<Long, IExposureNode> pc1) {
		final IExposureNode c0 = pc0.getSecond();
		final IExposureNode c1 = pc1.getSecond();

		if (c0 instanceof final Constant c0Const && c1 instanceof final Constant c1Const) {
			return new Pair<>(multiplyConstantByConstant(pc0.getFirst(), pc1.getFirst()), new Constant((int) multiplyConstantByConstant(c0Const.getConstant(), c1Const.getConstant()), ""));
		} else if (c0 instanceof final ExposureRecords c0Records && c1 instanceof final Constant const_c1) {
			final long constant = const_c1.getConstant();
			return new Pair<>(multiplyConstantByConstant(pc0.getFirst(), pc1.getFirst()), modify(c0Records, c -> new ExposureRecord(c.curveName, c.currencyUnit, c.unitPrice,
					multiplyVolumeByConstant(c.nativeVolume, constant), multiplyVolumeByConstant(c.nativeValue, constant), multiplyVolumeByConstant(c.mmbtuVolume, constant), c.date, c.volumeUnit)));
		} else if (c0 instanceof final Constant const_c0 && c1 instanceof final ExposureRecords c1Records) {
			final long constant = const_c0.getConstant();
			return new Pair<>(multiplyConstantByConstant(pc0.getFirst(), pc1.getFirst()), modify(c1Records, c -> new ExposureRecord(c.curveName, c.currencyUnit, c.unitPrice,
					multiplyVolumeByConstant(c.nativeVolume, constant), multiplyVolumeByConstant(c.nativeValue, constant), multiplyVolumeByConstant(c.mmbtuVolume, constant), c.date, c.volumeUnit)));
		} else if (c0 instanceof final ExposureRecords c0Records && c1 instanceof final ExposureRecords c1Records) {
			return new Pair<>(multiplyConstantByConstant(pc0.getFirst(), pc1.getFirst()), merge(c0Records, c1Records, (c_0, c_1) -> new ExposureRecord(c_0.curveName, c_0.currencyUnit, c_0.unitPrice, //
					Calculator.multiply(c_0.nativeVolume * 10L, c_1.nativeVolume), //
					Calculator.multiply(c_0.nativeValue * 10L, c_1.nativeValue), //
					Calculator.multiply(c_0.mmbtuVolume * 10L, c_1.mmbtuVolume), //
					c_0.date, c_0.volumeUnit)));
		}

		throw new IllegalStateException();

	}

	static class Constant implements IExposureNode {

		private final String newVolumeUnit;

		public Constant(final long constant, final String newVolumeUnit) {
			this.constant = constant;
			this.newVolumeUnit = newVolumeUnit;
		}

		public String getNewVolumeUnit() {
			return newVolumeUnit;
		}

		long constant;

		public long getConstant() {
			return constant;
		}

	}

	static class ExposureRecords implements IExposureNode {

		List<ExposureRecord> records = new LinkedList<>();

		public ExposureRecords(final ExposureRecord record) {
			records.add(record);
		}

		public ExposureRecords() {
		}

	}

	static record ExposureRecord(String curveName, String currencyUnit, long unitPrice, long nativeVolume, long nativeValue, long mmbtuVolume, LocalDate date, String volumeUnit) {
	}

	static record InputRecord(LocalDate date, long volumeInMMBTU, int cargoCV, ExposuresLookupData lookupData) {
		InputRecord withDate(final LocalDate d) {
			return new InputRecord(d, volumeInMMBTU(), cargoCV(), lookupData());
		}

		InputRecord withVolume(final long v) {
			return new InputRecord(date(), v, cargoCV(), lookupData());
		}
	}

	private @NonNull Pair<Long, IExposureNode> getExposureNode(final @NonNull ASTNode node, final InputRecord inputRecord) {
		final LocalDate date = inputRecord.date;
		final int dayOfMonth = inputRecord.date.getDayOfMonth();
		if (node instanceof final ShiftFunctionASTNode shiftNode) {
			final LocalDate pricingDate = shiftNode.mapTime(date.atStartOfDay()).toLocalDate();
			return getExposureNode(shiftNode.getToShift(), inputRecord.withDate(pricingDate));
		} else if (node instanceof final MonthFunctionASTNode monthNode) {
			final LocalDate pricingDate = monthNode.mapTime(date.atStartOfDay()).toLocalDate();
			return getExposureNode(monthNode.getSeries(), inputRecord.withDate(pricingDate));
		} else if (node instanceof final DatedAvgFunctionASTNode averageNode) {

			LocalDate startDate = averageNode.mapTimeToStartDate(date.atStartOfDay()).toLocalDate();

			final int months = averageNode.getMonths();
			final ExposureRecords records = new ExposureRecords();
			long price = 0;
			for (int i = 0; i < averageNode.getMonths(); ++i) {
				final Pair<Long, IExposureNode> p = getExposureNode(averageNode.getSeries(), inputRecord.withDate(startDate.plusMonths(i)));
				if (p.getSecond() instanceof ExposureRecords result) {
					price = Math.addExact(price, p.getFirst());
					result = modify(result,
							c -> new ExposureRecord(c.curveName, c.currencyUnit, c.unitPrice, c.nativeVolume / months, c.nativeValue / months, c.mmbtuVolume / months, c.date, c.volumeUnit));
					records.records.addAll(result.records);
				} else if (p.getSecond() instanceof Constant) {
					return new Pair<>(p.getFirst(), p.getSecond());
				}
			}

			return new Pair<>(price / months, records);
		} else if (node instanceof final VolumeTierASTNode volumeTierNode) {
			final long totalVolumeInMMBTU = inputRecord.volumeInMMBTU;
			long lowVolumeInMMBTU;
			long highVolumeInMMBTU;
			if (volumeTierNode.isM3Volume()) {
				final long thresholdInM3 = OptimiserUnitConvertor.convertToInternalVolume(volumeTierNode.getThreshold());
				final long thresholdInMMBtu = Calculator.convertM3ToMMBTu(thresholdInM3, inputRecord.cargoCV());

				lowVolumeInMMBTU = Math.min(totalVolumeInMMBTU, thresholdInMMBtu);
				highVolumeInMMBTU = Math.max(0, lowVolumeInMMBTU);
			} else {
				lowVolumeInMMBTU = Math.min(totalVolumeInMMBTU, OptimiserUnitConvertor.convertToInternalVolume(volumeTierNode.getThreshold()));
				highVolumeInMMBTU = Math.max(0, lowVolumeInMMBTU);
			}

			final ExposureRecords records = new ExposureRecords();
			long value = 0;
			{
				final Pair<Long, IExposureNode> p = getExposureNode(volumeTierNode.getLowTier(), inputRecord.withVolume(lowVolumeInMMBTU));

				// Shortcut if there is no high tier volume
				if (highVolumeInMMBTU == 0) {
					return p;
				}

				if (p.getSecond() instanceof final ExposureRecords result) {
					value += Calculator.costFromConsumption(lowVolumeInMMBTU, p.getFirst());
					records.records.addAll(result.records);
				} else if (p.getSecond() instanceof Constant) {
					return new Pair<>(p.getFirst(), p.getSecond());
				}
			}
			{
				final Pair<Long, IExposureNode> p = getExposureNode(volumeTierNode.getHighTier(), inputRecord.withVolume(highVolumeInMMBTU));
				if (p.getSecond() instanceof final ExposureRecords result) {
					value += Calculator.costFromConsumption(highVolumeInMMBTU, p.getFirst());
					records.records.addAll(result.records);
				} else if (p.getSecond() instanceof Constant) {
					return new Pair<>(p.getFirst(), p.getSecond());
				}
			}
			// Compute weighted price
			return new Pair<>((long) Calculator.getPerMMBTuFromTotalAndVolumeInMMBTu(value, inputRecord.volumeInMMBTU), records);
		} else if (node instanceof final SCurveFunctionASTNode scurveNode) {

			final Pair<Long, IExposureNode> baseNodeData = getExposureNode(scurveNode.getBase(), inputRecord);

			final double baseValue = baseNodeData.getFirst() / (double) Calculator.HighScaleFactor;
			final double lowCheck = scurveNode.getFirstThreshold();
			final double midCheck = scurveNode.getSecondThreshold();
			var selected = scurveNode.select(baseValue, lowCheck, midCheck);
			return switch (selected) {
			case LOW -> getExposureNode(scurveNode.getLowerSeries(), inputRecord);
			case MID -> getExposureNode(scurveNode.getMiddleSeries(), inputRecord);
			case HIGH -> getExposureNode(scurveNode.getHigherSeries(), inputRecord);
			};
		} else if (node instanceof final TierFunctionASTNode tierNode) {
			final Pair<Long, IExposureNode> baseNodeData = getExposureNode(tierNode.getTarget(), inputRecord);

			final double baseValue = baseNodeData.getFirst() / (double) Calculator.HighScaleFactor;
			final double lowCheck = tierNode.getLow().doubleValue();
			final double midCheck = tierNode.getMid().doubleValue();
			var selected = tierNode.select(baseValue, lowCheck, midCheck);
			return switch (selected) {
			case LOW -> getExposureNode(tierNode.getLowValue(), inputRecord);
			case MID -> getExposureNode(tierNode.getMidValue(), inputRecord);
			case HIGH -> getExposureNode(tierNode.getHighValue(), inputRecord);
			};

		} else if (node instanceof final ConstantASTNode constantNode) {
			final long constant = OptimiserUnitConvertor.convertToInternalPrice(constantNode.getConstant().doubleValue());
			return new Pair<>(constant, new Constant(constant, ""));
		} else if (node instanceof final SplitMonthFunctionASTNode splitNode) {
			if (dayOfMonth < splitNode.getSplitPoint()) {
				final Pair<Long, IExposureNode> pc0 = getExposureNode(splitNode.getSeries1(), inputRecord);
				return pc0;
			} else {
				final Pair<Long, IExposureNode> pc1 = getExposureNode(splitNode.getSeries2(), inputRecord);
				return pc1;
			}
		}
		// Arithmetic operator token
		else if (node instanceof final OperatorASTNode operatorNode) {

			final var operator = operatorNode.getOperator();
			final Pair<Long, IExposureNode> pc0 = getExposureNode(operatorNode.getLHS(), inputRecord);
			final Pair<Long, IExposureNode> pc1 = getExposureNode(operatorNode.getRHS(), inputRecord);

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

		} else if (node instanceof final NamedSeriesASTNode namedSeriesNode) {
			final String name = namedSeriesNode.getName().toLowerCase();
			if (inputRecord.lookupData.commodityMap.containsKey(name)) {
				final BasicCommodityCurveData curve = inputRecord.lookupData.commodityMap.get(name);
				if (curve.isSetExpression()) {
					final ASTNode node2 = getExposureCoefficient(curve.getExpression(), inputRecord.lookupData);
					return getExposureNode(node2, inputRecord);
				}

				// Should really look up actual value from curve...
				// Lazy commodity curves should be initialised by now.
				final ISeries series = commodityIndices.getSeries(name).get();
				// The series is in EXTERNAL format
				final Number evaluate = series.evaluate(Hours.between(externalDateProvider.getEarliestTime().toLocalDate(), date));

				final long unitPrice = OptimiserUnitConvertor.convertToInternalPrice(evaluate.doubleValue());
				long nativeVolume = inputRecord.volumeInMMBTU * 10;
				long volumeInMMBTU = inputRecord.volumeInMMBTU * 10;

				// Perform units conversion.
				for (final BasicUnitConversionData factor : inputRecord.lookupData.conversionMap.values()) {
					if (factor.getTo().equalsIgnoreCase(MMBTU)) {
						if (factor.getFrom().equalsIgnoreCase(curve.getVolumeUnit())) {
							if (LicenseFeatures.isPermitted(KnownFeatures.FEATURE_EXPOSURES_IGNORE_ENERGY_CONVERSION)) {
								// If we are in this situation, that means, that volume is *ACTUALLY* in native
								// units
								volumeInMMBTU /= factor.getFactor();
							} else {
								// Long * a double appears to get to Long.MAX_VALUE if we overflow
								nativeVolume *= factor.getFactor();
								if (nativeVolume == Long.MAX_VALUE) {
									throw new ArithmeticException("long overflow");
								}
							}
							break;
						}
					}
				}

				// Client S has a curve (units just $) with values of ~256 causing an overflow.
				// The formulas end up being something like XXX + (0.5 * (256 / 255)) so in
				// practise end up being a small adder on the rest of the price expression.
				// We don't normally want to do this, but an exception is made here to use
				// BigInteger arithmetic to handle the situation
				long costFromVolume;
				try {
					costFromVolume = Calculator.costFromVolume(nativeVolume, unitPrice);
				} catch (final ArithmeticException e) {
					costFromVolume = BigInteger.valueOf(nativeVolume) //
							.multiply(BigInteger.valueOf(unitPrice)) //
							.divide(BigInteger.valueOf(Calculator.HighScaleFactor)) //
							.longValueExact();

				}

				final ExposureRecord record = new ExposureRecord(curve.getName(), curve.getCurrencyUnit(), unitPrice, nativeVolume, costFromVolume, volumeInMMBTU, date, curve.getVolumeUnit());
				return new Pair<>(unitPrice, new ExposureRecords(record));
			} else if (inputRecord.lookupData.currencyList.stream().anyMatch(x -> x.equals(name))) {
				// Should really look up actual value from curve...
				// Currency curves should not be lazy
				final ISeries series = currencyIndices.getSeries(name).get();
				final Number evaluate = series.evaluate(Hours.between(externalDateProvider.getEarliestTime().toLocalDate(), date));
				final long unitPrice = OptimiserUnitConvertor.convertToInternalPrice(evaluate.doubleValue());
				return new Pair<>(unitPrice, new Constant(1_000_000, ""));
			} else if (inputRecord.lookupData.conversionMap.containsKey(name)) {
				final BasicUnitConversionData factor = inputRecord.lookupData.conversionMap.get(name);
				return new Pair<>((long) (factor.getFactor() * Calculator.HighScaleFactor), new Constant(1_000_000, factor.getTo()));
			} else if (inputRecord.lookupData.reverseConversionMap.containsKey(name)) {
				final BasicUnitConversionData factor = inputRecord.lookupData.reverseConversionMap.get(name);
				return new Pair<>((long) (factor.getFactor() * Calculator.HighScaleFactor), new Constant(1_000_000, factor.getTo()));
			} else {
				throw new IllegalStateException();
			}

		} else if (node instanceof final FunctionASTNode functionNode) {
			if (functionNode.getFunctionType() == FunctionType.MAX) {
				Pair<Long, IExposureNode> best = getExposureNode(functionNode.getArguments().get(0), inputRecord);
				for (int i = 1; i < functionNode.getArguments().size(); ++i) {
					final Pair<Long, IExposureNode> pc = getExposureNode(functionNode.getArguments().get(i), inputRecord);
					if (pc.getFirst() > best.getFirst()) {
						best = pc;
					}
				}
				return best;
			} else if (functionNode.getFunctionType() == FunctionType.MIN) {
				Pair<Long, IExposureNode> best = getExposureNode(functionNode.getArguments().get(0), inputRecord);
				for (int i = 1; i < functionNode.getArguments().size(); ++i) {
					final Pair<Long, IExposureNode> pc = getExposureNode(functionNode.getArguments().get(i), inputRecord);
					if (pc.getFirst() < best.getFirst()) {
						best = pc;
					}
				}
				return best;
			} else {
				throw new IllegalStateException();
			}
		}

		throw new IllegalStateException("Unexpected node type");
	}

	private static ExposureRecords merge(final ExposureRecords c0, final ExposureRecords c1, final BiFunction<ExposureRecord, ExposureRecord, ExposureRecord> mapper) {
		final ExposureRecords n = new ExposureRecords();
		final Iterator<ExposureRecord> c0Itr = c0.records.iterator();
		LOOP_C0: while (c0Itr.hasNext()) {
			final ExposureRecord c_c0 = c0Itr.next();
			final Iterator<ExposureRecord> c1Itr = c1.records.iterator();
			while (c1Itr.hasNext()) {
				final ExposureRecord c_c1 = c1Itr.next();
				if (c_c0.curveName.equalsIgnoreCase(c_c1.curveName)) {
					if (c_c0.date.equals(c_c1.date)) {
						if (Objects.equals(c_c0.volumeUnit, c_c1.volumeUnit)) {
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

	private static ExposureRecords modify(final ExposureRecords c0, final Function<ExposureRecord, ExposureRecord> mapper) {
		final ExposureRecords n = new ExposureRecords();
		for (final ExposureRecord c : c0.records) {
			n.records.add(mapper.apply(c));
		}
		return n;
	}

	private static long multiplyVolumeByConstant(final long volume, final long constant) {
		try {
			return Calculator.costFromVolume(volume, constant);
		} catch (final ArithmeticException e) {
			return BigInteger.valueOf(volume) //
					.multiply(BigInteger.valueOf(constant)) //
					.divide(BigInteger.valueOf(Calculator.HighScaleFactor)) //
					.longValueExact();
		}
	}

	private static long divideVolumeByConstant(final long volume, final long constant) {
		try {
			return Math.multiplyExact(volume, Calculator.HighScaleFactor) / constant;
		} catch (final ArithmeticException e) {
			return BigInteger.valueOf(volume) //
					.multiply(BigInteger.valueOf(Calculator.HighScaleFactor)) //
					.divide(BigInteger.valueOf(constant)) //
					.longValueExact();
		}
	}

	private static long divideConstantByVolume(final long constant, final long volume) {
		try {
			return Math.multiplyExact(constant, Calculator.ScaleFactor) / volume;
		} catch (final ArithmeticException e) {
			return BigInteger.valueOf(constant) //
					.multiply(BigInteger.valueOf(Calculator.ScaleFactor)) //
					.divide(BigInteger.valueOf(volume)) //
					.longValueExact();
		}
	}

	private static long divideVolumeByVolume(final long volumeA, final long volumeB) {
		try {
			return Math.multiplyExact(volumeA, Calculator.ScaleFactor) / volumeB;
		} catch (final ArithmeticException e) {
			return BigInteger.valueOf(volumeA) //
					.multiply(BigInteger.valueOf(Calculator.ScaleFactor)) //
					.divide(BigInteger.valueOf(volumeB)) //
					.longValueExact();
		}
	}

	private static long divideConstantByConstant(final long constantA, final long constantB) {
		try {
			return Math.multiplyExact(constantA, Calculator.HighScaleFactor) / constantB;
		} catch (final ArithmeticException e) {
			return BigInteger.valueOf(constantA) //
					.multiply(BigInteger.valueOf(Calculator.HighScaleFactor)) //
					.divide(BigInteger.valueOf(constantB)) //
					.longValueExact();
		}
	}

	private static long multiplyConstantByConstant(final long constantA, final long constantB) {
		try {
			return Math.multiplyExact(constantA, constantB) / Calculator.HighScaleFactor;
		} catch (final ArithmeticException e) {
			return BigInteger.valueOf(constantA) //
					.multiply(BigInteger.valueOf(constantB)) //
					.divide(BigInteger.valueOf(Calculator.HighScaleFactor)) //
					.longValueExact();
		}
	}

	private void applyProRataCorrection(final BasicPricingCalendar pricingCalendar, final BasicHolidayCalendar holidays, final BasicExposureRecord exposure) {
		if (pricingCalendar == null && holidays == null) {
			return;
		}
		final YearMonth month = YearMonth.from(exposure.getTime());
		BasicPricingCalendarEntry pricingEntry = null;
		if (pricingCalendar != null) {
			for (final var entry : pricingCalendar.getEntries()) {
				if (month.equals(entry.getMonth())) {
					pricingEntry = entry;
				}
			}
		}
		{
			final double proRataCorrection = getProRataCoefficient(pricingEntry, holidays, LocalDate.of(2000, 1, 1));
			final long correction = OptimiserUnitConvertor.convertToInternalConversionFactor(proRataCorrection);
			exposure.setVolumeMMBTU(multiplyVolumeByConstant(exposure.getVolumeMMBTU(), correction));
			exposure.setVolumeNative(multiplyVolumeByConstant(exposure.getVolumeNative(), correction));
			exposure.setVolumeValueNative(multiplyVolumeByConstant(exposure.getVolumeValueNative(), correction));
		}
	}

	private double getProRataCoefficient(final BasicPricingCalendarEntry pricingCalendarEntry, final BasicHolidayCalendar holidays, final LocalDate cutoff) {
		double i = 1.0;
		double k = 1.0;
		// workdays
		if (pricingCalendarEntry != null) {
			final LocalDate co;
			if (cutoff.isBefore(pricingCalendarEntry.getStart())) {
				co = pricingCalendarEntry.getStart();
			} else {
				co = cutoff;
			}
			for (LocalDate c = co; c.isBefore(pricingCalendarEntry.getEnd().plusDays(1)); c = c.plusDays(1)) {
				if (c.getDayOfWeek() != DayOfWeek.SATURDAY && c.getDayOfWeek() != DayOfWeek.SUNDAY) {
					i += 1.0;
				}
			}
			for (LocalDate c = pricingCalendarEntry.getStart(); c.isBefore(pricingCalendarEntry.getEnd().plusDays(1)); c = c.plusDays(1)) {
				if (c.getDayOfWeek() != DayOfWeek.SATURDAY && c.getDayOfWeek() != DayOfWeek.SUNDAY) {
					k += 1.0;
				}
			}
			if (holidays != null && !holidays.getHolidays().isEmpty()) {
				// extra holidays
				for (final LocalDate hce : holidays.getHolidays()) {
					if (hce.isAfter(co) //
							&& hce.isBefore(pricingCalendarEntry.getEnd())) {
						i -= 1.0;
					}
					if (hce.isAfter(pricingCalendarEntry.getStart()) //
							&& hce.isBefore(pricingCalendarEntry.getEnd())) {
						k -= 1.0;
					}
				}
			}
		}
		return i / k;
	}
}
