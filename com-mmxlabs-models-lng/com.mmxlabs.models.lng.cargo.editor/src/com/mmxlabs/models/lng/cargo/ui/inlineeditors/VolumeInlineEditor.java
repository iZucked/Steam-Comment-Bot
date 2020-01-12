/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2020
 * All rights reserved.
 */
package com.mmxlabs.models.lng.cargo.ui.inlineeditors;

import java.util.Objects;
import java.util.function.Supplier;

import org.eclipse.emf.databinding.EMFDataBindingContext;
import org.eclipse.emf.ecore.EAnnotation;
import org.eclipse.emf.ecore.EDataType;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.EcorePackage;
import org.eclipse.nebula.widgets.formattedtext.DoubleFormatter;
import org.eclipse.nebula.widgets.formattedtext.FloatFormatter;
import org.eclipse.nebula.widgets.formattedtext.FormattedText;
import org.eclipse.nebula.widgets.formattedtext.IntegerFormatter;
import org.eclipse.nebula.widgets.formattedtext.LongFormatter;
import org.eclipse.nebula.widgets.formattedtext.NumberFormatter;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.events.FocusEvent;
import org.eclipse.swt.events.FocusListener;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.forms.widgets.FormToolkit;

import com.mmxlabs.models.lng.cargo.CargoPackage;
import com.mmxlabs.models.mmxcore.MMXObject;
import com.mmxlabs.models.ui.NumberFormatterFactory.ExtendedDoubleFormatter;
import com.mmxlabs.models.ui.NumberFormatterFactory.ExtendedFloatFormatter;
import com.mmxlabs.models.ui.NumberFormatterFactory.ExtendedIntegerFormatter;
import com.mmxlabs.models.ui.NumberFormatterFactory.ExtendedLongFormatter;
import com.mmxlabs.models.ui.editors.impl.UnsettableInlineEditor;

/**
 * TODO look at nebula formattedtext
 * 
 * @author hinton
 * 
 */
public class VolumeInlineEditor extends UnsettableInlineEditor implements ModifyListener, DisposeListener {
	private final EDataType type;

	private FormattedText text;
	private Object defaultValue;
	private NumberFormatter formatter;
	private int scale = 1;
	private String unitPrefix = null;
	private String unitSuffix = null;

	private Label unitPrefixLabel;
	private Label unitSuffixLabel;

