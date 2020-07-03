package sample;

import datamodel.Contact;
import datamodel.ContactData;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Optional;


public class Controller {

    @FXML
    private TableView<Contact> tableView;

    @FXML
    private MenuItem addBtn;

    @FXML
    private GridPane gridPane;


    public void initialize() {
        //ArrayList<Contact> contactArrayList = new ArrayList<>();

        //contactArrayList.add(new Contact("Luca", "Antonelli", "0755546776", "My spouse"));
        //contactArrayList.add(new Contact("Leyla", "Aydin", "9093774783", "Mom's cellular phone number"));

        //ContactData.getInstance().getContacts().setAll(contactArrayList);
        tableView.setItems(ContactData.getInstance().getContacts());

    }

    //TEST DIFFERENT SITUATIONS WITH CONTACT DIALOG

    public void handleAddContact() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("ContactDialog.fxml"));

        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.getDialogPane().setContent(fxmlLoader.load());
        dialog.initOwner(gridPane.getScene().getWindow());
        dialog.setTitle("Add new contact");

        ContactDialog contactDialogController = fxmlLoader.getController();

        dialog.getDialogPane().getButtonTypes().add(ButtonType.OK);
        dialog.getDialogPane().getButtonTypes().add(ButtonType.CANCEL);

        Optional<ButtonType> result = dialog.showAndWait();

        try {
            if (result.isPresent() && result.get() == ButtonType.OK) {
                contactDialogController.enterContactData();
            }
            else if(result.isPresent() && result.get() == ButtonType.CANCEL) {
                dialog.close();
            } else {
                dialog.close();
            }

        } catch (NullPointerException e) {
            System.out.println(e.getMessage());
        }



    }


}
