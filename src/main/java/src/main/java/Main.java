import java.util.HashMap;
import java.util.Map;

public class Main {
    public static Map<String, Account> accounts = new HashMap<>();

    public static void main(String[] args) {

        createAccounts();
        Bank bank = new Bank(accounts);
        System.out.println("Сумма на всех банковских счетах: " + bank.getSumAllAccounts());
        for (int j = 0; j < 10; j++) {
            new Thread(() -> {
                try {
                    for (int i = 0; i < 100; i++) {
                        bank.transfer("account№" + Bank.generateRandomIntIntRange(1, 1000),
                                "account№" + Bank.generateRandomIntIntRange(1, 1000),
                                (long) (Math.random() * 50000 / 0.95));
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                System.out.println("Сумма на всех банковских счетах: " + bank.getSumAllAccounts());
            }).start();
        }
    }

    public static void createAccounts() {
        for (int i = 1; i <= 1000; i++) {
            String accountNum = "account№" + i;
            long money = (long) (Math.random() * 200000 + 1);
            accounts.put(accountNum, new Account(money, accountNum, false));
        }
    }
}