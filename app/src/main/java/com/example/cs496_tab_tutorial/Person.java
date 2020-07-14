package com.example.cs496_tab_tutorial;

public class Person {
    private String Name;
    private String Number;
    private String Id;

    public Person(String _Name, String _Number, String _Id){
        this.Name = _Name;
        this.Number = _Number;
        this.Id = _Id;
    }

    public Person(String _Name, String _Number){
        this.Name = _Name;
        this.Number = _Number;
        this.Id = "";
    }

    public Person(){
        this.Name="";
        this.Number="";
        this.Id="";
    }

    public String getName() {
        return Name;
    }
    public String getNumber() {
        return Number;
    }
    public String getId() { return Id; }

    public void setName(String name) {
        this.Name = name;
    }

    public void setNumber(String number) {
        this.Number = number;
    }

    public void setId(String id) {
        this.Id = id;
    }
}
