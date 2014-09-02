/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2014
 * All rights reserved.
 */
package com.mmxlabs.lingo.reports.views.schedule;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.viewers.ColumnLabelProvider;
import org.eclipse.jface.viewers.ITreeContentProvider;
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

import com.google.inject.Inject;
import com.mmxlabs.lingo.reports.IScenarioInstanceElementCollector;
import com.mmxlabs.lingo.reports.components.ColumnBlock;
import com.mmxlabs.lingo.reports.components.ColumnType;
import com.mmxlabs.lingo.reports.components.EMFReportView;
import com.mmxlabs.lingo.reports.extensions.EMFReportColumnManager;
import com.mmxlabs.lingo.reports.internal.Activator;
import com.mmxlabs.lingo.reports.utils.ColumnConfigurationDialog;
import com.mmxlabs.lingo.reports.utils.ColumnConfigurationDialog.IColumnInfoProvider;
import com.mmxlabs.lingo.reports.utils.ColumnConfigurationDialog.IColumnUpdater;
import com.mmxlabs.lingo.reports.views.AbstractConfigurableReportView;
import com.mmxlabs.lingo.reports.views.schedule.extpoint.IScheduleBasedColumnExtension;
import com.mmxlabs.lingo.reports.views.schedule.extpoint.IScheduleBasedColumnFactoryExtension;
import com.mmxlabs.lingo.reports.views.schedule.extpoint.IScheduleBasedReportInitialStateExtension;
import com.mmxlabs.lingo.reports.views.schedule.extpoint.IScheduleBasedReportInitialStateExtension.InitialColumn;
import com.mmxlabs.lingo.reports.views.schedule.extpoint.IScheduleBasedReportInitialStateExtension.InitialDiffOption;
import com.mmxlabs.lingo.reports.views.schedule.extpoint.IScheduleBasedReportInitialStateExtension.InitialRowType;
import com.mmxlabs.models.ui.tabular.generic.GenericEMFTableDataModel;

/**
 * A customisable report for schedule based data. Extension points define the available columns for all instances and initial state for each instance of this report. Optionally a dialog is available
 * for the user to change the default settings.
 */
public class ConfigurableScheduleReportView extends AbstractConfigurableReportView {
	/**
	 * The ID of the view as specified by the extension.
	 */
	public static final String ID = "com.mmxlabs.shiplingo.platform.reports.views.schedule.ConfigurableScheduleReportView";

	// // Memto keys
	// private static final String CONFIGURABLE_COLUMNS_ORDER = "CONFIGURABLE_COLUMNS_ORDER";
	// private static final String CONFIGURABLE_ROWS_ORDER = "CONFIGURABLE_ROWS_ORDER";
	// private static final String CARGO_REPORT_TYPE_ID = "CARGO_REPORT_TYPE_ID";
	// private static final String CONFIGURABLE_COLUMNS_REPORT_KEY = "CONFIGURABLE_COLUMNS_REPORT_KEY";

	private final ScheduleBasedReportBuilder builder;

	private final EPackage tableDataModel;

	//private IMemento memento;

	@Inject(optional = true)
	private Iterable<IScheduleBasedColumnFactoryExtension> columnFactoryExtensions;

	@Inject(optional = true)
	private Iterable<IScheduleBasedColumnExtension> columnExtensions;

	@Inject(optional = true)
	private Iterable<IScheduleBasedReportInitialStateExtension> initialStates;

	private boolean customisableReport = true;

	@Inject
	public ConfigurableScheduleReportView(final ScheduleBasedReportBuilder builder) {
		super(ID);

		// Setup the builder hooks.
		this.builder = builder;
		builder.setReport(this);
		builder.setPinDiffModeHelper(pinDiffModeHelper);

		tableDataModel = builder.getTableDataModel();
	}

	// // protected String getColumnSettingsMementoKey() {
	// // return CONFIGURABLE_COLUMNS_REPORT_KEY;
	// // }
	//
	// private void setColumnsImmovable() {
	// if (viewer != null) {
	// for (final GridColumn column : viewer.getGrid().getColumns()) {
	// column.setMoveable(false);
	// }
	// }
	// }

