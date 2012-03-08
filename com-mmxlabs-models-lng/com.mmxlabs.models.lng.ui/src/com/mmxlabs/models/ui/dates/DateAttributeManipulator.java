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

import com.mmxlabs.models.ui.tabular.BasicAttributeManipulator;

/**
 * A new date attribute manipulator which uses the {@link FormattedTextCellEditor} from nebula.
 * @author hinton
 *
 */
public class DateAttributeManipulator extends BasicAttributeManipulator {
	public DateAttributeManipulator(final EStructuralFeature field,
			final EditingDomain editingDomain) {
		super(field, editingDomain);
	}

	@Override
	public CellEditor createCellEditor(Composite c, Object object) {
		final FormattedTextCellEditor editor = new FormattedTextCellEditor(c);
		final DateFormatter formatter = new DateFormatter();
		formatter.setTimeZone(LocalDateUtil.getTimeZone(object, (EAttribute) field));
		editor.setFormatter(formatter);
		return editor;
	}
	
	@Override
	public String renderSetValue(Object object) {
		if (!(object instanceof Date)) {
			return "";
		}
		
		final Date date = (Date) object;
		
		final Calendar calendar = Calendar.getInstance(LocalDateUtil.getTimeZone(object, (EAttribute) field));
		calendar.setTime(date);
		
		final DateFormat df = DateFormat.getDateInstance(DateFormat.SHORT);
		df.setCalendar(calendar);
		
		return df.format(calendar.getTime()) + " ("
				+ calendar.getTimeZone().getDisplayName(false, TimeZone.SHORT)
				+ ")";
	}
}