/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2022
 * All rights reserved.
 */
package com.mmxlabs.lingo.reports.views.standard.econs;

import java.text.DecimalFormat;
import java.util.List;
import java.util.function.BiFunction;
import java.util.function.Function;

import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.Nullable;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.viewers.IColorProvider;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Display;

import com.mmxlabs.common.util.TriFunction;
import com.mmxlabs.lingo.reports.internal.Activator;
import com.mmxlabs.lingo.reports.views.formatters.Formatters;
import com.mmxlabs.lingo.reports.views.formatters.Formatters.DurationMode;
import com.mmxlabs.models.lng.cargo.VesselEvent;
import com.mmxlabs.models.lng.schedule.CargoAllocation;
import com.mmxlabs.models.lng.schedule.CharterLengthEvent;
import com.mmxlabs.models.lng.schedule.EndEvent;
import com.mmxlabs.models.lng.schedule.Event;
import com.mmxlabs.models.lng.schedule.EventGrouping;
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
				if (t instanceof Number) {
					final Number value = (Number) t;
					if (value.doubleValue() != 0.0) {
						final boolean isNegative = value.doubleValue() < 0.0;
						if (rowType == RowType.COST) {
							return isNegative ? cellImageGreenArrowDown : cellImageRedArrowUp;
						} else {
							return isNegative ? cellImageRedArrowDown : cellImageGreenArrowUp;
						}
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

	protected Image createImage(String path) {
		final ImageDescriptor imageDescriptor = Activator.Implementation.getImageDescriptor(path);
		return imageDescriptor.createImage();
	}

	@Override
	protected void finalize() throws Throwable {
		super.finalize();
		dispose();
	}

	public void dispose() {

		if (cellImageSteadyArrow != null) {
			cellImageSteadyArrow.dispose();
			cellImageSteadyArrow = null;
		}

		if (cellImageGreenArrowDown != null) {
			cellImageGreenArrowDown.dispose();
			cellImageGreenArrowDown = null;
		}

		if (cellImageGreenArrowUp != null) {
			cellImageGreenArrowUp.dispose();
			cellImageGreenArrowUp = null;
		}

		if (cellImageRedArrowDown != null) {
			cellImageRedArrowDown.dispose();
			cellImageRedArrowDown = null;
		}

		if (cellImageRedArrowUp != null) {
			cellImageRedArrowUp.dispose();
			cellImageRedArrowUp = null;
		}
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
			if (object instanceof CargoAllocation) {
				final CargoAllocation cargoAllocation = (CargoAllocation) object;
				return helper.apply(cargoAllocation);
			} else if (object instanceof OpenSlotAllocation) {
				final OpenSlotAllocation openSlotAllocation = (OpenSlotAllocation) object;
				return helper.apply(openSlotAllocation);
			} else if (object instanceof CharterLengthEvent) {
				final CharterLengthEvent charterLength = (CharterLengthEvent) object;
				return helper.apply(charterLength);
			} else if (object instanceof StartEvent) {
				final StartEvent startEvent = (StartEvent) object;
				return helper.apply(startEvent);
			} else if (object instanceof EndEvent) {
				final EndEvent endEvent = (EndEvent) object;
				return helper.apply(endEvent);
			} else if (object instanceof VesselEvent) {
				final VesselEvent vesselEvent = (VesselEvent) object;
				return helper.apply(vesselEvent);
			} else if (object instanceof GeneratedCharterOut) {
				final GeneratedCharterOut generatedCharterOut = (GeneratedCharterOut) object;
				return helper.apply(generatedCharterOut);
			} else if (object instanceof VesselEventVisit) {
				final VesselEventVisit eventVisit = (VesselEventVisit) object;
				return helper.apply(eventVisit);
			} else if (object instanceof Purge) {
				final Purge purge = (Purge) object;
				return helper.apply(purge);
			} else if (object instanceof MarketAllocation) {
				final MarketAllocation marketAllocation = (MarketAllocation) object;
				return helper.apply(marketAllocation);
			} else if (object instanceof DeltaPair) {
				return getFromDeltaPair(type, helper, object);
			} else if (object instanceof List<?>) {
				return getFromDeltaPairPairList(type, helper, object);
			} else if (object instanceof PaperDealAllocation) {
				final PaperDealAllocation paperDealAllocation = (PaperDealAllocation) object;
				return helper.apply(paperDealAllocation);
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

	protected <T, U> T getFromDeltaPairPairList(final Class<T> type, final Function<U, T> f, final Object object) {
		final List<DeltaPair> deltaPairs = (List<DeltaPair>) object;

		if (type == Integer.class) {
			int acc = 0;
			for (final DeltaPair deltaPair : deltaPairs) {
				Integer v = (Integer) getFromDeltaPair(type, f, deltaPair);
				if (v != null) {
					acc += (int) v;
				}
			}
			return type.cast(acc);
		} else if (type == Double.class) {
			double acc = 0;
			for (final DeltaPair deltaPair : deltaPairs) {
				Double v = (Double) getFromDeltaPair(type, f, deltaPair);
				if (v != null) {
					acc += (double) v;
				}
			}
			return type.cast(acc);
		} else if (type == Long.class) {
			long acc = 0;
			for (final DeltaPair deltaPair : deltaPairs) {
				Long v = (Long) getFromDeltaPair(type, f, deltaPair);
				if (v != null) {
					acc += (long) v;
				}
			}
			return type.cast(acc);
		}

		return null;
	}

	protected <T, U> T getFromDeltaPair(final Class<T> type, final Function<U, @Nullable T> f, final Object object) {
		Object first = null;
		Object second = null;

		if (object instanceof DeltaPair) {
			final DeltaPair deltaPair = (DeltaPair) object;
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
				} else {
					int ii = 0;
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
		});
	}

	protected ICellRenderer createPaperDealAllocationFormatter(final Function<PaperDealAllocation, String> func) {
		return new BaseFormatter() {
			@Override
			public @Nullable String render(final Object object) {

				try {
					if (object instanceof PaperDealAllocation) {
						final PaperDealAllocation paperDealAllocation = (PaperDealAllocation) object;
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

	protected <T> Function<Object, @Nullable T> createFirstSaleTransformer(final Class<T> cls, final Function<SlotAllocation, T> func) {
		return createMappingFunction(cls, object -> {
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
		});
	}

	protected @NonNull ICellRenderer createFirstSaleAllocationFormatter(final Function<SlotAllocation, String> func) {

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

	protected <T> Function<Object, @Nullable T> createFullLegTransformer2(final Class<T> resultType, final int index,
			final TriFunction<@Nullable SlotVisit, @Nullable Journey, @Nullable Idle, @Nullable T> func) {
		return createMappingFunction(resultType, object -> {
			try {
				SlotVisit slotVisit = null;
				Journey journey = null;
				Idle idle = null;
				if (object instanceof CargoAllocation) {
					final CargoAllocation cargoAllocation = (CargoAllocation) object;
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
				if (object instanceof EventGrouping) {
					final EventGrouping eg = (EventGrouping) object;
					for (final Event event : eg.getEvents()) {
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
		});
	}

}
