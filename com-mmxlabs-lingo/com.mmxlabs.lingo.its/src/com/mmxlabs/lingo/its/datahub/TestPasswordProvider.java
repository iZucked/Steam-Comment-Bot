package com.mmxlabs.lingo.its.datahub;

import javax.crypto.spec.PBEKeySpec;

import org.eclipse.equinox.security.storage.provider.IPreferencesContainer;
import org.eclipse.equinox.security.storage.provider.PasswordProvider;

/**
 * Default password provider to silence UI prompt for secure storage password during HubTests.
 * </p>
 * <p>
 * Requires {@link org.eclipse.equinox.security.secureStorage } to be used as an extension point, as described by {@link org.eclipse.equinox.security.storage.provider.PasswordProvider}
 *
 */
public class TestPasswordProvider extends PasswordProvider {

	@Override
	public PBEKeySpec getPassword(IPreferencesContainer container, int passwordType) {
		return new PBEKeySpec("password".toCharArray());
	}

}
