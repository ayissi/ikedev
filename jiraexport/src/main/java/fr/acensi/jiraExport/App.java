package fr.acensi.jiraExport;
  
import java.io.Console;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.springframework.beans.factory.BeanFactory;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.support.StaticApplicationContext;

import com.atlassian.jira.rest.client.domain.ChangelogGroup;
import com.atlassian.jira.rest.client.domain.ChangelogItem;
import com.atlassian.jira.rest.client.domain.Issue;
import com.atlassian.jira.rest.client.domain.IssueLink;
import com.google.common.collect.Sets;

import fr.acensi.jiraExport.conf.JiraExportConf;
import fr.acensi.jiraExport.exception.JiraExportException;




public class App {

	private static Logger logger = Logger.getLogger("LOG");
	
	static final  String URL ="";
	static final  String PWD ="";
	static final  String ISSUES_FILE ="";
	static  final String CHANGELOG_FILE ="";
	static final  String ISSUES_LINK_FILE = "C:\\Users\\invite01.acensi\\Issuelinks.csv";
  
	
    /**
     * Add a <code>FileHandler</code> to the provided <code>Logger</code>.
     * @param logger <code>Logger</code> to add <code>FileHandler</code>.
     */
    private static void addFileHandler(Logger logger) {
    	FileHandler   fileHandler = null; 
        try {
        fileHandler = new FileHandler(App.class.getName() + ".log");
        } catch (IOException ex) {
            logger.log(Level.SEVERE, null, ex);
        } catch (SecurityException ex) {
            logger.log(Level.SEVERE, null, ex);
        }
        logger.addHandler(fileHandler);
    }
    

	/**
	 * exporte dans un fichier csv, les différents noms de tickets et ceux qui y sont liés,
	 * dans une relation Parent-Enfant (Parent-Child)
	 * @param je l'exporter vers des csv 
	 * @param issues les tickets
	 * */			  
	public static void exportIssueAndLinks(JiraExporter je, Map<Long, Issue> issues) {
		 logger.info("exportIssueAndLinks : started");
		 List<String[]> entries = new ArrayList<>();  
		 issues = sortMapByComparator(issues);   
		for(Long l:issues.keySet()) {
			Issue issue = issues.get(l); 
			if(issue.getIssueLinks()!=null || (Sets.newHashSet(issue.getIssueLinks()).size()!=0)){
				logger.info("issue: " + issue.getKey() +" nbre de fils =  " + Sets.newHashSet(issue.getIssueLinks()).size() ); 
				for(IssueLink issueLink:issue.getIssueLinks()) {
					String[] linkEntry = new String[2];
					linkEntry[0] = issue.getKey();
					linkEntry[1] = issueLink.getTargetIssueKey();
					logger.info("parent: " + linkEntry[0] + " enfant: " + linkEntry[1]); 
					entries.add(linkEntry);
				}	
			}else if(Sets.newHashSet(issue.getIssueLinks()).size()==0) {
				logger.info("issue: " + issue.getKey() + " nbre de fils =  " + Sets.newHashSet(issue.getIssueLinks()).size() );  
				String[] linkEntry = new String[2];
				linkEntry[0] = issue.getKey();
				linkEntry[1] = "----";   
				logger.info("parent: " + linkEntry[0] + " enfant: " + linkEntry[1]);
				entries.add(linkEntry);
			} 
		}
		je.writeIssuesAndLinks(entries);
		logger.info("exportIssueAndLinks :finished");  
	}
	
	
	private static Map sortMapByComparator(Map unsortedMap) {
		List<Map.Entry<Long,Issue>> list = new LinkedList(unsortedMap.entrySet());
		 
		// sort list based on comparator
        
		/*Collections.sort(list, new Comparator(){  
			public int compare(Object o1, Object o2) {
				return ((Comparable) ((Map.Entry) (o1)).getValue())
                                       .compareTo(((Map.Entry) (o2)).getValue());
			}
		});*/
 
		
		/**
		 * compare deux <code> Issue </code> par l'ordre alphabétique de leurs clés 
		 * */
		class IssueComparator implements Comparator<Map.Entry<Long,Issue>> {
			@Override
			public int compare(Entry<Long, Issue> o1, Entry<Long, Issue> o2) {
				return o1.getValue().getKey()
						.compareTo(o2.getValue().getKey()) ;
			}
		};
			
		 Collections.sort(list, new IssueComparator());     
	
	    // put  sorted list into map again
       //LinkedHashMap make sure order in which keys were inserted
		Map sortedMap = new LinkedHashMap();
		for (Iterator it = list.iterator(); it.hasNext();) {
			Map.Entry entry = (Map.Entry) it.next();
			sortedMap.put(entry.getKey(), entry.getValue());
		}
		return sortedMap;
	}

