package ui;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.SwingWorker;
import javax.swing.table.DefaultTableModel;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import controller.Conversations;
import controller.ConversationsClient;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

import java.text.DecimalFormat;
import java.text.Normalizer;
import java.text.NumberFormat;
import java.text.ParseException;

import java.time.Duration;
import java.time.LocalDate;
import java.time.ZoneId;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.ListIterator;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import model.Conversation;
import model.MessageData;
import model.Messages;
import model.Page;



public class ScrapeInterfaceNew extends JFrame {

	private AtomicBoolean stopRequested = new AtomicBoolean(false);

	public final static String PHONE = "((?:\\d[- ]?){10})";//10 Digits with dashes and white spaces
	public final static String EMAIL = "[a-zA-Z0-9_.+-]+@[a-zA-Z0-9-]+\\.[a-zA-Z0-9-.]+";
	public final static Duration THIRTY_DAYS = Duration.ofDays(30);
	public final static Duration ONE_DAY = Duration.ofDays(1);
	public String apiKeyFieldValue = "";
	
	//Put the words in lowercase without accents
	public final static String[] interestKeywords = {"perfecto","excelente", "calle" ,"claro" , "necesito"
				,"numero","descuento","interesa","dispongo","bien","ahora","ahorita","quiero comprar","quiero hacer una compra"
				,"ok","okey","okei","oki","si tengo","requiero","urge","payjoy","pay joy","de acuerdo","gusta","tengo el enganche"
				,"tengo hasta","si cuento","macropay", "macro pay","quiero otro cel","termine de pagar","late","emociona","genial"
				,"fantastico","super","mañana","amigo","amiga"};   
		
	public final static  Set<String> interestKeywordsSet = new HashSet<String>(Arrays.asList(interestKeywords)); 

	private SwingWorker<Void, Object[]> worker;
	private Workbook workbook;
	private JDialog progressDialog;
	private JTextField apiKeyField;
	private JTable dataTable;
	private JLabel apiKeyLabel;
	private DefaultTableModel tableModel;
	private JButton startDownloadButton;
	private JButton stopButton;
	private JButton exportButton;
	
