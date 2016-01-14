package com.mmxlabs.scheduler.optimiser.contracts.impl;

import static org.mockito.Mockito.when;
import static org.mockito.Mockito.mock;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;
import org.mockito.Matchers;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.mmxlabs.common.curves.ICurve;
import com.mmxlabs.common.curves.StepwiseIntegerCurve;
import com.mmxlabs.optimiser.common.components.impl.TimeWindow;
import com.mmxlabs.scheduler.optimiser.components.IDischargeOption;
import com.mmxlabs.scheduler.optimiser.components.ILoadOption;
import com.mmxlabs.scheduler.optimiser.components.IPort;
import com.mmxlabs.scheduler.optimiser.components.IPortSlot;
import com.mmxlabs.scheduler.optimiser.components.PricingEventType;
import com.mmxlabs.scheduler.optimiser.contracts.ILoadPriceCalculator;
import com.mmxlabs.scheduler.optimiser.contracts.IPriceIntervalProvider;
import com.mmxlabs.scheduler.optimiser.contracts.ISalesPriceCalculator;
import com.mmxlabs.scheduler.optimiser.curves.IIntegerIntervalCurve;
import com.mmxlabs.scheduler.optimiser.curves.IPriceIntervalProducer;
import com.mmxlabs.scheduler.optimiser.curves.IntegerIntervalCurve;
import com.mmxlabs.scheduler.optimiser.curves.PriceIntervalProducer;
import com.mmxlabs.scheduler.optimiser.providers.ITimeZoneToUtcOffsetProvider;
import com.mmxlabs.scheduler.optimiser.providers.impl.TimeZoneToUtcOffsetProvider;
import com.mmxlabs.scheduler.optimiser.voyage.IPortTimeWindowsRecord;
import com.mmxlabs.scheduler.optimiser.voyage.impl.PortTimeWindowsRecord;

public class PriceIntervalProviderHelperTest {
	@Test
	public void testIntervals() {
		final StepwiseIntegerCurve c = new StepwiseIntegerCurve();
		
		c.setValueAfter(-1,5);
		c.setValueAfter(21,8);
		c.setValueAfter(22,9);
		c.setValueAfter(23,10);
		c.setValueAfter(24,15);
		c.setValueAfter(25,20);
		c.setValueAfter(39,29);
		c.setValueAfter(40,30);
		c.setValueAfter(41,31);
		
		ILoadOption loadSlot = Mockito.mock(ILoadOption.class);
		when(loadSlot.getPricingEvent()).thenReturn(PricingEventType.END_OF_LOAD);
		when(loadSlot.getPricingDate()).thenReturn(IPortSlot.NO_PRICING_DATE);
		IDischargeOption dischargeSlot = Mockito.mock(IDischargeOption.class);
		PortTimeWindowsRecord portTimeWindowsRecord = new PortTimeWindowsRecord();
		portTimeWindowsRecord.setSlot(loadSlot, null, 24, 0);
		portTimeWindowsRecord.setSlot(dischargeSlot, null, 24, 0);
		
		ILoadPriceCalculator loadPriceCalculator = Mockito.mock(ILoadPriceCalculator.class);
		when(loadPriceCalculator.getEstimatedPurchasePrice(Matchers.<ILoadOption> any(), Matchers.<IDischargeOption> any(), Matchers.anyInt())).thenAnswer(new Answer<Integer>() {
			@Override
			public Integer answer(final InvocationOnMock invocation) throws Throwable {
				final Object[] args = invocation.getArguments();
				final int input = (int) args[2];
				return c.getValueAtPoint(input);
			}
		});
		when(loadSlot.getLoadPriceCalculator()).thenReturn(loadPriceCalculator);
		PriceIntervalProviderHelper priceIntervalProviderHelper = createPriceIntervalProviderHelper(0);

		IIntegerIntervalCurve curve = new IntegerIntervalCurve();
		curve.add(0);
		curve.add(20);
		curve.add(30);
		curve.add(40);
		
		List<int[]> priceIntervals = new LinkedList<>();
		priceIntervalProviderHelper.buildIntervalsList(loadSlot, curve, null, 0, 50, portTimeWindowsRecord, priceIntervals);
		for (int[] i : priceIntervals) {
			System.out.println(Arrays.toString(i));
		}
		List<int[]> expected = new LinkedList<>();
		expected.add(new int[] {0,15});
		expected.add(new int[] {6,20});
		expected.add(new int[] {16,30});
		expected.add(new int[] {50,Integer.MIN_VALUE});
		
		System.out.println(c.getValueAtPoint(30));
		for (int i = 0; i < expected.size(); i++) {
			Assert.assertTrue(String.format("Expected %s got %s",Arrays.toString(expected.get(i)), Arrays.toString(priceIntervals.get(i))), expected.get(i)[0] == priceIntervals.get(i)[0] && expected.get(i)[1] == priceIntervals.get(i)[1]);
		}
	}
	
