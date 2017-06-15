/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.schedule.timewindowscheduling;

import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import org.eclipse.jdt.annotation.Nullable;
import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;
import org.mockito.Matchers;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.mmxlabs.common.NonNullPair;
import com.mmxlabs.common.Pair;
import com.mmxlabs.common.curves.StepwiseIntegerCurve;
import com.mmxlabs.optimiser.common.components.ITimeWindow;
import com.mmxlabs.optimiser.common.components.impl.TimeWindow;
import com.mmxlabs.scheduler.optimiser.OptimiserUnitConvertor;
import com.mmxlabs.scheduler.optimiser.components.IBaseFuel;
import com.mmxlabs.scheduler.optimiser.components.IConsumptionRateCalculator;
import com.mmxlabs.scheduler.optimiser.components.IDischargeOption;
import com.mmxlabs.scheduler.optimiser.components.ILoadOption;
import com.mmxlabs.scheduler.optimiser.components.IPort;
import com.mmxlabs.scheduler.optimiser.components.IPortSlot;
import com.mmxlabs.scheduler.optimiser.components.IVessel;
import com.mmxlabs.scheduler.optimiser.components.IVesselClass;
import com.mmxlabs.scheduler.optimiser.components.PricingEventType;
import com.mmxlabs.scheduler.optimiser.contracts.ICharterRateCalculator;
import com.mmxlabs.scheduler.optimiser.contracts.ILoadPriceCalculator;
import com.mmxlabs.scheduler.optimiser.contracts.IPriceIntervalProvider;
import com.mmxlabs.scheduler.optimiser.contracts.ISalesPriceCalculator;
import com.mmxlabs.scheduler.optimiser.contracts.IVesselBaseFuelCalculator;
import com.mmxlabs.scheduler.optimiser.curves.IIntegerIntervalCurve;
import com.mmxlabs.scheduler.optimiser.curves.IPriceIntervalProducer;
import com.mmxlabs.scheduler.optimiser.curves.IntegerIntervalCurve;
import com.mmxlabs.scheduler.optimiser.curves.PriceIntervalProducer;
import com.mmxlabs.scheduler.optimiser.providers.ERouteOption;
import com.mmxlabs.scheduler.optimiser.providers.IDistanceProvider;
import com.mmxlabs.scheduler.optimiser.providers.IRouteCostProvider;
import com.mmxlabs.scheduler.optimiser.providers.ITimeZoneToUtcOffsetProvider;
import com.mmxlabs.scheduler.optimiser.providers.IVesselProvider;
import com.mmxlabs.scheduler.optimiser.providers.impl.HashMapVesselEditor;
import com.mmxlabs.scheduler.optimiser.shared.port.DistanceMatrixEntry;
import com.mmxlabs.scheduler.optimiser.voyage.IPortTimeWindowsRecord;
import com.mmxlabs.scheduler.optimiser.voyage.impl.PortTimeWindowsRecord;

public class PriceIntervalProviderHelperTest {

	/**
	 * Test that the cheapest option is to go direct meaning: load {0, 0} and discharge {40, 45}
	 */
	@Test
	@Ignore
	public void testCanalTrimming_MinTimeGreaterThanCheapestOption_NewMethod() {
		final List<int[]> purchaseIntervals = new ArrayList<>();
		purchaseIntervals.add(new int[] { 0, 20 });
		purchaseIntervals.add(new int[] { 5, 25 });
		purchaseIntervals.add(new int[] { 15, 18 });
		purchaseIntervals.add(new int[] { 20, Integer.MIN_VALUE });

		final List<int[]> salesIntervals = new ArrayList<>();
		salesIntervals.add(new int[] { 40, 25 });
		salesIntervals.add(new int[] { 45, 20 });
		salesIntervals.add(new int[] { 50, 22 });
		salesIntervals.add(new int[] { 51, Integer.MIN_VALUE });

		final IPort from = Mockito.mock(IPort.class, "from");
		final IPort to = Mockito.mock(IPort.class, "to");

		final List<DistanceMatrixEntry> distances = new LinkedList<>();
		distances.add(new DistanceMatrixEntry(ERouteOption.DIRECT, from, to, 400));
		distances.add(new DistanceMatrixEntry(ERouteOption.SUEZ, from, to, 300));
		final TimeWindowsTrimming timeWindowsTrimming = getTimeWindowsTrimming(distances);

		final ILoadOption loadSlot = Mockito.mock(ILoadOption.class);
		Mockito.when(loadSlot.getMaxLoadVolumeMMBTU()).thenReturn(OptimiserUnitConvertor.convertToInternalVolume(160000 * 22.4));
		final ITimeWindow loadSlotTimeWindow = Mockito.mock(ITimeWindow.class);
		Mockito.when(loadSlot.getTimeWindow()).thenReturn(loadSlotTimeWindow);
		Mockito.when(loadSlotTimeWindow.getInclusiveStart()).thenReturn(0);
		final IDischargeOption dischargeSlot = Mockito.mock(IDischargeOption.class);

		final IPortTimeWindowsRecord portTimesWindowsRecord = Mockito.mock(IPortTimeWindowsRecord.class);
		Mockito.when(portTimesWindowsRecord.getSlotDuration(Matchers.eq(loadSlot))).thenReturn(0);
		Mockito.when(portTimesWindowsRecord.getSlotFeasibleTimeWindow(Matchers.eq(loadSlot))).thenReturn(loadSlotTimeWindow);

		final IVessel vessel = getIVessel(null);
		final int[] times = timeWindowsTrimming.trimCargoTimeWindowsWithRouteOptimisationAndBoilOff(portTimesWindowsRecord, vessel, loadSlot, dischargeSlot, purchaseIntervals, salesIntervals,
				salesIntervals, false);
		Assert.assertArrayEquals(new int[] { 0, 0, 40, 40 }, times);
	}

