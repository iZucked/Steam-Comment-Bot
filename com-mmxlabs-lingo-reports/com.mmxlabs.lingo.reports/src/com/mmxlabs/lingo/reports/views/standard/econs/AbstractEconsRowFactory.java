/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2019
 * All rights reserved.
 */
package com.mmxlabs.lingo.reports.views.standard.econs;

import java.text.DecimalFormat;
import java.util.List;
import java.util.function.Function;

import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.Nullable;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.viewers.IColorProvider;
import org.eclipse.swt.graphics.Image;

import com.mmxlabs.lingo.reports.internal.Activator;
import com.mmxlabs.models.lng.schedule.CargoAllocation;
import com.mmxlabs.models.lng.schedule.CharterLengthEvent;
import com.mmxlabs.models.lng.schedule.MarketAllocation;
import com.mmxlabs.models.lng.schedule.Purge;
import com.mmxlabs.models.lng.schedule.VesselEventVisit;
import com.mmxlabs.models.ui.tabular.BaseFormatter;
import com.mmxlabs.models.ui.tabular.ICellRenderer;
import com.mmxlabs.models.ui.tabular.IImageProvider;

public abstract class AbstractEconsRowFactory implements IEconsRowFactory {
	// Make into abstract parent
	private Image cellImageSteadyArrow;
	private Image cellImageGreenArrowDown;
	private Image cellImageGreenArrowUp;
	private Image cellImageRedArrowDown;
	private Image cellImageRedArrowUp;

	protected class EconsFormatter extends BaseFormatter implements IImageProvider {
		@Override
		public Image getImage(final Object element) {
			return null;
		}
	}

	public static final DecimalFormat DollarsFormat = new DecimalFormat("##,###,###,###");
	public static final DecimalFormat VolumeMMBtuFormat = new DecimalFormat("##,###,###,###");
	public static final DecimalFormat DollarsPerMMBtuFormat = new DecimalFormat("###.###");
	public static final DecimalFormat DaysFormat = new DecimalFormat("##");

	protected AbstractEconsRowFactory() {
		cellImageSteadyArrow = createImage("icons/steady_arrow.png");
		cellImageGreenArrowDown = createImage("icons/green_arrow_down.png");
		cellImageGreenArrowUp = createImage("icons/green_arrow_up.png");
		cellImageRedArrowDown = createImage("icons/red_arrow_down.png");
		cellImageRedArrowUp = createImage("icons/red_arrow_up.png");

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
			final boolean isCost, final @NonNull ICellRenderer renderer) {
		return createRow(order, name, true, prefixUnit, suffixUnit, isCost, renderer, null);
	}

	protected static CargoEconsReportRow createRow(final int order, final @NonNull String name, final boolean includeUnits, final @NonNull String prefixUnit, final String suffixUnit,
			final boolean isCost, final @NonNull ICellRenderer formatter, @Nullable final IColorProvider colourProvider) {
		final CargoEconsReportRow row = new CargoEconsReportRow();
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

	protected <T> Function<Object, @Nullable T> createMappingFunction(final Class<T> type, final Function<Object, T> helper) {
		return object -> {
			if (object instanceof CargoAllocation) {
				final CargoAllocation cargoAllocation = (CargoAllocation) object;
				return helper.apply(cargoAllocation);
			} else if (object instanceof CharterLengthEvent) {
				final CharterLengthEvent charterLength = (CharterLengthEvent) object;
				return helper.apply(charterLength);
			} else if (object instanceof VesselEventVisit) {
				final VesselEventVisit eventVisit = (VesselEventVisit) object;
				return helper.apply(eventVisit);
			} else if (object instanceof Purge) {
				final Purge purge = (Purge) object;
				return helper.apply(purge);
			} else if (object instanceof MarketAllocation) {
				final MarketAllocation marketAllocation = (MarketAllocation) object;
				return helper.apply(marketAllocation);
			} else if (object instanceof CargoAllocationPair) {
				return getFromCargoAllocationPair(type, helper, object);
			} else if (object instanceof List<?>) {
				return getFromCargoAllocationPairList(type, helper, object);
			}
			return null;
		};
	}

	protected <T> @NonNull ICellRenderer createBasicFormatter(final EconsOptions options, final boolean isCost, final Class<T> type, final Function<T, String> formatter,
			final Function<Object, @Nullable T> transformer) {
		return new EconsFormatter() {
			@Override
			public Image getImage(final Object element) {
				if (element instanceof DeltaPair || element instanceof List<?>) {
					final T t = getRawValue(element);
					if (t instanceof Number) {
						final Number value = (Number) t;
						if (value.doubleValue() != 0.0) {
							final boolean isNegative = value.doubleValue() < 0.0;
							if (isCost) {
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

		};
	}

	protected <T, U> T getFromCargoAllocationPairList(final Class<T> type, final Function<U, T> f, final Object object) {
		final List<DeltaPair> cargoAllocations = (List<DeltaPair>) object;

		if (type == Integer.class) {
			int acc = 0;
			for (final DeltaPair cargoAllocation : cargoAllocations) {
				Integer v = (Integer) getFromCargoAllocationPair(type, f, cargoAllocation);
				if (v != null) {
					acc += (int) v;
				}
			}
			return type.cast(acc);
		} else if (type == Double.class) {
			double acc = 0;
			for (final DeltaPair cargoAllocation : cargoAllocations) {
				Double v = (Double) getFromCargoAllocationPair(type, f, cargoAllocation);
				if (v != null) {
					acc += (double) v;
				}
			}
			return type.cast(acc);
		} else if (type == Long.class) {
			long acc = 0;
			for (final DeltaPair cargoAllocation : cargoAllocations) {
				Long v = (Long) getFromCargoAllocationPair(type, f, cargoAllocation);
				if (v != null) {
					acc += (long) v;
				}
			}
			return type.cast(acc);
		}

		return null;
	}

	protected <T, U> T getFromCargoAllocationPair(final Class<T> type, final Function<U, @Nullable T> f, final Object object) {
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

}
