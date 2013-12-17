/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2013
 * All rights reserved.
 */
package com.mmxlabs.models.util.importer;

import org.eclipse.jdt.annotation.NonNull;

import com.mmxlabs.models.mmxcore.MMXRootObject;

/**
 * An interface defining a callback function to be executed after the main model import code has run and any deferred actions have been executed. Such callbacks can be used to ensure model consistency
 * after import.
 * 
 * @author Simon Goodall
 * @since 8.0
 * 
 */
public interface IPostModelImporter {

	void onPostModelImport(@NonNull IImportContext context, @NonNull MMXRootObject rootObject);
}