	@SuppressWarnings("null")
	@Test
	public void testGetTotalEstimatedJourneyCost() {
		final PriceIntervalProviderHelper priceIntervalProviderHelper = createPriceIntervalProviderHelper(0);
		final IntervalData purchase = new IntervalData(0, 10, 5);
		final IntervalData sales = new IntervalData(50, 70, 10);
		final int loadDuration = 0;
		final int salesPrice = 10;
		final LadenRouteData[] lrd = new LadenRouteData[2];
		lrd[0] = new LadenRouteData(20, 30, 0, 300, 0);
		lrd[1] = new LadenRouteData(10, 15, OptimiserUnitConvertor.convertToInternalDailyCost(500000), 150, 0);
		final IVessel iVessel = getIVessel();
		final NonNullPair<LadenRouteData, Long> totalEstimatedJourneyCost = priceIntervalProviderHelper.getTotalEstimatedJourneyCost(purchase, sales, loadDuration, salesPrice, 0, lrd, 10300,
				iVessel.getVesselClass(), OptimiserUnitConvertor.convertToInternalDailyRate(22), true);

	}

	static IVessel getIVessel() {
		return getIVessel(null);
	}

	static IVessel getIVessel(@Nullable final IConsumptionRateCalculator optionalConsumptionRateCalculator) {
		final IVessel vessel = Mockito.mock(IVessel.class);
		final IVesselClass vesselClass = Mockito.mock(IVesselClass.class);
		final int maxSpeed = 10 * 1000;
		Mockito.when(vesselClass.getMaxSpeed()).thenReturn(maxSpeed);
		final IConsumptionRateCalculator consumptionRateCalculator = getMockedFixedConsumptionRateCalculator(maxSpeed);
		Mockito.when(vesselClass.getConsumptionRate(Mockito.any())).thenReturn(consumptionRateCalculator);
		final IBaseFuel baseFuel = Mockito.mock(IBaseFuel.class);
		Mockito.when(baseFuel.getEquivalenceFactor()).thenReturn(44100);
		Mockito.when(vesselClass.getBaseFuel()).thenReturn(baseFuel);
		Mockito.when(vessel.getVesselClass()).thenReturn(vesselClass);
		return vessel;
	}

	static IConsumptionRateCalculator getMockedFixedConsumptionRateCalculator(final int fixedSpeedValue) {
		final IConsumptionRateCalculator consumptionRateCalculator = Mockito.mock(IConsumptionRateCalculator.class);
		Mockito.when(consumptionRateCalculator.getSpeed(Matchers.anyLong())).thenReturn(fixedSpeedValue);
		return consumptionRateCalculator;
	}

	@Test
	public void testIntervals() {
		final StepwiseIntegerCurve c = new StepwiseIntegerCurve();

		c.setValueAfter(-1, 5);
		c.setValueAfter(21, 8);
		c.setValueAfter(22, 9);
		c.setValueAfter(23, 10);
		c.setValueAfter(24, 15);
		c.setValueAfter(25, 20);
		c.setValueAfter(39, 29);
		c.setValueAfter(40, 30);
		c.setValueAfter(41, 31);

		final ILoadOption loadSlot = Mockito.mock(ILoadOption.class);
		when(loadSlot.getPricingEvent()).thenReturn(PricingEventType.END_OF_LOAD);
		when(loadSlot.getPricingDate()).thenReturn(IPortSlot.NO_PRICING_DATE);
		final IDischargeOption dischargeSlot = Mockito.mock(IDischargeOption.class);
		final PortTimeWindowsRecord portTimeWindowsRecord = new PortTimeWindowsRecord();
		portTimeWindowsRecord.setSlot(loadSlot, null, 24, 0);
		portTimeWindowsRecord.setSlot(dischargeSlot, null, 24, 0);

		final ILoadPriceCalculator loadPriceCalculator = Mockito.mock(ILoadPriceCalculator.class);
		when(loadPriceCalculator.getEstimatedPurchasePrice(Matchers.<ILoadOption> any(), Matchers.<IDischargeOption> any(), Matchers.anyInt())).thenAnswer(new Answer<Integer>() {
			@Override
			public Integer answer(final InvocationOnMock invocation) throws Throwable {
				final Object[] args = invocation.getArguments();
				final int input = (int) args[2];
				return c.getValueAtPoint(input);
			}
		});
		when(loadSlot.getLoadPriceCalculator()).thenReturn(loadPriceCalculator);
		final PriceIntervalProviderHelper priceIntervalProviderHelper = createPriceIntervalProviderHelper(0);

		final IIntegerIntervalCurve curve = new IntegerIntervalCurve();
		curve.add(0);
		curve.add(20);
		curve.add(30);
		curve.add(40);

		final List<int[]> priceIntervals = new LinkedList<>();
		priceIntervalProviderHelper.buildIntervalsList(loadSlot, curve, null, 0, 50, portTimeWindowsRecord, priceIntervals);
		final List<int[]> expected = new LinkedList<>();
		expected.add(new int[] { 0, 15 });
		expected.add(new int[] { 6, 20 });
		expected.add(new int[] { 16, 30 });
		expected.add(new int[] { 50, Integer.MIN_VALUE });

		for (int i = 0; i < expected.size(); i++) {
			Assert.assertTrue(String.format("Expected %s got %s", Arrays.toString(expected.get(i)), Arrays.toString(priceIntervals.get(i))),
					expected.get(i)[0] == priceIntervals.get(i)[0] && expected.get(i)[1] == priceIntervals.get(i)[1]);
		}
	}

