/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2021
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.components.impl;

import org.eclipse.jdt.annotation.NonNull;

import com.mmxlabs.common.indexedobjects.IIndexingContext;
import com.mmxlabs.common.indexedobjects.impl.IndexedObject;
import com.mmxlabs.scheduler.optimiser.components.IBaseFuel;

/**
 * Default implementation of {@link IBaseFuel}
 * 
 * @author achurchill
 * 
 */
public class BaseFuel extends IndexedObject implements IBaseFuel {
	/**
	 * The name of the base fuel
	 */
	private @NonNull String name;

	/**
	 * Equivalence factor for converting base fuel to lng
	 */
	private int equivalenceFactor;

	public BaseFuel(final @NonNull IIndexingContext context, final @NonNull String name) {
		super(context);
		this.name = name;
	}

	@Override
	public String getName() {
		return name;
	}

	public void setName(final @NonNull String name) {
		this.name = name;
	}
//
//	@Override
//	public boolean equals(final Object obj) {
//		if (obj == this) {
//			return true;
//		}
//		if (obj instanceof BaseFuel) {
//			final BaseFuel p = (BaseFuel) obj;
//			return index == p.getIndex();
//			// if (!Equality.isEqual(name, p.getName())) {
//			// return false;
//			// }
//			//
//			// if (!Equality.isEqual(getEquivalenceFactor(), p.getEquivalenceFactor())) {
//			// return false;
//			// }
//			// return true;
//		}
//
//		return false;
//	}
//
//	@Override
//	public int hashCode() {
//		return index;
//	}

	@Override
	public String toString() {
		return name;
	}

	@Override
	public int getEquivalenceFactor() {
		return equivalenceFactor;
	}

	@Override
	public void setEquivalenceFactor(int factor) {
		equivalenceFactor = factor;
	}
}
