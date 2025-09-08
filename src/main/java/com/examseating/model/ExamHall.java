package com.examseating.model;

import java.sql.Timestamp;

/**
 * ExamHall model class
 */
public class ExamHall {
    private int id;
    private String hallName;
    private int capacity;
    private int rows;
    private int columns;
    private String building;
    private Integer floor;
    private String facilities;
    private boolean isActive;
    private Timestamp createdAt;
    private Timestamp updatedAt;
    
    // Additional properties for seating management
    private int occupiedSeats;
    private int availableSeats;
    
    // Constructors
    public ExamHall() {
        this.isActive = true;
    }
    
    public ExamHall(String hallName, int capacity, int rows, int columns, String building) {
        this();
        this.hallName = hallName;
        this.capacity = capacity;
        this.rows = rows;
        this.columns = columns;
        this.building = building;
    }
    
    public ExamHall(int id, String hallName, int capacity, int rows, int columns, 
                    String building, Integer floor, String facilities) {
        this(hallName, capacity, rows, columns, building);
        this.id = id;
        this.floor = floor;
        this.facilities = facilities;
    }
    
    // Getters and Setters
    public int getId() {
        return id;
    }
    
    public void setId(int id) {
        this.id = id;
    }
    
    public String getHallName() {
        return hallName;
    }
    
    public void setHallName(String hallName) {
        this.hallName = hallName;
    }
    
    public int getCapacity() {
        return capacity;
    }
    
    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }
    
    public int getRows() {
        return rows;
    }
    
    public void setRows(int rows) {
        this.rows = rows;
    }
    
    public int getColumns() {
        return columns;
    }
    
    public void setColumns(int columns) {
        this.columns = columns;
    }
    
    public String getBuilding() {
        return building;
    }
    
    public void setBuilding(String building) {
        this.building = building;
    }
    
    public Integer getFloor() {
        return floor;
    }
    
    public void setFloor(Integer floor) {
        this.floor = floor;
    }
    
    public String getFacilities() {
        return facilities;
    }
    
    public void setFacilities(String facilities) {
        this.facilities = facilities;
    }
    
    public boolean isActive() {
        return isActive;
    }
    
    public void setActive(boolean active) {
        isActive = active;
    }
    
    public int getOccupiedSeats() {
        return occupiedSeats;
    }
    
    public void setOccupiedSeats(int occupiedSeats) {
        this.occupiedSeats = occupiedSeats;
        this.availableSeats = this.capacity - occupiedSeats;
    }
    
    public int getAvailableSeats() {
        return availableSeats;
    }
    
    public void setAvailableSeats(int availableSeats) {
        this.availableSeats = availableSeats;
    }
    
    public Timestamp getCreatedAt() {
        return createdAt;
    }
    
    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }
    
    public Timestamp getUpdatedAt() {
        return updatedAt;
    }
    
    public void setUpdatedAt(Timestamp updatedAt) {
        this.updatedAt = updatedAt;
    }
    
    // Business methods
    public String getFullName() {
        StringBuilder sb = new StringBuilder(hallName);
        if (building != null && !building.isEmpty()) {
            sb.append(" (").append(building);
            if (floor != null) {
                sb.append(", Floor ").append(floor);
            }
            sb.append(")");
        }
        return sb.toString();
    }
    
    public boolean hasAvailableSeats(int requiredSeats) {
        return (capacity - occupiedSeats) >= requiredSeats;
    }
    
    public double getOccupancyPercentage() {
        if (capacity == 0) return 0.0;
        return (double) occupiedSeats / capacity * 100.0;
    }
    
    public String getOccupancyStatus() {
        double percentage = getOccupancyPercentage();
        if (percentage >= 90) {
            return "Full";
        } else if (percentage >= 70) {
            return "Nearly Full";
        } else if (percentage >= 40) {
            return "Moderate";
        } else {
            return "Available";
        }
    }
    
    public String getOccupancyCssClass() {
        double percentage = getOccupancyPercentage();
        if (percentage >= 90) {
            return "occupancy-full";
        } else if (percentage >= 70) {
            return "occupancy-high";
        } else if (percentage >= 40) {
            return "occupancy-medium";
        } else {
            return "occupancy-low";
        }
    }
    
    public boolean isValidSeatPosition(int row, int column) {
        return row > 0 && row <= rows && column > 0 && column <= columns;
    }
    
    public int calculateSeatNumber(int row, int column) {
        if (!isValidSeatPosition(row, column)) {
            throw new IllegalArgumentException("Invalid seat position: (" + row + ", " + column + ")");
        }
        return (row - 1) * columns + column;
    }
    
    public int[] getSeatPosition(int seatNumber) {
        if (seatNumber < 1 || seatNumber > capacity) {
            throw new IllegalArgumentException("Invalid seat number: " + seatNumber);
        }
        int row = (seatNumber - 1) / columns + 1;
        int column = (seatNumber - 1) % columns + 1;
        return new int[]{row, column};
    }
    
    public String[] getFacilitiesList() {
        if (facilities == null || facilities.isEmpty()) {
            return new String[0];
        }
        return facilities.split(",\\s*");
    }
    
    @Override
    public String toString() {
        return "ExamHall{" +
                "id=" + id +
                ", hallName='" + hallName + '\'' +
                ", capacity=" + capacity +
                ", rows=" + rows +
                ", columns=" + columns +
                ", building='" + building + '\'' +
                ", floor=" + floor +
                ", isActive=" + isActive +
                ", occupiedSeats=" + occupiedSeats +
                '}';
    }
    
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        ExamHall examHall = (ExamHall) obj;
        return id == examHall.id;
    }
    
    @Override
    public int hashCode() {
        return Integer.hashCode(id);
    }
}