	/**
	 * exporte les tickets passé en paramètre dans un fichier csv
	 * @param je l'exporteur
	 * @param issues les ressources à exporter 
	 * */
	public static void exportIssues(JiraExporter je, Map<Long, Issue> issues) {
		logger.info("started");
		DateFormat df = new SimpleDateFormat("dd/MM/yyyy hh:mm");
        List<String[]> entries = new ArrayList<>();
       issues = sortMapByComparator(issues); 
        for(Long l : issues.keySet()){
            Issue issue = issues.get(l);
            String[] entry = new String[10];
            entry[0]=issue.getKey();
            entry[1]=issue.getProject().getName();
            entry[2]=issue.getSummary();
            entry[3]=issue.getIssueType().getName();
            if(issue.getAssignee() != null) {
            	entry[4]=issue.getAssignee().getDisplayName();	
            }
            if(issue.getReporter() != null) {entry[5]=issue.getReporter().getDisplayName();}
            if(issue.getPriority() != null){entry[6]=issue.getPriority().getName();}
            if(issue.getStatus() != null){ entry[7]=issue.getStatus().getName();}
            if(issue.getResolution() != null){
            	entry[8]=issue.getResolution().getName();
            }
            if(issue.getCreationDate() != null){
            	entry[9]=df.format(issue.getCreationDate().toDate());
            }
            entries.add(entry);
        }
        je.writeIssues(entries);
        logger.info("finished");  
	}
	
	/**
	 * exporte les tickets passé en paramètre dans un fichier csv
	 * @param jc le connecteur jira
	 * @param je l'exporteur
	 * @param issues les ressources à exporter 
	 * */
	public static void exportChangelogGroup(JiraConnector jc,JiraExporter je, Map<Long, Issue> issues) {
        logger.info("started");
		DateFormat df = new SimpleDateFormat("dd/MM/yyyy hh:mm");
        List<String[]>  entries = new ArrayList<>();
        issues = sortMapByComparator(issues); 
        Map<Long, List<ChangelogGroup>> changelogs = jc.getChangelogMap(issues);
        for(Long l : changelogs.keySet()) {
            for(ChangelogGroup clg : changelogs.get(l)){  
                for(ChangelogItem cli : clg.getItems()) {
                    if(ChangelogItem.FieldType.JIRA.equals(cli.getFieldType())
                            && cli.getField().equals("status")) {
                        String[] entry = new String[5];
                        entry[0]=issues.get(l).getKey();
                        if(clg.getAuthor() != null) {
                        	 entry[1]=clg.getAuthor().getDisplayName();
                        }
                        entry[2]=cli.getFromString();
                        entry[3]=cli.getToString();
                        entry[4]=df.format(clg.getCreated().toDate());
                        entries.add(entry);            
                    }
                }
            }
        }
        je.writeChangelogs(entries);  
        logger.info("finished");
	}
		
	public static void main(String[] args){
		try{  
			addFileHandler(logger);
			logger.info("starting...\n");
	        BeanFactory beanFactory = (BeanFactory) new AnnotationConfigApplicationContext(JiraExportConf.class);
	        Console cnsl = null;  
	/*      String url = null;
	        String username = null;
	        String issuesFilePath = null;
	        String changelogFilePath = null;
	        String issueAndListPath = null ;
	        char[] pwd = null;*/   
	/* */   String url = "https://acensi.atlassian.net/";
	        String username = "james.kouthon";
	        String issuesFilePath ="C:\\Users\\invite01.acensi\\prblems.csv";
	        String changelogFilePath ="C:\\Users\\invite01.acensi\\changelogs.csv";  
	        String issueAndListPath ="C:\\Users\\invite01.acensi\\issuelinks.csv" ;
	        char[] pwd = "Juillet2007#".toCharArray();      
	        		
//	        try{
//	           cnsl = System.console();
//	           if (cnsl != null) {
//	               url = cnsl.readLine("URL : ");
//	               username = cnsl.readLine("Login : ");
//	               pwd = cnsl.readPassword("Password : ");
//	               issuesFilePath = cnsl.readLine("Issues File Path and name : ");
//	               changelogFilePath = cnsl.readLine("Changelog File Path and name :");
//	               issueAndListPath= cnsl.readLine("IssueLinks Path and name :");
//	           }              
//	        }catch(Exception ex){
//	           ex.printStackTrace();      
//	        }
	        
	        JiraConnector jc = (JiraConnector) beanFactory.getBean("jiraConnector", url, username, new String(pwd));
	        JiraExporter je = (JiraExporter) beanFactory.getBean("jiraExporter", issuesFilePath,changelogFilePath,issueAndListPath);
	        Map<Long, Issue> issues = jc.getIssuesMap();
	        exportIssueAndLinks(je,issues);  
	        exportIssues(je,issues);
	        exportChangelogGroup(jc,je,issues) ;

		}catch(Exception e) {
			logger.info(new JiraExportException(e).getMessage()); 
		}
		 
	}
	
}