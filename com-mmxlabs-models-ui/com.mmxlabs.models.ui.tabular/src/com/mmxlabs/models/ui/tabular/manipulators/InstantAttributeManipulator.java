/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
package com.mmxlabs.models.ui.tabular.manipulators;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

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
public class InstantAttributeManipulator extends BasicAttributeManipulator {

	public InstantAttributeManipulator(final EStructuralFeature field, final ICommandHandler commandHandler) {
		super(field, commandHandler);
	}

	@Override
	public CellEditor createCellEditor(final Composite c, final Object object) {
		final FormattedTextCellEditor editor = new FormattedTextCellEditor(c) {
			@Override
			protected Object doGetValue() {
				final Object superValue = super.doGetValue();
				if (superValue == null) {
					return LocalDateTime.ofInstant(Instant.now(), ZoneOffset.UTC);
				}
				return superValue;
			}
		};
		editor.setFormatter(new LocalDateTimeTextFormatter());
		return editor;
	}

	@Override
	public String renderSetValue(final Object owner, final Object object) {

		final LocalDateTime ldt;
		if (object instanceof Instant instant) {
			ldt = LocalDateTime.ofInstant(instant, ZoneOffset.UTC);
		} else {
			ldt = LocalDateTime.ofInstant(Instant.now(), ZoneOffset.UTC);
		}
		final LocalDateTimeTextFormatter formatter = new LocalDateTimeTextFormatter();
		formatter.setValue(ldt);
		return formatter.getDisplayString();
	}

	@Override
	public Comparable<?> getComparable(final Object object) {
		final Object o = getValue(object);
		if (o instanceof Instant i) {
			return i;
		} else {
			return null;
		}
	}
}