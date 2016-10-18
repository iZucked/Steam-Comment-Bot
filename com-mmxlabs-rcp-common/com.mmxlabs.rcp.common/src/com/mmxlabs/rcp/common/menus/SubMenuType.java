/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
package com.mmxlabs.rcp.common.menus;

import org.eclipse.jdt.annotation.NonNull;

public class SubMenuType implements IMenuType {
	private final @NonNull SubLocalMenuHelper subMenu;

	public SubMenuType(@NonNull SubLocalMenuHelper subMenu) {
		this.subMenu = subMenu;
	}

	public @NonNull SubLocalMenuHelper getSubMenu() {
		return subMenu;
	}
}