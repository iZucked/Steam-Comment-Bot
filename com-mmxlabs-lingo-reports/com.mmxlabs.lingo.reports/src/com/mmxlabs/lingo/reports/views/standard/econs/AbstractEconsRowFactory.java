/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
package com.mmxlabs.lingo.reports.views.standard.econs;

import java.text.DecimalFormat;
import java.util.List;
import java.util.function.BiFunction;
import java.util.function.Function;

import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.Nullable;
import org.eclipse.jface.viewers.IColorProvider;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Display;

import com.mmxlabs.common.util.TriFunction;
import com.mmxlabs.lingo.reports.views.formatters.Formatters;
import com.mmxlabs.lingo.reports.views.formatters.Formatters.DurationMode;
import com.mmxlabs.models.lng.cargo.VesselEvent;
import com.mmxlabs.models.lng.schedule.CargoAllocation;
import com.mmxlabs.models.lng.schedule.EndEvent;
import com.mmxlabs.models.lng.schedule.Event;
import com.mmxlabs.models.lng.schedule.EventGrouping;
import com.mmxlabs.models.lng.schedule.GeneratedCharterLengthEvent;
import com.mmxlabs.models.lng.schedule.GeneratedCharterOut;
import com.mmxlabs.models.lng.schedule.Idle;
import com.mmxlabs.models.lng.schedule.Journey;
import com.mmxlabs.models.lng.schedule.MarketAllocation;
import com.mmxlabs.models.lng.schedule.OpenSlotAllocation;
import com.mmxlabs.models.lng.schedule.PaperDealAllocation;
import com.mmxlabs.models.lng.schedule.PortVisit;
import com.mmxlabs.models.lng.schedule.Purge;
import com.mmxlabs.models.lng.schedule.SlotAllocation;
import com.mmxlabs.models.lng.schedule.SlotAllocationType;
import com.mmxlabs.models.lng.schedule.SlotVisit;
import com.mmxlabs.models.lng.schedule.StartEvent;
import com.mmxlabs.models.lng.schedule.VesselEventVisit;
import com.mmxlabs.models.ui.tabular.BaseFormatter;
import com.mmxlabs.models.ui.tabular.ICellRenderer;
import com.mmxlabs.models.ui.tabular.IImageProvider;
import com.mmxlabs.rcp.icons.lingo.CommonImages;
import com.mmxlabs.rcp.icons.lingo.CommonImages.IconMode;
import com.mmxlabs.rcp.icons.lingo.CommonImages.IconPaths;

public abstract class AbstractEconsRowFactory implements IEconsRowFactory {
	// Make into abstract parent
	private Image cellImageSteadyArrow;
	private Image cellImageGreenArrowDown;
	private Image cellImageGreenArrowUp;
	private Image cellImageRedArrowDown;
	private Image cellImageRedArrowUp;

	protected final class BasicEconsFormatter<T> extends EconsFormatter {
		private final RowType rowType;
		private final EconsOptions options;
		private final Function<T, String> formatter;
		private final Function<Object, @Nullable T> transformer;
		private final Class<T> type;

		protected BasicEconsFormatter(RowType rowType, EconsOptions options, Function<T, String> formatter, Function<Object, @Nullable T> transformer, Class<T> type) {
			this.rowType = rowType;
			this.options = options;
			this.formatter = formatter;
			this.transformer = transformer;
			this.type = type;
		}

		@Override
		public Image getImage(final Object element) {
			if (element instanceof DeltaPair || element instanceof List<?>) {
				final T t = getRawValue(element);
				if (t instanceof Number value && value.doubleValue() != 0.0) {
					final boolean isNegative = value.doubleValue() < 0.0;
					if (rowType == RowType.COST) {
						return isNegative ? cellImageGreenArrowDown : cellImageRedArrowUp;
					} else {
						return isNegative ? cellImageRedArrowDown : cellImageGreenArrowUp;
					}
				}
			}
			return null;
		}

		@Override
		public @Nullable String render(final Object object) {
			boolean deltaCol = (object instanceof DeltaPair || object instanceof List<?>);
			@Nullable
			final T value = deltaCol ? getAbsValue(object) : getRawValue(object);
			if (value != null) {
				return formatter.apply(value);
			}

			return "";
		}

		public @Nullable T getAbsValue(final Object object) {
			final T value = transformer.apply(object);
			if (value == null) {
				return null;
			}
			if (options.alwaysShowRawValue) {
				return value;
			}
			if (type == Integer.class) {
				return (T) (Integer) Math.abs((Integer) value);
			} else if (type == Long.class) {
				return (T) (Long) Math.abs((Long) value);
			} else if (type == Float.class) {
				return (T) (Float) Math.abs((Float) value);
			} else if (type == Double.class) {
				return (T) (Double) Math.abs((Double) value);
			}
			return value;
		}