	public VolumeInlineEditor(final EStructuralFeature feature) {
		super(feature);
		type = (EDataType) feature.getEType();

		final EAnnotation annotation = feature.getEAnnotation("http://www.mmxlabs.com/models/ui/numberFormat");
		String format = null;
		String defaultValueString = "0";

		if (annotation != null) {
			if (annotation.getDetails().containsKey("defaultValue")) {
				defaultValueString = annotation.getDetails().get("defaultValue");
			}

			if (annotation.getDetails().containsKey("formatString")) {
				format = annotation.getDetails().get("formatString");
			}

			if (annotation.getDetails().containsKey("scale")) {
				scale = Integer.parseInt(annotation.getDetails().get("scale"));
			}

			if (annotation.getDetails().containsKey("unitSuffix")) {
				unitSuffix = annotation.getDetails().get("unitSuffix");
			} else if (annotation.getDetails().containsKey("unit")) {
				unitSuffix = annotation.getDetails().get("unit");
			}
			if (annotation.getDetails().containsKey("unitPrefix")) {
				unitPrefix = annotation.getDetails().get("unitPrefix");
			}
		}

		if (type == EcorePackage.eINSTANCE.getELong()) {
			defaultValue = Long.parseLong(defaultValueString);
			final LongFormatter inner = format == null ? new LongFormatter() : new LongFormatter(format);
			final Supplier<String> overrideStringSupplier = () -> {
				if (input instanceof MMXObject) {
					final Object v = ((MMXObject) input).getUnsetValue(getFeature());
					inner.setValue(scale(v));
					return inner.getDisplayString();
				}
				return null;
			};
			formatter = format == null ? new ExtendedLongFormatter(overrideStringSupplier) : new ExtendedLongFormatter(format, overrideStringSupplier);

		} else if (type == EcorePackage.eINSTANCE.getEInt()) {
			defaultValue = Integer.parseInt(defaultValueString);
			final IntegerFormatter inner = format == null ? new IntegerFormatter() : new IntegerFormatter(format);
			final Supplier<String> overrideStringSupplier = () -> {
				if (input instanceof MMXObject) {
					final Object v = ((MMXObject) input).getUnsetValue(getFeature());

					if (feature == CargoPackage.Literals.SLOT__MAX_QUANTITY) {
						if (Objects.equals(0, v)) {
							return "-";
						}
					}
					if (Objects.equals(Integer.MAX_VALUE, v)) {
						return "-";
					}

					inner.setValue(scale(v));
					return inner.getDisplayString();
				}
				return null;
			};
			formatter = format == null ? new ExtendedIntegerFormatter(overrideStringSupplier) : new ExtendedIntegerFormatter(format, overrideStringSupplier);

		} else if (type == EcorePackage.eINSTANCE.getEFloat()) {
			defaultValue = Float.parseFloat(defaultValueString);
			final FloatFormatter inner = format == null ? new FloatFormatter() : new FloatFormatter(format);
			final Supplier<String> overrideStringSupplier = () -> {
				if (input instanceof MMXObject) {
					final Object v = ((MMXObject) input).getUnsetValue(getFeature());
					inner.setValue(scale(v));
					return inner.getDisplayString();
				}
				return null;
			};
			formatter = format == null ? new ExtendedFloatFormatter(overrideStringSupplier) : new ExtendedFloatFormatter(format, overrideStringSupplier);
		} else if (type == EcorePackage.eINSTANCE.getEDouble()) {
			defaultValue = Double.parseDouble(defaultValueString);
			final DoubleFormatter inner = format == null ? new DoubleFormatter() : new DoubleFormatter(format);
			final Supplier<String> overrideStringSupplier = () -> {
				if (input instanceof MMXObject) {
					final Object v = ((MMXObject) input).getUnsetValue(getFeature());
					inner.setValue(scale(v));
					return inner.getDisplayString();
				}
				return null;
			};
			formatter = format == null ? new ExtendedDoubleFormatter(overrideStringSupplier) : new ExtendedDoubleFormatter(format, overrideStringSupplier);
		}
		if (format == null) {
			formatter.setFixedLengths(false, false);
		}
	}

	@Override
	public Control createControl(Composite parent, EMFDataBindingContext dbc, FormToolkit toolkit) {
		isOverridable = false;
		EAnnotation eAnnotation = feature.getEContainingClass().getEAnnotation("http://www.mmxlabs.com/models/featureOverride");
		if (eAnnotation == null) {
			eAnnotation = feature.getEContainingClass().getEAnnotation("http://www.mmxlabs.com/models/featureOverrideByContainer");
		}
		if (eAnnotation != null) {
			for (EStructuralFeature f : feature.getEContainingClass().getEAllAttributes()) {
				if (f.getName().equals(feature.getName() + "Override")) {
					isOverridable = true;
				}
			}
			if (feature.isUnsettable()) {
				isOverridable = true;
			}
		}
		return super.createControl(parent, dbc, toolkit);
	}

	@Override
	public Control createValueControl(Composite parent) {
		if (unitPrefix != null || unitSuffix != null) {
			// final Composite sub = new Composite(parent, SWT.NONE);
			final Composite sub = toolkit.createComposite(parent, SWT.NONE);
			final GridLayout layout = new GridLayout(2, false);
			layout.marginHeight = layout.marginWidth = 0;
			sub.setLayout(layout);
			parent = sub;
		}

		if (unitPrefix != null) {
			unitPrefixLabel = toolkit.createLabel(parent, unitPrefix, SWT.NONE);
		}

		text = new FormattedText(parent, SWT.BORDER);
		text.setFormatter(formatter);
		text.setValue(defaultValue);

		text.getControl().addFocusListener(new FocusListener() {

			@Override
			public void focusLost(final FocusEvent e) {
				updateBackgroundColour(text.isEmpty());
			}

			@Override
			public void focusGained(final FocusEvent e) {
				updateBackgroundColour(false);

			}
		});
		text.getControl().addModifyListener(this);
		text.getControl().addDisposeListener(this);

		// Hook into Forms stuff
		toolkit.adapt(text.getControl(), true, false);

		if (unitSuffix != null) {
			unitSuffixLabel = toolkit.createLabel(parent, unitSuffix, SWT.NONE);
		}
		if (unitPrefix != null || unitSuffix != null) {
			text.getControl().setLayoutData(new GridData(GridData.FILL_BOTH));
			return parent;
		} else {
			return text.getControl();
		}
	}

