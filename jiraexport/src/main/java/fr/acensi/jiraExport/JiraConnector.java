package fr.acensi.jiraExport;

import com.atlassian.jira.rest.client.IssueRestClient;
import com.atlassian.jira.rest.client.JiraRestClient;
import com.atlassian.jira.rest.client.JiraRestClientFactory;
import com.atlassian.jira.rest.client.NullProgressMonitor;
import com.atlassian.jira.rest.client.domain.BasicIssue;
import com.atlassian.jira.rest.client.domain.ChangelogGroup;
import com.atlassian.jira.rest.client.domain.ChangelogItem;
import com.atlassian.jira.rest.client.domain.Issue;
import com.atlassian.jira.rest.client.domain.SearchResult;
import com.google.common.collect.Sets;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.logging.Logger;

import javax.inject.Inject;

import org.springframework.context.annotation.Scope;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

/**
 *
 * @author fateh.saada
 */
@Component("jiraConnector")
@Scope("prototype")
public class JiraConnector {  
    private String url;
    private String username;
    private String password;
    
    private Logger logger = Logger.getLogger("JiraConnector");
  
    public JiraConnector() {}
    
    public JiraConnector(String url, String username, String password) {
        this.url = url;
        this.username = username;
        this.password = password;
    }
    
    @Inject
    JiraRestClientFactory restClientFactory;

    @Inject
    private Environment env; 

    public Map<Long, Issue> getIssuesMap() throws URISyntaxException {
    	logger.info("started");
        Map<Long, Issue> result = new TreeMap<Long, Issue>();   
        final NullProgressMonitor pm = new NullProgressMonitor();
        URI restClientURI = new URI(url);
        JiraRestClient restClient = restClientFactory.createWithBasicHttpAuthentication(restClientURI, username, password);
        final SearchResult searchResult = restClient.getSearchClient().searchJql("assignee!='' or assignee is empty", 2000, 0, pm);
        IssueRestClient issueClient = restClient.getIssueClient();
        IssueRestClient.Expandos[] expandArr = new IssueRestClient.Expandos[] { IssueRestClient.Expandos.CHANGELOG };
        List<IssueRestClient.Expandos> expand = Arrays.asList(expandArr);
        logger.info("nbre d'issues: " + Sets.newHashSet(searchResult.getIssues()).size());
        int i =0;
        for(BasicIssue bi : searchResult.getIssues()) { 
        	i++ ;
        	logger.info("BasicIssue: " + i);
            Issue issue = issueClient.getIssue(bi.getKey(), expand, pm);
            result.put(issue.getId(), issue);
        }
        logger.info("finished");
        return result;    
    }

    
    public Map<Long, List<ChangelogGroup>> getChangelogMap(Map<Long, Issue> paramIssuesMap){
        Map<Long, List<ChangelogGroup>> result = new TreeMap<>();
        for(Long l : paramIssuesMap.keySet()) {
            result.put(l, new ArrayList<ChangelogGroup>());
            Iterable<ChangelogGroup> clg = paramIssuesMap.get(l).getChangelog();
            if(clg != null) {
                for(ChangelogGroup c : clg) {
                    result.get(l).add(c);
                }
            }
        }
        return result;
    }

    public void printIssuesAndLogs() throws URISyntaxException {  
        Map<Long, Issue> iMap = getIssuesMap();
        for(Long l : getChangelogMap(iMap).keySet()) {
            for(ChangelogGroup clg : getChangelogMap(iMap).get(l)) {
                if(clg != null) {
                    for(ChangelogItem cli : clg.getItems()) {
                        if(ChangelogItem.FieldType.JIRA.equals(cli.getFieldType())
                                && cli.getField().equals("status")) {
                            logger.info(cli.getFromString() + " ---> " + cli.getToString());
                        }
                    }
                }
            }
        }
    }
}
