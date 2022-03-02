/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2022
 * All rights reserved.
 */
package com.mmxlabs.common.csv;

/**
 * Describes an import problem
 * 
 * @author hinton
 * 
 */
public interface IImportProblem {
	/**
	 * If this problem is file-specific, returns the filename of the problematic
	 * file
	 * 
	 * Otherwise returns null
	 * 
	 * @return
	 */
	String getFilename();

	/**
	 * If this problem is line-specific, returns the line number of the problem.
	 * Otherwise null.
	 * 
	 * @return
	 */
	Integer getLineNumber();

	/**
	 * If this problem is field-specific, returns the field name of the problem.
	 * Otherwise null.
	 * 
	 * @return
	 */
	String getField();

	/**
	 * Returns the problem message; should not be null.
	 * 
	 * @return
	 */
	String getProblemDescription();
}