package com.project.visa.controller;

import com.project.visa.entity.TestEntity;
import com.project.visa.service.TestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import java.util.List;

@Controller
@RequestMapping("/test")
public class TestController {
    
    @Autowired
    private TestService testService;
    
    @GetMapping("/list")
    public String list(Model model) {
        List<TestEntity> tests = testService.findAll();
        model.addAttribute("tests", tests);
        model.addAttribute("template", "test/test-list");
        return "template";
    }
    
    @GetMapping("/create")
    public String createForm(Model model) {
        model.addAttribute("test", new TestEntity());
        model.addAttribute("template", "test/test-create");
        return "template";
    }
    
    @PostMapping("/save")
    public String save(@ModelAttribute TestEntity test, RedirectAttributes redirectAttributes) {
        try {
            testService.save(test);
            redirectAttributes.addFlashAttribute("success", "Test enregistré avec succès!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Erreur lors de l'enregistrement: " + e.getMessage());
        }
        return "redirect:/test/list";
    }
    
    @GetMapping("/edit/{id}")
    public String editForm(@PathVariable Long id, Model model) {
        testService.findById(id).ifPresent(test -> model.addAttribute("test", test));
        model.addAttribute("template", "test/test-edit");  
        return "template";
    }
    
    @PostMapping("/update/{id}")
    public String update(@PathVariable Long id, @ModelAttribute TestEntity test, RedirectAttributes redirectAttributes) {
        try {
            test.setId(id);
            testService.save(test);
            redirectAttributes.addFlashAttribute("success", "Test mis à jour avec succès!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Erreur lors de la mise à jour: " + e.getMessage());
        }
        return "redirect:/test/list";
    }
    
    @GetMapping("/delete/{id}")
    public String delete(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            testService.deleteById(id);
            redirectAttributes.addFlashAttribute("success", "Test supprimé avec succès!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Erreur lors de la suppression: " + e.getMessage());
        }
        return "redirect:/test/list";
    }
    
    @GetMapping("/search")
    public String search(@RequestParam String keyword, Model model) {
        List<TestEntity> tests = testService.searchByName(keyword);
        model.addAttribute("tests", tests);
        model.addAttribute("keyword", keyword);
        model.addAttribute("template", "test/test-list"); 
        return "template";
    }

    @GetMapping("/api/list")
    @ResponseBody
    public ResponseEntity<List<TestEntity>> getTestsAsJson() {
        List<TestEntity> tests = testService.findAll();
        return ResponseEntity.ok(tests);
    }
}