package ma.dev7hd.springmvc_patients_app;

import ma.dev7hd.springmvc_patients_app.entities.Patient;
import ma.dev7hd.springmvc_patients_app.repositories.PatientRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.util.Date;
import java.util.stream.Stream;

@SpringBootApplication
public class SpringMvcPatientsAppApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringMvcPatientsAppApplication.class, args);
    }
    @Bean
    CommandLineRunner commandLineRunner(PatientRepository patientRepository){
        return args -> {
            Stream.of("Adil","Mohammed","Hind","Widad").forEach(name -> {
                Patient patient = new Patient();
                patient.setName(name);
                patient.setBirthDay(new Date());
                patient.setSick(Math.random() > 0.5);
                patient.setScore((int) (Math.random() * 150));
                patientRepository.save(patient);
            });
        };
    }

}
