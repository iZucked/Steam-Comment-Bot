/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2018
 * All rights reserved.
 */
package com.mmxlabs.lingo.reports.views.standard.pnlcalcs;

import java.text.DecimalFormat;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.ToIntFunction;

import org.eclipse.emf.common.util.EList;
import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.Nullable;
import org.eclipse.jface.viewers.IColorProvider;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.widgets.Display;

import com.google.common.collect.Sets;
import com.mmxlabs.common.util.TriFunction;
import com.mmxlabs.lingo.reports.views.standard.econs.CargoAllocationPair;
import com.mmxlabs.lingo.reports.views.standard.econs.DeltaPair;
import com.mmxlabs.lingo.reports.views.standard.econs.VesselEventVisitPair;
import com.mmxlabs.models.lng.port.RouteOption;
import com.mmxlabs.models.lng.schedule.BasicSlotPNLDetails;
import com.mmxlabs.models.lng.schedule.CargoAllocation;
import com.mmxlabs.models.lng.schedule.Cooldown;
import com.mmxlabs.models.lng.schedule.EntityProfitAndLoss;
import com.mmxlabs.models.lng.schedule.Event;
import com.mmxlabs.models.lng.schedule.Fuel;
import com.mmxlabs.models.lng.schedule.FuelAmount;
import com.mmxlabs.models.lng.schedule.FuelQuantity;
import com.mmxlabs.models.lng.schedule.FuelUnit;
import com.mmxlabs.models.lng.schedule.FuelUsage;
import com.mmxlabs.models.lng.schedule.GeneralPNLDetails;
import com.mmxlabs.models.lng.schedule.GroupProfitAndLoss;
import com.mmxlabs.models.lng.schedule.Idle;
import com.mmxlabs.models.lng.schedule.Journey;
import com.mmxlabs.models.lng.schedule.PortVisit;
import com.mmxlabs.models.lng.schedule.ProfitAndLossContainer;
import com.mmxlabs.models.lng.schedule.Sequence;
import com.mmxlabs.models.lng.schedule.SlotAllocation;
import com.mmxlabs.models.lng.schedule.SlotAllocationType;
import com.mmxlabs.models.lng.schedule.SlotPNLDetails;
import com.mmxlabs.models.lng.schedule.SlotVisit;
import com.mmxlabs.models.lng.schedule.VesselEventVisit;
import com.mmxlabs.models.ui.tabular.BaseFormatter;
import com.mmxlabs.models.ui.tabular.ICellRenderer;

public class StandardPNLCalcRowFactory implements IPNLCalcsRowFactory {

	public static final DecimalFormat DollarsFormat = new DecimalFormat("##,###,###,###");
	public static final DecimalFormat VolumeM3Format = new DecimalFormat("##,###,###,###");
	public static final DecimalFormat VolumeMMBtuFormat = new DecimalFormat("##,###,###,###");
	public static final DecimalFormat DollarsPerMMBtuFormat = new DecimalFormat("###.###");
	public static final DecimalFormat DaysFormat = new DecimalFormat("##.#");
	public static final DecimalFormat SpeedFormat = new DecimalFormat("##.#");

