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
 * @since 4.2
 * 
 */
public final class PackActionFactory {

	private PackActionFactory() {

	}

	public PackGridTableColumnsAction createPackColumnsAction(final GridTableViewer viewer) {
		return new PackGridTableColumnsAction(viewer);
	}

	public PackGridTreeColumnsAction createPackColumnsAction(final GridTreeViewer viewer) {
		return new PackGridTreeColumnsAction(viewer);
	}

	public PackTableColumnsAction createPackColumnsAction(final TableViewer viewer) {
		return new PackTableColumnsAction(viewer);
	}

	public PackTreeColumnsAction createPackColumnsAction(final TreeViewer viewer) {
		return new PackTreeColumnsAction(viewer);
	}
}
