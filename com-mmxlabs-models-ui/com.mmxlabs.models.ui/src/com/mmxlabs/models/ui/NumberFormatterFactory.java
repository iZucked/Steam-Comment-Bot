/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2020
 * All rights reserved.
 */
package com.mmxlabs.models.ui;

import java.util.function.Supplier;

import org.eclipse.jdt.annotation.Nullable;
import org.eclipse.nebula.widgets.formattedtext.DoubleFormatter;
import org.eclipse.nebula.widgets.formattedtext.FloatFormatter;
import org.eclipse.nebula.widgets.formattedtext.IntegerFormatter;
import org.eclipse.nebula.widgets.formattedtext.LongFormatter;

import com.google.common.base.Objects;

/**
 * TODO look at nebula formattedtext
 * 
 * @author hinton
 * 
 */
public class NumberFormatterFactory {

	public static class ExtendedIntegerFormatter extends IntegerFormatter {

		private final @Nullable Supplier<String> overrideStringSupplier;

		public ExtendedIntegerFormatter() {
			super();
			overrideStringSupplier = null;
		}

		public ExtendedIntegerFormatter(final String format) {
			super(format);
			overrideStringSupplier = null;
		}

		public ExtendedIntegerFormatter(@Nullable final Supplier<String> overrideStringSupplier) {
			super();
			this.overrideStringSupplier = overrideStringSupplier;
		}

		public ExtendedIntegerFormatter(final String format, @Nullable final Supplier<String> overrideStringSupplier) {
			super(format);
			this.overrideStringSupplier = overrideStringSupplier;
		}

		@Override
		protected int format(final int p) {
			// Custom code: ignores the min value format specifier in the format string.
			if (editValue.length() == 0) {
				return p;
			}
			return super.format(p);

		};

		@Override
		public Object getValue() {
			// Custom code: Allow zero length string regardless of format string
			if (editValue.length() == 0) {
				return null;
			}
			return super.getValue();
		}

		@Override
		public String getDisplayString() {

			// Custom code: Delegate to override provider
			if (editValue.length() == 0) {
				if (overrideStringSupplier != null) {
					final String displayString = overrideStringSupplier.get();
					if (displayString != null) {
						return displayString;
					}
				}
			}
			// Custom code: Show infinity
			if (Objects.equal(Integer.MAX_VALUE, getValue())) {
				return "-";
			}
			return super.getDisplayString();
		};
	}

	public static class ExtendedLongFormatter extends LongFormatter {

		private final @Nullable Supplier<String> overrideStringSupplier;

		public ExtendedLongFormatter() {
			super();
			overrideStringSupplier = null;
		}

		public ExtendedLongFormatter(final String format) {
			super(format);
			overrideStringSupplier = null;
		}

		public ExtendedLongFormatter(@Nullable final Supplier<String> overrideStringSupplier) {
			super();
			this.overrideStringSupplier = overrideStringSupplier;
		}

		public ExtendedLongFormatter(final String format, @Nullable final Supplier<String> overrideStringSupplier) {
			super(format);
			this.overrideStringSupplier = overrideStringSupplier;
		}

		@Override
		protected int format(final int p) {
			// Custom code: ignores the min value format specifier in the format string.
			if (editValue.length() == 0) {
				return p;
			}
			return super.format(p);

		};

		@Override
		public Object getValue() {
			// Custom code: Allow zero length string regardless of format string
			if (editValue.length() == 0) {
				return null;
			}
			return super.getValue();
		}

		@Override
		public String getDisplayString() {

			// Custom code: Delegate to override provider
			if (editValue.length() == 0) {
				if (overrideStringSupplier != null) {
					final String displayString = overrideStringSupplier.get();
					if (displayString != null) {
						return displayString;
					}
				}
			}
			// Custom code: Show infinity
			if (Objects.equal(Long.MAX_VALUE, getValue())) {
				return "-";
			}
			return super.getDisplayString();
		};
	}

	public static class ExtendedFloatFormatter extends FloatFormatter {

		private final @Nullable Supplier<String> overrideStringSupplier;

		public ExtendedFloatFormatter() {
			super();
			overrideStringSupplier = null;
		}

		public ExtendedFloatFormatter(final String format) {
			super(format);
			overrideStringSupplier = null;
		}

		public ExtendedFloatFormatter(@Nullable final Supplier<String> overrideStringSupplier) {
			super();
			this.overrideStringSupplier = overrideStringSupplier;
		}

		public ExtendedFloatFormatter(final String format, @Nullable final Supplier<String> overrideStringSupplier) {
			super(format);
			this.overrideStringSupplier = overrideStringSupplier;
		}

		@Override
		protected int format(final int p) {
			// Custom code: ignores the min value format specifier in the format string.
			if (editValue.length() == 0) {
				return p;
			}
			return super.format(p);

		};

		@Override
		public Object getValue() {
			// Custom code: Allow zero length string regardless of format string
			if (editValue.length() == 0) {
				return null;
			}
			return super.getValue();
		}

		@Override
		public String getDisplayString() {

			// Custom code: Delegate to override provider
			if (editValue.length() == 0) {
				if (overrideStringSupplier != null) {
					final String displayString = overrideStringSupplier.get();
					if (displayString != null) {
						return displayString;
					}
				}
			}
			// Custom code: Show infinity
			if (Objects.equal(Float.MAX_VALUE, getValue())) {
				return "-";
			}
			return super.getDisplayString();
		};
	}

	public static class ExtendedDoubleFormatter extends DoubleFormatter {

		private final @Nullable Supplier<String> overrideStringSupplier;

		public ExtendedDoubleFormatter() {
			super();
			overrideStringSupplier = null;
		}

		public ExtendedDoubleFormatter(final String format) {
			super(format);
			overrideStringSupplier = null;
		}

		public ExtendedDoubleFormatter(@Nullable final Supplier<String> overrideStringSupplier) {
			super();
			this.overrideStringSupplier = overrideStringSupplier;
		}

		public ExtendedDoubleFormatter(final String format, @Nullable final Supplier<String> overrideStringSupplier) {
			super(format);
			this.overrideStringSupplier = overrideStringSupplier;
		}

		@Override
		protected int format(final int p) {
			// Custom code: ignores the min value format specifier in the format string.
			if (editValue.length() == 0) {
				return p;
			}
			return super.format(p);

		};

		@Override
		public Object getValue() {
			// Custom code: Allow zero length string regardless of format string
			if (editValue.length() == 0) {
				return null;
			}
			return super.getValue();
		}

		@Override
		public String getDisplayString() {

			// Custom code: Delegate to override provider
			if (editValue.length() == 0) {
				if (overrideStringSupplier != null) {
					final String displayString = overrideStringSupplier.get();
					if (displayString != null) {
						return displayString;
					}
				}
			}
			// Custom code: Show infinity
			if (Objects.equal(Double.MAX_VALUE, getValue())) {
				return "-";
			}
			return super.getDisplayString();
		};
	}
}