	// @Override
	// public void init(final IViewSite site, IMemento memento) throws PartInitException {
	// if (memento == null) {
	// memento = XMLMemento.createWriteRoot("workbench");
	// }
	// this.memento = memento;
	//
	// super.init(site, memento);
	// }

	@Override
	public void saveConfigState(final IMemento configMemento) {
		// super.saveState(memento);
		// final IMemento configMemento = memento.createChild(getColumnSettingsMementoKey());
		if (configMemento != null) {
			builder.saveToMemento(CONFIGURABLE_ROWS_ORDER, configMemento);
		}
		// getBlockManager().saveToMemento(CONFIGURABLE_COLUMNS_ORDER, configMemento);
	}

	// @Override
	// public void createPartControl(final Composite parent) {
	//
	// // Find the column definitions
	// registerReportColumns();
	//
	// // Check ext point to see if we can enable the customise action (created within createPartControl)
	// customisableReport = checkCustomisable();
	// super.createPartControl(parent);
	//
	// // Look at the extension points for the initial visibilities, rows and diff options
	// setInitialState();
	//
	// // force the columns to be immovable except by using the config dialog
	// setColumnsImmovable();
	//
	// // Set the sorter
	// viewer.setComparator(GenericEMFTableDataModel.createGroupComparator(viewer.getComparator(), tableDataModel));
	//
	// // Restore state from memto if possible.
	// if (memento != null) {
	// final IMemento configMemento = memento.getChild(getColumnSettingsMementoKey());
	//
	// if (configMemento != null) {
	// builder.initFromMemento(CONFIGURABLE_ROWS_ORDER, configMemento);
	// getBlockManager().initFromMemento(CONFIGURABLE_COLUMNS_ORDER, configMemento);
	// }
	// }
	//
	// }
	@Override
	protected void initConfigMemento(IMemento configMemento) {
		if (configMemento != null) {
			builder.initFromMemento(CONFIGURABLE_ROWS_ORDER, configMemento);
		}
	}

	/**
	 * Check the view extension point to see if we can enable the customise dialog
	 * 
	 * @return
	 */
	@Override
	protected boolean checkCustomisable() {

		if (initialStates != null) {

			for (final IScheduleBasedReportInitialStateExtension ext : initialStates) {

				final String viewId = ext.getViewID();

				if (viewId != null && viewId.equals(getViewSite().getId())) {
					final String customisableString = ext.getCustomisable();
					if (customisableString != null) {
						return customisableString.equals("true");
					}
				}
			}
		}
		return true;
	}

