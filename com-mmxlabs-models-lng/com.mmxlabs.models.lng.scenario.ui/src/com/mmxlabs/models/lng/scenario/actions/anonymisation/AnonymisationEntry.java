package com.mmxlabs.models.lng.scenario.actions.anonymisation;

import java.util.List;
import java.util.Set;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.edit.domain.EditingDomain;

public class AnonymisationEntry {
	public AnonymisationEntry(EditingDomain editingDomain, List<AnonymisationRecord> records, EObject renamee, String name, EStructuralFeature feature, //
			String prefix, AnonymisationRecordType type, final Set<String> usedIDStrings) {
		super();
		this.editingDomain = editingDomain;
		this.records = records;
		this.renamee = renamee;
		this.name = name;
		this.feature = feature;
		this.prefix = prefix;
		this.type = type;
		this.usedIDStrings = usedIDStrings;
	}

	final EditingDomain editingDomain;
	final List<AnonymisationRecord> records;
	final EObject renamee;
	final String name;
	final EStructuralFeature feature;
	final String prefix;
	final AnonymisationRecordType type;
	final Set<String> usedIDStrings;
}
