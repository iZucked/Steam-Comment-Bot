/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
package com.mmxlabs.models.ui.properties.factory;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.Nullable;

import com.mmxlabs.models.mmxcore.MMXRootObject;
import com.mmxlabs.models.ui.properties.DetailProperty;

/**
 * A {@link IDetailPropertyFactory} creates a {@link DetailProperty} tree from the given EObject - with or with out a supporting {@link MMXRootObject}
 * 
 * @author Simon Goodall
 * 
 */
public interface IDetailPropertyFactory {

	@Nullable
	DetailProperty createProperties(@NonNull EObject eObject);

	@Nullable
	DetailProperty createProperties(@NonNull EObject eObject, @Nullable MMXRootObject rootObject);
}
