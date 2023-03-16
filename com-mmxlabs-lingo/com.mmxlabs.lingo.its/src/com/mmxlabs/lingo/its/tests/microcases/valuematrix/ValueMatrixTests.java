package com.mmxlabs.lingo.its.tests.microcases.valuematrix;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;
import java.util.function.ToIntFunction;

import org.eclipse.jdt.annotation.NonNull;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import com.mmxlabs.common.Triple;
import com.mmxlabs.lngdataserver.lng.importers.creator.InternalDataConstants;
import com.mmxlabs.models.lng.analytics.Range;
import com.mmxlabs.models.lng.analytics.SwapValueMatrixModel;
import com.mmxlabs.models.lng.analytics.SwapValueMatrixParameters;
import com.mmxlabs.models.lng.analytics.SwapValueMatrixResult;
import com.mmxlabs.models.lng.analytics.ui.views.valuematrix.ValueMatrixUtils;
import com.mmxlabs.models.lng.cargo.CanalBookingSlot;
import com.mmxlabs.models.lng.cargo.Cargo;
import com.mmxlabs.models.lng.cargo.CargoFactory;
import com.mmxlabs.models.lng.cargo.CargoModel;
import com.mmxlabs.models.lng.cargo.DischargeSlot;
import com.mmxlabs.models.lng.cargo.LoadSlot;
import com.mmxlabs.models.lng.cargo.PanamaSeasonalityRecord;
import com.mmxlabs.models.lng.cargo.Slot;
import com.mmxlabs.models.lng.cargo.VesselCharter;
import com.mmxlabs.models.lng.cargo.VesselGroupCanalParameters;
import com.mmxlabs.models.lng.commercial.EVesselTankState;
import com.mmxlabs.models.lng.fleet.Vessel;
import com.mmxlabs.models.lng.port.CanalEntry;
import com.mmxlabs.models.lng.port.Port;
import com.mmxlabs.models.lng.spotmarkets.DESPurchaseMarket;
import com.mmxlabs.models.lng.spotmarkets.DESSalesMarket;
import com.mmxlabs.models.lng.types.TimePeriod;
import com.mmxlabs.models.lng.types.VolumeUnits;

public class ValueMatrixTests extends AbstractValueMatrixTest {

	private static final long TOTAL_PNL_DIFF_DIFF_DELTA = 2L;

	public static class IndexStateContainer {
		private final int[] values;
		private final Map<Integer, Integer> valueToIndex = new HashMap<>();

		public IndexStateContainer(final @NonNull Range range) {
			final List<@NonNull Integer> valuesList = new ArrayList<>();
			for (int i = range.getMin(); i <= range.getMax(); i += range.getStepSize()) {
				valuesList.add(i);
			}
			this.values = new int[valuesList.size()];
			final Iterator<Integer> iterValues = valuesList.iterator();
			int i = 0;
			while (iterValues.hasNext()) {
				final int currentValue = iterValues.next();
				this.values[i] = currentValue;
				valueToIndex.put(currentValue, i);
				++i;
			}
		}

		public int getSize() {
			return this.values.length;
		}

		public int getIndexOf(final int value) {
			return this.valueToIndex.get(value);
		}

		public int getValueAt(final int index) {
			return this.values[index];
		}
	}

	private void checkSolutionSet(final List<SwapValueMatrixResult> solutionSet, final SwapValueMatrixModel model) {
		checkConsecutivePairs(solutionSet, this::getRowHeaderValue, model.getParameters().getBasePriceRange().getStepSize(), this::getColumnHeaderValue);
		checkConsecutivePairs(solutionSet, this::getColumnHeaderValue, model.getParameters().getSwapPriceRange().getStepSize(), this::getRowHeaderValue);
	}

