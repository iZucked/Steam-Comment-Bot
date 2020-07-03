/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2020
 * All rights reserved.
 */
package com.mmxlabs.models.lng.fleet.ui.inlineeditors;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

import org.eclipse.emf.common.util.Enumerator;
import org.eclipse.emf.databinding.EMFDataBindingContext;
import org.eclipse.emf.ecore.EAnnotation;
import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EEnum;
import org.eclipse.emf.ecore.EEnumLiteral;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.ui.forms.widgets.FormToolkit;

import com.mmxlabs.models.lng.port.RouteOption;
import com.mmxlabs.models.lng.port.util.PortModelLabeller;
import com.mmxlabs.models.ui.editors.impl.MultiEnumInlineEditor;

public class RouteExclusionMultiInlineEditor extends MultiEnumInlineEditor {

	public RouteExclusionMultiInlineEditor(EStructuralFeature feature) {
		super(feature);
		myEnum = (EEnum) ((EAttribute) feature).getEAttributeType();
		List<EEnumLiteral> enumList = myEnum.getELiterals().stream().filter(e -> !e.getLiteral().equals("DIRECT")).sorted((a, b) -> a.getName().compareTo(b.getName())).collect(Collectors.toList());
		enumerators = new Enumerator[enumList.size()];
		for (int i = 0; i < enumerators.length; i++) {
			enumerators[i] = enumList.get(i).getInstance();
		}
	}

	@Override
	public Control createControl(Composite parent, EMFDataBindingContext dbc, FormToolkit toolkit) {
		isOverridable = false;
		EAnnotation eAnnotation = feature.getEContainingClass().getEAnnotation("http://www.mmxlabs.com/models/featureOverride");
		if (eAnnotation == null) {
			eAnnotation = feature.getEContainingClass().getEAnnotation("http://www.mmxlabs.com/models/featureOverrideByContainer");
		}
		if (eAnnotation != null) {
			for (EStructuralFeature f : feature.getEContainingClass().getEAllAttributes()) {
				if (f.getName().equals(feature.getName() + "Override")) {
					isOverridable = true;
					this.overrideToggleFeature = f;
				}
			}
			if (feature.isUnsettable()) {
				isOverridable = true;
			}
		}
		if (isOverridable) {
			isOverridableWithButton = true;
		}
		return super.createControl(parent, dbc, toolkit);
	}

	@Override
	protected void updateValueDisplay(final Object value) {
		if (theLabel == null || theLabel.isDisposed()) {
			return;
		}

		final List<?> selectedValues = (List<?>) value;
		if (selectedValues != null) {
			final List<RouteOption> options = new LinkedList<>();
			for (final Object obj : selectedValues) {
				if (obj instanceof RouteOption) {
					options.add((RouteOption) obj);
				}
			}

			final StringBuilder sb = new StringBuilder();
			Collections.sort(options, (a, b) -> a.compareTo(b));
			for (final RouteOption option : options) {
				if (sb.length() > 0) {
					sb.append(", ");
				}
				sb.append(PortModelLabeller.getName(option));
			}
			theLabel.setText(sb.toString());
		}
	}

}
