/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
package com.mmxlabs.models.ui.editors.util;

import org.eclipse.jdt.annotation.Nullable;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;

/**
 * Utility methods for doing things to controls
 * 
 * @author hinton
 * 
 */
public class ControlUtils {
	/**
	 * Set the enabled state on the given control, and all its children if it is a composite
	 * 
	 * @param sub
	 * @param enabled
	 */
	public static void setControlEnabled(final Control sub, final boolean enabled) {
		sub.setEnabled(enabled);
		if (sub instanceof Composite) {
			for (final Control c : ((Composite) sub).getChildren()) {
				setControlEnabled(c, enabled);
			}
		}
	}

	/**
	 * @param control
	 * @param class1
	 * @return
	 */
	public static <@Nullable U> U findControlOfClass(final Control control, Class<U> class1) {
		if (class1.isInstance(control)) {
			return class1.cast(control);
		} else if (control instanceof Composite) {
			for (final Control child : ((Composite) control).getChildren()) {
				final U possible = findControlOfClass(child, class1);
				if (possible != null) {
					return possible;
				}
			}
		}
		return null;

	}
}
