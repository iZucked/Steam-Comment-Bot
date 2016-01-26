/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
package com.mmxlabs.models.ui.editors.impl;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.emf.ecore.EAttribute;

import com.mmxlabs.common.Pair;

/**
 */
public class YesNoInlineEditor extends ValueListInlineEditor {
	static private List<Pair<String, Object>> values = getValues();	
	
	public YesNoInlineEditor(EAttribute feature) {				
		super(feature, values);
	}

	private static List<Pair<String, Object>> getValues() {
		ArrayList<Pair<String, Object>> result = new ArrayList<Pair<String, Object>>(); 
		result.add(new Pair<String, Object>("No", new Boolean(false)));
		result.add(new Pair<String, Object>("Yes", new Boolean(true)));
		return result;
	}

}