	@Override
	public Collection<PNLCalcsReportRow> createRows(@Nullable final Collection<Object> targets) {

		boolean containsCargo = false;
		boolean containsCooldown = false;
		if (targets == null || targets.isEmpty()) {
			containsCargo = true;
		} else {
			for (final Object target : targets) {
				if (target instanceof CargoAllocation) {
					final CargoAllocation cargoAllocation = (CargoAllocation) target;
					containsCargo = true;
					for (final Event evt : cargoAllocation.getEvents()) {
						if (evt instanceof Cooldown) {
							containsCooldown = true;
							break;
						}
					}
				}
			}
		}

		final IColorProvider greyColourProvider = new IColorProvider() {

			@Override
			public Color getForeground(final Object element) {

				return Display.getDefault().getSystemColor(SWT.COLOR_GRAY);

			}

			@Override
			public Color getBackground(final Object element) {
				return null;
			}
		};

		final List<PNLCalcsReportRow> rows = new LinkedList<>();

		rows.add(createRow(10, "Buy port", false, "", "", false, createFirstPurchaseFormatter(sa -> sa.getPort().getName())));
		rows.add(createRow(20, "Buy date", false, "", "", false, createFirstPurchaseFormatter(sa -> sa.getSlotVisit().getStart().format(DateTimeFormatter.ofLocalizedDate(FormatStyle.SHORT)))));
		rows.add(createRow(30, "Buy volume (m3)", false, "", "", false, createFirstPurchaseFormatter(Integer.class, VolumeM3Format::format, sa -> (sa.getVolumeTransferred()))));
		rows.add(createRow(40, "Buy volume (mmBtu)", false, "", "", false, createFirstPurchaseFormatter(Integer.class, VolumeMMBtuFormat::format, sa -> (sa.getEnergyTransferred()))));
		rows.add(createRow(50, "Buy price($/mmBtu)", false, "", "", false, createFirstPurchaseFormatter(Double.class, DollarsPerMMBtuFormat::format, sa -> (sa.getPrice()))));

		rows.add(createRow(60, "Sale port", false, "", "", false, createFirstSaleAllocationFormatter(sa -> sa.getPort().getName())));
		rows.add(createRow(70, "Sale date", false, "", "", false, createFirstSaleAllocationFormatter(sa -> sa.getSlotVisit().getStart().format(DateTimeFormatter.ofLocalizedDate(FormatStyle.SHORT)))));
		rows.add(createRow(80, "Sale price($/mmBtu)", false, "", "", false, createFirstSaleAllocationFormatter(Double.class, DollarsPerMMBtuFormat::format, sa -> (sa.getPrice()))));
		rows.add(createRow(90, "Sale volume (m3)", false, "", "", false, createFirstSaleAllocationFormatter(Integer.class, VolumeM3Format::format, sa -> (sa.getVolumeTransferred()))));
		rows.add(createRow(100, "Sale volume (mmBtu)", false, "", "", false, createFirstSaleAllocationFormatter(Integer.class, VolumeMMBtuFormat::format, sa -> (sa.getEnergyTransferred()))));

		// Spacer
		rows.add(createRow(110, "", false, "", "", false, createEmptyFormatter()));

		rows.add(createRow(120, "Purchase cost", false, "", "", true, createFirstPurchaseFormatter(Integer.class, DollarsFormat::format, sa -> (sa.getVolumeValue()))));
		rows.add(createRow(130, "Sales revenue", false, "", "", false, createFirstSaleAllocationFormatter(Integer.class, DollarsFormat::format, sa -> (sa.getVolumeValue()))));
		// Equity
		rows.add(createRow(140, "Equity", false, "", "", false, createEmptyFormatter()));
		rows.add(createRow(150, "Theoretical shipping cost", false, "", "", false, createEmptyFormatter()));

		// Theoretical shipping cost
		// Real Shipping cost

		// getShippingCost((fu) -> getFuelcost(fu, Fuel.BASE_FUEL, Fuel.PILOT_LIGHT));
		// rows.add(createRow(200, "Real shipping cost", false, "$", "", true, createShippingCosts((fu) -> getShippingCost(StandardPNLCalcRowFactory::getShippingCost(fuelCostFunc) )));

		Function<FuelUsage, Integer> fuelCostFunc = (fu) -> getFuelCost(fu, Fuel.BASE_FUEL, Fuel.PILOT_LIGHT);
		Function<SlotVisit, Integer> portCostFunc = (sv) -> sv.getPortCost();
		Function<Object, Integer> func2 = (object) -> getShippingCost(object, portCostFunc, fuelCostFunc);

		rows.add(createRow(200, "Real shipping cost", false, "$", "", true, createShippingCosts(Integer.class, DollarsFormat::format, func2)));

		// Spacer
		rows.add(createRow(210, "", false, "", "", false, createEmptyFormatter()));
		rows.add(createRow(220, "", false, "", "", false, createEmptyFormatter()));

		rows.add(createRow(300, "Real shipping", false, "", "", false, createEmptyFormatter()));

		for (int legIdx = 0; legIdx < 2; ++legIdx) {
			final int base = 1000 + 1000 * legIdx;

			if (legIdx == 0) {
				rows.add(createRow(base + 10, "Laden leg", false, "", "", false, createEmptyFormatter()));
			} else {
				rows.add(createRow(base + 9, "", false, "", "", false, createEmptyFormatter()));

				rows.add(createRow(base + 10, "Ballast leg", false, "", "", false, createEmptyFormatter()));
			}

			rows.add(createRow(base + 20, "    Speed", false, "", "", false, createFullLegFormatter2(legIdx, Double.class, SpeedFormat::format, (visit, travel, idle) -> travel.getSpeed())));
			rows.add(createRow(base + 30, "    Days", false, "", "", false, createFullLegFormatter2(legIdx, Double.class, DaysFormat::format,
					(visit, travel, idle) -> ((getOrZero(visit, Event::getDuration) + getOrZero(travel, Event::getDuration) + getOrZero(idle, Event::getDuration)) / 24.0))));

			// rows.add(createRow(base + 90, " Route", false, "", "", false, createFullLegFormatter(legIdx, (travel, idle) -> getRoute(travel.getRouteOption()))));
			// rows.add(createRow(base + 91, " Port duration", false, "", "", false, createFullLegFormatter(legIdx, (travel, idle) -> getPortDuration(travel))));
			// rows.add(createRow(base + 31, " Port days", false, "", "", false, createFullLegFormatter2(legIdx, Double.class, DaysFormat::format,
			// (visit, travel, idle) -> ((getOrZero(visit, Event::getDuration)) / 24.0))));

			rows.add(createRow(base + 40, "    Total BO (mmBtu)", false, "", "", false,
					createFullLegFormatter2(legIdx, Integer.class, VolumeMMBtuFormat::format, (visit, travel, idle) -> (getFuelVolume(visit, travel, idle, FuelUnit.MMBTU, Fuel.NBO, Fuel.FBO)))));
			rows.add(createRow(base + 50, "    Charter Cost", true, "$", "", true, createFullLegFormatter2(legIdx, Integer.class, DollarsFormat::format,
					(visit, travel, idle) -> (getOrZero(visit, Event::getCharterCost) + getOrZero(travel, Event::getCharterCost) + getOrZero(idle, Event::getCharterCost)))));
			rows.add(createRow(base + 60, "    Bunkers (MT)", false, "", "", false, createFullLegFormatter2(legIdx, Integer.class, VolumeM3Format::format,
					(visit, travel, idle) -> (getFuelVolume(visit, travel, idle, FuelUnit.MT, Fuel.BASE_FUEL, Fuel.PILOT_LIGHT)))));
			rows.add(createRow(base + 70, "    Bunkers cost", false, "", "", false,
					createFullLegFormatter2(legIdx, Integer.class, DollarsFormat::format, (visit, travel, idle) -> (getFuelCost(visit, travel, idle, Fuel.BASE_FUEL, Fuel.PILOT_LIGHT)))));
			rows.add(createRow(base + 80, "    Port Costs ", false, "", "", false,
					createFullLegFormatter2(legIdx, Integer.class, DollarsFormat::format, (visit, travel, idle) -> (visit.getPortCost()))));
			rows.add(createRow(base + 90, "    Route", false, "", "", false, createFullLegFormatter(legIdx, (travel, idle) -> getRoute(travel.getRouteOption()))));
			rows.add(createRow(base + 100, "    Canal Cost", true, "$", "", true,
					createFullLegFormatter2(legIdx, Integer.class, DollarsFormat::format, (visit, travel, idle) -> (getOrZero(travel, Journey::getToll)))));

			if (legIdx != 0) {
				rows.add(createRow(base + 109, "    Cooldown cost ($)", true, "$", "", true, createEmptyFormatter()));
			}

			rows.add(createRow(base + 110, "    Total cost ($)", true, "$", "", true, createFullLegFormatter2(legIdx, Long.class, DollarsFormat::format,
					(visit, travel, idle) -> (getEventShippingCost(visit) + getEventShippingCost(travel) + getEventShippingCost(idle)))));
			rows.add(createRow(base + 120, "    Idle days", false, "", "", false,
					createFullLegFormatter2(legIdx, Double.class, DaysFormat::format, (visit, travel, idle) -> (getOrZero(idle, Event::getDuration) / 24.0)), greyColourProvider));
			rows.add(createRow(base + 130, "    Idle BO", false, "", "", false,
					createFullLegFormatter2(legIdx, Integer.class, VolumeMMBtuFormat::format, (visit, travel, idle) -> (getFuelVolume(idle, FuelUnit.MMBTU, Fuel.NBO, Fuel.FBO))), greyColourProvider));
			rows.add(createRow(base + 140, "    Idle charter", true, "$", "", true,
					createFullLegFormatter2(legIdx, Integer.class, DollarsFormat::format, (visit, travel, idle) -> (getOrZero(idle, Event::getCharterCost))), greyColourProvider));
			rows.add(createRow(base + 150, "    Idle bunkers", false, "", "", false,
					createFullLegFormatter2(legIdx, Integer.class, VolumeM3Format::format, (visit, travel, idle) -> (getFuelVolume(idle, FuelUnit.MT, Fuel.BASE_FUEL, Fuel.PILOT_LIGHT))),
					greyColourProvider));

			rows.add(createRow(base + 160, "    Idle bunkers cost", false, "", "", false,
					createFullLegFormatter2(legIdx, Integer.class, DollarsFormat::format, (visit, travel, idle) -> (getFuelCost(idle, Fuel.BASE_FUEL, Fuel.PILOT_LIGHT))), greyColourProvider));

			rows.add(createRow(base + 170, "    Total idle cost ($)", false, "", "", true,
					createFullLegFormatter2(legIdx, Long.class, DollarsFormat::format, (visit, travel, idle) -> (getEventShippingCost(idle))), greyColourProvider));
		}

		return rows;
	}

