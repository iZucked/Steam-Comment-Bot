/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2020
 * All rights reserved.
 */
package com.mmxlabs.models.lng.nominations.utils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.inject.Guice;
import com.google.inject.Inject;
import com.google.inject.Injector;
import com.mmxlabs.models.lng.nominations.editor.internal.NominationTypeExtension;
import com.mmxlabs.models.lng.nominations.editor.internal.NominationTypeModule;
import com.mmxlabs.models.lng.port.Port;

public class NominationTypeRegistry {

	private static final NominationTypeRegistry instance = new NominationTypeRegistry();
	
	@Inject(optional = true)
	private Iterable<NominationTypeExtension> nominationTypeExtensions;
	
	private List<String> nominationTypes;
	private Map<String, String[]> nominationTypeToDependentFields;
	
	public static NominationTypeRegistry getInstance() {
		return instance;
	}
	
	private NominationTypeRegistry() {
		final Injector injector = Guice.createInjector(new NominationTypeModule());
		injector.injectMembers(this);
		
		this.nominationTypes = new ArrayList<>();
		this.nominationTypeToDependentFields = new HashMap<>();
		
		if (nominationTypeExtensions != null) {
			for (final NominationTypeExtension nte : nominationTypeExtensions) {
				this.nominationTypes.add(nte.getType());
				if (nte.getDependentFields() != null) {
					this.nominationTypeToDependentFields.put(nte.getType(), nte.getDependentFields().split(","));
				}
			}
		}
		
		Collections.sort(nominationTypes);
		
		nominationTypes = Collections.unmodifiableList(nominationTypes);
	}
	
	public List<String> getNominationTypes() {
		return this.nominationTypes;
	}
	
	public String[] getDependentFields(String nominationType) {
		return this.nominationTypeToDependentFields.get(nominationType);
	}
}
