package contactextraction_phase1;

import PojoClasses.ConfigProperties;
import static contactextraction_phase1.CoreNLP.CoreNLP_IsName;
import static contactextraction_phase1.NER.NER_PersonValidation;
import static contactextraction_phase1.NER.classifier;
import static contactextraction_phase1.NER.serializedClassifier;
import java.io.BufferedReader;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;
import org.apache.commons.lang.WordUtils;
import org.apache.log4j.Logger;
import org.codehaus.jackson.JsonParser;
import org.codehaus.jackson.annotate.JsonAutoDetect;
import org.codehaus.jackson.map.ObjectMapper;

public class MethodRepository {

    public static ObjectMapper objectMapper;
    static Logger log = Logger.getLogger(MethodRepository.class.getName());
//    public static POSModel model = null;
//    public static StanfordCoreNLP pipeline;
    public static Properties props;

    //public static String ipAddress = "", dataBase = "", userName = "", passWord = "", inputTable = "", outputTable = "", gateLocation = "", nerLocation = "", proxyIP = "", proxyPort = "", platformtorun = "", Offline_mode = "false", Drivename = "";
    public static String opennlpLocation = "bin_classifiers/binFiles/en-pos-maxent.bin";
    public static int startLimit = 0, endLimit = 0, processingTime = 0, ProjectID = 0;
    //public static boolean loadWithProxy = false,OfflineInput=false,InputTableInsertion=false;

    public static String USA_City1 = "";
    public static String USA_City2 = "";
    public static String USA_City3 = "";
    public static String USA_City4 = "";
    public static String USA_City5 = "";
    public static String USA_City6 = "";
    public static String USA_City7 = "";
    public static String USA_City8 = "";
    public static String USA_City9 = "";
    public static String USA_City10 = "";
    public static String USA_City11 = "";
    public static String USA_State = "";
    public static String UK_City = "";
    public static String UK_State = "";
    public static String Canada_State = "";
    public static String Canada_City = "";
    public static String EncodeStoredata_db = "";
    public static String EncryptionKey = "";
    public static String logfile = "";

    public static String connectionmethod = "";

    public static String BelguimPhonepatterns = "";
    public static String UKPhonepatterns = "";
    public static String SpainPhonepatterns = "";
    public static String PortugalPhonepatterns = "";
    public static String MexicoPhonepatterns = "";
    public static String GermanyPhonepatterns = "";
    public static String FrancePhonepatterns = "";
    public static String BrazilPhonepatterns = "";
    public static String CanadaPhonePatterns = "";
    public static String USPhonePatterns = "";
    public static String InputFolderPath = "";

    /**
     *
     * @param RegEx RegEx to be matched
     * @param Source Source in which RegEx will be matched
     * @return Match/Non-Match
     */
    public static boolean MatchPattern(String RegEx, String Source) {
        try {
            Pattern regex = Pattern.compile(RegEx, 234);
            Matcher regexMatcher = regex.matcher(Source);
            if (regexMatcher.find()) {
                return true;
            }
            return false;
        } catch (PatternSyntaxException e) {
//            System.err.println(new StringBuilder().append("MATCH PATTERN() ERROR : ").append(e.getMessage()).toString());
            log.error(" - " + new StringBuilder().append("MATCH PATTERN() ERROR : ").append(e.getMessage()).toString());
        }
        return false;
    }

    /**
     *
     * @param words Sentence to be counted
     * @return Word count
     */
    public static int WordCount(String words) {
        int i = 0;
        try {
            String[] array = words.split(" ");
            i = array.length;
        } catch (Exception e) {
            //e.printStackTrace();
            i = 0;
        }
        return i;
    }

    /**
     *
     * @param block Input Block
     * @return Framed RegEx
     */
    public static String FrameRegex(String block) {
        try {
            String tag = "";
            try {
                Pattern regex = Pattern.compile("(<[^<>]*>)", 234);
                Matcher regexMatcher = regex.matcher(block);
                while (regexMatcher.find()) {
                    if (tag.contains("href")) {
                        tag = tag.replaceAll("(?sim)<a\\s*href=\"[^<>]*\">", "<a href=\".*?\">");
                    }
                    tag += regexMatcher.group(1) + ",";
                }
                if (tag.endsWith(",")) {
                    tag = tag.substring(0, tag.length() - 1);
                }
            } catch (PatternSyntaxException ex) {
                ex.printStackTrace();
            }
            return tag;
        } catch (Exception e) {
//            System.err.println(new StringBuilder().append("GET FRAME REGEX() ERROR : ").append(e.getMessage()).toString());
            log.error(" - " + new StringBuilder().append("GET FRAME REGEX() ERROR : ").append(e.getMessage()).toString());
        }
        return "";
    }

