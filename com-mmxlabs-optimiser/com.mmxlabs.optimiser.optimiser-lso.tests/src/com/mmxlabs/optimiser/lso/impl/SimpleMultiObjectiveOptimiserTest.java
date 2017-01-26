package com.mmxlabs.optimiser.lso.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;
import org.mockito.Mockito;

import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Provides;
import com.google.inject.name.Named;
import com.mmxlabs.common.Pair;
import com.mmxlabs.optimiser.core.ISequences;
import com.mmxlabs.optimiser.core.fitness.IFitnessHelper;
import com.mmxlabs.optimiser.core.scenario.IOptimisationData;
import com.mmxlabs.optimiser.lso.SimilarityFitnessMode;
import com.mmxlabs.optimiser.lso.impl.SimpleMultiObjectiveOptimiser.eQuartile;
import com.mmxlabs.optimiser.lso.modules.LocalSearchOptimiserModule;


public class SimpleMultiObjectiveOptimiserTest {

	private List<Pair<ISequences,long[]>> createLargeTestData() {
		List<Pair<ISequences, long[]>> sortedArchive = new ArrayList<>();
		
		sortedArchive.add(new Pair<>(Mockito.mock(ISequences.class), new long[] {2965112638L,0}));
		sortedArchive.add(new Pair<>(Mockito.mock(ISequences.class), new long[] {2964567642L,1}));
		sortedArchive.add(new Pair<>(Mockito.mock(ISequences.class), new long[] {2284385981L,2}));
		sortedArchive.add(new Pair<>(Mockito.mock(ISequences.class), new long[] {2116173656,3}));
		sortedArchive.add(new Pair<>(Mockito.mock(ISequences.class), new long[] {2115989633,4}));
		sortedArchive.add(new Pair<>(Mockito.mock(ISequences.class), new long[] {2054742241,5}));
		sortedArchive.add(new Pair<>(Mockito.mock(ISequences.class), new long[] {2048426345,6}));
		sortedArchive.add(new Pair<>(Mockito.mock(ISequences.class), new long[] {2027494294,7}));
		sortedArchive.add(new Pair<>(Mockito.mock(ISequences.class), new long[] {1777072501,8}));
		sortedArchive.add(new Pair<>(Mockito.mock(ISequences.class), new long[] {1715825109,9}));
		sortedArchive.add(new Pair<>(Mockito.mock(ISequences.class), new long[] {1715398772,10}));
		sortedArchive.add(new Pair<>(Mockito.mock(ISequences.class), new long[] {1710578523,11}));
		sortedArchive.add(new Pair<>(Mockito.mock(ISequences.class), new long[] {1688088385,12}));
		sortedArchive.add(new Pair<>(Mockito.mock(ISequences.class), new long[] {1683330576,13}));
		sortedArchive.add(new Pair<>(Mockito.mock(ISequences.class), new long[] {1632824424,14}));
		sortedArchive.add(new Pair<>(Mockito.mock(ISequences.class), new long[] {1619467467,15}));
		sortedArchive.add(new Pair<>(Mockito.mock(ISequences.class), new long[] {1614378911,16}));
		sortedArchive.add(new Pair<>(Mockito.mock(ISequences.class), new long[] {1613890134,17}));
		sortedArchive.add(new Pair<>(Mockito.mock(ISequences.class), new long[] {1609809961,18}));
		sortedArchive.add(new Pair<>(Mockito.mock(ISequences.class), new long[] {1605162313,19}));
		sortedArchive.add(new Pair<>(Mockito.mock(ISequences.class), new long[] {1604348266,20}));
		sortedArchive.add(new Pair<>(Mockito.mock(ISequences.class), new long[] {1599193813,21}));
		sortedArchive.add(new Pair<>(Mockito.mock(ISequences.class), new long[] {1597686778,22}));
		sortedArchive.add(new Pair<>(Mockito.mock(ISequences.class), new long[] {1597552841,23}));
		sortedArchive.add(new Pair<>(Mockito.mock(ISequences.class), new long[] {1595864620,24}));
		sortedArchive.add(new Pair<>(Mockito.mock(ISequences.class), new long[] {1593009065,26}));
		sortedArchive.add(new Pair<>(Mockito.mock(ISequences.class), new long[] {1592005834,29}));
		sortedArchive.add(new Pair<>(Mockito.mock(ISequences.class), new long[] {1591187800,30}));
		sortedArchive.add(new Pair<>(Mockito.mock(ISequences.class), new long[] {1582458527,31}));
		sortedArchive.add(new Pair<>(Mockito.mock(ISequences.class), new long[] {1581744962,33}));
		sortedArchive.add(new Pair<>(Mockito.mock(ISequences.class), new long[] {1580255531,34}));
		sortedArchive.add(new Pair<>(Mockito.mock(ISequences.class), new long[] {1579829194,35}));
		sortedArchive.add(new Pair<>(Mockito.mock(ISequences.class), new long[] {1578924703,36}));
		sortedArchive.add(new Pair<>(Mockito.mock(ISequences.class), new long[] {1577123136,37}));
		sortedArchive.add(new Pair<>(Mockito.mock(ISequences.class), new long[] {1576382160,39}));
		sortedArchive.add(new Pair<>(Mockito.mock(ISequences.class), new long[] {1575668595,41}));
		sortedArchive.add(new Pair<>(Mockito.mock(ISequences.class), new long[] {1573868675,42}));
		sortedArchive.add(new Pair<>(Mockito.mock(ISequences.class), new long[] {1573273640,43}));
		sortedArchive.add(new Pair<>(Mockito.mock(ISequences.class), new long[] {1573144654,44}));
		sortedArchive.add(new Pair<>(Mockito.mock(ISequences.class), new long[] {1572560075,45}));
		sortedArchive.add(new Pair<>(Mockito.mock(ISequences.class), new long[] {1572431089,46}));
		sortedArchive.add(new Pair<>(Mockito.mock(ISequences.class), new long[] {1571586348,47}));
		sortedArchive.add(new Pair<>(Mockito.mock(ISequences.class), new long[] {1571577296,50}));
		sortedArchive.add(new Pair<>(Mockito.mock(ISequences.class), new long[] {1571088519,51}));
		sortedArchive.add(new Pair<>(Mockito.mock(ISequences.class), new long[] {1570687514,53}));
		sortedArchive.add(new Pair<>(Mockito.mock(ISequences.class), new long[] {1570296649,54}));
		sortedArchive.add(new Pair<>(Mockito.mock(ISequences.class), new long[] {1569876078,55}));
		sortedArchive.add(new Pair<>(Mockito.mock(ISequences.class), new long[] {1569583084,56}));
		sortedArchive.add(new Pair<>(Mockito.mock(ISequences.class), new long[] {1569123610,57}));
		sortedArchive.add(new Pair<>(Mockito.mock(ISequences.class), new long[] {1568721942,58}));
		sortedArchive.add(new Pair<>(Mockito.mock(ISequences.class), new long[] {1568410045,59}));
		sortedArchive.add(new Pair<>(Mockito.mock(ISequences.class), new long[] {1568008377,60}));
		sortedArchive.add(new Pair<>(Mockito.mock(ISequences.class), new long[] {1567369140,61}));
		sortedArchive.add(new Pair<>(Mockito.mock(ISequences.class), new long[] {1566655575,63}));
		sortedArchive.add(new Pair<>(Mockito.mock(ISequences.class), new long[] {1566547123,64}));
		sortedArchive.add(new Pair<>(Mockito.mock(ISequences.class), new long[] {1565522178,65}));
		sortedArchive.add(new Pair<>(Mockito.mock(ISequences.class), new long[] {1565115819,66}));
		sortedArchive.add(new Pair<>(Mockito.mock(ISequences.class), new long[] {1564808613,67}));
		sortedArchive.add(new Pair<>(Mockito.mock(ISequences.class), new long[] {1564402254,68}));
		sortedArchive.add(new Pair<>(Mockito.mock(ISequences.class), new long[] {1562422678,69}));
		sortedArchive.add(new Pair<>(Mockito.mock(ISequences.class), new long[] {1560316581,71}));
		sortedArchive.add(new Pair<>(Mockito.mock(ISequences.class), new long[] {1559141753,73}));
		sortedArchive.add(new Pair<>(Mockito.mock(ISequences.class), new long[] {1558689957,74}));
		sortedArchive.add(new Pair<>(Mockito.mock(ISequences.class), new long[] {1558401841,75}));
		sortedArchive.add(new Pair<>(Mockito.mock(ISequences.class), new long[] {1555284209,76}));
		sortedArchive.add(new Pair<>(Mockito.mock(ISequences.class), new long[] {1554570644,78}));
		sortedArchive.add(new Pair<>(Mockito.mock(ISequences.class), new long[] {1554427140,79}));
		sortedArchive.add(new Pair<>(Mockito.mock(ISequences.class), new long[] {1552325240,80}));
		sortedArchive.add(new Pair<>(Mockito.mock(ISequences.class), new long[] {1551611675,82}));
		sortedArchive.add(new Pair<>(Mockito.mock(ISequences.class), new long[] {1551368568,84}));
		sortedArchive.add(new Pair<>(Mockito.mock(ISequences.class), new long[] {1550799527,86}));
		sortedArchive.add(new Pair<>(Mockito.mock(ISequences.class), new long[] {1548808291,87}));
		sortedArchive.add(new Pair<>(Mockito.mock(ISequences.class), new long[] {1548735687,89}));
		sortedArchive.add(new Pair<>(Mockito.mock(ISequences.class), new long[] {1547744087,90}));
		sortedArchive.add(new Pair<>(Mockito.mock(ISequences.class), new long[] {1547577899,91}));
		sortedArchive.add(new Pair<>(Mockito.mock(ISequences.class), new long[] {1547030522,92}));
		sortedArchive.add(new Pair<>(Mockito.mock(ISequences.class), new long[] {1546591707,94}));
		sortedArchive.add(new Pair<>(Mockito.mock(ISequences.class), new long[] {1546498653,95}));
		sortedArchive.add(new Pair<>(Mockito.mock(ISequences.class), new long[] {1546494313,98}));
		sortedArchive.add(new Pair<>(Mockito.mock(ISequences.class), new long[] {1546453522,100}));
		sortedArchive.add(new Pair<>(Mockito.mock(ISequences.class), new long[] {1546432411,102}));		
		return sortedArchive;
	}
	
