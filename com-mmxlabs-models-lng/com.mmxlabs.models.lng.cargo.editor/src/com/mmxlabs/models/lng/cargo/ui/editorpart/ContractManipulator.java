/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2018
 * All rights reserved.
 */
package com.mmxlabs.models.lng.cargo.ui.editorpart;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

import org.eclipse.emf.common.command.Command;
import org.eclipse.emf.common.command.CompoundCommand;
import org.eclipse.emf.common.notify.Notifier;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.edit.command.CommandParameter;
import org.eclipse.emf.edit.command.SetCommand;
import org.eclipse.emf.edit.domain.EditingDomain;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.jface.viewers.ComboBoxCellEditor;
import org.eclipse.jface.viewers.ComboBoxViewerCellEditor;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CCombo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import com.mmxlabs.common.Pair;
import com.mmxlabs.models.lng.cargo.CargoPackage;
import com.mmxlabs.models.lng.cargo.Slot;
import com.mmxlabs.models.lng.commercial.Contract;
import com.mmxlabs.models.lng.pricing.ui.autocomplete.ExpressionAnnotationConstants;
import com.mmxlabs.models.lng.spotmarkets.SpotMarket;
import com.mmxlabs.models.mmxcore.MMXRootObject;
import com.mmxlabs.models.mmxcore.NamedObject;
import com.mmxlabs.models.ui.editors.autocomplete.AutoCompleteHelper;
import com.mmxlabs.models.ui.editors.autocomplete.IMMXContentProposalProvider;
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
 * 
 */
public class ContractManipulator implements ICellManipulator, ICellRenderer {

	private static final String NONE = "<None>";

	private final EditingDomain editingDomain;

	private final IReferenceValueProvider valueProvider;

	/**
	 * Label provider to map between contracts and price expressions to strings.
	 */
	private final LabelProvider labelProvider;

	private IExtraCommandsHook extraCommandsHook;

