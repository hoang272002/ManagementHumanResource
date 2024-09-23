package model;

public class ActivityDetail {
    private String manv;
    private double distance; // Distance in kilometers
    private String movingTime; // Time in hours, minutes, seconds
    private double totalDistance; // Total distance in kilometers
    private String startTime; // Start time of the competition
    private String endTime; // End time of the competition

    // Constructors, Getters, Setters
	public ActivityDetail() {super();}
    public ActivityDetail(String manv, double distance, String movingTime, double totalDistance, String startTime, String endTime) {
    	super();
        this.manv = manv;
        this.distance = distance;
        this.movingTime = movingTime;
        this.totalDistance = totalDistance;
        this.startTime = startTime;
        this.endTime = endTime;
    }

    // Getters and Setters
    public String getManv() {
        return manv;
    }

    public void setManv(String manv) {
        this.manv = manv;
    }

    public double getDistance() {
        return distance;
    }

    public void setDistance(double distance) {
        this.distance = distance;
    }

    public String getMovingTime() {
        return movingTime;
    }

    public void setMovingTime(String movingTime) {
        this.movingTime = movingTime;
    }

    public double getTotalDistance() {
        return totalDistance;
    }

    public void setTotalDistance(double totalDistance) {
        this.totalDistance = totalDistance;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }
}
