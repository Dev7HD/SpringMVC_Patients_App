package ma.dev7hd.springmvc_patients_app.web;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import ma.dev7hd.springmvc_patients_app.entities.Patient;
import ma.dev7hd.springmvc_patients_app.repositories.PatientRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Date;
import java.util.List;

@Controller
@AllArgsConstructor
public class PatientController {
    PatientRepository patientRepository;

    @GetMapping(path = "/index")
    public String listPatients(
            Model model,
            @RequestParam(name = "page", defaultValue = "0") int page,
            @RequestParam(name = "size", defaultValue = "5") int size,
            @RequestParam(name = "keyword", defaultValue = "") String kw,
            @RequestParam(name = "successUpdate", defaultValue = "false") boolean successUpdate
    ){
        Page<Patient> patientsList = patientRepository.findByNameContains(kw, PageRequest.of(page,size));
        model.addAttribute("patientsList", patientsList.getContent());
        model.addAttribute("pages", new int[patientsList.getTotalPages()]);
        model.addAttribute("currentPage", page);
        model.addAttribute("keyword", kw);
        model.addAttribute("successUpdate", successUpdate);
        return "index";
    }

    @GetMapping("/delete")
    public String delete(
            @RequestParam(name = "id") Long id,
            @RequestParam(name = "page", defaultValue = "0") int page,
            @RequestParam(name = "keyword", defaultValue = "") String keyword
    ){
        patientRepository.deleteById(id);
        return "redirect:/index?page=" + page + "&keyword=" + keyword;
    }

    @GetMapping("/")
    public String home(){
        return "redirect:/index";
    }

    @GetMapping("/newPatient")
    public String newPatient(Model model){
        model.addAttribute("patient", new Patient());
        return "newPatient";
    }

    @PostMapping("/savePatient")
    public String savePatient(@Valid Patient patient, BindingResult bindingResult){
        if (bindingResult.hasErrors()) {
            return "newPatient";
        }

        System.out.println(patient);
        patientRepository.save(patient);
        return "newPatient";
    }

    @GetMapping("/editPatient")
    public String editPatient(
            Model model,
            @RequestParam(name = "id") Long id,
            @RequestParam(name = "page", defaultValue = "0") int page,
            @RequestParam(name = "keyword", defaultValue = "") String keyword
    ){
        Patient patient = patientRepository.findById(id).get();
        model.addAttribute("patient", patient);
        model.addAttribute("currentPage", page);
        model.addAttribute("keyword", keyword);
        return "editPatient";
    }

    @PostMapping("/saveEditedPatient")
    public String saveEditedPatient(
            @Valid Patient patient,
            BindingResult bindingResult,
            @RequestParam(name = "page", defaultValue = "0") int page,
            @RequestParam(name = "keyword", defaultValue = "") String keyword
    ){
        if (bindingResult.hasErrors()) {
            return "editPatient";
        }
        patientRepository.save(patient);
        return "redirect:/index?page=" + page + "&keyword=" + keyword + "&successUpdate=true";
    }


}

