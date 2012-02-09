package com.mmxlabs.models.ui.impl;

import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EDataType;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.EcorePackage;

import com.mmxlabs.models.ui.editors.IInlineEditor;
import com.mmxlabs.models.ui.editors.IInlineEditorFactory;
import com.mmxlabs.models.ui.editors.impl.MultiReferenceInlineEditor;

/**
 * The inline editor factory which creates the normal editors
 * 
 * @author hinton
 *
 */
public class DefaultInlineEditorFactory implements IInlineEditorFactory {
	
	@Override
	public IInlineEditor createEditor(final EClass owner, final EStructuralFeature feature) {
		if (feature instanceof EReference) {
			final EReference reference = (EReference) feature;
			final EClass referenceClass = reference.getEReferenceType();
			if (reference.isMany()) {
				return new MultiReferenceInlineEditor(feature);
			} else {
				
			}
		} else if (feature instanceof EAttribute) {
			final EcorePackage ecore = EcorePackage.eINSTANCE;
			final EAttribute attribute = (EAttribute) feature;
			final EDataType attributeType = attribute.getEAttributeType();
			if (attributeType == ecore.getEInt()) {
				
			} else if (attributeType == ecore.getEShort()) {
				
			} else if (attributeType == ecore.getELong()) {
				
			} else if (attributeType == ecore.getEDouble()) {
				
			} else if (attributeType == ecore.getEFloat()) {
				
			} else if (attributeType == ecore.getEBoolean()) {
				
			} else if (attributeType == ecore.getEChar()) {
				
			} else if (attributeType == ecore.getEByte()) {
				
			} else if (attributeType == ecore.getEEnumerator()) {
				// handle enumerator?
			}
		}
		return null;
	}
}
