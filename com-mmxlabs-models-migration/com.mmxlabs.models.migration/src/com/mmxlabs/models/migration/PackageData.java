package com.mmxlabs.models.migration;

import java.util.Collection;

import com.google.common.collect.Lists;

/**
 * Helper class for loading and registering EPackages.
 * 
 * @since 3.0
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