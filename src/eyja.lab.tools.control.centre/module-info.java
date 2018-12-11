/**
 * Defines a module managing the interplay of different tools.
 * 
 * @author Planters
 *
 */
module eyja.lab.tools.control.centre {
	
	exports eyja.lab.tools.control.centre.management;
	exports eyja.lab.tools.control.centre.binaryop;
	exports eyja.lab.tools.control.centre.operation;
	
	uses eyja.lab.tools.control.centre.operation.Initialiser;
	
	requires java.base;
	requires transitive central.logging;
	
}