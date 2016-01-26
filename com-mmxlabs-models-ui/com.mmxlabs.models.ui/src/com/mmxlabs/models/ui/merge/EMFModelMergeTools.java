/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
package com.mmxlabs.models.ui.merge;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.eclipse.emf.common.command.Command;
import org.eclipse.emf.common.command.CompoundCommand;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.EStructuralFeature.Setting;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.emf.edit.command.AddCommand;
import org.eclipse.emf.edit.command.DeleteCommand;
import org.eclipse.emf.edit.command.ReplaceCommand;
import org.eclipse.emf.edit.command.SetCommand;
import org.eclipse.emf.edit.domain.EditingDomain;

import com.google.common.base.Preconditions;

/**
 * Utility class to help with merging data between different model instances.
 * 
 * @author Simon Goodall
 * 
 */
public class EMFModelMergeTools {

	public static IMappingDescriptor generateMappingDescriptor(final EObject sourceContainer, final EObject destinationContainer, final EReference ref) {

		if (ref.isContainment()) {
			if (ref.isMany()) {
				return generateMappingDescriptorManyContainment(sourceContainer, destinationContainer, ref);
			} else {
				// TODO
			}
		} else {
			if (ref.isMany()) {
				// TODO
			} else {
				return generateMappingDescriptorSingleNonContainment(sourceContainer, destinationContainer, ref);
			}
		}

		throw new IllegalArgumentException("Reference must be many containment or single non-containment");
	}

	/**
	 * Compare two EMF container objects which share the same containment feature and analyse the differences. Generate a {@link IMappingDescriptor} to describe these differences.
	 * 
	 * @param sourceContainer
	 * @param destinationContainer
	 * @param ref
	 * @return
	 */
	public static IMappingDescriptor generateMappingDescriptorManyContainment(final EObject sourceContainer, final EObject destinationContainer, final EReference ref) {

		// Assuming a containment list of EObjects
		Preconditions.checkArgument(ref.isMany());
		Preconditions.checkArgument(ref.isContainment());

		@SuppressWarnings("unchecked")
		final List<EObject> sourceList = (List<EObject>) sourceContainer.eGet(ref);
		@SuppressWarnings("unchecked")
		final List<EObject> destinationList = (List<EObject>) destinationContainer.eGet(ref);

		final Map<EObject, EObject> destinationToSourceMap = new HashMap<EObject, EObject>();
		final List<EObject> objectsAdded = new LinkedList<EObject>();
		// Pre-populate with all objects and take out the ones we find
		final Set<EObject> objectsRemoved = new HashSet<EObject>(destinationList);

		// Process both container list to find the matches and the items added and removed
		for (final EObject sourceObject : sourceList) {
			EObject destMatch = null;
			for (final EObject destObject : destinationList) {
				if (MMXObjectEquivalance.equivalent(sourceObject, destObject)) {
					destMatch = destObject;
					break;
				}
			}
			if (destMatch != null) {
				// Mark this as kept
				objectsRemoved.remove(destMatch);
				// Create the mapping
				destinationToSourceMap.put(destMatch, sourceObject);
			} else {
				// No existing entry found, so mark it as added
				objectsAdded.add(sourceObject);
			}
		}

		return new MappingDescriptorImpl(sourceContainer, destinationContainer, ref, destinationToSourceMap, objectsAdded, objectsRemoved);
	}

	/**
	 * Compare the single EObject reference between two objects and update
	 * 
	 * @param sourceContainer
	 * @param destinationContainer
	 * @param ref
	 * @return
	 */
	public static IMappingDescriptor generateMappingDescriptorSingleNonContainment(final EObject sourceContainer, final EObject destinationContainer, final EReference ref) {
		Preconditions.checkState(!ref.isContainment());
		Preconditions.checkState(!ref.isMany());

		final EObject sourceObject = (EObject) sourceContainer.eGet(ref);

		return new MappingDescriptorImpl(sourceContainer, destinationContainer, ref, Collections.<EObject, EObject> emptyMap(), Collections.singletonList(sourceObject),
				Collections.<EObject> emptyList());

	}

