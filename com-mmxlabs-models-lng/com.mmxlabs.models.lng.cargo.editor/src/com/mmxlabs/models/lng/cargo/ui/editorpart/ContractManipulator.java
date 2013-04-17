/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2013
 * All rights reserved.
 */
package com.mmxlabs.models.lng.cargo.ui.editorpart;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.eclipse.emf.common.command.Command;
import org.eclipse.emf.common.notify.Notifier;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.edit.command.CommandParameter;
import org.eclipse.emf.edit.command.SetCommand;
import org.eclipse.emf.edit.domain.EditingDomain;
import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.jface.viewers.ComboBoxCellEditor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CCombo;
import org.eclipse.swt.widgets.Composite;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mmxlabs.common.Pair;
import com.mmxlabs.models.lng.cargo.CargoPackage;
import com.mmxlabs.models.lng.commercial.Contract;
import com.mmxlabs.models.lng.types.AContract;
import com.mmxlabs.models.mmxcore.MMXObject;
import com.mmxlabs.models.ui.tabular.ICellManipulator;
import com.mmxlabs.models.ui.tabular.ICellRenderer;
import com.mmxlabs.models.ui.valueproviders.IReferenceValueProvider;
import com.mmxlabs.models.ui.valueproviders.IReferenceValueProviderProvider;

/**
 * A column manipulator for setting a {@link Contract} or a price expression.
 * 
 * Uses {@link ComboBoxCellEditor} for its edit control, and takes the values from an {@link IReferenceValueProvider}.
 * 
 * @author Simon Goodall
 * @since 3.0
 * 
 */
public class ContractManipulator implements ICellManipulator, ICellRenderer {

	private static final Logger log = LoggerFactory.getLogger(ContractManipulator.class);

	private final EditingDomain editingDomain;

	private final IReferenceValueProvider valueProvider;

	private ComboBoxCellEditor editor;

	// Data pertaining to a single slot. Note, this class will be used for multiple slot instances.
	private final ArrayList<Object> valueList = new ArrayList<Object>();
	private final ArrayList<String> names = new ArrayList<String>();

	/**
	 * Create a manipulator for the given field in the target object, taking values from the given valueProvider and creating set commands in the provided editingDomain.
	 * 
	 * @param field
	 *            the field to set
	 * @param valueProvider
	 *            provides the names & values for the field
	 * @param editingDomain
	 *            editing domain for setting
	 */
	public ContractManipulator(final IReferenceValueProviderProvider valueProvider, final EditingDomain editingDomain) {

		this.valueProvider = valueProvider.getReferenceValueProvider(CargoPackage.eINSTANCE.getSlot(), CargoPackage.eINSTANCE.getSlot_Contract());
		this.editingDomain = editingDomain;
	}

	@Override
	public String render(final Object object) {
		canEdit(object);
		Object superValue = getValue(object);

		if (superValue instanceof Integer) {
			final Integer idx = (Integer) superValue;
			if (idx.intValue() != -1) {
				superValue = valueList.get(idx);
			} else {
				superValue = SetCommand.UNSET_VALUE;
			}
		}

		if (superValue == SetCommand.UNSET_VALUE) {
			if (object instanceof MMXObject) {
				final Object defaultValue = ((MMXObject) object).getUnsetValue(CargoPackage.eINSTANCE.getSlot_Contract());
				if (defaultValue instanceof EObject || defaultValue == null) {
					return valueProvider.getName((EObject) object, CargoPackage.eINSTANCE.getSlot_Contract(), (EObject) defaultValue);
				}
			}
		} else {
			if ((superValue instanceof AContract) || (superValue == null)) {
				return valueProvider.getName((EObject) object, CargoPackage.eINSTANCE.getSlot_Contract(), (EObject) superValue);
			} else {
				return "" + superValue;
			}
		}
		return "";
	}

	public void doSetValue(final Object object, final Object value) {
		if (value.equals(-1) || (value.equals(0) && ((EObject) object).eIsSet(CargoPackage.eINSTANCE.getSlot_PriceExpression()))) {
			final CCombo cc = (CCombo) editor.getControl();
			final String text = cc.getText();
			runSetCommand(object, text);
			return;
		}
		final Object newValue = valueList.get((Integer) value);
		runSetCommand(object, (AContract) newValue);
	}

	public CellEditor createCellEditor(final Composite c, final Object object) {
		editor = new ComboBoxCellEditor(c, new String[0], SWT.FLAT | SWT.BORDER);
		// editor.setActivationStyle(ComboBoxCellEditor.DROP_DOWN_ON_MOUSE_ACTIVATION);
		setEditorNames();
		return editor;
	}

