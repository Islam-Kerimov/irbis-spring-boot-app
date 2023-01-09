package ru.plus.irbis.web.app.config;

import jakarta.annotation.PostConstruct;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.support.CronTrigger;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import ru.plus.irbis.web.app.model.entity.TaskProperties;
import ru.plus.irbis.web.app.repository.UserRepository;
import ru.plus.irbis.web.app.service.ScheduledTaskService;
import ru.plus.irbis.web.app.service.TaskPropertiesService;
import ru.plus.irbis.web.app.task.ScheduledTaskNews;

import java.util.List;

/**
 * Настройка сервиса планирования задач.
 */
@Configuration
@EnableScheduling
public class ApplicationConfiguration {

    private final TaskPropertiesService taskPropertiesService;

    private final TaskScheduler taskScheduler;

    private final ScheduledTaskService scheduledTaskService;

    private final UserRepository userRepository;

    public ApplicationConfiguration(TaskPropertiesService taskPropertiesService,
                                    TaskScheduler taskScheduler,
                                    ScheduledTaskService scheduledTaskService,
                                    UserRepository userRepository) {
        this.taskPropertiesService = taskPropertiesService;
        this.taskScheduler = taskScheduler;
        this.scheduledTaskService = scheduledTaskService;
        this.userRepository = userRepository;
    }

    @Bean
    public UserDetailsService userDetailsService() {
        return username -> userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
    }

    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService());
        authProvider.setPasswordEncoder(passwordEncoder());
        return authProvider;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * Инициализация всех задач.
     */
    @PostConstruct
    public void createTasks() {
        List<TaskProperties> tasks = taskPropertiesService.getTaskProperties();

        for (TaskProperties taskProperties : tasks) {
            ScheduledTaskNews scheduledTask = new ScheduledTaskNews(taskProperties, scheduledTaskService);
            taskScheduler.schedule(scheduledTask, new CronTrigger(taskProperties.getCronExpression()));
        }
    }
}
