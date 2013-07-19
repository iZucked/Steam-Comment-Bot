package com.mmxlabs.models.ui.editors.impl;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Text;

/**
 * @since 6.0
 */
public class MultiTextInlineEditor extends TextInlineEditor {
	final String newLine;

	public MultiTextInlineEditor(EStructuralFeature feature) {
		super(feature, SWT.MULTI | SWT.V_SCROLL | SWT.BORDER);
		newLine = System.getProperty("line.separator");
	}
	
	@Override
	protected synchronized void doSetValue(final Object value, final boolean forceCommandExecution) {
		if (value == null)
			return;
		final String valueString = "" + value;
		final String[] values = valueString.split(newLine);
		final List<String> valueList = new ArrayList<String>(values.length);
		for (final String s : values) {
			final String s2 = s.trim();
			if (s2.isEmpty())
				continue;
			valueList.add(s2);
		}
		super.doSetValue(valueList, forceCommandExecution);
	}

	@Override
	protected void updateValueDisplay(final Object value) {
		final StringBuilder sb = new StringBuilder();

		if (value instanceof List) {
			boolean c = false;
			for (final Object o : (List) value) {
				if (c) {
					sb.append(newLine);
				} else {
					c = true;
				}
				sb.append(o);
			}
			super.updateValueDisplay(sb.toString());
		} else {
			super.updateValueDisplay(value);
		}
	}

	@Override
	protected Text createText(final Composite parent) {
		// Override the Text implementation for force a minimum text height.
		// TODO: A better way might be to store a height hint in the inline editor to pass to the layout manger
		final Text text = new Text(parent, style) {

			private static final int minHeight = 50;

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
