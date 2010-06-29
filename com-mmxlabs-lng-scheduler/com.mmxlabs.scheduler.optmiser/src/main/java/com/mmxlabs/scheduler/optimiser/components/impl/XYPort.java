package com.mmxlabs.scheduler.optimiser.components.impl;

import com.mmxlabs.scheduler.optimiser.components.IXYPort;

/**
 * Default implementation of {@link IXYPort}
 * 
 * @author Simon Goodall
 * 
 */
public final class XYPort implements IXYPort {

	private String name;

	private float x;

	private float y;

	@Override
	public String getName() {
		return name;
	}

	public void setName(final String name) {
		this.name = name;
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
}
