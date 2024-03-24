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


@Controller
@AllArgsConstructor
public class PatientController {
    PatientRepository patientRepository;

    @GetMapping("/")
    public String home(){
        return "redirect:/user/index";
    }

    @GetMapping(path = "/user/index")
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

    @GetMapping("/admin/delete")
    public String delete(
            @RequestParam(name = "id") Long id,
            @RequestParam(name = "page", defaultValue = "0") int page,
            @RequestParam(name = "keyword", defaultValue = "") String keyword
    ){
        patientRepository.deleteById(id);
        return "redirect:/user/index?page=" + page + "&keyword=" + keyword;
    }

    @GetMapping("/admin/newPatient")
    public String newPatient(
            @RequestParam(name = "successSave", defaultValue = "false") boolean successSave,
            Model model
    ){
        model.addAttribute("patient", new Patient());
        model.addAttribute("successSave", successSave);
        return "newPatient";
    }

    @PostMapping("/admin/savePatient")
    public String savePatient(
            @Valid Patient patient,
            BindingResult bindingResult
    ){
        if (bindingResult.hasErrors()) {
            return "newPatient";
        }

        patientRepository.save(patient);
        return "redirect:/admin/newPatient?successSave=true";
    }

    @GetMapping("/admin/editPatient")
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

    @PostMapping("/admin/saveEditedPatient")
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
        return "redirect:/user/index?page=" + page + "&keyword=" + keyword + "&successUpdate=true";
    }


}

