package com.mmxlabs.models.lng.analytics.ui.views.providers;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.ETypedElement;
import org.eclipse.jdt.annotation.Nullable;
import org.eclipse.jface.viewers.CellLabelProvider;
import org.eclipse.jface.viewers.ViewerCell;

import com.mmxlabs.models.ui.tabular.ICellRenderer;
import com.mmxlabs.models.util.emfpath.EMFPath;

public class CellFormatterLabelProvider extends CellLabelProvider {

	private final ICellRenderer renderer;
	private final EMFPath path;

	public CellFormatterLabelProvider(final ICellRenderer renderer, final ETypedElement... pathObjects) {
		this(renderer, new EMFPath(true, pathObjects));
	}

	public CellFormatterLabelProvider(final ICellRenderer renderer, @Nullable final EMFPath path) {
		this.renderer = renderer;
		this.path = path;
	}

	@Override
	public void update(final ViewerCell cell) {
		Object element = cell.getElement();
		if (element instanceof EObject && path != null) {
			element = path.get((EObject) element);
		}
		cell.setText(renderer.render(element));
	}
}
