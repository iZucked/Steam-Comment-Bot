/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
package com.mmxlabs.models.ui.tabular.manipulators;

import java.util.function.Supplier;

import org.eclipse.emf.ecore.EAnnotation;
import org.eclipse.emf.ecore.EDataType;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.EcorePackage;
import org.eclipse.emf.edit.command.SetCommand;
import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.nebula.widgets.formattedtext.DoubleFormatter;
import org.eclipse.nebula.widgets.formattedtext.FloatFormatter;
import org.eclipse.nebula.widgets.formattedtext.FormattedTextCellEditor;
import org.eclipse.nebula.widgets.formattedtext.IntegerFormatter;
import org.eclipse.nebula.widgets.formattedtext.LongFormatter;
import org.eclipse.nebula.widgets.formattedtext.NumberFormatter;
import org.eclipse.swt.widgets.Composite;

import com.mmxlabs.models.mmxcore.MMXObject;
import com.mmxlabs.models.ui.NumberFormatterFactory.ExtendedDoubleFormatter;
import com.mmxlabs.models.ui.NumberFormatterFactory.ExtendedFloatFormatter;
import com.mmxlabs.models.ui.NumberFormatterFactory.ExtendedIntegerFormatter;
import com.mmxlabs.models.ui.NumberFormatterFactory.ExtendedLongFormatter;
import com.mmxlabs.models.ui.editors.ICommandHandler;

/**
 * @author hinton
 * 
 */
public class NumericAttributeManipulator extends BasicAttributeManipulator {

	private static final String NUMBER_FORMAT_FEATURE_ANNOTATION = "http://www.mmxlabs.com/models/ui/numberFormat";
	private static final String DEFAULT_VALUE_ANNOTATION_KEY = "defaultValue";
	private static final String FORMAT_STRING_ANNOTATION_KEY = "formatString";
	private static final String SCALE_ANNOTATION_KEY = "scale";

	private final EDataType type;
	protected Object defaultValue;

	private NumberFormatter formatter;
	private int scale = 1;
	private Object input;

	public NumericAttributeManipulator(final EStructuralFeature feature, final ICommandHandler commandHandler) {
		super(feature, commandHandler);
		type = (EDataType) feature.getEType();

		final EAnnotation annotation = feature.getEAnnotation(NUMBER_FORMAT_FEATURE_ANNOTATION);
		String format = null;
		String defaultValueString = "0";

		if (annotation != null) {
			if (annotation.getDetails().containsKey(DEFAULT_VALUE_ANNOTATION_KEY)) {
				defaultValueString = annotation.getDetails().get(DEFAULT_VALUE_ANNOTATION_KEY);
			}

			if (annotation.getDetails().containsKey(FORMAT_STRING_ANNOTATION_KEY)) {
				format = annotation.getDetails().get(FORMAT_STRING_ANNOTATION_KEY);
			}

			if (annotation.getDetails().containsKey(SCALE_ANNOTATION_KEY)) {
				scale = Integer.parseInt(annotation.getDetails().get(SCALE_ANNOTATION_KEY));
			}
		}

		if (type == EcorePackage.eINSTANCE.getELong()) {
			defaultValue = Long.parseLong(defaultValueString);
			final LongFormatter inner = format == null ? new LongFormatter() : new LongFormatter(format);
			final Supplier<String> overrideStringSupplier = buildOverrideStringSupplier(inner);
			formatter = format == null ? new ExtendedLongFormatter(overrideStringSupplier) : new ExtendedLongFormatter(format, overrideStringSupplier);

		} else if (type == EcorePackage.eINSTANCE.getEInt()) {
			defaultValue = Integer.parseInt(defaultValueString);
			final IntegerFormatter inner = format == null ? new IntegerFormatter() : new IntegerFormatter(format);
			final Supplier<String> overrideStringSupplier = buildOverrideStringSupplier(inner);
			formatter = format == null ? new ExtendedIntegerFormatter(overrideStringSupplier) : new ExtendedIntegerFormatter(format, overrideStringSupplier);

		} else if (type == EcorePackage.eINSTANCE.getEFloat()) {
			defaultValue = Float.parseFloat(defaultValueString);
			final FloatFormatter inner = format == null ? new FloatFormatter() : new FloatFormatter(format);
			final Supplier<String> overrideStringSupplier = buildOverrideStringSupplier(inner);
			formatter = format == null ? new ExtendedFloatFormatter(overrideStringSupplier) : new ExtendedFloatFormatter(format, overrideStringSupplier);
		} else if (type == EcorePackage.eINSTANCE.getEDouble()) {
			defaultValue = Double.parseDouble(defaultValueString);
			final DoubleFormatter inner = format == null ? new DoubleFormatter() : new DoubleFormatter(format);
			final Supplier<String> overrideStringSupplier = buildOverrideStringSupplier(inner);
			formatter = format == null ? new ExtendedDoubleFormatter(overrideStringSupplier) : new ExtendedDoubleFormatter(format, overrideStringSupplier);
		}
		if (format == null) {
			formatter.setFixedLengths(false, false);
		}
	}