	/**
	 * The {@link IMappingDescriptor} may have object which in turn reference data from the source model that is not being transferred over. These references should be updated to map to equivalent
	 * objects in the destination. Note this *will* change the source data outside of a {@link Command}.
	 * 
	 * @param descriptors
	 * @param sourceRoot
	 * @param destinationRoot
	 */
	public static void rewriteMappingDescriptors(final List<IMappingDescriptor> descriptors, final EObject sourceRoot, final EObject destinationRoot) {

		// Find the set of source object references are going to be transferred.
		
		// transferredObjects are objects which can "safely"(ish) be directly transferred into target data model 
		final Set<EObject> transferredObjects = new HashSet<EObject>();
		// remainingObjects are objects which need an equivalent to be found because they were non-contained objects
		final Set<EObject> remainingObjects = new HashSet<EObject>();

		final Map<EObject, EObject> sourceToDestinationMapping = new HashMap<EObject, EObject>();

		for (final IMappingDescriptor descriptor : descriptors) {
			// These are all the directly transferred objects
			for (final EObject eObj : descriptor.getAddedObjects()) {
				transferredObjects.add(eObj);

				// Process indirectly transferred objects
				processObject(eObj, transferredObjects, remainingObjects);
			}
			for (final EObject eObj : descriptor.getDestinationToSourceMap().values()) {
				transferredObjects.add(eObj);
				// Process indirectly transferred objects
				processObject(eObj, transferredObjects, remainingObjects);
			}
		}

		// Make sure remaining objects are really the remaining objects
		remainingObjects.removeAll(transferredObjects);

		// match up non-contained object references to corresponding objects if possible
		while (!remainingObjects.isEmpty()) {
			final EObject eObj = remainingObjects.iterator().next();
			final List<EReference> path = new LinkedList<EReference>();

			// Find containment path from object to root
			{
				EObject tmpObj = eObj.eContainer();
				while (tmpObj != sourceRoot && tmpObj != destinationRoot && tmpObj != null) {
					final EReference eContainmentFeature = tmpObj.eContainmentFeature();
					if (eContainmentFeature.isMany()) {
						throw new IllegalStateException("Unable to handle multiple containments in path to sourceRoot.");
					}
					path.add(0, eContainmentFeature);
					tmpObj = tmpObj.eContainer();
				}
				if (tmpObj == null) {
					throw new IllegalStateException("Object is not contained under sourceRoot");
				}

				if (tmpObj == destinationRoot) {
					// Already using a destination reference.
					remainingObjects.remove(eObj);
					continue;
				}
			}

			// Find destination equivalent container
			EObject destContainer = destinationRoot;
			for (final EReference ref : path) {
				destContainer = (EObject) destContainer.eGet(ref);
				if (destContainer == null) {
					// TODO: We could also just create a null mapping
					throw new IllegalStateException("Destination does not have correct containment structure. Missing shared data? [" + eObj.eClass().getName() + "]");
				}
			}

			// Build up equivalence mapping (including nulls);
			final Set<EObject> sourcesJustFound = new HashSet<EObject>();

			if (eObj.eContainingFeature().isMany()) {
				@SuppressWarnings("unchecked")
				final List<EObject> eObjectContainerObjects = (List<EObject>) eObj.eContainer().eGet(eObj.eContainingFeature());
				@SuppressWarnings("unchecked")
				final List<EObject> destObjectContainerObjects = (List<EObject>) destContainer.eGet(eObj.eContainingFeature());

				LOOP_SOURCE: for (final EObject sourceObject : eObjectContainerObjects) {
					for (final EObject destObject : destObjectContainerObjects) {
						if (MMXObjectEquivalance.equivalent(sourceObject, destObject)) {
							sourceToDestinationMapping.put(sourceObject, destObject);
							sourcesJustFound.add(sourceObject);
							continue LOOP_SOURCE;
						}
					}
					sourceToDestinationMapping.put(sourceObject, null);
					sourcesJustFound.add(sourceObject);
				}

			} else {
				final EObject sourceObject = (EObject) eObj.eContainer().eGet(eObj.eContainingFeature());
				final EObject destObject = (EObject) destContainer.eContainer().eGet(eObj.eContainingFeature());

				// Only object - assume they are equivalent, what ever they are
				sourceToDestinationMapping.put(sourceObject, destObject);
				sourcesJustFound.add(sourceObject);
			}

			final Map<EObject, Collection<Setting>> usagesBySource = EcoreUtil.UsageCrossReferencer.findAll(sourcesJustFound, transferredObjects);
			// Perform replacements
			for (final EObject source : sourcesJustFound) {
				// Update cross-references
				final Collection<EStructuralFeature.Setting> usages = usagesBySource.get(source);
				if (usages != null) {
					for (final EStructuralFeature.Setting setting : usages) {
						EcoreUtil.replace(setting.getEObject(), setting.getEStructuralFeature(), source, sourceToDestinationMapping.get(source));
					}
				}
			}
			// This ensures we eventually terminate the main loop
			assert sourcesJustFound.contains(eObj);
			remainingObjects.removeAll(sourcesJustFound);
		}

	}

