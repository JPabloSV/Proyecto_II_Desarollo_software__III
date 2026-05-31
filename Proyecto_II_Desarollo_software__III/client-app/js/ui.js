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