	private Object parent;

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
		labelProvider = new LabelProvider() {
			@Override
			public String getText(final Object element) {

				// Is the element missing?
				if (element == null || "".equals(element)) {
					return NONE;
				}

				if (element instanceof NamedObject) {
					return ((NamedObject) element).getName();
				}
				return super.getText(element);
			}
		};
	}

	@Override
	public String render(final Object object) {

		if (object == null) {
			return null;
		}
		final Object superValue = getValue(object);
		return labelProvider.getText(superValue);
	}

	public void doSetValue(final Object object, final Object value) {
		if (Objects.equals(getValue(object), value)) {
			return;
		}

		if (value instanceof SpotMarket) {
			runSetCommand(object, (SpotMarket) value);
			runSetCommand(object, (Contract) null);
			runSetCommand(object, (String) null);
		} else if (value instanceof Contract) {
			runSetCommand(object, (Contract) value);
			runSetCommand(object, (String) null);
		} else {
			final String text = (String) value;
			{
				final Iterable<Pair<String, EObject>> values = valueProvider.getAllowedValues((EObject) object, CargoPackage.eINSTANCE.getSlot_Contract());
				for (final Pair<String, EObject> v : values) {
					if (v.getFirst().toLowerCase().equals(text.toLowerCase())) {
						runSetCommand(object, (Contract) v.getSecond());
						return;
					}
				}
			}
			if (NONE.equals(text)) {
				runSetCommand(object, (Contract) null);
			} else {
				runSetCommand(object, (String) text);
			}

			return;
		}
	}

	public CellEditor createCellEditor(final Composite c, final Object object) {
		final ComboBoxViewerCellEditor editor = new ComboBoxViewerCellEditor(c, SWT.FLAT | SWT.BORDER) {

			private IMMXContentProposalProvider proposalHelper;

			@Override
			protected Control createControl(Composite parent) {
				Control control = super.createControl(parent);
				this.proposalHelper = AutoCompleteHelper.createControlProposalAdapter(control, ExpressionAnnotationConstants.TYPE_COMMODITY);
				for (Resource r : editingDomain.getResourceSet().getResources()) {
					for (EObject o : r.getContents()) {
						if (o instanceof MMXRootObject) {
							this.proposalHelper.setRootObject((MMXRootObject) o);
						}
					}
				}
				return control;
			}

			/**
			 * Override doGetValue to also return the custom string if a valid selection has not been made.
			 */
			@Override
			protected Object doGetValue() {
				final Object value = super.doGetValue();
				if (value == null) {
					return ((CCombo) getControl()).getText();
				}
				return value;
			}
		};
		editor.setContentProvider(new ArrayContentProvider());
		editor.setLabelProvider(labelProvider);
		setEditorValues(editor, (EObject) object);
		return editor;
	}

	@Override
	public Object getValue(final Object object) {

		if (object == null) {
			return "";
		}

		final EObject eObject = (EObject) object;

		if (eObject.eIsSet(CargoPackage.eINSTANCE.getSlot_PriceExpression())) {
			return eObject.eGet(CargoPackage.eINSTANCE.getSlot_PriceExpression());
		} else if (CargoPackage.Literals.SPOT_SLOT.isInstance(eObject)) {
			return eObject.eGet(CargoPackage.Literals.SPOT_SLOT__MARKET);
		} else if (eObject.eIsSet(CargoPackage.eINSTANCE.getSlot_Contract())) {
			return eObject.eGet(CargoPackage.eINSTANCE.getSlot_Contract());
		} else {
			return null;
		}
	}

	@Override
	public boolean canEdit(final Object object) {

		if (object == null) {
			return false;
		}
		return true;
	}

	private void setEditorValues(final ComboBoxViewerCellEditor editor, final EObject slot) {

		final List<Object> valueList = new ArrayList<Object>();
		if (CargoPackage.Literals.SPOT_SLOT.isInstance(slot)) {
			final EObject market = (EObject) slot.eGet(CargoPackage.Literals.SPOT_SLOT__MARKET);
			if (market != null) {
				valueList.add(market);
			}
		} else {
			final Iterable<Pair<String, EObject>> values = valueProvider.getAllowedValues(slot, CargoPackage.eINSTANCE.getSlot_Contract());
			for (final Pair<String, EObject> value : values) {
				valueList.add(value.getSecond());
			}
		}

		if (slot.eIsSet(CargoPackage.eINSTANCE.getSlot_PriceExpression())) {
			final Object priceExpression = slot.eGet(CargoPackage.eINSTANCE.getSlot_PriceExpression());
			valueList.add(0, priceExpression);
		}
		// Add our "null" element
		valueList.add(NONE);
		// Remove any real null value
		while (valueList.remove(null))
			;
		editor.setInput(valueList);
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
	public boolean isValueUnset(Object object) {
		if (object instanceof Slot) {
			Slot slot = (Slot) object;
			return !slot.isSetContract() && !slot.isSetPriceExpression();
		}
		return false;
	}

	@Override
	public final void setValue(final Object object, final Object value) {
		if (value == SetCommand.UNSET_VALUE && CargoPackage.eINSTANCE.getSlot_Contract().isUnsettable()) {
			runSetCommand(object, (Contract) value);
		} else {
			doSetValue(object, value);
		}
	}

	private void runSetCommand(final Object object, final SpotMarket value) {
		final Object currentValue = reallyGetValue(object);
		if (((currentValue == null) && (value == null)) || (((currentValue != null) && (value != null)) && currentValue.equals(value))) {
			return;
		}
		final Command command;
		if (value == null) {
			command = editingDomain.createCommand(SetCommand.class, new CommandParameter(object, CargoPackage.eINSTANCE.getSpotSlot_Market(), SetCommand.UNSET_VALUE));

		} else {
			command = editingDomain.createCommand(SetCommand.class, new CommandParameter(object, CargoPackage.eINSTANCE.getSpotSlot_Market(), value));
		}
		CompoundCommand cmd = new CompoundCommand();
		cmd.append(command);
		if (extraCommandsHook != null) {
			extraCommandsHook.applyExtraCommands(editingDomain, cmd, parent, object, value);
		}
		editingDomain.getCommandStack().execute(cmd);
	}

	private void runSetCommand(final Object object, final Contract value) {
		final Object currentValue = reallyGetValue(object);
		if (((currentValue == null) && (value == null)) || (((currentValue != null) && (value != null)) && currentValue.equals(value))) {
			return;
		}
		final Command command;
		if (value == null) {
			command = editingDomain.createCommand(SetCommand.class, new CommandParameter(object, CargoPackage.eINSTANCE.getSlot_Contract(), SetCommand.UNSET_VALUE));

		} else {
			command = editingDomain.createCommand(SetCommand.class, new CommandParameter(object, CargoPackage.eINSTANCE.getSlot_Contract(), value));
		}
		CompoundCommand cmd = new CompoundCommand();
		cmd.append(command);
		if (extraCommandsHook != null) {
			extraCommandsHook.applyExtraCommands(editingDomain, cmd, parent, object, value);
		}
		editingDomain.getCommandStack().execute(cmd);
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
		CompoundCommand cmd = new CompoundCommand();
		cmd.append(command);
		if (extraCommandsHook != null) {
			extraCommandsHook.applyExtraCommands(editingDomain, cmd, parent, object, value);
		}
		editingDomain.getCommandStack().execute(cmd);
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
		} else if (CargoPackage.Literals.SPOT_SLOT.isInstance(eObject)) {
			return eObject.eGet(CargoPackage.Literals.SPOT_SLOT__MARKET);
		}
		return eObject.eGet(CargoPackage.eINSTANCE.getSlot_Contract());
	}

	@SuppressWarnings("rawtypes")
	@Override
	public Comparable getComparable(final Object object) {
		return render(object);
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
