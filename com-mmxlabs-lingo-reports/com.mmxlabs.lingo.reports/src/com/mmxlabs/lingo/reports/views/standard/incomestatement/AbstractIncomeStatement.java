/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
package com.mmxlabs.lingo.reports.views.standard.incomestatement;

import java.time.YearMonth;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.Nullable;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IAction;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Image;
import org.eclipse.ui.PlatformUI;

import com.mmxlabs.common.CumulativeMap;
import com.mmxlabs.common.Pair;
import com.mmxlabs.lingo.reports.IReportContentsGenerator;
import com.mmxlabs.lingo.reports.ReportContentsGenerators;
import com.mmxlabs.lingo.reports.components.AbstractSimpleTabularReportTransformer;
import com.mmxlabs.lingo.reports.components.ColumnManager;
import com.mmxlabs.lingo.reports.components.SimpleTabularReportContentProvider;
import com.mmxlabs.lingo.reports.services.ISelectedDataProvider;
import com.mmxlabs.lingo.reports.views.standard.SimpleTabularReportView;
import com.mmxlabs.models.lng.cargo.DischargeSlot;
import com.mmxlabs.models.lng.cargo.LoadSlot;
import com.mmxlabs.models.lng.cargo.Slot;
import com.mmxlabs.models.lng.schedule.CargoAllocation;
import com.mmxlabs.models.lng.schedule.Event;
import com.mmxlabs.models.lng.schedule.Fuel;
import com.mmxlabs.models.lng.schedule.FuelUsage;
import com.mmxlabs.models.lng.schedule.Schedule;
import com.mmxlabs.models.lng.schedule.SlotAllocation;
import com.mmxlabs.models.lng.schedule.util.ScheduleModelKPIUtils;
import com.mmxlabs.models.lng.schedule.util.ScheduleModelKPIUtils.ShippingCostType;
import com.mmxlabs.scenario.service.ScenarioResult;
import com.mmxlabs.scenario.service.model.manager.ScenarioModelRecord;

public abstract class AbstractIncomeStatement<T> extends SimpleTabularReportView<IncomeStatementData> {

	public final Set<YearMonth> overallIncomeMonths = new HashSet<>();

	protected enum DateAggregationMode {
		DISPLAY_BY_CALENDAR_YEAR, DISPLAY_BY_MONTH
	}

	protected DateAggregationMode dateAggregationMode = DateAggregationMode.DISPLAY_BY_MONTH;

	protected AbstractIncomeStatement(final String helpContextId) {
		super(helpContextId);
	}

	@Override
	protected SimpleTabularReportContentProvider createContentProvider() {
		return new SimpleTabularReportContentProvider() {

			@Override
			public Object getParent(final Object element) {
				if (element instanceof final IncomeStatementData isd) {
					return isd.parent;
				}
				return super.getParent(element);
			}

			@Override
			public Object[] getChildren(final Object parentElement) {
				if (parentElement instanceof final IncomeStatementData isd) {
					return isd.children.toArray();
				}
				return super.getChildren(parentElement);
			}

			@Override
			public boolean hasChildren(final Object element) {
				if (element instanceof final IncomeStatementData isd) {
					return !isd.children.isEmpty();
				}
				return super.hasChildren(element);
			}

		};
	}

