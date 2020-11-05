import java.io.IOException;
import java.util.List;


public class AddressBookService {


    public enum IOService {
        CONSOLE_IO, FILE_IO, DB_IO, REST_IO, CSV, JSON
    }

    private static List<Address> contactList;
    

    public AddressBookService(List<Address> employeePayrollList) {
        //this();
        this.contactList = employeePayrollList;
    }

    public static List<Address> readContactData(IOService ioService) throws IOException {
        AddressBookJDBCServices addressBookJDBCServices =new AddressBookJDBCServices();
//        System.out.println(ioService + " " + IOService.DB_IO);
//        if (ioService.equals(IOService.FILE_IO))
//            contactList = new UserInterface2().readFromCSVFile();
        if (ioService.equals(IOService.DB_IO)) {
            contactList = addressBookJDBCServices.readData();
        }
        return contactList;
    }

    public static void updateCity(String firstName, String city) throws AddressBookDBException, IOException {
        AddressBookJDBCServices addressBookJDBCServices = new AddressBookJDBCServices();
        int result = addressBookJDBCServices.updateContactUsingSQL(firstName, "city", city);
        Address contact = getContactData(firstName);
        if (result != 0 && contact != null)
            contact.setCity(city);
        if (result == 0)
            throw new AddressBookDBException("Wrong name given", AddressBookDBException.ExceptionType.WRONG_NAME);
        if (contact == null)
            throw new AddressBookDBException("No data found", AddressBookDBException.ExceptionType.NO_DATA_FOUND);
    }

    private static Address getContactData(String name) throws IOException {
        readContactData(IOService.DB_IO);
        return contactList.stream().filter(e -> e.getFirst_Name().equals(name)).findFirst().orElse(null);
    }

    public static boolean isAddressBookSyncedWithDB(String firstName) throws IOException {
        Address contact = getContactData(firstName);
        return AddressBookJDBCServices.getContacts(firstName).get(0).getCity().equals(contact.getCity());
    }
}