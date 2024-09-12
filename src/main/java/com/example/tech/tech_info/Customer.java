package com.example.tech.tech_info;

public class Customer {

    private Long id;
    private String name;
    private String address;
    private Integer mobile;
    private Double payment;

    public Customer(Long id, String name, String address, Integer mobile, Double payment ) {
        this.id = id;
        this.name = name;
        this.address = address;
        this.mobile = mobile;
        this.payment = payment;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Integer getMobile() {
        return mobile;
    }

    public void setMobile(Integer mobile) {
        this.mobile = mobile;
    }

    public Double getPayment() {
        return payment;
    }

    public void setPayment(Double payment) {
        this.payment = payment;
    }

    @Override
    public String toString() {
        return "Customer{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", address='" + address + '\'' +
                ", mobile='" + mobile + '\'' +
                ", payment=" + payment +
                '}';
    }
}