	private void checkConsecutivePairs(final List<SwapValueMatrixResult> solutionSet, final ToIntFunction<SwapValueMatrixResult> mainHeaderValueProvider, final int stepSize,
			final ToIntFunction<SwapValueMatrixResult> alternativeHeaderValueProvider) {
		final Map<Integer, List<SwapValueMatrixResult>> sliceMap = new HashMap<>();
		solutionSet.forEach(res -> sliceMap.computeIfAbsent(mainHeaderValueProvider.applyAsInt(res), key -> new ArrayList<>()).add(res));
		for (final List<SwapValueMatrixResult> results : sliceMap.values()) {
			if (results.size() > 1) {
				results.sort((c1, c2) -> Integer.compare(alternativeHeaderValueProvider.applyAsInt(c1), alternativeHeaderValueProvider.applyAsInt(c2)));
				final long consecutiveDiffDifference = calculateTotalPnlDiff(results.get(1)) - calculateTotalPnlDiff(results.get(0));
				final Iterator<SwapValueMatrixResult> resultsIter = results.iterator();
				final SwapValueMatrixResult firstResult = resultsIter.next();
				int previousPrice = alternativeHeaderValueProvider.applyAsInt(firstResult);
				long previousPnlDiff = calculateTotalPnlDiff(firstResult);
				while (resultsIter.hasNext()) {
					final SwapValueMatrixResult currentResult = resultsIter.next();
					final int nextPrice = alternativeHeaderValueProvider.applyAsInt(currentResult);
					final long nextPnlDiff = calculateTotalPnlDiff(currentResult);
					Assertions.assertEquals(stepSize, nextPrice - previousPrice);
					Assertions.assertEquals(consecutiveDiffDifference, nextPnlDiff - previousPnlDiff, TOTAL_PNL_DIFF_DIFF_DELTA);
					previousPrice = nextPrice;
					previousPnlDiff = nextPnlDiff;
				}
			}
		}
	}

	private int getRowHeaderValue(final SwapValueMatrixResult result) {
		return (int) result.getSwapDiversionCargo().getDischargePrice();
	}

	private int getColumnHeaderValue(final SwapValueMatrixResult result) {
		return (int) result.getBaseCargo().getDischargePrice();
	}

	private long calculateTotalPnlDiff(final SwapValueMatrixResult result) {
		return result.getSwapPnlMinusBasePnl() //
				+ result.getSwapPrecedingPnl() - result.getBasePrecedingPnl()//
				+ result.getSwapSucceedingPnl() - result.getBaseSucceedingPnl();
	}

	private final List<List<SwapValueMatrixResult>> buildCommonSolutions(final SwapValueMatrixResult[][] matrix, final List<Predicate<SwapValueMatrixResult>> conditions) {
		if (conditions.size() >= 16) {
			// Not expecting too many conditions
			throw new IllegalStateException("Expected less than 32 conditions");
		}
		final Map<Integer, List<SwapValueMatrixResult>> commonSolutions = new HashMap<>();
		for (final SwapValueMatrixResult[] row : matrix) {
			for (final SwapValueMatrixResult result : row) {
				commonSolutions.computeIfAbsent(resultToKey(result, conditions), key -> new ArrayList<>()).add(result);
			}
		}
		return new ArrayList<>(commonSolutions.values());
	}

	private int resultToKey(final SwapValueMatrixResult result, final List<Predicate<SwapValueMatrixResult>> conditions) {
		int key = 0;
		for (final Predicate<SwapValueMatrixResult> condition : conditions) {
			key <<= 1;
			key += condition.test(result) ? 1 : 0;
		}
		return key;
	}

	public static @NonNull Triple<IndexStateContainer, IndexStateContainer, SwapValueMatrixResult[][]> buildResultsMatrix(final @NonNull SwapValueMatrixModel model) {
		// Rows
		final IndexStateContainer rowStateContainer = new IndexStateContainer(model.getParameters().getSwapPriceRange());
		// Columns
		final IndexStateContainer columnStateContainer = new IndexStateContainer(model.getParameters().getBasePriceRange());
		final SwapValueMatrixResult[][] matrix = new SwapValueMatrixResult[rowStateContainer.getSize()][columnStateContainer.getSize()];
		for (final SwapValueMatrixResult valueMatrixResult : model.getSwapValueMatrixResult().getResults()) {
			final double rowValue = valueMatrixResult.getSwapDiversionCargo().getDischargePrice();
			Assertions.assertEquals(rowValue, Math.rint(rowValue));
			final int rowIndex = rowStateContainer.getIndexOf((int) valueMatrixResult.getSwapDiversionCargo().getDischargePrice());

			final double columnValue = valueMatrixResult.getBaseCargo().getDischargePrice();
			Assertions.assertEquals(columnValue, Math.rint(columnValue));
			final int columnIndex = columnStateContainer.getIndexOf((int) columnValue);

			final SwapValueMatrixResult existingResult = matrix[rowIndex][columnIndex];
			Assertions.assertNull(existingResult);
			matrix[rowIndex][columnIndex] = valueMatrixResult;
		}
		for (final SwapValueMatrixResult[] row : matrix) {
			for (final SwapValueMatrixResult result : row) {
				Assertions.assertNotNull(result);
			}
		}
		return Triple.of(rowStateContainer, columnStateContainer, matrix);
	}

