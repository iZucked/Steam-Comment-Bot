/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
package com.mmxlabs.models.migration;

import java.util.Collection;

import com.google.common.collect.Lists;

/**
 * Helper class for loading and registering EPackages.
 * 
 */
public class PackageData {
	private final String nsURI;
	private final Collection<String> resourceURIs;

	public PackageData(final String nsURI, final Collection<String> resourceURIs) {
		this.nsURI = nsURI;
		this.resourceURIs = resourceURIs;
	}

	public PackageData(final String nsURI, final String... resourceURIs) {
		this(nsURI, Lists.newArrayList(resourceURIs));
	}

	public String getNsURI() {
		return nsURI;
	}

	public Collection<String> getResourceURIs() {
		return resourceURIs;
	}
}