/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
package com.mmxlabs.models.ui.editors.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import org.eclipse.emf.common.command.Command;
import org.eclipse.emf.common.command.CompoundCommand;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.edit.command.SetCommand;
import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.ColumnLabelProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;

import com.mmxlabs.common.Pair;
import com.mmxlabs.models.mmxcore.MMXRootObject;
import com.mmxlabs.models.ui.editors.dialogs.IDialogEditingContext;
import com.mmxlabs.models.ui.editors.util.CommandUtil;
import com.mmxlabs.models.ui.valueproviders.IReferenceValueProvider;
import com.mmxlabs.rcp.common.dialogs.ListSelectionDialog;

/**
 * An inline editor for editing multi-value references. Pops up a dialog.
 * 
 * @author Tom Hinton
 * 
 */
public class MultiReferenceInlineEditor extends UnsettableInlineEditor {
	private IReferenceValueProvider valueProvider;
	private Label theLabel;
	private Button button;

	/**
	 * @param path
	 * @param feature
	 * @param editingDomain
	 * @param commandProcessor
	 */
	public MultiReferenceInlineEditor(final EStructuralFeature feature) {
		super(feature);
	}

	@Override
	public void display(final IDialogEditingContext dialogContext, final MMXRootObject context, final EObject input, final Collection<EObject> range) {
		valueProvider = commandHandler.getReferenceValueProviderProvider().getReferenceValueProvider(input.eClass(), (EReference) feature);
		super.display(dialogContext, context, input, range);
	}

	@Override
	protected Object getInitialUnsetValue() {
		return Collections.emptyList();
	}

	@Override
	protected Control createValueControl(final Composite parent) {
		final Composite buttonAndLabel = toolkit.createComposite(parent);
		final GridLayout gl = new GridLayout(2, false);
		buttonAndLabel.setLayout(gl);
		gl.marginWidth = 0;
		gl.marginHeight = 0;

		final Label label = toolkit.createLabel(buttonAndLabel, "");
		{
			// Set a size hint, but allow width to increase if needed.
			final GridData gd = GridDataFactory.fillDefaults().hint(150, SWT.DEFAULT).grab(true, false).create();
			label.setLayoutData(gd);
		}

		button = toolkit.createButton(buttonAndLabel, "Edit", SWT.NONE);

		button.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(final SelectionEvent e) {
				final List<EObject> o = openDialogBox(parent);
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
			return SetCommand.create(commandHandler.getEditingDomain(), input, feature, value);
		} else {
			final CompoundCommand setter = CommandUtil.createMultipleAttributeSetter(commandHandler.getEditingDomain(), input, feature, (Collection<?>) value);
			return setter;
		}
	}

	@Override
	protected void updateValueDisplay(final Object value) {
		if (theLabel == null || theLabel.isDisposed()) {
			return;
		}

		final List<? extends EObject> selectedValues = (List<? extends EObject>) value;
		if (selectedValues != null) {
			final StringBuilder sb = new StringBuilder();
			for (final EObject obj : selectedValues) {
				if (sb.length() > 0)
					sb.append(", ");
				sb.append(valueProvider.getName(input, (EReference) feature, obj));
			}
			theLabel.setText(sb.toString());
		}
	}

	@SuppressWarnings("unchecked")
	protected List<EObject> openDialogBox(final Control cellEditorWindow) {
		final List<Pair<String, EObject>> options = valueProvider.getAllowedValues(input, feature);

		if (options.size() > 0 && options.get(0).getSecond() == null)
			options.remove(0);

		final ListSelectionDialog dlg = new ListSelectionDialog(cellEditorWindow.getShell(), options.toArray(), new ArrayContentProvider(), new LabelProvider() {

			@Override
			public String getText(final Object element) {
				return ((Pair<String, ?>) element).getFirst();
			}
		});
		dlg.setTitle("Value Selection");

		final ArrayList<Pair<String, EObject>> selectedOptions = new ArrayList<Pair<String, EObject>>();
		final Collection<EObject> sel = (Collection<EObject>) getValue();
		if (sel != null) {
			for (final Pair<String, EObject> p : options) {
				if (sel.contains(p.getSecond())) {
					selectedOptions.add(p);
				}
			}
		}

		dlg.setInitialSelections(selectedOptions.toArray());

		createColumns(dlg);

		dlg.groupBy(new ColumnLabelProvider() {
			@Override
			public String getText(final Object element) {
				return ((Pair<?, EObject>) element).getSecond().eClass().getName();
			}
		});

		if (dlg.open() == Window.OK) {
			final Object[] result = dlg.getResult();

			final ArrayList<EObject> resultList = new ArrayList<EObject>();
			for (final Object o : result) {
				resultList.add(((Pair<String, EObject>) o).getSecond());
			}
			return resultList;
		}

		return null;
	}

	/**
	 */
	protected void createColumns(final ListSelectionDialog dlg) {
		dlg.addColumn("Name", new ColumnLabelProvider() {
			@SuppressWarnings("unchecked")
			@Override
			public String getText(final Object element) {
				return ((Pair<String, ?>) element).getFirst();
			}
		});
	}

	@Override
	protected void setControlsEnabled(final boolean enabled) {
		final boolean controlsEnabled = !isFeatureReadonly() && enabled;

		theLabel.setEnabled(controlsEnabled);
		button.setEnabled(controlsEnabled);

		super.setControlsEnabled(controlsEnabled);
	}

	@Override
	protected void setControlsVisible(final boolean visible) {
		theLabel.setVisible(visible);
		button.setVisible(visible);

		super.setControlsEnabled(visible);
	}
}
