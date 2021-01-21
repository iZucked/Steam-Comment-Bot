/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2021
 * All rights reserved.
 */
package com.mmxlabs.optimiser.core;

/**
 * Slimmer interface to match an Eclipse progress monitor without adding the dependencies
 * 
 * @author Simon Goodall
 *
 */
public interface ISimpleProgressMonitor {

	void beginTask(String name, int totalWork);

	void done();

	boolean isCanceled();

	void worked(int work);
}
