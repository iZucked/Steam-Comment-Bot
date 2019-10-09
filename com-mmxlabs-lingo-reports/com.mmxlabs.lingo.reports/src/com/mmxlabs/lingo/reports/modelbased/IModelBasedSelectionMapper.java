/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2019
 * All rights reserved.
 */
package com.mmxlabs.lingo.reports.modelbased;

import org.eclipse.jface.viewers.ISelection;

public interface IModelBasedSelectionMapper<M> {

	ISelection adaptSelectionToExternal(ISelection selection);

	ISelection adaptSelectionToInternal(ISelection selection);

}