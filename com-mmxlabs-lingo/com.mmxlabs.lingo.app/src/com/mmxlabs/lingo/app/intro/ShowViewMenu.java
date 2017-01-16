/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
package com.mmxlabs.lingo.app.intro;

import java.net.MalformedURLException;
import java.net.URL;
import java.text.Collator;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.eclipse.core.commands.Command;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.commands.IParameter;
import org.eclipse.core.commands.NotEnabledException;
import org.eclipse.core.commands.NotHandledException;
import org.eclipse.core.commands.Parameterization;
import org.eclipse.core.commands.ParameterizedCommand;
import org.eclipse.core.commands.common.NotDefinedException;
import org.eclipse.e4.ui.model.application.MApplication;
import org.eclipse.e4.ui.model.application.descriptor.basic.MPartDescriptor;
import org.eclipse.e4.ui.model.application.ui.basic.MPart;
import org.eclipse.e4.ui.workbench.modeling.EModelService;
import org.eclipse.e4.ui.workbench.modeling.EPartService;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.ContributionItem;
import org.eclipse.jface.action.IContributionItem;
import org.eclipse.jface.action.IMenuListener;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.action.Separator;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.ui.IPluginContribution;
import org.eclipse.ui.IWorkbenchCommandConstants;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.activities.WorkbenchActivityHelper;
import org.eclipse.ui.commands.ICommandService;
import org.eclipse.ui.handlers.IHandlerService;
import org.eclipse.ui.internal.IWorkbenchHelpContextIds;
import org.eclipse.ui.internal.WorkbenchMessages;
import org.eclipse.ui.internal.WorkbenchPlugin;
import org.eclipse.ui.internal.WorkbenchWindow;
import org.eclipse.ui.internal.intro.IIntroConstants;
import org.eclipse.ui.internal.registry.ViewDescriptor;
import org.eclipse.ui.menus.CommandContributionItem;
import org.eclipse.ui.menus.CommandContributionItemParameter;
import org.eclipse.ui.services.IServiceLocator;
import org.eclipse.ui.views.IViewDescriptor;
import org.eclipse.ui.views.IViewRegistry;

/**
 * A <code>ShowViewMenu</code> is used to populate a menu manager with Show View actions. The visible views are determined by user preference from the Perspective Customize dialog.
 * 
 * 
 * Custom version to link up e4 views from perspective short cuts.
 * TODO: Fix up dialog box
 * 
 * 
 * @author sg
 *
 */
public class ShowViewMenu extends ContributionItem {

	public static final String SHOW_VIEW_ID = "com.mmxlabs.rcp.common.openview";

	public static final String VIEW_ID_PARM = "view.id";
	private IWorkbenchWindow window;

	private static final String NO_TARGETS_MSG = WorkbenchMessages.Workbench_showInNoTargets;

	private Comparator actionComparator = new Comparator() {
		@Override
		public int compare(Object o1, Object o2) {
			if (collator == null) {
				collator = Collator.getInstance();
			}
			CommandContributionItemParameter a1 = (CommandContributionItemParameter) o1;
			CommandContributionItemParameter a2 = (CommandContributionItemParameter) o2;
			return collator.compare(a1.label, a2.label);
		}
	};

	private Action showDlgAction;

	private Map actions = new HashMap(21);

	// Maps pages to a list of opened views
	private Map openedViews = new HashMap();

	private MenuManager menuManager;

	private IMenuListener menuListener = new IMenuListener() {
		@Override
		public void menuAboutToShow(IMenuManager manager) {
			manager.markDirty();
		}
	};
	private boolean makeFast;

	private static Collator collator;

	/**
	 * Creates a Show View menu.
	 * 
	 * @param window
	 *            the window containing the menu
	 * @param id
	 *            the id
	 */
	public ShowViewMenu(IWorkbenchWindow window, String id) {
		this(window, id, false);
	}

	/**
	 * Creates a Show View menu.
	 * 
	 * @param window
	 *            the window containing the menu
	 * @param id
	 *            the id
	 * @param makeFast
	 *            use the fact view variant of the command
	 */

	private EPartService ePartService;
	private EModelService eModelService;
	private MApplication eApplication;

