package com.mmxlabs.models.lng.adp.mull.profile;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jdt.annotation.NonNullByDefault;

@NonNullByDefault
public class MullProfile implements IMullProfile {

	private final List<IMullSubprofile> mullSubprofiles;
	
	public MullProfile(final com.mmxlabs.models.lng.adp.MullProfile eMullProfile) {
		this.mullSubprofiles = new ArrayList<>();
		for (final com.mmxlabs.models.lng.adp.MullSubprofile eMullSubprofile : eMullProfile.getInventories()) {
			if (eMullSubprofile == null) {
				throw new IllegalStateException("Mull inventory profile must be non-null");
			}
			this.mullSubprofiles.add(new MullSubprofile(eMullSubprofile));
		}
		validateConstruction();
	}

	public MullProfile(final List<IMullSubprofile> mullSubprofiles) {
		this.mullSubprofiles = mullSubprofiles;
		validateConstruction();
	}

	private void validateConstruction() {
		if (this.mullSubprofiles.isEmpty()) {
			throw new IllegalStateException("No subprofiles found");
		}
	}
	
	@Override
	public List<IMullSubprofile> getMullSubprofiles() {
		return this.mullSubprofiles;
	}

}
