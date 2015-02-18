package com.mmxlabs.lingo.reports.diff;

import org.eclipse.core.databinding.observable.map.IObservableMap;
import org.eclipse.emf.common.notify.AdapterFactory;
import org.eclipse.emf.edit.ui.provider.AdapterFactoryLabelProvider;
import org.eclipse.jface.databinding.viewers.ObservableMapCellLabelProvider;
import org.eclipse.jface.viewers.ViewerCell;

public class DataModelLabelProvider extends ObservableMapCellLabelProvider {

	private final IObservableMap[] attributeMaps;
	private AdapterFactory adapterFactory;
	private AdapterFactoryLabelProvider lp;

	public DataModelLabelProvider(AdapterFactory adapterFactory, final IObservableMap[] attributeMaps) {
		super(attributeMaps);
		this.adapterFactory = adapterFactory;
		this.attributeMaps = attributeMaps;
		this.lp = new AdapterFactoryLabelProvider(adapterFactory);
	}

	@Override
	public void update(final ViewerCell cell) {

		Object o = null;
		for (IObservableMap m : attributeMaps) {
			o = m.get(cell.getElement());
			if (o != null) {
				break;
			}
		}
		if (o == null) {
			cell.setText("");
		} else {
			cell.setText(o.toString());
		}
		
		cell.setImage(lp.getImage(cell.getElement()));
	}

}