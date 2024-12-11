package projekt7.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import projekt7.entity.Category;
import projekt7.service.CategoryService;

import java.util.List;

@Controller
@RequestMapping("/categories")
public class CategoryController {

    private final CategoryService categoryService;

    @Autowired
    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @GetMapping("/list")
    public String getAllCategories(Model model) {
        List<Category> categories = categoryService.findAllCategories();
        model.addAttribute("categories", categories);
        return "categories/list";  // Ścieżka do widoku z listą kategorii
    }

    @GetMapping("/form")
    public String showNewCategoryForm(Model model) {
        model.addAttribute("category", new Category());
        return "categories/form";
    }
    @PostMapping
    public String saveCategory(@ModelAttribute Category category) {
        System.out.println("Saving category: " + category);
        categoryService.saveCategory(category);
        return "redirect:/categories/list";  // Przekierowanie do listy kategorii po zapisaniu
    }

    @DeleteMapping("/delete/{id}")
    public String deleteCategory(@PathVariable("id") Long id) {
        categoryService.deleteCategoryById(id);
        return "redirect:/categories/list";  // Przekierowanie do listy kategorii po usunięciu
    }
}
