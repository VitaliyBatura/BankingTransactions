import java.util.Map;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;

public class Bank {

    private Map<String, Account> accounts;
    private final Random random = new Random();

    public Bank(Map<String, Account> accounts) {
        this.accounts = accounts;
    }

    public synchronized boolean isFraud(String fromAccountNum, String toAccountNum, long amount)
            throws InterruptedException {
        Thread.sleep(1000);
        return random.nextBoolean();
    }

    /**
     * TODO: реализовать метод. Метод переводит деньги между счетами. Если сумма транзакции > 50000,
     * то после совершения транзакции, она отправляется на проверку Службе Безопасности – вызывается
     * метод isFraud. Если возвращается true, то делается блокировка счетов (как – на ваше
     * усмотрение)
     */
    public void transfer(String fromAccountNum, String toAccountNum, long amount) throws InterruptedException {

        synchronized (compareTo(fromAccountNum) > 0 ? fromAccountNum : toAccountNum) {

            if (amount > getBalance(fromAccountNum)) {
                System.out.println("На счёте " + fromAccountNum + " недостаточно средств.");
                System.out.println("Баланс счёта " + fromAccountNum + " равен "
                        + accounts.get(fromAccountNum).getMoney() + " рублей.");
            }

            if ((amount <= getBalance(fromAccountNum) && amount <= 50000 && !accounts.get(fromAccountNum).isBlocked()
                    && !accounts.get(toAccountNum).isBlocked()) || (amount <= getBalance(fromAccountNum)
                    && amount > 50000 && !isFraud(fromAccountNum, toAccountNum, amount)
                    && !accounts.get(fromAccountNum).isBlocked()
                    && !accounts.get(toAccountNum).isBlocked())) {
                accounts.get(fromAccountNum).setMoney(getBalance(fromAccountNum) - amount);
                accounts.get(toAccountNum).setMoney(getBalance(toAccountNum) + amount);
                System.out.println("Со счёта " + fromAccountNum + " на счёт " + toAccountNum + " переведено "
                        + amount + " рублей.");
                System.out.println("Баланс счёта " + fromAccountNum + " равен "
                        + accounts.get(fromAccountNum).getMoney() + " рублей.");
                System.out.println("Баланс счёта " + toAccountNum + " равен "
                        + accounts.get(toAccountNum).getMoney() + " рублей.");
            }

            if (accounts.get(fromAccountNum).isBlocked()) {
                System.out.println("Транзакция невозможна. Счёт " + fromAccountNum + " заблокирован.");
            }

            if (accounts.get(toAccountNum).isBlocked()) {
                System.out.println("Транзакция невозможна. Счёт " + toAccountNum + " заблокирован.");
            }

            if (amount > 50000 && isFraud(fromAccountNum, toAccountNum, amount)) {
                accounts.get(fromAccountNum).setBlocked(true);
                accounts.get(toAccountNum).setBlocked(true);
                System.out.println("Сомнительная транзакция. Счета " + fromAccountNum + " и " + toAccountNum
                        + " заблокированы.");
            }
        }
    }

    /**
     * TODO: реализовать метод. Возвращает остаток на счёте.
     */
    public long getBalance(String accountNum) {
        return accounts.get(accountNum).getMoney();
    }

    public AtomicLong getSumAllAccounts() {
        AtomicLong sumAllAccounts = new AtomicLong();
        for (Map.Entry<String, Account> entry : accounts.entrySet()) {
            sumAllAccounts.set(sumAllAccounts.get() + getBalance(entry.getValue().getAccNumber()));
        }
        return sumAllAccounts;
    }

    public int compareTo(String fromAccountNum) {
        return fromAccountNum.compareTo(this.toString());
    }

    public static int generateRandomIntIntRange(int min, int max) {
        Random r = new Random();
        return r.nextInt((max - min) + 1) + min;
    }

    public void setAccounts(Map<String, Account> accounts) {
        this.accounts = accounts;
    }
}