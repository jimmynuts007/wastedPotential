package wastedPotentials;

import java.io.File;
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

    private Connection connection;
    private ArrayList<ArrayList<String>> arrTestNGSuiteLevelParameters;
    private ArrayList<String> arrTestNGListeners;
    private String projectName;
    private String projectDescription;
    private String projectSkeleton;
    private String projectLocation;
    private String testNg_Needed;
    private String maven_Needed;
    private String maven_project_ModelVersion,maven_project_GroupId,maven_project_ArtifactId,maven_project_Version;
    private ArrayList<ArrayList<String>> arrDependenciesMaven;
    private ArrayList<ArrayList<String>>  arrProjectLevelDetails;
    private ArrayList<ArrayList<String>> arrProjectSkeletons;
    private ArrayList<ArrayList<String>> arrQueries;
    private ArrayList<ArrayList<String>> arrtest_paramName_paramValue_csv;
    private ArrayList<String> arrQuery;
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
         
         arrQuery.add(queriesRS.getField("queryName"));
         arrQuery.add(queriesRS.getField("query"));
         arrQueries.add(arrQuery);
         
       // projectName	projectDescription	projectSkeleton	projectLocation
        //testNg_Needed	maven_Needed	maven_project_ModelVersion	
        //maven_project_GroupId	maven_project_ArtifactId	maven_project_Version
        //testNg_listenerClasses_csv	testNG_suite_level_paramName:paramValue_csv
       
         //psdquery  tcquery   dquery    psquery

         for(int i=0;i<arrQueries.size();i++) {
        	if(arrQueries.get(0).get(i)=="pldquery") {
        		pldquery=arrQueries.get(0).get(1);
            	
            }
        	else if(arrQueries.get(0).get(i)=="tcquery") {
        		tcquery=arrQueries.get(0).get(1);
            	
            }
        	else if(arrQueries.get(0).get(i)=="dquery") {
        	dquery=arrQueries.get(0).get(1);
}
        	else if(arrQueries.get(0).get(i)=="psquery") {
	psquery=arrQueries.get(0).get(1);
}
        	else {
        		throw new Exception("query name is invalid");
        	}

        	
        }
         Recordset pldRS=connection.executeQuery(pldquery);
        maven_Needed=pldRS.getField("maven_Needed");
        if(!maven_Needed.isEmpty() && maven_Needed.equals("Y")) {
        	maven_project_ModelVersion=pldRS.getField("maven_project_ModelVersion");
        	maven_project_GroupId=pldRS.getField(maven_project_GroupId);
        	maven_project_ArtifactId=pldRS.getField("maven_project_ArtifactId");
        	maven_project_Version=pldRS.getField("maven_project_Version");
        	Recordset dRS=connection.executeQuery(dquery);
        	while(dRS.next()) {
        	ArrayList<String> singledependency= new ArrayList<String>();
        	singledependency.add(dRS.getField("dependencyArtifactId"));
        	singledependency.add(dRS.getField("dependencyGroupId"));
        	singledependency.add(dRS.getField("dependencyVersion"));
            arrDependenciesMaven.add(singledependency);
        	}
        }
        testNg_Needed=pldRS.getField("testNg_Needed");
        if(!testNg_Needed.isEmpty() && testNg_Needed.equals("Y")) {
        	String testNg_listenerClasses_csv=pldRS.getField("testNg_listenerClasses_csv");
        	String testNG_suite_level_paramName_paramValue_csv=pldRS.getField("testNG_suite_level_paramName_paramValue_csv");
        	String[] listenerstring= testNg_listenerClasses_csv.split(",");
        	String[] suitelevelparams=testNG_suite_level_paramName_paramValue_csv.split(",");
        	
        	for(String s:listenerstring) {
        		arrTestNGListeners.add(s);
        	}
    		ArrayList<String> x= new ArrayList<>();

        	for(String s1: suitelevelparams) {
        		String[] s2=s1.split(":");
        		for(String s3:s2) {
        			x.add(s3);
        		}
        		arrTestNGSuiteLevelParameters.add(x);
        		x=new ArrayList<>();
        		
        	}
        }
        this.projectName=pldRS.getField("projectName");
        this.projectDescription=pldRS.getField("projectDescription");
        this.projectSkeleton=pldRS.getField("projectSkeleton");
        this.projectLocation=pldRS.getField("projectLocation");

        
    } catch (FilloException e) {
        e.printStackTrace();
    } finally {
        connection.close();
    }
    }


    public void createSuite(Recordset recordset) {
        XmlMapper xmlMapper = new XmlMapper();
        testNgSuite suite = new testNgSuite("TesTautomationGuru");
        try {
            while (recordset.next()) {

                String testName = recordset.getField("TestCaseDescription");
                String className = recordset.getField("ClassName");
                String param = "Data";
                String paramValue = recordset.getField("Data");

                suite.addTest(testName, param, paramValue, className);
            }
            xmlMapper.writeValue(new File("c:/testng-suite.xml"), suite);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            recordset.close();
        }
    }

	public void execute() {
		// TODO Auto-generated method stub
		
	}


}
