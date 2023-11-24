/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
package com.mmxlabs.models.ui.editors.dialogs;

import org.eclipse.jdt.annotation.NonNullByDefault;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.ui.forms.IFormColors;
import org.eclipse.ui.forms.widgets.FormToolkit;

/**
 * Helper / util class to restore Eclipse 2021 era Form dialog colours.
 */
@NonNullByDefault
public final class FormDialogColourHelper {

	private FormDialogColourHelper() {

	}

	public static void setFormColours(final FormToolkit toolkit) {
		// First call get to ensure the toolkit has triggered the initialisation functions as these reset the scheme...
		toolkit.getColors().getColor(IFormColors.TB_TOGGLE);
		toolkit.getColors().getColor(IFormColors.TB_TOGGLE_HOVER);
		toolkit.getColors().getColor(IFormColors.H_GRADIENT_START);
		toolkit.getColors().getColor(IFormColors.H_GRADIENT_END);

		// ... then set the colours
		// Blue section font
		toolkit.getColors().createColor(IFormColors.TB_TOGGLE, new RGB(0, 120, 215));
		toolkit.getColors().createColor(IFormColors.TB_TOGGLE_HOVER, new RGB(102, 174, 231));
		// Instead of the old gradient, use solid white
		toolkit.getColors().createColor(IFormColors.H_GRADIENT_START, new RGB(255, 255, 255));
		toolkit.getColors().createColor(IFormColors.H_GRADIENT_END, new RGB(255, 255, 255));
	}

}
