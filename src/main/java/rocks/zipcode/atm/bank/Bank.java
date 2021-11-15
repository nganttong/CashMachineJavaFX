package rocks.zipcode.atm.bank;

import rocks.zipcode.atm.ActionResult;

import java.util.HashMap;
import java.util.Map;

/**
 * @author ZipCodeWilmington
 */
public class Bank {

    public Map<Integer, Account> getAccounts() {
        return accounts;
    }

    private Map<Integer, Account> accounts = new HashMap<>();

    public Bank() {
        accounts.put(1, new BasicAccount(new AccountData(
                1, "Cay Tong", "CayT1@gmail.com", 500F
        )));

        accounts.put(2, new PremiumAccount(new AccountData(
                2, "Jimmy Crowley", "JimmyC@gmail.com", 200F
        )));

        accounts.put(3, new BasicAccount(new AccountData(
                3, "Trinh Tong", "TrinhT@zipcode.com", -10000F
        )));

        accounts.put(4, new PremiumAccount(new AccountData(
                4, "Relik Rajman", "RelikR@zipcode.com", 10000F
        )));

        accounts.put(5, new PremiumAccount(new AccountData(
                5, "Sol", "SolsPremAccount@zipcode.com", 1000000F
        )));
    }

    public ActionResult<AccountData> getAccountById(int id) {
        Account account = accounts.get(id);

        if (account != null) {
            return ActionResult.success(account.getAccountData());
        } else {
            return ActionResult.fail("No account with id: " + id + "\nEnter a valid account ID and click submit.");
        }
    }


    public void createBasicAccount(String nameForBasic, String emailForBasic, Float initialDepositForBasic) {
        int id = accounts.keySet().size();
        System.out.println("Got id size");
        accounts.put(id, new BasicAccount(new AccountData(id, nameForBasic, emailForBasic, initialDepositForBasic)));
        System.out.println("added new account");
    }

    public void createPremiumAccount(String nameForPremium, String emailForPremium, Float initialDepositForPremium) {
        int id = accounts.keySet().size();
        accounts.put(id, new BasicAccount(new AccountData(id, nameForPremium, emailForPremium, initialDepositForPremium)));
    }

    public ActionResult<AccountData> deposit(AccountData accountData, Float amount) {
        Account account = accounts.get(accountData.getId());
        account.deposit(amount);

        return ActionResult.success(account.getAccountData());
    }

    public ActionResult<AccountData> withdraw(AccountData accountData, Float amount) {
        Account account = accounts.get(accountData.getId());
        boolean ok = account.withdraw(amount);

        if (ok) {
            return ActionResult.success(account.getAccountData());
        } else {
            return ActionResult.fail("Withdraw failed: " + amount + ". Account has: " + account.getBalance());
        }
    }
}
