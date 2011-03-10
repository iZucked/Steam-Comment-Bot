/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2011
 */

package scenario.presentation.cargoeditor;

import java.util.List;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Composite;

import scenario.cargo.Cargo;
import scenario.cargo.Slot;

/**
 * A big ugly interface to let the {@link EObjectDetailView} and
 * {@link EObjectEditorViewerPane} display and edit EObject attributes.
 * 
 * The interface only has a single method,
 * {@link #getFeatureManipulator(List, EStructuralFeature)}, creates an
 * {@link IFeatureManipulator} for editing the {@link EStructuralFeature} found
 * at a path defined by a list of {@link EReference}s from a root object.
 * 
 * @author hinton
 * 
 */
public interface IFeatureEditor {
	/**
	 * IFeatureManipulator defines methods for displaying and editing a field on
	 * a model object, possibly on another object contained a few levels within
	 * the toplevel. This is because (for example) the {@link TableViewer}
	 * inside an {@link EObjectEditorViewerPane} knows when (say) a
	 * {@link Cargo} is selected, but we want to display the details of a
	 * {@link Slot} within it.
	 * 
	 * @author hinton
	 * 
	 */
	public interface IFeatureManipulator {
		/**
		 * Render a string value for the given model object, usually to go in a
		 * table. Note that row is the top-level object, and the manipulator may
		 * be for a child of that object.
		 * 
		 * @param row
		 *            a top-level model object
		 * @return a string showing the feature we want to show
		 */
		public String getStringValue(EObject row);

		/**
		 * Set the structural feature this manipulator deals with on the object
		 * row to a value which came out of a CellEditor created with
		 * {@link #createCellEditor(Composite)}
		 * 
		 * @param row
		 * @param value
		 */
		public void setFromEditorValue(EObject row, Object value);

		/**
		 * Return true if this feature should be editable on the given object.
		 * 
		 * This also gives an opportunity to do any behind-the-scenes wangling
		 * with the CellEditor. Ugly but necessary.
		 * 
		 * @param row
		 * @return
		 */
		public boolean canModify(final EObject row);

		/**
		 * Create a {@link CellEditor} for modifying this field in a table.
		 * 
		 * The CellEditor will have its value provided by
		 * {@link #getEditorValue(EObject)}, and when the CellEditor is used to
		 * make a change the value it produces will be passed to
		 * {@link #setFromEditorValue(EObject, Object)} to apply the change.
		 * 
		 * @param parent
		 * @return
		 */
		public CellEditor createCellEditor(final Composite parent);

		/**
		 * Transform the value of this feature into an object suitable for use
		 * by the {@link CellEditor} from {@link #createCellEditor(Composite)}.
		 * 
		 * @param row
		 * @return an editor value
		 */
		public Object getEditorValue(EObject row);

		/**
		 * Get an image for this feature on this object, to draw in a table
		 * view. This method is basically only hear because SWT checkboxes are
		 * broken. (see the boolean feature manipulator in
		 * {@link DefaultAttributeEditor} to see why.)
		 * 
		 * @param object
		 * @param columnImage
		 * @return
		 */
		public Image getImageValue(EObject object, Image columnImage);
	}

	/**
	 * Get a feature manipulator for the given path and field. When the feature
	 * manipulator is asked to render a feature on an object, should first chase
	 * all the references in path before getting field.
	 * 
	 * @param path
	 * @param field
	 * @return
	 */
	public IFeatureManipulator getFeatureManipulator(
			final List<EReference> path, final EStructuralFeature field);
}