	@Override
	public Object getValue(final Object object) {

		if (object == null) {
			return "";
		}

		final EObject eObject = (EObject) object;

		if (eObject.eIsSet(CargoPackage.eINSTANCE.getSlot_PriceExpression())) {
			return 0;
		}

		final int x = valueList.indexOf(eObject.eGet(CargoPackage.eINSTANCE.getSlot_Contract()));
		if (x == -1) {
			log.warn("Index of " + object + " to be selected is -1, so it is not a legal option in the control");
		}
		return x;
	}

	@Override
	public boolean canEdit(final Object object) {

		if (object == null) {
			return false;
		}

		// get legal item list
		final EObject slot = (EObject) object;
		final Iterable<Pair<String, EObject>> values = valueProvider.getAllowedValues(slot, CargoPackage.eINSTANCE.getSlot_Contract());

		valueList.clear();
		names.clear();
		for (final Pair<String, EObject> value : values) {
			names.add(value.getFirst());
			valueList.add(value.getSecond());
		}

		if (slot.eIsSet(CargoPackage.eINSTANCE.getSlot_PriceExpression())) {
			final Object priceExpression = slot.eGet(CargoPackage.eINSTANCE.getSlot_PriceExpression());
			names.add(0, priceExpression.toString());
			valueList.add(0, priceExpression);
		}

		setEditorNames();
		return valueList.size() > 0;
	}

	void setEditorNames() {
		if (editor == null) {
			return;
		}
		// names can be null; editor entries can't
		final String [] items = new String [names.size()];
		int i = 0;
		for (String name: names) {
			items[i++] = (name == null) ? "" : name;
		}
		//editor.setItems(names.toArray(new String[] {}));
		editor.setItems(items);
	}

	@Override
	public Iterable<Pair<Notifier, List<Object>>> getExternalNotifiers(final Object object) {
		if (object == null) {

			return Collections.emptySet();
		}

		final Object value = reallyGetValue(object);
		if (value instanceof EObject) {
			return valueProvider.getNotifiers((EObject) object, CargoPackage.eINSTANCE.getSlot_Contract(), (EObject) value);
		} else {
			return Collections.emptySet();
		}
	}

	protected String renderUnsetValue(final Object container, final Object unsetDefault) {
		return renderSetValue(container, unsetDefault);
	}

	protected String renderSetValue(final Object container, final Object setValue) {
		return setValue == null ? "" : setValue.toString();
	}

	@Override
	public final void setValue(final Object object, final Object value) {
		if (value == SetCommand.UNSET_VALUE && CargoPackage.eINSTANCE.getSlot_Contract().isUnsettable()) {
			runSetCommand(object, (AContract) value);
		} else {
			doSetValue(object, value);
		}
	}

	public void runSetCommand(final Object object, final AContract value) {
		final Object currentValue = reallyGetValue(object);
		if (((currentValue == null) && (value == null)) || (((currentValue != null) && (value != null)) && currentValue.equals(value))) {
			return;
		}

		final Command command = editingDomain.createCommand(SetCommand.class, new CommandParameter(object, CargoPackage.eINSTANCE.getSlot_Contract(), value));
		editingDomain.getCommandStack().execute(command);
	}

	public void runSetCommand(final Object object, final String value) {
		final Object currentValue = reallyGetValue(object);
		if (((currentValue == null) && (value == null)) || (((currentValue != null) && (value != null)) && currentValue.equals(value))) {
			return;
		}
		final Command command;
		if (value != null && !value.isEmpty()) {
			command = editingDomain.createCommand(SetCommand.class, new CommandParameter(object, CargoPackage.eINSTANCE.getSlot_PriceExpression(), value));
		} else {
			command = editingDomain.createCommand(SetCommand.class, new CommandParameter(object, CargoPackage.eINSTANCE.getSlot_PriceExpression(), SetCommand.UNSET_VALUE));

		}
		editingDomain.getCommandStack().execute(command);
	}

	@Override
	public final CellEditor getCellEditor(final Composite c, final Object object) {
		return createCellEditor(c, object);
	}

	@Override
	public Object getFilterValue(final Object object) {
		return getComparable(object);
	}

	private Object reallyGetValue(final Object object) {
		if (object == null) {
			return null;
		}

		final EObject eObject = (EObject) object;
		if (eObject.eIsSet(CargoPackage.eINSTANCE.getSlot_PriceExpression())) {
			return "" + eObject.eGet(CargoPackage.eINSTANCE.getSlot_PriceExpression());
		}

		return eObject.eGet(CargoPackage.eINSTANCE.getSlot_Contract());
	}

	@SuppressWarnings("rawtypes")
	@Override
	public Comparable getComparable(final Object object) {
		return render(object);
	}
}
