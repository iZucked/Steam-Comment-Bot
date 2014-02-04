/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2013
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.annotations;

/**
 * An annotation describing the heel levels at a port visit.
 * 
 * @author Simon Goodall
 * 
 */
public interface IHeelLevelAnnotation {

	long getStartHeelInM3();

	long getEndHeelInM3();
}
