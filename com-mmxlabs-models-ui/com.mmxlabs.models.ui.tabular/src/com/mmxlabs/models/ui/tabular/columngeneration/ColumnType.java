/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
package com.mmxlabs.models.ui.tabular.columngeneration;

public enum ColumnType {
	/**
	 * Standard column, always shown
	 */
	NORMAL,
	/**
	 * Column shown when there are multiple scenarios selected
	 */
	MULTIPLE,
	/**
	 * Column shown in pin/diff mode
	 */
	DIFF,
	/**
	* Column only shown in non diff comparison mode
	*/
	DELTA,
	/**
	* Column only shown in diff comparison mode
	*/
	NONDELTA,
	/**
	* Column only shown in non diff comparison mode but their are multiple scenarios selected
	*/
	NONDELTAMULTIPLE
}