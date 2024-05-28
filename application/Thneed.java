package application;

import java.io.Serializable;

/**
 * A Thneed represents a single product to be ordered in the Thneed order system.
 * Thneeds were introduced in Dr. Seuss books. They are highly versatile 
 * knitted fabrics that have a size and color.
 */
public class Thneed implements Serializable {
	
	private static final long serialVersionUID = 1L;

	private String size;
	private String color;
	
	/**
	 * Constructs a new Thneed
	 * @param size the size of the Thneed ("small", "medium", or "large")
	 * @param color the color of the Thneed
	 */
	public Thneed(String size, String color) {
		super();
		this.size = size;
		this.color = color;
	}
	
	/** Getters **/

	public String getSize() {
		return size;
	}
	
	public String getColor() {
		return color;
	}
	
	@Override
	public String toString() {
		return size +  " " + color;

	}
}
