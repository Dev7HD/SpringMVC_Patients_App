package ma.dev7hd.springmvc_patients_app.web;

import lombok.AllArgsConstructor;
import ma.dev7hd.springmvc_patients_app.entities.Patient;
import ma.dev7hd.springmvc_patients_app.repositories.PatientRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

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
            @RequestParam(name = "keyword", defaultValue = "") String kw
    ){
        Page<Patient> patientsList = patientRepository.findByNameContains(kw, PageRequest.of(page,size));
        model.addAttribute("patientsList", patientsList.getContent());
        model.addAttribute("pages", new int[patientsList.getTotalPages()]);
        model.addAttribute("currentPage", page);
        model.addAttribute("keyword", kw);
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
}
