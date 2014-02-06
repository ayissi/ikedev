package fr.acensi.jiraExport;

import au.com.bytecode.opencsv.CSVWriter;

import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.annotation.Resource;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 * @author fateh.saada
 */
@Component
@Scope("prototype")
public class JiraExporter {
    @Resource(name = "issueHeaderString")
    private String issueHeaderString;

    @Resource(name = "changelogHeaderString")
    private String changelogHeaderString;
    
    @Resource(name = "issueLinksHeader")
    private String issueLinksHeader;

    private final String issuesFilePath;
    private final String changelogFilePath;   
    private final String issuesAndLinkPath ;
 
	private static Logger logger = Logger.getLogger("LOG");
    public JiraExporter(String issuesFilePath, String changelogFilePath , String issuesAndLinkPath ) {
        this.issuesFilePath = issuesFilePath;
        this.changelogFilePath = changelogFilePath;
        this.issuesAndLinkPath = issuesAndLinkPath ;
    }

    public void writeIssues(List<String[]> issueEntriesList) {
        try (CSVWriter writer = new CSVWriter(new FileWriter(issuesFilePath), ',')) {
            writer.writeNext(getIssuesHeader());
            writer.writeAll(issueEntriesList);
            writer.flush();
            writer.close();
        } catch (IOException ex) {
            Logger.getLogger(JiraExporter.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void writeIssuesAndLinks(List<String[]> issuesAndLinksList){
        try (CSVWriter writer = new CSVWriter(new FileWriter(issuesAndLinkPath), ',')) {
        	logger.info("writeIssuesAndLinks: started");
        	writer.writeNext(getIssueLinksHeader());
            writer.writeAll(issuesAndLinksList);
            writer.flush();
        } catch (IOException ex) {
            Logger.getLogger(JiraExporter.class.getName()).log(Level.SEVERE, null, ex);
        }                                        
        logger.info("writeIssuesAndLinks: ended");
    }
    
    
    public void writeChangelogs(List<String[]> changelogEntriesList) {
        try (CSVWriter writer = new CSVWriter(new FileWriter(changelogFilePath), ',')) {
            writer.writeNext(getChangelogsHeader());
            writer.writeAll(changelogEntriesList);
            writer.flush();
        } catch (IOException ex) {
            Logger.getLogger(JiraExporter.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    private String[] getIssuesHeader() {
        return issueHeaderString.split(",");
    }
    
    private String[] getChangelogsHeader() {
        return changelogHeaderString.split(",");
    }
    
    private String[] getIssueLinksHeader() {
    	return issueLinksHeader.split(",");
    }
}
