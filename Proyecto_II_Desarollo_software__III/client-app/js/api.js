const API_URL = "http://localhost:8080/api";

const Api = {
    login: function(email, password) {
        return fetch(`${API_URL}/login`, {
            method: "POST",
            headers: { "Content-Type": "application/json" },
            body: JSON.stringify({ email, password })
        }).then(res => res.json());
    },

    registrar: function(usuario) {
        return fetch(`${API_URL}/users/register`, {
            method: "POST",
            headers: { "Content-Type": "application/json" },
            body: JSON.stringify(usuario)
        }).then(res => res.json());
    },

    obtenerServicios: function() {
        return fetch(`${API_URL}/services`)
            .then(res => res.json());
    },

    obtenerTickets: function(clienteId) {
        return fetch(`${API_URL}/tickets`)
            .then(res => res.json())
            .then(tickets => tickets.filter(t => t.client && t.client.id === clienteId));
    },

    crearTicket: function(ticket) {
        return fetch(`${API_URL}/tickets`, {
            method: "POST",
            headers: { "Content-Type": "application/json" },
            body: JSON.stringify(ticket)
        }).then(res => res.json());
    },

    logout: function() {
        return fetch(`${API_URL}/logout`, {
            method: "POST",
            headers: { "Content-Type": "application/json" }
        }).then(res => res.json());
    }
};