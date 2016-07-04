/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.components.impl;

import com.mmxlabs.common.indexedobjects.IIndexingContext;
import com.mmxlabs.scheduler.optimiser.components.IXYPort;

/**
 * Extended implementation of {@link Port} providing a X/Y location of some kind.
 * 
 * @author Simon Goodall
 * 
 */
public final class XYPort extends Port implements IXYPort {

	private float x;

	private float y;

	public XYPort(final IIndexingContext context) {
		super(context);
	}

	public XYPort(final IIndexingContext context, final String name, final float x, final float y) {
		super(context, name);
		this.x = x;
		this.y = y;
	}

	@Override
	public float getX() {
		return x;
	}

	public void setX(final float x) {
		this.x = x;
	}

	@Override
	public float getY() {
		return y;
	}

	public final void setY(final float y) {
		this.y = y;
	}

	@Override
	public boolean equals(final Object obj) {

		if (obj instanceof XYPort) {
			final XYPort p = (XYPort) obj;

			if (x != p.x) {
				return false;
			}
			if (y != p.y) {
				return false;
			}

			return super.equals(obj);
		}

		return false;
	}
	
	@Override
	public String toString() {
		return getName();
	}
}
