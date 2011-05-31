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
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;

import scenario.presentation.cargoeditor.detailview.EObjectDetailView.ICommandProcessor;

import com.mmxlabs.lngscheduler.emf.extras.EMFPath;

public abstract class DialogInlineEditor extends BasicAttributeInlineEditor {
	public DialogInlineEditor(EMFPath path, EStructuralFeature feature,
			EditingDomain editingDomain, final ICommandProcessor processor) {
		super(path, feature, editingDomain, processor);
	}
	private Button button;
	private Label label;
	
	protected Shell getShell() {
		return button.getShell();
	}
	
	@Override
	public Control createControl(final Composite parent) {
		final Composite contents = new Composite(parent, SWT.NONE);
		contents.setLayout(new RowLayout(SWT.HORIZONTAL));
		
		final Label label = new Label(contents, SWT.NONE);
		final Button button = new Button(contents, SWT.NONE);
		button.setText("Edit");
		this.label = label;
		this.button = button;
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
								});
					}
					
					@Override
					public void widgetSelected(SelectionEvent e) {
						final Object o = displayDialog(getValue());
						if (o != null)
							doSetValue(o);
					}
					
					@Override
					public void widgetDefaultSelected(SelectionEvent e) {}
				}
				);
		
		return super.wrapControl(contents);
	}

	@Override
	protected void updateDisplay(Object value) {
		render(value);
	}

	protected abstract Object displayDialog(final Object currentValue);
	protected abstract String render(final Object value);
}
