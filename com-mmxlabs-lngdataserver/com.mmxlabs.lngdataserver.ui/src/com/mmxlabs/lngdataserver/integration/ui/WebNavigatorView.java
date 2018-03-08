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

	@Override
	public void createPartControl(Composite parent) {
		parent.setLayout(new FillLayout());
		browser = new Browser(parent, SWT.NONE);
		System.out.println("Browser: " + browser.getBrowserType());

		browser.setBounds(0, 0, 600, 800);

		// String encodedBackend;
		// try {
		// encodedBackend = URLEncoder.encode(BackEndUrlProvider.INSTANCE.getUrl(), "UTF-8");
		// } catch (UnsupportedEncodingException e) {
		// throw new RuntimeException(e);
		// }
		// System.out.println("Opening: " + ServerUrlProvider.INSTANCE.getBaseUrl() + Activator.URL_PREFIX + "?apiBaseUrl=" + encodedBackend);
		selectionListener = new ISelectionListener() {

			@Override
			public void selectionChanged(IWorkbenchPart part, ISelection selection) {

				if (!(selection instanceof TreeSelection)) {
					return;
				}

				TreeSelection treeSelection = (TreeSelection) selection;

				if (treeSelection.getFirstElement() == null) {
					return;
				}

				Node node = (Node) treeSelection.getFirstElement();

				// check if distances node
				if (node.getParent() != null) {
					if (node.getParent().getDisplayName().equals("Distances")) {
						System.out.println("update received: " + node.getDisplayName());
						browser.setUrl(getUrl("distances", node.getDisplayName()));
					}
					if (node.getParent().getDisplayName().equals("Pricing")) {
						System.out.println("update received: " + node.getDisplayName());
						browser.setUrl(getUrl("pricing", node.getDisplayName()));
					}
					if (node.getParent().getDisplayName().equals("Vessels")) {
						System.out.println("update received: " + node.getDisplayName());
						browser.setUrl(getUrl("vessels", node.getDisplayName()));
					}
					if (node.getParent().getDisplayName().equals("Ports")) {
						System.out.println("update received: " + node.getDisplayName());
						browser.setUrl(getUrl("ports", node.getDisplayName()));
					}
				}
			}
		};

		try {
			// Icon from https://commons.wikimedia.org/wiki/Category:Throbbers#/media/File:Ajax-loader.gif
			URL resource = FileLocator.toFileURL(getClass().getResource("/wait.html"));
			browser.setUrl(resource.toExternalForm());
		} catch (IOException e) {
			e.printStackTrace();
		}

		BackEndUrlProvider.INSTANCE.addAvailableListener(() -> RunnerHelper.asyncExec(() -> {
			if (!browser.isDisposed()) {
				browser.setUrl(getUrl("distances", ""));
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

	private String getUrl(String type, String version) {
		String encodedBackend;
		try {
			encodedBackend = URLEncoder.encode(BackEndUrlProvider.INSTANCE.getUrl(), "UTF-8");
		} catch (UnsupportedEncodingException e) {
			throw new RuntimeException(e);
		}
		String url = BackEndUrlProvider.INSTANCE.getUrl() + Activator.URL_PREFIX + "#/" + type + "?apiBaseUrl=" + encodedBackend;
		if (version != "") {
			url = BackEndUrlProvider.INSTANCE.getUrl() + Activator.URL_PREFIX + "#/" + type + "/" + version + "?apiBaseUrl=" + encodedBackend;
		}
		System.out.println("DISTANCE calling: " + url);
		return url;
	}

	@Override
	public void setFocus() {
		browser.setFocus();
	}
}