    public ScrapeInterfaceNew() {
        super("Download Conversations From Facebook Pages");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // create input panel
        JPanel inputPanel = new JPanel();
        apiKeyLabel = new JLabel("API-Key: ");
        apiKeyField = new JTextField(50);
        inputPanel.add(apiKeyLabel);
        inputPanel.add(apiKeyField);

        // create table panel
        JPanel tablePanel = new JPanel(new BorderLayout());
        String[] columnNames = new String[]{"ConverId", "FBId", "LeadName", "Phone", "Email", "Created", "LastUpd", "Snippet", "LastUser", "LeadMsg"};
        tableModel = new DefaultTableModel(columnNames, 0);
        dataTable = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(dataTable);
        tablePanel.add(scrollPane, BorderLayout.CENTER);

        // create export button
        exportButton = new JButton("Export to Excel");
        exportButton.setEnabled(false);
        exportButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                exportToExcel();
            }
        });
        
        
     // create stop button
        stopButton = new JButton("Stop");
        stopButton.setEnabled(false);
        stopButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                stopProcess();
            }

            private void stopProcess() {
            	stopButton.setEnabled(false); // Set the stop flag
            	stopRequested.set(true);
                
      	        JOptionPane.showMessageDialog(ScrapeInterfaceNew.this, "Stopping the process will only require a few seconds. Click OK to continue.", "Extraction Stopped", JOptionPane.INFORMATION_MESSAGE);

            }
        });
        
        
        // create start download button
        startDownloadButton = new JButton("Start");
        
        startDownloadButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
    	    	startDownloadButton.setEnabled(false); // Enable the startDownloadButton again
       		    exportButton.setEnabled(false);

            	fillTableWithDummyData();
            }
        });
        
        // add buttons to button panel
        JPanel buttonPanel = new JPanel();
        buttonPanel.add(exportButton);
        
        buttonPanel.add(startDownloadButton);
        buttonPanel.add(stopButton);
        tablePanel.add(buttonPanel, BorderLayout.SOUTH);

        // add panels to main frame
        getContentPane().setLayout(new BorderLayout());
        getContentPane().add(inputPanel, BorderLayout.NORTH);
        getContentPane().add(tablePanel, BorderLayout.CENTER);
    }
    
    
    private void fillTableWithDummyData() {
    	
    	// Clear all rows from the table
    	tableModel.setRowCount(0);
    	
    	apiKeyFieldValue = apiKeyField.getText().trim();
    	 
    	    if (apiKeyFieldValue.isEmpty()) {
    	        // Show notification that there is no API key submitted
    	    	
    	    	startDownloadButton.setEnabled(true); // Enable the startDownloadButton again
       		    stopButton.setEnabled(false);
       		    exportButton.setEnabled(false);
    	    	
    	        JOptionPane.showMessageDialog(ScrapeInterfaceNew.this, "No API key submitted.", "No API Key", JOptionPane.INFORMATION_MESSAGE);
    	        return;
    	    }
    	    
  

        
        
        stopRequested.set(false); // Reset the stop flag before starting the process

        worker = new SwingWorker<Void, Object[]>() {
        	
            @Override
            protected Void doInBackground() throws Exception {
     
            	
	                ///////Long time process
	        		String baseUrl = "https://graph.facebook.com";
	        		String longTermAccessToken = apiKeyFieldValue;
	        		String apiKey = ""; //No API Key is used.
	        		String apiVersion ="v13.0";
	        		
	        		Page page = null;
	        		ConversationsClient conversationsClient = null;
	        		try {
	        		
	        			 conversationsClient = new ConversationsClient(baseUrl,apiKey);
	        		
	        		
	        		
		        		if (!conversationsClient.conversations().isValid(apiVersion,longTermAccessToken)) {
		        	    	startDownloadButton.setEnabled(true); // Enable the startDownloadButton again
		           		    stopButton.setEnabled(false);
		           		    exportButton.setEnabled(false);
		        	    	
		        	        JOptionPane.showMessageDialog(ScrapeInterfaceNew.this, "API key no valid.", "No Valid Key", JOptionPane.INFORMATION_MESSAGE);
		        	        return null;
		        		}
		        		
	        	        
	        		
		        		//Get Page 1
		        		//Page page = conversationsClient.conversations().getConversations(apiVersion,pageId,longTermAccessToken);
		        		page = conversationsClient.conversations().getConversations(apiVersion,longTermAccessToken);
        		
            	}catch (Exception e) {
    				System.out.println("No hay internet");
    				startDownloadButton.setEnabled(true); // Enable the startDownloadButton again
           		    stopButton.setEnabled(false);
           		    exportButton.setEnabled(false);
        	    	
        	        JOptionPane.showMessageDialog(ScrapeInterfaceNew.this, "Cannot perform the connection with Facebook API Graph. Check the internet access.", "No Internet Access", JOptionPane.INFORMATION_MESSAGE);
        	        return null;

    			}
            	
        		try {

		        		String next = page.getConversations().getPaging().getNext(); //get next from Page (always appear)
		        		
		        				        		
		
		        		boolean ban = true;
		
		        		String admin = conversationsClient.conversations().getAdmin(apiVersion,longTermAccessToken);
		
		        		//Get messages with pagination
		        		Messages m = null;
		
		        			m = conversationsClient.conversations().getMessages(next);
		
		            		
		            		//Here I start the download process
		            		startDownloadButton.setEnabled(false); 
		        	    	stopButton.setEnabled(true);
		        	        exportButton.setEnabled(false);   
            		
		        	        
		        	        
		        	        if (m==null) {
		        	        	//Means that there is no next page, so few messages.
		        	         	for (Conversation c: page.getConversations().getConversationList()) { 
				        			
					        		 if (stopRequested.get()) {
					                        break; // Exit the loop if stopRequested is true
					                    }
					        		 
					        		Conversations conver = getConversationData(c,admin);        			 
					        			  	    	Object[] rowData = new Object[]{
					                                        conver.getConversationGuid(),
					                                        conver.getMaskedFacebookId(),
					                                        conver.getLeadName(),
					                                        conver.getLeadPhoneNumber(),
					                                        conver.getLeadEmail(),
					                                        conver.getLeadCreatedTime(),
					                                        conver.getLeadUpdatedTime(),
					                                        conver.getSnippet(),
					                                        conver.getLastUserWhoSentMessage(),
					                                        conver.getLeadMessages()
					                                };
					        			  	    	 publish(rowData); // publish the new row to be added to the table
					  
					        	}
		        	        	
		        	        }
		        	        
		        	        
			        		while (m!=null && !stopRequested.get()) {	
			
			        				
			        				if (ban == true) {						
								         //Guardar los datos de la página
		 

								        			for (MessageData d : m.getMessageList()) { 

								        				if (stopRequested.get()) {
								                            break; // Exit the loop if stopRequested is true
								                        }
								        				
								        				Conversations conver = getConversationData(d,admin);
								        				
								        				Object[] rowData = new Object[]{
						                                        conver.getConversationGuid(),
						                                        conver.getMaskedFacebookId(),
						                                        conver.getLeadName(),
						                                        conver.getLeadPhoneNumber(),
						                                        conver.getLeadEmail(),
						                                        conver.getLeadCreatedTime(),
						                                        conver.getLeadUpdatedTime(),
						                                        conver.getSnippet(),
						                                        conver.getLastUserWhoSentMessage(),
						                                        conver.getLeadMessages()
						                                };
						        			  	    	 publish(rowData); // publish the new row to be added to the table
								        					
								
								        			}
								
								
								        			System.out.println("done!");
								        										
			        					
			        					ban = false;
			        				}else {
			 
					        			for (MessageData d : m.getMessageList()) { 
					
					        				 if (stopRequested.get()) {
					                             break; // Exit the loop if stopRequested is true
					                         }
					        				 
					        				Conversations conver = getConversationData(d,admin);
					        				
					        				Object[] rowData = new Object[]{
			                                        conver.getConversationGuid(),
			                                        conver.getMaskedFacebookId(),
			                                        conver.getLeadName(),
			                                        conver.getLeadPhoneNumber(),
			                                        conver.getLeadEmail(),
			                                        conver.getLeadCreatedTime(),
			                                        conver.getLeadUpdatedTime(),
			                                        conver.getSnippet(),
			                                        conver.getLastUserWhoSentMessage(),
			                                        conver.getLeadMessages()
			                                };
			        			  	    	 publish(rowData); // publish the new row to be added to the table
					        					
					        			}
			
			
					        			System.out.println("done!");
			        					
			        					
			        					}
			        				
			        				next = m.getPaging().getNext();
			        				
			            			m = conversationsClient.conversations().getMessages(next);
			
			        			
			        		}
                
        		}catch (Exception e) {
    				System.out.println("Hay un error en la petición" + e);
    				startDownloadButton.setEnabled(true); // Enable the startDownloadButton again
           		    stopButton.setEnabled(false);
           		    exportButton.setEnabled(false);
        	    	
        	        JOptionPane.showMessageDialog(ScrapeInterfaceNew.this, "Possible reasons: missing/expired page token or requires permissions.", "Access Token Error", JOptionPane.INFORMATION_MESSAGE);
    			}
                
                return null;
            }

            @Override
            protected void done() {
            	  if (stopRequested.get()) {
                      System.out.println("System was Stopped");
                      startDownloadButton.setEnabled(true); // Enable the startDownloadButton again
                      exportButton.setEnabled(true);
          	        JOptionPane.showMessageDialog(ScrapeInterfaceNew.this, "Extraction Sttoped", "Data Extraction Stoped Successfully.", JOptionPane.INFORMATION_MESSAGE);

                  } else {
                	  if (tableModel.getRowCount()!=0) {
		                      System.out.println("System completed succesfully");
		                      startDownloadButton.setEnabled(true); // Enable the startDownloadButton again
		             		  stopButton.setEnabled(false);
		                      exportButton.setEnabled(true);
		          	          JOptionPane.showMessageDialog(ScrapeInterfaceNew.this, "Extraction Finished", "Data Extraction Ended Successfully.", JOptionPane.INFORMATION_MESSAGE);
                	  }

                  }
            	  
            	  System.out.println("ALWAYS HAPPEN");
               
            }

            @Override
            protected void process(List<Object[]> chunks) {
                // update the table with the new rows
                for (Object[] rowData : chunks) {
                	if (stopRequested.get()) {
                        break; // Exit the loop and stop processing remaining chunks
                    }
                    tableModel.addRow(rowData);
                }
                dataTable.scrollRectToVisible(dataTable.getCellRect(tableModel.getRowCount() - 1, 0, true));
            }
        };
        worker.execute();
    }
    
    
    private static Conversations getConversationData(MessageData d, String admin) {
    	String conversationId= "";
		String messageCount = "";
		//int leadMessageCount= 0;
		String snippet= "";
		String unreadCount= "";
		String updatedTime= "";
		
		
		String leadUpdatedTime= "";
		
		String leadMaskedId = "";
		String leadName = "";
		String leadMessages = "";
		String leadMessagesRecentMonth = "";
		String stockMessages = "";
		String lastUserWhoSentMessage = "";
		String lastConversationsInARowFromStock = ""; //Last conversations in a row from Stock
		
		DateFormat dfShort = new SimpleDateFormat("yyyy-MM-dd");
		
		
		
		//The six variables that need to be acumulated somehow
		List<String> leadList = new ArrayList<String>();
		List<String> leadListRecentMonth = new ArrayList<String>();
		Set<Date> daysSetRecentMonth = new HashSet<Date>();
		Set<Date> daysSet = new HashSet<Date>();
		String leadCreatedTime= "";
		String leadCreatedTimeRecentMonth= "";
		//
		
		
		List<String> stockList = new ArrayList<String>();

		boolean flag = true;
		boolean flagStock = true;
		boolean flagLastUser = true;
    	
		String next = d.getMessages().getPaging().getNext();
		conversationId = d.getId(); //Id
		messageCount = d.getMessageCount(); //
		snippet = d.getSnippet(); //
		unreadCount = d.getUnreadCount(); //
		updatedTime = d.getUpdatedTime(); //


		//Ge lead name from Participants
		if (d.getParticipants().getParsipantList()!=null) {
		    if (d.getParticipants().getParsipantList().size()==2) {	  
		  	    	  if (!d.getParticipants().getParsipantList().get(0).getName().equals(admin)) { //Si el primero no es igual a Stock, entonces pon el primero.
		  	    		  leadName = d.getParticipants().getParsipantList().get(0).getName();
		  	    		  leadMaskedId = d.getParticipants().getParsipantList().get(0).getId();
		  	    	  }else {														
		  	    		  //Si el primero es igual a Stock, entonces pon el segundo.
		  	    		  leadName = d.getParticipants().getParsipantList().get(1).getName();
		  	    		  leadMaskedId= d.getParticipants().getParsipantList().get(1).getId();
		  	    	  }
		    }
		    
		}
	

		if (d.getMessages().getMessageList()!=null) {
		//System.out.println("--2");
		for (MessageData d2: d.getMessages().getMessageList()) {
			  //System.out.println("--3");
			  if(d2.getFrom().getName().equals(admin)) {
				  
					  /**
					   * Stock Conversation Data
					   */
				  		if (flagLastUser) {
				  			lastUserWhoSentMessage = d2.getFrom().getName();
				  			flagLastUser = false;
				  		}
					  stockList.add(d2.getMessage());
					  if (flagStock) {
						  lastConversationsInARowFromStock+= d2.getMessage() + " ";
					  }
					  
			  }else {
				  
					  /**
					   * Lead Conversation Data
					   */
					  flagStock = false;
					  //leadMessages += m.getMessage() + " ";
					  try {
						daysSet.add(dfShort.parse(d2.getCreated_time()));
					  } catch (ParseException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
						System.out.println("Opps there is an error with the date. Esto nunca debe de suceder!.");
					  }
					  leadList.add(d2.getMessage());
					  
					  
					  
					  if (flag) {
						  leadUpdatedTime= d2.getCreated_time(); //This date is here, because the last message is the most recent.
						  flag = false;
					  }
					  if (flagLastUser) {
				  			lastUserWhoSentMessage = d2.getFrom().getName();
				  			flagLastUser = false;
				  	  }
					  //leadMessageCount++;
					  
					  /**
					   * Collect Data only From the Last Month
					   */
					  try {
						  LocalDate leadUpdatedLocalTime = dfShort.parse(leadUpdatedTime).toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
						  LocalDate leadCreatedLocalTime = dfShort.parse(d2.getCreated_time()).toInstant().atZone(ZoneId.systemDefault()).toLocalDate();

						//  Duration duration = Duration.between(dfShort.parse(m.getCreated_time()).toInstant(),dfShort.parse(leadUpdatedTime).toInstant());
						  Duration duration = Duration.between(leadCreatedLocalTime.atStartOfDay(),leadUpdatedLocalTime.atStartOfDay());
						//  System.out.println("Start:" + leadUpdatedLocalTime.toString());
						//  System.out.println("End:" + leadCreatedLocalTime.toString());
						//  System.out.println("Duración:" + duration.compareTo(THIRTY_DAYS));
						 
						  
		  			  if (duration.compareTo(THIRTY_DAYS) < 0) {
		  				  leadCreatedTimeRecentMonth = d2.getCreated_time();
		  				  //System.out.println("Created Time:" + leadCreatedTimeRecentMonth + "-->" + m.getMessage());
		  				  daysSetRecentMonth.add(dfShort.parse(d2.getCreated_time()));
		  				  leadListRecentMonth.add(d2.getMessage());
		  				  
		  			  }else {
		  				 //System.out.println("****************Paso por aqui*********************");
		  				  //Duration is more or exactly thirty days. DO NOTHING!!
		  			  }
					  } catch (ParseException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
							System.out.println("Opps there is an error with the date. Esto nunca debe de suceder!.");
		  		  }
					  
					  leadCreatedTime = d2.getCreated_time();
					  
			  } 
			  
			 
		}
		//leadMessages = getMoreMessages(leadMessages,next);
		leadList = getMoreLeadMessagesList(leadList,next,admin);
		leadListRecentMonth = getMoreLeadMessagesListRecentMonth(leadListRecentMonth,next,admin,leadUpdatedTime);

		stockList = getMoreStockMessagesList(stockList,next,admin);

		daysSet = getMoreLeadDaysSet(daysSet,next,admin);
		daysSetRecentMonth = getMoreLeadDaysSetRecentMonth(daysSetRecentMonth,next,admin,leadUpdatedTime);

		leadCreatedTime = getMoreLeadCreatedTime(leadCreatedTime,next,admin);
		leadCreatedTimeRecentMonth = getMoreLeadCreatedTimeRecentMonth(leadCreatedTimeRecentMonth,next,admin,leadUpdatedTime);

		//OutputData output = getMoreLeadMessagesListComplete(leadList,leadListRecentMonth,daysSet,daysSetRecentMonth,leadCreatedTime,leadCreatedTimeRecentMonth,next,adminUser,leadUpdatedTime);
		}

		Conversations conversationsDAO = new Conversations();
		conversationsDAO.setLeadName(leadName);
		conversationsDAO.setConversationGuid(conversationId);
		conversationsDAO.setMessagesCount(Integer.parseInt(messageCount));
		conversationsDAO.setSnippet(snippet);
		if (unreadCount!=null) {
		conversationsDAO.setUnreadCount(Integer.parseInt(unreadCount));
		}

		conversationsDAO.setLeadInteractions(countAdClicks(stockList));
		//Important Dates

		// DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");

		Date updatedTimeFormated = null;
		Date leadCreatedTimeFormated = null;
		Date leadCreatedTimeRecentMonthFormated = null;
		Date leadUpdatedTimeTimeFormated = null;
		try {
		  	if (!updatedTime.trim().equals("")) {
		  		if (updatedTime!=null) {
		  			updatedTimeFormated = df.parse(updatedTime);
		  			conversationsDAO.setUpdatedTime(updatedTimeFormated);
		  		}
		  	}
		  	
		  	if (!leadCreatedTime.trim().equals("")) {
		  		if(leadCreatedTime!=null) {
					leadCreatedTimeFormated = df.parse(leadCreatedTime);
					conversationsDAO.setLeadCreatedTime(leadCreatedTimeFormated);
		  		}
		  	}
		  	
		  	//New Variable
		  	if (!leadCreatedTimeRecentMonth.trim().equals("")) {
		  		if(leadCreatedTimeRecentMonth!=null) {
		  		leadCreatedTimeRecentMonthFormated = df.parse(leadCreatedTimeRecentMonth);
					conversationsDAO.setLeadCreatedTimeRecentMonth(leadCreatedTimeRecentMonthFormated);
		  		}
		  	}
		  	
		  	
			
		  	if(!leadUpdatedTime.trim().equals("")) {
		  		if(leadUpdatedTime!=null) {
		  			leadUpdatedTimeTimeFormated = df.parse(leadUpdatedTime);
		  			conversationsDAO.setLeadUpdatedTime(leadUpdatedTimeTimeFormated);
		  		}
		  	}

		} catch (ParseException e) {
		e.printStackTrace();
		System.out.println("Opps there is an error with the date. Esto nunca debe de suceder!.");
		}

		/////////////////
		leadMessages = reverse(leadList);
		stockMessages = reverse(stockList);
		leadMessagesRecentMonth = reverse(leadListRecentMonth);

		//System.out.println("-------------------------------------");
		//System.out.println(leadName + "-->" + lastConversationsInARowFromStock);
		//System.out.println("-------------------------------------");
		if (hasQuestion(lastConversationsInARowFromStock)) {
		conversationsDAO.setLeadAbandoned(true);
		}else {
		conversationsDAO.setLeadAbandoned(false);
		}


		if (stockMessages.toLowerCase().contains("https://bit.ly/SucursalesMacropay".toLowerCase()) || stockMessages.toLowerCase().contains("https://macropay.mx/sucursales".toLowerCase())) {
		conversationsDAO.setLeadRedirectedToStore(true);
		}else {
		conversationsDAO.setLeadRedirectedToStore(false); 
		}

		if (stockMessages.toLowerCase().contains(admin.toLowerCase())) {
		conversationsDAO.setMacropayDeliveryAppears(true);
		}else {
		conversationsDAO.setMacropayDeliveryAppears(false);  
		}

		conversationsDAO.setMaskedFacebookId(leadMaskedId);
		conversationsDAO.setLeadMessages(leadMessages);
		conversationsDAO.setLeadMessagesRecentMonth(leadMessagesRecentMonth);
		conversationsDAO.setStockMessages(stockMessages);
		conversationsDAO.setLeadPhoneNumber(parseContent(leadMessages,PHONE));
		conversationsDAO.setLeadEmail(parseContent(leadMessages,EMAIL));
		conversationsDAO.setLastUserWhoSentMessage(lastUserWhoSentMessage);

		if (leadCreatedTimeFormated!=null) {
		if (leadUpdatedTimeTimeFormated!=null) {
			  long duration = getDifferenceDays(leadCreatedTimeFormated, leadUpdatedTimeTimeFormated);
			  conversationsDAO.setLeadConversationDuration(duration);
		}
		}

		//New Variable
		if (leadCreatedTimeRecentMonthFormated!=null) {
		if (leadUpdatedTimeTimeFormated!=null) {
			  long durationRecentMonth = getDifferenceDays(leadCreatedTimeRecentMonthFormated, leadUpdatedTimeTimeFormated);
			  conversationsDAO.setLeadConversationDurationRecentMonth(durationRecentMonth);
		}
		}

		//
		conversationsDAO.setLeadConversationUniqueDays(daysSet.size());
		conversationsDAO.setLeadConversationUniqueDaysRecentMonth(daysSetRecentMonth.size());
		conversationsDAO.setLeadMessagesCount(leadList.size());
		conversationsDAO.setLeadMessagesCountRecentMonth(leadListRecentMonth.size());
		conversationsDAO.setLeadConversationLenght(leadMessages.length());  //Should we compute the lenght with a preprocessing step ? 
		conversationsDAO.setLeadConversationLenghtRecentMonth(leadMessagesRecentMonth.length());
		conversationsDAO.setLeadMessageInterestLevel(getTotalKeywords(leadMessages));
		conversationsDAO.setLeadMessageInterestLevelRecentMonth(getTotalKeywords(leadMessagesRecentMonth));

		//Scores
		double fuzzyScore = 0.0;
		NumberFormat formatter = new DecimalFormat("#0.0");     

		conversationsDAO.setLeadScore(Double.parseDouble(formatter.format(fuzzyScore)));

		System.out.println(conversationsDAO.toString());

    	
    	return conversationsDAO;
    	
    	
    }
        
    private static Conversations getConversationData(Conversation c, String admin) {
		
    	
		String conversationId= "";
		String messageCount = "";
		//int leadMessageCount= 0;
		String snippet= "";
		String unreadCount= "";
		String updatedTime= "";
		
		
		String leadUpdatedTime= "";
		
		String leadMaskedId = "";
		String leadName = "";
		String leadMessages = "";
		String leadMessagesRecentMonth = "";
		String stockMessages = "";
		String lastUserWhoSentMessage = "";
		String lastConversationsInARowFromStock = ""; //Last conversations in a row from Stock
		
		DateFormat dfShort = new SimpleDateFormat("yyyy-MM-dd");
		
		
		
		//The six variables that need to be acumulated somehow
		List<String> leadList = new ArrayList<String>();
		List<String> leadListRecentMonth = new ArrayList<String>();
		Set<Date> daysSetRecentMonth = new HashSet<Date>();
		Set<Date> daysSet = new HashSet<Date>();
		String leadCreatedTime= "";
		String leadCreatedTimeRecentMonth= "";
		//
		
		
		List<String> stockList = new ArrayList<String>();
		String next = "";
		boolean flag = true;
		boolean flagStock = true;
		boolean flagLastUser = true;
		
		
    	  next = c.getMessages().getPaging().getNext();
  	  conversationId = c.getId(); //Id
  	  messageCount = c.getMessageCount(); //
  	  snippet = c.getSnippet(); //
  	  unreadCount = c.getUnreadCount(); //
  	  updatedTime = c.getUpdatedTime(); //
  	  
  	  //Ge lead name from Participants
  	  if (!c.getParticipants().getParsipantList().get(0).getName().equals(admin)) { //Si el primero no es igual a Stock, entonces pon el primero.
  		  leadName = c.getParticipants().getParsipantList().get(0).getName();
  		  leadMaskedId = c.getParticipants().getParsipantList().get(0).getId();
  	  }else {														
  		  leadName = c.getParticipants().getParsipantList().get(1).getName();
  		  leadMaskedId= c.getParticipants().getParsipantList().get(1).getId();
  	  }

  	  
  	  if (c.getMessages().getMessageList()!=null) {
	    	  for (MessageData me: c.getMessages().getMessageList()) {
	    		  if(me.getFrom().getName().equals(admin)) {
	    			  
		    			  /**
		    			   * Stock Conversation Data
		    			   */
	    			  		if (flagLastUser) {
	    			  			lastUserWhoSentMessage = me.getFrom().getName();
	    			  			flagLastUser = false;
	    			  		}
		    			  stockList.add(me.getMessage());
		    			  if (flagStock) {
		    				  lastConversationsInARowFromStock+= me.getMessage() + " ";
		    			  }
		    			  
	    		  }else {
	    			  
		    			  /**
		    			   * Lead Conversation Data
		    			   */
	    				  flagStock = false;
		    			  try {
							daysSet.add(dfShort.parse(me.getCreated_time()));
		    			  } catch (ParseException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
							System.out.println("Opps there is an error with the date. Esto nunca debe de suceder!.");
		    			  }
		    			  leadList.add(me.getMessage());
		    			  
		    			  
		    			  
		    			  if (flag) {
		    				  leadUpdatedTime= me.getCreated_time(); //This date is here, because the last message is the most recent.
		    				  flag = false;
		    			  }
		    			  if (flagLastUser) {
	    			  			lastUserWhoSentMessage = me.getFrom().getName();
	    			  			flagLastUser = false;
	    			  	  }
		    			  //leadMessageCount++;
		    			  
		    			  /**
		    			   * Collect Data only From the Last Month
		    			   */
		    			  try {
		    				  LocalDate leadUpdatedLocalTime = dfShort.parse(leadUpdatedTime).toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
		    				  LocalDate leadCreatedLocalTime = dfShort.parse(me.getCreated_time()).toInstant().atZone(ZoneId.systemDefault()).toLocalDate();

		    				  Duration duration = Duration.between(leadCreatedLocalTime.atStartOfDay(),leadUpdatedLocalTime.atStartOfDay());
		    
		    				 
		    				  
			    			  if (duration.compareTo(THIRTY_DAYS) < 0) {
			    				  leadCreatedTimeRecentMonth = me.getCreated_time();
			    				  daysSetRecentMonth.add(dfShort.parse(me.getCreated_time()));
			    				  leadListRecentMonth.add(me.getMessage());
			    				  
			    			  }else {
			    			  }
		    			  } catch (ParseException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
								System.out.println("Opps there is an error with the date. Esto nunca debe de suceder!.");
			    		  }
		    			  
		    			  leadCreatedTime = me.getCreated_time();
		    			  
	    		  } 
	    		  
	    		 
	    	  }
	    	  leadList = getMoreLeadMessagesList(leadList,next,admin);
	    	  leadListRecentMonth = getMoreLeadMessagesListRecentMonth(leadListRecentMonth,next,admin,leadUpdatedTime);
	    	  
	    	  stockList = getMoreStockMessagesList(stockList,next,admin);
	    	
	    	  daysSet = getMoreLeadDaysSet(daysSet,next,admin);
	    	  daysSetRecentMonth = getMoreLeadDaysSetRecentMonth(daysSetRecentMonth,next,admin,leadUpdatedTime);
	    	  
	    	  leadCreatedTime = getMoreLeadCreatedTime(leadCreatedTime,next,admin);
	    	  leadCreatedTimeRecentMonth = getMoreLeadCreatedTimeRecentMonth(leadCreatedTimeRecentMonth,next,admin,leadUpdatedTime);
	    	  
  	  }
  	  
  	  Conversations conversationsDAO = new Conversations();
  	  conversationsDAO.setLeadName(leadName);
  	  conversationsDAO.setConversationGuid(conversationId);
  	  conversationsDAO.setMessagesCount(Integer.parseInt(messageCount));
  	  conversationsDAO.setSnippet(snippet);
  	  conversationsDAO.setUnreadCount(Integer.parseInt(unreadCount));
  	  
  
  	  conversationsDAO.setLeadInteractions(countAdClicks(stockList));
  	  //Important Dates
  	  
  	  DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");

  	  Date updatedTimeFormated = null;
  	  Date leadCreatedTimeFormated = null;
  	  Date leadCreatedTimeRecentMonthFormated = null;
  	  Date leadUpdatedTimeTimeFormated = null;
		  try {
			  	if (!updatedTime.trim().equals("")) {
			  		if (updatedTime!=null) {
			  			updatedTimeFormated = df.parse(updatedTime);
			  			conversationsDAO.setUpdatedTime(updatedTimeFormated);
			  		}
			  	}
			  	
			  	if (!leadCreatedTime.trim().equals("")) {
			  		if(leadCreatedTime!=null) {
						leadCreatedTimeFormated = df.parse(leadCreatedTime);
						conversationsDAO.setLeadCreatedTime(leadCreatedTimeFormated);
			  		}
			  	}
			  	
			  	//New Variable
			  	if (!leadCreatedTimeRecentMonth.trim().equals("")) {
			  		if(leadCreatedTimeRecentMonth!=null) {
			  		leadCreatedTimeRecentMonthFormated = df.parse(leadCreatedTimeRecentMonth);
						conversationsDAO.setLeadCreatedTimeRecentMonth(leadCreatedTimeRecentMonthFormated);
			  		}
			  	}
			  	
			  	
				
			  	if(!leadUpdatedTime.trim().equals("")) {
			  		if(leadUpdatedTime!=null) {
			  			leadUpdatedTimeTimeFormated = df.parse(leadUpdatedTime);
			  			conversationsDAO.setLeadUpdatedTime(leadUpdatedTimeTimeFormated);
			  		}
			  	}
		  
		  } catch (ParseException e) {
			e.printStackTrace();
			System.out.println("Opps there is an error with the date. Esto nunca debe de suceder!.");
		  }
		  
  	  /////////////////
		  leadMessages = reverse(leadList);
		  stockMessages = reverse(stockList);
		  leadMessagesRecentMonth = reverse(leadListRecentMonth);
		  
		//System.out.println("-------------------------------------");
		  //System.out.println(leadName + "-->" + lastConversationsInARowFromStock);
		//System.out.println("-------------------------------------");
		  if (hasQuestion(lastConversationsInARowFromStock)) {
			conversationsDAO.setLeadAbandoned(true);
		  }else {
			conversationsDAO.setLeadAbandoned(false);
		  }
		  
		if (stockMessages.toLowerCase().contains("https://bit.ly/SucursalesMacropay".toLowerCase()) || stockMessages.toLowerCase().contains("https://macropay.mx/sucursales".toLowerCase())) {
			conversationsDAO.setLeadRedirectedToStore(true);
		  }else {
			conversationsDAO.setLeadRedirectedToStore(false); 
		  }
		  
		  if (stockMessages.toLowerCase().contains(admin.toLowerCase())) {
			conversationsDAO.setMacropayDeliveryAppears(true);
		  }else {
			conversationsDAO.setMacropayDeliveryAppears(false);  
		  }
		  
  	  conversationsDAO.setMaskedFacebookId(leadMaskedId);
  	  conversationsDAO.setLeadMessages(leadMessages);
  	  conversationsDAO.setLeadMessagesRecentMonth(leadMessagesRecentMonth);
  	  conversationsDAO.setStockMessages(stockMessages);
  	  conversationsDAO.setLeadPhoneNumber(parseContent(leadMessages,PHONE));
  	  conversationsDAO.setLeadEmail(parseContent(leadMessages,EMAIL));
  	  conversationsDAO.setLastUserWhoSentMessage(lastUserWhoSentMessage);
  	  
  	  if (leadCreatedTimeFormated!=null) {
  		  if (leadUpdatedTimeTimeFormated!=null) {
  			  long duration = getDifferenceDays(leadCreatedTimeFormated, leadUpdatedTimeTimeFormated);
  			  conversationsDAO.setLeadConversationDuration(duration);
  		  }
  	  }
  	  
  	  //New Variable
  	  if (leadCreatedTimeRecentMonthFormated!=null) {
  		  if (leadUpdatedTimeTimeFormated!=null) {
  			  long durationRecentMonth = getDifferenceDays(leadCreatedTimeRecentMonthFormated, leadUpdatedTimeTimeFormated);
  			  conversationsDAO.setLeadConversationDurationRecentMonth(durationRecentMonth);
  		  }
  	  }
  	  
  	  //
  	  conversationsDAO.setLeadConversationUniqueDays(daysSet.size());
  	  conversationsDAO.setLeadConversationUniqueDaysRecentMonth(daysSetRecentMonth.size());
  	  conversationsDAO.setLeadMessagesCount(leadList.size());
  	  conversationsDAO.setLeadMessagesCountRecentMonth(leadListRecentMonth.size());
  	  conversationsDAO.setLeadConversationLenght(leadMessages.length());  //Should we compute the lenght with a preprocessing step ? 
  	  conversationsDAO.setLeadConversationLenghtRecentMonth(leadMessagesRecentMonth.length());
  	  conversationsDAO.setLeadMessageInterestLevel(getTotalKeywords(leadMessages));
  	  conversationsDAO.setLeadMessageInterestLevelRecentMonth(getTotalKeywords(leadMessagesRecentMonth));

  	  //Scores
  	  double fuzzyScore = 0.0;
  	  NumberFormat formatter = new DecimalFormat("#0.0");     

  	  conversationsDAO.setLeadScore(Double.parseDouble(formatter.format(fuzzyScore)));
  	  
    	  System.out.println(conversationsDAO.toString());
    	
    	return conversationsDAO;

    }



