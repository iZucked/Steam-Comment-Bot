/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2020
 * All rights reserved.
 */
package com.mmxlabs.lingo.reports.views.standard.incomestatement;

import java.time.YearMonth;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.eclipse.emf.common.util.EList;
import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.Nullable;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.ui.PlatformUI;

import com.google.common.collect.Sets;
import com.mmxlabs.common.CumulativeMap;
import com.mmxlabs.common.Pair;
import com.mmxlabs.lingo.reports.IReportContents;
import com.mmxlabs.lingo.reports.components.AbstractSimpleTabularReportContentProvider;
import com.mmxlabs.lingo.reports.components.AbstractSimpleTabularReportTransformer;
import com.mmxlabs.lingo.reports.services.ISelectedDataProvider;
import com.mmxlabs.lingo.reports.views.standard.SimpleTabularReportView;
import com.mmxlabs.models.lng.cargo.DischargeSlot;
import com.mmxlabs.models.lng.cargo.LoadSlot;
import com.mmxlabs.models.lng.cargo.Slot;
import com.mmxlabs.models.lng.schedule.CargoAllocation;
import com.mmxlabs.models.lng.schedule.Cooldown;
import com.mmxlabs.models.lng.schedule.Event;
import com.mmxlabs.models.lng.schedule.Fuel;
import com.mmxlabs.models.lng.schedule.FuelQuantity;
import com.mmxlabs.models.lng.schedule.FuelUsage;
import com.mmxlabs.models.lng.schedule.Journey;
import com.mmxlabs.models.lng.schedule.PortVisit;
import com.mmxlabs.models.lng.schedule.Purge;
import com.mmxlabs.models.lng.schedule.Schedule;
import com.mmxlabs.models.lng.schedule.SlotAllocation;
import com.mmxlabs.rcp.common.actions.CopyGridToHtmlStringUtil;
import com.mmxlabs.rcp.common.actions.CopyGridToJSONUtil;
import com.mmxlabs.scenario.service.model.manager.ScenarioModelRecord;
import com.mmxlabs.scenario.service.ui.ScenarioResult;

public abstract class AbstractIncomeStatement<T> extends SimpleTabularReportView<IncomeStatementData> {

	public final Set<YearMonth> overallIncomeMonths = new HashSet<>();

	protected AbstractIncomeStatement(final String helpContextId) {
		super(helpContextId);
	}

