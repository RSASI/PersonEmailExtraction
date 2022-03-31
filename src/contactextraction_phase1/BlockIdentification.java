/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package contactextraction_phase1;

import static contactextraction_phase1.ContactExtraction_Phase1.sdf;
import static contactextraction_phase1.MethodRepository.EmailPersonNameMatch;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;
import org.apache.commons.lang.WordUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

/**
 *
 * @author SASIKUMAR
 */
public class BlockIdentification {
    
    static void processExecution(String PageSource,String FileName) throws IOException, InterruptedException{
        MailExtraction.ExtractMail_Person(PageSource);
        if(ContactExtraction_Phase1.EmailExtracted.size()>0){
            PersonContactExtraction(PageSource);
        }else{
            PersonNameExtraction(PageSource);
        }
        for(int i=0;i<ContactExtraction_Phase1.FinalArrayList.size();i++){
            String tempVar=ContactExtraction_Phase1.FinalArrayList.get(i);
            String[] split=tempVar.split("::~::");
            String id="";
            String UniqueID="";
            String DomainURL="";
            String DomainName="";
            String Url="";
            String Prefix="";
            String ContactName="";
            String Suffix="";
            String ContactFirstName="";
            String ContactMiddleName="";
            String ContactLastName="";
            String Email="";
            String DomainFound="";
            String ProcessStatus="";
            String StatusDescription="";
            String StartTime="";
            String EndTime="";
            String Language="";
            
            try {
                    id = split[0];
                } catch (Exception e) {
                    
                }
            try {
                     UniqueID= split[1];
                } catch (Exception e) {
                    
                }
            try {
                     DomainURL= split[2];
                } catch (Exception e) {
                    
                }
            try {
                     DomainName= split[3];
                } catch (Exception e) {
                    
                }
            try {
                     Url= split[4];
                } catch (Exception e) {
                    
                }
            try {
                     Prefix= split[5];
                } catch (Exception e) {
                    
                }
            try {
                     ContactName= split[6];
                } catch (Exception e) {
                    
                }
            try {
                     Suffix= split[7];
                } catch (Exception e) {
                    
                }
            try {
                     ContactFirstName= split[8];
                } catch (Exception e) {
                    
                }
            try {
                     ContactMiddleName= split[9];
                } catch (Exception e) {
                    
                }
            try {
                     ContactLastName= split[10];
                } catch (Exception e) {
                    
                }
            try {
                     Email= split[11];
                } catch (Exception e) {
                    
                }
            try {
                     DomainFound= split[12];
                } catch (Exception e) {
                    
                }
            try {
                     ProcessStatus= split[13];
                } catch (Exception e) {
                    
                }
            try {
                     StatusDescription= split[14];
                } catch (Exception e) {
                    
                }
            DBConnectivity.InsertOutputTable(id, UniqueID, DomainURL, DomainName, Url,Prefix,ContactName,Suffix,ContactFirstName, ContactMiddleName,ContactLastName,Email,DomainFound,ProcessStatus,StatusDescription,ContactExtraction_Phase1.startTime,sdf.format(new Date()),ContactExtraction_Phase1.Language);
            DBConnectivity.UpdateInputTable(id, 11);
            
            
        } 
        
    }
    
    
    
    public static void PersonContactExtraction(String PageContent) {
    ArrayList<String> FinalBlockList = new ArrayList();    
    try{
//                PageContent=PageContent.replaceAll("(?sim)<a[^<>]*>"," ").replaceAll("(?sim)\\s+"," ");
                org.jsoup.nodes.Document Annotated_annDocument = Jsoup.parse(PageContent.replaceAll("(?sim)\\s+"," "));
                
                Iterator email_str=ContactExtraction_Phase1.EmailExtracted.iterator();
                while(email_str.hasNext()){
                    String email_text=email_str.next().toString();
                    String emailperson_section_org=email_text.replaceAll("(?sim)@.*?$","");
                    String emailperson_section=WordUtils.capitalize(emailperson_section_org.trim());
                    boolean personemail=NER.NER_EmailPersonValidation(emailperson_section);
                    if (personemail==false){
                        String remove_firstletter=emailperson_section.trim().replaceAll("(?sim)^.","");
                        remove_firstletter=WordUtils.capitalize(remove_firstletter.trim());
                        personemail=NER.NER_EmailPersonValidation(remove_firstletter);
                    }
                    if (personemail==false){
                        personemail=CoreNLP.CoreNLP_IsName(emailperson_section);
                    }
                    if (personemail==false){
                        String remove_firstletter=emailperson_section.trim().replaceAll("(?sim)^.","");
                        remove_firstletter=WordUtils.capitalize(remove_firstletter.trim());
                        personemail=CoreNLP.CoreNLP_IsName(remove_firstletter);
                    }
                    if (personemail==false){
                        personemail=NER.NER_PersonValidation(emailperson_section);
                    }
                    if (personemail==false){
                        String remove_firstletter=emailperson_section.trim().replaceAll("(?sim)^.","");
                        remove_firstletter=WordUtils.capitalize(remove_firstletter.trim());
                        personemail=NER.NER_PersonValidation(remove_firstletter);
                    }
                    if(personemail == true){
                    
                    Elements titleElements = null;
                    titleElements = Annotated_annDocument.getElementsContainingOwnText(email_text);
//                    titleElements = Annotated_annDocument.getElementsContainingText(email_text);
                    if(titleElements.size()==0){
                    titleElements = Annotated_annDocument.getElementsContainingText(email_text);
                    }
                    for (Element titleElement : titleElements) {
                        if (email_text.trim().length() > 1) {
                            
                            String block=titleElement.outerHtml();
//                            System.out.println("Block "+block);
                            FinalExtraction(block,email_text);    
                            
                            
                            
                                }
                    }
                }else{
                        ContactExtraction_Phase1.BussinessEmail.add(email_text.trim());
                    }
                }
            } catch (Exception e) {
             }
            
            
}