	@Test
	public void testIntervalsPlusTime() {
		final StepwiseIntegerCurve c = new StepwiseIntegerCurve();

		c.setValueAfter(-1, 5);
		c.setValueAfter(21, 8);
		c.setValueAfter(22, 9);
		c.setValueAfter(23, 10);
		c.setValueAfter(24, 15);
		c.setValueAfter(25, 20);
		c.setValueAfter(27, 22);
		c.setValueAfter(28, 23);
		c.setValueAfter(29, 24);
		c.setValueAfter(30, 25);
		c.setValueAfter(40, 30);
		c.setValueAfter(41, 31);
		c.setValueAfter(41, 32);

		final ILoadOption loadSlot = Mockito.mock(ILoadOption.class);
		when(loadSlot.getPricingEvent()).thenReturn(PricingEventType.END_OF_LOAD);
		when(loadSlot.getPricingDate()).thenReturn(IPortSlot.NO_PRICING_DATE);
		final IDischargeOption dischargeSlot = Mockito.mock(IDischargeOption.class);
		final PortTimeWindowsRecord portTimeWindowsRecord = new PortTimeWindowsRecord();
		portTimeWindowsRecord.setSlot(loadSlot, null, 24, 0);
		portTimeWindowsRecord.setSlot(dischargeSlot, null, 24, 0);

		final ILoadPriceCalculator loadPriceCalculator = Mockito.mock(ILoadPriceCalculator.class);
		when(loadPriceCalculator.getEstimatedPurchasePrice(Matchers.<ILoadOption> any(), Matchers.<IDischargeOption> any(), Matchers.anyInt())).thenAnswer(new Answer<Integer>() {
			@Override
			public Integer answer(final InvocationOnMock invocation) throws Throwable {
				final Object[] args = invocation.getArguments();
				final int input = (int) args[2];
				return c.getValueAtPoint(input);
			}
		});
		when(loadSlot.getLoadPriceCalculator()).thenReturn(loadPriceCalculator);
		final PriceIntervalProviderHelper priceIntervalProviderHelper = createPriceIntervalProviderHelper(1);

		final IIntegerIntervalCurve curve = new IntegerIntervalCurve();
		curve.add(0);
		curve.add(20);
		curve.add(30);
		curve.add(40);

		final List<int[]> priceIntervals = new LinkedList<>();
		priceIntervalProviderHelper.buildIntervalsList(loadSlot, curve, null, 0, 50, portTimeWindowsRecord, priceIntervals);
		final List<int[]> expected = new LinkedList<>();
		expected.add(new int[] { 0, 20 });
		expected.add(new int[] { 5, 25 });
		expected.add(new int[] { 15, 30 });
		expected.add(new int[] { 50, Integer.MIN_VALUE });

		for (int i = 0; i < expected.size(); i++) {
			Assert.assertTrue(String.format("Expected %s got %s", Arrays.toString(expected.get(i)), Arrays.toString(priceIntervals.get(i))),
					expected.get(i)[0] == priceIntervals.get(i)[0] && expected.get(i)[1] == priceIntervals.get(i)[1]);
		}
	}

	@Test
	public void testIntervalsStartOfLoadWindowPlusTime() {
		final StepwiseIntegerCurve c = new StepwiseIntegerCurve();

		c.setValueAfter(-2, 4);
		c.setValueAfter(-1, 5);
		c.setValueAfter(0, 6);
		c.setValueAfter(1, 7);
		c.setValueAfter(2, 8);
		c.setValueAfter(21, 8);
		c.setValueAfter(22, 9);
		c.setValueAfter(23, 10);
		c.setValueAfter(24, 15);
		c.setValueAfter(25, 20);
		c.setValueAfter(39, 29);
		c.setValueAfter(40, 30);
		c.setValueAfter(41, 31);

		final ILoadOption loadSlot = Mockito.mock(ILoadOption.class);
		when(loadSlot.getPricingEvent()).thenReturn(PricingEventType.START_OF_LOAD_WINDOW);
		when(loadSlot.getPricingDate()).thenReturn(IPortSlot.NO_PRICING_DATE);
		final IDischargeOption dischargeSlot = Mockito.mock(IDischargeOption.class);
		final PortTimeWindowsRecord portTimeWindowsRecord = new PortTimeWindowsRecord();
		portTimeWindowsRecord.setSlot(loadSlot, new TimeWindow(0, 10), 24, 0);
		portTimeWindowsRecord.setSlot(dischargeSlot, null, 24, 0);

		final ILoadPriceCalculator loadPriceCalculator = Mockito.mock(ILoadPriceCalculator.class);
		when(loadPriceCalculator.getEstimatedPurchasePrice(Matchers.<ILoadOption> any(), Matchers.<IDischargeOption> any(), Matchers.anyInt())).thenAnswer(new Answer<Integer>() {
			@Override
			public Integer answer(final InvocationOnMock invocation) throws Throwable {
				final Object[] args = invocation.getArguments();
				final int input = (int) args[2];
				return c.getValueAtPoint(input);
			}
		});
		when(loadSlot.getLoadPriceCalculator()).thenReturn(loadPriceCalculator);
		final PriceIntervalProviderHelper priceIntervalProviderHelper = createPriceIntervalProviderHelper(2);

		final IIntegerIntervalCurve curve = new IntegerIntervalCurve();
		curve.add(0);
		curve.add(20);
		curve.add(30);
		curve.add(40);

		final List<int[]> priceIntervals = new LinkedList<>();
		priceIntervalProviderHelper.buildIntervalsList(loadSlot, curve, null, 0, 50, portTimeWindowsRecord, priceIntervals);
		final List<int[]> expected = new LinkedList<>();
		expected.add(new int[] { 0, 8 });
		expected.add(new int[] { 50, Integer.MIN_VALUE });

		for (int i = 0; i < expected.size(); i++) {
			Assert.assertTrue(String.format("Expected %s got %s", Arrays.toString(expected.get(i)), Arrays.toString(priceIntervals.get(i))),
					expected.get(i)[0] == priceIntervals.get(i)[0] && expected.get(i)[1] == priceIntervals.get(i)[1]);
		}
	}

