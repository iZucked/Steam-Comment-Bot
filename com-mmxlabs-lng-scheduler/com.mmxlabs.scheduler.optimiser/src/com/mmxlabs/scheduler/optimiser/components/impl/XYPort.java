/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.components.impl;

import org.eclipse.jdt.annotation.NonNullByDefault;
import org.eclipse.jdt.annotation.Nullable;

import com.mmxlabs.common.indexedobjects.IIndexingContext;
import com.mmxlabs.scheduler.optimiser.components.IXYPort;

/**
 * Extended implementation of {@link Port} providing a X/Y location of some kind.
 * 
 * @author Simon Goodall
 * 
 */
@NonNullByDefault
public final class XYPort extends Port implements IXYPort {

	private double  x;

	private double y;

	public XYPort(final IIndexingContext context) {
		super(context);
	}

	public XYPort(final IIndexingContext context, final String name, final double x, final double y) {
		super(context, name);
		this.x = x;
		this.y = y;
	}

	@Override
	public double getX() {
		return x;
	}

	public void setX(final double x) {
		this.x = x;
	}

	@Override
	public double getY() {
		return y;
	}

	public final void setY(final double y) {
		this.y = y;
	}

	@Override
	public boolean equals(final @Nullable Object obj) {

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
