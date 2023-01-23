/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
package com.mmxlabs.lngdataserver.lng.importers.update;

import java.util.function.Consumer;

import org.eclipse.emf.common.command.CompoundCommand;

public class UpdateWarning extends UpdateStep {

	private boolean applyQuickFix;
	private boolean hasQuickFix;

	private String quickFixMsg;
	private Consumer<CompoundCommand> quickFix;

	public UpdateWarning(String displayString, Consumer<CompoundCommand> action) {
		super(displayString, action);
	}

	public UpdateWarning(String displayString) {
		super(displayString, c -> {
		});
	}

	public UpdateWarning(String displayString, String quickFixMsg, Consumer<CompoundCommand> quickFix) {
		super(displayString, null);
		this.quickFixMsg = quickFixMsg;
		this.quickFix = quickFix;
		hasQuickFix = true;
	}

	public boolean isApplyQuickFix() {
		return applyQuickFix;
	}

	public void setApplyQuickFix(boolean applyQuickFix) {
		this.applyQuickFix = applyQuickFix;
	}

	public Consumer<CompoundCommand> getQuickFix() {
		return quickFix;
	}

	public boolean isHasQuickFix() {
		return hasQuickFix;
	}

	public String getQuickFixMsg() {
		return quickFixMsg;
	}
}