    public static void main(String[] args) {
        String name = SplitName("Bram van't Klooster");
    }

    public static String SplitName(String name) {

        String FName = "", MName = "", LName = "", Pfix = "", Sfix = "";
        try {

            name = name.replaceAll("(?sim)[^A-Z\\s-\\.',]", "");
            String taggedName = "";
            for (int i = 0; i < ContactExtraction_Phase1.prefix.size(); i++) {
                String prefixr = ContactExtraction_Phase1.prefix.get(i).replaceAll("(?sim)[^A-Z0-9,.-]", "").replaceAll("(?sim)(.)", "$1[^A-Z0-9,.-]*");
                Pattern regex = Pattern.compile("\\b(" + prefixr + "\\b[^A-Z0-9,\\s.-]*)",
                        Pattern.CANON_EQ | Pattern.DOTALL | Pattern.CASE_INSENSITIVE | Pattern.UNICODE_CASE | Pattern.MULTILINE);
                Matcher regexMatcher = regex.matcher(name);
                if (regexMatcher.find()) {
                    Pfix += regexMatcher.group(1) + " ";
                }
                name = name.replaceAll("(?sim)\\b" + prefixr + "\\b[^A-Z0-9\\s]*", "");
            }
            for (int i = 0; i < ContactExtraction_Phase1.suffix.size(); i++) {
                try {
                    String sfxTemp = ContactExtraction_Phase1.suffix.get(i).replaceAll("(?sim)[^A-Z0-9,.-]", "");
                    String suffixr = ContactExtraction_Phase1.suffix.get(i).replaceAll("(?sim)[^A-Z0-9,.-]", "").replaceAll("(?sim)(.)", "$1[^A-Z0-9,.-]*");
                    Pattern regex = Pattern.compile("\\b(" + suffixr + "\\b[^A-Z0-9,\\s.-]*)",
                            Pattern.CANON_EQ | Pattern.DOTALL | Pattern.CASE_INSENSITIVE | Pattern.UNICODE_CASE | Pattern.MULTILINE);
                    Matcher regexMatcher = regex.matcher(name);
                    if (regexMatcher.find()) {
                        if (sfxTemp.equalsIgnoreCase("I") || sfxTemp.equalsIgnoreCase("V")) {
                            if (name.endsWith("\\b(" + suffixr + "\\b[^A-Z0-9\\s]*)$")) {
                                Sfix = regexMatcher.group(1) + ", " + Sfix;
                                name = name.replaceAll("(?sim)\\b" + suffixr + "\\b[^A-Z0-9\\s]*", "");
                            }
                        } else {
                            Sfix = regexMatcher.group(1) + " " + Sfix;
                            name = name.replaceAll("(?sim)\\b" + suffixr + "\\b[^A-Z0-9\\s]*", "");
                        }
                    }

                } catch (Exception re) {
                    // System.out.println("sadsa" + re);
                }
            }
            name = name.replaceAll("\\(\\)", "");
            name = name.replaceAll("^(.*?)-\\s*$", "");
            name = name.replaceAll("\\s\\s+", " ");
            name = name.trim();
            name = name.replaceAll("(?sim)\\b(\\w\\.)(\\w{2,100})\\b", "$1 $2");
            if (name.contains(",")) {
                try {
                    name = name.split(",")[1] + " " + name.split(",")[0];
                } catch (Exception e) {
                }
            }

            if (!name.contains(" ") && name.contains("^")) {
                name = name.replaceAll("(?sim)\\^", " ");
            }

            name = name.replaceAll("(?sim)\\s\\s+", " ").trim();
            String[] splitname = name.split(" ");
            switch (splitname.length) {
                case 3:
                    FName = splitname[0];
                    MName = splitname[1];
                    LName = splitname[2];
                    break;
                case 4:
                    FName = splitname[0];
                    Pattern regex = Pattern.compile("^\\w\\.\\w\\.$",
                            Pattern.CANON_EQ | Pattern.DOTALL | Pattern.CASE_INSENSITIVE | Pattern.UNICODE_CASE | Pattern.MULTILINE);
                    Matcher regexMatcher1 = regex.matcher(splitname[1]);
                    Matcher regexMatcher2 = regex.matcher(splitname[2]);
                    if ((splitname[1].replaceAll("(?sim)[^A-Z0-9]", "").length() == 1 || regexMatcher1.find()) && splitname[2].replaceAll("(?sim)[^A-Z0-9]", "").length() > 1) {
                        MName = splitname[1];
                        LName = splitname[2] + " ";
                    } else if (splitname[1].replaceAll("(?sim)[^A-Z0-9]", "").length() > 1 && (splitname[2].replaceAll("(?sim)[^A-Z0-9]", "").length() == 1 || regexMatcher2.find())) {
                        FName += " " + splitname[1];
                        MName = splitname[2];
                    } else {
                        MName = splitname[1] + " " + splitname[2];
                    }
                    LName += splitname[3];
                    break;
                case 2:
                    FName = splitname[0];
                    LName = splitname[1];
                    break;
                case 1:
                    FName = splitname[0];
                    break;
                default:
                    break;
            }

            Pfix = Pfix.replaceAll("(?sim)\\s\\s+", " ").trim();
            FName = FName.replaceAll("(?sim),", " ").replaceAll("(?sim)\\s\\s+", " ").trim();
            MName = MName.replaceAll("(?sim),", " ").replaceAll("(?sim)\\s\\s+", " ").trim();
            LName = LName.replaceAll("(?sim),", " ").replaceAll("(?sim)\\s\\s+", " ").trim();
            FName = FName.replaceAll("(?sim)\\^", " ").trim();
            MName = MName.replaceAll("(?sim)\\^", " ").trim();
            LName = LName.replaceAll("(?sim)\\^", " ").trim();
            Sfix = Sfix.replaceAll("(?sim)\\s\\s+", " ").trim();

            Pattern regex = Pattern.compile("[a-zA-Z]",
                    Pattern.CANON_EQ | Pattern.DOTALL | Pattern.CASE_INSENSITIVE | Pattern.UNICODE_CASE | Pattern.MULTILINE);
            Matcher regexMatcher = regex.matcher(FName);
            if (!regexMatcher.find()) {
                FName = "";
            }

            regexMatcher = regex.matcher(MName);
            if (!regexMatcher.find()) {
                MName = "";
            }

            regexMatcher = regex.matcher(LName);
            if (!regexMatcher.find()) {
                LName = "";
            }

            if (FName.length() < 3 && !MName.equals("")) {

                regex = Pattern.compile("[a-zA-Z]\\.",
                        Pattern.CANON_EQ | Pattern.DOTALL | Pattern.CASE_INSENSITIVE | Pattern.UNICODE_CASE | Pattern.MULTILINE);
                regexMatcher = regex.matcher(FName);
                if (regexMatcher.find()) {
                    String temp = FName;
                    FName = MName;
                    MName = temp;
                }

            }
            if (LName.length() < 3) {
                regex = Pattern.compile("(^[a-zA-Z]\\.$)|(^[a-zA-Z]$)",
                        Pattern.CANON_EQ | Pattern.DOTALL | Pattern.CASE_INSENSITIVE | Pattern.UNICODE_CASE | Pattern.MULTILINE);
                regexMatcher = regex.matcher(LName);
                if (regexMatcher.find()) {
                    FName = FName.trim() + " " + LName;
                    LName = "";
                }

            }

//            if (MName.length() > 4 && !FName.equals("") && !MName.contains(".")) {
//            if (MName.length() > 4 && !FName.equals("") && (!MName.contains("."))) {
//                FName = FName.trim() + " " + MName;
//                MName = "";
//            }
        } catch (Exception e) {
        }
        String output = Pfix.trim() + "::-::" + FName.trim() + "::-::" + MName.trim() + "::-::" + LName.trim() + "::-::" + Sfix.trim();
        return output;
    }

