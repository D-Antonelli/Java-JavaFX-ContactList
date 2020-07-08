package sample;

import datamodel.Contact;
import datamodel.ContactData;
import javafx.collections.ObservableList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.util.Callback;

import java.io.IOException;
import java.util.Comparator;
import java.util.Optional;
import java.util.SortedSet;


public class Controller {

    @FXML
    private TableView<Contact> tableView;

    @FXML
    private GridPane gridPane;

    private SortedList<Contact> sortedContacts = new SortedList<>(ContactData.getInstance().getContacts());


    public void initialize() {
        //ContactData.getInstance().getContacts().clear();
        //display row data
        //ObservableList<Contact> list = ContactData.getInstance().getContacts();
        //tableView.setItems(list);

        //sort items
        sortedContacts.comparatorProperty().bind(tableView.comparatorProperty());
        tableView.setItems(sortedContacts);

        //responsive table view layout
        tableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
    }



    public void handleAdd() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("ContactDialog.fxml"));

        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.getDialogPane().setContent(fxmlLoader.load());
        dialog.initOwner(gridPane.getScene().getWindow());
        dialog.setTitle("Add new contact");

        ContactDialog contactDialogController = fxmlLoader.getController();

        dialog.getDialogPane().getButtonTypes().add(ButtonType.OK);
        dialog.getDialogPane().getButtonTypes().add(ButtonType.CANCEL);

        //form data validation
        final Button btOk = (Button) dialog.getDialogPane().lookupButton(ButtonType.OK);
        btOk.addEventFilter(ActionEvent.ACTION, event -> {
            //if none of the fields are empty, contact is saved
            boolean isSaved = contactDialogController.saveContact();
            if (!isSaved) {
                event.consume();
            }
        });

        Optional<ButtonType> result = dialog.showAndWait();

        if(result.isPresent() && result.get() == ButtonType.CANCEL) {
                dialog.close();
            }

        }
    }



