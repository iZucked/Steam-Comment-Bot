/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2015
 * All rights reserved.
 */
package com.mmxlabs.lingo.reports.diff;

import org.eclipse.core.databinding.observable.map.IObservableMap;
import org.eclipse.jface.databinding.viewers.ObservableMapCellLabelProvider;
import org.eclipse.jface.viewers.ViewerCell;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.FontData;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.PlatformUI;

import com.mmxlabs.lingo.reports.views.schedule.model.CycleGroup;
import com.mmxlabs.lingo.reports.views.schedule.model.UserGroup;

public class MixedDeltaCellLabelProvider extends ObservableMapCellLabelProvider {

	private static final String format = "%,d";
	private Font systemFont;
	private Font italicFont;

	public MixedDeltaCellLabelProvider(IObservableMap[] attributeMaps) {
		super(attributeMaps);
		Display display = PlatformUI.getWorkbench().getDisplay();
		systemFont = display.getSystemFont();
		FontData fontData = systemFont.getFontData()[0];
		italicFont = new Font(display, new FontData(fontData.getName(), fontData.getHeight(), SWT.ITALIC | SWT.BOLD));
	}

	@Override
	public void dispose() {
		italicFont.dispose();
		super.dispose();
	}

	@Override
	public void update(final ViewerCell cell) {
		final Object object = cell.getElement();
		if (object instanceof CycleGroup) {
			final CycleGroup group = (CycleGroup) object;
			final int delta = group.getDelta();
			cell.setText(String.format(format, delta));
			cell.setFont(systemFont);
		} else if (object instanceof UserGroup) {
			final UserGroup userGroup = (UserGroup) object;
			final int delta = userGroup.getDelta();
			cell.setText(String.format(format, delta));
			cell.setFont(italicFont);
		} else {
			cell.setText("");
		}

	}

}
