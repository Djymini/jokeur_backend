package com.ashleydev.jokeur_api.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "jokeur")
@Getter
@Setter
/**
 * cette class permet de récupèrer les propertiems qui sont defini dans le fichier application.yml
 */
public class JokeurProperties {

  String rssUrl;
}
