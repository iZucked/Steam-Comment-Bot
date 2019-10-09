/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2019
 * All rights reserved.
 */
package com.mmxlabs.models.lng.pricing.ui.autocomplete;

import java.util.Collections;
import java.util.List;
import java.util.Objects;

import org.eclipse.emf.common.command.Command;
import org.eclipse.emf.common.command.CompoundCommand;
import org.eclipse.emf.common.notify.Notifier;
import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.edit.command.CommandParameter;
import org.eclipse.emf.edit.command.SetCommand;
import org.eclipse.emf.edit.domain.EditingDomain;
import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.jface.viewers.ICellEditorListener;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.TextCellEditor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Text;

import com.mmxlabs.common.Pair;
import com.mmxlabs.models.mmxcore.MMXRootObject;
import com.mmxlabs.models.mmxcore.NamedObject;
import com.mmxlabs.models.ui.editors.autocomplete.AutoCompleteHelper;
import com.mmxlabs.models.ui.editors.autocomplete.IMMXContentProposalProvider;
import com.mmxlabs.models.ui.tabular.ICellManipulator;
import com.mmxlabs.models.ui.tabular.ICellRenderer;

public class PriceAttributeManipulator implements ICellManipulator, ICellRenderer {

	private final EAttribute attribute;
	
	private final EditingDomain editingDomain;

	/**
	 * Label provider to map between NamedObjects and price expressions to strings.
	 */
	private final LabelProvider labelProvider;

	private IExtraCommandsHook extraCommandsHook;

	private Object parent;
	
	/**
	 * Create a manipulator for the given field in the target object, taking values from the given valueProvider and creating set commands in the provided editingDomain.
	 * 
	 * @param attribute
	 *            the field to set
	 * @param editingDomain
	 *            editing domain for setting
	 * @param expressionAnnotationType
	 */
	public PriceAttributeManipulator(final EAttribute attribute, final EditingDomain editingDomain) {
		this.attribute = attribute;
		this.editingDomain = editingDomain;
		this.labelProvider = new LabelProvider() {
			@Override
			public String getText(final Object element) {
				// Is the element missing?
				if (element == null || "".equals(element)) {
					return "";
				}
				if (element instanceof NamedObject) {
					return ((NamedObject) element).getName();
				}
				return super.getText(element);
			}
		};
	}

	protected EAttribute getAttribute(final Object object) {
		return this.attribute;
	}
	
	@Override
	public String render(final Object object) {
		if (object == null) {
			return null;
		}
		final Object superValue = getValue(object);
		return labelProvider.getText(superValue);
	}

	public void doSetValue(final Object object, Object value) {
		doSetValue(object, value, true);
	}

	protected void doSetValue(final Object object, Object value, final boolean forceCommandExecution) {
		Object currentValue = getValue(object);
		if (Objects.equals(currentValue, value)) {
			return;
		}
		final String text = (String) value;
		if (forceCommandExecution || !(((currentValue != null && value != null) && currentValue.equals(value)))) {
			runSetCommand(object, (String) text);
		}
	}
		
	public CellEditor createCellEditor(final Composite c, final Object object) {
		TextCellEditor editor = new TextCellEditor(c, SWT.FLAT | SWT.BORDER) {
			private IMMXContentProposalProvider proposalHelper;

			@Override
			protected Control createControl(Composite parent) {
				Control control = super.createControl(parent);
				this.proposalHelper = AutoCompleteHelper.createControlProposalAdapter(control, getAttribute(object));
				for (Resource r : editingDomain.getResourceSet().getResources()) {
					for (EObject o : r.getContents()) {
						if (o instanceof MMXRootObject) {
							this.proposalHelper.setRootObject((MMXRootObject) o);
						}
					}
				}
				return control;
			}
			
		    @Override
			protected void doSetValue(Object value) {
				if (value == null) {
					value = "";
				}
				super.doSetValue(value);
		    }
		};

		editor.addListener(new ICellEditorListener() {

			@Override
			public void applyEditorValue() { }

			@Override
			public void cancelEditor() { }

			@Override
			public void editorValueChanged(boolean oldValidState, boolean newValidState) {	
				Text text = ((Text)editor.getControl());
				if (!editor.isActivated()) {
					PriceAttributeManipulator.this.doSetValue(object, text.getText(), false);
				}
			}
		});
		
		return editor;
	}

	@Override
	public Object getValue(final Object object) {
		if (object == null || "".equals(object)) {
			return "";
		}

		if (object instanceof EObject) {
			final EObject eObject = (EObject) object;

			if (eObject.eIsSet(getAttribute(object))) {
				return eObject.eGet(getAttribute(object));
			} else {
				return "";
			}
		}
		else if (object instanceof String) {
			return object;
		}
		else {
			return "";
		}
	}

	@Override
	public boolean canEdit(final Object object) {
		if (object == null) {
			return false;
		}
		return true;
	}

	@Override
	public Iterable<Pair<Notifier, List<Object>>> getExternalNotifiers(final Object object) {
		return Collections.emptySet();
	}

	protected String renderUnsetValue(final Object container, final Object unsetDefault) {
		return renderSetValue(container, unsetDefault);
	}

	protected String renderSetValue(final Object container, final Object setValue) {
		return setValue == null ? "" : setValue.toString();
	}

	@Override
	public boolean isValueUnset(final Object object) {
		if (object == null) {
			return false;
		}
		if (getAttribute(object).isUnsettable() && ((EObject) object).eIsSet(getAttribute(object)) == false) {
			return true;
		}
		return false;
	}
	
	public void runSetCommand(final Object object, final String value) {
		final Object currentValue = reallyGetValue(object);
		if (Objects.equals(currentValue, value)) {
			return;
		}
		final Command command;
		if (value != null && !value.isEmpty()) {
			command = editingDomain.createCommand(SetCommand.class, new CommandParameter(object, getAttribute(object), value));
		} else {
			command = editingDomain.createCommand(SetCommand.class, new CommandParameter(object, getAttribute(object), SetCommand.UNSET_VALUE));

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
		Object v = null;
		
		if (object instanceof EObject) {
			final EObject eObject = (EObject) object;
			if (eObject.eIsSet(this.getAttribute(object))) {
				v = eObject.eGet(this.getAttribute(object));
			}
		}
		if (v == null || v == SetCommand.UNSET_VALUE) {
			return "";
		}
		else {
			return v;
		}
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

	@Override
	public final void setValue(final Object object, final Object value) {
		if (value == SetCommand.UNSET_VALUE && getAttribute(object).isUnsettable()) {
			runSetCommand(object, (String)value);
		} else {
			doSetValue(object, value);
		}
	}
}
