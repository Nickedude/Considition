package considition.api.models;

public class TransportationMethod {
	public String name;
	public double speed; // Km/h
	public double pollutions; // Tonne CO2/h
	public int travelInterval; // Transportation is available every x minutes
	public boolean canUseAnywhere;
}
