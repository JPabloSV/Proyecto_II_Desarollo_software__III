const API_URL = "http://localhost:8080/api";

const Api = {
    login: function(email, password) {
        return fetch(`${API_URL}/users/login`, {
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

    obtenerSolicitudes: function(clienteId) {
        return fetch(`${API_URL}/issues?clientId=${clienteId}`)
            .then(res => res.json());
    },

    crearSolicitud: function(solicitud) {
        return fetch(`${API_URL}/issues`, {
            method: "POST",
            headers: { "Content-Type": "application/json" },
            body: JSON.stringify(solicitud)
        }).then(res => res.json());
    },

    agregarComentario: function(issueId, comentario) {
        return fetch(`${API_URL}/issues/${issueId}/comments`, {
            method: "POST",
            headers: { "Content-Type": "application/json" },
            body: JSON.stringify(comentario)
        }).then(res => res.json());
    }
};