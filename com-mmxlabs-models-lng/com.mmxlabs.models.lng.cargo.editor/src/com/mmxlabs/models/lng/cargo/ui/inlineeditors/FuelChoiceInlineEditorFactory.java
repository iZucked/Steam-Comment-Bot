package com.mmxlabs.models.lng.cargo.ui.inlineeditors;

import java.util.ArrayList;

import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EStructuralFeature;

import com.mmxlabs.models.lng.cargo.FuelChoice;
import com.mmxlabs.models.ui.editors.IInlineEditor;
import com.mmxlabs.models.ui.editors.IInlineEditorFactory;
import com.mmxlabs.models.ui.editors.impl.EENumInlineEditor;

public class FuelChoiceInlineEditorFactory implements IInlineEditorFactory {

	@Override
	public IInlineEditor createEditor(final EClass owner, final EStructuralFeature feature) {

		final ArrayList<Object> objectsList = new ArrayList<>();
		for (final FuelChoice fuelChoice : FuelChoice.values()) {
			String name = fuelChoice.getName();
			switch (fuelChoice) {
			case NBO_BUNKERS -> name = "NBO + Bunkers";
			case NBO_FBO -> name = "NBO + FBO";
			}

			objectsList.add(name);
			objectsList.add(fuelChoice);
		}
		return new EENumInlineEditor((EAttribute) feature, objectsList.toArray());
	}
}
