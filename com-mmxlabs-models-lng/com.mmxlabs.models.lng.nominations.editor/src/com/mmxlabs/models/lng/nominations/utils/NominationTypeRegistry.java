package com.mmxlabs.models.lng.nominations.utils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.google.inject.Guice;
import com.google.inject.Inject;
import com.google.inject.Injector;
import com.mmxlabs.models.lng.nominations.editor.internal.NominationTypeExtension;
import com.mmxlabs.models.lng.nominations.editor.internal.NominationTypeModule;

public class NominationTypeRegistry {

	final static private NominationTypeRegistry instance = new NominationTypeRegistry();
	
	@Inject(optional = true)
	private Iterable<NominationTypeExtension> nominationTypeExtensions;

	private List<String> nominationTypes;
	
	public static NominationTypeRegistry getInstance() {
		return instance;
	}
	
	private NominationTypeRegistry() {
		final Injector injector = Guice.createInjector(new NominationTypeModule());
		injector.injectMembers(this);
		
		this.nominationTypes = new ArrayList<>();
		
		if (nominationTypeExtensions != null) {
			for (final NominationTypeExtension nte : nominationTypeExtensions) {
				this.nominationTypes.add(nte.getType());
			}
		}
		
		Collections.sort(nominationTypes);
		
		nominationTypes = Collections.unmodifiableList(nominationTypes);
	}
	
	public List<String> getNominationTypes() {
		return this.nominationTypes;
	}
}
