/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2013
 * All rights reserved.
 */
package com.mmxlabs.models.ui.validation;

/**
 * A service for providing extra data to running validations
 * 
 * @author hinton
 *
 */
public interface IValidationInputService {
	public IExtraValidationContext getExtraContext();
}
