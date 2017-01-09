/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
package com.mmxlabs.models.ui.tabular.manipulators;

import java.time.LocalDate;

import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.edit.domain.EditingDomain;
import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.nebula.widgets.formattedtext.FormattedTextCellEditor;
import org.eclipse.swt.widgets.Composite;

import com.mmxlabs.models.datetime.ui.formatters.LocalDateTextFormatter;

/**
 * A {@link LocalDate} attribute manipulator which uses the {@link FormattedTextCellEditor} from nebula.
 * 
 * @author Simon Goodall
 * 
 */
public class LocalDateAttributeManipulator extends BasicAttributeManipulator {

	public LocalDateAttributeManipulator(final EStructuralFeature field, final EditingDomain editingDomain) {
		super(field, editingDomain);
	}

	@Override
	public CellEditor createCellEditor(final Composite c, final Object object) {
		final FormattedTextCellEditor editor = new FormattedTextCellEditor(c) {
			@Override
			protected Object doGetValue() {
				final Object superValue = super.doGetValue();
				if (superValue == null) {
					return LocalDate.now();
				}
				return superValue;
			}
		};
		editor.setFormatter(new LocalDateTextFormatter());
		return editor;
	}
	
	@Override
	public Object getValue(Object object) {
		return super.getValue(object);
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
		if (o instanceof LocalDate) {
			return (LocalDate) o;
		} else {
			return null;
		}
	}
	
 

}