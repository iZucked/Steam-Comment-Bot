/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
package com.mmxlabs.lngdataserver.browser.util;

import java.util.function.Function;

import org.eclipse.jdt.annotation.NonNull;
import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;
import org.osgi.framework.FrameworkUtil;
import org.osgi.framework.ServiceRegistration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mmxlabs.lngdataserver.browser.BrowserFactory;
import com.mmxlabs.lngdataserver.browser.CompositeNode;
import com.mmxlabs.lngdataserver.browser.Node;
import com.mmxlabs.lngdataserver.commons.IDataBrowserActionsHandler;
import com.mmxlabs.lngdataserver.commons.IDataRepository;

public class DataBrowserNodeHandler implements DataExtension {

	private static final Logger LOGGER = LoggerFactory.getLogger(DataBrowserNodeHandler.class);

	private final CompositeNode dataRoot = BrowserFactory.eINSTANCE.createCompositeNode();

	private IDataRepository<?> repository;
	private boolean active;

	private String name;

	private ServiceRegistration<@NonNull DataExtension> serviceRegistration;

	public <T> DataBrowserNodeHandler(String name, String typeID, IDataRepository<T> repository, Function<CompositeNode, IDataBrowserActionsHandler> handlerFactory) {
		this.name = name;
		this.repository = repository;

		final Node loading = BrowserFactory.eINSTANCE.createLeaf();
		loading.setDisplayName("loading...");
		dataRoot.setDisplayName(name);
		dataRoot.setType(typeID);
		dataRoot.getChildren().add(loading);
		dataRoot.setActionHandler(handlerFactory.apply(dataRoot));
	}

	@Override
	public CompositeNode getDataRoot() {
		return dataRoot;
	}

	public void start() {

		active = true;
		loadVersions();

		if (true) {
			Bundle bundle = FrameworkUtil.getBundle(DataBrowserNodeHandler.class);
			BundleContext bundleContext = bundle.getBundleContext();
			serviceRegistration = bundleContext.registerService(DataExtension.class, this, null);
		}
	}

	public void stop() {
		if (serviceRegistration != null) {
			serviceRegistration.unregister();
			serviceRegistration = null;
		}

		dataRoot.setActionHandler(null);
		dataRoot.getChildren().clear();
		dataRoot.setCurrent(null);

		active = false;
	}

	private void loadVersions() {

		if (active) {
			LOGGER.debug(name + " back-end ready, retrieving versions...");
			try {
				dataRoot.getChildren().clear();
				dataRoot.setDisplayName(name);
				dataRoot.getActionHandler().refreshLocal();
			} catch (Exception e) {
				LOGGER.error("Error retrieving " + name.toLowerCase() + " versions");
			}
			// register consumer to update on new version
			repository.registerLocalVersionListener(() -> {
				final IDataBrowserActionsHandler handler = dataRoot.getActionHandler();
				if (handler != null) {
					handler.refreshLocal();
				}
			});
			dataRoot.getActionHandler().refreshLocal();
		}
	}
}
