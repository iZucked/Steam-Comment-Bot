/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
package com.mmxlabs.rcp.common.actions;

import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.nebula.jface.gridviewer.GridTableViewer;
import org.eclipse.nebula.jface.gridviewer.GridTreeViewer;

/**
 * 
 * Helper class to create the various pack column action classes
 * 
 * @noinstantiate This class is not intended to be instantiated by clients.
 * @author Simon Goodall
 * 
 */
public final class PackActionFactory {

	private PackActionFactory() {

	}

	public static PackGridTableColumnsAction createPackColumnsAction(final GridTableViewer viewer) {
		return new PackGridTableColumnsAction(viewer);
	}

	public static PackGridTreeColumnsAction createPackColumnsAction(final GridTreeViewer viewer) {
		return new PackGridTreeColumnsAction(viewer);
	}

	public static PackTableColumnsAction createPackColumnsAction(final TableViewer viewer) {
		return new PackTableColumnsAction(viewer);
	}

	public static PackTreeColumnsAction createPackColumnsAction(final TreeViewer viewer) {
		return new PackTreeColumnsAction(viewer);
	}
}
