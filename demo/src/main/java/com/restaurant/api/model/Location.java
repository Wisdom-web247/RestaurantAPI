package com.restaurant.api.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;

@Entity
@Table(name = "locations")
public class Location {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank private String name;
    @NotBlank private String address;
    private String city;
    private String phone;
    private String hours;
    private Double latitude;
    private Double longitude;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String n) { this.name = n; }
    public String getAddress() { return address; }
    public void setAddress(String a) { this.address = a; }
    public String getCity() { return city; }
    public void setCity(String c) { this.city = c; }
    public String getPhone() { return phone; }
    public void setPhone(String p) { this.phone = p; }
    public String getHours() { return hours; }
    public void setHours(String h) { this.hours = h; }
    public Double getLatitude() { return latitude; }
    public void setLatitude(Double l) { this.latitude = l; }
    public Double getLongitude() { return longitude; }
    public void setLongitude(Double l) { this.longitude = l; }
}
