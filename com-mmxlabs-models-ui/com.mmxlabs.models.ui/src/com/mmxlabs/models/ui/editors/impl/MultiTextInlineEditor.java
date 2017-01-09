/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
package com.mmxlabs.models.ui.editors.impl;

import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Text;

/**
 * Sub class of {@link TextInlineEditor} to display a *single* string across multiple lines. This class also enforces a minimum control height.
 * 
 */
public class MultiTextInlineEditor extends TextInlineEditor {
	private final String newLine;

	private final int minHeight;

	public MultiTextInlineEditor(final EStructuralFeature feature) {
		this(feature, 50);
	}

	public MultiTextInlineEditor(final EStructuralFeature feature, final int minHeight) {
		super(feature, SWT.MULTI | SWT.V_SCROLL | SWT.BORDER | SWT.WRAP);
		newLine = System.getProperty("line.separator");
		this.minHeight = minHeight;
	}

	@Override
	protected Text createText(final Composite parent) {
		// Override the Text implementation for force a minimum text height.
		// TODO: A better way might be to store a height hint in the inline editor to pass to the layout manger
		final Text text = new Text(parent, style) {

			@Override
			protected void checkSubclass() {
				// Disable call to allow this sub-class
				// super.checkSubclass();
			}

			@Override
			public Point computeSize(final int wHint, final int hHint, final boolean changed) {
				final Point p = super.computeSize(wHint, hHint, changed);

				if (p.y < minHeight) {
					return new Point(p.x, minHeight);
				}
				return p;
			}
		};
		return text;
	}

}
