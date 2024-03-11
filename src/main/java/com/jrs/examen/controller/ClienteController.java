package com.jrs.examen.controller;

import com.jrs.examen.Sesion;
import com.jrs.examen.db.ConnectionProvider;
import com.jrs.examen.model.Cliente;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.export.JRPdfExporter;
import net.sf.jasperreports.export.SimpleExporterInput;
import net.sf.jasperreports.export.SimpleOutputStreamExporterOutput;
import net.sf.jasperreports.export.SimplePdfExporterConfiguration;
import net.sf.jasperreports.swing.JRViewer;

import javax.swing.*;
import java.net.URL;
import java.sql.Connection;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.ResourceBundle;

public class ClienteController implements Initializable {

    @FXML
    private Label log;
    @FXML
    private TextField txtNombre;
    @FXML
    private TextField txtTalla;
    @FXML
    private TextField txtEdad;
    @FXML
    private TextArea txtObservaciones;
    @FXML
    private TextField txtPeso;
    @FXML
    private ComboBox<String> comboSexo;
    @FXML
    private ComboBox<String> comboTipo;

    @Override
    public void initialize( URL url , ResourceBundle resourceBundle ) {
        ObservableList<String> datos = FXCollections.observableArrayList();
        ObservableList<String> datos2 = FXCollections.observableArrayList();

        datos.addAll("Varon","Mujer","Otro");
        comboSexo.setItems(datos);
        comboSexo.getSelectionModel().selectFirst();

        datos2.addAll("Sedentario","Moderado","Activo", "Muy activo");
        comboTipo.setItems(datos2);
        comboTipo.getSelectionModel().selectFirst();

    }



    @FXML
    public void descargarPDF( ActionEvent actionEvent ) {


        //Obtener Conexion del provider
        Connection c = null;
        try {
            c = ConnectionProvider.getConnection( );
        } catch ( SQLException e ) {
            throw new RuntimeException( e );
        }

        HashMap hm = new HashMap<>( );
        // hm.put( "annio", 1997 ); //parametro
        JasperPrint jasperPrint = null;
        try {
            jasperPrint = JasperFillManager.fillReport( "examen.jasper" , hm , c );
        } catch ( JRException e ) {
            throw new RuntimeException( e );
        }

        JRViewer viewer = new JRViewer( jasperPrint );

        JFrame frame = new JFrame( "Listado de Juegos" );
        frame.getContentPane( ).add( viewer );
        frame.setExtendedState( JFrame.MAXIMIZED_BOTH );
        frame.setVisible( true );

        System.out.print( "Done!" );


        /*  Exportar a PDF    */
        JRPdfExporter exp = new JRPdfExporter( );
        exp.setExporterInput( new SimpleExporterInput( jasperPrint ) );
        exp.setExporterOutput( new SimpleOutputStreamExporterOutput( "alumnos.pdf" ) );
        exp.setConfiguration( new SimplePdfExporterConfiguration( ) );
        try {
            exp.exportReport( );
        } catch ( JRException e ) {
            throw new RuntimeException( e );
        }

        System.out.print( "Done!" );

    }

    @FXML
    public void guardar( ActionEvent actionEvent ) {
        //cpmprobar campos vacios
        if( txtNombre.getText().isEmpty() || txtPeso.getText().isEmpty() ||
                txtEdad.getText().isEmpty() || txtObservaciones.getText().isEmpty() ){

            log.setText( "No puede haber ningun campo vacío!" );
        }
        //comprobar formatos
        try{
            Double metabolismo = 0.0;
            Double factorActividad = 0.0;
            if(comboSexo.getSelectionModel().getSelectedItem().equals( "varon" )) {
                metabolismo = 66.473 + 13.751 * Double.parseDouble( txtPeso.getText( ) ) + 5.0033 * Double.parseDouble( txtTalla.getText( ) ) - 6.755 * Double.parseDouble( txtEdad.getText( ) );
                switch (comboTipo.getSelectionModel().getSelectedItem().toLowerCase(  )){
                    case "sedentario": factorActividad = 1.3; break;
                    case "moderado": factorActividad= 1.6;  break;
                    case "activo": factorActividad= 1.7; break;
                    case "muy activo": factorActividad= 2.1; break;
                }
            }
            else{
                metabolismo = 655.0955 + 9.463 * Double.parseDouble( txtPeso.getText( ) ) + 1.8496 * Double.parseDouble( txtTalla.getText( ) ) - 4.6756 * Double.parseDouble( txtEdad.getText( ) );
                switch (comboTipo.getSelectionModel().getSelectedItem().toLowerCase(  )){
                    case "sedentario": factorActividad = 1.3; break;
                    case "moderado": factorActividad= 1.5;  break;
                    case "activo": factorActividad= 1.6; break;
                    case "muy activo": factorActividad= 1.9; break;
                }
            }


            DecimalFormat df = new DecimalFormat( "#.0");


            log.setText( "El cliente " + txtNombre.getText() + " tiene un GER de "+df.format( metabolismo ) + " y un GET de  " + df.format( metabolismo*factorActividad ) );
        }
        catch(Exception e){
            log.setText( "Datos introducidos incorrectos!" );
        }

    }


}