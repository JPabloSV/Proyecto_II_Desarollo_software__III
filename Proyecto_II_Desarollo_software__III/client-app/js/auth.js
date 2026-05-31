const API_URL = "http://localhost:8080/api";

document.getElementById("form-login").addEventListener("submit", function(e) {
    e.preventDefault();
    
    const email = document.getElementById("email").value;
    const password = document.getElementById("password").value;
    const mensajeError = document.getElementById("mensaje-error");
    
    mensajeError.classList.add("oculto");
    
    fetch(`${API_URL}/users/login`, {
        method: "POST",
        headers: {
            "Content-Type": "application/json"
        },
        body: JSON.stringify({ email, password })
    })
    .then(response => response.json())
    .then(data => {
        if (data.id) {
            sessionStorage.setItem("usuario", JSON.stringify(data));
            window.location.href = "dashboard.html";
        } else {
            mensajeError.textContent = data.message || "Credenciales incorrectas.";
            mensajeError.classList.remove("oculto");
        }
    })
    .catch(error => {
        mensajeError.textContent = "No se pudo conectar con el servidor.";
        mensajeError.classList.remove("oculto");
    });
});