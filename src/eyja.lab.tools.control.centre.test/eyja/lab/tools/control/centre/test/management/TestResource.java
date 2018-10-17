package eyja.lab.tools.control.centre.test.management;

import java.nio.ByteBuffer;

import eyja.lab.tools.control.centre.management.Resource;
import eyja.lab.tools.control.centre.management.ResourceID;
import eyja.lab.tools.control.centre.test.TestRunnerWrapper;

/**
 * The TestResource class is a simple implementation of a resource only consisting of a resource ID 
 * for testing purposes.
 * 
 * @author Planters
 *
 */
public class TestResource extends Resource {
		
	/**
	 * Create a test resource only consisting of an ID.
	 * 
	 * @param id - the resource's ID
	 */
	public TestResource(@SuppressWarnings("exports") ResourceID id) {
		this.setID(id);
	}
	
	/**
	 * Create a test resource only consisting of an ID.
	 */
	public TestResource() {
		// Default constructor
	}

	@Override
	public byte[] serialise() {
		return ByteBuffer.allocate(8).putLong(this.getID().getID()).array();
	}
	
	/**
	 * Create a random test resource.
	 * 
	 * @return a random test resource
	 */
	public static TestResource createRandomResource() {
		return new TestResource(new ResourceID(null, TestRunnerWrapper.RANDOM.nextLong()));
	}

}
