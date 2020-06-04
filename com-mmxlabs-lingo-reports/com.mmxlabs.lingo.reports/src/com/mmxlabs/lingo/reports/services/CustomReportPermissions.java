/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2020
 * All rights reserved.
 */
package com.mmxlabs.lingo.reports.services;

import com.mmxlabs.lngdataserver.server.UserPermissionsService;

public class CustomReportPermissions {
	public static final String serviceName = "teamreports";
	public static final String rolePublish = "publish";
	public static final String roleRead = "read";
	public static final String roleDelete = "delete";
	
	public static boolean hasCustomReportPermission() {
		return (hasCustomReportPublishPermission()
				|| hasCustomReportReadPermission()
				|| hasCustomReportDeletePermission());
	}
	
	public static boolean hasCustomReportPublishPermission() {
		return (UserPermissionsService.INSTANCE.hubSupportsPermissions())
				&& UserPermissionsService.INSTANCE.isPermitted(serviceName, rolePublish);
	}
	
	public static boolean hasCustomReportReadPermission() {
		return (UserPermissionsService.INSTANCE.hubSupportsPermissions())
				&& UserPermissionsService.INSTANCE.isPermitted(serviceName, roleRead);
	}
	
	public static boolean hasCustomReportDeletePermission() {
		return (UserPermissionsService.INSTANCE.hubSupportsPermissions())
				&& UserPermissionsService.INSTANCE.isPermitted(serviceName, roleDelete);
	}
	
	private CustomReportPermissions() {
	}
}