/**
* Count Positive Interactions
* @param stockList
* @return
*/
private static int countAdClicks(List<String> stockList) {

int result = 0;

for (String s: stockList) {
if (s.contains("respondió un anuncio") || s.contains("respondió tu mensaje de bienvenida automático") || s.contains("Estás respondiendo al comentario de un usuario en una publicación de tu página")) {
result++;
}
}

return result;
}



private static boolean hasQuestion(String lastConversationsInARowFromStock) {
//boolean result = false;
if (lastConversationsInARowFromStock.contains("¿")) {
if (lastConversationsInARowFromStock.contains("?")) {

return true;
}
}
return false;
}


public static long getDifferenceDays(Date d1, Date d2) {
long diff = d2.getTime() - d1.getTime();
return TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS);
}



/**
public static long getDifferenceDays(Date d1, Date d2) {

LocalDate leadUpdatedLocalTime = d1.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
LocalDate leadCreatedLocalTime = d2.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();

return Duration.between(leadCreatedLocalTime.atStartOfDay(),leadUpdatedLocalTime.atStartOfDay()).toDays();
}
*/


private static String getMessagesUntilPhone(String leadMessages) {
String result = "";
if (leadMessages!=null) {
if (!leadMessages.equals("")) {
if (!leadMessages.matches("^[\\s]+$")) {				 
for (String s : leadMessages.split(" ")) {
if (parseContentFind(s,PHONE)) { //Si encuentro el teléfono en el string , haz un break
break;
}		 
result+= s + " ";
}
return result.trim();
}
}
}

return result;
}


