/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2018
 * All rights reserved.
 */
package com.mmxlabs.lngdataserver.lng.importers.vessels.ui;

import org.eclipse.jface.layout.GridLayoutFactory;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mmxlabs.lngdataserver.integration.vessels.VesselsRepository;

public class VesselsSelectionPage extends WizardPage {

	private static final Logger LOGGER = LoggerFactory.getLogger(VesselsSelectionPage.class);

	private String versionTag;
	private boolean isSelected = false;

	protected VesselsSelectionPage(String pageName) {
		super(pageName);
	}

	@Override
	public void createControl(Composite parent) {
		final Composite container = new Composite(parent, SWT.NONE);

		// set the layout for the whole functional region
		GridLayout layout = GridLayoutFactory.createFrom(new GridLayout(2, false)).extendedMargins(0, 0, 0, 0).spacing(5, 0).create();
		container.setLayout(layout);

		(new Label(container, SWT.NULL)).setText("Select the version of the vessels to be imported: ");

		final Combo combo = new Combo(container, SWT.READ_ONLY);

		final VesselsRepository dr = VesselsRepository.INSTANCE;
		dr.isReady();
		dr.getLocalVersions().forEach(v -> combo.add(v.getIdentifier()));

		combo.addSelectionListener(new SelectionAdapter() {

			@Override
			public void widgetSelected(final SelectionEvent e) {
				versionTag = combo.getText();
				isSelected = true;
				getWizard().getContainer().updateButtons();
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
