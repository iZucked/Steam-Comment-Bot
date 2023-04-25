/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
package com.mmxlabs.models.ui.tabular.columngeneration;

public interface BlockColumnFactoryDisplayNameChanger extends EmfBlockColumnFactory {
	void setColumnNameByID(final String columnID, final String newName);
}
