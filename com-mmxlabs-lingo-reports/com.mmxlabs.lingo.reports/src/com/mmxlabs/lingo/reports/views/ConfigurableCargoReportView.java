/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2014
 * All rights reserved.
 */
package com.mmxlabs.lingo.reports.views;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.viewers.ITableLabelProvider;
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

import com.google.common.collect.Lists;
import com.google.inject.Inject;
import com.mmxlabs.lingo.reports.IScenarioInstanceElementCollector;
import com.mmxlabs.lingo.reports.components.ColumnBlock;
import com.mmxlabs.lingo.reports.components.ColumnType;
import com.mmxlabs.lingo.reports.components.EMFReportView;
import com.mmxlabs.lingo.reports.components.ScheduleBasedReportBuilder;
import com.mmxlabs.lingo.reports.components.EMFReportView.SimpleEmfBlockColumnFactory;
import com.mmxlabs.lingo.reports.components.columns.IScheduleBasedColumnExtension;
import com.mmxlabs.lingo.reports.components.columns.IScheduleBasedColumnFactoryExtension;
import com.mmxlabs.lingo.reports.components.columns.IScheduleColumnFactory;
import com.mmxlabs.lingo.reports.extensions.EMFReportColumnManager;
import com.mmxlabs.lingo.reports.internal.Activator;
import com.mmxlabs.lingo.reports.utils.ColumnConfigurationDialog;
import com.mmxlabs.lingo.reports.utils.ColumnConfigurationDialog.IColumnInfoProvider;
import com.mmxlabs.lingo.reports.utils.ColumnConfigurationDialog.IColumnUpdater;
import com.mmxlabs.lingo.reports.views.formatters.BaseFormatter;
import com.mmxlabs.lingo.reports.views.formatters.IntegerFormatter;
import com.mmxlabs.models.lng.cargo.CharterOutEvent;
import com.mmxlabs.models.lng.cargo.DischargeSlot;
import com.mmxlabs.models.lng.cargo.DryDockEvent;
import com.mmxlabs.models.lng.cargo.LoadSlot;
import com.mmxlabs.models.lng.cargo.MaintenanceEvent;
import com.mmxlabs.models.lng.cargo.Slot;
import com.mmxlabs.models.lng.cargo.VesselEvent;
import com.mmxlabs.models.lng.commercial.CommercialPackage;
import com.mmxlabs.models.lng.schedule.CargoAllocation;
import com.mmxlabs.models.lng.schedule.Event;
import com.mmxlabs.models.lng.schedule.GeneratedCharterOut;
import com.mmxlabs.models.lng.schedule.GroupProfitAndLoss;
import com.mmxlabs.models.lng.schedule.Idle;
import com.mmxlabs.models.lng.schedule.Journey;
import com.mmxlabs.models.lng.schedule.OpenSlotAllocation;
import com.mmxlabs.models.lng.schedule.SchedulePackage;
import com.mmxlabs.models.lng.schedule.SlotVisit;
import com.mmxlabs.models.lng.schedule.StartEvent;
import com.mmxlabs.models.lng.schedule.VesselEventVisit;
import com.mmxlabs.models.ui.tabular.generic.GenericEMFTableDataModel;

/**
 */
public class ConfigurableCargoReportView extends EMFReportView {
	/**
	 * The ID of the view as specified by the extension.
	 */
	private static final String CONFIGURABLE_COLUMNS_ORDER = "CONFIGURABLE_COLUMNS_ORDER";
	public static final String ID = "com.mmxlabs.shiplingo.platform.reports.views.ConfigurableCargoReportView";
	private static final String CONFIGURABLE_ROWS_ORDER = "CONFIGURABLE_ROWS_ORDER";
	private static final String CARGO_REPORT_TYPE_ID = "CARGO_REPORT_TYPE_ID";
	private static final String CONFIGURABLE_COLUMNS_REPORT_KEY = "CONFIGURABLE_COLUMNS_REPORT_KEY";

	final List<String> entityColumnNames = new ArrayList<String>();

	private final ScheduleBasedReportBuilder builder;

	private final EPackage tableDataModel;

	private IMemento memento;

	@Inject(optional = true)
	private Iterable<IScheduleBasedColumnFactoryExtension> columnFactoryExtensions;

	@Inject(optional = true)
	private Iterable<IScheduleBasedColumnExtension> columnExtensions;

	@Inject
	public ConfigurableCargoReportView(final ScheduleBasedReportBuilder builder) {
		super(ID);

		this.builder = builder;
		builder.setReport(this);
		builder.setPinDiffModeHelper(pinDiffModeHelper);

		tableDataModel = builder.getTableDataModel();
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
		builder.saveToMemento(CONFIGURABLE_ROWS_ORDER, configMemento);
		blockManager.saveToMemento(CONFIGURABLE_COLUMNS_ORDER, configMemento);
	}

