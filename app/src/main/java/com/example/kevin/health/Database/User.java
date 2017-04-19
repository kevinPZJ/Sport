package com.example.kevin.health.Database;

/**
 * Created by kevin on 2017/4/17.
 */

public class User {
    private int userId;

    private String userName;

    private int age;

    private int height;

    private int weight;

    private int perDayStep;

    private int totalDistance;

    private int completeDay;

    private int TargetPerDayStep;

    private int targetweight ;

    private int perDaySleephour ;


    public User(){
    }

    public User(int userId, String userName, int age, int height, int weight, int perDayStep, int totalDistance,
                int completeDay, int targetPerDayStep, int targetweight,int perDaySleephour) {
        super();
        this.userId = userId;
        this.userName = userName;
        this.age = age;
        this.height = height;
        this.weight = weight;
        this.perDayStep = perDayStep;
        this.totalDistance = totalDistance;
        this.completeDay = completeDay;
        this.TargetPerDayStep = targetPerDayStep;
        this.targetweight = targetweight;
        this.perDaySleephour = perDaySleephour;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public int getWeight() {
        return weight;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }

    public int getPerDayStep() {
        return perDayStep;
    }

    public void setPerDayStep(int perDayStep) {
        this.perDayStep = perDayStep;
    }

    public int getTotalDistance() {
        return totalDistance;
    }

    public void setTotalDistance(int totalDistance) {
        this.totalDistance = totalDistance;
    }

    public int getCompleteDay() {
        return completeDay;
    }

    public void setCompleteDay(int completeDay) {
        this.completeDay = completeDay;
    }

    public int getTargetPerDayStep() {
        return TargetPerDayStep;
    }

    public void setTargetPerDayStep(int targetPerDayStep) {
        TargetPerDayStep = targetPerDayStep;
    }

    public int getTargetHeight() {
        return targetweight;
    }

    public void setTargetHeight(int targetHeight) {
        this.targetweight = targetHeight;
    }

    public int getPerDaySleephour() {
        return perDaySleephour;
    }

    public void setPerDaySleephour(int perDaySleephour) {
        this.perDaySleephour = perDaySleephour;
    }

    @Override
    public String toString() {
        return "User [userId=" + userId + ", userName=" + userName + ", age=" + age + ", height=" + height + ", weight="
                + weight + ", perDayStep=" + perDayStep + ", totalDistance=" + totalDistance + ", completeDay="
                + completeDay + ", TargetPerDayStep=" + TargetPerDayStep + ", targetHeight=" + targetweight
                + ", perDaySleephour=" + perDaySleephour + "]";
    }

}
