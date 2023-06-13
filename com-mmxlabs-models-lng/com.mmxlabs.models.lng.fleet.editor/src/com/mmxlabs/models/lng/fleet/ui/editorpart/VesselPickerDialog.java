/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
package com.mmxlabs.models.lng.fleet.ui.editorpart;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.CellLabelProvider;
import org.eclipse.jface.viewers.ColumnLabelProvider;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.widgets.Shell;

import com.mmxlabs.common.Pair;
import com.mmxlabs.models.lng.fleet.Vessel;
import com.mmxlabs.models.lng.fleet.VesselGroup;
import com.mmxlabs.rcp.common.dialogs.ListSelectionDialog;

public class VesselPickerDialog extends ListSelectionDialog {

	private List<CellLabelProvider> searchedColumns = new ArrayList<>();

	private String title = "Value Selection";

	public VesselPickerDialog(final Shell parentShell, final Object input) {
		this(parentShell, input, new ArrayContentProvider(), new LabelProvider() {

			@SuppressWarnings("unchecked")
			@Override
			public String getText(final Object element) {
				return ((Pair<String, ?>) element).getFirst();
			}
		});
	}

	public VesselPickerDialog(final Shell parentShell, final Object input, final IStructuredContentProvider contentProvider, final LabelProvider labelProvider) {
		super(parentShell, input, contentProvider, labelProvider);
	}

	public void withTitle(String title) {
		this.title = title;
	}
	public void withMultiSelect(boolean multiSelect) {
		this.multiSelect = multiSelect;
	}

	public List<EObject> pick(final List<Pair<String, EObject>> options, final Collection<EObject> currentValue, final EReference feature) {
		if (!options.isEmpty() && options.get(0).getSecond() == null) {
			options.remove(0);
		}

		setTitle(title);

		final ArrayList<Pair<String, EObject>> selectedOptions = new ArrayList<>();
		final Collection<EObject> sel = currentValue;
		if (sel != null) {
			for (final Pair<String, EObject> p : options) {
				if (sel.contains(p.getSecond())) {
					selectedOptions.add(p);
				}
			}
		}

		setInitialSelections(selectedOptions.toArray());

		searchedColumns.clear();

		final ColumnLabelProvider nameLabelProvider = new ColumnLabelProvider() {
			@Override
			public String getText(final Object element) {
				return ((Pair<String, ?>) element).getFirst();
			}
		};

		final CellLabelProvider nameColumn = addColumn("Name", nameLabelProvider);
		searchedColumns.add(nameColumn);

		groupBy(new ColumnLabelProvider() {
			@Override
			public String getText(final Object element) {
				final EObject second = ((Pair<?, EObject>) element).getSecond();
				if (second instanceof VesselGroup) {
					return "Vessel Group";
				} else if (second instanceof Vessel) {
					return "By vessel";
				}
				return second.eClass().getName();
			}
		});

		if (open() == Window.OK) {
			final Object[] result = getResult();

			if (result == null) {
				return null;
			}

			final List<EObject> resultList = new ArrayList<>();
			for (final Object o : result) {
				resultList.add(((Pair<String, EObject>) o).getSecond());
			}
			return resultList;
		}

		return null;
	}

	@Override
	protected String getFilterableText(final Object element) {
		List<String> result = new LinkedList<>();
		for (final CellLabelProvider provider : searchedColumns) {
			final String text = ((ColumnLabelProvider) provider).getText(element);
			if (text != null) {
				result.add(text.toLowerCase());
			}
		}

		return String.join(" ", result);
	}
}
