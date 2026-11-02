package main.java;

import jakarta.persistence.*;

@Entity
@Table(name = "VEHICLE_MODEL")
public class VehicleModel {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@Column(name = "name")
	private String name;
	@Column(name = "mpg")
	private double mpg;
	
	@Column(name = "routine")
	private double routine;
	@Column(name = "large")
	private double large;
	@Column(name = "small")
	private double small;
	
	public VehicleModel(String name, double mpg, double routine, double large, double small) {
		this.name = name;
		this.mpg = mpg;
		
		this.routine = routine;
		this.large = large;
		this.small = small;
	}
	
	public VehicleModel() {}
	
	public double[] getMaintenanceCosts() {
		double[] maintenanceCosts = new double[3];
		maintenanceCosts[0] = routine;
		maintenanceCosts[1] = large;
		maintenanceCosts[2] = small;
		return maintenanceCosts;
	}
	
	public String getName() {
		return name;
	}
	
	public double getMPG() {
		return mpg;
	}
	
	public double getRoutineMaintenanceCost() {
		return routine;
	}
	
	public double getLargeMaintenanceCost() {
		return large;
	}
	
	public double getSmallMaintenanceCost() {
		return small;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public void setMPG(double mpg) {
		this.mpg = mpg;
	}
	
	public void setRoutineMaintenanceCost(double cost) {
		routine = cost;
	}
	
	public void setLargeMaintenanceCost(double cost) {
		large = cost;
	}
	
	public void setSmallMaintenanceCost(double cost) {
		small = cost;
	}
}
