/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2011
 * All rights reserved.
 */
package com.mmxlabs.lngscheduler.emf.extras;

import org.eclipse.emf.ecore.EObject;

/**
 * An interface for getting values out of an EObject, one way or another.
 * 
 * @author Tom Hinton
 * 
 */
public interface IEMFPathDelegate {
	public Object getValue(final EObject top);
}
