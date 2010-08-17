package com.mmxlabs.scheduler.optimiser.fitness.impl;

import com.mmxlabs.optimiser.core.scenario.common.IMatrixProvider;
import com.mmxlabs.optimiser.core.scenario.common.IMultiMatrixProvider;
import com.mmxlabs.scheduler.optimiser.components.IPort;
import com.mmxlabs.scheduler.optimiser.voyage.impl.VoyageOptions;

/**
 * Implementation of {@link IVoyagePlanChoice} to change the route used between
 * ports.
 * 
 * @author Simon Goodall
 * 
 */
public final class RouteVoyagePlanChoice implements IVoyagePlanChoice {

	private final VoyageOptions options;

	private final IMultiMatrixProvider<IPort, Integer> multiProvider;

	private final String[] choices;

	public RouteVoyagePlanChoice(final VoyageOptions options,
			final IMultiMatrixProvider<IPort, Integer> multiProvider) {
		this.options = options;
		this.choices = new String[] { "default" };
		this.multiProvider = multiProvider;
	}

	public RouteVoyagePlanChoice(final VoyageOptions options,
			final String[] choices,
			final IMultiMatrixProvider<IPort, Integer> multiProvider) {
		this.options = options;
		this.choices = choices;
		this.multiProvider = multiProvider;
	}

	@Override
	public int numChoices() {
		return choices.length;
	}

	@Override
	public boolean apply(final int choice) {

		options.setRoute(choices[choice]);

		final IMatrixProvider<IPort, Integer> m = multiProvider
				.get(choices[choice]);
		if (m == null) {
			return false;
		}

		final int distance = m.get(options.getFromPortSlot().getPort(), options
				.getToPortSlot().getPort());
		options.setDistance(distance);

		return true;
	}
}
