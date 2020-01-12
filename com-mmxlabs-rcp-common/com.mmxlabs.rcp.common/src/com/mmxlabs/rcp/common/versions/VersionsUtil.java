/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2020
 * All rights reserved.
 */
package com.mmxlabs.rcp.common.versions;

import java.time.Instant;
import java.util.UUID;

import com.mmxlabs.models.mmxcore.MMXCoreFactory;
import com.mmxlabs.models.mmxcore.VersionRecord;
import com.mmxlabs.rcp.common.user.UsernameProvider;

public final class VersionsUtil {

	private VersionsUtil() {

	}

	public static VersionRecord createNewRecord() {
		VersionRecord record = MMXCoreFactory.eINSTANCE.createVersionRecord();

		record.setCreatedAt(Instant.now());
		record.setCreatedBy(UsernameProvider.getUsername());
		record.setVersion(UUID.randomUUID().toString());

		return record;
	}
}
