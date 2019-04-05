package wastedPotentials;

public class projectCreator {
public static void main(String[] args) {
	

	XLSReader xlsReader= new XLSReader(System.getProperty("user.dir")+"//resources//input//inputForAutoSeleniumProjectGenerator.xlsx");
xlsReader.createSuite();
}}
