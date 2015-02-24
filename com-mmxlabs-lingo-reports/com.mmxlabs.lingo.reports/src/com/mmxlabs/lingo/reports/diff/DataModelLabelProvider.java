/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2015
 * All rights reserved.
 */
package com.mmxlabs.lingo.reports.diff;

import org.eclipse.core.databinding.observable.map.IObservableMap;
import org.eclipse.emf.common.notify.AdapterFactory;
import org.eclipse.emf.edit.ui.provider.AdapterFactoryLabelProvider;
import org.eclipse.jface.databinding.viewers.ObservableMapCellLabelProvider;
import org.eclipse.jface.viewers.ViewerCell;

import com.mmxlabs.lingo.reports.views.schedule.model.ChangeType;
import com.mmxlabs.lingo.reports.views.schedule.model.CycleGroup;

public class DataModelLabelProvider extends ObservableMapCellLabelProvider {

	private final IObservableMap[] attributeMaps;
	private final AdapterFactoryLabelProvider lp;

	public DataModelLabelProvider(final AdapterFactory adapterFactory, final IObservableMap[] attributeMaps) {
		super(attributeMaps);
		this.attributeMaps = attributeMaps;
		this.lp = new AdapterFactoryLabelProvider(adapterFactory);
	}

	@Override
	public void update(final ViewerCell cell) {

		final Object element = cell.getElement();
		if (element instanceof CycleGroup) {
			final CycleGroup cycleGroup = (CycleGroup) element;
			cell.setText(String.format("%d - %s change", cycleGroup.getIndex(), getChangeTypeString(cycleGroup.getChangeType())));
		} else {
			Object o = null;
			for (final IObservableMap m : attributeMaps) {
				o = m.get(element);
				if (o != null) {
					break;
				}
			}
			if (o == null) {
				cell.setText("");
			} else {
				cell.setText(o.toString());
			}
		}
		cell.setImage(lp.getImage(element));
	}

	public String getChangeTypeString(ChangeType changeType) {
		switch (changeType) {
		case CHARTERING:
			return "Chartering";
		case DURATION:
			return "Duration";
		case PNL:
			return "P&L";
		case VESSEL:
			return "Vessel";
		case WIRING:
			return "Wiring";
		default:
			break;
		}
		return "Unknown";
	}

}