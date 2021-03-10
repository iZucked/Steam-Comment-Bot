/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2021
 * All rights reserved.
 */
package com.mmxlabs.models.lng.port.ui.editorpart;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

import org.eclipse.emf.common.notify.Notifier;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.swt.widgets.Control;

import com.mmxlabs.common.Equality;
import com.mmxlabs.common.Pair;
import com.mmxlabs.models.lng.port.ui.editors.PortMultiReferenceInlineEditor;
import com.mmxlabs.models.ui.editors.ICommandHandler;
import com.mmxlabs.models.ui.editors.util.CommandUtil;
import com.mmxlabs.models.ui.tabular.manipulators.DialogFeatureManipulator;
import com.mmxlabs.models.ui.valueproviders.IReferenceValueProvider;
import com.mmxlabs.models.ui.valueproviders.IReferenceValueProviderProvider;

/**
 * @author hinton
 * @since 3.1
 * 
 */
public class MultiplePortReferenceManipulator extends DialogFeatureManipulator {

	/** @see PortMultiReferenceInlineEditor */
	private static final int MAX_DISPLAY_LENGTH = 32;
	private static final int MIN_DISPLAY_NAMES = 2;

	private final com.mmxlabs.models.ui.valueproviders.IReferenceValueProvider valueProvider;
	private final EAttribute nameAttribute;

	public MultiplePortReferenceManipulator(final EStructuralFeature field, final ICommandHandler commandHandler, final IReferenceValueProvider valueProvider, final EAttribute nameAttribute) {
		super(field, commandHandler);
		this.valueProvider = valueProvider;
		this.nameAttribute = nameAttribute;
	}

	public MultiplePortReferenceManipulator(final EStructuralFeature field, final IReferenceValueProviderProvider providerProvider, final ICommandHandler commandHandler,
			final EAttribute nameAttribute) {
		this(field, commandHandler, providerProvider.getReferenceValueProvider(field.getEContainingClass(), (EReference) field), nameAttribute);
	}

	@Override
	protected String renderValue(final Object value) {
		if (!(value instanceof List)) {
			return "";
		}
		final List<? extends EObject> selectedValues = (List<? extends EObject>) value;
		final StringBuilder sb = new StringBuilder();
		int numNamesAdded = 0;
		for (final EObject obj : selectedValues) {
			String name = obj.eGet(nameAttribute).toString();
			if (sb.length() > 0) {
				sb.append(", ");
			}
			if (sb.length() + name.length() <= MAX_DISPLAY_LENGTH || numNamesAdded <= MIN_DISPLAY_NAMES - 1) {
				sb.append(name);
				++numNamesAdded;
			} else {
				sb.append("...");
				break;
			}
		}
		return sb.toString();
	}

	@Override
	public void doSetValue(final Object object, final Object value) {
		final Object currentValue = getValue(object);
		if (Equality.isEqual(currentValue, value)) {
			return;
		}
		commandHandler.handleCommand(CommandUtil.createMultipleAttributeSetter(commandHandler.getEditingDomain(), (EObject) object, field, (Collection) value), (EObject)object, field);
	}

	@SuppressWarnings("unchecked")
	@Override
	protected Object openDialogBox(final Control cellEditorWindow, final Object object) {
		final List<Pair<String, EObject>> options = valueProvider.getAllowedValues((EObject) object, field);

		if ((!options.isEmpty()) && (options.get(0).getSecond() == null)) {
			options.remove(0);
		}

		PortPickerDialog picker = new PortPickerDialog(cellEditorWindow.getShell(), options.toArray());
		return picker.pick(options, (List<EObject>) getValue(object), (EReference) field);
	}

	@Override
	public Iterable<Pair<Notifier, List<Object>>> getExternalNotifiers(final Object object) {
		if (object != null) {
			final EList<EObject> values = (EList) super.getValue(object);
			final LinkedList<Pair<Notifier, List<Object>>> notifiers = new LinkedList<>();
			for (final EObject ref : values) {
				for (final Pair<Notifier, List<Object>> p : valueProvider.getNotifiers((EObject) object, (EReference) field, ref)) {
					notifiers.add(p);
				}
			}
			return notifiers;
		}

		return super.getExternalNotifiers(object);
	}

}
