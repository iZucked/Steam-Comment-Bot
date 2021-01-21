/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2021
 * All rights reserved.
 */
package com.mmxlabs.optimiser.lso.multiobjective.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Provides;
import com.google.inject.name.Named;
import com.google.inject.name.Names;
import com.mmxlabs.optimiser.common.components.ILookupManager;
import com.mmxlabs.optimiser.core.ISequences;
import com.mmxlabs.optimiser.core.fitness.IFitnessHelper;
import com.mmxlabs.optimiser.core.scenario.IOptimisationData;
import com.mmxlabs.optimiser.core.scenario.IPhaseOptimisationData;
import com.mmxlabs.optimiser.lso.SimilarityFitnessMode;
import com.mmxlabs.optimiser.lso.modules.LocalSearchOptimiserModule;
import com.mmxlabs.optimiser.lso.multiobjective.impl.SimpleMultiObjectiveOptimiser.eQuartile;
import com.mmxlabs.optimiser.lso.multiobjective.modules.MultiObjectiveOptimiserModule;

public class SimpleMultiObjectiveOptimiserTest {

	private List<NonDominatedSolution> createLargeTestData() {
		List<NonDominatedSolution> sortedArchive = new ArrayList<>();

		sortedArchive.add(new NonDominatedSolution(Mockito.mock(ISequences.class), new long[] { 2965112638L, 0 }, null));
		sortedArchive.add(new NonDominatedSolution(Mockito.mock(ISequences.class), new long[] { 2964567642L, 1 }, null));
		sortedArchive.add(new NonDominatedSolution(Mockito.mock(ISequences.class), new long[] { 2284385981L, 2 }, null));
		sortedArchive.add(new NonDominatedSolution(Mockito.mock(ISequences.class), new long[] { 2116173656L, 3 }, null));
		sortedArchive.add(new NonDominatedSolution(Mockito.mock(ISequences.class), new long[] { 2115989633L, 4 }, null));
		sortedArchive.add(new NonDominatedSolution(Mockito.mock(ISequences.class), new long[] { 2054742241L, 5 }, null));
		sortedArchive.add(new NonDominatedSolution(Mockito.mock(ISequences.class), new long[] { 2048426345L, 6 }, null));
		sortedArchive.add(new NonDominatedSolution(Mockito.mock(ISequences.class), new long[] { 2027494294L, 7 }, null));
		sortedArchive.add(new NonDominatedSolution(Mockito.mock(ISequences.class), new long[] { 1777072501L, 8 }, null));
		sortedArchive.add(new NonDominatedSolution(Mockito.mock(ISequences.class), new long[] { 1715825109L, 9 }, null));
		sortedArchive.add(new NonDominatedSolution(Mockito.mock(ISequences.class), new long[] { 1715398772L, 10 }, null));
		sortedArchive.add(new NonDominatedSolution(Mockito.mock(ISequences.class), new long[] { 1710578523L, 11 }, null));
		sortedArchive.add(new NonDominatedSolution(Mockito.mock(ISequences.class), new long[] { 1688088385L, 12 }, null));
		sortedArchive.add(new NonDominatedSolution(Mockito.mock(ISequences.class), new long[] { 1683330576L, 13 }, null));
		sortedArchive.add(new NonDominatedSolution(Mockito.mock(ISequences.class), new long[] { 1632824424L, 14 }, null));
		sortedArchive.add(new NonDominatedSolution(Mockito.mock(ISequences.class), new long[] { 1619467467L, 15 }, null));
		sortedArchive.add(new NonDominatedSolution(Mockito.mock(ISequences.class), new long[] { 1614378911L, 16 }, null));
		sortedArchive.add(new NonDominatedSolution(Mockito.mock(ISequences.class), new long[] { 1613890134L, 17 }, null));
		sortedArchive.add(new NonDominatedSolution(Mockito.mock(ISequences.class), new long[] { 1609809961L, 18 }, null));
		sortedArchive.add(new NonDominatedSolution(Mockito.mock(ISequences.class), new long[] { 1605162313L, 19 }, null));
		sortedArchive.add(new NonDominatedSolution(Mockito.mock(ISequences.class), new long[] { 1604348266L, 20 }, null));
		sortedArchive.add(new NonDominatedSolution(Mockito.mock(ISequences.class), new long[] { 1599193813L, 21 }, null));
		sortedArchive.add(new NonDominatedSolution(Mockito.mock(ISequences.class), new long[] { 1597686778L, 22 }, null));
		sortedArchive.add(new NonDominatedSolution(Mockito.mock(ISequences.class), new long[] { 1597552841L, 23 }, null));
		sortedArchive.add(new NonDominatedSolution(Mockito.mock(ISequences.class), new long[] { 1595864620L, 24 }, null));
		sortedArchive.add(new NonDominatedSolution(Mockito.mock(ISequences.class), new long[] { 1593009065L, 26 }, null));
		sortedArchive.add(new NonDominatedSolution(Mockito.mock(ISequences.class), new long[] { 1592005834L, 29 }, null));
		sortedArchive.add(new NonDominatedSolution(Mockito.mock(ISequences.class), new long[] { 1591187800L, 30 }, null));
		sortedArchive.add(new NonDominatedSolution(Mockito.mock(ISequences.class), new long[] { 1582458527L, 31 }, null));
		sortedArchive.add(new NonDominatedSolution(Mockito.mock(ISequences.class), new long[] { 1581744962L, 33 }, null));
		sortedArchive.add(new NonDominatedSolution(Mockito.mock(ISequences.class), new long[] { 1580255531L, 34 }, null));
		sortedArchive.add(new NonDominatedSolution(Mockito.mock(ISequences.class), new long[] { 1579829194L, 35 }, null));
		sortedArchive.add(new NonDominatedSolution(Mockito.mock(ISequences.class), new long[] { 1578924703L, 36 }, null));
		sortedArchive.add(new NonDominatedSolution(Mockito.mock(ISequences.class), new long[] { 1577123136L, 37 }, null));
		sortedArchive.add(new NonDominatedSolution(Mockito.mock(ISequences.class), new long[] { 1576382160L, 39 }, null));
		sortedArchive.add(new NonDominatedSolution(Mockito.mock(ISequences.class), new long[] { 1575668595L, 41 }, null));
		sortedArchive.add(new NonDominatedSolution(Mockito.mock(ISequences.class), new long[] { 1573868675L, 42 }, null));
		sortedArchive.add(new NonDominatedSolution(Mockito.mock(ISequences.class), new long[] { 1573273640L, 43 }, null));
		sortedArchive.add(new NonDominatedSolution(Mockito.mock(ISequences.class), new long[] { 1573144654L, 44 }, null));
		sortedArchive.add(new NonDominatedSolution(Mockito.mock(ISequences.class), new long[] { 1572560075L, 45 }, null));
		sortedArchive.add(new NonDominatedSolution(Mockito.mock(ISequences.class), new long[] { 1572431089L, 46 }, null));
		sortedArchive.add(new NonDominatedSolution(Mockito.mock(ISequences.class), new long[] { 1571586348L, 47 }, null));
		sortedArchive.add(new NonDominatedSolution(Mockito.mock(ISequences.class), new long[] { 1571577296L, 50 }, null));
		sortedArchive.add(new NonDominatedSolution(Mockito.mock(ISequences.class), new long[] { 1571088519L, 51 }, null));
		sortedArchive.add(new NonDominatedSolution(Mockito.mock(ISequences.class), new long[] { 1570687514L, 53 }, null));
		sortedArchive.add(new NonDominatedSolution(Mockito.mock(ISequences.class), new long[] { 1570296649L, 54 }, null));
		sortedArchive.add(new NonDominatedSolution(Mockito.mock(ISequences.class), new long[] { 1569876078L, 55 }, null));
		sortedArchive.add(new NonDominatedSolution(Mockito.mock(ISequences.class), new long[] { 1569583084L, 56 }, null));
		sortedArchive.add(new NonDominatedSolution(Mockito.mock(ISequences.class), new long[] { 1569123610L, 57 }, null));
		sortedArchive.add(new NonDominatedSolution(Mockito.mock(ISequences.class), new long[] { 1568721942L, 58 }, null));
		sortedArchive.add(new NonDominatedSolution(Mockito.mock(ISequences.class), new long[] { 1568410045L, 59 }, null));
		sortedArchive.add(new NonDominatedSolution(Mockito.mock(ISequences.class), new long[] { 1568008377L, 60 }, null));
		sortedArchive.add(new NonDominatedSolution(Mockito.mock(ISequences.class), new long[] { 1567369140L, 61 }, null));
		sortedArchive.add(new NonDominatedSolution(Mockito.mock(ISequences.class), new long[] { 1566655575L, 63 }, null));
		sortedArchive.add(new NonDominatedSolution(Mockito.mock(ISequences.class), new long[] { 1566547123L, 64 }, null));
		sortedArchive.add(new NonDominatedSolution(Mockito.mock(ISequences.class), new long[] { 1565522178L, 65 }, null));
		sortedArchive.add(new NonDominatedSolution(Mockito.mock(ISequences.class), new long[] { 1565115819L, 66 }, null));
		sortedArchive.add(new NonDominatedSolution(Mockito.mock(ISequences.class), new long[] { 1564808613L, 67 }, null));
		sortedArchive.add(new NonDominatedSolution(Mockito.mock(ISequences.class), new long[] { 1564402254L, 68 }, null));
		sortedArchive.add(new NonDominatedSolution(Mockito.mock(ISequences.class), new long[] { 1562422678L, 69 }, null));
		sortedArchive.add(new NonDominatedSolution(Mockito.mock(ISequences.class), new long[] { 1560316581L, 71 }, null));
		sortedArchive.add(new NonDominatedSolution(Mockito.mock(ISequences.class), new long[] { 1559141753L, 73 }, null));
		sortedArchive.add(new NonDominatedSolution(Mockito.mock(ISequences.class), new long[] { 1558689957L, 74 }, null));
		sortedArchive.add(new NonDominatedSolution(Mockito.mock(ISequences.class), new long[] { 1558401841L, 75 }, null));
		sortedArchive.add(new NonDominatedSolution(Mockito.mock(ISequences.class), new long[] { 1555284209L, 76 }, null));
		sortedArchive.add(new NonDominatedSolution(Mockito.mock(ISequences.class), new long[] { 1554570644L, 78 }, null));
		sortedArchive.add(new NonDominatedSolution(Mockito.mock(ISequences.class), new long[] { 1554427140L, 79 }, null));
		sortedArchive.add(new NonDominatedSolution(Mockito.mock(ISequences.class), new long[] { 1552325240L, 80 }, null));
		sortedArchive.add(new NonDominatedSolution(Mockito.mock(ISequences.class), new long[] { 1551611675L, 82 }, null));
		sortedArchive.add(new NonDominatedSolution(Mockito.mock(ISequences.class), new long[] { 1551368568L, 84 }, null));
		sortedArchive.add(new NonDominatedSolution(Mockito.mock(ISequences.class), new long[] { 1550799527L, 86 }, null));
		sortedArchive.add(new NonDominatedSolution(Mockito.mock(ISequences.class), new long[] { 1548808291L, 87 }, null));
		sortedArchive.add(new NonDominatedSolution(Mockito.mock(ISequences.class), new long[] { 1548735687L, 89 }, null));
		sortedArchive.add(new NonDominatedSolution(Mockito.mock(ISequences.class), new long[] { 1547744087L, 90 }, null));
		sortedArchive.add(new NonDominatedSolution(Mockito.mock(ISequences.class), new long[] { 1547577899L, 91 }, null));
		sortedArchive.add(new NonDominatedSolution(Mockito.mock(ISequences.class), new long[] { 1547030522L, 92 }, null));
		sortedArchive.add(new NonDominatedSolution(Mockito.mock(ISequences.class), new long[] { 1546591707L, 94 }, null));
		sortedArchive.add(new NonDominatedSolution(Mockito.mock(ISequences.class), new long[] { 1546498653L, 95 }, null));
		sortedArchive.add(new NonDominatedSolution(Mockito.mock(ISequences.class), new long[] { 1546494313L, 98 }, null));
		sortedArchive.add(new NonDominatedSolution(Mockito.mock(ISequences.class), new long[] { 1546453522L, 100 }, null));
		sortedArchive.add(new NonDominatedSolution(Mockito.mock(ISequences.class), new long[] { 1546432411L, 102 }, null));
		return sortedArchive;
	}

