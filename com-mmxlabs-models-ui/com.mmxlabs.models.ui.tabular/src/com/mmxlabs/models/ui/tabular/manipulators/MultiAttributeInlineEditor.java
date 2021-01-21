/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2021
 * All rights reserved.
 */
package com.mmxlabs.models.ui.tabular.manipulators;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.function.Function;
import java.util.function.Supplier;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.edit.command.SetCommand;
import org.eclipse.emf.edit.domain.EditingDomain;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.ColumnLabelProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.widgets.Control;

import com.mmxlabs.common.Equality;
import com.mmxlabs.common.Pair;
import com.mmxlabs.models.ui.editors.util.CommandUtil;
import com.mmxlabs.rcp.common.dialogs.ListSelectionDialog;

public class MultiAttributeInlineEditor extends DialogFeatureManipulator {
	private Function<Object, String> labelProvider;
	private Supplier<List<Object>> valuesSupplier;

	private static final int MAX_DISPLAY_LENGTH = 32;
	private static final int MIN_DISPLAY_NAMES = 2;

	public MultiAttributeInlineEditor(final EStructuralFeature feature, EditingDomain editingDomain, Function<Object, String> labelProvider, Supplier<List<Object>> valuesSupplier) {
		super(feature, editingDomain);
		this.labelProvider = labelProvider;
		this.valuesSupplier = valuesSupplier;
	}

	@Override
	protected String renderValue(final Object value) {
		if (!(value instanceof List)) {
			return "";
		}
		@SuppressWarnings("unchecked")
		final List<?> selectedValues = (List<?>) value;
		final StringBuilder sb = new StringBuilder();
		int numNamesAdded = 0;
		for (final Object obj : selectedValues) {
			String name = labelProvider.apply(obj);
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

		final List<Pair<String, Object>> options = new LinkedList<>();

		for (Object o : valuesSupplier.get()) {
			options.add(new Pair<>(labelProvider.apply(o), o));
		}

		if ((options.size() > 0) && (options.get(0).getSecond() == null)) {
			options.remove(0);
		}

		final ListSelectionDialog dlg = new ListSelectionDialog(cellEditorWindow.getShell(), (Object) options.toArray(), new ArrayContentProvider(),

				new LabelProvider() {

					@Override
					public String getText(final Object element) {
						return ((Pair<String, ?>) element).getFirst();
					}
				});
		dlg.setTitle("Value Selection");

		final ArrayList<Pair<String, Object>> selectedOptions = new ArrayList<>();
		Object value = getValue(object);
		if (value == SetCommand.UNSET_VALUE) {
			return null;
		}
		final Collection<?> sel = (Collection<?>) value;
		for (final Pair<String, Object> p : options) {
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

		// dlg.groupBy(new ColumnLabelProvider() {
		// @Override
		// public String getText(final Object element) {
		// return ((Pair<?, EObject>) element).getSecond().eClass().getName();
		// }
		// });

		if (dlg.open() == Window.OK) {
			final Object[] result = dlg.getResult();

			final ArrayList<Object> resultList = new ArrayList<>();
			for (final Object o : result) {
				resultList.add(((Pair<String, Object>) o).getSecond());
			}

			return resultList;
		}
		return null;
	}
}
