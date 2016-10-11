/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
package com.mmxlabs.models.ui.tabular.manipulators;

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
import org.eclipse.jface.viewers.ColumnLabelProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.widgets.Control;

import com.mmxlabs.common.Equality;
import com.mmxlabs.common.Pair;
import com.mmxlabs.models.ui.editors.util.CommandUtil;
import com.mmxlabs.models.ui.valueproviders.IReferenceValueProvider;
import com.mmxlabs.models.ui.valueproviders.IReferenceValueProviderProvider;
import com.mmxlabs.rcp.common.dialogs.ListSelectionDialog;

/**
 * @author hinton
 * 
 */
public class MultipleReferenceManipulator extends DialogFeatureManipulator {

	/** @see PortMultiReferenceInlineEditor */
	private final static int MAX_DISPLAY_LENGTH = 32;
	private static final int MIN_DISPLAY_NAMES = 2;

	private final com.mmxlabs.models.ui.valueproviders.IReferenceValueProvider valueProvider;
	private final EAttribute nameAttribute;

	public MultipleReferenceManipulator(final EStructuralFeature field, final EditingDomain editingDomain, final IReferenceValueProvider valueProvider, final EAttribute nameAttribute) {
		super(field, editingDomain);
		this.valueProvider = valueProvider;
		this.nameAttribute = nameAttribute;
	}

	public MultipleReferenceManipulator(final EStructuralFeature field, final IReferenceValueProviderProvider providerProvider, final EditingDomain editingDomain, final EAttribute nameAttribute) {
		this(field, editingDomain, providerProvider.getReferenceValueProvider(field.getEContainingClass(), (EReference) field), nameAttribute);
	}

	@Override
	protected String renderValue(final Object value) {
		if (!(value instanceof List)) {
			return "";
		}
		@SuppressWarnings("unchecked")
		final List<? extends EObject> selectedValues = (List<? extends EObject>) value;
		final StringBuilder sb = new StringBuilder();
		int numNamesAdded = 0;
		for (final EObject obj : selectedValues) {
			String name = obj.eGet(nameAttribute).toString();
			if (sb.length() > 0) {
				sb.append(", ");
			}
			if (sb.length() + name.length() <= MAX_DISPLAY_LENGTH || numNamesAdded <= MIN_DISPLAY_NAMES - 1) {
				sb.append(name);
				++numNamesAdded;
			} else {
				sb.append("...");
				break;
			}
		}
		return sb.toString();
	}

	@Override
	public void doSetValue(final Object object, final Object value) {
		final Object currentValue = getValue(object);
		if (Equality.isEqual(currentValue, value)) {
			return;
		}
		editingDomain.getCommandStack().execute(CommandUtil.createMultipleAttributeSetter(editingDomain, (EObject) object, field, (Collection<?>) value));
	}

	@SuppressWarnings("unchecked")
	@Override
	protected Object openDialogBox(final Control cellEditorWindow, final Object object) {
		final List<Pair<String, EObject>> options = valueProvider.getAllowedValues((EObject) object, field);

		if ((options.size() > 0) && (options.get(0).getSecond() == null)) {
			options.remove(0);
		}

		final ListSelectionDialog listSelectionDialog = new ListSelectionDialog(cellEditorWindow.getShell(), options.toArray(), new ArrayContentProvider(),

				new LabelProvider() {

					@Override
					public String getText(final Object element) {
						return ((Pair<String, ?>) element).getFirst();
					}
				});
		final ListSelectionDialog dlg = listSelectionDialog;// , "Select values:");
		dlg.setTitle("Value Selection");

		final ArrayList<Pair<String, EObject>> selectedOptions = new ArrayList<Pair<String, EObject>>();
		final Collection<EObject> sel = (Collection<EObject>) getValue(object);
		for (final Pair<String, EObject> p : options) {
			if (sel.contains(p.getSecond())) {
				selectedOptions.add(p);
			}
		}

		dlg.setInitialSelections(selectedOptions.toArray());
		dlg.addColumn("Name", new ColumnLabelProvider() {
			@Override
			public String getText(final Object element) {
				return ((Pair<String, ?>) element).getFirst();
			}
		});

		// dlg.addColumn("Type", );

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

	@Override
	public Iterable<Pair<Notifier, List<Object>>> getExternalNotifiers(final Object object) {
		if (object != null) {
			@SuppressWarnings("unchecked")
			final EList<EObject> values = (EList<EObject>) super.getValue(object);
			final LinkedList<Pair<Notifier, List<Object>>> notifiers = new LinkedList<Pair<Notifier, List<Object>>>();
			for (final EObject ref : values) {
				for (final Pair<Notifier, List<Object>> p : valueProvider.getNotifiers((EObject) object, (EReference) field, ref)) {
					notifiers.add(p);
				}
			}
			return notifiers;
		}

		return super.getExternalNotifiers(object);
	}
}
