// Configuración de la URL base del Backend de Spring Boot
const API_URL = "http://localhost:8080/api/tickets";

// Esperar a que el DOM cargue completamente antes de ejecutar peticiones
document.addEventListener("DOMContentLoaded", () => {
    loadTickets();

    // Asignar evento al botón de actualizar
    document.getElementById("btn-refresh").addEventListener("click", loadTickets);
});

/**
 * Realiza una petición GET al backend para obtener los tickets y renderizarlos
 */
function loadTickets() {
    const tableBody = document.getElementById("tickets-body");
    tableBody.innerHTML = `<tr><td colspan="6" class="text-center">Cargando solicitudes actuales...</td></tr>`;

    fetch(API_URL)
            .then(response => {
                if (!response.ok) {
                    throw new Error(`Error en el servidor: ${response.status}`);
                }
                return response.json(); // Parsea el array JSON que envía Spring Boot
            })
            .then(tickets => {
                renderTicketsTable(tickets);
            })
            .catch(error => {
                console.error("Error al consumir la API:", error);
                tableBody.innerHTML = `
                <tr>
                    <td colspan="6" class="text-center" style="color: #DC2626; font-weight: bold;">
                        No se pudo conectar con el servidor backend. Asegúrate de tener Spring Boot encendido.
                    </td
                </tr>`;
            });
}

function renderTicketsTable(tickets) {
    const tableBody = document.getElementById("tickets-body");
    tableBody.innerHTML = "";

    if (tickets.length === 0) {
        tableBody.innerHTML = `<tr><td colspan="6" class="text-center">No hay tickets registrados en el sistema.</td></tr>`;
        return;
    }

    tickets.forEach(ticket => {
        const row = document.createElement("tr");

        const technicianName = ticket.technician ? ticket.technician.name : "<em>Sin asignar</em>";
        const clientName = ticket.client ? ticket.client.name : "Anónimo";
        const statusClass = ticket.status.toLowerCase();

        row.innerHTML = `
            <td><strong>#${ticket.id}</strong></td>
            <td>
                <strong>${ticket.title}</strong>
                <div class="ticket-desc">${ticket.description}</div>
            </td>
            <td>${ticket.priority}</td>
            <td>
                <span class="status-badge status-${statusClass}">${ticket.status}</span>
            </td>
            <td>${clientName}</td>
            <td>${technicianName}</td>
        `;

        tableBody.appendChild(row);
    });
}


document.getElementById("ticket-form").addEventListener("submit", function (event) {
    event.preventDefault();


    const newTicket = {
        title: document.getElementById("form-title").value,
        description: document.getElementById("form-description").value,
        priority: document.getElementById("form-priority").value,
        client: {id: parseInt(document.getElementById("form-client-id").value)},
        category: {id: parseInt(document.getElementById("form-category-id").value)}
    };

    // Enviar petición POST a Spring Boot
    fetch(API_URL, {
        method: "POST",
        headers: {
            "Content-Type": "application/json"
        },
        body: JSON.stringify(newTicket)
    })
            .then(response => {
                if (!response.ok) {
                    throw new Error("No se pudo guardar la solicitud.");
                }
                return response.json();
            })
            .then(data => {
                alert(`¡Éxito! Ticket #${data.id} creado correctamente.`);
                document.getElementById("ticket-form").reset();

                setTimeout(() => {
                    loadTickets();
                }, 300);
            })
            .catch(error => {
                console.error("Error al registrar ticket:", error);
                alert("Error al comunicarse con el backend de soporte.");
            });
});


function switchTab(tab) {
    const loginForm = document.getElementById("login-form");
    const registerForm = document.getElementById("register-form");
    const loginBtn = document.getElementById("btn-tab-login");
    const registerBtn = document.getElementById("btn-tab-register");

    if (tab === 'login') {
        loginForm.classList.remove("hidden");
        registerForm.classList.add("hidden");
        loginBtn.classList.add("active");
        registerBtn.classList.remove("active");
    } else {
        loginForm.classList.add("hidden");
        registerForm.classList.remove("hidden");
        loginBtn.classList.remove("active");
        registerBtn.classList.add("active");
    }
}


document.getElementById("register-form").addEventListener("submit", function (e) {
    e.preventDefault();

    const userData = {
        name: document.getElementById("reg-name").value,
        email: document.getElementById("reg-email").value,
        password: document.getElementById("reg-password").value,
        role: document.getElementById("reg-role").value
    };

    fetch(`${BASE_USERS_URL}/register`, {
        method: "POST",
        headers: {"Content-Type": "application/json"},
        body: JSON.stringify(userData)
    })
            .then(response => {
                if (!response.ok) {
                    return response.json().then(err => {
                        throw new Error(err.message);
                    });
                }
                return response.json();
            })
            .then(data => {
                alert(`¡Registro exitoso! Cuenta creada para ${data.name}. Ya puedes iniciar sesión.`);
                document.getElementById("register-form").reset();
                switchTab('login'); // Cambia visualmente al formulario de ingreso
            })
            .catch(error => {
                alert(error.message || "Error al procesar el registro en el servidor.");
            });
});

document.getElementById("login-form").addEventListener("submit", function (e) {
    e.preventDefault();

    const credentials = {
        email: document.getElementById("login-email").value,
        password: document.getElementById("login-password").value
    };

    fetch(`${BASE_USERS_URL}/login`, {
        method: "POST",
        headers: {"Content-Type": "application/json"},
        body: JSON.stringify(credentials)
    })
            .then(response => {
                if (response.status === 401) {
                    throw new Error("Credenciales inválidas. Verifique el correo o la contraseña.");
                }
                if (!response.ok) {
                    throw new Error("No se pudo conectar con el servidor de autenticación.");
                }
                return response.json();
            })
            .then(user => {
                alert(`¡Bienvenido al sistema, ${user.name}!`);

                sessionStorage.setItem("loggedUser", JSON.stringify(user));

                window.location.href = "index.html";
            })
            .catch(error => {
                alert(error.message);
            });
});

document.addEventListener("DOMContentLoaded", () => {
    console.log("Sistema de autenticación listo.");

    // Vinculamos manualmente los eventos a los botones por si acaso
    document.getElementById("btn-tab-login").onclick = () => switchTab('login');
    document.getElementById("btn-tab-register").onclick = () => switchTab('register');
});

function switchTab(tab) {
    const loginForm = document.getElementById("login-form");
    const registerForm = document.getElementById("register-form");
    const loginBtn = document.getElementById("btn-tab-login");
    const registerBtn = document.getElementById("btn-tab-register");

    if (!loginForm || !registerForm)
        return;

    if (tab === 'login') {
        loginForm.classList.remove("hidden");
        registerForm.classList.add("hidden");
        loginBtn.classList.add("active");
        registerBtn.classList.remove("active");
    } else {
        loginForm.classList.add("hidden");
        registerForm.classList.remove("hidden");
        loginBtn.classList.remove("active");
        registerBtn.classList.add("active");
    }
}