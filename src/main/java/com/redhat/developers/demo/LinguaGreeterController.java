package com.redhat.developers.demo;

import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.jboss.logging.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import io.cloudevents.json.Json;
import io.cloudevents.v03.CloudEventBuilder;
import io.cloudevents.v03.CloudEventImpl;
import io.vertx.core.json.JsonObject;
import java.net.URI;
import java.util.UUID;
import com.google.cloud.translate.TranslateOptions;
import com.google.cloud.translate.Translation;
import com.google.cloud.translate.Translate.TranslateOption;

@RestController
@RequestMapping("/")
public class LinguaGreeterController {

    Logger logger = Logger.getLogger(LinguaGreeterController.class);

    @Value("${google.api.translate.srcLangCode}")
    String srcLangCode;

    @PostMapping(consumes = {MediaType.APPLICATION_JSON_VALUE},produces={MediaType.APPLICATION_JSON_VALUE})
    public @ResponseBody String translateGreeting(@RequestBody String payload) {
        logger.debug("Received body:" + payload);

        JsonObject jPayload = new JsonObject(payload);

        String message = jPayload.getString("greeting");
        String targetLangCode = jPayload.getString("targetLangCode");
        String translation = "Invalid Request. Translation not done";
        if (message != null && targetLangCode != null) {
            translation = translateText(message, targetLangCode);
        }
        jPayload.put("translation", translation);
        
        //build CE Response
        final String eventId = UUID.randomUUID().toString();
        final URI src = URI.create("/trigger");
        final String eventType = "com.redhat.developers.demo.LinguaGreeter";

        final CloudEventImpl<String> cloudEvent =
                CloudEventBuilder.<String>builder().withType(eventType).withId(eventId)
                        .withSource(src).withData(jPayload.encode()).build();
        // marshalling as json
        final String json = Json.encode(cloudEvent);

        logger.debug("Sending CE Response:" + json);

        return json;
    }

    private String translateText(String message, String targetLangCode) {
        TranslateOptions translateOptions = TranslateOptions.getDefaultInstance();

        Translation translator = translateOptions.getService().translate(message,
                TranslateOption.sourceLanguage(srcLangCode),
                TranslateOption.targetLanguage(targetLangCode));
        return translator.getTranslatedText();
    }
}
