/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2021
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.evaluation;

import java.util.LinkedList;
import java.util.List;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import com.mmxlabs.common.CollectionsUtil;
import com.mmxlabs.scheduler.optimiser.moves.util.MetricType;
import com.mmxlabs.scheduler.optimiser.scheduling.ScheduledRecord;

public class ScheduledVoyagePlanResultTests {

	@Test
	public void checkMaxDuration() {

		// Check max duration
		final ScheduledVoyagePlanResult resultA = new ScheduledVoyagePlanResult();
		resultA.metrics = new long[MetricType.values().length];
		resultA.arrivalTimes = CollectionsUtil.toArrayList(new int[] { 0 });
		resultA.returnTime = 10;

		final ScheduledVoyagePlanResult resultB = new ScheduledVoyagePlanResult();
		resultB.metrics = new long[MetricType.values().length];
		resultB.arrivalTimes = CollectionsUtil.toArrayList(new int[] { 0 });

		final ScheduledRecord a = new ScheduledRecord(resultA, new PreviousHeelRecord(), 0, 9);
		final ScheduledRecord b = new ScheduledRecord(resultB, new PreviousHeelRecord(), 0, Integer.MAX_VALUE);

		Assertions.assertFalse(a.betterThan(b, false));
		Assertions.assertTrue(b.betterThan(a, false));
	}

	@Test
	public void checkPNL() {
		// Check pnl
		final ScheduledVoyagePlanResult resultA = new ScheduledVoyagePlanResult();
		resultA.metrics = new long[MetricType.values().length];
		resultA.arrivalTimes = CollectionsUtil.toArrayList(new int[] { 0 });

		final ScheduledVoyagePlanResult resultB = new ScheduledVoyagePlanResult();
		resultB.metrics = new long[MetricType.values().length];
		resultB.arrivalTimes = CollectionsUtil.toArrayList(new int[] { 0 });

		final ScheduledRecord a = new ScheduledRecord(resultA, new PreviousHeelRecord(), 0, Integer.MAX_VALUE);
		final ScheduledRecord b = new ScheduledRecord(resultB, new PreviousHeelRecord(), 0, Integer.MAX_VALUE);

		a.metrics[MetricType.PNL.ordinal()] = 1;
		b.metrics[MetricType.PNL.ordinal()] = 2;

		Assertions.assertFalse(a.betterThan(b, false));
		Assertions.assertTrue(b.betterThan(a, false));
	}

	@Test
	public void checkCapacity() {
		// Check capacity
		final ScheduledVoyagePlanResult resultA = new ScheduledVoyagePlanResult();
		resultA.metrics = new long[MetricType.values().length];
		resultA.arrivalTimes = CollectionsUtil.toArrayList(new int[] { 0 });

		final ScheduledVoyagePlanResult resultB = new ScheduledVoyagePlanResult();
		resultB.metrics = new long[MetricType.values().length];
		resultB.arrivalTimes = CollectionsUtil.toArrayList(new int[] { 0 });

		final ScheduledRecord a = new ScheduledRecord(resultA, new PreviousHeelRecord(), 0, Integer.MAX_VALUE);
		final ScheduledRecord b = new ScheduledRecord(resultB, new PreviousHeelRecord(), 0, Integer.MAX_VALUE);

		a.metrics[MetricType.CAPACITY.ordinal()] = 2;
		b.metrics[MetricType.CAPACITY.ordinal()] = 1;

		Assertions.assertFalse(a.betterThan(b, false));
		Assertions.assertTrue(b.betterThan(a, false));
	}

	@Test
	public void checkLateness() {
		// Check lateness
		final ScheduledVoyagePlanResult resultA = new ScheduledVoyagePlanResult();
		resultA.metrics = new long[MetricType.values().length];
		resultA.arrivalTimes = CollectionsUtil.toArrayList(new int[] { 0 });

		final ScheduledVoyagePlanResult resultB = new ScheduledVoyagePlanResult();
		resultB.metrics = new long[MetricType.values().length];
		resultB.arrivalTimes = CollectionsUtil.toArrayList(new int[] { 0 });

		final ScheduledRecord a = new ScheduledRecord(resultA, new PreviousHeelRecord(), 0, Integer.MAX_VALUE);
		final ScheduledRecord b = new ScheduledRecord(resultB, new PreviousHeelRecord(), 0, Integer.MAX_VALUE);

		a.metrics[MetricType.LATENESS.ordinal()] = 2;
		b.metrics[MetricType.LATENESS.ordinal()] = 1;

		Assertions.assertFalse(a.betterThan(b, false));
		Assertions.assertTrue(b.betterThan(a, false));
	}

