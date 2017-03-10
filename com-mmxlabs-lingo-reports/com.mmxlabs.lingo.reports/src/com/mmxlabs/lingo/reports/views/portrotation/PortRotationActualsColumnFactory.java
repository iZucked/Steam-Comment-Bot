/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2015
 * All rights reserved.
 */
package com.mmxlabs.lingo.reports.views.portrotation;

import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.List;

import org.eclipse.emf.common.notify.Notifier;
import org.eclipse.emf.ecore.EObject;

import com.mmxlabs.common.Pair;
import com.mmxlabs.lingo.reports.components.ColumnType;
import com.mmxlabs.lingo.reports.extensions.EMFReportColumnManager;
import com.mmxlabs.lingo.reports.views.formatters.AsDateTimeFormatter;
import com.mmxlabs.lingo.reports.views.formatters.CostFormatter;
import com.mmxlabs.lingo.reports.views.formatters.IntegerFormatter;
import com.mmxlabs.lingo.reports.views.formatters.NumberOfDPFormatter;
import com.mmxlabs.lingo.reports.views.portrotation.IPortRotationColumnFactory;
import com.mmxlabs.lingo.reports.views.portrotation.PortRotationBasedReportBuilder;
import com.mmxlabs.lingo.reports.views.portrotation.PortRotationReportView;
import com.mmxlabs.models.lng.actuals.ActualsModel;
import com.mmxlabs.models.lng.actuals.CargoActuals;
import com.mmxlabs.models.lng.actuals.SlotActuals;
import com.mmxlabs.models.lng.cargo.Cargo;
import com.mmxlabs.models.lng.cargo.Slot;
import com.mmxlabs.models.lng.scenario.model.LNGScenarioModel;
import com.mmxlabs.models.lng.schedule.Event;
import com.mmxlabs.models.lng.schedule.SlotAllocation;
import com.mmxlabs.models.lng.schedule.SlotVisit;
import com.mmxlabs.models.ui.tabular.BaseFormatter;
import com.mmxlabs.models.ui.tabular.ICellRenderer;
import com.mmxlabs.scenario.service.ui.ScenarioResult;

public class PortRotationActualsColumnFactory implements IPortRotationColumnFactory {

