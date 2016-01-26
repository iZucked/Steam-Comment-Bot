/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
package com.mmxlabs.models.ui.tabular.manipulators;

import java.time.LocalDate;
import java.time.YearMonth;

import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.edit.domain.EditingDomain;
import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.nebula.widgets.formattedtext.FormattedTextCellEditor;
import org.eclipse.swt.widgets.Composite;

import com.mmxlabs.models.datetime.ui.formatters.YearMonthTextFormatter;

/**
 * A {@link LocalDate} attribute manipulator which uses the {@link FormattedTextCellEditor} from nebula.
 * 
 * @author Simon Goodall
 * 
 */
public class YearMonthAttributeManipulator extends BasicAttributeManipulator {

	public YearMonthAttributeManipulator(final EStructuralFeature field, final EditingDomain editingDomain) {
		super(field, editingDomain);
	}

	@Override
	public CellEditor createCellEditor(final Composite c, final Object object) {
		final FormattedTextCellEditor editor = new FormattedTextCellEditor(c){
			@Override
			protected Object doGetValue() {
				final Object superValue = super.doGetValue();
				if (superValue == null) {
					return YearMonth.now();
				}
				return superValue;
			}
		};
		editor.setFormatter(new YearMonthTextFormatter());
		return editor;
	}

	@Override
	public String renderSetValue(final Object owner, final Object object) {
		final YearMonthTextFormatter formatter = new YearMonthTextFormatter();
		formatter.setValue(object);
		return formatter.getDisplayString();
	}

	@Override
	public Comparable<?> getComparable(final Object object) {
		final Object o = getValue(object);
		if (o instanceof YearMonth) {
			return (YearMonth) o;
		} else {
			return null;
		}
	}
	

//	 


}