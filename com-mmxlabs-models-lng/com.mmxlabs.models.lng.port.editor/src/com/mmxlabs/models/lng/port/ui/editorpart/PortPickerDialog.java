/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2020
 * All rights reserved.
 */
package com.mmxlabs.models.lng.port.ui.editorpart;

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
import com.mmxlabs.license.features.KnownFeatures;
import com.mmxlabs.license.features.LicenseFeatures;
import com.mmxlabs.models.lng.port.CapabilityGroup;
import com.mmxlabs.models.lng.port.Location;
import com.mmxlabs.models.lng.port.Port;
import com.mmxlabs.models.lng.port.PortCountryGroup;
import com.mmxlabs.models.lng.port.PortGroup;
import com.mmxlabs.models.lng.port.PortPackage;
import com.mmxlabs.models.lng.types.PortCapability;
import com.mmxlabs.rcp.common.dialogs.ListSelectionDialog;

public class PortPickerDialog extends ListSelectionDialog {

	private static boolean STS_ENABLED = LicenseFeatures.isPermitted(KnownFeatures.FEATURE_SHIP_TO_SHIP);

	private List<CellLabelProvider> searchedColumns = new ArrayList<>();

	public PortPickerDialog(final Shell parentShell, final Object input) {
		this(parentShell, input, new ArrayContentProvider(), new LabelProvider() {

			@SuppressWarnings("unchecked")
			@Override
			public String getText(final Object element) {
				return ((Pair<String, ?>) element).getFirst();
			}
		});
	}

	public PortPickerDialog(final Shell parentShell, final Object input, final IStructuredContentProvider contentProvider, final LabelProvider labelProvider) {
		super(parentShell, input, contentProvider, labelProvider);
	}

	public List<EObject> pick(final List<Pair<String, EObject>> options, final Collection<EObject> currentValue, final EReference feature) {
		if (!options.isEmpty() && options.get(0).getSecond() == null) {
			options.remove(0);
		}

		setTitle("Value Selection");

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

		if (feature.getEReferenceType().isSuperTypeOf(PortPackage.eINSTANCE.getPort())) {

			final ColumnLabelProvider countryLabelProvider = new ColumnLabelProvider() {
				@Override
				public String getText(final Object element) {
					final Pair<?, EObject> p = (Pair<?, EObject>) element;
					if (p.getSecond() instanceof Port) {
						final Location location = ((Port) p.getSecond()).getLocation();
						if (location != null) {
							return location.getCountry();
						}
					}
					return "";
				}
			};

			final CellLabelProvider countryColumn = addColumn("Country", countryLabelProvider);
			searchedColumns.add(countryColumn);

			for (final PortCapability pc : PortCapability.values()) {
				if (pc == PortCapability.TRANSFER && !STS_ENABLED) {
					continue;
				}
				
				addColumn(pc.getName(), new ColumnLabelProvider() {
					@Override
					public String getText(final Object element) {
						final Pair<?, EObject> p = (Pair<?, EObject>) element;
						if (p.getSecond() instanceof Port) {
							return ((Port) p.getSecond()).getCapabilities().contains(pc) ? "Yes" : "No";
						} else {
							return "";
						}
					}
				});
			}
		}

		groupBy(new ColumnLabelProvider() {
			@Override
			public String getText(final Object element) {
				final EObject second = ((Pair<?, EObject>) element).getSecond();
				if (second instanceof PortCountryGroup) {
					return "Country Group";
				} else if (second instanceof PortGroup) {
					return "Port Group";
				} else if (second instanceof CapabilityGroup) {
					return "Capability Group";
				} else if (second instanceof Port) {
					return "By port";
				}
				return second.eClass().getName();
			}
		});
		if (feature.getEReferenceType().isSuperTypeOf(PortPackage.eINSTANCE.getPort())) {

			final ColumnLabelProvider aliasesLabelProvider = new ColumnLabelProvider() {
				@Override
				public String getText(final Object element) {

					final Pair<?, EObject> p = (Pair<?, EObject>) element;
					if (p.getSecond() instanceof Port) {
						final Location location = ((Port) p.getSecond()).getLocation();
						if (location != null) {
							return String.join(", ", location.getOtherNames());
						}
					}
					return "";
				}
			};

			final CellLabelProvider aliasColumn = addColumn("Aliases", aliasesLabelProvider);
			searchedColumns.add(aliasColumn);
		}

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
