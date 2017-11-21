/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
package com.mmxlabs.lingo.reports.views.standard.econs;

import java.text.DecimalFormat;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.function.BiFunction;
import java.util.function.Function;

import org.apache.shiro.SecurityUtils;
import org.eclipse.emf.common.util.EList;
import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.Nullable;
import org.eclipse.jface.viewers.IColorProvider;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.widgets.Display;

import com.google.common.collect.Sets;
import com.mmxlabs.lingo.reports.views.standard.econs.CargoEconsReportComponent.CargoAllocationPair;
import com.mmxlabs.lingo.reports.views.standard.econs.CargoEconsReportComponent.DeltaPair;
import com.mmxlabs.lingo.reports.views.standard.econs.CargoEconsReportComponent.VesselEventVisitPair;
import com.mmxlabs.lingo.reports.views.standard.econs.StandardEconsRowFactory.EconsOptions.MarginBy;
import com.mmxlabs.models.lng.cargo.CharterOutEvent;
import com.mmxlabs.models.lng.cargo.DischargeSlot;
import com.mmxlabs.models.lng.cargo.LoadSlot;
import com.mmxlabs.models.lng.schedule.BasicSlotPNLDetails;
import com.mmxlabs.models.lng.schedule.CargoAllocation;
import com.mmxlabs.models.lng.schedule.Cooldown;
import com.mmxlabs.models.lng.schedule.EntityProfitAndLoss;
import com.mmxlabs.models.lng.schedule.Event;
import com.mmxlabs.models.lng.schedule.Fuel;
import com.mmxlabs.models.lng.schedule.FuelQuantity;
import com.mmxlabs.models.lng.schedule.FuelUsage;
import com.mmxlabs.models.lng.schedule.GeneralPNLDetails;
import com.mmxlabs.models.lng.schedule.GroupProfitAndLoss;
import com.mmxlabs.models.lng.schedule.Journey;
import com.mmxlabs.models.lng.schedule.MarketAllocation;
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

public class StandardEconsRowFactory implements IEconsRowFactory {

	public static class EconsOptions {
		enum MarginBy {
			PURCHASE_VOLUME, SALE_VOLUME
		};

		MarginBy marginBy = MarginBy.SALE_VOLUME;

	}

	public static final DecimalFormat DollarsFormat = new DecimalFormat("$##,###,###,###");
	public static final DecimalFormat VolumeMMBtuFormat = new DecimalFormat("##,###,###,###mmBtu");
	public static final DecimalFormat DollarsPerMMBtuFormat = new DecimalFormat("$###.###/mmBtu");
	public static final DecimalFormat DaysFormat = new DecimalFormat("##");

	public Collection<CargoEconsReportRow> createRows(@NonNull final EconsOptions options, @Nullable final Collection<Object> targets) {

		boolean containsCargo = false;
		boolean containsEvent = false;
		boolean containsCharterOut = false;
		if (targets == null || targets.isEmpty()) {
			containsCargo = true;
		} else {
			for (final Object target : targets) {
				if (target instanceof CargoAllocation) {
					containsCargo = true;
				}
				if (target instanceof VesselEventVisit) {
					final VesselEventVisit vesselEventVisit = (VesselEventVisit) target;
					containsEvent = true;
					if (vesselEventVisit.getVesselEvent() instanceof CharterOutEvent) {
						containsCharterOut = true;
					}
				}
			}
		}

		final List<CargoEconsReportRow> rows = new LinkedList<>();
		if (containsCargo) {
			rows.add(createRow(10, "Purchase", "$", true, createBuyValuePrice(options)));
			rows.add(createRow(20, "    Price", "$/mmBTu", true, createBuyPrice(options)));
			rows.add(createRow(30, "    Volume", "mmBTu", false, createBuyVolumeMMBTuPrice(options)));
		}
		rows.add(createRow(40, "Shipping", "$", true, createShippingCosts(options)));
		rows.add(createRow(50, "    Bunkers", "$", true, createShippingBunkersTotal(options)));
		rows.add(createRow(60, "    Port", "$", true, createShippingPortCosts(options)));
		rows.add(createRow(70, "    Canal", "$", true, createShippingCanalCosts(options)));
		rows.add(createRow(80, "    Boil-off", "$", true, createShippingBOGTotal(options), createBOGColourProvider(options)));
		rows.add(createRow(90, "    Charter Cost", "$", true, createShippingCharterCosts(options), createCharterFeesColourProvider(options)));
		if (containsCharterOut) {
			rows.add(createRow(100, "Charter Revenue", "$", false, createShippingCharterRevenue(options)));
			rows.add(createRow(110, "Repositioning", "$", true, createShippingRepositioning(options)));
			rows.add(createRow(120, "Ballast bonus", "$", false, createShippingBallastBonus(options)));
			rows.add(createRow(130, "Charter Duration", "", false, createCharterDays(options)));
		}
		if (containsCargo) {
			rows.add(createRow(140, "Sale", "$", false, createSellValuePrice(options)));
			rows.add(createRow(150, "    Price", "$/mmBTu", false, createSellPrice(options)));
			rows.add(createRow(160, "    Volume", "mmBtu", false, createSellVolumeMMBTuPrice(options)));
			if (SecurityUtils.getSubject().isPermitted("features:report-equity-book")) {
				rows.add(createRow(170, "Equity P&L", "$", false, createPNLEquity(options)));
			}
			rows.add(createRow(180, "Addn. P&L", "$", false, createPNLAdditional(options)));
		}
		rows.add(createRow(190, "P&L", "$", false, createPNLTotal(options)));
		if (containsCargo) {
			final CargoEconsReportRow row = createRow(200, "Margin", "$/mmBTu", false, createPNLPerMMBTU(options));
			row.tooltip = () -> {
				switch (options.marginBy) {
				case PURCHASE_VOLUME:
					return "Margin by purchase volume";
				case SALE_VOLUME:
					return "Margin by sales volume";
				default:
					return null;

				}
			};
			rows.add(row);
		}

		return rows;
	}

