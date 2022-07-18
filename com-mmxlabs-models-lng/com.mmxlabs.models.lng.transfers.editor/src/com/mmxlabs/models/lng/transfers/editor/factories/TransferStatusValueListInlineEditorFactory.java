package com.mmxlabs.models.lng.transfers.editor.factories;

import java.util.ArrayList;

import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.ETypedElement;

import com.mmxlabs.models.lng.transfers.TransferStatus;
import com.mmxlabs.models.ui.editors.IInlineEditor;
import com.mmxlabs.models.ui.editors.IInlineEditorFactory;
import com.mmxlabs.models.ui.editors.impl.EENumInlineEditor;

/**
 * Inline editor for Transfer Status Enum
 * @author FM
 *
 */
public class TransferStatusValueListInlineEditorFactory implements IInlineEditorFactory {
	@Override
	public IInlineEditor createEditor(final EClass owner, final ETypedElement typedElement) {
		
		ArrayList<Object> objectsList = new ArrayList<>();
		for (final TransferStatus type : TransferStatus.values()) {
			final String name;
			switch (type) {
			case CONFIRMED:
				name = "Confirmed";
				break;
			case DRAFT:
				name = "Draft";
				break;
			case CANCELLED:
				name = "Cancelled";
				break;
			default:
				name = type.getName();
				break;

			}
			objectsList.add(name);
			objectsList.add(type);
		}
		return new EENumInlineEditor((EAttribute) typedElement, objectsList.toArray());
	}
}