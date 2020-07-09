package sample;

import datamodel.Contact;
import datamodel.ContactData;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;

import java.io.IOException;
import java.util.Optional;


public class Controller {

    @FXML
    private TableView<Contact> tableView;

    @FXML
    private TableColumn<Contact, String> firstNameCol;

    @FXML
    private TableColumn<Contact, String> secondNameCol;

    @FXML
    private GridPane gridPane;

    @FXML
    private ChoiceBox<String> choiceBox;

    private SortedList<Contact> sortedContacts;


    public void initialize() {
        //ContactData.getInstance().getContacts().clear();
        //display row data
        ObservableList<Contact> list = ContactData.getInstance().getContacts();
        tableView.setItems(list);


        //responsive table view layout
        tableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        //set choicebox
        choiceBox.setItems(FXCollections.observableArrayList("First Name", "Last Name", "Default"));

        //sort table columns according to selected choice
        choiceBox.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                sortedContacts = new SortedList<>(ContactData.getInstance().getContacts());
                tableView.setItems(sortedContacts);
                sortedContacts.comparatorProperty().bind(tableView.comparatorProperty());

                switch (newValue) {
                    case "First Name":
                        tableView.getSortOrder().clear();
                        secondNameCol.setSortable(false);
                        tableView.getSortOrder().add(firstNameCol);
                        firstNameCol.setSortable(true);
                        firstNameCol.setSortType(TableColumn.SortType.ASCENDING);
                        break;

                    case "Last Name":
                        tableView.getSortOrder().clear();
                        firstNameCol.setSortable(false);

                        tableView.getSortOrder().add(secondNameCol);
                        secondNameCol.setSortable(true);
                        secondNameCol.setSortType(TableColumn.SortType.ASCENDING);
                        break;

                    case "Default":
                        tableView.setItems(list);
                        break;
                }
            }
        });
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