	public ShowViewMenu(IWorkbenchWindow window, String id, final boolean makeFast) {
		super(id);
		this.window = window;
		this.makeFast = makeFast;
		final IHandlerService handlerService = (IHandlerService) window.getService(IHandlerService.class);
		final ICommandService commandService = (ICommandService) window.getService(ICommandService.class);

		eApplication = (MApplication) window.getService(MApplication.class);

		ePartService = (EPartService) window.getService(EPartService.class);

		eModelService = (EModelService) window.getService(EModelService.class);

		final ParameterizedCommand cmd = getCommand(commandService, makeFast);

		showDlgAction = new Action(WorkbenchMessages.ShowView_title) {
			@Override
			public void run() {
				try {
					handlerService.executeCommand(cmd, null);
				} catch (final ExecutionException e) {
					// Do nothing.
				} catch (NotDefinedException e) {
					// Do nothing.
				} catch (NotEnabledException e) {
					// Do nothing.
				} catch (NotHandledException e) {
					// Do nothing.
				}
			}
		};

		window.getWorkbench().getHelpSystem().setHelp(showDlgAction, IWorkbenchHelpContextIds.SHOW_VIEW_OTHER_ACTION);
		// indicate that a show views submenu has been created
		if (window instanceof WorkbenchWindow) {
			((WorkbenchWindow) window).addSubmenu(WorkbenchWindow.SHOW_VIEW_SUBMENU);
		}

		showDlgAction.setActionDefinitionId(IWorkbenchCommandConstants.VIEWS_SHOW_VIEW);

	}

	/**
	 * Overridden to always return true and force dynamic menu building.
	 */
	@Override
	public boolean isDynamic() {
		return true;
	}

	/**
	 * Fills the menu with Show View actions.
	 */
	private void fillMenu(IMenuManager innerMgr) {
		// Remove all.
		innerMgr.removeAll();

		// If no page disable all.
		IWorkbenchPage page = window.getActivePage();
		if (page == null) {
			return;
		}

		// If no active perspective disable all
		if (page.getPerspective() == null) {
			return;
		}

		// Get visible actions.
		List viewIds = Arrays.asList(page.getShowViewShortcuts());

		Collection<MPart> parts = ePartService.getParts();

		// add all open views
		viewIds = addOpenedViews(page, viewIds);

		List actions = new ArrayList(viewIds.size());
		for (Iterator i = viewIds.iterator(); i.hasNext();) {
			String id = (String) i.next();
			if (id.equals(IIntroConstants.INTRO_VIEW_ID)) {
				continue;
			}
			CommandContributionItemParameter item = getItem(id);
			if (item != null) {
				actions.add(item);
			}
		}
		Collections.sort(actions, actionComparator);
		for (Iterator i = actions.iterator(); i.hasNext();) {
			CommandContributionItemParameter ccip = (CommandContributionItemParameter) i.next();
			if (WorkbenchActivityHelper.filterItem(ccip)) {
				continue;
			}
			CommandContributionItem item = new CommandContributionItem(ccip);
			innerMgr.add(item);
		}

		// We only want to add the separator if there are show view shortcuts,
		// otherwise, there will be a separator and then the 'Other...' entry
		// and that looks weird as the separator is separating nothing
		if (!innerMgr.isEmpty()) {
			innerMgr.add(new Separator());
		}

		// Add Other...
		innerMgr.add(showDlgAction);
	}

	static class PluginCCIP extends CommandContributionItemParameter implements IPluginContribution {

		private String localId;
		private String pluginId;

