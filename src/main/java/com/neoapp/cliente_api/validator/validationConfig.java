package com.neoapp.cliente_api.validator;

import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;

@Configuration
public class validationConfig {

    /* Esta classe é responsável por configurar o Bean Validation do Spring para
     que ele use seu arquivo de mensagens personalizado (validationMessages.properties).
     Sua classe está correta, e é exatamente o que discutimos anteriormente. Ela é crucial
     para garantir que as mensagens personalizadas que você definiu nas anotações de validação
      (@NotBlank, @Pattern, etc.) sejam lidas e retornadas corretamente para o cliente.*/

    @Bean
    public MessageSource messageSource(){
        ResourceBundleMessageSource source = new ResourceBundleMessageSource();
        source.setBasename("validationMessages");
        source.setDefaultEncoding("UTF-8");
        return source;
    }

    @Bean
    public LocalValidatorFactoryBean validator(MessageSource messageSource) {
        LocalValidatorFactoryBean bean = new LocalValidatorFactoryBean();
        bean.setValidationMessageSource(messageSource);
        return bean;
    }
}
