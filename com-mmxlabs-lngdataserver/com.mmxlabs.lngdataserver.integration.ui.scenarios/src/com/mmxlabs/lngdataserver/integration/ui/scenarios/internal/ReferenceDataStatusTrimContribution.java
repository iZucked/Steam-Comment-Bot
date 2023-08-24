/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
package com.mmxlabs.lngdataserver.integration.ui.scenarios.internal;

import java.io.File;
import java.nio.file.Files;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.IPath;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mmxlabs.common.util.exceptions.UserFeedbackException;
import com.mmxlabs.license.features.LicenseFeatures;
import com.mmxlabs.lngdataserver.integration.paper.PricingRepository;
import com.mmxlabs.models.lng.scenario.model.util.LNGScenarioSharedModelTypes;

public class ReferenceDataStatusTrimContribution {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(ReferenceDataStatusTrimContribution.class);

	private Runnable versionChangeRunnable;
	private final Image circleOrange = new Image(Display.getDefault(), ReferenceDataStatusTrimContribution.class.getResourceAsStream("/icons/circle_orange.png"));
	private final Image circleGreen = new Image(Display.getDefault(), ReferenceDataStatusTrimContribution.class.getResourceAsStream("/icons/circle_green.png"));
	
	// Future extension to include all reference data
	@SuppressWarnings("unused")
	private static final List<String> types = getBaseCaseTypesToCheck();
	
	public static class RDSRecords {
		public RDSRecords() {
			super();
		}
		public Map<String, String> typeVersion = new HashMap<String, String>();
		public boolean isDismissed = false;
	}
	
	private RDSRecords currentRecords;
	private Label mainLabel = null;


	@PostConstruct
	protected Control createControl(Composite parent) {

		final Label myLabel = new Label(parent, SWT.NONE);
		myLabel.setText("Pricing version status");
		mainLabel = myLabel;
		myLabel.addMouseListener(new MouseListener() {
			
			@Override
			public void mouseUp(MouseEvent e) {
				// No need
			}
			
			@Override
			public void mouseDown(MouseEvent e) {
				// No need for this
			}
			
			@Override
			public void mouseDoubleClick(MouseEvent e) {
				dismissNotification(myLabel);
			}
		});
		
		currentRecords = getSaved();
		versionChangeRunnable = this::versionChanged;
		PricingRepository.INSTANCE.registerLocalVersionListener(versionChangeRunnable);
		setLabelTextAndToolTip(myLabel, !currentRecords.isDismissed);
		
		return myLabel;
	}
	
	private void changed() {
		Display.getDefault().asyncExec(() -> {
			if (mainLabel != null) {
				setLabelTextAndToolTip(mainLabel, true);
			}
		});
	}
	
	private void versionChanged() {
		final String cV = currentRecords.typeVersion.get(LNGScenarioSharedModelTypes.MARKET_CURVES.getID());
		final String nV = PricingRepository.INSTANCE.getCurrentVersion();
		if (nV != null && (cV == null || !cV.equals(nV))) {
			currentRecords.isDismissed = false;
			currentRecords.typeVersion.remove(LNGScenarioSharedModelTypes.MARKET_CURVES.getID());
			currentRecords.typeVersion.put(LNGScenarioSharedModelTypes.MARKET_CURVES.getID(), nV);
			saveRecord(currentRecords);
			changed();
		}
	}
	
	private void dismissNotification(final Label myLabel) {
		currentRecords.isDismissed = true;
		saveRecord(currentRecords);
		setLabelTextAndToolTip(myLabel, false);
	}

	private void setLabelTextAndToolTip(final Label myLabel, final boolean changed) {
		if (!myLabel.isDisposed()) {
			myLabel.setToolTipText(dataHubStatusToolTipText(changed));
			myLabel.setImage(dataHubStatusImage(changed));
		}
	}
	
	@PreDestroy
	public void dispose() {

		if (circleGreen != null && !circleGreen.isDisposed()) {
			circleGreen.dispose();
		}
		if (circleOrange != null && !circleOrange.isDisposed()) {
			circleOrange.dispose();
		}
		if (mainLabel != null && !mainLabel.isDisposed()) {
			mainLabel.dispose();
		}
		if (versionChangeRunnable != null) {
			PricingRepository.INSTANCE.deregisterLocalVersionListener(versionChangeRunnable);
		}
	}
	
	private void saveRecord(final RDSRecords myRecord) {
		final File recordsFile = getRecordFile();
		final ObjectMapper mapper = new ObjectMapper();
		try {
			mapper.writeValue(recordsFile, myRecord);
		} catch (Exception e) {
			LOGGER.error(e.getMessage());
		}
	}
	
	private RDSRecords getSaved() {
		RDSRecords result = new RDSRecords();
		final File recordsFile = getRecordFile();
		if (recordsFile.exists() && recordsFile.canRead()) {
			try {
				final String json = Files.readString(recordsFile.toPath());
				if (json != null && !json.isEmpty()) {
					final ObjectMapper mapper = new ObjectMapper();
					result = mapper.readValue(json, RDSRecords.class);
				}
			} catch (Exception e) {
				LOGGER.error(e.getMessage());
			}
		} else {
			
		}
		return result;
	}
	
	@SuppressWarnings("null")
	private File getRecordFile() {
		final IPath wsPath = ResourcesPlugin.getWorkspace().getRoot().getLocation();		
		final File directory = new File(wsPath.toOSString() + IPath.SEPARATOR + "bcs");
		boolean dirExists = directory.exists();
		if (!dirExists) {
			dirExists = directory.mkdirs();
		}
		if (dirExists) {
			return new File(directory.getAbsolutePath() + "/refdata.json");
		}

		throw new UserFeedbackException(String.format(//
				"LiNGO is not permitted to read or write in the workspace - %s! %n Check user permissions!",//
				directory.getAbsolutePath()));
	}
	
	private String dataHubStatusToolTipText(final boolean changed) {
		if (changed && !currentRecords.isDismissed) {
			return "New reference data is available! \nRight click on the scenario to update. \nDouble click to dismiss.";
		}
		return "You have the latest reference data \nor you have dismissed the notification";
	}
	
	private Image dataHubStatusImage(final boolean changed) {
		if (changed && !currentRecords.isDismissed) {
			return circleOrange;
		}
		return circleGreen;
	}
	
	public static final List<String> getBaseCaseTypesToCheck() {
		final List<String> types = new LinkedList<>();
		types.add(LNGScenarioSharedModelTypes.MARKET_CURVES.getID());
		if (LicenseFeatures.isPermitted("features:hub-sync-distances")) {
			types.add(LNGScenarioSharedModelTypes.DISTANCES.getID());
			types.add(LNGScenarioSharedModelTypes.LOCATIONS.getID());
			types.add(LNGScenarioSharedModelTypes.PORT_GROUPS.getID());
		}
		if (LicenseFeatures.isPermitted("features:hub-sync-vessels")) {
			types.add(LNGScenarioSharedModelTypes.BUNKER_FUELS.getID());
			types.add(LNGScenarioSharedModelTypes.FLEET.getID());
			types.add(LNGScenarioSharedModelTypes.VESSEL_GROUPS.getID());
		}
		return types;
	}
}
