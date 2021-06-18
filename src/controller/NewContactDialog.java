package controller;

import datamodel.Contact;
import datamodel.IsTextFieldNull;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.TextInputControl;

public class NewContactDialog implements IsTextFieldNull{

    @FXML
    private TextField firstName;

    @FXML
    private TextField lastName;

    @FXML
    private TextField phoneNumber;

    @FXML
    private TextArea notes;

    @FXML
    private Label promptText;

    public Contact createNewContact() {
        //data validation
        if(isTextFieldNull(firstName) || isTextFieldNull(lastName) || isTextFieldNull(phoneNumber) || isTextFieldNull(notes)) {
            promptText.setText("Please fill all fields!");
            return null;
        } else {
            return new Contact(firstName.getText(), lastName.getText(), phoneNumber.getText(), notes.getText());
        }
    }


    @Override
    public boolean isTextFieldNull(TextInputControl input) {
        if(input.getText().trim().isEmpty() || input.getText().isEmpty()) {
            input.styleProperty().set("-fx-border-color: red;");
            return true;
        }
        input.styleProperty().set("-fx-border-color: green;");
        return false;
    }
}
