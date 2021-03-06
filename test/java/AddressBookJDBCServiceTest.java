import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;


public class AddressBookJDBCServiceTest {
    AddressBookJDBCServices addressBookJDBCServices;

    @Before
    public void initialize() {
        addressBookJDBCServices = new AddressBookJDBCServices();
    }

    @Test
    public void givenAddressBookData_WhenRetrieved_ShouldMatchContactCount() {
        List<Address> contactList = addressBookJDBCServices.readData();
        assertEquals(4, contactList.size());
    }

    @Test
    public void givenName_WhenUpdatedContactInfo_ShouldSyncWithDB() throws AddressBookDBException, IOException {
        AddressBookService.updateCity("Navneet", "Samastipur");
        boolean isSynced = AddressBookService.isAddressBookSyncedWithDB("Navneet");
       //assertTrue(isSynced);
        Assert.assertEquals(true, isSynced);
    }

    @Test
    public void givenDateRange_WhenRetrievedContactInfo_ShouldMatchCount() throws AddressBookDBException{
        LocalDate startDate = LocalDate.of(2017, 03, 01);
        LocalDate endDate= LocalDate.now();
        List<Address> contactList= AddressBookService.getContactsForDateRange(startDate,endDate);
        System.out.println("contactList: "+ contactList.toString());
        Assert.assertEquals(4,contactList.size());
    }

    @Test
    public void givenAddressBookData_whenRetreivedByState_ShouldMatchContactCount() {
        List<Address>contactList=AddressBookService.getContactsByCity("Samastipur");
        assertEquals(1,contactList.size());
    }

    @Test
    public void givenContactData_WhenAddedToDB_ShouldSyncWithDB() throws AddressBookDBException, IOException {
        AddressBookService.addNewContact("2018-08-06", "" +
                        "+", "Kumar", "Agra", "Ghaziabad",
                "Uttar Pradesh", "220502", "870045", "amit.kumar@gmail.com");
        boolean isSynced = AddressBookService.isAddressBookSyncedWithDB("Amit");
        Assert.assertEquals(true, isSynced);
    }


    @Test
    public void givenMultipeContacts_WhenAddedToDBWithMultiThreads_ShouldSyncWithDB() throws AddressBookDBException, IOException {
        List<Address> contacts = new ArrayList<Address>() {
            {
                add(new Address("Doland", "Trump", "Prembaripul", "New Delhi", "Delhi", "110077",
                        "1099", "doland.trump@gmail.com"));
                add(new Address("Virat", "Kuhli", "Mirzapur", "Bengaluru", "Karnataka", "560091", "8800",
                        "virat.kuhli@eesaalcupnamde.com"));
            }
        };
        AddressBookService.addNewMultipleContacts(contacts);
        //assertEquals(7, AddressBookService.readContactData(AddressBookService.IOService.DB_IO).size());
    }


}