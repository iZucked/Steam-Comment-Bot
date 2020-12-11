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
import com.mmxlabs.models.lng.nominations.NominationsParameters;

public class GeneratedNominationsProvider extends EContentAdapter {

	private final @NonNull NominationsModel nominationsModel;
	private SoftReference<List<AbstractNomination>> generatedNominations;

	// To get round circular references.
	public interface NominationsGenerator {
		List<AbstractNomination> generateNominationsFromSpecs(@NonNull NominationsModel nominationsModel);
	}

	public static GeneratedNominationsProvider getOrCreate(final @NonNull NominationsModel nominationsModel, final EObject... relatedModels) {
		synchronized (nominationsModel) {
			// Find existing if present...
			for (Object o : nominationsModel.eAdapters()) {
				if (o instanceof GeneratedNominationsProvider) {
					GeneratedNominationsProvider provider = (GeneratedNominationsProvider) o;

					// Make sure it is fully hooked up to related models.
					for (EObject related : relatedModels) {
						if (!related.eAdapters().contains(provider)) {
							related.eAdapters().add(provider);
						}
					}
					return provider;
				}
			}
			// Else create a new one
			return new GeneratedNominationsProvider(nominationsModel, relatedModels);
		}
	}

	private GeneratedNominationsProvider(final @NonNull NominationsModel nominationsModel, final EObject... relatedModels) {
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
		if (notification.getNotifier() instanceof NominationsParameters) {
			//We don't care about changes to 1M,3M,All,Start,End date in Nominations view as is just filtering.
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
		} else {
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
