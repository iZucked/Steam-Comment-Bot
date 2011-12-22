/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2011
 * All rights reserved.
 */
package com.mmxlabs.shiplingo.ui.tableview;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

import org.eclipse.emf.common.notify.Notifier;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.edit.domain.EditingDomain;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.swt.widgets.Control;
import org.eclipse.ui.dialogs.ListSelectionDialog;

import com.mmxlabs.common.Equality;
import com.mmxlabs.common.Pair;
import com.mmxlabs.shiplingo.ui.detailview.base.IReferenceValueProvider;
import com.mmxlabs.shiplingo.ui.detailview.utils.CommandUtil;

/**
 * @author hinton
 * 
 */
public class MultipleReferenceManipulator extends DialogFeatureManipulator {
	private final IReferenceValueProvider valueProvider;
	private EAttribute nameAttribute;

	public MultipleReferenceManipulator(final EStructuralFeature field,
			final EditingDomain editingDomain,
			final IReferenceValueProvider valueProvider,
			final EAttribute nameAttribute) {
		super(field, editingDomain);
		this.valueProvider = valueProvider;
		this.nameAttribute = nameAttribute;
	}

	@Override
	protected String renderValue(Object value) {
		if (value == null)
			return "";
		List<? extends EObject> selectedValues = (List<? extends EObject>) value;
		final StringBuilder sb = new StringBuilder();
		for (final EObject obj : selectedValues) {
			if (sb.length() > 0)
				sb.append(", ");
			sb.append(obj.eGet(nameAttribute));
		}
		return sb.toString();
	}

	@Override
	public void setValue(Object object, Object value) {
		final Object currentValue = getValue(object);
		if (Equality.isEqual(currentValue, value)) return;
		editingDomain.getCommandStack().execute(
				CommandUtil.createMultipleAttributeSetter(editingDomain,
						(EObject) object, field, (Collection) value));
	}

	@SuppressWarnings("unchecked")
	@Override
	protected Object openDialogBox(Control cellEditorWindow, Object object) {
		List<Pair<String, EObject>> options = valueProvider.getAllowedValues(
				(EObject) object, field);

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
		final Collection<EObject> sel = (Collection<EObject>) getValue(object);
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

	@Override
	public Iterable<Pair<Notifier, List<Object>>> getExternalNotifiers(
			Object object) {
		if (object != null) {
			final EList<EObject> values = (EList) super.getValue(object);
			final LinkedList<Pair<Notifier, List<Object>>> notifiers = new LinkedList<Pair<Notifier, List<Object>>>();
			for (final EObject ref : values) {
				for (final Pair<Notifier, List<Object>> p : valueProvider
						.getNotifiers((EObject) object, (EReference) field, ref)) {
					notifiers.add(p);
				}
			}
			return notifiers;
		}

		return super.getExternalNotifiers(object);
	}
}
