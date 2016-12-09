package com.kkrishna.gymtime;

import java.util.Arrays;
import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.rest.core.config.RepositoryRestConfiguration;
import org.springframework.data.rest.webmvc.config.RepositoryRestConfigurerAdapter;
import org.springframework.hateoas.MediaTypes;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.orm.jpa.vendor.HibernateJpaSessionFactoryBean;

import com.kkrishna.gymtime.dao.Gym;
import com.kkrishna.gymtime.dao.User;
import com.kkrishna.gymtime.dao.events.CommentsEventHandler;

@Configuration
public class GymTimeConfiguration extends RepositoryRestConfigurerAdapter {

	@Override
	public void configureRepositoryRestConfiguration(RepositoryRestConfiguration config) {
		config.exposeIdsFor(User.class);
		config.exposeIdsFor(Gym.class);
	}

	@Bean
	public HibernateJpaSessionFactoryBean sessionFactory() {
		return new HibernateJpaSessionFactoryBean();
	}

	@Override
	public void configureHttpMessageConverters(List<HttpMessageConverter<?>> messageConverters) {
		((MappingJackson2HttpMessageConverter) messageConverters.get(0))
				.setSupportedMediaTypes(Arrays.asList(MediaTypes.HAL_JSON));
		((MappingJackson2HttpMessageConverter) messageConverters.get(1))
				.setSupportedMediaTypes(Arrays.asList(MediaType.APPLICATION_JSON));

	}

	@Bean
	CommentsEventHandler commentsEventHandler() {
		return new CommentsEventHandler();
	}
}
