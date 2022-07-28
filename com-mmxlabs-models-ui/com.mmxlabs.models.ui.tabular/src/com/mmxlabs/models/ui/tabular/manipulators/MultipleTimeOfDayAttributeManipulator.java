/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2022
 * All rights reserved.
 */
package com.mmxlabs.models.ui.tabular.manipulators;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.ColumnLabelProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.widgets.Control;

import com.mmxlabs.common.Pair;
import com.mmxlabs.models.ui.editors.ICommandHandler;
import com.mmxlabs.rcp.common.dialogs.ListSelectionDialog;

public class MultipleTimeOfDayAttributeManipulator extends DialogFeatureManipulator {

	private static final int MAX_DISPLAY_LENGTH = 32;
	private static final int MIN_DISPLAY_NAMES = 2;

	@NonNull
	protected final ArrayList<Integer> valueList;
	@NonNull
	protected final ArrayList<String> names;

	public MultipleTimeOfDayAttributeManipulator(EStructuralFeature field, ICommandHandler commandHandler) {
		super(field, commandHandler);
		valueList = new ArrayList<>(24);
		names = new ArrayList<>(24);
		for (int hour = 0; hour < 24; ++hour) {
			valueList.add(hour);
			names.add(String.format("%02d:00", hour));
		}
	}

	@Override
	protected Object openDialogBox(Control cellEditorWindow, Object object) {
		final List<Pair<String, Integer>> options = getAllOptions();
		final ListSelectionDialog listSelectionDialog = new ListSelectionDialog(cellEditorWindow.getShell(), options.toArray(), new ArrayContentProvider(), new LabelProvider() {
			@Override
			public String getText(Object element) {
				return ((Pair<String, ?>) element).getFirst();
			}
		});
		listSelectionDialog.setTitle("Value Selection");
		final ArrayList<Pair<String, Integer>> selectedOptions = new ArrayList<>();
		Object value = getValue(object);
		final Collection<Integer> sel = (Collection<Integer>) value;
		for (final Pair<String, Integer> p : options) {
			if (sel.contains(p.getSecond())) {
				selectedOptions.add(p);
			}
		}
		listSelectionDialog.setInitialSelections(selectedOptions.toArray());
		listSelectionDialog.addColumn("Time", new ColumnLabelProvider() {
			@Override
			public String getText(Object element) {
				return ((Pair<String, ?>) element).getFirst();
			}
		});
		
		if (listSelectionDialog.open() == Window.OK) {
			final Object[] result = listSelectionDialog.getResult();
			final ArrayList<Integer> resultList = new ArrayList<>();
			for (final Object o : result) {
				resultList.add(((Pair<String, Integer>) o).getSecond());
			}
			return resultList;
		}
		return null;
	}

	@NonNull
	private List<Pair<String, Integer>> getAllOptions() {
		assert valueList.size() == names.size();
		final Iterator<Integer> valueIter = valueList.iterator();
		final Iterator<String> namesIter = names.iterator();
		final List<Pair<String, Integer>> options = new ArrayList<>();
		while (valueIter.hasNext()) {
			options.add(Pair.of(namesIter.next(), valueIter.next()));
		}
		return options;
	}

	@Override
	protected String renderValue(Object value) {
		final List<Integer> selectedValues = (List<Integer>) value;
		if (selectedValues.isEmpty() || selectedValues.size() == 24) {
			return "All";
		}
		
		final StringBuilder sb = new StringBuilder();
		int numNamesAdded = 0;
		for (final int hour : selectedValues) {
			String name = String.format("%02d:00", hour);
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

}