	@Override
	protected AbstractSimpleTabularReportTransformer<IncomeStatementData> createTransformer() {
		return new AbstractSimpleTabularReportTransformer<IncomeStatementData>() {

			/**
			 * Returns the list of year / month labels for the entire known exposure data range. This may conceivably include months in which no transactions occur.
			 * 
			 * @return
			 */
			private List<YearMonth> dateRange() {
				final List<YearMonth> result = new ArrayList<>();
				YearMonth earliest = null;
				YearMonth latest = null;

				for (final YearMonth key : overallIncomeMonths) {
					if (earliest == null || earliest.isAfter(key)) {
						earliest = key;
					}
					if (latest == null || latest.isBefore(key)) {
						latest = key;
					}
				}

				if (earliest == null) {
					return result;
				}

				YearMonth my = earliest;

				result.add(my);
				while (my.isBefore(latest)) {
					my = my.plusMonths(1);
					result.add(my);
				}
				return result;
			}

			@Override
			public List<ColumnManager<IncomeStatementData>> getColumnManagers(@NonNull final ISelectedDataProvider selectedDataProvider) {
				final ArrayList<ColumnManager<IncomeStatementData>> result = new ArrayList<>();

				if (selectedDataProvider.getAllScenarioResults().size() > 1) {
					result.add(new ColumnManager<IncomeStatementData>("Scenario") {

						@Override
						public Color getForeground(final IncomeStatementData element) {
							return AbstractIncomeStatement.this.getForeground(element);
						}

						@Override
						public String getColumnText(final IncomeStatementData data) {
							final ScenarioResult scenarioResult = data.scenarioResult;
							if (scenarioResult != null) {
								final ScenarioModelRecord modelRecord = scenarioResult.getModelRecord();
								if (modelRecord != null) {
									return modelRecord.getName();
								}
							}
							return null;
						}

						@Override
						public Image getColumnImage(final IncomeStatementData data) {
							final ScenarioResult scenarioResult = data.scenarioResult;
							if (selectedDataProvider.getPinnedScenarioResult() == scenarioResult) {
								return pinImage;
							}
							return null;
						}

						@Override
						public int compare(final IncomeStatementData o1, final IncomeStatementData o2) {
							final String s1 = getColumnText(o1);
							final String s2 = getColumnText(o2);
							if (s1 == null) {
								return -1;
							}
							if (s2 == null) {
								return 1;
							}
							return s1.compareTo(s2);
						}

						@Override
						public boolean isTree() {
							return false;
						}
					});
				}

				result.add(new ColumnManager<IncomeStatementData>("Line Item") {

					@Override
					public Color getForeground(final IncomeStatementData element) {
						return AbstractIncomeStatement.this.getForeground(element);
					}

					@Override
					public String getColumnText(final IncomeStatementData data) {
						return doGetColumnText(data);
					}

					@Override
					public int compare(final IncomeStatementData o1, final IncomeStatementData o2) {
						return doCompare(o1, o2);
					}

					@Override
					public boolean isTree() {
						return true;
					}
				});

				final List<YearMonth> currentYear = new ArrayList<>(12);
				for (final YearMonth date : dateRange()) {
					currentYear.add(date);
					if (dateAggregationMode == DateAggregationMode.DISPLAY_BY_MONTH) {

						result.add(createMonthColumn(date));

						// Add Year totals -- Month here starts at 1
						if (date.getMonthValue() == Calendar.DECEMBER + 1) {
							result.add(createYearTotalColumn(currentYear));
							currentYear.clear();
						}
					}
					if (dateAggregationMode == DateAggregationMode.DISPLAY_BY_CALENDAR_YEAR) {

						// Add Year totals -- Month here starts at 1
						if (date.getMonthValue() == Calendar.DECEMBER + 1) {
							result.add(createYearTotalColumn(currentYear));
							currentYear.clear();
						}
					}
				}

				// Add in tail end of year
				if (!currentYear.isEmpty()) {

					result.add(createYearTotalColumn(currentYear));

					currentYear.clear();

				}

				return result;
			}

			private ColumnManager<IncomeStatementData> createMonthColumn(final YearMonth date) {
				return new ColumnManager<IncomeStatementData>(String.format("%d-%02d", date.getYear(), date.getMonthValue())) {
					@Override
					public String getColumnText(final IncomeStatementData data) {
						double result = data.valuesByMonth.containsKey(date) ? data.valuesByMonth.get(date) : 0;
						if (data.valuesByMonth2 != null) {
							final double resultB = data.valuesByMonth2.containsKey(date) ? data.valuesByMonth2.get(date) : 0;
							if (resultB != 0.0) {
								result = result / resultB;
							} else {
								result = 0.0;
							}
						}
						return String.format("%,.01f", result);
					}

					@Override
					public int compare(final IncomeStatementData o1, final IncomeStatementData o2) {
						double result1 = o1.valuesByMonth.containsKey(date) ? o1.valuesByMonth.get(date) : 0;
						double result2 = o2.valuesByMonth.containsKey(date) ? o2.valuesByMonth.get(date) : 0;

						if (o1.valuesByMonth2 != null) {
							final double resultB = o1.valuesByMonth2.containsKey(date) ? o1.valuesByMonth2.get(date) : 0;
							if (resultB != 0.0) {
								result1 = result1 / resultB;
							} else {
								result1 = 0.0;
							}
						}

						if (o2.valuesByMonth2 != null) {
							final double resultB = o2.valuesByMonth2.containsKey(date) ? o2.valuesByMonth2.get(date) : 0;
							if (resultB != 0.0) {
								result2 = result2 / resultB;
							} else {
								result2 = 0.0;
							}
						}

						return Double.compare(result1, result2);
					}

					@Override
					public Color getForeground(final IncomeStatementData element) {
						return AbstractIncomeStatement.this.getForeground(element);
					}
				};
			}

			private ColumnManager<IncomeStatementData> createYearTotalColumn(final List<YearMonth> currentYear) {
				return new ColumnManager<IncomeStatementData>(String.format("%d", currentYear.get(0).getYear())) {
					private final List<YearMonth> thisYear = new ArrayList<>(currentYear);

					@Override
					public String getColumnText(final IncomeStatementData data) {

						double result = 0.0;
						for (final YearMonth my : thisYear) {
							result += data.valuesByMonth.containsKey(my) ? data.valuesByMonth.get(my) : 0;
						}
						if (data.valuesByMonth2 != null) {
							double resultB = 0.0;
							for (final YearMonth my : thisYear) {
								resultB += data.valuesByMonth2.containsKey(my) ? data.valuesByMonth2.get(my) : 0;
							}
							if (resultB != 0.0) {
								result = result / resultB;
							} else {
								result = 0.0;
							}
						}
						return String.format("%,.01f", result);
					}

					@Override
					public int compare(final IncomeStatementData o1, final IncomeStatementData o2) {
						double result1 = 0.0;
						double result2 = 0.0;
						for (final YearMonth my : thisYear) {
							result1 += o1.valuesByMonth.containsKey(my) ? o1.valuesByMonth.get(my) : 0;
							result2 += o2.valuesByMonth.containsKey(my) ? o2.valuesByMonth.get(my) : 0;
						}

						if (o1.valuesByMonth2 != null) {
							double resultB = 0.0;
							for (final YearMonth my : thisYear) {

								resultB += o1.valuesByMonth2.containsKey(my) ? o1.valuesByMonth2.get(my) : 0;
							}
							if (resultB != 0.0) {
								result1 = result1 / resultB;
							} else {
								result1 = 0.0;
							}
						}
						if (o2.valuesByMonth2 != null) {
							double resultB = 0.0;
							for (final YearMonth my : thisYear) {
								resultB += o2.valuesByMonth2.containsKey(my) ? o2.valuesByMonth2.get(my) : 0;
							}
							if (resultB != 0.0) {
								result2 = result2 / resultB;
							} else {
								result2 = 0.0;
							}
						}

						return Double.compare(result1, result2);
					}

					@Override
					public Color getForeground(final IncomeStatementData element) {
						return AbstractIncomeStatement.this.getForeground(element);
					}
				};
			}

			@Override
			public List<IncomeStatementData> createData(@Nullable final Pair<@NonNull Schedule, @NonNull ScenarioResult> pinnedPair,
					@NonNull final List<@NonNull Pair<@NonNull Schedule, @NonNull ScenarioResult>> otherPairs) {
				resetData();
				overallIncomeMonths.clear();

				final List<IncomeStatementData> output = new LinkedList<>();
				{
					if (pinnedPair != null) {
						output.addAll(createData(pinnedPair.getFirst(), pinnedPair.getSecond()));
					}
					for (final Pair<@NonNull Schedule, ScenarioResult> p : otherPairs) {
						output.addAll(createData(p.getFirst(), p.getSecond()));
					}
				}

				return output;
			}

			public List<IncomeStatementData> createData(final Schedule schedule, final ScenarioResult scenarioResult) {
				final List<IncomeStatementData> output = new ArrayList<>();

				IncomeStatementData shippingCostItem = null;
				final EnumMap<LineItems, IncomeStatementData> m = new EnumMap<>(LineItems.class);
				for (final LineItems lineItem : LineItems.values()) {
					if (lineItem == LineItems.AvgSalesPrice || lineItem == LineItems.AvgPurchasePrice) {
						// Skip these, derived later
						continue;
					}

					final IncomeStatementData data = getIncomeByMonth(scenarioResult, schedule, lineItem);
					m.put(lineItem, data);
					if (lineItem == LineItems.ShippingCosts) {
						shippingCostItem = data;
						output.add(data);
					} else if (lineItem == LineItems.Hire || lineItem == LineItems.PortCharges || lineItem == LineItems.VariableCosts) {
						assert shippingCostItem != null;
						shippingCostItem.children.add(data);
						data.parent = shippingCostItem;
					} else {
						output.add(data);
					}

					for (final YearMonth key : data.valuesByMonth.keySet()) {
						overallIncomeMonths.add(key);
					}
				}

				// Avg Purchase Price;
				gatherAvgPriceData(schedule, scenarioResult, output, m, LineItems.AvgPurchasePrice, LineItems.PurchaseVolume, LineItems.Costs);
				// Avg Sales Price;
				gatherAvgPriceData(schedule, scenarioResult, output, m, LineItems.AvgSalesPrice, LineItems.SalesVolume, LineItems.Revenues);

				return output;
			}

			private void gatherAvgPriceData(final Schedule schedule, final ScenarioResult scenarioResult, final List<IncomeStatementData> output, final EnumMap<LineItems, IncomeStatementData> m,
					final LineItems avgDataKey, final LineItems volumeData, final LineItems valueData) {
				{

					// Step 1, compute the totals
					final IncomeStatementData volsInMMBTU = m.get(volumeData);
					final IncomeStatementData valuesInDollars = m.get(valueData);

					final Map<YearMonth, Double> valuesByMonth = new HashMap<>();
					final Map<YearMonth, Double> volumesByMonth = new HashMap<>();
					for (final YearMonth key : overallIncomeMonths) {
						final Double volume = volsInMMBTU.valuesByMonth.get(key);
						final Double value = valuesInDollars.valuesByMonth.get(key);

						if (volume != null && value != null) {
							if (volume != 0.0) {
								valuesByMonth.put(key, value);
								volumesByMonth.put(key, volume);
							}
						}
					}

					final IncomeStatementData data = new IncomeStatementData(scenarioResult, schedule, avgDataKey, valuesByMonth, volumesByMonth);
					output.add(data);

					// Step 2, compute the contract sub-totals
					for (final IncomeStatementData dVols : volsInMMBTU.children) {
						for (final IncomeStatementData dValues : valuesInDollars.children) {
							if (dVols.key == dValues.key) {

								final Map<YearMonth, Double> dvaluesByMonth = new HashMap<>();
								final Map<YearMonth, Double> dvolumesByMonth = new HashMap<>();

								for (final YearMonth key : overallIncomeMonths) {

									final Double volume = dVols.valuesByMonth.get(key);
									final Double value = dValues.valuesByMonth.get(key);
									if (volume != null && value != null) {
										if (volume != 0.0) {
											dvaluesByMonth.put(key, value);
											dvolumesByMonth.put(key, volume);
										}
									}
								}
								final IncomeStatementData data2 = new IncomeStatementData(scenarioResult, schedule, dVols.key, dvaluesByMonth, dvolumesByMonth);
								data.children.add(data2);
							}
						}
					}

				}
			}
		};
	}

