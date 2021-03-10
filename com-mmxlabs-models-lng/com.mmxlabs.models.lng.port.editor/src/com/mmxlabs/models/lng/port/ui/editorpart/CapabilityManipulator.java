/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2021
 * All rights reserved.
 */
package com.mmxlabs.models.lng.port.ui.editorpart;

import java.util.Collections;
import java.util.List;

import org.eclipse.emf.common.notify.Notifier;
import org.eclipse.emf.edit.command.AddCommand;
import org.eclipse.emf.edit.command.RemoveCommand;
import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.jface.viewers.ComboBoxCellEditor;
import org.eclipse.swt.widgets.Composite;

import com.mmxlabs.common.Pair;
import com.mmxlabs.models.lng.port.Port;
import com.mmxlabs.models.lng.port.PortPackage;
import com.mmxlabs.models.lng.types.PortCapability;
import com.mmxlabs.models.ui.editors.ICommandHandler;
import com.mmxlabs.models.ui.tabular.ICellManipulator;
import com.mmxlabs.models.ui.tabular.ICellRenderer;

/**
 * Column for editing port capabilities.
 * 
 * @author hinton
 */
class CapabilityManipulator implements ICellRenderer, ICellManipulator {
	private final PortCapability capability;
	private final ICommandHandler commandHandler;
	private IExtraCommandsHook extraCommandsHook;
	private Object parent;

	public CapabilityManipulator(final PortCapability capability, final ICommandHandler commandHandler) {
		this.capability = capability;
		this.commandHandler = commandHandler;
	}

	@Override
	public void setValue(final Object object, final Object value) {

		final Port p = (Port) object;
		if ((Integer) value == 0) {
			if (!p.getCapabilities().contains(capability)) {
				commandHandler.handleCommand(AddCommand.create(commandHandler.getEditingDomain(), p, PortPackage.eINSTANCE.getPort_Capabilities(), capability), p,
						PortPackage.eINSTANCE.getPort_Capabilities());
			}
		} else {
			if (p.getCapabilities().contains(capability)) {
				commandHandler.handleCommand(RemoveCommand.create(commandHandler.getEditingDomain(), p, PortPackage.eINSTANCE.getPort_Capabilities(), capability), p,
						PortPackage.eINSTANCE.getPort_Capabilities());
			}
		}
	}

	@Override
	public CellEditor getCellEditor(final Composite parent, final Object object) {
		return new ComboBoxCellEditor(parent, new String[] { "Y", "N" });
	}

	@Override
	public Object getValue(final Object object) {
		final Port p = (Port) object;
		return p.getCapabilities().contains(capability) ? 0 : 1;
	}

	@Override
	public boolean canEdit(final Object object) {
		return true;
	}

	@Override
	public String render(final Object object) {
		return ((Integer) getValue(object)) == 0 ? "Y" : "N";
	}

	@Override
	public Comparable getComparable(final Object object) {
		return render(object);
	}

	@Override
	public boolean isValueUnset(Object object) {
		return false;
	}

	@Override
	public Object getFilterValue(final Object object) {
		return getComparable(object);
	}

	@Override
	public Iterable<Pair<Notifier, List<Object>>> getExternalNotifiers(final Object object) {
		return Collections.emptySet();
	}

	@Override
	public void setParent(Object parent, Object object) {
		this.parent = parent;

	}

	@Override
	public void setExtraCommandsHook(IExtraCommandsHook extraCommandsHook) {
		this.extraCommandsHook = extraCommandsHook;
	}
}