	private List<NonDominatedSolution> createWeirdDistributionTestData() {
		List<NonDominatedSolution> sortedArchive = new ArrayList<>();
		sortedArchive.add(new NonDominatedSolution(Mockito.mock(ISequences.class), new long[] { -2965112638L, 0 }, null));
		sortedArchive.add(new NonDominatedSolution(Mockito.mock(ISequences.class), new long[] { -2964567642L, 1 }, null));
		sortedArchive.add(new NonDominatedSolution(Mockito.mock(ISequences.class), new long[] { -2284385981L, 2 }, null));
		sortedArchive.add(new NonDominatedSolution(Mockito.mock(ISequences.class), new long[] { -2116173656L, 3 }, null));
		sortedArchive.add(new NonDominatedSolution(Mockito.mock(ISequences.class), new long[] { -2115989633L, 4 }, null));
		sortedArchive.add(new NonDominatedSolution(Mockito.mock(ISequences.class), new long[] { -2054742241L, 5 }, null));
		sortedArchive.add(new NonDominatedSolution(Mockito.mock(ISequences.class), new long[] { -2048426345L, 6 }, null));
		sortedArchive.add(new NonDominatedSolution(Mockito.mock(ISequences.class), new long[] { -2027494294L, 7 }, null));
		sortedArchive.add(new NonDominatedSolution(Mockito.mock(ISequences.class), new long[] { -1777072501L, 8 }, null));
		sortedArchive.add(new NonDominatedSolution(Mockito.mock(ISequences.class), new long[] { -1715825109L, 9 }, null));
		sortedArchive.add(new NonDominatedSolution(Mockito.mock(ISequences.class), new long[] { -1715398772L, 10 }, null));
		sortedArchive.add(new NonDominatedSolution(Mockito.mock(ISequences.class), new long[] { -1710578523L, 11 }, null));
		sortedArchive.add(new NonDominatedSolution(Mockito.mock(ISequences.class), new long[] { -1688088385L, 12 }, null));
		sortedArchive.add(new NonDominatedSolution(Mockito.mock(ISequences.class), new long[] { -1683330576L, 13 }, null));
		sortedArchive.add(new NonDominatedSolution(Mockito.mock(ISequences.class), new long[] { -1632824424L, 14 }, null));
		sortedArchive.add(new NonDominatedSolution(Mockito.mock(ISequences.class), new long[] { -1619467467L, 15 }, null));
		sortedArchive.add(new NonDominatedSolution(Mockito.mock(ISequences.class), new long[] { -1614378911L, 16 }, null));
		sortedArchive.add(new NonDominatedSolution(Mockito.mock(ISequences.class), new long[] { -1613890134L, 17 }, null));
		sortedArchive.add(new NonDominatedSolution(Mockito.mock(ISequences.class), new long[] { -1609809961L, 18 }, null));
		sortedArchive.add(new NonDominatedSolution(Mockito.mock(ISequences.class), new long[] { -1605162313L, 19 }, null));
		sortedArchive.add(new NonDominatedSolution(Mockito.mock(ISequences.class), new long[] { -1604348266L, 20 }, null));
		sortedArchive.add(new NonDominatedSolution(Mockito.mock(ISequences.class), new long[] { -1599193813L, 21 }, null));
		sortedArchive.add(new NonDominatedSolution(Mockito.mock(ISequences.class), new long[] { -1597686778L, 22 }, null));
		sortedArchive.add(new NonDominatedSolution(Mockito.mock(ISequences.class), new long[] { -1597552841L, 23 }, null));
		sortedArchive.add(new NonDominatedSolution(Mockito.mock(ISequences.class), new long[] { -1595864620L, 24 }, null));
		sortedArchive.add(new NonDominatedSolution(Mockito.mock(ISequences.class), new long[] { -1593009065L, 26 }, null));
		sortedArchive.add(new NonDominatedSolution(Mockito.mock(ISequences.class), new long[] { -1592005834L, 29 }, null));
		sortedArchive.add(new NonDominatedSolution(Mockito.mock(ISequences.class), new long[] { -1591187800L, 30 }, null));
		sortedArchive.add(new NonDominatedSolution(Mockito.mock(ISequences.class), new long[] { -1582458527L, 31 }, null));
		sortedArchive.add(new NonDominatedSolution(Mockito.mock(ISequences.class), new long[] { -1581744962L, 33 }, null));
		sortedArchive.add(new NonDominatedSolution(Mockito.mock(ISequences.class), new long[] { -1580255531L, 34 }, null));
		sortedArchive.add(new NonDominatedSolution(Mockito.mock(ISequences.class), new long[] { -1579829194L, 35 }, null));
		sortedArchive.add(new NonDominatedSolution(Mockito.mock(ISequences.class), new long[] { -1578924703L, 36 }, null));
		sortedArchive.add(new NonDominatedSolution(Mockito.mock(ISequences.class), new long[] { -1577123136L, 37 }, null));
		sortedArchive.add(new NonDominatedSolution(Mockito.mock(ISequences.class), new long[] { -1576382160L, 39 }, null));
		sortedArchive.add(new NonDominatedSolution(Mockito.mock(ISequences.class), new long[] { -1575668595L, 41 }, null));
		sortedArchive.add(new NonDominatedSolution(Mockito.mock(ISequences.class), new long[] { -1573868675L, 42 }, null));
		sortedArchive.add(new NonDominatedSolution(Mockito.mock(ISequences.class), new long[] { -1573273640L, 43 }, null));
		sortedArchive.add(new NonDominatedSolution(Mockito.mock(ISequences.class), new long[] { -1573144654L, 44 }, null));
		sortedArchive.add(new NonDominatedSolution(Mockito.mock(ISequences.class), new long[] { -1572560075L, 45 }, null));
		sortedArchive.add(new NonDominatedSolution(Mockito.mock(ISequences.class), new long[] { -1572431089L, 46 }, null));
		sortedArchive.add(new NonDominatedSolution(Mockito.mock(ISequences.class), new long[] { -1571586348L, 47 }, null));
		sortedArchive.add(new NonDominatedSolution(Mockito.mock(ISequences.class), new long[] { -1571577296L, 50 }, null));
		sortedArchive.add(new NonDominatedSolution(Mockito.mock(ISequences.class), new long[] { -1571088519L, 51 }, null));
		sortedArchive.add(new NonDominatedSolution(Mockito.mock(ISequences.class), new long[] { -1570687514L, 53 }, null));
		sortedArchive.add(new NonDominatedSolution(Mockito.mock(ISequences.class), new long[] { -1570296649L, 54 }, null));
		sortedArchive.add(new NonDominatedSolution(Mockito.mock(ISequences.class), new long[] { -1569876078L, 55 }, null));
		sortedArchive.add(new NonDominatedSolution(Mockito.mock(ISequences.class), new long[] { -1569583084L, 56 }, null));
		sortedArchive.add(new NonDominatedSolution(Mockito.mock(ISequences.class), new long[] { -1569123610L, 57 }, null));
		sortedArchive.add(new NonDominatedSolution(Mockito.mock(ISequences.class), new long[] { -1568721942L, 58 }, null));
		sortedArchive.add(new NonDominatedSolution(Mockito.mock(ISequences.class), new long[] { -1568410045L, 59 }, null));
		sortedArchive.add(new NonDominatedSolution(Mockito.mock(ISequences.class), new long[] { -1568008377L, 60 }, null));
		sortedArchive.add(new NonDominatedSolution(Mockito.mock(ISequences.class), new long[] { -1567369140L, 61 }, null));
		sortedArchive.add(new NonDominatedSolution(Mockito.mock(ISequences.class), new long[] { -1566655575L, 63 }, null));
		sortedArchive.add(new NonDominatedSolution(Mockito.mock(ISequences.class), new long[] { -1566547123L, 64 }, null));
		sortedArchive.add(new NonDominatedSolution(Mockito.mock(ISequences.class), new long[] { -1565522178L, 65 }, null));
		sortedArchive.add(new NonDominatedSolution(Mockito.mock(ISequences.class), new long[] { -1565115819L, 66 }, null));
		sortedArchive.add(new NonDominatedSolution(Mockito.mock(ISequences.class), new long[] { -1564808613L, 67 }, null));
		sortedArchive.add(new NonDominatedSolution(Mockito.mock(ISequences.class), new long[] { -1564402254L, 68 }, null));
		sortedArchive.add(new NonDominatedSolution(Mockito.mock(ISequences.class), new long[] { -1562422678L, 69 }, null));
		sortedArchive.add(new NonDominatedSolution(Mockito.mock(ISequences.class), new long[] { -1560316581L, 71 }, null));
		sortedArchive.add(new NonDominatedSolution(Mockito.mock(ISequences.class), new long[] { -1559141753L, 73 }, null));
		sortedArchive.add(new NonDominatedSolution(Mockito.mock(ISequences.class), new long[] { -1558689957L, 74 }, null));
		sortedArchive.add(new NonDominatedSolution(Mockito.mock(ISequences.class), new long[] { -1558401841L, 75 }, null));
		sortedArchive.add(new NonDominatedSolution(Mockito.mock(ISequences.class), new long[] { -1555284209L, 76 }, null));
		sortedArchive.add(new NonDominatedSolution(Mockito.mock(ISequences.class), new long[] { -1554570644L, 78 }, null));
		sortedArchive.add(new NonDominatedSolution(Mockito.mock(ISequences.class), new long[] { -1554427140L, 79 }, null));
		sortedArchive.add(new NonDominatedSolution(Mockito.mock(ISequences.class), new long[] { -1552325240L, 80 }, null));
		sortedArchive.add(new NonDominatedSolution(Mockito.mock(ISequences.class), new long[] { -1551611675L, 82 }, null));
		sortedArchive.add(new NonDominatedSolution(Mockito.mock(ISequences.class), new long[] { -1551368568L, 84 }, null));
		sortedArchive.add(new NonDominatedSolution(Mockito.mock(ISequences.class), new long[] { -1550799527L, 86 }, null));
		sortedArchive.add(new NonDominatedSolution(Mockito.mock(ISequences.class), new long[] { -1548808291L, 87 }, null));
		sortedArchive.add(new NonDominatedSolution(Mockito.mock(ISequences.class), new long[] { -1548735687L, 89 }, null));
		sortedArchive.add(new NonDominatedSolution(Mockito.mock(ISequences.class), new long[] { -1547744087L, 90 }, null));
		sortedArchive.add(new NonDominatedSolution(Mockito.mock(ISequences.class), new long[] { -1547577899L, 91 }, null));
		sortedArchive.add(new NonDominatedSolution(Mockito.mock(ISequences.class), new long[] { -1547030522L, 92 }, null));
		sortedArchive.add(new NonDominatedSolution(Mockito.mock(ISequences.class), new long[] { -1546591707L, 94 }, null));
		sortedArchive.add(new NonDominatedSolution(Mockito.mock(ISequences.class), new long[] { -1546498653L, 95 }, null));
		sortedArchive.add(new NonDominatedSolution(Mockito.mock(ISequences.class), new long[] { -1546494313L, 98 }, null));
		sortedArchive.add(new NonDominatedSolution(Mockito.mock(ISequences.class), new long[] { -1546453522L, 100 }, null));
		sortedArchive.add(new NonDominatedSolution(Mockito.mock(ISequences.class), new long[] { -1546432411L, 102 }, null));

		return sortedArchive;
	}