	@Override
	protected void updateValueDisplay(final Object value) {
		if (text != null) {
			boolean a = !valueIsSet();
			updateBackgroundColour(a || text.isEmpty());
			text.setValue(scale(value));
		}
	}

	@Override
	protected Object getInitialUnsetValue() {
		return defaultValue;
	}

	@Override
	public void widgetDisposed(final DisposeEvent e) {
		if (e.getSource() instanceof Text) {
			final Text t = (Text) e.getSource();
			t.removeModifyListener(this);
			t.removeDisposeListener(this);
		}
		text = null;
	}

	@Override
	public void modifyText(final ModifyEvent e) {
		updateBackgroundColour(text.isEmpty());
		if (text.isEmpty()) {
			unsetValue();
		} else {
			if (text.isValid()) {
				doSetValue(descale(text.getValue()), false);
			}
		}
	}

	private Object scale(final Object internalValue) {
		if (internalValue instanceof Integer) {
			return ((Integer) internalValue).intValue() * scale;
		} else if (internalValue instanceof Long) {
			return ((Long) internalValue).longValue() * scale;
		} else if (internalValue instanceof Float) {
			return ((Float) internalValue).floatValue() * scale;
		} else if (internalValue instanceof Double) {
			return ((Double) internalValue).doubleValue() * scale;
		}
		return internalValue;
	}

	private Object descale(final Object displayValue) {
		if (displayValue instanceof Integer) {
			return ((Integer) displayValue).intValue() / scale;
		} else if (displayValue instanceof Long) {
			return ((Long) displayValue).longValue() / scale;
		} else if (displayValue instanceof Float) {
			return ((Float) displayValue).floatValue() / scale;
		} else if (displayValue instanceof Double) {
			return ((Double) displayValue).doubleValue() / scale;
		}
		return displayValue;
	}

	@Override
	protected void setControlsEnabled(final boolean enabled) {
		final boolean controlsEnabled = !isFeatureReadonly() && enabled;

		if (text != null) {
			boolean a = !valueIsSet();
			updateBackgroundColour(a || text.isEmpty());
			text.getControl().setEnabled(controlsEnabled);
		}
		if (unitPrefixLabel != null) {
			unitPrefixLabel.setEnabled(controlsEnabled);
		}
		if (unitSuffixLabel != null) {
			unitSuffixLabel.setEnabled(controlsEnabled);
		}
		super.setControlsEnabled(controlsEnabled);
	}

	private void updateBackgroundColour(final boolean controlEmpty) {
		if (controlEmpty) {
			if (canOverride()) {
				text.getControl().setBackground(Display.getDefault().getSystemColor(SWT.COLOR_WIDGET_LIGHT_SHADOW));
			} else {
				text.getControl().setBackground(Display.getDefault().getSystemColor(SWT.COLOR_WHITE));
			}
		} else {
			text.getControl().setBackground(Display.getDefault().getSystemColor(SWT.COLOR_WHITE));
		}
	}

	@Override
	protected void setControlsVisible(final boolean visible) {
		if (text != null) {
			text.getControl().setVisible(visible);
		}
		if (unitPrefixLabel != null) {
			unitPrefixLabel.setVisible(visible);
		}
		if (unitSuffixLabel != null) {
			unitSuffixLabel.setVisible(visible);
		}
		super.setControlsVisible(visible);
	}
}
