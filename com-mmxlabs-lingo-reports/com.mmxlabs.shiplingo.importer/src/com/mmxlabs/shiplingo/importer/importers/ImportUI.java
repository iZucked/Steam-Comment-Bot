/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2012
 * All rights reserved.
 */
package com.mmxlabs.shiplingo.importer.importers;

import java.util.HashSet;
import java.util.Set;

import org.eclipse.jface.viewers.Viewer;
import org.eclipse.ui.PlatformUI;

/**
 * Hack to stop importers bogging down the UI.
 * 
 * @author Tom Hinton
 * 
 */
public class ImportUI {
	static boolean isImporting = false;
	private static final Set<Viewer> refresh = new HashSet<Viewer>();

	/**
	 * If true, an import job is currently running, and you shouldn't do a refresh at the moment.
	 * 
	 * @return
	 */
	public static synchronized boolean isImporting() {
		return isImporting;
	}

	/**
	 * Call this to say that an import is starting
	 */
	public static synchronized void beginImport() {
		isImporting = true;
	}

	/**
	 * Call this when your import is done; any deferred refreshes will happen.
	 */
	public static synchronized void endImport() {
		isImporting = false;

		// TODO should we do one runnable which refreshes everything?
		for (final Viewer v : refresh) {
			PlatformUI.getWorkbench().getDisplay().asyncExec(new Runnable() {
				@Override
				public void run() {
					v.refresh();
				}
			});
		}

		refresh.clear();
	}

	/**
	 * Refresh a viewer next time an import task finishes.
	 * 
	 * @param viewer
	 */
	public static synchronized void refreshLater(final Viewer viewer) {
		refresh.add(viewer);
	}

	/**
	 * Refresh the viewer now, if no import, or later, if importing.
	 * 
	 * @param viewer
	 */
	public static synchronized void refresh(final Viewer viewer) {
		if (isImporting()) {
			refreshLater(viewer);
		} else {
			viewer.refresh();
		}
	}
}
