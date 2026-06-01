const API_URL = "http://localhost:8080/api";

const Api = {
    login: function(email, password) {
        return fetch(`${API_URL}/login`, {
            method: "POST",
            headers: { "Content-Type": "application/json" },
            body: JSON.stringify({ email, password })
        }).then(res => res.json());
    },

    obtenerTodosTickets: function() {
        return fetch(`${API_URL}/tickets`)
            .then(res => res.json());
    },

    obtenerTicketPorId: function(id) {
        return fetch(`${API_URL}/tickets/${id}`)
            .then(res => res.json());
    },

    asignarTecnico: function(ticketId, tecnicoId) {
        return fetch(`${API_URL}/tickets/${ticketId}/assign/${tecnicoId}`, {
            method: "PUT",
            headers: { "Content-Type": "application/json" }
        }).then(res => res.json());
    },

    actualizarEstado: function(ticketId, status) {
        return fetch(`${API_URL}/tickets/${ticketId}/status?status=${status}`, {
            method: "PATCH",
            headers: { "Content-Type": "application/json" }
        }).then(res => res.json());
    },

    obtenerUsuarios: function() {
        return fetch(`${API_URL}/users`)
            .then(res => res.json());
    },

    logout: function() {
        return fetch(`${API_URL}/logout`, {
            method: "POST",
            headers: { "Content-Type": "application/json" }
        }).then(res => res.json());
    }
};