private static int getTotalKeywords(String leadMessages) {

int result = 0;
if (leadMessages!=null) {
if (!leadMessages.equals("")) {
if (!leadMessages.matches("^[\\s]+$")) {			
for (String s: interestKeywordsSet) {
if (unaccent(leadMessages.toLowerCase()).contains(s)) {
	result++;
}
}
}
}
}

return result; 
}



public static String unaccent(String src) {
return Normalizer
.normalize(src, Normalizer.Form.NFD)
.replaceAll("[^\\p{ASCII}]", "");
}


private static String reverse(List<String> leadMessagesList) {
String result = "";
if (leadMessagesList!=null) {
if (leadMessagesList.size()!=0) {
ListIterator li = leadMessagesList.listIterator(leadMessagesList.size());
while(li.hasPrevious()) {
result += li.previous() + " ";
}
return result.trim();
}
}
return result;
}




private static List<String> getMoreLeadMessagesList(List<String> leadMessagesList, String next,String admin) {
Messages messages = null; 

if (next !=null) {
ConversationsClient conversationsClient = new ConversationsClient("","");
messages = conversationsClient.conversations().getMessages(next);
//System.out.println("--4");
if (messages!=null) {
if (messages.getMessageList()!=null) {
//System.out.println("--5");
for (MessageData m: messages.getMessageList()) {
//System.out.println("--6");
if (m.getFrom().getName().equals(admin)) {
//Do nothing
}else {
leadMessagesList.add(m.getMessage());
}
}
}
}

if (messages!=null) {
if (messages.getPaging()!=null) {
next = messages.getPaging().getNext();
}else {
next = null;
}
}else {
next = null;
}

leadMessagesList = getMoreLeadMessagesList(leadMessagesList,next,admin);
}
return leadMessagesList;
}




