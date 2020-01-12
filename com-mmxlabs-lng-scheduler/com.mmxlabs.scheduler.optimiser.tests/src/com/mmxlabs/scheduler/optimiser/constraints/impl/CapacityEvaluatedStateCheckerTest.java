/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2020
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.constraints.impl;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jdt.annotation.NonNull;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.mmxlabs.optimiser.core.ISequences;
import com.mmxlabs.optimiser.core.evaluation.IEvaluationState;
import com.mmxlabs.scheduler.optimiser.components.IPortSlot;
import com.mmxlabs.scheduler.optimiser.components.impl.PortSlot;
import com.mmxlabs.scheduler.optimiser.evaluation.SchedulerEvaluationProcess;
import com.mmxlabs.scheduler.optimiser.fitness.VolumeAllocatedSequence;
import com.mmxlabs.scheduler.optimiser.fitness.VolumeAllocatedSequences;
import com.mmxlabs.scheduler.optimiser.voyage.impl.CapacityViolationType;

@SuppressWarnings("null")
public class CapacityEvaluatedStateCheckerTest {

	private CapacityEvaluatedStateChecker CESC;
	private ISequences sequences;
	private IEvaluationState state;
	private PortSlot port;
	private ArrayList<@NonNull CapacityViolationType> violations;
	private VolumeAllocatedSequence first;
	private List<@NonNull IPortSlot> sequenceSlots;
	private ArrayList<VolumeAllocatedSequence> list;
	private VolumeAllocatedSequences VAS;

	@BeforeEach
	public void prepChecker() {
		CESC = new CapacityEvaluatedStateChecker("Test");

		// Check initial state
		Assertions.assertFalse(CESC.isInitialised());
		Assertions.assertEquals(0, CESC.getInitialSoftViolations());
		Assertions.assertNull(CESC.getInitialViolatedSlots());

		sequences = mock(ISequences.class);
		port = mock(PortSlot.class);

		violations = new ArrayList<@NonNull CapacityViolationType>();
		violations.add(CapacityViolationType.FORCED_COOLDOWN);

		first = mock(VolumeAllocatedSequence.class);
		sequenceSlots = new ArrayList<IPortSlot>();
		sequenceSlots.add(port);

		when(first.getCapacityViolations(port)).thenReturn(violations);

		list = new ArrayList<VolumeAllocatedSequence>();
		list.add(first);
		when(first.getSequenceSlots()).thenReturn(sequenceSlots);
		// when(first.getCapacityViolationCount(port)).thenReturn((long) violations.size());

		VAS = new VolumeAllocatedSequences(list);

		state = mock(IEvaluationState.class);
		when(state.getData(SchedulerEvaluationProcess.VOLUME_ALLOCATED_SEQUENCES, VolumeAllocatedSequences.class)).thenReturn(VAS);
		Assertions.assertTrue(CESC.checkConstraints(sequences, sequences, state));
	}

	@Test
	public void validStep() {

		// Initial Block
		Assertions.assertTrue(CESC.isInitialised());
		Assertions.assertEquals(1, CESC.getInitialSoftViolations());
		Assertions.assertEquals(0, CESC.getInitialViolatedSlots().size());

		// Run Again, Valid.
		Assertions.assertTrue(CESC.checkConstraints(sequences, sequences, state));
		Assertions.assertTrue(CESC.isInitialised());
		Assertions.assertEquals(1, CESC.getInitialSoftViolations());
		Assertions.assertEquals(1, CESC.getCurrentSoftViolations());
		Assertions.assertEquals(0, CESC.getInitialViolatedSlots().size());
	}

	@Test
	public void increasedViolations() {

		violations.add(CapacityViolationType.FORCED_COOLDOWN);
		// when(first.getCapacityViolationCount(port)).thenReturn((long) violations.size());

		// Run Again, Increased Violation Count - Fail
		Assertions.assertFalse(CESC.checkConstraints(sequences, sequences, state));
		Assertions.assertTrue(CESC.isInitialised());
		Assertions.assertEquals(1, CESC.getInitialSoftViolations());
		Assertions.assertEquals(2, CESC.getCurrentSoftViolations());
		Assertions.assertEquals(0, CESC.getInitialViolatedSlots().size());
	}

	@Test
	public void invalidViolation() {

		violations = new ArrayList<@NonNull CapacityViolationType>();
		violations.add(CapacityViolationType.MAX_LOAD);
		// when(first.getCapacityViolationCount(port)).thenReturn((long) violations.size());
		when(first.getCapacityViolations(port)).thenReturn(violations);
		// when(first.getCapacityViolationCount(port)).thenReturn((long) violations.size());

		// Run Again, Flagged Capacity Violation
		Assertions.assertFalse(CESC.checkConstraints(sequences, sequences, state));
		Assertions.assertTrue(CESC.isInitialised());
		Assertions.assertEquals(1, CESC.getInitialSoftViolations());
		Assertions.assertEquals(0, CESC.getCurrentSoftViolations());
		Assertions.assertEquals(0, CESC.getInitialViolatedSlots().size());
	}

}
