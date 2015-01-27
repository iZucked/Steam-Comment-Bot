package com.mmxlabs.models.ui.editorpart;

import com.mmxlabs.rcp.common.editors.IReasonProvider;

public class SimpleReasonProvider implements IReasonProvider {

	private final Throwable t;

	public SimpleReasonProvider(final Throwable t) {
		this.t = t;
	}

	@Override
	public String getTitle() {
		return t.getMessage();
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
		return t;
	}

}
