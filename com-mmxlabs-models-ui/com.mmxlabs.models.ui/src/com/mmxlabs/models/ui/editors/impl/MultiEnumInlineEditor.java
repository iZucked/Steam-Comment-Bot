/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2022
 * All rights reserved.
 */
package com.mmxlabs.models.ui.editors.impl;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import org.eclipse.emf.common.command.Command;
import org.eclipse.emf.common.command.CompoundCommand;
import org.eclipse.emf.common.util.Enumerator;
import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EEnum;
import org.eclipse.emf.ecore.ETypedElement;
import org.eclipse.emf.edit.command.SetCommand;
import org.eclipse.jface.layout.GridDataFactory;
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
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.ui.dialogs.ListSelectionDialog;

import com.mmxlabs.models.ui.editors.util.CommandUtil;
import com.mmxlabs.rcp.icons.lingo.CommonImages;
import com.mmxlabs.rcp.icons.lingo.CommonImages.IconMode;
import com.mmxlabs.rcp.icons.lingo.CommonImages.IconPaths;

/**
 * An editor for picking multiple values from an enum.
 * 
 * @author hinton
 * 
 */
public class MultiEnumInlineEditor extends UnsettableInlineEditor {
	protected Label theLabel;
	protected EEnum myEnum;
	protected Enumerator[] enumerators;

	public MultiEnumInlineEditor(final ETypedElement typedElement) {
		super(typedElement);
		myEnum = (EEnum) ((EAttribute) typedElement).getEAttributeType();
		enumerators = new Enumerator[myEnum.getELiterals().size()];
		for (int i = 0; i < enumerators.length; i++) {
			enumerators[i] = myEnum.getELiterals().get(i).getInstance();
		}
	}

	/**
	 */
	@Override
	public Control createValueControl(final Composite parent) {
		// final Composite buttonAndLabel = new Composite(parent, SWT.NONE);
		final Composite buttonAndLabel = toolkit.createComposite(parent);
		final GridLayout gl = new GridLayout(2, false);
		buttonAndLabel.setLayout(gl);
		gl.marginWidth = 0;
		gl.marginHeight = 0;
		buttonAndLabel.setBackground(Display.getDefault().getSystemColor(SWT.COLOR_WHITE));
		final Label label = toolkit.createLabel(buttonAndLabel, "");
		{
			// Set a size hint, but allow width to increase if needed.
			final GridData gd = GridDataFactory.fillDefaults().hint(150, SWT.DEFAULT).grab(true, false).create();
			label.setLayoutData(gd);
			label.setBackground(Display.getDefault().getSystemColor(SWT.COLOR_WHITE));
		}

		Button button = toolkit.createButton(buttonAndLabel, "", SWT.NONE);
		button.setImage(CommonImages.getImage(IconPaths.Edit, IconMode.Enabled));

		button.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(final SelectionEvent e) {
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
	protected Command createSetCommand(final Object value) {

		if (value == SetCommand.UNSET_VALUE) {
			CompoundCommand cmd = new CompoundCommand();

			cmd.append(SetCommand.create(commandHandler.getEditingDomain(), input, typedElement, value));
			if (overrideToggleFeature != null) {
				cmd.append(SetCommand.create(commandHandler.getEditingDomain(), input, overrideToggleFeature, Boolean.FALSE));
			}
			return cmd;
		} else {

			final CompoundCommand setter = CommandUtil.createMultipleAttributeSetter(commandHandler.getEditingDomain(), input, typedElement, (Collection<?>) value);
			return setter;
		}
	}

	@Override
	protected void updateValueDisplay(final Object value) {
		final List<Enumerator> selectedValues = (List<Enumerator>) value;
		final StringBuilder sb = new StringBuilder();
		for (final Enumerator obj : selectedValues) {
			if (sb.length() > 0)
				sb.append(", ");
			sb.append(obj.getName());
		}
	}

	protected List<Object> openDialogBox(final Control cellEditorWindow) {
		final ListSelectionDialog dlg = new ListSelectionDialog(cellEditorWindow.getShell(), enumerators, new ArrayContentProvider(), new LabelProvider() {
			@Override
			public String getText(final Object element) {
				return ((Enumerator) element).getName();
			}
		}, "Select values:");
		dlg.setTitle("Value Selection");

		dlg.setInitialSelections(((Collection<?>) getValue()).toArray());
		dlg.setBlockOnOpen(true);
		dlg.open();
		final Object[] result = dlg.getResult();
		if (result == null) {
			return null;
		}
		return Arrays.asList(result);
	}

	@Override
	protected Object getInitialUnsetValue() {
		return Collections.emptyList();
	}
}
