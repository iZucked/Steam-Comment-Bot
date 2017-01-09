/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.components.impl;

import java.util.Objects;

import org.eclipse.jdt.annotation.NonNull;

import com.mmxlabs.optimiser.core.ISequenceElement;
import com.mmxlabs.scheduler.optimiser.components.IPortSlot;

public class WrappedSequenceElement implements ISequenceElement {

	private final @NonNull IPortSlot portSlot;

	public WrappedSequenceElement(final @NonNull IPortSlot portSlot) {
		this.portSlot = portSlot;
	}

	@Override
	public int getIndex() {
		throw new UnsupportedOperationException();
	}

	@Override
	public @NonNull String getName() {
		return "Wrapped" + portSlot.getId();
	}

	@Override
	public int hashCode() {
		return portSlot.hashCode();
	}

	@Override
	public boolean equals(final Object obj) {
		if (obj == this) {
			return true;
		}
		if (obj instanceof WrappedSequenceElement) {
			final WrappedSequenceElement other = (WrappedSequenceElement) obj;
			return Objects.equals(portSlot, other.portSlot);
		}
		return false;
	}

	@NonNull
	public IPortSlot getPortSlot() {
		return portSlot;
	}
}
