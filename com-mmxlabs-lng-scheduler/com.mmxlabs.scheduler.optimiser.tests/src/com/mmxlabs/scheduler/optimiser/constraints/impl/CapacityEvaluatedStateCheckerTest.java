/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.constraints.impl;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Collections;

import org.eclipse.jdt.annotation.NonNull;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.google.common.collect.ImmutableList;
import com.mmxlabs.scheduler.optimiser.components.impl.PortSlot;
import com.mmxlabs.scheduler.optimiser.evaluation.VoyagePlanRecord;
import com.mmxlabs.scheduler.optimiser.fitness.ProfitAndLossSequences;
import com.mmxlabs.scheduler.optimiser.fitness.VolumeAllocatedSequence;
import com.mmxlabs.scheduler.optimiser.voyage.IPortTimesRecord;
import com.mmxlabs.scheduler.optimiser.voyage.impl.CapacityViolationType;

@SuppressWarnings("null")
public class CapacityEvaluatedStateCheckerTest {

	private CapacityEvaluatedStateChecker capacityEvaluatedStateChecker;
	private PortSlot port;
	private ArrayList<@NonNull CapacityViolationType> violations;
	private VoyagePlanRecord first;
	private VolumeAllocatedSequence volumeAllocatedSequence;
	private ArrayList<VolumeAllocatedSequence> list;
	private ProfitAndLossSequences profitAndLossSequences;

	@BeforeEach
	public void prepChecker() {
		capacityEvaluatedStateChecker = new CapacityEvaluatedStateChecker("Test");

		// Check initial state
		Assertions.assertFalse(capacityEvaluatedStateChecker.isInitialised());
		Assertions.assertEquals(0, capacityEvaluatedStateChecker.getInitialSoftViolations());
		Assertions.assertNull(capacityEvaluatedStateChecker.getInitialViolatedSlots());

		port = mock(PortSlot.class);

		violations = new ArrayList<>();
		violations.add(CapacityViolationType.FORCED_COOLDOWN);

		first = mock(VoyagePlanRecord.class);
		volumeAllocatedSequence = mock(VolumeAllocatedSequence.class);

		when(volumeAllocatedSequence.getVoyagePlanRecords()).thenReturn(Collections.singletonList(first));
		when(first.getCapacityViolations(port)).thenReturn(violations);
		IPortTimesRecord ptr = mock(IPortTimesRecord.class);
		
		when(ptr.getSlots()).thenReturn(ImmutableList.of(port));
		when(first.getPortTimesRecord()).thenReturn(ptr);
		when(first.getCapacityViolations(port)).thenReturn(violations);

		list = new ArrayList<>();
		list.add(volumeAllocatedSequence);

		profitAndLossSequences = new ProfitAndLossSequences(list);

		Assertions.assertTrue(capacityEvaluatedStateChecker.checkConstraints(profitAndLossSequences));
	}

	@Test
	public void validStep() {

		// Initial Block
		Assertions.assertTrue(capacityEvaluatedStateChecker.isInitialised());
		Assertions.assertEquals(1, capacityEvaluatedStateChecker.getInitialSoftViolations());
		Assertions.assertEquals(0, capacityEvaluatedStateChecker.getInitialViolatedSlots().size());

		// Run Again, Valid.
		Assertions.assertTrue(capacityEvaluatedStateChecker.checkConstraints(profitAndLossSequences));
		Assertions.assertTrue(capacityEvaluatedStateChecker.isInitialised());
		Assertions.assertEquals(1, capacityEvaluatedStateChecker.getInitialSoftViolations());
		Assertions.assertEquals(1, capacityEvaluatedStateChecker.getCurrentSoftViolations());
		Assertions.assertEquals(0, capacityEvaluatedStateChecker.getInitialViolatedSlots().size());
	}

	@Test
	public void increasedViolations() {

		violations.add(CapacityViolationType.FORCED_COOLDOWN);

		// Run Again, Increased Violation Count - Fail
		Assertions.assertFalse(capacityEvaluatedStateChecker.checkConstraints(profitAndLossSequences));
		Assertions.assertTrue(capacityEvaluatedStateChecker.isInitialised());
		Assertions.assertEquals(1, capacityEvaluatedStateChecker.getInitialSoftViolations());
		Assertions.assertEquals(2, capacityEvaluatedStateChecker.getCurrentSoftViolations());
		Assertions.assertEquals(0, capacityEvaluatedStateChecker.getInitialViolatedSlots().size());
	}

	@Test
	public void invalidViolation() {

		violations = new ArrayList<>();
		violations.add(CapacityViolationType.MAX_LOAD);
		when(first.getCapacityViolations(port)).thenReturn(violations);

		// Run Again, Flagged Capacity Violation
		Assertions.assertFalse(capacityEvaluatedStateChecker.checkConstraints(profitAndLossSequences));
		Assertions.assertTrue(capacityEvaluatedStateChecker.isInitialised());
		Assertions.assertEquals(1, capacityEvaluatedStateChecker.getInitialSoftViolations());
		Assertions.assertEquals(0, capacityEvaluatedStateChecker.getCurrentSoftViolations());
		Assertions.assertEquals(0, capacityEvaluatedStateChecker.getInitialViolatedSlots().size());
	}

}
