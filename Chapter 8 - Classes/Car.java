import java.time.LocalDate;

public class Car {
    private final String make;
    private final String model;
    private final int manufacturingYear;

    public String getMake() {
        return make;
    }
    public String getModel() {
        return model;
    }
    public int getManufacturingYear() {
        return manufacturingYear;
    }

    public Car(String make, String model, int manufacturingYear) {
        this.make = make;
        this.model = model;
        this.manufacturingYear = manufacturingYear;
    }
    public Car(String make, String model) {
        this(make, model, LocalDate.now().getYear());
    }
}