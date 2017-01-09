/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
package com.mmxlabs.rcp.common.application;

import java.io.File;
import java.io.FileInputStream;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Platform;
import org.osgi.framework.Bundle;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.google.inject.Guice;
import com.google.inject.Inject;
import com.google.inject.Injector;
import com.mmxlabs.common.Pair;

public class WorkbenchStateManager {
	@Inject
	private Iterable<ViewPartRewriteExtension> extensions;

	private File getWorkbenchStateFile() {
		for (final Bundle bundle : Platform.getBundles("org.eclipse.ui.workbench", null)) {
			IPath path = Platform.getStateLocation(bundle);
			if (path == null) {
				return null;
			}
			path = path.append("workbench.xml");
			return path.toFile();
		}
		return null;
	}

	public static void cleanupWorkbenchState() {
		final Injector injector = Guice.createInjector(new ViewPartRewriteModule());
		final WorkbenchStateManager hook = new WorkbenchStateManager();
		injector.injectMembers(hook);
		hook.doCleanupWorkbenchState();
	}

	public void doCleanupWorkbenchState() {

		final Map<String, String> viewIdsToReplace = new HashMap<>();

		final List<String> viewIdsToRemove = new LinkedList<>();

		for (final ViewPartRewriteExtension ext : extensions) {
			final String oldViewId = ext.getOldViewID();
			final String newViewId = ext.getNewViewID();
			if (newViewId == null || newViewId.isEmpty()) {
				viewIdsToRemove.add(oldViewId);
			} else {
				viewIdsToReplace.put(oldViewId, newViewId);
			}
		}

		if (viewIdsToRemove.isEmpty() && viewIdsToReplace.isEmpty()) {
			return;
		}

		final File stateFile = getWorkbenchStateFile();
		if (stateFile != null && stateFile.exists()) {
			try (FileInputStream input = new FileInputStream(stateFile)) {

				final DocumentBuilderFactory docbf = DocumentBuilderFactory.newInstance();
				docbf.setNamespaceAware(true);
				final DocumentBuilder docbuilder = docbf.newDocumentBuilder();
				final Document document = docbuilder.parse(input);

				boolean changed = false;
				changed |= removeNodes(viewIdsToReplace, viewIdsToRemove, document, "//window/page/perspectives/perspective/layout/mainWindow/info/folder/page", "content");
				changed |= removeNodes(viewIdsToReplace, viewIdsToRemove, document, "//window/page/views/view", "id");
				changed |= removeNodes(viewIdsToReplace, viewIdsToRemove, document, "//window/page/perspectives/perspective/show_view_action", "id");
				changed |= removeNodes(viewIdsToReplace, viewIdsToRemove, document, "//window/page/perspectives/perspective/view", "id");
				// Duplicated views cause workbench restoration errors.
				changed |= removeDuplicateNodes(document, "//window/page/perspectives/perspective/view", "id");
				changed |= removeNodes(viewIdsToReplace, viewIdsToRemove, document, "//window/page/perspectives/perspective/fastViewBars/fastViewBar", "selectedTabId");
				changed |= removeNodes(viewIdsToReplace, viewIdsToRemove, document, "//window/page/perspectives/perspective/fastViewBars/fastViewBar/fastViews/view", "id");

				if (changed) {
					// write the content back over original state
					final TransformerFactory transformerFactory = TransformerFactory.newInstance();
					final Transformer transformer = transformerFactory.newTransformer();
					final DOMSource source = new DOMSource(document);
					final StreamResult result = new StreamResult(stateFile);
					transformer.transform(source, result);
				}
			} catch (final Exception e) {
				e.printStackTrace();
			}
		}
	}

	private boolean removeNodes(final Map<String, String> viewIdsToReplace, final List<String> viewIdsToRemove, final Document document, final String xpath, final String id)
			throws XPathExpressionException {
		boolean changed = false;
		final XPath xp = XPathFactory.newInstance().newXPath();

		final NodeList nodes = (NodeList) xp.evaluate(xpath, document, XPathConstants.NODESET);

		final List<Node> nodesToRemove = new LinkedList<>();

		for (int i = 0, len = nodes.getLength(); i < len; i++) {
			final Node nPage = nodes.item(i);
			final Node nContent = nPage.getAttributes().getNamedItem(id);
			final String viewId = nContent.getNodeValue();
			if (viewIdsToRemove.contains(viewId)) {
				nodesToRemove.add(nPage);
			} else if (viewIdsToReplace.containsKey(viewId)) {
				changed = true;
				nContent.setNodeValue(viewIdsToReplace.get(viewId));
			}
		}

		for (final Node node : nodesToRemove) {
			final Node parentNode = node.getParentNode();
			parentNode.removeChild(node);

			changed = true;

		}
		return changed;
	}

	private boolean removeDuplicateNodes(final Document document, final String xpath, final String id) throws XPathExpressionException {
		boolean changed = false;
		final XPath xp = XPathFactory.newInstance().newXPath();

		final NodeList nodes = (NodeList) xp.evaluate(xpath, document, XPathConstants.NODESET);

		final Set<Pair<Node, String>> seenElements = new HashSet<>();
		final List<Node> nodesToRemove = new LinkedList<>();

		for (int i = 0, len = nodes.getLength(); i < len; i++) {
			final Node nPage = nodes.item(i);
			final Node nContent = nPage.getAttributes().getNamedItem(id);
			final String viewId = nContent.getNodeValue();

			final Pair<Node, String> p = new Pair<>(nPage.getParentNode(), viewId);
			if (!seenElements.add(p)) {
				nodesToRemove.add(nPage);
			}
		}

		for (final Node node : nodesToRemove) {
			final Node parentNode = node.getParentNode();
			parentNode.removeChild(node);
			changed = true;

		}
		return changed;
	}
}
