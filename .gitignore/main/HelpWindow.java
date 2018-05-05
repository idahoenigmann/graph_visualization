package main;

import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.File;

class HelpWindow {
    static void about_window(Stage stage) {
        VBox root = new VBox();
        Scene scene = new Scene(root, 300, 200);

        //add stylesheet
        File file = new File("../../src/main/text.css");
        scene.getStylesheets().add("file:" + file.getAbsolutePath());

        Text t1 = new Text("Bla blub bla\n");   //name of program
        t1.getStyleClass().add("title");

        Text t2 = new Text("Version 1.0\n\n");  //version number
        t2.getStyleClass().add("body_small");

        Text t3 = new Text("Licensing Information");
        t3.setUnderline(true);
        t3.getStyleClass().add("body_small");

        EventHandler<javafx.scene.input.MouseEvent> license_window = event -> {
            String license_text = "Permission is hereby granted, free of charge, to any person or organization\n" +
                    "obtaining a copy of the software and accompanying documentation covered\n by" +
                    "this license (the \"Software\") to use, reproduce, display, distribute,\n" +
                    "execute, and transmit the Software, and to prepare derivative works of the\n" +
                    "Software, and to permit third-parties to whom the Software is furnished to\n" +
                    "do so, all subject to the following:\n" +
                    "\n" +
                    "The copyright notices in the Software and this entire statement, including\n" +
                    "the above license grant, this restriction and the following disclaimer,\n" +
                    "must be included in all copies of the Software, in whole or in part, and\n" +
                    "all derivative works of the Software, unless such copies or derivative\n" +
                    "works are solely in the form of machine-executable object code generated\n by" +
                    "a source language processor.\n" +
                    "\n" +
                    "THE SOFTWARE IS PROVIDED \"AS IS\", WITHOUT WARRANTY OF ANY KIND,\nEXPRESS OR" +
                    "IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF\nMERCHANTABILITY," +
                    "FITNESS FOR A PARTICULAR PURPOSE, TITLE AND\nNON-INFRINGEMENT. IN NO EVENT" +
                    "SHALL THE COPYRIGHT HOLDERS OR ANYONE DISTRIBUTING THE SOFTWARE BE LIABLE" +
                    "FOR ANY DAMAGES OR OTHER LIABILITY, WHETHER IN CONTRACT, TORT OR OTHERWISE,\n" +
                    "ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER\n" +
                    "DEALINGS IN THE SOFTWARE.\n";


            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setHeight(alert.getHeight() + 100);
            alert.setTitle("License");
            //alert.setHeaderText(null);
            alert.setHeaderText("Boost Software License  -  Version 1.0  -  August 17th, 2003");    //correct license? i mean 2003; really?
            alert.setContentText(license_text);

            alert.showAndWait();
        };

        //on click show licensing information
        t3.setOnMouseClicked(license_window);

        root.getChildren().add(t1);
        root.getChildren().add(t2);
        root.getChildren().add(t3);

        root.setAlignment(Pos.CENTER);

        Database_GUI.close_window_on_esc(scene, stage);

        stage.setTitle("About");
        stage.setScene(scene);
        stage.setResizable(false);      //prevents user from changing window size
        stage.initOwner(MainWindow.stage);
        stage.initModality(Modality.WINDOW_MODAL);      //keep window focused
        stage.show();
    }

    static void getting_started_window(Stage stage) {
        //what is an entity-type
        //how does one create an entity-type
        //what is an entity
        //how does one create an entity
        //what is a relationship
        //create a relationship
        //update
        //delete
        //show all
    }
}
