/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
package com.mmxlabs.lingo.app;

import java.net.MalformedURLException;
import java.net.URL;
import java.text.Collator;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

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
import org.eclipse.e4.ui.workbench.modeling.EModelService;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.ActionContributionItem;
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

import com.google.common.collect.Sets;

/**
 * A <code>ShowViewMenu</code> is used to populate a menu manager with Show View actions. The visible views are determined by user preference from the Perspective Customize dialog.
 * 
 * 
 * Custom version to link up e4 views from perspective short cuts. TODO: Fix up dialog box
 * 
 * 
 * @author sg
 *
 */
@SuppressWarnings("restriction")
public class ShowViewMenu extends ContributionItem {

	// These are the display names for the category. The API's used here do no get the underlying id.
	private static final String USER_REPORTS = "User Reports";
	private static final String TEAM_REPORTS = "Team Reports";

	// Part ids to always ignore
	private static final Set<String> IGNORED = Sets.newHashSet(IIntroConstants.INTRO_VIEW_ID);

	public static final String SHOW_VIEW_ID = "com.mmxlabs.rcp.common.openview";

	public static final String VIEW_ID_PARM = "view.id";
	private final IWorkbenchWindow window;

	private static final String NO_TARGETS_MSG = WorkbenchMessages.Workbench_showInNoTargets;

	private final Comparator<CommandContributionItemParameter> actionComparator = (o1, o2) -> {
		if (collator == null) {
			collator = Collator.getInstance();
		}
		final CommandContributionItemParameter a1 = o1;
		final CommandContributionItemParameter a2 = o2;
		return collator.compare(a1.label, a2.label);
	};

	private final Action showDlgAction;

	// Maps pages to a list of opened views
	private final Map<IWorkbenchPage, List<String>> openedViews = new HashMap<>();

	private MenuManager menuManager;

	private final IMenuListener menuListener = IMenuManager::markDirty;

	private final boolean makeFast;

	private static Collator collator;

	private final EModelService eModelService;
	private final MApplication eApplication;

