package it.BeGear.E_commerce.Configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import static org.springframework.security.config.http.SessionCreationPolicy.STATELESS;
import static org.springframework.security.config.Customizer.withDefaults;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;


@Configuration
public class SpringSecurityConfiguration {



    @Autowired
    JwtAuthFilter jwtAuthFilter;

    @Bean
    SecurityFilterChain defaultSecurityFilterChain(HttpSecurity http) throws Exception {
        http.csrf(csrf -> csrf.disable());
        http.authorizeHttpRequests((requests) -> requests
                .requestMatchers(
                        "/gestione_utenti/aggiungiSaldo",
                        "/gestione_utenti/sottraiSaldo",
                        "/gestione_utenti/getSaldo",
                        "/gestione_utenti/getUtente/{id}",
                        "/gestione_utenti/getUtenti",
                        "/gestione_prodotti/acquistaProdotti",
                        "/commenti/aggiungiCommento/{prodottoId}/{utenteId}",
                        "gestione_carrelli/rimuovi/{utenteId}/{prodottoId}",
                        "/gestione_carrelli/leggiCarrello/{id}",
                        "gestione_carrelli/aggiungi/{utenteId}/{prodottoId}/{quantita}").authenticated()
                .requestMatchers("/gestione_prodotti/leggiProdotti",
                        "/gestione_prodotti/filtratiPerSaldo",
                        "/gestione_prodotti/prodottiPiuVendutiPerTutteLeFasce",
                        "/gestione_prodotti/creaProdotto",


                        "/gestione_prodotti/getProdottoById/{id}",
                        // categorie
                        "/gestione_categorie/creaCategoria",
                        "/gestione_categorie/modificaCategoria/{id}",
                        "/gestione_categorie/ottieniCategorie",
                        "/gestione_categorie/cancellaCategoria/{id}",
                        "/gestione_categorie/prodottiPerCategoria/{categoriaNome}",
                        //////////////////////////////////////////////

                        "/gestione_utenti/aggiungiUtente",
                        "/gestione_prodotti/prodottiPiuVenduti/{fascia}",
                        "/gestione_utenti/login",
                        "/gestione_prodotti/assegnaFornitore",
                        "/commenti/commentiPerProdotto/{prodottoId}",
                        "/gestione_prodotti/assegnaMagazzino").permitAll()
                .requestMatchers("/gestione_utenti/modificaUtente/{id}",
                        "/gestione_prodotti/modificaProdotto/{id}",
                        "/gestione_prodotti/cancellaProdotto/{id}",
                        "/commenti/cancellaCommenti/{id}").hasAuthority("ADMIN"));
        http.cors(cors -> cors.configurationSource(corsConfigurationSource()));
        http.sessionManagement(session -> session.sessionCreationPolicy(STATELESS));
        http.addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }


    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(List.of("http://localhost:4200")); // Allow your Angular app's origin
        configuration.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        configuration.setAllowedHeaders(List.of("*")); // Allow all headers
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }
}