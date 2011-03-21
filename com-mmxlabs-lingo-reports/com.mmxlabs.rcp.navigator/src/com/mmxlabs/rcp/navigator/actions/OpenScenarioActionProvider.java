package com.mmxlabs.rcp.navigator;

import java.util.Iterator;
import java.util.List;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.mapping.ResourceMapping;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.jface.action.GroupMarker;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.IActionBars;
import org.eclipse.ui.actions.OpenFileAction;
import org.eclipse.ui.actions.OpenWithMenu;
import org.eclipse.ui.internal.navigator.AdaptabilityUtility;
import org.eclipse.ui.internal.navigator.resources.plugin.WorkbenchNavigatorMessages;
import org.eclipse.ui.navigator.CommonActionProvider;
import org.eclipse.ui.navigator.ICommonActionConstants;
import org.eclipse.ui.navigator.ICommonActionExtensionSite;
import org.eclipse.ui.navigator.ICommonMenuConstants;
import org.eclipse.ui.navigator.ICommonViewerWorkbenchSite;

import com.mmxlabs.rcp.navigator.actions.OpenScenarioAction;
import com.mmxlabs.rcp.navigator.scenario.ScenarioTreeNodeClass;

public class OpenScenarioActionProvider extends CommonActionProvider {

	private OpenScenarioAction openScenarioAction;

	private ICommonViewerWorkbenchSite viewSite = null;

	private boolean contribute = false;

	public void init(ICommonActionExtensionSite aConfig) {
		if (aConfig.getViewSite() instanceof ICommonViewerWorkbenchSite) {
			viewSite = (ICommonViewerWorkbenchSite) aConfig.getViewSite();
			openScenarioAction = new OpenScenarioAction(viewSite.getPage());
			contribute = true;
		}
	}

	public void fillContextMenu(IMenuManager aMenu) {
		if (!contribute || getContext().getSelection().isEmpty()) {
			return;
		}

		IStructuredSelection selection = (IStructuredSelection) getContext()
				.getSelection();

		openScenarioAction.selectionChanged(selection);
		if (openScenarioAction.isEnabled()) {
			aMenu.insertAfter(ICommonMenuConstants.GROUP_OPEN, openScenarioAction);
		}
		addOpenWithMenu(aMenu);
	}

	public void fillActionBars(IActionBars theActionBars) {
		if (!contribute) {
			return;
		}
		IStructuredSelection selection = (IStructuredSelection) getContext()
				.getSelection();

		ScenarioTreeNodeClass object = getIFile(selection);

		if (object != null) {
			openScenarioAction.selectionChanged(selection);
			theActionBars.setGlobalActionHandler(ICommonActionConstants.OPEN,
					openScenarioAction);
		}

	}

	private void addOpenWithMenu(IMenuManager aMenu) {
		IStructuredSelection ss = (IStructuredSelection) getContext()
				.getSelection();

		ScenarioTreeNodeClass openable = getIFile(ss);

		if (openable != null) {
			// Create a menu flyout.
			IMenuManager submenu = new MenuManager(
					WorkbenchNavigatorMessages.OpenActionProvider_OpenWithMenu_label,
					ICommonMenuConstants.GROUP_OPEN_WITH);
			submenu.add(new GroupMarker(ICommonMenuConstants.GROUP_TOP));
			submenu.add(new OpenWithMenu(viewSite.getPage(), openable));
			submenu.add(new GroupMarker(ICommonMenuConstants.GROUP_ADDITIONS));

			// Add the submenu.
			if (submenu.getItems().length > 2 && submenu.isEnabled()) {
				aMenu.appendToGroup(ICommonMenuConstants.GROUP_OPEN_WITH,
						submenu);
			}
		}
	}

	private ScenarioTreeNodeClass getIFile(IStructuredSelection ss) {
		if (ss == null || ss.size() != 1) {
			return null;
		}

		Object o = ss.getFirstElement();

		// first try IResource
		ScenarioTreeNodeClass openable = (ScenarioTreeNodeClass) AdaptabilityUtility.getAdapter(o,
				ScenarioTreeNodeClass.class);
		return openable;
//		// otherwise try ResourceMapping
//		if (openable == null) {
//			openable = (IAdaptable) AdaptabilityUtility.getAdapter(o,
//					ResourceMapping.class);
//		} else if (((IResource) openable).getType() != IResource.FILE) {
//			openable = null;
//		}
//		return openable;
	}
}
