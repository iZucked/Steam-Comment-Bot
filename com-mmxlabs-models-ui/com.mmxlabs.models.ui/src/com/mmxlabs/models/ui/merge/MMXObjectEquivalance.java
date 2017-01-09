/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
package com.mmxlabs.models.ui.merge;

import java.util.HashSet;
import java.util.Set;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.jdt.annotation.NonNull;

import com.mmxlabs.models.mmxcore.MMXCorePackage;
import com.mmxlabs.models.mmxcore.NamedObject;
import com.mmxlabs.models.mmxcore.OtherNamesObject;
import com.mmxlabs.models.mmxcore.UUIDObject;

/**
 * Utility class to compare our internal EMF objects for equivalence based on UUID then by name
 * 
 */
public class MMXObjectEquivalance {

	/**
	 * Returns true if two EObject's are equivalent. Objects should be {@link UUIDObject}s, {@link NamedObject}s or {@link OtherNamesObject} to qualify for equivalence.
	 * 
	 * @param o1
	 * @param o2
	 * @return
	 */
	public static boolean equivalent(final EObject o1, final EObject o2) {

		// Some quick checks first...
		if (o1 == null || o2 == null) {
			return false;
		}

		if (o1 == o2) {
			return true;
		}

		// Now for more expensive instanceof checks and string comparisons...
		if (o1 instanceof UUIDObject && o2 instanceof UUIDObject) {
			final UUIDObject u1 = (UUIDObject) o1;
			final UUIDObject u2 = (UUIDObject) o2;
			if (equivalentUUIDObject(u1, u2)) {
				return true;
			}
			// Compare OtherNamesObject first as this extends NamedObject
		}
		if (o1 instanceof OtherNamesObject && o2 instanceof OtherNamesObject) {
			final OtherNamesObject u1 = (OtherNamesObject) o1;
			final OtherNamesObject u2 = (OtherNamesObject) o2;
			if (equivalentOtherNamesObject(u1, u2)) {
				return true;
			}
		} else if (o1 instanceof NamedObject && o2 instanceof NamedObject) {
			final NamedObject u1 = (NamedObject) o1;
			final NamedObject u2 = (NamedObject) o2;
			if (equivalentNamedObject(u1, u2)) {
				return true;
			}
		}

		return false;
	}

	/**
	 * Return true if the UUID in o1 matches the UUID in o2 - this is case sensitive.
	 * 
	 * @param o1
	 * @param o2
	 * @return
	 */
	public static boolean equivalentUUIDObject(@NonNull final UUIDObject o1, @NonNull final UUIDObject o2) {
		if (o1 == o2) {
			return true;
		}
		if (o1.getUuid() == null) {
			return false;
		} else if (o2.getUuid() == null) {
			return false;
		} else {
			return o1.getUuid().equals(o2.getUuid());
		}
	}

	/**
	 * Return true if the name in o1 matches the name in o2 - this is case sensitive.
	 * 
	 * @param o1
	 * @param o2
	 * @return
	 */
	public static boolean equivalentNamedObject(@NonNull final NamedObject o1, @NonNull final NamedObject o2) {
		if (o1 == o2) {
			return true;
		}
		if (o1.getName() == null) {
			return false;
		} else if (o2.getName() == null) {
			return false;
		} else {
			return o1.getName().trim().equals(o2.getName().trim());
		}
	}

	/**
	 * Return true if any of the names in o1 match with any of the names in o2 - including the empty string, but excluding null strings. This will check both the {@link MMXCorePackage#Literals
	 * #NAMED_OBJECT__NAME} or {@link MMXCorePackage#Literals#OTHER_NAMES_OBJECT__OTHER_NAMES} fields.
	 * 
	 * @param o1
	 * @param o2
	 * @return
	 */
	public static boolean equivalentOtherNamesObject(@NonNull final OtherNamesObject o1, @NonNull final OtherNamesObject o2) {
		if (o1 == o2) {
			return true;
		}
		// Calculate intersection of the object names
		final Set<String> names1 = new HashSet<String>();
		final Set<String> names2 = new HashSet<String>();

		if (o1.getName() != null) {
			names1.add(o1.getName().trim());
		}
		for (final String n : o1.getOtherNames()) {
			names1.add(n.trim());
		}
		if (o2.getName() != null) {
			names2.add(o2.getName().trim());
		}
		for (final String n : o2.getOtherNames()) {
			names2.add(n.trim());
		}
		names1.retainAll(names2);

		// If not empty, then we have a match!
		return !names1.isEmpty();
	}
}
