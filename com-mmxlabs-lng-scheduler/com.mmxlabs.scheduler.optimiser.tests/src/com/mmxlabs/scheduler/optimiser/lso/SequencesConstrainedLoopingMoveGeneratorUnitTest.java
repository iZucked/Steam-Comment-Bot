package com.mmxlabs.scheduler.optimiser.lso;

import static org.hamcrest.Matchers.not;
import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import org.junit.Test;
import  org.mockito.Mockito;

import com.mmxlabs.common.Pair;
import com.mmxlabs.optimiser.core.IResource;
import com.mmxlabs.optimiser.core.ISequenceElement;

public class SequencesConstrainedLoopingMoveGeneratorUnitTest {
	
	
	ConstrainedMoveGenerator mockOwner = Mockito.mock(ConstrainedMoveGenerator.class);
	ISequenceElement mockSequenceA = Mockito.mock(ISequenceElement.class);
	ISequenceElement mockSequenceB = Mockito.mock(ISequenceElement.class);
	Pair<ISequenceElement, ISequenceElement> mockedPair = new Pair<>(mockSequenceA, mockSequenceB);
	
	Map<ISequenceElement, Pair<IResource, Integer>> mockReverseLookup = new HashMap<>();
	
	IResource mockResourceA = Mockito.mock(IResource.class);
	IResource mockResourceB = Mockito.mock(IResource.class);

	
	ArrayList<Pair<ISequenceElement, ISequenceElement>> mockedList = new ArrayList<>();
	
	SequencesConstrainedLoopingMoveGeneratorUnit loopingMoveGenerator = new SequencesConstrainedLoopingMoveGeneratorUnit(mockOwner);
	
	
	@Test
	public void test_SCLGU(){
		
		mockedList.add(mockedPair);
		
		Mockito.when(mockOwner.getValidBreaks()).thenReturn(mockedList);
		
		Pair<IResource,Integer> firstMockPair = new Pair<>(mockResourceA,1);
		Pair<IResource,Integer> secondMockPair = new Pair<>(mockResourceB,2);
		
		mockReverseLookup.put(mockSequenceA, firstMockPair);
		mockReverseLookup.put(mockSequenceB, secondMockPair);
	
		Mockito.when(mockOwner.getReverseLookup()).thenReturn(mockReverseLookup);
		
		Random x = new Random();
		
		Mockito.when(mockOwner.getRandom()).thenReturn(x);
		
		Pair<Pair<IResource, Integer>, Pair<IResource, Integer>> answer = loopingMoveGenerator.findEdge();
		
		Pair<IResource,Integer>firstPair = answer.getFirst();
		Pair<IResource,Integer>secondPair = answer.getSecond();
		
		// Match pairs, assert they are different
		assertEquals(firstMockPair, firstPair);
		assertNotNull(firstPair);
		assertEquals(secondMockPair, secondPair);
		assertNotNull(secondPair);
		assertNotEquals(firstPair, secondPair);
		
		int loopCount = loopingMoveGenerator.getCurrentLoops();
		// Check no looped
		assertEquals(0,loopCount);
		
		mockReverseLookup = new HashMap<>();

		Mockito.when(mockOwner.getReverseLookup()).thenReturn(mockReverseLookup);
		Mockito.when(mockOwner.getValidBreaks()).thenReturn(mockedList);
		
		loopingMoveGenerator = new SequencesConstrainedLoopingMoveGeneratorUnit(mockOwner);
		
		Pair<Pair<IResource, Integer>, Pair<IResource, Integer>> answerFullLoop = loopingMoveGenerator.findEdge();
		Pair<IResource,Integer>firstFullPair = answerFullLoop.getFirst();
		Pair<IResource,Integer>secondFullPair = answerFullLoop.getSecond();
		
		int loopCountFullLoop = loopingMoveGenerator.getCurrentLoops();
		int maxLoops = loopingMoveGenerator.getMaxLoops();
		// Check maximum loops
		assertEquals(maxLoops,loopCountFullLoop);
		assertNull(firstFullPair);
		assertNull(secondFullPair);
		
		Pair<IResource,Integer> firstMockNullPair = new Pair<>(null,1);
		Pair<IResource,Integer> secondMockNullPair = new Pair<>(null,2);
		
		mockReverseLookup = new HashMap<>();
		
		mockReverseLookup.put(mockSequenceA, firstMockNullPair);
		mockReverseLookup.put(mockSequenceB, secondMockNullPair);
	
		Mockito.when(mockOwner.getReverseLookup()).thenReturn(mockReverseLookup);
		
		Pair<Pair<IResource, Integer>, Pair<IResource, Integer>> answerNullLoop = loopingMoveGenerator.findEdge();
		
		int loopCountNullLoop = loopingMoveGenerator.getCurrentLoops();

		// Check maximum loops
		assertNull(answerNullLoop);
		assertEquals(maxLoops,loopCountNullLoop);
		
		
		
	}
	

	
	

}
