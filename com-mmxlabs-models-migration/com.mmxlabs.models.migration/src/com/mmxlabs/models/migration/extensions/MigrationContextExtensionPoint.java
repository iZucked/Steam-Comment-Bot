package com.mmxlabs.models.migration.extensions;

import org.ops4j.peaberry.eclipse.ExtensionBean;
import org.ops4j.peaberry.eclipse.MapName;

@ExtensionBean("com.mmxlabs.models.migration.MigrationContext")
public interface MigrationContextExtensionPoint {

	@MapName("name")
	String getName();

	@MapName("latestVersion")
	String getLatestVersion();

}
