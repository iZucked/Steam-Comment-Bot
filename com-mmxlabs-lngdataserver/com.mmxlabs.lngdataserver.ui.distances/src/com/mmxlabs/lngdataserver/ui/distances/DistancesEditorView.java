package com.mmxlabs.lngdataserver.ui.distances;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import org.eclipse.swt.SWT;
import org.eclipse.swt.browser.Browser;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.part.ViewPart;

import com.mmxlabs.lngdataserver.server.BackEndUrlProvider;
import com.mmxlabs.lngdataserver.ui.server.ServerUrlProvider;

public class DistancesEditorView extends ViewPart {
	
	public static final String ID = "com.mmxlabs.lngdataserver.ui.distances.DistancesEditorView";

	private Browser browser;
	
	@Override
	public void createPartControl(Composite parent) {
		parent.setLayout(new FillLayout());
		browser = new Browser(parent, SWT.NONE);
		System.out.println("Browser: " + browser.getBrowserType());

		browser.setBounds(0, 0, 600, 800);
		
		String encodedBackend;
		try {
			encodedBackend = URLEncoder.encode(BackEndUrlProvider.INSTANCE.getUrl(), "UTF-8");
		} catch (UnsupportedEncodingException e) {
			throw new RuntimeException(e);
		}
		System.out.println("Opening: " + ServerUrlProvider.INSTANCE.getBaseUrl() + Activator.URL_PREFIX + "?apiBaseUrl=" + encodedBackend);
		browser.setUrl(ServerUrlProvider.INSTANCE.getBaseUrl() + Activator.URL_PREFIX + "?apiBaseUrl=" + encodedBackend);
	}

	@Override
	public void setFocus() {
		browser.setFocus();
	}
}
