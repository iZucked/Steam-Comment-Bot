/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
package com.mmxlabs.models.ui.validation.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mmxlabs.models.ui.validation.IExtraValidationContext;

/**
 * Very simple object which just holds a threadlocal
 * 
 * @author hinton
 * 
 */
public class ValidationInputService {
	private static final Logger log = LoggerFactory.getLogger(ValidationInputService.class);
	final ThreadLocal<IExtraValidationContext> extraContext = new ThreadLocal<IExtraValidationContext>();

	public IExtraValidationContext getExtraContext() {
		return extraContext.get();
	}

	public void setExtraContext(final IExtraValidationContext extraContext) {
		if (extraContext != null && getExtraContext() != null) {
			log.warn("extra validation context was set without being cleared first; this suggests use of a validator without the ValidationHelper support class, which could cause problems");
		}
		this.extraContext.set(extraContext);
	}
}
