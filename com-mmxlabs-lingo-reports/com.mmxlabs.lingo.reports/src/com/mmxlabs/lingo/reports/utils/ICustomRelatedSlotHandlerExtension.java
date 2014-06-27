/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2014
 * All rights reserved.
 */
package com.mmxlabs.lingo.reports.utils;

import org.ops4j.peaberry.eclipse.ExtensionBean;
import org.ops4j.peaberry.eclipse.MapName;

@ExtensionBean("com.mmxlabs.lingo.reports.CustomRelatedSlotHandlerExtension")
public interface ICustomRelatedSlotHandlerExtension {

	@MapName("class")
	ICustomRelatedSlotHandler getInstance();
}