	public static PNLCalcsReportRow createRow(final int order, final @NonNull String name, final boolean includeUnits, final @NonNull String prefixUnit, final String suffixUnit, final boolean isCost,
			final @NonNull ICellRenderer renderer) {
		return createRow(order, name, true, prefixUnit, suffixUnit, isCost, renderer, null);
	}

	public static PNLCalcsReportRow createRow(final int order, final @NonNull String name, final boolean includeUnits, final @NonNull String prefixUnit, final String suffixUnit, final boolean isCost,
			final @NonNull ICellRenderer formatter, @Nullable final IColorProvider colourProvider) {
		final PNLCalcsReportRow row = new PNLCalcsReportRow();
		row.order = order;
		row.name = name;
		row.includeUnits = includeUnits;
		row.prefixUnit = prefixUnit;
		row.suffixUnit = suffixUnit;
		row.formatter = formatter;
		row.colourProvider = colourProvider;
		row.isCost = isCost;

		return row;
	}

	public <T, U> T getFromCargoAllocationPair(final Class<T> type, final Function<U, T> f, final Object object) {
		Object first = null;
		Object second = null;

		if (object instanceof DeltaPair) {
			final DeltaPair deltaPair = (DeltaPair) object;
			first = deltaPair.first();
			second = deltaPair.second();
		}

		final T valueFirst = f.apply((U) first);

		if (second != null) {
			final T valueSecond = f.apply((U) second);
			if (valueSecond != null) {
				if (valueFirst instanceof Integer) {
					return type.cast(((int) valueSecond - (int) valueFirst));
				} else if (valueFirst instanceof Double) {
					return type.cast(((double) valueSecond - (double) valueFirst));
				} else if (valueFirst instanceof Long) {
					return type.cast(((long) valueSecond - (long) valueFirst));
				}
			}
		}

		return valueFirst;
	}