	@Test
	public void testIntervalsPlusTime() {
		final StepwiseIntegerCurve c = new StepwiseIntegerCurve();
		
		c.setValueAfter(-1,5);
		c.setValueAfter(21,8);
		c.setValueAfter(22,9);
		c.setValueAfter(23,10);
		c.setValueAfter(24,15);
		c.setValueAfter(25,20);
		c.setValueAfter(27,22);
		c.setValueAfter(28,23);
		c.setValueAfter(29,24);
		c.setValueAfter(30,25);
		c.setValueAfter(40,30);
		c.setValueAfter(41,31);
		c.setValueAfter(41,32);
		
		ILoadOption loadSlot = Mockito.mock(ILoadOption.class);
		when(loadSlot.getPricingEvent()).thenReturn(PricingEventType.END_OF_LOAD);
		when(loadSlot.getPricingDate()).thenReturn(IPortSlot.NO_PRICING_DATE);
		IDischargeOption dischargeSlot = Mockito.mock(IDischargeOption.class);
		PortTimeWindowsRecord portTimeWindowsRecord = new PortTimeWindowsRecord();
		portTimeWindowsRecord.setSlot(loadSlot, null, 24, 0);
		portTimeWindowsRecord.setSlot(dischargeSlot, null, 24, 0);
		
		ILoadPriceCalculator loadPriceCalculator = Mockito.mock(ILoadPriceCalculator.class);
		when(loadPriceCalculator.getEstimatedPurchasePrice(Matchers.<ILoadOption> any(), Matchers.<IDischargeOption> any(), Matchers.anyInt())).thenAnswer(new Answer<Integer>() {
			@Override
			public Integer answer(final InvocationOnMock invocation) throws Throwable {
				final Object[] args = invocation.getArguments();
				final int input = (int) args[2];
				return c.getValueAtPoint(input);
			}
		});
		when(loadSlot.getLoadPriceCalculator()).thenReturn(loadPriceCalculator);
		PriceIntervalProviderHelper priceIntervalProviderHelper = createPriceIntervalProviderHelper(1);
	
		IIntegerIntervalCurve curve = new IntegerIntervalCurve();
		curve.add(0);
		curve.add(20);
		curve.add(30);
		curve.add(40);
		
		List<int[]> priceIntervals = new LinkedList<>();
		priceIntervalProviderHelper.buildIntervalsList(loadSlot, curve, null, 0, 50, portTimeWindowsRecord, priceIntervals);
		for (int[] i : priceIntervals) {
			System.out.println(Arrays.toString(i));
		}
		List<int[]> expected = new LinkedList<>();
		expected.add(new int[] {0,20});
		expected.add(new int[] {5,25});
		expected.add(new int[] {15,30});
		expected.add(new int[] {50,Integer.MIN_VALUE});
		
		System.out.println(c.getValueAtPoint(30));
		for (int i = 0; i < expected.size(); i++) {
			Assert.assertTrue(String.format("Expected %s got %s",Arrays.toString(expected.get(i)), Arrays.toString(priceIntervals.get(i))), expected.get(i)[0] == priceIntervals.get(i)[0] && expected.get(i)[1] == priceIntervals.get(i)[1]);
		}
	}

