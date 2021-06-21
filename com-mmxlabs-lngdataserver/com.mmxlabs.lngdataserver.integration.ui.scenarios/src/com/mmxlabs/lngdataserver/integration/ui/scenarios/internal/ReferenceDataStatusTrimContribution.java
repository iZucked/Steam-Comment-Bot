/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2021
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
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mmxlabs.common.util.exceptions.UserFeedbackException;
import com.mmxlabs.license.features.LicenseFeatures;
import com.mmxlabs.models.lng.scenario.model.util.LNGScenarioSharedModelTypes;
import com.mmxlabs.scenario.service.manifest.Manifest;
import com.mmxlabs.scenario.service.model.ScenarioInstance;
import com.mmxlabs.scenario.service.model.manager.ScenarioStorageUtil;
import com.mmxlabs.scenario.service.ui.IBaseCaseVersionsProvider.IBaseCaseChanged;

public class ReferenceDataStatusTrimContribution {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(ReferenceDataStatusTrimContribution.class);
	protected IBaseCaseChanged listener;
	private final Activator plugin = Activator.getDefault();
	
	private static final List<String> types = getBaseCaseTypesToCheck();
	
	private class RDSRecords {
		public Map<String, String> typeVersion = new HashMap<String, String>();
		public boolean isDismissed = false;
	}
	
	private RDSRecords currentRecords;
	private RDSRecords newRecords;
	private Label mainLabel = null;


	@PostConstruct
	protected Control createControl(Composite parent) {
		
		final int minHeight = 36;
		final Composite control = new Composite(parent, SWT.NONE)
		{
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
			}
		};
		control.setLayout(new FillLayout());
		final Label myLabel = new Label(control, SWT.CENTER);
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
		
		final BaseCaseVersionsProviderService service = plugin.getBaseCaseVersionsProviderService();
		currentRecords = getSaved();
		listener = new IBaseCaseChanged() {

			@Override
			public void changed() {
				Display.getDefault().asyncExec(() -> {
					if (service != null) {
						setLabelTextAndToolTip(myLabel, bcChanged(service.getBaseCase()));
					}
				});
			}
		};
		IBaseCaseChanged pListener = listener;
		if (service != null && pListener != null) {
			service.addChangedListener(pListener);
		}
		control.redraw();
		return control;
	}
	
	private boolean bcChanged(ScenarioInstance si) {
		if (si != null) {
			boolean versionChanged = false;
			final Manifest mf = si.getManifest();
			newRecords = new RDSRecords();
			if (mf != null) {
				newRecords.typeVersion = ScenarioStorageUtil.extractScenarioDataVersions(mf);
			}
			if (!types.isEmpty() && currentRecords != null && !newRecords.typeVersion.isEmpty()) {
				for (final String type : types) {
					final String cV = currentRecords.typeVersion.get(type);
					final String nV = newRecords.typeVersion.get(type);
					if (nV != null && (cV == null || !cV.equalsIgnoreCase(nV))) {
						versionChanged = true;
						break;
					}
				}
				if (versionChanged) {
					currentRecords = newRecords;
					saveRecord(currentRecords);
					return true;
				}
			}
		}
		return false;
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
		IBaseCaseChanged pListener = listener;
		BaseCaseVersionsProviderService service = plugin.getBaseCaseVersionsProviderService();
		if (service != null && pListener != null) {
			service.removeChangedListener(pListener);
		}
		if (mainLabel != null && !mainLabel.isDisposed()) {
			if (mainLabel.getImage()!= null && !mainLabel.getImage().isDisposed()) {
				mainLabel.getImage().dispose();
			}
			mainLabel.dispose();
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
		Display display = Display.getDefault();
		if (changed && !currentRecords.isDismissed) {
			return new Image(display, ReferenceDataStatusTrimContribution.class.getResourceAsStream("/icons/circle_orange.png"));
		}
		return new Image(display, ReferenceDataStatusTrimContribution.class.getResourceAsStream("/icons/circle_green.png"));
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
