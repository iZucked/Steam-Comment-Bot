package com.mmxlabs.models.lng.transformer.ui.parametermodes.actions;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jface.action.IAction;
import org.eclipse.jface.action.IMenuCreator;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.MenuAdapter;
import org.eclipse.swt.events.MenuEvent;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.ui.IEditorActionDelegate;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.actions.ActionDelegate;
import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;
import org.osgi.framework.FrameworkUtil;
import org.osgi.framework.ServiceReference;

import com.mmxlabs.models.lng.transformer.ui.OptimisationHelper;
import com.mmxlabs.models.lng.transformer.ui.parametermodes.IParameterModesRegistry;

public abstract class ParameterModesActionDelegate extends ActionDelegate implements IEditorActionDelegate, IMenuCreator {
	protected IEditorPart editor;
	protected IAction action;

	protected IParameterModesRegistry registry;
	/**
	 * Cascading menu
	 */
	private Menu fCreatedMenu;

	@Override
	public void setActiveEditor(final IAction action, final IEditorPart targetEditor) {
		this.editor = targetEditor;
		this.action = action;
		action.setMenuCreator(this);
	}

	@Override
	public void selectionChanged(final IAction action, final ISelection selection) {
		super.selectionChanged(action, selection);
		action.setMenuCreator(this);
	}

	@Override
	public void dispose() {
		action = null;
		editor = null;

		if (fCreatedMenu != null) {
			fCreatedMenu.dispose();
		}
	}

	@Override
	public Menu getMenu(final Control parent) {
		if (fCreatedMenu != null) {
			fCreatedMenu.dispose();
		}
		fCreatedMenu = new Menu(parent);

		initMenus();

		return fCreatedMenu;
	}

	@Override
	public Menu getMenu(final Menu parent) {
		if (fCreatedMenu != null) {
			fCreatedMenu.dispose();
		}
		fCreatedMenu = new Menu(parent);

		initMenus();

		return fCreatedMenu;
	}

	protected void initMenus() {
		// Add listener to re-populate the menu each time
		// it is shown to reflect changes in selection or active perspective
		fCreatedMenu.addMenuListener(new MenuAdapter() {
			@Override
			public void menuShown(final MenuEvent e) {
				final Menu m = (Menu) e.widget;
				final MenuItem[] items = m.getItems();
				for (int i = 0; i < items.length; i++) {
					items[i].dispose();
				}

				IParameterModesRegistry registry = getParameterModesRegistry();

				final List<String> modes = new ArrayList<String>(registry.getParameterModes());
				modes.add(OptimisationHelper.PARAMETER_MODE_CUSTOM);

				for (final String mode : registry.getParameterModes()) {
					final MenuItem item = new MenuItem(m, SWT.DEFAULT);
					item.setText(mode);

					item.addSelectionListener(new SelectionAdapter() {
						@Override
						public void widgetSelected(final SelectionEvent e) {
							runWithMode(mode);
						}
					});
				}
				// Add custom mode
				{
					final MenuItem item = new MenuItem(m, SWT.DEFAULT);
					item.setText("Custom");

					item.addSelectionListener(new SelectionAdapter() {
						@Override
						public void widgetSelected(final SelectionEvent e) {
							runCustomMode();
						}
					});
				}
				// Add edit menu
				{

					MenuItem sep = new MenuItem(m, SWT.SEPARATOR);

					final MenuItem item = new MenuItem(m, SWT.DEFAULT);
					item.setText("Customise...");

					item.addSelectionListener(new SelectionAdapter() {
						@Override
						public void widgetSelected(final SelectionEvent e) {
							editAndRunCustomMode();
						}
					});
				}
			}

		});
	}

	protected abstract void runLastMode();

	protected abstract void runWithMode(String mode);

	protected abstract void runCustomMode();

	protected abstract void editAndRunCustomMode();

	protected IParameterModesRegistry getParameterModesRegistry() {
		Bundle bundle = FrameworkUtil.getBundle(ParameterModesActionDelegate.class);
		BundleContext bundleContext = bundle.getBundleContext();
		ServiceReference<IParameterModesRegistry> serviceReference = bundleContext.getServiceReference(IParameterModesRegistry.class);
		if (serviceReference != null) {
			IParameterModesRegistry registry = bundleContext.getService(serviceReference);
			return registry;
		}
		return null;
	}
}
