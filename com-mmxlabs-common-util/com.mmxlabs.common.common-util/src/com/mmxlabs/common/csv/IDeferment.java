/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
package com.mmxlabs.common.csv;

import org.eclipse.jdt.annotation.NonNull;

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
	public void run(@NonNull final IImportContext context);

	/**
	 * Deferments are sorted in ascending order by stage.
	 * 
	 * @return
	 */
	public int getStage();
}