	public @NonNull ICellRenderer createEmptyFormatter() {
		return new BaseFormatter() {
			@Override
			public @Nullable String render(final Object object) {
				return null;
			}
		};
	}

	public <T> @NonNull ICellRenderer createFirstPurchaseFormatter(final Class<T> type, final Function<T, String> formatter, final Function<SlotAllocation, T> func) {
		return new BaseFormatter() {
			@Override
			public @Nullable String render(final Object object) {
				if (object instanceof DeltaPair) {
					final DeltaPair deltaPair = (DeltaPair) object;
					final Object first = deltaPair.first();
					final Object second = deltaPair.second();
					final T valueFirst = get(first);
					T t = valueFirst;
					if (second != null) {
						final T valueSecond = get(second);
						if (valueSecond != null) {
							if (valueFirst instanceof Integer) {
								t = type.cast(((int) valueSecond - (int) valueFirst));
							} else if (valueFirst instanceof Double) {
								t = type.cast(((double) valueSecond - (double) valueFirst));
							} else if (valueFirst instanceof Long) {
								t = type.cast(((long) valueSecond - (long) valueFirst));
							}
						}
					}
					return formatter.apply(t);
				} else {
					final T t = get(object);
					if (t != null) {
						return formatter.apply(t);
					}
					return "";
				}
			}

			public @Nullable T get(final Object object) {
				try {
					if (object instanceof CargoAllocation) {
						final CargoAllocation cargoAllocation = (CargoAllocation) object;
						for (final SlotAllocation slotAllocation : cargoAllocation.getSlotAllocations()) {
							if (slotAllocation.getSlotAllocationType() == SlotAllocationType.PURCHASE) {
								return func.apply(slotAllocation);
							}
						}
					}
					if (object instanceof SlotAllocation) {
						final SlotAllocation slotAllocation = (SlotAllocation) object;

						if (slotAllocation.getSlotAllocationType() == SlotAllocationType.PURCHASE) {
							return func.apply(slotAllocation);
						}
					}
				} catch (final NullPointerException e) {
					// Ignore NPE
				}
				return null;
			}
		};
	}

