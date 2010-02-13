

import org.jmock.Expectations;
import org.jmock.Mockery;
import org.jmock.integration.junit4.JMock;
import org.jmock.integration.junit4.JUnit4Mockery;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.acme.task.ITaskMonitor;
import com.acme.task.impl.WrappedTaskMonitor;

/**
 * Ensure the {@link WrappedTaskMonitor} correctly invokes methods on the target
 * object.
 */
@RunWith(JMock.class)
public class WrappedTaskMonitorTest {

	Mockery context = new JUnit4Mockery();

	@Test
	public void testGetTotalWork() {

		final ITaskMonitor target = context.mock(ITaskMonitor.class);

		final WrappedTaskMonitor wrapped = new WrappedTaskMonitor(target);

		context.checking(new Expectations() {
			{
				oneOf(target).getTotalWork();
			}
		});

		wrapped.getTotalWork();

		context.assertIsSatisfied();

	}

	@Test
	public void testGetCurrentWork() {

		final ITaskMonitor target = context.mock(ITaskMonitor.class);

		final WrappedTaskMonitor wrapped = new WrappedTaskMonitor(target);

		context.checking(new Expectations() {
			{
				oneOf(target).getCurrentWork();
			}
		});

		wrapped.getCurrentWork();

		context.assertIsSatisfied();

	}

	@Test
	public void testGetStatus() {

		final ITaskMonitor target = context.mock(ITaskMonitor.class);

		final WrappedTaskMonitor wrapped = new WrappedTaskMonitor(target);

		context.checking(new Expectations() {
			{
				oneOf(target).getStatus();
			}
		});

		wrapped.getStatus();

		context.assertIsSatisfied();

	}

	@Test
	public void testGetErrorMessage() {

		final ITaskMonitor target = context.mock(ITaskMonitor.class);

		final WrappedTaskMonitor wrapped = new WrappedTaskMonitor(target);

		context.checking(new Expectations() {
			{
				oneOf(target).getErrorMessage();
			}
		});

		wrapped.getErrorMessage();

		context.assertIsSatisfied();

	}

	@Test
	public void testGetErrorObject() {

		final ITaskMonitor target = context.mock(ITaskMonitor.class);

		final WrappedTaskMonitor wrapped = new WrappedTaskMonitor(target);

		context.checking(new Expectations() {
			{
				oneOf(target).getErrorObject();
			}
		});

		wrapped.getErrorObject();

		context.assertIsSatisfied();

	}

	@Test
	public void testGetStatusMessage() {

		final ITaskMonitor target = context.mock(ITaskMonitor.class);

		final WrappedTaskMonitor wrapped = new WrappedTaskMonitor(target);

		context.checking(new Expectations() {
			{
				oneOf(target).getStatusMessage();
			}
		});

		wrapped.getStatusMessage();

		context.assertIsSatisfied();

	}

	@Test
	public void testGetTaskName() {

		final ITaskMonitor target = context.mock(ITaskMonitor.class);

		final WrappedTaskMonitor wrapped = new WrappedTaskMonitor(target);

		context.checking(new Expectations() {
			{
				oneOf(target).getTaskName();
			}
		});

		wrapped.getTaskName();

		context.assertIsSatisfied();

	}

	@Test
	public void testCancel() {

		final ITaskMonitor target = context.mock(ITaskMonitor.class);

		final WrappedTaskMonitor wrapped = new WrappedTaskMonitor(target);

		context.checking(new Expectations() {
			{
				oneOf(target).cancel();
			}
		});

		wrapped.cancel();

		context.assertIsSatisfied();

	}

	@Test
	public void testIsCancelled() {

		final ITaskMonitor target = context.mock(ITaskMonitor.class);

		final WrappedTaskMonitor wrapped = new WrappedTaskMonitor(target);

		context.checking(new Expectations() {
			{
				oneOf(target).isCancelled();
			}
		});

		wrapped.isCancelled();

		context.assertIsSatisfied();

	}

	@Test
	public void testSetError() {

		final ITaskMonitor target = context.mock(ITaskMonitor.class);

		final WrappedTaskMonitor wrapped = new WrappedTaskMonitor(target);

		context.checking(new Expectations() {
			{
				oneOf(target).setError(null, null);
			}
		});

		wrapped.setError(null, null);

		context.assertIsSatisfied();

	}

	@Test
	public void testSetStatus() {

		final ITaskMonitor target = context.mock(ITaskMonitor.class);

		final WrappedTaskMonitor wrapped = new WrappedTaskMonitor(target);

		context.checking(new Expectations() {
			{
				oneOf(target).setStatus(null);
			}
		});

		wrapped.setStatus(null);

		context.assertIsSatisfied();

	}

	@Test
	public void testSetStatusMessage() {

		final ITaskMonitor target = context.mock(ITaskMonitor.class);

		final WrappedTaskMonitor wrapped = new WrappedTaskMonitor(target);

		context.checking(new Expectations() {
			{
				oneOf(target).setStatusMessage(null);
			}
		});

		wrapped.setStatusMessage(null);

		context.assertIsSatisfied();

	}

	@Test
	public void testSetTaskName() {

		final ITaskMonitor target = context.mock(ITaskMonitor.class);

		final WrappedTaskMonitor wrapped = new WrappedTaskMonitor(target);

		context.checking(new Expectations() {
			{
				oneOf(target).setTaskName(null);
			}
		});

		wrapped.setTaskName(null);

		context.assertIsSatisfied();
	}

	@Test
	public void testSetTotalWork() {

		final ITaskMonitor target = context.mock(ITaskMonitor.class);

		final WrappedTaskMonitor wrapped = new WrappedTaskMonitor(target);

		context.checking(new Expectations() {
			{
				oneOf(target).setTotalWork(0);
			}
		});

		wrapped.setTotalWork(0);

		context.assertIsSatisfied();

	}

}
