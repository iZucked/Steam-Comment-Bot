/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2013
 * All rights reserved.
 */
package com.mmxlabs.models.util.importer;

import java.util.Collection;
import java.util.List;

import org.eclipse.emf.ecore.EClass;

import com.mmxlabs.models.mmxcore.MMXRootObject;
import com.mmxlabs.models.mmxcore.NamedObject;

/**
 * A single instance of this will be used whenever some importing is happening. It tracks all of the named objects that may be referenced, and handles setting up any deferred processing which has to
 * happen after the main importing is done.
 * 
 * @author hinton
 * 
 */
public interface IImportContext {
	/**
	 * This stage is intended for any simple modifications to attributes, which just require that the object be largely OK
	 */
	public static final int STAGE_MODIFY_ATTRIBUTES = 5;
	/**
	 * This is the stage during which references are resolved
	 */
	public static final int STAGE_RESOLVE_REFERENCES = 10;
	/**
	 * This is the stage during which references that depend on other references are resolved
	 */
	public static final int STAGE_RESOLVE_CROSSREFERENCES = 20;
	/**
	 * This is for anything which requires all references to be resolved
	 */
	public static final int STAGE_REFERENCES_RESOLVED = 25;
	/**
	 * This is for things which do arbitrary changes and require most stuff to be in place.
	 */
	public static final int STAGE_MODIFY_SUBMODELS = 30;

	/**
	 * Instances of this can be used to do things like looking up named references after the bulk of the import has been completed.
	 * 
	 * 
	 * @author hinton
	 * 
	 */
	public interface IDeferment {
		/**
		 * Execute this deferment in the given context. A deferment can legally add another deferment back into the context. If this happens, execution is paused, all deferments are re-sorted, and
		 * then they are processed again
		 * 
		 * @param context
		 */
		public void run(final IImportContext context);

		/**
		 * Deferments are sorted in ascending order by stage.
		 * 
		 * @return
		 */
		public int getStage();
	}

	/**
	 * Describes an import problem
	 * 
	 * @author hinton
	 * 
	 */
	public interface IImportProblem {
		/**
		 * If this problem is file-specific, returns the filename of the problematic file
		 * 
		 * Otherwise returns null
		 * 
		 * @return
		 */
		public String getFilename();

		/**
		 * If this problem is line-specific, returns the line number of the problem. Otherwise null.
		 * 
		 * @return
		 */
		public Integer getLineNumber();

		/**
		 * If this problem is field-specific, returns the field name of the problem. Otherwise null.
		 * 
		 * @return
		 */
		public String getField();

		/**
		 * Returns the problem message; should not be null.
		 * 
		 * @return
		 */
		public String getProblemDescription();
	}

	public MMXRootObject getRootObject();

	/**
	 * Get a named object which has been passed to {@link #registerNamedObject(NamedObject)} whose type is the closest subtype of the given type
	 * 
	 * @param name
	 * @param preferredType
	 * @return
	 */
	public NamedObject getNamedObject(final String name, final EClass preferredType);

	/**
	 * Returns a {@link Collection} of all objects which have been passed to {@link #registerNamedObject(NamedObject)} with the given name.
	 * 
	 * @param name
	 * @return
	 */
	public Collection<NamedObject> getNamedObjects(String name);

	/**
	 * Add the given named object to this context.
	 * 
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
	 * Add a reader to the reader stack; this is used to find error locations in {@link #addProblem(String, boolean, boolean)}
	 * 
	 * @param reader
	 */
	public void pushReader(CSVReader reader);

	/**
	 * Pop a reader from the reader stack; see also {@link #pushReader(CSVReader)}
	 */
	public void popReader();

	/**
	 * Add a problem message to the list of problems. See also {@link #pushReader(CSVReader)} and {@link #popReader()}
	 * 
	 * @param string
	 * @param trackFile
	 *            if true record the file that was open when this happened
	 * @param trackField
	 *            if true record the last field read when this happened
	 */
	public IImportProblem createProblem(final String string, final boolean trackFile, final boolean trackLine, final boolean trackField);

	public void addProblem(IImportProblem problem);

	/**
	 * Returns a list of the problems which have happened so far
	 * 
	 * @return
	 */
	public List<IImportProblem> getProblems();

	/**
	 * @return the current reader
	 */
	public CSVReader peekReader();

}
