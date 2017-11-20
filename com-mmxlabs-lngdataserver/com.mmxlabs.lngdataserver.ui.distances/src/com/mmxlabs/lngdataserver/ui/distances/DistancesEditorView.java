package com.mmxlabs.lngdataserver.ui.distances;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.TreeSelection;
import org.eclipse.swt.SWT;
import org.eclipse.swt.browser.Browser;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.ISelectionListener;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.part.ViewPart;
import org.w3c.dom.traversal.NodeIterator;
import com.mmxlabs.lngdataserver.browser.Node;
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
		
//		String encodedBackend;
//		try {
//			encodedBackend = URLEncoder.encode(BackEndUrlProvider.INSTANCE.getUrl(), "UTF-8");
//		} catch (UnsupportedEncodingException e) {
//			throw new RuntimeException(e);
//		}
//		System.out.println("Opening: " + ServerUrlProvider.INSTANCE.getBaseUrl() + Activator.URL_PREFIX + "?apiBaseUrl=" + encodedBackend);
		browser.setUrl(getUrl(""));
		
		
		getViewSite().getWorkbenchWindow().getSelectionService().addSelectionListener("com.mmxlabs.lngdataserver.browser.ui.DataBrowser", new ISelectionListener() {
			
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
				if( node.getParent() != null && node.getParent().getDisplayName().equals("Distances")) {
					System.out.println("update received: " + node.getDisplayName());
					browser.setUrl(getUrl(node.getDisplayName()));
				}
			}
		});
	}
	
	private String getUrl(String version) {
		String encodedBackend;
		try {
			encodedBackend = URLEncoder.encode(BackEndUrlProvider.INSTANCE.getUrl(), "UTF-8");
		} catch (UnsupportedEncodingException e) {
			throw new RuntimeException(e);
		}
		String url = ServerUrlProvider.INSTANCE.getBaseUrl() + Activator.URL_PREFIX + "#/distances" + "?apiBaseUrl=" + encodedBackend;
		if (version != "") {
			url = ServerUrlProvider.INSTANCE.getBaseUrl() + Activator.URL_PREFIX  + "#/distances" + "/" + version + "?apiBaseUrl=" + encodedBackend;
		}
		System.out.println("calling: " + url);
		return url;
	}

	@Override
	public void setFocus() {
		browser.setFocus();
	}
}
