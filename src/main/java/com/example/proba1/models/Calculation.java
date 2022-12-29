package com.example.servingwebcontent.models;

public class Calculation {
    private int CalculationId;
    private int Height;
    private int Weight;
    private int Age;
    private int CaloriesIntake;
    private int ExtraCalories;
    private int MenuId;

    public Calculation(int calculationId, int height, int weight, int age, int caloriesIntake, int extraCalories, int menuId) {
        CalculationId = calculationId;
        Height = height;
        Weight = weight;
        Age = age;
        CaloriesIntake = caloriesIntake;
        ExtraCalories = extraCalories;
        MenuId = menuId;
    }

    public int getCalculationId() {
        return CalculationId;
    }

    public void setCalculationId(int calculationId) {
        CalculationId = calculationId;
    }

    public int getHeight() {
        return Height;
    }

    public void setHeight(int height) {
        Height = height;
    }

    public int getWeight() {
        return Weight;
    }

    public void setWeight(int weight) {
        Weight = weight;
    }

    public int getAge() {
        return Age;
    }

    public void setAge(int age) {
        Age = age;
    }

    public int getCaloriesIntake() {
        return CaloriesIntake;
    }

    public void setCaloriesIntake(int caloriesIntake) {
        CaloriesIntake = caloriesIntake;
    }

    public int getExtraCalories() {
        return ExtraCalories;
    }

    public void setExtraCalories(int extraCalories) {
        ExtraCalories = extraCalories;
    }

    public int getMenuId() {
        return MenuId;
    }

    public void setMenuId(int menuId) {
        MenuId = menuId;
    }

    @java.lang.Override
    public java.lang.String toString() {
        return "Calculator{" +
                "CalculationId=" + CalculationId +
                ", Height=" + Height +
                ", Weight=" + Weight +
                ", Age=" + Age +
                ", CaloriesIntake=" + CaloriesIntake +
                ", ExtraCalories=" + ExtraCalories +
                ", MenuId=" + MenuId +
                '}';
    }

    public Calculation() {
    }
}