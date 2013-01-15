package com.mmxlabs.models.migration.extensions;

import org.ops4j.peaberry.eclipse.ExtensionBean;
import org.ops4j.peaberry.eclipse.MapName;

@ExtensionBean("com.mmxlabs.models.migration.DefaultMigrationContext")
public interface DefaultMigrationContextExtensionPoint {

	@MapName("context")
	String getContext();
}
