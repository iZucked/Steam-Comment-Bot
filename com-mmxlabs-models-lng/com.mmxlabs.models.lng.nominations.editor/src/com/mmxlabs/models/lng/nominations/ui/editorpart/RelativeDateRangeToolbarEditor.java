/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2022
 * All rights reserved.
 */
package com.mmxlabs.models.lng.nominations.ui.editorpart;

import java.time.LocalDate;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jface.action.ControlContribution;
import org.eclipse.jface.layout.GridLayoutFactory;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;

import com.mmxlabs.models.lng.nominations.NominationsParameters;
import com.mmxlabs.models.lng.scenario.model.LNGScenarioModel;
import com.mmxlabs.models.lng.scenario.model.util.ScenarioModelUtil;
import com.mmxlabs.models.ui.editorpart.IScenarioEditingLocation;
import com.mmxlabs.models.ui.util.LimitWidgetHeightListener;

public class RelativeDateRangeToolbarEditor extends ControlContribution {

	private static final String FROM_START_STR = "from today's date.";

	private final AbstractNominationsViewerPane parent;

	private final IScenarioEditingLocation jointModelEditor;

	private Button btn1M;
	private Button btn3M;
	private Button btnAll;

	private RangeMode rangeMode = RangeMode.OneMonth;

	public RelativeDateRangeToolbarEditor(AbstractNominationsViewerPane parent, final String id, IScenarioEditingLocation jointModelEditor) {
		super(id);
		this.parent = parent;
		this.jointModelEditor = jointModelEditor;
	}

	@Override
	protected Control createControl(final Composite ppparent) {
		final int minHeight = 36;
		final Composite pparent = new Composite(ppparent, SWT.NONE) {
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
			}
		};
		pparent.setLayout(GridLayoutFactory.fillDefaults().numColumns(10).equalWidth(false).spacing(3, 0).margins(0, 7).create());

		btn1M = createRangeModeButton(pparent, "1m", "1 month " + FROM_START_STR, RangeMode.OneMonth);
		btn3M = createRangeModeButton(pparent, "3m", "3 month " + FROM_START_STR, RangeMode.ThreeMonth);
		btnAll = createRangeModeButton(pparent, "All", "Display all", RangeMode.All);

		// Set the default range mode button so user knows what the current range is set
		// to, on opening the nominations view.
		// For now disable selection of buttons, as is complicated to maintain with
		// From,To date range as well.
		// this.selectRangeModeButton(this.rangeMode);

		return pparent;
	}

	private Button createRangeModeButton(Composite btnParent, String buttonText, String toolTip, RangeMode rangeModeOnClick) {
		final Button btn = new Button(btnParent, SWT.TOGGLE);
		btn.setText(buttonText);
		btn.setToolTipText(toolTip);
		btn.addSelectionListener(new SelectionListener() {

			@Override
			public void widgetSelected(final SelectionEvent e) {
				onClick(rangeModeOnClick, btn);
			}

			@Override
			public void widgetDefaultSelected(final SelectionEvent e) {
				onClick(rangeModeOnClick, btn);
			}

			private void onClick(RangeMode rangeModeOnClick, final Button btn) {
				// Update the current range mode.
				rangeMode = rangeModeOnClick;

				// Set the currently selected range mode button to be selected.
				// For now disable selection of buttons, as is complicated to maintain with
				// From,To date range as well.
				selectRangeModeButton(rangeMode);

				// Set the end date to be x months from the start date.
				setNominationParametersEndDate(rangeMode);

				// Get the nominations viewer pane to update.
				parent.refresh();
			}
		});
		btn.addListener(SWT.Resize, new LimitWidgetHeightListener(btnParent, btn));
		return btn;
	}

	private void selectRangeModeButton(RangeMode rangeMode) {
		switch (rangeMode) {
		case OneMonth:
			setSelectedButton(btn1M);
			break;

		case ThreeMonth:
			setSelectedButton(btn3M);
			break;

		case All:
			setSelectedButton(btnAll);
			break;
		}
	}

	private void setNominationParametersEndDate(RangeMode rangeMode) {
		NominationsParameters np = this.getNominationParameters();

		switch (rangeMode) {
		case OneMonth:
			np.setStartDate(LocalDate.now());
			np.setEndDate(np.getStartDate().plusMonths(1));
			break;

		case ThreeMonth:
			np.setStartDate(LocalDate.now());
			np.setEndDate(np.getStartDate().plusMonths(3));
			break;

		case All:
			np.setStartDate(null);
			np.setEndDate(null);
			break;
		}
	}

	// For now we do not set any button to be selected, so this sets all buttons not
	// to be selected.
	private void setSelectedButton(Button btn) {
		// Set other buttons to not be selected.
		final Button[] btns = new Button[] { btn1M, btn3M, btnAll };
		for (final Button otherBtn : btns) {
//			if (otherBtn != btn) {
			otherBtn.setSelection(false);
//			}
		}

		// Set this one to be selected.
		// For now we do not set any button to be selected.
		// btn.setSelection(true);
	}

	private @NonNull NominationsParameters getNominationParameters() {
		final EObject scenario = this.jointModelEditor.getScenarioDataProvider().getScenario();

		if (scenario instanceof LNGScenarioModel) {
			final LNGScenarioModel lngScenario = (LNGScenarioModel) scenario;
			return ScenarioModelUtil.getNominationsModel(lngScenario).getNominationParameters();
		}

		throw new IllegalStateException();
	}

	public LocalDate getStartDate() {
		return getNominationParameters().getStartDate();
	}

	public LocalDate getEndDate() {
		return getNominationParameters().getEndDate();
	}

	public LocalDate getPromptStartDate() {
		final EObject scenario = this.jointModelEditor.getScenarioDataProvider().getScenario();

		if (scenario instanceof LNGScenarioModel) {
			final LNGScenarioModel lngScenario = (LNGScenarioModel) scenario;
			final LocalDate startDate = lngScenario.getPromptPeriodStart();
			if (startDate != null) {
				return startDate;
			}
		}

		return LocalDate.MIN;
	}

	public RangeMode getRangeMode() {
		return this.rangeMode;
	}
}
