/**
 * Defines a testing module for eyja.lab.tools.control.centre.
 * 
 * @author Planters
 *
 */
module eyja.lab.tools.control.centre.test {
	
	exports eyja.lab.tools.control.centre.test;
	exports eyja.lab.tools.control.centre.test.management;
	exports eyja.lab.tools.control.centre.test.binaryop;
	
	requires java.base;
	requires eyja.lab.tools.control.centre;
	requires central.logging;
	requires koro.sensei.tester;
	
}