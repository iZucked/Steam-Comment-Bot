/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2012
 * All rights reserved.
 */
package com.mmxlabs.models.lng.analytics.evaluation;

import org.eclipse.core.runtime.IProgressMonitor;

import com.mmxlabs.models.mmxcore.MMXRootObject;

/**
 * Provides the facility for continuous evaluation of scenarios on a background thread.
 * 
 * @author hinton
 *
 */
public interface IEvaluationService {
	public void evaluate(final MMXRootObject scenario, final IProgressMonitor monitor);
}
