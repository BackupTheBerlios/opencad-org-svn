package org.opencad.modelling;

import java.io.Serializable;

public class Corner implements Serializable {

	private static final long serialVersionUID = -1332083715502519329L;

	private Double x, y;

	public final Double getX() {
		return x;
	}

	public final void setX(Double x) {
		this.x = x;
	}

	public final Double getY() {
		return y;
	}

	public final void setY(Double y) {
		this.y = y;
	}

	public Corner(Double x, Double y) {
		this.x = x;
		this.y = y;
	}

	public String toString() {
		return x + "," + y;
	}

}
