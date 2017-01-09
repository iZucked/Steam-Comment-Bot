/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
package com.mmxlabs.common.detailtree.impl;

import com.mmxlabs.common.detailtree.IDetailTreeElement;

/**
 * Implementation of {@link IDetailTreeElement} indicating the contained object is duration related.
 * 
 * @author Simon Goodall
 * 
 */
public class DurationDetailElement implements IDetailTreeElement {

	private final Number d;

	public DurationDetailElement(final Number d) {
		this.d = d;
	}

	@Override
	public Object getObject() {
		return d;
	}
}
