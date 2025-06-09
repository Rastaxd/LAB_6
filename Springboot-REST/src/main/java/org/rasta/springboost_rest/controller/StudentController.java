package com.project.controller;

import com.project.model.Student;
import com.project.service.StudentService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.HttpStatusCodeException;

@Controller
public class StudentController {
    private StudentService studentService;

    public StudentController(StudentService studentService) {
        this.studentService = studentService;
    }

    @GetMapping("/studentList")
    public String studentList(Model model, Pageable pageable,
                              @RequestParam(required = false) String nrIndeksu,
                              @RequestParam(required = false) String nazwisko) {
        if (nrIndeksu != null && !nrIndeksu.isEmpty()) {
            model.addAttribute("studenci", studentService.searchByNrIndeksu(nrIndeksu, pageable).getContent());
            model.addAttribute("searchNrIndeksu", nrIndeksu);
        } else if (nazwisko != null && !nazwisko.isEmpty()) {
            model.addAttribute("studenci", studentService.searchByNazwisko(nazwisko, pageable).getContent());
            model.addAttribute("searchNazwisko", nazwisko);
        } else {
            model.addAttribute("studenci", studentService.getStudenci(pageable).getContent());
        }
        return "studentList";
    }

    @GetMapping("/studentEdit")
    public String studentEdit(@RequestParam(name="studentId", required = false) Integer studentId, Model model) {
        if(studentId != null) {
            model.addAttribute("student", studentService.getStudent(studentId).get());
        } else {
            Student student = new Student();
            model.addAttribute("student", student);
        }
        return "studentEdit";
    }

    @PostMapping(path = "/studentEdit")
    public String studentEditSave(@ModelAttribute @Valid Student student, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "studentEdit";
        }
        try {
            student = studentService.setStudent(student);
        } catch (HttpStatusCodeException e) {
            bindingResult.rejectValue(StringUtils.EMPTY, String.valueOf(e.getStatusCode().value()),
                    e.getStatusCode().toString());
            return "studentEdit";
        }
        return "redirect:/studentList";
    }

    @PostMapping(params="cancel", path = "/studentEdit")
    public String studentEditCancel() {
        return "redirect:/studentList";
    }

    @PostMapping(params="delete", path = "/studentEdit")
    public String studentEditDelete(@ModelAttribute Student student) {
        studentService.deleteStudent(student.getStudentId());
        return "redirect:/studentList";
    }
}