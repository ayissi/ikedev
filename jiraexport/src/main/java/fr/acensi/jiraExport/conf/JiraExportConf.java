package fr.acensi.jiraExport.conf;

import com.atlassian.jira.rest.client.JiraRestClient;
import com.atlassian.jira.rest.client.JiraRestClientFactory;
import com.atlassian.jira.rest.client.internal.jersey.JerseyJiraRestClientFactory;
import java.net.URI;
import java.net.URISyntaxException;
import javax.inject.Inject;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;

/**
 *
 * @author fateh.saada
 */
@Configuration
@PropertySource("classpath:jira-export.properties")
@ComponentScan("fr.acensi.jiraExport")
public class JiraExportConf {
    @Inject
    Environment env;
    
    @Bean
    public JiraRestClientFactory getJiraRestClientFactory() {
        return new JerseyJiraRestClientFactory();
    }

    @Bean(name = "issueHeaderString")
    public String getIssueHeaderString() {
        return env.getProperty("issue.header.string");
    }

    @Bean(name = "changelogHeaderString")
    public String getChangelogHeaderString() {
        return env.getProperty("changelog.header.string");
    }
    
    @Bean(name = "issuesFilePath")
    public String getIssuesFilePath() {
        return env.getProperty("issues.file.path");
    }

    @Bean(name = "changelogFilePath")
    public String getChangelogFilePath() {
        return env.getProperty("changelog.file.path");
    }
    
    @Bean(name = "issueLinksHeader")
    public String getIssueLinksHeader(){
    	return env.getProperty("issue.links.header");
    }
}