		public PluginCCIP(IViewDescriptor v, IServiceLocator serviceLocator, String id, String commandId, int style) {
			super(serviceLocator, id, commandId, style);
			localId = ((ViewDescriptor) v).getLocalId();
			pluginId = ((ViewDescriptor) v).getPluginId();
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see org.eclipse.ui.IPluginContribution#getLocalId()
		 */
		@Override
		public String getLocalId() {
			return localId;
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see org.eclipse.ui.IPluginContribution#getPluginId()
		 */
		@Override
		public String getPluginId() {
			return pluginId;
		}

	}

	private CommandContributionItemParameter getItem(String viewId) {
		{
			List findElements = eModelService.findElements(eApplication, viewId, MPart.class, (List) null);
			if (findElements.size() == 1) {
				MPart p = (MPart) findElements.get(0);
				String label = p.getLabel();

				CommandContributionItemParameter parms = new CommandContributionItemParameter(window, viewId, SHOW_VIEW_ID, CommandContributionItem.STYLE_PUSH);
				parms.label = label;
				try {
					parms.icon = ImageDescriptor.createFromURL(new URL(p.getIconURI()));
				} catch (MalformedURLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				parms.parameters = new HashMap();

				parms.parameters.put(VIEW_ID_PARM, viewId);
				if (makeFast) {
					parms.parameters.put(IWorkbenchCommandConstants.VIEWS_SHOW_VIEW_PARM_FASTVIEW, "true"); //$NON-NLS-1$
				}
				return parms;
			}
		}
		{
			List findElements = eModelService.findElements(eApplication, viewId, MPartDescriptor.class, (List) null);
			if (findElements.size() == 1) {
				MPartDescriptor p = (MPartDescriptor) findElements.get(0);
				String label = p.getLabel();

				CommandContributionItemParameter parms = new CommandContributionItemParameter(window, viewId, SHOW_VIEW_ID, CommandContributionItem.STYLE_PUSH);
				parms.label = label;
				try {
					parms.icon = ImageDescriptor.createFromURL(new URL(p.getIconURI()));
				} catch (MalformedURLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				parms.parameters = new HashMap();

				parms.parameters.put(VIEW_ID_PARM, viewId);
				if (makeFast) {
					parms.parameters.put(IWorkbenchCommandConstants.VIEWS_SHOW_VIEW_PARM_FASTVIEW, "true"); //$NON-NLS-1$
				}
				return parms;
			}
		}
		IViewRegistry reg = WorkbenchPlugin.getDefault().getViewRegistry();
		IViewDescriptor desc = reg.find(viewId);
		if (desc == null) {
			return null;
		}
		String label = desc.getLabel();

		CommandContributionItemParameter parms = new PluginCCIP(desc, window, viewId, SHOW_VIEW_ID, CommandContributionItem.STYLE_PUSH);
		parms.label = label;
		parms.icon = desc.getImageDescriptor();
		parms.parameters = new HashMap();

		parms.parameters.put(VIEW_ID_PARM, viewId);
		if (makeFast) {
			parms.parameters.put(IWorkbenchCommandConstants.VIEWS_SHOW_VIEW_PARM_FASTVIEW, "true"); //$NON-NLS-1$
		}
		return parms;
	}

	private List addOpenedViews(IWorkbenchPage page, List actions) {
		ArrayList views = getParts(page);
		ArrayList result = new ArrayList(views.size() + actions.size());

		for (int i = 0; i < actions.size(); i++) {
			Object element = actions.get(i);
			if (result.indexOf(element) < 0) {
				result.add(element);
			}
		}
		for (int i = 0; i < views.size(); i++) {
			Object element = views.get(i);
			if (result.indexOf(element) < 0) {
				result.add(element);
			}
		}
		return result;
	}

	private ArrayList getParts(IWorkbenchPage page) {
		ArrayList parts = (ArrayList) openedViews.get(page);
		if (parts == null) {
			parts = new ArrayList();
			openedViews.put(page, parts);
		}
		return parts;
	}

	@Override
	public void fill(Menu menu, int index) {
		if (getParent() instanceof MenuManager) {
			((MenuManager) getParent()).addMenuListener(menuListener);
		}

		if (menuManager != null) {
			menuManager.dispose();
			menuManager = null;
		}

		menuManager = new MenuManager();
		fillMenu(menuManager);
		IContributionItem items[] = menuManager.getItems();
		if (items.length == 0) {
			MenuItem item = new MenuItem(menu, SWT.NONE, index++);
			item.setText(NO_TARGETS_MSG);
			item.setEnabled(false);
		} else {
			for (int i = 0; i < items.length; i++) {
				items[i].fill(menu, index++);
			}
		}
	}

	// for dynamic UI
	protected void removeAction(String viewId) {
		actions.remove(viewId);
	}

	/**
	 * @param commandService
	 * @param makeFast
	 */
	private ParameterizedCommand getCommand(ICommandService commandService, final boolean makeFast) {
		Command c = commandService.getCommand(IWorkbenchCommandConstants.VIEWS_SHOW_VIEW);
		Parameterization[] parms = null;
		if (makeFast) {
			try {
				IParameter parmDef = c.getParameter(IWorkbenchCommandConstants.VIEWS_SHOW_VIEW_PARM_FASTVIEW);
				parms = new Parameterization[] { new Parameterization(parmDef, "true") //$NON-NLS-1$
				};
			} catch (NotDefinedException e) {
				// this should never happen
			}
		}
		return new ParameterizedCommand(c, parms);
	}
}