	@Override
	protected AbstractSimpleTabularReportContentProvider<IncomeStatementData> createContentProvider() {
		return new AbstractSimpleTabularReportContentProvider<IncomeStatementData>() {
			@Override
			public void inputChanged(final Viewer viewer, final Object oldInput, final Object newInput) {

			}

			@Override
			public Object getParent(final Object element) {
				if (element instanceof IncomeStatementData) {
					return ((IncomeStatementData) element).parent;
				}
				return super.getParent(element);
			}

			@Override
			public Object[] getChildren(final Object parentElement) {
				if (parentElement instanceof IncomeStatementData) {
					return ((IncomeStatementData) parentElement).children.toArray();
				}
				return super.getChildren(parentElement);
			}

			@Override
			public boolean hasChildren(final Object element) {
				if (element instanceof IncomeStatementData) {
					return !((IncomeStatementData) element).children.isEmpty();
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

				if (selectedDataProvider.getScenarioResults().size() > 1) {
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
					result.add(createMonthColumn(date));
					// Add Year totals -- Month here starts at 1
					if (date.getMonthValue() == Calendar.DECEMBER + 1) {
						result.add(createYearTotalColumn(currentYear));
						currentYear.clear();
					}
				}

				// Add in tail end of year
				if (!currentYear.isEmpty()) {

					result.add(createYearTotalColumn(currentYear));

					currentYear.clear();

				}

				return result;
			}

			private com.mmxlabs.lingo.reports.components.AbstractSimpleTabularReportTransformer.ColumnManager<IncomeStatementData> createMonthColumn(final YearMonth date) {
				return new ColumnManager<IncomeStatementData>(String.format("%d-%02d", date.getYear(), date.getMonthValue())) {
					@Override
					public String getColumnText(final IncomeStatementData data) {
						final double result = data.valuesByMonth.containsKey(date) ? data.valuesByMonth.get(date) : 0;
						return String.format("%,.01f", result);
					}

					@Override
					public int compare(final IncomeStatementData o1, final IncomeStatementData o2) {
						final double result1 = o1.valuesByMonth.containsKey(date) ? o1.valuesByMonth.get(date) : 0;
						final double result2 = o2.valuesByMonth.containsKey(date) ? o2.valuesByMonth.get(date) : 0;
						return Double.compare(result1, result2);
					}

					@Override
					public Color getForeground(final IncomeStatementData element) {
						return AbstractIncomeStatement.this.getForeground(element);
					}
				};
			}

			private com.mmxlabs.lingo.reports.components.AbstractSimpleTabularReportTransformer.ColumnManager<IncomeStatementData> createYearTotalColumn(final List<YearMonth> currentYear) {
				return new ColumnManager<IncomeStatementData>(String.format("%d", currentYear.get(0).getYear())) {
					private final List<YearMonth> thisYear = new ArrayList<>(currentYear);

					@Override
					public String getColumnText(final IncomeStatementData data) {
						double result = 0.0;
						for (final YearMonth my : thisYear) {
							result += data.valuesByMonth.containsKey(my) ? data.valuesByMonth.get(my) : 0;
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
						return Double.compare(result1, result2);
					}

					@Override
					public Color getForeground(final IncomeStatementData element) {
						return AbstractIncomeStatement.this.getForeground(element);
					}
				};
			}

			@Override
			public @NonNull List<@NonNull IncomeStatementData> createData(@Nullable final Pair<@NonNull Schedule, @NonNull ScenarioResult> pinnedPair,
					@NonNull final List<@NonNull Pair<@NonNull Schedule, @NonNull ScenarioResult>> otherPairs) {
				resetData();
				overallIncomeMonths.clear();

				final List<@NonNull IncomeStatementData> output = new LinkedList<>();
				{
					if (pinnedPair != null) {
						output.addAll(createData(pinnedPair.getFirst(), pinnedPair.getSecond()));
					}
					for (final Pair<@NonNull Schedule, @NonNull ScenarioResult> p : otherPairs) {
						output.addAll(createData(p.getFirst(), p.getSecond()));
					}
				}

				return output;
			}

			public List<IncomeStatementData> createData(final Schedule schedule, final ScenarioResult scenarioResult) {
				final List<IncomeStatementData> output = new ArrayList<IncomeStatementData>();

				IncomeStatementData shippingCostItem = null;
				for (final LineItems lineItem : LineItems.values()) {
					final IncomeStatementData exposures = getIncomeByMonth(scenarioResult, schedule, lineItem);
					if (lineItem == LineItems.ShippingCosts) {
						shippingCostItem = exposures;
						output.add(exposures);
					} else if (lineItem == LineItems.Hire || lineItem == LineItems.PortCharges || lineItem == LineItems.VariableCosts) {
						assert shippingCostItem != null;
						shippingCostItem.children.add(exposures);
						exposures.parent = shippingCostItem;
					} else {
						output.add(exposures);
					}

					for (final YearMonth key : exposures.valuesByMonth.keySet()) {
						overallIncomeMonths.add(key);
					}
				}

				return output;
			}

		};
	}

	private IncomeStatementData getIncomeByMonth(final ScenarioResult scenarioResult, final Schedule schedule, final LineItems lineItem) {
		final CumulativeMap<YearMonth> result = new CumulativeMap<YearMonth>();
		final Map<T, CumulativeMap<YearMonth>> subTypeMap = new HashMap<>();

		for (final CargoAllocation cargoAllocation : schedule.getCargoAllocations()) {

			double total = 0.0;
			switch (lineItem) {
			case BOG:
				for (final Event event : cargoAllocation.getEvents()) {
					if (event instanceof FuelUsage) {
						final FuelUsage fuelUsage = (FuelUsage) event;
						total += getFuelCost(fuelUsage, Fuel.NBO, Fuel.FBO);
					}
				}
				break;
			case Costs:
				for (final SlotAllocation slotAllocation : cargoAllocation.getSlotAllocations()) {
					final Slot slot = slotAllocation.getSlot();
					if (slot instanceof LoadSlot) {
						total += slotAllocation.getVolumeValue();
					}
				}
				break;
			case Ebitda: {
				for (final SlotAllocation slotAllocation : cargoAllocation.getSlotAllocations()) {
					final Slot slot = slotAllocation.getSlot();
					if (slot instanceof LoadSlot) {
						total -= slotAllocation.getVolumeValue();
					}
				}
				for (final SlotAllocation slotAllocation : cargoAllocation.getSlotAllocations()) {
					final Slot slot = slotAllocation.getSlot();
					if (slot instanceof DischargeSlot) {
						total += slotAllocation.getVolumeValue();
					}
				}
				total -= getVariableCost(cargoAllocation);
				for (final Event event : cargoAllocation.getEvents()) {
					if (event instanceof PortVisit) {
						final PortVisit portVisit = (PortVisit) event;
						total -= portVisit.getPortCost();
					}
				}
				for (final Event event : cargoAllocation.getEvents()) {
					total -= event.getCharterCost();
				}
			}
				break;
			case GrossMargin: {

				// IT IS OUT BY A few hundred dollars :( - probably internal rounding
				for (final SlotAllocation slotAllocation : cargoAllocation.getSlotAllocations()) {
					final Slot slot = slotAllocation.getSlot();
					if (slot instanceof LoadSlot) {
						final LoadSlot loadSlot = (LoadSlot) slot;
						total -= slotAllocation.getVolumeValue();
					}
				}
				for (final SlotAllocation slotAllocation : cargoAllocation.getSlotAllocations()) {
					final Slot slot = slotAllocation.getSlot();
					if (slot instanceof DischargeSlot) {
						total += slotAllocation.getVolumeValue();
					}
				}

				break;
			}
			case Hire:
				for (final Event event : cargoAllocation.getEvents()) {
					total += event.getCharterCost();
				}
				break;
			case PortCharges:
				for (final Event event : cargoAllocation.getEvents()) {
					if (event instanceof PortVisit) {
						final PortVisit portVisit = (PortVisit) event;
						total += portVisit.getPortCost();
					}
				}
				break;
			case Revenues: {
				for (final SlotAllocation slotAllocation : cargoAllocation.getSlotAllocations()) {
					final Slot slot = slotAllocation.getSlot();
					if (slot instanceof DischargeSlot) {
						total += slotAllocation.getVolumeValue();
					}
				}

			}
				break;
			case ShippingCosts:
				total += getVariableCost(cargoAllocation);
				for (final Event event : cargoAllocation.getEvents()) {
					if (event instanceof PortVisit) {
						final PortVisit portVisit = (PortVisit) event;
						total += portVisit.getPortCost();
					}
				}
				for (final Event event : cargoAllocation.getEvents()) {
					total += event.getCharterCost();
				}
				break;
			case VariableCosts:
				total += getVariableCost(cargoAllocation);
				break;
			}

			// Find discharge date
			ZonedDateTime dischargeDate = null;
			T subType = (T) null;
			for (final SlotAllocation slotAllocation : cargoAllocation.getSlotAllocations()) {
				final Slot slot = slotAllocation.getSlot();
				if (slot instanceof DischargeSlot) {
					final DischargeSlot dischargeSlot = (DischargeSlot) slot;
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

	private int getFuelCost(final FuelUsage fuelUser, final Fuel... fuels) {
		final Set<Fuel> fuelsOfInterest = Sets.newHashSet(fuels);
		int sum = 0;
		if (fuelUser != null) {
			final EList<FuelQuantity> fuelQuantities = fuelUser.getFuels();
			for (final FuelQuantity fq : fuelQuantities) {
				if (fuelsOfInterest.contains(fq.getFuel())) {
					sum += fq.getCost();
				}
			}
		}
		return sum;
	}

	/**
	 */
	private Integer getVariableCost(final CargoAllocation cargoAllocation) {

		if (cargoAllocation == null) {
			return null;
		}

		// Bit of a double count here, but need to decide what to add to the model
		int shippingCost = 0;
		for (final Event event : cargoAllocation.getEvents()) {

			if (event instanceof Journey) {
				final Journey journey = (Journey) event;
				// Canal Costs
				shippingCost += journey.getToll();
			}

			if (event instanceof FuelUsage) {
				final FuelUsage fuelUsage = (FuelUsage) event;
				// Base fuel costs
				shippingCost += getFuelCost(fuelUsage, Fuel.BASE_FUEL, Fuel.PILOT_LIGHT);
			}

			if (event instanceof Purge) {
				final Purge purge = (Purge) event;
				shippingCost += purge.getCost();
			}
			if (event instanceof Cooldown) {
				final Cooldown cooldown = (Cooldown) event;
				shippingCost += cooldown.getCost();
			}
		}

		return shippingCost;

	}

	private Color getForeground(final Object element) {
		if (element instanceof IncomeStatementData) {
			final IncomeStatementData incomeStatementData = (IncomeStatementData) element;
			if (incomeStatementData.key instanceof LineItems) {
				final LineItems lineItems = (LineItems) incomeStatementData.key;
				if (lineItems == LineItems.BOG) {
					return PlatformUI.getWorkbench().getDisplay().getSystemColor(SWT.COLOR_GRAY);
				}
			} else {
				return getForeground(incomeStatementData.parent);
			}
		}

		return null;
	}

	protected abstract void resetData();

	protected abstract T getSubType(DischargeSlot dischargeSlot);

	protected abstract int doCompare(final IncomeStatementData o1, final IncomeStatementData o2);

	protected abstract String doGetColumnText(final IncomeStatementData data);

	@Override
	public <T> T getAdapter(final Class<T> adapter) {

		if (IReportContents.class.isAssignableFrom(adapter)) {

			final CopyGridToJSONUtil jsonUtil = new CopyGridToJSONUtil(viewer.getGrid(), true);
			final String jsonContents = jsonUtil.convert();
			return (T) new IReportContents() {

				@Override
				public String getJSONContents() {
					return jsonContents;
				}
			};

		}
		return super.getAdapter(adapter);
	}

}