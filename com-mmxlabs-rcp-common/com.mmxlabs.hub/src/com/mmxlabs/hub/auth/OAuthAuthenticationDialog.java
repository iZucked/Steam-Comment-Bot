/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2020
 * All rights reserved.
 */
package com.mmxlabs.hub.auth;

import java.util.LinkedList;
import java.util.List;

import org.eclipse.jface.window.Window;
import org.eclipse.swt.SWT;
import org.eclipse.swt.browser.Browser;
import org.eclipse.swt.browser.LocationAdapter;
import org.eclipse.swt.browser.LocationEvent;
import org.eclipse.swt.events.KeyAdapter;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.ToolBar;
import org.eclipse.swt.widgets.ToolItem;

import com.mmxlabs.hub.UpstreamUrlProvider;
import com.mmxlabs.hub.common.http.HttpClientUtil;

import okhttp3.OkHttpClient;

public class OAuthAuthenticationDialog extends Window {

	private static Browser browser;
	private static final OkHttpClient httpClient = HttpClientUtil.basicBuilder().build();

	private String baseUrl;
	private String fragment;

	private List<Runnable> disposeHandlers = new LinkedList<>();

	public OAuthAuthenticationDialog(Shell shell, final String baseUrl, String fragment) {
		super(shell);
		this.baseUrl = baseUrl;
		this.fragment = fragment;

		setShellStyle(SWT.DIALOG_TRIM | SWT.APPLICATION_MODAL | SWT.MAX | SWT.RESIZE | getDefaultOrientation());
		setBlockOnOpen(true);
	}

	@Override
	protected void configureShell(Shell newShell) {
		newShell.setText("Data Hub OAuth Login");
		super.configureShell(newShell);
		newShell.setSize(640, 480);
	}

	@Override
	protected Control createContents(Composite _parent) {

		_parent.setLayout(new FillLayout());
		Composite parent = new Composite(_parent, SWT.DEFAULT | SWT.BORDER);
		parent.setLayout(new GridLayout(1, false));

		ToolBar toolBar = new ToolBar(parent, SWT.FLAT | SWT.RIGHT);
		toolBar.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1));

		ToolItem tltmBack = new ToolItem(toolBar, SWT.NONE);
		ToolItem tltmForward = new ToolItem(toolBar, SWT.NONE);
		ToolItem tltmRefresh = new ToolItem(toolBar, SWT.NONE);

		Text txtLocation = new Text(parent, SWT.BORDER);
		txtLocation.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				if (e.keyCode == SWT.CR) {
					String modifiedUrl = txtLocation.getText();
					browser.setUrl(modifiedUrl);
				}
			}
		});
		txtLocation.setText("Location");
		txtLocation.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));

		// Make sure the URL isn't editable
		txtLocation.setEnabled(false);
		txtLocation.setEditable(false);

		browser = new Browser(parent, SWT.NONE);

		GridData gd_browser = new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1);
		gd_browser.heightHint = 691;
		browser.setLayoutData(gd_browser);
		browser.setData("oauthBrowserId"); // this id is used in swtbot tests

		/* Add event handling */
		tltmBack.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				browser.back();
			}
		});
		tltmBack.setText("Back");

		tltmForward.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				browser.forward();
			}
		});
		tltmForward.setText("Forward");

		tltmRefresh.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				browser.refresh();
			}
		});
		tltmRefresh.setText("Refresh");

		browser.addLocationListener(new LocationAdapter() {

			// update location in text bar
			@Override
			public void changed(LocationEvent event) {
				if (event.top) {
					txtLocation.setText(event.location);
				}
				/*
				 * if the user is already logged in, the hub sometimes redirects the browser straight to /ui/#/dashboard. If this happens we just shutdown the browser
				 */
				if (event.location.contains(baseUrl + UpstreamUrlProvider.HOME_PATH) || event.location.contains(UpstreamUrlProvider.BASIC_LOGIN_FORM_PATH) || event.location.contains("/logout")) {
					OAuthAuthenticationDialog.this.close();
				} else if (event.location.contains(baseUrl + UpstreamUrlProvider.URI_AFTER_SUCCESSFULL_AUTHENTICATION)) {
					String cookie = Browser.getCookie("JSESSIONID", baseUrl);

					// store sso cookie in secure preferences for later use
					AuthenticationManager.getInstance().storeInSecurePreferences(OAuthAuthenticationManager.COOKIE, "JSESSIONID=" + cookie);

					OAuthAuthenticationDialog.this.close();
				}
			}
		});

		browser.setUrl(baseUrl + fragment);

		return parent;
	}

	@Override
	public boolean close() {
		try {
			disposeHandlers.forEach(Runnable::run);
		} catch (Exception e) {
			e.printStackTrace();
		}
		disposeHandlers.clear();
		return super.close();
	}

	public void addDisposeListener(Runnable hook) {
		disposeHandlers.add(hook);
	}
}
