const Ui = {
    verificarSesion: function() {
        const usuario = sessionStorage.getItem("usuario");
        if (!usuario) {
            window.location.href = "index.html";
            return null;
        }
        return JSON.parse(usuario);
    },

    verificarSupervisor: function() {
        const usuario = this.verificarSesion();
        if (usuario && usuario.role !== "SUPERVISOR") {
            alert("Solo los supervisores pueden realizar esta acción.");
            return null;
        }
        return usuario;
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

    ocultarMensaje: function(elementoId) {
        const elemento = document.getElementById(elementoId);
        if (elemento) {
            elemento.classList.add("oculto");
        }
    }
};