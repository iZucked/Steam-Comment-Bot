package com.mmxlabs.hub.auth;

import java.io.IOException;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.browser.Browser;
import org.eclipse.swt.browser.LocationAdapter;
import org.eclipse.swt.browser.LocationEvent;
import org.eclipse.swt.events.KeyAdapter;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.ToolBar;
import org.eclipse.swt.widgets.ToolItem;
import org.eclipse.ui.plugin.AbstractUIPlugin;

import com.mmxlabs.hub.UpstreamUrlProvider;
import com.mmxlabs.hub.common.http.HttpClientUtil;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class OAuthAuthenticationShell extends Shell {

	private static Browser browser;
	private String accessToken;
	private static final OkHttpClient httpClient = HttpClientUtil.basicBuilder().build();

	private Image logo;

	public String getAccessToken() {
		return accessToken;
	}

	/**
	 * Launch the application.
	 *
	 * @param args
	 */

	public void run(Shell shell, boolean visible) {
		try {
			if (visible) {
				super.open();
				super.layout();
			}
			while (!super.isDisposed()) {
				if (!shell.getDisplay().readAndDispatch()) {
					shell.getDisplay().sleep();
				}
			}
			super.dispose();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Create the shell.
	 *
	 * @param display, url
	 */
	public OAuthAuthenticationShell(Shell shell, final String baseUrl, String fragment) {
		super(shell, SWT.SHELL_TRIM);

		this.logo = AbstractUIPlugin.imageDescriptorFromPlugin("com.mmxlabs.lingo.app", "icons/logo-square-128.png").createImage();

		setImage(this.logo);
		setLayout(new GridLayout(2, false));

		ToolBar toolBar = new ToolBar(this, SWT.FLAT | SWT.RIGHT);
		toolBar.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1));

		ToolItem tltmBack = new ToolItem(toolBar, SWT.NONE);
		ToolItem tltmForward = new ToolItem(toolBar, SWT.NONE);
		ToolItem tltmRefresh = new ToolItem(toolBar, SWT.NONE);

		Text txtLocation = new Text(this, SWT.BORDER);
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

		browser = new Browser(this, SWT.NONE);

		GridData gd_browser = new GridData(SWT.FILL, SWT.FILL, false, false, 2, 1);
		gd_browser.heightHint = 691;
		browser.setLayoutData(gd_browser);

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
				 * if the user is already logged in, the hub sometimes redirects the browser
				 * straight to /ui/#/dashboard. If this happens we just shutdown the browser
				 */
				if (event.location.contains(UpstreamUrlProvider.HOME_PATH)) {
					OAuthAuthenticationShell.this.close();
				}

				String cookie = Browser.getCookie("JSESSIONID", baseUrl);

				// TODO: this is very unsafe because it only checks the uri
				if (event.location.contains(baseUrl + UpstreamUrlProvider.URI_AFTER_SUCCESSFULL_AUTHENTICATION)) {

					// get access token from datahub
					final Request request = AuthenticationManager.buildRequestWithoutAuthentication().url(baseUrl + UpstreamUrlProvider.TOKEN_ENDPOINT).addHeader("Cookie", "JSESSIONID=" + cookie)
							.build();

					try (Response response = httpClient.newCall(request).execute()) {
						if (response.isSuccessful()) {
							accessToken = response.body().string();
						}
					} catch (IOException e) {
						MessageDialog.openError(OAuthAuthenticationShell.this, "", "Unable to authenticate with the DataHub. Please try again");
					}

					// shutdown shell
					OAuthAuthenticationShell.this.close();
				} else if (event.location.contains("/oauth2/logout")) {
					OAuthAuthenticationShell.this.close();
				}
			}
		});

		browser.setUrl(baseUrl + fragment);
		createContents();
	}

	@Override
	public void dispose() {
		if (this.logo != null) {
			this.logo.dispose();
			this.logo = null;
		}
		super.dispose();
	}

	/**
	 * Create contents of the shell.
	 */
	protected void createContents() {
		setText("LiNGO Login");
		setSize(640, 780);
	}

	@Override
	protected void checkSubclass() {
		// Disable the check that prevents subclassing of SWT components
	}
}