	private SwapValueMatrixModel buildDefaultValueMatrixModel(final @NonNull VesselCharter vc, final @NonNull LoadSlot ls, final @NonNull DischargeSlot ds, final @NonNull DESPurchaseMarket dpm,
			final @NonNull DESSalesMarket dsm) {
		final SwapValueMatrixModel model = ValueMatrixUtils.createEmptyValueMatrixModel("value-matrix");
		final SwapValueMatrixParameters parameters = model.getParameters();
		parameters.getBaseVesselCharter().setVesselCharter(vc);
		parameters.getBaseLoad().setSlot(ls);
		parameters.getBaseDischarge().setSlot(ds);

		parameters.getBasePriceRange().setMin(10);
		parameters.getBasePriceRange().setMax(12);
		parameters.getBasePriceRange().setStepSize(1);

		parameters.getSwapLoadMarket().setMarket(dpm);
		parameters.getSwapLoadMarket().setMonth(YearMonth.of(2023, 1));

		parameters.getSwapDischargeMarket().setMarket(dsm);
		parameters.getSwapDischargeMarket().setMonth(YearMonth.of(2023, 1));

		parameters.getSwapPriceRange().setMin(9);
		parameters.getSwapPriceRange().setMax(11);
		parameters.getSwapPriceRange().setStepSize(1);

		parameters.setSwapFee(0.3);
		return model;
	}

