/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2022
 * All rights reserved.
 */
package com.mmxlabs.license.features;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashSet;
import java.util.Set;

import org.eclipse.jdt.annotation.NonNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.inject.Guice;
import com.google.inject.Inject;
import com.google.inject.Injector;
import com.mmxlabs.license.features.internal.FeatureEnablementExtension;
import com.mmxlabs.license.features.internal.FeatureEnablementModule;

public class LicenseFeatures {

	private static final Logger LOG = LoggerFactory.getLogger(LicenseFeatures.class);

	private static final String PREFIX_FEATURES = "features:";

	// Extra enablements programatically added. Really for ITS use
	private static Set<String> extraEnablementsSet = new HashSet<>();

	// Enablements loaded from plugin.xml
	private static Set<String> enabledFeatures = new HashSet<>();

	// On class-load run this stuff so it is ready when we try to use the class. The Application is too late....
	static {
		// Initialise feature enablements
		LicenseFeatures.initialiseFeatureEnablements();
	}

	public static void initialiseFeatureEnablements(final String... extraEnablements) {
		addFeatureEnablements(extraEnablements);

		class MyFeatureClass {

			@Inject(optional = true)
			private Iterable<FeatureEnablementExtension> featureEnablements;

			public MyFeatureClass() {
				final Injector injector = Guice.createInjector(new FeatureEnablementModule());
				injector.injectMembers(this);

			}

			public void loadFeatures() {

				if (featureEnablements != null) {
					for (final FeatureEnablementExtension featureEnablementExtension : featureEnablements) {
						if (featureEnablementExtension != null) {
							final String feature = featureEnablementExtension.getFeature();
							if (feature != null) {
								final String permissionKey = PREFIX_FEATURES + clean(feature);

								final String expiryDate = featureEnablementExtension.getExpiryDate();
								if (expiryDate != null) {
									try {
										// Feature can expire, check the date string for now. If unable to parse the date, then treat it as expired.
										final LocalDate date = LocalDate.parse(expiryDate, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
										final LocalDate now = LocalDate.now();
										if (date != null && now != null && !now.isAfter(date)) {
											enabledFeatures.add(permissionKey);
										}
									} catch (final IllegalArgumentException e) {
										// Unable to parse string
										LOG.error("Features: Unable to parse date {}", expiryDate);
									}
								} else {
									// No expiry date, so permit now
									enabledFeatures.add(permissionKey);
								}
							}
						}
					}
				}
			}
		}
		new MyFeatureClass().loadFeatures();

	}

	public static final boolean isPermitted(@NonNull final String feature) {

		final boolean result = enabledFeatures.contains(feature) || extraEnablementsSet.contains(feature);

		if ((!result)) {
			if (feature.startsWith("feature:")) {
				LOG.info("Probable typo - use \"features:\" instead of \"feature:\"");
			}
		}

		return result;

	}

	/**
	 * Sanitise input string
	 * 
	 * @param feature
	 * @return
	 */
	public static String clean(String feature) {

		feature = feature.replaceAll(":", "_");
		feature = feature.replaceAll("\\*", "_");

		return feature;
	}

	public static void addFeatureEnablements(final String... extraEnablements) {
		for (final String s : extraEnablements) {
			final String permissionKey = PREFIX_FEATURES + clean(s);
			extraEnablementsSet.add(permissionKey);
		}
	}

	/**
	 * FIXME: I really want a push/pop capability for ITS
	 * 
	 * @param extraEnablements
	 */
	public static void removeFeatureEnablements(final String... extraEnablements) {
		for (final String s : extraEnablements) {
			final String permissionKey = PREFIX_FEATURES + clean(s);
			extraEnablementsSet.remove(permissionKey);
		}
	}
}
