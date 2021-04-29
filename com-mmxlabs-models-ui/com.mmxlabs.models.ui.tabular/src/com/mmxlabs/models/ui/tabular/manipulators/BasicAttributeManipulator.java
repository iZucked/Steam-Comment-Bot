/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2021
 * All rights reserved.
 */
package com.mmxlabs.models.ui.tabular.manipulators;

import java.util.Collections;
import java.util.List;
import java.util.Objects;

import org.eclipse.emf.common.command.Command;
import org.eclipse.emf.common.command.CompoundCommand;
import org.eclipse.emf.common.notify.Notifier;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EAnnotation;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.EcorePackage;
import org.eclipse.emf.edit.command.CommandParameter;
import org.eclipse.emf.edit.command.SetCommand;
import org.eclipse.emf.edit.domain.EditingDomain;
import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.jface.viewers.TextCellEditor;
import org.eclipse.swt.widgets.Composite;

import com.mmxlabs.common.Pair;
import com.mmxlabs.models.mmxcore.MMXObject;
import com.mmxlabs.models.ui.editors.ICommandHandler;
import com.mmxlabs.models.ui.tabular.ICellManipulator;
import com.mmxlabs.models.ui.tabular.ICellRenderer;

/**
 * Displays a textbox for editing an EAttribute.
 * 
 * @author hinton
 */
public class BasicAttributeManipulator implements ICellManipulator, ICellRenderer {
	protected final EStructuralFeature field;
	protected final ICommandHandler commandHandler;
	protected boolean isOverridable;
	protected final EAnnotation overrideAnnotation;
	protected EStructuralFeature overrideToggleFeature;
	private IExtraCommandsHook extraCommandsHook;
	private Object parent;

	public BasicAttributeManipulator(final EStructuralFeature field, final ICommandHandler commandHandler) {
		super();
		this.field = field;
		this.commandHandler = commandHandler;

		overrideAnnotation = field == null ? null : field.getEContainingClass().getEAnnotation("http://www.mmxlabs.com/models/featureOverride");
		if (overrideAnnotation != null) {
			EAnnotation overrideFeatureAnnotation = field == null ? null : field.getEAnnotation("http://www.mmxlabs.com/models/overrideFeature");
			if (overrideFeatureAnnotation != null) {
				isOverridable = true;
				overrideToggleFeature = (EStructuralFeature) overrideFeatureAnnotation.getReferences().get(0);
			} else if (field.isUnsettable()) {
				isOverridable = true;
			} else {
				if (field.isMany()) {
					for (EStructuralFeature f : field.getEContainingClass().getEAllAttributes()) {
						if (f.getName().equals(field.getName() + "Override")) {
							isOverridable = true;
							overrideToggleFeature = f;
							break;
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

		// if the object for some reason does not support the field (e.g. it's a placeholder row in a table)
		// ignore it
		if (object instanceof EObject && !((EObject) object).eClass().getEAllStructuralFeatures().contains(field)) {
			return null;
		}

		if (overrideToggleFeature != null) {
			if (!(Boolean) ((EObject) object).eGet(overrideToggleFeature)) {
				return renderUnsetValue(object, (object instanceof MMXObject) ? ((MMXObject) object).getUnsetValue(field) : null);
			}
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
		if (Objects.equals(currentValue, value)) {
			return;
		}
		if (value == null) {
			return;
		}
		CompoundCommand cmd = new CompoundCommand();
		EditingDomain editingDomain = commandHandler.getEditingDomain();
		final Command command = editingDomain.createCommand(SetCommand.class, new CommandParameter(object, field, value));
		cmd.append(command);
		if (extraCommandsHook != null) {
			extraCommandsHook.applyExtraCommands(editingDomain, cmd, parent, object, value);
		}
		// command.setLabel("Set " + field.getName() + " to " + (value == null ? "null" : value.toString()));
		commandHandler.handleCommand(cmd, (EObject) object, field);
	}

	public void doSetValue(final Object object, final Object value) {
		runSetCommand(object, value);
	}

	@Override
	public final CellEditor getCellEditor(final Composite c, final Object object) {
		if (object == null) {
			return null;
		}

		if (!isOverridable && field.isUnsettable()) {
			final CellEditorWrapper wrapper = new CellEditorWrapper(c);
			wrapper.setDelegate(createCellEditor(wrapper.getInnerComposite(), object));
			return wrapper;
		}
		return createCellEditor(c, object);
	}

	protected CellEditor createCellEditor(final Composite c, final Object object) {
		return new TextCellEditor(c);
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
		EObject eObject = (EObject) object;

		if (overrideToggleFeature != null) {
			if (!(Boolean) eObject.eGet(overrideToggleFeature)) {
				return SetCommand.UNSET_VALUE;
			}
		}

		if (field.isUnsettable() && eObject.eIsSet(field) == false) {
			return SetCommand.UNSET_VALUE;
		}

		final Object result = eObject.eGet(field);
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
		if (overrideToggleFeature != null) {
			return !(Boolean) ((EObject) object).eGet(overrideToggleFeature);
		}
		if (field.isUnsettable() && ((EObject) object).eIsSet(field) == false) {
			return true;
		}
		return false;
	}

	@Override
	public boolean canEdit(final Object object) {
		if (object == null) {
			return false;
		}
		if (field != null && !field.getEContainingClass().isInstance(object)) {
			return false;
		}
		return true;
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
