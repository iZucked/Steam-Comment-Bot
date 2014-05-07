package com.mmxlabs.models.lng.port.ui.editorpart;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.CellLabelProvider;
import org.eclipse.jface.viewers.ColumnLabelProvider;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Shell;

import com.mmxlabs.common.Pair;
import com.mmxlabs.models.lng.port.Location;
import com.mmxlabs.models.lng.port.Port;
import com.mmxlabs.models.lng.port.PortPackage;
import com.mmxlabs.models.lng.types.PortCapability;
import com.mmxlabs.rcp.common.dialogs.ListSelectionDialog;

public class PortPickerDialog extends ListSelectionDialog {
	List<CellLabelProvider> searchedColumns = new ArrayList<>();
	
	public PortPickerDialog(Shell parentShell, Object input) {
		this(parentShell, input, new ArrayContentProvider(), new LabelProvider() {

			@SuppressWarnings("unchecked")
			@Override
			public String getText(final Object element) {
				return ((Pair<String, ?>) element).getFirst();
			}
		});
	}

	public PortPickerDialog(Shell parentShell, Object input,
			IStructuredContentProvider contentProvider,
			LabelProvider labelProvider) {
		super(parentShell, input, contentProvider, labelProvider);
	}
	
	public List<EObject> pick(final Control cellEditorWindow, final List<Pair<String, EObject>> options, final Collection<EObject> currentValue, final EReference feature) {
		if (options.size() > 0 && options.get(0).getSecond() == null)
			options.remove(0);

		setTitle("Value Selection");

		final ArrayList<Pair<String, EObject>> selectedOptions = new ArrayList<Pair<String, EObject>>();
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

		ColumnLabelProvider nameLabelProvider = new ColumnLabelProvider() {
			@Override
			public String getText(final Object element) {
				return ((Pair<String, ?>) element).getFirst();
			}
		};
		
		CellLabelProvider nameColumn = addColumn("Name", nameLabelProvider);
		searchedColumns.add(nameColumn);

		if (feature.getEReferenceType().isSuperTypeOf(PortPackage.eINSTANCE.getPort())) {

			ColumnLabelProvider countryLabelProvider = new ColumnLabelProvider() {
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
			
			
			CellLabelProvider countryColumn = addColumn("Country", countryLabelProvider);
			searchedColumns.add(countryColumn);

			for (final PortCapability pc : PortCapability.values()) {
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
				return ((Pair<?, EObject>) element).getSecond().eClass().getName();
			}
		});

		if (open() == Window.OK) {
			final Object[] result = getResult();

			final ArrayList<EObject> resultList = new ArrayList<EObject>();
			for (final Object o : result) {
				resultList.add(((Pair<String, EObject>) o).getSecond());
			}
			return resultList;
		}

		return null;		
	}

	@Override
	protected String getFilterableText(final Object element) {
		String result = "";
		for (CellLabelProvider provider: searchedColumns) {
			result = result + ((ColumnLabelProvider) provider).getText(element).toLowerCase() + " ";
		}
		return result;
	}	
}
