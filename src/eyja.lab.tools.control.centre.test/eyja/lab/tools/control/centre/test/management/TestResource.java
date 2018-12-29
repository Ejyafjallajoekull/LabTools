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
public final class TestResource extends Resource {
		
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
	
	@Override
	public int hashCode() {
		return (this.id == null) ? 0 : this.id.hashCode();
	}
	
	@Override
	public boolean equals(Object obj) {
		// ignore origin as this leads to a circular equals relation and a resulting infinity loop
		if (obj instanceof TestResource) {
			TestResource comp = (TestResource) obj;
			if (this.id == null && comp.id == null) {
				return true;
			} else if (this.id != null && comp.id != null) {
				return this.id.getID() == comp.id.getID();
			}
		}
		return false;
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
