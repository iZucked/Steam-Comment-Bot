package com.mmxlabs.lngdataserver.integration.ui.vessels;

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
import com.mmxlabs.lngdataserver.ui.server.ServerUrlProvider;
import com.mmxlabs.rcp.common.RunnerHelper;

public class VesselsEditorView extends ViewPart {

	public static final String ID = "com.mmxlabs.lngdataserver.ui.vessels.VesselsEditorView";

	private Browser browser;

	private ISelectionListener selectionListener;

	@Override
	public void createPartControl(Composite parent) {
		parent.setLayout(new FillLayout());
		browser = new Browser(parent, SWT.NONE);
		System.out.println("Browser vessels: " + browser.getBrowserType());

		browser.setBounds(0, 0, 600, 800);

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
				if (node.getParent() != null && node.getParent().getDisplayName().equals("Vessels")) {
					System.out.println("update received: " + node.getDisplayName());
					browser.setUrl(getUrl(node.getDisplayName()));
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
				browser.setUrl(getUrl(""));
				getViewSite().getWorkbenchWindow().getSelectionService().addSelectionListener("com.mmxlabs.lngdataserver.browser.ui.DataBrowser", selectionListener);
			}
		}));
	}

	private String getUrl(String version) {
		String encodedBackend;
		try {
			encodedBackend = URLEncoder.encode(BackEndUrlProvider.INSTANCE.getUrl(), "UTF-8");
		} catch (UnsupportedEncodingException e) {
			throw new RuntimeException(e);
		}
		String url = ServerUrlProvider.INSTANCE.getBaseUrl() + Activator.URL_PREFIX + "#/vessels/latest" + "?apiBaseUrl=" + encodedBackend;
		if (version != "") {
			url = ServerUrlProvider.INSTANCE.getBaseUrl() + Activator.URL_PREFIX + "#/vessels" + "/" + version + "?apiBaseUrl=" + encodedBackend;
		}
		System.out.println("Vessels calling: " + url);
		return url;
	}

	@Override
	public void dispose() {
		if (selectionListener != null) {
			getViewSite().getWorkbenchWindow().getSelectionService().removeSelectionListener("com.mmxlabs.lngdataserver.browser.ui.DataBrowser", selectionListener);
		}
	}

	@Override
	public void setFocus() {
		browser.setFocus();
	}
}
