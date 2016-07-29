/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
package com.mmxlabs.models.lng.fleet.ui.inlineeditors;

import java.util.List;
import java.util.stream.Collectors;

import org.eclipse.emf.common.util.Enumerator;
import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EEnum;
import org.eclipse.emf.ecore.EEnumLiteral;
import org.eclipse.emf.ecore.EStructuralFeature;

import com.mmxlabs.models.ui.editors.impl.MultiEnumInlineEditor;

public class RouteExclusionMultiInlineEditor extends MultiEnumInlineEditor {

	public RouteExclusionMultiInlineEditor(EStructuralFeature feature) {
		super(feature);
		myEnum = (EEnum) ((EAttribute) feature).getEAttributeType();
		List<EEnumLiteral> enumList = myEnum.getELiterals().stream().filter(e-> !e.getLiteral().equals("DIRECT")).sorted((a,b) -> a.getName().compareTo(b.getName())).collect(Collectors.toList());
		enumerators = new Enumerator[enumList.size()];
		for (int i = 0; i < enumerators.length; i++) {
			enumerators[i] = enumList.get(i).getInstance();
		}
	}

}
