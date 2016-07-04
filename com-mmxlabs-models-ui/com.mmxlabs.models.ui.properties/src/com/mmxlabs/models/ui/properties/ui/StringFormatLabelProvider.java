/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
package com.mmxlabs.models.ui.properties.ui;

import org.eclipse.jface.viewers.BaseLabelProvider;
import org.eclipse.jface.viewers.ILabelProvider;
import org.eclipse.swt.graphics.Image;

public class StringFormatLabelProvider extends BaseLabelProvider implements ILabelProvider {

	private final String formatString;

	public StringFormatLabelProvider(final String formatString) {
		this.formatString = formatString;
	}

	@Override
	public Image getImage(final Object element) {
		return null;
	}

	@Override
	public String getText(final Object element) {
		return String.format(formatString, element);
	}
}