private static Set<Date> getMoreLeadDaysSetRecentMonth(Set<Date> daysSetRecentMonth, String next,String admin,String leadUpdatedTime ) {
Messages messages = null; 
DateFormat dfShort = new SimpleDateFormat("yyyy-MM-dd");

if (next !=null) {
ConversationsClient conversationsClient = new ConversationsClient("","");
messages = conversationsClient.conversations().getMessages(next);
if (messages!=null) {
if (messages.getMessageList()!=null) {
for (MessageData m: messages.getMessageList()) {
if (m.getFrom().getName().equals(admin)) {
//Do nothing
}else {

/**
   * Collect Data only From the Last Month
   */
  try {
	  LocalDate leadUpdatedLocalTime = dfShort.parse(leadUpdatedTime).toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
	  LocalDate leadCreatedLocalTime = dfShort.parse(m.getCreated_time()).toInstant().atZone(ZoneId.systemDefault()).toLocalDate();

	  Duration duration = Duration.between(leadCreatedLocalTime.atStartOfDay(),leadUpdatedLocalTime.atStartOfDay());

	  if (duration.compareTo(THIRTY_DAYS) < 0) {
		  daysSetRecentMonth.add(dfShort.parse(m.getCreated_time()));
		
	  }else { 
			  //Duration is more or exactly thirty days. DO NOTHING!!
		  }
	  } catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.out.println("Opps there is an error with the date. Esto nunca debe de suceder!.");
	  }			  
}
}
}
}

if (messages!=null) {
if (messages.getPaging()!=null) {
next = messages.getPaging().getNext();
}else {
next = null;
}
}else {
next = null;
}

daysSetRecentMonth = getMoreLeadDaysSetRecentMonth(daysSetRecentMonth,next,admin,leadUpdatedTime);
}
return daysSetRecentMonth;
}




