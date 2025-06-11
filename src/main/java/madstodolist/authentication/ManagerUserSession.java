package madstodolist.authentication;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import madstodolist.service.UsuarioService;

import javax.servlet.http.HttpSession;

@Component
public class ManagerUserSession {

    @Autowired
    HttpSession session;

    // Añadimos el id de usuario en la sesión HTTP para hacer
    // una autorización sencilla. En los métodos de controllers
    // comprobamos si el id del usuario logeado coincide con el obtenido
    // desde la URL
    @Autowired
    UsuarioService usuarioService;

    public void logearUsuario(Long idUsuario) {
        session.setAttribute("idUsuarioLogeado", idUsuario);
        // línea para funcionamiento adecuado de la barra de navegación
        session.setAttribute("usuario", usuarioService.findById(idUsuario));
    }

    public Long usuarioLogeado() {
        return (Long) session.getAttribute("idUsuarioLogeado");
    }

    public void logout() {
        session.setAttribute("idUsuarioLogeado", null);
    }
}
