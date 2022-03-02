/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2022
 * All rights reserved.
 */
package com.mmxlabs.lngdataserver.commons;

public interface IDataBrowserActionsHandler {
	
	boolean supportsPublish();
	boolean publish(String version);
	
	boolean supportsSetCurrent();
	boolean setCurrent(String version);
	
	boolean supportsDelete();
	boolean delete(String version);
	
	boolean supportsSyncUpstream();
	boolean syncUpstream();
	
	boolean supportsRename();
	boolean rename(String oldVersion, String newVersion);
	
	boolean supportsRefreshLocal();
	boolean refreshLocal();
}