	/**
	 * Examine the view extension point to determine the default set of columns, order,row types and diff options.
	 */
	protected void setInitialState() {

		if (initialStates != null) {

			for (final IScheduleBasedReportInitialStateExtension ext : initialStates) {

				final String viewId = ext.getViewID();

				// Is this a matching view definition?
				if (viewId != null && viewId.equals(getViewSite().getId())) {
					// Get visibile columns and order
					{
						final InitialColumn[] initialColumns = ext.getInitialColumns();
						if (initialColumns != null) {
							final List<String> defaultOrder = new LinkedList<>();
							for (final InitialColumn col : initialColumns) {
								final String blockID = col.getID();
								ColumnBlock block = getBlockManager().getBlockByID(blockID);
								if (block == null) {
									block = getBlockManager().createBlock(blockID, "", ColumnType.NORMAL);
								}
								block.setUserVisible(true);
								defaultOrder.add(blockID);

							}
							getBlockManager().setBlockIDOrder(defaultOrder);
						}
					}
					// Get row types
					{
						final List<String> rowFilter = new ArrayList<>(ScheduleBasedReportBuilder.ROW_FILTER_ALL.length);
						final InitialRowType[] initialRows = ext.getInitialRows();
						if (initialRows != null) {
							for (final InitialRowType row : initialRows) {

								switch (row.getRowType()) {
								case "cargo":
									rowFilter.add(ScheduleBasedReportBuilder.ROW_FILTER_CARGO_ROW);
									break;
								case "long":
									rowFilter.add(ScheduleBasedReportBuilder.ROW_FILTER_LONG_CARGOES);
									break;
								case "short":
									rowFilter.add(ScheduleBasedReportBuilder.ROW_FILTER_SHORT_CARGOES);
									break;
								case "virtualcharters":
									rowFilter.add(ScheduleBasedReportBuilder.ROW_FILTER_CHARTER_OUT_ROW);
									break;
								case "event":
									rowFilter.add(ScheduleBasedReportBuilder.ROW_FILTER_VESSEL_EVENT_ROW);
									break;
								case "orphanlegs":
									rowFilter.add(ScheduleBasedReportBuilder.ROW_FILTER_VESSEL_START_ROW);
									break;
								}
							}
						}
						builder.setRowFilter(rowFilter.toArray(new String[0]));
					}
					// Get diff options
					{
						final List<String> diffOptions = new ArrayList<>(ScheduleBasedReportBuilder.DIFF_FILTER_ALL.length);
						final InitialDiffOption[] initialDiffOptions = ext.getInitialDiffOptions();
						if (initialDiffOptions != null) {
							for (final InitialDiffOption diffOption : initialDiffOptions) {

								switch (diffOption.getOption()) {

								case "vessel":
									diffOptions.add(ScheduleBasedReportBuilder.DIFF_FILTER_VESSEL_CHANGES);
									break;
								case "scenario":
									diffOptions.add(ScheduleBasedReportBuilder.DIFF_FILTER_PINNDED_SCENARIO);
									break;
								}
							}
						}
						builder.setDiffFilter(diffOptions.toArray(new String[0]));
					}
					break;
				}
			}
		}
	}

	@Override
	protected ITreeContentProvider getContentProvider() {
		final ITreeContentProvider superProvider = super.getContentProvider();
		return builder.createPNLColumnssContentProvider(superProvider);
	}

	@Override
	protected void processInputs(final Object[] result) {
		builder.processInputs(result);
	}

	@Override
	protected IScenarioInstanceElementCollector getElementCollector() {
		return builder.getElementCollector();
	}

	/**
	 * Returns a key of some kind for the element
	 * 
	 * @param element
	 * @return
	 */
	@Override
	public String getElementKey(final EObject element) {
		return builder.getElementKey(element);
	}

	@Override
	protected boolean isElementDifferent(final EObject pinnedObject, final EObject otherObject) {
		return builder.isElementDifferent(pinnedObject, otherObject);
	}

	@Override
	protected List<?> adaptSelectionFromWidget(final List<?> selection) {
		return builder.adaptSelectionFromWidget(selection);
	}

	/**
	 */
	@Override
	protected boolean handleSelections() {
		return true;
	}

