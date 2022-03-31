package contactextraction_phase1;



import static contactextraction_phase1.ContactExtraction_Phase1.log;
import static contactextraction_phase1.ContactExtraction_Phase1.prefix;
import static contactextraction_phase1.ContactExtraction_Phase1.suffix;
import static contactextraction_phase1.CoreNLP.CoreNLP_IsName;
import static contactextraction_phase1.MethodRepository.WordCount;
import edu.stanford.nlp.ie.AbstractSequenceClassifier;
import edu.stanford.nlp.ling.Word;
import java.io.IOException;
import java.io.StringReader;
import java.util.Iterator;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;
import opennlp.tools.cmdline.PerformanceMonitor;
import opennlp.tools.cmdline.postag.POSModelLoader;
import opennlp.tools.postag.POSModel;
import opennlp.tools.postag.POSSample;
import opennlp.tools.postag.POSTaggerME;
import opennlp.tools.tokenize.WhitespaceTokenizer;
import opennlp.tools.util.ObjectStream;
import opennlp.tools.util.PlainTextByLineStream;
import org.apache.commons.lang.WordUtils;
import org.apache.commons.lang3.StringUtils;
import org.json.simple.parser.ParseException;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */


/**
 *
 * @author SasiRSK
 */

 
 
 public class NER
 {
   public static AbstractSequenceClassifier classifier = null;
   public static String[] NER_Sentence_Framing = { " is a good person.", " is working in Google.", " has joined in Apple Inc." };
   public static String[] NER_Sentence_Framing_org = { " is a good workplace to work.", " is a subsdiary of Google.", " has been merged with Apple Inc." };
//   public static String serializedClassifier = "classifiers/example.serialized.ncc.ncc.ser.gz";   
   public static String serializedClassifier = "classifiers/english.all.3class.distsim.crf.ser.gz";   
//   public static String serializedClassifier = "classifiers/english.muc.7class.distsim.crf.ser.gz";   
 
     public static void main(String[] args) throws Exception {
         init(serializedClassifier);
         String Input="Sean Valentino";
         Input=WordUtils.capitalize(Input);
         System.out.println(""+Input);
//         String Input="Infosys";

   boolean validation=     NER_PersonValidation(Input);
         System.out.println("Validation "+String.valueOf(validation));
         boolean validation1=     NER_OrganizationValidation(Input);
         System.out.println("Validation "+String.valueOf(validation1));
//     boolean isfilename = CheckPersonnelNameFromFile("non");
//     System.out.println("ispaersonfromfile" + isfilename);    
    }
     
     
     
 
   public static void init(String serializedClassifier)
     throws Exception
   {
     classifier = edu.stanford.nlp.ie.crf.CRFClassifier.getClassifierNoExceptions(serializedClassifier);
   }
   
   public static String NERNameIdentification(String Input) throws IOException, InterruptedException, ParseException {

//        String output = "";
//        Input = Input.replaceAll("(?sim)<.*?>", " ");
        Thread.sleep(1000);
        classifier = edu.stanford.nlp.ie.crf.CRFClassifier.getClassifierNoExceptions(serializedClassifier);
        Input=Input.replaceAll("(?sim)\\s+", " ");
        String output = classifier.classifyWithInlineXML(Input);
        
        if (!output.contains("<PERSON>")) {
            return "";
        } else {
            String person = "";
            boolean doubleSpaceNamefoundMatch = false;
            SortedSet s = new TreeSet();
            try {
                Pattern regex = Pattern.compile("<PERSON>([^<>]*)</PERSON>",
                        Pattern.CANON_EQ | Pattern.DOTALL | Pattern.CASE_INSENSITIVE | Pattern.UNICODE_CASE | Pattern.MULTILINE);
                Matcher regexMatcher = regex.matcher(output);
                while (regexMatcher.find()) {
                    doubleSpaceNamefoundMatch = false;
//                    person += regexMatcher.group(1) + " ";
                    String Name = regexMatcher.group(1);
                    if(Name.contains("\n")){
                        Name = Name.replaceAll("(?sim)\n", " ");
                    }
                    try {
                        Pattern namesplitregex = Pattern.compile("\\s{2,}",
                                Pattern.CANON_EQ | Pattern.DOTALL | Pattern.CASE_INSENSITIVE | Pattern.UNICODE_CASE | Pattern.MULTILINE);
                        Matcher namesplitregexMatcher = namesplitregex.matcher(Name);
                        if (namesplitregexMatcher.find()) {
                            doubleSpaceNamefoundMatch = true;
                        }
                    } catch (PatternSyntaxException ex) {
                        // Syntax error in the regular expression
                    }

                    //    doubleSpaceNamefoundMatch = Name.matches("(?sim)\\s{2,}");
//Added on 11122017 for Name Missed Scenario, (NER Issue)  
// Added on 11/12/2017 For cases like <Person> name1 \n name2 \n name3 </Person> splitting
//Example : http://castleharlan.com/our-people/item/23-sylvia-f-rosen
                    if (Name.contains("\n")) {
                        Name = Name.replaceAll("(?sim)\n", "::-::");
                        String[] arrOfStr = Name.split("::-::");
                        for (String splittedname : arrOfStr) {

                            boolean isName = CoreNLP_IsName(splittedname.replaceAll("(?sim)\\s+", " ").trim());
                            if (isName == false) {
                                isName = CoreNLP_IsName(splittedname.replaceAll("(?sim)^(.*?),.*?$", "$1").replaceAll("(?sim)\\s+", " ").trim());
                            }
                            if (isName == false) {
                                splittedname=WordUtils.capitalize(splittedname);
                                isName = CoreNLP_IsName(splittedname.replaceAll("(?sim)^(.*?),.*?$", "$1").replaceAll("(?sim)\\s+", " ").trim());
                            }
                            
                            
                            if (isName == true) {
                                s.add(splittedname.trim());
                                // person += Name + "|";
                            }
                        }
                    } else if (doubleSpaceNamefoundMatch == true) {
                        Name = Name.replaceAll("(?sim)\\s{2,}", "::-::");
                        String[] arrOfStr = Name.split("::-::");
                        for (String splittedname : arrOfStr) {

                            boolean isName = CoreNLP_IsName(splittedname.replaceAll("(?sim)\\s+", " ").trim());
                            if (isName == false) {
                                isName = CoreNLP_IsName(splittedname.replaceAll("(?sim)^(.*?),.*?$", "$1").replaceAll("(?sim)\\s+", " ").trim());
                            }
                            if (isName == false) {
                                splittedname=WordUtils.capitalize(splittedname);
                                isName = CoreNLP_IsName(splittedname.replaceAll("(?sim)^(.*?),.*?$", "$1").replaceAll("(?sim)\\s+", " ").trim());
                            }
                            if (isName == true) {
                                s.add(splittedname.trim());
                                // person += Name + "|";
                            }
                        }
                    } else {
                        boolean isName = CoreNLP_IsName(Name.replaceAll("(?sim)\\s+", " ").trim());
                        if (isName == false) {
                            isName = CoreNLP_IsName(Name.replaceAll("(?sim)^(.*?),.*?$", "$1").replaceAll("(?sim)\\s+", " ").trim());
                        }
                        if (isName == false) {
                                Name=WordUtils.capitalize(Name);
                                isName = CoreNLP_IsName(Name.replaceAll("(?sim)^(.*?),.*?$", "$1").replaceAll("(?sim)\\s+", " ").trim());
                            }
                        if (isName == true) {
                            s.add(Name.trim());
                            // person += Name + "|";
                        }
                    }
                }
                Iterator<String> itr = s.iterator();
                while (itr.hasNext()) {
                    person += itr.next() + "|";
                }
                //Added               
                person = person.replaceAll("\\s{2,}", "|");
                person = person.replaceAll("(?sim)\\|\\s*$", "");

                //   System.out.println("Persons "+ person);
                return person;
            } catch (PatternSyntaxException ex) {
//                ex.printStackTrace();
                log.error(" - " + ex.getMessage());
            }
        }
        return "";
    }

   
   public static void NERNameIdentification1(String Input) throws IOException, InterruptedException {
//        Input=Input.replaceAll("(?sim)<.*?>", " ");
       Input=Input.replaceAll("(?sim)<p[^<>]*>.*?</p>", " ");
       Input=Input.replaceAll("(?sim)<[^<>]*>","<dummy>");
       Input=Input.replaceAll("(?sim)(<dummy>\\s*)+","<dummy>");
       System.out.println("Source "+Input);
       try {
	Pattern regex = Pattern.compile("<dummy>([^<>]*)",
		Pattern.CANON_EQ | Pattern.DOTALL | Pattern.CASE_INSENSITIVE | Pattern.UNICODE_CASE | Pattern.MULTILINE);
	Matcher regexMatcher = regex.matcher(Input);
	while (regexMatcher.find()) {
		// regexMatcher.group(); regexMatcher.start(); regexMatcher.end();
            String tempname=regexMatcher.group(1).replaceAll("(?sim)<[^<>]*>"," ").replaceAll("(?sim)\\s+"," ");
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
            System.out.println("temp"+tempname);
            if(tempname.contains("|")){
                String[] tempArr=tempname.split("\\|");
                for(String tempArrname:tempArr){
            String Person=tempArrname.toString().trim();
            Person=Person.replaceAll("(?sim)<[^<>]*>"," ").replaceAll("(?sim)\\s+"," ");
            if(!Person.trim().isEmpty()){
            boolean isvalid=NER_PersonValidation(Person);
            if (isvalid==false){
                isvalid=CoreNLP.CoreNLP_IsName(Person);
            }
            if (isvalid==true){
            ContactExtraction_Phase1.PersonNames.add(Person.replaceAll("(?sim)\\s+"," ").trim());
                System.out.println("Valid Names "+Person);    
            }
            }
	}
            }else{
                String Person=tempname;
                        ;
            Person=Person.replaceAll("(?sim)<[^<>]*>"," ").replaceAll("(?sim)\\s+"," ");
            if(!Person.trim().isEmpty()){
            boolean isvalid=NER_PersonValidation(Person);
            if (isvalid==false){
                isvalid=CoreNLP.CoreNLP_IsName(Person);
            }
            if (isvalid==true){
            ContactExtraction_Phase1.PersonNames.add(Person.replaceAll("(?sim)\\s+"," ").trim());
                System.out.println("Valid Names "+Person);    
            }
            }
            }
        }
} catch (PatternSyntaxException ex) {
	// Syntax error in the regular expression
}
       
       
       classifier = edu.stanford.nlp.ie.crf.CRFClassifier.getClassifierNoExceptions(serializedClassifier);
        Input=Input.replaceAll("(?sim)\\s+", " ");
        String output = classifier.classifyWithInlineXML(Input);
//        System.out.println(output);
        if (output.contains("<ORGANIZATION>")) {
           
//            String compname = "";
            boolean doubleSpaceNamefoundMatch=false;
            
            try {
                Pattern regex = Pattern.compile("<ORGANIZATION>([^<>]*)</ORGANIZATION>",
                        Pattern.CANON_EQ | Pattern.DOTALL | Pattern.CASE_INSENSITIVE | Pattern.UNICODE_CASE | Pattern.MULTILINE);
                Matcher regexMatcher = regex.matcher(output);
                while (regexMatcher.find()) {
                    doubleSpaceNamefoundMatch=false;
//                    person += regexMatcher.group(1) + " ";
                    String compname = regexMatcher.group(1);
//                    System.out.println("Stanford NER Company Name :  "+compname);
   
                }
            }catch(Exception e){
                
            }
        }
//            return "";

if((output.contains("<PERSON>")))
          {
            String person = "";
            boolean doubleSpaceNamefoundMatch=false;
            
            try {
                Pattern regex = Pattern.compile("<PERSON>([^<>]*)</PERSON>",
                        Pattern.CANON_EQ | Pattern.DOTALL | Pattern.CASE_INSENSITIVE | Pattern.UNICODE_CASE | Pattern.MULTILINE);
                Matcher regexMatcher = regex.matcher(output);
                while (regexMatcher.find()) {
                    doubleSpaceNamefoundMatch=false;
//                    person += regexMatcher.group(1) + " ";
                    String Name = regexMatcher.group(1);
                    if(!Name.trim().isEmpty()){
                    System.out.println("Stanford NER Name :  "+Name);
                    String PNAme=NER.NERNameIdentification(Name);
                    ContactExtraction_Phase1.PersonNames.add(PNAme.replaceAll("(?sim)\\s+"," ").trim());
                    }
   
                }
            }catch(Exception e){
                
            }
        }
   }

    public static boolean NER_PersonValidation(String Input) {
        String PersonnelName=Input;
        int trueCount=0;
        for (int j = 0; j < NER_Sentence_Framing.length; j++) {
         String input = PersonnelName.trim() + NER_Sentence_Framing[j];
         
 
         String output = classifier.classifyWithInlineXML(input);
         
         
         
         {
 
//           for (int i = 0; i < Main.Prefix.size(); i++) {
//             String prefix = ((String)Main.Prefix.get(i)).replaceAll("(?sim)[^A-Z0-9<>]", "").replaceAll("(?sim)(.)", "$1[^A-Z0-9<>]*");
//             output = output.replaceAll("(?sim)\\b" + prefix + "\\b[^A-Z0-9\\s<>]*", "");
//           }
           output = output.replaceAll("(?sim)<ORGANIZATION>", "");
           output = output.replaceAll("(?sim)</ORGANIZATION>", "");
           String tempsentence = output;
           
           output = output.replace(NER_Sentence_Framing[j], "");
           if (output.contains("<PERSON>")){
               trueCount++;
           }
         }
        }
         
       //if (trueCount > (NER_Sentence_Framing.length / 2)) {
       if (trueCount >= 2) {
         return true;
       } 
       return false;
    }
    
    
    public static boolean NER_EmailPersonValidation(String Input) {
        String PersonnelName_Fname="";
        String PersonnelName_Lname="";
        String emailperson_section=Input;
        
        try {
	Pattern regex = Pattern.compile("^([^\\.\\-_]+)([\\.\\-_])(.*?)$",
		Pattern.CANON_EQ | Pattern.DOTALL | Pattern.CASE_INSENSITIVE | Pattern.UNICODE_CASE | Pattern.MULTILINE);
	Matcher regexMatcher = regex.matcher(emailperson_section);
	if (regexMatcher.find()) {
            
            PersonnelName_Fname=regexMatcher.group(1);
            PersonnelName_Lname=regexMatcher.group(3);
            
        }
		 
} catch (PatternSyntaxException ex) {
	// Syntax error in the regular expression
}    
        
        if(PersonnelName_Fname.trim().isEmpty() || PersonnelName_Fname==null){
            PersonnelName_Fname=emailperson_section;
            
        }else{
            PersonnelName_Fname=PersonnelName_Fname.replaceAll("(?sim)\\d+", "");
        }
        if(PersonnelName_Lname.trim().isEmpty() || PersonnelName_Lname==null){
            PersonnelName_Lname="";
        }else{
            PersonnelName_Lname=PersonnelName_Lname.replaceAll("(?sim)\\d+", "");
        }
        
        if(PersonnelName_Fname.trim().length() >=3){
        String PersonnelName=PersonnelName_Fname.trim();
        int trueCount=0;
        for (int j = 0; j < NER_Sentence_Framing.length; j++) {
         String input = PersonnelName.trim() + NER_Sentence_Framing[j];
         String output = classifier.classifyWithInlineXML(input);
         {
           output = output.replaceAll("(?sim)<ORGANIZATION>", "");
           output = output.replaceAll("(?sim)</ORGANIZATION>", "");
           output = output.replace(NER_Sentence_Framing[j], "");
           if (output.contains("<PERSON>")){
               trueCount++;
           }
         }
        }
       if (trueCount >= 2) {
         return true;
       }
        }
        
        else if (PersonnelName_Lname.trim().length()>=3){
        String PersonnelName=PersonnelName_Lname.trim();
        int trueCount=0;
        for (int j = 0; j < NER_Sentence_Framing.length; j++) {
         String input = PersonnelName.trim() + NER_Sentence_Framing[j];
         String output = classifier.classifyWithInlineXML(input);
         {
           output = output.replaceAll("(?sim)<ORGANIZATION>", "");
           output = output.replaceAll("(?sim)</ORGANIZATION>", "");
           output = output.replace(NER_Sentence_Framing[j], "");
           if (output.contains("<PERSON>")){
               trueCount++;
           }
         }
        }
       if (trueCount >= 2) {
         return true;
       }    
        }
       return false;
    }
    
    
       public static boolean NER_OrganizationValidation(String Input) {
        String OrgName=Input;
        int trueCount=0;
        for (int j = 0; j < NER_Sentence_Framing_org.length; j++) {
         String input = OrgName.trim() + NER_Sentence_Framing_org[j];
         
 
         String output = classifier.classifyWithInlineXML(input);
         
         
         
         {
           output = output.replaceAll("(?sim)<PERSON>", "");
           output = output.replaceAll("(?sim)</PERSON>", "");
           output = output.replaceAll("(?sim)is a subsdiary.*?$", "");
           output = output.replaceAll("(?sim)has been merged.*?$", "");
           String tempsentence = output;
           
           output = output.replace(NER_Sentence_Framing_org[j], "");
           
           if (output.contains("<ORGANIZATION>")){
               trueCount++;
           }
         }
        }
         
       //if (trueCount > (NER_Sentence_Framing.length / 2)) {
       if (trueCount >= 2) {
         return true;
       } 
       return false;
    }
 

    
    
 

 
 
 
 
   public static String GetPersonnelName(String PersonnelName)
     throws Exception
   {
     {
       PersonnelName = PersonnelName.replaceAll("(?sim)\\[JT\\]", " ");
       PersonnelName = PersonnelName.replaceAll("(?sim)\\[/JT\\]", " ");
       PersonnelName = PersonnelName.replaceAll("(?sim)\\s\\s+", " ");
       Pattern regex1 = Pattern.compile("^([A-Z]+\\s)+", 168);
       
       Matcher regexMatcher1 = regex1.matcher(PersonnelName);
       if (regexMatcher1.find()) {
         String resultString = regexMatcher1.group();
         resultString = WordUtils.capitalize(resultString.toLowerCase());
         PersonnelName = PersonnelName.replaceAll("(?sm)^([A-Z]+\\s)+", resultString);
       }
       
       String input = PersonnelName.trim() + " is working in Google.";
       
       String output = classifier.classifyWithInlineXML(input);
       if (!output.contains("<PERSON>")) {
            boolean corenlp=CoreNLP_IsName(PersonnelName);
           if(corenlp==true){
               return PersonnelName;
           }else{
               return "";
           }
         //return "";
       }else{
         String person = "";
       try {
         Pattern regex = Pattern.compile("<PERSON>([^<>]*)</PERSON>", 234);
         
         Matcher regexMatcher = regex.matcher(output);
         if (regexMatcher.find())
         {
           person = regexMatcher.group(1);
         }
         return person.trim();
       } catch (PatternSyntaxException ex) {
         ex.printStackTrace();
         return "";
       }
     }
     
//    
   }
//     
   }
 
   
 
 
 
 
 
   public static String IsNameValid(String name)
   {
     try
     {
       if ((name.length() >=3) && (MethodRepository.WordCount(name) <= 10)) {
         name = name.trim();
       } else {
         name = "";
       }
     } catch (Exception e) {
       e.printStackTrace();
       name = "";
     }
     return name;
   }
//   
   public static String NERFinalValidation(String contactname) throws IOException, InterruptedException
   {
     int trueCount = 0;
     
     String nersentencenumber = "";
     
     contactname = contactname.replaceAll("(?sm)(?=(^|\\s))(.)([A-Z])(?=(\\s|$))", "$1$3\\.");
     contactname = contactname.replaceAll("(?sim)(\\.)", "$1 ");
     contactname = contactname.replaceAll("(?sim)\\s\\s+", " ");
     if (WordCount(contactname) <= 25) {
       contactname = contactname.replaceAll("(?sim)\\[JT\\].*?\\[/JT\\]", " ");
       contactname = contactname.replaceAll("(?sim)\\[JT\\]", " ");
       contactname = contactname.replaceAll("(?sim)\\[/JT\\]", " ");
       contactname = contactname.replaceAll("(?sim)\\s\\s+", " ");
       contactname = contactname.replaceAll("(?sim)[^A-Z\\s-\\.',]", "");
       for (int i = 0; i < suffix.size(); i++) {
         String Suffix = (suffix.get(i)).replaceAll("(?sim)[^A-Z0-9]", "").replaceAll("(?sim)(.)", "$1[^A-Z0-9]*");
         contactname = contactname.replaceAll("(?sim)\\b" + Suffix + "\\b[^A-Z0-9\\s]*", "");
       }
       if (contactname.replaceAll("(?sim)[^A-Z]", "").replaceAll("(?sm)[A-Z]", "").length() > 1) {
         String s = contactname.replaceAll("(?sim)[^A-Z]", "").replaceAll("(?sm)[A-Z]", "");
         for (;;) {
           Pattern regex = Pattern.compile("[^A-Za-z0-9]*([A-Z][\\.-]?)+[^A-Za-z0-9]*$", 168);
           
           Matcher regexMatcher = regex.matcher(contactname);
           if (!regexMatcher.find()) break;
           String t = regexMatcher.group();
           t = t.replaceAll("(?sim)[^A-Z0-9]", "");
           if (t.length() <= 1) break;
           contactname = contactname.replaceAll("(?sm)[^A-Za-z0-9]*([A-Z][\\.-]?)+[^A-Za-z0-9]*$", "");
           contactname = contactname.replaceAll("\\s\\s+", " ");
           contactname = contactname.trim();
         }
       }
       
 
 
 
 
 
//       contactname = contactname.replaceAll("(?sim)\\b(vere|von|van|de|del|della|di|da|pietro|vanden|du|st.|st|la|ter|der)\\b[^ A-Z0-9]*", " ");
       contactname = contactname.replaceAll("(?sim)\\b(\\w\\.)(\\w{2,100})\\b", "$1 $2");
       contactname = contactname.replaceAll("\\s\\s+", " ");
       contactname = contactname.trim();
       for (int j = 0; j < NER_Sentence_Framing.length; j++) {
         String input = contactname.trim() + NER_Sentence_Framing[j];
         
 
         String output = classifier.classifyWithInlineXML(input);
         
         if (output.contains("<PERSON>"))
         {
 
 
 
           for (int i = 0; i < prefix.size(); i++) {
             String Prefix = ((String)prefix.get(i)).replaceAll("(?sim)[^A-Z0-9<>]", "").replaceAll("(?sim)(.)", "$1[^A-Z0-9<>]*");
             output = output.replaceAll("(?sim)\\b" + Prefix + "\\b[^A-Z0-9\\s<>]*", "");
           }
           output = output.replaceAll("(?sim)<ORGANIZATION>", "");
           output = output.replaceAll("(?sim)</ORGANIZATION>", "");
           String tempsentence = output;
           
           output = output.replace(NER_Sentence_Framing[j], "");
           String tempOutput = output.replaceAll("(?sim)^(.*?)<PERSON>.*?$", "$1").trim();
           if (tempOutput.length() <= 0)
           {
 
             output = output.replaceAll("(?sim)<PERSON>([^<>]*)</PERSON>", "");
             output = output.replaceAll("(?sim)[^A-Z]", "");
             if (output.replaceAll("(?sm)[A-Z][^a-z0-9\\s]+", "").trim().length() <= 0)
             {
               nersentencenumber = nersentencenumber + "," + Integer.toString(j + 1);
               nersentencenumber = nersentencenumber.replaceAll("(?sim)^,|,$", "").trim();
               trueCount++;
             }
           }
         } }
       //if (trueCount > NER_Sentence_Framing.length / 2) {
       if (trueCount >= 2) {
         return "Verified ~ " + nersentencenumber;
       } else if (trueCount <2){
           boolean corenlp= CoreNLP_IsName(contactname);
           if(corenlp==true){
           return "Verified ~"+ 0;   
           }
       }
     }
     
 
     return "Unverified ~ " + nersentencenumber;
   }
 
   
   
   
 
 
 
 
 
   public static boolean CheckPersonnelName(String PersonnelName)
     throws Exception
   {
     //PersonnelName = PersonnelName.replaceAll("(?sm)(?=(^|\\s))(.)([A-Z])(?=(\\s|$))", "$1$3\\.");
//Regex Modified to match scenario like "| A.KAT"        
     PersonnelName = PersonnelName.replaceAll("(?sm)(?=(^\\|\\s))(.)([A-Z])(?=(\\s\\|$))", "$1$3\\.");
     
     PersonnelName = PersonnelName.replaceAll("(?sim)(\\.)", "$1 ");
     PersonnelName = PersonnelName.replaceAll("(?sim)\\s\\s+", " ");
     
     if (MethodRepository.WordCount(PersonnelName) <= 25) {
       PersonnelName = PersonnelName.replaceAll("(?sim)\\[JT\\].*?\\[/JT\\]", " ");
       PersonnelName = PersonnelName.replaceAll("(?sim)\\[JT\\]", " ");
       PersonnelName = PersonnelName.replaceAll("(?sim)\\[/JT\\]", " ");
       
       PersonnelName = PersonnelName.replaceAll("(?sim)\\s\\s+", " ");
       PersonnelName = PersonnelName.replaceAll("(?sim)[^A-Z\\s-\\.',]", "");
       for (int i = 0; i < suffix.size(); i++) {
         String Suffix = ((String)suffix.get(i)).replaceAll("(?sim)[^A-Z0-9]", "").replaceAll("(?sim)(.)", "$1[^A-Z0-9]*");
         PersonnelName = PersonnelName.replaceAll("(?sim)\\b" + Suffix + "\\b[^A-Z0-9\\s]*", "");
       }
       String t="";
       if (PersonnelName.replaceAll("(?sim)[^A-Z]", "").replaceAll("(?sm)[A-Z]", "").length() > 1) {
         String s = PersonnelName.replaceAll("(?sim)[^A-Z]", "").replaceAll("(?sm)[A-Z]", "");
         
         for (;;) {
           Pattern regex = Pattern.compile("[^A-Za-z0-9]*([A-Z][\\.-]?)+[^A-Za-z0-9]*$", 168);
           
           Matcher regexMatcher = regex.matcher(PersonnelName);
           if (!regexMatcher.find()) break;
           t = regexMatcher.group();
           t = t.replaceAll("(?sim)[^A-Z0-9]", "");
           if (t.length() <= 1) break;
           PersonnelName = PersonnelName.replaceAll("(?sm)[^A-Za-z0-9]*([A-Z][\\.-]?)+[^A-Za-z0-9]*$", "");
           PersonnelName = PersonnelName.replaceAll("\\s\\s+", " ");
           PersonnelName = PersonnelName.trim();
         }
       }
       
 
 
 
 
 
//       PersonnelName = PersonnelName.replaceAll("(?sim)\\b(vere|von|van|de|del|della|di|da|pietro|vanden|du|st.|st|la|ter|der)\\b[^ A-Z0-9]*", " ");
       PersonnelName = PersonnelName.replaceAll("(?sim)\\b(\\w\\.)(\\w{2,100})\\b", "$1 $2");
//Added on 19/06/2017 to replace Initials -  Umashankar S as Umashankar       for Name Validation
       PersonnelName=PersonnelName.replaceAll("(?sim)[\\.]*\\s"+t+"\\s*$","");
       
       PersonnelName = PersonnelName.replaceAll("\\s\\s+", " ");
       PersonnelName = PersonnelName.trim();
       int trueCount = 0;
       
       for (int j = 0; j < NER_Sentence_Framing.length; j++) {
         String input = PersonnelName.trim() + NER_Sentence_Framing[j];
         
 
         String output = classifier.classifyWithInlineXML(input);
         
         if (output.contains("<PERSON>"))
         {
 
           for (int i = 0; i < prefix.size(); i++) {
             String Prefix = ((String)prefix.get(i)).replaceAll("(?sim)[^A-Z0-9<>]", "").replaceAll("(?sim)(.)", "$1[^A-Z0-9<>]*");
             output = output.replaceAll("(?sim)\\b" + Prefix + "\\b[^A-Z0-9\\s<>]*", "");
           }
           output = output.replaceAll("(?sim)<ORGANIZATION>", "");
           output = output.replaceAll("(?sim)</ORGANIZATION>", "");
           String tempsentence = output;
           
           output = output.replace(NER_Sentence_Framing[j], "");
//           String tempOutput = output.replaceAll("(?sim)^(.*?)<PERSON>.*?$", "$1").trim();
           if (output.contains("<PERSON>")){
               trueCount++;
           }
           
//           if (tempOutput.length() <= 0)
//           {
// 
// 
//             output = output.replaceAll("(?sim)<PERSON>([^<>]*)</PERSON>", "");
//             output = output.replaceAll("(?sim)[^A-Z]", "");
//           }
////             if (output.replaceAll("(?sm)[A-Z][^a-z0-9\\s]+", "").trim().length() <= 0)
//             if (output.replaceAll("(?sm)[A-Z][^a-z0-9\\s]+", "").trim().length() <= 0)
//             {
// 
//               trueCount++; 
//             }
//           
         
         } 
       
       }
         
       //if (trueCount > (NER_Sentence_Framing.length / 2)) {
       if (trueCount >= 2) {
         return true;
       } 
       
//Added on 19/06/2017 to enable CORENlp Module to validate NAme (Lakshminarasimhan J) 
         
       else if (trueCount <2){
         return CoreNLP_IsName(PersonnelName);
     }else{
       return false;
     }
     }
     
     return false;
   }
   
 
 
 
 
 
   public static boolean POSTag(String input)
     throws IOException
   {
     String output = "";
     try
     {
       POSModel model = (POSModel)new POSModelLoader().load(new java.io.File("bin_classifiers/binFiles/en-pos-maxent.bin"));
       
       PerformanceMonitor perfMon = new PerformanceMonitor(System.err, "sent");
       POSTaggerME tagger = new POSTaggerME(model);
       ObjectStream<String> lineStream = new PlainTextByLineStream(new StringReader(input));
       
       perfMon.start();
       String line;
       while ((line = (String)lineStream.read()) != null)
       {
         String[] whitespaceTokenizerLine = WhitespaceTokenizer.INSTANCE.tokenize(line);
         String[] tags = tagger.tag(whitespaceTokenizerLine);
         
         POSSample sample = new POSSample(whitespaceTokenizerLine, tags);
         output = sample.toString();
         perfMon.incrementCounter();
       }
       lineStream.close();
       perfMon.stopAndPrintFinalResult();
       if ((StringUtils.containsIgnoreCase(output, "_VB")) || (StringUtils.containsIgnoreCase(output, "_IN")) || (StringUtils.containsIgnoreCase(output, "_DT"))) {
         return false;
       }
       return true;
     }
     catch (Exception e) {
       e.printStackTrace(); }
     return false;
   }
   
 
 
 
 
 
   
 }