    public static void loadprops() throws InterruptedException, IOException {
        try {
            objectMapper = new ObjectMapper().configure(JsonParser.Feature.ALLOW_COMMENTS, true);

            objectMapper.setVisibilityChecker(objectMapper.getSerializationConfig().getDefaultVisibilityChecker()
                    .withFieldVisibility(JsonAutoDetect.Visibility.ANY)
                    .withGetterVisibility(JsonAutoDetect.Visibility.NONE)
                    .withSetterVisibility(JsonAutoDetect.Visibility.NONE)
                    .withCreatorVisibility(JsonAutoDetect.Visibility.NONE));

            File fileAsset = new File("Configuration.json");
            byte[] jsonDataAsset = new byte[(int) fileAsset.length()];
            FileInputStream finAsset = new FileInputStream(fileAsset);
            finAsset.read(jsonDataAsset);

            ContactExtraction_Phase1.configProperties = objectMapper.readValue(jsonDataAsset, ConfigProperties.class);
        } catch (Exception ex) {
            log.info("Configuration.json File Not Found");
            System.exit(0);
        }
    }

    public static String EmailPersonNameMatch(String BlockContent, String Input) throws IOException, InterruptedException {

        String PersonnelName_Fname = "";
        String PersonnelName_Lname = "";
        String emailperson_section = Input;
        String statusFlag = "false";

        try {
            Pattern regex = Pattern.compile("^([^\\.\\-_]+)([\\.\\-_])(.*?)$",
                    Pattern.CANON_EQ | Pattern.DOTALL | Pattern.CASE_INSENSITIVE | Pattern.UNICODE_CASE | Pattern.MULTILINE);
            Matcher regexMatcher = regex.matcher(emailperson_section);
            if (regexMatcher.find()) {

                PersonnelName_Fname = regexMatcher.group(1);
                PersonnelName_Lname = regexMatcher.group(3);

            }

        } catch (PatternSyntaxException ex) {
            // Syntax error in the regular expression
        }

        if (PersonnelName_Fname.trim().isEmpty() || PersonnelName_Fname == null) {
            PersonnelName_Fname = emailperson_section;

        } else {
            PersonnelName_Fname = PersonnelName_Fname.replaceAll("(?sim)\\d+", "");
        }
        if (PersonnelName_Lname.trim().isEmpty() || PersonnelName_Lname == null) {
            PersonnelName_Lname = "";
        } else {
            PersonnelName_Lname = PersonnelName_Lname.replaceAll("(?sim)\\d+", "");
        }

        if (PersonnelName_Fname.trim().length() >= 3) {
            String PersonnelName = PersonnelName_Fname.trim();

            String FindName = "";
            try {
                Pattern regex = Pattern.compile("<[^<>]*>([^<>]*)",
                        Pattern.CANON_EQ | Pattern.DOTALL | Pattern.CASE_INSENSITIVE | Pattern.UNICODE_CASE | Pattern.MULTILINE);
                Matcher regexMatcher = regex.matcher(BlockContent);
                while (regexMatcher.find()) {
                    // regexMatcher.group(); regexMatcher.start(); regexMatcher.end();
                    FindName = regexMatcher.group(1).trim();
                    FindName = FindName.replaceAll("(?sim)\n", "").trim();
                    if (FindName.contains(" ")) {
                        String temp[] = FindName.split(" ");
                        for (String splittedname : temp) {
                            splittedname = WordUtils.capitalize(splittedname);

//                            boolean isName = CoreNLP_IsName(splittedname.replaceAll("(?sim)\\s+", " ").trim());
//                            if (isName == false) {
//                                isName = CoreNLP_IsName(splittedname.replaceAll("(?sim)^(.*?),.*?$", "$1").replaceAll("(?sim)\\s+", " ").trim());
//                            }
//                            if (isName == true) 
//                            {
                            if (splittedname.toLowerCase().contains(PersonnelName.toLowerCase())) {
                                statusFlag = "true";
                                return FindName;
                            } else if (PersonnelName.toLowerCase().contains(splittedname.toLowerCase())) {
                                statusFlag = "true";
                                return FindName;
                            }
                        }
                    } else if (FindName.contains(",")) {
                        String temp[] = FindName.split(",");
                        for (String splittedname : temp) {
                            splittedname = WordUtils.capitalize(splittedname);

//                            boolean isName = CoreNLP_IsName(splittedname.replaceAll("(?sim)\\s+", " ").trim());
//                            if (isName == false) {
//                                isName = CoreNLP_IsName(splittedname.replaceAll("(?sim)^(.*?),.*?$", "$1").replaceAll("(?sim)\\s+", " ").trim());
//                            }
//                            if (isName == true) 
//                            {
                            if (splittedname.toLowerCase().contains(PersonnelName.toLowerCase())) {
                                statusFlag = "true";
                                return FindName;
                            } else if (PersonnelName.toLowerCase().contains(splittedname.toLowerCase())) {
                                statusFlag = "true";
                                return FindName;
                            }
                        }
                    } else {

                        if (FindName.toLowerCase().contains(PersonnelName.toLowerCase())) {
                            statusFlag = "true";
                            return FindName;
                        }
                    }
                }
            } catch (PatternSyntaxException ex) {
                // Syntax error in the regular expression
            }
            if (statusFlag == "false") {
                String remove_firstletter = PersonnelName.trim().replaceAll("(?sim)^.", "");
                remove_firstletter = WordUtils.capitalize(remove_firstletter.trim());
                String FindName1 = "";
                try {
                    Pattern regex = Pattern.compile("<[^<>]*>([^<>]*)",
                            Pattern.CANON_EQ | Pattern.DOTALL | Pattern.CASE_INSENSITIVE | Pattern.UNICODE_CASE | Pattern.MULTILINE);
                    Matcher regexMatcher = regex.matcher(BlockContent);
                    while (regexMatcher.find()) {
                        // regexMatcher.group(); regexMatcher.start(); regexMatcher.end();
                        FindName1 = regexMatcher.group(1);
                        FindName1 = FindName1.replaceAll("(?sim)\n", "").trim();
                        if (FindName1.contains(" ")) {
                            String temp[] = FindName1.split(" ");
                            for (String splittedname : temp) {
                                splittedname = WordUtils.capitalize(splittedname);

//                            boolean isName = CoreNLP_IsName(splittedname.replaceAll("(?sim)\\s+", " ").trim());
//                            if (isName == false) {
//                                isName = CoreNLP_IsName(splittedname.replaceAll("(?sim)^(.*?),.*?$", "$1").replaceAll("(?sim)\\s+", " ").trim());
//                            }
//                            if (isName == true) 
//                            {
                                if (splittedname.toLowerCase().contains(remove_firstletter.toLowerCase())) {
                                    statusFlag = "true";
                                    return FindName1;
                                } else if (remove_firstletter.toLowerCase().contains(splittedname.toLowerCase())) {
                                    statusFlag = "true";
                                    return FindName1;
                                }
                            }
                        } else if (FindName1.contains(",")) {
                            String temp[] = FindName1.split(",");
                            for (String splittedname : temp) {
                                splittedname = WordUtils.capitalize(splittedname);

//                            boolean isName = CoreNLP_IsName(splittedname.replaceAll("(?sim)\\s+", " ").trim());
//                            if (isName == false) {
//                                isName = CoreNLP_IsName(splittedname.replaceAll("(?sim)^(.*?),.*?$", "$1").replaceAll("(?sim)\\s+", " ").trim());
//                            }
//                            if (isName == true) 
//                            {
                                if (splittedname.toLowerCase().contains(PersonnelName.toLowerCase())) {
                                    statusFlag = "true";
                                    return FindName1;
                                } else if (PersonnelName.toLowerCase().contains(splittedname.toLowerCase())) {
                                    statusFlag = "true";
                                    return FindName1;
                                }
                            }
                        } else {

                            if (FindName1.toLowerCase().contains(remove_firstletter.toLowerCase())) {
                                statusFlag = "true";
                                return FindName1;
                            }
                        }
                    }
                } catch (PatternSyntaxException ex) {
                    // Syntax error in the regular expression
                }

            }

        } else if (PersonnelName_Lname.trim().length() >= 3) {
            String PersonnelName = PersonnelName_Lname.trim();
            String FindName = "";
            statusFlag = "false";
            try {
                Pattern regex = Pattern.compile("<[^<>]*>([^<>]*)",
                        Pattern.CANON_EQ | Pattern.DOTALL | Pattern.CASE_INSENSITIVE | Pattern.UNICODE_CASE | Pattern.MULTILINE);
                Matcher regexMatcher = regex.matcher(BlockContent);
                while (regexMatcher.find()) {
                    // regexMatcher.group(); regexMatcher.start(); regexMatcher.end();
                    FindName = regexMatcher.group(1).trim();
                    FindName = FindName.replaceAll("(?sim)\n", "").trim();
                    if (FindName.contains(" ")) {
                        String temp[] = FindName.split(" ");
                        for (String splittedname : temp) {
                            splittedname = WordUtils.capitalize(splittedname);

//                            boolean isName = CoreNLP_IsName(splittedname.replaceAll("(?sim)\\s+", " ").trim());
//                            if (isName == false) {
//                                isName = CoreNLP_IsName(splittedname.replaceAll("(?sim)^(.*?),.*?$", "$1").replaceAll("(?sim)\\s+", " ").trim());
//                            }
//                            if (isName == true) 
//                            {
                            if (splittedname.toLowerCase().contains(PersonnelName.toLowerCase())) {
                                statusFlag = "true";
                                return FindName;
                            } else if (PersonnelName.toLowerCase().contains(splittedname.toLowerCase())) {
                                statusFlag = "true";
                                return FindName;
                            }
                        }
                    } else if (FindName.contains(",")) {
                        String temp[] = FindName.split(",");
                        for (String splittedname : temp) {
                            splittedname = WordUtils.capitalize(splittedname);

//                            boolean isName = CoreNLP_IsName(splittedname.replaceAll("(?sim)\\s+", " ").trim());
//                            if (isName == false) {
//                                isName = CoreNLP_IsName(splittedname.replaceAll("(?sim)^(.*?),.*?$", "$1").replaceAll("(?sim)\\s+", " ").trim());
//                            }
//                            if (isName == true) 
//                            {
                            if (splittedname.toLowerCase().contains(PersonnelName.toLowerCase())) {
                                statusFlag = "true";
                                return FindName;
                            } else if (PersonnelName.toLowerCase().contains(splittedname.toLowerCase())) {
                                statusFlag = "true";
                                return FindName;
                            }
                        }
                    } else {

                        if (FindName.toLowerCase().contains(PersonnelName.toLowerCase())) {
                            statusFlag = "true";
                            return FindName;
                        }
                    }
                }
            } catch (PatternSyntaxException ex) {
                // Syntax error in the regular expression
            }

            if (statusFlag == "false") {
                String remove_firstletter = PersonnelName.trim().replaceAll("(?sim)^.", "");
                remove_firstletter = WordUtils.capitalize(remove_firstletter.trim());
                String FindName1 = "";
                try {
                    Pattern regex = Pattern.compile("<[^<>]*>([^<>]*)",
                            Pattern.CANON_EQ | Pattern.DOTALL | Pattern.CASE_INSENSITIVE | Pattern.UNICODE_CASE | Pattern.MULTILINE);
                    Matcher regexMatcher = regex.matcher(BlockContent);
                    while (regexMatcher.find()) {
                        // regexMatcher.group(); regexMatcher.start(); regexMatcher.end();
                        FindName1 = regexMatcher.group(1);
                        FindName1 = FindName1.replaceAll("(?sim)\n", "").trim();
                        if (FindName1.contains(" ")) {
                            String temp[] = FindName1.split(" ");
                            for (String splittedname : temp) {
                                splittedname = WordUtils.capitalize(splittedname);

//                            boolean isName = CoreNLP_IsName(splittedname.replaceAll("(?sim)\\s+", " ").trim());
//                            if (isName == false) {
//                                isName = CoreNLP_IsName(splittedname.replaceAll("(?sim)^(.*?),.*?$", "$1").replaceAll("(?sim)\\s+", " ").trim());
//                            }
//                            if (isName == true) 
//                            {
                                if (splittedname.toLowerCase().contains(remove_firstletter.toLowerCase())) {
                                    statusFlag = "true";
                                    return FindName1;
                                } else if (remove_firstletter.toLowerCase().contains(splittedname.toLowerCase())) {
                                    statusFlag = "true";
                                    return FindName1;
                                }
                            }
                        } else if (FindName1.contains(",")) {
                            String temp[] = FindName1.split(",");
                            for (String splittedname : temp) {
                                splittedname = WordUtils.capitalize(splittedname);

//                            boolean isName = CoreNLP_IsName(splittedname.replaceAll("(?sim)\\s+", " ").trim());
//                            if (isName == false) {
//                                isName = CoreNLP_IsName(splittedname.replaceAll("(?sim)^(.*?),.*?$", "$1").replaceAll("(?sim)\\s+", " ").trim());
//                            }
//                            if (isName == true) 
//                            {
                                if (splittedname.toLowerCase().contains(PersonnelName.toLowerCase())) {
                                    statusFlag = "true";
                                    return FindName1;
                                } else if (PersonnelName.toLowerCase().contains(splittedname.toLowerCase())) {
                                    statusFlag = "true";
                                    return FindName1;
                                }
                            }
                        } else {

                            if (FindName1.toLowerCase().contains(remove_firstletter.toLowerCase())) {
                                statusFlag = "true";
                                return FindName1;
                            }
                        }
                    }
                } catch (PatternSyntaxException ex) {
                    // Syntax error in the regular expression
                }

            }
//        return "";
        }
        return "";
    }

