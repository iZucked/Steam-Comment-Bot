/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2022
 * All rights reserved.
 */
package com.mmxlabs.models.util.emfpath;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.jdt.annotation.Nullable;

public interface IEMFPath {

	@Nullable
	Object get(EObject root);

	@Nullable
	Object get(EObject root, int depth);

}
