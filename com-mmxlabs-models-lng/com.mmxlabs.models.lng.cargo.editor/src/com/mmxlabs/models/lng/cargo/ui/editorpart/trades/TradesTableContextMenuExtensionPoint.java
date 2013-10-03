package com.mmxlabs.models.lng.cargo.ui.editorpart.trades;

import org.ops4j.peaberry.eclipse.ExtensionBean;
import org.ops4j.peaberry.eclipse.MapName;

@ExtensionBean("com.mmxlabs.models.lng.cargo.editor.TradesTableContextMenuExtension")
public interface TradesTableContextMenuExtensionPoint {

	@MapName("class")
	public ITradesTableContextMenuExtension createInstance();

}
