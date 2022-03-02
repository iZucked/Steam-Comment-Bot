/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2022
 * All rights reserved.
 */
package com.mmxlabs.rcp.common.editors;

import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.Nullable;

/**
 * Interface to provide human readable information about an {@link Exception} to a user.
 * 
 * @author Simon Goodall
 * 
 */
public interface IReasonProvider {
	@NonNull
	String getTitle();

	@NonNull
	String getDescription();

	@NonNull
	String getResolutionSteps();

	@Nullable
	Throwable getThrowable();

}
