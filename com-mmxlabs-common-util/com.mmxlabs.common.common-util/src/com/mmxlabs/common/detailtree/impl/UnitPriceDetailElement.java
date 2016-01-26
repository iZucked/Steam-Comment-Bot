/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
package com.mmxlabs.common.detailtree.impl;

import com.mmxlabs.common.detailtree.IDetailTreeElement;

/**
 * Implementation of {@link IDetailTreeElement} indicating the contained object is unit price related. We expect a small number with decimal place accuracy.
 * 
 * @author Simon Goodall
 * @since 2.1
 */
public class UnitPriceDetailElement implements IDetailTreeElement {

	private final Number d;

	public UnitPriceDetailElement(final Number d) {
		this.d = d;
	}

	@Override
	public Object getObject() {
		return d;
	}
}
