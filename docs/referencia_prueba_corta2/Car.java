/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package uml_pruebacorta2;

/**
 *
 * @author mcfra
 */
public abstract class Car implements Vehicle {

    private CarType type;
    private String carID;
    private int maxSpeed;
    private int currentSpeed;

    public Car(CarType type, String carID, int maxSpeed) {
        this.type = type;
        this.carID = carID;
        this.maxSpeed = maxSpeed;
        this.currentSpeed = 0;
    }

    public CarType getType() {
        return type;
    }

    public String getCarID() {
        return carID;
    }

    public int getMaxSpeed() {
        return maxSpeed;
    }

    @Override
    public int increaseSpeed(int dv) {
        currentSpeed += dv;

        if (currentSpeed > maxSpeed) {
            currentSpeed = maxSpeed;
        }

        return currentSpeed;
    }

    @Override
    public int decreaseSpeed(int dv) {
        currentSpeed -= dv;

        if (currentSpeed < 0) {
            currentSpeed = 0;
        }

        return currentSpeed;
    }
}
