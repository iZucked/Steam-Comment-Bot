/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2012
 * All rights reserved.
 */
package scenario.optimiser.lso;

/**
 * <!-- begin-user-doc --> A representation of the model object '<em><b>Random Move Generator Settings</b></em>'. <!-- end-user-doc -->
 * 
 * <p>
 * The following features are supported:
 * <ul>
 * <li>{@link scenario.optimiser.lso.RandomMoveGeneratorSettings#isUsing2over2 <em>Using2over2</em>}</li>
 * <li>{@link scenario.optimiser.lso.RandomMoveGeneratorSettings#isUsing3over2 <em>Using3over2</em>}</li>
 * <li>{@link scenario.optimiser.lso.RandomMoveGeneratorSettings#isUsing4over1 <em>Using4over1</em>}</li>
 * <li>{@link scenario.optimiser.lso.RandomMoveGeneratorSettings#isUsing4over2 <em>Using4over2</em>}</li>
 * <li>{@link scenario.optimiser.lso.RandomMoveGeneratorSettings#getWeightFor2opt2 <em>Weight For2opt2</em>}</li>
 * <li>{@link scenario.optimiser.lso.RandomMoveGeneratorSettings#getWeightFor3opt2 <em>Weight For3opt2</em>}</li>
 * <li>{@link scenario.optimiser.lso.RandomMoveGeneratorSettings#getWeightFor4opt1 <em>Weight For4opt1</em>}</li>
 * <li>{@link scenario.optimiser.lso.RandomMoveGeneratorSettings#getWeightFor4opt2 <em>Weight For4opt2</em>}</li>
 * </ul>
 * </p>
 * 
 * @see scenario.optimiser.lso.LsoPackage#getRandomMoveGeneratorSettings()
 * @model
 * @generated
 */
public interface RandomMoveGeneratorSettings extends MoveGeneratorSettings {
	/**
	 * Returns the value of the '<em><b>Using2over2</b></em>' attribute. <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Using2over2</em>' attribute isn't clear, there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * 
	 * @return the value of the '<em>Using2over2</em>' attribute.
	 * @see #setUsing2over2(boolean)
	 * @see scenario.optimiser.lso.LsoPackage#getRandomMoveGeneratorSettings_Using2over2()
	 * @model
	 * @generated
	 */
	boolean isUsing2over2();

	/**
	 * Sets the value of the '{@link scenario.optimiser.lso.RandomMoveGeneratorSettings#isUsing2over2 <em>Using2over2</em>}' attribute. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @param value
	 *            the new value of the '<em>Using2over2</em>' attribute.
	 * @see #isUsing2over2()
	 * @generated
	 */
	void setUsing2over2(boolean value);

	/**
	 * Returns the value of the '<em><b>Using3over2</b></em>' attribute. <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Using3over2</em>' attribute isn't clear, there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * 
	 * @return the value of the '<em>Using3over2</em>' attribute.
	 * @see #setUsing3over2(boolean)
	 * @see scenario.optimiser.lso.LsoPackage#getRandomMoveGeneratorSettings_Using3over2()
	 * @model
	 * @generated
	 */
	boolean isUsing3over2();

	/**
	 * Sets the value of the '{@link scenario.optimiser.lso.RandomMoveGeneratorSettings#isUsing3over2 <em>Using3over2</em>}' attribute. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @param value
	 *            the new value of the '<em>Using3over2</em>' attribute.
	 * @see #isUsing3over2()
	 * @generated
	 */
	void setUsing3over2(boolean value);

	/**
	 * Returns the value of the '<em><b>Using4over1</b></em>' attribute. <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Using4over1</em>' attribute isn't clear, there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * 
	 * @return the value of the '<em>Using4over1</em>' attribute.
	 * @see #setUsing4over1(boolean)
	 * @see scenario.optimiser.lso.LsoPackage#getRandomMoveGeneratorSettings_Using4over1()
	 * @model
	 * @generated
	 */
	boolean isUsing4over1();

	/**
	 * Sets the value of the '{@link scenario.optimiser.lso.RandomMoveGeneratorSettings#isUsing4over1 <em>Using4over1</em>}' attribute. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @param value
	 *            the new value of the '<em>Using4over1</em>' attribute.
	 * @see #isUsing4over1()
	 * @generated
	 */
	void setUsing4over1(boolean value);

