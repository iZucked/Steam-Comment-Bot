/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
package com.mmxlabs.common.curves;

import java.util.Map;

import org.eclipse.jdt.annotation.NonNull;

/**
 * An interface for a curve, which uses integer precision.
 * 
 * @author hinton, sg
 * 
 */
public interface IParameterisedCurve {

	boolean hasParameters();

	int getValueAtPoint(int point, @NonNull Map<String, String> params);

}
