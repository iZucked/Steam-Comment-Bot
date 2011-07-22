/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2011
 * All rights reserved.
 */
package scenario.presentation.cargoeditor.detailview;

import java.util.LinkedList;
import java.util.List;

import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EEnum;
import org.eclipse.emf.ecore.EEnumLiteral;
import org.eclipse.emf.edit.domain.EditingDomain;

import scenario.presentation.cargoeditor.detailview.EObjectDetailView.ICommandProcessor;

import com.mmxlabs.common.Pair;
import com.mmxlabs.lngscheduler.emf.extras.EMFPath;

public class EENumInlineEditor extends ValueListInlineEditor {
	public EENumInlineEditor(EMFPath path, EAttribute feature,
			EditingDomain editingDomain, final ICommandProcessor processor) {
		super(path, feature, editingDomain, processor,
				getValues((EEnum) feature.getEAttributeType()));
	}

	private static List<Pair<String, Object>> getValues(final EEnum eenum) {
		final LinkedList<Pair<String, Object>> values = new LinkedList<Pair<String, Object>>();
		for (final EEnumLiteral literal : eenum.getELiterals()) {
			values.add(new Pair<String, Object>(literal.getName(), literal
					.getInstance()));
		}
		return values;
	}
}