	@Test
	public void testIntervalsStartOfLoadWindow() {
		final StepwiseIntegerCurve c = new StepwiseIntegerCurve();

		c.setValueAfter(-1, 5);
		c.setValueAfter(21, 8);
		c.setValueAfter(22, 9);
		c.setValueAfter(23, 10);
		c.setValueAfter(24, 15);
		c.setValueAfter(25, 20);
		c.setValueAfter(39, 29);
		c.setValueAfter(40, 30);
		c.setValueAfter(41, 31);

		final ILoadOption loadSlot = Mockito.mock(ILoadOption.class);
		when(loadSlot.getPricingEvent()).thenReturn(PricingEventType.START_OF_LOAD_WINDOW);
		when(loadSlot.getPricingDate()).thenReturn(IPortSlot.NO_PRICING_DATE);
		final IDischargeOption dischargeSlot = Mockito.mock(IDischargeOption.class);
		final PortTimeWindowsRecord portTimeWindowsRecord = new PortTimeWindowsRecord();
		portTimeWindowsRecord.setSlot(loadSlot, new TimeWindow(0, 10), 24, 0);
		portTimeWindowsRecord.setSlot(dischargeSlot, null, 24, 0);

		final ILoadPriceCalculator loadPriceCalculator = Mockito.mock(ILoadPriceCalculator.class);
		when(loadPriceCalculator.getEstimatedPurchasePrice(Matchers.<ILoadOption> any(), Matchers.<IDischargeOption> any(), Matchers.anyInt())).thenAnswer(new Answer<Integer>() {
			@Override
			public Integer answer(final InvocationOnMock invocation) throws Throwable {
				final Object[] args = invocation.getArguments();
				final int input = (int) args[2];
				return c.getValueAtPoint(input);
			}
		});
		when(loadSlot.getLoadPriceCalculator()).thenReturn(loadPriceCalculator);
		final PriceIntervalProviderHelper priceIntervalProviderHelper = createPriceIntervalProviderHelper(0);

		final IIntegerIntervalCurve curve = new IntegerIntervalCurve();
		curve.add(0);
		curve.add(20);
		curve.add(30);
		curve.add(40);

		final List<int[]> priceIntervals = new LinkedList<>();
		priceIntervalProviderHelper.buildIntervalsList(loadSlot, curve, null, 0, 50, portTimeWindowsRecord, priceIntervals);
		final List<int[]> expected = new LinkedList<>();
		expected.add(new int[] { 0, 5 });
		expected.add(new int[] { 50, Integer.MIN_VALUE });

		for (int i = 0; i < expected.size(); i++) {
			Assert.assertTrue(String.format("Expected %s got %s", Arrays.toString(expected.get(i)), Arrays.toString(priceIntervals.get(i))),
					expected.get(i)[0] == priceIntervals.get(i)[0] && expected.get(i)[1] == priceIntervals.get(i)[1]);
		}
	}

