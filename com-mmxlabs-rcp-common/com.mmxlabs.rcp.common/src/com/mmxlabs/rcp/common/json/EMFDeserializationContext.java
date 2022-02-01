/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2021
 * All rights reserved.
 */
package com.mmxlabs.rcp.common.json;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.MissingResourceException;
import java.util.stream.Collectors;

import org.eclipse.emf.common.notify.AdapterFactory;
import org.eclipse.emf.common.util.ResourceLocator;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.edit.provider.ComposedAdapterFactory;
import org.eclipse.emf.edit.provider.IItemLabelProvider;
import org.eclipse.emf.edit.provider.ReflectiveItemProviderAdapterFactory;
import org.eclipse.jdt.annotation.Nullable;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationConfig;
import com.fasterxml.jackson.databind.InjectableValues;
import com.fasterxml.jackson.databind.deser.DefaultDeserializationContext;
import com.fasterxml.jackson.databind.deser.DeserializerCache;
import com.fasterxml.jackson.databind.deser.DeserializerFactory;
import com.fasterxml.jackson.databind.deser.DefaultDeserializationContext.Impl;
import com.fasterxml.jackson.databind.util.ClassUtil;
import com.mmxlabs.common.Pair;
import com.mmxlabs.common.util.ToBooleanBiFunction;

public class EMFDeserializationContext extends DefaultDeserializationContext {

	private static final ComposedAdapterFactory FACTORY = createAdapterFactory();

	private static ComposedAdapterFactory createAdapterFactory() {
		final List<AdapterFactory> factories = new ArrayList<>();
		factories.add(new ComposedAdapterFactory(ComposedAdapterFactory.Descriptor.Registry.INSTANCE));
		factories.add(new ReflectiveItemProviderAdapterFactory());
		return new ComposedAdapterFactory(factories);
	}

	private class State {
		final List<Runnable> actions = new LinkedList<>();
		final Map<Pair<String, String>, Object> nameMap = new HashMap<>();
		final Map<Pair<String, String>, Object> idMap = new HashMap<>();
		final Collection<EStructuralFeature> ignoredFeatures = new HashSet<>();
		ToBooleanBiFunction<JSONReference, @Nullable String> missingReferenceHandler = (ref, lbl) -> Boolean.TRUE;
	}

	private final State state;

	public void ignoreFeature(final EStructuralFeature feature) {
		state.ignoredFeatures.add(feature);
	}

	public void setMissingFeatureHandler(final ToBooleanBiFunction<JSONReference, @Nullable String> missingReferenceHandler) {
		state.missingReferenceHandler = missingReferenceHandler;
	}

	public void registerType(final Object value) {

		if (value instanceof EObject eObject) {

			final JSONReference ref = JSONReference.of(eObject);

			registerType(ref.getClassType(), ref.getGlobalId(), ref.getName(), eObject);
		}
	}

	public void registerType(final String lookupId, final Object value) {

		if (value instanceof EObject eObject) {

			final JSONReference ref = JSONReference.of(lookupId, eObject);

			registerType(ref.getClassType(), ref.getGlobalId(), ref.getName(), eObject);
		}
	}

	public void registerType(final String type, final String id, final String name, final Object value) {
		if (name != null) {
			state.nameMap.put(new Pair<>(type, name), value);
		}
		if (id != null) {
			state.idMap.put(new Pair<>(type, id), value);
		}
	}

	public Object lookupType(final JSONReference ref) {
		final Object idValue = state.idMap.get(new Pair<>(ref.getClassType(), ref.getGlobalId()));

		if (idValue != null) {
			return idValue;
		}
		final Object nameValue = state.nameMap.get(new Pair<>(ref.getClassType(), ref.getName()));
		if (nameValue != null) {
			return nameValue;
		}

		return null;

	}

	public void deferLookup(final JSONReference ref, final EObject owner, final EReference feature) {

		// Check the ignored features list
		if (state.ignoredFeatures.contains(feature)) {
			return;
		}

		if (feature.isMany()) {
			state.actions.add(() -> {
				final Object referenceObject = lookupType(ref);
				if (referenceObject == null) {
					handleMissingReference(ref, feature);
				} else {
					((List) owner.eGet(feature)).add(referenceObject);
				}
			});
		} else {
			state.actions.add(() -> {

				final Object referenceObject = lookupType(ref);
				if (referenceObject == null) {
					handleMissingReference(ref, feature);
				} else {
					owner.eSet(feature, referenceObject);
				}
			});
		}
	}

