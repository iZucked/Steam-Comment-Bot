package com.mmxlabs.demo.app.intro;

import java.io.*;
import java.util.*;

import org.eclipse.swt.widgets.*;
import org.eclipse.ui.forms.widgets.*;
import org.eclipse.ui.intro.config.*;
import org.w3c.dom.*;

//

public class DynamicContentProvider implements IIntroXHTMLContentProvider {


    @Override
	public void init(IIntroContentProviderSite site) {
    }


    @Override
	public void createContent(String id, PrintWriter out) {
    }

    @Override
	public void createContent(String id, Composite parent, FormToolkit toolkit) {
    }

    private String getCurrentTimeString() {
        StringBuffer content = new StringBuffer(
                "Dynamic content from Intro ContentProvider: ");
        content.append("Current time is: ");
        content.append(new Date(System.currentTimeMillis()));
        return content.toString();
    }

    @Override
	public void createContent(String id, Element parent) {
        Document dom = parent.getOwnerDocument();
        Element para = dom.createElement("p");
        para.setAttribute("id", "someDynamicContentId");
        para.appendChild(dom.createTextNode(getCurrentTimeString()));
        parent.appendChild(para);

    }


    @Override
	public void dispose() {

    }



}