	public @NonNull ICellRenderer createFirstPurchaseFormatter(final Function<SlotAllocation, String> func) {
		return new BaseFormatter() {
			@Override
			public @Nullable String render(final Object object) {

				try {
					if (object instanceof CargoAllocation) {
						final CargoAllocation cargoAllocation = (CargoAllocation) object;
						for (final SlotAllocation slotAllocation : cargoAllocation.getSlotAllocations()) {
							if (slotAllocation.getSlotAllocationType() == SlotAllocationType.PURCHASE) {
								return func.apply(slotAllocation);
							}
						}
					}
					if (object instanceof SlotAllocation) {
						final SlotAllocation slotAllocation = (SlotAllocation) object;

						if (slotAllocation.getSlotAllocationType() == SlotAllocationType.PURCHASE) {
							return func.apply(slotAllocation);
						}
					}
				} catch (final NullPointerException e) {
					// Ignore NPE
				}
				return null;
			}
		};
	}

	public <T> @NonNull ICellRenderer createFirstSaleAllocationFormatter(final Class<T> type, final Function<T, String> formatter, final Function<SlotAllocation, T> func) {

		return new BaseFormatter() {
			@Override
			public @Nullable String render(final Object object) {
				if (object instanceof DeltaPair) {
					final DeltaPair deltaPair = (DeltaPair) object;
					final Object first = deltaPair.first();
					final Object second = deltaPair.second();
					final T valueFirst = get(first);
					T t = valueFirst;
					if (second != null) {
						final T valueSecond = get(second);
						if (valueSecond != null) {
							if (valueFirst instanceof Integer) {
								t = type.cast(((int) valueSecond - (int) valueFirst));
							} else if (valueFirst instanceof Double) {
								t = type.cast(((double) valueSecond - (double) valueFirst));
							} else if (valueFirst instanceof Long) {
								t = type.cast(((long) valueSecond - (long) valueFirst));
							}
						}
					}
					return formatter.apply(t);
				} else {
					final T t = get(object);
					if (t != null) {
						return formatter.apply(t);
					}
					return "";
				}
			}

			public @Nullable T get(final Object object) {
				try {
					if (object instanceof CargoAllocation) {
						final CargoAllocation cargoAllocation = (CargoAllocation) object;
						for (final SlotAllocation slotAllocation : cargoAllocation.getSlotAllocations()) {
							if (slotAllocation.getSlotAllocationType() == SlotAllocationType.SALE) {
								return func.apply(slotAllocation);
							}
						}
					}
					if (object instanceof SlotAllocation) {
						final SlotAllocation slotAllocation = (SlotAllocation) object;
						if (slotAllocation.getSlotAllocationType() == SlotAllocationType.SALE) {
							return func.apply(slotAllocation);
						}
					}
				} catch (final NullPointerException e) {
					// Ignore NPE
				}
				return null;
			}
		};
	}

	public @NonNull ICellRenderer createFirstSaleAllocationFormatter(final Function<SlotAllocation, String> func) {

		return new BaseFormatter() {
			@Override
			public @Nullable String render(final Object object) {

				try {
					if (object instanceof CargoAllocation) {
						final CargoAllocation cargoAllocation = (CargoAllocation) object;
						for (final SlotAllocation slotAllocation : cargoAllocation.getSlotAllocations()) {
							if (slotAllocation.getSlotAllocationType() == SlotAllocationType.SALE) {
								return func.apply(slotAllocation);
							}
						}
					}
					if (object instanceof SlotAllocation) {
						final SlotAllocation slotAllocation = (SlotAllocation) object;
						if (slotAllocation.getSlotAllocationType() == SlotAllocationType.SALE) {
							return func.apply(slotAllocation);
						}
					}
				} catch (final NullPointerException e) {
					// Ignore NPE
				}
				return null;
			}
		};
	}

