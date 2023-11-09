/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
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
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mmxlabs.common.util.exceptions.UserFeedbackException;
import com.mmxlabs.hub.services.users.UsernameProvider;
import com.mmxlabs.rcp.icons.lingo.CommonImages;
import com.mmxlabs.rcp.icons.lingo.CommonImages.IconMode;
import com.mmxlabs.rcp.icons.lingo.CommonImages.IconPaths;
import com.mmxlabs.scenario.service.model.Metadata;
import com.mmxlabs.scenario.service.model.ScenarioInstance;
import com.mmxlabs.scenario.service.ui.IBaseCaseVersionsProvider.IBaseCaseChanged;

public class BaseCaseStatusTrimContribution {

	private static final Logger LOGGER = LoggerFactory.getLogger(BaseCaseStatusTrimContribution.class);
	protected IBaseCaseChanged listener;
	private final Activator plugin = Activator.getDefault();

	private static class BCSVersionRecord {
		public String currentUUID = "";
		public String publisher = "";
		public boolean isDismissed = false;
	}

	BCSVersionRecord myRecord = new BCSVersionRecord();
	private Label mainLabel = null;

	@PostConstruct
	protected Control createControl(Composite parent) {

		final Label myLabel = new Label(parent, SWT.NONE);
		myLabel.setText("Base case status");
		mainLabel = myLabel;
		myLabel.addMouseListener(new MouseAdapter() {

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
		setLabelTextAndToolTip(myLabel, false);
		return myLabel;
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
			mainLabel.dispose();
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
			return new File(directory.getAbsolutePath() + "/records.json");
		}

		throw new UserFeedbackException(String.format(//
				"LiNGO is not permitted to read or write in the workspace - %s! %n Check user permissions!", //
				directory.getAbsolutePath()));
	}

	private String dataHubStatusToolTipText(final boolean changed) {
		if (changed && !myRecord.isDismissed) {
			return String.format("New base case was published by %s %n Double click to dismiss", myRecord.publisher);
		}
		return "You have the latest base case \nor you have dismissed the notification";
	}

	private final Image baseFlagGreen = CommonImages.getImage(IconPaths.BaseFlagGreen, IconMode.Enabled);
	private final Image baseFlagEmpty = CommonImages.getImage(IconPaths.BaseFlag, IconMode.Enabled);

	private Image dataHubStatusImage(final boolean changed) {
		if (changed && !myRecord.isDismissed) {
			return baseFlagGreen;
		}
		return baseFlagEmpty;
	}
}
