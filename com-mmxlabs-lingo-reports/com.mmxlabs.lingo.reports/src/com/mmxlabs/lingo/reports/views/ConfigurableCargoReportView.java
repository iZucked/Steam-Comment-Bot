/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2014
 * All rights reserved.
 */
package com.mmxlabs.lingo.reports.views;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.IMemento;
import org.eclipse.ui.IViewSite;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.XMLMemento;

import com.mmxlabs.lingo.reports.IScenarioInstanceElementCollector;
import com.mmxlabs.lingo.reports.IScenarioViewerSynchronizerOutput;
import com.mmxlabs.lingo.reports.utils.ColumnConfigurationDialog;
import com.mmxlabs.lingo.reports.utils.ColumnConfigurationDialog.IColumnInfoProvider;
import com.mmxlabs.lingo.reports.utils.ColumnConfigurationDialog.IColumnUpdater;
import com.mmxlabs.lingo.reports.utils.ScheduleDiffUtils;
import com.mmxlabs.lingo.reports.views.formatters.BaseFormatter;
import com.mmxlabs.lingo.reports.views.formatters.IntegerFormatter;
import com.mmxlabs.models.lng.cargo.CharterOutEvent;
import com.mmxlabs.models.lng.cargo.DischargeSlot;
import com.mmxlabs.models.lng.cargo.DryDockEvent;
import com.mmxlabs.models.lng.cargo.LoadSlot;
import com.mmxlabs.models.lng.cargo.MaintenanceEvent;
import com.mmxlabs.models.lng.cargo.Slot;
import com.mmxlabs.models.lng.cargo.VesselEvent;
import com.mmxlabs.models.lng.commercial.BaseEntityBook;
import com.mmxlabs.models.lng.commercial.BaseLegalEntity;
import com.mmxlabs.models.lng.commercial.CommercialModel;
import com.mmxlabs.models.lng.commercial.CommercialPackage;
import com.mmxlabs.models.lng.commercial.Contract;
import com.mmxlabs.models.lng.scenario.model.LNGScenarioModel;
import com.mmxlabs.models.lng.schedule.CargoAllocation;
import com.mmxlabs.models.lng.schedule.EntityProfitAndLoss;
import com.mmxlabs.models.lng.schedule.GeneratedCharterOut;
import com.mmxlabs.models.lng.schedule.GroupProfitAndLoss;
import com.mmxlabs.models.lng.schedule.OpenSlotAllocation;
import com.mmxlabs.models.lng.schedule.ProfitAndLossContainer;
import com.mmxlabs.models.lng.schedule.SchedulePackage;
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

	public ConfigurableCargoReportView() {
		super(ID);

		builder = new ScheduleBasedReportBuilder(this, pinDiffModeHelper);
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
		addPNLColumn(CommercialPackage.Literals.BASE_LEGAL_ENTITY__TRADING_BOOK);
		addPNLColumn(CommercialPackage.Literals.BASE_LEGAL_ENTITY__SHIPPING_BOOK);

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

		builder.createPinDiffColumns();
	}

	private Integer getEntityPNLEntry(final ProfitAndLossContainer container, final String entity, final EStructuralFeature bookContainmentFeature) {
		if (container == null) {
			return null;
		}

		final GroupProfitAndLoss groupProfitAndLoss = container.getGroupProfitAndLoss();
		if (groupProfitAndLoss == null) {
			return null;
		}

		int groupTotal = 0;
		boolean foundValue = false;
		for (final EntityProfitAndLoss entityPNL : groupProfitAndLoss.getEntityProfitAndLosses()) {
			if (entity == null || entityPNL.getEntity().getName().equals(entity)) {
				foundValue = true;
				final BaseEntityBook entityBook = entityPNL.getEntityBook();
				if (entityBook == null) {
					// Fall back code path for old models.
					if (bookContainmentFeature == CommercialPackage.Literals.BASE_LEGAL_ENTITY__TRADING_BOOK) {
						groupTotal += groupProfitAndLoss.getProfitAndLoss();
					}
				} else {
					if (entityBook.eContainmentFeature() == bookContainmentFeature) {
						groupTotal += entityPNL.getProfitAndLoss();
					}
				}
			}
		}
		if (foundValue) {
			return groupTotal;
		}
		// with a specific entity name, we search the upstream, shipping and downstream entities for the P&L data
		return null;
	}

	private void addPNLColumn(final EStructuralFeature bookContainmentFeature) {
		addPNLColumn("Group Total", null, bookContainmentFeature);
	}

	private void addSpecificEntityPNLColumn(final String entityName, final EStructuralFeature bookContainmentFeature) {
		final ColumnHandler handler = addPNLColumn(entityName, entityName, bookContainmentFeature);
		if (handler != null) {
			handler.setBlockName("P & L", ColumnType.NORMAL);
		}
	}

	private ColumnHandler addPNLColumn(final String entityLabel, final String entityKey, final EStructuralFeature bookContainmentFeature) {
		final String book = bookContainmentFeature == CommercialPackage.Literals.BASE_LEGAL_ENTITY__SHIPPING_BOOK ? "Shipping" : "Trading";
		final String title = String.format("P&L (%s - %s)", entityLabel, book);

		// HACK: don't the label to the entity column names if the column is for total group P&L
		if (entityKey != null) {
			if (entityColumnNames.contains(title)) {
				return null;
			}
			entityColumnNames.add(title);
		}

		return addColumn(title, ColumnType.NORMAL, new IntegerFormatter() {
			@Override
			public Integer getIntValue(final Object object) {
				ProfitAndLossContainer container = null;

				if (object instanceof CargoAllocation || object instanceof VesselEventVisit || object instanceof StartEvent || object instanceof GeneratedCharterOut
						|| object instanceof OpenSlotAllocation) {
					container = (ProfitAndLossContainer) object;
				}
				if (object instanceof SlotVisit) {
					final SlotVisit slotVisit = (SlotVisit) object;
					if (slotVisit.getSlotAllocation().getSlot() instanceof LoadSlot) {
						container = slotVisit.getSlotAllocation().getCargoAllocation();
					}
				}

				return getEntityPNLEntry(container, entityKey, bookContainmentFeature);
			}
		}, targetObjectRef);
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
		blockManager.saveToMemento(CONFIGURABLE_COLUMNS_ORDER, memento);
	}

	@Override
	public void createPartControl(final Composite parent) {
		super.createPartControl(parent);
		viewer.setComparator(GenericEMFTableDataModel.createGroupComparator(viewer.getComparator(), tableDataModel));
		if (memento != null) {
			blockManager.initFromMemento(CONFIGURABLE_COLUMNS_ORDER, memento);
		}

	}

	@Override
	protected ITreeContentProvider getContentProvider() {
		final ITreeContentProvider superProvider = super.getContentProvider();

		return new ITreeContentProvider() {
			@Override
			public void inputChanged(final Viewer viewer, final Object oldInput, final Object newInput) {
				superProvider.inputChanged(viewer, oldInput, newInput);
				Display.getCurrent().asyncExec(new Runnable() {
					@Override
					public void run() {
						if (viewer.getControl().isDisposed()) {
							return;
						}

						if (newInput instanceof IScenarioViewerSynchronizerOutput) {
							final IScenarioViewerSynchronizerOutput synchronizerOutput = (IScenarioViewerSynchronizerOutput) newInput;
							final Collection<LNGScenarioModel> rootObjects = synchronizerOutput.getLNGScenarioModels();
							for (final String s : entityColumnNames) {
								removeColumn(s);
							}

							entityColumnNames.clear();

							for (final LNGScenarioModel rootObject : rootObjects) {

								final CommercialModel commercialModel = rootObject.getCommercialModel();
								if (commercialModel != null) {
									for (final BaseLegalEntity e : commercialModel.getEntities()) {
										addSpecificEntityPNLColumn(e.getName(), CommercialPackage.Literals.BASE_LEGAL_ENTITY__TRADING_BOOK);
										addSpecificEntityPNLColumn(e.getName(), CommercialPackage.Literals.BASE_LEGAL_ENTITY__SHIPPING_BOOK);
									}
								}
							}
						}

						viewer.refresh();
					}
				});
			}

			@Override
			public void dispose() {
				superProvider.dispose();
			}

			@Override
			public Object[] getElements(final Object object) {
				return superProvider.getElements(object);
			}

			@Override
			public Object[] getChildren(final Object parentElement) {
				return superProvider.getChildren(parentElement);
			}

			@Override
			public Object getParent(final Object element) {
				return superProvider.getParent(element);
			}

			@Override
			public boolean hasChildren(final Object element) {
				return superProvider.hasChildren(element);
			}
		};
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
	public String getElementKey(EObject element) {
		return builder.getElementKey(element);
	}

	@Override
	protected boolean isElementDifferent(final EObject pinnedObject, final EObject otherObject) {
		return ScheduleDiffUtils.isElementDifferent((EObject) pinnedObject.eGet(targetObjectRef), (EObject) otherObject.eGet(targetObjectRef));
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
								return ((ColumnBlock) element).name;
							}

						};
					}

					@Override
					protected IColumnUpdater getColumnUpdater() {
						return updater;
					}
				};
				dialog.setColumnsObjs(blockManager.getBlocksInVisibleOrder().toArray());
				dialog.setCheckBoxInfo(ScheduleBasedReportBuilder.ROW_FILTER_ALL, builder.getRowFilterInfo());
				dialog.open();

				synchronizer.refreshViewer();

			}

		};
		manager.appendToGroup("additions", configureColumnsAction);
	}

}
