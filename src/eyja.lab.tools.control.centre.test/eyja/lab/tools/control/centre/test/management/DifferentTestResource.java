package eyja.lab.tools.control.centre.test.management;

import java.nio.ByteBuffer;

import eyja.lab.tools.control.centre.management.Resource;
import eyja.lab.tools.control.centre.management.ResourceID;
import eyja.lab.tools.control.centre.test.TestRunnerWrapper;

/**
 * The DifferentTestResource class is a simple implementation of a resource only consisting of 
 * a resource ID for testing purposes. It is similar to the TestResource class.
 * 
 * @author Planters
 *
 */
public class DifferentTestResource extends Resource {

	/**
	 * Create a test resource only consisting of an ID.
	 * 
	 * @param id - the resource's ID
	 */
	public DifferentTestResource(@SuppressWarnings("exports") ResourceID id) {
		this.setID(id);
	}
	
	/**
	 * Create a test resource only consisting of an ID.
	 */
	public DifferentTestResource() {
		// Default constructor
	}
	
	@Override
	public byte[] serialise() {
		return ByteBuffer.allocate(8).putLong(this.getID().getID()).array();
	}
	
	@Override
	public boolean equals(Object obj) {
		// ignore origin
		if (obj != null && obj instanceof DifferentTestResource) {
			TestResource comp = (TestResource) obj;
			if (this.getID() == null && comp.getID() == null) {
				return true;
			} else if (this.getID() != null && comp.getID() != null) {
				return this.getID().getID() == comp.getID().getID();
			}
		}
		return false;
	}
	
	/**
	 * Create a random test resource.
	 * 
	 * @return a random test resource
	 */
	public static DifferentTestResource createRandomResource() {
		return new DifferentTestResource(new ResourceID(null, TestRunnerWrapper.RANDOM.nextLong()));
	}


}
