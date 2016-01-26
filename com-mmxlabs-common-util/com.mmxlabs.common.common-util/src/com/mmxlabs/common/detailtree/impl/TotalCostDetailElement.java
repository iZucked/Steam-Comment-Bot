/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
package com.mmxlabs.common.detailtree.impl;

import com.mmxlabs.common.detailtree.IDetailTreeElement;

/**
 * Implementation of {@link IDetailTreeElement} indicating the contained object is a large cost related. We expect a large integer based number - no decimal places.
 * 
 * @author Simon Goodall
 * @since 2.1
 */
public class TotalCostDetailElement implements IDetailTreeElement {

	private final Number d;

	public TotalCostDetailElement(final Number d) {
		this.d = d;
	}

	@Override
	public Object getObject() {
		return d;
	}
}
