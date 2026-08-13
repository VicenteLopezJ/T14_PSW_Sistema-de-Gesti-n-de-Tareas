package com.t14.tasks;

import java.util.Scanner;

public class AppInteractiva {

    public static void main(String[] args) {
        GestorTareas gestor = new GestorTareas();
        Scanner scanner = new Scanner(System.in);

        // Precargar tareas iniciales
        gestor.registrarTarea("Disenar base de datos", "Modelo ER", 1);
        gestor.registrarTarea("Implementar pruebas unitarias", "Pruebas AAA", 2);
        gestor.registrarTarea("Crear interfaz", "Consola app", 3);

        System.out.println("=========================================");
        System.out.println("  GESTOR DE TAREAS INTERACTIVO DE CONSOLA");
        System.out.println("=========================================");
        
        while (true) {
            System.out.println("\n--- LISTA ACTUAL DE TAREAS ---");
            for (Tarea t : gestor.obtenerTodas()) {
                System.out.printf("[ID: %s] | Estado: %-11s | Titulo: %s%n", 
                    t.getId(), t.getEstado(), t.getTitulo());
            }

            System.out.print("\n-> Ingresa el ID de la tarea que deseas modificar (o 'q' para salir): ");
            String input = scanner.nextLine();
            
            if (input.equalsIgnoreCase("q")) {
                System.out.println("Saliendo...");
                break;
            }

            try {
                Tarea tarea = gestor.buscar(input).orElseThrow(() -> new GestorTareas.TareaNoEncontradaException(input));
                
                System.out.println("\nSelecciona el nuevo estado para la tarea #" + input + ":");
                System.out.println(" 1. PENDIENTE");
                System.out.println(" 2. EN_PROGRESO");
                System.out.println(" 3. COMPLETADA");
                System.out.println(" 4. CANCELADA");
                System.out.print("Opcion (1-4): ");
                
                String opcionStr = scanner.nextLine();
                int opcion = Integer.parseInt(opcionStr);
                
                Tarea.Estado nuevoEstado = switch (opcion) {
                    case 1 -> Tarea.Estado.PENDIENTE;
                    case 2 -> Tarea.Estado.EN_PROGRESO;
                    case 3 -> Tarea.Estado.COMPLETADA;
                    case 4 -> Tarea.Estado.CANCELADA;
                    default -> throw new IllegalArgumentException("Opcion no valida");
                };

                gestor.cambiarEstado(tarea.getId(), nuevoEstado);
                System.out.println("\n-----------------------------------------");
                System.out.println("[EXITO] Se cambio el estado correctamente.");
                
            } catch (GestorTareas.TareaNoEncontradaException e) {
                System.out.println("\n[ERROR] Tarea no encontrada.");
            } catch (GestorTareas.TransicionInvalidaException e) {
                System.out.println("\n[ERROR] " + e.getMessage());
            } catch (Exception e) {
                System.out.println("\n[ERROR] Entrada invalida.");
            }
            
            System.out.println("Total de tareas pendientes actualmente: " + gestor.obtenerPendientes().size());
            System.out.println("-----------------------------------------");
        }
        
        scanner.close();
    }
}
