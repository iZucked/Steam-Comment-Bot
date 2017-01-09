/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
package com.mmxlabs.models.ui.properties.ui;

import java.time.YearMonth;

import org.eclipse.jface.viewers.BaseLabelProvider;
import org.eclipse.jface.viewers.ILabelProvider;
import org.eclipse.swt.graphics.Image;

public class YearMonthFormatLabelProvider extends BaseLabelProvider implements ILabelProvider {

	@Override
	public Image getImage(final Object element) {
		return null;
	}

	@Override
	public String getText(final Object element) {
		if (element instanceof YearMonth) {
			final YearMonth yearMonth = (YearMonth) element;
			return String.format("%02d/%02d", yearMonth.getMonthValue(), yearMonth.getYear());
		} else {
			throw new IllegalArgumentException("Can only format YearMonth s!");
		}
	}
}
