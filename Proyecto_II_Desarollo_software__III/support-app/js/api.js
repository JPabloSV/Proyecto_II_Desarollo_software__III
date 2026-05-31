const API_URL = "http://localhost:8080/api";

const Api = {
    login: function(email, password) {
        return fetch(`${API_URL}/users/login`, {
            method: "POST",
            headers: { "Content-Type": "application/json" },
            body: JSON.stringify({ email, password })
        }).then(res => res.json());
    },

    obtenerTodasSolicitudes: function() {
        return fetch(`${API_URL}/support/issues`)
            .then(res => res.json());
    },

    obtenerSolicitudPorId: function(id) {
        return fetch(`${API_URL}/support/issues/${id}`)
            .then(res => res.json());
    },

    asignarSoportista: function(issueId, supporterId) {
        return fetch(`${API_URL}/issues/${issueId}/assign/${supporterId}`, {
            method: "PUT",
            headers: { "Content-Type": "application/json" }
        }).then(res => res.json());
    },

    actualizarEstado: function(issueId, status) {
        return fetch(`${API_URL}/issues/${issueId}/status?status=${status}`, {
            method: "PATCH",
            headers: { "Content-Type": "application/json" }
        }).then(res => res.json());
    },

    agregarNota: function(issueId, nota) {
        return fetch(`${API_URL}/issues/${issueId}/notes`, {
            method: "POST",
            headers: { "Content-Type": "application/json" },
            body: JSON.stringify(nota)
        }).then(res => res.json());
    },

    agregarComentario: function(issueId, comentario) {
        return fetch(`${API_URL}/issues/${issueId}/comments`, {
            method: "POST",
            headers: { "Content-Type": "application/json" },
            body: JSON.stringify(comentario)
        }).then(res => res.json());
    },

    obtenerSoportistas: function() {
        return fetch(`${API_URL}/users?role=SUPPORTER`)
            .then(res => res.json());
    }
};