	private @Nullable IColorProvider createBOGColourProvider(final EconsOptions options) {

		return new IColorProvider() {

			@Override
			public Color getForeground(final Object element) {

				return Display.getDefault().getSystemColor(SWT.COLOR_GRAY);

			}

			@Override
			public Color getBackground(final Object element) {
				return null;
			}
		};
	}

	private @Nullable IColorProvider createCharterFeesColourProvider(final EconsOptions options) {

		return new IColorProvider() {

			@Override
			public Color getForeground(final Object element) {

				if (element instanceof CargoAllocation) {
					final CargoAllocation cargoAllocation = (CargoAllocation) element;
					final boolean isFleet = cargoAllocation.getSequence() != null && (!(cargoAllocation.getSequence().isSpotVessel() || cargoAllocation.getSequence().isTimeCharterVessel()));
					if (isFleet) {
						return Display.getDefault().getSystemColor(SWT.COLOR_GRAY);
					}
				} else if (element instanceof VesselEventVisit) {
					final VesselEventVisit vesselEventVisit = (VesselEventVisit) element;
					final boolean isFleet = vesselEventVisit.getSequence() != null && (!(vesselEventVisit.getSequence().isSpotVessel() || vesselEventVisit.getSequence().isTimeCharterVessel()));
					if (isFleet) {
						return Display.getDefault().getSystemColor(SWT.COLOR_GRAY);
					}
				}

				return null;
			}

			@Override
			public Color getBackground(final Object element) {
				return null;
			}

		};
	}

	public static CargoEconsReportRow createRow(final int order, final @NonNull String name, final @NonNull String unit, boolean isCost, final @NonNull ICellRenderer renderer) {
		return createRow(order, name, unit, isCost, renderer, null);
	}

	public static CargoEconsReportRow createRow(final int order, final @NonNull String name, final @NonNull String unit, boolean isCost, final @NonNull ICellRenderer formatter,
			@Nullable final IColorProvider colourProvider) {
		final CargoEconsReportRow row = new CargoEconsReportRow();
		row.order = order;
		row.name = name;
		row.unit = unit;
		row.formatter = formatter;
		row.colourProvider = colourProvider;
		row.isCost = isCost;
		
		return row;
	}
	public <T, U, W> T getFromCargoAllocationPairListBi(Class<T> type, BiFunction<U, W, T> f, Object object, W options) {
		final List<DeltaPair> cargoAllocations = (List<DeltaPair>) object;

		if (type.getName().equals("java.lang.Integer")) {
			int acc = 0;
			for (DeltaPair cargoAllocation: cargoAllocations) {
				acc += (int) getFromCargoAllocationPairBi(type, f, cargoAllocation, options);
			}
			return type.cast(acc);
		} else if (type.getName().equals("java.lang.Double")) {
			double acc = 0;
			for (DeltaPair cargoAllocation: cargoAllocations) {
				acc += (double) getFromCargoAllocationPairBi(type, f, cargoAllocation, options);
			}
			return type.cast(acc);
		} else if (type.getName().equals("java.lang.Long")) {
			long acc = 0;
			for (DeltaPair cargoAllocation: cargoAllocations) {
				acc += (long) getFromCargoAllocationPairBi(type, f, cargoAllocation, options);
			}
			return type.cast(acc);
		}	

		return null;
	}

	public <T, U, W> T getFromCargoAllocationPairBi(Class<T> type, BiFunction<U, W, T> f, Object object, W options) {
		Object first = null;
		Object second = null;
		

		if (object instanceof DeltaPair) {
			final DeltaPair deltaPair = (DeltaPair) object;
			first = deltaPair.first();
			second = deltaPair.second();
		}

		T valueFirst = f.apply((U) first, options);

		if (second != null) {
			T valueSecond = f.apply((U) second, options);

			if (valueFirst instanceof Integer) {
				return type.cast(((int) valueFirst - (int) valueSecond));
			} else if (valueFirst instanceof Double) {
				return type.cast(((double) valueFirst - (double) valueSecond));
			} else if (valueFirst instanceof Long) {
				return type.cast(((long) valueFirst - (long) valueSecond));
			}
		}

		return valueFirst;
	}
	
	
	
	public <T,U> T getFromCargoAllocationPairList(Class<T> type, Function<U, T> f, Object object) {
		final List<DeltaPair> cargoAllocations = (List<DeltaPair>) object;
		
		if (type.getName().equals("java.lang.Integer")) {
			int acc = 0;
			for (DeltaPair cargoAllocation: cargoAllocations) {
				acc += (int) getFromCargoAllocationPair(type, f, cargoAllocation);
			}
			return type.cast(acc);
		} else if (type.getName().equals("java.lang.Double")) {
			double acc = 0;
			for (DeltaPair cargoAllocation: cargoAllocations) {
				acc += (double) getFromCargoAllocationPair(type, f, cargoAllocation);
			}
			return type.cast(acc);
		} else if (type.getName().equals("java.lang.Long")) {
			long acc = 0;
			for (DeltaPair cargoAllocation: cargoAllocations) {
				acc += (long) getFromCargoAllocationPair(type, f, cargoAllocation);
			}
			return type.cast(acc);
		}	

		return null;
	}

	public <T, U> T getFromCargoAllocationPair(Class<T> type, Function<U, T> f, Object object) {
		Object first = null;
		Object second = null;
		
		if (object instanceof DeltaPair) {
			final DeltaPair deltaPair = (DeltaPair) object;
			first = deltaPair.first();
			second = deltaPair.second();
		}
		
		T valueFirst = f.apply((U) first);

		if (second != null) {
			T valueSecond = f.apply((U) second);

			if (valueFirst instanceof Integer) {
				return type.cast(((int) valueSecond - (int) valueFirst));
			} else if (valueFirst instanceof Double) {
				return type.cast(((double) valueSecond - (double) valueFirst));
			} else if (valueFirst instanceof Long) {
				return type.cast(((long) valueSecond - (long) valueFirst));
			}
		}

		return valueFirst;
	}

