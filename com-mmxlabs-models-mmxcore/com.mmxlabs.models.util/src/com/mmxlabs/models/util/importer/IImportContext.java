package com.mmxlabs.models.util.importer;

import org.eclipse.emf.ecore.EClass;

import com.mmxlabs.models.mmxcore.NamedObject;

/**
 * A single instance of this will be used whenever some importing is happening. It tracks all of the named objects that may be referenced,
 * and handles setting up any deferred processing which has to happen after the main importing is done.
 * 
 * @author hinton
 *
 */
public interface IImportContext {
	/**
	 * Instances of this can be used to do things like looking up named references after the bulk of the import has been completed.
	 * 
	 * 
	 * @author hinton
	 *
	 */
	public interface IDeferment {
		/**
		 * Execute this deferment in the given context. A deferment can legally add another deferment back into the context.
		 * If this happens, execution is paused, all deferments are re-sorted, and then they are processed again
		 * @param context
		 */
		public void run(final IImportContext context);
		/**
		 * Deferments are sorted in ascending order by stage.
		 * @return
		 */
		public int getStage();
	}
	
	/**
	 * Get a named object which has been passed to {@link #registerNamedObject(NamedObject)} whose type
	 * is the closest subtype of the given type
	 * @param name
	 * @param preferredType
	 * @return
	 */
	public NamedObject getNamedObject(final String name, final EClass preferredType);
	/**
	 * Add the given named object to this context.
	 * @param object
	 */
	public void registerNamedObject(final NamedObject object);
	/**
	 * Process a deferment later; these are executed in order of {@link IDeferment#getStage()}.
	 * 
	 * 
	 * @param deferment
	 */
	public void doLater(final IDeferment deferment);
	
	/**
	 * Identify a problem in the import, to be flagged up for the user somehow.
	 * @param column
	 * @param value
	 * @param message
	 */
	public void addProblem(String column, String value, String message);
	public void pushReader(CSVReader reader);
	public void popReader();
}
