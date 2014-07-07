/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2014
 * All rights reserved.
 */
package com.mmxlabs.lingo.reports.views;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.ITreeContentProvider;
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

import com.mmxlabs.lingo.reports.IScenarioInstanceElementCollector;
import com.mmxlabs.lingo.reports.components.ColumnBlock;
import com.mmxlabs.lingo.reports.components.ColumnType;
import com.mmxlabs.lingo.reports.components.EMFReportView;
import com.mmxlabs.lingo.reports.components.ScheduleBasedReportBuilder;
import com.mmxlabs.lingo.reports.internal.Activator;
import com.mmxlabs.lingo.reports.utils.ColumnConfigurationDialog;
import com.mmxlabs.lingo.reports.utils.ColumnConfigurationDialog.IColumnInfoProvider;
import com.mmxlabs.lingo.reports.utils.ColumnConfigurationDialog.IColumnUpdater;
import com.mmxlabs.lingo.reports.utils.ScheduleDiffUtils;
import com.mmxlabs.lingo.reports.views.formatters.BaseFormatter;
import com.mmxlabs.models.lng.cargo.CharterOutEvent;
import com.mmxlabs.models.lng.cargo.DischargeSlot;
import com.mmxlabs.models.lng.cargo.DryDockEvent;
import com.mmxlabs.models.lng.cargo.LoadSlot;
import com.mmxlabs.models.lng.cargo.MaintenanceEvent;
import com.mmxlabs.models.lng.cargo.Slot;
import com.mmxlabs.models.lng.cargo.VesselEvent;
import com.mmxlabs.models.lng.commercial.CommercialPackage;
import com.mmxlabs.models.lng.commercial.Contract;
import com.mmxlabs.models.lng.schedule.CargoAllocation;
import com.mmxlabs.models.lng.schedule.GeneratedCharterOut;
import com.mmxlabs.models.lng.schedule.OpenSlotAllocation;
import com.mmxlabs.models.lng.schedule.SlotAllocation;
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

	final List<String> entityColumnNames = new ArrayList<String>();

	private final ScheduleBasedReportBuilder builder;

	private final EPackage tableDataModel;
	private final EStructuralFeature nameObjectRef;
	private final EStructuralFeature name2ObjectRef;
	private final EStructuralFeature targetObjectRef;
	private final EStructuralFeature cargoAllocationRef;
	private final EStructuralFeature loadAllocationRef;
	private final EStructuralFeature dischargeAllocationRef;
	private final EStructuralFeature openSlotAllocationRef;

	private IMemento memento;

	@Inject
	public ConfigurableCargoReportView(final ScheduleBasedReportBuilder builder) {
		super(ID);

		this.builder = builder;
		builder.setReport(this);
		builder.setPinDiffModeHelper(pinDiffModeHelper);

		tableDataModel = builder.getTableDataModel();
		nameObjectRef = builder.getNameObjectRef();
		name2ObjectRef = builder.getName2ObjectRef();
		targetObjectRef = builder.getTargetObjectRef();
		cargoAllocationRef = builder.getCargoAllocationRef();
		loadAllocationRef = builder.getLoadAllocationRef();
		dischargeAllocationRef = builder.getDischargeAllocationRef();
		openSlotAllocationRef = builder.getOpenSlotAllocationRef();

		addScheduleColumn("Schedule", containingScheduleFormatter);

		addColumn("L-ID", ColumnType.NORMAL, objectFormatter, nameObjectRef);
		addColumn("D-ID", ColumnType.NORMAL, objectFormatter, name2ObjectRef);

		// add the total (aggregate) P&L column
		builder.addPNLColumn(CommercialPackage.Literals.BASE_LEGAL_ENTITY__TRADING_BOOK);
		builder.addPNLColumn(CommercialPackage.Literals.BASE_LEGAL_ENTITY__SHIPPING_BOOK);

		addColumn("Discharge Port", ColumnType.NORMAL, new BaseFormatter() {
			@Override
			public String format(final Object object) {
				if (object instanceof SlotAllocation) {
					final SlotAllocation slotAllocation = (SlotAllocation) object;
					return slotAllocation.getPort().getName();
				}
				return null;
			}

			@Override
			public Comparable<?> getComparable(final Object object) {
				if (object instanceof SlotAllocation) {
					final SlotAllocation slotAllocation = (SlotAllocation) object;
					return slotAllocation.getPort().getName();
				}
				return "";
			}
		}, dischargeAllocationRef);
		addColumn("Sales Contract", ColumnType.NORMAL, new BaseFormatter() {
			@Override
			public String format(final Object object) {
				if (object instanceof SlotAllocation) {
					final SlotAllocation slotAllocation = (SlotAllocation) object;
					final Contract contract = slotAllocation.getContract();
					if (contract != null) {
						return contract.getName();
					}
				}
				return null;
			}

			@Override
			public Comparable<?> getComparable(final Object object) {
				if (object instanceof SlotAllocation) {
					final SlotAllocation slotAllocation = (SlotAllocation) object;
					final Contract contract = slotAllocation.getContract();
					if (contract != null) {
						return contract.getName();
					}
				}
				return "";
			}
		}, dischargeAllocationRef);

		addColumn("Purchase Price", ColumnType.NORMAL, new BaseFormatter() {
			@Override
			public String format(final Object object) {
				if (object instanceof SlotAllocation) {
					final SlotAllocation slotAllocation = (SlotAllocation) object;
					return super.format(slotAllocation.getPrice());
				}
				return null;
			}

			@Override
			public Comparable getComparable(final Object object) {
				if (object instanceof SlotAllocation) {
					final SlotAllocation slotAllocation = (SlotAllocation) object;
					return slotAllocation.getPrice();
				}
				return 0.0;
			}
		}, loadAllocationRef);
		addColumn("Sales Price", ColumnType.NORMAL, new BaseFormatter() {
			@Override
			public String format(final Object object) {
				if (object instanceof SlotAllocation) {
					final SlotAllocation slotAllocation = (SlotAllocation) object;
					return super.format(slotAllocation.getPrice());
				}
				return null;
			}

			@Override
			public Comparable getComparable(final Object object) {
				if (object instanceof SlotAllocation) {
					final SlotAllocation slotAllocation = (SlotAllocation) object;
					return slotAllocation.getPrice();
				}
				return 0.0;
			}
		}, dischargeAllocationRef);

		addColumn("Type", ColumnType.NORMAL, new BaseFormatter() {
			@Override
			public String format(final Object object) {
				if (object instanceof OpenSlotAllocation) {
					final OpenSlotAllocation openSlotAllocation = (OpenSlotAllocation) object;
					String type = "Open Slot";
					final Slot slot = openSlotAllocation.getSlot();
					if (slot instanceof LoadSlot) {
						type = "Long";
					} else if (slot instanceof DischargeSlot) {
						type = "Short";
					}
					return type;
				} else if (object instanceof SlotVisit || object instanceof CargoAllocation) {
					return "Cargo";
				} else if (object instanceof StartEvent) {
					return "Start";
				} else if (object instanceof GeneratedCharterOut) {
					return "Charter Out (virt)";
				} else if (object instanceof VesselEventVisit) {
					final VesselEvent vesselEvent = ((VesselEventVisit) object).getVesselEvent();
					if (vesselEvent instanceof DryDockEvent) {
						return "Dry Dock";
					} else if (vesselEvent instanceof MaintenanceEvent) {
						return "Maintenance";
					} else if (vesselEvent instanceof CharterOutEvent) {
						return "Charter Out";
					}
				}
				return "Unknown";
			}
		}, targetObjectRef);

		builder.createPNLColumnBlock();

		builder.createPinDiffColumns();
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
		builder.saveToMemento(CONFIGURABLE_ROWS_ORDER, memento);
		blockManager.saveToMemento(CONFIGURABLE_COLUMNS_ORDER, memento);
	}

	@Override
	public void createPartControl(final Composite parent) {
		super.createPartControl(parent);
		viewer.setComparator(GenericEMFTableDataModel.createGroupComparator(viewer.getComparator(), tableDataModel));
		if (memento != null) {
			builder.initFromMemento(CONFIGURABLE_ROWS_ORDER, memento);
			blockManager.initFromMemento(CONFIGURABLE_COLUMNS_ORDER, memento);
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
								ColumnBlock block = (ColumnBlock) element;
								return block.name;
							}

							@Override
							public Image getColumnImage(Object element, int columnIndex) {
								ColumnBlock block = (ColumnBlock) element;
								if (block.isModeVisible()) {
									return visibleIcon;
								} else {
									return nonVisibleIcon;
								}
							}

						};
					}

					@Override
					protected IColumnUpdater getColumnUpdater() {
						return updater;
					}
				};
				dialog.setColumnsObjs(blockManager.getBlocksInVisibleOrder().toArray());
				dialog.setRowCheckBoxInfo(ScheduleBasedReportBuilder.ROW_FILTER_ALL, builder.getRowFilterInfo());
				dialog.setDiffCheckBoxInfo(ScheduleBasedReportBuilder.DIFF_FILTER_ALL, builder.getDiffFilterInfo());
				dialog.open();

				builder.refreshDiffOptions();
				
				nonVisibleIcon.dispose();

				synchronizer.refreshViewer();

			}

		};
		manager.appendToGroup("additions", configureColumnsAction);
	}

}
