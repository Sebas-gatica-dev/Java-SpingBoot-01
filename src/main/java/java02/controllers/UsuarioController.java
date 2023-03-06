package java02.controllers;


import de.mkammerer.argon2.Argon2;
import de.mkammerer.argon2.Argon2Factory;
import java02.dao.UsuarioDao;
import java02.models.Usuario;
import java02.utils.JWTUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
public class UsuarioController {
    @Autowired
    private UsuarioDao usuarioDao;

    @Autowired
    private JWTUtil jwtUtil;

    @RequestMapping(value = "api/usuarios/{id}", method = RequestMethod.GET)
    public Usuario getUsuario(@PathVariable long id) {

        Usuario usuario = new Usuario();
        usuario.setNombre("Lucas");
        usuario.setApellido("Gatica");
        usuario.setEmail("example@gmail.com");
        usuario.setTelefono("123456");
        usuario.setId(id);
        return usuario;
    }


    @RequestMapping(value = "api/usuarios")
    public List<Usuario> getUsuarios(@RequestHeader(value="Authorization") String token) {

       String usuarioId = jwtUtil.getKey(token);
       if(!validarToken(token)){
           return null;
       }

      return usuarioDao.getUsuarios();
    }

    private boolean validarToken(String token){
        String usuarioId = jwtUtil.getKey(token);
        return usuarioId != null;
    }

    @RequestMapping(value = "api/usuarios", method = RequestMethod.POST)
    public void registrarUsuario(@RequestBody Usuario usuario) {

         Argon2 argon2 = Argon2Factory.create(Argon2Factory.Argon2Types.ARGON2id);
         String hash = argon2.hash(1,1024,1,usuario.getPassword());

         usuario.setPassword(hash);

         usuarioDao.registrar(usuario);
    }


    @RequestMapping(value = "api/usuarios/{id}", method = RequestMethod.DELETE)
    public void  eliminar(@RequestHeader(value="Authorization") String token,
               @PathVariable long id) {
        if(!validarToken(token)){
            return;
        }
          usuarioDao.eliminar(id);

    }




}
