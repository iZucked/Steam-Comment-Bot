/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
package com.mmxlabs.models.lng.cargo.ui.editorpart.events;

import org.ops4j.peaberry.eclipse.ExtensionBean;
import org.ops4j.peaberry.eclipse.MapName;

/**
 */
@ExtensionBean("com.mmxlabs.models.lng.cargo.editor.VesselEventsTableContextMenuExtension")
public interface VesselEventsTableContextMenuExtensionPoint {

	@MapName("class")
	public IVesselEventsTableContextMenuExtension createInstance();

}
