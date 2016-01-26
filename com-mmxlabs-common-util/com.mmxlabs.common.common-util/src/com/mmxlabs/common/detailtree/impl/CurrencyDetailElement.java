/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
package com.mmxlabs.common.detailtree.impl;

import com.mmxlabs.common.detailtree.IDetailTreeElement;

/**
 * Implementation of {@link IDetailTreeElement} indicating the contained object is currency related.
 * 
 * @author Simon Goodall
 * 
 */
public class CurrencyDetailElement implements IDetailTreeElement {

	private final Number d;

	public CurrencyDetailElement(final Number d) {
		this.d = d;
	}

	@Override
	public Object getObject() {
		return d;
	}
}
