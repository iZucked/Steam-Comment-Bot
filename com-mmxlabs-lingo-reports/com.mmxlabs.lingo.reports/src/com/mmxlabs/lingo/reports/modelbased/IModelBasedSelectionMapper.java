package com.mmxlabs.lingo.reports.modelbased;

import org.eclipse.jface.viewers.ISelection;

public interface IModelBasedSelectionMapper<M> {

	ISelection adaptSelectionToExternal(ISelection selection);

	ISelection adaptSelectionToInternal(ISelection selection);

}