package com.mmxlabs.widgets.schedulechart.draw;

import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatterBuilder;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Display;

import com.mmxlabs.common.Pair;
import com.mmxlabs.widgets.schedulechart.ScheduleTimeScale;
import com.mmxlabs.widgets.schedulechart.TimeScaleView;

public class DrawableScheduleTimeScale<T extends ScheduleTimeScale> extends DrawableElement {

	private final @NonNull T sts;

	private final Color black = Display.getDefault().getSystemColor(SWT.COLOR_BLACK);
	private final Color gray = Display.getDefault().getSystemColor(SWT.COLOR_GRAY);
	private final Color bg = new Color(new RGB(230, 239, 249));
	private final Color white = Display.getDefault().getSystemColor(SWT.COLOR_WHITE);
	
	private Optional<Integer> lockedHeaderY = Optional.empty();
	
	private static final int spacer = 3;
	private static final int headerRowHeight = 18 + spacer;

	public DrawableScheduleTimeScale(@NonNull T sts) {
		this.sts = sts;
	}

	@Override
	protected List<BasicDrawableElement> getBasicDrawableElements(Rectangle bounds, DrawerQueryResolver resolver) {
	List<BasicDrawableElement> res = new ArrayList<>();

		res.addAll(getHeaders().getBasicDrawableElements(resolver));
		res.addAll(getGrid(bounds.y + getTotalHeaderHeight()).getBasicDrawableElements(resolver));

		return res;
	}
	
	public DrawableElement getHeaders() {
		final var grid = new DrawableElement() {
			@Override
			protected List<BasicDrawableElement> getBasicDrawableElements(Rectangle bounds, DrawerQueryResolver resolver) {
				List<BasicDrawableElement> res = new ArrayList<>();
				
				final LocalDateTime startDate = sts.getStartTime();
				final var view = sts.getUnitWidths().zoomLevel().getView();
				final var unitsForViews = switch (view) {
					case TS_VIEW_DAY -> new Pair<>(ChronoUnit.DAYS, ChronoUnit.HOURS);
					case TS_VIEW_WEEK -> new Pair<>(ChronoUnit.WEEKS, ChronoUnit.DAYS);
					case TS_VIEW_MONTH -> new Pair<>(ChronoUnit.MONTHS, ChronoUnit.WEEKS);
					case TS_VIEW_YEAR -> new Pair<>(ChronoUnit.YEARS, ChronoUnit.MONTHS);
					default -> throw new IllegalArgumentException("Unexpected value: " + view);
				};

				int y = lockedHeaderY.orElse(bounds.y);

				Rectangle headerBounds = new Rectangle(bounds.x, y, bounds.width, headerRowHeight);
				getDrawableElemsForHeader(res, headerBounds, unitsForViews.getFirst(), startDate, (date, unit) -> {
					String format = switch (unit) {
					case DAYS -> "";
					case WEEKS -> "MMM d, ''yy";
					case MONTHS -> "MMMM ''yy";
					case YEARS -> "YYYY";
					default -> "";
					};
					return format.isEmpty() ? "" : date.format(new DateTimeFormatterBuilder().appendPattern(format).toFormatter());
				}, getAdjuster(), resolver);

				y += headerRowHeight;
				headerBounds = new Rectangle(bounds.x, y, bounds.width, headerRowHeight);
				getDrawableElemsForHeader(res, headerBounds, unitsForViews.getSecond(), startDate, (date, unit) -> {
					String format = switch (unit) {
					case DAYS -> "EEEEE";
					case WEEKS -> "MMM d";
					case MONTHS -> "MMMM";
					default -> "";
					};
					return format.isEmpty() ? "" : date.format(new DateTimeFormatterBuilder().appendPattern(format).toFormatter());
				}, getAdjuster(), resolver);

				return res;
			}
		};
		grid.setBounds(getBounds());
		return grid;
	}

	
	public DrawableElement getGrid(int startY) {
		final var grid = new DrawableElement() {
			@Override
			protected List<BasicDrawableElement> getBasicDrawableElements(Rectangle bounds, DrawerQueryResolver queryResolver) {
				List<BasicDrawableElement> res = new ArrayList<>();
				final var view = sts.getUnitWidths().zoomLevel().getView();
				Rectangle gridBounds = new Rectangle(bounds.x, startY, bounds.width, bounds.height - startY);
				getDrawableElemsForGrid(res, gridBounds, view == TimeScaleView.TS_VIEW_YEAR ? ChronoUnit.MONTHS : ChronoUnit.DAYS, sts.getStartTime(), getAdjuster());
				return res;
			}
		};
		grid.setBounds(getBounds());
		return grid;
	}

