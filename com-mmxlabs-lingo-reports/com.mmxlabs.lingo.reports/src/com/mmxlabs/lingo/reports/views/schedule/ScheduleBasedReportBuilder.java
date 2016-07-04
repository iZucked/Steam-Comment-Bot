/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
package com.mmxlabs.lingo.reports.views.schedule;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.emf.common.notify.Notifier;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.jdt.annotation.Nullable;
import org.eclipse.nebula.widgets.grid.GridColumn;

import com.google.inject.Inject;
import com.mmxlabs.common.Pair;
import com.mmxlabs.lingo.reports.components.ColumnBlock;
import com.mmxlabs.lingo.reports.components.ColumnBlockManager;
import com.mmxlabs.lingo.reports.components.ColumnHandler;
import com.mmxlabs.lingo.reports.components.ColumnType;
import com.mmxlabs.lingo.reports.components.EmfBlockColumnFactory;
import com.mmxlabs.lingo.reports.utils.ColumnConfigurationDialog.OptionInfo;
import com.mmxlabs.lingo.reports.utils.ICustomRelatedSlotHandler;
import com.mmxlabs.lingo.reports.utils.ICustomRelatedSlotHandlerExtension;
import com.mmxlabs.lingo.reports.utils.ScheduleDiffUtils;
import com.mmxlabs.lingo.reports.views.AbstractReportBuilder;
import com.mmxlabs.lingo.reports.views.formatters.IntegerFormatter;
import com.mmxlabs.lingo.reports.views.schedule.model.Row;
import com.mmxlabs.lingo.reports.views.schedule.model.ScheduleReportPackage;
import com.mmxlabs.models.lng.cargo.DischargeSlot;
import com.mmxlabs.models.lng.cargo.LoadSlot;
import com.mmxlabs.models.lng.commercial.BaseEntityBook;
import com.mmxlabs.models.lng.commercial.BaseLegalEntity;
import com.mmxlabs.models.lng.commercial.CommercialModel;
import com.mmxlabs.models.lng.commercial.CommercialPackage;
import com.mmxlabs.models.lng.scenario.model.LNGScenarioModel;
import com.mmxlabs.models.lng.scenario.model.util.ScenarioModelUtil;
import com.mmxlabs.models.lng.schedule.CargoAllocation;
import com.mmxlabs.models.lng.schedule.EndEvent;
import com.mmxlabs.models.lng.schedule.EntityProfitAndLoss;
import com.mmxlabs.models.lng.schedule.Event;
import com.mmxlabs.models.lng.schedule.GeneratedCharterOut;
import com.mmxlabs.models.lng.schedule.GroupProfitAndLoss;
import com.mmxlabs.models.lng.schedule.OpenSlotAllocation;
import com.mmxlabs.models.lng.schedule.ProfitAndLossContainer;
import com.mmxlabs.models.lng.schedule.SlotAllocation;
import com.mmxlabs.models.lng.schedule.SlotVisit;
import com.mmxlabs.models.lng.schedule.StartEvent;
import com.mmxlabs.models.lng.schedule.VesselEventVisit;
import com.mmxlabs.models.ui.tabular.ICellRenderer;

/**
 * Big helper class for any report based on {@link CargoAllocation}s, {@link OpenSlotAllocation}s, or other events. This builds the internal report data model and handles pin/diff comparison hooks.
 * Currently this class also some generic columns used in these reports but these should be broken out into separate classes as part of FogBugz: 51/
 * 
 * @author Simon Goodall
 * 
 */
public class ScheduleBasedReportBuilder extends AbstractReportBuilder {
	public static final String CARGO_REPORT_TYPE_ID = "CARGO_REPORT_TYPE_ID";

	public static final String COLUMN_BLOCK_PNL = "com.mmxlabs.lingo.reports.components.columns.schedule.pnl_group";

	public static final OptionInfo ROW_FILTER_LONG_CARGOES = new OptionInfo("ROW_FILTER_LONG_CARGOES", "Longs");
	public static final OptionInfo ROW_FILTER_SHORT_CARGOES = new OptionInfo("ROW_FILTER_SHORT_CARGOES", "Shorts");
	public static final OptionInfo ROW_FILTER_VESSEL_START_ROW = new OptionInfo("ROW_FILTER_VESSEL_START_ROW", "Start ballast legs");
	public static final OptionInfo ROW_FILTER_VESSEL_END_ROW = new OptionInfo("ROW_FILTER_VESSEL_END_ROW", "End event legs");
	public static final OptionInfo ROW_FILTER_VESSEL_EVENT_ROW = new OptionInfo("ROW_FILTER_VESSEL_EVENT_ROW", "Vessel Events");
	public static final OptionInfo ROW_FILTER_CHARTER_OUT_ROW = new OptionInfo("ROW_FILTER_CHARTER_OUT_ROW", "Charter Outs (Virtual)");
	public static final OptionInfo ROW_FILTER_CARGO_ROW = new OptionInfo("ROW_FILTER_CARGO_ROW", "Cargoes");

