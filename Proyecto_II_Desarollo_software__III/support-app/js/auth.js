
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
                    if (data.id && (data.role === "SUPPORTER" || data.role === "SUPERVISOR" || data.role === "TECHNICIAN")) {
                        sessionStorage.setItem("usuario", JSON.stringify(data));
                        window.location.href = "dashboard.html";
                    } else if (data.id && data.role === "CLIENT") {
                        mensajeError.textContent = "No tenés permisos para acceder al panel de soporte.";
                        mensajeError.classList.remove("oculto");
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