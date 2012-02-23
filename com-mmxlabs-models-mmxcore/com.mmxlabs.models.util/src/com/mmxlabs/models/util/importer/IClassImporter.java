package com.mmxlabs.models.util.importer;

import java.util.Collection;
import java.util.Map;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;

public interface IClassImporter {
	public Collection<EObject> importObjects(final EClass targetClass, final String filename, final IImportContext context);
	public Collection<EObject> importObject(final EClass targetClass, final Map<String, String> row, final IImportContext context);
	public Collection<Map<String, String>> exportObjects(final Collection<EObject> objects);
}
