package foodstore_backend.config;

import foodstore_backend.model.Usuario;
import foodstore_backend.model.enums.Rol;
import foodstore_backend.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

// Crea un usuario administrador por defecto si no existe
@Component
public class UserLoad implements CommandLineRunner {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) {
        String adminEmail = "admin@admin.com";

        boolean existeAdmin = usuarioRepository
                .findByEmailAndEliminadoFalse(adminEmail)
                .isPresent();

        if (!existeAdmin) {
            Usuario admin = new Usuario();
            admin.setNombre("Admin");
            admin.setApellido("Sistema");
            admin.setEmail(adminEmail);
            admin.setCelular("0000000000");
            admin.setPassword(passwordEncoder.encode("123456"));
            admin.setRol(Rol.ADMIN);
            admin.setEliminado(false);

            usuarioRepository.save(admin);

            System.out.println("Administrador por defecto creado: " + adminEmail);
        }
    }
}