private static List<String> getMoreLeadMessagesListRecentMonth(List<String> leadListRecentMonth, String next,String admin,String leadUpdatedTime ) {
Messages messages = null; 
DateFormat dfShort = new SimpleDateFormat("yyyy-MM-dd");

if (next !=null) {
ConversationsClient conversationsClient = new ConversationsClient("","");
messages = conversationsClient.conversations().getMessages(next);
//System.out.println("--4");
if (messages!=null) {
if (messages.getMessageList()!=null) {
//System.out.println("--5");
for (MessageData m: messages.getMessageList()) {
//System.out.println("--6");
if (m.getFrom().getName().equals(admin)) {
//Do nothing
}else {

/**
   * Collect Data only From the Last Month
   */
  try {
	  LocalDate leadUpdatedLocalTime = dfShort.parse(leadUpdatedTime).toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
	  LocalDate leadCreatedLocalTime = dfShort.parse(m.getCreated_time()).toInstant().atZone(ZoneId.systemDefault()).toLocalDate();

	  Duration duration = Duration.between(leadCreatedLocalTime.atStartOfDay(),leadUpdatedLocalTime.atStartOfDay());

	  if (duration.compareTo(THIRTY_DAYS) < 0) {
		  leadListRecentMonth.add(m.getMessage());
		
	  }else { 
			  //Duration is more or exactly thirty days. DO NOTHING!!
		  }
	  } catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.out.println("Opps there is an error with the date. Esto nunca debe de suceder!.");
	  }			  
}
}
}
}

if (messages!=null) {
if (messages.getPaging()!=null) {
next = messages.getPaging().getNext();
}else {
next = null;
}
}else {
next = null;
}

leadListRecentMonth = getMoreLeadMessagesListRecentMonth(leadListRecentMonth,next,admin,leadUpdatedTime);
}
return leadListRecentMonth;
}