	@Test
	public void findQuartileSolution() {
		List<NonDominatedSolution> sortedArchive = createLargeTestData();
		SimpleMultiObjectiveOptimiser simpleMultiObjectiveOptimiser = getSimpleMultiObjectiveOptimiser(SimilarityFitnessMode.HIGH);
		List<NonDominatedSolution> createWeirdDistributionTestData = createWeirdDistributionTestData();
		NonDominatedSolution quartileSolution = simpleMultiObjectiveOptimiser.findSolutionWhichReachesQuartile(sortedArchive, 0, eQuartile.THIRD, true);
		Assertions.assertNotNull(quartileSolution);
		long[] fitnesses = quartileSolution.getFitnesses();
	}

	@Disabled
	@Test
	public void createLargeSpatiallyDividedSolutions() {
		List<NonDominatedSolution> sortedArchive = createLargeTestData();
		SimpleMultiObjectiveOptimiser simpleMultiObjectiveOptimiser = getSimpleMultiObjectiveOptimiser(SimilarityFitnessMode.HIGH);
		List<List<NonDominatedSolution>> spatiallyDividedSolutions = simpleMultiObjectiveOptimiser.getSpatiallyDividedSolutions(sortedArchive, 4, 0);
		checkSpatiallyDividedSolutions(sortedArchive, spatiallyDividedSolutions, new int[][] { { 0, 26 }, { 26, 52 }, { 52, 78 }, { 78, 102 } });
		ISequences bestRawSequences = simpleMultiObjectiveOptimiser.getBestRawSequences(sortedArchive, 4, 0);
		NonDominatedSolution collection = sortedArchive.stream().filter(a -> a.getSequences().equals(bestRawSequences)).collect(Collectors.toList()).get(0);
	}

