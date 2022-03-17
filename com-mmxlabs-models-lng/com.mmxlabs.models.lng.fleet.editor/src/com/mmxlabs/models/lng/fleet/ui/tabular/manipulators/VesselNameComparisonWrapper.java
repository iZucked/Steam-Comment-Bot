/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2022
 * All rights reserved.
 */
package com.mmxlabs.models.lng.fleet.ui.tabular.manipulators;

import org.eclipse.jdt.annotation.NonNull;

import com.mmxlabs.models.lng.fleet.Vessel;
import com.mmxlabs.models.ui.tabular.ICellManipulator;
import com.mmxlabs.models.ui.tabular.ICellRenderer;
import com.mmxlabs.models.ui.tabular.manipulators.IdentityManipulatorWrapper;

/**
 * Vessel attribute/reference manipulator wrapper that trims the vessel name of
 * it's first and last characters when comparing (Minimax Labs maintained
 * vessels are contained in angled brackets at time of writing)
 * 
 * @author miten
 *
 * @param <T>
 */
public class VesselNameComparisonWrapper<T extends ICellManipulator & ICellRenderer> extends IdentityManipulatorWrapper<T> {

	public VesselNameComparisonWrapper(@NonNull T wrapped) {
		super(wrapped);
	}

	@SuppressWarnings("rawtypes")
	@Override
	public Comparable getComparable(final Object object) {
		if (object instanceof Vessel) {
			final Vessel vessel = (Vessel) object;
			if (vessel.isMmxReference()) {
				final String vesselName = vessel.getName();
				return vesselName.substring(1, vesselName.length() - 1);
			}
		}
		return super.getComparable(object);
	}

}
