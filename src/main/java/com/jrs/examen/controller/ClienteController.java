package com.jrs.examen.controller;

import com.jrs.examen.Sesion;
import com.jrs.examen.db.ConnectionProvider;
import com.jrs.examen.model.Alumno;
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
import java.util.HashMap;
import java.util.ResourceBundle;

public class AlumnosController implements Initializable {

    @FXML
    private TableColumn<Alumno,String> colFecha;
    @FXML
    private TableColumn<Alumno,String> colDni;
    @FXML
    private TableColumn<Alumno,String> colHLC;
    @FXML
    private TableColumn<Alumno,String> colDI;
    @FXML
    private TableColumn<Alumno,String> coltelefono;
    @FXML
    private TableColumn<Alumno,String> colAD;
    @FXML
    private Label log;
    @FXML
    private TableColumn<Alumno,String> colNombre;
    @FXML
    private TableColumn<Alumno,String> colPMDM;
    @FXML
    private TableColumn<Alumno,String> colSGE;
    @FXML
    private TableColumn<Alumno,String> colPSP;
    @FXML
    private TableColumn<Alumno,String> colLocalidad;
    @FXML
    private TableColumn<Alumno,String> colCorreo;
    @FXML
    private TableColumn<Alumno,String> colEIE;
    @FXML
    private TextField txtSGE;
    @FXML
    private TextField txtLocalidad;
    @FXML
    private TextField txtAD;
    @FXML
    private TextField txtDNI;
    @FXML
    private TextField txtHLC;
    @FXML
    private TextField txtNombre;
    @FXML
    private TextField txtPMDP;
    @FXML
    private TextField txtCorreo;
    @FXML
    private TextField txtDI;
    @FXML
    private TextField txtEIE;
    @FXML
    private TextField txttlf;
    @FXML
    private TextField txtPSP;
    @FXML
    private DatePicker dateFecha;
    @FXML
    private TableView<Alumno> tablaAlumnos;

    @Override
    public void initialize( URL url , ResourceBundle resourceBundle ) {

        //tabla
        colFecha.setCellValueFactory(new PropertyValueFactory<>("fechaNacimiento"));
        colDni.setCellValueFactory(new PropertyValueFactory<>("dni"));
        colHLC.setCellValueFactory(new PropertyValueFactory<>("HLC"));
        colDI.setCellValueFactory(new PropertyValueFactory<>("DI"));
        coltelefono.setCellValueFactory(new PropertyValueFactory<>("telefono"));
        colAD.setCellValueFactory(new PropertyValueFactory<>("AD"));
        colNombre.setCellValueFactory(new PropertyValueFactory<>("nombre"));
        colPMDM.setCellValueFactory(new PropertyValueFactory<>("PMDM"));
        colSGE.setCellValueFactory(new PropertyValueFactory<>("SGE"));
        colPSP.setCellValueFactory(new PropertyValueFactory<>("PSP"));
        colLocalidad.setCellValueFactory(new PropertyValueFactory<>("localidad"));
        colCorreo.setCellValueFactory(new PropertyValueFactory<>("correo"));
        colEIE.setCellValueFactory(new PropertyValueFactory<>("EIE"));

        //observable de la tabla
        tablaAlumnos.getSelectionModel( ).selectedItemProperty( ).addListener( ( observableValue , pedido , t1 ) -> {
            Sesion.setAlumnoPulsado( t1 );
        } );
    }


