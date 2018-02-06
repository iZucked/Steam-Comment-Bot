package com.mmxlabs.lngdataserver.lng.importers.port.ui;

import java.io.IOException;

import org.eclipse.jface.layout.GridLayoutFactory;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mmxlabs.lngdataserver.integration.ports.internal.IPortsProvider;
import com.mmxlabs.lngdataserver.integration.ports.internal.PortsRepository;
import com.mmxlabs.lngdataserver.port.ApiException;

public class PortsSelectionPage extends WizardPage{
	
	private static final Logger LOGGER = LoggerFactory.getLogger(PortsSelectionPage.class);
	
	private final PortsRepository portsRepository = new PortsRepository();
	
	private String versionTag;
	private boolean isSelected = false;

	protected PortsSelectionPage(String pageName) {
		super(pageName);
	}

	@Override
	public void createControl(Composite parent) {
		final Composite container = new Composite(parent, SWT.NONE);

		// set the layout for the whole functional region
		GridLayout layout = GridLayoutFactory.createFrom(new GridLayout(2, false)).extendedMargins(0, 0, 0, 0).spacing(5, 0).create();
		container.setLayout(layout);
		
		(new Label(container, SWT.NULL)).setText("Select the version of the ports to be imported: ");
		
		final Combo combo = new Combo(container, SWT.READ_ONLY);
		
		try {
			portsRepository.getVersions().forEach(v -> combo.add(v.getIdentifier()));
		} catch (IOException e1) {
			LOGGER.error("Error retrieving ports versions, IO", e1);
		} catch (ApiException e2) {
			LOGGER.error("API Error retrieving ports versions", e2);
		}
		
	    combo.addSelectionListener(new SelectionListener() {
	        public void widgetSelected(SelectionEvent e) {
	        	versionTag = combo.getText();
	        	isSelected = true;
	        	getWizard().getContainer().updateButtons();
	        }

	        public void widgetDefaultSelected(SelectionEvent e) {
	        	versionTag = combo.getText();
	        	getWizard().getContainer().updateButtons();
	        	isSelected = false;
	        }
	      });
	    setControl(container);
	}
	
	public IPortsProvider getPortsVersion() {
		if (isSelected) {
			try {
				return portsRepository.getPortsProvider(versionTag);
			} catch (Exception e) {
				LOGGER.error("Error retrieving ports for version {}", versionTag);
				LOGGER.error(e.getMessage());
				throw new RuntimeException("Error retrieving ports for version " + versionTag);
			}
		}else {
			throw new IllegalStateException("No version selected");
		}
	}

	@Override
	public boolean canFlipToNextPage(){
		return isPageComplete();
	}
	
	@Override
	public boolean isPageComplete(){
		return isSelected;
	}
}
