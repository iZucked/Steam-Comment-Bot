/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2011
 * All rights reserved.
 */
package scenario.presentation.cargoeditor.detailview;

import org.eclipse.emf.ecore.EDataType;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.EcorePackage;
import org.eclipse.emf.edit.domain.EditingDomain;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Spinner;

import scenario.ScenarioPackage;
import scenario.presentation.cargoeditor.detailview.EObjectDetailView.ICommandProcessor;

import com.mmxlabs.lngscheduler.emf.extras.EMFPath;

public class NumberInlineEditor extends UnsettableInlineEditor {
	private EDataType type;

	private final static class NumberTypes {
		public final static EDataType l = EcorePackage.eINSTANCE.getELong();
		public final static EDataType f = EcorePackage.eINSTANCE.getEFloat();
		public final static EDataType d = EcorePackage.eINSTANCE.getEDouble();
		public final static EDataType i = EcorePackage.eINSTANCE.getEInt();
		public final static EDataType p = ScenarioPackage.eINSTANCE
				.getPercentage();

		public static Object create(final EDataType type, int spinnerValue) {
			int digits = getDigits(type);
			if (type == l) {
				return Long.valueOf(spinnerValue);
			} else if (type == i) {
				return Integer.valueOf(spinnerValue);
			} else if (type == p) {
				return (Double) ((double) (spinnerValue * Math.pow(10, -(digits + 2))));
			} else if (type == f) {
				return (Float) ((float) (spinnerValue * Math.pow(10, -digits)));
			} else if (type == d) {
				return (Double) ((double) (spinnerValue * Math.pow(10, -digits)));
			} else {
				throw new RuntimeException("Unknown type of numeric field");
			}
		}

		public static int convert(final EDataType type, final Object value) {
			int digits = getDigits(type);
			if (type == l || type == i) {
				return ((Number) value).intValue();
			} else if (type == f) {
				return ((int) (((Number) value).floatValue() * Math.pow(10,
						digits)));
			} else if (type == d) {
				return ((int) (((Number) value).doubleValue() * Math.pow(10,
						digits)));
			} else if (type == p) {
				return ((int) (((Number) value).doubleValue() * Math.pow(10,
						digits + 2)));
			} else {
				throw new RuntimeException("Unknown type of numeric field");
			}
		}

		public static int getDigits(final EDataType type) {
			if (type == l || type == i) {
				return 0;
			} else if (type == f || type == d) {
				return 2;
			} else if (type == p) {
				return 1;
			} else {
				return 0;
			}
		}

		public static void setupSpinner(final EDataType type,
				final Spinner spinner) {
			if (type == l || type == i) {
				spinner.setDigits(0);
				spinner.setMaximum(Integer.MAX_VALUE);
				spinner.setMinimum(0);
			} else if (type == f || type == d) {
				spinner.setDigits(2);
				spinner.setMaximum(Integer.MAX_VALUE);
				spinner.setMinimum(0);
			} else if (type == p) {
				spinner.setDigits(1);
				spinner.setMaximum(1000);
				spinner.setMinimum(0);
			}
		}

		public static Object getDefaultValue(EDataType type) {
			if (type == i)
				return ((Integer) 0);
			if (type == l)
				return ((Long) 0l);
			if (type == d || type == p)
				return ((Double) 0d);
			if (type == f)
				return ((Float) 0f);
				
			throw new RuntimeException("Unknown type for number : " + type.getName());
		}
	}

	public NumberInlineEditor(EMFPath path, EStructuralFeature feature,
			EditingDomain editingDomain, final ICommandProcessor processor) {
		super(path, feature, editingDomain, processor);
	}

	private Spinner spinner;

	@Override
	public Control createValueControl(Composite parent) {
		type = (EDataType) feature.getEType();
		final Composite box = new Composite(parent, SWT.NONE);
		final GridLayout boxLayout = new GridLayout(1, false);
		boxLayout.marginHeight = 0;
		boxLayout.marginWidth = 0;
		box.setLayout(boxLayout);
		final Spinner spinner = new Spinner(box, SWT.BORDER);

		spinner.addSelectionListener(new SelectionListener() {
			{
				final SelectionListener sl = this;
				spinner.addDisposeListener(new DisposeListener() {
					@Override
					public void widgetDisposed(DisposeEvent e) {
						spinner.removeSelectionListener(sl);
					}
				});
			}

			@Override
			public void widgetSelected(SelectionEvent e) {
				doSetValue(NumberTypes.create(type, spinner.getSelection()));
			}

			@Override
			public void widgetDefaultSelected(SelectionEvent e) {
				doSetValue(NumberTypes.create(type, spinner.getSelection()));
			}
		});

		NumberTypes.setupSpinner(type, spinner);

		this.spinner = spinner;
		spinner.setLayoutData(new GridData(GridData.FILL_BOTH));

		if (type == NumberTypes.p) {
			boxLayout.numColumns = 2;
			new Label(box, SWT.NONE).setText("%");
		}

		return box;
	}

	@Override
	protected void updateValueDisplay(Object value) {
		if (spinner.isDisposed())
			return;
		if (value == null) {
			spinner.setSelection(0);
		} else {
			spinner.setSelection(NumberTypes.convert(type, value));
		}
	}

	@Override
	protected Object getInitialUnsetValue() {
		return NumberTypes.getDefaultValue(type);
	}
}
