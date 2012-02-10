/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2012
 * All rights reserved.
 */
package com.mmxlabs.shiplingo.ui.detailview.editors;

import java.text.ParseException;

import org.eclipse.emf.ecore.EDataType;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.edit.domain.EditingDomain;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;

import com.mmxlabs.lngscheduler.emf.extras.EMFPath;

public class NumberInlineEditor extends UnsettableInlineEditor {
	final EDataType type;

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
		final Text textControl = new Text(box, SWT.BORDER);

		// Verifier to restrict value text entry
		textControl.addVerifyListener(new NumberVerifyListener(type));

		textControl.addModifyListener(new ModifyListener() {

			@Override
			public void modifyText(final ModifyEvent e) {
				final String text = textControl.getText();
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

		this.text = textControl;
		textControl.setLayoutData(new GridData(GridData.FILL_BOTH));

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