	@Test
	public void testIntervalsStartOfLoadWindowPlusTime() {
		final StepwiseIntegerCurve c = new StepwiseIntegerCurve();
		
		c.setValueAfter(-2,4);
		c.setValueAfter(-1,5);
		c.setValueAfter(0,6);
		c.setValueAfter(1,7);
		c.setValueAfter(2,8);
		c.setValueAfter(21,8);
		c.setValueAfter(22,9);
		c.setValueAfter(23,10);
		c.setValueAfter(24,15);
		c.setValueAfter(25,20);
		c.setValueAfter(39,29);
		c.setValueAfter(40,30);
		c.setValueAfter(41,31);
		
		ILoadOption loadSlot = Mockito.mock(ILoadOption.class);
		when(loadSlot.getPricingEvent()).thenReturn(PricingEventType.START_OF_LOAD_WINDOW);
		when(loadSlot.getPricingDate()).thenReturn(IPortSlot.NO_PRICING_DATE);
		IDischargeOption dischargeSlot = Mockito.mock(IDischargeOption.class);
		PortTimeWindowsRecord portTimeWindowsRecord = new PortTimeWindowsRecord();
		portTimeWindowsRecord.setSlot(loadSlot, new TimeWindow(0, 10), 24, 0);
		portTimeWindowsRecord.setSlot(dischargeSlot, null, 24, 0);
		
		ILoadPriceCalculator loadPriceCalculator = Mockito.mock(ILoadPriceCalculator.class);
		when(loadPriceCalculator.getEstimatedPurchasePrice(Matchers.<ILoadOption> any(), Matchers.<IDischargeOption> any(), Matchers.anyInt())).thenAnswer(new Answer<Integer>() {
			@Override
			public Integer answer(final InvocationOnMock invocation) throws Throwable {
				final Object[] args = invocation.getArguments();
				final int input = (int) args[2];
				return c.getValueAtPoint(input);
			}
		});
		when(loadSlot.getLoadPriceCalculator()).thenReturn(loadPriceCalculator);
		PriceIntervalProviderHelper priceIntervalProviderHelper = createPriceIntervalProviderHelper(2);
		
		IIntegerIntervalCurve curve = new IntegerIntervalCurve();
		curve.add(0);
		curve.add(20);
		curve.add(30);
		curve.add(40);
		
		List<int[]> priceIntervals = new LinkedList<>();
		priceIntervalProviderHelper.buildIntervalsList(loadSlot, curve, null, 0, 50, portTimeWindowsRecord, priceIntervals);
		for (int[] i : priceIntervals) {
			System.out.println(Arrays.toString(i));
		}
		List<int[]> expected = new LinkedList<>();
		expected.add(new int[] {0,8});
		expected.add(new int[] {50,Integer.MIN_VALUE});
		
		System.out.println(c.getValueAtPoint(30));
		for (int i = 0; i < expected.size(); i++) {
			Assert.assertTrue(String.format("Expected %s got %s",Arrays.toString(expected.get(i)), Arrays.toString(priceIntervals.get(i))), expected.get(i)[0] == priceIntervals.get(i)[0] && expected.get(i)[1] == priceIntervals.get(i)[1]);
		}
	}
	
	@Test
	public void testIntervalsStartOfLoadWindow() {
		final StepwiseIntegerCurve c = new StepwiseIntegerCurve();
		
		c.setValueAfter(-1,5);
		c.setValueAfter(21,8);
		c.setValueAfter(22,9);
		c.setValueAfter(23,10);
		c.setValueAfter(24,15);
		c.setValueAfter(25,20);
		c.setValueAfter(39,29);
		c.setValueAfter(40,30);
		c.setValueAfter(41,31);
		
		ILoadOption loadSlot = Mockito.mock(ILoadOption.class);
		when(loadSlot.getPricingEvent()).thenReturn(PricingEventType.START_OF_LOAD_WINDOW);
		when(loadSlot.getPricingDate()).thenReturn(IPortSlot.NO_PRICING_DATE);
		IDischargeOption dischargeSlot = Mockito.mock(IDischargeOption.class);
		PortTimeWindowsRecord portTimeWindowsRecord = new PortTimeWindowsRecord();
		portTimeWindowsRecord.setSlot(loadSlot, new TimeWindow(0, 10), 24, 0);
		portTimeWindowsRecord.setSlot(dischargeSlot, null, 24, 0);
		
		ILoadPriceCalculator loadPriceCalculator = Mockito.mock(ILoadPriceCalculator.class);
		when(loadPriceCalculator.getEstimatedPurchasePrice(Matchers.<ILoadOption> any(), Matchers.<IDischargeOption> any(), Matchers.anyInt())).thenAnswer(new Answer<Integer>() {
			@Override
			public Integer answer(final InvocationOnMock invocation) throws Throwable {
				final Object[] args = invocation.getArguments();
				final int input = (int) args[2];
				return c.getValueAtPoint(input);
			}
		});
		when(loadSlot.getLoadPriceCalculator()).thenReturn(loadPriceCalculator);
		PriceIntervalProviderHelper priceIntervalProviderHelper = createPriceIntervalProviderHelper(0);

		IIntegerIntervalCurve curve = new IntegerIntervalCurve();
		curve.add(0);
		curve.add(20);
		curve.add(30);
		curve.add(40);
		
		List<int[]> priceIntervals = new LinkedList<>();
		priceIntervalProviderHelper.buildIntervalsList(loadSlot, curve, null, 0, 50, portTimeWindowsRecord, priceIntervals);
		for (int[] i : priceIntervals) {
			System.out.println(Arrays.toString(i));
		}
		List<int[]> expected = new LinkedList<>();
		expected.add(new int[] {0,5});
		expected.add(new int[] {50,Integer.MIN_VALUE});
		
		System.out.println(c.getValueAtPoint(30));
		for (int i = 0; i < expected.size(); i++) {
			Assert.assertTrue(String.format("Expected %s got %s",Arrays.toString(expected.get(i)), Arrays.toString(priceIntervals.get(i))), expected.get(i)[0] == priceIntervals.get(i)[0] && expected.get(i)[1] == priceIntervals.get(i)[1]);
		}
	}
	