	@Test
	public void testSimpleValueMatrix() {
		final Port pKarratha = portFinder.findPortById(InternalDataConstants.PORT_KARRATHA);
		final Port pTokyoBay = portFinder.findPortById(InternalDataConstants.PORT_TOKYO_BAY);
		final Vessel vMediumShip = fleetModelFinder.findVessel(InternalDataConstants.REF_VESSEL_STEAM_145);

		final double ltPurchaseCost = 10.5;

		final VesselCharter vcMediumShip = cargoModelBuilder.makeVesselCharter(vMediumShip, entity) //
				.withCharterRate("70000") //
				.withStartWindow(LocalDateTime.of(2023, 1, 12, 0, 0)) //
				.withStartHeel(500, 6_000, 22.6, "10.6") //
				.withEndWindow(LocalDateTime.of(2023, 2, 20, 0, 0)) //
				.withEndHeel(500, 500, EVesselTankState.MUST_BE_COLD, false) //
				.build();

		final Cargo cargo = cargoModelBuilder.makeCargo() //
				.makeFOBPurchase("L1", LocalDate.of(2023, 1, 14), pKarratha, null, entity, String.format("%.1f", ltPurchaseCost)) //
				.withVolumeLimits(130_000, 150_000, VolumeUnits.M3) //
				.withWindowSize(0, TimePeriod.DAYS) //
				.withWindowStartTime(0) //
				.with(s -> ((LoadSlot) s).setCargoCV(23)) //
				.build() //
				.makeDESSale("D1", LocalDate.of(2023, 1, 28), pTokyoBay, null, entity, "10") //
				.withVolumeLimits(100_000, 145_000, VolumeUnits.M3) //
				.withWindowSize(0, TimePeriod.DAYS) //
				.build() //
				.withVesselAssignment(vcMediumShip, 0) //
				.build();

		final DESPurchaseMarket dpmTokyoBay = spotMarketsModelBuilder.makeDESPurchaseMarket("DES Purchase Tokyo Bay", Collections.singletonList(pTokyoBay), entity, "15", 23.0) //
				.withVolumeLimits(100_000, 135_000, VolumeUnits.M3) //
				.withAvailabilityConstant(1) //
				.withEnabled(true) //
				.build();
		final DESSalesMarket dsmTokyoBay = spotMarketsModelBuilder.makeDESSaleMarket("DES Sale Tokyo Bay", pTokyoBay, entity, "15") //
				.withVolumeLimits(100_000, 180_000, VolumeUnits.M3) //
				.withAvailabilityConstant(1) //
				.withEnabled(true) //
				.build();

		Assertions.assertEquals(2, cargo.getSlots().size());
		final List<Slot<?>> sortedSlots = cargo.getSortedSlots();
		Assertions.assertInstanceOf(LoadSlot.class, sortedSlots.get(0));
		Assertions.assertInstanceOf(DischargeSlot.class, sortedSlots.get(1));

		final LoadSlot loadSlot = (LoadSlot) sortedSlots.get(0);
		final DischargeSlot dischargeSlot = (DischargeSlot) sortedSlots.get(1);

		final SwapValueMatrixModel model = ValueMatrixUtils.createEmptyValueMatrixModel("value-matrix");
		final SwapValueMatrixParameters parameters = model.getParameters();
		parameters.getBaseVesselCharter().setVesselCharter(vcMediumShip);
		parameters.getBaseLoad().setSlot(loadSlot);
		parameters.getBaseDischarge().setSlot(dischargeSlot);

		parameters.getBasePriceRange().setMin(1);
		parameters.getBasePriceRange().setMax(20);
		parameters.getBasePriceRange().setStepSize(1);

		parameters.getSwapLoadMarket().setMarket(dpmTokyoBay);
		parameters.getSwapLoadMarket().setMonth(YearMonth.of(2023, 1));

		parameters.getSwapDischargeMarket().setMarket(dsmTokyoBay);
		parameters.getSwapDischargeMarket().setMonth(YearMonth.of(2023, 1));

		parameters.getSwapPriceRange().setMin(1);
		parameters.getSwapPriceRange().setMax(25);
		parameters.getSwapPriceRange().setStepSize(1);

		parameters.setSwapFee(0.3);

		evaluateValueMatrix(model, false);

		final Triple<IndexStateContainer, IndexStateContainer, SwapValueMatrixResult[][]> triple = buildResultsMatrix(model);

		/*
		 * There should be different solution "structures" that depend on: // (1) Market
		 * price + swap fee < sales price. If true, max backfill load and discharge. //
		 * (2) Market price > load price. If true, max diversion load and discharge. //
		 * (3) Sales price > load price. If true, max base load and base discharge. //
		 * Combinations of the above three conditions partition the matrix into "convex"
		 * regions. // Translating horizontally or vertically by one step to a neighbour
		 * in the same region should have the same volumes and the price difference
		 * should be constant if moving in the same direction to another neighbour in
		 * the same region.
		 */
		final List<Predicate<SwapValueMatrixResult>> conditions = new ArrayList<>();
		conditions.add(result -> result.getSwapBackfillCargo().getLoadPrice() < result.getSwapBackfillCargo().getDischargePrice());
		conditions.add(result -> result.getSwapDiversionCargo().getDischargePrice() > result.getSwapDiversionCargo().getLoadPrice());
		conditions.add(result -> result.getBaseCargo().getDischargePrice() > result.getBaseCargo().getLoadPrice());
		final List<List<SwapValueMatrixResult>> commonSolutions = buildCommonSolutions(triple.getThird(), conditions);
		// Expecting 6 regions
		Assertions.assertEquals(6, commonSolutions.size());
		for (final List<SwapValueMatrixResult> solutionSet : commonSolutions) {
			checkSolutionSet(solutionSet, model);
		}
	}

