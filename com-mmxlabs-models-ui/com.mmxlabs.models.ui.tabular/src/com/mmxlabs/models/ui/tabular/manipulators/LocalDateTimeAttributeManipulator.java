/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
package com.mmxlabs.models.ui.tabular.manipulators;

import java.time.LocalDate;
import java.time.LocalDateTime;

import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.nebula.widgets.formattedtext.FormattedTextCellEditor;
import org.eclipse.swt.widgets.Composite;

import com.mmxlabs.models.ui.date.LocalDateTimeTextFormatter;
import com.mmxlabs.models.ui.editors.ICommandHandler;

/**
 * A {@link LocalDate} attribute manipulator which uses the {@link FormattedTextCellEditor} from nebula.
 * 
 * @author Simon Goodall
 * 
 */
public class LocalDateTimeAttributeManipulator extends BasicAttributeManipulator {
	
	private final LocalDateTimeTextFormatter formatter;
	
	public LocalDateTimeAttributeManipulator(final EStructuralFeature field, final ICommandHandler commandHandler, final LocalDateTimeTextFormatter formatter) {
		super(field, commandHandler);
		this.formatter = formatter;
	}

	public LocalDateTimeAttributeManipulator(final EStructuralFeature field, final ICommandHandler commandHandler) {
		super(field, commandHandler);
		formatter = new LocalDateTimeTextFormatter();
	}

	@Override
	public CellEditor createCellEditor(final Composite c, final Object object) {
		final FormattedTextCellEditor editor = new FormattedTextCellEditor(c) {
			@Override
			protected Object doGetValue() {
				final Object superValue = super.doGetValue();
				if (superValue == null) {
					return LocalDateTime.now().withMinute(0).withSecond(0).withNano(0);
				}
				return superValue;
			}
		};
		editor.setFormatter(formatter);
		return editor;
	}

	@Override
	public String renderSetValue(final Object owner, final Object object) {
		formatter.setValue(object);
		return formatter.getDisplayString();
	}

	@Override
	public Comparable<?> getComparable(final Object object) {
		final Object o = getValue(object);
		if (o instanceof LocalDateTime ldt) {
			return ldt;
		} else {
			return null;
		}
	}
}