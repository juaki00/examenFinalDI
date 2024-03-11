package com.jrs.examen;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class App extends Application {
    @Override
    public void start( Stage stage ) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader( App.class.getResource( "cliente-view.fxml" ) );
        Scene scene = new Scene( fxmlLoader.load( ) , 600 , 500 );
        stage.setTitle( "Alumnos" );
        stage.setMinWidth( 600 );
        stage.setMinHeight( 500 );
        stage.setScene( scene );
        stage.show( );
    }

    public static void main( String[] args ) {
        launch( );
    }
}