package com.mmxlabs.models.ui.properties.views;

import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jface.viewers.ColumnLabelProvider;

import com.mmxlabs.models.ui.properties.DetailProperty;

public class DetailPropertyLabelProvider extends ColumnLabelProvider {

	private final DetailPropertyColumnType columnType;

	public DetailPropertyLabelProvider(@NonNull final DetailPropertyColumnType columnType) {
		this.columnType = columnType;
	}

	@Override
	public String getText(Object element) {

		if (element instanceof DetailProperty) {
			final DetailProperty detailProperty = (DetailProperty) element;
			switch (columnType) {
			case ATTRIBUTE:
				return detailProperty.getName();
			case UNITS:
				return detailProperty.getUnits();
			case VALUE:
				return detailProperty.format();
			default:
				break;
			}
		}

		return super.getText(element);
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