	@Override
	public void registerColumn(final String columnID, final EMFReportColumnManager manager, final PortRotationBasedReportBuilder builder) {

		abstract class SlotActualFormatterWrapper implements ICellRenderer {

			final ICellRenderer f;

			public SlotActualFormatterWrapper(final ICellRenderer formatter) {
				f = formatter;
			}

			@Override
			public String render(final Object object) {
				if (object instanceof SlotVisit) {
					final SlotActuals slotActuals = findSlotActuals(builder.getReport(), (SlotVisit) object);
					if (slotActuals != null) {
						final Object o = getObject(slotActuals);
						return o == null ? "" : f.render(o);
					}
				}
				return "";
			}

			protected abstract Object getObject(SlotActuals slotActuals);

			@Override
			public Comparable<?> getComparable(final Object object) {
				if (object instanceof SlotVisit) {
					final SlotActuals slotActuals = findSlotActuals(builder.getReport(), (SlotVisit) object);
					if (slotActuals != null) {
						final Object o = getObject(slotActuals);
						return o == null ? "" : f.getComparable(o);
					}
				}
				return null;
			}

			@Override
			public Object getFilterValue(final Object object) {
				if (object instanceof SlotVisit) {
					final SlotActuals slotActuals = findSlotActuals(builder.getReport(), (SlotVisit) object);
					if (slotActuals != null) {
						final Object o = getObject(slotActuals);
						return o == null ? "" : f.getFilterValue(o);
					}
				}
				return null;
			}

			@Override
			public boolean isValueUnset(Object object) {
				return false;
			}

			@Override
			public Iterable<Pair<Notifier, List<Object>>> getExternalNotifiers(final Object object) {
				return null;
			}
		}

		switch (columnID) {
		case "com.mmxlabs.lingo.reports.portrotations.actuals.startdate":
			manager.registerColumn(PortRotationBasedReportBuilder.PORT_ROTATION_REPORT_TYPE_ID, columnID, "Start Date (Actual)", null, ColumnType.NORMAL,
					new SlotActualFormatterWrapper(new AsDateTimeFormatter(DateTimeFormatter.ofLocalizedDateTime(FormatStyle.SHORT), true)) {

						@Override
						protected Object getObject(final SlotActuals slotActuals) {
							return slotActuals.getOperationsStartAsDateTime();
						}
					});
			break;
		case "com.mmxlabs.lingo.reports.portrotations.actuals.enddate":
			manager.registerColumn(PortRotationBasedReportBuilder.PORT_ROTATION_REPORT_TYPE_ID, columnID, "End Date (Actual)", null, ColumnType.NORMAL,
					new SlotActualFormatterWrapper(new AsDateTimeFormatter(DateTimeFormatter.ofLocalizedDateTime(FormatStyle.SHORT), true)) {

						@Override
						protected Object getObject(final SlotActuals slotActuals) {
							return slotActuals.getOperationsEndAsDateTime();
						}
					});
			break;
		case "com.mmxlabs.lingo.reports.portrotations.actuals.basefuel":

			manager.registerColumn(PortRotationBasedReportBuilder.PORT_ROTATION_REPORT_TYPE_ID, columnID, "Base Fuel (Actual)", null, ColumnType.NORMAL,
					new SlotActualFormatterWrapper(new IntegerFormatter()) {

						@Override
						protected Object getObject(final SlotActuals slotActuals) {
							return slotActuals.getBaseFuelConsumption();
						}
					});
			break;
		case "com.mmxlabs.lingo.reports.portrotations.actuals.cv":

			manager.registerColumn(PortRotationBasedReportBuilder.PORT_ROTATION_REPORT_TYPE_ID, columnID, "CV (Actual)", null, ColumnType.NORMAL,
					new SlotActualFormatterWrapper(new NumberOfDPFormatter(1)) {

						@Override
						protected Object getObject(final SlotActuals slotActuals) {
							return slotActuals.getCV();
						}
					});
			break;
		case "com.mmxlabs.lingo.reports.portrotations.actuals.counterparty":

			manager.registerColumn(PortRotationBasedReportBuilder.PORT_ROTATION_REPORT_TYPE_ID, columnID, "Counterparty (Actual)", null, ColumnType.NORMAL,
					new SlotActualFormatterWrapper(new BaseFormatter()) {

						@Override
						protected Object getObject(final SlotActuals slotActuals) {
							return slotActuals.getCounterparty();
						}
					});
			break;
		case "com.mmxlabs.lingo.reports.portrotations.actuals.penalty":

			manager.registerColumn(PortRotationBasedReportBuilder.PORT_ROTATION_REPORT_TYPE_ID, columnID, "Penalty (Actual)", null, ColumnType.NORMAL,
					new SlotActualFormatterWrapper(new BaseFormatter()) {

						@Override
						protected Object getObject(final SlotActuals slotActuals) {
							return slotActuals.getPenalty() != null ? slotActuals.getPenalty().name() : null;
						}
					});
			break;
		case "com.mmxlabs.lingo.reports.portrotations.actuals.pricedol":

			manager.registerColumn(PortRotationBasedReportBuilder.PORT_ROTATION_REPORT_TYPE_ID, columnID, "Price DOL (Actual)", null, ColumnType.NORMAL,
					new SlotActualFormatterWrapper(new NumberOfDPFormatter(2)) {

						@Override
						protected Object getObject(final SlotActuals slotActuals) {
							return slotActuals.getPriceDOL();
						}
					});
			break;
		case "com.mmxlabs.lingo.reports.portrotations.actuals.titletransfer":

			manager.registerColumn(PortRotationBasedReportBuilder.PORT_ROTATION_REPORT_TYPE_ID, columnID, "Title Transfer (Actual)", null, ColumnType.NORMAL,
					new SlotActualFormatterWrapper(new BaseFormatter()) {

						@Override
						protected Object getObject(final SlotActuals slotActuals) {
							return slotActuals.getTitleTransferPoint() != null ? slotActuals.getTitleTransferPoint().getName() : null;
						}
					});
			break;
		case "com.mmxlabs.lingo.reports.portrotations.actuals.transfervolume_m3":

			manager.registerColumn(PortRotationBasedReportBuilder.PORT_ROTATION_REPORT_TYPE_ID, columnID, "Transfer Volume (Actual)", null, ColumnType.NORMAL, new NumberOfDPFormatter(1) {
				@Override
				public Double getDoubleValue(final Object object) {
					if (object instanceof SlotVisit) {
						final SlotVisit sv = (SlotVisit) object;
						final SlotActuals slotActuals = findSlotActuals(builder.getReport(), sv);
						if (slotActuals != null) {
							return (double) slotActuals.getVolumeInM3();
						}
					}
					return null;
				}
			});// .setTooltip("In m³");
			break;
		case "com.mmxlabs.lingo.reports.portrotations.actuals.transfervolume_mmbtu":

			manager.registerColumn(PortRotationBasedReportBuilder.PORT_ROTATION_REPORT_TYPE_ID, columnID, "Volume mmBtu (Actual)", null, ColumnType.NORMAL,
					new SlotActualFormatterWrapper(new NumberOfDPFormatter(1)) {

						@Override
						protected Object getObject(final SlotActuals slotActuals) {
							return slotActuals.getVolumeInMMBtu();
						}
					});

			break;
		case "com.mmxlabs.lingo.reports.portrotations.actuals.portcosts":

			manager.registerColumn(PortRotationBasedReportBuilder.PORT_ROTATION_REPORT_TYPE_ID, columnID, "Port Costs (Actual)", null, ColumnType.NORMAL, new CostFormatter(false) {
				@Override
				public Integer getIntValue(final Object object) {
					if (object instanceof SlotVisit) {
						final SlotVisit sv = (SlotVisit) object;
						final SlotActuals slotActuals = findSlotActuals(builder.getReport(), sv);
						if (slotActuals != null) {
							return slotActuals.getPortCharges();
						}
					}
					return null;
				}
			});
			break;
		}
	}

