package console;

import model.Empleado;
import model.Proyecto;
import model.Tarea;
import service.GestorHorasService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import jakarta.annotation.PostConstruct;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

@Component
public class MenuConsole {

    private final GestorHorasService gestorHorasService;
    private final Scanner scanner;
    private final DateTimeFormatter dateFormatter;

    @Autowired
    public MenuConsole(GestorHorasService gestorHorasService) {
        this.gestorHorasService = gestorHorasService;
        this.scanner = new Scanner(System.in);
        this.dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    }

    @PostConstruct
    public void iniciar() {
        mostrarBienvenida();

        int opcion;
        do {
            mostrarMenuPrincipal();
            opcion = leerOpcion();

            switch (opcion) {
                case 1 -> registrarTarea();
                case 2 -> consultarHorasTrabajador();
                case 3 -> consultarHorasProyecto();
                case 4 -> mostrarResumenCompleto();
                case 0 -> System.out.println("\n¡Hasta luego!");
                default -> System.out.println("\nOpción no válida.");
            }

            if (opcion != 0) {
                System.out.println("\nPresione Enter para continuar...");
                scanner.nextLine();
            }

        } while (opcion != 0);

        scanner.close();
        System.exit(0);
    }

    private void mostrarBienvenida() {
        System.out.println("----- GESTIÓN DE HORAS TRABAJADAS -----");
    }

    private void mostrarMenuPrincipal() {
        System.out.println("\n--MENÚ PRINCIPAL--");
        System.out.println("1. Registrar horas trabajadas");
        System.out.println("2. Consultar horas por trabajador");
        System.out.println("3. Consultar horas por proyecto");
        System.out.println("4. Mostrar resumen completo");
        System.out.println("0. Salir");
        System.out.println(" ");
        System.out.print("Seleccione: ");
    }

    private void registrarTarea() {
        System.out.println("\n--- REGISTRAR HORAS ---");

        System.out.println("\n--- TRABAJADORES ---");
        List<Empleado> empleados = gestorHorasService.obtenerTodosEmpleados();
        for (Empleado emp : empleados) {
            System.out.println(emp.getId() + ". " + emp.getNombre() + " " + emp.getApellido());
        }

        System.out.print("\nSeleccione ID del trabajador: ");
        Long empleadoId = leerLong();
        Empleado empleado = gestorHorasService.obtenerEmpleadoPorId(empleadoId);
        if (empleado == null) {
            System.out.println("Trabajador no encontrado.");
            return;
        }

        System.out.println("\n--- PROYECTOS ---");
        List<Proyecto> proyectos = gestorHorasService.obtenerTodosProyectos();
        for (Proyecto proy : proyectos) {
            System.out.println(proy.getId() + ". " + proy.getNombre() + " (" + proy.getCliente() + ")");
        }

        System.out.print("\nSeleccione ID del proyecto: ");
        Long proyectoId = leerLong();
        Proyecto proyecto = gestorHorasService.obtenerProyectoPorId(proyectoId);
        if (proyecto == null) {
            System.out.println("Proyecto no encontrado.");
            return;
        }

        System.out.print("Fecha (dd/MM/yyyy) [hoy]: ");
        String fechaStr = scanner.nextLine();
        LocalDate fecha;

        if (fechaStr.isEmpty()) {
            fecha = LocalDate.now();
        } else {
            try {
                fecha = LocalDate.parse(fechaStr, dateFormatter);
            } catch (DateTimeParseException e) {
                System.out.println("Fecha inválida. Usando hoy.");
                fecha = LocalDate.now();
            }
        }

        System.out.print("Horas trabajadas: ");
        int horas = leerInt();
        if (horas <= 0) {
            System.out.println("Las horas deben ser > 0.");
            return;
        }

        System.out.print("Descripción de la tarea: ");
        String descripcion = scanner.nextLine();

        Tarea tarea = new Tarea(null, empleado, proyecto, fecha, horas, descripcion);
        gestorHorasService.registrarTarea(tarea);

        System.out.println("\nHoras registradas correctamente!");
        System.out.println("Trabajador: " + empleado.getNombre());
        System.out.println("Proyecto: " + proyecto.getNombre());
        System.out.println("Horas: " + horas);
    }