		public @Nullable T getRawValue(final Object object) {
			return transformer.apply(object);
		}
	}

	protected class EconsFormatter extends BaseFormatter implements IImageProvider {
		@Override
		public Image getImage(final Object element) {
			return null;
		}
	}

	public static final DecimalFormat DollarsFormat = new DecimalFormat("##,###,###,###");
	public static final DecimalFormat VolumeMMBtuFormat = new DecimalFormat("##,###,###,###");
	public static final DecimalFormat DollarsPerMMBtuFormat = new DecimalFormat("###.###");
	public static final DecimalFormat VolumeM3Format = new DecimalFormat("##,###,###,###");
	public static final DecimalFormat SpeedFormat = new DecimalFormat("##.#");
	public static final DecimalFormat CVFormat = new DecimalFormat("##.#");

	protected AbstractEconsRowFactory() {
		cellImageSteadyArrow = CommonImages.getImage(IconPaths.SteadyArrow, IconMode.Enabled);
		cellImageGreenArrowDown = CommonImages.getImage(IconPaths.GreenArrowDown, IconMode.Enabled);
		cellImageGreenArrowUp = CommonImages.getImage(IconPaths.GreenArrowUp, IconMode.Enabled);
		cellImageRedArrowDown = CommonImages.getImage(IconPaths.RedArrowDown, IconMode.Enabled);
		cellImageRedArrowUp = CommonImages.getImage(IconPaths.RedArrowUp, IconMode.Enabled);
	}

	protected static CargoEconsReportRow createRow(final int order, final @NonNull String name, final boolean includeUnits, final @NonNull String prefixUnit, final String suffixUnit,
			final @NonNull ICellRenderer renderer) {
		return createRow(order, name, true, prefixUnit, suffixUnit, renderer, null);
	}

	protected static CargoEconsReportRow createRow(final int order, final @NonNull String name, final boolean includeUnits, final @NonNull String prefixUnit, final String suffixUnit,
			final @NonNull ICellRenderer formatter, @Nullable final IColorProvider colourProvider) {
		final CargoEconsReportRow row = new CargoEconsReportRow();
		row.order = order;
		row.name = name;
		row.includeUnits = includeUnits;
		row.prefixUnit = prefixUnit;
		row.suffixUnit = suffixUnit;
		row.formatter = formatter;
		row.colourProvider = colourProvider;

		return row;
	}

	protected <T> Function<Object, @Nullable T> createMappingFunction(final Class<T> type, final Function<Object, T> helper) {
		return object -> {
			if (object instanceof CargoAllocation //
					|| object instanceof OpenSlotAllocation //
					|| object instanceof GeneratedCharterLengthEvent //
					|| object instanceof StartEvent //
					|| object instanceof EndEvent //
					|| object instanceof VesselEvent //
					|| object instanceof GeneratedCharterOut //
					|| object instanceof VesselEventVisit //
					|| object instanceof Purge //
					|| object instanceof MarketAllocation //
					|| object instanceof PaperDealAllocation //
			) {
				return helper.apply(object);
			} else if (object instanceof DeltaPair) {
				return getFromDeltaPair(type, helper, object);
			} else if (object instanceof List<?>) {
				return getFromDeltaPairPairList(type, helper, object);
			}
			return null;
		};
	}

	protected <T> @NonNull ICellRenderer createBasicFormatter(final EconsOptions options, final RowType rowType, final Class<T> type, final Function<T, String> formatter,
			final Function<Object, @Nullable T> transformer) {
		return new BasicEconsFormatter<T>(rowType, options, formatter, transformer, type);
	}

	protected @NonNull ICellRenderer createIntegerDaysFormatter(final EconsOptions options, final RowType rowType, final Function<Object, @Nullable Integer> transformer) {
		return createBasicFormatter(options, rowType, Integer.class, new Function<Integer, String>() {
			@Override
			public String apply(Integer days) {
				return Formatters.formatAsDays(DurationMode.DAYS_HOURS_HUMAN, days * 24L);
			}
		}, transformer);
	}

	protected @NonNull ICellRenderer createIntegerDaysFromHoursFormatter(final EconsOptions options, final RowType rowType, final Function<Object, @Nullable Integer> transformer) {
		return createBasicFormatter(options, rowType, Integer.class, new Function<Integer, String>() {
			@Override
			public String apply(Integer hours) {
				return Formatters.formatAsDays(DurationMode.DAYS_HOURS_HUMAN, hours);
			}
		}, transformer);
	}

