package projekt7.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.filter.HiddenHttpMethodFilter;

@Configuration
public class WebConfig {
   // Wykorzystanie HiddenHttpMethodFilter: Gdy formularz z metodą POST jest przesyłany,
    // HiddenHttpMethodFilter sprawdza, czy w przesyłanych danych znajduje się pole _method.
    // Jeśli tak, filtr konwertuje to żądanie POST na odpowiednie żądanie DELETE lub PUT,
    // pozwalając kontrolerowi na obsłużenie tego żądania zgodnie z jego mapowaniem.

    @Bean
    public HiddenHttpMethodFilter hiddenHttpMethodFilter() {
        return new HiddenHttpMethodFilter();
    }
}
