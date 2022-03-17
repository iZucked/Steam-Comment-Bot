/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2022
 * All rights reserved.
 */
package com.mmxlabs.models.lng.cargo.ui.editorpart;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.ControlContribution;
import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.jface.layout.GridLayoutFactory;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;

import com.mmxlabs.models.ui.util.LimitWidgetHeightListener;

public class ActionToolbarEditor extends ControlContribution {

	private Action action;

	private boolean locked = false;
	private Button actionBtn;

	public ActionToolbarEditor(final String id, Action action) {
		super(id);
		this.action = action;
	}

	@Override
	protected Control createControl(final Composite ppparent) {

		final int minHeight = 36;
		final Composite pparent = new Composite(ppparent, SWT.NONE) {
			@Override
			protected void checkSubclass() {
			}

			@Override
			public Point getSize() {
				final Point p = super.getSize();
				return new Point(p.x, Math.max(minHeight, p.y));
			}

			@Override
			public void setSize(int width, int height) {
				super.setSize(width, Math.max(minHeight, height));
			}

			@Override
			public Point computeSize(int wHint, int hHint) {
				final Point p = super.computeSize(wHint, hHint);
				return new Point(p.x, Math.max(minHeight, p.y));
			}

			@Override
			public Point computeSize(int wHint, int hHint, boolean b) {
				final Point p = super.computeSize(wHint, hHint, b);
				return new Point(p.x, Math.max(minHeight, p.y));
			};
		};
		pparent.setLayout(GridLayoutFactory.fillDefaults().numColumns(1).equalWidth(false).spacing(3, 0).margins(0, 7).create());
		{
			actionBtn = new Button(pparent, SWT.PUSH);
			actionBtn.setText(action.getText());
			actionBtn.setToolTipText(action.getToolTipText());

			actionBtn.addSelectionListener(new SelectionAdapter() {

				@Override
				public void widgetSelected(final SelectionEvent e) {
					action.run();
				}

			});
			// Limit height to toolbar height.
			actionBtn.addListener(SWT.Resize, new LimitWidgetHeightListener(pparent, actionBtn));
			actionBtn.setLayoutData(GridDataFactory.swtDefaults().align(SWT.CENTER, SWT.CENTER).minSize(36, 36).create());
		}
		return pparent;
	}

	public boolean isLocked() {
		return locked;
	}

	public void setLocked(final boolean locked) {
		this.locked = locked;
		actionBtn.setEnabled(!locked);
	}
}