	protected @NonNull ICellRenderer createDoubleDaysFormatter(final EconsOptions options, final RowType rowType, final Function<Object, @Nullable Double> transformer) {
		return createBasicFormatter(options, rowType, Double.class, new Function<Double, String>() {
			@Override
			public String apply(Double days) {
				return Formatters.formatAsDays(DurationMode.DAYS_HOURS_HUMAN, days * 24);
			}
		}, transformer);
	}

	protected <T, U> @Nullable T getFromDeltaPairPairList(final Class<T> type, final Function<U, T> f, final Object object) {
		final List<DeltaPair> deltaPairs = (List<DeltaPair>) object;

		if (type == Integer.class) {
			int acc = 0;
			for (final DeltaPair deltaPair : deltaPairs) {
				Integer v = (Integer) getFromDeltaPair(type, f, deltaPair);
				if (v != null) {
					acc += v;
				}
			}
			return type.cast(acc);
		} else if (type == Double.class) {
			double acc = 0;
			for (final DeltaPair deltaPair : deltaPairs) {
				Double v = (Double) getFromDeltaPair(type, f, deltaPair);
				if (v != null) {
					acc += v;
				}
			}
			return type.cast(acc);
		} else if (type == Long.class) {
			long acc = 0;
			for (final DeltaPair deltaPair : deltaPairs) {
				Long v = (Long) getFromDeltaPair(type, f, deltaPair);
				if (v != null) {
					acc += v;
				}
			}
			return type.cast(acc);
		}

		return null;
	}

	protected <T, U> T getFromDeltaPair(final Class<T> type, final Function<U, @Nullable T> f, final Object object) {
		Object first = null;
		Object second = null;

		if (object instanceof DeltaPair deltaPair) {
			first = deltaPair.first();
			second = deltaPair.second();
		}

		T valueFirst = f.apply((U) first);

		// Negate value first, incase the second case is not present.
		if (valueFirst != null) {
			if (valueFirst instanceof Integer) {
				valueFirst = type.cast(-(int) valueFirst);
			} else if (valueFirst instanceof Double) {
				valueFirst = type.cast(-(double) valueFirst);
			} else if (valueFirst instanceof Long) {
				valueFirst = type.cast(-(long) valueFirst);
			}
		}

		if (second != null) {
			final T valueSecond = f.apply((U) second);
			if (valueSecond != null) {
				if (valueFirst == null) {
					return valueSecond;
				}
				if (valueSecond instanceof Integer) {
					return type.cast(((int) valueSecond + (int) valueFirst));
				} else if (valueSecond instanceof Double) {
					return type.cast(((double) valueSecond + (double) valueFirst));
				} else if (valueSecond instanceof Long) {
					return type.cast(((long) valueSecond + (long) valueFirst));
				}
			}
		}

		return valueFirst;
	}

	protected final IColorProvider greyColourProvider = new IColorProvider() {

		@Override
		public Color getForeground(final Object element) {

			return Display.getDefault().getSystemColor(SWT.COLOR_GRAY);

		}

		@Override
		public Color getBackground(final Object element) {
			return null;
		}
	};

	public @NonNull ICellRenderer createEmptyFormatter() {
		return new BaseFormatter() {
			@Override
			public @Nullable String render(final Object object) {
				return null;
			}
		};
	}

	protected <T> Function<Object, @Nullable T> createFirstPurchaseTransformer(final Class<T> cls, final Function<SlotAllocation, T> func) {
		return createMappingFunction(cls, object -> {
			try {
				if (object instanceof CargoAllocation cargoAllocation) {
					for (final SlotAllocation slotAllocation : cargoAllocation.getSlotAllocations()) {
						if (slotAllocation.getSlotAllocationType() == SlotAllocationType.PURCHASE) {
							return func.apply(slotAllocation);
						}
					}
				}
				if (object instanceof SlotAllocation slotAllocation) {
					if (slotAllocation.getSlotAllocationType() == SlotAllocationType.PURCHASE) {
						return func.apply(slotAllocation);
					}
				}
			} catch (final NullPointerException e) {
				// Ignore NPE
			}
			return null;
		});
	}

	protected ICellRenderer createPaperDealAllocationFormatter(final Function<PaperDealAllocation, String> func) {
		return new BaseFormatter() {
			@Override
			public @Nullable String render(final Object object) {

				try {
					if (object instanceof PaperDealAllocation paperDealAllocation) {
						return func.apply(paperDealAllocation);
					}
				} catch (final NullPointerException e) {
					// Ignore NPE
				}
				return null;
			}
		};
	}

