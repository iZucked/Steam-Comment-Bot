/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2012
 * All rights reserved.
 */
package scenario.presentation.cargoeditor.handlers;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.edit.command.AddCommand;
import org.eclipse.emf.edit.domain.EditingDomain;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.ActionContributionItem;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.action.IMenuCreator;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.ui.ISharedImages;
import org.eclipse.ui.PlatformUI;

import scenario.presentation.cargoeditor.LockableAction;

/**
 * An action for adding model elements
 * 
 * @author Tom Hinton
 * 
 */
public abstract class AddAction extends LockableAction implements IMenuCreator {
	private Menu lastMenu = null;

	public AddAction(final EditingDomain editingDomain, final String name) {
		super("Add " + name, IAction.AS_DROP_DOWN_MENU);
		this.editingDomain = editingDomain;

		setImageDescriptor(PlatformUI.getWorkbench().getSharedImages().getImageDescriptor(ISharedImages.IMG_OBJ_ADD));
		setToolTipText("Add " + name);
		setText("Add " + name);
	}

	/**
	 * Create an instance of the object to add, or return null to cancel the action
	 * 
	 * @return
	 */
	public abstract EObject createObject(boolean useSelection);

	public abstract Object getOwner();

	public abstract Object getFeature();

	private final EditingDomain editingDomain;

	private Action addSubAction, copySubAction;

	private boolean defaultToCopy = true;

	@Override
	public final void run() {
		run(defaultToCopy);
	}

	protected void run(final boolean usingSelection) {
		final EObject object = createObject(usingSelection);
		if (object == null) {
			return; // if cancelled, subclasses return null
		}
		editingDomain.getCommandStack().execute(AddCommand.create(editingDomain, getOwner(), getFeature(), object));
	}

	@Override
	public void dispose() {
		lastMenu.dispose();
	}

	@Override
	public Menu getMenu(final Control parent) {
		if (lastMenu != null) {
			lastMenu.dispose();
		}
		lastMenu = new Menu(parent);

		createMenuItems(lastMenu);

		return lastMenu;
	}

	@Override
	public Menu getMenu(final Menu parent) {
		if (lastMenu != null) {
			lastMenu.dispose();
		}
		lastMenu = new Menu(parent);
		createMenuItems(lastMenu);
		return lastMenu;
	}

	/**
	 * @param lastMenu2
	 */
	private void createMenuItems(final Menu menu) {
		{
			final Action a = new Action("Duplicate", IAction.AS_RADIO_BUTTON) {
				@Override
				public void run() {
					// AddAction.this.run(true);

					defaultToCopy = true;
				}

				@Override
				public boolean isChecked() {
					return defaultToCopy;
				}

			};

			final ActionContributionItem actionContributionItem = new ActionContributionItem(a);
			actionContributionItem.fill(menu, -1);
			a.setChecked(defaultToCopy);

			copySubAction = a;
		}
		{
			final Action a = new Action("Add New", IAction.AS_RADIO_BUTTON) {

				@Override
				public void run() {
					// AddAction.this.run(false);
					defaultToCopy = false;
				}

				@Override
				public boolean isChecked() {
					return !defaultToCopy;
				}
			};
			final ActionContributionItem actionContributionItem = new ActionContributionItem(a);
			actionContributionItem.fill(menu, -1);
			a.setChecked(!defaultToCopy);
			addSubAction = a;
		}
	}

	@Override
	public IMenuCreator getMenuCreator() {
		return this;
	}
}