	public static final OptionInfo DIFF_FILTER_VESSEL_CHANGES = new OptionInfo("DIFF_FILTER_VESSEL_CHANGES", "Show Vessel Changes");

	private final ScheduleDiffUtils scheduleDiffUtils = new ScheduleDiffUtils();

	@Inject(optional = true)
	private Iterable<ICustomRelatedSlotHandlerExtension> customRelatedSlotHandlers;

	public ScheduleBasedReportBuilder() {
		/** All filters (note this order is also used in the {@link ConfigurableScheduleReportView} dialog */
		DIFF_FILTER_ALL = new OptionInfo[] { DIFF_FILTER_PINNDED_SCENARIO, DIFF_FILTER_VESSEL_CHANGES };

		/** All filters (note this order is also used in the {@link ConfigurableScheduleReportView} dialog */
		ROW_FILTER_ALL = new OptionInfo[] { ROW_FILTER_CARGO_ROW, ROW_FILTER_LONG_CARGOES, ROW_FILTER_SHORT_CARGOES, ROW_FILTER_VESSEL_EVENT_ROW, ROW_FILTER_CHARTER_OUT_ROW,
				ROW_FILTER_VESSEL_START_ROW, ROW_FILTER_VESSEL_END_ROW };

	}

	public boolean showRow(Row row) {
		OpenSlotAllocation openSlotAllocation = row.getOpenSlotAllocation();
		if (openSlotAllocation != null) {
			return showOpenSlot(openSlotAllocation);
		}
		SlotAllocation loadAllocation = row.getLoadAllocation();
		if (loadAllocation != null) {
			return showEvent(loadAllocation.getSlotVisit());
		}
		EObject target = row.getTarget();
		if (target instanceof Event) {
			return showEvent((Event) target);
		}

		return true;
	}

	public boolean showOpenSlot(final OpenSlotAllocation openSlotAllocation) {

		if (openSlotAllocation.getSlot() instanceof LoadSlot) {
			return rowFilterInfo.contains(ROW_FILTER_LONG_CARGOES.id);
		} else if (openSlotAllocation.getSlot() instanceof DischargeSlot) {
			return rowFilterInfo.contains(ROW_FILTER_SHORT_CARGOES.id);
		}
		return false;
	}

	public boolean showEvent(final Event event) {
		if (event instanceof StartEvent) {
			return rowFilterInfo.contains(ROW_FILTER_VESSEL_START_ROW.id);
		} else if (event instanceof EndEvent) {
			return rowFilterInfo.contains(ROW_FILTER_VESSEL_END_ROW.id);
		} else if (event instanceof VesselEventVisit) {
			return rowFilterInfo.contains(ROW_FILTER_VESSEL_EVENT_ROW.id);
		} else if (event instanceof GeneratedCharterOut) {
			return rowFilterInfo.contains(ROW_FILTER_CHARTER_OUT_ROW.id);
		} else if (event instanceof SlotVisit) {
			final SlotVisit slotVisit = (SlotVisit) event;
			if (slotVisit.getSlotAllocation().getSlot() instanceof LoadSlot) {
				return rowFilterInfo.contains(ROW_FILTER_CARGO_ROW.id);
			}
		}
		return false;
	}

	public List<ICustomRelatedSlotHandler> getCustomRelatedSlotHandlers() {
		final List<ICustomRelatedSlotHandler> l = new LinkedList<ICustomRelatedSlotHandler>();
		if (customRelatedSlotHandlers != null) {
			for (final ICustomRelatedSlotHandlerExtension h : customRelatedSlotHandlers) {
				l.add(h.getInstance());
			}
		}
		return l;
	}

	// / Normal Columns

	// ////// Pin / Diff Columns

	@Override
	public void refreshDiffOptions() {
		scheduleDiffUtils.setCheckAssignmentDifferences(diffFilterInfo.contains(DIFF_FILTER_VESSEL_CHANGES.id));
		// pinDiffModeHelper.setShowPinnedData(diffFilterInfo.contains(DIFF_FILTER_PINNDED_SCENARIO.id));
	}

	protected Pair<EObject, CargoAllocation> getIfCargoAllocation(final Object obj, final EStructuralFeature cargoAllocationRef) {
		if (obj instanceof EObject) {
			final EObject eObj = (EObject) obj;
			if (eObj.eIsSet(cargoAllocationRef)) {
				return new Pair<EObject, CargoAllocation>(eObj, (CargoAllocation) eObj.eGet(cargoAllocationRef));
			}
		}
		return null;
	}

	// //// P&L Columns

	private final List<String> entityColumnNames = new ArrayList<String>();

	private IAdaptable adaptableReport;

	public EmfBlockColumnFactory getEmptyPNLColumnBlockFactory() {
		return new EmfBlockColumnFactory() {

			@Override
			public ColumnHandler addColumn(final ColumnBlockManager blockManager) {
				ColumnBlock block = blockManager.getBlockByID(COLUMN_BLOCK_PNL);
				if (block == null) {
					block = blockManager.createBlock(COLUMN_BLOCK_PNL, "[P&L]", ColumnType.NORMAL);
				}
				block.setPlaceholder(true);
				return null;
			}

		};
	}

