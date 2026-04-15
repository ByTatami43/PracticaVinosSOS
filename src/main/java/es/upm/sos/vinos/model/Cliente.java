package es.upm.sos.vinos.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.hateoas.RepresentationModel;

import java.time.LocalDate;

@Entity
@Table(name = "clientes")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Cliente extends RepresentationModel<Cliente> {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @NotNull(message = "El nombre es obligatorio y no puede ser null")
    private String nombre;

    @NotNull(message = "El correo es obligatorio y no puede ser null")
    @Email
    private String correo;

    @NotNull
    private LocalDate fechaNacimiento;


}
