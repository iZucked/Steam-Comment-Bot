/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
package com.mmxlabs.lingo.reports.views.portrotation;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.EnumMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

import com.mmxlabs.lingo.reports.views.AbstractReportBuilder;
import com.mmxlabs.lingo.reports.views.formatters.IntegerFormatter;
import com.mmxlabs.lingo.reports.views.formatters.NumberOfDPFormatter;
import com.mmxlabs.lingo.reports.views.formatters.VariableNumberOfDPFormatter;
import com.mmxlabs.models.lng.fleet.BaseFuel;
import com.mmxlabs.models.lng.fleet.FleetModel;
import com.mmxlabs.models.lng.scenario.model.LNGScenarioModel;
import com.mmxlabs.models.lng.scenario.model.util.ScenarioModelUtil;
import com.mmxlabs.models.lng.schedule.CargoAllocation;
import com.mmxlabs.models.lng.schedule.Fuel;
import com.mmxlabs.models.lng.schedule.FuelAmount;
import com.mmxlabs.models.lng.schedule.FuelQuantity;
import com.mmxlabs.models.lng.schedule.FuelUnit;
import com.mmxlabs.models.lng.schedule.FuelUsage;
import com.mmxlabs.models.lng.schedule.OpenSlotAllocation;
import com.mmxlabs.models.ui.tabular.columngeneration.ColumnBlock;
import com.mmxlabs.models.ui.tabular.columngeneration.ColumnBlockManager;
import com.mmxlabs.models.ui.tabular.columngeneration.ColumnHandler;
import com.mmxlabs.models.ui.tabular.columngeneration.ColumnType;
import com.mmxlabs.models.ui.tabular.columngeneration.EmfBlockColumnFactory;

/**
 * Big helper class for any report based on {@link CargoAllocation}s, {@link OpenSlotAllocation}s, or other events. This builds the internal report data model and handles pin/diff comparison hooks.
 * Currently this class also some generic columns used in these reports but these should be broken out into separate classes as part of FogBugz: 51/
 * 
 * @author Simon Goodall
 * 
 */
public class PortRotationBasedReportBuilder extends AbstractReportBuilder {
	public static final String PORT_ROTATION_REPORT_TYPE_ID = "PORT_ROTATION_REPORT_TYPE_ID";

	protected static final String COLUMN_BLOCK_FUELS = "com.mmxlabs.lingo.reports.components.columns.portrotation.fuels";
	private final List<ColumnHandler> baseFuelCols = new ArrayList<>();
	private final List<String> baseFuelNames = new ArrayList<>();
	/**
	 * Mapping between fuel and the quantity unit displayed.
	 */
	private static final Map<Fuel, FuelUnit> fuelQuanityUnits = new EnumMap<>(Fuel.class);
	/**
	 * Mapping between fuel and the unit price unit displayed.
	 */
	private static final Map<Fuel, FuelUnit> fuelUnitPriceUnits = new EnumMap<>(Fuel.class);
	static {
		fuelQuanityUnits.put(Fuel.FBO, FuelUnit.M3);
		fuelQuanityUnits.put(Fuel.NBO, FuelUnit.M3);

		fuelUnitPriceUnits.put(Fuel.FBO, FuelUnit.MMBTU);
		fuelUnitPriceUnits.put(Fuel.NBO, FuelUnit.MMBTU);
	}
	private PortRotationReportView report;

	public PortRotationReportView getReport() {
		return report;
	}

	public void setReport(final PortRotationReportView report) {
		this.report = report;
	}

