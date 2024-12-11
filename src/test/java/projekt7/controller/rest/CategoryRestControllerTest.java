package projekt7.controller.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.restassured.RestAssured;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.transaction.annotation.Transactional;
import projekt7.entity.Category;
import projekt7.repository.CategoryRepository;
import projekt7.repository.MenuItemRepository;
import projekt7.repository.RestaurantRepository;

import static org.hamcrest.Matchers.hasSize;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@Transactional
@ActiveProfiles("test")
public class CategoryRestControllerTest {

    @Autowired
    private ObjectMapper objectMapper;
    @LocalServerPort
    private int port;
    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private RestaurantRepository restaurantRepository;

    @Autowired
    private MenuItemRepository menuItemRepository;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private PasswordEncoder passwordEncoder;

    private Category category;

    @BeforeEach
    public void setUp() throws Exception {
        RestAssured.port = port;

        // Inicjalizacja testowej kategorii
        category = new Category();
        category.setName("Test Category");
        category = categoryRepository.save(category); // Zapisanie kategorii do repozytorium
    }

    @Test
    @WithMockUser(username = "testuser", roles = {"ADMIN"})
    public void testGetAllCategories() throws Exception {
        // Testowanie endpointu GET /api/categories
        mockMvc.perform(get("/api/categories")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1))) // Czy jest jedna kategoria
                .andExpect(jsonPath("$[0].name").value("Test Category")); // Sprawdź nazwę kategorii
    }

    @Test
    @WithMockUser(username = "testuser", roles = {"ADMIN"})
    public void testCreateAndGetCategory() throws Exception {
        // Tworzenie nowej kategorii
        Category newCategory = new Category();
        newCategory.setName("New Category");

        // Zapisz nową kategorię
        newCategory = categoryRepository.save(newCategory);

        // Weryfikacja nowej kategorii
        mockMvc.perform(get("/api/categories"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[?(@.name == 'New Category')]").exists()); // Sprawdź, czy nowa kategoria istnieje
    }

    @Test
    @WithMockUser(username = "testuser", roles = {"ADMIN"})
    public void testUpdateCategory() throws Exception {
        // Nowe dane do aktualizacji
        Category updatedCategory = new Category();
        updatedCategory.setName("Updated Category");

        // Testowanie endpointu PUT /api/categories/{id}
        mockMvc.perform(MockMvcRequestBuilders.put("/api/categories/{id}", category.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updatedCategory)) // Konwersja obiektu do JSON
                        .with(csrf())) // Dodanie tokenu CSRF
                .andExpect(status().isOk()) // Oczekiwany status 200 OK
                .andExpect(jsonPath("$.name").value("Updated Category")); // Sprawdzenie nowej nazwy kategorii

        // Sprawdzenie, czy kategoria została zaktualizowana w bazie danych
        Category retrievedCategory = categoryRepository.findById(category.getId()).orElse(null);
        assertNotNull(retrievedCategory);
        assertEquals("Updated Category", retrievedCategory.getName());
    }

    @Test
    @WithMockUser(username = "testuser", roles = {"ADMIN"})
    public void testDeleteCategory() throws Exception {
        // Weryfikacja, czy kategoria została utworzona
        assertNotNull(category);

        // Testowanie endpointu DELETE /api/categories/{id}
        mockMvc.perform(delete("/api/categories/{id}", category.getId())
                        .with(csrf())) // Dodanie tokenu CSRF
                .andExpect(status().isNoContent()); // Oczekiwany status 204 No Content

        // Weryfikacja, czy kategoria została usunięta
        mockMvc.perform(get("/api/categories"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(0))); // Sprawdzenie, że lista kategorii jest pusta
    }
}
