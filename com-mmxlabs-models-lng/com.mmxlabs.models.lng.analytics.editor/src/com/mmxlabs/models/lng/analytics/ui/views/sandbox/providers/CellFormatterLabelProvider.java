/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2020
 * All rights reserved.
 */
package com.mmxlabs.models.lng.analytics.ui.views.sandbox.providers;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.ETypedElement;
import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.Nullable;
import org.eclipse.jface.viewers.CellLabelProvider;
import org.eclipse.jface.viewers.ViewerCell;
import org.eclipse.swt.graphics.Image;

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
		cell.setImage(getImage(cell, element));
	}

	protected @Nullable Image getImage(final @NonNull ViewerCell cell, final @Nullable Object element) {
		return null;
	}
}
