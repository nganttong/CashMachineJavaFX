package rocks.zipcode.atm;

import javafx.scene.control.Button;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextArea;
import rocks.zipcode.atm.bank.Account;
import rocks.zipcode.atm.bank.AccountData;
import rocks.zipcode.atm.bank.Bank;
import rocks.zipcode.atm.bank.BasicAccount;

import java.awt.*;
import java.util.function.Consumer;
import java.util.function.Supplier;

/**
 * @author ZipCodeWilmington
 */
public class CashMachine {

    private final Bank bank;
    private AccountData accountData = null;

    public CashMachine(Bank bank) {
        this.bank = bank;
    }

    private Consumer<AccountData> update = data -> {
        accountData = data;
    };

    public void login(int id) {
        tryCall(
                () -> bank.getAccountById(id),
                update
        );
    }

    public void deposit(Float amount) {
        if (accountData != null) {
            tryCall(
                    () -> bank.deposit(accountData, amount),
                    update
            );
        }
    }

    public void withdraw(Float amount) {
        if (accountData != null) {
            tryCall(
                    () -> bank.withdraw(accountData, amount),
                    update
            );
        }
    }

    public void exit() {
        if (accountData != null) {
            accountData = null;
        }
    }

    @Override
    public String toString() {
        return accountData != null ? accountData.toString() : "Enter a valid account ID and click submit, please.";
    }

    public boolean hasLoadedValidAccount(){
        return accountData != null;
    }

    private <T> void tryCall(Supplier<ActionResult<T> > action, Consumer<T> postAction) {
        try {
            ActionResult<T> result = action.get();
            if (result.isSuccess()) {
                T data = result.getData();
                postAction.accept(data);
            } else {
                String errorMessage = result.getErrorMessage();
                throw new RuntimeException(errorMessage);
            }
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    public void createAccount(String accountType, String acctName, String acctEmail, Float initialDeposit) {
        if (accountType.equals("Basic")) {
            bank.createBasicAccount(acctName, acctEmail, initialDeposit);
        } else if (accountType.equals("Premium")) {
            bank.createPremiumAccount(acctName, acctEmail, initialDeposit);
        }
    }

    public void generateAccountMenuLogin(Menu menu, TextArea textArea, Button btnDep, Button btnWith) {
        for (int acctKey : bank.getAccounts().keySet()) {
            AccountData acct = bank.getAccounts().get(acctKey).getAccountData();
            updateMenuItems(menu, textArea, btnDep, btnWith, acctKey, acct);
        }
    }

    public void attemptAccountLogin(Integer id, TextArea areaInfo, Button btnDeposit, Button btnWithdraw) {
        this.login(id);
        if (this.hasLoadedValidAccount()) {
            btnDeposit.setDisable(false);
            btnWithdraw.setDisable(false);
        }

        areaInfo.setText(this.toString());
    }

    public void addAccountToMenu(Menu accountMenu, TextArea areaInfo, Button btnDeposit, Button btnWithdraw) {
        int key = bank.getAccounts().keySet().size();
        AccountData newAccount = bank.getAccounts().get(key).getAccountData();
        updateMenuItems(accountMenu, areaInfo, btnDeposit, btnWithdraw, key, newAccount);
    }

    private void updateMenuItems(Menu accountMenu, TextArea areaInfo, Button btnDeposit, Button btnWithdraw, int key, AccountData newAccount) {
        MenuItem menuItem = new MenuItem(newAccount.getName());
        menuItem.setOnAction(event ->
                this.attemptAccountLogin(key, areaInfo, btnDeposit, btnWithdraw));
        accountMenu.getItems().add(menuItem);
    }
}
