package service;

import model.Empleado;
import model.Proyecto;
import model.Tarea;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class GestorHorasService {

    // Colecciones requeridas por el enunciado
    private final Map<Long, Empleado> empleados = new HashMap<>();
    private final Map<Long, Proyecto> proyectos = new HashMap<>();
    private final List<Tarea> tareas = new ArrayList<>();

    private Long empleadoIdCounter = 1L;
    private Long proyectoIdCounter = 1L;
    private Long tareaIdCounter = 1L;

    public GestorHorasService() {
        inicializarDatos();
    }

    private void inicializarDatos() {
        // 5 empleados como pide el enunciado
        empleados.put(empleadoIdCounter, new Empleado(empleadoIdCounter++, "Juan", "Pérez", "Desarrollador", "TI"));
        empleados.put(empleadoIdCounter, new Empleado(empleadoIdCounter++, "María", "Gómez", "Analista", "Sistemas"));
        empleados.put(empleadoIdCounter, new Empleado(empleadoIdCounter++, "Carlos", "Rodríguez", "Jefe Proyecto", "Gestión"));
        empleados.put(empleadoIdCounter, new Empleado(empleadoIdCounter++, "Ana", "López", "Diseñadora", "UX"));
        empleados.put(empleadoIdCounter, new Empleado(empleadoIdCounter++, "Luis", "Martínez", "Tester", "Calidad"));

        // 10 proyectos como pide el enunciado
        proyectos.put(proyectoIdCounter, new Proyecto(proyectoIdCounter++, "Portal Web", "Portal corporativo", "Empresa A", true));
        proyectos.put(proyectoIdCounter, new Proyecto(proyectoIdCounter++, "App Móvil", "App de ventas", "Empresa B", true));
        proyectos.put(proyectoIdCounter, new Proyecto(proyectoIdCounter++, "Migración BD", "Migración de datos", "Interno", true));
        proyectos.put(proyectoIdCounter, new Proyecto(proyectoIdCounter++, "E-commerce", "Tienda online", "Empresa C", true));
        proyectos.put(proyectoIdCounter, new Proyecto(proyectoIdCounter++, "Sistema ERP", "Sistema integral", "Empresa D", true));
        proyectos.put(proyectoIdCounter, new Proyecto(proyectoIdCounter++, "BI Dashboard", "Panel de control", "Empresa E", true));
        proyectos.put(proyectoIdCounter, new Proyecto(proyectoIdCounter++, "Seguridad", "Auditoría seguridad", "Empresa F", true));
        proyectos.put(proyectoIdCounter, new Proyecto(proyectoIdCounter++, "Cloud", "Migración nube", "Interno", true));
        proyectos.put(proyectoIdCounter, new Proyecto(proyectoIdCounter++, "App IoT", "Aplicación IoT", "Empresa G", true));
        proyectos.put(proyectoIdCounter, new Proyecto(proyectoIdCounter++, "Rediseño UX", "Mejora experiencia", "Empresa H", true));
    }

    // Métodos básicos necesarios
    public List<Empleado> obtenerTodosEmpleados() {
        return new ArrayList<>(empleados.values());
    }

    public Empleado obtenerEmpleadoPorId(Long id) {
        return empleados.get(id);
    }

    public List<Proyecto> obtenerTodosProyectos() {
        return new ArrayList<>(proyectos.values());
    }

    public Proyecto obtenerProyectoPorId(Long id) {
        return proyectos.get(id);
    }

    public void registrarTarea(Tarea tarea) {
        tarea.setId(tareaIdCounter++);
        tareas.add(tarea);
    }

    // CONSULTAS DE HORAS (lo que pide el enunciado)
    public int obtenerHorasPorEmpleado(Long empleadoId) {
        return tareas.stream()
                .filter(t -> t.getEmpleado().getId().equals(empleadoId))
                .mapToInt(Tarea::getHorasTrabajadas)
                .sum();
    }

    public int obtenerHorasPorProyecto(Long proyectoId) {
        return tareas.stream()
                .filter(t -> t.getProyecto().getId().equals(proyectoId))
                .mapToInt(Tarea::getHorasTrabajadas)
                .sum();
    }

    public List<Tarea> obtenerTareasPorEmpleado(Long empleadoId) {
        return tareas.stream()
                .filter(t -> t.getEmpleado().getId().equals(empleadoId))
                .collect(Collectors.toList());
    }

    public Map<Empleado, Integer> obtenerResumenHorasPorEmpleado() {
        return tareas.stream()
                .collect(Collectors.groupingBy(
                        Tarea::getEmpleado,
                        Collectors.summingInt(Tarea::getHorasTrabajadas)
                ));
    }

    public Map<Proyecto, Integer> obtenerResumenHorasPorProyecto() {
        return tareas.stream()
                .collect(Collectors.groupingBy(
                        Tarea::getProyecto,
                        Collectors.summingInt(Tarea::getHorasTrabajadas)
                ));
    }
}