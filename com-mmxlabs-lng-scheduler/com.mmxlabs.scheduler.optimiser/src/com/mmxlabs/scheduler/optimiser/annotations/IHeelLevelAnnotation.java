/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.annotations;

import com.mmxlabs.optimiser.core.IElementAnnotation;

/**
 * An annotation describing the heel levels at a port visit.
 * 
 * @author Simon Goodall
 * 
 */
public interface IHeelLevelAnnotation extends IElementAnnotation {

	long getStartHeelInM3();

	long getEndHeelInM3();
}