	public static double cargoAllocationBuyPriceHelper(Object object) {
		if (object instanceof CargoAllocation) {
			CargoAllocation cargoAllocation = (CargoAllocation) object;
		
		for (final SlotAllocation allocation : cargoAllocation.getSlotAllocations()) {
			if (allocation.getSlotAllocationType() == SlotAllocationType.PURCHASE || allocation.getSlot() instanceof LoadSlot) {
				return allocation.getPrice();
			}
		}
		}
		return 0.0f;
	}
	
	public @NonNull ICellRenderer createBuyPrice(final EconsOptions options) {
		return new BaseFormatter() {
			@Override
			public @Nullable String render(final Object object) {
				if (object instanceof CargoAllocation) {
					final CargoAllocation cargoAllocation = (CargoAllocation) object;
					final double price = cargoAllocationBuyPriceHelper(cargoAllocation);
					return DollarsPerMMBtuFormat.format(price);
				} else if (object instanceof CargoAllocationPair) {
					double value = getFromCargoAllocationPair(Double.class, StandardEconsRowFactory::cargoAllocationBuyPriceHelper, object);
					return DollarsPerMMBtuFormat.format(value); 
				} else if (object instanceof List<?>) {
					double value = getFromCargoAllocationPairList(Double.class, StandardEconsRowFactory::cargoAllocationBuyPriceHelper, object);
					return DollarsPerMMBtuFormat.format(value); 
				}

				return null;
			}
		};
	}

	private static double cargoAllocationSellPriceHelper(Object object) {

		if (object instanceof CargoAllocation) {
			CargoAllocation cargoAllocation = (CargoAllocation) object;

			for (final SlotAllocation allocation : cargoAllocation.getSlotAllocations()) {
				if (allocation.getSlotAllocationType() == SlotAllocationType.SALE || allocation.getSlot() instanceof DischargeSlot) {
					return allocation.getPrice();
				}
			}
		}
		return 0.0f;
	}

	public @NonNull ICellRenderer createSellPrice(final EconsOptions options) {
		return new BaseFormatter() {
			@Override
			public @Nullable String render(final Object object) {
				if (object instanceof CargoAllocation) {
					final CargoAllocation cargoAllocation = (CargoAllocation) object;

					final double price = cargoAllocationSellPriceHelper(cargoAllocation);

					return DollarsPerMMBtuFormat.format(price);
				} else if (object instanceof CargoAllocationPair) {
					double value = getFromCargoAllocationPair(Double.class, StandardEconsRowFactory::cargoAllocationSellPriceHelper, object);
					return DollarsPerMMBtuFormat.format(value);
				} else if (object instanceof List<?>) {
					double value = getFromCargoAllocationPairList(Double.class, StandardEconsRowFactory::cargoAllocationSellPriceHelper, object);
					return DollarsPerMMBtuFormat.format(value);
				}

				return null;
			}
		};
	}

	private static long cargoAllocationBuyValuePriceHelper(Object object) {
		long cost = 0;

		if (object instanceof CargoAllocation) {
			CargoAllocation cargoAllocation = (CargoAllocation) object;

			for (final SlotAllocation allocation : cargoAllocation.getSlotAllocations()) {
				if (allocation.getSlotAllocationType() == SlotAllocationType.PURCHASE || allocation.getSlot() instanceof LoadSlot) {
					cost += allocation.getVolumeValue();
				}
			}
		}
		return cost;
	}

	public @NonNull ICellRenderer createBuyValuePrice(final EconsOptions options) {
		return new BaseFormatter() {
			@Override
			public @Nullable String render(final Object object) {
				if (object instanceof CargoAllocation) {
					final CargoAllocation cargoAllocation = (CargoAllocation) object;

					long cost = cargoAllocationBuyValuePriceHelper(cargoAllocation);

					return DollarsFormat.format(cost);
				} else if (object instanceof MarketAllocation) {
					final MarketAllocation marketAllocation = (MarketAllocation) object;
					final SlotAllocation allocation = marketAllocation.getSlotAllocation();
					return DollarsFormat.format(allocation.getVolumeValue());
				} else if (object instanceof CargoAllocationPair) {
					long value = getFromCargoAllocationPair(Long.class, StandardEconsRowFactory::cargoAllocationBuyValuePriceHelper, object);
					return DollarsFormat.format(value);
				} else if (object instanceof List<?>) {
					long value = getFromCargoAllocationPairList(Long.class, StandardEconsRowFactory::cargoAllocationBuyValuePriceHelper, object);
					return DollarsFormat.format(value);
				}

				return null;
			}
		};
	}

	private static long cargoAllocationSellValuePriceHelper(Object object) {
		long cost = 0;

		if (object instanceof CargoAllocation) {
			CargoAllocation cargoAllocation = (CargoAllocation) object;

			for (final SlotAllocation allocation : cargoAllocation.getSlotAllocations()) {
				if (allocation.getSlotAllocationType() == SlotAllocationType.SALE || allocation.getSlot() instanceof DischargeSlot) {
					cost += allocation.getVolumeValue();
				}
			}
		}
		return cost;
	}

