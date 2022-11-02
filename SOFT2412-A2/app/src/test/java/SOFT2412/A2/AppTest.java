/*
 * This Java source file was generated by the Gradle 'init' task.
 */
package SOFT2412.A2;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDateTime;
import java.util.*;

class AppTest {
    UserInterface ui = new UserInterface();
    VendingMachine vm = new VendingMachine();
    Seller john = new Seller("john", "john123", "pass");
    Owner owner = new Owner("owner", "owner123", "pass");
    Cashier karen = new Cashier("karen", "karen123", "pass");

    @Test
    public void testAddItem() {
        return;
    }

    @Test
    // Only testing if the payByCard method works
    public void testPayByCard() {
        int quantity = 4;
        String itemCode = "se";
        vm.payByCard(quantity, itemCode);
    }

    @Test
    public void testCardInput() {
        String[] temp = {"buy", "card", "4", "se"};
        List<String> input = Arrays.asList(temp);
        ui.buy(input);
    }

    // Need to find a way to update the txt file to its original inventory after testing
    @Test
    public void testUpdateItem() {
        vm.updateItem("cc", 1);
        assertEquals(vm.getInventory().get(vm.searchByItemCode("cc")), 6);
    }

    // This also tests updateCash(), updateItem(), updateTransactions(), and calculateChange() because the method is called in payByCash()
    @Test
    public void testPayByCash() {
        vm.defaulting();
        vm.payByCash(1, "cc", "$5*1 $2*1");
        System.out.println();
        assertEquals(6, vm.getCash().get("$5"));
        assertEquals(4, vm.getCash().get("$2"));
        assertEquals(4, vm.getCash().get("50c"));
        vm.updateTotalSold("cc", -1);
        vm.defaulting();
    }

    // Check if card details were saved in internal card list
    @Test
    public void saveCardDetails() {
        Card.defaultCards();
        Card testCard = new Card("Test", "123456");
        Card.updateCards(testCard);
        assertTrue(Card.getCards().contains(testCard));
    }

    // Check that we've saved this card for the user ONLY
    @Test
    void testUser() {
        Map<String, String> holder = ui.allCommandBriefs;
        holder = ui.allCommandUsage;
        User md = new Customer("Md", "Emmder", "password124");
        Card coolCard = new Card("Md", "123456");
        md.addCard(md, coolCard);
        assertEquals(md.getCard(), coolCard);
    }

    @Test
    public void testVendingMachineValidation() {

        // Test that we don't let user buy something if they haven't paid enough for it
        vm.defaulting();
        try {
            vm.payByCash(1, "mm", "");
            vm.payByCash(1, "mm", "$1*1");
            fail("allows us to buy if they haven't paid");
        } catch (ArithmeticException ae) {
            // Check that we haven't taken their money
            assertEquals(vm.getCash().get("$1"), 5);
            // Check that we have not given them the item
            assertEquals(vm.getInventory().get(vm.searchByItemCode("mm")), 7);
        }

        vm.defaulting();
        // Test that we don't let user buy something if it's over our stock
        try {
            vm.payByCash(8, "cc", "$100*2");
            fail("Let user buy amount over the stock that we have.");
        } catch (NoSuchElementException ne) {
            // Check that we didn't take their money
            assertEquals(vm.getCash().get("$100"), 5);
            // Check that we didn't lose stock
            assertEquals(7, vm.getInventory().get(vm.searchByItemCode("cc")));
        }
        vm.defaulting();

        // Test that we don't let user buy something if we don't have enough change by first removing all our change
        vm.updateLine(".src/main/resources/cash.txt", "5c", "0", 1);
        vm.updateLine(".src/main/resources/cash.txt", "10c", "0", 1);
        vm.updateLine(".src/main/resources/cash.txt", "20c", "0", 1);
        vm.updateLine(".src/main/resources/cash.txt", "50c", "0", 1);
        vm.updateLine(".src/main/resources/cash.txt", "$1", "0", 1);

        UserInterface ui = new UserInterface();
        try{
            vm.payByCash(3, "sp", "$50*1");
            fail("allowed to buy when there was no change");
        }catch(IllegalStateException ie){
            // Check that we didn't lose stock
            assertEquals(vm.getInventory().get(vm.searchByItemCode("sp")), 7);
            // Check that we didn't take their money
            assertEquals(vm.getCash().get("$50"), 5);
        }


        vm.defaulting();
    }