	/**
	 * Updates "transferredObjects" to include all recursively contained references of "eObj", adding non-contained references 
	 * (including those of recursively contained objects) to "remainingObjects"
	 * 
	 * @param eObj  
	 * @param transferredObjects   
	 * @param remainingObjects
	 */
	private static void processObject(final EObject eObj, final Set<EObject> transferredObjects, final Set<EObject> remainingObjects) {
		final EClass eClass = eObj.eClass();
		for (final EReference ref : eClass.getEAllReferences()) {
			if (eObj.eIsSet(ref)) {
				if (ref.isMany()) {
					final List<?> objects = (List<?>) eObj.eGet(ref);
					for (final Object obj : objects) {
						if (obj instanceof EObject) {
							if (ref.isContainment()) {
								transferredObjects.add((EObject) obj);
								processObject((EObject) obj, transferredObjects, remainingObjects);
							} else {
								remainingObjects.add((EObject) obj);
							}
						}
					}
				} else {
					final Object obj = eObj.eGet(ref);
					if (obj instanceof EObject) {
						if (ref.isContainment()) {
							transferredObjects.add((EObject) obj);
							processObject((EObject) obj, transferredObjects, remainingObjects);
						} else {
							remainingObjects.add((EObject) obj);
						}
					}
				}

			}

		}

	}

	/**
	 * Take a {@link List} of {@link IMappingDescriptor}s and "apply" them to the rootObject (this should be the same as the {@link IMappingDescriptor#getDestinationContainer()} generating a
	 * {@link Command} to perform the actual changes required. This will move the contents of the source container into the destination container. If the source data is not to be changed, consider
	 * copying the data first. See {@link EcoreUtil#copyAll(Collection)}.
	 * 
	 * @param domain
	 * @param rootObject
	 * @param descriptors
	 * @return
	 */
	public static Command applyMappingDescriptors(final EditingDomain domain, final EObject rootObject, final List<IMappingDescriptor> descriptors) {

		return new MergeCommand(domain, rootObject, descriptors);
	}

	private static class MergeCommand extends CompoundCommand {

		/**
		 * This caches the label.
		 */
		protected static final String LABEL = "Scenario Data Merge";

		/**
		 * This caches the description.
		 */
		protected static final String DESCRIPTION = "Merge data between scenarios";

		/**
		 * This constructs a command that deletes the objects in the given collection.
		 */
		public MergeCommand(EditingDomain domain, final EObject rootObject, final List<IMappingDescriptor> descriptors) {
			super(0, LABEL, DESCRIPTION);
			this.domain = domain;
			this.rootObject = rootObject;
			this.descriptors = descriptors;
		}