	@Override
	public void createPartControl(final Composite parent) {
		final EMFReportColumnManager manager = new EMFReportColumnManager();

		// See ActivatorModule
		Activator.getDefault().getInjector().injectMembers(this);

		registerReportColumns(manager, builder);

		super.createPartControl(parent);

		// Set default visibility - hardcoded for now, TODO take from ext point.
		{
			final List<String> defaultOrder = Lists.newArrayList("com.mmxlabs.lingo.reports.components.columns.schedule.schedule", "com.mmxlabs.lingo.reports.components.columns.schedule.l-id",
					"com.mmxlabs.lingo.reports.components.columns.schedule.d-id", "com.mmxlabs.lingo.reports.components.columns.schedule.diff_prevwiring",
					"com.mmxlabs.lingo.reports.components.columns.schedule.type", "com.mmxlabs.lingo.reports.components.columns.schedule.vessel",
					"com.mmxlabs.lingo.reports.components.columns.schedule.diff_prevvessel", "com.mmxlabs.lingo.reports.components.columns.schedule.pnl_total",
					"com.mmxlabs.lingo.reports.components.columns.schedule.buyport", "com.mmxlabs.lingo.reports.components.columns.schedule.loaddate",
					"com.mmxlabs.lingo.reports.components.columns.schedule.sellport", "com.mmxlabs.lingo.reports.components.columns.schedule.purchasecontract",
					"com.mmxlabs.lingo.reports.components.columns.schedule.dischargedate", "com.mmxlabs.lingo.reports.components.columns.schedule.salescontract",
					"com.mmxlabs.lingo.reports.components.columns.schedule.buyprice", "com.mmxlabs.lingo.reports.components.columns.schedule.sellprice",
					"com.mmxlabs.lingo.reports.components.columns.schedule.buyvolume_m3", "com.mmxlabs.lingo.reports.components.columns.schedule.sellvolume_m3",
					"com.mmxlabs.lingo.reports.components.columns.schedule.diff_permutation");

			for (final String blockID : defaultOrder) {
				ColumnBlock block = blockManager.getBlockByID(blockID);
				if (block == null) {
					block = blockManager.createBlock(blockID, "", ColumnType.NORMAL);
				}
				block.setUserVisible(true);

				blockManager.setBlockIDOrder(defaultOrder);
			}
			builder.setRowFilter(ScheduleBasedReportBuilder.ROW_FILTER_ALL);
		}

		// force the columns to be immovable except by using the config dialog
		setColumnsImmovable();

		viewer.setComparator(GenericEMFTableDataModel.createGroupComparator(viewer.getComparator(), tableDataModel));

		if (memento != null) {
			final IMemento configMemento = memento.getChild(getColumnSettingsMementoKey());

			if (configMemento != null) {
				builder.initFromMemento(CONFIGURABLE_ROWS_ORDER, configMemento);
				blockManager.initFromMemento(CONFIGURABLE_COLUMNS_ORDER, configMemento);
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

	/**
	 * Fills the top-right pulldown menu, adding an option to configure the columns visible in this view.
	 */
	@Override
	protected void fillLocalPullDown(final IMenuManager manager) {
		super.fillLocalPullDown(manager);
		final IWorkbench wb = PlatformUI.getWorkbench();
		final IWorkbenchWindow win = wb.getActiveWorkbenchWindow();

		final Action configureColumnsAction = new Action("Configure Contents") {
			@Override
			public void run() {
				final IColumnInfoProvider infoProvider = new ColumnConfigurationDialog.ColumnInfoAdapter() {

					@Override
					public int getColumnIndex(final Object columnObj) {
						return blockManager.getBlockIndex((ColumnBlock) columnObj);
					}

					@Override
					public boolean isColumnVisible(final Object columnObj) {
						return blockManager.getBlockVisible((ColumnBlock) columnObj);
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
						blockManager.swapBlockOrder((ColumnBlock) columnObj1, (ColumnBlock) columnObj2);
						viewer.refresh();
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
					protected ITableLabelProvider getLabelProvider() {
						return new TableLabelProvider() {
							@Override
							public String getColumnText(final Object element, final int columnIndex) {
								final ColumnBlock block = (ColumnBlock) element;
								return block.blockName;
							}

							@Override
							public Image getColumnImage(final Object element, final int columnIndex) {
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
								return block.blockID;
							}
						};
					}

					@Override
					protected IColumnUpdater getColumnUpdater() {
						return updater;
					}
				};
				dialog.setColumnsObjs(blockManager.getBlocksInVisibleOrder().toArray());
				// dialog.setRowCheckBoxInfo(ScheduleBasedReportBuilder.ROW_FILTER_ALL, builder.getRowFilterInfo());
				// dialog.setDiffCheckBoxInfo(ScheduleBasedReportBuilder.DIFF_FILTER_ALL, builder.getDiffFilterInfo());
				dialog.addCheckBoxInfo("Row Filters", ScheduleBasedReportBuilder.ROW_FILTER_ALL, builder.getRowFilterInfo());
				dialog.addCheckBoxInfo("Diff Filters", ScheduleBasedReportBuilder.DIFF_FILTER_ALL, builder.getDiffFilterInfo());
				dialog.open();

				builder.refreshDiffOptions();

				nonVisibleIcon.dispose();
				visibleIcon.dispose();

				synchronizer.refreshViewer();

			}

		};
		manager.appendToGroup("additions", configureColumnsAction);
	}

	private void registerReportColumns(final EMFReportColumnManager manager, final ScheduleBasedReportBuilder builder) {

		final Map<String, IScheduleColumnFactory> handlerMap = new HashMap<>();
		if (columnFactoryExtensions != null) {
			for (final IScheduleBasedColumnFactoryExtension ext : columnFactoryExtensions) {
				final String handlerID = ext.getHandlerID();
				handlerMap.put(handlerID, ext.getFactory());
			}
		}
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

		// Create the actual columns
		manager.addColumns(CARGO_REPORT_TYPE_ID, this);
	}
}
