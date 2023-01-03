/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
package com.mmxlabs.common.curves;

public interface ILazyCurve extends IParameterisedCurve {
	void initialise();

	void clear();
}
