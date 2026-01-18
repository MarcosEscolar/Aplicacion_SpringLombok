package model;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Tarea {
    private Long id;
    private Empleado empleado;
    private Proyecto proyecto;
    private LocalDate fecha;
    private int horasTrabajadas;
    private String descripcion;
}
