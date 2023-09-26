/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
package com.mmxlabs.models.lng.cargo.editor.bulk.views;

import org.eclipse.emf.ecore.EClass;


/**
 * Interface to dynamically extend a {@link CargoBulkEditorPackage} {@link Row}
 * @author achurchill
 *
 */
public interface ITradesBasedRowModelTransformerFactory {

	void extendRowModel(String rowExtensionId, EClass currentRowModel);
	
}
