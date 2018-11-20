package pe.edu.unmsm.sistemas.servidorclinica.daemon;

import java.net.URI;
import java.net.URISyntaxException;

import com.fasterxml.jackson.databind.JsonNode;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.http.RequestEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class DaemonController {

    private static final Logger log = LoggerFactory.getLogger(DaemonController.class);

    @Value("${app.uri.cada-cinco-minutos}")
    private String urlCadaCincoMinutos;

    @Value("${app.uri.diario-siete-am}")
    private String urlDiarioSieteAM;

    @Scheduled(initialDelayString = "${app.retraso-inicial}", fixedRateString = "${app.intervalo.cada-cinco-minutos}")
    public void llamadaCadaCincoMinutos() {
        log.info("Inicio de llamada al endpoint daemon {}", urlCadaCincoMinutos);

        RestTemplate restTemplate = new RestTemplate();
        log.debug("URI: {}", urlCadaCincoMinutos);

        try {
            RequestEntity<Void> req = RequestEntity.get(new URI(urlCadaCincoMinutos)).accept(MediaType.APPLICATION_JSON)
                    .build();

            restTemplate.exchange(req, JsonNode.class);
            log.info("Fin de llamada al endpoint daemon");
        } catch (URISyntaxException e) {
            log.error("Error en la sintaxis de la URL " + urlCadaCincoMinutos, e);
        }
    }

    @Scheduled(cron = "${app.intervalo.diario-siete-am}", zone = "GMT-05:00")
    public void llamadaDiariaSieteAM() {
        log.info("Inicio de llamada al endpoint daemon {}", urlDiarioSieteAM);

        RestTemplate restTemplate = new RestTemplate();
        log.debug("URI: {}", urlDiarioSieteAM);

        try {
            RequestEntity<Void> req = RequestEntity.get(new URI(urlDiarioSieteAM)).accept(MediaType.APPLICATION_JSON)
                    .build();

            restTemplate.exchange(req, JsonNode.class);
            log.info("Fin de llamada al endpoint daemon");
        } catch (URISyntaxException e) {
            log.error("Error en la sintaxis de la URL " + urlDiarioSieteAM, e);
        }
    }

}