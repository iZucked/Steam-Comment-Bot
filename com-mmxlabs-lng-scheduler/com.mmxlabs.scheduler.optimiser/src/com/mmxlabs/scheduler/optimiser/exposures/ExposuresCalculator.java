/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2022
 * All rights reserved.
 */
/**
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.exposures;

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
import com.mmxlabs.common.parser.astnodes.NamedSeriesASTNode;
import com.mmxlabs.common.parser.astnodes.Operator;
import com.mmxlabs.common.parser.astnodes.OperatorASTNode;
import com.mmxlabs.common.parser.astnodes.SCurveFunctionASTNode;
import com.mmxlabs.common.parser.astnodes.ShiftFunctionASTNode;
import com.mmxlabs.common.parser.astnodes.SplitMonthFunctionASTNode;
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

	static final long HighScaleFactor = 1_000_000;
	static final int ScaleFactor = 1_000;

	public List<BasicExposureRecord> calculateExposures(final ICargoValueAnnotation cargoValueAnnotation, final IPortSlot portSlot) {
		if (exposuresEnabled) {
			// only support one load and one discharge
			assert cargoValueAnnotation.getSlots().size() == 2;
			final IPortSlot dischargeOption = cargoValueAnnotation.getSlots().get(1);

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
				pricingTime = pricingEventHelper.getLoadPricingDate((ILoadOption) portSlot, (IDischargeOption) dischargeOption, cargoValueAnnotation);
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

		final InputRecord inputRecord = new InputRecord();
		inputRecord.volumeInMMBTU = volumeInMMBtu;
		final IExposureNode enode = getExposureNode(inputRecord, node, pricingDate, cargoCV, lookupData).getSecond();
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

	public static Pair<Integer, IExposureNode> add(Pair<Integer, IExposureNode> pc0, Pair<Integer, IExposureNode> pc1) {
		final IExposureNode c0 = pc0.getSecond();
		final IExposureNode c1 = pc1.getSecond();

		if (c0 instanceof Constant c0Const && c1 instanceof Constant c1Const) {
			return new Pair<>(pc0.getFirst() + pc1.getFirst(), new Constant(c0Const.getConstant() + c1Const.getConstant(), ""));
		} else if (c0 instanceof ExposureRecords && c1 instanceof Constant) {
			return new Pair<>(pc0.getFirst() + pc1.getFirst(), c0);
		} else if (c0 instanceof Constant && c1 instanceof ExposureRecords) {
			return new Pair<>(pc0.getFirst() + pc1.getFirst(), c1);
		} else if (c0 instanceof ExposureRecords c0Records && c1 instanceof ExposureRecords c1Records) {
			return new Pair<>(pc0.getFirst() + pc1.getFirst(), merge(c0Records, c1Records, (c_0, c_1) -> new ExposureRecord(c_0.curveName, c_0.currencyUnit, c_0.unitPrice,
					c_0.nativeVolume + c_1.nativeVolume, c_0.nativeValue + c_1.nativeValue, c_0.mmbtuVolume + c_1.mmbtuVolume, c_0.date, c_0.volumeUnit)));
		}
		throw new IllegalStateException();
	}

	public static Pair<Integer, IExposureNode> subtract(Pair<Integer, IExposureNode> pc0, Pair<Integer, IExposureNode> pc1) {
		final IExposureNode c0 = pc0.getSecond();
		final IExposureNode c1 = pc1.getSecond();

		if (c0 instanceof Constant c0Const && c1 instanceof Constant c1Const) {
			return new Pair<>(pc0.getFirst() - pc1.getFirst(), new Constant(c0Const.getConstant() - c1Const.getConstant(), ""));
		} else if (c0 instanceof ExposureRecords && c1 instanceof Constant) {
			return new Pair<>(pc0.getFirst() - pc1.getFirst(), c0);
		} else if (c0 instanceof Constant && c1 instanceof ExposureRecords) {
			return new Pair<>(pc0.getFirst() - pc1.getFirst(),
					modify((ExposureRecords) c1, c -> new ExposureRecord(c.curveName, c.currencyUnit, c.unitPrice, -c.nativeVolume, -c.nativeValue, -c.mmbtuVolume, c.date, c.volumeUnit)));
		} else if (c0 instanceof ExposureRecords c0Records && c1 instanceof ExposureRecords c1Records) {
			final ExposureRecords newC1 = modify(c1Records, c -> new ExposureRecord(c.curveName, c.currencyUnit, c.unitPrice, -c.nativeVolume, -c.nativeValue, -c.mmbtuVolume, c.date, c.volumeUnit));
			// + is fine here as we have just negated the volumes above
			return new Pair<>(pc0.getFirst() - pc1.getFirst(), merge(c0Records, newC1, (c_0, c_1) -> new ExposureRecord(c_0.curveName, c_0.currencyUnit, c_0.unitPrice,
					c_0.nativeVolume + c_1.nativeVolume, c_0.nativeValue + c_1.nativeValue, c_0.mmbtuVolume + c_1.mmbtuVolume, c_0.date, c_0.volumeUnit)));
		}
		throw new IllegalStateException();

	}

	public static Pair<Integer, IExposureNode> percent(Pair<Integer, IExposureNode> pc0, Pair<Integer, IExposureNode> pc1) {
		final IExposureNode c0 = pc0.getSecond();
		final IExposureNode c1 = pc1.getSecond();

		if (c0 instanceof Constant && c1 instanceof Constant) {
			return new Pair<>((int) (multiplyConstantByConstant(pc0.getFirst(), pc1.getFirst()) / 100), //
					new Constant((int) (multiplyConstantByConstant(((Constant) c0).getConstant(), ((Constant) c1).getConstant()) / 100), ""));
		} else if (c0 instanceof ExposureRecords && c1 instanceof Constant) {
			assert false;
			throw new UnsupportedOperationException();
			// return modify((ExposureRecords) c0, c -> new ExposureRecord(c.index,
			// c.unitPrice, -c.nativeVolume, -c.nativeValue, -c.mmbtuVolume, c.date));
		} else if (c0 instanceof Constant && c1 instanceof ExposureRecords) {
			final int constant = ((Constant) c0).getConstant() / 100;
			return new Pair<>((int) (multiplyConstantByConstant(pc0.getFirst(), pc1.getFirst()) / 100), modify((ExposureRecords) c1, c -> new ExposureRecord(c.curveName, c.currencyUnit, c.unitPrice,
					multiplyVolumeByConstant(c.nativeVolume, constant), multiplyVolumeByConstant(c.nativeValue, constant), multiplyVolumeByConstant(c.mmbtuVolume, constant), c.date, c.volumeUnit)));
		} else if (c0 instanceof ExposureRecords c0Records && c1 instanceof ExposureRecords c1Records) {
			// return merge((ExposureRecords) c0, (ExposureRecords) c1, (c_0, c_1) -> new
			// ExposureRecord(c_0.index, c_0.unitPrice, c_0.nativeVolume - c_1.nativeVolume,
			// c_0.nativeValue - c_1.nativeValue, c_0.mmbtuVolume - c_1.mmbtuVolume,
			// c_0.date));
			throw new UnsupportedOperationException();
		}

		throw new IllegalStateException();

	}

	public static Pair<Integer, IExposureNode> divide(Pair<Integer, IExposureNode> pc0, Pair<Integer, IExposureNode> pc1) {
		final IExposureNode c0 = pc0.getSecond();
		final IExposureNode c1 = pc1.getSecond();

		if (c0 instanceof Constant && c1 instanceof Constant) {
			final int value = (int) (pc1.getFirst() == 0 ? 0 : divideConstantByConstant(pc0.getFirst(), pc1.getFirst()));
			final Constant const_c0 = (Constant) c0;
			final Constant const_c1 = (Constant) c1;
			final int newConstant = (int) (const_c1.getConstant() == 0 ? 0 : divideConstantByConstant(const_c0.getConstant(), const_c1.getConstant()));

			return new Pair<>(value, new Constant(newConstant, ""));
		} else if (c0 instanceof ExposureRecords && c1 instanceof Constant) {
			final int value = pc1.getFirst() == 0 ? 0 : (int) divideVolumeByConstant(pc0.getFirst(), pc1.getFirst());
			final Constant const_c1 = (Constant) c1;

			final int constant = const_c1.getConstant();
			if (constant == 0.0) {
				return new Pair<>(value, modify((ExposureRecords) c0, c -> new ExposureRecord(c.curveName, c.currencyUnit, c.unitPrice, 0, 0, 0, c.date, c.volumeUnit)));
			} else {
				return new Pair<>(value, modify((ExposureRecords) c0, c -> {
					final long nativeVolume = divideVolumeByConstant(c.nativeVolume, constant);
					final long nativeValue = divideVolumeByConstant(c.nativeValue, constant);
					final long mmbtuVolume = divideVolumeByConstant(c.mmbtuVolume, constant);
					return new ExposureRecord(c.curveName, c.currencyUnit, c.unitPrice, nativeVolume, nativeValue, mmbtuVolume, c.date, c.volumeUnit);
				}));
			}
		} else if (c0 instanceof Constant && c1 instanceof ExposureRecords) {
			final int value = (int) (pc1.getFirst() == 0 ? 0 : divideConstantByVolume(pc0.getFirst(), pc1.getFirst()));
			final Constant const_c0 = (Constant) c0;

			final int constant = const_c0.getConstant();
			if (constant == 0.0) {
				return new Pair<>(value, modify((ExposureRecords) c0, c -> new ExposureRecord(c.curveName, c.currencyUnit, c.unitPrice, 0, 0, 0, c.date, c.volumeUnit)));
			} else {
				return new Pair<>(value, modify((ExposureRecords) c1, c -> {
					final long nativeVolume = c.nativeVolume == 0 ? 0 : divideConstantByVolume(constant, c.nativeVolume);
					final long nativeValue = c.nativeValue == 0 ? 0 : divideConstantByVolume(constant, c.nativeValue);
					final long mmbtuVolume = c.mmbtuVolume == 0 ? 0 : divideConstantByVolume(constant, c.mmbtuVolume);
					return new ExposureRecord(c.curveName, c.currencyUnit, c.unitPrice, nativeVolume, nativeValue, mmbtuVolume, c.date, c.volumeUnit);
				}));
			}
		} else if (c0 instanceof ExposureRecords c0Records && c1 instanceof ExposureRecords c1Records) {
			final int value = (int) (pc1.getFirst() == 0 ? 0 : divideVolumeByVolume(pc0.getFirst(), pc1.getFirst()));
			return new Pair<>(value, merge((ExposureRecords) c0, (ExposureRecords) c1, (c_0, c_1) -> {
				final long nativeVolume = c_1.nativeVolume == 0 ? 0 : divideVolumeByVolume(c_0.nativeVolume, c_1.nativeVolume) * 10;
				final long nativeValue = c_1.nativeValue == 0 ? 0 : divideVolumeByVolume(c_0.nativeValue, c_1.nativeValue) * 10;
				final long mmbtuVolume = c_1.mmbtuVolume == 0 ? 0 : divideVolumeByVolume(c_0.mmbtuVolume, c_1.mmbtuVolume) * 10;
				return new ExposureRecord(c_0.curveName, c_0.currencyUnit, c_0.unitPrice, nativeVolume, nativeValue, mmbtuVolume, c_0.date, c_0.volumeUnit);
			}));
		}

		throw new IllegalStateException();

	}

	public static Pair<Integer, IExposureNode> times(Pair<Integer, IExposureNode> pc0, Pair<Integer, IExposureNode> pc1) {
		final IExposureNode c0 = pc0.getSecond();
		final IExposureNode c1 = pc1.getSecond();

		if (c0 instanceof Constant c0Const && c1 instanceof Constant c1Const) {
			return new Pair<>((int) multiplyConstantByConstant(pc0.getFirst(), pc1.getFirst()), new Constant((int) multiplyConstantByConstant(c0Const.getConstant(), c1Const.getConstant()), ""));
		} else if (c0 instanceof ExposureRecords c0Records && c1 instanceof Constant const_c1) {
			final int constant = const_c1.getConstant();
			return new Pair<>((int) multiplyConstantByConstant(pc0.getFirst(), pc1.getFirst()), modify(c0Records, c -> new ExposureRecord(c.curveName, c.currencyUnit, c.unitPrice,
					multiplyVolumeByConstant(c.nativeVolume, constant), multiplyVolumeByConstant(c.nativeValue, constant), multiplyVolumeByConstant(c.mmbtuVolume, constant), c.date, c.volumeUnit)));
		} else if (c0 instanceof Constant const_c0 && c1 instanceof ExposureRecords c1Records) {
			final int constant = const_c0.getConstant();
			return new Pair<>((int) multiplyConstantByConstant(pc0.getFirst(), pc1.getFirst()), modify(c1Records, c -> new ExposureRecord(c.curveName, c.currencyUnit, c.unitPrice,
					multiplyVolumeByConstant(c.nativeVolume, constant), multiplyVolumeByConstant(c.nativeValue, constant), multiplyVolumeByConstant(c.mmbtuVolume, constant), c.date, c.volumeUnit)));
		} else if (c0 instanceof ExposureRecords c0Records && c1 instanceof ExposureRecords c1Records) {
			return new Pair<>((int) multiplyConstantByConstant(pc0.getFirst(), pc1.getFirst()), merge(c0Records, c1Records, (c_0, c_1) -> new ExposureRecord(c_0.curveName, c_0.currencyUnit,
					c_0.unitPrice, (c_0.nativeVolume * c_1.nativeVolume) / 10, (c_0.nativeValue * c_1.nativeValue) / 10, (c_0.mmbtuVolume * c_1.mmbtuVolume) / 10, c_0.date, c_0.volumeUnit)));
		}

		throw new IllegalStateException();

	}

	static class Constant implements IExposureNode {

		private final String newVolumeUnit;

		public Constant(final int constant, final String newVolumeUnit) {
			this.constant = constant;
			this.newVolumeUnit = newVolumeUnit;
		}

		public String getNewVolumeUnit() {
			return newVolumeUnit;
		}

		int constant;

		public int getConstant() {
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

	static record ExposureRecord(String curveName, String currencyUnit, int unitPrice, long nativeVolume, long nativeValue, long mmbtuVolume, LocalDate date, String volumeUnit) {
	}

	static class InputRecord {
		long volumeInMMBTU;

		public InputRecord() {

		}

		public InputRecord(final long value) {
			volumeInMMBTU = value;
		}
	}

	private @NonNull Pair<Integer, IExposureNode> getExposureNode(final InputRecord inputRecord, final @NonNull ASTNode node, final LocalDate date, final int cargoCV,
			final ExposuresLookupData lookupData) {
		final int dayOfMonth = date.getDayOfMonth();
		if (node instanceof final ShiftFunctionASTNode shiftNode) {
			return getExposureNode(inputRecord, shiftNode.getToShift(), date.minusMonths(shiftNode.getShiftBy()), cargoCV, lookupData);
		} else if (node instanceof final DatedAvgFunctionASTNode averageNode) {

			LocalDate startDate = date.minusMonths(averageNode.getMonths());
			if (averageNode.getReset() != 1) {
				startDate = startDate.minusMonths((date.getMonthValue() - 1) % averageNode.getReset());
			}
			startDate = startDate.minusMonths(averageNode.getLag());
			final int months = averageNode.getMonths();
			final ExposureRecords records = new ExposureRecords();
			int price = 0;
			for (int i = 0; i < averageNode.getMonths(); ++i) {
				final Pair<Integer, IExposureNode> p = getExposureNode(inputRecord, averageNode.getSeries(), startDate.plusMonths(i), cargoCV, lookupData);
				if (p.getSecond() instanceof ExposureRecords result) {
					price += p.getFirst();
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
				final long thresholdInMMBtu = Calculator.convertM3ToMMBTu(thresholdInM3, cargoCV);

				lowVolumeInMMBTU = Math.min(totalVolumeInMMBTU, thresholdInMMBtu);
				highVolumeInMMBTU = Math.max(0, lowVolumeInMMBTU);
			} else {
				lowVolumeInMMBTU = Math.min(totalVolumeInMMBTU, OptimiserUnitConvertor.convertToInternalVolume(volumeTierNode.getThreshold()));
				highVolumeInMMBTU = Math.max(0, lowVolumeInMMBTU);
			}

			final ExposureRecords records = new ExposureRecords();
			long value = 0;
			{
				final Pair<Integer, IExposureNode> p = getExposureNode(new InputRecord(lowVolumeInMMBTU), volumeTierNode.getLowTier(), date, cargoCV, lookupData);

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
				final Pair<Integer, IExposureNode> p = getExposureNode(new InputRecord(highVolumeInMMBTU), volumeTierNode.getHighTier(), date, cargoCV, lookupData);
				if (p.getSecond() instanceof final ExposureRecords result) {
					value += Calculator.costFromConsumption(highVolumeInMMBTU, p.getFirst());
					records.records.addAll(result.records);
				} else if (p.getSecond() instanceof Constant) {
					return new Pair<>(p.getFirst(), p.getSecond());
				}
			}
			// Compute weighted price
			return new Pair<>(Calculator.getPerMMBTuFromTotalAndVolumeInMMBTu(value, inputRecord.volumeInMMBTU), records);
		} else if (node instanceof final SCurveFunctionASTNode scurveNode) {

			final Pair<Integer, IExposureNode> baseNodeData = getExposureNode(inputRecord, scurveNode.getBase(), date, cargoCV, lookupData);

			ASTNode selectedOption;
			final double foo = baseNodeData.getFirst() / (double) HighScaleFactor;
			if (foo < scurveNode.getFirstThreshold()) {
				selectedOption = scurveNode.getLowerSeries();
			} else if (foo > scurveNode.getSecondThreshold()) {
				selectedOption = scurveNode.getHigherSeries();
			} else {
				selectedOption = scurveNode.getMiddleSeries();
			}

			return getExposureNode(inputRecord, selectedOption, date, cargoCV, lookupData);
		} else if (node instanceof final ConstantASTNode constantNode) {
			final int constant = OptimiserUnitConvertor.convertToInternalPrice(constantNode.getConstant().doubleValue());
			return new Pair<>(constant, new Constant(constant, ""));
		} else if (node instanceof final SplitMonthFunctionASTNode splitNode) {
			if (dayOfMonth < splitNode.getSplitPoint()) {
				final Pair<Integer, IExposureNode> pc0 = getExposureNode(inputRecord, splitNode.getSeries1(), date, cargoCV, lookupData);
				return pc0;
			} else {
				final Pair<Integer, IExposureNode> pc1 = getExposureNode(inputRecord, splitNode.getSeries2(), date, cargoCV, lookupData);
				return pc1;
			}
		}
		// Arithmetic operator token
		else if (node instanceof final OperatorASTNode operatorNode) {

			final var operator = operatorNode.getOperator();
			final Pair<Integer, IExposureNode> pc0 = getExposureNode(inputRecord, operatorNode.getLHS(), date, cargoCV, lookupData);
			final Pair<Integer, IExposureNode> pc1 = getExposureNode(inputRecord, operatorNode.getRHS(), date, cargoCV, lookupData);

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

		} else if (node instanceof final NamedSeriesASTNode namedSeriesExpression) {
			final String name = namedSeriesExpression.getName().toLowerCase();
			if (lookupData.commodityMap.containsKey(name)) {
				final BasicCommodityCurveData curve = lookupData.commodityMap.get(name);
				if (curve.isSetExpression()) {
					final ASTNode node2 = getExposureCoefficient(curve.getExpression(), lookupData);
					return getExposureNode(inputRecord, node2, date, cargoCV, lookupData);
				}

				// Should really look up actual value from curve...
				// Lazy commodity curves should be initialised by now.
				final ISeries series = commodityIndices.getSeries(name).get();
				// The series is in EXTERNAL format
				final Number evaluate = series.evaluate(Hours.between(externalDateProvider.getEarliestTime().toLocalDate(), date));

				final int unitPrice = OptimiserUnitConvertor.convertToInternalPrice(evaluate.doubleValue());
				long nativeVolume = inputRecord.volumeInMMBTU * 10;
				long volumeInMMBTU = inputRecord.volumeInMMBTU * 10;

				// Perform units conversion.
				for (final BasicUnitConversionData factor : lookupData.conversionMap.values()) {
					if (factor.getTo().equalsIgnoreCase(MMBTU)) {
						if (factor.getFrom().equalsIgnoreCase(curve.getVolumeUnit())) {
							if (LicenseFeatures.isPermitted(KnownFeatures.FEATURE_EXPOSURES_IGNORE_ENERGY_CONVERSION)) {
								// If we are in this situation, that means, that volume is *ACTUALLY* in native
								// units
								volumeInMMBTU /= factor.getFactor();
							} else {
								nativeVolume *= factor.getFactor();
							}
							break;
						}
					}
				}

				final ExposureRecord record = new ExposureRecord(curve.getName(), curve.getCurrencyUnit(), unitPrice, nativeVolume, Calculator.costFromVolume(nativeVolume, unitPrice), volumeInMMBTU,
						date, curve.getVolumeUnit());
				return new Pair<>(unitPrice, new ExposureRecords(record));
			} else if (lookupData.currencyList.stream().anyMatch(x -> x.equals(name))) {
				// Should really look up actual value from curve...
				// Currency curves should not be lazy
				final ISeries series = currencyIndices.getSeries(name).get();
				final Number evaluate = series.evaluate(Hours.between(externalDateProvider.getEarliestTime().toLocalDate(), date));
				final int unitPrice = OptimiserUnitConvertor.convertToInternalPrice(evaluate.doubleValue());
				return new Pair<>(unitPrice, new Constant(1_000_000, ""));
			} else if (lookupData.conversionMap.containsKey(name)) {
				final BasicUnitConversionData factor = lookupData.conversionMap.get(name);
				return new Pair<>((int) (factor.getFactor() * HighScaleFactor), new Constant(1_000_000, factor.getTo()));
			} else if (lookupData.reverseConversionMap.containsKey(name)) {
				final BasicUnitConversionData factor = lookupData.reverseConversionMap.get(name);
				return new Pair<>((int) (factor.getFactor() * HighScaleFactor), new Constant(1_000_000, factor.getTo()));
			} else {
				throw new IllegalStateException();
			}

		} else if (node instanceof final FunctionASTNode functionNode) {
			if (functionNode.getFunctionType() == FunctionType.MAX) {
				Pair<Integer, IExposureNode> best = getExposureNode(inputRecord, functionNode.getArguments().get(0), date, cargoCV, lookupData);
				for (int i = 1; i < functionNode.getArguments().size(); ++i) {
					final Pair<Integer, IExposureNode> pc = getExposureNode(inputRecord, functionNode.getArguments().get(i), date, cargoCV, lookupData);
					if (pc.getFirst() > best.getFirst()) {
						best = pc;
					}
				}
				return best;
			} else if (functionNode.getFunctionType() == FunctionType.MIN) {
				Pair<Integer, IExposureNode> best = getExposureNode(inputRecord, functionNode.getArguments().get(0), date, cargoCV, lookupData);
				for (int i = 1; i < functionNode.getArguments().size(); ++i) {
					final Pair<Integer, IExposureNode> pc = getExposureNode(inputRecord, functionNode.getArguments().get(i), date, cargoCV, lookupData);
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
		return Calculator.costFromVolume(volume, constant);
	}

	private static long divideVolumeByConstant(final long volume, final long constant) {
		return (volume * HighScaleFactor) / constant;
	}

	private static long divideConstantByVolume(final long constant, final long volume) {
		return (constant * ScaleFactor) / volume;
	}

	private static long divideVolumeByVolume(final long volumeA, final long volumeB) {
		return (volumeA * ScaleFactor) / volumeB;
	}

	private static long divideConstantByConstant(final int constantA, final int constantB) {
		return (constantA * HighScaleFactor) / constantB;
	}

	private static long multiplyConstantByConstant(final long constantA, final long constantB) {
		return (constantA * constantB) / HighScaleFactor;
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
