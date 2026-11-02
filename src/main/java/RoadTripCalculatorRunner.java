package main.java;

public class RoadTripCalculatorRunner {

	public static void main(String[] args) {
		HibernateIO.connect();
		SaveIO.boot();
		new RoadTripCalculator();
	}

}
