package com.mmxlabs.optimiser.fitness.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import junit.framework.Assert;

import org.jmock.Mockery;
import org.jmock.integration.junit4.JUnit4Mockery;
import org.junit.Test;

import com.mmxlabs.optimiser.fitness.IFitnessFunctionFactory;

public class FitnessFunctionRegisteryTest {

	Mockery context = new JUnit4Mockery();
	
	@Test
	public void testGetFitnessFunctionFactories() {
		
		FitnessFunctionRegistery registery = new FitnessFunctionRegistery();
		
		IFitnessFunctionFactory factory1 = context.mock(IFitnessFunctionFactory.class, "fectory1");
		IFitnessFunctionFactory factory2 = context.mock(IFitnessFunctionFactory.class, "fectory2");
		IFitnessFunctionFactory factory3 = context.mock(IFitnessFunctionFactory.class, "fectory3");
		
		registery.registerFitnessFunction("factory1", factory1);
		registery.registerFitnessFunction("factory2", factory2);
		registery.registerFitnessFunction("factory3", factory3);
		
		Collection<IFitnessFunctionFactory> fitnessFunctionFactories = registery.getFitnessFunctionFactories();
		Assert.assertEquals(3, fitnessFunctionFactories.size());
		
		Assert.assertTrue(fitnessFunctionFactories.contains(factory1));
		Assert.assertTrue(fitnessFunctionFactories.contains(factory2));
		Assert.assertTrue(fitnessFunctionFactories.contains(factory3));


		context.assertIsSatisfied();
		
	}

	

	@Test
	public void testGetFitnessFunctionFactories_Str() {
		
		FitnessFunctionRegistery registery = new FitnessFunctionRegistery();
		
		IFitnessFunctionFactory factory1 = context.mock(IFitnessFunctionFactory.class, "fectory1");
		IFitnessFunctionFactory factory2 = context.mock(IFitnessFunctionFactory.class, "fectory2");
		IFitnessFunctionFactory factory3 = context.mock(IFitnessFunctionFactory.class, "fectory3");
		
		registery.registerFitnessFunction("factory1", factory1);
		registery.registerFitnessFunction("factory2", factory2);
		registery.registerFitnessFunction("factory3", factory3);
		
		List<String> names = new ArrayList<String>(2);
		names.add("factory3");
		names.add("factory2");
		
		List<IFitnessFunctionFactory> fitnessFunctionFactories = registery.getFitnessFunctionFactories(names);
		Assert.assertEquals(2, fitnessFunctionFactories.size());
		
		Assert.assertEquals(factory3, fitnessFunctionFactories.get(0));
		Assert.assertEquals(factory2, fitnessFunctionFactories.get(1));
		

		context.assertIsSatisfied();
	}
	
	
	@Test
	public void testGetFitnessFunctionFactories_Str2() {

		
		FitnessFunctionRegistery registery = new FitnessFunctionRegistery();
		
		IFitnessFunctionFactory factory1 = context.mock(IFitnessFunctionFactory.class, "fectory1");
		IFitnessFunctionFactory factory2 = context.mock(IFitnessFunctionFactory.class, "fectory2");
		IFitnessFunctionFactory factory3 = context.mock(IFitnessFunctionFactory.class, "fectory3");
		
		registery.registerFitnessFunction("factory1", factory1);
		registery.registerFitnessFunction("factory2", factory2);
		registery.registerFitnessFunction("factory3", factory3);
		
		List<String> names = new ArrayList<String>(2);
		names.add("factory3");
		names.add("factory3");
		
		List<IFitnessFunctionFactory> fitnessFunctionFactories = registery.getFitnessFunctionFactories(names);
		Assert.assertEquals(2, fitnessFunctionFactories.size());
		
		Assert.assertEquals(factory3, fitnessFunctionFactories.get(0));
		Assert.assertEquals(factory3, fitnessFunctionFactories.get(1));
		
		Assert.fail("Ambiguous API - Duplicate entries?");

		context.assertIsSatisfied();
	
	}
	

	
	@Test
	public void testGetFitnessFunctionFactories_Str3() {

		
		FitnessFunctionRegistery registery = new FitnessFunctionRegistery();
		
		IFitnessFunctionFactory factory1 = context.mock(IFitnessFunctionFactory.class, "fectory1");
		IFitnessFunctionFactory factory2 = context.mock(IFitnessFunctionFactory.class, "fectory2");
		IFitnessFunctionFactory factory3 = context.mock(IFitnessFunctionFactory.class, "fectory3");
		
		registery.registerFitnessFunction("factory1", factory1);
		registery.registerFitnessFunction("factory2", factory2);
		registery.registerFitnessFunction("factory3", factory3);
		
		List<String> names = new ArrayList<String>(2);
		names.add("factory3");
		names.add("unknown");
		names.add("factory1");
		
		List<IFitnessFunctionFactory> fitnessFunctionFactories = registery.getFitnessFunctionFactories(names);
		Assert.assertEquals(3, fitnessFunctionFactories.size());
		
		Assert.assertEquals(factory3, fitnessFunctionFactories.get(0));
		Assert.assertNull(fitnessFunctionFactories.get(1));
		Assert.assertEquals(factory1, fitnessFunctionFactories.get(2));
		
		Assert.fail("Ambiguous API - Duplicate entries?");
		
		context.assertIsSatisfied();
	}
	
}
