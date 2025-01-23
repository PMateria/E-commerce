package it.BeGear.E_commerce.Configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import static org.springframework.security.config.Customizer.withDefaults;


@Configuration
public class SpringSecurityConfiguration {

    @Bean
    SecurityFilterChain defaultSecurityFilterChain(HttpSecurity http) throws Exception {
        http.csrf(csrf -> csrf.disable());
        http.authorizeHttpRequests((requests) -> requests
                .requestMatchers("/gestione_prodotti/creaProdotto",
                        "/gestione_prodotti/modificaProdotto",
                        "/gestione_prodotti/cancellaProdotto",
                        "/gestione_utenti/aggiungiSaldo",
                        "/gestione_utenti/sottraiSaldo",
                        "/gestione_utenti/getSaldo",
                        "/gestione_prodotti/filtratiPerSaldo",
                        "/gestione_prodotti/prodottiPiuVenduti",
                        "/gestione_prodotti/prodottiPiVendutiPerTutteLeFasce" ).authenticated()
                .requestMatchers("/gestione_prodotti/leggiProdotti",
                        "/gestione_utenti/aggiungiUtente").permitAll());

        http.authorizeHttpRequests((requests) -> requests.anyRequest().permitAll());
        http.formLogin(withDefaults());
        http.httpBasic(withDefaults());
        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }
}