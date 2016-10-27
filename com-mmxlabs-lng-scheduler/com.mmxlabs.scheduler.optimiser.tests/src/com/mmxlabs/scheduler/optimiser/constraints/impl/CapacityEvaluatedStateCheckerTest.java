/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.constraints.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jdt.annotation.NonNull;
import org.junit.Before;
import org.junit.Test;

import com.mmxlabs.optimiser.core.ISequences;
import com.mmxlabs.optimiser.core.evaluation.IEvaluationState;
import com.mmxlabs.scheduler.optimiser.components.IPortSlot;
import com.mmxlabs.scheduler.optimiser.components.impl.PortSlot;
import com.mmxlabs.scheduler.optimiser.evaluation.SchedulerEvaluationProcess;
import com.mmxlabs.scheduler.optimiser.fitness.VolumeAllocatedSequence;
import com.mmxlabs.scheduler.optimiser.fitness.VolumeAllocatedSequences;
import com.mmxlabs.scheduler.optimiser.voyage.impl.CapacityViolationType;


public class CapacityEvaluatedStateCheckerTest {
	
	private CapacityEvaluatedStateChecker CESC;
	ISequences sequences;
	IEvaluationState state;
	PortSlot port;
	ArrayList<@NonNull CapacityViolationType> violations;
	VolumeAllocatedSequence first;
	List<@NonNull IPortSlot> sequenceSlots;
	ArrayList<VolumeAllocatedSequence> list;
	VolumeAllocatedSequences VAS;
	
	
	@Before
	public void prepChecker(){
		CESC = new CapacityEvaluatedStateChecker("Test");
		
		//Check initial state
		assertFalse(CESC.isInitialised());
		assertEquals(0,CESC.getInitialViolations());
		assertNull(CESC.getViolatedSlots());
		
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
		when(first.getCapacityViolationCount(port)).thenReturn((long) violations.size());

		VAS = new VolumeAllocatedSequences(list);
	
		state = mock(IEvaluationState.class);
		when(state.getData(SchedulerEvaluationProcess.VOLUME_ALLOCATED_SEQUENCES, VolumeAllocatedSequences.class)).thenReturn(VAS);
		assertTrue(CESC.checkConstraints(sequences, sequences, state));
	}
	
	@Test
	public void validStep(){
		
		//Initial Block
		assertTrue(CESC.isInitialised());
		assertEquals(1,CESC.getInitialViolations());
		assertEquals(0,CESC.getViolatedSlots().size());
		
		//Run Again, Valid.
		assertTrue(CESC.checkConstraints(sequences, sequences, state));
		assertTrue(CESC.isInitialised());
		assertEquals(1,CESC.getInitialViolations());
		assertEquals(1,CESC.getTotalViolations());
		assertEquals(0,CESC.getViolatedSlots().size());	
	}
	
	@Test
	public void increasedViolations(){
		
		violations.add(CapacityViolationType.FORCED_COOLDOWN);
		when(first.getCapacityViolationCount(port)).thenReturn((long) violations.size());
		
		//Run Again, Increased Violation Count - Fail
		assertFalse(CESC.checkConstraints(sequences, sequences, state));
		assertTrue(CESC.isInitialised());
		assertEquals(1,CESC.getInitialViolations());
		assertEquals(2,CESC.getTotalViolations());
		assertEquals(0,CESC.getViolatedSlots().size());	
	}
	
	@Test
	public void invalidViolation(){
		
		violations = new ArrayList<@NonNull CapacityViolationType>();
		violations.add(CapacityViolationType.MAX_LOAD);
		when(first.getCapacityViolationCount(port)).thenReturn((long) violations.size());
		when(first.getCapacityViolations(port)).thenReturn(violations);
		when(first.getCapacityViolationCount(port)).thenReturn((long) violations.size());
		
		
		//Run Again, Flagged Capacity Violation
		assertFalse(CESC.checkConstraints(sequences, sequences, state));
		assertTrue(CESC.isInitialised());
		assertEquals(1,CESC.getInitialViolations());
		assertEquals(1,CESC.getTotalViolations());
		assertEquals(0,CESC.getViolatedSlots().size());
		
	}
	
	


}
