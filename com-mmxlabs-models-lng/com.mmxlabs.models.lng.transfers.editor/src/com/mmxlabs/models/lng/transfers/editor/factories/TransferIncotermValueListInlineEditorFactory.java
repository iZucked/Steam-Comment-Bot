package com.mmxlabs.models.lng.transfers.editor.factories;

import java.util.ArrayList;

import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EStructuralFeature;

import com.mmxlabs.models.lng.transfers.TransferIncoterm;
import com.mmxlabs.models.ui.editors.IInlineEditor;
import com.mmxlabs.models.ui.editors.IInlineEditorFactory;
import com.mmxlabs.models.ui.editors.impl.EENumInlineEditor;

/**
 * Inline editor for Transfer Incoterm Enum
 * @author FM
 *
 */
public class TransferIncotermValueListInlineEditorFactory implements IInlineEditorFactory {
	@Override
	public IInlineEditor createEditor(final EClass owner, final EStructuralFeature feature) {
		
		ArrayList<Object> objectsList = new ArrayList<>();
		for (final TransferIncoterm type : TransferIncoterm.values()) {
			final String name;
			switch (type) {
			case BOTH:
				name = "Any";
				break;
			case FOB:
				name = "FOB";
				break;
			case DES:
				name = "DES";
				break;
			default:
				name = type.getName();
				break;

			}
			objectsList.add(name);
			objectsList.add(type);
		}
		return new EENumInlineEditor((EAttribute) feature, objectsList.toArray());
	}
}