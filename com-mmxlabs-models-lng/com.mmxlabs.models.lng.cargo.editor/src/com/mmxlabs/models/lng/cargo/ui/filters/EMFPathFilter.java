package com.mmxlabs.models.lng.cargo.ui.filters;

import java.util.Objects;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerFilter;

import com.mmxlabs.models.util.emfpath.IEMFPath;

public class EMFPathFilter extends ViewerFilter {
	@Override
	public int hashCode() {
		return Objects.hash(nonEObjectBehaviour, path, value);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (obj instanceof EMFPathFilter other) {
			return nonEObjectBehaviour == other.nonEObjectBehaviour && Objects.equals(path, other.path) && Objects.equals(value, other.value);
		}
		return false;

	}

	private final IEMFPath path;
	private final EObject value;
	private final boolean nonEObjectBehaviour;

	@Override
	public boolean select(Viewer viewer, Object parentElement, Object element) {
		if (element instanceof EObject object) {
			return value.equals(path.get(object));
		}
		return nonEObjectBehaviour;
	}

	public EMFPathFilter(IEMFPath path, EObject value, boolean nonEObjectBehaviour) {
		this.path = path;
		this.value = value;
		this.nonEObjectBehaviour = nonEObjectBehaviour;

	}

	public EMFPathFilter(IEMFPath path, EObject value) {
		this(path, value, false);


	}

	public IEMFPath getPath() {
		return this.path;
	}

	public EObject getValue() {
		return this.value;
	}
}
