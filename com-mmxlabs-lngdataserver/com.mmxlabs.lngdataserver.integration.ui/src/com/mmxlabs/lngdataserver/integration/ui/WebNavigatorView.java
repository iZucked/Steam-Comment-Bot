/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2018
 * All rights reserved.
 */
package com.mmxlabs.lngdataserver.integration.ui;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLEncoder;

import org.eclipse.core.runtime.FileLocator;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.TreeSelection;
import org.eclipse.swt.SWT;
import org.eclipse.swt.browser.Browser;
import org.eclipse.swt.browser.ProgressEvent;
import org.eclipse.swt.browser.ProgressListener;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.ISelectionListener;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.part.ViewPart;

import com.mmxlabs.lngdataserver.browser.Node;
import com.mmxlabs.lngdataserver.server.BackEndUrlProvider;
import com.mmxlabs.rcp.common.RunnerHelper;

public class WebNavigatorView extends ViewPart {

	public static final String ID = "com.mmxlabs.lngdataserver.integration.ui.WebNavigatorView";

	private Browser browser;
	private ISelectionListener selectionListener;

	private final Object lockObject = new Object(); // lock object for changing the browser base URL
	private boolean ready = true; // Can we load the URL immediately or do we need to delay it?
	private String nextUrl = null; // The next URL to load in.
	private final ProgressListener listener = new ProgressListener() {

		@Override
		public void completed(final ProgressEvent event) {
			// URL has finished loading. Check to see if another URL has been requested.
			ready = true;
			synchronized (lockObject) {
				if (nextUrl != null) {
					// Clear the nextURL value...
					final String u = nextUrl;
					nextUrl = null;
					// .. and trigger an update for the URL
					updateURL(u);
				}
			}
		}

		@Override
		public void changed(final ProgressEvent event) {

		}
	};

	@Override
	public void createPartControl(final Composite parent) {
		parent.setLayout(new FillLayout());
		browser = new Browser(parent, SWT.NONE);
		System.out.println("Browser: " + browser.getBrowserType());

		browser.setBounds(0, 0, 600, 800);

		browser.addProgressListener(listener);

		selectionListener = new ISelectionListener() {

			@Override
			public void selectionChanged(final IWorkbenchPart part, final ISelection selection) {

				if (!(selection instanceof TreeSelection)) {
					return;
				}

				final TreeSelection treeSelection = (TreeSelection) selection;

				if (treeSelection.getFirstElement() == null) {
					return;
				}

				final Node node = (Node) treeSelection.getFirstElement();

				// check if distances node
				if (node.getParent() != null) {
					if (node.getParent().getDisplayName().equals("Distances")) {
						System.out.println("update received: " + node.getDisplayName());
						updateURL(getUrl("distances", node.getVersionIdentifier()));
					}
					if (node.getParent().getDisplayName().equals("Pricing")) {
						System.out.println("update received: " + node.getDisplayName());
						updateURL(getUrl("pricing", node.getVersionIdentifier()));
					}
					if (node.getParent().getDisplayName().equals("Vessels")) {
						System.out.println("update received: " + node.getDisplayName());
						updateURL(getUrl("vessels", node.getVersionIdentifier()));
					}
					if (node.getParent().getDisplayName().equals("Ports")) {
						System.out.println("update received: " + node.getDisplayName());
						updateURL(getUrl("ports", node.getVersionIdentifier()));
					}
				}
			}

		};

		try {
			// Icon from https://commons.wikimedia.org/wiki/Category:Throbbers#/media/File:Ajax-loader.gif
			// Trigger extraction of throbber.gif to file system so relative urls work in builds
			final URL _gif_resource = FileLocator.toFileURL(getClass().getResource("/throbber.gif"));
			final URL resource = FileLocator.toFileURL(getClass().getResource("/wait.html"));
			browser.setUrl(resource.toExternalForm());
		} catch (final IOException e) {
			e.printStackTrace();
		}

		BackEndUrlProvider.INSTANCE.addAvailableListener(() -> RunnerHelper.asyncExec(() -> {
			if (!browser.isDisposed()) {
				updateURL(getUrl("pricing", "latest"));
				getViewSite().getWorkbenchWindow().getSelectionService().addSelectionListener("com.mmxlabs.lngdataserver.browser.ui.DataBrowser", selectionListener);
			}
		}));
	}

	@Override
	public void dispose() {
		if (selectionListener != null) {
			getViewSite().getWorkbenchWindow().getSelectionService().removeSelectionListener("com.mmxlabs.lngdataserver.browser.ui.DataBrowser", selectionListener);
		}
	}

	private String getUrl(final String type, final String version) {
		String encodedBackend;
		try {
			encodedBackend = URLEncoder.encode(BackEndUrlProvider.INSTANCE.getUrl(), "UTF-8");
		} catch (final UnsupportedEncodingException e) {
			throw new RuntimeException(e);
		}
		String url = BackEndUrlProvider.INSTANCE.getUrl() + Activator.URL_PREFIX + "#/" + type + "?apiBaseUrl=" + encodedBackend;
		if (version != "") {
			url = BackEndUrlProvider.INSTANCE.getUrl() + Activator.URL_PREFIX + "#/" + type + "/" + version + "?apiBaseUrl=" + encodedBackend;
		}
		System.out.println("Navigator calling: " + url);
		return url;
	}

	@Override
	public void setFocus() {
		browser.setFocus();
	}

	/**
	 * Set the new URL for the browser OR store the URL for delayed loading.
	 * 
	 * @param url
	 */
	private void updateURL(final String url) {

		synchronized (lockObject) {
			if (ready) {
				ready = false;
				browser.setUrl(url);
			} else {
				nextUrl = url;
			}
		}
	}
}
