/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
package com.mmxlabs.models.ui;

import java.util.Collections;
import java.util.List;

import org.eclipse.emf.ecore.EObject;

import com.mmxlabs.models.mmxcore.MMXRootObject;

public abstract class BaseComponentHelper implements IComponentHelper {

	@Override
	public List<EObject> getExternalEditingRange(MMXRootObject root,
			EObject value) {
		return Collections.emptyList();
	}

	@Override
	public int getDisplayPriority() {
		return 0;
	}
}
