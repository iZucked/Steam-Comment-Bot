/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
package com.mmxlabs.models.ui.properties.views;

import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jface.viewers.ColumnLabelProvider;
import org.eclipse.jface.viewers.IColorProvider;
import org.eclipse.swt.graphics.Color;

import com.mmxlabs.models.ui.properties.DetailProperty;

public class DetailPropertyLabelProvider extends ColumnLabelProvider {

	private final DetailPropertyColumnType columnType;

	public DetailPropertyLabelProvider(@NonNull final DetailPropertyColumnType columnType) {
		this.columnType = columnType;
	}

	@Override
	public String getText(final Object element) {

		if (element instanceof DetailProperty) {
			final DetailProperty dp = (DetailProperty) element;
			String pre = dp.getUnitsPrefix() != null ? dp.getUnitsPrefix() : "";
			String val = dp.format() != null ? dp.format() : "";
			final String post = dp.getUnitsSuffix() != null ? dp.getUnitsSuffix() : "";
			switch (columnType) {
			case ATTRIBUTE:
				return dp.getName();
			case ATTRIBUTE_WITH_DIMENSIONS:
				return dp.getName() + (!(pre + post).isEmpty() ? " (" + pre + post + ")" : "");
			case UNIT:
				return pre + post;
			case VALUE:
				return val;
			case DIMENSIONED_VALUE:
				if (!pre.isEmpty() && val.startsWith("-")) {
					val = val.substring(1);
					pre = "-" + pre;
				}
				return pre + val + post;
			default:
				break;
			}
		}

		return super.getText(element);
	}

	@Override
	public Color getForeground(final Object element) {
		if (element instanceof DetailProperty) {
			final DetailProperty dp = (DetailProperty) element;
			if (dp.getLabelProvider() instanceof IColorProvider) {
				final IColorProvider colorProvider = (IColorProvider) dp.getLabelProvider();
				return colorProvider.getForeground(dp);
			}
		}
		return super.getForeground(element);
	}

	@Override
	public Color getBackground(final Object element) {
		if (element instanceof DetailProperty) {
			final DetailProperty dp = (DetailProperty) element;
			if (dp.getLabelProvider() instanceof IColorProvider) {
				final IColorProvider colorProvider = (IColorProvider) dp.getLabelProvider();
				return colorProvider.getBackground(dp);
			}
		}
		return super.getBackground(element);
	}

	@Override
	public String getToolTipText(final Object element) {

		if (element instanceof DetailProperty) {
			final DetailProperty detailProperty = (DetailProperty) element;
			return detailProperty.getDescription();
		}

		return super.getToolTipText(element);
	}

}
