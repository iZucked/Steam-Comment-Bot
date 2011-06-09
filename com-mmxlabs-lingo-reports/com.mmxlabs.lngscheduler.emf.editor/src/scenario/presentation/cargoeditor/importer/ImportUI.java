/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2011
 * All rights reserved.
 */
package scenario.presentation.cargoeditor.importer;

import java.util.HashSet;
import java.util.Set;

import org.eclipse.jface.viewers.Viewer;
import org.eclipse.ui.PlatformUI;

/**
 * Hack to stop importers bogging down the UI.
 * @author Tom Hinton
 *
 */
public class ImportUI {
	static boolean isImporting = false;
	private static final Set<Viewer> refresh = new HashSet<Viewer>();
	public static synchronized boolean isImporting() {
		return isImporting;
	}
	
	public static synchronized void beginImport() {
		isImporting = true;
	}
	
	public static synchronized void endImport() {
		isImporting = false;
		
		for (final Viewer v : refresh) {
			PlatformUI.getWorkbench().getDisplay().asyncExec(
					new Runnable() {
						public void run() {
							v.refresh();
						}
					});
		}
		
		refresh.clear();
	}
	
	public static synchronized void refreshLater(final Viewer viewer) {
		refresh.add(viewer);
	}
	
	public static synchronized void refresh(final Viewer viewer) {
		if (isImporting()) {
			refreshLater(viewer);
		} else {
			viewer.refresh();
		}
	}
}
