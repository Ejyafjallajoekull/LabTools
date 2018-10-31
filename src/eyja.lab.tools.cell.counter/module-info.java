/**
 * Defines a cell counting module for use in biological laboratories.
 * 
 * @author Planters
 *
 */
module eyja.lab.tools.cell.counter {
	
	exports eyja.lab.tools.cell.counter.core;
	exports eyja.lab.tools.cell.counter.functionality;
	
	requires java.base;
	requires transitive eyja.lab.tools.control.centre;
	
}