private static String getMoreLeadCreatedTimeRecentMonth(String leadCreatedTimeRecentMonth, String next,String admin,String leadUpdatedTime ) {
Messages messages = null; 
DateFormat dfShort = new SimpleDateFormat("yyyy-MM-dd");

if (next !=null) {
ConversationsClient conversationsClient = new ConversationsClient("","");
messages = conversationsClient.conversations().getMessages(next);
if (messages!=null) {
if (messages.getMessageList()!=null) {
for (MessageData m: messages.getMessageList()) {
if (m.getFrom().getName().equals(admin)) {
//Do nothing
}else {

/**
   * Collect Data only From the Last Month
   */
  try {
	  LocalDate leadUpdatedLocalTime = dfShort.parse(leadUpdatedTime).toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
	  LocalDate leadCreatedLocalTime = dfShort.parse(m.getCreated_time()).toInstant().atZone(ZoneId.systemDefault()).toLocalDate();

	  Duration duration = Duration.between(leadCreatedLocalTime.atStartOfDay(),leadUpdatedLocalTime.atStartOfDay());

	  if (duration.compareTo(THIRTY_DAYS) < 0) {
		
		  leadCreatedTimeRecentMonth = m.getCreated_time();
		  //System.out.println("Si estoy entrando ******************************* Chingao" + leadCreatedTimeRecentMonth);
	  }else { 
			  //Duration is more or exactly thirty days. DO NOTHING!!
		  }
	  } catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.out.println("Opps there is an error with the date. Esto nunca debe de suceder!.");
	  }			  
}
}
}
}

if (messages!=null) {
if (messages.getPaging()!=null) {
next = messages.getPaging().getNext();
}else {
next = null;
}
}else {
next = null;
}

leadCreatedTimeRecentMonth = getMoreLeadCreatedTimeRecentMonth(leadCreatedTimeRecentMonth,next,admin,leadUpdatedTime);
}
return leadCreatedTimeRecentMonth;
}




private static String getMoreLeadCreatedTime(String createdTime, String next,String admin) {
Messages messages = null; 

if (next !=null) {
ConversationsClient conversationsClient = new ConversationsClient("","");
messages = conversationsClient.conversations().getMessages(next);
if (messages!=null) {
if (messages.getMessageList()!=null) {
for (MessageData m: messages.getMessageList()) {
if (m.getFrom().getName().equals(admin)) {
//Do nothing
}else {
createdTime = m.getCreated_time();
}
}
}
}

if (messages!=null) {
if (messages.getPaging()!=null) {
next = messages.getPaging().getNext();
}else {
next = null;
}
}else {
next = null;
}

createdTime = getMoreLeadCreatedTime(createdTime,next,admin);
}
return createdTime;
}