	@Test
	public void testBuildDateChangeCurveAsIntegerList() {
		final StepwiseIntegerCurve pp = new StepwiseIntegerCurve();

		pp.setValueAfter(-1, 5);
		pp.setValueAfter(21, 8);
		pp.setValueAfter(22, 9);
		pp.setValueAfter(23, 10);
		pp.setValueAfter(24, 15);
		pp.setValueAfter(25, 20);
		pp.setValueAfter(39, 29);
		pp.setValueAfter(40, 30);
		pp.setValueAfter(41, 31);

		final StepwiseIntegerCurve sp = new StepwiseIntegerCurve();

		sp.setValueAfter(-1, 5 - 5);
		sp.setValueAfter(21, 8 - 5);
		sp.setValueAfter(22, 9 - 5);
		sp.setValueAfter(23, 10 - 5);
		sp.setValueAfter(24, 15 - 5);
		sp.setValueAfter(25, 20 - 5);
		sp.setValueAfter(39, 29 - 5);
		sp.setValueAfter(40, 30 - 5);
		sp.setValueAfter(41, 31 - 5);

		final ILoadOption loadSlot = Mockito.mock(ILoadOption.class);
		when(loadSlot.getPricingEvent()).thenReturn(PricingEventType.START_OF_LOAD);
		when(loadSlot.getPricingDate()).thenReturn(IPortSlot.NO_PRICING_DATE);

		final IDischargeOption dischargeSlot = Mockito.mock(IDischargeOption.class);
		when(dischargeSlot.getPricingEvent()).thenReturn(PricingEventType.START_OF_LOAD);
		when(dischargeSlot.getPricingDate()).thenReturn(IPortSlot.NO_PRICING_DATE);
		final PortTimeWindowsRecord portTimeWindowsRecord = new PortTimeWindowsRecord();
		portTimeWindowsRecord.setSlot(loadSlot, new TimeWindow(0, 10), 24, 0);
		portTimeWindowsRecord.setSlot(dischargeSlot, new TimeWindow(20, 30), 24, 0);

		final ILoadPriceCalculator loadPriceCalculator = Mockito.mock(ILoadPriceCalculator.class);
		when(loadPriceCalculator.getEstimatedPurchasePrice(Matchers.<ILoadOption> any(), Matchers.<IDischargeOption> any(), Matchers.anyInt())).thenAnswer(new Answer<Integer>() {
			@Override
			public Integer answer(final InvocationOnMock invocation) throws Throwable {
				final Object[] args = invocation.getArguments();
				final int input = (int) args[2];
				return pp.getValueAtPoint(input);
			}
		});
		when(loadSlot.getLoadPriceCalculator()).thenReturn(loadPriceCalculator);

		final ISalesPriceCalculator salesPriceCalculator = Mockito.mock(ISalesPriceCalculator.class);
		when(salesPriceCalculator.getEstimatedSalesPrice(Matchers.<ILoadOption> any(), Matchers.<IDischargeOption> any(), Matchers.anyInt())).thenAnswer(new Answer<Integer>() {
			@Override
			public Integer answer(final InvocationOnMock invocation) throws Throwable {
				final Object[] args = invocation.getArguments();
				final int input = (int) args[2];
				return sp.getValueAtPoint(input);
			}
		});
		when(dischargeSlot.getDischargePriceCalculator()).thenReturn(salesPriceCalculator);

		final IPriceIntervalProvider loadPriceIntervalProvider = Mockito.mock(IPriceIntervalProvider.class);
		when(loadPriceIntervalProvider.getPriceHourIntervals(Matchers.<IPortSlot> any(), Mockito.anyInt(), Mockito.anyInt(), Matchers.<IPortTimeWindowsRecord> any()))
				.thenReturn(Arrays.asList(0, 10, 20, 30, 40, 50));
		final IPriceIntervalProvider dischargePriceIntervalProvider = Mockito.mock(IPriceIntervalProvider.class);
		when(dischargePriceIntervalProvider.getPriceHourIntervals(Matchers.<IPortSlot> any(), Mockito.anyInt(), Mockito.anyInt(), Matchers.<IPortTimeWindowsRecord> any()))
				.thenReturn(Arrays.asList(0 + 5, 10 + 5, 20 + 5, 30 + 5, 40 + 5, 50 + 5));

		final PriceIntervalProviderHelper priceIntervalProviderHelper = createPriceIntervalProviderHelper(0);

		final int[][] overlappingIntervals = priceIntervalProviderHelper.getOverlappingWindows(loadSlot, dischargeSlot, loadPriceIntervalProvider, dischargePriceIntervalProvider, 0, 50,
				portTimeWindowsRecord);
		final int[][] expected = new int[][] { { 0, 5 }, { 5, 10 }, { 10, 15 }, { 15, 20 }, { 20, 25 }, { 25, 30 }, { 30, 35 }, { 35, 40 }, { 40, 45 }, { 45, 50 }, };
		for (int i = 0; i < expected.length; i++) {
			Assert.assertTrue(String.format("Expected %s got %s", Arrays.toString(expected[i]), Arrays.toString(overlappingIntervals[i])),
					expected[i][0] == overlappingIntervals[i][0] && expected[i][1] == overlappingIntervals[i][1]);
		}

		final List<int[]> priceDiffs = priceIntervalProviderHelper.buildComplexPriceIntervals(0, 50, loadSlot, dischargeSlot, loadPriceIntervalProvider, dischargePriceIntervalProvider,
				portTimeWindowsRecord);
		for (int i = 0; i < priceDiffs.size() - 1; i++) {
			final int[] d = priceDiffs.get(i);
			Assert.assertTrue(d[1] == -5);
		}
		Assert.assertTrue(priceDiffs.get(priceDiffs.size() - 1)[0] == 50 && priceDiffs.get(priceDiffs.size() - 1)[1] == Integer.MIN_VALUE);
	}

