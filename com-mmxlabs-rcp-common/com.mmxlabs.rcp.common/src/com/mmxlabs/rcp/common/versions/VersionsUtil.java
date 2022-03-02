/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2022
 * All rights reserved.
 */
package com.mmxlabs.rcp.common.versions;

import java.time.Instant;
import java.util.UUID;

import com.mmxlabs.hub.services.users.UsernameProvider;
import com.mmxlabs.models.mmxcore.MMXCoreFactory;
import com.mmxlabs.models.mmxcore.VersionRecord;

public final class VersionsUtil {

	private VersionsUtil() {

	}

	public static VersionRecord createNewRecord() {
		VersionRecord record = MMXCoreFactory.eINSTANCE.createVersionRecord();

		record.setCreatedAt(Instant.now());
		record.setCreatedBy(UsernameProvider.INSTANCE.getUserID());
		record.setVersion(UUID.randomUUID().toString());

		return record;
	}
}