    private static void FinalExtraction(String block, String email_text) throws IOException, InterruptedException {
        
        String inputblock=block.replaceAll("<a href[^<>]*>","").replaceAll(email_text,"");
                org.jsoup.nodes.Document block_document = Jsoup.parse(inputblock);

        String jsoupContent=block_document.toString().trim();
//        System.out.println("jsoupContent -- "+jsoupContent);
        String finalcontent=jsoupContent.replaceAll("(?sim)"+email_text,"");
        
        String emailperson_section_org=email_text.replaceAll("(?sim)@.*?$","");
        
       String PersonName = EmailPersonNameMatch(finalcontent,emailperson_section_org);
       if(PersonName.trim().isEmpty()){
           String page_finalcontent=ContactExtraction_Phase1.PageSource;
           page_finalcontent=page_finalcontent.replaceAll(email_text,"");
           page_finalcontent=page_finalcontent.replaceAll("<[^<>]*>","<dummy>");
           page_finalcontent=page_finalcontent.replaceAll("(?sim)(<dummy>\\s*)+","<dummy>");
//            CharSequence emailperson_section;
//           PersonName=EmailPersonNameMatch(page_finalcontent,emailperson_section_org);
           String PersonnelName_Fname="";
           String PersonnelName_Lname="";
        try {
            Pattern regex = Pattern.compile("^([^\\.\\-_]+)([\\.\\-_])(.*?)$",
                    Pattern.CANON_EQ | Pattern.DOTALL | Pattern.CASE_INSENSITIVE | Pattern.UNICODE_CASE | Pattern.MULTILINE);
            Matcher regexMatcher = regex.matcher(emailperson_section_org);
            if (regexMatcher.find()) {

                PersonnelName_Fname= regexMatcher.group(1);
                PersonnelName_Lname = regexMatcher.group(3);

            }

        } catch (PatternSyntaxException ex) {
	// Syntax error in the regular expression
        }

        if (PersonnelName_Fname.trim().isEmpty() || PersonnelName_Fname == null) {
            PersonnelName_Fname = emailperson_section_org;

        } else {
            PersonnelName_Fname = PersonnelName_Fname.replaceAll("(?sim)\\d+", "");
        }
        if (PersonnelName_Lname.trim().isEmpty() || PersonnelName_Lname == null) {
            PersonnelName_Lname = "";
        } else {
            PersonnelName_Lname = PersonnelName_Lname.replaceAll("(?sim)\\d+", "");
        }

        if (PersonnelName_Fname.trim().length() >= 3) {
           
           
           
           PersonName=MethodRepository.EmailPersonNameMatch_PageSource(page_finalcontent,PersonnelName_Fname);
       }else if (PersonnelName_Lname.trim().length() >= 3) {
        if(PersonName.trim().isEmpty()){
            PersonName=MethodRepository.EmailPersonNameMatch_PageSource(page_finalcontent,PersonnelName_Lname);
        }
       }
       }
       String Prefix="";
       String FName="";
       String MName="";
       String LName="";
       String Suffix="";
        System.out.println("Person Name -- "+PersonName);
        System.out.println("Email --  "+email_text);
        String SplitNames=MethodRepository.SplitName(PersonName);
         String[] split = SplitNames.split("::-::");
                try {
                    Prefix = split[0];
                } catch (Exception e) {
                    Prefix = "";
                }
                try {
                    FName = split[1].trim();
                    if (FName.endsWith(",")) {
                        FName = FName.substring(0, FName.length() - 1).trim();
                    }
                } catch (Exception e) {
                    FName = "";
                }
                try {
                    MName = split[2].trim();
                    if (MName.endsWith(",")) {
                        MName = MName.substring(0, MName.length() - 1).trim();
                    }
                } catch (Exception e) {
                    MName = "";
                }
                try {
                    LName = split[3].trim();
                    if (LName.endsWith(",")) {
                        LName = LName.substring(0, LName.length() - 1).trim();
                    }
                } catch (Exception e) {
                    LName = "";
                }
                try {
                    Suffix = split[4].trim();
                } catch (Exception e) {
                    Suffix = "";
                }
        System.out.println("Prefix "+Prefix);    
        System.out.println("Suffix "+Suffix);    
        System.out.println("FName "+FName);    
        System.out.println("MName "+MName);    
        System.out.println("LName "+LName);    
        String emaildomain=email_text.replaceAll("(?sim)^.*?@","").trim();
        System.out.println("    ------------------------------");
       String ProcessStatus="Success";
       String StatusDescription="PersonName & Email Extracted";
        
        ContactExtraction_Phase1.FinalArrayList.add(ContactExtraction_Phase1.id+"::~::"+ContactExtraction_Phase1.URLID+"::~::"+ContactExtraction_Phase1.DURL+"::~::"+ContactExtraction_Phase1.Normalized_DURL+"::~::"+ContactExtraction_Phase1.URL_Source+"::~::"+Prefix+"::~::"+PersonName+"::~::"+Suffix+"::~::"+FName+"::~::"+MName+"::~::"+LName+"::~::"+email_text+"::~::"+emaildomain+"::~::"+ProcessStatus+"::~::"+StatusDescription);
        
        
        
        
        
        
     }

