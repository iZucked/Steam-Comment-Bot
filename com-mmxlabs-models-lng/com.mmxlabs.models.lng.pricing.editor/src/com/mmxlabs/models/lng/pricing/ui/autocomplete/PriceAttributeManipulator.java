/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2019
 * All rights reserved.
 */
package com.mmxlabs.models.lng.pricing.ui.autocomplete;

import java.util.Collections;
import java.util.List;

import org.eclipse.emf.common.command.Command;
import org.eclipse.emf.common.command.CompoundCommand;
import org.eclipse.emf.common.notify.Notifier;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EAnnotation;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.EcorePackage;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.edit.command.CommandParameter;
import org.eclipse.emf.edit.command.SetCommand;
import org.eclipse.emf.edit.domain.EditingDomain;
import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.jface.viewers.TextCellEditor;
import org.eclipse.swt.widgets.Composite;

import com.mmxlabs.common.Pair;
import com.mmxlabs.models.mmxcore.MMXObject;
import com.mmxlabs.models.mmxcore.MMXRootObject;
import com.mmxlabs.models.ui.editors.autocomplete.AutoCompleteHelper;
import com.mmxlabs.models.ui.editors.autocomplete.IMMXContentProposalProvider;
import com.mmxlabs.models.ui.tabular.ICellManipulator;
import com.mmxlabs.models.ui.tabular.ICellRenderer;
import com.mmxlabs.models.ui.tabular.manipulators.CellEditorWrapper;

/**
 * Displays a textbox for editing an EAttribute.
 * 
 * @author hinton
 */
public class PriceAttributeManipulator implements ICellManipulator, ICellRenderer {
	protected final EStructuralFeature field;
	protected final EditingDomain editingDomain;
	protected boolean isOverridable;
	private final EAnnotation overrideAnnotation;
	private IMMXContentProposalProvider proposalHelper;
	private String expressionType;
	private IExtraCommandsHook extraCommandsHook;
	private Object parent;

	public PriceAttributeManipulator(final EStructuralFeature field, final EditingDomain editingDomain, String expressionType) {
		super();
		this.field = field;
		this.editingDomain = editingDomain;
		this.expressionType = expressionType;

		overrideAnnotation = field == null ? null : field.getEContainingClass().getEAnnotation("http://www.mmxlabs.com/models/featureOverride");
		if (overrideAnnotation != null) {
			if (field.isUnsettable()) {
				isOverridable = true;
			} else {
				if (field.isMany()) {
					for (EStructuralFeature f : field.getEContainingClass().getEAllAttributes()) {
						if (f.getName().equals(field.getName() + "Override")) {
							isOverridable = true;
						}
					}
				}
			}
		}
	}

	@Override
	public String render(final Object object) {

		if (object == null) {
			return null;
		}

		// if the object for some reason does not support the field (e.g. it's a
		// placeholder row in a table)
		// ignore it
		if (object instanceof EObject && !((EObject) object).eClass().getEAllStructuralFeatures().contains(field)) {
			return null;
		}

		if ((object instanceof EObject) && (field.isUnsettable()) && !((EObject) object).eIsSet(field)) {
			if (isOverridable) {
				EList<EObject> references = overrideAnnotation.getReferences();
				for (EObject o : references) {
					if (o instanceof EStructuralFeature) {
						EStructuralFeature eStructuralFeature = (EStructuralFeature) o;
						EObject parent = (EObject) ((EObject) object).eGet(eStructuralFeature);
						if (parent != null) {
							return renderUnsetValue(object, parent.eGet(field));
						}
					}
				}
			}
			return renderUnsetValue(object, (object instanceof MMXObject) ? ((MMXObject) object).getUnsetValue(field) : null);
		} else {
			return renderSetValue(object, getValue(object));
		}
	}

	protected String renderUnsetValue(final Object container, final Object unsetDefault) {
		return renderSetValue(container, unsetDefault);
	}

	protected String renderSetValue(final Object container, final Object setValue) {
		return setValue == null ? null : setValue.toString();
	}

	@Override
	public final void setValue(final Object object, final Object value) {
		if (value == null && isOverridable) {
			runSetCommand(object, SetCommand.UNSET_VALUE);

		} else if (value == SetCommand.UNSET_VALUE && field.isUnsettable()) {
			runSetCommand(object, value);
		} else {
			doSetValue(object, value);
		}
	}

	public void runSetCommand(final Object object, final Object value) {
		final Object currentValue = reallyGetValue(object);
		if (((currentValue == null) && (value == null)) || (((currentValue != null) && (value != null)) && currentValue.equals(value))) {
			return;
		}

		final Command command = editingDomain.createCommand(SetCommand.class, new CommandParameter(object, field, value));
		CompoundCommand cmd = new CompoundCommand();
		cmd.append(command);
		if (extraCommandsHook != null) {
			extraCommandsHook.applyExtraCommands(editingDomain, cmd, parent, object, value);
		}
		editingDomain.getCommandStack().execute(cmd);
	}

	public void doSetValue(final Object object, final Object value) {
		runSetCommand(object, value);
	}

	@Override
	public final CellEditor getCellEditor(final Composite c, final Object object) {
		if (object == null) {
			return null;
		}

		if (field.isUnsettable()) {
			final CellEditorWrapper wrapper = new CellEditorWrapper(c);
			wrapper.setDelegate(createCellEditor(wrapper.getInnerComposite(), object));
			return wrapper;
		}
		return createCellEditor(c, object);
	}

	protected CellEditor createCellEditor(final Composite c, final Object object) {
		TextCellEditor text = new TextCellEditor(c);
		this.proposalHelper = AutoCompleteHelper.createControlProposalAdapter(text.getControl(), expressionType);
		for (Resource r : editingDomain.getResourceSet().getResources()) {
			for (EObject o : r.getContents()) {
				if (o instanceof MMXRootObject) {
					this.proposalHelper.setRootObject((MMXRootObject) o);
				}
			}
		}
		return text;
	}

	@Override
	public Object getValue(final Object object) {
		if (object == null) {
			return null;
		}

		return reallyGetValue(object);
	}

	@Override
	public Object getFilterValue(final Object object) {
		return getComparable(object);
	}

	private Object reallyGetValue(final Object object) {
		if (object == null) {
			return null;
		}
		if (field.isUnsettable() && ((EObject) object).eIsSet(field) == false)
			return SetCommand.UNSET_VALUE;
		final Object result = ((EObject) object).eGet(field);
		if ((result == null) && (field.getEType() == EcorePackage.eINSTANCE.getEString())) {
			return "";
		} else {
			return result;
		}
	}

	@Override
	public boolean isValueUnset(final Object object) {
		if (object == null) {
			return false;
		}
		if (field.isUnsettable() && ((EObject) object).eIsSet(field) == false) {
			return true;
		}
		return false;
	}

	@Override
	public boolean canEdit(final Object object) {
		return object != null;
	}

	@SuppressWarnings("rawtypes")
	@Override
	public Comparable getComparable(final Object object) {
		return render(object);
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