	public @NonNull ICellRenderer createSellValuePrice(final EconsOptions options) {
		return new BaseFormatter() {
			@Override
			public @Nullable String render(final Object object) {
				if (object instanceof CargoAllocation) {
					final CargoAllocation cargoAllocation = (CargoAllocation) object;

					long cost = cargoAllocationSellValuePriceHelper(cargoAllocation);
					return DollarsFormat.format(cost);
				} else if (object instanceof MarketAllocation) {
					final MarketAllocation marketAllocation = (MarketAllocation) object;
					final SlotAllocation allocation = marketAllocation.getSlotAllocation();
					return DollarsFormat.format(allocation.getVolumeValue());
				} else if (object instanceof CargoAllocationPair) {
					long value = getFromCargoAllocationPair(Long.class, StandardEconsRowFactory::cargoAllocationSellValuePriceHelper, object);
					return DollarsFormat.format(value);
				} else if (object instanceof List<?>) {
					long value = getFromCargoAllocationPairList(Long.class, StandardEconsRowFactory::cargoAllocationSellValuePriceHelper, object);
					return DollarsFormat.format(value);
				}

				return null;
			}
		};
	}

	private static long cargoAllocationBuyVolumeMMBTuPriceHelper(Object object) {
		long cost = 0;

		if (object instanceof CargoAllocation) {
			CargoAllocation cargoAllocation = (CargoAllocation) object;

			for (final SlotAllocation allocation : cargoAllocation.getSlotAllocations()) {
				if (allocation.getSlotAllocationType() == SlotAllocationType.PURCHASE || allocation.getSlot() instanceof LoadSlot) {
					cost += allocation.getEnergyTransferred();
				}
			}
		}
		return cost;
	}

	public @NonNull ICellRenderer createBuyVolumeMMBTuPrice(final EconsOptions options) {
		return new BaseFormatter() {
			@Override
			public @Nullable String render(final Object object) {
				if (object instanceof CargoAllocation) {
					final CargoAllocation cargoAllocation = (CargoAllocation) object;

					long cost = cargoAllocationBuyVolumeMMBTuPriceHelper(cargoAllocation);
					return VolumeMMBtuFormat.format(cost);
				} else if (object instanceof MarketAllocation) {
					final MarketAllocation marketAllocation = (MarketAllocation) object;
					final SlotAllocation allocation = marketAllocation.getSlotAllocation();
					return VolumeMMBtuFormat.format(allocation.getEnergyTransferred());
				} else if (object instanceof CargoAllocationPair) {
					long value = getFromCargoAllocationPair(Long.class, StandardEconsRowFactory::cargoAllocationBuyVolumeMMBTuPriceHelper, object);
					return VolumeMMBtuFormat.format(value);
				} else if (object instanceof List<?>) {
					long value = getFromCargoAllocationPairList(Long.class, StandardEconsRowFactory::cargoAllocationBuyVolumeMMBTuPriceHelper, object);
					return VolumeMMBtuFormat.format(value);
				}

				return null;
			}
		};
	}

	private static long cargoAllocationSellVolumeMMBTuPriceHelper(Object object) {
		long cost = 0;

		if (object instanceof CargoAllocation) {
			CargoAllocation cargoAllocation = (CargoAllocation) object;

			for (final SlotAllocation allocation : cargoAllocation.getSlotAllocations()) {
				if (allocation.getSlotAllocationType() == SlotAllocationType.SALE || allocation.getSlot() instanceof DischargeSlot) {
					cost += allocation.getEnergyTransferred();
				}
			}
		}
		return cost;
	}

	public @NonNull ICellRenderer createSellVolumeMMBTuPrice(final EconsOptions options) {
		return new BaseFormatter() {
			@Override
			public @Nullable String render(final Object object) {
				if (object instanceof CargoAllocation) {
					final CargoAllocation cargoAllocation = (CargoAllocation) object;
					final long cost = cargoAllocationSellVolumeMMBTuPriceHelper(cargoAllocation);
					return VolumeMMBtuFormat.format(cost);
				} else if (object instanceof MarketAllocation) {
					final MarketAllocation marketAllocation = (MarketAllocation) object;
					final SlotAllocation allocation = marketAllocation.getSlotAllocation();
					return VolumeMMBtuFormat.format(allocation.getEnergyTransferred());
				} else if (object instanceof CargoAllocationPair) {
					long value = getFromCargoAllocationPair(Long.class, StandardEconsRowFactory::cargoAllocationSellVolumeMMBTuPriceHelper, object);
					return VolumeMMBtuFormat.format(value);
				} else if (object instanceof List<?>) {
					long value = getFromCargoAllocationPairList(Long.class, StandardEconsRowFactory::cargoAllocationSellVolumeMMBTuPriceHelper, object);
					return VolumeMMBtuFormat.format(value);
				}

				return null;
			}
		};
	}

	public double cargoAllocationPNLPerMMBTUHelper(Object object, StandardEconsRowFactory.EconsOptions options) {
		if (object instanceof CargoAllocation) {

			CargoAllocation cargoAllocation = (CargoAllocation) object;
			final Integer pnl = getPNLValue(cargoAllocation);
			if (pnl != null) {
				double volume = 0.0;
				if (options.marginBy == MarginBy.PURCHASE_VOLUME) {
					for (final SlotAllocation allocation : cargoAllocation.getSlotAllocations()) {
						if (allocation.getSlotAllocationType() == SlotAllocationType.PURCHASE || allocation.getSlot() instanceof LoadSlot) {
							volume += allocation.getEnergyTransferred();
						}
					}
				} else if (options.marginBy == MarginBy.SALE_VOLUME) {
					for (final SlotAllocation allocation : cargoAllocation.getSlotAllocations()) {
						if (allocation.getSlotAllocationType() == SlotAllocationType.SALE || allocation.getSlot() instanceof DischargeSlot) {
							volume += allocation.getEnergyTransferred();
						}
					}
				} else {
					return 0.0f;
				}

				if (volume != 0.0) {
					return (double) pnl / volume;
				} else {
					return 0.0f;
				}
			}
		}
		return 0.0f;
	}

