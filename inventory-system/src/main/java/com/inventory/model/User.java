package com.inventory.model;

public class User {
    int id;
    String username;
    String password;
    String role;

    public User(){

    }

    public User(int id,String username,String password,String role){
        this.id=id;
        this.username=username;
        this.password=password;
        this.role=role;
    }

    public int getId(){
        return id;
    }

    public String getUserName(){
        return username;
    }

    public String getPassword(){
        return password;
    }

    public String getRole(){
        return role;
    }

    public String toString(){
        System.out.println("User Details");
        return "ID: "+id+" Name: "+username+" Role: "+role;
    }

}