	public synchronized void refreshDataColumns(Collection<LNGScenarioModel> rootModels) {
		ColumnBlock block = blockManager.getBlockByID(COLUMN_BLOCK_FUELS);

		// Clear existing fuel columns
		for (final ColumnHandler h : baseFuelCols) {
			blockManager.removeColumn(h);
		}
		baseFuelCols.clear();
		baseFuelNames.clear();

		List<ColumnHandler> handlers = new LinkedList<>();

		{

			Stream.of(Fuel.NBO, Fuel.FBO).forEach(fuelName -> {

				ColumnHandler h1 = blockManager.createColumn(block, fuelName.toString()) //
						.withCellRenderer(new VariableNumberOfDPFormatter() {

							public String getFormatString(Object object) {
								if (object instanceof FuelUsage) {
									final FuelUsage mix = (FuelUsage) object;
									for (final FuelQuantity q : mix.getFuels()) {
										if (q.getFuel().equals(fuelName)) {
											final FuelUnit unit = fuelQuanityUnits.get(fuelName);
											if (unit == FuelUnit.MT) {
												return "%.3G";
											}
										}
									}
								}
								return "%.0f";
							}

							@Override
							public Double getDoubleValue(final Object object) {
								if (object instanceof FuelUsage) {
									final FuelUsage mix = (FuelUsage) object;
									for (final FuelQuantity q : mix.getFuels()) {
										if (q.getFuel().equals(fuelName)) {
											final FuelUnit unit = fuelQuanityUnits.get(fuelName);
											for (final FuelAmount fa : q.getAmounts()) {
												if (fa.getUnit() == unit) {
													return fa.getQuantity();
												}
											}
										}
									}

									return null;
								} else {
									return null;
								}
							}
						}) //
						.withTooltip("In " + fuelQuanityUnits.get(fuelName)) //
						.build();
				handlers.add(h1);

				ColumnHandler h2 = blockManager.createColumn(block, fuelName + " Unit Price") //
						.withCellRenderer(new NumberOfDPFormatter(2) {
							@Override
							public Double getDoubleValue(final Object object) {
								if (object instanceof FuelUsage mix) {
									for (final FuelQuantity q : mix.getFuels()) {
										if (q.getFuel() == fuelName) {
											final FuelUnit unit = fuelUnitPriceUnits.get(fuelName);
											for (final FuelAmount fa : q.getAmounts()) {
												if (fa.getUnit() == unit) {
													return fa.getUnitPrice();
												}
											}
										}
									}
								}
								return null;
							}
						}) //
						.withTooltip("Price per " + fuelUnitPriceUnits.get(fuelName)) //
						.build();
				handlers.add(h2);

				ColumnHandler h3 = blockManager.createColumn(block, fuelName + " Cost") //
						.withCellRenderer(new NumberOfDPFormatter(0) {
							@Override
							public Double getDoubleValue(final Object object) {
								if (object instanceof FuelUsage) {
									for (final FuelQuantity q : ((FuelUsage) object).getFuels()) {
										if (q.getFuel().equals(fuelName)) {
											return q.getCost();
										}
									}
									return null;
								} else {
									return null;
								}
							}
						}) //
						.build();
				handlers.add(h3);
			});
		}

		for (final LNGScenarioModel rootObject : rootModels) {
			if (rootObject != null) {
				final FleetModel fleetModel = ScenarioModelUtil.getFleetModel(rootObject);
				for (final BaseFuel e : fleetModel.getBaseFuels()) {
					if (e.getName() != null && !e.getName().isBlank()) {
						handlers.addAll(addFuelColumns(block, e.getName()));
					}
				}
			}
		}

		baseFuelCols.addAll(handlers);

	}

	private synchronized Collection<ColumnHandler> addFuelColumns(final ColumnBlock block, final String fuelName) {

		if (baseFuelNames.contains(fuelName)) {
			return Collections.emptyList();
		}
		baseFuelNames.add(fuelName);

		List<ColumnHandler> cols = new ArrayList<>(3);
		ColumnHandler ch1 = blockManager.createColumn(block, fuelName) //
				.withCellRenderer(new VariableNumberOfDPFormatter() {

					public String getFormatString(Object object) {
						return "%.3G";
					}

					@Override
					public Double getDoubleValue(final Object object) {
						if (object instanceof FuelUsage mix) {
							double total = 0.0;
							for (final FuelQuantity q : mix.getFuels()) {
								BaseFuel baseFuel = q.getBaseFuel();
								if (baseFuel != null && baseFuel.getName().equals(fuelName)) {
									final FuelUnit unit = FuelUnit.MT;
									for (final FuelAmount fa : q.getAmounts()) {
										if (fa.getUnit() == unit) {
											total += fa.getQuantity();
										}
									}
								}
							}

							return total == 0 ? null : total;
						} else {
							return null;
						}
					}
				}) //
				.withTooltip("In MT") //
				.build();

		cols.add(ch1);

		ColumnHandler ch2 = blockManager.createColumn(block, fuelName + " Unit Price") //
				.withCellRenderer(new NumberOfDPFormatter(2) {
					@Override
					public Double getDoubleValue(final Object object) {
						if (object instanceof FuelUsage mix) {
							for (final FuelQuantity q : mix.getFuels()) {
								BaseFuel f = q.getBaseFuel();
								if (f != null && fuelName.equals(f.getName())) {
									final FuelUnit unit = FuelUnit.MT;
									for (final FuelAmount fa : q.getAmounts()) {
										if (fa.getUnit() == unit) {
											return fa.getUnitPrice();
										}
									}
								}
							}
						}
						return null;
					}
				}) //
				.withTooltip("Price per MT") //
				.build();

		cols.add(ch2);
		ColumnHandler ch3 = blockManager.createColumn(block, fuelName + " Cost") //
				.withCellRenderer(new IntegerFormatter() {
					@Override
					public Integer getIntValue(final Object object) {
						if (object instanceof FuelUsage) {
							int total = 0;
							for (final FuelQuantity q : ((FuelUsage) object).getFuels()) {
								BaseFuel baseFuel = q.getBaseFuel();
								if (baseFuel != null && baseFuel.getName().equals(fuelName)) {
									total += q.getCost();
								}
							}
							return total == 0 ? null : total;
						} else {
							return null;
						}
					}
				}) //
				.build();
		cols.add(ch3);
		return cols;
	}

	public EmfBlockColumnFactory getEmptyFuelsColumnBlockFactory() {
		return new EmfBlockColumnFactory() {

			@Override
			public ColumnHandler addColumn(final ColumnBlockManager blockManager) {
				ColumnBlock block = blockManager.getBlockByID(COLUMN_BLOCK_FUELS);
				if (block == null) {
					block = blockManager.createBlock(COLUMN_BLOCK_FUELS, "[Fuels]", ColumnType.NORMAL);
				}
				block.setPlaceholder(true);
				block.setForceGroup(true);

				return null;
			}
		};
	}

}