	public @NonNull ICellRenderer createPNLPerMMBTU(final EconsOptions options) {
		return new BaseFormatter() {
			@Override
			public @Nullable String render(final Object object) {
				if (object instanceof CargoAllocation) {
					final CargoAllocation cargoAllocation = (CargoAllocation) object;
					double PNLPerMMBTU = cargoAllocationPNLPerMMBTUHelper(cargoAllocation, options);

					if (PNLPerMMBTU != 0.0) {
						return DollarsPerMMBtuFormat.format(PNLPerMMBTU);
					} else {
						return null;
					}
				} else if (object instanceof MarketAllocation) {
					final MarketAllocation marketAllocation = (MarketAllocation) object;

					final Integer pnl = getPNLValue(marketAllocation);
					if (pnl != null) {
						final SlotAllocation allocation = marketAllocation.getSlotAllocation();
						final double volume = allocation.getEnergyTransferred();

						return DollarsPerMMBtuFormat.format((double) pnl / volume);
					}
				} else if (object instanceof CargoAllocationPair) {
					double value = getFromCargoAllocationPairBi(Double.class, (data, options) -> {
						return cargoAllocationPNLPerMMBTUHelper(data, options);
					}, object, options);
					return DollarsPerMMBtuFormat.format(value);
				} else if (object instanceof List<?>) {
					double value = getFromCargoAllocationPairListBi(Double.class, (data, options) -> {
						return cargoAllocationPNLPerMMBTUHelper(data, options);
					}, object, options);
					return DollarsPerMMBtuFormat.format(value);
				}

				return null;
			}
		};
	}

	public @NonNull ICellRenderer createPNLTotal(final EconsOptions options) {
		return new BaseFormatter() {
			@Override
			public @Nullable String render(final Object object) {
				if (object instanceof CargoAllocation) {
					final CargoAllocation cargoAllocation = (CargoAllocation) object;

					final Integer pnl = getPNLValue(cargoAllocation);
					if (pnl != null) {
						return DollarsFormat.format(pnl);
					}
				} else if (object instanceof VesselEventVisit) {
					final VesselEventVisit cargoAllocation = (VesselEventVisit) object;

					final Integer pnl = getPNLValue(cargoAllocation);
					if (pnl != null) {
						return DollarsFormat.format(pnl);
					}
				} else if (object instanceof VesselEventVisitPair) {
					Integer value = getFromCargoAllocationPair(Integer.class, StandardEconsRowFactory::getPNLValue, object);

					if (value != null) {
						return DollarsFormat.format(value);
					}
				} else if (object instanceof MarketAllocation) {
					final MarketAllocation marketAllocation = (MarketAllocation) object;
					final Integer pnl = getPNLValue(marketAllocation);
					if (pnl != null) {
						return DollarsFormat.format(pnl);
					}
				} else if (object instanceof CargoAllocationPair) {
					Integer value = getFromCargoAllocationPair(Integer.class, StandardEconsRowFactory::getPNLValue, object);
					if (value != null) {
						return DollarsFormat.format(value);
					}
				} else if (object instanceof List<?>) {
					Integer value = getFromCargoAllocationPairList(Integer.class, StandardEconsRowFactory::getPNLValue, object);
					if (value != null) {
						return DollarsFormat.format(value);
					}
				}

				return null;
			}
		};
	}

	public @NonNull ICellRenderer createPNLEquity(final EconsOptions options) {
		return new BaseFormatter() {
			@Override
			public @Nullable String render(final Object object) {
				if (object instanceof CargoAllocation) {
					final CargoAllocation cargoAllocation = (CargoAllocation) object;

					final Integer pnl = getEquityPNLValue(cargoAllocation);
					if (pnl != null) {
						return DollarsFormat.format(pnl);
					}
				} else if (object instanceof CargoAllocationPair) {
					Integer value = getFromCargoAllocationPair(Integer.class, StandardEconsRowFactory::getEquityPNLValue, object);
					if (value != null) {
						return DollarsFormat.format(value);
					}
				} else if (object instanceof List<?>) {
					Integer value = getFromCargoAllocationPairList(Integer.class, StandardEconsRowFactory::getEquityPNLValue, object);
					if (value != null) {
						return DollarsFormat.format(value);
					}
				}

				return null;
			}
		};
	}

	public @NonNull ICellRenderer createPNLAdditional(final EconsOptions options) {
		return new BaseFormatter() {
			@Override
			public @Nullable String render(final Object object) {
				if (object instanceof CargoAllocation) {
					final CargoAllocation cargoAllocation = (CargoAllocation) object;

					final Integer pnl = getAdditionalPNLValue(cargoAllocation);
					if (pnl != null) {
						return DollarsFormat.format(pnl);
					}
				} else if (object instanceof MarketAllocation) {
					final MarketAllocation marketAllocation = (MarketAllocation) object;
					final Integer pnl = getAdditionalPNLValue(marketAllocation);
					if (pnl != null) {
						return DollarsFormat.format(pnl);
					}
				} else if (object instanceof CargoAllocationPair) {
					Integer value = getFromCargoAllocationPair(Integer.class, StandardEconsRowFactory::getAdditionalPNLValue, object);
					if (value != null) {
						return DollarsFormat.format(value);
					}
				} else if (object instanceof List<?>) {
					Integer value = getFromCargoAllocationPairList(Integer.class, StandardEconsRowFactory::getAdditionalPNLValue, object);
					if (value != null) {
						return DollarsFormat.format(value);
					}
				}

				return null;
			}
		};
	}

	private static int genericShippingBOGTotalHelper(Object object) {
		if (object instanceof CargoAllocation) {
			int cost = 0;

			final CargoAllocation cargoAllocation = (CargoAllocation) object;
			for (final Event event : cargoAllocation.getEvents()) {
				if (event instanceof FuelUsage) {
					final FuelUsage fuelUsage = (FuelUsage) event;
					cost += getFuelCost(fuelUsage, Fuel.NBO, Fuel.FBO);
				}
			}
			return cost;
		} else if (object instanceof VesselEventVisit) {

			final VesselEventVisit cargoAllocation = (VesselEventVisit) object;

			int cost = 0;
			for (final Event event : cargoAllocation.getEvents()) {
				if (event instanceof FuelUsage) {
					final FuelUsage fuelUsage = (FuelUsage) event;
					cost += getFuelCost(fuelUsage, Fuel.NBO, Fuel.FBO);
				}
			}
			return cost;
		}
		return 0;

	}

