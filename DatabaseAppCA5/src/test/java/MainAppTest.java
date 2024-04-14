import com.sun.tools.javac.Main;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
//import org.junit.Assert;
//import org.junit.Before;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class MainAppTest {

    private DAO dao;
    private Integer testWeaponID;

    @org.junit.jupiter.api.BeforeEach
    public void setUp()
    {
        dao = DAO.getInstance();
    }

    @AfterEach
    void tearDown() throws SQLException {
        // Delete the entity added during the test
        if (testWeaponID != null) {
            dao.deleteWeapon(testWeaponID);
        }
    }
    //Feature 1 Tests
    @org.junit.jupiter.api.Test
    void testFindAllWeapons() throws SQLException {
        var MainApp = new MainApp();
        List<DS_Weapons> weapons = dao.getAllWeapons();
        //this doesn't work once we add more entities in the Junit tests
        //assertEquals(30, weapons.size());
        int expectedAmountOfWeapons = weapons.size();
        assertTrue(expectedAmountOfWeapons > 0);
    }

    //Feature 2 Tests
    @org.junit.jupiter.api.Test
    void testValidWeaponFound() throws SQLException {
        DS_Weapons weapon = dao.getWeaponById(3);
        assertNotNull(weapon);
        assertEquals(3, weapon.getID());
    }
    @org.junit.jupiter.api.Test
    void testInvalidWeaponFound() throws SQLException {
        DS_Weapons weapon = dao.getWeaponById(300);
        assertNull(weapon);
    }

    //Feature 3 Tests

    @org.junit.jupiter.api.Test
    void testDeleteWeapon() throws SQLException {

        //Adding a test weapon to DB
        DS_Weapons testWeapon = new DS_Weapons();
        testWeapon.setName("Test Weapon");
        testWeapon.setAttack(100);
        testWeapon.setWeight(1.1f);
        testWeapon.setLocation("Test Location");
        dao.insertWeapon(testWeapon);

        //getting the id of the test weapon added
        int testWeaponID = testWeapon.getID();

        //Checking if we successfully added the weapon
        assertNotNull(dao.getWeaponById(testWeaponID));

        //Deleting Test weapon by its id
        dao.deleteWeapon(testWeaponID);

        //checking if the weapon is deleted successfully
        assertNull(dao.getWeaponById(testWeaponID));
    }

    //Feature 4 Tests
    @org.junit.jupiter.api.Test
    void testInsertEntitySuccessfully() throws SQLException {
        //Adding a test weapon to DB
        DS_Weapons testWeapon = new DS_Weapons();
        testWeapon.setName("Test Weapon");
        testWeapon.setAttack(100);
        testWeapon.setWeight(1.1f);
        testWeapon.setLocation("Test Location");
        dao.insertWeapon(testWeapon);

        //checking if it was inserted successfully
        DS_Weapons testInsert = dao.getWeaponById(testWeapon.getID());
        assertNotNull(testInsert);
        assertEquals("Test Weapon", testInsert.getName());
        assertEquals(100, testInsert.getAttack());
        assertEquals(1.1f, testInsert.getWeight());
        assertEquals("Test Location", testInsert.getLocation());
    }
    //Feature 5 Tests
    @org.junit.jupiter.api.Test
    void testUpdateExistingEntity() throws SQLException {
        DS_Weapons UpdateTestWeapon = new DS_Weapons();
        UpdateTestWeapon.setName("Update Test Weapon");
        UpdateTestWeapon.setAttack(100);
        UpdateTestWeapon.setWeight(1.1f);
        UpdateTestWeapon.setLocation("Update Test Location");

        int updateTestID = 3;
        DS_Weapons UpdatedTestWeapon = dao.update(updateTestID, UpdateTestWeapon);

        DS_Weapons retrievedWeapon = dao.getWeaponById(updateTestID);
        assertNotNull(retrievedWeapon);
        assertEquals("Update Test Weapon", retrievedWeapon.getName());
        assertEquals(100, retrievedWeapon.getAttack());
        assertEquals(1.1f, retrievedWeapon.getWeight());
        assertEquals("Update Test Location", retrievedWeapon.getLocation());
    }

    //Feature 6 Tests
    @org.junit.jupiter.api.Test
    void testFilterWeaponsByCriteria() throws SQLException {

        //inserting test data into the db to test filter
        DS_Weapons testWeapon1 = new DS_Weapons();
        testWeapon1.setID(49);
        testWeapon1.setName("Sword");
        testWeapon1.setAttack(10000);
        testWeapon1.setWeight(5.0f);
        testWeapon1.setLocation("Location1");
        dao.insertWeapon(testWeapon1);

        //assigning testWeapon id with added test weapon for deletion after test is completed
        testWeaponID = testWeapon1.getID();

        DS_Weapons testWeapon2 = new DS_Weapons();
        testWeapon2.setID(50);
        testWeapon2.setName("Axe");
        testWeapon2.setAttack(120000);
        testWeapon2.setWeight(7.0f);
        testWeapon2.setLocation("Location2");
        dao.insertWeapon(testWeapon2);

        //filtering by the given criteria
        List<DS_Weapons> filteredWeapons = dao.getWeaponByFilter("attack", 10000);

        //checking that the list contains only the correct weapons(ones that meet the criteria)
        assertEquals(1, filteredWeapons.size());
        assertEquals("Sword", filteredWeapons.get(0).getName());
    }
    //Feature 7 Tests

    @org.junit.jupiter.api.Test
    void testListToJsonStringEmptyList() {
        List<DS_Weapons> emptyList = new ArrayList<>();
        String jsonString = JSON_Converter.listToJsonString(emptyList);
        assertEquals("[]", jsonString);
    }

    @org.junit.jupiter.api.Test
    void testListToJsonStringSingleItem() {
        List<DS_Weapons> singleItemList = new ArrayList<>();
        DS_Weapons testWeapon = new DS_Weapons();
        testWeapon.setName("Sword");
        testWeapon.setAttack(100);
        testWeapon.setWeight(5.0f);
        testWeapon.setLocation("Location1");
        singleItemList.add(testWeapon);
        String jsonString = JSON_Converter.listToJsonString(singleItemList);
        assertEquals("[{\"ID\":0,\"Name\":\"Sword\",\"Attack\":100,\"Weight\":5.0,\"Location\":\"Location1\"}]", jsonString);
    }

    //Feature 8 Tests

    @org.junit.jupiter.api.Test
    void testGetWeaponByIdJson() throws SQLException {
        String expectedJson = "{\"ID\":2,\"Name\":\"Broadsword\",\"Attack\":205,\"Weight\":3.0,\"Location\":\"Purchased\"}";

        int weaponId = 2;
        String actualJson = dao.getWeaponbyIDasJson(weaponId);

        // Assert that the actual JSON response matches the expected JSON string
        assertEquals(expectedJson, actualJson);
    }
}