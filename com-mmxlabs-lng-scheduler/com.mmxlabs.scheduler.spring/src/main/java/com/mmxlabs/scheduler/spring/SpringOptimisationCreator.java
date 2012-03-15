/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2012
 * All rights reserved.
 */
package com.mmxlabs.scheduler.spring;

import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.springframework.beans.factory.config.PropertyPlaceholderConfigurer;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.context.support.StaticApplicationContext;

import com.mmxlabs.optimiser.core.IOptimisationContext;
import com.mmxlabs.optimiser.core.IOptimiserProgressMonitor;
import com.mmxlabs.optimiser.core.scenario.IOptimisationData;
import com.mmxlabs.optimiser.lso.ILocalSearchOptimiser;

/**
 * Class to construct an {@link IOptimisationContext} and {@link ILocalSearchOptimiser} using Spring to wire together various beans.
 * 
 * @author Simon Goodall
 * 
 */
public class SpringOptimisationCreator {

	private ClassPathXmlApplicationContext context;

	public SpringOptimisationCreator(final IOptimisationData optimisationData, final Properties props, final Map<String, Double> fitnessWeights, final List<String> constraintCheckers,
			final IOptimiserProgressMonitor monitor) {

		// Create a parent application context containing the common beans
		final ClassPathXmlApplicationContext c = new ClassPathXmlApplicationContext();
		c.setClassLoader(SpringOptimisationCreator.class.getClassLoader());
		c.setConfigLocations(new String[] { "/constraint-registry.xml", "/fitness-registry.xml" });

		c.refresh();

		// Create another app context in which we can register our provided
		// "singletons"
		final StaticApplicationContext staticContext = new StaticApplicationContext(c);
		staticContext.refresh();
		staticContext.getBeanFactory().registerSingleton("optimisationData", optimisationData);
		staticContext.getBeanFactory().registerSingleton("fitnessWeights", fitnessWeights);
		staticContext.getBeanFactory().registerSingleton("constraintCheckerNames", constraintCheckers);
		staticContext.getBeanFactory().registerSingleton("progressMonitor", monitor);

		// Create final context to instantiate optimisation context and
		// optimisation itself.
		context = new ClassPathXmlApplicationContext(staticContext);
		context.setClassLoader(SpringOptimisationCreator.class.getClassLoader());

		final PropertyPlaceholderConfigurer cfg = new PropertyPlaceholderConfigurer();
		cfg.setProperties(props);
		context.addBeanFactoryPostProcessor(cfg);

		context.setConfigLocations(new String[] { "/optimiser-context.xml", "/local-search-optimiser.xml" });

		context.refresh();
	}

	public void dispose() {
		context.destroy();
		context.close();
		context = null;

		// TODO: Destroy other parent contexts too?
	}

	public ILocalSearchOptimiser getOptimiser() {

		final ILocalSearchOptimiser optimiser = context.getBean("optimiser", ILocalSearchOptimiser.class);

		return optimiser;
	}

	public IOptimisationContext getOptimisationContext() {

		final IOptimisationContext optContext = context.getBean("optimisationContext", IOptimisationContext.class);

		return optContext;
	}
}