    public static String EmailPersonNameMatch_PageSource(String BlockContent, String Input) throws IOException, InterruptedException {
//        Input=Input.replaceAll("(?sim)<.*?>", " ");
        String PersonnelName = Input;
        BlockContent = BlockContent.replaceAll("(?sim)<p[^<>]*>.*?</p>", " ");
        BlockContent = BlockContent.replaceAll("(?sim)<[^<>]*>", "<dummy>");
        BlockContent = BlockContent.replaceAll("(?sim)(<dummy>\\s*)+", "<dummy>");
//       System.out.println("Source "+BlockContent);
        try {
            Pattern regex = Pattern.compile("<dummy>([^<>]*)",
                    Pattern.CANON_EQ | Pattern.DOTALL | Pattern.CASE_INSENSITIVE | Pattern.UNICODE_CASE | Pattern.MULTILINE);
            Matcher regexMatcher = regex.matcher(BlockContent);
            while (regexMatcher.find()) {
                // regexMatcher.group(); regexMatcher.start(); regexMatcher.end();
                String tempname = regexMatcher.group(1).replaceAll("(?sim)<[^<>]*>", " ").replaceAll("(?sim)\\s+", " ");
                try {
                    Pattern regex1 = Pattern.compile("\\d+",
                            Pattern.CANON_EQ | Pattern.DOTALL | Pattern.CASE_INSENSITIVE | Pattern.UNICODE_CASE | Pattern.MULTILINE);
                    Matcher regexMatcher1 = regex1.matcher(tempname);
                    if (regexMatcher1.find()) {
                        // regexMatcher.group(); regexMatcher.start(); regexMatcher.end();
                        continue;
                    }
                } catch (PatternSyntaxException ex) {
	// Syntax error in the regular expression
                }
//            System.out.println("temp"+tempname);
                if (tempname.contains("|")) {
                    String[] tempArr = tempname.split("\\|");
                    for (String tempArrname : tempArr) {
                        String Person = tempArrname.toString().trim();
                        Person = Person.replaceAll("(?sim)<[^<>]*>", " ").replaceAll("(?sim)\\s+", " ");
                        if (!Person.trim().isEmpty()) {
                            boolean isvalid = NER_PersonValidation(Person);
                            if (isvalid == false) {
                                isvalid = CoreNLP.CoreNLP_IsName(Person);
                            }
                            if (isvalid == true) {
                                if (Person.toLowerCase().contains(PersonnelName.toLowerCase())) {
//                        statusFlag="true";
                                    return Person;
                                } else {
                                    String temp_PersonnelName = PersonnelName.replaceAll("(?sim)^.", "");
                                    if (Person.toLowerCase().contains(temp_PersonnelName.toLowerCase())) {
//                        statusFlag="true";
                                        return Person;

                                    }
                                }

//            ContactExtraction_Phase1.PersonNames.add(Person.replaceAll("(?sim)\\s+"," ").trim());
//                System.out.println("Valid Names "+Person);    
                            }
                        }
                    }
                } else if (tempname.contains(",")) {
                    String[] tempArr = tempname.split(",");
                    for (String tempArrname : tempArr) {
                        String Person = tempArrname.toString().trim();
                        Person = Person.replaceAll("(?sim)<[^<>]*>", " ").replaceAll("(?sim)\\s+", " ");
                        if (!Person.trim().isEmpty()) {
                            boolean isvalid = NER_PersonValidation(Person);
                            if (isvalid == false) {
                                isvalid = CoreNLP.CoreNLP_IsName(Person);
                            }
                            if (isvalid == true) {
                                if (Person.toLowerCase().contains(PersonnelName.toLowerCase())) {
//                        statusFlag="true";
                                    return Person;
                                } else {
                                    String temp_PersonnelName = PersonnelName.replaceAll("(?sim)^.", "");
                                    if (Person.toLowerCase().contains(temp_PersonnelName.toLowerCase())) {
//                        statusFlag="true";
                                        return Person;

                                    }
                                }

//            ContactExtraction_Phase1.PersonNames.add(Person.replaceAll("(?sim)\\s+"," ").trim());
//                System.out.println("Valid Names "+Person);    
                            }
                        }
                    }
                } else {
                    String Person = tempname;
                    ;
                    Person = Person.replaceAll("(?sim)<[^<>]*>", " ").replaceAll("(?sim)\\s+", " ");
                    if (!Person.trim().isEmpty()) {
                        boolean isvalid = NER_PersonValidation(Person);
                        if (isvalid == false) {
                            isvalid = CoreNLP.CoreNLP_IsName(Person);
                        }
                        if (isvalid == true) {
                            if (Person.toLowerCase().contains(PersonnelName.toLowerCase())) {
//                        statusFlag="true";
                                return Person;
                            } else {
                                String temp_PersonnelName = PersonnelName.replaceAll("(?sim)^.", "");
                                if (Person.toLowerCase().contains(temp_PersonnelName.toLowerCase())) {
//                        statusFlag="true";
                                    return Person;

                                }
                            }
                        }
                    }
                }
            }
        } catch (PatternSyntaxException ex) {
	// Syntax error in the regular expression
        }

        classifier = edu.stanford.nlp.ie.crf.CRFClassifier.getClassifierNoExceptions(serializedClassifier);
        BlockContent = BlockContent.replaceAll("(?sim)\\s+", " ");
        String output = classifier.classifyWithInlineXML(BlockContent);
//        System.out.println(output);
        if (output.contains("<ORGANIZATION>")) {

//            String compname = "";
            boolean doubleSpaceNamefoundMatch = false;

            try {
                Pattern regex = Pattern.compile("<ORGANIZATION>([^<>]*)</ORGANIZATION>",
                        Pattern.CANON_EQ | Pattern.DOTALL | Pattern.CASE_INSENSITIVE | Pattern.UNICODE_CASE | Pattern.MULTILINE);
                Matcher regexMatcher = regex.matcher(output);
                while (regexMatcher.find()) {
                    doubleSpaceNamefoundMatch = false;
//                    person += regexMatcher.group(1) + " ";
                    String compname = regexMatcher.group(1);
//                    System.out.println("Stanford NER Company Name :  "+compname);

                }
            } catch (Exception e) {

            }
        }
//            return "";

        if ((output.contains("<PERSON>"))) {
            String person = "";
            boolean doubleSpaceNamefoundMatch = false;

            try {
                Pattern regex = Pattern.compile("<PERSON>([^<>]*)</PERSON>",
                        Pattern.CANON_EQ | Pattern.DOTALL | Pattern.CASE_INSENSITIVE | Pattern.UNICODE_CASE | Pattern.MULTILINE);
                Matcher regexMatcher = regex.matcher(output);
                while (regexMatcher.find()) {
                    doubleSpaceNamefoundMatch = false;
//                    person += regexMatcher.group(1) + " ";
                    String Name = regexMatcher.group(1);
                    if (!Name.trim().isEmpty()) {
//                    System.out.println("Stanford NER Name :  "+Name);
                        String Person = NER.NERNameIdentification(Name);
                        if (Person.toLowerCase().contains(PersonnelName.toLowerCase())) {
//                        statusFlag="true";
                            return Person;
                        } else {
                            String temp_PersonnelName = PersonnelName.replaceAll("(?sim)^.", "");
                            if (Person.toLowerCase().contains(temp_PersonnelName.toLowerCase())) {
//                        statusFlag="true";
                                return Person;

                            }
                        }

//                    ContactExtraction_Phase1.PersonNames.add(PNAme.replaceAll("(?sim)\\s+"," ").trim());
                    }

                }
            } catch (Exception e) {

            }
        }
        return "";

    }

//        return "";
    public static final ArrayList<String> File_Reader(String FileName) {
        ArrayList Items_List = new ArrayList();
        try {
            InputStream in = new FileInputStream(new File(FileName));
            BufferedReader reader = new BufferedReader(new InputStreamReader(in));
            StringBuilder out = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                //out.append(line);
                if (!line.isEmpty()) {
                    Items_List.add(line);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
            System.exit(0);
        }
        return Items_List;
    }

    public static String CleanDomain(String domainurl) {
        String temp = "";
        try {
            Pattern regex = Pattern.compile("^(http[s:]*[//]*)*(www\\.)*(.*?)[/]*$",
                    Pattern.CANON_EQ | Pattern.DOTALL | Pattern.CASE_INSENSITIVE | Pattern.UNICODE_CASE | Pattern.MULTILINE);
            Matcher regexMatcher = regex.matcher(domainurl);
            if (regexMatcher.find()) {
                // regexMatcher.group(); regexMatcher.start(); regexMatcher.end();
                temp = regexMatcher.group(3);

            }
        } catch (PatternSyntaxException ex) {
            // Syntax error in the regular expression
        }
        if (!temp.trim().isEmpty()) {
            if (temp.contains("/")) {
                try {
                    Pattern regex = Pattern.compile("^(.*?)/.*?$",
                            Pattern.CANON_EQ | Pattern.DOTALL | Pattern.CASE_INSENSITIVE | Pattern.UNICODE_CASE | Pattern.MULTILINE);
                    Matcher regexMatcher = regex.matcher(temp);
                    if (regexMatcher.find()) {
                        // regexMatcher.group(); regexMatcher.start(); regexMatcher.end();
                        temp = regexMatcher.group(1);
                        return "~~" + temp.replaceAll("(?sim)/\\s*$", "").trim();
                    }
                } catch (PatternSyntaxException ex) {
                    // Syntax error in the regular expression
                }
            } else {
                return temp.replaceAll("(?sim)/\\s*$", "").trim();
            }
        }

        return domainurl.replaceAll("(?sim)/\\s*$", "").trim();

    }
}