	private void handleMissingReference(final JSONReference ref, final EReference feature) {
		// TODO: Maybe we should just pass in the reference to the missing feature
		// handler and make this a util method?
		String typeLabel = null;
		try {

			final int idx = ref.getClassType().lastIndexOf('/');
			final String type = (idx > 0) ? ref.getClassType().substring(idx + 1) : ref.getClassType();
			EClass eClass = EMFDeserializer.getEClass(feature.getEReferenceType(), type);
			ResourceLocator rl = findResourceLocator(eClass.getEPackage());
			if (rl != null) {
				typeLabel = rl.getString(String.format("_UI_%s_type", eClass.getName()));
			}

		} catch (MissingResourceException e) {

		}
		if (state.missingReferenceHandler.accept(ref, typeLabel)) {
			throw new IllegalArgumentException();
		}
	}

	public void runDeferredActions() {

		final List<Runnable> actionsCopy = new LinkedList<>(state.actions);
		state.actions.clear();
		actionsCopy.forEach(Runnable::run);
	}

	/**
	 * Default constructor for a blueprint object, which will use the standard {@link DeserializerCache}, given factory.
	 */
	public EMFDeserializationContext(final DeserializerFactory df) {
		super(df, null);
		this.state = new State();
	}

	protected EMFDeserializationContext(final EMFDeserializationContext src, final DeserializationConfig config, final JsonParser jp, final InjectableValues values) {
		super(src, config, jp, values);
		this.state = src.state;

	}

	protected EMFDeserializationContext(final EMFDeserializationContext src) {
		super(src);
		this.state = src.state;

	}

	protected EMFDeserializationContext(final EMFDeserializationContext src, final DeserializerFactory factory) {
		super(src, factory);
		this.state = src.state;
	}

	  private EMFDeserializationContext(EMFDeserializationContext src, DeserializationConfig config) {
          super(src, config);
          this.state = src.state;
      }
	
	@Override
	public EMFDeserializationContext copy() {
		ClassUtil.verifyMustOverride(Impl.class, this, "copy");
		return new EMFDeserializationContext(this);
	}

	@Override
	public EMFDeserializationContext createInstance(final DeserializationConfig config, final JsonParser p, final InjectableValues values) {
		return new EMFDeserializationContext(this, config, p, values);
	}

	@Override
	public EMFDeserializationContext with(final DeserializerFactory factory) {
		return new EMFDeserializationContext(this, factory);
	}

	@Override
	public EMFDeserializationContext createDummyInstance(DeserializationConfig config) {
		// need to be careful to create "real", not blue-print, instance
		return new EMFDeserializationContext(this, config);
	}

	// Pulled from gmf-runtime
	// https://github.com/eclipse/gmf-runtime/blob/master/org.eclipse.gmf.runtime.emf.core/src/org/eclipse/gmf/runtime/emf/core/internal/util/MetamodelManager.java
	private static ResourceLocator findResourceLocator(EPackage pkg) {
		ResourceLocator result = null;

		// the composed adapter factory has a registry of pairs by EPackage
		// and adapter class
		List<Object> types = new java.util.ArrayList<>(2);
		types.add(pkg);
		types.add(IItemLabelProvider.class);

		AdapterFactory factory = FACTORY.getFactoryForTypes(types);

		if (factory != null) {
			// find some EClass to instantiate to get an item provider for it
			EObject instance = null;

			for (Iterator<?> iter = pkg.getEClassifiers().iterator(); iter.hasNext();) {
				Object next = iter.next();

				if ((next instanceof EClass) && !((EClass) next).isAbstract()) {
					instance = pkg.getEFactoryInstance().create((EClass) next);
					break;
				}
			}

			if (instance != null) {
				Object adapter = factory.adapt(instance, IItemLabelProvider.class);

				if (adapter instanceof ResourceLocator) {
					result = (ResourceLocator) adapter;
				}
			}
		}

		return result;
	}
}