	@Test
	public void testBuildDateChangeCurveAsIntegerList() {
		final StepwiseIntegerCurve pp = new StepwiseIntegerCurve();
		
		pp.setValueAfter(-1,5);
		pp.setValueAfter(21,8);
		pp.setValueAfter(22,9);
		pp.setValueAfter(23,10);
		pp.setValueAfter(24,15);
		pp.setValueAfter(25,20);
		pp.setValueAfter(39,29);
		pp.setValueAfter(40,30);
		pp.setValueAfter(41,31);
		
		final StepwiseIntegerCurve sp = new StepwiseIntegerCurve();
		
		sp.setValueAfter(-1,5-5);
		sp.setValueAfter(21,8-5);
		sp.setValueAfter(22,9-5);
		sp.setValueAfter(23,10-5);
		sp.setValueAfter(24,15-5);
		sp.setValueAfter(25,20-5);
		sp.setValueAfter(39,29-5);
		sp.setValueAfter(40,30-5);
		sp.setValueAfter(41,31-5);
		
		ILoadOption loadSlot = Mockito.mock(ILoadOption.class);
		when(loadSlot.getPricingEvent()).thenReturn(PricingEventType.START_OF_LOAD);
		when(loadSlot.getPricingDate()).thenReturn(IPortSlot.NO_PRICING_DATE);
		
		
		IDischargeOption dischargeSlot = Mockito.mock(IDischargeOption.class);
		when(dischargeSlot.getPricingEvent()).thenReturn(PricingEventType.START_OF_LOAD);
		when(dischargeSlot.getPricingDate()).thenReturn(IPortSlot.NO_PRICING_DATE);
		PortTimeWindowsRecord portTimeWindowsRecord = new PortTimeWindowsRecord();
		portTimeWindowsRecord.setSlot(loadSlot, new TimeWindow(0, 10), 24, 0);
		portTimeWindowsRecord.setSlot(dischargeSlot, new TimeWindow(20, 30), 24, 0);
		
		ILoadPriceCalculator loadPriceCalculator = Mockito.mock(ILoadPriceCalculator.class);
		when(loadPriceCalculator.getEstimatedPurchasePrice(Matchers.<ILoadOption> any(), Matchers.<IDischargeOption> any(), Matchers.anyInt())).thenAnswer(new Answer<Integer>() {
			@Override
			public Integer answer(final InvocationOnMock invocation) throws Throwable {
				final Object[] args = invocation.getArguments();
				final int input = (int) args[2];
				return pp.getValueAtPoint(input);
			}
		});
		when(loadSlot.getLoadPriceCalculator()).thenReturn(loadPriceCalculator);
		
		
		ISalesPriceCalculator salesPriceCalculator = Mockito.mock(ISalesPriceCalculator.class);
		when(salesPriceCalculator.getEstimatedSalesPrice(Matchers.<ILoadOption> any(), Matchers.<IDischargeOption> any(), Matchers.anyInt())).thenAnswer(new Answer<Integer>() {
			@Override
			public Integer answer(final InvocationOnMock invocation) throws Throwable {
				final Object[] args = invocation.getArguments();
				final int input = (int) args[2];
				return sp.getValueAtPoint(input);
			}
		});
		when(dischargeSlot.getDischargePriceCalculator()).thenReturn(salesPriceCalculator);

		IPriceIntervalProvider loadPriceIntervalProvider = Mockito.mock(IPriceIntervalProvider.class);
		when(loadPriceIntervalProvider.getPriceHourIntervals(Matchers.<IPortSlot> any(), Mockito.anyInt(), Mockito.anyInt(), Matchers.<IPortTimeWindowsRecord> any())).thenReturn(Arrays.asList(0,10,20,30,40,50));
		IPriceIntervalProvider dischargePriceIntervalProvider = Mockito.mock(IPriceIntervalProvider.class);
		when(dischargePriceIntervalProvider.getPriceHourIntervals(Matchers.<IPortSlot> any(), Mockito.anyInt(), Mockito.anyInt(), Matchers.<IPortTimeWindowsRecord> any())).thenReturn(Arrays.asList(0+5,10+5,20+5,30+5,40+5,50+5));
		
		PriceIntervalProviderHelper priceIntervalProviderHelper = createPriceIntervalProviderHelper(0);
		
		List<int[]> priceIntervals = new LinkedList<>();
		int[][] overlappingIntervals = priceIntervalProviderHelper.getOverlappingWindows(loadSlot, dischargeSlot, loadPriceIntervalProvider, dischargePriceIntervalProvider, 0, 50, portTimeWindowsRecord);
		for (int[] i : overlappingIntervals) {
			System.out.println(Arrays.toString(i));
		}
		int[][] expected = new int[][]{
				{0, 5},
				{5, 10},
				{10, 15},
				{15, 20},
				{20, 25},
				{25, 30},
				{30, 35},
				{35, 40},
				{40, 45},
				{45, 50},
		};
		for (int i = 0; i < expected.length; i++) {
			Assert.assertTrue(String.format("Expected %s got %s",Arrays.toString(expected[i]), Arrays.toString(overlappingIntervals[i])), expected[i][0] == overlappingIntervals[i][0] && expected[i][1] == overlappingIntervals[i][1]);
		}
		
		List<int[]> priceDiffs = priceIntervalProviderHelper.buildComplexPriceIntervals(0, 50, loadSlot, dischargeSlot, loadPriceIntervalProvider, dischargePriceIntervalProvider, portTimeWindowsRecord);
		for (int[] d : priceDiffs) {
			System.out.println(Arrays.toString(d));
		}
		for (int i = 0; i < priceDiffs.size() - 1; i++) {
			int[] d = priceDiffs.get(i);
			Assert.assertTrue(d[1] == -5);
		}
		Assert.assertTrue(priceDiffs.get(priceDiffs.size()-1)[0] == 50 && priceDiffs.get(priceDiffs.size()-1)[1] == Integer.MIN_VALUE);
	}

