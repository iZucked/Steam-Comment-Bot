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
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.EStructuralFeature;
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
 * @since 4.1
 * 
 */
public class EMFModelMergeTools {

	/**
	 * Compare two EMF container objects which share the same containment feature and analyse the differences. Generate a {@link IMappingDescriptor} to describe these differences.
	 * 
	 * @param sourceContainer
	 * @param destinationContainer
	 * @param ref
	 * @return
	 */
	public static IMappingDescriptor generateMappingDescriptor(final EObject sourceContainer, final EObject destinationContainer, final EReference ref) {

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
	 * Take a {@link List} of {@link IMappingDescriptor}s and "apply" them to the rootObject (this should be the same as the {@link IMappingDescriptor#getDestinationContainer()} generating a
	 * {@link Command} to perform the actual changes required. This will move the contents of the source container into the destination container. If the source data is not to be changed, consider
	 * copying the data first. See {@link EcoreUtil#copyAll(Collection)}.
	 * 
	 * @param domain
	 * @param rootObject
	 * @param descriptors
	 * @return
	 */
	public static Command patchInMappingDescriptors(final EditingDomain domain, final EObject rootObject, final List<IMappingDescriptor> descriptors) {
		final Collection<EObject> emfObjectsToSearch = Collections.singleton(rootObject);

		final Set<EObject> objectsOfInterest = new HashSet<EObject>();

		final Map<EObject, EObject> bigMapOfDestinationToSource = new HashMap<EObject, EObject>();
		for (final IMappingDescriptor descriptor : descriptors) {
			bigMapOfDestinationToSource.putAll(descriptor.getDestinationToSourceMap());
		}
		objectsOfInterest.addAll(bigMapOfDestinationToSource.keySet());

		final Map<EObject, Collection<EStructuralFeature.Setting>> usagesByCopy = EcoreUtil.UsageCrossReferencer.findAll(objectsOfInterest, emfObjectsToSearch);
		final CompoundCommand cmd = new CompoundCommand();
		// Perform replacements
		for (final Map.Entry<EObject, EObject> e : bigMapOfDestinationToSource.entrySet()) {
			final EObject source = e.getValue();
			final EObject dest = e.getKey();
			// Update cross-references
			final Collection<EStructuralFeature.Setting> usages = usagesByCopy.get(dest);
			if (usages != null) {
				for (final EStructuralFeature.Setting setting : usages) {
					if (setting.getEStructuralFeature().isMany()) {
						cmd.append(ReplaceCommand.create(domain, setting.getEObject(), setting.getEStructuralFeature(), dest, Collections.<EObject> singleton(source)));
					} else {
						cmd.append(SetCommand.create(domain, setting.getEObject(), setting.getEStructuralFeature(), source));
					}
				}
			}
			// Update containers
			if (dest.eContainmentFeature().isMany()) {
				cmd.append(ReplaceCommand.create(domain, dest.eContainer(), dest.eContainmentFeature(), dest, Collections.<EObject> singleton(source)));
			} else {
				cmd.append(SetCommand.create(domain, dest.eContainer(), dest.eContainmentFeature(), source));
			}
		}

		// Perform additions
		for (final IMappingDescriptor descriptor : descriptors) {
			final List<EObject> addedObjects = descriptor.getAddedObjects();
			if (!addedObjects.isEmpty()) {
				cmd.append(AddCommand.create(domain, descriptor.getDestinationContainer(), descriptor.getReference(), addedObjects));
			}
		}

		// Perform deletions
		for (final IMappingDescriptor descriptor : descriptors) {
			final Collection<EObject> removedObjects = descriptor.getRemovedObjects();
			if (!removedObjects.isEmpty()) {
				cmd.append(DeleteCommand.create(domain, removedObjects));
			}
		}

		return cmd;
	}
}
