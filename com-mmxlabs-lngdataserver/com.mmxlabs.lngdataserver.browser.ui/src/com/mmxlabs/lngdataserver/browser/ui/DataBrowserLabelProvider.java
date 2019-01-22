/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2018
 * All rights reserved.
 */
package com.mmxlabs.lngdataserver.browser.ui;

import java.util.Objects;
import java.util.Set;

import org.eclipse.emf.common.notify.AdapterFactory;
import org.eclipse.emf.edit.ui.provider.AdapterFactoryLabelProvider;
import org.eclipse.jface.viewers.ColumnLabelProvider;
import org.eclipse.jface.viewers.IColorProvider;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Image;
import org.eclipse.ui.PlatformUI;
import com.mmxlabs.lngdataserver.browser.CompositeNode;
import com.mmxlabs.lngdataserver.browser.Node;
import com.mmxlabs.lngdataserver.commons.IBaseCaseVersionsProvider;
import com.mmxlabs.models.lng.scenario.model.util.LNGScenarioSharedModelTypes;

public class DataBrowserLabelProvider extends ColumnLabelProvider implements IColorProvider {

	private final Set<Node> selectedNodes;
	private final Color background;
	private final AdapterFactoryLabelProvider lp;

	private Image img_published;
	private Image img_local;

	private IBaseCaseVersionsProvider baseCaseProvider;

	public IBaseCaseVersionsProvider getBaseCaseProvider() {
		return baseCaseProvider;
	}

	public void setBaseCaseProvider(IBaseCaseVersionsProvider baseCaseProvider) {
		this.baseCaseProvider = baseCaseProvider;
	}

	public DataBrowserLabelProvider(final AdapterFactory adapterFactory, final Set<Node> selectedNodes) {
		lp = new AdapterFactoryLabelProvider(adapterFactory);
		this.selectedNodes = selectedNodes;
		this.background = PlatformUI.getWorkbench().getDisplay().getSystemColor(SWT.COLOR_GREEN);

		img_published = Activator.createScaledImage("published");
		img_local = Activator.createScaledImage("local");
	}

	@Override
	public void dispose() {
		if (img_local != null) {
			img_local.dispose();
			img_local = null;
		}
		if (img_published != null) {
			img_published.dispose();
			img_published = null;
		}
		super.dispose();
	}

	@Override
	public Image getImage(final Object element) {

		if (element instanceof CompositeNode) {
			// TODO: Select an image!
		} else if (element instanceof Node) {
			// figure out if published
			final Node node = (Node) element;
			if (node.isPublished()) {
				return img_published;
			} else {
				return img_local;
			}
		}
		return null;

	}

	@Override
	public String getText(final Object element) {
		if (element instanceof Node) {
			final Node node = (Node) element;
			String prefix = "";
			if (node.eContainer() instanceof CompositeNode) {
				final CompositeNode compositeNode = (CompositeNode) node.eContainer();

				if (compositeNode.getType() == LNGScenarioSharedModelTypes.MARKET_CURVES.getID()) {
					if (baseCaseProvider != null && Objects.equals(node.getVersionIdentifier(), baseCaseProvider.getPricingVersion())) {
						prefix = "** ";

					}
				}
			}
			return prefix + node.getDisplayName();
		}
		return "Error displaying label";
	}

	@Override
	public Color getBackground(final Object object) {

		if (selectedNodes.contains(object)) {
			return background;
		}

		return super.getBackground(object);
	}
}
