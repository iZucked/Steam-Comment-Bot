package com.mmxlabs.models.lng.commercial.editorfactories;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EEnumLiteral;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;

import com.mmxlabs.models.ui.editors.impl.UnsettableInlineEditor;

public abstract class AbstractValueListInlineEditor extends
		UnsettableInlineEditor {

	private Combo combo;
	protected final List<String> names;
	protected final List<Object> values;

	public AbstractValueListInlineEditor(EStructuralFeature feature, EList<EEnumLiteral> eLiterals) {
		super(feature);
		names = new ArrayList<String>(eLiterals.size());
		values = new ArrayList<Object>(eLiterals.size());
	}

	@Override
	protected Control createValueControl(final Composite parent) {
		combo = new Combo(parent, SWT.READ_ONLY);
	
		combo.setItems(names.toArray(new String[names.size()]));
	
		final SelectionListener sl = new SelectionAdapter() {
			@Override
			public void widgetSelected(final SelectionEvent e) {
				doSetValue(values.get(combo.getSelectionIndex()), false);
			}
		};
	
		combo.addSelectionListener(sl);
		combo.addDisposeListener(new DisposeListener() {
			@Override
			public void widgetDisposed(final DisposeEvent e) {
				combo.removeSelectionListener(sl);
			}
		});
	
		return wrapControl(combo);
	}

	@Override
	protected void updateValueDisplay(final Object value) {
		if (combo.isDisposed()) {
			return;
		}
		combo.select(values.indexOf(value));
	}

	@Override
	protected Object getInitialUnsetValue() {
		return values.get(0);
	}

	@Override
	protected void setControlsEnabled(final boolean enabled) {
		final boolean controlsEnabled = !isFeatureReadonly() && enabled;
	
		combo.setEnabled(controlsEnabled);
	
		super.setControlsEnabled(controlsEnabled);
	}

	@Override
	protected void setControlsVisible(final boolean visible) {
	
		combo.setVisible(visible);
	
		super.setControlsVisible(visible);
	}

}