	/**
	 * Creates a Show View menu.
	 * 
	 * @param window
	 *            the window containing the menu
	 * @param id
	 *            the id
	 */
	public ShowViewMenu(final IWorkbenchWindow window, final String id) {
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
	public ShowViewMenu(final IWorkbenchWindow window, final String id, final boolean makeFast) {
		super(id);
		this.window = window;
		this.makeFast = makeFast;
		final IHandlerService handlerService = window.getService(IHandlerService.class);
		final ICommandService commandService = window.getService(ICommandService.class);

		eApplication = window.getService(MApplication.class);
		eModelService = window.getService(EModelService.class);

		final ParameterizedCommand cmd = getCommand(commandService, makeFast);

		showDlgAction = new Action(WorkbenchMessages.ShowView_title) {
			@Override
			public void run() {
				try {
					handlerService.executeCommand(cmd, null);
				} catch (final ExecutionException e) {
					// Do nothing.
				} catch (final NotDefinedException e) {
					// Do nothing.
				} catch (final NotEnabledException e) {
					// Do nothing.
				} catch (final NotHandledException e) {
					// Do nothing.
				}
			}
		};

		window.getWorkbench().getHelpSystem().setHelp(showDlgAction, IWorkbenchHelpContextIds.SHOW_VIEW_OTHER_ACTION);
		// indicate that a show views submenu has been created
		if (window instanceof final WorkbenchWindow ww) {
			ww.addSubmenu(WorkbenchWindow.SHOW_VIEW_SUBMENU);
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
	private void fillMenu(final IMenuManager innerMgr) {
		// Remove all.
		innerMgr.removeAll();

		// If no page disable all.
		final IWorkbenchPage page = window.getActivePage();
		if (page == null) {
			return;
		}

		// If no active perspective disable all
		if (page.getPerspective() == null) {
			return;
		}

		// Get visible actions from perspective
		List<String> viewIds = Arrays.asList(page.getShowViewShortcuts());

		// add all open views
		viewIds = addOpenedViews(page, viewIds);

		// Add all user and team reports.
		{
			final IViewRegistry reg = WorkbenchPlugin.getDefault().getViewRegistry();
			for (final var cat : reg.getViews()) {
				if (cat.getCategoryPath().length > 0) {
					if (cat.getCategoryPath()[0].equals(TEAM_REPORTS) || cat.getCategoryPath()[0].equals(USER_REPORTS)) {
						viewIds.add(cat.getId());
					}
				}
			}
		}

		// Next from the collection of id's, find the category and put the info in a grouped map
		// Filter out views we are not interested in.
		final Map<String, List<CommandContributionItemParameter>> m = new HashMap<>();
		for (final Iterator<String> i = viewIds.iterator(); i.hasNext();) {
			final String id = i.next();
			if (IGNORED.contains(id)) {
				continue;
			}
			getItem(id, m);
		}

		// Sort alphabetically, but move User and Team reports to the top
		List<String> keys = new LinkedList<>(m.keySet());
		Collections.sort(keys);
		if (keys.contains(TEAM_REPORTS)) {
			keys.remove(TEAM_REPORTS);
			keys.add(0, TEAM_REPORTS);
		}
		if (keys.contains(USER_REPORTS)) {
			keys.remove(USER_REPORTS);
			keys.add(0, USER_REPORTS);
		}

		for (final String key : keys) {
			final var views = m.get(key);
			// Activity bindings should filter out the views
			views.removeIf(WorkbenchActivityHelper::filterItem);
			if (views.isEmpty()) {
				continue;
			}

			Collections.sort(views, actionComparator);
			if (key.equals(USER_REPORTS) || key.equals(TEAM_REPORTS)) {
				final MenuManager minimgr = new MenuManager(key);
				innerMgr.add(minimgr);
				for (final var ccip : views) {
					minimgr.add(new CommandContributionItem(ccip));
				}
			} else {
				final Action a = new Action("- " + key + " -") {

				};
				a.setEnabled(false);

				final ActionContributionItem aci = new ActionContributionItem(a) {

					@Override
					public boolean isSeparator() {
						return true;
					}
				};
				innerMgr.add(aci);
				for (final var ccip : views) {
					innerMgr.add(new CommandContributionItem(ccip));
				}
			}
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

		private final String localId;
		private final String pluginId;

		public PluginCCIP(final IViewDescriptor v, final IServiceLocator serviceLocator, final String id, final String commandId, final int style) {
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

	private boolean getItem(final String viewId, final Map<String, List<CommandContributionItemParameter>> groupedViews) {
		// This disabled code queried the view part before the view description.
		// SG: I think we want to ignore this as we often get the wrong name/icon from the persisted state rather than original definition
		// {
		// List<MPart> findElements = eModelService.findElements(eApplication, viewId, MPart.class, (List) null);
		// if (findElements.size() == 1) {
		// MPart p = findElements.get(0);
		// String label = p.getLabel();
		//
		// CommandContributionItemParameter parms = new CommandContributionItemParameter(window, viewId, SHOW_VIEW_ID, CommandContributionItem.STYLE_PUSH);
		// parms.label = label;
		// try {
		// parms.icon = ImageDescriptor.createFromURL(new URL(p.getIconURI()));
		// } catch (MalformedURLException e) {
		// // TODO Auto-generated catch block
		// e.printStackTrace();
		// }
		// parms.parameters = new HashMap();
		//
		// parms.parameters.put(VIEW_ID_PARM, viewId);
		// if (makeFast) {
		// parms.parameters.put(IWorkbenchCommandConstants.VIEWS_SHOW_VIEW_PARM_FASTVIEW, "true"); //$NON-NLS-1$
		// }
		//
		// groupedViews.computeIfAbsent(p.get, null)
		// return parms;
		// }
		// }
		{
			final List<MPartDescriptor> findElements = eModelService.findElements(eApplication, viewId, MPartDescriptor.class, (List) null);
			if (findElements.size() == 1) {
				final MPartDescriptor p = findElements.get(0);
				final String label = p.getLabel();

				final CommandContributionItemParameter parms = new CommandContributionItemParameter(window, viewId, SHOW_VIEW_ID, CommandContributionItem.STYLE_PUSH);
				parms.label = label;
				try {
					parms.icon = ImageDescriptor.createFromURL(new URL(p.getIconURI()));
				} catch (final MalformedURLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				parms.parameters = new HashMap<Object, Object>();

				parms.parameters.put(VIEW_ID_PARM, viewId);
				if (makeFast) {
					parms.parameters.put(IWorkbenchCommandConstants.VIEWS_SHOW_VIEW_PARM_FASTVIEW, "true"); //$NON-NLS-1$
				}

				groupedViews.computeIfAbsent(p.getCategory(), k -> new LinkedList<>()).add(parms);
				return true;
			}
		}
		final IViewRegistry reg = WorkbenchPlugin.getDefault().getViewRegistry();
		final IViewDescriptor desc = reg.find(viewId);
		if (desc == null) {
			return false;
		}
		final String label = desc.getLabel();

		final CommandContributionItemParameter parms = new PluginCCIP(desc, window, viewId, SHOW_VIEW_ID, CommandContributionItem.STYLE_PUSH);
		parms.label = label;
		parms.icon = desc.getImageDescriptor();
		parms.parameters = new HashMap<Object, Object>();

		parms.parameters.put(VIEW_ID_PARM, viewId);
		if (makeFast) {
			parms.parameters.put(IWorkbenchCommandConstants.VIEWS_SHOW_VIEW_PARM_FASTVIEW, "true"); //$NON-NLS-1$
		}
		groupedViews.computeIfAbsent(desc.getCategoryPath()[0], k -> new LinkedList<>()).add(parms);
		return true;
	}

	private List<String> addOpenedViews(final IWorkbenchPage page, final List<String> actions) {
		final List<String> views = getParts(page);
		final List<String> result = new ArrayList<>(views.size() + actions.size());

		for (final String element : actions) {
			if (!result.contains(element)) {
				result.add(element);
			}
		}
		for (final String element : views) {
			if (!result.contains(element)) {
				result.add(element);
			}
		}
		return result;
	}

	private List<String> getParts(final IWorkbenchPage page) {
		return openedViews.computeIfAbsent(page, k -> new LinkedList<>());
	}

	@Override
	public void fill(final Menu menu, int index) {
		if (getParent() instanceof MenuManager mgr) {
			mgr.addMenuListener(menuListener);
		}

		if (menuManager != null) {
			menuManager.dispose();
			menuManager = null;
		}

		menuManager = new MenuManager();
		fillMenu(menuManager);
		final IContributionItem[] items = menuManager.getItems();
		if (items.length == 0) {
			final MenuItem item = new MenuItem(menu, SWT.NONE, index);
			item.setText(NO_TARGETS_MSG);
			item.setEnabled(false);
		} else {
			for (int i = 0; i < items.length; i++) {
				items[i].fill(menu, index++);
			}
		}
	}

	/**
	 * @param commandService
	 * @param makeFast
	 */
	private ParameterizedCommand getCommand(final ICommandService commandService, final boolean makeFast) {
		final Command c = commandService.getCommand(IWorkbenchCommandConstants.VIEWS_SHOW_VIEW);
		Parameterization[] parms = null;
		if (makeFast) {
			try {
				final IParameter parmDef = c.getParameter(IWorkbenchCommandConstants.VIEWS_SHOW_VIEW_PARM_FASTVIEW);
				parms = new Parameterization[] { new Parameterization(parmDef, "true") //$NON-NLS-1$
				};
			} catch (final NotDefinedException e) {
				// this should never happen
			}
		}
		return new ParameterizedCommand(c, parms);
	}
}