	protected ICellRenderer createFirstPurchaseFormatter(final Function<SlotAllocation, String> func) {
		return new BaseFormatter() {
			@Override
			public @Nullable String render(final Object object) {

				try {
					if (object instanceof CargoAllocation cargoAllocation) {
						for (final SlotAllocation slotAllocation : cargoAllocation.getSlotAllocations()) {
							if (slotAllocation.getSlotAllocationType() == SlotAllocationType.PURCHASE) {
								return func.apply(slotAllocation);
							}
						}
					}
					if (object instanceof SlotAllocation slotAllocation) {
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

	protected <T> Function<Object, @Nullable T> createSaleTransformer(int n, final Class<T> cls, final Function<SlotAllocation, T> func) {
		return createMappingFunction(cls, object -> {
			try {
				if (object instanceof CargoAllocation cargoAllocation) {
					int t = 0;
					for (final SlotAllocation slotAllocation : cargoAllocation.getSlotAllocations()) {
						if (slotAllocation.getSlotAllocationType() == SlotAllocationType.SALE) {
							t++;
							if (t == n) {
								return func.apply(slotAllocation);
							}
						}
					}
				}
				if (object instanceof SlotAllocation slotAllocation) {
					int t = 0;

					if (slotAllocation.getSlotAllocationType() == SlotAllocationType.SALE) {
						t++;
						if (t == n) {
							return func.apply(slotAllocation);
						}
					}
				}
			} catch (final NullPointerException e) {
				// Ignore NPE
			}
			return null;
		});
	}

	protected @NonNull ICellRenderer createSaleAllocationFormatter(int n, final Function<SlotAllocation, String> func) {

		return new BaseFormatter() {
			@Override
			public @Nullable String render(final Object object) {

				try {
					if (object instanceof CargoAllocation cargoAllocation) {
						int t = 0;

						for (final SlotAllocation slotAllocation : cargoAllocation.getSlotAllocations()) {
							if (slotAllocation.getSlotAllocationType() == SlotAllocationType.SALE) {
								t++;
								if (t == n) {
									return func.apply(slotAllocation);
								}
							}
						}
					}
					if (object instanceof SlotAllocation slotAllocation) {
						int t = 0;

						if (slotAllocation.getSlotAllocationType() == SlotAllocationType.SALE) {
							t++;
							if (t == n) {
								return func.apply(slotAllocation);
							}
						}
					}
				} catch (final NullPointerException e) {
					// Ignore NPE
				}
				return null;
			}
		};
	}

	protected <T> Function<Object, @Nullable T> createFullLegTransformer2(final Class<T> resultType, boolean laden, final int index,
			final TriFunction<@Nullable SlotVisit, @Nullable Journey, @Nullable Idle, @Nullable T> func) {
		return createMappingFunction(resultType, object -> {
			try {
				SlotVisit slotVisit = null;
				Journey journey = null;
				Idle idle = null;
				if (object instanceof CargoAllocation cargoAllocation) {
					int legNumber = -1;
					boolean isLaden = true;
					for (final Event event : cargoAllocation.getEvents()) {
						if (event instanceof PortVisit) {
							if (isLaden == laden && legNumber == index) {
								return func.apply(slotVisit, journey, idle);
							}
							journey = null;
							idle = null;
							slotVisit = null;
							++legNumber;
							if (isLaden == laden && legNumber > index) {
								return null;
							}
						}
						if (event instanceof SlotVisit sv) {
							slotVisit = sv;
						}
						if (event instanceof Journey j) {
							journey = j;
							// Laden to ballast switch
							if (isLaden && !journey.isLaden()) {
								isLaden = journey.isLaden();
								legNumber = 0;
							}

						}
						if (event instanceof Idle i) {
							idle = i;
						}
					}
					if (isLaden == laden && legNumber == index) {
						return func.apply(slotVisit, journey, idle);
					}

				} else if (object instanceof EventGrouping eg) {
					for (final Event event : eg.getEvents()) {
						if (event instanceof SlotVisit sv) {
							slotVisit = sv;
						}
						if (event instanceof Journey j) {
							journey = j;
						}
						if (event instanceof Idle i) {
							idle = i;
						}
					}
					return func.apply(slotVisit, journey, idle);
				}
			} catch (final NullPointerException e) {
				// ignore npe's
			}
			return null;
		});
	}

	protected <T> Function<Object, @Nullable T> createFullLegTransformer(final Class<T> resultType, final int index, final BiFunction<Journey, Idle, T> func) {
		return createMappingFunction(resultType, object -> {
			try {
				if (object instanceof CargoAllocation cargoAllocation) {
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
						if (event instanceof Journey j) {
							journey = j;
						}
						if (event instanceof Idle i) {
							idle = i;
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
		});
	}

}
