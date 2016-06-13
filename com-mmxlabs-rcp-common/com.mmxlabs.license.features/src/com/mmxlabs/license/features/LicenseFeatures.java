/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
package com.mmxlabs.license.features;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashSet;
import java.util.Set;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAccount;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.mgt.DefaultSecurityManager;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.subject.Subject;
import org.eclipse.jdt.annotation.NonNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.inject.Guice;
import com.google.inject.Inject;
import com.google.inject.Injector;
import com.mmxlabs.license.features.internal.FeatureEnablementExtension;
import com.mmxlabs.license.features.internal.FeatureEnablementModule;

public class LicenseFeatures {

	private static final Logger LOG = LoggerFactory.getLogger(LoggerFactory.class);

	private static Set<String> extraEnablementsSet = new HashSet<>();

	public static void initialiseFeatureEnablements(final String... extraEnablements) {
		for (final String s : extraEnablements) {
			extraEnablementsSet.add(s);
		}

		class MyAuthRealm extends AuthorizingRealm {

			@Inject(optional = true)
			private Iterable<FeatureEnablementExtension> featureEnablements;

			public MyAuthRealm() {
				final Injector injector = Guice.createInjector(new FeatureEnablementModule());
				injector.injectMembers(this);

			}

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
				// if (extraEnablements != null) {
				for (final String feature : extraEnablementsSet) {
					if (feature != null) {
						final String permissionKey = "features:" + clean(feature);
						sai.addStringPermission(permissionKey);

					}
				}
				// }

				return sai;
			}

			@Override
			protected AuthenticationInfo doGetAuthenticationInfo(final AuthenticationToken token) throws AuthenticationException {

				return new SimpleAccount(token.getPrincipal(), token.getCredentials(), "MyAuthRealm");
			}
		}
		final MyAuthRealm realm = new MyAuthRealm();
		// injector.injectMembers(realm);

		// Disable caching for immediate updates
		realm.setAuthorizationCachingEnabled(false);

		final SecurityManager securityManager = new DefaultSecurityManager(realm);
		SecurityUtils.setSecurityManager(securityManager);
	}

	public static final boolean isPermitted(@NonNull final String feature) {
		final Subject subject = SecurityUtils.getSubject();
		if (!subject.isAuthenticated()) {
			subject.login(new UsernamePasswordToken("user", "password"));
		}
		return subject.isPermitted(feature);

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
			extraEnablementsSet.add(s);
		}
	}

	/**
	 * FIXME: I really want a push/pop capability for ITS
	 * 
	 * @param extraEnablements
	 */
	public static void removeFeatureEnablements(final String... extraEnablements) {
		for (final String s : extraEnablements) {
			extraEnablementsSet.remove(s);
		}
	}
}
