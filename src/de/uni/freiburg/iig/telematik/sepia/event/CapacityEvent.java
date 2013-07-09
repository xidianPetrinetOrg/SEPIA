package de.uni.freiburg.iig.telematik.sepia.event;

import java.util.EventObject;

import de.uni.freiburg.iig.telematik.sepia.petrinet.AbstractPlace;


@SuppressWarnings("serial")
public class CapacityEvent<P extends AbstractPlace<?,?>> extends EventObject {
	
	public int capacity = 0;
	
	public CapacityEvent(P source, int capacity) {
		super(source);
		this.capacity = capacity;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public P getSource() {
		return (P) super.getSource();
	}
	
	public int getCapacity(){
		return capacity;
	}

}