	private List<Pair<ISequences,long[]>> createWeirdDistributionTestData() {
		List<Pair<ISequences, long[]>> sortedArchive = new ArrayList<>();
		
		sortedArchive.add(new Pair<>(Mockito.mock(ISequences.class), new long[] {-2965112638L,0}));
		sortedArchive.add(new Pair<>(Mockito.mock(ISequences.class), new long[] {-2964567642L,1}));
		sortedArchive.add(new Pair<>(Mockito.mock(ISequences.class), new long[] {-2284385981L,2}));
		sortedArchive.add(new Pair<>(Mockito.mock(ISequences.class), new long[] {-2116173656,3}));
		sortedArchive.add(new Pair<>(Mockito.mock(ISequences.class), new long[] {-2115989633,4}));
		sortedArchive.add(new Pair<>(Mockito.mock(ISequences.class), new long[] {-2054742241,5}));
		sortedArchive.add(new Pair<>(Mockito.mock(ISequences.class), new long[] {-2048426345,6}));
		sortedArchive.add(new Pair<>(Mockito.mock(ISequences.class), new long[] {-2027494294,7}));
		sortedArchive.add(new Pair<>(Mockito.mock(ISequences.class), new long[] {-1777072501,8}));
		sortedArchive.add(new Pair<>(Mockito.mock(ISequences.class), new long[] {-1715825109,9}));
		sortedArchive.add(new Pair<>(Mockito.mock(ISequences.class), new long[] {-1715398772,10}));
		sortedArchive.add(new Pair<>(Mockito.mock(ISequences.class), new long[] {-1710578523,11}));
		sortedArchive.add(new Pair<>(Mockito.mock(ISequences.class), new long[] {-1688088385,12}));
		sortedArchive.add(new Pair<>(Mockito.mock(ISequences.class), new long[] {-1683330576,13}));
		sortedArchive.add(new Pair<>(Mockito.mock(ISequences.class), new long[] {-1632824424,14}));
		sortedArchive.add(new Pair<>(Mockito.mock(ISequences.class), new long[] {-1619467467,15}));
		sortedArchive.add(new Pair<>(Mockito.mock(ISequences.class), new long[] {-1614378911,16}));
		sortedArchive.add(new Pair<>(Mockito.mock(ISequences.class), new long[] {-1613890134,17}));
		sortedArchive.add(new Pair<>(Mockito.mock(ISequences.class), new long[] {-1609809961,18}));
		sortedArchive.add(new Pair<>(Mockito.mock(ISequences.class), new long[] {-1605162313,19}));
		sortedArchive.add(new Pair<>(Mockito.mock(ISequences.class), new long[] {-1604348266,20}));
		sortedArchive.add(new Pair<>(Mockito.mock(ISequences.class), new long[] {-1599193813,21}));
		sortedArchive.add(new Pair<>(Mockito.mock(ISequences.class), new long[] {-1597686778,22}));
		sortedArchive.add(new Pair<>(Mockito.mock(ISequences.class), new long[] {-1597552841,23}));
		sortedArchive.add(new Pair<>(Mockito.mock(ISequences.class), new long[] {-1595864620,24}));
		sortedArchive.add(new Pair<>(Mockito.mock(ISequences.class), new long[] {-1593009065,26}));
		sortedArchive.add(new Pair<>(Mockito.mock(ISequences.class), new long[] {-1592005834,29}));
		sortedArchive.add(new Pair<>(Mockito.mock(ISequences.class), new long[] {-1591187800,30}));
		sortedArchive.add(new Pair<>(Mockito.mock(ISequences.class), new long[] {-1582458527,31}));
		sortedArchive.add(new Pair<>(Mockito.mock(ISequences.class), new long[] {-1581744962,33}));
		sortedArchive.add(new Pair<>(Mockito.mock(ISequences.class), new long[] {-1580255531,34}));
		sortedArchive.add(new Pair<>(Mockito.mock(ISequences.class), new long[] {-1579829194,35}));
		sortedArchive.add(new Pair<>(Mockito.mock(ISequences.class), new long[] {-1578924703,36}));
		sortedArchive.add(new Pair<>(Mockito.mock(ISequences.class), new long[] {-1577123136,37}));
		sortedArchive.add(new Pair<>(Mockito.mock(ISequences.class), new long[] {-1576382160,39}));
		sortedArchive.add(new Pair<>(Mockito.mock(ISequences.class), new long[] {-1575668595,41}));
		sortedArchive.add(new Pair<>(Mockito.mock(ISequences.class), new long[] {-1573868675,42}));
		sortedArchive.add(new Pair<>(Mockito.mock(ISequences.class), new long[] {-1573273640,43}));
		sortedArchive.add(new Pair<>(Mockito.mock(ISequences.class), new long[] {-1573144654,44}));
		sortedArchive.add(new Pair<>(Mockito.mock(ISequences.class), new long[] {-1572560075,45}));
		sortedArchive.add(new Pair<>(Mockito.mock(ISequences.class), new long[] {-1572431089,46}));
		sortedArchive.add(new Pair<>(Mockito.mock(ISequences.class), new long[] {-1571586348,47}));
		sortedArchive.add(new Pair<>(Mockito.mock(ISequences.class), new long[] {-1571577296,50}));
		sortedArchive.add(new Pair<>(Mockito.mock(ISequences.class), new long[] {-1571088519,51}));
		sortedArchive.add(new Pair<>(Mockito.mock(ISequences.class), new long[] {-1570687514,53}));
		sortedArchive.add(new Pair<>(Mockito.mock(ISequences.class), new long[] {-1570296649,54}));
		sortedArchive.add(new Pair<>(Mockito.mock(ISequences.class), new long[] {-1569876078,55}));
		sortedArchive.add(new Pair<>(Mockito.mock(ISequences.class), new long[] {-1569583084,56}));
		sortedArchive.add(new Pair<>(Mockito.mock(ISequences.class), new long[] {-1569123610,57}));
		sortedArchive.add(new Pair<>(Mockito.mock(ISequences.class), new long[] {-1568721942,58}));
		sortedArchive.add(new Pair<>(Mockito.mock(ISequences.class), new long[] {-1568410045,59}));
		sortedArchive.add(new Pair<>(Mockito.mock(ISequences.class), new long[] {-1568008377,60}));
		sortedArchive.add(new Pair<>(Mockito.mock(ISequences.class), new long[] {-1567369140,61}));
		sortedArchive.add(new Pair<>(Mockito.mock(ISequences.class), new long[] {-1566655575,63}));
		sortedArchive.add(new Pair<>(Mockito.mock(ISequences.class), new long[] {-1566547123,64}));
		sortedArchive.add(new Pair<>(Mockito.mock(ISequences.class), new long[] {-1565522178,65}));
		sortedArchive.add(new Pair<>(Mockito.mock(ISequences.class), new long[] {-1565115819,66}));
		sortedArchive.add(new Pair<>(Mockito.mock(ISequences.class), new long[] {-1564808613,67}));
		sortedArchive.add(new Pair<>(Mockito.mock(ISequences.class), new long[] {-1564402254,68}));
		sortedArchive.add(new Pair<>(Mockito.mock(ISequences.class), new long[] {-1562422678,69}));
		sortedArchive.add(new Pair<>(Mockito.mock(ISequences.class), new long[] {-1560316581,71}));
		sortedArchive.add(new Pair<>(Mockito.mock(ISequences.class), new long[] {-1559141753,73}));
		sortedArchive.add(new Pair<>(Mockito.mock(ISequences.class), new long[] {-1558689957,74}));
		sortedArchive.add(new Pair<>(Mockito.mock(ISequences.class), new long[] {-1558401841,75}));
		sortedArchive.add(new Pair<>(Mockito.mock(ISequences.class), new long[] {-1555284209,76}));
		sortedArchive.add(new Pair<>(Mockito.mock(ISequences.class), new long[] {-1554570644,78}));
		sortedArchive.add(new Pair<>(Mockito.mock(ISequences.class), new long[] {-1554427140,79}));
		sortedArchive.add(new Pair<>(Mockito.mock(ISequences.class), new long[] {-1552325240,80}));
		sortedArchive.add(new Pair<>(Mockito.mock(ISequences.class), new long[] {-1551611675,82}));
		sortedArchive.add(new Pair<>(Mockito.mock(ISequences.class), new long[] {-1551368568,84}));
		sortedArchive.add(new Pair<>(Mockito.mock(ISequences.class), new long[] {-1550799527,86}));
		sortedArchive.add(new Pair<>(Mockito.mock(ISequences.class), new long[] {-1548808291,87}));
		sortedArchive.add(new Pair<>(Mockito.mock(ISequences.class), new long[] {-1548735687,89}));
		sortedArchive.add(new Pair<>(Mockito.mock(ISequences.class), new long[] {-1547744087,90}));
		sortedArchive.add(new Pair<>(Mockito.mock(ISequences.class), new long[] {-1547577899,91}));
		sortedArchive.add(new Pair<>(Mockito.mock(ISequences.class), new long[] {-1547030522,92}));
		sortedArchive.add(new Pair<>(Mockito.mock(ISequences.class), new long[] {-1546591707,94}));
		sortedArchive.add(new Pair<>(Mockito.mock(ISequences.class), new long[] {-1546498653,95}));
		sortedArchive.add(new Pair<>(Mockito.mock(ISequences.class), new long[] {-1546494313,98}));
		sortedArchive.add(new Pair<>(Mockito.mock(ISequences.class), new long[] {-1546453522,100}));
		sortedArchive.add(new Pair<>(Mockito.mock(ISequences.class), new long[] {-1546432411,102}));
		
		return sortedArchive;
	}
	