	/*
	 * 
	 */
	@Test
	@Disabled("Needs completing: medium ship value matrix should not use Panama booking because it is needed by large ship")
	public void testPanama() {
		final Port pCameron = portFinder.findPortById(InternalDataConstants.PORT_CAMERON);
		final Port pHimeji = portFinder.findPortById(InternalDataConstants.PORT_HIMEJI);
		final Port pSabinePass = portFinder.findPortById(InternalDataConstants.PORT_SABINE_PASS);
		final Port pTokyoBay = portFinder.findPortById(InternalDataConstants.PORT_TOKYO_BAY);

		final Vessel vMediumShip = fleetModelFinder.findVessel(InternalDataConstants.REF_VESSEL_STEAM_145);
		final Vessel vLargeShip = fleetModelFinder.findVessel(InternalDataConstants.REF_VESSEL_TFDE_160);

		final VesselCharter vcMediumShip = cargoModelBuilder.makeVesselCharter(vMediumShip, entity) //
				.withCharterRate("70000") //
				.withStartWindow(LocalDateTime.of(2023, 1, 12, 0, 0)) //
				.withStartHeel(500, 6_000, 22.6, "10.6") //
				.withEndWindow(LocalDateTime.of(2023, 3, 12, 0, 0)) //
				.withEndHeel(500, 500, EVesselTankState.MUST_BE_COLD, false) //
				.build();
		final VesselCharter vcLargeShip = cargoModelBuilder.makeVesselCharter(vLargeShip, entity) //
				.withCharterRate("70000") //
				.withStartWindow(LocalDateTime.of(2023, 1, 12, 0, 0)) //
				.withStartHeel(500, 6_000, 22.6, "10.6") //
				.withEndWindow(LocalDateTime.of(2023, 3, 12, 0, 0)) //
				.withEndHeel(500, 500, EVesselTankState.MUST_BE_COLD, false) //
				.build();

		final Cargo mediumShipCargo = cargoModelBuilder.makeCargo() //
				.makeFOBPurchase("Medium ship load", LocalDate.of(2023, 1, 13), pCameron, null, entity, "10.5") //
				.withVolumeLimits(130_000, 150_000, VolumeUnits.M3) //
				.withWindowSize(0, TimePeriod.DAYS) //
				.withWindowStartTime(0) //
				.with(s -> ((LoadSlot) s).setCargoCV(23)) //
				.build() //
				.makeDESSale("Medium ship discharge", LocalDate.of(2023, 2, 5), pTokyoBay, null, entity, "SPLITMONTH(customHigh, customLow, 6)") //
				.withVolumeLimits(100_000, 145_000, VolumeUnits.M3) //
				.withWindowSize(2, TimePeriod.DAYS) //
				.build() //
				.withVesselAssignment(vcMediumShip, 0) //
				.build();
		
		final Cargo largeShipCargo = cargoModelBuilder.makeCargo() //
				.makeFOBPurchase("Large ship load", LocalDate.of(2023, 1, 13), pSabinePass, null, entity, "5") //
				.withVolumeLimits(130_000, 165_000, VolumeUnits.M3) //
				.withWindowSize(0, TimePeriod.DAYS) //
				.withWindowStartTime(0) //
				.build() //
				.makeDESSale("Large ship discharge", LocalDate.of(2023, 2, 7), pHimeji, null, entity, "10") //
				.withVolumeLimits(100_000, 160_000, VolumeUnits.M3) //
				.withWindowSize(0, TimePeriod.DAYS) //
				.build() //
				.withVesselAssignment(vcLargeShip, 0) //
				.build();
	
		final CargoModel cargoModel = cargoModelFinder.getCargoModel();
		final List<VesselGroupCanalParameters> bookingCodes = cargoModel.getCanalBookings().getVesselGroupCanalParameters();
		Assertions.assertEquals(1, bookingCodes.size());
		final VesselGroupCanalParameters defaultBookingCode = bookingCodes.get(0);
		final List<PanamaSeasonalityRecord> seasonalityRecords = cargoModel.getCanalBookings().getPanamaSeasonalityRecords();
		Assertions.assertEquals(1, seasonalityRecords.size());
		final PanamaSeasonalityRecord defaultSeasonalityRecord = seasonalityRecords.get(0);
		Assertions.assertTrue(defaultSeasonalityRecord.getVesselGroupCanalParameter() == defaultBookingCode);
		defaultSeasonalityRecord.setNorthboundWaitingDays(2);
		defaultSeasonalityRecord.setSouthboundWaitingDays(2);

		final List<CanalBookingSlot> bookings = cargoModel.getCanalBookings().getCanalBookingSlots();
		Assertions.assertTrue(bookings.isEmpty());
		// This booking slot should go to the Large ship because it needs it
		final CanalBookingSlot bookingSlot = CargoFactory.eINSTANCE.createCanalBookingSlot();
		bookingSlot.setBookingDate(LocalDate.of(2023, 1, 18));
		bookingSlot.setCanalEntrance(CanalEntry.NORTHSIDE);
		bookingSlot.setBookingCode(defaultBookingCode);

		final DESPurchaseMarket dpmTokyoBay = spotMarketsModelBuilder.makeDESPurchaseMarket("DES Purchase Tokyo Bay", Collections.singletonList(pTokyoBay), entity, "15", 23.0) //
				.withVolumeLimits(100_000, 135_000, VolumeUnits.M3) //
				.withAvailabilityConstant(1) //
				.withEnabled(true) //
				.build();
		final DESSalesMarket dsmTokyoBay = spotMarketsModelBuilder.makeDESSaleMarket("DES Sale Tokyo Bay", pTokyoBay, entity, "15") //
				.withVolumeLimits(100_000, 180_000, VolumeUnits.M3) //
				.withAvailabilityConstant(1) //
				.withEnabled(true) //
				.build();
	}

