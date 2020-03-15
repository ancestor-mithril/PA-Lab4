
import java.util.Objects;

/**
 * this class has setters and getters, constructor, toString, equals
 */
public class Resident {
    private String name;
    private Hospital assignedHospital;
    public Resident(String name){
        this.name=name;
        assignedHospital=null;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Hospital getAssignedHospital() {
        return assignedHospital;
    }

    /**
     * not public because it's package private
     * @param assignedHospital
     */
    void setAssignedHospital(Hospital assignedHospital) {
        this.assignedHospital = assignedHospital;
    }

    @Override
    public String toString() {
        return "Resident{" +
                "name='" + name + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Resident resident = (Resident) o;
        return Objects.equals(name, resident.name) &&
                Objects.equals(assignedHospital, resident.assignedHospital);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, assignedHospital);
    }
}
