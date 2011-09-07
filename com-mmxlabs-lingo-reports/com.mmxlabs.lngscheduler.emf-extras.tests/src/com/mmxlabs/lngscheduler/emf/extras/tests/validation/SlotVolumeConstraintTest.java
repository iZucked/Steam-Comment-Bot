package com.mmxlabs.lngscheduler.emf.extras.tests.validation;

import org.eclipse.emf.validation.EMFEventType;
import org.eclipse.emf.validation.IValidationContext;
import org.eclipse.emf.validation.model.IConstraintStatus;
import org.jmock.Expectations;
import org.jmock.Mockery;
import org.junit.Test;

import scenario.Scenario;
import scenario.cargo.Slot;

import com.mmxlabs.lngscheduler.emf.extras.validation.SlotVolumeConstraint;

/**
 * Details from case 171:
 * 
 * SlotVolumeConstraint is supposed to check that a slot's min and max volume limits are set sensibly, i.e. that the minimum volume is not greater than the maximum volume.
 * 
 * Although slot volumes limits can be queried by the Slot.getMinQuantity() and Slot.getMaxQuantity() methods, these values are "unsettable", and have corresponding Slot.isSetMinQuantity() and
 * isSetMaxQuantity() methods. If the values aren't set, then the min and max values are taken from the slot's contract instead, and so on
 * 
 * However, all the code uses the convenience method Slot.getOrContractMinQuantity(Scenario) and its Max counterpart, so you can Mock a slot which has these.
 * 
 * A little fiddle here is that the constraint needs to figure out the Scenario which contains a slot, your mock slot will need to have Slot.eContainer() and Slot.eContainingFeature() methods.
 * eContainer() should return a mocked Scenario and eContainingFeature() can probably return null. Something similar will be required in any other constraints that need to find out which Scenario the
 * object they are validating belongs to, as the IValidationContext only gives the Slot or whatever.
 * 
 */
public class SlotVolumeConstraintTest {

	private final Mockery context = new Mockery();

	/**
	 * Check that the constraint succeeds if everything is correct.
	 */
	@Test
	public void testSlotVolumeConstraintCorrectMinMax() {

		// The max and min to test. They are correct, i.e. non-negative and the
		// max is greater than the min.
		final int max = 2;
		final int min = 1;

		testSlotVolumeConstraint(true, min, max);
	}

	/**
	 * Test that an equal min and max still work.
	 */
	@Test
	public void testSlotVolumeConstraintEqualMinMax() {
		final int max = 2;
		final int min = 2;

		testSlotVolumeConstraint(true, max, min);
	}

	/**
	 * Test a max smaller than a min produces a failure.
	 */
	@Test
	public void testSlotVolumeConstraintMaxLessThanMin() {

		final int min = 2;
		final int max = 1;

		testSlotVolumeConstraint(false, min, max);
	}

	/**
	 * Test a negative minimum fails
	 */
	@Test
	public void testSlotVolumeConstraintNegativeMinQuantity() {

		final int min = -1;
		// The max doesn't matter and won't get checked, but need a value
		// anyway.
		final int max = 2;

		testSlotVolumeConstraint(false, min, max);
	}

	/**
	 * Test a negative maximum fails.
	 */
	@Test
	public void testSlotVolumeConstraintNegativeMaxQuantity() {

		final int min = 1;
		final int max = -1;

		testSlotVolumeConstraint(false, min, max);
	}

	/**
	 * Test that a null scenario still produces a success.
	 */
	@Test
	public void testSlotVolumeConstraintNullScenario() {

		// This is the constraint we will be testing
		final SlotVolumeConstraint constraint = new SlotVolumeConstraint();

		final Slot slot = context.mock(Slot.class);

		final IValidationContext validationContext = context.mock(IValidationContext.class);
		final IConstraintStatus successStatus = context.mock(IConstraintStatus.class);

		context.checking(new Expectations() {
			{
				// This should return a scenario, but we set it to null to test
				// that the constraint still succeeds.
				atLeast(1).of(slot).eContainer();
				will(returnValue(null));

				atLeast(1).of(slot).eContainingFeature();
				will(returnValue(null));

				atLeast(1).of(validationContext).getTarget();
				will(returnValue(slot));

				atLeast(1).of(validationContext).getEventType();
				will(returnValue(EMFEventType.NULL));

				atLeast(1).of(validationContext).createSuccessStatus();
				will(returnValue(successStatus));
			}
		});

		constraint.validate(validationContext);

		context.assertIsSatisfied();
	}

	private void testSlotVolumeConstraint(final boolean expectSuccess, final int min, final int max) {

		final Mockery context = new Mockery();
		// This is the constraint we will be testing
		final SlotVolumeConstraint constraint = new SlotVolumeConstraint();

		final Slot slot = context.mock(Slot.class);
		final Scenario scenario = context.mock(Scenario.class);

		final IValidationContext validationContext = context.mock(IValidationContext.class);
		final IConstraintStatus resultStatus = context.mock(IConstraintStatus.class);

		context.checking(new Expectations() {
			{
				atLeast(1).of(slot).eContainer();
				will(returnValue(scenario));

				atLeast(1).of(slot).eContainingFeature();
				will(returnValue(null));

				atLeast(1).of(slot).getSlotOrContractMinQuantity(scenario);
				will(returnValue(min));
				// The test may fail at the checking the minimum and never get
				// to check the max, so put atLeast(0)
				atLeast(0).of(slot).getSlotOrContractMaxQuantity(scenario);
				will(returnValue(max));

				atLeast(1).of(validationContext).getTarget();
				will(returnValue(slot));

				atLeast(1).of(validationContext).getEventType();
				will(returnValue(EMFEventType.NULL));

				if (expectSuccess) {
					atLeast(1).of(validationContext).createSuccessStatus();
					will(returnValue(resultStatus));
				} else {
					atLeast(1).of(validationContext).createFailureStatus();
					will(returnValue(resultStatus));
				}
			}
		});

		constraint.validate(validationContext);

		context.assertIsSatisfied();
	}
}
