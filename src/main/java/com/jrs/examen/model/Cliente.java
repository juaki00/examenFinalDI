package com.jrs.examen.model;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Alumno {
    private String nombre ;
    private String correo;
    private String dni;
    private String telefono;
    private LocalDate fechaNacimiento;
    private String localidad;
    private Double AD;
    private Double SGE;
    private Double DI;
    private Double PMDM;
    private Double PSP;
    private Double EIE;
    private Double HLC;


    public Double notaMedia(){
        return AD+SGE+DI+PMDM+PSP+EIE+HLC / 7;
    }

    public Integer modulosSuspensos(){
        Integer suspensas = 0;
        if(AD<5) suspensas++;
        if(SGE<5) suspensas++;
        if(DI<5) suspensas++;
        if(PMDM<5) suspensas++;
        if(PSP<5) suspensas++;
        if(EIE<5) suspensas++;
        if(HLC<5) suspensas++;
        return suspensas;
    }
}
