/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2011
 * All rights reserved.
 */
package scenario.presentation.cargoeditor.importer;

import java.io.IOException;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;

/**
 * Manages running an import of a bunch of EObjects having a given class.
 * @author Tom Hinton
 *
 */
public class ImportSession implements Runnable {
	private Collection<DeferredReference> deferredReferences;
	private String inputFileName;
	private NamedObjectRegistry namedObjectRegistry;
	private List<EObject> importedObjects;
	
	private EClass outputObjectClass;
	
	public Collection<DeferredReference> getDeferredReferences() {
		return deferredReferences;
	}
	public void setDeferredReferences(Collection<DeferredReference> deferredReferences) {
		this.deferredReferences = deferredReferences;
	}
	public String getInputFileName() {
		return inputFileName;
	}
	public void setInputFileName(String inputFileName) {
		this.inputFileName = inputFileName;
	}
	public NamedObjectRegistry getNamedObjectRegistry() {
		return namedObjectRegistry;
	}
	public void setNamedObjectRegistry(NamedObjectRegistry namedObjectRegistry) {
		this.namedObjectRegistry = namedObjectRegistry;
	}

	public EClass getOutputObjectClass() {
		return outputObjectClass;
	}
	public void setOutputObjectClass(EClass outputObjectClass) {
		this.outputObjectClass = outputObjectClass;
	}
	@Override
	public void run() {
		importedObjects = new LinkedList<EObject>();
		final EObjectImporter importer = EObjectImporterFactory.getInstance().getImporter(outputObjectClass);
		// open input file and retrieve fields
		
		CSVReader reader;
		try {
			reader = new CSVReader(inputFileName);
			Map<String, String> row = null;
			while ((row = reader.readRow()) != null) {
				final EObject importedObject = importer.importObject(row, deferredReferences, namedObjectRegistry);
				importedObjects.add(importedObject);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	public List<EObject> getImportedObjects() {
		return Collections.unmodifiableList(importedObjects);
	}
}
