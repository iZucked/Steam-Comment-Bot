package com.mmxlabs.models.lng.analytics.ui.views.valuematrix;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.ActionContributionItem;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.action.IMenuCreator;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Menu;

import com.mmxlabs.rcp.icons.lingo.CommonImages;
import com.mmxlabs.rcp.icons.lingo.CommonImages.IconMode;
import com.mmxlabs.rcp.icons.lingo.CommonImages.IconPaths;

public class HighlightAction extends Action implements IMenuCreator {

	private final ValueMatrixResultsComponent resultsComponent;

	public HighlightAction(final ValueMatrixResultsComponent resultsComponent) {
		super("Highlight", IAction.AS_DROP_DOWN_MENU);
		setImageDescriptor(CommonImages.getImageDescriptor(IconPaths.Highlighter, IconMode.Enabled));
		this.resultsComponent = resultsComponent;
	}

	private Menu lastMenu = null;
	@Override
	public IMenuCreator getMenuCreator() {
		return this;
	}

	@Override
	public Menu getMenu(Control parent) {
		if (lastMenu != null && !lastMenu.isDisposed()) {
			lastMenu.dispose();
		}
		lastMenu = new Menu(parent);
		createMenuItems(lastMenu);
		return lastMenu;
	}
	
	@Override
	public Menu getMenu(Menu parent) {
		if (lastMenu != null && !lastMenu.isDisposed()) {
			lastMenu.dispose();
		}
		lastMenu = new Menu(parent);
		createMenuItems(lastMenu);
		return lastMenu;
	}

	@Override
	public void dispose() {
		if (lastMenu != null) {
			if (!lastMenu.isDisposed()) {
				lastMenu.dispose();
			}
			lastMenu = null;
		}
	}

	private void createMenuItems(final Menu menu) {
		final Action showGroups = new Action("Show groups", IAction.AS_CHECK_BOX) {
			@Override
			public void run() {
				resultsComponent.toggleShowGroups();
				setChecked(resultsComponent.isShowingGroups());
				resultsComponent.softRefresh();
			}
		};
		final Action lossesInRed = new Action("Losses in red", IAction.AS_CHECK_BOX) {
			@Override
			public void run() {
				resultsComponent.toggleHighlightLosses();
				setChecked(resultsComponent.isHighlightingLosses());
				resultsComponent.softRefresh();
			}
		};
		final ActionContributionItem showGroupsAci = new ActionContributionItem(showGroups);
		final ActionContributionItem lossesInRedAci = new ActionContributionItem(lossesInRed);

		showGroupsAci.fill(menu, -1);
		lossesInRedAci.fill(menu, -1);

		showGroups.setChecked(resultsComponent.isShowingGroups());
		lossesInRed.setChecked(resultsComponent.isHighlightingLosses());
	}
}
