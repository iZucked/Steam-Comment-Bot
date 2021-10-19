/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2021
 * All rights reserved.
 */
package com.mmxlabs.hub.auth;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import org.eclipse.jface.preference.PreferenceDialog;
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
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.dialogs.PreferencesUtil;

import com.mmxlabs.hub.DataHubServiceProvider;
import com.mmxlabs.hub.IDataHubStateChangeListener;

public class DataHubStatusTrimContribution {
	
	protected IDataHubStateChangeListener listener;
	
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
		myLabel.setText("DataHub status");
		
		{
			final boolean online = DataHubServiceProvider.getInstance().isHubOnline();
			final boolean loggedin = DataHubServiceProvider.getInstance().isLoggedIn();
			setLabelTextAndToolTip(myLabel, online, loggedin);
		}
		{
			myLabel.addMouseListener(new MouseListener() {
				
				@Override
				public void mouseDown(MouseEvent e) {
					// Nothing to do here. No one is here but us robots
				}
				
				@Override
				public void mouseDoubleClick(MouseEvent e) {
					if (!DataHubServiceProvider.getInstance().isOnlineAndLoggedIn()) {
						final Shell shell = e.display.getActiveShell();
						PreferenceDialog dialog = PreferencesUtil.createPreferenceDialogOn(shell, 
								"com.mmxlabs.lngdataserver.server.DataserverPreferencePage", 
								new String[] {"com.mmxlabs.lngdataserver.server.DataserverPreferencePage"}, null);
						dialog.open();
					}
				}

				@Override
				public void mouseUp(MouseEvent e) {
					// When mouse is up it means that the cat caught it and threw it up in the air to catch like a peanut.
					// Is cat going to catch the mouse, or is it going to be a misfortune for cat, but a life changing
					// slip for the mouse?
					// We are not going to find that out. Mouse might became an alcoholic or a drug addict. It might start selling
					// it's body for cheap drugs and end up in the cell, or rotting somewhere behind the dump.
					// Poor mouse! It might've been better off eaten by the cat.
				}
			});
		}
		
		listener = new IDataHubStateChangeListener() {
			
			@Override
			public void hubStateChanged(boolean online, boolean loggedin, boolean changedToOnlineAndLoggedIn) {
				Display.getDefault().asyncExec(() -> {
					setLabelTextAndToolTip(myLabel, online, loggedin);
				});
			}
			
			@Override
			public void hubPermissionsChanged() {
				// we don't care about permission change
			}
		};
		IDataHubStateChangeListener pListener = listener;
		DataHubServiceProvider.getInstance().addDataHubStateListener(pListener);
		
		return control;
	}

	private void setLabelTextAndToolTip(final Label myLabel, final boolean online, final boolean loggedin) {
		if (!myLabel.isDisposed()) {
			myLabel.setToolTipText(dataHubStatusToolTipText(online, loggedin));
			myLabel.setImage(dataHubStatusImage(online, loggedin));
		}
	}
	
	@PreDestroy
	public void dispose() {
		IDataHubStateChangeListener pListener = listener;
		if (pListener != null) {
			DataHubServiceProvider.getInstance().removeDataHubStateListener(pListener);
		}
		if (!connectedAndAuth.isDisposed()) {
			connectedAndAuth.dispose();
		}
		if (!connectedNotAuth.isDisposed()) {
			connectedNotAuth.dispose();
		}
		if (!disconnected.isDisposed()) {
			disconnected.dispose();
		}
		if (mainLabel != null && !mainLabel.isDisposed()) {
			if (mainLabel.getImage()!= null && !mainLabel.getImage().isDisposed()) {
				mainLabel.getImage().dispose();
			}
			mainLabel.dispose();
		}
	}
	
	private String dataHubStatusToolTipText(boolean online, boolean loggedin) {
		String result = " Not connected\n Double click to check connection settings";
		if (online && loggedin) {
			result = " Connected and logged in";
		}
		if (online && !loggedin) {
			result = " Connected and logged out\n Double click to check connection settings";
		}
		return result;
	}
	
	private final Image connectedAndAuth = new Image(Display.getDefault(), DataHubStatusTrimContribution.class.getResourceAsStream("/icons/connectedauth.png"));
	private final Image connectedNotAuth = new Image(Display.getDefault(), DataHubStatusTrimContribution.class.getResourceAsStream("/icons/connectednotauth.png"));
	private final Image disconnected = new Image(Display.getDefault(), DataHubStatusTrimContribution.class.getResourceAsStream("/icons/disconnected.png"));
	
	private Image dataHubStatusImage(boolean online, boolean loggedin) {
		if (online && loggedin) {
			return connectedAndAuth;
		}
		if (online && !loggedin) {
			return connectedNotAuth;
		}
		return disconnected;
	}
}
