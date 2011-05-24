/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2011
 * All rights reserved.
 */
package scenario.presentation.cargoeditor.detailview;

import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.edit.domain.EditingDomain;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;

import scenario.presentation.cargoeditor.EObjectDetailView.ICommandProcessor;

import com.mmxlabs.lngscheduler.emf.extras.EMFPath;

public class BooleanInlineEditor extends BasicAttributeInlineEditor {
	private Button button;
	public BooleanInlineEditor(EMFPath path, EStructuralFeature feature,
			EditingDomain editingDomain, final ICommandProcessor commandProcessor) {
		super(path, feature, editingDomain, commandProcessor);
	}

	@Override
	public Control createControl(final Composite parent) {
		final Button button = new Button(parent, SWT.CHECK);
		
		button.addSelectionListener(
				new SelectionListener() {
					{
						final SelectionListener sl = this;
						button.addDisposeListener(
								new DisposeListener() {
									@Override
									public void widgetDisposed(DisposeEvent e) {
										button.removeSelectionListener(sl);
									}
								}
								);
					}
					@Override
					public void widgetSelected(SelectionEvent e) {
						doSetValue((Boolean) button.getSelection());
					}
					
					@Override
					public void widgetDefaultSelected(SelectionEvent e) {}
				});
		
		this.button = button;
		
		return button;
	}

	@Override
	protected void updateDisplay(final Object value) {
		if (Boolean.TRUE.equals(value)) {
			this.button.setSelection(true);
		} else {
			this.button.setSelection(false);
		}
	}

}
