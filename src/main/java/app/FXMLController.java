package app;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.FileChooser;

public class FXMLController implements Initializable {

    @FXML
    private TextField hashTextfield;

    @FXML
    private Button compareHashesButton;

    @FXML
    private Button browseFilesButton;

    @FXML
    private TextField filepathTextfield;

    @FXML
    private TextField calculatedHashTextfield;

    @FXML
    private Label hashesMatchLabel;

    @FXML
    private ChoiceBox<String> hashAlgorithmChoiceBox;

    private String[] hashAlgorithms = { "MD2", "MD4", "MD5", "SHA1", "SHA256", "SHA384", "SHA512" };
    private String selectedAlgorithm;
    private String program = "certUtil";
    private String baseCommand = "-hashfile";

    private File fileToHash;

    @FXML
    private void compareHashesButtonAction(ActionEvent event) throws IOException {
        String command = program + baseCommand + " \"" + filepathTextfield.getText() + "\" " + selectedAlgorithm;
        System.out.println("Running command: " + command);
        String filePath = filepathTextfield.getText().replace("\\", "\\\\");
        String result = Processes.run(program, baseCommand, filePath, selectedAlgorithm);
        //System.out.println(result);
        String[] lines = result.split("\\R");
        calculatedHashTextfield.setText(lines[1]);
        
        if(hashTextfield.getText().equals(calculatedHashTextfield.getText())){
            hashesMatchLabel.setText("Hashes match: True");
        }else{
            hashesMatchLabel.setText("Hashes match: False");

        }
    }

    @FXML
    private void browseFilesButtonAction(ActionEvent event) {

        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select File");
        fileToHash = fileChooser.showOpenDialog(hashTextfield.getScene().getWindow());

        filepathTextfield.setText(fileToHash.getAbsolutePath());

    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        hashAlgorithmChoiceBox.setItems(FXCollections.observableArrayList(hashAlgorithms));
        hashAlgorithmChoiceBox.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {

            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                selectedAlgorithm = newValue;
            }

        });
    }

}
