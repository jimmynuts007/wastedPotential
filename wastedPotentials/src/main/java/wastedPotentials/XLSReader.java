package wastedPotentials;

import java.io.File;
import java.util.ArrayList;

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
    private ArrayList<String> arrQuery;
    public XLSReader(String filePath){
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
        	if(arrQueries.get(0).get(i)=="psdquery") {
            	
            }
        	else if(arrQueries.get(0).get(i)=="tcquery") {
            	
            }
        	else if(arrQueries.get(0).get(i)=="dquery") {
        	
}
        	else if(arrQueries.get(0).get(i)=="psquery") {
	
}
        	else {
        		throw new Exception("query name is invalid");
        	}

        	
        }
         
        maven_Needed=recordset1.getField("maven_Needed");
        if(!maven_Needed.isEmpty() && maven_Needed.equals("Y")) {
        	
        }
        testNg_Needed=recordset1.getField("testNg_Needed");
        if(!testNg_Needed.isEmpty() && testNg_Needed.equals("Y")) {
        	
        }
        
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
