package com.ngs.cform.test;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class Test {

	public static void main(String[] args) {
		Canvas c = new Canvas();
		List<? extends Shape> list = new ArrayList<>();
//		list.add(new Circle());
		c.drawAll(list); 
		
		List<? super Shape> shapes = null;
//		shapes.add(new Circle());
		
		Gen<? super String> g = new Gen<>();
		g.doSomething("", "", "");
		
		c.add(new ArrayList<>());
		
		Collection li = c.getParts();
	}
}

 class Shape {
	public void draw(Canvas c) {};
}

class Circle extends Shape {
	private int x, y, radius;

	public void draw(Canvas c) {
		// ...÷
	}
}

class Rectangle extends Shape {
	private int x, y, width, height;

	public void draw(Canvas c) {
		// ...
	}
}

class Canvas {
	public void draw(Shape s) {
		s.draw(this);
	}

	public void drawAll(List<? extends Shape> shapes) {
		for (Shape s : shapes) {
			this.draw(s);
		}
	}
	
	public void add(Collection<Object> l) {
		
	}
	
	public Collection<String> getParts() {
		return null;
	}
}



class Gen<T> {
	<F, m> T doSomething(T o, F r, m m) {
		return o;
	}
}