	protected IncomeStatementData getIncomeByMonth(final ScenarioResult scenarioResult, final Schedule schedule, final LineItems lineItem) {
		final CumulativeMap<YearMonth> result = new CumulativeMap<>();
		final Map<T, CumulativeMap<YearMonth>> subTypeMap = new HashMap<>();

		for (final CargoAllocation cargoAllocation : schedule.getCargoAllocations()) {

			double total = 0.0;
			switch (lineItem) {
			case BOG:
				for (final Event event : cargoAllocation.getEvents()) {
					if (event instanceof final FuelUsage fuelUsage) {
						total += ScheduleModelKPIUtils.getFuelCost(fuelUsage, Fuel.NBO, Fuel.FBO);
					}
				}
				break;
			case Costs:
				for (final SlotAllocation slotAllocation : cargoAllocation.getSlotAllocations()) {
					final Slot<?> slot = slotAllocation.getSlot();
					if (slot instanceof LoadSlot) {
						total += slotAllocation.getVolumeValue();
					}
				}
				break;
			case Ebitda: {
				// Revenue
				for (final SlotAllocation slotAllocation : cargoAllocation.getSlotAllocations()) {
					final Slot<?> slot = slotAllocation.getSlot();
					if (slot instanceof DischargeSlot) {
						total += slotAllocation.getVolumeValue();
					}
				}

				// Minus purchase costs
				for (final SlotAllocation slotAllocation : cargoAllocation.getSlotAllocations()) {
					final Slot<?> slot = slotAllocation.getSlot();
					if (slot instanceof LoadSlot) {
						total -= slotAllocation.getVolumeValue();
					}
				}
				// Minus ship costs
				total -= ScheduleModelKPIUtils.calculateEventShippingCost(cargoAllocation, false, false, ShippingCostType.ALL);
			}
				break;
			case GrossMargin: {

				// IT IS OUT BY A few hundred dollars :( - probably internal rounding
				for (final SlotAllocation slotAllocation : cargoAllocation.getSlotAllocations()) {
					final Slot<?> slot = slotAllocation.getSlot();
					if (slot instanceof LoadSlot) {
						total -= slotAllocation.getVolumeValue();
					}
				}
				for (final SlotAllocation slotAllocation : cargoAllocation.getSlotAllocations()) {
					final Slot<?> slot = slotAllocation.getSlot();
					if (slot instanceof DischargeSlot) {
						total += slotAllocation.getVolumeValue();
					}
				}

				break;
			}
			case Hire:
				total += ScheduleModelKPIUtils.calculateEventShippingCost(cargoAllocation, false, false, ShippingCostType.HIRE_COSTS);
				break;
			case PortCharges:
				total += ScheduleModelKPIUtils.calculateEventShippingCost(cargoAllocation, false, false, ShippingCostType.PORT_COSTS);
				break;
			case Revenues: {
				for (final SlotAllocation slotAllocation : cargoAllocation.getSlotAllocations()) {
					final Slot<?> slot = slotAllocation.getSlot();
					if (slot instanceof DischargeSlot) {
						total += slotAllocation.getVolumeValue();
					}
				}

			}
				break;
			case ShippingCosts:
				total += ScheduleModelKPIUtils.calculateEventShippingCost(cargoAllocation, false, false, ShippingCostType.ALL);
				break;
			case VariableCosts:
				total += getVariableCost(cargoAllocation);
				break;

			case SalesVolume: {
				for (final SlotAllocation slotAllocation : cargoAllocation.getSlotAllocations()) {
					final Slot<?> slot = slotAllocation.getSlot();
					if (slot instanceof DischargeSlot) {
						total += slotAllocation.getEnergyTransferred();
					}
				}

			}
				break;
			case PurchaseVolume: {
				for (final SlotAllocation slotAllocation : cargoAllocation.getSlotAllocations()) {
					final Slot<?> slot = slotAllocation.getSlot();
					if (slot instanceof LoadSlot) {
						total += slotAllocation.getEnergyTransferred();
					}
				}

			}
				break;
			case AvgPurchasePrice:
			case AvgSalesPrice:
				// Computed later
				break;
			}

			// Find discharge date
			ZonedDateTime dischargeDate = null;
			T subType = null;
			for (final SlotAllocation slotAllocation : cargoAllocation.getSlotAllocations()) {
				final Slot<?> slot = slotAllocation.getSlot();
				if (slot instanceof final DischargeSlot dischargeSlot) {
					dischargeDate = slotAllocation.getSlotVisit().getStart();
					subType = getSubType(dischargeSlot);
				}
			}

			if (dischargeDate != null) {
				result.plusEquals(YearMonth.of(dischargeDate.toLocalDateTime().getYear(), dischargeDate.toLocalDateTime().getMonthValue()), total);

				final CumulativeMap<YearMonth> cumulativeAreaMap;
				if (subTypeMap.containsKey(subType)) {
					cumulativeAreaMap = subTypeMap.get(subType);
				} else {
					cumulativeAreaMap = new CumulativeMap<>();
					subTypeMap.put(subType, cumulativeAreaMap);
				}
				cumulativeAreaMap.plusEquals(YearMonth.of(dischargeDate.toLocalDateTime().getYear(), dischargeDate.toLocalDateTime().getMonthValue()), total);
			}
		}

		final IncomeStatementData data = new IncomeStatementData(scenarioResult, schedule, lineItem, result);
		for (final T subType : getSubTypes()) {
			if (subTypeMap.containsKey(subType)) {
				final CumulativeMap<YearMonth> cumulativeAreaMap = subTypeMap.get(subType);
				final IncomeStatementData areaData = new IncomeStatementData(scenarioResult, schedule, subType, cumulativeAreaMap);
				data.children.add(areaData);
				areaData.parent = data;
			}
		}
		return data;

	}

