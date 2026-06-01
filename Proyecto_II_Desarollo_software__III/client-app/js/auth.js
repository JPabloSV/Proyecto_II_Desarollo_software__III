

const loginForm = document.getElementById("form-login");
if (loginForm) {
    loginForm.addEventListener("submit", function (e) {
        e.preventDefault();

        const email = document.getElementById("email").value;
        const password = document.getElementById("password").value;
        const mensajeError = document.getElementById("mensaje-error");

        mensajeError.classList.add("oculto");

        fetch(`${API_URL}/login`, {
            method: "POST",
            headers: {"Content-Type": "application/json"},
            body: JSON.stringify({email, password})
        })
                .then(res => res.json())
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
        fetch(`${API_URL}/users/register`, {
            method: "POST",
            headers: {"Content-Type": "application/json"},
            body: JSON.stringify(usuario)
        })
                .then(res => res.json())
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