/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
package com.mmxlabs.lingo.reports.views.tabular;

import org.eclipse.jface.viewers.IStructuredContentProvider;

/**
 * An interface for content providers to tabular views - oddly this kind of thing doesn't seem to be anywhere in JFace and you still have to make your TableViewerColumns and so on by hand.
 * 
 * @author hinton
 * 
 */
public interface IReflectiveContentProvider extends IStructuredContentProvider {
	public Class<?> getRowClass();
}
