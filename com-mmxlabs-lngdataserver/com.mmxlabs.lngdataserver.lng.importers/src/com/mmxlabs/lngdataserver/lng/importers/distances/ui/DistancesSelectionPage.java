package com.mmxlabs.lngdataserver.lng.importers.distances.ui;

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

import com.mmxlabs.lngdataserver.integration.distances.DistanceRepository;
import com.mmxlabs.lngdataserver.server.BackEndUrlProvider;

public class DistancesSelectionPage extends WizardPage {

	private String versionTag;
	private boolean isSelected = false;
	private boolean checked = false;

	protected DistancesSelectionPage(String pageName) {
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

		DistanceRepository dr =   DistanceRepository.INSTANCE;
		dr.isReady();
		dr.getVersions().forEach(v -> combo.add(v.getIdentifier()));

		combo.addSelectionListener(new SelectionListener() {

			@Override
			public void widgetSelected(SelectionEvent e) {
				versionTag = combo.getText();
				isSelected = true;
				cleanChecked();
				getWizard().getContainer().updateButtons();
			}

			@Override
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
		// lostDistances.clear();
	}

}