	// private @NonNull ICellRenderer createFullLegFormatter(final int index, final TriFunction<SlotVisit, Journey, Idle, String> func) {
	// return new BaseFormatter() {
	// @Override
	// public @Nullable String render(final Object object) {
	// try {
	// if (object instanceof CargoAllocation) {
	// final CargoAllocation cargoAllocation = (CargoAllocation) object;
	// SlotVisit slotVisit = null;
	// Journey journey = null;
	// Idle idle = null;
	// int legNumber = -1;
	// for (final Event event : cargoAllocation.getEvents()) {
	// if (event instanceof PortVisit) {
	// if (legNumber == index) {
	// return func.apply(slotVisit, journey, idle);
	// }
	// journey = null;
	// idle = null;
	// slotVisit = null;
	// ++legNumber;
	// if (legNumber > index) {
	// return null;
	// }
	// }
	// if (event instanceof SlotVisit) {
	// slotVisit = (SlotVisit) event;
	// }
	// if (event instanceof Journey) {
	// journey = (Journey) event;
	// }
	// if (event instanceof Idle) {
	// idle = (Idle) event;
	// }
	// }
	// if (legNumber == index) {
	// return func.apply(slotVisit, journey, idle);
	// }
	//
	// }
	// } catch (final NullPointerException e) {
	// // ignore npe's
	// }
	// return null;
	// }
	// };
	// }

	public <T> @NonNull ICellRenderer createFullLegFormatter2(final int index, final Class<T> type, final Function<T, String> formatter, final TriFunction<SlotVisit, Journey, Idle, T> func) {
		return new BaseFormatter() {
			@Override
			public @Nullable String render(final Object object) {

				if (object instanceof DeltaPair) {
					final DeltaPair deltaPair = (DeltaPair) object;
					final Object first = deltaPair.first();
					final Object second = deltaPair.second();
					final T valueFirst = get(first);
					T t = valueFirst;
					if (second != null) {
						final T valueSecond = get(second);
						if (valueSecond != null) {
							if (valueFirst instanceof Integer) {
								t = type.cast(((int) valueSecond - (int) valueFirst));
							} else if (valueFirst instanceof Double) {
								t = type.cast(((double) valueSecond - (double) valueFirst));
							} else if (valueFirst instanceof Long) {
								t = type.cast(((long) valueSecond - (long) valueFirst));
							}
						}
					}
					if (t != null) {
						return formatter.apply(t);
					}
				} else {
					final T t = get(object);
					if (t != null) {
						return formatter.apply(t);
					}
				}
				return "";
			}

			public @Nullable T get(final Object object) {
				try {
					if (object instanceof CargoAllocation) {
						final CargoAllocation cargoAllocation = (CargoAllocation) object;
						SlotVisit slotVisit = null;
						Journey journey = null;
						Idle idle = null;
						int legNumber = -1;
						for (final Event event : cargoAllocation.getEvents()) {
							if (event instanceof PortVisit) {
								if (legNumber == index) {
									return func.apply(slotVisit, journey, idle);
								}
								journey = null;
								idle = null;
								slotVisit = null;
								++legNumber;
								if (legNumber > index) {
									return null;
								}
							}
							if (event instanceof SlotVisit) {
								slotVisit = (SlotVisit) event;
							}
							if (event instanceof Journey) {
								journey = (Journey) event;
							}
							if (event instanceof Idle) {
								idle = (Idle) event;
							}
						}
						if (legNumber == index) {
							return func.apply(slotVisit, journey, idle);
						}

					}
				} catch (final NullPointerException e) {
					// ignore npe's
				}
				return null;
			}
		};
	}

	private @NonNull ICellRenderer createFullLegFormatter(final int index, final BiFunction<Journey, Idle, String> func) {
		return new BaseFormatter() {
			@Override
			public @Nullable String render(final Object object) {

				try {
					if (object instanceof CargoAllocation) {
						final CargoAllocation cargoAllocation = (CargoAllocation) object;
						Journey journey = null;
						Idle idle = null;
						int legNumber = -1;
						for (final Event event : cargoAllocation.getEvents()) {
							if (event instanceof PortVisit) {
								if (legNumber == index) {
									return func.apply(journey, idle);
								}
								journey = null;
								idle = null;
								++legNumber;
								if (legNumber > index) {
									return null;
								}
							}
							if (event instanceof Journey) {
								journey = (Journey) event;
							}
							if (event instanceof Idle) {
								idle = (Idle) event;
							}

						}
						if (legNumber == index) {
							return func.apply(journey, idle);
						}

					}
				} catch (final NullPointerException e) {
					// ignore npe's
				}
				return null;
			}
		};
	}

