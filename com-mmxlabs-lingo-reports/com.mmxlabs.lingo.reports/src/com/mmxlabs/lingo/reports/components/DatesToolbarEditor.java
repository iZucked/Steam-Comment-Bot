/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
package com.mmxlabs.lingo.reports.components;

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

	protected LocalDateTextFormatter startDateFormatter = new LocalDateTextFormatter(true);
	protected LocalDateTextFormatter endDateFormatter = new LocalDateTextFormatter(true);
	protected FormattedText startDateText;
	protected FormattedText endDateText;
	protected LocalDate startDate = LocalDate.now();
	protected LocalDate endDate = LocalDate.now().plusMonths(6);
	protected final ModifyListener listener;
	protected final ModifyListener extraListener;
	protected boolean locked = false;
	
	public @Nullable LocalDate getStartDate() {
		LocalDate result = null;
		if (startDateText.getValue() instanceof final LocalDate ld && startDateText.isValid()) {
			result = ld;
			startDate = result;
		}
		return result;
	}
	
	public @Nullable LocalDate getEndDate() {
		LocalDate result = null;
		if (endDateText.getValue() instanceof final LocalDate ld && endDateText.isValid()) {
			result = ld;
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
//	
//	@Override
//	protected int computeWidth(Control control) {
//		int i = super.computeWidth(control);
//		System.out.println(String.format("compute width: %d", i));
//		return i;
//	}
//	
//	@Override
//	public void update() {
//		System.out.println("updating the control");
//		super.update();
//	}

	@Override
	protected Control createControl(final Composite ppparent) {

		final Composite pparent = createMainComposite(ppparent);
		
		{
			final Label lbl = new Label(pparent, SWT.NONE);
			lbl.setText("From: ");
			lbl.setLayoutData(GridDataFactory.swtDefaults().align(SWT.CENTER, SWT.CENTER).minSize(45, -1).create());

			startDateText = createStartDateEditorControl(pparent);

			final Label lbl2 = new Label(pparent, SWT.NONE);
			lbl2.setText(" to ");
			lbl2.setLayoutData(GridDataFactory.swtDefaults().align(SWT.CENTER, SWT.CENTER).minSize(20, -1).create());

			endDateText = createEndDateEditorControl(pparent);
		}
		return createOtherControls(pparent);
	}
	
	protected Composite createMainComposite(final Composite parent) {
		final Composite pparent = new Composite(parent, SWT.NONE) {
			
			int minHeight = 40;
			int minWidth = -1;

			@Override
			public Point getSize() {
				Point p = super.getSize();
				return new Point(Math.max(p.x, minWidth), Math.max(minHeight, p.y));
			}

			@Override
			public void setSize(int width, int height) {
				super.setSize(Math.max(width, minWidth), Math.max(minHeight, height));
			}

			@Override
			public Point computeSize(int wHint, int hHint) {
				Point p = super.computeSize(wHint, hHint);
				return new Point(Math.max(p.x, minWidth), Math.max(minHeight, p.y));
			}

			@Override
			public Point computeSize(int wHint, int hHint, boolean b) {
				Point p = super.computeSize(wHint, hHint, b);
				return new Point(Math.max(p.x, minWidth), Math.max(minHeight, p.y));
			}
		};
		
		pparent.setLayout(GridLayoutFactory.swtDefaults().numColumns(10).equalWidth(false).spacing(3, 0).margins(0, 7).create());
		return pparent;
	}
	
	public Control createOtherControls(final Composite pparent) {
		return pparent;
	}

	public boolean isLocked() {
		return locked;
	}

	public void setLocked(final boolean locked) {
		startDateText.getControl().setEnabled(locked);
		endDateText.getControl().setEnabled(locked);
		setOtherControlsLocked(locked);
		this.locked = locked;
	}
	
	protected void setOtherControlsLocked(final boolean locked) {
		// this is for overrides
	}

	protected FormattedText createStartDateEditorControl(final Composite parent) {
		startDateText = new FormattedText(parent);
		startDateText.setFormatter(startDateFormatter);
		startDateText.getControl().setLayoutData(GridDataFactory.swtDefaults().minSize(80, -1).hint(80, 20).create());
		startDateText.setValue(startDate);
		startDateText.getControl().addModifyListener(listener);
		startDateText.getControl().pack();
		return startDateText;
	}
	
	protected FormattedText createEndDateEditorControl(final Composite parent) {
		endDateText = new FormattedText(parent);
		endDateText.setFormatter(endDateFormatter);
		endDateText.getControl().setLayoutData(GridDataFactory.swtDefaults().minSize(80, -1).hint(80, 20).create());
		endDateText.setValue(endDate);
		endDateText.getControl().addModifyListener(extraListener);
		return endDateText;
	}
}