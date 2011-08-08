package com.mmxlabs.jobcontroller.jobs;

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
