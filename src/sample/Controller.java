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

    @FXML
    private Label totalNumOfContacts;

    private SortedList<Contact> sortedContacts;


    public void initialize() {
        //ContactData.getInstance().getContacts().clear();
        //display row data
        ObservableList<Contact> list = ContactData.getInstance().getContacts();
        tableView.setItems(list);

        //get total number of saved contacts
        String size = String.valueOf(list.size());
        totalNumOfContacts.setText(size);

        //responsive table view layout
        tableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        tableView.getSelectionModel().selectFirst();

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
                        tableView.getSelectionModel().selectFirst();
                        break;

                    case "Last Name":
                        tableView.getSortOrder().clear();
                        firstNameCol.setSortable(false);
                        tableView.getSortOrder().add(secondNameCol);
                        secondNameCol.setSortable(true);
                        secondNameCol.setSortType(TableColumn.SortType.ASCENDING);
                        tableView.getSelectionModel().selectFirst();
                        break;

                    case "Default":
                        tableView.setItems(list);
                        tableView.getSelectionModel().selectFirst();
                        break;
                }
            }
        });

    }


    public void handleAdd()  {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("NewContactDialog.fxml"));
        Dialog<ButtonType> dialog = new Dialog<>();

        try {
            dialog.getDialogPane().setContent(fxmlLoader.load());
            dialog.initOwner(gridPane.getScene().getWindow());
            dialog.setTitle("Add new contact");
        }
        catch(IOException e) {
            System.out.println(e.getMessage());
        }

        NewContactDialog newContactDialogController = fxmlLoader.getController();

        dialog.getDialogPane().getButtonTypes().add(ButtonType.OK);
        dialog.getDialogPane().getButtonTypes().add(ButtonType.CANCEL);

        //form data validation
        final Button btOk = (Button) dialog.getDialogPane().lookupButton(ButtonType.OK);
        btOk.addEventFilter(ActionEvent.ACTION, event -> {
            //if none of the fields are empty, contact is saved
            boolean isSaved = newContactDialogController.saveContact();
            if (!isSaved) {
                event.consume();
            }
        });

        Optional<ButtonType> result = dialog.showAndWait();

        if(result.isPresent() && result.get() == ButtonType.CANCEL) {
                dialog.close();
            }

        }

    public void handleEdit()  {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("EditContactDialog.fxml"));
        Dialog<ButtonType> dialog = new Dialog<>();

        try {
            dialog.getDialogPane().setContent(fxmlLoader.load());
            dialog.initOwner(gridPane.getScene().getWindow());
            dialog.setTitle("Edit existing contact");
        }
        catch(IOException e) {
            System.out.println(e.getMessage());
        }

        EditContactDialog editController = fxmlLoader.getController();

        //get selection on tableview
        Contact selectedContact = tableView.getSelectionModel().getSelectedItem();

        //copy selection details on edit dialog pane
        editController.showSelectedContact(selectedContact);

        dialog.getDialogPane().getButtonTypes().add(ButtonType.OK);
        dialog.getDialogPane().getButtonTypes().add(ButtonType.CANCEL);

        final Button btOk = (Button) dialog.getDialogPane().lookupButton(ButtonType.OK);
        btOk.addEventFilter(ActionEvent.ACTION, event -> {
            //if one/all of the fields are empty, contact is not edited
            Contact editedContact = editController.getModifiedContact();
            if (editedContact == null) {
                event.consume();
                //otherwise, change contact details
            } else {
                ContactData.getInstance().editExistingContact(selectedContact, editedContact);
            }
        });

        Optional<ButtonType> result = dialog.showAndWait();

        if(result.isPresent() && result.get() == ButtonType.CANCEL) {
            dialog.close();
        }

    }

    }