	@Test
	public void testBuildDateChangeCurveAsIntegerListPlusTime() {
		final int start = 0;
		final int end = 50;
		
		final StepwiseIntegerCurve pp = new StepwiseIntegerCurve();
		
		pp.setValueAfter(-1,5);
		pp.setValueAfter(21,8);
		pp.setValueAfter(22,9);
		pp.setValueAfter(23,10);
		pp.setValueAfter(24,15);
		pp.setValueAfter(25,20);
		pp.setValueAfter(39,29);
		pp.setValueAfter(40,30);
		pp.setValueAfter(41,31);
		pp.setValueAfter(45,60);
		pp.setValueAfter(46,70);
		
		final StepwiseIntegerCurve sp = new StepwiseIntegerCurve();
		
		sp.setValueAfter(-1,5-5);
		sp.setValueAfter(21,8-5);
		sp.setValueAfter(22,9-5);
		sp.setValueAfter(23,10-5);
		sp.setValueAfter(24,15-5);
		sp.setValueAfter(25,20-5);
		sp.setValueAfter(39,29-5);
		sp.setValueAfter(40,30-5);
		sp.setValueAfter(41,31-5);
		sp.setValueAfter(45,60-5);
		sp.setValueAfter(46,65);
		
		ILoadOption loadSlot = Mockito.mock(ILoadOption.class);
		when(loadSlot.getPricingEvent()).thenReturn(PricingEventType.START_OF_LOAD);
		when(loadSlot.getPricingDate()).thenReturn(IPortSlot.NO_PRICING_DATE);
		IDischargeOption dischargeSlot = Mockito.mock(IDischargeOption.class);
		when(dischargeSlot.getPricingEvent()).thenReturn(PricingEventType.START_OF_LOAD);
		when(dischargeSlot.getPricingDate()).thenReturn(IPortSlot.NO_PRICING_DATE);
		final PortTimeWindowsRecord portTimeWindowsRecord = new PortTimeWindowsRecord();
		portTimeWindowsRecord.setSlot(loadSlot, new TimeWindow(0, 10), 24, 0);
		portTimeWindowsRecord.setSlot(dischargeSlot, new TimeWindow(20, 30), 24, 0);
		
		IPort portL = Mockito.mock(IPort.class);
		IPort portD = Mockito.mock(IPort.class);
		when(portL.getName()).thenReturn("A");
		when(portD.getName()).thenReturn("B");
		when(loadSlot.getPort()).thenReturn(portL);
		when(dischargeSlot.getPort()).thenReturn(portD);
		
		ILoadPriceCalculator loadPriceCalculator = Mockito.mock(ILoadPriceCalculator.class);
		when(loadPriceCalculator.getEstimatedPurchasePrice(Matchers.<ILoadOption> any(), Matchers.<IDischargeOption> any(), Matchers.anyInt())).thenAnswer(new Answer<Integer>() {
			@Override
			public Integer answer(final InvocationOnMock invocation) throws Throwable {
				final Object[] args = invocation.getArguments();
				final int input = (int) args[2];
				return pp.getValueAtPoint(input);
//				return 0;
			}
		});
		when(loadSlot.getLoadPriceCalculator()).thenReturn(loadPriceCalculator);
		
		
		ISalesPriceCalculator salesPriceCalculator = Mockito.mock(ISalesPriceCalculator.class);
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

		IPriceIntervalProvider loadPriceIntervalProvider = Mockito.mock(IPriceIntervalProvider.class);
		when(loadPriceIntervalProvider.getPriceHourIntervals(Matchers.<IPortSlot> any(), Mockito.anyInt(), Mockito.anyInt(), Matchers.<IPortTimeWindowsRecord> any())).thenAnswer(new Answer<List<Integer>>() {
			@Override
			public List<Integer> answer(final InvocationOnMock invocation) throws Throwable {
				final Object[] args = invocation.getArguments();
				final IPortSlot portSlot = (IPortSlot) args[0];
				return priceIntervalProviderHelper.buildDateChangeCurveAsIntegerList(start, end, portSlot, new int[] {0,10,20,30,40,50}, portTimeWindowsRecord);
			}
		});
		
		IPriceIntervalProvider dischargePriceIntervalProvider = Mockito.mock(IPriceIntervalProvider.class);
		when(dischargePriceIntervalProvider.getPriceHourIntervals(Matchers.<IPortSlot> any(), Mockito.anyInt(), Mockito.anyInt(), Matchers.<IPortTimeWindowsRecord> any())).thenAnswer(new Answer<List<Integer>>() {
			@Override
			public List<Integer> answer(final InvocationOnMock invocation) throws Throwable {
				final Object[] args = invocation.getArguments();
				final IPortSlot portSlot = (IPortSlot) args[0];
				return priceIntervalProviderHelper.buildDateChangeCurveAsIntegerList(start, end, portSlot, new int[] {0+5,10+5,20+5,30+5,40+5,50+5}, portTimeWindowsRecord);
			}
		});

		int[][] overlappingIntervals = priceIntervalProviderHelper.getOverlappingWindows(loadSlot, dischargeSlot, loadPriceIntervalProvider, dischargePriceIntervalProvider, 0, 50, portTimeWindowsRecord);
		for (int[] i : overlappingIntervals) {
			System.out.println(Arrays.toString(i));
		}
		int[][] expected = new int[][]{
				{0, 8},
				{8, 13},
				{13, 18},
				{18, 23},
				{23, 28},
				{28, 33},
				{33, 38},
				{38, 43},
				{43, 48},
		};
		for (int i = 0; i < expected.length; i++) {
			Assert.assertTrue(String.format("Expected %s got %s",Arrays.toString(expected[i]), Arrays.toString(overlappingIntervals[i])), expected[i][0] == overlappingIntervals[i][0] && expected[i][1] == overlappingIntervals[i][1]);
		}
		
		List<int[]> priceDiffs = priceIntervalProviderHelper.buildComplexPriceIntervals(0, 50, loadSlot, dischargeSlot, loadPriceIntervalProvider, dischargePriceIntervalProvider, portTimeWindowsRecord);
		for (int[] d : priceDiffs) {
			System.out.println(Arrays.toString(d));
		}
		for (int i = 0; i < priceDiffs.size() - 1; i++) {
			int[] d = priceDiffs.get(i);
			Assert.assertTrue(d[1] == -5);
		}
		Assert.assertTrue(priceDiffs.get(priceDiffs.size()-1)[0] == 50 && priceDiffs.get(priceDiffs.size()-1)[1] == Integer.MIN_VALUE);
	}

