package com.mmxlabs.models.ui.editors.util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.eclipse.emf.common.command.AbstractCommand;
import org.eclipse.emf.common.command.Command;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.change.ChangeDescription;
import org.eclipse.emf.ecore.change.util.ChangeRecorder;
import org.eclipse.emf.ecore.util.EcoreUtil;

/**
 * A class to maintain changes in e.g a Dialog where we wish to edit objects but have a Cancel button to trash changes.s Based on code from EMF forum
 * http://www.eclipse.org/forums/index.php?t=msg&th=200576/
 * 
 * @since 3.0
 */
public class DialogEcoreCopier {
	private final List<EObject> originals = new ArrayList<EObject>();
	private final List<EObject> copies = new ArrayList<EObject>();
	private final EcoreUtil.Copier copier;
	private ChangeRecorder changeRecorder;

	public DialogEcoreCopier() {
		copier = new EcoreUtil.Copier();
	}

	public EObject copy(final EObject original) {
		final EObject copy = copier.copy(original);
		originals.add(original);
		copies.add(copy);

		return copy;
	}

	public List<EObject> copyAll(final List<EObject> theOriginals) {

		final List<EObject> newCopies = new ArrayList<EObject>(theOriginals.size());
		for (final EObject original : theOriginals) {
			newCopies.add(copy(original));
		}

		return copies;
	}

	public void record() {
		copier.copyReferences();

		this.changeRecorder = new ChangeRecorder(copies);
	}

	public Command createEditCommand() {
		if (changeRecorder == null) {
			throw new IllegalStateException("Change record already cleaned up.");
		}

		final ChangeDescription changeDescription = changeRecorder.endRecording();
		changeRecorder = null;

		if (isEmpty(changeDescription)) {
			return null;
		}

		changeDescription.applyAndReverse();
		rewriteReferences(changeDescription);

		return new ChangeCommand(changeDescription, originals);
	}

	private boolean isEmpty(final ChangeDescription changeDescription) {
		return changeDescription.getObjectChanges().isEmpty() && changeDescription.getObjectsToAttach().isEmpty() && changeDescription.getObjectsToDetach().isEmpty()
				&& changeDescription.getResourceChanges().isEmpty();
	}

	private void rewriteReferences(final EObject object) {
		final Map<EObject, Collection<EStructuralFeature.Setting>> usagesByCopy = EcoreUtil.UsageCrossReferencer.findAll(copier.values(), object);

		for (final Map.Entry<EObject, EObject> entry : copier.entrySet()) {
			final EObject original = entry.getKey();
			final EObject copy = entry.getValue();

			final Collection<EStructuralFeature.Setting> usages = usagesByCopy.get(copy);
			if (usages != null) {
				for (final EStructuralFeature.Setting setting : usages) {
					if (needsToBeReplaced(setting)) {
						EcoreUtil.replace(setting, copy, original);
					}
				}
			}
		}
	}

	private boolean needsToBeReplaced(final EStructuralFeature.Setting setting) {
		final EStructuralFeature feature = setting.getEStructuralFeature();
		return !feature.isDerived() && feature.isChangeable();
	}

	/**
	 * Class to wrap a {@link ChangeDescription} in a {@link Command}
	 * 
	 */
	private static class ChangeCommand extends AbstractCommand {
		private ChangeDescription changeDescription;
		private Collection<?> affectedObjects;

		public ChangeCommand(final ChangeDescription changeDescription, final Collection<?> affectedObjects) {
			this.changeDescription = changeDescription;
			this.affectedObjects = affectedObjects;
		}

		@Override
		public Collection<?> getAffectedObjects() {
			return affectedObjects == null ? Collections.emptyList() : affectedObjects;
		}

		@Override
		public void dispose() {
			changeDescription = null;
			affectedObjects = null;
			super.dispose();
		}

		@Override
		public boolean canExecute() {
			return true;
		}

		@Override
		public void execute() {
			changeDescription.applyAndReverse();
		}

		@Override
		public void undo() {
			changeDescription.applyAndReverse();
		}

		@Override
		public void redo() {
			changeDescription.applyAndReverse();
		}
	}
}