	@Test
	public void testBuildDateChangeCurveAsIntegerListPlusTime() {
		final int start = 0;
		final int end = 50;

		final StepwiseIntegerCurve pp = new StepwiseIntegerCurve();

		pp.setValueAfter(-1, 5);
		pp.setValueAfter(21, 8);
		pp.setValueAfter(22, 9);
		pp.setValueAfter(23, 10);
		pp.setValueAfter(24, 15);
		pp.setValueAfter(25, 20);
		pp.setValueAfter(39, 29);
		pp.setValueAfter(40, 30);
		pp.setValueAfter(41, 31);
		pp.setValueAfter(45, 60);
		pp.setValueAfter(46, 70);

		final StepwiseIntegerCurve sp = new StepwiseIntegerCurve();

		sp.setValueAfter(-1, 5 - 5);
		sp.setValueAfter(21, 8 - 5);
		sp.setValueAfter(22, 9 - 5);
		sp.setValueAfter(23, 10 - 5);
		sp.setValueAfter(24, 15 - 5);
		sp.setValueAfter(25, 20 - 5);
		sp.setValueAfter(39, 29 - 5);
		sp.setValueAfter(40, 30 - 5);
		sp.setValueAfter(41, 31 - 5);
		sp.setValueAfter(45, 60 - 5);
		sp.setValueAfter(46, 65);

		final ILoadOption loadSlot = Mockito.mock(ILoadOption.class);
		when(loadSlot.getPricingEvent()).thenReturn(PricingEventType.START_OF_LOAD);
		when(loadSlot.getPricingDate()).thenReturn(IPortSlot.NO_PRICING_DATE);
		final IDischargeOption dischargeSlot = Mockito.mock(IDischargeOption.class);
		when(dischargeSlot.getPricingEvent()).thenReturn(PricingEventType.START_OF_LOAD);
		when(dischargeSlot.getPricingDate()).thenReturn(IPortSlot.NO_PRICING_DATE);
		final PortTimeWindowsRecord portTimeWindowsRecord = new PortTimeWindowsRecord();
		portTimeWindowsRecord.setSlot(loadSlot, new TimeWindow(0, 10), 24, 0);
		portTimeWindowsRecord.setSlot(dischargeSlot, new TimeWindow(20, 30), 24, 0);

		final IPort portL = Mockito.mock(IPort.class);
		final IPort portD = Mockito.mock(IPort.class);
		when(portL.getName()).thenReturn("A");
		when(portD.getName()).thenReturn("B");
		when(loadSlot.getPort()).thenReturn(portL);
		when(dischargeSlot.getPort()).thenReturn(portD);

		final ILoadPriceCalculator loadPriceCalculator = Mockito.mock(ILoadPriceCalculator.class);
		when(loadPriceCalculator.getEstimatedPurchasePrice(Matchers.<ILoadOption> any(), Matchers.<IDischargeOption> any(), Matchers.anyInt())).thenAnswer(new Answer<Integer>() {
			@Override
			public Integer answer(final InvocationOnMock invocation) throws Throwable {
				final Object[] args = invocation.getArguments();
				final int input = (int) args[2];
				return pp.getValueAtPoint(input);
				// return 0;
			}
		});
		when(loadSlot.getLoadPriceCalculator()).thenReturn(loadPriceCalculator);

		final ISalesPriceCalculator salesPriceCalculator = Mockito.mock(ISalesPriceCalculator.class);
		when(salesPriceCalculator.getEstimatedSalesPrice(Matchers.<ILoadOption> any(), Matchers.<IDischargeOption> any(), Matchers.anyInt())).thenAnswer(new Answer<Integer>() {
			@Override
			public Integer answer(final InvocationOnMock invocation) throws Throwable {
				final Object[] args = invocation.getArguments();
				final int input = (int) args[2];
				return sp.getValueAtPoint(input);
			}
		});
		when(dischargeSlot.getDischargePriceCalculator()).thenReturn(salesPriceCalculator);

		final PriceIntervalProviderHelper priceIntervalProviderHelper = createPriceIntervalProviderHelper(2, 0);

		final IPriceIntervalProvider loadPriceIntervalProvider = Mockito.mock(IPriceIntervalProvider.class);
		when(loadPriceIntervalProvider.getPriceHourIntervals(Matchers.<IPortSlot> any(), Mockito.anyInt(), Mockito.anyInt(), Matchers.<IPortTimeWindowsRecord> any()))
				.thenAnswer(new Answer<List<Integer>>() {
					@Override
					public List<Integer> answer(final InvocationOnMock invocation) throws Throwable {
						final Object[] args = invocation.getArguments();
						final IPortSlot portSlot = (IPortSlot) args[0];
						return priceIntervalProviderHelper.buildDateChangeCurveAsIntegerList(start, end, portSlot, new int[] { 0, 10, 20, 30, 40, 50 }, portTimeWindowsRecord);
					}
				});

		final IPriceIntervalProvider dischargePriceIntervalProvider = Mockito.mock(IPriceIntervalProvider.class);
		when(dischargePriceIntervalProvider.getPriceHourIntervals(Matchers.<IPortSlot> any(), Mockito.anyInt(), Mockito.anyInt(), Matchers.<IPortTimeWindowsRecord> any()))
				.thenAnswer(new Answer<List<Integer>>() {
					@Override
					public List<Integer> answer(final InvocationOnMock invocation) throws Throwable {
						final Object[] args = invocation.getArguments();
						final IPortSlot portSlot = (IPortSlot) args[0];
						return priceIntervalProviderHelper.buildDateChangeCurveAsIntegerList(start, end, portSlot, new int[] { 0 + 5, 10 + 5, 20 + 5, 30 + 5, 40 + 5, 50 + 5 }, portTimeWindowsRecord);
					}
				});

		final int[][] overlappingIntervals = priceIntervalProviderHelper.getOverlappingWindows(loadSlot, dischargeSlot, loadPriceIntervalProvider, dischargePriceIntervalProvider, 0, 50,
				portTimeWindowsRecord);
		final int[][] expected = new int[][] { { 0, 8 }, { 8, 13 }, { 13, 18 }, { 18, 23 }, { 23, 28 }, { 28, 33 }, { 33, 38 }, { 38, 43 }, { 43, 48 }, };
		for (int i = 0; i < expected.length; i++) {
			Assert.assertTrue(String.format("Expected %s got %s", Arrays.toString(expected[i]), Arrays.toString(overlappingIntervals[i])),
					expected[i][0] == overlappingIntervals[i][0] && expected[i][1] == overlappingIntervals[i][1]);
		}

		final List<int[]> priceDiffs = priceIntervalProviderHelper.buildComplexPriceIntervals(0, 50, loadSlot, dischargeSlot, loadPriceIntervalProvider, dischargePriceIntervalProvider,
				portTimeWindowsRecord);
		for (int i = 0; i < priceDiffs.size() - 1; i++) {
			final int[] d = priceDiffs.get(i);
			Assert.assertTrue(d[1] == -5);
		}
		Assert.assertTrue(priceDiffs.get(priceDiffs.size() - 1)[0] == 50 && priceDiffs.get(priceDiffs.size() - 1)[1] == Integer.MIN_VALUE);
	}