    @Test
    void testTransactions() {
        vm.defaulting();
        Transaction.loadTransactions(vm);

        LocalDateTime timeNow = LocalDateTime.now();
        Transaction successfulAnon = new Transaction("", new Food("sample", "imaginary", "md", 0.0), timeNow, 20.0, 3, "imagination", "SuccessfulTest");
        assertTrue(Transaction.anonTransactions.contains(successfulAnon));

        Transaction cancelled = new Transaction("", timeNow, "buildingTest");
        assertTrue(Transaction.cancelTransactions.contains(cancelled));

        User.signup("customer", "Tester", "test", "password1234");
        Transaction successfulUser = new Transaction("Tester", new Food("sample", "imaginary", "md", 0.0), timeNow, 20.0, 0, "imagination", "SuccessfulTest");
        assertTrue(successfulUser.getPaymentMethod().equals("imagination"));

        assertTrue(Math.abs(successfulUser.getPaid() - 20.0) < 0.00001);
        assertTrue(Transaction.userTransactions.get(User.getUserByName("Tester")).contains(successfulUser));
        Transaction repeatSuccess = new Transaction("Tester", new Food("sample", "imaginary", "md", 0.0), timeNow, 20.0, 0, "imagination", "SuccessfulTest");
        assertTrue(Transaction.userTransactions.get(User.getUserByName("Tester")).contains(repeatSuccess));
        vm.defaulting();
    }

    @Test
    void callingFood() {
        Food f = new Food("new food", "just new", "jn", 100000);
        assertTrue(f.getCategory().equals("just new"));
        assertTrue(f.getName().equals("new food"));
        f.setCategory("actually old");
        assertTrue(f.getCategory().equals("actually old"));
        f.setName("dust");
        assertTrue(f.getName().equals("dust"));
        f.setItemCode("du");
        assertTrue(f.getItemCode().equals("du"));
        f.setCost(2000);
        assertTrue(Math.abs(f.getCost() - 2000) < 0.0001);
    }

    @Test void sellerTestEditItemDetail() {
        john.editItemDetail("category", "pi", "Candy");
        assertEquals(ui.vm.searchByItemCode("pi").getCategory(), "Candy");
        ui.vm.defaulting();
    }

    @Test void adminsEditChange() {
        Cashier.editChange("$5", 15);
        assertEquals(ui.vm.getCash().get("$5"), 15);
        karen.editChange("$20", 1);
        assertEquals(ui.vm.getCash().get("$20"), 1);
        ui.vm.defaulting();

    }

    @Test void testTransactionSummaries(){
        vm.defaulting();
        Transaction.loadTransactions(vm);
        Owner o = new Owner("md", "md", "password");
        assertEquals(o.getCancelledSummary().split("\n").length, 5);
        Cashier c = new Cashier("md", "md", "password");
        assertEquals(c.getTransactionSummary().split("\n").length, 6);
        vm.defaulting();
    }

    @Test void ownerAddRemove() {
        // Test removing
        vm.defaulting();
        User.loadUsers();
        assertEquals(5, User.getUsers().size()); //It fails even here when vm.defaulting makes users.txt have 5 users. Test stack trace says that there are 49 users :0
        // test removing the owner- should not be able to
        try {
            Owner.removeUser("generic");
            fail("tried to remove owner");
            Owner.removeUser("fake user");
            fail("tried to remove someone who's not there");
        } catch (IllegalStateException ie) {
            assertTrue(true);
        } catch (NoSuchElementException ne) {
            assertTrue(true);
        }

        ui.currentUser = new Owner("Own", "ownzer", "o123");
        // test removing someone who's there
        Owner.removeUser("mark234");
        assertEquals(4, User.getUsers().size());

        vm.defaulting();
        User.loadUsers();
        // Test adding
        Owner.addUser("cashier", "test", "newname", "password");
        assertTrue(User.getUsers().contains(User.getUserByName("test")));
        assertEquals(6, User.getUsers().size());
        // Cannot add invalid user type
        Owner.addUser("sca", "test", "test1", "p");
        assertEquals(6, User.getUsers().size());
        // Cannot add customer
        Owner.addUser("customer", "test", "test2", "p");
        assertEquals(6, User.getUsers().size());
        // Cannot add owner
        Owner.addUser("owner", "test", "test3", "p");
        assertEquals(6, User.getUsers().size());
        vm.defaulting();
    }