	/**
	 * Returns the value of the '<em><b>Using4over2</b></em>' attribute. <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Using4over2</em>' attribute isn't clear, there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * 
	 * @return the value of the '<em>Using4over2</em>' attribute.
	 * @see #setUsing4over2(boolean)
	 * @see scenario.optimiser.lso.LsoPackage#getRandomMoveGeneratorSettings_Using4over2()
	 * @model
	 * @generated
	 */
	boolean isUsing4over2();

	/**
	 * Sets the value of the '{@link scenario.optimiser.lso.RandomMoveGeneratorSettings#isUsing4over2 <em>Using4over2</em>}' attribute. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @param value
	 *            the new value of the '<em>Using4over2</em>' attribute.
	 * @see #isUsing4over2()
	 * @generated
	 */
	void setUsing4over2(boolean value);

	/**
	 * Returns the value of the '<em><b>Weight For2opt2</b></em>' attribute. The default value is <code>"1"</code>. <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Weight For2opt2</em>' attribute isn't clear, there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * 
	 * @return the value of the '<em>Weight For2opt2</em>' attribute.
	 * @see #setWeightFor2opt2(double)
	 * @see scenario.optimiser.lso.LsoPackage#getRandomMoveGeneratorSettings_WeightFor2opt2()
	 * @model default="1" required="true"
	 * @generated
	 */
	double getWeightFor2opt2();

	/**
	 * Sets the value of the '{@link scenario.optimiser.lso.RandomMoveGeneratorSettings#getWeightFor2opt2 <em>Weight For2opt2</em>}' attribute. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @param value
	 *            the new value of the '<em>Weight For2opt2</em>' attribute.
	 * @see #getWeightFor2opt2()
	 * @generated
	 */
	void setWeightFor2opt2(double value);

	/**
	 * Returns the value of the '<em><b>Weight For3opt2</b></em>' attribute. The default value is <code>"1"</code>. <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Weight For3opt2</em>' attribute isn't clear, there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * 
	 * @return the value of the '<em>Weight For3opt2</em>' attribute.
	 * @see #setWeightFor3opt2(double)
	 * @see scenario.optimiser.lso.LsoPackage#getRandomMoveGeneratorSettings_WeightFor3opt2()
	 * @model default="1" required="true"
	 * @generated
	 */
	double getWeightFor3opt2();

	/**
	 * Sets the value of the '{@link scenario.optimiser.lso.RandomMoveGeneratorSettings#getWeightFor3opt2 <em>Weight For3opt2</em>}' attribute. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @param value
	 *            the new value of the '<em>Weight For3opt2</em>' attribute.
	 * @see #getWeightFor3opt2()
	 * @generated
	 */
	void setWeightFor3opt2(double value);

	/**
	 * Returns the value of the '<em><b>Weight For4opt1</b></em>' attribute. The default value is <code>"1"</code>. <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Weight For4opt1</em>' attribute isn't clear, there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * 
	 * @return the value of the '<em>Weight For4opt1</em>' attribute.
	 * @see #setWeightFor4opt1(double)
	 * @see scenario.optimiser.lso.LsoPackage#getRandomMoveGeneratorSettings_WeightFor4opt1()
	 * @model default="1" required="true"
	 * @generated
	 */
	double getWeightFor4opt1();

	/**
	 * Sets the value of the '{@link scenario.optimiser.lso.RandomMoveGeneratorSettings#getWeightFor4opt1 <em>Weight For4opt1</em>}' attribute. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @param value
	 *            the new value of the '<em>Weight For4opt1</em>' attribute.
	 * @see #getWeightFor4opt1()
	 * @generated
	 */
	void setWeightFor4opt1(double value);

	/**
	 * Returns the value of the '<em><b>Weight For4opt2</b></em>' attribute. The default value is <code>"1"</code>. <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Weight For4opt2</em>' attribute isn't clear, there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * 
	 * @return the value of the '<em>Weight For4opt2</em>' attribute.
	 * @see #setWeightFor4opt2(double)
	 * @see scenario.optimiser.lso.LsoPackage#getRandomMoveGeneratorSettings_WeightFor4opt2()
	 * @model default="1" required="true"
	 * @generated
	 */
	double getWeightFor4opt2();

	/**
	 * Sets the value of the '{@link scenario.optimiser.lso.RandomMoveGeneratorSettings#getWeightFor4opt2 <em>Weight For4opt2</em>}' attribute. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @param value
	 *            the new value of the '<em>Weight For4opt2</em>' attribute.
	 * @see #getWeightFor4opt2()
	 * @generated
	 */
	void setWeightFor4opt2(double value);

} // RandomMoveGeneratorSettings