	private void getDrawableElemsForHeader(List<BasicDrawableElement> res, final Rectangle headerBounds, final ChronoUnit unit, final LocalDateTime startDate, FormattedDateProvider f,
			AdjustedDateProvider a, DrawerQueryResolver r) {
		final int y = headerBounds.y;
		final int maxX = headerBounds.x + headerBounds.width;

		LocalDateTime date = a.getAdjustedDate(startDate, unit);
		int x = sts.getXForDateTime(date);

		if (x > headerBounds.x) {
			res.add(BasicDrawableElements.Rectangle.withBounds(headerBounds.x, y, x - headerBounds.x, headerBounds.height).bgColour(bg).create());
			res.add(BasicDrawableElements.Rectangle.withBounds(headerBounds.x, y, x - headerBounds.x, headerBounds.height).strokeColour(gray).create());
		}

		final Optional<Integer> unitWidth = sts.getUnitWidthFromRegularUnit(unit);
		while (x < maxX) {
			LocalDateTime nextDate = date.plus(1, unit);
			final int currX = x;
			int width = unitWidth.orElseGet(() -> sts.getXForDateTime(nextDate) - currX);

			res.add(BasicDrawableElements.Rectangle.withBounds(x, y, width, headerBounds.height).bgColour(bg).create());
			res.add(BasicDrawableElements.Text.from(x, y, f.getDateString(date, unit)).padding(2).textColour(black).create());
			res.add(BasicDrawableElements.Rectangle.withBounds(x, y, width, headerBounds.height).strokeColour(gray).create());

			date = nextDate;
			x += width;
		}

	}

	private void getDrawableElemsForGrid(List<BasicDrawableElement> res, final Rectangle gridBounds, final ChronoUnit unit, final LocalDateTime startDate, AdjustedDateProvider a) {
		LocalDateTime date = a.getAdjustedDate(startDate, unit);
		int x = sts.getXForDateTime(date);
		int y = gridBounds.y;
		int maxX = gridBounds.x + gridBounds.width;
		final Optional<Integer> unitWidth = sts.getUnitWidthFromRegularUnit(unit);

		while (x < maxX) {
			LocalDateTime nextDate = date.plus(1, unit);
			final int currX = x;
			int width = unitWidth.orElseGet(() -> currX - sts.getXForDateTime(nextDate));

			res.add(BasicDrawableElements.Rectangle.withBounds(x, y, width, gridBounds.height).bgColour(unit == ChronoUnit.DAYS && date.getDayOfWeek().ordinal() >= 5 ? bg : null).create());
			res.add(BasicDrawableElements.Rectangle.withBounds(x, y, width, gridBounds.height).strokeColour(gray).create());

			date = nextDate;
			x += width;
		}
	}

	public void setLockedHeaderY(int y) {
		lockedHeaderY = Optional.of(y);
	}
	
	public Optional<Integer> getLockedHeaderY() {
		return lockedHeaderY;
	}
	
	public int getTotalHeaderHeight() {
		return headerRowHeight * 2;
	}
	
	private AdjustedDateProvider getAdjuster() {
		LocalDateTime startDate = sts.getStartTime();
		return (date, unit) -> switch (unit) {
			case WEEKS -> (LocalDateTime) TemporalAdjusters.nextOrSame(DayOfWeek.MONDAY).adjustInto(startDate);
			case MONTHS -> (LocalDateTime) TemporalAdjusters.firstDayOfNextMonth().adjustInto(startDate);
			default -> date;
		};
	}
	
	private static interface FormattedDateProvider {
		String getDateString(LocalDateTime date, ChronoUnit unit);
	}

	private static interface AdjustedDateProvider {
		LocalDateTime getAdjustedDate(LocalDateTime date, ChronoUnit unit);
	}

}