package com.mmxlabs.lngdataserver.browser.ui.context;

import org.eclipse.jdt.annotation.NonNullByDefault;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.viewers.TreeSelection;

@NonNullByDefault
public interface IDataBrowserContextMenuExtension {

	/**
	 * Contribute items to the current menu. Return true if items added
	 * 
	 * @param selection
	 * @param menuManager
	 * @return
	 */
	boolean contributeToMenu(TreeSelection selection, MenuManager menuManager);
}