	private ActualsModel getActualsModel(final PortRotationReportView report, final EObject target) {
		final ScenarioResult scenarioResult = report.getScenarioInstance(target);
		if (scenarioResult != null) {
			LNGScenarioModel lngScenarioModel = scenarioResult.getTypedRoot(LNGScenarioModel.class);
			if (lngScenarioModel != null) {
				return lngScenarioModel.getActualsModel();
			}
		}
		return null;
	}

	private CargoActuals findCargoActuals(final PortRotationReportView report, final Event event, final Cargo cargo) {
		final ActualsModel actualsModel = getActualsModel(report, event);
		if (actualsModel != null) {
			for (final CargoActuals cargoActuals : actualsModel.getCargoActuals()) {
				if (cargoActuals.getCargo() == cargo) {
					return cargoActuals;
				}
			}
		}
		return null;
	}

	private SlotActuals findSlotActuals(final PortRotationReportView report, final SlotVisit slotVisit) {
		final SlotAllocation sa = slotVisit.getSlotAllocation();
		if (sa == null) {
			return null;
		}
		final Slot slot = sa.getSlot();
		if (slot == null) {
			return null;
		}
		final Cargo cargo = slot.getCargo();
		if (cargo == null) {
			return null;
		}
		final CargoActuals cargoActuals = findCargoActuals(report, slotVisit, cargo);
		if (cargoActuals == null) {
			return null;
		}
		for (final SlotActuals slotActuals : cargoActuals.getActuals()) {
			if (slotActuals.getSlot() == slot) {
				return slotActuals;
			}
		}
		return null;
	}
}
