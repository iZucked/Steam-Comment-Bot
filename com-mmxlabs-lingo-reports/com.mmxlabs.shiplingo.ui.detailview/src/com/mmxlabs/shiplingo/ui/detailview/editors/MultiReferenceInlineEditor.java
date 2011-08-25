/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2011
 * All rights reserved.
 */
package com.mmxlabs.shiplingo.ui.detailview.editors;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.eclipse.emf.common.command.Command;
import org.eclipse.emf.common.command.CompoundCommand;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.edit.domain.EditingDomain;
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

import com.mmxlabs.common.Pair;
import com.mmxlabs.lngscheduler.emf.extras.EMFPath;
import com.mmxlabs.shiplingo.ui.detailview.base.IReferenceValueProvider;
import com.mmxlabs.shiplingo.ui.detailview.utils.CommandUtil;

/**
 * An inline editor for editing multi-value references. Pops up a dialog.
 * 
 * @author Tom Hinton
 * 
 */
public class MultiReferenceInlineEditor extends BasicAttributeInlineEditor {
	private IReferenceValueProvider valueProvider;
	private Label theLabel;

	/**
	 * @param path
	 * @param feature
	 * @param editingDomain
	 * @param commandProcessor
	 */
	public MultiReferenceInlineEditor(final EMFPath path,
			final EStructuralFeature feature,
			final EditingDomain editingDomain,
			final ICommandProcessor commandProcessor,
			final IReferenceValueProvider valueProvider) {
		super(path, feature, editingDomain, commandProcessor);
		this.valueProvider = valueProvider;
	}

	@Override
	public Control createControl(final Composite parent) {
		final Composite buttonAndLabel = new Composite(parent, SWT.NONE);
		final GridLayout gl = new GridLayout(2, false);
		buttonAndLabel.setLayout(gl);
		gl.marginWidth = 0;
		gl.marginHeight = 0;

		final Label label = new Label(buttonAndLabel, SWT.NONE);
		label.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		final Button button = new Button(buttonAndLabel, SWT.NONE);
		button.setText("Edit");

		button.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				final List<EObject> o = openDialogBox(parent);
				if (o != null) {
					doSetValue(o);
					updateDisplay(o);
				}
			}
		});

		theLabel = label;

		return super.wrapControl(buttonAndLabel);
	}

	@Override
	protected Command createSetCommand(Object value) {
		final CompoundCommand setter = CommandUtil
				.createMultipleAttributeSetter(editingDomain, input, feature,
						(Collection) value);
		return setter;
	}

	@Override
	protected void updateDisplay(Object value) {
		List<? extends EObject> selectedValues = (List<? extends EObject>) value;
		final StringBuilder sb = new StringBuilder();
		for (final EObject obj : selectedValues) {
			if (sb.length() > 0)
				sb.append(", ");
			sb.append(valueProvider.getName(input, (EReference) feature, obj));
		}
		theLabel.setText(sb.toString());
	}

	@SuppressWarnings("unchecked")
	protected List<EObject> openDialogBox(Control cellEditorWindow) {
		List<Pair<String, EObject>> options = valueProvider.getAllowedValues(
				input, feature);

		if (options.size() > 0 && options.get(0).getSecond() == null)
			options.remove(0);

		ListSelectionDialog dlg = new ListSelectionDialog(
				cellEditorWindow.getShell(), options.toArray(),
				new ArrayContentProvider(), new LabelProvider() {

					@Override
					public String getText(Object element) {
						return ((Pair<String, ?>) element).getFirst();
					}
				}, "Select values:");
		dlg.setTitle("Value Selection");

		final ArrayList<Pair<String, EObject>> selectedOptions = new ArrayList<Pair<String, EObject>>();
		final Collection<EObject> sel = (Collection<EObject>) getValue();
		for (final Pair<String, EObject> p : options) {
			if (sel.contains(p.getSecond())) {
				selectedOptions.add(p);
			}
		}

		dlg.setInitialSelections(selectedOptions.toArray());
		dlg.setBlockOnOpen(true);
		dlg.open();
		Object[] result = dlg.getResult();
		if (result == null)
			return null;
		else {
			final ArrayList<EObject> resultList = new ArrayList<EObject>();
			for (final Object o : result) {
				resultList.add(((Pair<String, EObject>) o).getSecond());
			}
			return resultList;
		}
	}
}
