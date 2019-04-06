package wastedPotentials;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;

import javax.swing.Spring;

import com.codoid.products.exception.FilloException;
import com.codoid.products.fillo.Connection;
import com.codoid.products.fillo.Fillo;
import com.codoid.products.fillo.Recordset;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;


public class XLSReader {

    private  Fillo fillo;
    private  String filePath;
    String testNg_listenerClasses_csv;
	String testNG_suite_level_paramName_paramValue_csv;
    private Connection connection;
    private ArrayList<ArrayList<String>> arrTestNGSuiteLevelParameters=new ArrayList<ArrayList<String>>();
    private ArrayList<String> arrTestNGListeners= new ArrayList<>();
    private String projectName;
    private String projectDescription;
    private String projectSkeleton;
    private String projectLocation;
    private String testNg_Needed;
    private String maven_Needed;
    private String maven_project_ModelVersion,maven_project_GroupId,maven_project_ArtifactId,maven_project_Version;
    private ArrayList<ArrayList<String>> arrDependenciesMaven=new ArrayList<ArrayList<String>>();
    private ArrayList<ArrayList<String>>  arrProjectLevelDetails=new ArrayList<ArrayList<String>>();
    private ArrayList<ArrayList<String>> arrProjectSkeletons=new ArrayList<ArrayList<String>>();
    private ArrayList<ArrayList<String>> arrQueries=new ArrayList<ArrayList<String>>();
    private ArrayList<ArrayList<String>> arrtest_paramName_paramValue_csv=new ArrayList<ArrayList<String>>();
    private ArrayList<String> arrQuery=new ArrayList<>();
    private String pldquery;
    private String tcquery;      
    private String dquery;
    private String psquery;
    public XLSReader(String filePath) throws Exception{
       try {
    	this.fillo = new Fillo();
        this.filePath = filePath;
        connection = fillo.getConnection(this.filePath);
        Recordset queriesRS = connection.executeQuery("select * from queries");
        //queryName	query
         arrQueries= new ArrayList<ArrayList<String>>();
         while(queriesRS.next()) {
         arrQuery.add(queriesRS.getField("queryName"));
         arrQuery.add(queriesRS.getField("query"));
         arrQueries.add(arrQuery);
         arrQuery= new ArrayList<String>();
         }
         
       // projectName	projectDescription	projectSkeleton	projectLocation
        //testNg_Needed	maven_Needed	maven_project_ModelVersion	
        //maven_project_GroupId	maven_project_ArtifactId	maven_project_Version
        //testNg_listenerClasses_csv	testNG_suite_level_paramName:paramValue_csv
       
         //psdquery  tcquery   dquery    psquery
//pldquery
     

         for(int i=0;i<arrQueries.size();i++) {
        	if(arrQueries.get(i).get(0).equalsIgnoreCase("pldquery")) {
        		pldquery=arrQueries.get(i).get(1);
            	
            }
        	else if(arrQueries.get(i).get(0).equalsIgnoreCase("tcquery")) {
        		tcquery=arrQueries.get(i).get(1);
            	
            }
        	else if(arrQueries.get(i).get(0).equalsIgnoreCase("dquery")) {
        	dquery=arrQueries.get(i).get(1);
}
        	else if(arrQueries.get(i).get(0).equalsIgnoreCase("psquery")) {
	psquery=arrQueries.get(i).get(1);
}
        	else {
        		throw new Exception("query name is invalid");
        	}

        	
        }
         Recordset pldRS=connection.executeQuery(pldquery);
        
		while(pldRS.next()) {
         this.projectName=pldRS.getField("projectName");
         this.projectDescription=pldRS.getField("projectDescription");
         this.projectSkeleton=pldRS.getField("projectSkeleton");
         this.projectLocation=pldRS.getField("projectLocation");
         maven_project_ModelVersion=pldRS.getField("maven_project_ModelVersion");
     	maven_project_GroupId=pldRS.getField("maven_project_GroupId");
     	maven_project_ArtifactId=pldRS.getField("maven_project_ArtifactId");
     	maven_project_Version=pldRS.getField("maven_project_Version");
     	 testNg_listenerClasses_csv=pldRS.getField("testNg_listenerClasses_csv");
    	 testNG_suite_level_paramName_paramValue_csv=pldRS.getField("testNG_suite_level_paramName_paramValue_csv");
   
         }
         Recordset psRS=connection.executeQuery(psquery);
         while(psRS.next()) {
         	if(projectSkeleton==psRS.getField("project_skeleton")) {
         		createPaths(projectLocation, psRS.getField("projectStructure"));
         	}
         }
         
         maven_Needed=pldRS.getField("maven_Needed");
        if(!maven_Needed.isEmpty() && maven_Needed.equals("Y")) {
        	
        	Recordset dRS=connection.executeQuery(dquery);
        	while(dRS.next()) {
        	ArrayList<String> singledependency= new ArrayList<String>();
        	singledependency.add(dRS.getField("dependencyArtifactId"));
        	singledependency.add(dRS.getField("dependencyGroupId"));
        	singledependency.add(dRS.getField("dependencyVersion"));
            arrDependenciesMaven.add(singledependency);
        	singledependency= new ArrayList<String>();

        	}
        }
        testNg_Needed=pldRS.getField("testNg_Needed");
        if(!testNg_Needed.isEmpty() && testNg_Needed.equals("Y")) {
        	 	String[] listenerstring= testNg_listenerClasses_csv.split(",");
        	String[] suitelevelparams=testNG_suite_level_paramName_paramValue_csv.split(",");
        	
        	for(String s:listenerstring) {
        		arrTestNGListeners.add(s);
        	}
    		ArrayList<String> x= new ArrayList<String>();

        	for(String s1: suitelevelparams) {
        		String[] s2=s1.split(":");
        		for(String s3:s2) {
        			x.add(s3);
        		}
        		arrTestNGSuiteLevelParameters.add(x);
        		x=new ArrayList<String>();
        		
        	}
        	Recordset tcRS= connection.executeQuery(tcquery);
        	createSuite(this.projectName,tcRS, this.projectLocation+"//src//" );
        }
        
      //  testCaseName	testClasses_csv	numberOfInstances	test_paramName_paramValue_csv

    } catch (Exception e) {
        e.printStackTrace();
    } finally {
        connection.close();
    }
    }