	public @NonNull ICellRenderer createShippingBOGTotal(final EconsOptions options) {
		return new BaseFormatter() {
			@Override
			public @Nullable String render(final Object object) {
				if (object instanceof CargoAllocation) {
					final CargoAllocation cargoAllocation = (CargoAllocation) object;

					int cost = genericShippingBOGTotalHelper(cargoAllocation);
					return DollarsFormat.format(cost);
				} else if (object instanceof VesselEventVisit) {
					final VesselEventVisit vesselEventVisit = (VesselEventVisit) object;

					int cost = genericShippingBOGTotalHelper(vesselEventVisit);
					return DollarsFormat.format(cost);
				} else if (object instanceof VesselEventVisitPair) {
					int value = getFromCargoAllocationPair(Integer.class, StandardEconsRowFactory::genericShippingBOGTotalHelper, object);
					return DollarsFormat.format(value);
				} else if (object instanceof CargoAllocationPair) {
					int value = getFromCargoAllocationPair(Integer.class, StandardEconsRowFactory::genericShippingBOGTotalHelper, object);
					return DollarsFormat.format(value);
				} else if (object instanceof List<?>) {
					int value = getFromCargoAllocationPairList(Integer.class, StandardEconsRowFactory::genericShippingBOGTotalHelper, object);
					return DollarsFormat.format(value);
				}

				return null;
			}

		};

	}

	private static int genericShippingBunkersTotalHelper(Object object) {

		if (object instanceof CargoAllocation) {
			int cost = 0;
			CargoAllocation cargoAllocation = (CargoAllocation) object;
			for (final Event event : cargoAllocation.getEvents()) {
				if (event instanceof FuelUsage) {
					final FuelUsage fuelUsage = (FuelUsage) event;
					cost += getFuelCost(fuelUsage, Fuel.BASE_FUEL, Fuel.PILOT_LIGHT);
				}
			}

			return cost;
		} else if (object instanceof VesselEventVisit) {
			VesselEventVisit cargoAllocation = (VesselEventVisit) object;
			int cost = 0;
			for (final Event event : cargoAllocation.getEvents()) {
				if (event instanceof FuelUsage) {
					final FuelUsage fuelUsage = (FuelUsage) event;
					cost += getFuelCost(fuelUsage, Fuel.BASE_FUEL, Fuel.PILOT_LIGHT);
				}
			}
			return cost;
		}
		return 0;
	}

	public @NonNull ICellRenderer createShippingBunkersTotal(final EconsOptions options) {
		return new BaseFormatter() {
			@Override
			public @Nullable String render(final Object object) {
				if (object instanceof CargoAllocation) {
					final CargoAllocation cargoAllocation = (CargoAllocation) object;

					final int cost = genericShippingBunkersTotalHelper(cargoAllocation);
					return DollarsFormat.format(cost);
				}
				if (object instanceof VesselEventVisit) {
					final VesselEventVisit vesselEventVisit = (VesselEventVisit) object;

					int cost = genericShippingBunkersTotalHelper(vesselEventVisit);
					return DollarsFormat.format(cost);

				} else if (object instanceof VesselEventVisitPair) {
					int value = getFromCargoAllocationPair(Integer.class, StandardEconsRowFactory::genericShippingBunkersTotalHelper, object);
					return DollarsFormat.format(value);
				} else if (object instanceof CargoAllocationPair) {
					int value = getFromCargoAllocationPair(Integer.class, StandardEconsRowFactory::genericShippingBunkersTotalHelper, object);
					return DollarsFormat.format(value);
				} else if (object instanceof List<?>) {
					int value = getFromCargoAllocationPairList(Integer.class, StandardEconsRowFactory::genericShippingBunkersTotalHelper, object);
					return DollarsFormat.format(value);
				}

				return null;
			}
		};
	}

	private static int genericShippingPortCostsHelper(Object object) {

		if (object instanceof CargoAllocation) {
			CargoAllocation cargoAllocation = (CargoAllocation) object;

			int cost = 0;
			for (final Event event : cargoAllocation.getEvents()) {
				if (event instanceof PortVisit) {
					final PortVisit portVisit = (PortVisit) event;
					cost += portVisit.getPortCost();
				}
			}

			return cost;
		} else if (object instanceof VesselEventVisit) {
			VesselEventVisit vesselEventVisit = (VesselEventVisit) object;
			int cost = 0;
			for (final Event event : vesselEventVisit.getEvents()) {
				if (event instanceof PortVisit) {
					final PortVisit portVisit = (PortVisit) event;
					cost += portVisit.getPortCost();
				}
			}
			return cost;
		}
		return 0;
	}

	public @NonNull ICellRenderer createShippingPortCosts(final EconsOptions options) {
		return new BaseFormatter() {
			@Override
			public @Nullable String render(final Object object) {
				if (object instanceof CargoAllocation) {
					final CargoAllocation cargoAllocation = (CargoAllocation) object;

					final int cost = genericShippingPortCostsHelper(cargoAllocation);
					return DollarsFormat.format(cost);
				} else if (object instanceof VesselEventVisit) {
					final VesselEventVisit vesselEventVisit = (VesselEventVisit) object;
					int cost = genericShippingPortCostsHelper(vesselEventVisit);
					return DollarsFormat.format(cost);
				} else if (object instanceof VesselEventVisitPair) {
					int value = getFromCargoAllocationPair(Integer.class, StandardEconsRowFactory::genericShippingPortCostsHelper, object);
					return DollarsFormat.format(value);
				} else if (object instanceof CargoAllocationPair) {
					int value = getFromCargoAllocationPair(Integer.class, StandardEconsRowFactory::genericShippingPortCostsHelper, object);
					return DollarsFormat.format(value);
				} else if (object instanceof List<?>) {
					int value = getFromCargoAllocationPairList(Integer.class, StandardEconsRowFactory::genericShippingPortCostsHelper, object);
					return DollarsFormat.format(value);
				}

				return null;
			}
		};
	}

