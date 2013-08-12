/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2013
 * All rights reserved.
 */
package com.mmxlabs.license.features;

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

import com.google.inject.Guice;
import com.google.inject.Inject;
import com.google.inject.Injector;
import com.mmxlabs.license.features.internal.FeatureEnablementExtension;
import com.mmxlabs.license.features.internal.FeatureEnablementModule;

public class LicenseFeatures {
	public static void initialiseFeatureEnablements() {

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
								sai.addStringPermission("features:" + clean(feature));

							}
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
	private static String clean(String feature) {

		feature = feature.replaceAll(":", "_");
		feature = feature.replaceAll("\\*", "_");

		return feature;
	}
}