	public void refreshPNLColumns(List<LNGScenarioModel> rootObjects) {

		// Clear existing entity columns
		for (final String s : entityColumnNames) {
			blockManager.removeColumn(s);
		}

		entityColumnNames.clear();

		List<ColumnHandler> handlers = new LinkedList<>();
		for (final LNGScenarioModel rootObject : rootObjects) {

			if (rootObject != null) {
				final CommercialModel commercialModel = ScenarioModelUtil.getCommercialModel(rootObject);
				for (final BaseLegalEntity e : commercialModel.getEntities()) {
					handlers.add(addPNLColumn(COLUMN_BLOCK_PNL, e.getName(), e.getName(), CommercialPackage.Literals.BASE_LEGAL_ENTITY__TRADING_BOOK));
					handlers.add(addPNLColumn(COLUMN_BLOCK_PNL, e.getName(), e.getName(), CommercialPackage.Literals.BASE_LEGAL_ENTITY__SHIPPING_BOOK));
				}
			}
		}
		for (final ColumnHandler handler : handlers) {
			if (handler != null) {
				final GridColumn column = handler.createColumn().getColumn();
				column.setVisible(handler.block.getVisible());
				column.pack();
			}
		}

	}

	public EmfBlockColumnFactory getTotalPNLColumnFactory(final String columnId, @Nullable final EStructuralFeature bookContainmentFeature) {
		return new EmfBlockColumnFactory() {
			@Override
			public ColumnHandler addColumn(final ColumnBlockManager blockManager) {
				final String book;
				if (bookContainmentFeature == CommercialPackage.Literals.BASE_LEGAL_ENTITY__SHIPPING_BOOK) {
					book = "Shipping";
				} else if (bookContainmentFeature == CommercialPackage.Literals.BASE_LEGAL_ENTITY__TRADING_BOOK) {
					book = "Trading";
				} else if (bookContainmentFeature == CommercialPackage.Literals.BASE_LEGAL_ENTITY__UPSTREAM_BOOK) {
					book = "Upstream";
				} else if (bookContainmentFeature == null) {
					book = "Total";
				} else {
					throw new IllegalArgumentException("Unknown entity book type");
				}
				final String title = String.format("P&L (%s)", book);

				final ColumnBlock block = blockManager.createBlock(columnId, title, ColumnType.NORMAL);
				return blockManager.createColumn(block, title, new IntegerFormatter() {
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

						return getEntityPNLEntry(container, null, bookContainmentFeature);
					}
				}, ScheduleReportPackage.Literals.ROW__TARGET);
			}

		};

	}

	public ColumnHandler addWiringColumn(final String blockId) {

		return blockManager.createColumn(blockManager.getBlockByID(blockId), "Wiring", new ICellRenderer() {

			@Override
			public Comparable getComparable(Object object) {
				// TODO Auto-generated method stub
				return null;
			}

			@Override
			public String render(Object object) {
				// TODO Auto-generated method stub
				return null;
			}

			@Override
			public Object getFilterValue(Object object) {
				// TODO Auto-generated method stub
				return null;
			}

			@Override
			public Iterable<Pair<Notifier, List<Object>>> getExternalNotifiers(Object object) {
				// TODO Auto-generated method stub
				return null;
			}

			@Override
			public boolean isValueUnset(Object object) {
				return false;
			}

		}, false, ScheduleReportPackage.Literals.ROW__TARGET);
	}

	private ColumnHandler addPNLColumn(final String blockId, final String entityLabel, final String entityKey, final EStructuralFeature bookContainmentFeature) {
		final String book = bookContainmentFeature == CommercialPackage.Literals.BASE_LEGAL_ENTITY__SHIPPING_BOOK ? "Shipping" : "Trading";
		final String title = String.format("P&L (%s - %s)", entityLabel, book);

		// HACK: don't the label to the entity column names if the column is for total group P&L
		if (entityKey != null) {
			if (entityColumnNames.contains(title)) {
				return null;
			}
			entityColumnNames.add(title);
		}

		return blockManager.createColumn(blockManager.getBlockByID(blockId), title, new IntegerFormatter() {
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
		}, false, ScheduleReportPackage.Literals.ROW__TARGET);
	}

	/**
	 * Returns the P&L. If entity is null, then the P&L for the given book across all entities. If book is null, assume trading book.
	 * 
	 * @param container
	 * @param entity
	 * @param bookContainmentFeature
	 * @return
	 */
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
		return null;
	}

	public ScheduleDiffUtils getScheduleDiffUtils() {
		return scheduleDiffUtils;
	}

	public IAdaptable getAdaptableReport() {
		return adaptableReport;
	}

	public void setAdaptableReport(IAdaptable adaptableReport) {
		this.adaptableReport = adaptableReport;
	}

}
