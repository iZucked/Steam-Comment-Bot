/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2015
 * All rights reserved.
 */
package com.mmxlabs.lingo.reports.views;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.viewers.ColumnLabelProvider;
import org.eclipse.nebula.widgets.grid.GridColumn;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.IMemento;
import org.eclipse.ui.IViewSite;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.XMLMemento;
import org.eclipse.ui.plugin.AbstractUIPlugin;

import com.mmxlabs.lingo.reports.components.ColumnBlock;
import com.mmxlabs.lingo.reports.components.EMFReportView;
import com.mmxlabs.lingo.reports.internal.Activator;
import com.mmxlabs.lingo.reports.utils.ColumnConfigurationDialog;
import com.mmxlabs.lingo.reports.utils.ColumnConfigurationDialog.IColumnInfoProvider;
import com.mmxlabs.lingo.reports.utils.ColumnConfigurationDialog.IColumnUpdater;

/**
 * A customisable report for schedule based data. Extension points define the available columns for all instances and initial state for each instance of this report. Optionally a dialog is available
 * for the user to change the default settings.
 */
public abstract class AbstractConfigurableReportView extends EMFReportView {

	// Memto keys
	protected static final String CONFIGURABLE_COLUMNS_ORDER = "CONFIGURABLE_COLUMNS_ORDER";
	protected static final String CONFIGURABLE_ROWS_ORDER = "CONFIGURABLE_ROWS_ORDER";
	protected static final String CONFIGURABLE_COLUMNS_REPORT_KEY = "CONFIGURABLE_COLUMNS_REPORT_KEY";

	private IMemento memento;

	private boolean customisableReport = true;

	public AbstractConfigurableReportView(String helpContextID) {
		super(helpContextID);
	}

	protected String getColumnSettingsMementoKey() {
		return CONFIGURABLE_COLUMNS_REPORT_KEY;
	}

	private void setColumnsImmovable() {
		if (viewer != null) {
			for (final GridColumn column : viewer.getGrid().getColumns()) {
				column.setMoveable(false);
			}
		}
	}

	@Override
	public void init(final IViewSite site, IMemento memento) throws PartInitException {
		if (memento == null) {
			memento = XMLMemento.createWriteRoot("workbench");
		}
		this.memento = memento;

		super.init(site, memento);
	}

	@Override
	public void saveState(final IMemento memento) {
		super.saveState(memento);
		final IMemento configMemento = memento.createChild(getColumnSettingsMementoKey());
		getBlockManager().saveToMemento(CONFIGURABLE_COLUMNS_ORDER, configMemento);
		saveConfigState(configMemento);
	}

	protected void saveConfigState(final IMemento configMemento) {

	}

	@Override
	public void createPartControl(final Composite parent) {

		// Find the column definitions
		registerReportColumns();

		// Check ext point to see if we can enable the customise action (created within createPartControl)
		customisableReport = checkCustomisable();
		super.createPartControl(parent);

		// Look at the extension points for the initial visibilities, rows and diff options
		setInitialState();

		// force the columns to be immovable except by using the config dialog
		setColumnsImmovable();

		// Restore state from memento if possible.
		if (memento != null) {
			final IMemento configMemento = memento.getChild(getColumnSettingsMementoKey());

			if (configMemento != null) {
				getBlockManager().initFromMemento(CONFIGURABLE_COLUMNS_ORDER, configMemento);
				initConfigMemento(configMemento);
			}
		}

	}

	protected abstract void registerReportColumns();

	protected void initConfigMemento(IMemento configMemento) {

	}

	/**
	 * Check the view extension point to see if we can enable the customise dialog
	 * 
	 * @return
	 */
	protected boolean checkCustomisable() {

		return true;
	}

	/**
	 * Examine the view extension point to determine the default set of columns, order,row types and diff options.
	 */
	protected abstract void setInitialState();

	/**
	 * Fills the top-right pulldown menu, adding an option to configure the columns visible in this view.
	 */
	@Override
	protected void fillLocalPullDown(final IMenuManager manager) {
		super.fillLocalPullDown(manager);
		final IWorkbench wb = PlatformUI.getWorkbench();
		final IWorkbenchWindow win = wb.getActiveWorkbenchWindow();

		// Only create action if permitted.
		if (customisableReport) {
			Action configureColumnsAction = new Action("Configure Contents") {
				@Override
				public void run() {
					final IColumnInfoProvider infoProvider = new ColumnConfigurationDialog.ColumnInfoAdapter() {

						@Override
						public int getColumnIndex(final Object columnObj) {
							return getBlockManager().getBlockIndex((ColumnBlock) columnObj);
						}

						@Override
						public boolean isColumnVisible(final Object columnObj) {
							return getBlockManager().getBlockVisible((ColumnBlock) columnObj);
						}

					};

					final IColumnUpdater updater = new ColumnConfigurationDialog.ColumnUpdaterAdapter() {

						@Override
						public void setColumnVisible(final Object columnObj, final boolean visible) {

							((ColumnBlock) columnObj).setUserVisible(visible);
							viewer.refresh();

						}

						@Override
						public void swapColumnPositions(final Object columnObj1, final Object columnObj2) {
							getBlockManager().swapBlockOrder((ColumnBlock) columnObj1, (ColumnBlock) columnObj2);
							viewer.refresh();
						}

						@Override
						public Object[] resetColumnStates() {
							setInitialState();
							return getBlockManager().getBlocksInVisibleOrder().toArray();
						}

					};

					final Image nonVisibleIcon = AbstractUIPlugin.imageDescriptorFromPlugin(Activator.PLUGIN_ID, "icons/read_obj_disabled.gif").createImage();
					final Image visibleIcon = AbstractUIPlugin.imageDescriptorFromPlugin(Activator.PLUGIN_ID, "icons/read_obj.gif").createImage();

					final ColumnConfigurationDialog dialog = new ColumnConfigurationDialog(win.getShell()) {

						@Override
						protected IColumnInfoProvider getColumnInfoProvider() {
							return infoProvider;
						}

						@Override
						protected ColumnLabelProvider getLabelProvider() {
							return new ColumnLabelProvider() {
								@Override
								public String getText(final Object element) {
									final ColumnBlock block = (ColumnBlock) element;
									return block.blockName;
								}

								@Override
								public Image getImage(final Object element) {
									final ColumnBlock block = (ColumnBlock) element;
									if (block.isModeVisible()) {
										return visibleIcon;
									} else {
										return nonVisibleIcon;
									}
								}

								@Override
								public String getToolTipText(final Object element) {
									final ColumnBlock block = (ColumnBlock) element;
									return block.tooltip;
								}
							};
						}

						@Override
						protected IColumnUpdater getColumnUpdater() {
							return updater;
						}
					};
					dialog.setColumnsObjs(getBlockManager().getBlocksInVisibleOrder().toArray());

					// See if this report has any additional check items to add
					addDialogCheckBoxes(dialog);
					dialog.open();

					// Dialog has been closed, check the output
					postDialogOpen(dialog);
					nonVisibleIcon.dispose();
					visibleIcon.dispose();

					synchronizer.refreshViewer();

				}

			};
			manager.appendToGroup("additions", configureColumnsAction);
		}

	}

	protected void addDialogCheckBoxes(final ColumnConfigurationDialog dialog) {

	}

	protected void postDialogOpen(final ColumnConfigurationDialog dialog) {

	}
}
