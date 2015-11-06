/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2015
 * All rights reserved.
 */
package com.mmxlabs.models.lng.transformer.period;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.jdt.annotation.NonNull;

import com.mmxlabs.models.lng.cargo.SpotSlot;
import com.mmxlabs.models.lng.parameters.ParametersPackage;
import com.mmxlabs.models.lng.schedule.Schedule;
import com.mmxlabs.models.lng.schedule.SchedulePackage;

/**
 * @author Simon Goodall
 *
 */
public final class CopiedScenarioEntityMapping implements IScenarioEntityMapping {

	@NonNull
	private final Map<EObject, EObject> optimiserRealToCopy = new HashMap<>();
	@NonNull
	private final Map<EObject, EObject> optimiserCopyToReal = new HashMap<>();

	@NonNull
	private final Map<EObject, EObject> originalRealToCopy = new HashMap<>();
	@NonNull
	private final Map<EObject, EObject> originalCopyToReal = new HashMap<>();

	@NonNull
	private final IScenarioEntityMapping originalMapping;

	@NonNull
	private Set<EObject> unusedObjects;

	@NonNull
	private Set<EObject> usedObjects;

	public CopiedScenarioEntityMapping(@NonNull final IScenarioEntityMapping originalMapping, @NonNull final EcoreUtil.Copier originalScenarioCopier,
			@NonNull final EcoreUtil.Copier optimiserScenarioCopier) {
		this.originalMapping = originalMapping;
		for (final Map.Entry<EObject, EObject> e : originalScenarioCopier.entrySet()) {
			originalRealToCopy.put(e.getKey(), e.getValue());
			originalCopyToReal.put(e.getValue(), e.getKey());
		}
		for (final Map.Entry<EObject, EObject> e : optimiserScenarioCopier.entrySet()) {
			optimiserRealToCopy.put(e.getKey(), e.getValue());
			optimiserCopyToReal.put(e.getValue(), e.getKey());
		}

		this.usedObjects = originalMapping.getUsedOriginalObjects().stream() //
				.filter(t -> t.eClass().getEPackage() != ParametersPackage.eINSTANCE) //
				.filter(t -> t.eClass().getEPackage() != SchedulePackage.eINSTANCE) //
				.map(t -> mapOriginalRealToCopy(t)).collect(Collectors.toSet()); //
		this.unusedObjects = originalMapping.getUnusedOriginalObjects().stream() //
				.filter(t -> t.eClass().getEPackage() != ParametersPackage.eINSTANCE) //
				.filter(t -> t.eClass().getEPackage() != SchedulePackage.eINSTANCE) //
				.map(t -> mapOriginalRealToCopy(t)).collect(Collectors.toSet()); //
	}

	@NonNull
	private EObject mapOptimiserRealToCopy(@NonNull final EObject eObject) {
		assert optimiserRealToCopy.containsKey(eObject);
		return optimiserRealToCopy.get(eObject);
	}

	@NonNull
	private EObject mapOptimiserCopyToReal(@NonNull final EObject eObject) {
		assert optimiserCopyToReal.containsKey(eObject);
		return optimiserCopyToReal.get(eObject);
	}

	@NonNull
	private EObject mapOriginalRealToCopy(@NonNull final EObject eObject) {
		// boolean a = originalCopyToReal.containsKey(eObject);
		// boolean b = originalRealToCopy.containsKey(eObject);
		// boolean c = optimiserCopyToReal.containsKey(eObject);
		// boolean d = optimiserRealToCopy.containsKey(eObject);
		//
		// boolean e = originalCopyToReal.containsValue(eObject);
		// boolean f = originalRealToCopy.containsValue(eObject);
		// boolean g = optimiserCopyToReal.containsValue(eObject);
		// boolean h = optimiserRealToCopy.containsValue(eObject);

		assert originalRealToCopy.containsKey(eObject);
		return originalRealToCopy.get(eObject);
	}

	@NonNull
	private EObject mapOriginalCopyToReal(@NonNull final EObject eObject) {
		assert originalCopyToReal.containsKey(eObject);
		return originalCopyToReal.get(eObject);
	}

	@Override
	public void createMappings(final Map<EObject, EObject> originalToCopyMap) {
		throw new UnsupportedOperationException();
	}

	@Override
	public void createMapping(final EObject original, final EObject copy) {
		throw new UnsupportedOperationException();
	}

	@Override
	public void registerRemovedOriginal(final EObject original) {
		throw new UnsupportedOperationException();
	}

	@SuppressWarnings("unchecked")
	@Override
	public <T extends EObject> T getOriginalFromCopy(final T copyOfOptimiser) {
		if (copyOfOptimiser == null) {
			return null;
		}
		if (copyOfOptimiser instanceof SpotSlot && !optimiserCopyToReal.containsKey(copyOfOptimiser)) {
			return null;
		}
		
		final EObject realOptimiser = mapOptimiserCopyToReal(copyOfOptimiser);
		
		final T copyOfOriginal = (T) originalMapping.getOriginalFromCopy(realOptimiser);
		if (copyOfOptimiser instanceof SpotSlot && copyOfOriginal == null) {
			return null;
		}
		return (T) mapOriginalRealToCopy(copyOfOriginal);
	}

	@SuppressWarnings("unchecked")
	@Override
	public <T extends EObject> T getCopyFromOriginal(final T copyOfOriginal) {
		if (copyOfOriginal == null) {
			return null;
		}
		final EObject realOriginal = mapOriginalCopyToReal(copyOfOriginal);
		final T copyOfOptimiser = (T) originalMapping.getCopyFromOriginal(realOriginal);
		return (T) mapOptimiserRealToCopy(copyOfOptimiser);
	}

	@Override
	public Collection<EObject> getUsedOriginalObjects() {
		return usedObjects;
	}

	@Override
	public Collection<EObject> getUnusedOriginalObjects() {
		return unusedObjects;
	}
}