	@Test
	public void testIndividualCombinations() {
		final Port pKarratha = portFinder.findPortById(InternalDataConstants.PORT_KARRATHA);
		final Port pTokyoBay = portFinder.findPortById(InternalDataConstants.PORT_TOKYO_BAY);
		final Vessel vMediumShip = fleetModelFinder.findVessel(InternalDataConstants.REF_VESSEL_STEAM_145);

		final double ltPurchaseCost = 10.5;

		final VesselCharter vcMediumShip = cargoModelBuilder.makeVesselCharter(vMediumShip, entity) //
				.withCharterRate("70000") //
				.withStartWindow(LocalDateTime.of(2023, 1, 12, 0, 0)) //
				.withStartHeel(500, 6_000, 22.6, "10.6") //
				.withEndWindow(LocalDateTime.of(2023, 2, 20, 0, 0)) //
				.withEndHeel(500, 500, EVesselTankState.MUST_BE_COLD, false) //
				.build();

		final Cargo cargo = cargoModelBuilder.makeCargo() //
				.makeFOBPurchase("L1", LocalDate.of(2023, 1, 14), pKarratha, null, entity, String.format("%.1f", ltPurchaseCost)) //
				.withVolumeLimits(130_000, 150_000, VolumeUnits.M3) //
				.withWindowSize(0, TimePeriod.DAYS) //
				.withWindowStartTime(0) //
				.with(s -> ((LoadSlot) s).setCargoCV(23)) //
				.build() //
				.makeDESSale("D1", LocalDate.of(2023, 1, 28), pTokyoBay, null, entity, "10") //
				.withVolumeLimits(100_000, 145_000, VolumeUnits.M3) //
				.withWindowSize(0, TimePeriod.DAYS) //
				.build() //
				.withVesselAssignment(vcMediumShip, 0) //
				.build();

		final DESPurchaseMarket dpmTokyoBay = spotMarketsModelBuilder.makeDESPurchaseMarket("DES Purchase Tokyo Bay", Collections.singletonList(pTokyoBay), entity, "15", 23.0) //
				.withVolumeLimits(100_000, 135_000, VolumeUnits.M3) //
				.withAvailabilityConstant(1) //
				.withEnabled(true) //
				.build();
		final DESSalesMarket dsmTokyoBay = spotMarketsModelBuilder.makeDESSaleMarket("DES Sale Tokyo Bay", pTokyoBay, entity, "15") //
				.withVolumeLimits(100_000, 180_000, VolumeUnits.M3) //
				.withAvailabilityConstant(1) //
				.withEnabled(true) //
				.build();

		Assertions.assertEquals(2, cargo.getSlots().size());
		final List<Slot<?>> sortedSlots = cargo.getSortedSlots();
		Assertions.assertInstanceOf(LoadSlot.class, sortedSlots.get(0));
		Assertions.assertInstanceOf(DischargeSlot.class, sortedSlots.get(1));

		final LoadSlot loadSlot = (LoadSlot) sortedSlots.get(0);
		final DischargeSlot dischargeSlot = (DischargeSlot) sortedSlots.get(1);

		final SwapValueMatrixModel model = buildDefaultValueMatrixModel(vcMediumShip, loadSlot, dischargeSlot, dpmTokyoBay, dsmTokyoBay);
		model.getParameters().getBasePriceRange().setMin(10);
		model.getParameters().getBasePriceRange().setMax(12);
		model.getParameters().getSwapPriceRange().setMin(9);
		model.getParameters().getSwapPriceRange().setMax(11);

		ValueMatrixTestUtil.verifyPermutations(model, (m, permutation) -> evaluateValueMatrixWithOrder(m, permutation, true, true));
	}
}
