/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package contactextraction_phase1;

import PojoClasses.ConfigProperties;
import static contactextraction_phase1.CoreNLP.props;
import static contactextraction_phase1.MethodRepository.File_Reader;
import static contactextraction_phase1.NER.init;

import edu.stanford.nlp.pipeline.StanfordCoreNLP;
import java.lang.reflect.Field;
import java.nio.charset.Charset;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.Properties;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

/**
 *
 * @author SASIKUMAR
 */
public class ContactExtraction_Phase1 {

    public static final Logger log = Logger.getLogger(ContactExtraction_Phase1.class.getName());
    public static ConfigProperties configProperties;
    public static ArrayList<String> prefix = new ArrayList();
    public static ArrayList<String> suffix = new ArrayList();
    public static ArrayList<String> FinalArrayList = new ArrayList();
    public static ArrayList<String> EmailRegex = new ArrayList();
    public static HashSet<String> EmailExtracted = new HashSet<>();
    public static HashSet<String> BussinessEmail = new HashSet<>();
    public static HashSet<String> PersonNames = new HashSet<>();
    public static String serializedClassifier = "classifiers/english.all.3class.distsim.crf.ser.gz";
    /**
     * @param args the command line arguments
     */
    public static String URLID = "";
    public static String URL_Source = "";
    public static String DURL = "";
    public static String Normalized_DURL = "";
    public static String PageSource = "";
    public static String Language = "";
    public static String HTMLInputID = "";
    public static int id = 0;
static String startTime = "";
static SimpleDateFormat sdf = new SimpleDateFormat("MMddyyyyHHmmssz");
static long startTime1;
    public static void set_charset(String EncodingCharset) {
        try {
            try {
                Charset.forName(EncodingCharset);
            } catch (Exception e) {
                log.error(e.getLocalizedMessage());
                System.exit(1);
            }

            System.setProperty("file.encoding", EncodingCharset);
            Field charset = Charset.class.getDeclaredField("defaultCharset");
            charset.setAccessible(true);
            charset.set(null, null);
        } catch (Exception e) {
            log.error(" - Exception : ", e);

        }
    }

