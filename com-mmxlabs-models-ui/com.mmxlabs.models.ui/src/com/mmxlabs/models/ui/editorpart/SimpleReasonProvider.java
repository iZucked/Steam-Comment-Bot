/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
package com.mmxlabs.models.ui.editorpart;

import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.Nullable;

import com.mmxlabs.rcp.common.editors.IReasonProvider;

public class SimpleReasonProvider implements IReasonProvider {

	private final @NonNull Throwable throwable;

	public SimpleReasonProvider(@NonNull final Throwable throwable) {
		this.throwable = throwable;
	}

	@Override
	public String getTitle() {
		@Nullable
		String message = throwable.getMessage();
		if (message == null) {
			return "";
		}
		return message;
	}

	@Override
	public String getDescription() {
		return "An unknown internal error has occured.";
	}

	@Override
	public String getResolutionSteps() {
		return "Contact Minimax Labs with this scenario and technical details.";
	}

	@Override
	public Throwable getThrowable() {
		return throwable;
	}

}
