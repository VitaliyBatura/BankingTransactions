import junit.framework.TestCase;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;

public class BankTest extends TestCase {

    Map<String, Account> accounts = new HashMap<>();
    Bank bank = new Bank(accounts);
    AtomicLong sumAllAccounts = new AtomicLong();
    String accNum;
    long money1;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        for (int i = 1; i <= 1000; i++) {
            String accountNum = "account№" + i;
            long money = (long) (Math.random() * 200000 + 1);
            if (i == 1) {
                accNum = "account№" + i;
                money1 = money;
            }
            sumAllAccounts.set(sumAllAccounts.get() + money);
            accounts.put(accountNum, new Account(money, accountNum, false));
        }
        bank.setAccounts(accounts);
    }

    public void testGetSumAllAccounts() {
        long expected = Long.parseLong(String.valueOf(sumAllAccounts));
        long actual = Long.parseLong(String.valueOf(bank.getSumAllAccounts()));
        assertEquals(expected, actual);
    }

    public void testGetBalance() {
        long expected = money1;
        long actual = accounts.get(accNum).getMoney();
        assertEquals(expected, actual);
    }

    public void testTransfer() throws InterruptedException {
        long before = Long.parseLong(String.valueOf(sumAllAccounts));
        for (int i = 0; i < 200; i++) {
            bank.transfer("account№" + Bank.generateRandomIntIntRange(1, 1000),
                    "account№" + Bank.generateRandomIntIntRange(1, 1000),
                    (long) (Math.random() * 50000 / 0.95));
        }
        long after = Long.parseLong(String.valueOf(bank.getSumAllAccounts()));
        assertEquals(before, after);
    }

    @Override
    protected void tearDown() throws Exception {
        super.tearDown();
    }
}
