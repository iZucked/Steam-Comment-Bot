/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2022
 * All rights reserved.
 */
package com.mmxlabs.common.curves;

public interface ILazyCurve extends IParameterisedCurve {
	void initialise();

	void clear();
}
