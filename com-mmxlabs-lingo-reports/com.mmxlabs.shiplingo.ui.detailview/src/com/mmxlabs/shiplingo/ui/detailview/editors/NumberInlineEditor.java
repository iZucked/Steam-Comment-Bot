/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2012
 * All rights reserved.
 */
package com.mmxlabs.shiplingo.ui.detailview.editors;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.text.ParseException;

import org.eclipse.emf.ecore.EDataType;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.EcorePackage;
import org.eclipse.emf.edit.domain.EditingDomain;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.VerifyEvent;
import org.eclipse.swt.events.VerifyListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;

import scenario.ScenarioPackage;

import com.mmxlabs.lngscheduler.emf.extras.EMFPath;

public class NumberInlineEditor extends UnsettableInlineEditor {
	private final EDataType type;

	private final static class NumberTypes {
		public final static EDataType l = EcorePackage.eINSTANCE.getELong();
		public final static EDataType f = EcorePackage.eINSTANCE.getEFloat();
		public final static EDataType d = EcorePackage.eINSTANCE.getEDouble();
		public final static EDataType i = EcorePackage.eINSTANCE.getEInt();
		public final static EDataType p = ScenarioPackage.eINSTANCE.getPercentage();

		public static NumberFormat getFormatter(final EDataType type) {
			final NumberFormat formatter;

			if (type == l) {
				formatter = DecimalFormat.getIntegerInstance();
			} else if (type == i) {
				formatter = DecimalFormat.getIntegerInstance();
			} else if (type == p) {
				formatter = DecimalFormat.getNumberInstance();
			} else if (type == f) {
				formatter = DecimalFormat.getNumberInstance();
			} else if (type == d) {
				formatter = DecimalFormat.getNumberInstance();
			} else {
				throw new RuntimeException("Unknown type of numeric field");
			}

			formatter.setMaximumFractionDigits(getDigits(type));
			return formatter;
		}

		public static Object toNumber(final EDataType type, final String textValue) throws ParseException {

			final NumberFormat formatter = getFormatter(type);

			final Object num = formatter.parse(textValue);

			if (type == p) {
				if (num instanceof Number) {
					double d = ((Number) num).doubleValue();
					d /= 100.0;
					return d;
				}
			}

			return num;
		}

		public static String toString(final EDataType type, final Object value) {

			final NumberFormat formatter = getFormatter(type);

			if (type == p) {
				if (value instanceof Number) {
					double d = ((Number) value).doubleValue();
					d *= 100.0;
					return formatter.format(d);
				}
			}
			return formatter.format(value);
		}

		public static int getDigits(final EDataType type) {
			if ((type == l) || (type == i)) {
				return 0;
			} else if ((type == f) || (type == d)) {
				return 2;
			} else if (type == p) {
				return 1;
			} else {
				return 0;
			}
		}

		public static Object getDefaultValue(final EDataType type) {
			if (type == i) {
				return 0;
			}
			if (type == l) {
				return 0l;
			}
			if ((type == d) || (type == p)) {
				return 0d;
			}
			if (type == f) {
				return 0f;
			}

			throw new RuntimeException("Unknown type for number : " + type.getName());
		}
	}

	public NumberInlineEditor(final EMFPath path, final EStructuralFeature feature, final EditingDomain editingDomain, final ICommandProcessor processor) {
		super(path, feature, editingDomain, processor);
		type = (EDataType) feature.getEType();
	}

	private Text text;

	@Override
	public Control createValueControl(final Composite parent) {
		final Composite box = new Composite(parent, SWT.NONE);
		final GridLayout boxLayout = new GridLayout(1, false);
		boxLayout.marginHeight = 0;
		boxLayout.marginWidth = 0;
		box.setLayout(boxLayout);
		final Text spinner = new Text(box, SWT.BORDER);

		// Verifier to restrict value text entry
		spinner.addVerifyListener(new VerifyListener() {

			/**
			 * Locale equivalent of period character
			 */
			final char period = DecimalFormatSymbols.getInstance().getDecimalSeparator();

			@Override
			public void verifyText(final VerifyEvent e) {

				// Stage 1: Verify Key presses
				if ((e.character >= '0') && (e.character <= '9')) {
					e.doit = true;
				} else if (e.character == period) {
					// Allow single period
					e.doit = !spinner.getText().contains("" + period);
					// Handle Backspace
				} else if (e.character == '\b') {
					e.doit = true;
					// Always allow it
					return;
				} else {
					e.doit = false;
				}

				// Step 2. parse number.
				if (e.doit) {
					// Check number of dp is valid
					final String newText = spinner.getText() + e.character;
					final int idx = newText.indexOf(period);
					if (idx != -1) {
						final int digits = NumberTypes.getDigits(type);
						if (digits == 0) {
							e.doit = false;
							return;
						}

						// Handle .xxx case
						if (idx == 0) {
							if ((newText.length() - 1) > digits) {
								e.doit = false;
								return;
							}
							// Handle xxx. case
						} else if (idx == (newText.length() - 1)) {
							// Ok
						} else {
							final String[] nums = newText.split(period == '.' ? "\\." : "" + period);
							if (nums[1].length() > digits) {
								e.doit = false;
								return;
							}
						}
					}
					try {
						// Finally attempt to parse the number
						NumberTypes.toNumber(type, newText);

					} catch (final ParseException e1) {
						e.doit = false;
						throw new RuntimeException(e1);
					}
				}
			}
		});

		spinner.addModifyListener(new ModifyListener() {

			@Override
			public void modifyText(final ModifyEvent e) {
				final String text = spinner.getText();
				if (text.isEmpty()) {
					unsetValue();
				} else {
					try {
						doSetValue(NumberTypes.toNumber(type, text));
					} catch (final ParseException e1) {
						// We do not expect to get here - the verify listener should have taken care of ensuring valid input
						throw new RuntimeException(e1);
					}
				}
			}
		});

		this.text = spinner;
		spinner.setLayoutData(new GridData(GridData.FILL_BOTH));

		if (type == NumberTypes.p) {
			boxLayout.numColumns = 2;
			new Label(box, SWT.NONE).setText("%");
		}

		return box;
	}

	@Override
	protected void updateValueDisplay(final Object value) {
		if (text.isDisposed()) {
			return;
		}
		if (value == null) {
			text.setText("");
		} else {
			text.setText(NumberTypes.toString(type, value));
		}
	}

	@Override
	protected Object getInitialUnsetValue() {
		return NumberTypes.getDefaultValue(type);
	}

	@Override
	protected Object getValue() {
		// TODO Auto-generated method stub
		return super.getValue();
	}
}