	private static int genericShippingCanalCostsHelper(Object object) {
		if (object instanceof CargoAllocation) {
			CargoAllocation cargoAllocation = (CargoAllocation) object;

			int cost = 0;
			for (final Event event : cargoAllocation.getEvents()) {
				if (event instanceof Journey) {
					final Journey journey = (Journey) event;
					cost += journey.getToll();
				}
			}
			return cost;
		} else if (object instanceof VesselEventVisit) {
			VesselEventVisit vesselEventVisit = (VesselEventVisit) object;
			int cost = 0;
			for (final Event event : vesselEventVisit.getEvents()) {
				if (event instanceof Journey) {
					final Journey journey = (Journey) event;
					cost += journey.getToll();
				}
			}
			return cost;
		}
		return 0;
	}

	public @NonNull ICellRenderer createShippingCanalCosts(final EconsOptions options) {
		return new BaseFormatter() {
			@Override
			public @Nullable String render(final Object object) {
				if (object instanceof CargoAllocation) {
					final CargoAllocation cargoAllocation = (CargoAllocation) object;

					final int cost = genericShippingCanalCostsHelper(cargoAllocation);
					return DollarsFormat.format(cost);
				} else if (object instanceof VesselEventVisit) {
					int cost = genericShippingCanalCostsHelper(object);
					return DollarsFormat.format(cost);
				} else if (object instanceof VesselEventVisitPair) {
					int value = getFromCargoAllocationPair(Integer.class, StandardEconsRowFactory::genericShippingPortCostsHelper, object);
					return DollarsFormat.format(value);
				} else if (object instanceof CargoAllocationPair) {
					int value = getFromCargoAllocationPair(Integer.class, StandardEconsRowFactory::genericShippingCanalCostsHelper, object);
					return DollarsFormat.format(value);
				} else if (object instanceof List<?>) {
					int value = getFromCargoAllocationPairList(Integer.class, StandardEconsRowFactory::genericShippingCanalCostsHelper, object);
					return DollarsFormat.format(value);
				}
				return null;

			}
		};
	}

	public static int genericShippingCharterCostsHelper(Object object) {

		if (object instanceof CargoAllocation) {
			CargoAllocation cargoAllocation = (CargoAllocation) object;
			int cost = 0;
			for (final Event event : cargoAllocation.getEvents()) {
				cost += event.getCharterCost();
			}
			return cost;
		}

		if (object instanceof VesselEventVisit) {
			VesselEventVisit vesselEventVisit = (VesselEventVisit) object;
			int cost = 0;
			for (final Event event : vesselEventVisit.getEvents()) {
				cost += event.getCharterCost();
			}
			return cost;
		}

		return 0;
	}

	public @NonNull ICellRenderer createShippingCharterCosts(final EconsOptions options) {
		return new BaseFormatter() {
			@Override
			public @Nullable String render(final Object object) {
				if (object instanceof CargoAllocation) {
					final CargoAllocation cargoAllocation = (CargoAllocation) object;

					final int cost = genericShippingCharterCostsHelper(cargoAllocation);
					return DollarsFormat.format(cost);
				}
				if (object instanceof VesselEventVisit) {
					int cost = genericShippingCharterCostsHelper(object);
					return DollarsFormat.format(cost);
				} else if (object instanceof VesselEventVisitPair) {
					int value = getFromCargoAllocationPair(Integer.class, StandardEconsRowFactory::genericShippingCharterCostsHelper, object);
					return DollarsFormat.format(value);
				} else if (object instanceof CargoAllocationPair) {
					int value = getFromCargoAllocationPair(Integer.class, StandardEconsRowFactory::genericShippingCharterCostsHelper, object);
					return DollarsFormat.format(value);
				} else if (object instanceof List<?>) {
					int value = getFromCargoAllocationPairList(Integer.class, StandardEconsRowFactory::genericShippingCharterCostsHelper, object);
					return DollarsFormat.format(value);
				}
				return null;

			}
		};
	}

	public static int vesselEventVisitShippingCharterRevenueHelper(Object object) {
		int revenue = 0;

		if (object instanceof VesselEventVisit) {
			VesselEventVisit cargoAllocation = (VesselEventVisit) object;
			if (cargoAllocation.getVesselEvent() instanceof CharterOutEvent) {
				final CharterOutEvent charterOutEvent = (CharterOutEvent) cargoAllocation.getVesselEvent();
				revenue = charterOutEvent.getHireRate() * charterOutEvent.getDurationInDays();
			}
		}
		return revenue;
	}

	public @NonNull ICellRenderer createShippingCharterRevenue(final EconsOptions options) {
		return new BaseFormatter() {
			@Override
			public @Nullable String render(final Object object) {

				if (object instanceof VesselEventVisit) {
					final VesselEventVisit cargoAllocation = (VesselEventVisit) object;
					final int revenue = vesselEventVisitShippingCharterRevenueHelper(cargoAllocation);

					return DollarsFormat.format(revenue);
				} else if (object instanceof VesselEventVisitPair) {
					int value = getFromCargoAllocationPair(Integer.class, StandardEconsRowFactory::vesselEventVisitShippingCharterRevenueHelper, object);
					return DollarsFormat.format(value);
				} else if (object instanceof List<?>) {
					int value = getFromCargoAllocationPairList(Integer.class, StandardEconsRowFactory::vesselEventVisitShippingCharterRevenueHelper, object);
					return DollarsFormat.format(value);
				}

				return null;

			}
		};
	}

