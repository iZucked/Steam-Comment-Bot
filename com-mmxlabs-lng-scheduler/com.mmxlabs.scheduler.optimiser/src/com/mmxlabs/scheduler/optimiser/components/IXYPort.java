/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2014
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.components;

/**
 * Extended version of {@link IPort} providing an X/Y co-ordinate. TODO: This could be generalised to provide e.g. ICoordinate
 * 
 * @author Simon Goodall
 * 
 */
public interface IXYPort extends IPort {

	float getX();

	float getY();
}