	private void checkSpatiallyDividedSolutions(List<NonDominatedSolution> archive, List<List<NonDominatedSolution>> spatiallyDividedSolutions, int[][] breakpoints) {
		Assertions.assertTrue(spatiallyDividedSolutions.size() == 4);
		int sumOfSizes = spatiallyDividedSolutions.stream().mapToInt(i -> i.size()).sum();
		Assertions.assertTrue(sumOfSizes == archive.size() - 1);
		for (int i = 0; i < breakpoints.length; i++) {
			int[] bp = breakpoints[i];
			List<NonDominatedSolution> division = spatiallyDividedSolutions.get(i);
			for (NonDominatedSolution solution : division) {
				long[] fitnesses = solution.getFitnesses();
				Assertions.assertTrue(bp[0] < fitnesses[1] && bp[1] >= fitnesses[1], String.format("breakpoints: %s fitnesses: %s", Arrays.toString(bp), Arrays.toString(fitnesses)));
			}
		}
	}

	public SimpleMultiObjectiveOptimiser getSimpleMultiObjectiveOptimiser(final SimilarityFitnessMode similarityFitnessMode) {
		Injector injector = Guice.createInjector(new AbstractModule() {

			@Override
			protected void configure() {
				bind(IOptimisationData.class).toInstance(Mockito.mock(IOptimisationData.class));
				bind(IPhaseOptimisationData.class).toInstance(Mockito.mock(IPhaseOptimisationData.class));
				bind(IFitnessHelper.class).toInstance(Mockito.mock(IFitnessHelper.class));
				bind(ILookupManager.class).toInstance(Mockito.mock(ILookupManager.class));
				bind(Boolean.class).annotatedWith(Names.named(LocalSearchOptimiserModule.OPTIMISER_DEBUG_MODE)).toInstance(Boolean.FALSE);
				// Make sure array length is equals to number of fitness objectives.
				bind(long[].class).annotatedWith(Names.named(MultiObjectiveOptimiserModule.MULTIOBJECTIVE_OBJECTIVE_EPSILON_DOMINANCE_VALUES)).toInstance(new long[2]);
			}

			@Provides
			@Named(LocalSearchOptimiserModule.SIMILARITY_SETTING)
			private SimilarityFitnessMode getSimilarityMode() {
				return similarityFitnessMode;
			}
		});

		SimpleMultiObjectiveOptimiser smoo = new SimpleMultiObjectiveOptimiser(null, new Random(0));
		injector.injectMembers(smoo);
		return smoo;
	}

}
