/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2011
 * All rights reserved.
 */
package com.mmxlabs.models.ui.mmxcore;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.emf.ecore.EClass;

import com.mmxlabs.models.mmxcore.MMXCorePackage;
import com.mmxlabs.models.ui.BaseComponentHelper;
import com.mmxlabs.models.ui.ComponentHelperUtils;
import com.mmxlabs.models.ui.IInlineEditorContainer;
import com.mmxlabs.models.ui.editors.impl.TextInlineEditor;

public class NamedObjectComponentHelper extends BaseComponentHelper {
	@Override
	public void addEditorsToComposite(IInlineEditorContainer detailComposite) {
		addEditorsToComposite(detailComposite, MMXCorePackage.eINSTANCE.getNamedObject());
	}

	@Override
	public void addEditorsToComposite(IInlineEditorContainer detailComposite,
			EClass displayedClass) {
		detailComposite.addInlineEditor(ComponentHelperUtils.createDefaultEditor(displayedClass, MMXCorePackage.eINSTANCE.getNamedObject_Name()));
		detailComposite.addInlineEditor(new TextInlineEditor(MMXCorePackage.eINSTANCE.getNamedObject_OtherNames()) {
			@Override
			protected synchronized void doSetValue(Object value,
					boolean forceCommandExecution) {
				if (value == null) return;
				final String valueString = "" + value;
				final String[] values = valueString.split(",");
				final List<String> valueList = new ArrayList<String>(values.length);
				for (final String s : values) {
					final String s2 = s.trim();
					if (s2.isEmpty()) continue;
					valueList.add(s2);
				}
				super.doSetValue(valueList, forceCommandExecution);
			}

			@Override
			protected void updateValueDisplay(Object value) {
				final StringBuilder sb = new StringBuilder();
				
				if (value instanceof List) {
					boolean c = false;
					for (final Object o : (List) value) {
						if (c) {
							sb.append(", ");
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
		});
	}
}
