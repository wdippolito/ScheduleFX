package objects;

/**
 * class for representing customer data structure
 */
public class Customer {

    private int customerId;

    private String name;

    private String address;

    private String phoneNumber;

    private String postalCode;


    private int divisionId;

    private String division;

    private String country;

    public Customer(int customerId, String name, String address, String phoneNumber, String postalCode, int divisionId, String division, String country){
        this.customerId = customerId;
        this.name = name;
        this.address = address;
        this.phoneNumber = phoneNumber;
        this.postalCode = postalCode;
        this.divisionId = divisionId;
        this.division = division;
        this.country = country;
    }


    public int getCustomerId() {
        return customerId;
    }

    public String getName() {
        return name;
    }

    public String getAddress() {
        return address;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public int getDivisionId() {
        return divisionId;
    }

    public String getDivision() {
        return division;
    }

    public String getPostalCode() {
        return postalCode;
    }

    public String getCountry() {
        return country;
    }
}
