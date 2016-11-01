package com.mmxlabs.lingo.reports.views.standard;

import java.text.DecimalFormat;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import org.apache.shiro.SecurityUtils;
import org.eclipse.emf.common.util.EList;
import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.Nullable;
import org.eclipse.jface.viewers.IColorProvider;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.widgets.Display;

import com.google.common.collect.Sets;
import com.mmxlabs.lingo.reports.views.standard.StandardEconsRowFactory.EconsOptions.MarginBy;
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
import com.mmxlabs.models.lng.schedule.SlotAllocation;
import com.mmxlabs.models.lng.schedule.SlotAllocationType;
import com.mmxlabs.models.lng.schedule.SlotPNLDetails;
import com.mmxlabs.models.lng.schedule.SlotVisit;
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

	public Collection<CargoEconsReportRow> createRows(@NonNull final EconsOptions options) {
		List<CargoEconsReportRow> rows = new LinkedList<>();
		rows.add(createRow(10, "Purchase Cost", "$", createBuyValuePrice(options)));
		rows.add(createRow(20, "> Purchase Price", "$/mmBTu", createBuyPrice(options)));
		rows.add(createRow(30, "> Volume", "mmBTu", createBuyVolumeMMBTuPrice(options)));
		rows.add(createRow(40, "Shipping Cost", "$", createShippingCosts(options)));
		rows.add(createRow(50, "> Bunkers", "$", createShippingBunkersTotal(options)));
		rows.add(createRow(60, "> Port", "$", createShippingPortCosts(options)));
		rows.add(createRow(70, "> Canal", "$", createShippingCanalCosts(options)));
		rows.add(createRow(80, "> Boil-off", "$", createShippingBOGTotal(options), createBOGColourProvider(options)));
		rows.add(createRow(90, "> Charter fees", "$", createShippingCharterCosts(options), createCharterFeesColourProvider(options)));
		rows.add(createRow(100, "Sale Revenue", "$", createSellValuePrice(options)));
		rows.add(createRow(110, "> Sales Price", "$/mmBTu", createSellPrice(options)));
		rows.add(createRow(120, "> Volume", "mmBtu", createSellVolumeMMBTuPrice(options)));
		if (SecurityUtils.getSubject().isPermitted("features:report-equity-book")) {
			rows.add(createRow(130, "Equity P&L", "$", createPNLEquity(options)));
		}
		rows.add(createRow(140, "Addn. P&L", "$", createPNLAdditional(options)));
		rows.add(createRow(150, "P&L", "$", createPNLTotal(options)));
		{
			CargoEconsReportRow row = createRow(160, "Margin", "$/mmBTu", createPNLPerMMBTU(options));
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
					final boolean isFleet = !(cargoAllocation.getSequence().isSpotVessel() || cargoAllocation.getSequence().isTimeCharterVessel());
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

	public static CargoEconsReportRow createRow(int order, final @NonNull String name, final @NonNull String unit, final @NonNull ICellRenderer renderer) {
		return createRow(order, name, unit, renderer, null);
	}

	public static CargoEconsReportRow createRow(int order, final @NonNull String name, final @NonNull String unit, final @NonNull ICellRenderer formatter,
			@Nullable final IColorProvider colourProvider) {
		CargoEconsReportRow row = new CargoEconsReportRow();
		row.order = order;
		row.name = name;
		row.unit = unit;
		row.formatter = formatter;
		row.colourProvider = colourProvider;

		return row;
	}

	public @NonNull ICellRenderer createBuyPrice(final EconsOptions options) {
		return new BaseFormatter() {
			@Override
			public @Nullable String render(final Object object) {
				if (object instanceof CargoAllocation) {
					final CargoAllocation cargoAllocation = (CargoAllocation) object;
					for (final SlotAllocation allocation : cargoAllocation.getSlotAllocations()) {
						if (allocation.getSlotAllocationType() == SlotAllocationType.PURCHASE || allocation.getSlot() instanceof LoadSlot) {
							return DollarsPerMMBtuFormat.format(allocation.getPrice());
						}
					}
				}

				return null;
			}
		};
	}

	public @NonNull ICellRenderer createSellPrice(final EconsOptions options) {
		return new BaseFormatter() {
			@Override
			public @Nullable String render(final Object object) {
				if (object instanceof CargoAllocation) {
					final CargoAllocation cargoAllocation = (CargoAllocation) object;

					for (final SlotAllocation allocation : cargoAllocation.getSlotAllocations()) {
						if (allocation.getSlotAllocationType() == SlotAllocationType.SALE || allocation.getSlot() instanceof DischargeSlot) {
							return DollarsPerMMBtuFormat.format(allocation.getPrice());
						}
					}
				}

				return null;
			}
		};
	}

	public @NonNull ICellRenderer createBuyValuePrice(final EconsOptions options) {
		return new BaseFormatter() {
			@Override
			public @Nullable String render(final Object object) {
				if (object instanceof CargoAllocation) {
					final CargoAllocation cargoAllocation = (CargoAllocation) object;

					long cost = 0;
					for (final SlotAllocation allocation : cargoAllocation.getSlotAllocations()) {
						if (allocation.getSlotAllocationType() == SlotAllocationType.PURCHASE || allocation.getSlot() instanceof LoadSlot) {
							cost += allocation.getVolumeValue();
						}
					}
					return DollarsFormat.format(cost);
				} else if (object instanceof MarketAllocation) {
					final MarketAllocation marketAllocation = (MarketAllocation) object;
					final SlotAllocation allocation = marketAllocation.getSlotAllocation();
					return DollarsFormat.format(allocation.getVolumeValue());
				}

				return null;
			}
		};
	}

	public @NonNull ICellRenderer createSellValuePrice(final EconsOptions options) {
		return new BaseFormatter() {
			@Override
			public @Nullable String render(final Object object) {
				if (object instanceof CargoAllocation) {
					final CargoAllocation cargoAllocation = (CargoAllocation) object;

					long cost = 0;
					for (final SlotAllocation allocation : cargoAllocation.getSlotAllocations()) {
						if (allocation.getSlotAllocationType() == SlotAllocationType.SALE || allocation.getSlot() instanceof DischargeSlot) {
							cost += allocation.getVolumeValue();
						}
					}
					return DollarsFormat.format(cost);
				} else if (object instanceof MarketAllocation) {
					final MarketAllocation marketAllocation = (MarketAllocation) object;
					final SlotAllocation allocation = marketAllocation.getSlotAllocation();
					return DollarsFormat.format(allocation.getVolumeValue());
				}

				return null;
			}
		};
	}

	public @NonNull ICellRenderer createBuyVolumeMMBTuPrice(final EconsOptions options) {
		return new BaseFormatter() {
			@Override
			public @Nullable String render(final Object object) {
				if (object instanceof CargoAllocation) {
					final CargoAllocation cargoAllocation = (CargoAllocation) object;

					long cost = 0;
					for (final SlotAllocation allocation : cargoAllocation.getSlotAllocations()) {
						if (allocation.getSlotAllocationType() == SlotAllocationType.PURCHASE || allocation.getSlot() instanceof LoadSlot) {
							cost += allocation.getEnergyTransferred();
						}
					}
					return VolumeMMBtuFormat.format(cost);
				} else if (object instanceof MarketAllocation) {
					final MarketAllocation marketAllocation = (MarketAllocation) object;
					final SlotAllocation allocation = marketAllocation.getSlotAllocation();
					return VolumeMMBtuFormat.format(allocation.getEnergyTransferred());
				}

				return null;
			}
		};
	}

	public @NonNull ICellRenderer createSellVolumeMMBTuPrice(final EconsOptions options) {
		return new BaseFormatter() {
			@Override
			public @Nullable String render(final Object object) {
				if (object instanceof CargoAllocation) {
					final CargoAllocation cargoAllocation = (CargoAllocation) object;

					long cost = 0;
					for (final SlotAllocation allocation : cargoAllocation.getSlotAllocations()) {
						if (allocation.getSlotAllocationType() == SlotAllocationType.SALE || allocation.getSlot() instanceof DischargeSlot) {
							cost += allocation.getEnergyTransferred();
						}
					}
					return VolumeMMBtuFormat.format(cost);
				} else if (object instanceof MarketAllocation) {
					final MarketAllocation marketAllocation = (MarketAllocation) object;
					final SlotAllocation allocation = marketAllocation.getSlotAllocation();
					return VolumeMMBtuFormat.format(allocation.getEnergyTransferred());
				}

				return null;
			}
		};
	}

	public @NonNull ICellRenderer createPNLPerMMBTU(final EconsOptions options) {
		return new BaseFormatter() {
			@Override
			public @Nullable String render(final Object object) {
				if (object instanceof CargoAllocation) {
					final CargoAllocation cargoAllocation = (CargoAllocation) object;

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
							return null;
						}
						return DollarsPerMMBtuFormat.format((double) pnl / volume);
					}
				} else if (object instanceof MarketAllocation) {
					final MarketAllocation marketAllocation = (MarketAllocation) object;

					final Integer pnl = getPNLValue(marketAllocation);
					if (pnl != null) {
						final SlotAllocation allocation = marketAllocation.getSlotAllocation();
						double volume = allocation.getEnergyTransferred();

						return DollarsPerMMBtuFormat.format((double) pnl / volume);
					}
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
				} else if (object instanceof MarketAllocation) {
					final MarketAllocation marketAllocation = (MarketAllocation) object;
					final Integer pnl = getPNLValue(marketAllocation);
					if (pnl != null) {
						return DollarsFormat.format(pnl);
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
				}
				return null;
			}
		};
	}

	public @NonNull ICellRenderer createShippingBOGTotal(final EconsOptions options) {
		return new BaseFormatter() {
			@Override
			public @Nullable String render(final Object object) {
				if (object instanceof CargoAllocation) {
					final CargoAllocation cargoAllocation = (CargoAllocation) object;

					int cost = 0;
					for (final Event event : cargoAllocation.getEvents()) {
						if (event instanceof FuelUsage) {
							final FuelUsage fuelUsage = (FuelUsage) event;
							cost += getFuelCost(fuelUsage, Fuel.NBO, Fuel.FBO);
						}
					}
					return DollarsFormat.format(cost);
				}
				return null;
			}

		};
	}

	public @NonNull ICellRenderer createShippingBunkersTotal(final EconsOptions options) {
		return new BaseFormatter() {
			@Override
			public @Nullable String render(final Object object) {
				if (object instanceof CargoAllocation) {
					final CargoAllocation cargoAllocation = (CargoAllocation) object;

					int cost = 0;
					for (final Event event : cargoAllocation.getEvents()) {
						if (event instanceof FuelUsage) {
							final FuelUsage fuelUsage = (FuelUsage) event;
							cost += getFuelCost(fuelUsage, Fuel.BASE_FUEL, Fuel.PILOT_LIGHT);
						}
					}
					return DollarsFormat.format(cost);
				}
				return null;
			}
		};
	}

	public @NonNull ICellRenderer createShippingPortCosts(final EconsOptions options) {
		return new BaseFormatter() {
			@Override
			public @Nullable String render(final Object object) {
				if (object instanceof CargoAllocation) {
					final CargoAllocation cargoAllocation = (CargoAllocation) object;

					int cost = 0;
					for (final Event event : cargoAllocation.getEvents()) {
						if (event instanceof PortVisit) {
							final PortVisit portVisit = (PortVisit) event;
							cost += portVisit.getPortCost();
						}
					}
					return DollarsFormat.format(cost);
				}
				return null;
			}
		};
	}

	public @NonNull ICellRenderer createShippingCanalCosts(final EconsOptions options) {
		return new BaseFormatter() {
			@Override
			public @Nullable String render(final Object object) {
				if (object instanceof CargoAllocation) {
					final CargoAllocation cargoAllocation = (CargoAllocation) object;

					int cost = 0;
					for (final Event event : cargoAllocation.getEvents()) {
						if (event instanceof Journey) {
							final Journey journey = (Journey) event;
							cost += journey.getToll();
						}
					}
					return DollarsFormat.format(cost);
				}
				return null;

			}
		};
	}

	public @NonNull ICellRenderer createShippingCharterCosts(final EconsOptions options) {
		return new BaseFormatter() {
			@Override
			public @Nullable String render(final Object object) {
				if (object instanceof CargoAllocation) {
					final CargoAllocation cargoAllocation = (CargoAllocation) object;

					int cost = 0;
					for (final Event event : cargoAllocation.getEvents()) {
						cost += event.getCharterCost();
					}
					return DollarsFormat.format(cost);
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
				return null;

			}
		};
	}

	private static Integer getShippingCost(final CargoAllocation cargoAllocation) {

		if (cargoAllocation == null) {
			return null;
		}

		// Bit of a double count here, but need to decide what to add to the model
		int shippingCost = 0;
		int charterCost = 0;
		for (final Event event : cargoAllocation.getEvents()) {

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
		if (cargoAllocation.getSequence().isSpotVessel() || cargoAllocation.getSequence().isTimeCharterVessel()) {
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
