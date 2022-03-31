package com.mmxlabs.models.lng.adp.mull.profile;

import java.util.List;

import org.eclipse.jdt.annotation.NonNullByDefault;

@NonNullByDefault
public interface IMullProfile {

	public List<IMullSubprofile> getMullSubprofiles();
}
