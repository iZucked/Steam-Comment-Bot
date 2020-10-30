/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2020
 * All rights reserved.
 */
package com.mmxlabs.lngdataserver.integration.ui.scenarios.internal;

import java.io.File;
import java.nio.file.Files;

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
import com.mmxlabs.hub.services.users.UsernameProvider;
import com.mmxlabs.scenario.service.model.Metadata;
import com.mmxlabs.scenario.service.model.ScenarioInstance;
import com.mmxlabs.scenario.service.ui.IBaseCaseVersionsProvider.IBaseCaseChanged;

public class BaseCaseStatusTrimContribution {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(BaseCaseStatusTrimContribution.class);
	protected IBaseCaseChanged listener;
	private final Activator plugin = Activator.getDefault();
	
	private static class BCSVersionRecord{
		public String currentUUID = "";
		public String publisher = "";
		public boolean isDismissed = false;
	}
	
	BCSVersionRecord myRecord = new BCSVersionRecord();

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
		myLabel.setText("Base case status");
		myLabel.addMouseListener(new MouseListener() {
			
			@Override
			public void mouseUp(MouseEvent e) {
				// No need
			}
			
			@Override
			public void mouseDown(MouseEvent e) {
				dismissNotification(myLabel);
			}
			
			@Override
			public void mouseDoubleClick(MouseEvent e) {
				dismissNotification(myLabel);
			}
		});
		
		final BaseCaseVersionsProviderService service = plugin.getBaseCaseVersionsProviderService();
		myRecord = getSaved();
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
		
		return control;
	}
	
	private boolean bcChanged(ScenarioInstance si) {
		if (si != null) {
			boolean uuidChanged = false;
			String uuid = "";
			if (myRecord.currentUUID != "") {
				uuid = si.getUuid();
				if (!uuid.equalsIgnoreCase(myRecord.currentUUID)) {
					uuidChanged = true;
				}
			} else {
				uuid = si.getUuid();
				uuidChanged = true;
			}
			if (uuidChanged) {
				final Metadata md = si.getMetadata();
				if (md != null) {
					final String foo = UsernameProvider.INSTANCE.getDisplayName(md.getCreator());
					final String publisher = foo == null ? md.getCreator() : foo;
					if (!myRecord.publisher.equals(publisher)) {
						myRecord.isDismissed = false;
						myRecord.currentUUID = uuid;
						myRecord.publisher = publisher;
						saveRecord(myRecord);
						return true;
					}
				}
			}
		}
		return false;
	}
	
	private void dismissNotification(final Label myLabel) {
		myRecord.isDismissed = true;
		saveRecord(myRecord);
		setLabelTextAndToolTip(myLabel, false);
	}

	private void setLabelTextAndToolTip(final Label myLabel, final boolean changed) {
		if (!myLabel.isDisposed()) {
			myLabel.setToolTipText(dataHubStatusToolTipText(changed));
			//myLabel.setImage(dataHubStatusImage(online, loggedin));
		}
	}
	
	@PreDestroy
	public void dispose() {
		IBaseCaseChanged pListener = listener;
		BaseCaseVersionsProviderService service = plugin.getBaseCaseVersionsProviderService();
		if (service != null && pListener != null) {
			service.removeChangedListener(pListener);
		}
	}
	
	private void saveRecord(final BCSVersionRecord myRecord) {
		final File recordsFile = getRecordFile();
		final ObjectMapper mapper = new ObjectMapper();
		try {
			mapper.writeValue(recordsFile, myRecord);
		} catch (Exception e) {
			LOGGER.error(e.getMessage());
		}
	}
	
	private BCSVersionRecord getSaved() {
		BCSVersionRecord result = myRecord;
		final File recordsFile = getRecordFile();
		if (recordsFile.exists() && recordsFile.canRead()) {
			try {
				final String json = Files.readString(recordsFile.toPath());
				if (json != null && !json.isEmpty()) {
					final ObjectMapper mapper = new ObjectMapper();
					result = mapper.readValue(json, BCSVersionRecord.class);
				}
			} catch (Exception e) {
				LOGGER.error(e.getMessage());
			}
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
			final File recordsFile = new File(directory.getAbsolutePath() + "/records.json");
			return recordsFile;
		}

		throw new UserFeedbackException(String.format(//
				"LiNGO is not permitted to read or write in the workspace - %s! %n Check user permissions!",//
				directory.getAbsolutePath()));
	}
	
	private String dataHubStatusToolTipText(final boolean changed) {
		if (changed && !myRecord.isDismissed) {
			return String.format("New base case was published by %s %nRight click to dismiss", myRecord.publisher);
		}
		return "You have the latest base case";
	}
	
	private Image dataHubStatusImage(final boolean changed) {
		Display display = Display.getDefault();
		if (changed && !myRecord.isDismissed) {
			return new Image(display, BaseCaseStatusTrimContribution.class.getResourceAsStream("/icons/connectedauth.png"));
		}
		return new Image(display, BaseCaseStatusTrimContribution.class.getResourceAsStream("/icons/disconnected.png"));
	}
}
