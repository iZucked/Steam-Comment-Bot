/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2012
 * All rights reserved.
 */
package com.mmxlabs.models.ui.editors.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.eclipse.emf.common.command.Command;
import org.eclipse.emf.common.command.CompoundCommand;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.EStructuralFeature;
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
import com.mmxlabs.models.ui.editors.util.CommandUtil;
import com.mmxlabs.models.ui.valueproviders.IReferenceValueProvider;
import com.mmxlabs.rcp.common.dialogs.ListSelectionDialog;

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
	public MultiReferenceInlineEditor(final EStructuralFeature feature) {
		super(feature);
	}

	@Override
	public void display(MMXRootObject context, EObject input) {
		valueProvider = commandHandler
				.getReferenceValueProviderProvider()
				.getReferenceValueProvider(input.eClass(), (EReference) feature);
		super.display(context, input);
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
				.createMultipleAttributeSetter(
						commandHandler.getEditingDomain(), input, feature,
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
				});
		dlg.setTitle("Value Selection");

		final ArrayList<Pair<String, EObject>> selectedOptions = new ArrayList<Pair<String, EObject>>();
		final Collection<EObject> sel = (Collection<EObject>) getValue();
		for (final Pair<String, EObject> p : options) {
			if (sel.contains(p.getSecond())) {
				selectedOptions.add(p);
			}
		}

		dlg.setInitialSelections(selectedOptions.toArray());
		dlg.addColumn("Name", new ColumnLabelProvider() {
			@Override
			public String getText(Object element) {
				return ((Pair<String, ?>) element).getFirst();
			}
		});

		// if
		// (((EReference)feature).getEReferenceType().isSuperTypeOf(PortPackage.eINSTANCE.getPort()))
		// {
		// for (final PortCapability pc : PortCapability.values()) {
		// dlg.addColumn(pc.getName(), new ColumnLabelProvider(){
		// @Override
		// public String getText(Object element) {
		// final Pair<?, EObject> p = (Pair<?, EObject>)element;
		// if (p.getSecond() instanceof Port) {
		// return ((Port)p.getSecond()).getCapabilities().contains(pc) ? "Yes" :
		// "No";
		// } else {
		// return "";
		// }
		// }
		// });
		// }
		// }

		dlg.groupBy(new ColumnLabelProvider() {
			@Override
			public String getText(Object element) {
				return ((Pair<?, EObject>) element).getSecond().eClass()
						.getName();
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
}
