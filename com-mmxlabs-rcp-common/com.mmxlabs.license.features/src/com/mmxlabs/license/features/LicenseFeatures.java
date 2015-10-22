/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2015
 * All rights reserved.
 */
package com.mmxlabs.license.features;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAccount;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.mgt.DefaultSecurityManager;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.inject.Guice;
import com.google.inject.Inject;
import com.google.inject.Injector;
import com.mmxlabs.license.features.internal.FeatureEnablementExtension;
import com.mmxlabs.license.features.internal.FeatureEnablementModule;

public class LicenseFeatures {

	private static final Logger LOG = LoggerFactory.getLogger(LoggerFactory.class);

	public static void initialiseFeatureEnablements(final String... extraEnablements) {

		final Injector injector = Guice.createInjector(new FeatureEnablementModule());

		class MyAuthRealm extends AuthorizingRealm {

			@Inject(optional = true)
			private Iterable<FeatureEnablementExtension> featureEnablements;

			@Override
			protected AuthorizationInfo doGetAuthorizationInfo(final PrincipalCollection principals) {
				final SimpleAuthorizationInfo sai = new SimpleAuthorizationInfo();

				if (featureEnablements != null) {
					for (final FeatureEnablementExtension featureEnablementExtension : featureEnablements) {
						if (featureEnablementExtension != null) {
							final String feature = featureEnablementExtension.getFeature();
							if (feature != null) {
								final String permissionKey = "features:" + clean(feature);

								final String expiryDate = featureEnablementExtension.getExpiryDate();
								if (expiryDate != null) {
									try {
										// Feature can expire, check the date string for now. If unable to parse the date, then treat it as expired.
										final LocalDate date = LocalDate.parse(expiryDate, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
										final LocalDate now = LocalDate.now();
										if (date != null && now != null) {
											if (!now.isAfter(date)) {
												sai.addStringPermission(permissionKey);
											} else {
												// LOG.warn("Feature %s has expired.", feature);
											}
										}
									} catch (final IllegalArgumentException e) {
										// Unable to parse string
										LOG.error("Features: Unable to parse date %s", expiryDate);
									}
								} else {
									// No expiry date, so permit now
									sai.addStringPermission(permissionKey);
								}
							}
						}
					}
				}
				if (extraEnablements != null) {
					for (final String feature : extraEnablements) {
						if (feature != null) {
							final String permissionKey = "features:" + clean(feature);
							sai.addStringPermission(permissionKey);

						}
					}
				}

				return sai;
			}

			@Override
			protected AuthenticationInfo doGetAuthenticationInfo(final AuthenticationToken token) throws AuthenticationException {

				return new SimpleAccount(token.getPrincipal(), token.getCredentials(), "MyAuthRealm");
			}
		}
		final MyAuthRealm realm = new MyAuthRealm();
		injector.injectMembers(realm);

		// Disable caching for immediate updates
		realm.setAuthorizationCachingEnabled(false);

		final SecurityManager securityManager = new DefaultSecurityManager(realm);
		SecurityUtils.setSecurityManager(securityManager);
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
}
