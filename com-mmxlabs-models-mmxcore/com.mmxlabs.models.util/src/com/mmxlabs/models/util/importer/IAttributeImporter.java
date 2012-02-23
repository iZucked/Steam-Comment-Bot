package com.mmxlabs.models.util.importer;

import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EObject;

public interface IAttributeImporter {
	public void setAttribute(final EObject container, EAttribute attribute, final String value, final IImportContext context);
	public String writeAttribute(final EObject container, final EAttribute attribute, final Object value);
}
