/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2013
 * All rights reserved.
 */
package com.mmxlabs.models.ui.dates;

import java.text.DateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.edit.domain.EditingDomain;
import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.nebula.widgets.formattedtext.DateFormatter;
import org.eclipse.nebula.widgets.formattedtext.FormattedTextCellEditor;
import org.eclipse.swt.widgets.Composite;

import com.mmxlabs.models.ui.tabular.manipulators.BasicAttributeManipulator;

/**
 * A new date attribute manipulator which uses the {@link FormattedTextCellEditor} from nebula.
 * 
 * @author hinton
 * 
 */
public class DateAttributeManipulator extends BasicAttributeManipulator {
	private boolean showTimezone;

	public DateAttributeManipulator(final EStructuralFeature field, final EditingDomain editingDomain) {
		this(field, editingDomain, false);
	}

	public DateAttributeManipulator(final EStructuralFeature field, final EditingDomain editingDomain, final boolean showTimezone) {
		super(field, editingDomain);
		this.showTimezone = showTimezone;
	}

	@Override
	public CellEditor createCellEditor(final Composite c, final Object object) {
		final FormattedTextCellEditor editor = new FormattedTextCellEditor(c) {
			@Override
			protected Object doGetValue() {
				final Object superValue = super.doGetValue();
				if (superValue == null) {
					final Calendar cal = Calendar.getInstance(LocalDateUtil.getTimeZone(object, (EAttribute) field));
					cal.set(Calendar.MILLISECOND, 0);
					cal.set(Calendar.SECOND, 0);
					cal.set(Calendar.MINUTE, 0);
					cal.set(Calendar.HOUR, 0);
					return cal.getTime();
				}
				return superValue;
			}
		};
		final DateFormatter formatter = new DateFormatter();
		formatter.setTimeZone(LocalDateUtil.getTimeZone(object, (EAttribute) field));
		editor.setFormatter(formatter);
		return editor;
	}

	@Override
	public String renderSetValue(final Object owner, final Object object) {
		if (!(object instanceof Date)) {
			return "";
		}

		final Date date = (Date) object;

		final Calendar calendar = Calendar.getInstance(LocalDateUtil.getTimeZone(owner, (EAttribute) field));
		calendar.setTime(date);

		final DateFormat df = DateFormat.getDateInstance(DateFormat.SHORT);
		df.setCalendar(calendar);

		String str = df.format(calendar.getTime());
		if (isShowTimezone()) {
			str += " (" + calendar.getTimeZone().getDisplayName(false, TimeZone.SHORT) + ")";
		}
		return str;
	}

	@Override
	public Comparable getComparable(final Object object) {
		final Object o = getValue(object);
		if (o instanceof Date) {
			return (Date) o;
		} else {
			return null;
		}
	}

	public boolean isShowTimezone() {
		return showTimezone;
	}

	public void setShowTimezone(final boolean showTimezone) {
		this.showTimezone = showTimezone;
	}
}