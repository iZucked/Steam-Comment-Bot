package scenario.presentation.cargoeditor;

import java.util.List;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.swt.widgets.Composite;

public interface IFeatureEditor {
	public interface IFeatureManipulator {
		public String getStringValue(EObject o);
		public void setFromEditorValue(EObject o, Object value);
		public boolean canModify(EObject row);
		public CellEditor createCellEditor(final Composite parent);
		public Object getEditorValue(EObject row);
	}

	public IFeatureManipulator getFeatureManipulator(final List<EReference> path,
			final EStructuralFeature field);
}
