/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2019
 * All rights reserved.
 */
package com.mmxlabs.lngdataserver.lng.importers.distances.ui;

import org.eclipse.jface.layout.GridLayoutFactory;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;

import com.mmxlabs.lngdataserver.integration.distances.DistanceRepository;

public class DistancesSelectionPage extends WizardPage {

	private String versionTag;
	private boolean isSelected = false;

	protected DistancesSelectionPage(final String pageName) {
		super(pageName);
	}

	@Override
	public void createControl(final Composite parent) {
		final Composite container = new Composite(parent, SWT.NONE);

		// set the layout for the whole functional region
		final GridLayout layout = GridLayoutFactory.createFrom(new GridLayout(2, false)).extendedMargins(0, 0, 0, 0).spacing(5, 0).create();
		container.setLayout(layout);

		(new Label(container, SWT.NULL)).setText("Select the version of the distances to be imported: ");

		final Combo combo = new Combo(container, SWT.READ_ONLY);

		final DistanceRepository dr = DistanceRepository.INSTANCE;
		dr.getLocalVersions().forEach(v -> combo.add(v.getIdentifier()));

		combo.addSelectionListener(new SelectionListener() {

			@Override
			public void widgetSelected(final SelectionEvent e) {
				versionTag = combo.getText();
				isSelected = true;
				getWizard().getContainer().updateButtons();
			}

			@Override
			public void widgetDefaultSelected(final SelectionEvent e) {
				versionTag = combo.getText();
				getWizard().getContainer().updateButtons();
				isSelected = false;
			}
		});
		setControl(container);
	}

	@Override
	public boolean isPageComplete() {
		return isSelected;
	}

	public String getVersionTag() {
		return versionTag;
	}
}