    private static void PersonNameExtraction(String PageSource) throws IOException, InterruptedException {
        
        NER.NERNameIdentification1(PageSource);
        if(ContactExtraction_Phase1.PersonNames.size()>0){
            Iterator name_it=ContactExtraction_Phase1.PersonNames.iterator();
            while(name_it.hasNext()){
                String PersonName=name_it.next().toString().trim();
                String Prefix="";
       String FName="";
       String MName="";
       String LName="";
       String Suffix="";
       String email_text="";
        System.out.println("Person Name -- "+PersonName);
//        System.out.println("Email --  "+email_text);
        String SplitNames=MethodRepository.SplitName(PersonName);
         String[] split = SplitNames.split("::-::");
                try {
                    Prefix = split[0];
                } catch (Exception e) {
                    Prefix = "";
                }
                try {
                    FName = split[1].trim();
                    if (FName.endsWith(",")) {
                        FName = FName.substring(0, FName.length() - 1).trim();
                    }
                } catch (Exception e) {
                    FName = "";
                }
                try {
                    MName = split[2].trim();
                    if (MName.endsWith(",")) {
                        MName = MName.substring(0, MName.length() - 1).trim();
                    }
                } catch (Exception e) {
                    MName = "";
                }
                try {
                    LName = split[3].trim();
                    if (LName.endsWith(",")) {
                        LName = LName.substring(0, LName.length() - 1).trim();
                    }
                } catch (Exception e) {
                    LName = "";
                }
                try {
                    Suffix = split[4].trim();
                } catch (Exception e) {
                    Suffix = "";
                }
        System.out.println("Prefix "+Prefix);    
        System.out.println("Suffix "+Suffix);    
        System.out.println("FName "+FName);    
        System.out.println("MName "+MName);    
        System.out.println("LName "+LName);    
//        String emaildomain=email_text.replaceAll("(?sim)^.*?@","").trim();
        String emaildomain="".trim();
        System.out.println("    ------------------------------");
       String ProcessStatus="Success";
       String StatusDescription="PersonName Alone Extracted";
        
        ContactExtraction_Phase1.FinalArrayList.add(ContactExtraction_Phase1.id+"::~::"+ContactExtraction_Phase1.URLID+"::~::"+ContactExtraction_Phase1.DURL+"::~::"+ContactExtraction_Phase1.Normalized_DURL+"::~::"+ContactExtraction_Phase1.URL_Source+"::~::"+Prefix+"::~::"+PersonName+"::~::"+Suffix+"::~::"+FName+"::~::"+MName+"::~::"+LName+"::~::"+email_text+"::~::"+emaildomain+"::~::"+ProcessStatus+"::~::"+StatusDescription);
        
       
                
            }
        }else{
            String FName="";
       String MName="";
       String LName="";
       String Suffix="";
       String Prefix="";
       String email_text="";
       String PersonName="";
//       String PersonName="";
       String emaildomain="";
       String ProcessStatus="Failure";
       String StatusDescription="PersonName&Email NotFound";
        ContactExtraction_Phase1.FinalArrayList.add(ContactExtraction_Phase1.id+"::~::"+ContactExtraction_Phase1.URLID+"::~::"+ContactExtraction_Phase1.DURL+"::~::"+ContactExtraction_Phase1.Normalized_DURL+"::~::"+ContactExtraction_Phase1.URL_Source+"::~::"+Prefix+"::~::"+PersonName+"::~::"+Suffix+"::~::"+FName+"::~::"+MName+"::~::"+LName+"::~::"+email_text+"::~::"+emaildomain+"::~::"+ProcessStatus+"::~::"+StatusDescription);    
        }
        
        
    }

    
    
    
    
    
    
    
    
    
}
