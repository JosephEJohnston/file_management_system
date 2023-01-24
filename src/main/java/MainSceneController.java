import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextField;

import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class MainSceneController implements Initializable {

    @FXML
    private TextField pathTextField;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }

    public void changeDirectory(ActionEvent event) {
        String directoryPath = pathTextField.getText();

        File directory = new File(directoryPath);
        if (!directory.isDirectory()) {
            return;
        }

        File[] files = directory.listFiles();
        if (files == null) {
            return;
        }

        List<File> fileList = new ArrayList<>(List.of(files));
        fileList.forEach(System.out::println);
    }
}
