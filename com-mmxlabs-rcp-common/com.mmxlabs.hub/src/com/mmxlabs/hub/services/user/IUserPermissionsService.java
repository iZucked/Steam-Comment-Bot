package com.mmxlabs.hub.services.user;

public interface IUserPermissionsService {

	boolean isPermitted(String service, String permission);

	boolean hubSupportsPermissions();
}