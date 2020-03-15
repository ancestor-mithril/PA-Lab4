import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;


/**
 * has constructors, getter, setters, adders, and removers, toString, equals, compareTo
 */
public class Hospital implements Comparable<Hospital> {
    private String name;
    private int capacity;
    private List<Resident> residentList;
    public Hospital(String name){
        this.name=name;
        residentList =new ArrayList<>();
    }
    public Hospital(String name, int capacity){
        this.name=name;
        this.capacity=capacity;
        residentList =new ArrayList<>();
    }
    public Hospital(String name, int capacity, List<Resident> residentList) {
        this.name = name;
        this.capacity = capacity;
        this.residentList=new ArrayList<>();
        this.residentList = residentList;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getCapacity() {
        return capacity;
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }

    public List<Resident> getResidentList() {
        return residentList;
    }

    public void addResident(Resident resident) {
        this.residentList.add(resident);
        resident.setAssignedHospital(this);
    }

    public boolean removeResident(Resident resident){
        if (!residentList.contains(resident))
            return false;
        residentList.remove(resident);
        resident.setAssignedHospital(null);
        return true;
    }

    @Override
    public String toString() {
        return "Hospital{" +
                "name='" + name + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Hospital hospital = (Hospital) o;
        return capacity == hospital.capacity &&
                Objects.equals(name, hospital.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, capacity);
    }


    @Override
    public int compareTo(Hospital h) {
        return this.getName().compareTo(h.getName());
    }


}
