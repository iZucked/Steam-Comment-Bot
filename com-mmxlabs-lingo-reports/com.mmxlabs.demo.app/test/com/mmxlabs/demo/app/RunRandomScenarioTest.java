package com.mmxlabs.demo.app;

import java.io.IOException;
import java.io.InputStream;

import org.junit.Assert;
import org.junit.Test;

import scenario.Scenario;

import com.mmxlabs.common.Pair;
import com.mmxlabs.demo.app.wizards.RandomScenarioUtils;
import com.mmxlabs.jobcontroller.emf.IncompleteScenarioException;
import com.mmxlabs.jobcontroller.emf.LNGScenarioTransformer;
import com.mmxlabs.jobcontroller.emf.optimisationsettings.OptimisationTransformer;
import com.mmxlabs.optimiser.core.IOptimisationContext;
import com.mmxlabs.optimiser.core.IOptimiser;
import com.mmxlabs.optimiser.core.ISequences;
import com.mmxlabs.optimiser.core.scenario.IOptimisationData;
import com.mmxlabs.optimiser.lso.IOptimiserProgressMonitor;
import com.mmxlabs.optimiser.lso.impl.LocalSearchOptimiser;
import com.mmxlabs.scheduler.optimiser.components.ISequenceElement;

public class RunRandomScenarioTest {
	@Test
	public void runRandomScenarioTest() throws IOException,
			IncompleteScenarioException {
		InputStream stream = this.getClass()
				.getResourceAsStream("/data/distances.csv");

		RandomScenarioUtils utils = new RandomScenarioUtils();
		Scenario s = utils.createRandomScenario(stream, true, 100, true);

		// create optimiser

		LNGScenarioTransformer transformer = new LNGScenarioTransformer(s);

		IOptimisationData<ISequenceElement> data = transformer
				.createOptimisationData();

		OptimisationTransformer ot = new OptimisationTransformer(
				transformer.getOptimisationSettings());

		Pair<IOptimisationContext<ISequenceElement>, LocalSearchOptimiser<ISequenceElement>> optimiserAndContext = ot
				.createOptimiserAndContext(data);
		
		final LocalSearchOptimiser<ISequenceElement> optimiser = optimiserAndContext.getSecond();
		final IOptimisationContext<ISequenceElement> context = optimiserAndContext.getFirst();
		
		optimiser.setProgressMonitor(new IOptimiserProgressMonitor<ISequenceElement>() {
			
			@Override
			public void report(IOptimiser<ISequenceElement> optimiser, int iteration,
					long currentFitness, long bestFitness,
					ISequences<ISequenceElement> currentState,
					ISequences<ISequenceElement> bestState) {
				Assert.assertTrue(currentFitness > 0);
				Assert.assertTrue(bestFitness > 0);
				Assert.assertTrue(bestFitness <= currentFitness);
			}
			
			@Override
			public void done(IOptimiser<ISequenceElement> optimiser, long bestFitness,
					ISequences<ISequenceElement> bestState) {
				
			}
			
			@Override
			public void begin(IOptimiser<ISequenceElement> optimiser,
					long initialFitness, ISequences<ISequenceElement> initialState) {
				Assert.assertTrue(initialFitness > 0);
			}
		});
		
		optimiser.init();
		optimiser.optimise(context);
	}
}
