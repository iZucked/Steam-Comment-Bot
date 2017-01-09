/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
package com.mmxlabs.models.lng.cargo.ui.editorpart;

import org.eclipse.emf.ecore.EAnnotation;
import org.eclipse.emf.ecore.EDataType;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.EcorePackage;
import org.eclipse.emf.edit.command.SetCommand;
import org.eclipse.emf.edit.domain.EditingDomain;
import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.nebula.widgets.formattedtext.DoubleFormatter;
import org.eclipse.nebula.widgets.formattedtext.FloatFormatter;
import org.eclipse.nebula.widgets.formattedtext.FormattedTextCellEditor;
import org.eclipse.nebula.widgets.formattedtext.IntegerFormatter;
import org.eclipse.nebula.widgets.formattedtext.LongFormatter;
import org.eclipse.nebula.widgets.formattedtext.NumberFormatter;
import org.eclipse.swt.widgets.Composite;

import com.mmxlabs.models.mmxcore.MMXObject;
import com.mmxlabs.models.ui.tabular.manipulators.BasicAttributeManipulator;

/**
 * @author SG, hinton
 * 
 */
public class VolumeAttributeManipulator extends BasicAttributeManipulator {

	private final EDataType type;

	private NumberFormatter formatter;

	public VolumeAttributeManipulator(final EStructuralFeature feature, final EditingDomain editingDomain) {
		super(feature, editingDomain);
		type = (EDataType) feature.getEType();

		final EAnnotation annotation = feature.getEAnnotation("http://www.mmxlabs.com/models/ui/numberFormat");
		String format = null;

		if (annotation != null) {
			if (annotation.getDetails().containsKey("formatString")) {
				format = annotation.getDetails().get("formatString");
			}
		}

		if (type == EcorePackage.eINSTANCE.getELong()) {
			formatter = format == null ? new LongFormatter() : new LongFormatter(format);
		} else if (type == EcorePackage.eINSTANCE.getEInt()) {
			formatter = format == null ? new IntegerFormatter() : new IntegerFormatter(format);
		} else if (type == EcorePackage.eINSTANCE.getEFloat()) {
			formatter = format == null ? new FloatFormatter() : new FloatFormatter(format);
		} else if (type == EcorePackage.eINSTANCE.getEDouble()) {
			formatter = format == null ? new DoubleFormatter() : new DoubleFormatter(format);
		}
		if (format == null) {
			formatter.setFixedLengths(false, false);
		}

	}

	@Override
	public Comparable getComparable(final Object object) {
		Object object2 = super.getValue(object);
		if (object2 == null || object2 == SetCommand.UNSET_VALUE) {
			if ((object instanceof EObject) && (field.isUnsettable()) && !((EObject) object).eIsSet(field)) {
				object2 = (object instanceof MMXObject) ? ((MMXObject) object).getUnsetValue(field) : null;
			}
		}
		if (object2 instanceof Comparable) {
			return (Comparable) object2;
		}
		return -Integer.MAX_VALUE;
	}

	@Override
	protected CellEditor createCellEditor(final Composite c, final Object object) {
		final FormattedTextCellEditor editor = new FormattedTextCellEditor(c);

		editor.setFormatter(formatter);
		return editor;
	}

	@Override
	protected String renderSetValue(final Object container, final Object setValue) {
		if (setValue instanceof Number) {
			final Number number = (Number) setValue;
			final double n = number.doubleValue();
			return String.format("%sk", super.renderSetValue(container, (int) Math.round(n / 1000.0)));
		}

		return super.renderSetValue(container, setValue);
	}
}
