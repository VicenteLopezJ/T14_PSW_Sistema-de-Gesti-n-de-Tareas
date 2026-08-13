package com.t14.tasks;

import java.util.Scanner;

public class GestorTareasEspagueti {
    public static void main(String[] args) {
        // Almacenamiento usando arreglos paralelos en lugar de un Objeto "Tarea"
        String[] ids = new String[100];
        String[] titulos = new String[100];
        String[] descripciones = new String[100];
        int[] prioridades = new int[100];
        String[] estados = new String[100]; 
        
        int totalTareas = 0;
        int contadorId = 101; // ID inicial
        Scanner scanner = new Scanner(System.in);
        boolean ejecutando = true;

        // Datos de ejemplo iniciales (quemados en el código)
        ids[0] = "T-" + contadorId++; titulos[0] = "Disenar base de datos"; descripciones[0] = "Modelo ER"; prioridades[0] = 1; estados[0] = "PENDIENTE";
        ids[1] = "T-" + contadorId++; titulos[1] = "Implementar pruebas unitarias"; descripciones[1] = "Pruebas AAA"; prioridades[1] = 2; estados[1] = "PENDIENTE";
        ids[2] = "T-" + contadorId++; titulos[2] = "Crear interfaz"; descripciones[2] = "Consola app"; prioridades[2] = 3; estados[2] = "PENDIENTE";
        totalTareas = 3;

        // Bucle gigante e infinito donde ocurre absolutamente todo
        while (ejecutando) {
            System.out.println("\n=========================================");
            System.out.println("  SISTEMA ESPAGUETI DE TAREAS");
            System.out.println("=========================================");
            System.out.println("1. Registrar nueva tarea");
            System.out.println("2. Cambiar estado de tarea");
            System.out.println("3. Validar/Ver tareas pendientes");
            System.out.println("4. Salir");
            System.out.print("Seleccione una opcion: ");
            
            String opcionStr = scanner.nextLine();
            int opcion = 0;
            try { opcion = Integer.parseInt(opcionStr); } catch (Exception e) { opcion = 0; }

            if (opcion == 1) { // ── REGISTRAR TAREA ──
                System.out.print("Ingrese titulo: ");
                String tit = scanner.nextLine();
                // Validaciones anidadas mezcladas con lógica de interfaz
                if (tit == null || tit.trim().isEmpty()) {
                    System.out.println("Error: Titulo no puede ser vacio.");
                } else {
                    System.out.print("Ingrese descripcion: ");
                    String desc = scanner.nextLine();
                    System.out.print("Ingrese prioridad (1=Alta, 2=Media, 3=Baja): ");
                    int pri = 0;
                    try { pri = Integer.parseInt(scanner.nextLine()); } catch(Exception e) {}
                    
                    if (pri < 1 || pri > 3) {
                        System.out.println("Error: Prioridad debe estar entre 1 y 3.");
                    } else {
                        ids[totalTareas] = "T-" + contadorId++;
                        titulos[totalTareas] = tit;
                        descripciones[totalTareas] = desc;
                        prioridades[totalTareas] = pri;
                        estados[totalTareas] = "PENDIENTE";
                        System.out.println("Exito. Tarea registrada con ID: " + ids[totalTareas]);
                        totalTareas++;
                    }
                }
            } else if (opcion == 2) { // ── CAMBIAR ESTADO ──
                System.out.print("Ingrese ID de la tarea a modificar: ");
                String idBuscado = scanner.nextLine();
                boolean encontrado = false;
                
                // Búsqueda manual iterando arreglos paralelos
                for (int i = 0; i < totalTareas; i++) {
                    if (ids[i].equals(idBuscado)) {
                        encontrado = true;
                        System.out.println("Estado actual: " + estados[i]);
                        System.out.println("Nuevos estados: 1=EN_PROGRESO, 2=COMPLETADA, 3=CANCELADA");
                        System.out.print("Seleccione (1-3): ");
                        String opcEstado = scanner.nextLine();
                        
                        // Máquina de estados con condicionales profundamente anidados
                        if (opcEstado.equals("1")) {
                            if (estados[i].equals("PENDIENTE")) {
                                estados[i] = "EN_PROGRESO";
                                System.out.println("Actualizado a EN_PROGRESO");
                            } else { System.out.println("Error: Solo PENDIENTE puede pasar a EN_PROGRESO"); }
                        } else if (opcEstado.equals("2")) {
                            if (estados[i].equals("EN_PROGRESO")) {
                                estados[i] = "COMPLETADA";
                                System.out.println("Actualizado a COMPLETADA");
                            } else { System.out.println("Error: Solo EN_PROGRESO puede pasar a COMPLETADA"); }
                        } else if (opcEstado.equals("3")) {
                            if (!estados[i].equals("COMPLETADA")) {
                                estados[i] = "CANCELADA";
                                System.out.println("Actualizado a CANCELADA");
                            } else { System.out.println("Error: Tareas completadas no se cancelan"); }
                        } else {
                            System.out.println("Opcion no valida.");
                        }
                        break;
                    }
                }
                if (!encontrado) { System.out.println("No se encontro el ID."); }
                
            } else if (opcion == 3) { // ── VALIDAR TAREAS PENDIENTES ──
                System.out.println("\n--- TAREAS PENDIENTES ---");
                int contPendientes = 0;
                for (int i = 0; i < totalTareas; i++) {
                    if (estados[i].equals("PENDIENTE")) {
                        System.out.println("[ID: " + ids[i] + "] | Prioridad: " + prioridades[i] + " | Titulo: " + titulos[i]);
                        contPendientes++;
                    }
                }
                if (contPendientes == 0) { System.out.println("No hay tareas pendientes."); }
                
            } else if (opcion == 4) { // ── SALIR ──
                ejecutando = false;
                System.out.println("Saliendo...");
            } else {
                System.out.println("Opcion incorrecta.");
            }
        }
        scanner.close();
    }
}
