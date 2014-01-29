/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2013
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.fitness.impl;

import com.mmxlabs.scheduler.optimiser.fitness.ISequenceScheduler;
import com.mmxlabs.scheduler.optimiser.voyage.impl.VoyagePlan;

/**
 * Abstract {@link ISequenceScheduler} implementation to manage the sequence optimisation given a set of arrival times at each sequence element. This class handles the construction of
 * {@link VoyagePlan}s and uses a {@link IVoyagePlanOptimiser} implementation to make the best route choices.
 * 
 * @author Simon Goodall
 * 
 */
public abstract class AbstractSequenceScheduler extends AbstractLoggingSequenceScheduler implements ISequenceScheduler {



}