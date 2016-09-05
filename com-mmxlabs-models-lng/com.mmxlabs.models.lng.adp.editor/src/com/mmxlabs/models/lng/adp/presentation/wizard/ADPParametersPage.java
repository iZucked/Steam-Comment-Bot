/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
package com.mmxlabs.models.lng.adp.presentation.wizard;

import java.time.YearMonth;

import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.nebula.widgets.formattedtext.FormattedText;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;

import com.mmxlabs.models.datetime.ui.formatters.YearMonthTextFormatter;
import com.mmxlabs.models.lng.adp.ADPModel;

public class ADPParametersPage extends WizardPage {

	private ADPModel adpModel;
	private FormattedText formattedText;
	private YearMonthTextFormatter dateFormatter;

	protected ADPParametersPage(ADPModel adpModel) {
		super("ADP Model Param", "Set main ADP params", null);
		this.adpModel = adpModel;
	}

	@Override
	public boolean isPageComplete() {

		adpModel.setYearStart((YearMonth) dateFormatter.getValue());

		return super.isPageComplete();
	}

	@Override
	public void createControl(Composite parent) {

		final Composite container = new Composite(parent, SWT.NULL);

		final GridLayout layout = new GridLayout(2, true);
		container.setLayout(layout);

		Label lbl = new Label(container, SWT.NONE);
		lbl.setText("Year Start");
		
		formattedText = new FormattedText(container);
		dateFormatter = new YearMonthTextFormatter();
		formattedText.setFormatter(dateFormatter);
		
		YearMonth now = YearMonth.now().withMonth(10);
		
		formattedText.setValue(now);

		setControl(container);
	}
}