	public <T> @NonNull ICellRenderer createShippingCosts(final Class<T> type, final Function<T, String> formatter, final Function<Object, T> func) {
		return new BaseFormatter() {
			@Override
			public @Nullable String render(final Object object) {
				if (object instanceof CargoAllocation) {
					final CargoAllocation cargoAllocation = (CargoAllocation) object;

					final T cost = func.apply(object);
					if (cost != null) {
						// return DollarsFormat.format(cost);

						// final T t = get(object);
						// if (t != null) {
						return formatter.apply(cost);
						// }
						// return "";

					}
				}
				if (object instanceof VesselEventVisit) {
					final VesselEventVisit cargoAllocation = (VesselEventVisit) object;

					final T cost = func.apply(object);
					return formatter.apply(cost);
					// if (cost != null) {
					// return DollarsFormat.format(cost);
					// }
				} else if (object instanceof VesselEventVisitPair) {
					T cost = getFromCargoAllocationPair(type, func, object);
					return DollarsFormat.format(cost);
				} else if (object instanceof CargoAllocationPair) {
					final T cost = getFromCargoAllocationPair(type, func, object);
					return DollarsFormat.format(cost);
				}

				return null;

			}
		};
	}

	//
	private static Integer getShippingCost(final Object object, Function<SlotVisit, Integer> slotVisitFunc, Function<FuelUsage, Integer> fuelUsageFunc) {

		if (object == null) {
			return null;
		}

		Sequence sequence = null;
		List<Event> events = null;

		if (object instanceof CargoAllocation) {
			sequence = ((CargoAllocation) object).getSequence();
			events = ((CargoAllocation) object).getEvents();
		} else if (object instanceof VesselEventVisit) {
			sequence = ((VesselEventVisit) object).getSequence();
			events = ((VesselEventVisit) object).getEvents();
		}

		if (sequence == null || events == null) {
			return null;
		}
		// Bit of a double count here, but need to decide what to add to the model
		int shippingCost = 0;
		int charterCost = 0;
		for (final Event event : events) {

			charterCost += event.getCharterCost();

			if (event instanceof SlotVisit) {
				final SlotVisit slotVisit = (SlotVisit) event;
				// Port Costs
				shippingCost += slotVisitFunc.apply(slotVisit);
			}

			if (event instanceof Journey) {
				final Journey journey = (Journey) event;
				// Canal Costs
				shippingCost += journey.getToll();
			}

			if (event instanceof FuelUsage) {
				final FuelUsage fuelUsage = (FuelUsage) event;
				// Base fuel costs
				shippingCost += fuelUsageFunc.apply(fuelUsage);
			}

			if (event instanceof Cooldown) {
				final Cooldown cooldown = (Cooldown) event;
				shippingCost += cooldown.getCost();
			}
		}

		// Add on chartering costs
		if (sequence == null || sequence.isSpotVessel() || sequence.isTimeCharterVessel()) {
			shippingCost += charterCost;
		}
		return shippingCost;

	}

	private static long getEventShippingCost(final Event event) {

		if (event == null) {
			return 0;
		}

		final Sequence sequence = event.getSequence();

		if (sequence == null) {
			return 0;
		}
		// Bit of a double count here, but need to decide what to add to the model
		long shippingCost = 0;
		long charterCost = 0;
		{

			charterCost += event.getCharterCost();

			if (event instanceof SlotVisit) {
				final SlotVisit slotVisit = (SlotVisit) event;
				// Port Costs
				shippingCost += slotVisit.getPortCost();
			}

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

			if (event instanceof Cooldown) {
				final Cooldown cooldown = (Cooldown) event;
				shippingCost += cooldown.getCost();
			}
		}

		// Add on chartering costs
		if (sequence.isSpotVessel() || sequence.isTimeCharterVessel()) {
			shippingCost += charterCost;
		}
		return shippingCost;

	}

