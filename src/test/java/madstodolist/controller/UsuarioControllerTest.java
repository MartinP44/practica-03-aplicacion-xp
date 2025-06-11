package madstodolist.controller;


import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.Collections;
import java.util.List;

import madstodolist.dto.UsuarioData;
import madstodolist.service.UsuarioService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.ui.ExtendedModelMap;
import org.springframework.ui.Model;

@ExtendWith(MockitoExtension.class)
class UsuarioControllerTest {

    @Mock
    private UsuarioService usuarioService;

    @InjectMocks
    private UsuarioController controller;

    private Model model;

    @BeforeEach
    void setUp() {
        model = new ExtendedModelMap();
    }

    @Test
    void listadoUsuarios_shouldPopulateModelAndReturnView() {
        // GIVEN
        // Un servicio que devuelve una lista con un único UsuarioData
        List<UsuarioData> lista = Collections.singletonList(new UsuarioData());
        when(usuarioService.allUsuarios()).thenReturn(lista);

        // WHEN
        // Llamamos al controlador para obtener la vista de listado
        String vista = controller.listadoUsuarios(model);

        // THEN
        // El nombre de la vista debe ser "listadoUsuarios" y el modelo debe contener la lista
        assertEquals("listadoUsuarios", vista);
        assertSame(lista, model.getAttribute("usuarios"));
        verify(usuarioService).allUsuarios();
    }

    @Test
    void descripcionUsuario_shouldPopulateModelAndReturnView() {
        // GIVEN
        // Un UsuarioData específico devuelto por el servicio
        UsuarioData ud = new UsuarioData();
        ud.setId(42L);
        when(usuarioService.findById(42L)).thenReturn(ud);

        // WHEN
        // Llamamos al controlador para describir al usuario con id 42
        String vista = controller.descripcionUsuario(42L, model);

        // THEN
        // Debe regresar la plantilla "descripcionUsuario" y el modelo debe contener el DTO correcto
        assertEquals("descripcionUsuario", vista);
        assertSame(ud, model.getAttribute("usuario"));
        verify(usuarioService).findById(42L);
    }
}