		protected EditingDomain domain;
		protected EObject rootObject;
		protected List<IMappingDescriptor> descriptors;

		@Override
		protected boolean prepare() {
			prepareCommand();
			if (commandList.isEmpty() && descriptors.isEmpty()) {
				return false;
			} else {
				for (Command command : commandList) {
					if (!command.canExecute()) {
						return false;
					}
				}

				return true;
			}
		}

		protected void prepareCommand() {
			// Perform additions
			for (final IMappingDescriptor descriptor : descriptors) {

				final EReference ref = descriptor.getReference();
				if (ref.isContainment() && ref.isMany()) {
					final List<EObject> addedObjects = descriptor.getAddedObjects();
					if (!addedObjects.isEmpty()) {
						append(AddCommand.create(domain, descriptor.getDestinationContainer(), descriptor.getReference(), addedObjects));
					}
				}
			}

		}

		@Override
		public void execute() {
			super.execute();

			final Collection<EObject> emfObjectsToSearch = Collections.singleton(rootObject);

			final Set<EObject> objectsOfInterest = new HashSet<EObject>();

			final Map<EObject, EObject> bigMapOfDestinationToSource = new HashMap<EObject, EObject>();
			for (final IMappingDescriptor descriptor : descriptors) {
				bigMapOfDestinationToSource.putAll(descriptor.getDestinationToSourceMap());
			}
			objectsOfInterest.addAll(bigMapOfDestinationToSource.keySet());

			// Perform replacements
			for (final Map.Entry<EObject, EObject> e : bigMapOfDestinationToSource.entrySet()) {
				final EObject source = e.getValue();
				final EObject dest = e.getKey();
				// Update cross-references
				final Collection<EStructuralFeature.Setting> usages = EcoreUtil.UsageCrossReferencer.find(dest, emfObjectsToSearch);
				if (usages != null) {
					for (final EStructuralFeature.Setting setting : usages) {
						if (setting.getEStructuralFeature().isMany()) {
							appendAndExecute(ReplaceCommand.create(domain, setting.getEObject(), setting.getEStructuralFeature(), dest, Collections.<EObject> singleton(source)));
						} else {
							appendAndExecute(SetCommand.create(domain, setting.getEObject(), setting.getEStructuralFeature(), source));
						}
					}
				}
				// Update containers
				if (dest.eContainmentFeature().isMany()) {
					appendAndExecute(ReplaceCommand.create(domain, dest.eContainer(), dest.eContainmentFeature(), dest, Collections.<EObject> singleton(source)));
				} else {
					appendAndExecute(SetCommand.create(domain, dest.eContainer(), dest.eContainmentFeature(), source));
				}
			}

			// Update other refs
			for (final IMappingDescriptor descriptor : descriptors) {
				final EReference ref = descriptor.getReference();
				if (!ref.isContainment() && !ref.isMany()) {
					if (descriptor.getAddedObjects().isEmpty()) {
						appendAndExecute(SetCommand.create(domain, descriptor.getDestinationContainer(), descriptor.getReference(), SetCommand.UNSET_VALUE));
					} else {
						Preconditions.checkState(descriptor.getAddedObjects().size() == 1);
						appendAndExecute(SetCommand.create(domain, descriptor.getDestinationContainer(), descriptor.getReference(), descriptor.getAddedObjects().get(0)));
					}
				}
			}

			// Perform deletions
			for (final IMappingDescriptor descriptor : descriptors) {

				final EReference ref = descriptor.getReference();
				if (ref.isContainment() && ref.isMany()) {
					final Collection<EObject> removedObjects = descriptor.getRemovedObjects();
					if (!removedObjects.isEmpty()) {
						appendAndExecute(DeleteCommand.create(domain, removedObjects));
					}
				}
			}
		}
	}

}
