/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
package com.mmxlabs.jobmanager.jobs.impl;

import java.util.Collection;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import com.mmxlabs.jobmanager.jobs.IJobControl;
import com.mmxlabs.jobmanager.jobs.IJobControlFactory;
import com.mmxlabs.jobmanager.jobs.IJobDescriptor;

/**
 * Implementation of {@link IJobControlFactory} which takes a {@link List} of {@link IJobControlFactory} instances and iterates over each one to try and generate a {@link IJobControl}.
 * {@link IJobControlFactory} are tried in the order they have been added and the first {@link IJobControl} created is returned.
 * 
 * @author Simon Goodall
 * 
 */
public final class CompositeJobControlFactory implements IJobControlFactory {

	private final List<IJobControlFactory> jobControlFactories = new LinkedList<IJobControlFactory>();

	/**
	 * Construct an empty {@link CompositeJobControlFactory}. {@link IJobControlFactory} should be registered by calling {@link #addJobControlFactory(IJobControlFactory)}.
	 */
	public CompositeJobControlFactory() {

	}

	/**
	 * Construct a {@link CompositeJobControlFactory} with the given {@link Collection} of {@link IJobControlFactory}s
	 * 
	 * @param factories
	 */
	public CompositeJobControlFactory(final Collection<IJobControlFactory> factories) {
		jobControlFactories.addAll(factories);
	}

	/**
	 * Construct a {@link CompositeJobControlFactory} with the given {@link Collection} of {@link IJobControlFactory}s
	 * 
	 * @param factories
	 */
	public CompositeJobControlFactory(final IJobControlFactory... factories) {
		for (final IJobControlFactory factory : factories) {
			jobControlFactories.add(factory);
		}
	}

	@Override
	public IJobControl createJobControl(final IJobDescriptor job) {

		for (final IJobControlFactory factory : jobControlFactories) {
			final IJobControl jobControl = factory.createJobControl(job);
			if (jobControl != null) {
				return jobControl;
			}
		}
		return null;
	}

	/**
	 * Append a {@link IJobControlFactory} to the current {@link List} of {@link IJobControlFactory}s
	 * 
	 * @param factory
	 */
	public void addJobControlFactory(final IJobControlFactory factory) {
		jobControlFactories.add(factory);
	}

	/**
	 * Remove a {@link IJobControlFactory} from the current {@link List} of {@link IJobControlFactory}s
	 * 
	 * @param factory
	 */
	public void removeJobControlFactory(final IJobControlFactory factory) {
		jobControlFactories.remove(factory);
	}

	/**
	 * Returns an unmodifiable {@link List} of {@link IJobControlFactory}s.
	 * 
	 * @return
	 */
	public List<IJobControlFactory> getJobControlFactories() {
		return Collections.unmodifiableList(jobControlFactories);
	}
}
