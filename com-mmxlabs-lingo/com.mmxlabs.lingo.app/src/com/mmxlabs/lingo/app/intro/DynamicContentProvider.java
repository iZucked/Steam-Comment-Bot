/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
package com.mmxlabs.lingo.app.intro;

import java.io.PrintWriter;
import java.util.Date;

import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.intro.config.IIntroContentProviderSite;
import org.eclipse.ui.intro.config.IIntroXHTMLContentProvider;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

//

public class DynamicContentProvider implements IIntroXHTMLContentProvider {

	@Override
	public void init(final IIntroContentProviderSite site) {
	}

	@Override
	public void createContent(final String id, final PrintWriter out) {
	}

	@Override
	public void createContent(final String id, final Composite parent, final FormToolkit toolkit) {
	}

	private static String getCurrentTimeString() {
		final StringBuffer content = new StringBuffer("Dynamic content from Intro ContentProvider: ");
		content.append("Current time is: ");
		content.append(new Date(System.currentTimeMillis()));
		return content.toString();
	}

	@Override
	public void createContent(final String id, final Element parent) {
		final Document dom = parent.getOwnerDocument();
		final Element para = dom.createElement("p");
		para.setAttribute("id", "someDynamicContentId");
		para.appendChild(dom.createTextNode(getCurrentTimeString()));
		parent.appendChild(para);

	}

	@Override
	public void dispose() {

	}

}
