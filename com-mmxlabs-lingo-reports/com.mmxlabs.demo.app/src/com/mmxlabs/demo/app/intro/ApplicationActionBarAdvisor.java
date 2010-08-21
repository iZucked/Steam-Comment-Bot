package com.mmxlabs.demo.app.intro;

import org.eclipse.jface.action.GroupMarker;
import org.eclipse.jface.action.IContributionItem;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.action.Separator;
import org.eclipse.ui.IWorkbenchActionConstants;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.actions.ActionFactory;
import org.eclipse.ui.actions.ActionFactory.IWorkbenchAction;
import org.eclipse.ui.actions.ContributionItemFactory;
import org.eclipse.ui.application.ActionBarAdvisor;
import org.eclipse.ui.application.IActionBarConfigurer;

public class ApplicationActionBarAdvisor extends ActionBarAdvisor {

	private IWorkbenchAction introAction;

	private IWorkbenchAction quitAction;

	private IWorkbenchAction saveAction;
	
	private IWorkbenchAction saveAllAction;
	
	private IWorkbenchAction saveAsAction;

	private IContributionItem showViewMenu;

	public ApplicationActionBarAdvisor(IActionBarConfigurer configurer) {
		super(configurer);
	}

	@Override
	protected void makeActions(IWorkbenchWindow window) {
		if (window.getWorkbench().getIntroManager().hasIntro()){
			introAction = ActionFactory.INTRO.create(window);
			register(introAction);
		}
		quitAction = ActionFactory.QUIT.create(window);
		register(quitAction);

		saveAction = ActionFactory.SAVE.create(window);
		register(saveAction);

		saveAsAction = ActionFactory.SAVE_AS.create(window);
		register(saveAsAction);

		saveAllAction = ActionFactory.SAVE_ALL.create(window);
		register(saveAllAction);

		showViewMenu = ContributionItemFactory.VIEWS_SHORTLIST.create(window);

	}

	@Override
	protected void fillMenuBar(IMenuManager menuBar) {
		{
			MenuManager fileMenu = new MenuManager("&File",
					IWorkbenchActionConstants.M_FILE);
			menuBar.add(fileMenu);

			// fileMenu.add(newAction);
			// fileMenu.add(new Separator());

			MenuManager menu = fileMenu;

			menu.add(new GroupMarker(IWorkbenchActionConstants.FILE_START));

			IMenuManager newMenu = new MenuManager("&New", "new");
			newMenu.add(new GroupMarker(IWorkbenchActionConstants.MB_ADDITIONS));

			menu.add(newMenu);
			menu.add(new Separator());
			menu.add(new GroupMarker(IWorkbenchActionConstants.MB_ADDITIONS));
			menu.add(new Separator());
			// addToMenuAndRegister(menu, ActionFactory.CLOSE.create(window));
			// addToMenuAndRegister(menu,
			// ActionFactory.CLOSE_ALL.create(window));
			// menu.add(new Separator());
			menu.add(saveAction);
			menu.add(saveAsAction);
			menu.add(saveAllAction);
			menu.add(new Separator());
			// addToMenuAndRegister(menu, ActionFactory.QUIT.create(window));

			fileMenu.add(quitAction);

			menu.add(new GroupMarker(IWorkbenchActionConstants.FILE_END));
		}

		MenuManager windowMenu = new MenuManager("&Window",
				IWorkbenchActionConstants.M_WINDOW);
		menuBar.add(windowMenu);

		{
			MenuManager showViewMenuMgr = new MenuManager("&Show View",
					"showView"); //

			showViewMenuMgr.add(showViewMenu);
			windowMenu.add(showViewMenuMgr);
		}

		MenuManager helpMenu = new MenuManager("&Help",
				IWorkbenchActionConstants.M_HELP);
		menuBar.add(helpMenu);

		// Help
		if (introAction != null) 
			helpMenu.add(introAction);
	}
}
