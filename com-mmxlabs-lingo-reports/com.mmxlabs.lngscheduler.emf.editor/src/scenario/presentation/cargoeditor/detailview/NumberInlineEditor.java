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
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Spinner;

import scenario.presentation.cargoeditor.detailview.EObjectDetailView.ICommandProcessor;

import com.mmxlabs.lngscheduler.emf.extras.EMFPath;

public class NumberInlineEditor extends UnsettableInlineEditor {
	private EDataType type;

	public NumberInlineEditor(EMFPath path, EStructuralFeature feature,
			EditingDomain editingDomain, final ICommandProcessor processor) {
		super(path, feature, editingDomain, processor);
	}

	private Spinner spinner;

	@Override
	public Control createValueControl(Composite parent) {
		final Spinner spinner = new Spinner(parent, SWT.BORDER);

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
				doSetValue(getSpinnerValue());
			}

			@Override
			public void widgetDefaultSelected(SelectionEvent e) {
				doSetValue(getSpinnerValue());
			}
		});

		if (feature.getEType().equals(EcorePackage.eINSTANCE.getEInt())
				|| feature.getEType().equals(EcorePackage.eINSTANCE.getELong())) {
			spinner.setDigits(0);
			spinner.setMaximum(Integer.MAX_VALUE);
			spinner.setMinimum(0);
		} else {
			spinner.setDigits(2);
			spinner.setMaximum(Integer.MAX_VALUE);
			spinner.setMinimum(0);
		}

		type = (EDataType) feature.getEType();

		this.spinner = spinner;
		return spinner;
	}

	private Number getSpinnerValue() {
		if (spinner.getDigits() == 0) {
			if (type.equals(EcorePackage.eINSTANCE.getELong())) {
				return Long.valueOf(spinner.getSelection());
			} else {
				return Integer.valueOf(spinner.getSelection());
			}
		} else {
			if (type.equals(EcorePackage.eINSTANCE.getEFloat())) {
				return (Float) ((float) (spinner.getSelection() * Math.pow(10,
						-spinner.getDigits())));
			} else {
				return (Double) ((spinner.getSelection() * Math.pow(10,
						-spinner.getDigits())));
			}
		}
	}

	private int numberToSelection(final Number number) {
		final int d = spinner.getDigits();

		if (d == 0) {
			return number.intValue();
		} else {
			return ((int) (number.floatValue() * Math.pow(10, d)));
		}
	}

	@Override
	protected void updateValueDisplay(Object value) {
		if (spinner.isDisposed()) return;
		if (value == null) {
			spinner.setSelection(0);
		} else {
			spinner.setSelection(numberToSelection((Number) value));
		}
	}

	@Override
	protected Object getInitialUnsetValue() {
		return (Integer) 0;
	}

}
