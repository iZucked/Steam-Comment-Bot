/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2020
 * All rights reserved.
 */
package com.mmxlabs.lngdataserver.lng.importers.distanceupdate;

import java.util.function.Consumer;

import org.eclipse.emf.common.command.CompoundCommand;

public class UserUpdateStep extends UpdateStep {

	private boolean applyQuickFix;
	private boolean hasQuickFix;

	private String quickFixMsg;
	private Consumer<CompoundCommand> quickFix;

	public UserUpdateStep(final String displayString, final Consumer<CompoundCommand> action) {
		super(displayString, action);
	}

	public UserUpdateStep(final String displayString, final Consumer<CompoundCommand> action, final String quickFixMsg, final Consumer<CompoundCommand> quickFix) {
		super(displayString, action);
		this.quickFixMsg = quickFixMsg;
		this.quickFix = quickFix;
		hasQuickFix = true;

	}

	public boolean isApplyQuickFix() {
		return applyQuickFix;
	}

	public void setApplyQuickFix(final boolean applyQuickFix) {
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
