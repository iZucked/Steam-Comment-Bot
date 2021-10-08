package com.mmxlabs.lingo.app.updater.model;

import java.util.LinkedList;
import java.util.List;

public class UpdateVersions {

	private List<UpdateVersion> versions = new LinkedList<>();

	public List<UpdateVersion> getVersions() {
		return versions;
	}

	public void setVersions(List<UpdateVersion> versions) {
		this.versions = versions;
	}
}
