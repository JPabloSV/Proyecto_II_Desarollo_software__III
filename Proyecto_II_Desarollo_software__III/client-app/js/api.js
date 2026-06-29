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

    obtenerCategorias: function() {
        return fetch(`${API_URL}/categories`)
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
    },

    // ==========================================
    // NUEVO MÉTODO: CREAR COMENTARIO (NOTA TÉCNICA)
    // ==========================================
    crearComentario: function(ticketId, userId, texto) {
        return fetch(`${API_URL}/comments/add`, {
            method: "POST",
            headers: { "Content-Type": "application/json" },
            body: JSON.stringify({
                ticketId: parseInt(ticketId),
                userId: parseInt(userId),
                text: texto
            })
        }).then(res => {
            if (!res.ok) {
                throw new Error("Error en la respuesta del servidor al guardar el comentario.");
            }
            return res.json();
        });
    }
};

// ==========================================
// CONTROLADOR DEL FORMULARIO DE LOGIN
// ==========================================
const loginForm = document.getElementById("form-login");
if (loginForm) {
    loginForm.addEventListener("submit", function (e) {
        e.preventDefault();

        const email = document.getElementById("email").value;
        const password = document.getElementById("password").value;
        const mensajeError = document.getElementById("mensaje-error");

        mensajeError.classList.add("oculto");

        Api.login(email, password)
            .then(data => {
                if (data.id) {
                    if (data.role !== "CLIENT") {
                        mensajeError.textContent = "Esta página es solo para clientes.";
                        mensajeError.classList.remove("oculto");
                        return;
                    }
                    sessionStorage.setItem("usuario", JSON.stringify(data));
                    window.location.href = "dashboard.html";
                } else {
                    mensajeError.textContent = data.message || "Credenciales incorrectas.";
                    mensajeError.classList.remove("oculto");
                }
            })
            .catch(() => {
                mensajeError.textContent = "No se pudo conectar con el servidor.";
                mensajeError.classList.remove("oculto");
            });
    });
}

// ==========================================
// CONTROLADOR DEL FORMULARIO DE REGISTRO
// ==========================================
const registroForm = document.getElementById("form-registro");
if (registroForm) {
    registroForm.addEventListener("submit", function (e) {
        e.preventDefault();

        const serviceIds = [];
        document.querySelectorAll(".servicio-item input:checked").forEach(cb => {
            serviceIds.push(parseInt(cb.value));
        });

        if (serviceIds.length === 0) {
            const error = document.getElementById("mensaje-error");
            error.textContent = "Debes seleccionar al menos un servicio.";
            error.classList.remove("oculto");
            return;
        }

        const usuario = {
            name: document.getElementById("nombre").value,
            firstName: document.getElementById("primerApellido").value,
            secondSurname: document.getElementById("segundoApellido").value,
            email: document.getElementById("email").value,
            password: document.getElementById("password").value,
            phone: document.getElementById("telefono").value,
            address: document.getElementById("direccion").value,
            serviceIds: serviceIds
        };

        Api.registrar(usuario)
            .then(data => {
                if (data.userId) {
                    const exito = document.getElementById("mensaje-exito");
                    exito.textContent = "Registro exitoso. Redirigiendo...";
                    exito.classList.remove("oculto");
                    setTimeout(() => {
                        window.location.href = "index.html";
                    }, 2000);
                } else {
                    const error = document.getElementById("mensaje-error");
                    error.textContent = data.message || "Error al registrar usuario.";
                    error.classList.remove("oculto");
                }
            })
            .catch(() => {
                const error = document.getElementById("mensaje-error");
                error.textContent = "No se pudo conectar con el servidor.";
                error.classList.remove("oculto");
            });
    });
}

// ==========================================
// MÓDULO DE INTERFAZ DE USUARIO (UI)
// ==========================================
const Ui = {
    verificarSesion: function() {
        const usuario = sessionStorage.getItem("usuario");
        if (!usuario) {
            window.location.href = "index.html";
            return null;
        }
        return JSON.parse(usuario);
    },

    cerrarSesion: function() {
        sessionStorage.removeItem("usuario");
        window.location.href = "index.html";
    },

    mostrarNombreUsuario: function(usuario) {
        const elemento = document.getElementById("nombre-usuario");
        if (elemento) {
            elemento.textContent = usuario.name;
        }
    },

    mostrarError: function(elementoId, mensaje) {
        const elemento = document.getElementById(elementoId);
        if (elemento) {
            elemento.textContent = mensaje;
            elemento.classList.remove("oculto");
        }
    },

    ocultarError: function(elementoId) {
        const elemento = document.getElementById(elementoId);
        if (elemento) {
            elemento.classList.add("oculto");
        }
    }
};