    private void consultarHorasTrabajador() {
        System.out.println("\n--- HORAS POR TRABAJADOR ---");

        List<Empleado> empleados = gestorHorasService.obtenerTodosEmpleados();
        for (Empleado emp : empleados) {
            System.out.println(emp.getId() + ". " + emp.getNombre() + " " + emp.getApellido());
        }

        System.out.print("\nSeleccione ID del trabajador: ");
        Long empleadoId = leerLong();
        Empleado empleado = gestorHorasService.obtenerEmpleadoPorId(empleadoId);

        if (empleado == null) {
            System.out.println("Trabajador no encontrado.");
            return;
        }

        int totalHoras = gestorHorasService.obtenerHorasPorEmpleado(empleadoId);

        System.out.println("\n--- RESUMEN ---");
        System.out.println("Trabajador: " + empleado.getNombre() + " " + empleado.getApellido());
        System.out.println("Puesto: " + empleado.getPuesto());
        System.out.println("Total horas trabajadas: " + totalHoras + " horas");

        List<Tarea> tareas = gestorHorasService.obtenerTareasPorEmpleado(empleadoId);
        if (!tareas.isEmpty()) {
            System.out.println("\n--- Detalle por proyecto ---");
            for (Tarea tarea : tareas) {
                System.out.println("- " + tarea.getProyecto().getNombre() +
                        ": " + tarea.getHorasTrabajadas() + " horas (" +
                        tarea.getFecha().format(dateFormatter) + ")");
            }
        }
    }

    private void consultarHorasProyecto() {
        System.out.println("\n--- HORAS POR PROYECTO ---");

        List<Proyecto> proyectos = gestorHorasService.obtenerTodosProyectos();
        for (Proyecto proy : proyectos) {
            System.out.println(proy.getId() + ". " + proy.getNombre());
        }

        System.out.print("\nSeleccione ID del proyecto: ");
        Long proyectoId = leerLong();
        Proyecto proyecto = gestorHorasService.obtenerProyectoPorId(proyectoId);

        if (proyecto == null) {
            System.out.println("Proyecto no encontrado.");
            return;
        }

        int totalHoras = gestorHorasService.obtenerHorasPorProyecto(proyectoId);

        System.out.println("\n--- RESUMEN ---");
        System.out.println("Proyecto: " + proyecto.getNombre());
        System.out.println("Cliente: " + proyecto.getCliente());
        System.out.println("Total horas trabajadas: " + totalHoras + " horas");
    }

    private void mostrarResumenCompleto() {
        System.out.println("\n--- RESUMEN COMPLETO ---");

        System.out.println("\n--- HORAS POR TRABAJADOR ---");
        Map<Empleado, Integer> resumenEmpleados = gestorHorasService.obtenerResumenHorasPorEmpleado();

        if (resumenEmpleados.isEmpty()) {
            System.out.println("No hay horas registradas.");
            return;
        }

        for (Map.Entry<Empleado, Integer> entry : resumenEmpleados.entrySet()) {
            Empleado emp = entry.getKey();
            int horas = entry.getValue();
            System.out.println("- " + emp.getNombre() + " " + emp.getApellido() +
                    ": " + horas + " horas");
        }

        System.out.println("\n--- HORAS POR PROYECTO ---");
        Map<Proyecto, Integer> resumenProyectos = gestorHorasService.obtenerResumenHorasPorProyecto();

        for (Map.Entry<Proyecto, Integer> entry : resumenProyectos.entrySet()) {
            Proyecto proy = entry.getKey();
            int horas = entry.getValue();
            System.out.println("- " + proy.getNombre() +
                    " (" + proy.getCliente() + "): " + horas + " horas");
        }
    }

    private int leerOpcion() {
        try {
            return Integer.parseInt(scanner.nextLine());
        } catch (NumberFormatException e) {
            return -1;
        }
    }

    private Long leerLong() {
        try {
            return Long.parseLong(scanner.nextLine());
        } catch (NumberFormatException e) {
            return -1L;
        }
    }

    private int leerInt() {
        try {
            return Integer.parseInt(scanner.nextLine());
        } catch (NumberFormatException e) {
            return -1;
        }
    }
}