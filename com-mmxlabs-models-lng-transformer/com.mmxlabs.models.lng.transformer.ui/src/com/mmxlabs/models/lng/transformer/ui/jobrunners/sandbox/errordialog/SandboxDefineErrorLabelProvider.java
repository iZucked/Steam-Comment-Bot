/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
package com.mmxlabs.models.lng.transformer.ui.jobrunners.sandbox.errordialog;

import org.eclipse.jface.viewers.BaseLabelProvider;
import org.eclipse.jface.viewers.ILabelProvider;
import org.eclipse.swt.graphics.Image;

/**
 * 
 * @author Simon Goodall
 * 
 */
public class SandboxDefineErrorLabelProvider extends BaseLabelProvider implements ILabelProvider {

	@Override
	public String getText(final Object element) {
		if (element instanceof SolutionErrorSet set) {
			return set.name();
		}
		if (element instanceof String str) {
			return str;
		}
		return null;
	}

	@Override
	public Image getImage(Object element) {
		return null;
	}

}
