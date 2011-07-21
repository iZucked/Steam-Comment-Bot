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
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Text;

import scenario.presentation.cargoeditor.detailview.EObjectDetailView.ICommandProcessor;

import com.mmxlabs.lngscheduler.emf.extras.EMFPath;

public class TextInlineEditor extends UnsettableInlineEditor {
	private Text text;
	public TextInlineEditor(EMFPath path, EStructuralFeature feature,
			EditingDomain editingDomain, final ICommandProcessor processor) {
		super(path, feature, editingDomain, processor);
	}

	@Override
	public Control createValueControl(Composite parent) {
		final Text text = new Text(parent, SWT.BORDER);
		
		text.addModifyListener(
				new ModifyListener() {
					{
						final ModifyListener ml = this;
						text.addDisposeListener(new DisposeListener() {
							@Override
							public void widgetDisposed(DisposeEvent e) {
								text.removeModifyListener(ml);
							}
						});
					}
					@Override
					public void modifyText(ModifyEvent e) {
						TextInlineEditor.this.doSetValue(text.getText());
					}
				});
		
		this.text = text;
		
		return text;
	}

	@Override
	protected void updateValueDisplay(final Object value) {
		if (text.isDisposed()) return;
		text.setText(value == null ? "" : value.toString());
	}

	@Override
	protected Object getInitialUnsetValue() {
		return "";
	}
}