	private PriceIntervalProviderHelper createPriceIntervalProviderHelper(final int timeDiff) {
		PriceIntervalProviderHelper ppih = new PriceIntervalProviderHelper();
		final ITimeZoneToUtcOffsetProvider t = Mockito.mock(ITimeZoneToUtcOffsetProvider.class);
		when(t.UTC(Matchers.anyInt(), Matchers.<IPort> any())).thenAnswer(new Answer<Integer>() {
			@Override
			public Integer answer(final InvocationOnMock invocation) throws Throwable {
				final Object[] args = invocation.getArguments();
				final int input = (int) args[0]+timeDiff;
				return input;
			}
		});
		when(t.localTime(Matchers.anyInt(), Matchers.<IPort> any())).thenAnswer(new Answer<Integer>() {
			@Override
			public Integer answer(final InvocationOnMock invocation) throws Throwable {
				final Object[] args = invocation.getArguments();
				final int input = (int) args[0]-timeDiff;
				return input;
			}
		});
		when(t.UTC(Matchers.anyInt(), Matchers.<IPortSlot> any())).thenAnswer(new Answer<Integer>() {
			@Override
			public Integer answer(final InvocationOnMock invocation) throws Throwable {
				final Object[] args = invocation.getArguments();
				final int input = (int) args[0]-2;
				return input;
			}
		});
		
		Injector injector = Guice.createInjector(new AbstractModule() {

			@Override
			protected void configure() {
				bind(ITimeZoneToUtcOffsetProvider.class).toInstance(t);
				bind(IPriceIntervalProducer.class).to(PriceIntervalProducer.class);
			}
		});

		injector.injectMembers(ppih);
		return ppih;
	}
	
