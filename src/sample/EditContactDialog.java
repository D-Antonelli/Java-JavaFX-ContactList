package sample;

import datamodel.Contact;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Cell;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;


public class EditContactDialog {

    @FXML
    private TextField firstName;

    @FXML
    private TextField lastName;

    @FXML
    private TextField phone;

    @FXML
    private TextArea notes;




    public Contact getEditedContact() {
        firstName.textProperty().addListener((observable, oldValue, newValue) -> firstName.setText(newValue));

        lastName.textProperty().addListener((observable, oldValue, newValue) -> lastName.setText(newValue));

        phone.textProperty().addListener((observable, oldValue, newValue) -> phone.setText(newValue));

        notes.textProperty().addListener((observable, oldValue, newValue) -> notes.setText(newValue));

        return new Contact(firstName.getText(), lastName.getText(), phone.getText(), notes.getText());
    }


    public void showSelected(Contact selectedContact) {
        firstName.setText(selectedContact.getFirstName());
        lastName.setText(selectedContact.getLastName());
        phone.setText(selectedContact.getPhoneNumber());
        notes.setText(selectedContact.getNotes());
    }

}
