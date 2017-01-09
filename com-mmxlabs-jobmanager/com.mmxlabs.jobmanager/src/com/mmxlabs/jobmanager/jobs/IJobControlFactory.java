/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
package com.mmxlabs.jobmanager.jobs;

/**
 * A factory interface used to generate {@link IJobControl} instances for a given {@link IJobDescriptor}.s
 * 
 * @author Simon Goodall
 * 
 */
public interface IJobControlFactory {

	/**
	 * Create and returns a new {@link IJobControl} instance for the given {@link IJobDescriptor}.
	 */
	IJobControl createJobControl(IJobDescriptor job);
}
