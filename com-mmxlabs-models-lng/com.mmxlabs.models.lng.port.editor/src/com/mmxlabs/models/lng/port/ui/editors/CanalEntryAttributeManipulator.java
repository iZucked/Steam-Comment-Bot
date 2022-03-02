/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2022
 * All rights reserved.
 */
package com.mmxlabs.models.lng.port.ui.editors;

import java.util.LinkedList;
import java.util.List;
import java.util.function.Function;

import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.jface.viewers.ComboBoxCellEditor;
import org.eclipse.swt.widgets.Composite;

import com.mmxlabs.common.Pair;
import com.mmxlabs.models.lng.cargo.CanalBookingSlot;
import com.mmxlabs.models.lng.port.CanalEntry;
import com.mmxlabs.models.lng.port.PortModel;
import com.mmxlabs.models.lng.port.util.ModelDistanceProvider;
import com.mmxlabs.models.lng.port.util.PortModelLabeller;
import com.mmxlabs.models.ui.editors.ICommandHandler;
import com.mmxlabs.models.ui.tabular.manipulators.ValueListAttributeManipulator;

public class CanalEntryAttributeManipulator extends ValueListAttributeManipulator {
	PortModel portModel;
	final Function<CanalEntry, String> nameProvider;
	public CanalEntryAttributeManipulator(PortModel portModel, final EAttribute field, final ICommandHandler commandHandler, final Function<CanalEntry, String> nameProvider) {
		super(field, commandHandler, getValues(nameProvider));
		this.portModel = portModel;
		this.nameProvider = nameProvider;
	}

	private static List<Pair<String, Object>> getValues(final Function<CanalEntry, String> nameProvider) {
		final LinkedList<Pair<String, Object>> values = new LinkedList<Pair<String, Object>>();
		for (final CanalEntry literal : CanalEntry.values()) {
			values.add(new Pair<String, Object>(nameProvider.apply(literal), literal));
		}
		return values;
	}
	
	@Override
	public CellEditor createCellEditor(final Composite c, final Object object) {
		// where we populate list
		// need port model to populate
		CanalBookingSlot canalBookingSlot = null;
		if (object instanceof CanalBookingSlot) {
			canalBookingSlot = (CanalBookingSlot) object;
			String[] names = new String[CanalEntry.values().length];
			int n = 0;
			for (final CanalEntry literal : CanalEntry.values()) {
				names[n] = nameProvider.apply(literal);
				n++;
			}
			return new ComboBoxCellEditor(c, names);
		}
		return null;
	}
	
	@Override
	public String render(final Object object) {
		Object value = super.getValue(object);
		CanalBookingSlot canalBookingSlot = null;
		if (object instanceof CanalBookingSlot) {
			canalBookingSlot = (CanalBookingSlot) object;
			String[] names = new String[CanalEntry.values().length];
			int n = 0;
			for (final CanalEntry literal : CanalEntry.values()) {
				names[n] = nameProvider.apply(literal);
				n++;
			}
			return names[(int) value];
		} else {
			return super.render(object);
		}
	}


}