	static PriceIntervalProviderHelper createPriceIntervalProviderHelper(final int timeDiff) {
		final PriceIntervalProviderHelper ppih = new PriceIntervalProviderHelper();
		final ITimeZoneToUtcOffsetProvider t = Mockito.mock(ITimeZoneToUtcOffsetProvider.class);
		when(t.UTC(Matchers.anyInt(), Matchers.<IPort> any())).thenAnswer(new Answer<Integer>() {
			@Override
			public Integer answer(final InvocationOnMock invocation) throws Throwable {
				final Object[] args = invocation.getArguments();
				final int input = (int) args[0] + timeDiff;
				return input;
			}
		});
		when(t.localTime(Matchers.anyInt(), Matchers.<IPort> any())).thenAnswer(new Answer<Integer>() {
			@Override
			public Integer answer(final InvocationOnMock invocation) throws Throwable {
				final Object[] args = invocation.getArguments();
				final int input = (int) args[0] - timeDiff;
				return input;
			}
		});
		when(t.UTC(Matchers.anyInt(), Matchers.<IPortSlot> any())).thenAnswer(new Answer<Integer>() {
			@Override
			public Integer answer(final InvocationOnMock invocation) throws Throwable {
				final Object[] args = invocation.getArguments();
				final int input = (int) args[0] - 2;
				return input;
			}
		});
		final IVesselBaseFuelCalculator vesselBaseFuelCalculator = Mockito.mock(IVesselBaseFuelCalculator.class);
		when(vesselBaseFuelCalculator.getBaseFuelPrice(Matchers.<IVesselClass> any(), Mockito.anyInt())).thenReturn(OptimiserUnitConvertor.convertToInternalDailyRate(220));
		final Injector injector = Guice.createInjector(new AbstractModule() {

			@Override
			protected void configure() {
				bind(ITimeZoneToUtcOffsetProvider.class).toInstance(t);
				bind(IPriceIntervalProducer.class).to(PriceIntervalProducer.class);
				bind(IVesselProvider.class).to(HashMapVesselEditor.class);
				bind(IVesselBaseFuelCalculator.class).toInstance(vesselBaseFuelCalculator);
			}
		});

		injector.injectMembers(ppih);
		return ppih;
	}

	private static PriceIntervalProviderHelper createPriceIntervalProviderHelper(final int timeDiffA, final int timeDiffB) {
		final PriceIntervalProviderHelper ppih = new PriceIntervalProviderHelper();
		final ITimeZoneToUtcOffsetProvider t = Mockito.mock(ITimeZoneToUtcOffsetProvider.class);
		final IVesselBaseFuelCalculator vbfc = Mockito.mock(IVesselBaseFuelCalculator.class);
		when(t.UTC(Matchers.anyInt(), Matchers.<IPort> any())).thenAnswer(new Answer<Integer>() {
			@Override
			public Integer answer(final InvocationOnMock invocation) throws Throwable {
				final Object[] args = invocation.getArguments();
				final IPort port = (IPort) args[1];
				final int input = port.getName().equals("A") ? (int) args[0] + timeDiffA : (int) args[0] + timeDiffB;
				return input;
			}
		});
		when(t.localTime(Matchers.anyInt(), Matchers.<IPort> any())).thenAnswer(new Answer<Integer>() {
			@Override
			public Integer answer(final InvocationOnMock invocation) throws Throwable {
				final Object[] args = invocation.getArguments();
				final IPort port = (IPort) args[1];
				final int input = port.getName().equals("A") ? (int) args[0] - timeDiffA : (int) args[0] - timeDiffB;
				return input;
			}
		});

		final Injector injector = Guice.createInjector(new AbstractModule() {

			@Override
			protected void configure() {
				bind(ITimeZoneToUtcOffsetProvider.class).toInstance(t);
				bind(IPriceIntervalProducer.class).to(PriceIntervalProducer.class);
				bind(IVesselBaseFuelCalculator.class).toInstance(vbfc);
				bind(IVesselProvider.class).to(HashMapVesselEditor.class);
			}
		});

		injector.injectMembers(ppih);
		return ppih;
	}