	@Test
	public void findQuartileSolution() {
		List<Pair<ISequences, long[]>> sortedArchive = createLargeTestData();
		SimpleMultiObjectiveOptimiser simpleMultiObjectiveOptimiser = getSimpleMultiObjectiveOptimiser(SimilarityFitnessMode.HIGH);
		List<Pair<ISequences,long[]>> createWeirdDistributionTestData = createWeirdDistributionTestData();
		Pair<ISequences, long[]> quartileSolution = simpleMultiObjectiveOptimiser.findSolutionWhichReachesQuartile(sortedArchive, 0, eQuartile.THIRD, true);
		Assert.assertNotNull(quartileSolution);
		long[] fitnesses = quartileSolution.getSecond();
		System.out.println(Arrays.toString(fitnesses));
	}

	@Ignore
	@Test
	public void createLargeSpatiallyDividedSolutions() {
		List<Pair<ISequences, long[]>> sortedArchive = createLargeTestData();
		SimpleMultiObjectiveOptimiser simpleMultiObjectiveOptimiser = getSimpleMultiObjectiveOptimiser(SimilarityFitnessMode.HIGH);
		List<List<Pair<ISequences,long[]>>> spatiallyDividedSolutions = simpleMultiObjectiveOptimiser.getSpatiallyDividedSolutions(sortedArchive, 4, 0);
		checkSpatiallyDividedSolutions(sortedArchive, spatiallyDividedSolutions, new int[][] {{0, 26}, {26, 52}, {52, 78}, {78, 102}});
		ISequences bestRawSequences = simpleMultiObjectiveOptimiser.getBestRawSequences(sortedArchive, 4, 0);
		Pair<ISequences,long[]> collection = sortedArchive.stream().filter(a->a.getFirst().equals(bestRawSequences)).collect(Collectors.toList()).get(0);
		System.out.println(Arrays.toString(collection.getSecond()));
		int z = 0;
	}

	
	private void checkSpatiallyDividedSolutions(List<Pair<ISequences,long[]>> archive, List<List<Pair<ISequences,long[]>>> spatiallyDividedSolutions, int[][] breakpoints) {
		Assert.assertTrue(spatiallyDividedSolutions.size() == 4);
		int sumOfSizes = spatiallyDividedSolutions.stream().mapToInt(i -> i.size()).sum();
		Assert.assertTrue(sumOfSizes == archive.size() - 1);
		for (int i = 0; i < breakpoints.length; i++) {
			int[] bp = breakpoints[i];
			List<Pair<ISequences, long[]>> division = spatiallyDividedSolutions.get(i);
			for (Pair<ISequences, long[]> solution : division) {
				long[] fitnesses = solution.getSecond();
				Assert.assertTrue(String.format("breakpoints: %s fitnesses: %s", Arrays.toString(bp), Arrays.toString(fitnesses)), bp[0] < fitnesses[1] && bp[1] >= fitnesses[1]);
			}
		}
	}

	public SimpleMultiObjectiveOptimiser getSimpleMultiObjectiveOptimiser(final SimilarityFitnessMode similarityFitnessMode) {
		Injector injector = Guice.createInjector(new AbstractModule() {

			@Override
			protected void configure() {
				bind(IOptimisationData.class).toInstance(Mockito.mock(IOptimisationData.class));
				bind(IFitnessHelper.class).toInstance(Mockito.mock(IFitnessHelper.class));
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
