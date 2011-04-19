/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2011
 * All rights reserved.
 */
package com.mmxlabs.lngscheduler.emf.extras.validation;

import org.eclipse.emf.validation.model.IClientSelector;

/**
 * Tells the validation framework whether to validate certain objects.
 * @author Tom Hinton
 *
 */
public class ScenarioClientSelector implements IClientSelector {

	/* (non-Javadoc)
	 * @see org.eclipse.emf.validation.model.IClientSelector#selects(java.lang.Object)
	 */
	@Override
	public boolean selects(final Object object) {
		System.err.println("Selecting " + object);
		return true; // not sure what ought to happen here
	}

}