	private static TimeWindowSchedulingCanalDistanceProvider getTimeWindowSchedulingCanalDistanceProvider(final List<DistanceMatrixEntry> distances) {
		final TimeWindowSchedulingCanalDistanceProvider timeWindowSchedulingCanalDistanceProvider = new TimeWindowSchedulingCanalDistanceProvider();

		final Injector injector = Guice.createInjector(new AbstractModule() {

			@Override
			protected void configure() {
				bind(IDistanceProvider.class).toInstance(getDistanceProvider(distances));
				bind(IRouteCostProvider.class).toInstance(getIRouteCostProvider());
				bind(IConsumptionRateCalculator.class).toInstance(Mockito.mock(IConsumptionRateCalculator.class));
			}
		});

		injector.injectMembers(timeWindowSchedulingCanalDistanceProvider);
		return timeWindowSchedulingCanalDistanceProvider;
	}

	private static TimeWindowsTrimming getTimeWindowsTrimming(final List<DistanceMatrixEntry> distances) {
		final TimeWindowsTrimming timeWindowsTrimming = new TimeWindowsTrimming();

		final ITimeZoneToUtcOffsetProvider t = Mockito.mock(ITimeZoneToUtcOffsetProvider.class);
		when(t.UTC(Matchers.anyInt(), Matchers.<IPort> any())).thenAnswer(new Answer<Integer>() {
			@Override
			public Integer answer(final InvocationOnMock invocation) throws Throwable {
				final Object[] args = invocation.getArguments();
				final int input = (int) args[0];
				return input;
			}
		});
		when(t.localTime(Matchers.anyInt(), Matchers.<IPort> any())).thenAnswer(new Answer<Integer>() {
			@Override
			public Integer answer(final InvocationOnMock invocation) throws Throwable {
				final Object[] args = invocation.getArguments();
				final int input = (int) args[0];
				return input;
			}
		});
		when(t.UTC(Matchers.anyInt(), Matchers.<IPortSlot> any())).thenAnswer(new Answer<Integer>() {
			@Override
			public Integer answer(final InvocationOnMock invocation) throws Throwable {
				final Object[] args = invocation.getArguments();
				final int input = (int) args[0] - 2;
				return input;
			}
		});

		final IVesselBaseFuelCalculator vesselBaseFuelCalculator = Mockito.mock(IVesselBaseFuelCalculator.class);
		when(vesselBaseFuelCalculator.getBaseFuelPrice(Matchers.<IVesselClass> any(), Mockito.anyInt())).thenReturn(OptimiserUnitConvertor.convertToInternalDailyRate(220));

		final Injector injector = Guice.createInjector(new AbstractModule() {

			@Override
			protected void configure() {
				bind(ITimeZoneToUtcOffsetProvider.class).toInstance(t);
				bind(IPriceIntervalProducer.class).to(PriceIntervalProducer.class);
				bind(IVesselProvider.class).to(HashMapVesselEditor.class);
				bind(IPriceIntervalProducer.class).to(PriceIntervalProducer.class);
				bind(PriceIntervalProviderHelper.class).toInstance(createPriceIntervalProviderHelper(0));
				bind(IDistanceProvider.class).toInstance(getDistanceProvider(distances));
				bind(IRouteCostProvider.class).toInstance(getIRouteCostProvider());
				bind(IConsumptionRateCalculator.class).toInstance(Mockito.mock(IConsumptionRateCalculator.class));

				bind(IVesselBaseFuelCalculator.class).toInstance(vesselBaseFuelCalculator);
				bind(ICharterRateCalculator.class).toInstance(Mockito.mock(ICharterRateCalculator.class));
				bind(ITimeWindowSchedulingCanalDistanceProvider.class).toInstance(getTimeWindowSchedulingCanalDistanceProvider(distances));
			}
		});

		injector.injectMembers(timeWindowsTrimming);
		return timeWindowsTrimming;

	}

	private static IDistanceProvider getDistanceProvider(final List<DistanceMatrixEntry> distances) {
		final IDistanceProvider distanceProvider = Mockito.mock(IDistanceProvider.class);
		Mockito.when(distanceProvider.getAllDistanceValues(Mockito.any(IPort.class), Mockito.any(IPort.class))).thenReturn(distances);
		return distanceProvider;
	}

	private static IRouteCostProvider getIRouteCostProvider() {
		final IRouteCostProvider routeCostProvider = Mockito.mock(IRouteCostProvider.class);
		Mockito.when(routeCostProvider.getRouteTransitTime(Mockito.any(), Mockito.any())).thenReturn(0);
		Mockito.when(routeCostProvider.getRouteCost(Mockito.eq(ERouteOption.DIRECT), Mockito.any(), Mockito.anyInt(), Mockito.any())).thenReturn(0L);
		Mockito.when(routeCostProvider.getRouteCost(Mockito.eq(ERouteOption.SUEZ), Mockito.any(), Mockito.anyInt(), Mockito.any()))
				.thenReturn(OptimiserUnitConvertor.convertToInternalFixedCost(500_000));
		Mockito.when(routeCostProvider.getRouteCost(Mockito.eq(ERouteOption.PANAMA), Mockito.any(), Mockito.anyInt(), Mockito.any()))
				.thenReturn(OptimiserUnitConvertor.convertToInternalFixedCost(450_000));
		return routeCostProvider;
	}

}
