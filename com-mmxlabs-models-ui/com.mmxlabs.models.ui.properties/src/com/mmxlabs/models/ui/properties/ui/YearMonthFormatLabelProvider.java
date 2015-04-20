/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2015
 * All rights reserved.
 */
package com.mmxlabs.models.ui.properties.ui;

import org.eclipse.jface.viewers.BaseLabelProvider;
import org.eclipse.jface.viewers.ILabelProvider;
import org.eclipse.swt.graphics.Image;
import org.joda.time.YearMonth;
import org.joda.time.format.DateTimeFormat;

public class YearMonthFormatLabelProvider extends BaseLabelProvider implements ILabelProvider {

	@Override
	public Image getImage(final Object element) {
		return null;
	}

	@Override
	public String getText(final Object element) {
		if (element instanceof YearMonth) {
			final YearMonth yearMonth = (YearMonth) element;
			return DateTimeFormat.shortDate().print(yearMonth);
		} else {
			throw new IllegalArgumentException("Can only format YearMonth s!");
		}
	}
}
