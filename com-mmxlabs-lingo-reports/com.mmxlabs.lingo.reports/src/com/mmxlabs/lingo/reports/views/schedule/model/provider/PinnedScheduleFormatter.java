/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
package com.mmxlabs.lingo.reports.views.schedule.model.provider;

import org.eclipse.swt.graphics.Image;

import com.mmxlabs.lingo.reports.views.schedule.model.Row;
import com.mmxlabs.models.ui.tabular.BaseFormatter;
import com.mmxlabs.models.ui.tabular.IImageProvider;

public class PinnedScheduleFormatter extends BaseFormatter implements IImageProvider {

	private Image pinImage;

	public PinnedScheduleFormatter(Image pinImage) {
		this.pinImage = pinImage;
	}

	@Override
	public Image getImage(Object element) {
		if (element instanceof Row) {
			if (((Row) element).isReference()) {
				return pinImage;
			}
		}

		return null;
	}

}