	protected abstract Collection<T> getSubTypes();

	/**
	 */
	private Long getVariableCost(final CargoAllocation cargoAllocation) {

		if (cargoAllocation == null) {
			return null;
		}

		final long shippingCost = ScheduleModelKPIUtils.calculateEventShippingCost(cargoAllocation, false, false, ShippingCostType.ALL) //
				// Take off cost items tallied separately.
				- ScheduleModelKPIUtils.calculateEventShippingCost(cargoAllocation, false, false, ShippingCostType.HIRE_COSTS) //
				- ScheduleModelKPIUtils.calculateEventShippingCost(cargoAllocation, false, false, ShippingCostType.PORT_COSTS) //
		;

		return shippingCost;

	}

	private Color getForeground(final Object element) {
		if (element instanceof final IncomeStatementData incomeStatementData) {
			if (incomeStatementData.key instanceof final LineItems lineItems) {
				if (lineItems == LineItems.BOG) {
					return PlatformUI.getWorkbench().getDisplay().getSystemColor(SWT.COLOR_GRAY);
				}
			} else {
				return getForeground(incomeStatementData.parent);
			}
		}

		return null;
	}

	private void setDateAggregationModeActionText(final Action a, final DateAggregationMode mode) {
		String modeStr = null;
		switch (mode) {
		case DISPLAY_BY_CALENDAR_YEAR:
			modeStr = "Calendar Year";
			break;
		case DISPLAY_BY_MONTH:
			modeStr = "Monthly";
			break;
		default:
			assert false;
			break;

		}
		assert modeStr != null;
		a.setText("Show: " + modeStr);
	}

