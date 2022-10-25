import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.time.LocalDate;

@Getter
@Setter
@ToString
@RequiredArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "patients")
public class Patient {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Pattern(regexp="^[A-Za-z]*$", message = "Le prénom doit être de type text sans espace")
    @NotBlank(message = "Le prénom est obligatoire")
    @Size(min = 2, max = 26, message = "La longueur du prénom doit être de 2 à 26 caractères")
    @Column(name = "first_name")
    private String firstName;

    @Pattern(regexp="^[A-Za-z]*$", message = "Le nom de famille doit être de type text sans espace")
    @NotBlank(message = "Le nom de famille est obligatoire")
    @Size(min = 2, max = 26, message = "La longueur du nom de famille doit être de 2 à 26 caractères")
    @Column(name = "last_name")
    private String lastName;

    @Past(message = "Veuillez entrer une date de naissance valide")
    @NotNull(message = "La date de naissance est obligatoire")
    @JsonFormat(shape=JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    @Column(name = "birthDate")
    private LocalDate birthDate;

    @NotNull(message = "Le genre du sexe est obligatoire")
    @Column(name = "gender")
    private String gender;

    @Column(name = "adress")
    private String adress;

    @Column(name = "phone")
    private String phone;

}