    @Deprecated
    public void onHelloButtonClick( ActionEvent actionEvent ){
        //Obtener Conexion del provider
        Connection c = null;
        try {
            c = ConnectionProvider.getConnection();
        } catch ( SQLException e ) {
            throw new RuntimeException( e );
        }

        HashMap hm = new HashMap<>();
       // hm.put( "annio", 1997 ); //annio => parametro agregado anteriormente en JasperSoft
        JasperPrint jasperPrint = null;
        try {
            jasperPrint = JasperFillManager.fillReport( "examen.jasper", hm, c);
        } catch ( JRException e ) {
            throw new RuntimeException( e );
        }

        JRViewer viewer = new JRViewer(jasperPrint);

        JFrame frame = new JFrame("Listado de Juegos");
        frame.getContentPane().add(viewer);
        frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
        frame.setVisible(true);

        System.out.print("Done!");


        /*  Exportar a PDF    */
        JRPdfExporter exp = new JRPdfExporter();
        exp.setExporterInput(new SimpleExporterInput( jasperPrint));
        exp.setExporterOutput(new SimpleOutputStreamExporterOutput( "juegos.pdf"));
        exp.setConfiguration(new SimplePdfExporterConfiguration());
        try {
            exp.exportReport();
        } catch ( JRException e ) {
            throw new RuntimeException( e );
        }

        System.out.print("Done!");

    }

    @FXML
    public void insertar( ActionEvent actionEvent ) {

        //Comprobar si algun campo esta vacio
        if(txtNombre.getText().isEmpty()){
            var alert = new Alert(Alert.AlertType.WARNING);
            alert.setHeaderText("Campos Obligatorios");
            alert.setContentText("Todos los campos son obligatorios");
            alert.showAndWait();
        }
        else {
            try { //Comprobar el formato y rango de las notas
                if (
                        Double.parseDouble( txtAD.getText( ) ) < 0 || Double.parseDouble( txtAD.getText( ) ) > 10 ||
                        Double.parseDouble( txtSGE.getText( ) ) < 0 || Double.parseDouble( txtSGE.getText( ) ) > 10 ||
                        Double.parseDouble( txtDI.getText( ) ) < 0 || Double.parseDouble( txtDI.getText( ) ) > 10 ||
                        Double.parseDouble( txtPMDP.getText( ) ) < 0 || Double.parseDouble( txtPMDP.getText( ) ) > 10 ||
                        Double.parseDouble( txtPSP.getText( ) ) < 0 || Double.parseDouble( txtPSP.getText( ) ) > 10 ||
                        Double.parseDouble( txtEIE.getText( ) ) < 0 || Double.parseDouble( txtEIE.getText( ) ) > 10 ||
                        Double.parseDouble( txtHLC.getText( ) ) < 0 || Double.parseDouble( txtHLC.getText( ) ) > 10
                ){
                    var alert = new Alert(Alert.AlertType.WARNING);
                    alert.setHeaderText("Nota invalida");
                    alert.setContentText("Las notas deben estar comprendidas entre 0 y 10");
                    alert.show();
                }else{
                    //Añadir alumno
                    Alumno alumno = new Alumno( );
                    alumno.setNombre( txtNombre.getText( ) );
                    alumno.setCorreo( txtCorreo.getText( ) );
                    alumno.setDni( txtDNI.getText( ) );
                    alumno.setTelefono( txttlf.getText( ) );
                    alumno.setFechaNacimiento( dateFecha.getValue( ) );
                    alumno.setLocalidad( txtLocalidad.getText( ) );
                    alumno.setAD(  Double.parseDouble( txtAD.getText( ) ) );
                    alumno.setSGE( Double.parseDouble( txtSGE.getText( ) ) );
                    alumno.setDI( Double.parseDouble( txtDI.getText( ) ) );
                    alumno.setPMDM( Double.parseDouble( txtPMDP.getText( ) ) );
                    alumno.setPSP( Double.parseDouble( txtPSP.getText( ) ) );
                    alumno.setEIE( Double.parseDouble( txtEIE.getText( ) ) );
                    alumno.setHLC( Double.parseDouble( txtHLC.getText( ) ) );
                    tablaAlumnos.getItems( ).add( alumno );
                    log.setText( "Alumno insertado" );
                }
            }
            catch ( NumberFormatException e ){
                var alert = new Alert(Alert.AlertType.WARNING);
                alert.setHeaderText("Nota invalida");
                alert.setContentText("Las notas deben ser numeros");
                alert.show();
            }

        }
    }

    @FXML
    public void generarInforme( ActionEvent actionEvent ) {


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
}