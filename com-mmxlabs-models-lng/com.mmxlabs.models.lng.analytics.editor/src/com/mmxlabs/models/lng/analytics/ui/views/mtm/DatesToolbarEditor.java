/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2020
 * All rights reserved.
 */
package com.mmxlabs.models.lng.analytics.ui.views.mtm;

import java.time.LocalDate;

import org.eclipse.jdt.annotation.Nullable;
import org.eclipse.jface.action.ControlContribution;
import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.jface.layout.GridLayoutFactory;
import org.eclipse.nebula.widgets.formattedtext.FormattedText;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;

import com.mmxlabs.models.ui.date.LocalDateTextFormatter;

/**
 * Based on PromptToolbarEditor
 * @author FM
 *
 */
public class DatesToolbarEditor extends ControlContribution {
	
	public DatesToolbarEditor(String id, ModifyListener listener) {
		super(id);
		this.listener = listener;
		this.extraListener = listener;
	}
	
	public DatesToolbarEditor(String id, ModifyListener startListener, ModifyListener endListener) {
		super(id);
		this.listener = startListener;
		this.extraListener = endListener;
	}

	private LocalDateTextFormatter startDateFormatter = new LocalDateTextFormatter(true);
	private LocalDateTextFormatter endDateFormatter = new LocalDateTextFormatter(true);
	private FormattedText startDateText;
	private FormattedText endDateText;
	private LocalDate startDate = LocalDate.now();
	private LocalDate endDate = LocalDate.now().plusMonths(3);
	private final ModifyListener listener;
	private final ModifyListener extraListener;
	
	public @Nullable LocalDate getStartDate() {
		LocalDate result = null;
		if (startDateText.getValue() instanceof LocalDate && startDateText.isValid()) {
			result = (LocalDate) startDateText.getValue();
			startDate = result;
		}
		return result;
	}
	
	public @Nullable LocalDate getEndDate() {
		LocalDate result = null;
		if (endDateText.getValue() instanceof LocalDate && endDateText.isValid()) {
			result = (LocalDate) endDateText.getValue();
			endDate = result;
		}
		return result;
	}
	
	public void setStartDate(final LocalDate date) {
		startDate = date;
		if (startDateText != null) {
			startDateText.setValue(startDate);
		}
	}
	
	public void setEndDate(final LocalDate date) {
		endDate = date;
		if (endDateText != null) {
			endDateText.setValue(endDate);
		}
	}

	private boolean locked = false;

	@Override
	protected Control createControl(final Composite ppparent) {

		int minHeight = 40;
		final Composite pparent = new Composite(ppparent, SWT.NONE) {
			@Override
			protected void checkSubclass() {
			}

			@Override
			public Point getSize() {
				Point p = super.getSize();
				return new Point(p.x, Math.max(minHeight, p.y));
			}

			@Override
			public void setSize(int width, int height) {
				super.setSize(width, Math.max(minHeight, height));
			}

			@Override
			public Point computeSize(int wHint, int hHint) {
				Point p = super.computeSize(wHint, hHint);
				return new Point(p.x, Math.max(minHeight, p.y));
			}

			@Override
			public Point computeSize(int wHint, int hHint, boolean b) {
				Point p = super.computeSize(wHint, hHint, b);
				return new Point(p.x, Math.max(minHeight, p.y));
			};
		};
		pparent.setLayout(GridLayoutFactory.fillDefaults().numColumns(10).equalWidth(false).spacing(3, 0).margins(0, 7).create());
		
		{
			final Label lbl = new Label(pparent, SWT.NONE);
			lbl.setText("Generate from:");
			lbl.setLayoutData(GridDataFactory.swtDefaults().align(SWT.CENTER, SWT.CENTER).minSize(1000, -1).create());

			startDateText = createStartDateEditorControl(pparent);

			final Label lbl2 = new Label(pparent, SWT.NONE);
			lbl2.setText(" to ");
			lbl2.setLayoutData(GridDataFactory.swtDefaults().align(SWT.CENTER, SWT.CENTER).create());

			endDateText = createEndDateEditorControl(pparent);
		}
		return pparent;
	}

	public boolean isLocked() {
		return locked;
	}

	public void setLocked(final boolean locked) {
		this.locked = locked;
		startDateText.getControl().setEnabled(!locked);
		endDateText.getControl().setEnabled(!locked);
	}

	protected FormattedText createStartDateEditorControl(final Composite parent) {
		startDateText = new FormattedText(parent);
		startDateText.setFormatter(startDateFormatter);
		startDateText.getControl().setLayoutData(GridDataFactory.swtDefaults().minSize(90, -1).hint(90, 20).create());
		startDateText.setValue(startDate);
		startDateText.getControl().addModifyListener(listener);
		return startDateText;
	}
	
	protected FormattedText createEndDateEditorControl(final Composite parent) {
		endDateText = new FormattedText(parent);
		endDateText.setFormatter(endDateFormatter);
		endDateText.getControl().setLayoutData(GridDataFactory.swtDefaults().minSize(90, -1).hint(90, 20).create());
		endDateText.setValue(endDate);
		endDateText.getControl().addModifyListener(extraListener);
		return endDateText;
	}
}