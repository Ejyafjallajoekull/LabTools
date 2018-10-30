/**
 * Defines a module launching and initialising different tools.
 * 
 * @author Planters
 *
 */
module eyja.lab.tools.launch.pad {
	
	exports eyja.lab.tools.launch.pad.core;
	
	requires java.base;
	requires central.logging;
	requires transitive eyja.lab.tools.control.centre;
	requires eyja.lab.tools.cell.counter;
	
}