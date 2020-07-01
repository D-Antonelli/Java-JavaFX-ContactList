package sample;

import datamodel.Contact;
import datamodel.ContactData;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import java.util.ArrayList;


public class Controller {

    @FXML
    private TableView<Contact> tableView;

    @FXML
    private TableColumn<Contact, String> firstCol;

    @FXML
    private TableColumn<Contact, String> secondCol;

    @FXML
    private TableColumn<Contact, String> thirdCol;

    @FXML
    private TableColumn<Contact, String> lastCol;

    private ArrayList<Contact> contactArrayList;


    public void initialize() {
        contactArrayList = new ArrayList<>();

        Contact firstContact = new Contact();
        firstContact.setFirstName("Derya");
        firstContact.setLastName("Aydin");
        firstContact.setPhoneNumber("0984647");
        firstContact.setNotes("Personal");

        contactArrayList.add(firstContact);
        ContactData.getInstance().getContacts().setAll(contactArrayList);
        tableView.setItems(ContactData.getInstance().getContacts());

    }


}
