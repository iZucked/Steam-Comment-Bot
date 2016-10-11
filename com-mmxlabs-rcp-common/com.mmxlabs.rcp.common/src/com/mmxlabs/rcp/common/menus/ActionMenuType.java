package com.mmxlabs.rcp.common.menus;

import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jface.action.IAction;

public class ActionMenuType implements IMenuType {
	private final @NonNull IAction action;

	public ActionMenuType(@NonNull IAction action) {
		this.action = action;
	}

	public @NonNull IAction getAction() {
		return action;
	}
}