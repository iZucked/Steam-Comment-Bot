package scenario.presentation.cargoeditor.detailview;

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

import com.mmxlabs.lngscheduler.emf.extras.EMFPath;

public class NumberInlineEditor extends BasicAttributeInlineEditor {
	public NumberInlineEditor(EMFPath path, EStructuralFeature feature,
			EditingDomain editingDomain) {
		super(path, feature, editingDomain);
	}

	private Spinner spinner;
	@Override
	public Control createControl(Composite parent) {
		final Spinner spinner = new Spinner(parent, SWT.NONE);
		
		spinner.addSelectionListener(new SelectionListener() {
			{
				final SelectionListener sl = this;
				spinner.addDisposeListener(
						new DisposeListener() {
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
		
		if (feature.getEType().equals(EcorePackage.eINSTANCE.getEInt()) ||
				feature.getEType().equals(EcorePackage.eINSTANCE.getELong())) {
			spinner.setDigits(0);
			spinner.setMaximum(Integer.MAX_VALUE);
			spinner.setMinimum(0);
		} else {
			spinner.setDigits(2);
			spinner.setMaximum(Integer.MAX_VALUE);
			spinner.setMinimum(0);
		}
		
		this.spinner = spinner;
		return spinner;
	}
	
	private Number getSpinnerValue() {
		if (spinner.getDigits() == 0) {
			return (Integer) spinner.getSelection();
		} else {
			return (Float) ((float)
					(spinner.getSelection() * Math.pow(10, -spinner.getDigits())));
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
	protected void updateDisplay(final Object value) {
		spinner.setSelection(numberToSelection((Number) value));
	}

}
