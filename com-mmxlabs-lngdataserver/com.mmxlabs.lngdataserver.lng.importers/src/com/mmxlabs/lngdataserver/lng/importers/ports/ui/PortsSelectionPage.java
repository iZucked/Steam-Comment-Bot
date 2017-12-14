package com.mmxlabs.lngdataserver.lng.importers.ports.ui;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.jface.layout.GridLayoutFactory;
import org.eclipse.jface.wizard.IWizardPage;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;

import com.mmxlabs.lngdataserver.distances.DistanceRepository;
import com.mmxlabs.lngdataserver.distances.PortRepository;
import com.mmxlabs.lngdataserver.server.BackEndUrlProvider;
import com.mmxlabs.models.lng.port.RouteLine;
import com.mmxlabs.models.lng.port.RouteOption;
import com.mmxlabs.rcp.common.ServiceHelper;
import com.mmxlabs.scenario.service.model.ScenarioInstance;

public class PortsSelectionPage extends WizardPage {

	private String versionTag;
	private boolean isSelected = false;
	private boolean checked = false;

	protected PortsSelectionPage(String pageName) {
		super(pageName);
	}

	@Override
	public void createControl(Composite parent) {
		final Composite container = new Composite(parent, SWT.NONE);

		// set the layout for the whole functional region
		GridLayout layout = GridLayoutFactory.createFrom(new GridLayout(2, false)).extendedMargins(0, 0, 0, 0).spacing(5, 0).create();
		container.setLayout(layout);

		(new Label(container, SWT.NULL)).setText("Select the version of the distances to be imported: ");

		final Combo combo = new Combo(container, SWT.READ_ONLY);

		PortRepository dr = new PortRepository(BackEndUrlProvider.INSTANCE.getUrl());
		// dr.getVersions().forEach(v -> combo.add(v));
		combo.add("<versionlesss!>");

		combo.addSelectionListener(new SelectionListener() {
			public void widgetSelected(SelectionEvent e) {
				versionTag = combo.getText();
				isSelected = true;
				cleanChecked();
				getWizard().getContainer().updateButtons();
			}

			public void widgetDefaultSelected(SelectionEvent e) {
				versionTag = combo.getText();
				getWizard().getContainer().updateButtons();
				isSelected = false;
				cleanChecked();
			}
		});
		setControl(container);
	}

	@Override
	public boolean canFlipToNextPage() {
		return isPageComplete();
	}

	@Override
	public boolean isPageComplete() {
		return isSelected;
	}

	public String getVersionTag() {
		return versionTag;
	}

	@Override
	public IWizardPage getPreviousPage() {
		checked = false;
		return super.getPreviousPage();
	}

	/**
	 * Indicates whether the selected distance was evaluated for lost distances
	 * 
	 * @return
	 */
	public boolean isChecked() {
		return checked;
	}

	public void setChecked(boolean checked) {
		this.checked = checked;
	}

	private void cleanChecked() {
		checked = false;
	}

}
