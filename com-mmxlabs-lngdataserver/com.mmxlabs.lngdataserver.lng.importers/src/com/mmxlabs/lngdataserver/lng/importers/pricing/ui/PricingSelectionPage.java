package com.mmxlabs.lngdataserver.lng.importers.pricing.ui;

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

import com.mmxlabs.lngdataserver.pricing.IPricingProvider;
import com.mmxlabs.lngdataserver.pricing.PricingRepository;

public class PricingSelectionPage extends WizardPage{
	
	private static final Logger LOGGER = LoggerFactory.getLogger(PricingSelectionPage.class);
	
	private final PricingRepository pricingRepository = new PricingRepository();
	
	private String versionTag;
	private boolean isSelected = false;

	protected PricingSelectionPage(String pageName) {
		super(pageName);
	}

	@Override
	public void createControl(Composite parent) {
		final Composite container = new Composite(parent, SWT.NONE);

		// set the layout for the whole functional region
		GridLayout layout = GridLayoutFactory.createFrom(new GridLayout(2, false)).extendedMargins(0, 0, 0, 0).spacing(5, 0).create();
		container.setLayout(layout);
		
		(new Label(container, SWT.NULL)).setText("Select the version of the prices to be imported: ");
		
		final Combo combo = new Combo(container, SWT.READ_ONLY);
		
		try {
			pricingRepository.getVersions().forEach(v -> combo.add(v.getIdentifier()));
		} catch (IOException e1) {
			LOGGER.error("Error retrieving pricing versions", e1);
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
	
	public IPricingProvider getPricingVersion() {
		if (isSelected) {
			try {
				return pricingRepository.getPricingProvider(versionTag);
			} catch (IOException e) {
				LOGGER.error("Error retrieving pricing for version {}", versionTag);
				LOGGER.error(e.getMessage());
				throw new RuntimeException("Error retrieving pricing for version " + versionTag);
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
