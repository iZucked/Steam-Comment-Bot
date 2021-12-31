/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2022
 * All rights reserved.
 */
package com.mmxlabs.hub.auth;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

import org.eclipse.jdt.annotation.Nullable;
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
import org.eclipse.swt.widgets.Layout;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.ToolBar;
import org.eclipse.swt.widgets.ToolItem;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mmxlabs.hub.UpstreamUrlProvider;
import com.mmxlabs.hub.common.http.HttpClientUtil;

import okhttp3.OkHttpClient;

public class OAuthDialog extends Window {

	private static final Logger LOGGER = LoggerFactory.getLogger(OAuthDialog.class);

	private Browser browser;
	private static final OkHttpClient httpClient = HttpClientUtil.basicBuilder().build();

	private String baseUrl;
	private String fragment;

	private List<Runnable> disposeHandlers = new LinkedList<>();

	public OAuthDialog(final String upstreamUrl, String path, @Nullable Shell shell) {
		super(shell);
		this.baseUrl = upstreamUrl;
		this.fragment = path;

		setShellStyle(SWT.DIALOG_TRIM | SWT.APPLICATION_MODAL | SWT.MAX | SWT.RESIZE | getDefaultOrientation());
		setBlockOnOpen(true);
	}

	@Override
	protected void configureShell(Shell newShell) {
		newShell.setText("Data Hub OAuth Login");
		super.configureShell(newShell);
		newShell.setLayout(getLayout());
		newShell.setSize(640, 480);
	}

	@Override
	protected Layout getLayout() {
		return new GridLayout(1, false);
	}

	@Override
	protected Control createContents(Composite _parent) {

		_parent.setLayout(new FillLayout());
		Composite oauthComposite = new Composite(_parent, SWT.BORDER);
		oauthComposite.setLayout(getLayout());

		ToolBar toolBar = new ToolBar(oauthComposite, SWT.FLAT | SWT.RIGHT);
		toolBar.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1));

		ToolItem tltmBack = new ToolItem(toolBar, SWT.NONE);
		ToolItem tltmForward = new ToolItem(toolBar, SWT.NONE);
		ToolItem tltmRefresh = new ToolItem(toolBar, SWT.NONE);

		Text txtLocation = new Text(oauthComposite, SWT.BORDER);
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
		// TODO: Set the system property AND/OR add a preference page entry.
		// org.eclipse.swt.browser.DefaultType=edge
		// Needs webview2 
		// see https://docs.microsoft.com/en-us/microsoft-edge/webview2/concepts/distribution
		// see https://developer.microsoft.com/en-us/microsoft-edge/webview2/#webview-title
		// (SG used the Evergreen Bootstrapper)
		browser = new Browser(oauthComposite, SWT.EDGE);

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
				LOGGER.info(event.location);
				if (event.location.contains(UpstreamUrlProvider.BASIC_LOGIN_FORM_PATH) || event.location.contains("/logout")) {
					OAuthDialog.this.close();
				} else if (event.location.contains(baseUrl + UpstreamUrlProvider.URI_AFTER_SUCCESSFULL_AUTHENTICATION) || event.location.contains(baseUrl + UpstreamUrlProvider.HOME_PATH)) {
					String cookie = Browser.getCookie("JSESSIONID", baseUrl);

					// store sso cookie in secure preferences for later use
					AuthenticationManager.getInstance().storeInSecurePreferences(OAuthManager.COOKIE, "JSESSIONID=" + cookie);

					OAuthDialog.this.close();
				}
			}
		});

		browser.setUrl(baseUrl + fragment);
		// Edge doesn't retain the cookie between runs
		Optional<String> value = AuthenticationManager.getInstance().retrieveFromSecurePreferences(OAuthManager.COOKIE);
		if (value.isPresent()) {
			Browser.setCookie(value.get(), baseUrl);
		}
		return oauthComposite;
	}

	@Override
	public boolean close() {
		// Clear cookies *before* we close the browser instance
		if (fragment.equals(UpstreamUrlProvider.LOGOUT_PATH)) {
			Browser.clearSessions();
			// edge doesn't support getCookie() and setCookie()
			// see https://www.eclipse.org/swt/faq.php#edgelimitations
			if ("ie".equals(browser.getBrowserType())) {
				Browser.setCookie("JSESSIONID=;", baseUrl);
				Browser.setCookie("authenticated=;", baseUrl + "/authenticated");
			}
		}

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
