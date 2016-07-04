/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
package com.mmxlabs.jobmanager.manager;

import com.mmxlabs.jobmanager.jobs.IJobDescriptor;

/**
 * Interface to apply a scoring system to determine the suitability of a {@link IJobManager} instance for the execution of a given {@link IJobDescriptor}. The scoring system is left up to the
 * implementation of the interface, but in general, the lower the number the better the match. I.e. a score of zero should be taken as a perfect match. Negative numbers are assumed to mean no match.
 * 
 * @author Simon Goodall
 * 
 */
public interface IJobMatcher {

	int matchJob(IJobDescriptor descriptor, IJobManager jobManager);
}