	@Test
	public void checkChainUpForCompare() {
		// Test chain up for compare
		final ScheduledVoyagePlanResult resultA1 = new ScheduledVoyagePlanResult();
		resultA1.arrivalTimes = CollectionsUtil.toArrayList(new int[] { 0 });
		resultA1.metrics = new long[MetricType.values().length];
		resultA1.metrics[MetricType.LATENESS.ordinal()] = 2;

		final ScheduledVoyagePlanResult resultB1 = new ScheduledVoyagePlanResult();
		resultB1.arrivalTimes = CollectionsUtil.toArrayList(new int[] { 0 });
		resultB1.metrics = new long[MetricType.values().length];
		resultB1.metrics[MetricType.LATENESS.ordinal()] = 1;

		final ScheduledRecord a1 = new ScheduledRecord(resultA1, new PreviousHeelRecord(), 0, Integer.MAX_VALUE);
		final ScheduledRecord b1 = new ScheduledRecord(resultB1, new PreviousHeelRecord(), 0, Integer.MAX_VALUE);

		Assertions.assertFalse(a1.betterThan(b1, false));
		Assertions.assertTrue(b1.betterThan(a1, false));

		final ScheduledVoyagePlanResult resultA2 = new ScheduledVoyagePlanResult();
		resultA2.arrivalTimes = CollectionsUtil.toArrayList(new int[] { 0 });
		resultA2.metrics = new long[MetricType.values().length];
		resultA2.metrics[MetricType.LATENESS.ordinal()] = 0;

		final ScheduledVoyagePlanResult resultB2 = new ScheduledVoyagePlanResult();
		resultB2.arrivalTimes = CollectionsUtil.toArrayList(new int[] { 0 });
		resultB2.metrics = new long[MetricType.values().length];
		resultB2.metrics[MetricType.LATENESS.ordinal()] = 3;

		final ScheduledRecord a2 = new ScheduledRecord(resultA2, new PreviousHeelRecord(), a1);
		final ScheduledRecord b2 = new ScheduledRecord(resultB2, new PreviousHeelRecord(), b1);

		Assertions.assertTrue(a2.betterThan(b2, false));
		Assertions.assertFalse(b2.betterThan(a2, false));

		final ScheduledVoyagePlanResult resultA3 = new ScheduledVoyagePlanResult();
		resultA3.arrivalTimes = CollectionsUtil.toArrayList(new int[] { 0 });
		resultA3.metrics = new long[MetricType.values().length];

		final ScheduledVoyagePlanResult resultB3 = new ScheduledVoyagePlanResult();
		resultB3.arrivalTimes = CollectionsUtil.toArrayList(new int[] { 0 });
		resultB3.metrics = new long[MetricType.values().length];

		final ScheduledRecord a3 = new ScheduledRecord(resultA3, new PreviousHeelRecord(), a2);
		final ScheduledRecord b3 = new ScheduledRecord(resultB3, new PreviousHeelRecord(), b2);

		a3.maxDurationLateness = 1;
		b3.maxDurationLateness = 0;
		Assertions.assertFalse(a3.betterThan(b3, false));
		Assertions.assertTrue(b3.betterThan(a3, false));

		{
			final List<ScheduledRecord> l = new LinkedList<>();
			l.add(a3);
			l.add(b3);
			l.sort(ScheduledRecord.getComparator(false));

			Assertions.assertEquals(l.get(0), b3);
			Assertions.assertEquals(l.get(1), a3);
		}
		{
			final List<ScheduledRecord> l = new LinkedList<>();
			l.add(b3);
			l.add(a3);
			l.sort(ScheduledRecord.getComparator(false));

			Assertions.assertEquals(l.get(0), b3);
			Assertions.assertEquals(l.get(1), a3);
		}
	}

	@Test
	public void checkChainDates() {
		// test chain dates
		final ScheduledVoyagePlanResult resultA1 = new ScheduledVoyagePlanResult();
		resultA1.arrivalTimes = CollectionsUtil.toArrayList(new int[] { 2 });
		resultA1.metrics = new long[MetricType.values().length];
		resultA1.returnTime = 5;

		final ScheduledRecord a1 = new ScheduledRecord(resultA1, new PreviousHeelRecord(), 0, Integer.MAX_VALUE);

		final ScheduledVoyagePlanResult resultA2 = new ScheduledVoyagePlanResult();
		resultA2.arrivalTimes = CollectionsUtil.toArrayList(new int[] { 5 });
		resultA2.metrics = new long[MetricType.values().length];
		resultA2.returnTime = 10;

		final ScheduledRecord a2 = new ScheduledRecord(resultA2, new PreviousHeelRecord(), a1);

		Assertions.assertEquals(a2.sequenceStartTime, 2);
		Assertions.assertEquals(a2.currentEndTime, 10);

		final ScheduledVoyagePlanResult resultA3 = new ScheduledVoyagePlanResult();
		resultA3.arrivalTimes = CollectionsUtil.toArrayList(new int[] { 10 });
		resultA3.metrics = new long[MetricType.values().length];
		resultA3.returnTime = 20;

		final ScheduledRecord a3 = new ScheduledRecord(resultA3, new PreviousHeelRecord(), a2);

		Assertions.assertEquals(a3.sequenceStartTime, 2);
		Assertions.assertEquals(a3.currentEndTime, 20);
	}
}