	// /**
	// * Fills the top-right pulldown menu, adding an option to configure the columns visible in this view.
	// */
	// @Override
	// protected void fillLocalPullDown(final IMenuManager manager) {
	// super.fillLocalPullDown(manager);
	// final IWorkbench wb = PlatformUI.getWorkbench();
	// final IWorkbenchWindow win = wb.getActiveWorkbenchWindow();
	//
	// // Only create action if permitted.
	// if (customisableReport) {
	// Action configureColumnsAction = new Action("Configure Contents") {
	// @Override
	// public void run() {
	// final IColumnInfoProvider infoProvider = new ColumnConfigurationDialog.ColumnInfoAdapter() {
	//
	// @Override
	// public int getColumnIndex(final Object columnObj) {
	// return getBlockManager().getBlockIndex((ColumnBlock) columnObj);
	// }
	//
	// @Override
	// public boolean isColumnVisible(final Object columnObj) {
	// return getBlockManager().getBlockVisible((ColumnBlock) columnObj);
	// }
	//
	// };
	//
	// final IColumnUpdater updater = new ColumnConfigurationDialog.ColumnUpdaterAdapter() {
	//
	// @Override
	// public void setColumnVisible(final Object columnObj, final boolean visible) {
	//
	// ((ColumnBlock) columnObj).setUserVisible(visible);
	// viewer.refresh();
	//
	// }
	//
	// @Override
	// public void swapColumnPositions(final Object columnObj1, final Object columnObj2) {
	// getBlockManager().swapBlockOrder((ColumnBlock) columnObj1, (ColumnBlock) columnObj2);
	// viewer.refresh();
	// }
	//
	// };
	//
	// final Image nonVisibleIcon = AbstractUIPlugin.imageDescriptorFromPlugin(Activator.PLUGIN_ID, "icons/read_obj_disabled.gif").createImage();
	// final Image visibleIcon = AbstractUIPlugin.imageDescriptorFromPlugin(Activator.PLUGIN_ID, "icons/read_obj.gif").createImage();
	//
	// final ColumnConfigurationDialog dialog = new ColumnConfigurationDialog(win.getShell()) {
	//
	// @Override
	// protected IColumnInfoProvider getColumnInfoProvider() {
	// return infoProvider;
	// }
	//
	// @Override
	// protected ColumnLabelProvider getLabelProvider() {
	// return new ColumnLabelProvider() {
	// @Override
	// public String getText(final Object element) {
	// final ColumnBlock block = (ColumnBlock) element;
	// return block.blockName;
	// }
	//
	// @Override
	// public Image getImage(final Object element) {
	// final ColumnBlock block = (ColumnBlock) element;
	// if (block.isModeVisible()) {
	// return visibleIcon;
	// } else {
	// return nonVisibleIcon;
	// }
	// }
	//
	// @Override
	// public String getToolTipText(final Object element) {
	// final ColumnBlock block = (ColumnBlock) element;
	// return block.tooltip;
	// }
	// };
	// }
	//
	// @Override
	// protected IColumnUpdater getColumnUpdater() {
	// return updater;
	// }
	// };
	// dialog.setColumnsObjs(getBlockManager().getBlocksInVisibleOrder().toArray());
	// // dialog.setRowCheckBoxInfo(ScheduleBasedReportBuilder.ROW_FILTER_ALL, builder.getRowFilterInfo());
	// // dialog.setDiffCheckBoxInfo(ScheduleBasedReportBuilder.DIFF_FILTER_ALL, builder.getDiffFilterInfo());
	//
	// dialog.open();
	//
	//
	// nonVisibleIcon.dispose();
	// visibleIcon.dispose();
	//
	// synchronizer.refreshViewer();
	//
	// }
	//
	// };
	// manager.appendToGroup("additions", configureColumnsAction);
	// }
	//
	// }
	//

	protected void addDialogCheckBoxes(final ColumnConfigurationDialog dialog) {
		dialog.addCheckBoxInfo("Row Filters", ScheduleBasedReportBuilder.ROW_FILTER_ALL, builder.getRowFilterInfo());
		dialog.addCheckBoxInfo("Diff Filters", ScheduleBasedReportBuilder.DIFF_FILTER_ALL, builder.getDiffFilterInfo());
	}

	protected void postDialogOpen(final ColumnConfigurationDialog dialog) {
		builder.refreshDiffOptions();

	}

	/**
	 * Examine the eclipse registry for defined columns for this report and hook them in.
	 */
	@Override
	protected void registerReportColumns() {

		final EMFReportColumnManager manager = new EMFReportColumnManager();

		// Find any shared column factories and install.
		final Map<String, IScheduleColumnFactory> handlerMap = new HashMap<>();
		if (columnFactoryExtensions != null) {
			for (final IScheduleBasedColumnFactoryExtension ext : columnFactoryExtensions) {
				final String handlerID = ext.getHandlerID();
				handlerMap.put(handlerID, ext.getFactory());
			}
		}

		// Now find the column definitions themselves.
		if (columnExtensions != null) {

			for (final IScheduleBasedColumnExtension ext : columnExtensions) {
				IScheduleColumnFactory factory;
				if (ext.getHandlerID() != null) {
					factory = handlerMap.get(ext.getHandlerID());
				} else {
					factory = ext.getFactory();
				}
				if (factory != null) {
					factory.registerColumn(ext.getColumnID(), manager, builder);
				}
			}
		}

		// Create the actual columns instances.
		manager.addColumns(ScheduleBasedReportBuilder.CARGO_REPORT_TYPE_ID, this);
	}

}