    public void createSuite(String suitename,Recordset recordset,String basepath) {
    	System.out.println(suitename+basepath);
        XmlMapper xmlMapper = new XmlMapper();
        testNgSuite suite = new testNgSuite(suitename);
        try {
            while (recordset.next()) {

                String testName = recordset.getField("testCaseName");
                String className = recordset.getField("testClasses_csv");
                String param = "Data";
               // String paramValue = recordset.getField("Data");
                String paramValue="value";
                suite.addTest(testName, param, paramValue, className);
            }
            xmlMapper.writeValue(new File(basepath+"//testng-suite.xml"), suite);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            recordset.close();
        }
    }

	public void execute() {
		// TODO Auto-generated method stub
		
	}
public void createPaths(String basepath,String codePath) {
	System.out.println(basepath+codePath);
	// a = "[src[main[java,resources],test[java,resources]],Target[Classes,test-classes]]" ;
	// Path path = Paths.get("C:\\Images\\Background\\Backgroundkeandar\\..\\..\\Foreground\\Necklace\\..\\Earrings\\..\\Etc");
	 //Path path1 = Paths.get("D:\\images\\src\\main\\java\\..\\resurce\\..\\..\\test\\java\\..\\resource\\..\\..\\..\\Target\\Classes\\..\\Test-Classes");
	 StringBuilder s = new StringBuilder(codePath.length());
	 s.append("D:\\Images\\");
	 for(int i=1;i<codePath.length()-1;i++) {
	 if(codePath.charAt(i)=='[') {
	  s.append("\\");
	 }
	 else if(codePath.charAt(i)==']') {
	  s.append("\\..\\");
	  
	 }
	 else if(codePath.charAt(i)==',') {
	 s.append("\\..\\");
	}
	 else {
	  s.append(codePath.charAt(i));
	 }
	 }
	 
	 try {
		    Files.createDirectories(Paths.get(s.toString()));
	  System.out.println(s);
	 //   Files.createDirectories(Paths.get("D:\\Images\\src\\main\\java\\..\\resources\\..\\..\\test\\java\\..\\resources\\..\\..\\..\\target\\classes\\..\\test-classes\\..\\"));	 
	  } catch (IOException e) {
	 
	 
	  System.out.println("Cannot create directories - " + e.getMessage());
	 }
	 
}
}


