package wastedPotentials;

import java.io.File;
import java.util.ArrayList;

import com.codoid.products.exception.FilloException;
import com.codoid.products.fillo.Connection;
import com.codoid.products.fillo.Fillo;
import com.codoid.products.fillo.Recordset;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;


public class XLSReader {

    private final Fillo fillo;
    private final String filePath;

    private Connection connection;
    private ArrayList<ArrayList<String>> arrSuiteLevelParameters;
    private Arraylist
    public XLSReader(String filePath) {
        fillo = new Fillo();
        this.filePath = filePath;
    }

    public void getTests(String query) {
        try {
            connection = fillo.getConnection(this.filePath);
            Recordset recordset = connection.executeQuery(query);
            this.createSuite(recordset);
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


}
