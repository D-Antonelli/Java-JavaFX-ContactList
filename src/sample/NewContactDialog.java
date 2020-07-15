package sample;

import datamodel.Contact;
import datamodel.ContactData;
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

    public boolean saveContact() {
        //data validation
        if(isTextFieldNull(firstName) || isTextFieldNull(lastName) || isTextFieldNull(phoneNumber) || isTextFieldNull(notes)) {
            promptText.setText("Please fill all fields!");
            return false;
        } else {
            Contact newContact = new Contact(firstName.getText(), lastName.getText(), phoneNumber.getText(), notes.getText());
            ContactData.getInstance().addNewContact(newContact);
            return true;
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
