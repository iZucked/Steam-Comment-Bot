package com.mmxlabs.optimiser.fitness;

import static org.junit.Assert.fail;
import junit.framework.Assert;

import org.jmock.Mockery;
import org.jmock.integration.junit4.JMock;
import org.jmock.integration.junit4.JUnit4Mockery;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(JMock.class)
class MatrixProviderFitnessComponentTest {

	
	Mockery context = new JUnit4Mockery();
	@Test
	public void testMatrixProviderFitnessComponent() {
		
		String name = "name";
		String key = "key";
		MatrixProviderFitnessCore<Object>core = new MatrixProviderFitnessCore<Object>(name, key );
		MatrixProviderFitnessComponent<Object> component = new MatrixProviderFitnessComponent<Object>(name, core);
		
		Assert.assertSame(name, component.getName());
		Assert.assertSame(core, component.getFitnessCore());
	}

	@Test
	public void testGetFitness() {
		fail("Not yet implemented");
	}
}