	private static int getFuelCost(final FuelUsage fuelUser, final Fuel... fuels) {
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

	private static int getFuelVolume(final FuelUsage fuelUser, final FuelUnit unit, final Fuel... fuels) {
		final Set<Fuel> fuelsOfInterest = Sets.newHashSet(fuels);
		int sum = 0;
		if (fuelUser != null) {
			final EList<FuelQuantity> fuelQuantities = fuelUser.getFuels();
			for (final FuelQuantity fq : fuelQuantities) {
				if (fuelsOfInterest.contains(fq.getFuel())) {
					for (final FuelAmount fa : fq.getAmounts()) {
						if (fa.getUnit() == unit) {
							sum += fa.getQuantity();
						}
					}
				}
			}
		}
		return sum;
	}

	private static int getFuelCost(final FuelUsage fuelUser1, final FuelUsage fuelUser2, final FuelUsage fuelUser3, final Fuel... fuels) {
		return getFuelCost(fuelUser1, fuels) + getFuelCost(fuelUser2, fuels) + getFuelCost(fuelUser3, fuels);
	}

	private static int getFuelVolume(final FuelUsage fuelUser1, final FuelUsage fuelUser2, final FuelUnit unit, final Fuel... fuels) {
		return getFuelVolume(fuelUser1, unit, fuels) + getFuelVolume(fuelUser2, unit, fuels);
	}

	private static int getFuelVolume(final FuelUsage fuelUser1, final FuelUsage fuelUser2, final FuelUsage fuelUser3, final FuelUnit unit, final Fuel... fuels) {
		return getFuelVolume(fuelUser1, unit, fuels) + getFuelVolume(fuelUser2, unit, fuels) + getFuelVolume(fuelUser3, unit, fuels);
	}

	private static Integer getEquityPNLValue(final ProfitAndLossContainer container) {

		int equityPNL = 0;

		if (container != null) {

			final GroupProfitAndLoss dataWithKey = container.getGroupProfitAndLoss();
			if (dataWithKey != null) {
				final EList<EntityProfitAndLoss> entityProfitAndLosses = dataWithKey.getEntityProfitAndLosses();
				for (final EntityProfitAndLoss entityProfitAndLoss : entityProfitAndLosses) {
					if (entityProfitAndLoss.getEntityBook().equals(entityProfitAndLoss.getEntity().getUpstreamBook())) {
						equityPNL += entityProfitAndLoss.getProfitAndLoss();
					}
				}

			}
		}

		return equityPNL;
	}

	private static Integer getAdditionalPNLValue(final ProfitAndLossContainer container) {

		int addnPNL = 0;

		if (container != null) {

			final GroupProfitAndLoss dataWithKey = container.getGroupProfitAndLoss();
			if (dataWithKey != null) {
				for (final GeneralPNLDetails generalPNLDetails : container.getGeneralPNLDetails()) {
					if (generalPNLDetails instanceof SlotPNLDetails) {
						final SlotPNLDetails slotPNLDetails = (SlotPNLDetails) generalPNLDetails;
						for (final GeneralPNLDetails details : slotPNLDetails.getGeneralPNLDetails()) {
							if (details instanceof BasicSlotPNLDetails) {
								addnPNL += ((BasicSlotPNLDetails) details).getAdditionalPNL();
								addnPNL += ((BasicSlotPNLDetails) details).getExtraShippingPNL();
								addnPNL += ((BasicSlotPNLDetails) details).getExtraUpsidePNL();
							}
						}
					}
				}
			}
		}

		return addnPNL;
	}

	/**
	 * Get total cargo PNL value
	 * 
	 * @param container
	 * @param entity
	 * @return
	 */
	private static Integer getPNLValue(final ProfitAndLossContainer container) {
		if (container == null) {
			return null;
		}

		final GroupProfitAndLoss groupProfitAndLoss = container.getGroupProfitAndLoss();
		if (groupProfitAndLoss == null) {
			return null;
		}
		// Rounding!
		return (int) groupProfitAndLoss.getProfitAndLoss();
	}

	private static <T> int getOrZero(final T object, final ToIntFunction<T> func) {
		if (object != null) {
			return func.applyAsInt(object);
		}
		return 0;
	}

	private static int getPortDuration(final Journey travel) {
		return travel.getPreviousEvent().getDuration();
	}

	private static String getRoute(final RouteOption routeOption) {
		switch (routeOption) {

		case DIRECT:
			return "direct";
		case PANAMA:
			return "Panama";
		case SUEZ:
			return "Suez";
		default:
			break;

		}
		return null;
	}
}
