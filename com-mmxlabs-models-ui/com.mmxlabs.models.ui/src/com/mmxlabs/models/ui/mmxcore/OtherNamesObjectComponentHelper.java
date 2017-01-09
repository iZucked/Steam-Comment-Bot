/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
package com.mmxlabs.models.ui.mmxcore;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.emf.ecore.EAnnotation;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Text;

import com.mmxlabs.models.mmxcore.MMXCorePackage;
import com.mmxlabs.models.ui.BaseComponentHelper;
import com.mmxlabs.models.ui.IInlineEditorContainer;
import com.mmxlabs.models.ui.editors.impl.TextInlineEditor;

public class OtherNamesObjectComponentHelper extends BaseComponentHelper {
	public static final String OTHER_NAMES_ANNOTATION = "http://www.mmxlabs.com/models/mmxcore/annotations/namedobject";

	@Override
	public void addEditorsToComposite(final IInlineEditorContainer detailComposite) {
		addEditorsToComposite(detailComposite, MMXCorePackage.eINSTANCE.getOtherNamesObject());
	}

	@Override
	public void addEditorsToComposite(final IInlineEditorContainer detailComposite, final EClass displayedClass) {

		final String newLine = System.getProperty("line.separator");

		final EAnnotation annotation = displayedClass.getEAnnotation(OTHER_NAMES_ANNOTATION);
		if (annotation != null) {
			final String value = annotation.getDetails().get("showOtherNames");
			if (value != null && Boolean.valueOf(value)) {
				detailComposite.addInlineEditor(new TextInlineEditor(MMXCorePackage.eINSTANCE.getOtherNamesObject_OtherNames(), SWT.MULTI | SWT.V_SCROLL | SWT.BORDER) {
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

				});
			}
		}
	}
}
