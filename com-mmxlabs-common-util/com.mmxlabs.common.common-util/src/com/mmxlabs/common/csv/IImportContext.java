/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
package com.mmxlabs.common.csv;

import java.util.List;

import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.Nullable;

/**
 * A single instance of this will be used whenever some importing is happening. It tracks all of the named objects that may be referenced, and handles setting up any deferred processing which has to
 * happen after the main importing is done.
 * 
 * @author hinton
 * 
 */
public interface IImportContext {

	/**
	 * Process a deferment later; these are executed in order of {@link IDeferment#getStage()}.
	 * 
	 * 
	 * @param deferment
	 */
	public void doLater(@NonNull final IDeferment deferment);

	/**
	 * Add a reader to the reader stack; this is used to find error locations in {@link #addProblem(String, boolean, boolean)}
	 * 
	 * @param reader
	 */
	public void pushReader(@NonNull CSVReader reader);

	/**
	 * Pop a reader from the reader stack; see also {@link #pushReader(CSVReader)}
	 */
	@Nullable
	public CSVReader popReader();

	/**
	 * Add a problem message to the list of problems. See also {@link #pushReader(CSVReader)} and {@link #popReader()}
	 * 
	 * @param string
	 * @param trackFile
	 *            if true record the file that was open when this happened
	 * @param trackField
	 *            if true record the last field read when this happened
	 */
	@NonNull
	public IImportProblem createProblem(final String string, final boolean trackFile, final boolean trackLine, final boolean trackField);

	public void addProblem(@NonNull IImportProblem problem);

	/**
	 * Returns a list of the problems which have happened so far
	 * 
	 * @return
	 */
	public List<IImportProblem> getProblems();

	/**
	 * @return the current reader
	 */
	@Nullable
	public CSVReader peekReader();

	public char getDecimalSeparator();
}
