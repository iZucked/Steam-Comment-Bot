/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
package com.mmxlabs.models.ui.editors.impl;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import org.eclipse.emf.common.command.Command;
import org.eclipse.emf.common.command.CompoundCommand;
import org.eclipse.emf.common.util.Enumerator;
import org.eclipse.emf.databinding.EMFDataBindingContext;
import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EEnum;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.ui.dialogs.ListSelectionDialog;
import org.eclipse.ui.forms.widgets.FormToolkit;

import com.mmxlabs.models.ui.editors.util.CommandUtil;

/**
 * An editor for picking multiple values from an enum.
 * 
 * @author hinton
 * 
 */
public class MultiEnumInlineEditor extends BasicAttributeInlineEditor {
	private Label theLabel;
	private EEnum myEnum;
	private Enumerator[] enumerators;

	public MultiEnumInlineEditor(EStructuralFeature feature) {
		super(feature);
		myEnum = (EEnum) ((EAttribute) feature).getEAttributeType();
		enumerators = new Enumerator[myEnum.getELiterals().size()];
		for (int i = 0; i < enumerators.length; i++) {
			enumerators[i] = myEnum.getELiterals().get(i).getInstance();
		}
	}

	/**
	 */
	@Override
	public Control createControl(final Composite parent, final EMFDataBindingContext dbc, final FormToolkit toolkit) {
		// final Composite buttonAndLabel = new Composite(parent, SWT.NONE);
		final Composite buttonAndLabel = toolkit.createComposite(parent);
		final GridLayout gl = new GridLayout(2, false);
		buttonAndLabel.setLayout(gl);
		gl.marginWidth = 0;
		gl.marginHeight = 0;

		// final Label label = new Label(buttonAndLabel, SWT.NONE);
		label = toolkit.createLabel(buttonAndLabel, "");
		label.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		
//		final Button button = new Button(buttonAndLabel, SWT.NONE);
		final Button button = toolkit.createButton(buttonAndLabel, "Edit", SWT.NONE);

		button.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				final List<Object> o = openDialogBox(parent);
				if (o != null) {
					doSetValue(o, false);
					updateDisplay(o);
				}
			}
		});

		theLabel = label;

		return super.wrapControl(buttonAndLabel);
	}

	@Override
	protected Command createSetCommand(Object value) {
		final CompoundCommand setter = CommandUtil.createMultipleAttributeSetter(commandHandler.getEditingDomain(), input, feature, (Collection<?>) value);
		return setter;
	}

	@Override
	protected void updateDisplay(final Object value) {
		List<Enumerator> selectedValues = (List<Enumerator>) value;
		final StringBuilder sb = new StringBuilder();
		for (final Enumerator obj : selectedValues) {
			if (sb.length() > 0)
				sb.append(", ");
			sb.append(obj.getName());
		}
		theLabel.setText(sb.toString());
	}

	protected List<Object> openDialogBox(Control cellEditorWindow) {
		ListSelectionDialog dlg = new ListSelectionDialog(cellEditorWindow.getShell(), enumerators, new ArrayContentProvider(), new LabelProvider() {
			@Override
			public String getText(Object element) {
				return ((Enumerator) element).getName();
			}
		}, "Select values:");
		dlg.setTitle("Value Selection");

		dlg.setInitialSelections(((Collection<?>) getValue()).toArray());
		dlg.setBlockOnOpen(true);
		dlg.open();
		Object[] result = dlg.getResult();

		return Arrays.asList(result);
	}
}
