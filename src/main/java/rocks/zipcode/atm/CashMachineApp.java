package rocks.zipcode.atm;

import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.PopupBuilder;
import rocks.zipcode.atm.bank.Bank;
import javafx.application.Application;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.scene.layout.FlowPane;

import java.util.ArrayList;
import java.util.List;

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

        welcomeLabel.setFont(Font.font("Verdana", FontWeight.BOLD, 20));
        welcomeLabel.setTextFill(Color.BEIGE);


        VBox vbox = new VBox(10);
        vbox.setPrefSize(600, 600);
        vbox.setAlignment(Pos.TOP_CENTER);
        //add background using vbox.setStyle
        vbox.setStyle("-fx-background-color:c5b9cd");
        //vbox.setStyle("-fx-background-image: url(\File:background.jpg\"););

        TextArea areaInfo = new TextArea();


        Button btnCreateAccount = new Button("Create New Account");
        btnCreateAccount.setOnAction(event -> {
        List<String> choices = new ArrayList<>();
        choices.add("Basic");
        choices.add("Premium");

        ChoiceDialog<String> dialog = new ChoiceDialog<>("Basic", choices);
        dialog.setTitle("New Account");

        dialog.setContentText("Choose account type:");
        dialog.show();
        });

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
            attemptAccountLogin(id, areaInfo, btnDeposit, btnWithdraw);
        });


        Button btnExit = new Button("Logout");
        btnExit.setOnAction(e -> {
            cashMachine.exit();

            btnDeposit.setDisable(true);
            btnWithdraw.setDisable(true);


            areaInfo.setText(cashMachine.toString());
        });

        Menu accountMenu = new Menu("Accounts");
        MenuItem accountOne = new MenuItem("Account 1000");
        accountOne.setOnAction(event -> {
        attemptAccountLogin(1000, areaInfo, btnDeposit, btnWithdraw);
        });
        MenuItem accountTwo = new MenuItem("Account 2000");
        accountTwo.setOnAction(event -> {
            attemptAccountLogin(2000, areaInfo, btnDeposit, btnWithdraw);
        });
        MenuItem accountThree = new MenuItem("Account 3000");
        accountThree.setOnAction(event -> {
            attemptAccountLogin(3000, areaInfo, btnDeposit, btnWithdraw);
        });
        MenuItem accountFour = new MenuItem("Account 4000");
        accountFour.setOnAction(event -> {
            attemptAccountLogin(4000, areaInfo, btnDeposit, btnWithdraw);
        });

        accountMenu.getItems().addAll(accountOne, accountTwo, accountThree, accountFour);

        MenuBar menuBar = new MenuBar(accountMenu);
        //sets position of menu bar
        menuBar.setTranslateX(0);
        menuBar.setTranslateY(0);


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

    private void attemptAccountLogin(Integer id, TextArea areaInfo, Button btnDeposit, Button btnWithdraw) {
        cashMachine.login(id);
        if(cashMachine.hasLoadedValidAccount()){
            btnDeposit.setDisable(false);
            btnWithdraw.setDisable(false);
        }

        areaInfo.setText(cashMachine.toString());
    }
}
