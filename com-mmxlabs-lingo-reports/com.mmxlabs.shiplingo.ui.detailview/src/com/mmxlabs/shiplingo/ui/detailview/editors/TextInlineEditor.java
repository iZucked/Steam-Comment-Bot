/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2011
 * All rights reserved.
 */
package com.mmxlabs.shiplingo.ui.detailview.editors;

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

import com.mmxlabs.lngscheduler.emf.extras.EMFPath;

public class TextInlineEditor extends UnsettableInlineEditor {
	private Text text;
	private final int style;

	public TextInlineEditor(final EMFPath path,
			final EStructuralFeature feature,
			final EditingDomain editingDomain, final ICommandProcessor processor) {
		this(path, feature, editingDomain, processor, SWT.BORDER);
	}

	/**
	 * @param inputPath
	 * @param annotatedObject_Notes
	 * @param editingDomain
	 * @param commandProcessor
	 * @param i
	 */
	public TextInlineEditor(final EMFPath path,
			final EStructuralFeature feature,
			final EditingDomain editingDomain,
			final ICommandProcessor commandProcessor, final int style) {
		super(path, feature, editingDomain, commandProcessor);
		this.style = style;
	}

	@Override
	public Control createValueControl(Composite parent) {
		final Text text = new Text(parent, style);

		text.addModifyListener(new ModifyListener() {
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
		if (text.isDisposed())
			return;
		text.setText(value == null ? "" : value.toString());
	}

	@Override
	protected Object getInitialUnsetValue() {
		return "";
	}
}
