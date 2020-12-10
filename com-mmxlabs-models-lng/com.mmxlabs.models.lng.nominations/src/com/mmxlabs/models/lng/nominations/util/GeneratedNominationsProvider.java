package com.mmxlabs.models.lng.nominations.util;

import java.lang.ref.SoftReference;
import java.util.List;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.util.EContentAdapter;
import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.Nullable;

import com.mmxlabs.models.lng.nominations.AbstractNomination;
import com.mmxlabs.models.lng.nominations.NominationsModel;

public class GeneratedNominationsProvider extends EContentAdapter {

	private final @NonNull NominationsModel nominationsModel;
	private SoftReference<List<AbstractNomination>> generatedNominations;
	
	//To get round circular references.
	public interface NominationsGenerator {
		List<AbstractNomination> generateNominationsFromSpecs(@NonNull NominationsModel nominationsModel);
	}
	
	public GeneratedNominationsProvider(final @NonNull NominationsModel nominationsModel, final EObject...relatedModels) {
		this.nominationsModel = nominationsModel;
		this.nominationsModel.eAdapters().add(this);
		for (EObject relatedModel : relatedModels) {
			relatedModel.eAdapters().add(this);
		}
		this.generatedNominations = null;
	}
	
	@Override
	public void notifyChanged(final @Nullable Notification notification) {
		super.notifyChanged(notification);
		if (notification != null && notification.isTouch()) {
			return;
		}
		clearCache();
	}

	private void clearCache() {
		generatedNominations = null;
	}

	public List<AbstractNomination> getGeneratedNominations(NominationsGenerator ng) {
		if (this.generatedNominations == null) {
			return regenerate(ng);
		}
		else {
			List<AbstractNomination> nominations = this.generatedNominations.get();
			if (nominations == null) {
				nominations = regenerate(ng);
			}
			return nominations;
		}
	}

	private List<AbstractNomination> regenerate(NominationsGenerator ng) {
		List<AbstractNomination> nominations = ng.generateNominationsFromSpecs(this.nominationsModel);
		generatedNominations = new SoftReference<>(nominations);
		return nominations;
	}
}