	protected abstract void resetData();

	protected abstract T getSubType(DischargeSlot dischargeSlot);

	protected abstract int doCompare(final IncomeStatementData o1, final IncomeStatementData o2);

	protected abstract String doGetColumnText(final IncomeStatementData data);

	@Override
	public <U> U getAdapter(final Class<U> adapter) {
		if (IReportContentsGenerator.class.isAssignableFrom(adapter)) {
			return adapter.cast(ReportContentsGenerators.createJSONFor(selectedScenariosServiceListener, viewer.getGrid()));
		}
		return super.getAdapter(adapter);
	}

	@Override
	protected void makeActions() {
		super.makeActions();

		final Action displayModeToggle = new Action("Show:", IAction.AS_PUSH_BUTTON) {

			@Override
			public void run() {

				final int modeIdx = (dateAggregationMode.ordinal() + 1) % DateAggregationMode.values().length;
				dateAggregationMode = DateAggregationMode.values()[modeIdx];
				setDateAggregationModeActionText(this, dateAggregationMode);
				getViewSite().getActionBars().updateActionBars();
				AbstractIncomeStatement.this.refresh();

			}
		};
		setDateAggregationModeActionText(displayModeToggle, dateAggregationMode);

		getViewSite().getActionBars().getToolBarManager().add(displayModeToggle);

		getViewSite().getActionBars().getToolBarManager().update(true);
	}

}