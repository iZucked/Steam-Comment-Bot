/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2020
 * All rights reserved.
 */
package com.mmxlabs.models.lng.nominations.editor.internal;

import org.eclipse.jdt.annotation.NonNull;
import org.ops4j.peaberry.eclipse.ExtensionBean;
import org.ops4j.peaberry.eclipse.MapName;

@ExtensionBean("com.mmxlabs.models.lng.nominations.editor.NominationType")
public interface NominationTypeExtension {

	@MapName("type")
	@NonNull
	String getType();
	
	@MapName("dependentFields")
	String getDependentFields();
}
