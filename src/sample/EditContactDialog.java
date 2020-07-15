package sample;

import datamodel.Contact;
import datamodel.IsTextFieldNull;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import java.util.ArrayList;


public class EditContactDialog implements IsTextFieldNull {

    @FXML
    private TextField firstName;

    @FXML
    private TextField lastName;

    @FXML
    private TextField phone;

    @FXML
    private TextArea notes;

    @FXML
    private Label alert;

    private final ArrayList<TextInputControl> textInputList;

    public EditContactDialog()  {
        textInputList = new ArrayList<>();
    }

    public Contact getModifiedContact() {
        textInputList.add(firstName);
        textInputList.add(lastName);
        textInputList.add(notes);
        textInputList.add(phone);
        setInputListener(textInputList);

        if(isTextFieldNull(firstName) || isTextFieldNull(lastName) || isTextFieldNull(notes) || isTextFieldNull(phone)) {
            alert.setText("Please fill all fields!");
            return null;
        } else {
            return new Contact(firstName.getText(), lastName.getText(), phone.getText(), notes.getText());
        }
    }

    private void setInputListener(ArrayList<TextInputControl> list) {
        list.forEach(textInputControl ->
                textInputControl.textProperty().addListener(((observable, oldValue, newValue) ->
                        textInputControl.setText(newValue))
                ));
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



    public void showSelectedContact(Contact selectedContact) {
        firstName.setText(selectedContact.getFirstName());
        lastName.setText(selectedContact.getLastName());
        phone.setText(selectedContact.getPhoneNumber());
        notes.setText(selectedContact.getNotes());
    }

}
