/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
package com.mmxlabs.models.lng.transformer.ui.breakdown;

import java.io.Serializable;

import com.google.common.base.Objects;
import com.mmxlabs.optimiser.core.ISequenceElement;

/**
 * Individual change TODO: Add in detailed information to reproduce the change
 *
 */
public class Change implements Serializable {
	public String description;

	/**
	 * SG Testing constructor -- ignore
	 * 
	 * @param s
	 * @param e
	 * @param others
	 */
	public Change(final String s, final ISequenceElement e, final ISequenceElement... others) {
		this(s);
	}

	public Change(final String s) {
		this.description = s;
	}

	@Override
	public boolean equals(final Object obj) {
		if (obj instanceof Change) {
			return Objects.equal(description, ((Change) obj).description);
		}
		return false;
	}

	@Override
	public int hashCode() {
		return description.hashCode();
	}
}