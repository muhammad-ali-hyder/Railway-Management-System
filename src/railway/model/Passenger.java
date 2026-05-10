package railway.model;

public class Passenger {
    private int id;
    private int userId;
    private String name;
    private int age;
    private String gender;
    private String idProof;

    public Passenger() {}

    public Passenger(int id, int userId, String name, int age, String gender, String idProof) {
        this.id = id;
        this.userId = userId;
        this.name = name;
        this.age = age;
        this.gender = gender;
        this.idProof = idProof;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public int getUserId() { return userId; }
    public void setUserId(int userId) { this.userId = userId; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public int getAge() { return age; }
    public void setAge(int age) { this.age = age; }

    public String getGender() { return gender; }
    public void setGender(String gender) { this.gender = gender; }

    public String getIdProof() { return idProof; }
    public void setIdProof(String idProof) { this.idProof = idProof; }

    @Override
    public String toString() {
        return name + " (Age: " + age + ", " + gender + ")";
    }
}
