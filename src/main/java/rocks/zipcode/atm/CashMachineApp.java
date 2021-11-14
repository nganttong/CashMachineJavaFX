package rocks.zipcode.atm;

import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.PopupBuilder;
import javafx.util.converter.CurrencyStringConverter;
import javafx.util.converter.FloatStringConverter;
import javafx.util.converter.IntegerStringConverter;
import javafx.util.converter.NumberStringConverter;
import rocks.zipcode.atm.bank.Bank;
import javafx.application.Application;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.scene.layout.FlowPane;
import sun.jvm.hotspot.oops.FloatField;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * @author ZipCodeWilmington
 */
public class CashMachineApp extends Application {

    private TextField accountField = new TextField();
    private TextField depositField = new TextField();
    private TextField withdrawField = new TextField();
    private CashMachine cashMachine = new CashMachine(new Bank());
    private Label welcomeLabel = new Label("Welcome to ZipCloudBank!");

    private Parent createContent() {

        depositField.setTextFormatter(new TextFormatter<>(new FloatStringConverter()));
        withdrawField.setTextFormatter(new TextFormatter<>(new FloatStringConverter()));

        welcomeLabel.setFont(Font.font("Verdana", FontWeight.BOLD, 20));
        welcomeLabel.setTextFill(Color.BEIGE);


        VBox vbox = new VBox(10);
        vbox.setPrefSize(600, 600);
        vbox.setAlignment(Pos.TOP_CENTER);
        //add background using vbox.setStyle
        vbox.setStyle("-fx-background-color:c5b9cd");
        //vbox.setStyle("-fx-background-image: url(\File:background.jpg\"););

        TextArea areaInfo = new TextArea();

        Button btnDeposit = new Button("Deposit");
        btnDeposit.setOnAction(e -> {
            Float amount = Float.parseFloat(depositField.getText());
            cashMachine.deposit(amount);

            areaInfo.setText(cashMachine.toString());
        });
        btnDeposit.setDisable(true);

        Button btnWithdraw = new Button("Withdraw");
        btnWithdraw.setOnAction(e -> {
            Float amount = Float.parseFloat(withdrawField.getText());
            cashMachine.withdraw(amount);

            areaInfo.setText(cashMachine.toString());
        });
        btnWithdraw.setDisable(true);

        Button btnSubmit = new Button("Set Account ID");
        btnSubmit.setOnAction(e -> {
            int id = Integer.parseInt(accountField.getText());
            cashMachine.attemptAccountLogin(id, areaInfo, btnDeposit, btnWithdraw);
        });


        Button btnExit = new Button("Logout");
        btnExit.setOnAction(e -> {
            cashMachine.exit();

            btnDeposit.setDisable(true);
            btnWithdraw.setDisable(true);

            areaInfo.setText(cashMachine.toString());
        });

        // Accounts Menu Stuff

        Menu accountMenu = new Menu("Accounts");
        cashMachine.generateAccountMenuLogin(accountMenu, areaInfo, btnDeposit, btnWithdraw);

        MenuBar menuBar = new MenuBar(accountMenu);
        //sets position of menu bar
        menuBar.setTranslateX(0);
        menuBar.setTranslateY(0);


        Button btnCreateAccount = new Button("Create New Account");
        btnCreateAccount.setOnAction(event -> {
            List<String> choices = new ArrayList<>();
            choices.add("Basic");
            choices.add("Premium");

            ChoiceDialog<String> dialog = new ChoiceDialog<>("Basic", choices);
            dialog.setTitle("New Account");
            dialog.setContentText("Choose account type:");
            Optional<String> result = dialog.showAndWait();

            if (result.isPresent()){
                String accountType = result.get();

                Stage accountStage = new Stage();

                TextField newAccountName = new TextField();
                TextField newAccountEmail = new TextField();
                TextField newAccountBalance = new TextField();
                newAccountBalance.setTextFormatter(new TextFormatter<>(new FloatStringConverter()));

                Label nameLabel = new Label("Name: ");
                Label emailLabel = new Label("Email: ");
                Label balanceLabel = new Label("Deposit Amount: ");

                Button button = new Button("Create new account.");
                button.setTranslateX(250);
                button.setTranslateY(75);

                HBox box = new HBox(nameLabel, newAccountName, emailLabel, newAccountEmail, balanceLabel, newAccountBalance);

                // TODO -- set styling for this dialog box
                Button newAccountButton = new Button("Create");
                Group newAccountStuff = new Group(box, newAccountButton);
                accountStage.setScene(new Scene(newAccountStuff));
                accountStage.show();
                newAccountButton.setOnAction(e -> {
                    //Retrieving data
                    Float createAccountBalance = Float.parseFloat(newAccountBalance.getText());
                    cashMachine.createAccount(accountType, newAccountName.getText(), newAccountEmail.getText(), createAccountBalance);
                    cashMachine.addAccountToMenu(accountMenu, areaInfo, btnDeposit, btnWithdraw);
                    accountStage.hide();
                });

            }
        });

        FlowPane flowpaneAccount = new FlowPane();
        flowpaneAccount.getChildren().add(accountField);
        flowpaneAccount.getChildren().add(btnSubmit);

        FlowPane flowpaneDeposit = new FlowPane();
        flowpaneDeposit.getChildren().add(depositField);
        flowpaneDeposit.getChildren().add(btnDeposit);

        FlowPane flowpaneWithdraw = new FlowPane();
        flowpaneWithdraw.getChildren().add(withdrawField);
        flowpaneWithdraw.getChildren().add(btnWithdraw);

        FlowPane flowpaneSwitchUser = new FlowPane();
        flowpaneSwitchUser.getChildren().add(btnExit);

        vbox.getChildren().addAll(menuBar,welcomeLabel,btnCreateAccount, flowpaneAccount,
                flowpaneDeposit, flowpaneWithdraw, flowpaneSwitchUser, areaInfo);
        return vbox;
    }

    @Override
    public void start(Stage stage) throws Exception {
        stage.setScene(new Scene(createContent()));
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