	private PriceIntervalProviderHelper createPriceIntervalProviderHelper(final int timeDiffA, final int timeDiffB) {
		PriceIntervalProviderHelper ppih = new PriceIntervalProviderHelper();
		final ITimeZoneToUtcOffsetProvider t = Mockito.mock(ITimeZoneToUtcOffsetProvider.class);
		when(t.UTC(Matchers.anyInt(), Matchers.<IPort> any())).thenAnswer(new Answer<Integer>() {
			@Override
			public Integer answer(final InvocationOnMock invocation) throws Throwable {
				final Object[] args = invocation.getArguments();
				final IPort port = (IPort) args[1];
				final int input = port.getName().equals("A") ? (int) args[0]+timeDiffA : (int) args[0]+timeDiffB;
				return input;
			}
		});
		when(t.localTime(Matchers.anyInt(), Matchers.<IPort> any())).thenAnswer(new Answer<Integer>() {
			@Override
			public Integer answer(final InvocationOnMock invocation) throws Throwable {
				final Object[] args = invocation.getArguments();
				final IPort port = (IPort) args[1];
				final int input = port.getName().equals("A") ? (int) args[0]-timeDiffA : (int) args[0]-timeDiffB;
				return input;
			}
		});
		
		Injector injector = Guice.createInjector(new AbstractModule() {

			@Override
			protected void configure() {
				bind(ITimeZoneToUtcOffsetProvider.class).toInstance(t);
				bind(IPriceIntervalProducer.class).to(PriceIntervalProducer.class);
			}
		});

		injector.injectMembers(ppih);
		return ppih;
	}


}