	public static int vesselEventVisitShippingBallastBonusHelper(Object object) {
		int revenue = 0;

		if (object instanceof VesselEventVisit) {
			VesselEventVisit cargoAllocation = (VesselEventVisit) object;
			if (cargoAllocation.getVesselEvent() instanceof CharterOutEvent) {
				final CharterOutEvent charterOutEvent = (CharterOutEvent) cargoAllocation.getVesselEvent();
				revenue = charterOutEvent.getBallastBonus();
			}
		}
		return revenue;
	}

	public @NonNull ICellRenderer createShippingBallastBonus(final EconsOptions options) {
		return new BaseFormatter() {
			@Override
			public @Nullable String render(final Object object) {

				if (object instanceof VesselEventVisit) {
					final VesselEventVisit cargoAllocation = (VesselEventVisit) object;

					int revenue = vesselEventVisitShippingBallastBonusHelper(cargoAllocation);
					return DollarsFormat.format(revenue);
				} else if (object instanceof VesselEventVisitPair) {
					int value = getFromCargoAllocationPair(Integer.class, StandardEconsRowFactory::vesselEventVisitShippingBallastBonusHelper, object);
					return DollarsFormat.format(value);
				} else if (object instanceof List<?>) {
					int value = getFromCargoAllocationPairList(Integer.class, StandardEconsRowFactory::vesselEventVisitShippingBallastBonusHelper, object);
					return DollarsFormat.format(value);
				}
				return null;

			}
		};
	}

	public static int vesselEventVisitCharterDaysHelper(Object object) {
		int days = 0;

		if (object instanceof VesselEventVisit) {
			VesselEventVisit cargoAllocation = (VesselEventVisit) object;
			if (cargoAllocation.getVesselEvent() instanceof CharterOutEvent) {
				final CharterOutEvent charterOutEvent = (CharterOutEvent) cargoAllocation.getVesselEvent();
				days = charterOutEvent.getDurationInDays();
			}
		}
		return days;
	}

	public @NonNull ICellRenderer createCharterDays(final EconsOptions options) {
		return new BaseFormatter() {
			@Override
			public @Nullable String render(final Object object) {

				if (object instanceof VesselEventVisit) {
					final VesselEventVisit cargoAllocation = (VesselEventVisit) object;
					final int days = vesselEventVisitCharterDaysHelper(cargoAllocation);

					return DaysFormat.format(days);
				} else if (object instanceof VesselEventVisitPair) {
					int days = getFromCargoAllocationPair(Integer.class, StandardEconsRowFactory::vesselEventVisitCharterDaysHelper, object);
					return DaysFormat.format(days);
				} else if (object instanceof List<?>) {
					int days = getFromCargoAllocationPairList(Integer.class, StandardEconsRowFactory::vesselEventVisitCharterDaysHelper, object);
					return DaysFormat.format(days);
				}

				return null;

			}
		};
	}

	public static int vesselEventVisitShippingRepositioningHelper(Object object) {
		int revenue = 0;

		if (object instanceof VesselEventVisit) {
			VesselEventVisit cargoAllocation = (VesselEventVisit) object;
			if (cargoAllocation.getVesselEvent() instanceof CharterOutEvent) {
				final CharterOutEvent charterOutEvent = (CharterOutEvent) cargoAllocation.getVesselEvent();
				revenue = charterOutEvent.getRepositioningFee();
			}
		}
		return revenue;
	}

	public @NonNull ICellRenderer createShippingRepositioning(final EconsOptions options) {
		return new BaseFormatter() {
			@Override
			public @Nullable String render(final Object object) {

				if (object instanceof VesselEventVisit) {
					final VesselEventVisit cargoAllocation = (VesselEventVisit) object;

					final int revenue = vesselEventVisitShippingRepositioningHelper(cargoAllocation);
					return DollarsFormat.format(revenue);
				} else if (object instanceof VesselEventVisitPair) {
					int revenue = getFromCargoAllocationPair(Integer.class, StandardEconsRowFactory::vesselEventVisitShippingRepositioningHelper, object);
					return DollarsFormat.format(revenue);
				} else if (object instanceof List<?>) {
					int revenue = getFromCargoAllocationPairList(Integer.class, StandardEconsRowFactory::vesselEventVisitShippingRepositioningHelper, object);
					return DollarsFormat.format(revenue);
				}
				return null;

			}
		};
	}

	public @NonNull ICellRenderer createShippingCosts(final EconsOptions options) {
		return new BaseFormatter() {
			@Override
			public @Nullable String render(final Object object) {
				if (object instanceof CargoAllocation) {
					final CargoAllocation cargoAllocation = (CargoAllocation) object;

					final Integer cost = getShippingCost(cargoAllocation);
					if (cost != null) {
						return DollarsFormat.format(cost);
					}
				}
				if (object instanceof VesselEventVisit) {
					final VesselEventVisit cargoAllocation = (VesselEventVisit) object;

					final Integer cost = getShippingCost(object);
					if (cost != null) {
						return DollarsFormat.format(cost);
					}
				} else if (object instanceof VesselEventVisitPair) {
					int cost = getFromCargoAllocationPair(Integer.class, StandardEconsRowFactory::getShippingCost, object);
					return DollarsFormat.format(cost);
				} else if (object instanceof CargoAllocationPair) {
					int cost = getFromCargoAllocationPair(Integer.class, StandardEconsRowFactory::getShippingCost, object);
					return DollarsFormat.format(cost);
				} else if (object instanceof List<?>) {
					int sum = getFromCargoAllocationPairList(Integer.class, StandardEconsRowFactory::getShippingCost, object);
					return DollarsFormat.format(sum);
				}

				return null;

			}
		};
	}

	private static Integer getShippingCost(Object object) {
		
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
		if (sequence == null || sequence.isSpotVessel() || sequence.isTimeCharterVessel()) {
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
}
