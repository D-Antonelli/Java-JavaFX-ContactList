package sample;

import datamodel.Contact;
import datamodel.ContactData;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;

import javafx.scene.Scene;
import javafx.scene.control.*;

import javafx.scene.layout.GridPane;
import javafx.stage.Modality;
import javafx.stage.Stage;


import java.io.IOException;
import java.util.ArrayList;


public class Controller {

    @FXML
    private TableView<Contact> tableView;

    @FXML
    private MenuItem addBtn;

    @FXML
    private GridPane gridPane;


    public void initialize() {
        ArrayList<Contact> contactArrayList = new ArrayList<>();

        contactArrayList.add(new Contact("Luca", "Antonelli", "0755546776", "My spouse"));
        contactArrayList.add(new Contact("Leyla", "Aydin", "9093774783", "Mom's cellular phone number"));

        ContactData.getInstance().getContacts().setAll(contactArrayList);
        tableView.setItems(ContactData.getInstance().getContacts());

    }

    public void handleAddContact() throws IOException {
        Dialog<Contact> dialog = new Dialog<>();
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("ContactDialog.fxml"));
        //ContactDialog contactDialogController = fxmlLoader.getController();
        dialog.getDialogPane().setContent(fxmlLoader.load());
        dialog.getDialogPane().getButtonTypes().add(ButtonType.OK);
        dialog.getDialogPane().getButtonTypes().add(ButtonType.CANCEL);
        dialog.initOwner(gridPane.getScene().getWindow());
        dialog.setTitle("Add new contact");
        dialog.show();
    }


}
