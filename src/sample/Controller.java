package sample;

import datamodel.Contact;
import datamodel.ContactData;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.GridPane;

import java.io.IOException;
import java.util.Comparator;
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

    @FXML
    private TextField searchField;

    private SortedList<Contact> sortedContacts;

    private FilteredList<Contact> filteredContacts;

    private ObservableList<Contact> list = ContactData.getInstance().getContacts();

    private boolean isFilterToggled = false;

    public void initialize() {
        //ContactData.getInstance().getContacts().clear();
        //display row data

        tableView.setItems(list);

        //get total number of saved contacts
        totalNumOfContacts.setText(String.valueOf(list.size()));

        //responsive table view layout
        tableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        tableView.getSelectionModel().selectFirst();

        //reflect row changes into total num of contacts
        tableView.getItems().addListener(new ListChangeListener<Contact>() {
            @Override
            public void onChanged(Change<? extends Contact> c) {
                while(c.next()) {
                    totalNumOfContacts.setText(String.valueOf(list.size()));
                }
            }
        });

        //set choicebox
        choiceBox.setItems(FXCollections.observableArrayList("First Name", "Last Name", "Date Created"));

        //sort table columns according to selected choice
        choiceBox.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {

            //While searching for data, allow sorting
            if(isFilterToggled) {
                sortedContacts = new SortedList<>(filteredContacts);
            } else {
                sortedContacts = new SortedList<>(list);
            }

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

                case "Date Created":
                    //if searching, bring filter list. Else, show only list.
                    if(isFilterToggled) {
                        tableView.setItems(filteredContacts);
                    } else {
                        tableView.setItems(list);
                    }

                    tableView.getSelectionModel().selectFirst();
                    break;
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
            Contact newContact = newContactDialogController.createNewContact();

            if (newContact == null) {
                event.consume();
            }

            else {
                ContactData.getInstance().addNewContact(newContact);
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
                tableView.sort();
            }
        });

        Optional<ButtonType> result = dialog.showAndWait();

        if(result.isPresent() && result.get() == ButtonType.CANCEL) {
            dialog.close();
        }

    }

    public void handleDelete() {
        //get selected contact
        Contact selectedContact = tableView.getSelectionModel().getSelectedItem();

        //set confirmation alert
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Are you sure you want to delete contact?");
        alert.setHeaderText("Delete contact");

        Optional<ButtonType> optional = alert.showAndWait();

        if(optional.isPresent() && optional.get() == ButtonType.OK) {
            ContactData.getInstance().deleteContact(selectedContact);
        } else if(optional.isPresent() && optional.get() == ButtonType.CANCEL){
            alert.close();
        }
    }


    public void handleSearch(KeyEvent keyEvent) {
        isFilterToggled = true;
        filteredContacts = new FilteredList<Contact>(list, p -> true);

        filteredContacts.setPredicate(p ->
                                        p.getFirstName().toLowerCase().startsWith(searchField.getText().toLowerCase().trim()) ||
                                        p.getLastName().toLowerCase().startsWith(searchField.getText().toLowerCase().trim()) ||
                                        p.getPhoneNumber().startsWith(searchField.getText().trim())
                                        );

        tableView.setItems(filteredContacts);

        if(searchField.getText().isEmpty()) {
            isFilterToggled = false;
        }
    }

}