private static Set<Date> getMoreLeadDaysSet(Set<Date> daysSet, String next,String admin) {
Messages messages = null; 
DateFormat dfShort = new SimpleDateFormat("yyyy-MM-dd");
if (next !=null) {
ConversationsClient conversationsClient = new ConversationsClient("","");
messages = conversationsClient.conversations().getMessages(next);
//System.out.println("--4");
if (messages!=null) {
if (messages.getMessageList()!=null) {
//System.out.println("--5");
for (MessageData m: messages.getMessageList()) {
//System.out.println("--6");
if (m.getFrom().getName().equals(admin)) {
//Do nothing
}else {

 try {
		daysSet.add(dfShort.parse(m.getCreated_time()));
	  } catch (ParseException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
		System.out.println("Opps there is an error with the date. Esto nunca debe de suceder!.");
	  }
 
}
}
}
}

if (messages!=null) {
if (messages.getPaging()!=null) {
next = messages.getPaging().getNext();
}else {
next = null;
}
}else {
next = null;
}

daysSet = getMoreLeadDaysSet(daysSet,next,admin);
}
return daysSet;
}





private static List<String> getMoreLeadMessagesListRecentMonth(List<String> leadMessagesList, String next,String admin) {
Messages messages = null; 

if (next !=null) {
ConversationsClient conversationsClient = new ConversationsClient("","");
messages = conversationsClient.conversations().getMessages(next);
//System.out.println("--4");
if (messages!=null) {
if (messages.getMessageList()!=null) {
//System.out.println("--5");
for (MessageData m: messages.getMessageList()) {
//System.out.println("--6");
if (m.getFrom().getName().equals(admin)) {
//Do nothing
}else {
leadMessagesList.add(m.getMessage());
}
}
}
}

if (messages!=null) {
if (messages.getPaging()!=null) {
next = messages.getPaging().getNext();
}else {
next = null;
}
}else {
next = null;
}

leadMessagesList = getMoreLeadMessagesList(leadMessagesList,next,admin);
}
return leadMessagesList;
}




private static List<String> getMoreStockMessagesList(List<String> stockMessagesList, String next,String admin) {
Messages messages = null; 

if (next !=null) {
ConversationsClient conversationsClient = new ConversationsClient("","");
messages = conversationsClient.conversations().getMessages(next);
//System.out.println("--4");
if (messages!=null) {
if (messages.getMessageList()!=null) {
//System.out.println("--5");
for (MessageData m: messages.getMessageList()) {
//System.out.println("--6");
if (m.getFrom().getName().equals(admin)) {
stockMessagesList.add(m.getMessage());
}else {
//Do nothing
}
}
}
}
if (messages!=null) {
if (messages.getPaging()!=null) {
next = messages.getPaging().getNext();
}else {
next = null;
}
}else {
next = null;
}
stockMessagesList = getMoreStockMessagesList(stockMessagesList,next,admin);
}
return stockMessagesList;
}



public static String parseContent(String urlContent, String regex) {

// this pattern is specific for the given website
Pattern pattProduct = Pattern.compile(regex);

// create a matcher from the given pattern for the URL content
Matcher matcher = pattProduct.matcher(urlContent);

// find the first pattern match and return null if nothing has been found
if (!matcher.find()){
return null;
}

// check if there are multiple matches and raise warning
if (matcher.groupCount() > 1)
System.err.println("Multiple matches found");

// return the value in between (.*?)
return matcher.group();
}


public static boolean parseContentFind(String urlContent, String regex) {

// this pattern is specific for the given website
Pattern pattProduct = Pattern.compile(regex);

// create a matcher from the given pattern for the URL content
Matcher matcher = pattProduct.matcher(urlContent);

// find the first pattern match and return null if nothing has been found
if (!matcher.find()){
return false;
}

// check if there are multiple matches and raise warning
if (matcher.groupCount() > 1)
System.err.println("Multiple matches found");

// return the value in between (.*?)
return true;
}
    
    /////////////////////////////////////////////////////////


private void exportToExcel() {
    // create new thread for file export
    Thread exportThread = new Thread(new Runnable() {
        public void run() {
            // create new Excel workbook and sheet
            Workbook workbook = new XSSFWorkbook();
            Sheet sheet = workbook.createSheet("Data");

            // add header row
            Row headerRow = sheet.createRow(0);
            String[] headers = new String[]{"ConverId", "FBId", "LeadName", "Phone", "Email", "Created", "LastUpd", "Snippet", "LastUser", "LeadMsg"};
            for (int i = 0; i < headers.length; i++) {
                Cell cell = headerRow.createCell(i);
                cell.setCellValue(headers[i]);
            }

            // add table data to sheet
            ArrayList<Object[]> data = new ArrayList<Object[]>();

            for (int i = 0; i < tableModel.getRowCount(); i++) {
                Object[] row = new Object[tableModel.getColumnCount()];
                for (int j = 0; j < tableModel.getColumnCount(); j++) {
                    row[j] = tableModel.getValueAt(i, j);
                }
                data.add(row);
            }

            System.out.println("size:" + data.size());

            if (data.size() == 0) {
                // Show notification that there is no data in the table
                JOptionPane.showMessageDialog(ScrapeInterfaceNew.this, "No data in the table.", "No Data", JOptionPane.INFORMATION_MESSAGE);
                return;
            }

            for (int i = 0; i < data.size(); i++) {
                Row row = sheet.createRow(i + 1); // start from row 1 to leave space for the header row
                Object[] rowData = data.get(i);
                for (int j = 0; j < rowData.length; j++) {
                    Cell cell = row.createCell(j);
                    if (rowData[j] != null) {
                        System.out.println("rowData[j].toString():" + rowData[j].toString());
                        cell.setCellValue(rowData[j].toString());
                    }
                }
            }

            System.out.println("pasa rapido");
            // write workbook to file
            try {
                JFileChooser fileChooser = new JFileChooser();
                fileChooser.setCurrentDirectory(new File(System.getProperty("user.home")));
                int userSelection = fileChooser.showSaveDialog(ScrapeInterfaceNew.this);

                if (userSelection == JFileChooser.APPROVE_OPTION) {
                    File fileToSave = fileChooser.getSelectedFile();
                    String filePath = fileToSave.getAbsolutePath();
                    FileOutputStream fileOut = new FileOutputStream(filePath + ".xlsx");
                    workbook.write(fileOut);
                    fileOut.close();
                    System.out.println("Data exported to " + filePath + ".xlsx");
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    });

    // start the export thread
    exportThread.start();
}




public static void main(String[] args) {
    SwingUtilities.invokeLater(new Runnable() {
        public void run() {
            ScrapeInterfaceNew app = new ScrapeInterfaceNew();
            app.setVisible(true);
        }
    });
}
}