/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
package com.mmxlabs.models.ui.tabular.manipulators;

import java.time.LocalDate;
import java.time.ZonedDateTime;

import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.edit.domain.EditingDomain;
import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.nebula.widgets.formattedtext.FormattedTextCellEditor;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.DateTime;

import com.mmxlabs.models.datetime.ui.formatters.LocalDateTextFormatter;

/**
 * A {@link LocalDate} attribute manipulator which uses the {@link FormattedTextCellEditor} from nebula.
 * 
 * @author Simon Goodall
 * 
 */
public class DateTimeAttributeManipulator extends BasicAttributeManipulator {

	public DateTimeAttributeManipulator(final EStructuralFeature field, final EditingDomain editingDomain) {
		super(field, editingDomain);
	}

	@Override
	public CellEditor createCellEditor(final Composite c, final Object object) {
		final FormattedTextCellEditor editor = new FormattedTextCellEditor(c);
		editor.setFormatter(new LocalDateTextFormatter());
		return editor;
	}

	@Override
	public String renderSetValue(final Object owner, final Object object) {
		final LocalDateTextFormatter formatter = new LocalDateTextFormatter();
		formatter.setValue(object);
		return formatter.getDisplayString();
	}

	@Override
	public Comparable<?> getComparable(final Object object) {
		final Object o = getValue(object);
		if (o instanceof DateTime) {
			return (ZonedDateTime) o;
		} else {
			return null;
		}
	}
	

 


}