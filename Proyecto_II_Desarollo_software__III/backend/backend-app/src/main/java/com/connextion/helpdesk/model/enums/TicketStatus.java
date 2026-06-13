package com.connextion.helpdesk.model.enums;

import com.fasterxml.jackson.annotation.JsonCreator;

public enum TicketStatus {
    ABIERTO, ASIGNADO, EN_PROCESO, RESUELTO, CERRADO;

    @JsonCreator
    public static TicketStatus fromString(String value) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException("El estado del ticket no puede estar vacío.");
        }

        String v = value.trim().toUpperCase().replace(" ", "_");

        return switch (v) {
            case "OPEN" -> ABIERTO;
            case "ASSIGNED" -> ASIGNADO;
            case "IN_PROGRESS", "IN_PROGRESO", "EN_PROGRESO" -> EN_PROCESO;
            case "RESOLVED" -> RESUELTO;
            case "CLOSED" -> CERRADO;
            default -> TicketStatus.valueOf(v);
        };
    }
}