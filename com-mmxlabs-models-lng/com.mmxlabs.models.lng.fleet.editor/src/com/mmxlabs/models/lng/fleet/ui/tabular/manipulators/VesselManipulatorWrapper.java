/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
package com.mmxlabs.models.lng.fleet.ui.tabular.manipulators;

import org.eclipse.jdt.annotation.NonNull;

import com.mmxlabs.models.lng.fleet.Vessel;
import com.mmxlabs.models.ui.tabular.ICellManipulator;
import com.mmxlabs.models.ui.tabular.ICellRenderer;
import com.mmxlabs.models.ui.tabular.manipulators.IdentityManipulatorWrapper;

/**
 * Vessel attribute/reference manipulator wrapper that prevents editing if the
 * vessel is a (read-only) Minimax Labs maintained vessel.
 * 
 * @author miten
 *
 * @param <T>
 */
public class VesselManipulatorWrapper<T extends ICellManipulator & ICellRenderer> extends IdentityManipulatorWrapper<T> {

	public VesselManipulatorWrapper(final @NonNull T wrapped) {
		super(wrapped);
	}

	@Override
	public boolean canEdit(final Object object) {
		if (object instanceof Vessel) {
			return !((Vessel) object).isMmxReference() && super.canEdit(object);
		}
		return super.canEdit(object);
	}
}