	private @NonNull Supplier<String> buildOverrideStringSupplier(final @NonNull NumberFormatter numberFormatter) {
		return () -> {
			if (input instanceof MMXObject mmxObject) {
				final Object v = mmxObject.getUnsetValue(field);
				numberFormatter.setValue(scale(v));
				return numberFormatter.getDisplayString();
			}
			return null;
		};
	}

	//
	@Override
	public Object getValue(final Object object) {
		this.input = object;
		final Object value = super.getValue(object);
		return scale(value);
	}

	@Override
	public void doSetValue(final Object object, final Object value) {
		super.doSetValue(object, descale(value));
	}

	@Override
	public Comparable<?> getComparable(final Object object) {
		Object object2 = super.getValue(object);
		if (object2 == null || object2 == SetCommand.UNSET_VALUE) {
			if ((object instanceof EObject eObj) && (field.isUnsettable()) && !eObj.eIsSet(field)) {
				object2 = object instanceof MMXObject mmxObject ? mmxObject.getUnsetValue(field) : null;
			}
		}
		if (object2 instanceof Comparable<?> comparable) {
			return comparable;
		}
		return -Integer.MAX_VALUE;
	}

	@Override
	protected String renderSetValue(final Object container, final Object setValue) {
		if (setValue == null) {
			return null;
		}
		formatter.setValue(setValue);
		// return setValue == null ? null :
		return formatter.getDisplayString();// setValue.toString();
	}

	@Override
	protected CellEditor createCellEditor(final Composite c, final Object object) {
		final FormattedTextCellEditor editor = new FormattedTextCellEditor(c);

		editor.setFormatter(formatter);
		return editor;
	}

	private Object scale(final Object internalValue) {

		if (scale == 1) {
			return internalValue;
		}

		if (internalValue instanceof Integer integer) {
			return integer.intValue() * (double) scale;
		} else if (internalValue instanceof Long longObj) {
			return longObj.longValue() * (double) scale;
		} else if (internalValue instanceof Float floatObj) {
			return floatObj.floatValue() * (double) scale;
		} else if (internalValue instanceof Double doubleObj) {
			return doubleObj.doubleValue() * scale;
		}
		return internalValue;
	}

	private Object descale(final Object displayValue) {
		if (scale == 1) {
			return displayValue;
		}

		if (displayValue instanceof Integer integer) {
			return integer.intValue() / (double) scale;
		} else if (displayValue instanceof Long longObj) {
			return longObj.longValue() / (double) scale;
		} else if (displayValue instanceof Float floatObj) {
			return floatObj.floatValue() / (double) scale;
		} else if (displayValue instanceof Double doubleObj) {
			return doubleObj.doubleValue() / scale;
		}
		return displayValue;
	}
}
