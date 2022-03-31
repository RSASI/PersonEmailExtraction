/*
 * Decompiled with CFR 0_118.
 */
package contactextraction_phase1;

;
import static contactextraction_phase1.ContactExtraction_Phase1.configProperties;
import static contactextraction_phase1.ContactExtraction_Phase1.log;
import java.io.File;
import java.io.FileReader;
import java.io.Reader;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Properties;

public class DBConnectivity {

    public static Connection con = null;
    public static String IPAddress = "";
    public static String DBName = "";
    public static String UserName = "";
    public static String Password = "";
    static String Input_Table = "";
    public static String New_OutputTable = "";
    public static int Start;
    public static int End;
    public static int Timeout;
    public static int Thread_Pool;

    public static void UpdateInputTable(String HTMLInputid, int statusflag) {
        try {

            CallableStatement sd = null;
            sd = DBConnectivity.con.prepareCall(" update " + configProperties.getInput_Table() + " with(updlock) set Status = ?  where id = ? ");
            sd.setInt(1, statusflag);
            sd.setInt(2, Integer.parseInt(HTMLInputid));
//            sd.setString(3, ErrorDesc);
//            sd.setString(4, Procesname);
//            sd.setString(5, currentdate);
//            sd.setString(6, StTime);
            sd.executeUpdate();
            System.out.println("Update count:" + sd.getUpdateCount());
            sd.clearBatch();
            sd.clearWarnings();
            sd.close();
        } catch (Exception ell) {
            ell.printStackTrace();
        }

        System.out.println("Input Table Updated for ID :" + HTMLInputid);

    }

    
    static void InsertOutputTable(String id, String UniqueID, String DomainURL, String DomainName, String Url, String Prefix,String ContactName,String Suffix, String ContactFirstName, String ContactMiddleName, String ContactLastName, String Email, String DomainFound, String ProcessStatus,String StatusDescription,String startTime, String EndTime,String Language) {
    PreparedStatement pstmt = null;
        try {
            String qry = "INSERT INTO " + configProperties.getDBName() + ".dbo." + configProperties.getOutput_Table() + "  ([InputID],[UniqueID],[DomainURL],[DomainName],[Url],"
                    + "[Prefix],[ContactName],[ContactFirstName],[ContactMiddleName],[ContactLastName],"
                    + "[Suffix],[Email],[DomainFound],[ProcessStatus],[StatusDescription],[StartTime],[EndTime],[Language]) VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
                    pstmt = DBConnectivity.con.prepareStatement(qry);        
                    pstmt.setString(1, id);
                    pstmt.setString(2, UniqueID);
                    pstmt.setString(3, DomainURL);
                    pstmt.setString(4, DomainName);
                    pstmt.setString(5, Url);
                    pstmt.setString(6, Prefix);
                    pstmt.setString(7, ContactName);
                    pstmt.setString(8, ContactFirstName);
                    pstmt.setString(9, ContactMiddleName);
                    pstmt.setString(10, ContactLastName);
                    pstmt.setString(11, Suffix);
                    pstmt.setString(12, Email);
                    pstmt.setString(13, DomainFound);
                    pstmt.setString(14, ProcessStatus);
                    pstmt.setString(15, StatusDescription);
                    pstmt.setString(16, startTime);
                    pstmt.setString(17, EndTime);
                    pstmt.setString(18, Language);
                    pstmt.executeUpdate();
                    pstmt.clearBatch();
                    pstmt.clearWarnings();
                    pstmt.close();
                
            
        } catch (SQLException r) {
            r.printStackTrace();
        }
    
    
    }
    Properties props = null;
    static String Offline;
    public static int deadlockpriority;
    public static int lockout1;
    public static int lockout2;
    public static String MailUsername;
    public static String MailTo;
    public static String MailCC;
    public static String MailBCC;
    public static String Mail_Subject;
    public static String Mail_Desc;
    public static String CTPInsertion;
    public static String Highbeam_insertion;
    public static String URL_Exclusion_Regex;
    public static String InstanceNO;
    public static String processname;
    static String Stage;
    public static String TrackerTable;
    public static String shortdescription;
    public static String Processtype;

    public static String UserAgent = "";
    public static String MetaURLRegex = "";
    public static String CoreContentExtraction = "";

    public static int LoopCount = 0;
    public static int ConnectionTimeOut;
    public static int recordinputstatus;

    public static void DBConnection() throws SQLException, ClassNotFoundException, Exception {
        Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
        con = DriverManager.getConnection("jdbc:sqlserver://" + configProperties.getIPAddress() + ";" + "user=" + configProperties.getUserName() + ";" + "Password=" + configProperties.getPassword() + ";" + "DatabaseName=" + configProperties.getDBName());
    }

    public static void CreateOutputtable() {
        PreparedStatement ps = null;
        String query = "";
        query = "if not exists (select * from  " + configProperties.getDBName() + ".INFORMATION_SCHEMA.TABLES where TABLE_NAME ='" + configProperties.getOutput_Table() + "' )\n" + "    CREATE TABLE [dbo].[" + configProperties.getOutput_Table() + "](\n"
                + "	[id] [int] IDENTITY(1,1) NOT NULL,\n" +
"	[InputID] [nchar](10) NULL,\n" +
"	[UniqueID] [nvarchar](50) NULL,\n" +
"	[DomainURL] [nvarchar](max) NULL,\n" +
"	[DomainName] [nvarchar](max) NULL,\n" +
"	[Url] [nvarchar](max) NULL,\n" +
"	[Prefix] [nvarchar](50) NULL,\n" +
"	[ContactName] [nvarchar](max) NULL,\n" +
"	[ContactFirstName] [nvarchar](max) NULL,\n" +
"	[ContactMiddleName] [nvarchar](max) NULL,\n" +
"	[ContactLastName] [nvarchar](max) NULL,\n" +
"	[Suffix] [nvarchar](50) NULL,\n" +
"	[Email] [nvarchar](max) NULL,\n" +
"	[DomainFound] [nvarchar](max) NULL,\n" +
"	[ProcessStatus] [nvarchar](max) NULL,\n" +
"	[StatusDescription] [nvarchar](max) NULL,\n" +
"	[StartTime] [nvarchar](max) NULL,\n" +
"	[EndTime] [nvarchar](max) NULL,\n" +
"	[Language] [nchar](10) NULL\n"
                + ") ON [PRIMARY] TEXTIMAGE_ON [PRIMARY]";
        try {
//            DBConnectivity.DBConnection();
            ps = DBConnectivity.con.prepareStatement(query);
            ps.executeUpdate();
            System.out.println(configProperties.getOutput_Table() + " Output Table Created ");
        } catch (Exception s) {
            log.info("DB Connection Failure");
        }
    }
}