    public static void main(String[] args) throws Exception {
        PropertyConfigurator.configure("log4j.properties");
        startTime1 = System.currentTimeMillis();
        try {
            MethodRepository.loadprops();
            DBConnectivity.DBConnection();
            DBConnectivity.CreateOutputtable();
            prefix = File_Reader("Dependencies/Prefix.txt");
            suffix = File_Reader("Dependencies/Suffix.txt");
            EmailRegex = File_Reader("Dependencies/MailRegex.txt");
            set_charset(configProperties.getCharset());
        } catch (Exception e) {
            log.error(" - DB Connection Problem/Configuration File Reading Problem" + e);
            System.exit(0);
        }

        if (configProperties.getNER_NLP_Module().equalsIgnoreCase("true")) {
            try {
                // NER Initialization           
                init(serializedClassifier);

                // CoreNLP Initialization       
                props = new Properties();
//            props.setProperty("annotators", "tokenize, ssplit, pos, lemma, ner, parse, dcoref, sentiment");
                props.put("annotators", "tokenize, ssplit, pos, lemma, ner");

                CoreNLP.pipeline = new StanfordCoreNLP(props);

                log.info(" - Loading Jazzy Spell Checker dependencies...");
                JazzySpellChecker.dictFile = "Dependencies/english.0";
                JazzySpellChecker.phonetFile = "Dependencies/phonet.en";

            } catch (Exception r) {
                log.error(" - NER/CoreNLP Initialization Problem " + r);
                System.exit(0);
            }
        }

        Statement stmt = DBConnectivity.con.createStatement();
        
        
         try {
                PreparedStatement st1 = DBConnectivity.con.prepareStatement("BEGIN TRY BEGIN TRANSACTION SET DEADLOCK_PRIORITY ? COMMIT TRANSACTION END TRY BEGIN CATCH IF XACT_STATE() <> 0 ROLLBACK TRANSACTION END CATCH");
                st1.setInt(1, Integer.parseInt(configProperties.getDeadlockpriority()));
                st1.executeUpdate();
                st1.clearBatch();
                st1.close();
            } catch (Exception var10) {
                log.info("Error While Setting Deadlock Priority");
//                log.info(createLogJson("Bot Initiated", 0, "Error While Setting Deadlock Priority " + var10.getMessage()));
//                log.error(appendLog(""), var10);
                System.exit(0);
            }


        System.out.println("select id,InputID,URL_ID,LINK,DOMAINURL,PAGE_SOURCE,LANGUAGE  from " + configProperties.getInput_Table() + " where status in (" + configProperties.getStatus() + ") and id between " + configProperties.getStart() + " and " + configProperties.getEnd() + " order by id");
        ResultSet rs = stmt.executeQuery("select id,InputID,URL_ID,LINK,DOMAINURL,PAGE_SOURCE,LANGUAGE  from " + configProperties.getInput_Table() + " where status in (" + configProperties.getStatus() + ") and id between " + configProperties.getStart() + " and " + configProperties.getEnd() + " order by id");
        while (rs.next()) {
           
            startTime = sdf.format(new Date());
            id = rs.getInt(1);
            System.out.println("Processing id " + id);
            HTMLInputID = rs.getString(2);
            URLID = rs.getString(3);
            URL_Source = rs.getString(4);
            System.out.println("Processing URL " + URL_Source);
            DURL = rs.getString(5);
            Normalized_DURL=MethodRepository.CleanDomain(DURL);
            PageSource = rs.getString(6);
            Language = rs.getString(7);
            
            if ((URL_Source.toLowerCase().endsWith(".3g2")) || (URL_Source.toLowerCase().endsWith(".3gp"))
                    || (URL_Source.toLowerCase().endsWith(".ai")) || (URL_Source.toLowerCase().endsWith(".aif"))
                    || (URL_Source.toLowerCase().endsWith(".apk"))
                    || (URL_Source.toLowerCase().endsWith(".arj"))
                    || (URL_Source.toLowerCase().endsWith(".avi"))
                    || (URL_Source.toLowerCase().endsWith(".bak"))
                    || (URL_Source.toLowerCase().endsWith(".bat"))
                    || (URL_Source.toLowerCase().endsWith(".bin"))
                    || (URL_Source.toLowerCase().endsWith(".bmp"))
                    || (URL_Source.toLowerCase().endsWith(".cab"))
                    || (URL_Source.toLowerCase().endsWith(".cda"))
                    || (URL_Source.toLowerCase().endsWith(".cer"))
                    || (URL_Source.toLowerCase().endsWith(".cfg"))
                    || (URL_Source.toLowerCase().endsWith(".class"))
                    || (URL_Source.toLowerCase().endsWith(".cpl"))
                    || (URL_Source.toLowerCase().endsWith(".cpp")) || (URL_Source.toLowerCase().endsWith(".cs"))
                    || (URL_Source.toLowerCase().endsWith(".css"))
                    || (URL_Source.toLowerCase().endsWith(".csv"))
                    || (URL_Source.toLowerCase().endsWith(".cur"))
                    || (URL_Source.toLowerCase().endsWith(".dat")) || (URL_Source.toLowerCase().endsWith(".db"))
                    || (URL_Source.toLowerCase().endsWith(".dbf"))
                    || (URL_Source.toLowerCase().endsWith(".deb"))
                    || (URL_Source.toLowerCase().endsWith(".dll"))
                    || (URL_Source.toLowerCase().endsWith(".dmg"))
                    || (URL_Source.toLowerCase().endsWith(".dmp"))
                    || (URL_Source.toLowerCase().endsWith(".doc"))
                    || (URL_Source.toLowerCase().endsWith(".docx"))
                    || (URL_Source.toLowerCase().endsWith(".drv"))
                    || (URL_Source.toLowerCase().endsWith(".exe"))
                    || (URL_Source.toLowerCase().endsWith(".f4v"))
                    || (URL_Source.toLowerCase().endsWith(".fla"))
                    || (URL_Source.toLowerCase().endsWith(".flr"))
                    || (URL_Source.toLowerCase().endsWith(".flv"))
                    || (URL_Source.toLowerCase().endsWith(".fnt"))
                    || (URL_Source.toLowerCase().endsWith(".fon"))
                    || (URL_Source.toLowerCase().endsWith(".gadget"))
                    || (URL_Source.toLowerCase().endsWith(".gif"))
                    || (URL_Source.toLowerCase().endsWith(".h264"))
                    || (URL_Source.toLowerCase().endsWith(".icns"))
                    || (URL_Source.toLowerCase().endsWith(".ico"))
                    || (URL_Source.toLowerCase().endsWith(".ini"))
                    || (URL_Source.toLowerCase().endsWith(".iso"))
                    || (URL_Source.toLowerCase().endsWith(".jar"))
                    || (URL_Source.toLowerCase().endsWith(".java"))
                    || (URL_Source.toLowerCase().endsWith(".jpeg"))
                    || (URL_Source.toLowerCase().endsWith(".jpg"))
                    || (URL_Source.toLowerCase().endsWith(".key"))
                    || (URL_Source.toLowerCase().endsWith(".lnk"))
                    || (URL_Source.toLowerCase().endsWith(".log"))
                    || (URL_Source.toLowerCase().endsWith(".m4v"))
                    || (URL_Source.toLowerCase().endsWith(".mdb"))
                    || (URL_Source.toLowerCase().endsWith(".mid"))
                    || (URL_Source.toLowerCase().endsWith(".midi"))
                    || (URL_Source.toLowerCase().endsWith(".mkv"))
                    || (URL_Source.toLowerCase().endsWith(".mng"))
                    || (URL_Source.toLowerCase().endsWith(".mov"))
                    || (URL_Source.toLowerCase().endsWith(".mp2"))
                    || (URL_Source.toLowerCase().endsWith(".mp3"))
                    || (URL_Source.toLowerCase().endsWith(".mp4"))
                    || (URL_Source.toLowerCase().endsWith(".mpa"))
                    || (URL_Source.toLowerCase().endsWith(".mpe"))
                    || (URL_Source.toLowerCase().endsWith(".mpeg"))
                    || (URL_Source.toLowerCase().endsWith(".mpg"))
                    || (URL_Source.toLowerCase().endsWith(".msi"))
                    || (URL_Source.toLowerCase().endsWith(".odp"))
                    || (URL_Source.toLowerCase().endsWith(".ods"))
                    || (URL_Source.toLowerCase().endsWith(".odt"))
                    || (URL_Source.toLowerCase().endsWith(".ogg"))
                    || (URL_Source.toLowerCase().endsWith(".otf"))
                    || (URL_Source.toLowerCase().endsWith(".part"))
                    || (URL_Source.toLowerCase().endsWith(".pdf"))
                    || (URL_Source.toLowerCase().endsWith(".pkg")) || (URL_Source.toLowerCase().endsWith(".pl"))
                    || (URL_Source.toLowerCase().endsWith(".png"))
                    || (URL_Source.toLowerCase().endsWith(".pps"))
                    || (URL_Source.toLowerCase().endsWith(".ppt"))
                    || (URL_Source.toLowerCase().endsWith(".pptx"))
                    || (URL_Source.toLowerCase().endsWith(".ps")) || (URL_Source.toLowerCase().endsWith(".psd"))
                    || (URL_Source.toLowerCase().endsWith(".py")) || (URL_Source.toLowerCase().endsWith(".rar"))
                    || (URL_Source.toLowerCase().endsWith(".rm")) || (URL_Source.toLowerCase().endsWith(".rpm"))
                    || (URL_Source.toLowerCase().endsWith(".rss"))
                    || (URL_Source.toLowerCase().endsWith(".rtf"))
                    || (URL_Source.toLowerCase().endsWith(".sav")) || (URL_Source.toLowerCase().endsWith(".sh"))
                    || (URL_Source.toLowerCase().endsWith(".sql"))
                    || (URL_Source.toLowerCase().endsWith(".svg"))
                    || (URL_Source.toLowerCase().endsWith(".swf"))
                    || (URL_Source.toLowerCase().endsWith(".swift"))
                    || (URL_Source.toLowerCase().endsWith(".sys"))
                    || (URL_Source.toLowerCase().endsWith(".tar"))
                    || (URL_Source.toLowerCase().endsWith(".tar.gz"))
                    || (URL_Source.toLowerCase().endsWith(".tex"))
                    || (URL_Source.toLowerCase().endsWith(".tif"))
                    || (URL_Source.toLowerCase().endsWith(".tiff"))
                    || (URL_Source.toLowerCase().endsWith(".tmp"))
                    || (URL_Source.toLowerCase().endsWith(".toast"))
                    || (URL_Source.toLowerCase().endsWith(".ttf"))
                    || (URL_Source.toLowerCase().endsWith(".txt")) || (URL_Source.toLowerCase().endsWith(".vb"))
                    || (URL_Source.toLowerCase().endsWith(".vcd"))
                    || (URL_Source.toLowerCase().endsWith(".vob"))
                    || (URL_Source.toLowerCase().endsWith(".wav"))
                    || (URL_Source.toLowerCase().endsWith(".wks"))
                    || (URL_Source.toLowerCase().endsWith(".wma"))
                    || (URL_Source.toLowerCase().endsWith(".wmv"))
                    || (URL_Source.toLowerCase().endsWith(".wpd"))
                    || (URL_Source.toLowerCase().endsWith(".wpl"))
                    || (URL_Source.toLowerCase().endsWith(".wps"))
                    || (URL_Source.toLowerCase().endsWith(".wsf"))
                    || (URL_Source.toLowerCase().endsWith(".xlr"))
                    || (URL_Source.toLowerCase().endsWith(".xls"))
                    || (URL_Source.toLowerCase().endsWith(".xlsx"))
                    || (URL_Source.toLowerCase().endsWith(".xml"))
                    || (URL_Source.toLowerCase().endsWith(".zip"))) {

                    //  Insert Output Table
                //  Update Input Table  
                DBConnectivity.InsertOutputTable(String.valueOf(id), URLID, DURL, Normalized_DURL, URL_Source,"","","","","","","","","Failure","Invalid URL",ContactExtraction_Phase1.startTime,sdf.format(new Date()),ContactExtraction_Phase1.Language);

                DBConnectivity.UpdateInputTable(HTMLInputID, 2);
                continue;
            }

            if (configProperties.getCleansingLogic().equalsIgnoreCase("true")) {

                PageSource = PageSource.trim();
                PageSource = PageSource.replaceAll("(?sim)<script[^<>]*>.*?</script>", " ");
                PageSource = PageSource.replaceAll("(?sim)<wix[^<>]*>.*?</wix[^<>]*>", " ");
                PageSource = PageSource.replaceAll("(?sim)<style[^<>]*>.*?</style>", " ");
                PageSource = PageSource.replaceAll("(?sim)<!--[^<>]*-->", " ");
//                PageSource = PageSource.replaceAll("(?sim)<a href[^<>]*>", "");
                PageSource = PageSource.replaceAll("(?sim)<[^<>]*images[^<>]*>", "");
                if (PageSource.contains("%PDF") || PageSource.contains("&#24")) {
                    log.error("Invalid Page Source");
                    //  Insert Output Table
                    DBConnectivity.InsertOutputTable(String.valueOf(id), URLID, DURL, Normalized_DURL, URL_Source,"","","","","","","","","Failure","Invalid Page Source",ContactExtraction_Phase1.startTime,sdf.format(new Date()),ContactExtraction_Phase1.Language);


//  Update Input Table  
                    DBConnectivity.UpdateInputTable(HTMLInputID, 3);
                    continue;
                }
            }

            if (configProperties.getDiacriticsClean().equalsIgnoreCase("true")) {
                PageSource = Clean.diacriticsClean(PageSource);
            }

            int timeOutForProcessing = Integer.parseInt(configProperties.getTimeoutForProcessingInMinutes()) * 60;
            timeOutForProcessing = timeOutForProcessing - Integer.parseInt(configProperties.getTimeRelayInSeconds());

            ExecutorService executor = Executors.newSingleThreadExecutor();
            Future<String> future = executor.submit(new ThreadScheduler(PageSource, id + "_" + HTMLInputID));
            try {
                future.get(timeOutForProcessing, TimeUnit.SECONDS);
                log.info(" - Extraction Finished.... ");
            } catch (TimeoutException e) {
                future.cancel(true);
                String tempFlag = "";
                log.info(" - Final Status Scenario " + e);
                    DBConnectivity.InsertOutputTable(String.valueOf(id), URLID, DURL, Normalized_DURL, URL_Source,"","","","","","","","","Failure","TimeOutException ",ContactExtraction_Phase1.startTime,sdf.format(new Date()),ContactExtraction_Phase1.Language);


//  Update Input Table  
                    DBConnectivity.UpdateInputTable(HTMLInputID, 4);
                
                if ("".equals(tempFlag)) {
                    tempFlag = "Process Time Out";
                } else {
                    tempFlag = e + " - Process Time Out";
                }
             executor.shutdown();
                while (!executor.isTerminated()) {
                }
                executor.shutdown();
                long endtime = System.currentTimeMillis();
                long totaltime = endtime - startTime1;
                long mins = (totaltime /= 1000) / 60;
                System.out.println("Time taken for Domain Validation Process Completion:" + totaltime + " seconds");
                System.out.println("Time taken for Domain Validation Process Completion:" + mins + " mins");
                
                
                
                
            }
            executor.shutdown();
        }
        try{
            DBConnectivity.con.close();
        }catch(Exception r){
//        r.printStackTrace();
        log.error("SQl DB Connection Close Error "+r);
    }
        System.exit(0);
    }

}
