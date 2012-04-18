/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2012
 * All rights reserved.
 */
package com.mmxlabs.models.lng.analytics.ui.liveeval;

import org.eclipse.core.resources.IResource;

/**
 * This is a bodge to make the evaluation handler from the platform available here.
 * 
 * @author hinton
 *
 */
public interface IResourceEvaluator {
	public void evaluate(final IResource resource);
}