    // Testing getUsernames() in Owner class
    @Test
    void testGetUsernames() {
        String usernames = owner.getUsernames();
        // 140 characters is the length of the start and end of the usernames table.
        // If length is above that, it means method returned 1 or more username's data
        assertTrue(usernames.length() > 140);
    }

    // Testing editItemDetail() in Seller class
    @Test
    void testEditItemDetailSeller() {
        boolean changed = false;
        // name
        vm.defaulting();
        john.editItemDetail("name", "Pringles", "testName");
        vm.loadInventory();
        changed = false;
        for(Food f: vm.getInventory().keySet()) {
            if(f.getName().equals("testName"))
                changed = true;
        }
        assertTrue(changed);
        vm.defaulting();
        // code
        john.editItemDetail("code", "ps", "testCode");
        vm.loadInventory();
        changed = false;
        for(Food f: vm.getInventory().keySet()) {
            if(f.getItemCode().equals("testCode"))
                changed = true;
        }
        assertTrue(changed);
        vm.defaulting();
        // category
        john.editItemDetail("category", "Chocolates", "testCategory");
        vm.loadInventory();
        changed = false;
        for(Food f: vm.getInventory().keySet()) {
            if(f.getCategory().equals("testCategory"))
                changed = true;
        }
        assertTrue(changed);
        vm.defaulting();
        // price
        john.editItemDetail("price", "1.3", "100.0");
        vm.loadInventory();
        changed = false;
        for(Food f: vm.getInventory().keySet()) {
            if(f.getCost() == 100.0)
                changed = true;
        }
        assertTrue(changed);
        vm.defaulting();
        // quantity
        john.editItemDetail("quantity", "7", "15");
        vm.loadInventory();
        changed = false;
        for(Food f: vm.getInventory().keySet()) {
            if(vm.getInventory().get(f) == 15)
                changed = true;
        }
        assertTrue(changed);
        vm.defaulting();
    }

    // Testing getSummary() in Seller class
    @Test
    void testGetSummarySeller() {
        String summary = john.getSummary();
        // 176 characters is the length of the start and end of the summary table.
        // If length is above that, it means method returned data
        assertTrue(summary.length() > 176);
    }

    // Testing signup() method in User class
    @Test
    void testSignup() {
        vm.defaulting();
        User.loadUsers();
        ui.currentUser = new Customer("", "", ""); // Default user when application starts
        // Testing restrictive property of signup
        int length = User.getUsers().size();
        User.signup("owner", "testName", "testUsername1", "testPassword");
        User.signup("cashier", "testName", "testUsername2", "testPassword");
        User.signup("seller", "testName", "testUsername3", "testPassword");
        // Length should not change since default customer user is not allowed to signup for an admin type account
        assertTrue(length == User.getUsers().size());

        // Testing working functionality
        User.signup("customer", "testName", "testUsername4", "testPassword");
        assertFalse(length == User.getUsers().size());
        vm.defaulting();
    }

}

/*@Test void testTransactionSummaries() {
         vm.defaulting();
         Owner o = new Owner("md", "md", "password");
         assertEquals(o.getCancelledSummary().split("\n").length, 7);
         Cashier c = new Cashier("md", "md", "password");
         assertEquals(c.getTransactionSummary().split("\n").length, 10);
         vm.defaulting();
    }*/
// @Test void ownerTestEditItemQuantity() {
//     owner.editItemQuantity("ts", 5);
//     assertEquals(ui.vm.getInventory().get(ui.vm.searchByItemCode("ts")), 5);
//     ui.vm.defaulting();
// }