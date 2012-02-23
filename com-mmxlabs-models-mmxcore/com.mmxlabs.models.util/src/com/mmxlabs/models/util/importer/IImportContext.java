package com.mmxlabs.models.util.importer;

import org.eclipse.emf.ecore.EClass;

import com.mmxlabs.models.mmxcore.NamedObject;

public interface IImportContext {
	public interface IDeferment {
		public void run(final IImportContext context);
		public int getStage();
	}
	
	public NamedObject getNamedObject(final String name, final EClass preferredType);
	public void registerNamedObject(final NamedObject object);
	public void doLater(final IDeferment deferment);
	public void addProblem(String filename, String column, String value, int lineNumber, String message);
}
