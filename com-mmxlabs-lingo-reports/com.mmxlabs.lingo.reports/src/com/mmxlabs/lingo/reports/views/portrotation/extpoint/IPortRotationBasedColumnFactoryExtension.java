/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
package com.mmxlabs.lingo.reports.views.portrotation.extpoint;

import org.ops4j.peaberry.eclipse.ExtensionBean;
import org.ops4j.peaberry.eclipse.MapName;

import com.mmxlabs.lingo.reports.views.portrotation.IPortRotationColumnFactory;

@ExtensionBean("com.mmxlabs.lingo.reports.PortRotationBasedColumnFactory")
public interface IPortRotationBasedColumnFactoryExtension {

	@MapName("handlerID")
	String getHandlerID();

	@MapName("factory")
	IPortRotationColumnFactory getFactory();

}
